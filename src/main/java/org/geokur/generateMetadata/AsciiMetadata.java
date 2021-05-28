/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

import org.geokur.ISO19103Schema.Record;
import org.geokur.ISO19108Schema.TM_Instant;
import org.geokur.ISO19108Schema.TM_Period;
import org.geokur.ISO19115Schema.*;
import org.geokur.ISO19157Schema.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AsciiMetadata implements Metadata {

    Properties properties;
    String fileName;
    DS_DataSet dsDataSet;
    String fileNameGeodata;
    List<String> geoTableName;
    List<String> geoColNameJoin;
    List<String> asciiColNameJoin;
    List<String> asciiColNameDefine;
    List<String> asciiColNameIgnore;
    List<String> descriptionAsciiColNameDefine;
    boolean colJoinNumerical;
    List<Boolean> colJoinSequential;
    List<String> colNameJoinNumerical;
    boolean postgresUse;
    String postgresHostname;
    String postgresDatabase;
    String postgresUser;
    String postgresPasswd;
    String tableName;
    String tableNameMasked;
    boolean thematicMapping;
    String thematicMappingFile;
    String thematicMappingColFrom;
    String thematicMappingColTo;
    boolean availabilityTemporal = true;
    boolean availabilityThematic = true;
    boolean availabilityAdditional = true;

    Connection connectionAscii = null;
    Statement statementAscii = null;
    Map<String, String> thematicMappingDictionary;

    public AsciiMetadata(Properties properties, DS_DataSet dsDataSet) {
        this.properties = properties;
        this.fileName = properties.geodata;
        this.dsDataSet = dsDataSet;
        this.fileNameGeodata = properties.geodataReference;
        this.geoTableName = properties.geoTableNames;
        this.geoColNameJoin = properties.geoColNamesJoin;
        this.asciiColNameJoin = properties.asciiColNamesJoin;
        this.asciiColNameDefine = properties.asciiColNamesDefine;
        this.asciiColNameIgnore = properties.asciiColNamesIgnore;
        this.descriptionAsciiColNameDefine = properties.descriptionAsciiColNamesDefine;
        this.colJoinNumerical = properties.colJoinNumerical;
        this.colJoinSequential = properties.colJoinSequential;
        this.colNameJoinNumerical = new ArrayList<>();
        this.postgresUse = properties.postgresUse;
        if (this.postgresUse) {
            this.postgresHostname = properties.postgresHostname;
            this.postgresDatabase = properties.postgresDatabase;
            this.postgresUser = properties.postgresUser;
            this.postgresPasswd = properties.postgresPasswd;
            this.tableName = properties.postgresTable;
            this.tableNameMasked = tableName + "_masked";
            pushToPostgresql();
        }
        this.thematicMapping = properties.thematicMapping;
        if (this.thematicMapping) {
            this.thematicMappingFile = properties.thematicMappingFile;
            this.thematicMappingColFrom = properties.thematicMappingColFrom;
            this.thematicMappingColTo = properties.thematicMappingColTo;
            this.thematicMappingDictionary = getMappingDictionary();
        }
    }


    public void pushToPostgresql() {
        // put all data from csv file to postgresql database

        connectionAscii = null;
        statementAscii = null;

        String url = "jdbc:postgresql://" + postgresHostname + "/" + postgresDatabase;
        String delimiter = null;

        try {
            // connect to database
            connectionAscii = DriverManager.getConnection(url, postgresUser, postgresPasswd);
            statementAscii = connectionAscii.createStatement();

            // open stream to csv file
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String headerline = reader.readLine();
            Pattern pattern = Pattern.compile(properties.asciiColNamesJoin.get(0));
            Matcher matcher = pattern.matcher(headerline);
            int idxStart;
            int idxEnd;
            if (matcher.find()) {
                idxStart = matcher.start();
                idxEnd = matcher.end();
            } else {
                throw new Exception("Delimiter not found");
            }
            if (idxStart > 0) {
                // first asciiVColNamesJoin entry not the first entry in headerline
                delimiter = String.valueOf(headerline.charAt(idxStart - 1));
            } else {
                delimiter = String.valueOf(headerline.charAt(idxEnd));
            }
            String[] header = headerline.replace(" ", "_").split(delimiter);

            // create table in database with necessary columns to mirror csv, datatype currently bigint
            statementAscii.executeUpdate("DROP TABLE IF EXISTS " + tableName);
            StringBuilder command = new StringBuilder();
            command.append("CREATE TABLE ").append(tableName).append(" (");
            for (String headerAct : header) {
//                command.append("\"").append(headerAct).append("\"").append(" TEXT");
                command.append(headerAct).append(" TEXT");
                if (!headerAct.equals(header[header.length - 1])) {
                    command.append(",");
                } else {
                    command.append(")");
                }
            }
            statementAscii.executeUpdate(command.toString());
            reader.close();

            // copy csv content to sql database using system command psql (much faster)
//            ResultSet tmpRS;
//            tmpRS = statementAscii.executeQuery("SELECT COUNT(*) FROM " + tableName);
//            tmpRS.next();
//            while(tmpRS.getInt(1) == 0) {
            String commandCopy = "\\COPY " + tableName + " FROM '" + fileName + "' DELIMITER '" + delimiter + "' CSV HEADER";
            ProcessBuilder pb = new ProcessBuilder().command("psql", "-h", postgresHostname, "-d", postgresDatabase, "-c", commandCopy);
            pb.inheritIO().start();
            Thread.sleep(500); // seems to be necessary for safer copying
//                tmpRS = statementAscii.executeQuery("SELECT COUNT(*) FROM " + tableName);
//                tmpRS.next();
//            }

            // add joined criteria column
            statementAscii.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN joincritascii TEXT");
            command = new StringBuilder();
            command.append("UPDATE ").append(tableName).append(" SET joincritascii=CONCAT(");
            for (int i = 0; i < asciiColNameJoin.size() - 1; i++) {
                command.append(asciiColNameJoin.get(i)).append(", ");
            }
            command.append(asciiColNameJoin.get(asciiColNameJoin.size() - 1)).append(")");
            statementAscii.executeUpdate(command.toString());

        } catch (SQLException | IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getMappingDictionary() {
        // get mapping dictionary from csv file

        Map<String, String> dictionary = new HashMap<>();

        String delimiter = ",";
        try (BufferedReader reader = new BufferedReader(new FileReader(thematicMappingFile))) {
            // read header line
            List<String> header = Arrays.asList(reader.readLine().toLowerCase().split(delimiter));
            int idxColFrom = header.indexOf(thematicMappingColFrom.toLowerCase());
            int idxColTo = header.indexOf(thematicMappingColTo.toLowerCase());

            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineParted = line.split(delimiter, -1);
                dictionary.put(lineParted[idxColFrom], lineParted[idxColTo]);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return dictionary;
    }

    public DS_Resource getMetadata() {
        // read ascii file in combination with some geodata and put its metadata into DS_Resource

        //TODO: allow shape file as geodata source

        try {
            // open geopackage
            Geopackage gpkg = new Geopackage(fileNameGeodata);
            Connection connection = gpkg.getConnection();
            Statement statement = gpkg.getStatement(connection);

            for (String geoTableNameAct : geoTableName) {
                String displayTableName = "geoTableName: " + geoTableNameAct;
                String displayLine = "-".repeat(displayTableName.length());
                System.out.println(displayLine);
                System.out.println(displayTableName);
                System.out.println(displayLine);

                List<String> colNamesAssessment = new ArrayList<>();
                int[] numDataAssessment = null;
                int csvNumAssessment = -1;
                int numDataAll = -1;
                double polygonPerArea = -1;
                int[] excessDataAll = null;
                List<List<String>> yearsAssessmentCols = new ArrayList<>();
                List<List<String>> commoditiesAssessmentCols = new ArrayList<>();
                List<List<String>> commoditiesAssessmentColsAll = new ArrayList<>();
                List<List<List<String>>> additionalAssessmentCols = new ArrayList<>();
                List<Integer> numGeoAssessmentCols = new ArrayList<>();
                List<Integer> numYearsAssessmentCols = new ArrayList<>();
                List<Integer> numCommoditiesAssessmentCols = new ArrayList<>();
                List<List<Integer>> numAdditionalAssessmentCols = new ArrayList<>();
                List<List<EmpiricalDistributionProperty>> temporalPerGeo = null;
                List<List<EmpiricalDistributionProperty>> thematicPerGeo = null;
                List<List<EmpiricalDistributionProperty>> thematicPerTemp = null;


                // read geopackage content
                List<List<String>> geoColContent = new ArrayList<>();
                for (String geoColNameJoinAct : geoColNameJoin) {
                    geoColContent.add(gpkg.getTableColContent(statement, geoTableNameAct, geoColNameJoinAct));
                }

                // get current list of combined join criteria (concatenate all columns given in geoColNameJoin)
                // double entries are removed
                List<String> joinCritGeo = new ArrayList<>();
                for (int geoCt1 = 0; geoCt1 < geoColContent.get(0).size(); geoCt1++) {
                    // loop over lines in geodata
                    StringBuilder joinCritGeoAct = new StringBuilder();
                    for (List<String> strings : geoColContent) {
                        // loop over join criteria in geodata and concatenate to
                        joinCritGeoAct.append(strings.get(geoCt1));
                    }
                    joinCritGeo.add(joinCritGeoAct.toString());
                }
                joinCritGeo = getStringListUniqueMembers(joinCritGeo);

                // get index of temporal and thematic columns
//                int idxTemporal = descriptionAsciiColNameDefine.indexOf("temporal");
//                int idxThematic = descriptionAsciiColNameDefine.indexOf("thematic");
                List<Integer> idxTemporalList = indexOfAll(descriptionAsciiColNameDefine, "temporal");
                List<Integer> idxThematicList = indexOfAll(descriptionAsciiColNameDefine, "thematic");
                List<Integer> idxAdditional = indexOfAll(descriptionAsciiColNameDefine, "additional");

//                if (idxTemporal == -1) {
                if (idxTemporalList.size() == 0) {
                    // no temporal column available
                    availabilityTemporal = false;
                }
//                if (idxThematic == -1) {
                if (idxThematicList.size() == 0) {
                    // no thematic column available
                    availabilityThematic = false;
                }
                if (idxAdditional.size() == 0) {
                    // no additional defining columns available
                    availabilityAdditional = false;
                }

                // multiple columns for temporal and thematic dimensions only possible with Postgres database
                int idxTemporal = -1;
                if (idxTemporalList.size() == 1) {
                    idxTemporal = idxTemporalList.get(0);
                } else if (idxTemporalList.size() > 1 && postgresUse) {
                    StringBuilder asciiColNameDefineTmp = new StringBuilder();
                    for (int idx : idxTemporalList) {
                        asciiColNameDefineTmp.append(asciiColNameDefine.get(idx)).append("##");
                    }
                    asciiColNameDefine.add(asciiColNameDefineTmp.toString());
                    idxTemporal = asciiColNameDefine.size() - 1;
                } else if (idxTemporalList.size() > 1 && !postgresUse) {
                    System.out.println("Multiple temporal columns only possible with Postgres database");
                    System.exit(8);
                }

                int idxThematic = -1;
                if (idxThematicList.size() == 1) {
                    idxThematic = idxThematicList.get(0);
                } else if (idxThematicList.size() > 1 && postgresUse) {
                    StringBuilder asciiColNameDefineTmp = new StringBuilder();
                    for (int idx : idxThematicList) {
                        asciiColNameDefineTmp.append(asciiColNameDefine.get(idx)).append("_");
                    }
                    asciiColNameDefine.add(asciiColNameDefineTmp.toString());
                    idxThematic = asciiColNameDefine.size() - 1;
                } else if (idxThematicList.size() > 1 && !postgresUse) {
                    System.out.println("Multiple thematic columns only possible with Postgres database");
                    System.exit(8);
                }


                // read all relevant ascii content (concatenate all column names for the sake of performance)
                if (postgresUse) {
                    // if requested use postgres database -> recommended for larger csv files
                    try {
                        StringBuilder command;

                        command = new StringBuilder();
                        command.append("SELECT * FROM ").append(tableName).append(" LIMIT 1");
                        ResultSet resultSet = statementAscii.executeQuery(command.toString());
                        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                        int numColumns = resultSetMetaData.getColumnCount();
                        String[] colNames = new String[numColumns];
                        for (int i = 1; i <= numColumns; i++) {
                            colNames[i - 1] = resultSetMetaData.getColumnName(i);
                        }

                        for (String colNameAct : colNames) {
                            if (!containsIgnoreCase(asciiColNameJoin, colNameAct) &&
                                    !containsIgnoreCase(asciiColNameDefine, colNameAct) &&
                                    !containsIgnoreCase(asciiColNameIgnore, colNameAct) &&
                                    !containsIgnoreCase(colNameJoinNumerical, colNameAct) &&
                                    !colNameAct.equals("joincritascii")) {
                                colNamesAssessment.add(colNameAct);
                            }
                        }
                        csvNumAssessment = colNamesAssessment.size();


                        // concatenate temporal columns and remove single entries in asciiColNameDefine and change idxTemporal
                        if (availabilityTemporal && idxTemporalList.size() > 1) {
                            command = new StringBuilder();
                            command.append("ALTER TABLE ").append(tableName).append( " ADD COLUMN ")
                                    .append(asciiColNameDefine.get(idxTemporal)).append(" TEXT");
                            statementAscii.executeUpdate(command.toString());

                            command = new StringBuilder();
                            command.append("UPDATE ").append(tableName).append(" SET ").append(asciiColNameDefine.get(idxTemporal)).append("=CONCAT_WS(' ', '', ");
                            for (int i = 0; i < idxTemporalList.size() - 1; i++) {
                                command.append(asciiColNameDefine.get(idxTemporalList.get(i))).append(", ");
                            }
                            command.append(asciiColNameDefine.get(idxTemporalList.get(idxTemporalList.size() - 1))).append(")");
                            statementAscii.executeUpdate(command.toString());

                            String tmpTemporal = asciiColNameDefine.get(idxTemporal);
                            String tmpThematic = null;
                            if (availabilityThematic) {
                                tmpThematic = asciiColNameDefine.get(idxThematic);
                            }
                            for (int i = idxTemporalList.size() - 1; i >= 0; i--) {
                                asciiColNameDefine.remove(i);
                            }
                            idxTemporal = asciiColNameDefine.indexOf(tmpTemporal);
                            if (availabilityThematic) {
                                idxThematic = asciiColNameDefine.indexOf(tmpThematic);
                            }
                        }


                        // concatenate thematic columns and remove single entries in asciiColNameDefine and change idxThematic
                        if (availabilityThematic && idxThematicList.size() > 1) {
                            command = new StringBuilder();
                            command.append("ALTER TABLE ").append(tableName).append( " ADD COLUMN ")
                                    .append(asciiColNameDefine.get(idxThematic)).append(" TEXT");
                            statementAscii.executeUpdate(command.toString());

                            command = new StringBuilder();
                            command.append("UPDATE ").append(tableName).append(" SET ").append(asciiColNameDefine.get(idxThematic)).append("=CONCAT_WS(' ', '', ");
                            for (int i = 0; i < idxThematicList.size() - 1; i++) {
                                command.append(asciiColNameDefine.get(idxThematicList.get(i))).append(", ");
                            }
                            command.append(asciiColNameDefine.get(idxThematicList.get(idxThematicList.size() - 1))).append(")");
                            statementAscii.executeUpdate(command.toString());

                            String tmpThematic = asciiColNameDefine.get(idxThematic);
                            String tmpTemporal = null;
                            if (availabilityTemporal) {
                                tmpTemporal = asciiColNameDefine.get(idxTemporal);
                            }
                            for (int i = idxThematicList.size() - 1; i >= 0; i--) {
                                asciiColNameDefine.remove(i);
                            }
                            idxThematic = asciiColNameDefine.indexOf(tmpThematic);
                            if (availabilityTemporal) {
                                idxTemporal = asciiColNameDefine.indexOf(tmpTemporal);
                            }
                        }


                        // add new table with masked data from tmpdata and add incremental index column
                        // table is masked to geometries that are available in geometry reference
                        statementAscii.executeUpdate("DROP TABLE IF EXISTS " + tableNameMasked);

                        boolean tryNumerical = false;
                        if (colJoinNumerical && anyTrue(colJoinSequential)) {
                            // if joining criteria is numerical and at least some criteria an be sequentially interpreted
                            // -> try faster version of masking table
                            tryNumerical = true;
                        }

                        command = new StringBuilder();
                        command.append("SELECT EXISTS(SELECT 1 FROM information_schema.columns WHERE table_name='")
                                .append(tableName).append("' AND column_name='").append(asciiColNameJoin.get(0).toLowerCase())
                                .append("_numerical')");
                        ResultSet tmpRS = statementAscii.executeQuery(command.toString());
                        tmpRS.next();
                        if (tryNumerical && !tmpRS.getBoolean(1)) {
                            // change of tmpdata table for faster version - only proceed the first time
                            for (String tmp : asciiColNameJoin) {
                                String colTmp = tmp + "_numerical";
                                colNameJoinNumerical.add(colTmp);
                                command = new StringBuilder();
                                command.append("ALTER TABLE ").append(tableName).append(" ADD COLUMN ").append(colTmp).append(" decimal");
                                statementAscii.executeUpdate(command.toString());
                                command = new StringBuilder();
                                command.append("UPDATE ").append(tableName).append(" SET ").append(colTmp).append("=").append(tmp).append("::decimal");
                                statementAscii.executeUpdate(command.toString());
                            }
                        }

                        if (tryNumerical) {
                            List<List<Long>> valMinMax = new ArrayList<>();
                            List<List<Long>> valDistinct = new ArrayList<>();
                            for (int i = 0; i < geoColContent.size(); i++) {
                                if (colJoinSequential.get(i)) {
                                    Long[] tmp = new Long[geoColContent.get(i).size()];
                                    for (int j = 0; j < geoColContent.get(i).size(); j++) {
                                        tmp[j] = Long.parseLong(geoColContent.get(i).get(j));
                                    }
                                    List<Long> arrayMinMax = new ArrayList<>();
                                    arrayMinMax.add(Collections.min(Arrays.asList(tmp)));
                                    arrayMinMax.add(Collections.max(Arrays.asList(tmp)));
                                    valMinMax.add(arrayMinMax);
                                    List<Long> arrayDistinct = new ArrayList<>();
                                    valDistinct.add(arrayDistinct);
                                } else {
                                    List<Long> arrayMinMax = new ArrayList<>();
                                    valMinMax.add(arrayMinMax);
                                    List<String> arrayDistinctString = getStringListUniqueMembers(geoColContent.get(i));
                                    List<Long> arrayDistinct = new ArrayList<>();
                                    for (String tmp : arrayDistinctString) {
                                        arrayDistinct.add(Long.parseLong(tmp));
                                    }
                                    valDistinct.add(arrayDistinct);
                                }
                            }
                            command = new StringBuilder();
                            command.append("CREATE TEMP TABLE ").append(tableNameMasked).append(" AS SELECT * FROM ").append(tableName).append(" WHERE ");
                            for (int i = 0; i < geoColContent.size(); i++) {
                                String colTmp = asciiColNameJoin.get(i) + "_numerical";
                                if (colJoinSequential.get(i)) {
                                    // get values from min to max
                                    command.append("(").append(colTmp).append(">=").append(valMinMax.get(i).get(0)).append(" AND ")
                                            .append(colTmp).append("<=").append(valMinMax.get(i).get(1)).append(")");
                                } else {
                                    // get distinct values
                                    command.append("(");
                                    for (int j = 0; j < valDistinct.get(i).size(); j++) {
                                        command.append(colTmp).append("=").append(valDistinct.get(i).get(j));
                                        if (j < valDistinct.get(i).size() - 1) {
                                            command.append(" OR ");
                                        }
                                    }
                                    command.append(")");
                                }
                                if (i < geoColContent.size() - 1) {
                                    command.append(" AND ");
                                }
                            }
                        } else {
                            command = new StringBuilder();
                            command.append("CREATE TEMP TABLE ").append(tableNameMasked).append(" AS SELECT * FROM ").append(tableName).append(" WHERE ");
                            for (int i = 0; i < joinCritGeo.size() - 1; i++) {
                                // use $$xxx$$$ instead of 'xxx' as string definition
                                // strings containing originally $$ are less likely than '
//                            command.append("joincritascii='").append(joinCritGeo.get(i)).append("' OR ");
                                command.append("joincritascii=$$").append(joinCritGeo.get(i)).append("$$ OR ");
                            }
                            command.append("joincritascii=$$").append(joinCritGeo.get(joinCritGeo.size() - 1)).append("$$");
                        }
                        statementAscii.executeUpdate(command.toString());


                        command = new StringBuilder();
                        command.append("ALTER TABLE ").append(tableNameMasked).append(" ADD COLUMN idxadded SERIAL");
                        statementAscii.executeUpdate(command.toString());

                        statementAscii.executeUpdate("ANALYZE " + tableNameMasked);

                        // get number of values for each assessment column
                        numDataAssessment = new int[csvNumAssessment];
                        for (int i = 0; i < csvNumAssessment; i++) {
                            command = new StringBuilder();
                            command.append("SELECT COUNT(").append(colNamesAssessment.get(i)).append(") FROM ").append(tableName).append("_masked");
                            ResultSet tmp = statementAscii.executeQuery(command.toString());
                            tmp.next();
                            numDataAssessment[i] = tmp.getInt(1);
                        }

                        // get number of all values (with masking)
                        command = new StringBuilder();
                        command.append("SELECT COUNT(idxadded) FROM ").append(tableNameMasked);
                        tmpRS = statementAscii.executeQuery(command.toString());
                        tmpRS.next();
                        numDataAll = tmpRS.getInt(1);
                        if (numDataAll == 0) {
                            // no data fo current geography table in csv available
                            System.out.println("No data for " + geoTableNameAct + " available");
                            continue;
                        }

                        // get polygons per area (per 1000 square kilometer)
                        // only polygons with data representations in ascii file are taken into account
                        List<String> joinCritGeoExist = new ArrayList<>(); // elements in joinCritGeo used in csv file, too
                        command = new StringBuilder();
                        command.append("SELECT DISTINCT(joincritascii) FROM ").append(tableNameMasked);
                        tmpRS = statementAscii.executeQuery(command.toString());
                        while (tmpRS.next()) {
                            joinCritGeoExist.add(tmpRS.getString(1));
                        }

                        gpkg.open(geoTableNameAct);
                        if (gpkg.geometryType.contains("polygon")) {
                            List<FeatureDescriptor> featureDescriptors = gpkg.getCenterArea();
                            List<Integer> featureDescriptorsUsed = new ArrayList<>();
                            double areaKm2UTM = 0.0;
                            int ct = -1;
                            for (FeatureDescriptor featureDescriptor : featureDescriptors) {
                                ct++;
                                String attributeValueMerged = featureDescriptor.getAttributeValuesIntendedMerged(geoColNameJoin);
                                if (joinCritGeoExist.contains(attributeValueMerged)) {
                                    featureDescriptorsUsed.add(ct);
                                    areaKm2UTM = areaKm2UTM + featureDescriptor.getAreaKm2UTM();
                                }
                            }
                            polygonPerArea = featureDescriptorsUsed.size() / areaKm2UTM * 1000;
                        } else {
                            polygonPerArea = -999;
                        }
                        gpkg.dispose();

                        // look for missing table entries: if no empty cells exist, simplify the database search for "IS NOT NULL" -> only one time necessary, not for all columns
                        int[] numNull = new int[csvNumAssessment];
                        for (int i = 0; i < csvNumAssessment; i++) {
                            command = new StringBuilder();
                            command.append("SELECT COUNT(*) FROM ").append(tableNameMasked).append(" WHERE ").append(colNamesAssessment.get(i)).append(" IS NULL");
                            tmpRS = statementAscii.executeQuery(command.toString());
                            tmpRS.next();
                            numNull[i] = tmpRS.getInt(1);
                        }
                        boolean noNull = allIdentical(numNull, 0);

                        // get number/rate of excess items in each assessed column of ascii data -> concatenate all join and definition rows and find duplicates
                        statementAscii.executeUpdate("ALTER TABLE " + tableNameMasked + " ADD COLUMN joindefinecritascii TEXT");
                        command = new StringBuilder();
                        command.append("UPDATE ").append(tableNameMasked).append(" SET joindefinecritascii=CONCAT_WS('##', '', ");
                        for (String tmp : asciiColNameJoin) {
                            command.append(tmp).append(", ");
                        }
                        for (int i = 0; i < asciiColNameDefine.size() - 1; i++) {
                            command.append(asciiColNameDefine.get(i)).append(", ");
                        }
                        command.append(asciiColNameDefine.get(asciiColNameDefine.size() - 1)).append(")");
                        statementAscii.executeUpdate(command.toString());
//                        statementAscii.executeUpdate("CREATE INDEX ON " + tableNameMasked + " (joindefinecritascii)");

                        excessDataAll = new int[csvNumAssessment]; // excess data (0 means no duplicate, 1 means one duplicate)
                        int ct = -1;
                        for (String tmp : colNamesAssessment) {
                            System.out.println("Get excess data for " + tmp);
                            ct++;
                            command = new StringBuilder();
                            command.append("SELECT COUNT(joindefinecritascii) FROM ").append(tableNameMasked)
                                    .append(" WHERE ").append(tmp).append(" IS NOT NULL");
                            tmpRS = statementAscii.executeQuery(command.toString());
                            tmpRS.next();
                            int numAll = tmpRS.getInt(1);

                            command = new StringBuilder();
                            command.append("SELECT COUNT(DISTINCT(joindefinecritascii)) FROM ").append(tableNameMasked)
                                    .append(" WHERE ").append(tmp).append(" IS NOT NULL");
                            tmpRS = statementAscii.executeQuery(command.toString());
                            tmpRS.next();
                            int numDistinct = tmpRS.getInt(1);
                            excessDataAll[ct] = numAll - numDistinct;

                            if (noNull) {
                                // if no Null entries occur -> all excess data values are the same
                                System.out.println("No Nulls in assessment columns: no further search necessary");
                                Arrays.fill(excessDataAll, excessDataAll[0]);
                                break;
                            }
                        }
//                        System.out.println("Control: " + ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME));

                        // get number of geo/years/commodities/additional for all columns of masked assessment data
                        for (int i = 0; i < csvNumAssessment; i++) {
                            // spatial dimension
                            System.out.println("Get number of spatial/temporal/thematic/additional dimensions for " + colNamesAssessment.get(i));
                            command = new StringBuilder();
                            command.append("SELECT COUNT(DISTINCT(joincritascii)) FROM ")
                                    .append(tableNameMasked).append(" WHERE ").append(colNamesAssessment.get(i))
                                    .append(" IS NOT NULL");
                            tmpRS = statementAscii.executeQuery(command.toString());
                            tmpRS.next();
                            numGeoAssessmentCols.add(tmpRS.getInt(1));

                            // temporal dimension
                            if (availabilityTemporal) {
                                List<String> yearsAssessmentColsAct = new ArrayList<>();
                                command = new StringBuilder();
                                command.append("SELECT DISTINCT(").append(asciiColNameDefine.get(idxTemporal)).append(") FROM ")
                                        .append(tableNameMasked).append(" WHERE ").append(colNamesAssessment.get(i))
                                        .append(" IS NOT NULL")
                                        .append(" AND ").append(asciiColNameDefine.get(idxTemporal)).append(" IS NOT NULL"); // to meet rows without temporal entry
                                tmpRS = statementAscii.executeQuery(command.toString());
                                while (tmpRS.next()) {
                                    yearsAssessmentColsAct.add(tmpRS.getString(1));
                                }
                                Collections.sort(yearsAssessmentColsAct);
                                yearsAssessmentCols.add(yearsAssessmentColsAct);
                                numYearsAssessmentCols.add(yearsAssessmentColsAct.size());
                            }

                            // thematic dimension - get all distinct thematic classes and number
                            if (availabilityThematic) {
                                List<String> commoditiesAssessmentColsAct = new ArrayList<>();
                                command = new StringBuilder();
                                command.append("SELECT DISTINCT(").append(asciiColNameDefine.get(idxThematic)).append(") FROM ")
                                        .append(tableNameMasked).append(" WHERE ").append(colNamesAssessment.get(i))
                                        .append(" IS NOT NULL")
                                        .append(" AND ").append(asciiColNameDefine.get(idxThematic)).append(" IS NOT NULL"); // to meet rows without thematic entry
                                tmpRS = statementAscii.executeQuery(command.toString());
                                while (tmpRS.next()) {
                                    commoditiesAssessmentColsAct.add(tmpRS.getString(1));
                                }
                                Collections.sort(commoditiesAssessmentColsAct);
                                commoditiesAssessmentCols.add(commoditiesAssessmentColsAct);
                                numCommoditiesAssessmentCols.add(commoditiesAssessmentColsAct.size());
                            }

                            // additional dimension - get all distinct additional classes and number
                            if (availabilityAdditional) {
                                List<List<String>> additionalAssessmentColAllAct = new ArrayList<>();
                                List<Integer> additionalAssessmentColAllNumAct = new ArrayList<>();
                                for (int idxAdditionalAct : idxAdditional) {
                                    List<String> additionalAssessmentColAct = new ArrayList<>();
                                    command = new StringBuilder();
                                    command.append("SELECT DISTINCT(").append(asciiColNameDefine.get(idxAdditionalAct)).append(") FROM ")
                                            .append(tableNameMasked).append(" WHERE ").append(colNamesAssessment.get(i))
                                            .append(" IS NOT NULL")
                                            .append(" AND ").append(asciiColNameDefine.get(idxAdditionalAct)).append(" IS NOT NULL"); // to meet rows without additional entry
                                    tmpRS = statementAscii.executeQuery(command.toString());
                                    while (tmpRS.next()) {
                                        additionalAssessmentColAct.add(tmpRS.getString(1));
                                    }
                                    Collections.sort(additionalAssessmentColAct);
                                    additionalAssessmentColAllAct.add(additionalAssessmentColAct);
                                    additionalAssessmentColAllNumAct.add(additionalAssessmentColAct.size());
                                }
                                additionalAssessmentCols.add(additionalAssessmentColAllAct);
                                numAdditionalAssessmentCols.add(additionalAssessmentColAllNumAct);
                            }

                            //thematic dimension - get all commodity entries for the current attribute
                            if (availabilityThematic && thematicMapping) {
                                List<String> commoditiesAssessmentColsAllAct = new ArrayList<>();
                                command = new StringBuilder();
                                command.append("SELECT ").append(asciiColNameDefine.get(idxThematic)).append(" FROM ")
                                        .append(tableNameMasked).append(" WHERE ").append(colNamesAssessment.get(i))
                                        .append(" IS NOT NULL")
                                        .append(" AND ").append(asciiColNameDefine.get(idxThematic)).append(" IS NOT NULL"); // to meet rows without thematic entry
                                tmpRS = statementAscii.executeQuery(command.toString());
                                while (tmpRS.next()) {
                                    commoditiesAssessmentColsAllAct.add(tmpRS.getString(1));
                                }
                                commoditiesAssessmentColsAll.add(commoditiesAssessmentColsAllAct);
                            }

                            if (noNull) {
                                // if no Null entries occur -> shorten the search and fill all lists with the same values
                                System.out.println("No Nulls in assessment columns: no further search necessary");
                                for (int j = 1; j < csvNumAssessment; j++) {
                                    // loop over all assessment columns from second element to get correct overall length of lists
                                    numGeoAssessmentCols.add(numGeoAssessmentCols.get(0));
                                    if (availabilityTemporal) {
                                        yearsAssessmentCols.add(yearsAssessmentCols.get(0));
                                        numYearsAssessmentCols.add(numYearsAssessmentCols.get(0));
                                    }
                                    if (availabilityThematic) {
                                        commoditiesAssessmentCols.add(commoditiesAssessmentCols.get(0));
                                        numCommoditiesAssessmentCols.add(numCommoditiesAssessmentCols.get(0));
                                    }
                                    if (availabilityAdditional) {
                                        additionalAssessmentCols.add(additionalAssessmentCols.get(0));
                                        numAdditionalAssessmentCols.add(numAdditionalAssessmentCols.get(0));
                                    }
                                    if (availabilityThematic && thematicMapping) {
                                        commoditiesAssessmentColsAll.add(commoditiesAssessmentColsAll.get(0));
                                    }
                                }
                                break;
                            }
                        }

                        // get combinations
                        // get count properties of temporal units per geographical unit
                        temporalPerGeo = new ArrayList<>();
                        thematicPerGeo = new ArrayList<>();
                        thematicPerTemp = new ArrayList<>();
                        for (int i = 0; i < csvNumAssessment; i++) {
                            System.out.println("Get number of combinations for " + colNamesAssessment.get(i));
                            command = new StringBuilder();
                            command.append("SELECT COUNT(DISTINCT(joincritascii)) FROM ").append(tableNameMasked);
                            tmpRS = statementAscii.executeQuery(command.toString());
                            tmpRS.next();
                            int numGeoDistinct = tmpRS.getInt(1); // number of distinct geographic units

                            // temporal units per geographical unit
                            if (availabilityTemporal) {
                                List<EmpiricalDistributionProperty> tmp = new ArrayList<>();
                                int[] temporalPerGeoSingular = new int[numGeoDistinct];
                                Arrays.fill(temporalPerGeoSingular, 0); // prefill with zeros
                                command = new StringBuilder();
                                command.append("SELECT joincritascii, COUNT(DISTINCT(").append(asciiColNameDefine.get(idxTemporal))
                                        .append(")) FROM ").append(tableNameMasked).append(" WHERE ").append(colNamesAssessment.get(i))
                                        .append(" IS NOT NULL GROUP BY joincritascii");
                                tmpRS = statementAscii.executeQuery(command.toString());
                                int ctAct = -1;
                                while (tmpRS.next()) {
                                    // only categories with a count!=0 are included here
                                    ctAct++;
                                    temporalPerGeoSingular[ctAct] = tmpRS.getInt("count");
                                }

                                tmp.add(new EmpiricalDistributionProperty("mean", getMean(temporalPerGeoSingular)));
                                tmp.add(new EmpiricalDistributionProperty("min", getQuantile(temporalPerGeoSingular, 0)));
                                tmp.add(new EmpiricalDistributionProperty("5 % quantile", getQuantile(temporalPerGeoSingular, .05)));
                                tmp.add(new EmpiricalDistributionProperty("25 % quantile", getQuantile(temporalPerGeoSingular, .25)));
                                tmp.add(new EmpiricalDistributionProperty("50 % quantile", getQuantile(temporalPerGeoSingular, .5)));
                                tmp.add(new EmpiricalDistributionProperty("75 % quantile", getQuantile(temporalPerGeoSingular, .75)));
                                tmp.add(new EmpiricalDistributionProperty("95 % quantile", getQuantile(temporalPerGeoSingular, .95)));
                                tmp.add(new EmpiricalDistributionProperty("max", getQuantile(temporalPerGeoSingular, 1)));

                                temporalPerGeo.add(tmp);
                            }

                            // thematic units per geographical unit
                            if (availabilityThematic) {
                                List<EmpiricalDistributionProperty> tmp = new ArrayList<>();
                                int[] thematicPerGeoSingular = new int[numGeoDistinct];
                                Arrays.fill(thematicPerGeoSingular, 0); // prefill with zeros
                                command = new StringBuilder();
                                command.append("SELECT joincritascii, COUNT(DISTINCT(").append(asciiColNameDefine.get(idxThematic))
                                        .append(")) FROM ").append(tableNameMasked).append(" WHERE ").append(colNamesAssessment.get(i))
                                        .append(" IS NOT NULL GROUP BY joincritascii");
                                tmpRS = statementAscii.executeQuery(command.toString());
                                int ctAct = -1;
                                while (tmpRS.next()) {
                                    // only categories with a count!=0 are included here
                                    ctAct++;
                                    thematicPerGeoSingular[ctAct] = tmpRS.getInt("count");
                                }

                                tmp.add(new EmpiricalDistributionProperty("mean", getMean(thematicPerGeoSingular)));
                                tmp.add(new EmpiricalDistributionProperty("min", getQuantile(thematicPerGeoSingular, 0)));
                                tmp.add(new EmpiricalDistributionProperty("5 % quantile", getQuantile(thematicPerGeoSingular, .05)));
                                tmp.add(new EmpiricalDistributionProperty("25 % quantile", getQuantile(thematicPerGeoSingular, .25)));
                                tmp.add(new EmpiricalDistributionProperty("50 % quantile", getQuantile(thematicPerGeoSingular, .5)));
                                tmp.add(new EmpiricalDistributionProperty("75 % quantile", getQuantile(thematicPerGeoSingular, .75)));
                                tmp.add(new EmpiricalDistributionProperty("95 % quantile", getQuantile(thematicPerGeoSingular, .95)));
                                tmp.add(new EmpiricalDistributionProperty("max", getQuantile(thematicPerGeoSingular, 1)));

                                thematicPerGeo.add(tmp);
                            }

                            // thematic units per temporal unit
                            if (availabilityTemporal && availabilityThematic) {
                                command = new StringBuilder();
                                command.append("SELECT COUNT(DISTINCT(").append(asciiColNameDefine.get(idxTemporal)).append(")) FROM ").append(tableNameMasked);
                                tmpRS = statementAscii.executeQuery(command.toString());
                                tmpRS.next();
                                int numTempDistinct = tmpRS.getInt(1); // number of distinct geographic units

                                List<EmpiricalDistributionProperty> tmp = new ArrayList<>();
                                int[] thematicPerTempSingular = new int[numTempDistinct];
                                Arrays.fill(thematicPerTempSingular, 0); // prefill with zeros
                                command = new StringBuilder();
                                command.append("SELECT ").append(asciiColNameDefine.get(idxTemporal)).append(", COUNT(DISTINCT(").append(asciiColNameDefine.get(idxThematic))
                                        .append(")) FROM ").append(tableNameMasked).append(" WHERE ").append(colNamesAssessment.get(i))
                                        .append(" IS NOT NULL GROUP BY ").append(asciiColNameDefine.get(idxTemporal));
                                tmpRS = statementAscii.executeQuery(command.toString());
                                int ctAct = -1;
                                while (tmpRS.next()) {
                                    // only categories with a count!=0 are included here
                                    ctAct++;
                                    thematicPerTempSingular[ctAct] = tmpRS.getInt("count");
                                }

                                tmp.add(new EmpiricalDistributionProperty("mean", getMean(thematicPerTempSingular)));
                                tmp.add(new EmpiricalDistributionProperty("min", getQuantile(thematicPerTempSingular, 0)));
                                tmp.add(new EmpiricalDistributionProperty("5 % quantile", getQuantile(thematicPerTempSingular, .05)));
                                tmp.add(new EmpiricalDistributionProperty("25 % quantile", getQuantile(thematicPerTempSingular, .25)));
                                tmp.add(new EmpiricalDistributionProperty("50 % quantile", getQuantile(thematicPerTempSingular, .5)));
                                tmp.add(new EmpiricalDistributionProperty("75 % quantile", getQuantile(thematicPerTempSingular, .75)));
                                tmp.add(new EmpiricalDistributionProperty("95 % quantile", getQuantile(thematicPerTempSingular, .95)));
                                tmp.add(new EmpiricalDistributionProperty("max", getQuantile(thematicPerTempSingular, 1)));

                                thematicPerTemp.add(tmp);
                            }

                            if (noNull) {
                                // if no Null entries occur -> shorten the search and fill all lists with the same values
                                System.out.println("No Nulls in assessment columns: no further search necessary");
                                for (int j = 1; j < csvNumAssessment; j++) {
                                    // loop over all assessment columns from second element to get correct overall length of lists
                                    if (availabilityTemporal) {
                                        temporalPerGeo.add(temporalPerGeo.get(0));
                                    }
                                    if (availabilityThematic) {
                                        thematicPerGeo.add(thematicPerGeo.get(0));
                                    }
                                    if (availabilityTemporal && availabilityThematic) {
                                        thematicPerTemp.add(thematicPerTemp.get(0));
                                    }
                                }
                                break;
                            }
                        }


                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }


                } else {
                    // direct conversion of csv content into Java objects -> suitable for smaller files
                    // -> no use of postgres database
                    CSVReader csvContent = new CSVReader(fileName, ",", asciiColNameJoin, asciiColNameDefine, asciiColNameIgnore);
                    csvContent.getContent();
                    csvNumAssessment = csvContent.headerAssessment.size();
                    colNamesAssessment = csvContent.headerAssessment;

                    // get indices of all relevant combinations in asciiColNameJoin
                    List<Integer> idxRelevant = new ArrayList<>();
                    List<String> joinCritGeoExist = new ArrayList<>(); // elements in joinCritGeo used in csv file, too
                    for (int csvCt1 = 0; csvCt1 < csvContent.fileContentJoin.size(); csvCt1++) {
                        // loop over all entries in csv
                        StringBuilder joinCritActCsv = new StringBuilder();
                        for (int csvCt2 = 0; csvCt2 < asciiColNameJoin.size(); csvCt2++) {
                            // loop over all join criteria in csv
                            joinCritActCsv.append(csvContent.fileContentJoin.get(csvCt1)[csvCt2]);
                        }
                        if (joinCritGeo.contains(joinCritActCsv.toString())) {
                            // this combination of criteria is valid for current geodata
                            idxRelevant.add(csvCt1);

                            if (!joinCritGeoExist.contains(joinCritActCsv.toString())) {
                                joinCritGeoExist.add(joinCritActCsv.toString());
                            }
                        }
                    }

                    // get a list of relevant csv data in current geodata
                    List<String[]> csvContentMaskedJoin = new ArrayList<>(); // for excess items also join parameters necessary
                    List<String[]> csvContentMaskedDefine = new ArrayList<>();
                    List<String[]> csvContentMaskedAssessment = new ArrayList<>();
                    for (Integer idxRelevantAct : idxRelevant) {
                        csvContentMaskedJoin.add(csvContent.fileContentJoin.get(idxRelevantAct));
                        csvContentMaskedDefine.add(csvContent.fileContentDefine.get(idxRelevantAct));
                        csvContentMaskedAssessment.add(csvContent.fileContentAssessment.get(idxRelevantAct));
                    }

                    // get number/rate of missing values in each assessed column of ascii data
                    numDataAll = csvContentMaskedAssessment.size();
                    numDataAssessment = new int[csvNumAssessment];
                    Arrays.fill(numDataAssessment, 0); // preallocate with zero
                    for (String[] tmp : csvContentMaskedAssessment) {
                        int i = -1;
                        for (String tmp2 : tmp) {
                            i++;
                            if (!tmp2.equals("")) {
                                numDataAssessment[i]++;
                            }
                        }
                    }
                    // TODO: get missing values within secondary criteria (date with increment)

                    // get number/rate of excess items in each assessed column of ascii data -> concatenate all definition rows
                    String joinDelimiter = "##";
                    List<String> joinCritAsciiDefined = new ArrayList<>();
                    for (int i = 0; i < csvContentMaskedDefine.size(); i++) {
                        StringBuilder joinCritAsciiAct = new StringBuilder();
                        for (String tmp : csvContentMaskedJoin.get(i)) {
                            joinCritAsciiAct.append(joinDelimiter);
                            joinCritAsciiAct.append(tmp);
                        }
                        for (String tmp : csvContentMaskedDefine.get(i)) {
                            joinCritAsciiAct.append(joinDelimiter); // delimiter between fields for minimizing cluttered entries
                            joinCritAsciiAct.append(tmp);
                        }
                        joinCritAsciiDefined.add(joinCritAsciiAct.toString());
                    }
                    List<List<Integer>> joinCritAsciiDefinedIndices = getStringOccurrenceIndices(joinCritAsciiDefined);
                    excessDataAll = new int[csvNumAssessment]; // excess data (0 means no duplicate, 1 means one duplicate)
                    Arrays.fill(excessDataAll, 0);
                    for (int ctAssessmentColumn = 0; ctAssessmentColumn < csvNumAssessment; ctAssessmentColumn++) {
                        // loop over assessment columns
                        for (List<Integer> idxElementAct : joinCritAsciiDefinedIndices) {
                            // loop over index list
                            if (idxElementAct.size() == 1) {
                                // only one index available -> no duplicates
                                continue;
                            }
                            int[] entriesAct = new int[idxElementAct.size()];
                            Arrays.fill(entriesAct, 0);
                            for (int ctDuplicates = 0; ctDuplicates < idxElementAct.size(); ctDuplicates++) {
                                // loop over potentially duplicate entries
                                if (!csvContentMaskedAssessment.get(idxElementAct.get(ctDuplicates))[ctAssessmentColumn].equals("")) {
                                    // entry in particular row/column available
                                    entriesAct[ctDuplicates] = 1;
                                }
                            }
                            int entriesActSum = 0;
                            for (int entriesTmp : entriesAct) {
                                entriesActSum = entriesActSum + entriesTmp;
                            }
                            if (entriesActSum > 0) {
                                // only add to excess data if some data in this column is available
                                excessDataAll[ctAssessmentColumn] = excessDataAll[ctAssessmentColumn] + entriesActSum - 1;
                            }
                        }
                    }

                    // get polygons per area (per 1000 square kilometer)
                    // only polygons with data representations in ascii file are taken into account
                    gpkg.open(geoTableNameAct);
                    if (gpkg.geometryType.contains("polygon")) {
                        List<FeatureDescriptor> featureDescriptors = gpkg.getCenterArea();
                        List<Integer> featureDescriptorsUsed = new ArrayList<>();
                        double areaKm2UTM = 0.0;
                        int ct = -1;
                        for (FeatureDescriptor featureDescriptor : featureDescriptors) {
                            ct++;
                            String attributeValueMerged = featureDescriptor.getAttributeValuesIntendedMerged(geoColNameJoin);
                            if (joinCritGeoExist.contains(attributeValueMerged)) {
                                featureDescriptorsUsed.add(ct);
                                areaKm2UTM = areaKm2UTM + featureDescriptor.getAreaKm2UTM();
                            }
                        }
                        polygonPerArea = featureDescriptorsUsed.size() / areaKm2UTM * 1000;
                    } else {
                        polygonPerArea = -999;
                    }
                    gpkg.dispose();

                    // get spatial data from csvContentMaskedJoin
                    List<String> csvContentMaskedSpatial = new ArrayList<>();
                    for (String[] tmp : csvContentMaskedJoin) {
                        StringBuilder tmp2 = new StringBuilder();
                        for (String tmp3 : tmp) {
                            tmp2.append(tmp3);
                        }
                        csvContentMaskedSpatial.add(tmp2.toString());
                    }

                    // get temporal and thematic data from csvContentMaskedDefine
                    List<String> csvContentMaskedTemporal = new ArrayList<>();
                    List<String> csvContentMaskedThematic = new ArrayList<>();
                    for (String[] tmp : csvContentMaskedDefine) {
                        csvContentMaskedTemporal.add(tmp[idxTemporal]);
                        csvContentMaskedThematic.add(tmp[idxThematic]);
                    }

                    // prepare number of geometries, years, commodities per attribute
                    boolean[][] idxMaskedRelevantAll = new boolean[csvNumAssessment][numDataAll];
                    for (int i = 0; i < csvNumAssessment; i++) {
                        // preallocate boolean matrix
                        Arrays.fill(idxMaskedRelevantAll[i], false);
                    }
                    for (int i = 0; i < numDataAll; i++) {
                        // fill boolean index matrix
                        String[] tmp = csvContentMaskedAssessment.get(i);
                        for (int j = 0; j < csvNumAssessment; j++) {
                            if (!tmp[j].equals("")) {
                                idxMaskedRelevantAll[j][i] = true;
                            }
                        }
                    }

                    // get number of geometries per attribute
                    List<List<String>> geoMaskedAssessment = new ArrayList<>();
                    List<List<String>> geoMaskedAssessmentUnique = new ArrayList<>();
                    for (int i = 0; i < csvNumAssessment; i++) {
                        // get available geometries of all columns of masked assessment data
                        geoMaskedAssessment.add(getListFromLogicalIndex(csvContentMaskedSpatial, idxMaskedRelevantAll[i]));
                        geoMaskedAssessmentUnique.add(getStringListUniqueMembers(geoMaskedAssessment.get(i)));
                        numGeoAssessmentCols.add(geoMaskedAssessmentUnique.get(i).size());
                    }

                    // get number of years per attribute
                    if (availabilityTemporal) {
                        List<List<String>> yearsMaskedAssessment = new ArrayList<>();
                        for (int i = 0; i < csvNumAssessment; i++) {
                            // get available years of all columns of masked assessment data
                            yearsMaskedAssessment.add(getListFromLogicalIndex(csvContentMaskedTemporal, idxMaskedRelevantAll[i]));
                            List<String> yearsAssessmentColsAct = getStringListUniqueMembers(yearsMaskedAssessment.get(i));
                            Collections.sort(yearsAssessmentColsAct);
                            yearsAssessmentCols.add(yearsAssessmentColsAct);
                            numYearsAssessmentCols.add(yearsAssessmentCols.get(i).size());
                        }
                    }

                    // get number of commodities per attribute
                    if (availabilityThematic) {
                        List<List<String>> commoditiesMaskedAssessment = new ArrayList<>();
                        for (int i = 0; i < csvNumAssessment; i++) {
                            // get available commodities of all columns of masked assessment data
                            commoditiesMaskedAssessment.add(getListFromLogicalIndex(csvContentMaskedThematic, idxMaskedRelevantAll[i]));
                            List<String> commoditiesAssessmentColsAct = getStringListUniqueMembers(commoditiesMaskedAssessment.get(i));
                            Collections.sort(commoditiesAssessmentColsAct);
                            commoditiesAssessmentCols.add(commoditiesAssessmentColsAct);
                            numCommoditiesAssessmentCols.add(commoditiesAssessmentCols.get(i).size());
                            if (thematicMapping) {
                                commoditiesAssessmentColsAll.add(commoditiesMaskedAssessment.get(i));
                            }
                        }
                    }

                    // get additional dimension per attribute
                    //TODO: add additional dimension for java objects

                    // prepare for various distribution parameters
                    List<String> csvContentMaskedJoined = new ArrayList<>();
                    for (String[] csvContentMaskedJoinAct : csvContentMaskedJoin) {
                        StringBuilder tmp = new StringBuilder();
                        for (String csvContentMaskedJoinActI : csvContentMaskedJoinAct) {
                            tmp.append(csvContentMaskedJoinActI);
                        }
                        csvContentMaskedJoined.add(tmp.toString());
                    }

                    CsvMaskedContent csvMaskedContent = new CsvMaskedContent(csvContentMaskedJoined, csvContentMaskedTemporal, csvContentMaskedThematic, csvContentMaskedAssessment, idxMaskedRelevantAll);

                    // get distribution parameters of number of years per attribute over all spatial units
                    if (availabilityTemporal) {
                        temporalPerGeo = csvMaskedContent.getTemporalPerGeo();
                    }

                    // get distribution parameters of number of commodities per attribute over all spatial units
                    if (availabilityThematic) {
                        thematicPerGeo = csvMaskedContent.getThematicPerGeo();
                    }

                    // get distribution parameters of number of commodities per attribute over all temporal units
                    if (availabilityTemporal && availabilityThematic) {
                        thematicPerTemp = csvMaskedContent.getThematicPerTemp();
                    }
                }


                ///////////////////////////////////
                // Instantiate Metadata Elements //
                ///////////////////////////////////
                // order of classes according to MD_Metadata:
                // metadataIdentifier:           1 basicInformation
                // defaultLocale:                1 basicInformation
                // parentMetadata:               1 basicInformation
                // contact:                      1 basicInformation
                // dateInfo:                     1 basicInformation
                // metadataStandard:             5 metadataContact
                // metadataProfile:              5 metadataContact
                // alternativeMetadataReference: 5 metadataContact
                // otherLocale:                  1 basicInformation
                // metadataLinkage:              1 basicInformation
                // spatialRepresentationInfo:    3 structureOfSpatialData
                // referenceSystemInfo:          2 referenceSystem
                // metadataExtensionInfo:        5 metadataContact
                // identificationInfo:           3 structureOfSpatialData
                // contentInfo:                  3 structureOfSpatialData
                // distributionInfo:             1 basicInformation
                // dataQualityInfo:              4 dataQuality
                // portrayalCatalogueInfo:       5 metadataContact
                // metadataConstraints:          1 basicInformation
                // applicationSchemaInfo:        5 metadataContact
                // metadataMaintenance:          5 metadataContact
                // resourceLineage:              6 provenance
                // metadataScope:                5 metadataContact

                // get (1) basic information
                System.out.println("Basic Information:");

                String identifierCode = "pid:" + UUID.randomUUID().toString();

                MD_Identifier mdIdentifier = new MD_Identifier();
                mdIdentifier.addCode(identifierCode);
                mdIdentifier.finalizeClass();

                CI_Individual ciIndividual = new CI_Individual();
                ciIndividual.addName(System.getProperty("user.name"));
                ciIndividual.finalizeClass();

                CI_Responsibility ciResponsibility = new CI_Responsibility();
                ciResponsibility.addRole(new CI_RoleCode(CI_RoleCode.CI_RoleCodes.resourceProvider));
                ciResponsibility.addParty(ciIndividual);
                ciResponsibility.finalizeClass();

                File csvFile = new File(fileName);

                ZonedDateTime creation = ZonedDateTime.parse(Files.readAttributes(csvFile.toPath(), BasicFileAttributes.class).creationTime().toString());
                creation = creation.withZoneSameInstant(ZoneId.of("UTC")); // convert to UTC timezone
                String creationString = creation.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME); // datetime in ISO 8601 format

                ZonedDateTime lastModified = ZonedDateTime.ofInstant(Instant.ofEpochMilli(csvFile.lastModified()), ZoneId.systemDefault()); // local timezone
                lastModified = lastModified.withZoneSameInstant(ZoneId.of("UTC")); // convert to UTC timezone
                String lastModifiedString = lastModified.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME); // datetime in ISO 8601 format

                CI_Date ciDateCreation = new CI_Date();
                ciDateCreation.addDateType(new CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes.creation));
                ciDateCreation.addDate(creationString);
                ciDateCreation.finalizeClass();

                CI_Date ciDateLastModified = new CI_Date();
                ciDateLastModified.addDateType(new CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes.lastUpdate));
                ciDateLastModified.addDate(lastModifiedString);
                ciDateLastModified.finalizeClass();


                // get (2) reference system
                System.out.println("Reference System:");


                // get (3) structure of spatial data
                System.out.println("Structure of Spatial Data:");

                String now = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

                CI_Date ciDate = new CI_Date();
                ciDate.addDate(now);
                ciDate.addDateType(new CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes.creation));
                ciDate.finalizeClass();

                // TODO: informative title available?
                CI_Citation ciCitation = new CI_Citation();
                ciCitation.addTitle("");
                ciCitation.addDate(ciDate);
                ciCitation.finalizeClass();

                MD_DataIdentification mdDataIdentification = new MD_DataIdentification();
                mdDataIdentification.addCitation(ciCitation);

                // TODO: spatial resolution and spatial extent (via flagging in Geopackage class)

                List<TemporalRegularity> temporalRegularities = new ArrayList<>();
                for (int i = 0; i < csvNumAssessment; i++) {
                    // temporal dimension
                    if (availabilityTemporal) {
                        EX_Extent exExtent = new EX_Extent();
                        exExtent.addDescription("attributeName: " + colNamesAssessment.get(i));
                        TemporalRegularity tr = getRegularity(yearsAssessmentCols.get(i));
                        temporalRegularities.add(tr);
                        // resolution will be -999
                        if (tr.regularity) {
                            // if temporal resolution is evenly incremented give begin and end and temporal resolution
                            TM_Instant tmInstantBegin = new TM_Instant();
                            tmInstantBegin.addPosition(String.valueOf(tr.begin));
                            TM_Instant tmInstantEnd = new TM_Instant();
                            tmInstantEnd.addPosition(String.valueOf(tr.end));
                            TM_Period tmPeriod = new TM_Period();
                            tmPeriod.addBegin(tmInstantBegin);
                            tmPeriod.addEnd(tmInstantEnd);
                            tmPeriod.finalizeClass();
                            EX_TemporalExtent exTemporalExtent = new EX_TemporalExtent();
                            exTemporalExtent.addExtent(tmPeriod);
                            exTemporalExtent.finalizeClass();
                            exExtent.addTemporalElement(exTemporalExtent);
                        } else {
                            // give all single temporal data
                            for (String tmp : yearsAssessmentCols.get(i)) {
                                TM_Instant tmInstant = new TM_Instant();
                                tmInstant.addPosition(tmp);
                                EX_TemporalExtent exTemporalExtent = new EX_TemporalExtent();
                                exTemporalExtent.addExtent(tmInstant);
                                exTemporalExtent.finalizeClass();
                                exExtent.addTemporalElement(exTemporalExtent);
                            }
                        }
                        exExtent.finalizeClass();
                        mdDataIdentification.addTemporalResolution(String.valueOf(tr.resolution));
                        mdDataIdentification.addExtent(exExtent);
                    }

                    // thematic dimension
                    if (availabilityThematic) {
                        MD_Keywords mdKeywords = new MD_Keywords();
                        mdKeywords.addType(new MD_KeywordTypeCode(MD_KeywordTypeCode.MD_KeywordTypeCodes.theme));
                        mdKeywords.addKeyword("attributeName: " + colNamesAssessment.get(i));
                        for (String tmp : commoditiesAssessmentCols.get(i)) {
                            if (thematicMapping) {
                                mdKeywords.addKeyword(thematicMappingDictionary.get(tmp));
                            } else {
                                mdKeywords.addKeyword(tmp);
                            }
                        }
                        mdKeywords.finalizeClass();
                        mdDataIdentification.addDescriptiveKeywords(mdKeywords);
                    }
                }

                String environmentalDescription = "file name: " + fileName + "; "
                        + "file size: " + (int) csvFile.length() + " B; "
                        + "geographical file: " + fileNameGeodata + "; "
                        + "layer name: " + geoTableNameAct + "; "
                        + "os: " + System.getProperty("os.name");

                mdDataIdentification.addSpatialRepresentationType(new MD_SpatialRepresentationTypeCode(MD_SpatialRepresentationTypeCode.MD_SpatialRepresentationTypeCodes.textTable));
                mdDataIdentification.addEnvironmentalDescription(environmentalDescription);
                mdDataIdentification.finalizeClass();


                // get (4) data quality
                System.out.println("Data Quality:");

                // completenessOmission of data in attribute rows - count of missing data
                List<DQ_CompletenessOmission> dqCompletenessOmissionsCount = new ArrayList<>();
                for (int i = 0; i < csvNumAssessment; i++) {
                    if ((numDataAll - numDataAssessment[i]) > 0) {
                        dqCompletenessOmissionsCount.add(makeDQCompletenessOmission(colNamesAssessment.get(i), numDataAll, numDataAssessment[i], "count"));
                    }
                }

                // completenessOmission of data in attribute rows - rate of missing data
                List<DQ_CompletenessOmission> dqCompletenessOmissionsRate = new ArrayList<>();
                for (int i = 0; i < csvNumAssessment; i++) {
                    if ((numDataAll - numDataAssessment[i]) > 0) {
                        dqCompletenessOmissionsRate.add(makeDQCompletenessOmission(colNamesAssessment.get(i), numDataAll, numDataAssessment[i], "rate"));
                    }
                }

                // completenessCommission of data in attribute rows - count of excess items
                List<DQ_CompletenessCommission> dqCompletenessCommissionsCount = new ArrayList<>();
                for (int i = 0; i < csvNumAssessment; i++) {
                    if (excessDataAll[i] > 0) {
                        dqCompletenessCommissionsCount.add(makeDQCompletenessCommission(colNamesAssessment.get(i), numDataAll, excessDataAll[i], "count"));
                    }
                }

                // completenessCommission of data in attribute rows - rate of excess items
                List<DQ_CompletenessCommission> dqCompletenessCommissionsRate = new ArrayList<>();
                for (int i = 0; i < csvNumAssessment; i++) {
                    if (excessDataAll[i] > 0) {
                        dqCompletenessCommissionsRate.add(makeDQCompletenessCommission(colNamesAssessment.get(i), numDataAll, excessDataAll[i], "rate"));
                    }
                }

                // thematicAccuracy -> count incorrect attribute values (commodities not available in mapping dictionary)
                List<DQ_NonQuantitativeAttributeCorrectness> dqNonQuantitativeAttributeCorrectnessCount = new ArrayList<>();
                List<DQ_NonQuantitativeAttributeCorrectness> dqNonQuantitativeAttributeCorrectnessRate = new ArrayList<>();
                List<Integer[]> mappability = new ArrayList<>();
                if (availabilityThematic && thematicMapping) {
                    for (int i = 0; i < csvNumAssessment; i++) {
                        mappability.add(evaluateCommoditiesMappingPossibility(commoditiesAssessmentColsAll.get(i)));
                        dqNonQuantitativeAttributeCorrectnessCount.add(makeDQNonQuantitativeAttributeCorrectnessCount(colNamesAssessment.get(i), mappability.get(i), now));
                        dqNonQuantitativeAttributeCorrectnessRate.add(makeDQNonQuantitativeAttributeCorrectnessRate(colNamesAssessment.get(i), mappability.get(i), now));
                    }
                }


                // metaquality - number of polygons per area
                DQ_Representativity dqRepresentativitySpatial = new DQ_Representativity();
                if (gpkg.geometryType.contains("polygon")) {
                    DQ_MeasureReference dqMeasureReference = new DQ_MeasureReference();
                    dqMeasureReference.addNameOfMeasure("polygons per area");
                    dqMeasureReference.addMeasureDescription("Number of polygons per 1000 square kilometer. " +
                            "Area size summarized by all polygons, regardless of overlapping.");
                    dqMeasureReference.finalizeClass();

                    DQ_FullInspection dqFullInspection = new DQ_FullInspection();
                    dqFullInspection.addDateTime(now);
                    dqFullInspection.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
                    dqFullInspection.finalizeClass();

                    Record record = new Record();
                    record.addField(String.format(Locale.ENGLISH, "%f", polygonPerArea));
                    record.finalizeClass();

                    DQ_QuantitativeResult dqQuantitativeResult = new DQ_QuantitativeResult();
                    dqQuantitativeResult.addDateTime(now);
                    dqQuantitativeResult.addValue(record);
                    dqQuantitativeResult.addValueUnit("polygons per 1000 square km");

                    dqRepresentativitySpatial.addMeasure(dqMeasureReference);
                    dqRepresentativitySpatial.addEvaluationMethod(dqFullInspection);
                    dqRepresentativitySpatial.addResult(dqQuantitativeResult);
                    dqRepresentativitySpatial.finalizeClass();
                }

                // metaquality - number of different geographies per attribute
                List<DQ_Representativity> dqRepresentativitiesSpatial = new ArrayList<>();
                for (int i = 0; i < csvNumAssessment; i++) {
                    dqRepresentativitiesSpatial.add(makeDQRepresentativitySpatial(colNamesAssessment.get(i), numGeoAssessmentCols.get(i), now));
                }

                // metaquality - number of different years per attribute
                List<DQ_Representativity> dqRepresentativitiesTemporal = new ArrayList<>();
                if (availabilityTemporal) {
                    for (int i = 0; i < csvNumAssessment; i++) {
                        dqRepresentativitiesTemporal.add(makeDQRepresentativityTemporal(colNamesAssessment.get(i), numYearsAssessmentCols.get(i), now));
                    }
                }

                // metaquality - number of different commodity elements per attribute
                List<DQ_Representativity> dqRepresentativitiesThematic = new ArrayList<>();
                if (availabilityThematic) {
                    for (int i = 0; i < csvNumAssessment; i++) {
                        dqRepresentativitiesThematic.add(makeDQRepresentativityThematic(colNamesAssessment.get(i), numCommoditiesAssessmentCols.get(i), now));
                    }
                }

                // metaquality - number of different additional dimension elements per attribute
                List<List<DQ_Representativity>> dqRepresentativitiesAdditional = new ArrayList<>();
                if (availabilityAdditional) {
                    for (int i = 0; i < idxAdditional.size(); i++) {
                        List<DQ_Representativity> dqRepresentativitiesAdditionalAct = new ArrayList<>();
                        String addName = asciiColNameDefine.get(idxAdditional.get(i));
                        for (int j = 0; j < csvNumAssessment; j++) {
                            dqRepresentativitiesAdditionalAct.add(makeDQRepresentativityAdditional(colNamesAssessment.get(j), addName, numAdditionalAssessmentCols.get(j).get(i), now));
                        }
                        dqRepresentativitiesAdditional.add(dqRepresentativitiesAdditionalAct);
                    }
                }

                // metaquality - distribution parameters of different years per geographical unit per attribute
                List<DQ_Representativity> dqRepresentativitiesTempPerGeo = new ArrayList<>();
                if (availabilityTemporal) {
                    for (int i = 0; i < csvNumAssessment; i++) {
                        dqRepresentativitiesTempPerGeo.add(makeDQRepresentativityParamTempPerGeo(colNamesAssessment.get(i), temporalPerGeo.get(i), now));
                    }
                }

                // metaquality - distribution parameters of different commodities per geographical unit per attribute
                List<DQ_Representativity> dqRepresentativitiesThematicPerGeo = new ArrayList<>();
                if (availabilityThematic) {
                    for (int i = 0; i < csvNumAssessment; i++) {
                        dqRepresentativitiesThematicPerGeo.add(makeDQRepresentativityParamThematicPerGeo(colNamesAssessment.get(i), thematicPerGeo.get(i), now));
                    }
                }

                // metaquality - distribution parameters of different commodities per temporal unit per attribute
                List<DQ_Representativity> dqRepresentativitiesThematicPerTemp = new ArrayList<>();
                if (availabilityTemporal && availabilityThematic) {
                    for (int i = 0; i < csvNumAssessment; i++) {
                        dqRepresentativitiesThematicPerTemp.add(makeDQRepresentativityParamThematicPerTemp(colNamesAssessment.get(i), thematicPerTemp.get(i), now));
                    }
                }

                // temporal consistency - correct order and regularly distributed time steps
                List<DQ_TemporalConsistency> dqTemporalConsistencies = new ArrayList<>();
                if (availabilityTemporal) {
                    for (int i = 0; i < csvNumAssessment; i++) {
                        dqTemporalConsistencies.add(makeDQTemporalConsistency(colNamesAssessment.get(i), temporalRegularities.get(i).regularity, now));
                    }
                }

                // frame around data quality fields
                MD_Scope mdScope = new MD_Scope();
                mdScope.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.dataset));
                mdScope.finalizeClass();

                CI_Date ciDateReport = new CI_Date();
                ciDateReport.addDate(now);
                ciDateReport.addDateType(new CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes.creation));
                ciDateReport.finalizeClass();

                CI_Citation ciCitationReport = new CI_Citation();
                ciCitationReport.addTitle("Reporting as standalone quality report");
                ciCitationReport.addDate(ciDateReport);
                ciCitationReport.finalizeClass();

                DQ_StandaloneQualityReportInformation dqStandaloneQualityReportInformation = new DQ_StandaloneQualityReportInformation();
                dqStandaloneQualityReportInformation.addReportReference(ciCitationReport);
                dqStandaloneQualityReportInformation.addAbstract("The standalone quality report attached to this quality evaluation is providing more details on the derivation and aggregation method.");
                dqStandaloneQualityReportInformation.finalizeClass();

                DQ_DataQuality dqDataQuality = new DQ_DataQuality();
                dqDataQuality.addScope(mdScope);
                dqDataQuality.addStandaloneQualityReport(dqStandaloneQualityReportInformation);
                for (DQ_CompletenessOmission dqCompletenessOmission : dqCompletenessOmissionsCount) {
                    dqDataQuality.addReport(dqCompletenessOmission);
                }
                for (DQ_CompletenessOmission dqCompletenessOmission : dqCompletenessOmissionsRate) {
                    dqDataQuality.addReport(dqCompletenessOmission);
                }
                for (DQ_CompletenessCommission dqCompletenessCommission : dqCompletenessCommissionsCount) {
                    dqDataQuality.addReport(dqCompletenessCommission);
                }
                for (DQ_CompletenessCommission dqCompletenessCommission : dqCompletenessCommissionsRate) {
                    dqDataQuality.addReport(dqCompletenessCommission);
                }
                if (thematicMapping) {
                    for (DQ_NonQuantitativeAttributeCorrectness dqNonQuantitativeAttributeCorrectness : dqNonQuantitativeAttributeCorrectnessCount) {
                        dqDataQuality.addReport(dqNonQuantitativeAttributeCorrectness);
                    }
                    for (DQ_NonQuantitativeAttributeCorrectness dqNonQuantitativeAttributeCorrectness : dqNonQuantitativeAttributeCorrectnessRate) {
                        dqDataQuality.addReport(dqNonQuantitativeAttributeCorrectness);
                    }
                }

                if (gpkg.geometryType.contains("polygon")) {
                    dqDataQuality.addReport(dqRepresentativitySpatial);
                }
                for (DQ_Representativity dqRepresentativitySpatial2 : dqRepresentativitiesSpatial) {
                    dqDataQuality.addReport((dqRepresentativitySpatial2));
                }
                if (availabilityTemporal) {
                    for (DQ_Representativity dqRepresentativityTemporal : dqRepresentativitiesTemporal) {
                        dqDataQuality.addReport(dqRepresentativityTemporal);
                    }
                }
                if (availabilityThematic) {
                    for (DQ_Representativity dqRepresentativityThematic : dqRepresentativitiesThematic) {
                        dqDataQuality.addReport(dqRepresentativityThematic);
                    }
                }
                if (availabilityAdditional) {
                    for (List<DQ_Representativity> dqRepresentativitiesAdditionalAct : dqRepresentativitiesAdditional) {
                        for (DQ_Representativity dqRepresentativityAdditional : dqRepresentativitiesAdditionalAct) {
                            dqDataQuality.addReport(dqRepresentativityAdditional);
                        }
                    }
                }
                if (availabilityTemporal) {
                    for (DQ_Representativity dqRepresentativityTempPerGeo : dqRepresentativitiesTempPerGeo) {
                        dqDataQuality.addReport(dqRepresentativityTempPerGeo);
                    }
                }
                if (availabilityThematic) {
                    for (DQ_Representativity dqRepresentativityThematicPerGeo : dqRepresentativitiesThematicPerGeo) {
                        dqDataQuality.addReport(dqRepresentativityThematicPerGeo);
                    }
                }
                if (availabilityTemporal && availabilityThematic) {
                    for (DQ_Representativity dqRepresentativityThematicPerTemp : dqRepresentativitiesThematicPerTemp) {
                        dqDataQuality.addReport(dqRepresentativityThematicPerTemp);
                    }
                }
                if (availabilityTemporal) {
                    for (DQ_TemporalConsistency dqTemporalConsistency : dqTemporalConsistencies) {
                        dqDataQuality.addReport(dqTemporalConsistency);
                    }
                }
                dqDataQuality.addReport(makeDQFormatConsistency(properties.allowedFileFormat, now));

                dqDataQuality.finalizeClass();


                // get (5) metadata contact
                System.out.println("Metadata Contact:");

                CI_Citation ciCitationMetadataStandard = new CI_Citation();
                ciCitationMetadataStandard.addTitle("ISO 19115-1");
                ciCitationMetadataStandard.addEdition("First edition 2014-04-01");
                ciCitationMetadataStandard.finalizeClass();


                // get (6) provenance


                // aggregate all data in MD_Metadata
                MD_Metadata mdMetadata = new MD_Metadata();
                mdMetadata.addMetadataIdentifier(mdIdentifier);
                mdMetadata.addContact(ciResponsibility);
                mdMetadata.addDateInfo(ciDateCreation);
                mdMetadata.addDateInfo(ciDateLastModified);
                mdMetadata.addIdentificationInfo(mdDataIdentification);
                mdMetadata.addMetadataStandard(ciCitationMetadataStandard);

                mdMetadata.addDataQualityInfo(dqDataQuality);
                mdMetadata.finalizeClass();

                dsDataSet.addHas(mdMetadata);
            }
            dsDataSet.finalizeClass();

            if (postgresUse) {
                try {
                    statementAscii.executeUpdate("DROP TABLE IF EXISTS " + tableName);
                    statementAscii.executeUpdate("DROP TABLE IF EXISTS " + tableNameMasked);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return dsDataSet;
    }


    ////////////////////
    // helper methods //
    ////////////////////

    static boolean containsIgnoreCase(List<String> list, String string) {
        for(String i : list){
            if(i.equalsIgnoreCase(string))
                return true;
        }
        return false;
    }

    static List<Integer> getIntListUniqueMembers(List<Integer> list) {
        // return unique members of a integer list (sorting as occurrence in original list)
        return list.stream().distinct().collect(Collectors.toList());
    }

    static List<String> getStringListUniqueMembers(List<String> list) {
        // return unique members of a string list (sorting as occurrence in original list)
        return list.stream().distinct().collect(Collectors.toList());
    }

    static List<List<Integer>> getIntOccurrenceIndices(List<Integer> list) {
        // return indices of occurrence
        List<Integer> listUnique = getIntListUniqueMembers(list);
        List<List<Integer>> indicesUnique = new ArrayList<>();
        for (int listUniqueAct : listUnique) {
            List<Integer> idx = new ArrayList<>();
            int idxAct = list.indexOf(listUniqueAct);
            idx.add(idxAct);
            int ct = 0;

            List<Integer> tmp1Sub = list.subList(idxAct + 1, list.size());
            idxAct = tmp1Sub.indexOf(listUniqueAct);
            while (idxAct != -1) {
                ct++;
                idx.add(idxAct + idx.get(ct - 1) + 1);
                tmp1Sub = tmp1Sub.subList(idxAct + 1, tmp1Sub.size());
                idxAct = tmp1Sub.indexOf(listUniqueAct);
            }
            indicesUnique.add(idx);
        }
        return indicesUnique;
    }

    static List<List<Integer>> getStringOccurrenceIndices(List<String> list) {
        // return indices of occurrence
        List<String> listUnique = getStringListUniqueMembers(list);
        List<List<Integer>> indicesUnique = new ArrayList<>();
        for (String listUniqueAct : listUnique) {
            List<Integer> idx = new ArrayList<>();
            int idxAct = list.indexOf(listUniqueAct);
            idx.add(idxAct);
            int ct = 0;

            List<String> tmp1Sub = list.subList(idxAct + 1, list.size());
            idxAct = tmp1Sub.indexOf(listUniqueAct);
            while (idxAct != -1) {
                ct++;
                idx.add(idxAct + idx.get(ct - 1) + 1);
                tmp1Sub = tmp1Sub.subList(idxAct + 1, tmp1Sub.size());
                idxAct = tmp1Sub.indexOf(listUniqueAct);
            }
            indicesUnique.add(idx);
        }
        return indicesUnique;
    }

    static List<Integer> indexOfAll(List<String> list, String match) {
        // return all indices of match in list
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(match)) {
                indexes.add(i);
            }
        }

        return indexes;
    }

    static DQ_CompletenessOmission makeDQCompletenessOmission(String nameAttribute, int numDataTarget, int numDataActual, String method) {
        // instantiate DQ_CompletenessOmission class for count or rate (defined by "method" argument) of missing values
        DQ_MeasureReference dqMeasureReference = new DQ_MeasureReference();
        if (method.equals("count")) {
            dqMeasureReference.addNameOfMeasure("number of missing items");
            dqMeasureReference.addMeasureDescription("count of all items that should have been in the data set or sample and are missing");
        } else if (method.equals("rate")) {
            dqMeasureReference.addNameOfMeasure("rate of missing items");
            dqMeasureReference.addMeasureDescription("rate of all items that should have been in the data set or sample and are missing");
        }
        dqMeasureReference.finalizeClass();

        DQ_EvaluationMethod dqEvaluationMethod = new DQ_FullInspection();
        dqEvaluationMethod.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
        if (method.equals("count")) {
            dqEvaluationMethod.addEvaluationMethodDescription("count of missing items in the data set");
        } else if (method.equals("rate")) {
            dqEvaluationMethod.addEvaluationMethodDescription("rate of missing items in the data set");
        }
        dqEvaluationMethod.finalizeClass();

        MD_ScopeDescription mdScopeDescription = new MD_ScopeDescription();
        mdScopeDescription.addAttributes(nameAttribute);
        mdScopeDescription.finalizeClass();

        MD_Scope mdScopeAttribute = new MD_Scope();
        mdScopeAttribute.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.attribute));
        mdScopeAttribute.addLevelDescription(mdScopeDescription);
        mdScopeAttribute.finalizeClass();

        String stringMissing;
        if (method.equals("count")) {
            stringMissing = Integer.toString(numDataTarget - numDataActual);
        } else if (method.equals("rate")) {
            stringMissing = Double.toString(100 - (double) numDataActual/numDataTarget * 100);
        } else {
            stringMissing = "";
        }

        Record record = new Record();
        record.addField(stringMissing);
        record.finalizeClass();

        DQ_QuantitativeResult dqQuantitativeResult = new DQ_QuantitativeResult();
        dqQuantitativeResult.addResultScope(mdScopeAttribute);
        dqQuantitativeResult.addValue(record);
        if (method.equals("count")) {
            dqQuantitativeResult.addValueUnit("same unit as in " + nameAttribute + " column");
        } else if (method.equals("rate")) {
            dqQuantitativeResult.addValueUnit("%");
        }
        dqQuantitativeResult.finalizeClass();

        DQ_CompletenessOmission dqCompletenessOmission = new DQ_CompletenessOmission();
        dqCompletenessOmission.addMeasure(dqMeasureReference);
        dqCompletenessOmission.addEvaluationMethod(dqEvaluationMethod);
        dqCompletenessOmission.addResult(dqQuantitativeResult);
        dqCompletenessOmission.finalizeClass();

        return dqCompletenessOmission;
    }

    static DQ_CompletenessCommission makeDQCompletenessCommission(String nameAttribute, int numDataTarget, int numExcessDataActual, String method) {
        // instantiate DQ_CompletenessCommission class for count or rate (defined by "method" argument) of excess items
        DQ_MeasureReference dqMeasureReference = new DQ_MeasureReference();
        if (method.equals("count")) {
            dqMeasureReference.addNameOfMeasure("number of excess items");
            dqMeasureReference.addMeasureDescription("number of items within the data set or sample that should not have been present");
        } else if (method.equals("rate")) {
            dqMeasureReference.addNameOfMeasure("rate of excess items");
            dqMeasureReference.addMeasureDescription("number of excess items in the data set or sample in relation to the number of items that should have been present");
        }
        dqMeasureReference.finalizeClass();

        DQ_EvaluationMethod dqEvaluationMethod = new DQ_FullInspection();
        dqEvaluationMethod.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
        if (method.equals("count")) {
            dqEvaluationMethod.addEvaluationMethodDescription("count of excess items in the data set");
        } else if (method.equals("rate")) {
            dqEvaluationMethod.addEvaluationMethodDescription("rate of excess items in the data set");
        }
        dqEvaluationMethod.finalizeClass();

        MD_ScopeDescription mdScopeDescription = new MD_ScopeDescription();
        mdScopeDescription.addAttributes(nameAttribute);
        mdScopeDescription.finalizeClass();

        MD_Scope mdScopeAttribute = new MD_Scope();
        mdScopeAttribute.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.attribute));
        mdScopeAttribute.addLevelDescription(mdScopeDescription);
        mdScopeAttribute.finalizeClass();

        String stringMissing;
        if (method.equals("count")) {
            stringMissing = Integer.toString(numExcessDataActual);
        } else if (method.equals("rate")) {
            stringMissing = Double.toString((double) numExcessDataActual/numDataTarget * 100);
        } else {
            stringMissing = "";
        }

        Record record = new Record();
        record.addField(stringMissing);
        record.finalizeClass();

        DQ_QuantitativeResult dqQuantitativeResult = new DQ_QuantitativeResult();
        dqQuantitativeResult.addResultScope(mdScopeAttribute);
        dqQuantitativeResult.addValue(record);
        if (method.equals("count")) {
            dqQuantitativeResult.addValueUnit("same unit as in " + nameAttribute + " column");
        } else if (method.equals("rate")) {
            dqQuantitativeResult.addValueUnit("%");
        }
        dqQuantitativeResult.finalizeClass();

        DQ_CompletenessCommission dqCompletenessCommission = new DQ_CompletenessCommission();
        dqCompletenessCommission.addMeasure(dqMeasureReference);
        dqCompletenessCommission.addEvaluationMethod(dqEvaluationMethod);
        dqCompletenessCommission.addResult(dqQuantitativeResult);
        dqCompletenessCommission.finalizeClass();

        return dqCompletenessCommission;
    }

    static DQ_Representativity makeDQRepresentativitySpatial(String nameAttribute, int numDataActual, String now) {
        // instantiate DQ_Representativity class for number of different spatial units
        DQ_MeasureReference dqMeasureReference = new DQ_MeasureReference();
        dqMeasureReference.addNameOfMeasure("number of different spatial elements");
        dqMeasureReference.addMeasureDescription("Number of different geometries at given attribute.");
        dqMeasureReference.finalizeClass();

        DQ_EvaluationMethod dqEvaluationMethod = new DQ_FullInspection();
        dqEvaluationMethod.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
        dqEvaluationMethod.addDateTime(now);
        dqEvaluationMethod.addEvaluationMethodDescription("number of different geometries at given attribute");
        dqEvaluationMethod.finalizeClass();

        MD_ScopeDescription mdScopeDescription = new MD_ScopeDescription();
        mdScopeDescription.addAttributes(nameAttribute);
        mdScopeDescription.finalizeClass();

        MD_Scope mdScopeAttribute = new MD_Scope();
        mdScopeAttribute.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.attribute));
        mdScopeAttribute.addLevelDescription(mdScopeDescription);
        mdScopeAttribute.finalizeClass();

        String stringNumDataActual;
        stringNumDataActual = Integer.toString(numDataActual);

        Record record = new Record();
        record.addField(stringNumDataActual);
        record.finalizeClass();

        DQ_QuantitativeResult dqQuantitativeResult = new DQ_QuantitativeResult();
        dqQuantitativeResult.addResultScope(mdScopeAttribute);
        dqQuantitativeResult.addDateTime(now);
        dqQuantitativeResult.addValue(record);
        dqQuantitativeResult.addValueUnit("number of geometries");
        dqQuantitativeResult.finalizeClass();

        DQ_Representativity dqRepresentativity = new DQ_Representativity();
        dqRepresentativity.addMeasure(dqMeasureReference);
        dqRepresentativity.addEvaluationMethod(dqEvaluationMethod);
        dqRepresentativity.addResult(dqQuantitativeResult);
        dqRepresentativity.finalizeClass();

        return dqRepresentativity;
    }

    static DQ_Representativity makeDQRepresentativityTemporal(String nameAttribute, int numDataActual, String now) {
        // instantiate DQ_Representativity class for number of different years
        DQ_MeasureReference dqMeasureReference = new DQ_MeasureReference();
        dqMeasureReference.addNameOfMeasure("number of different temporal elements");
        dqMeasureReference.addMeasureDescription("Number of different years at given attribute. Different years do not necessarily have be consecutive.");
        dqMeasureReference.finalizeClass();

        DQ_EvaluationMethod dqEvaluationMethod = new DQ_FullInspection();
        dqEvaluationMethod.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
        dqEvaluationMethod.addDateTime(now);
        dqEvaluationMethod.addEvaluationMethodDescription("number of different years at given attribute");
        dqEvaluationMethod.finalizeClass();

        MD_ScopeDescription mdScopeDescription = new MD_ScopeDescription();
        mdScopeDescription.addAttributes(nameAttribute);
        mdScopeDescription.finalizeClass();

        MD_Scope mdScopeAttribute = new MD_Scope();
        mdScopeAttribute.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.attribute));
        mdScopeAttribute.addLevelDescription(mdScopeDescription);
        mdScopeAttribute.finalizeClass();

        String stringNumDataActual;
        stringNumDataActual = Integer.toString(numDataActual);

        Record record = new Record();
        record.addField(stringNumDataActual);
        record.finalizeClass();

        DQ_QuantitativeResult dqQuantitativeResult = new DQ_QuantitativeResult();
        dqQuantitativeResult.addResultScope(mdScopeAttribute);
        dqQuantitativeResult.addDateTime(now);
        dqQuantitativeResult.addValue(record);
        dqQuantitativeResult.addValueUnit("number of years");
        dqQuantitativeResult.finalizeClass();

        DQ_Representativity dqRepresentativity = new DQ_Representativity();
        dqRepresentativity.addMeasure(dqMeasureReference);
        dqRepresentativity.addEvaluationMethod(dqEvaluationMethod);
        dqRepresentativity.addResult(dqQuantitativeResult);
        dqRepresentativity.finalizeClass();

        return dqRepresentativity;
    }

    static DQ_Representativity makeDQRepresentativityThematic(String nameAttribute, int numDataActual, String now) {
        // instantiate DQ_Representativity class for number of different commodities
        DQ_MeasureReference dqMeasureReference = new DQ_MeasureReference();
        dqMeasureReference.addNameOfMeasure("number of different commodity elements");
        dqMeasureReference.addMeasureDescription("Number of different commodity elements at given attribute. All commodity elements are equally interpreted");
        dqMeasureReference.finalizeClass();

        DQ_EvaluationMethod dqEvaluationMethod = new DQ_FullInspection();
        dqEvaluationMethod.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
        dqEvaluationMethod.addDateTime(now);
        dqEvaluationMethod.addEvaluationMethodDescription("number of different commodity elements at given attribute");
        dqEvaluationMethod.finalizeClass();

        MD_ScopeDescription mdScopeDescription = new MD_ScopeDescription();
        mdScopeDescription.addAttributes(nameAttribute);
        mdScopeDescription.finalizeClass();

        MD_Scope mdScopeAttribute = new MD_Scope();
        mdScopeAttribute.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.attribute));
        mdScopeAttribute.addLevelDescription(mdScopeDescription);
        mdScopeAttribute.finalizeClass();

        String stringNumDataActual;
        stringNumDataActual = Integer.toString(numDataActual);

        Record record = new Record();
        record.addField(stringNumDataActual);
        record.finalizeClass();

        DQ_QuantitativeResult dqQuantitativeResult = new DQ_QuantitativeResult();
        dqQuantitativeResult.addResultScope(mdScopeAttribute);
        dqQuantitativeResult.addDateTime(now);
        dqQuantitativeResult.addValue(record);
        dqQuantitativeResult.addValueUnit("number of commodity elements");
        dqQuantitativeResult.finalizeClass();

        DQ_Representativity dqRepresentativity = new DQ_Representativity();
        dqRepresentativity.addMeasure(dqMeasureReference);
        dqRepresentativity.addEvaluationMethod(dqEvaluationMethod);
        dqRepresentativity.addResult(dqQuantitativeResult);
        dqRepresentativity.finalizeClass();

        return dqRepresentativity;
    }

    static DQ_Representativity makeDQRepresentativityAdditional(String nameAttribute, String addName, int numDataActual, String now) {
        // instantiate DQ_Representativity class for number of different additional dimension
        DQ_MeasureReference dqMeasureReference = new DQ_MeasureReference();
        dqMeasureReference.addNameOfMeasure("number of different elements in additional dimension (" + addName + ")");
        dqMeasureReference.addMeasureDescription("Number of different elements in additional dimension (" + addName + ") at given attribute. All elements in additional dimension are equally interpreted");
        dqMeasureReference.finalizeClass();

        DQ_EvaluationMethod dqEvaluationMethod = new DQ_FullInspection();
        dqEvaluationMethod.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
        dqEvaluationMethod.addDateTime(now);
        dqEvaluationMethod.addEvaluationMethodDescription("number of different elements in additional dimension (" + addName + ") at given attribute");
        dqEvaluationMethod.finalizeClass();

        MD_ScopeDescription mdScopeDescription = new MD_ScopeDescription();
        mdScopeDescription.addAttributes(nameAttribute);
        mdScopeDescription.finalizeClass();

        MD_Scope mdScopeAttribute = new MD_Scope();
        mdScopeAttribute.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.attribute));
        mdScopeAttribute.addLevelDescription(mdScopeDescription);
        mdScopeAttribute.finalizeClass();

        String stringNumDataActual;
        stringNumDataActual = Integer.toString(numDataActual);

        Record record = new Record();
        record.addField(stringNumDataActual);
        record.finalizeClass();

        DQ_QuantitativeResult dqQuantitativeResult = new DQ_QuantitativeResult();
        dqQuantitativeResult.addResultScope(mdScopeAttribute);
        dqQuantitativeResult.addDateTime(now);
        dqQuantitativeResult.addValue(record);
        dqQuantitativeResult.addValueUnit("number of elements in additional dimension (" + addName + ")");
        dqQuantitativeResult.finalizeClass();

        DQ_Representativity dqRepresentativity = new DQ_Representativity();
        dqRepresentativity.addMeasure(dqMeasureReference);
        dqRepresentativity.addEvaluationMethod(dqEvaluationMethod);
        dqRepresentativity.addResult(dqQuantitativeResult);
        dqRepresentativity.finalizeClass();

        return dqRepresentativity;
    }

    static DQ_Representativity makeDQRepresentativityParamTempPerGeo(String nameAttribute, List<EmpiricalDistributionProperty> temporalPerGeo, String now) {
        // instantiate DQ_Representativity class for distribution parameters of temporal units per geographic units
        DQ_MeasureReference dqMeasureReference = new DQ_MeasureReference();
        dqMeasureReference.addNameOfMeasure("distribution parameters of different temporal elements per geographical unit");
        dqMeasureReference.addMeasureDescription("Parameters for distribution of number of different temporal elements per geographic unit at given attribute.");
        dqMeasureReference.finalizeClass();

        DQ_EvaluationMethod dqEvaluationMethod = new DQ_FullInspection();
        dqEvaluationMethod.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
        dqEvaluationMethod.addDateTime(now);
        dqEvaluationMethod.addEvaluationMethodDescription("distribution parameters of different temporal elements per geographical unit at given attribute");
        dqEvaluationMethod.finalizeClass();

        MD_ScopeDescription mdScopeDescription = new MD_ScopeDescription();
        mdScopeDescription.addAttributes(nameAttribute);
        mdScopeDescription.finalizeClass();

        MD_Scope mdScopeAttribute = new MD_Scope();
        mdScopeAttribute.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.attribute));
        mdScopeAttribute.addLevelDescription(mdScopeDescription);
        mdScopeAttribute.finalizeClass();

        Record record = new Record();
        for (EmpiricalDistributionProperty tmp : temporalPerGeo) {
            record.addField(tmp.propertyName, Double.toString(tmp.value));
        }
        record.finalizeClass();

        DQ_QuantitativeResult dqQuantitativeResult = new DQ_QuantitativeResult();
        dqQuantitativeResult.addResultScope(mdScopeAttribute);
        dqQuantitativeResult.addDateTime(now);
        dqQuantitativeResult.addValue(record);
        dqQuantitativeResult.addValueUnit("number of temporal elements per geographical unit");
        dqQuantitativeResult.finalizeClass();

        DQ_Representativity dqRepresentativity = new DQ_Representativity();
        dqRepresentativity.addMeasure(dqMeasureReference);
        dqRepresentativity.addEvaluationMethod(dqEvaluationMethod);
        dqRepresentativity.addResult(dqQuantitativeResult);
        dqRepresentativity.finalizeClass();

        return dqRepresentativity;
    }

    static DQ_Representativity makeDQRepresentativityParamThematicPerGeo(String nameAttribute, List<EmpiricalDistributionProperty> thematicPerGeo, String now) {
        // instantiate DQ_Representativity class for distribution parameters of thematic units per geographic units
        DQ_MeasureReference dqMeasureReference = new DQ_MeasureReference();
        dqMeasureReference.addNameOfMeasure("distribution parameters of different thematic elements per geographical unit");
        dqMeasureReference.addMeasureDescription("Parameters for distribution of number of different thematic elements per geographic unit at given attribute.");
        dqMeasureReference.finalizeClass();

        DQ_EvaluationMethod dqEvaluationMethod = new DQ_FullInspection();
        dqEvaluationMethod.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
        dqEvaluationMethod.addDateTime(now);
        dqEvaluationMethod.addEvaluationMethodDescription("distribution parameters of different thematic elements per geographical unit at given attribute");
        dqEvaluationMethod.finalizeClass();

        MD_ScopeDescription mdScopeDescription = new MD_ScopeDescription();
        mdScopeDescription.addAttributes(nameAttribute);
        mdScopeDescription.finalizeClass();

        MD_Scope mdScopeAttribute = new MD_Scope();
        mdScopeAttribute.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.attribute));
        mdScopeAttribute.addLevelDescription(mdScopeDescription);
        mdScopeAttribute.finalizeClass();

        Record record = new Record();
        for (EmpiricalDistributionProperty tmp : thematicPerGeo) {
            record.addField(tmp.propertyName, Double.toString(tmp.value));
        }
        record.finalizeClass();

        DQ_QuantitativeResult dqQuantitativeResult = new DQ_QuantitativeResult();
        dqQuantitativeResult.addResultScope(mdScopeAttribute);
        dqQuantitativeResult.addDateTime(now);
        dqQuantitativeResult.addValue(record);
        dqQuantitativeResult.addValueUnit("number of thematic elements per geographical unit");
        dqQuantitativeResult.finalizeClass();

        DQ_Representativity dqRepresentativity = new DQ_Representativity();
        dqRepresentativity.addMeasure(dqMeasureReference);
        dqRepresentativity.addEvaluationMethod(dqEvaluationMethod);
        dqRepresentativity.addResult(dqQuantitativeResult);
        dqRepresentativity.finalizeClass();

        return dqRepresentativity;
    }

    static DQ_Representativity makeDQRepresentativityParamThematicPerTemp(String nameAttribute, List<EmpiricalDistributionProperty> thematicPerTemp, String now) {
        // instantiate DQ_Representativity class for distribution parameters of thematic units per temporal units
        DQ_MeasureReference dqMeasureReference = new DQ_MeasureReference();
        dqMeasureReference.addNameOfMeasure("distribution parameters of different thematic elements per temporal unit");
        dqMeasureReference.addMeasureDescription("Parameters for distribution of number of different thematic elements per temporal unit at given attribute.");
        dqMeasureReference.finalizeClass();

        DQ_EvaluationMethod dqEvaluationMethod = new DQ_FullInspection();
        dqEvaluationMethod.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
        dqEvaluationMethod.addDateTime(now);
        dqEvaluationMethod.addEvaluationMethodDescription("distribution parameters of different thematic elements per temporal unit at given attribute");
        dqEvaluationMethod.finalizeClass();

        MD_ScopeDescription mdScopeDescription = new MD_ScopeDescription();
        mdScopeDescription.addAttributes(nameAttribute);
        mdScopeDescription.finalizeClass();

        MD_Scope mdScopeAttribute = new MD_Scope();
        mdScopeAttribute.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.attribute));
        mdScopeAttribute.addLevelDescription(mdScopeDescription);
        mdScopeAttribute.finalizeClass();

        Record record = new Record();
        for (EmpiricalDistributionProperty tmp : thematicPerTemp) {
            record.addField(tmp.propertyName, Double.toString(tmp.value));
        }
        record.finalizeClass();

        DQ_QuantitativeResult dqQuantitativeResult = new DQ_QuantitativeResult();
        dqQuantitativeResult.addResultScope(mdScopeAttribute);
        dqQuantitativeResult.addDateTime(now);
        dqQuantitativeResult.addValue(record);
        dqQuantitativeResult.addValueUnit("number of thematic elements per temporal unit");
        dqQuantitativeResult.finalizeClass();

        DQ_Representativity dqRepresentativity = new DQ_Representativity();
        dqRepresentativity.addMeasure(dqMeasureReference);
        dqRepresentativity.addEvaluationMethod(dqEvaluationMethod);
        dqRepresentativity.addResult(dqQuantitativeResult);
        dqRepresentativity.finalizeClass();

        return dqRepresentativity;
    }

    DQ_FormatConsistency makeDQFormatConsistency(List<String> allowedFileFormat, String now) {
        // adherence to data format given, if tif available in properties.allowedFileFormat

        StringBuilder evaluationTitle = new StringBuilder();
        evaluationTitle.append("Allowed file formats: ");
        for (int i = 0; i < allowedFileFormat.size(); i++) {
            evaluationTitle.append(allowedFileFormat.get(i));
            if (i != allowedFileFormat.size() - 1) {
                evaluationTitle.append(", ");
            }
        }

        DQ_EvaluationMethod dqEvaluationMethod = new DQ_FullInspection();
        dqEvaluationMethod.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
        dqEvaluationMethod.addDateTime(now);
        dqEvaluationMethod.addEvaluationMethodDescription(evaluationTitle.toString());
        dqEvaluationMethod.finalizeClass();

        DQ_ConformanceResult dqConformanceResult = new DQ_ConformanceResult();
        dqConformanceResult.addPass(allowedFileFormat.stream().anyMatch("csv"::equalsIgnoreCase));
        dqConformanceResult.finalizeClass();

        DQ_FormatConsistency dqFormatConsistency = new DQ_FormatConsistency();
        dqFormatConsistency.addEvaluationMethod(dqEvaluationMethod);
        dqFormatConsistency.addResult(dqConformanceResult);
        dqFormatConsistency.finalizeClass();

        return dqFormatConsistency;
    }

    DQ_TemporalConsistency makeDQTemporalConsistency(String nameAttribute, Boolean regularity, String now) {
        // correctness of ordered events or sequences

        DQ_EvaluationMethod dqEvaluationMethod = new DQ_FullInspection();
        dqEvaluationMethod.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
        dqEvaluationMethod.addDateTime(now);
        dqEvaluationMethod.addEvaluationMethodDescription("Correct order of events; time steps regularly distributed");
        dqEvaluationMethod.finalizeClass();

        MD_ScopeDescription mdScopeDescription = new MD_ScopeDescription();
        mdScopeDescription.addAttributes(nameAttribute);
        mdScopeDescription.finalizeClass();

        MD_Scope mdScopeAttribute = new MD_Scope();
        mdScopeAttribute.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.attribute));
        mdScopeAttribute.addLevelDescription(mdScopeDescription);
        mdScopeAttribute.finalizeClass();

        DQ_ConformanceResult dqConformanceResult = new DQ_ConformanceResult();
        dqConformanceResult.addResultScope(mdScopeAttribute);
        dqConformanceResult.addPass(regularity);
        dqConformanceResult.finalizeClass();

        DQ_TemporalConsistency dqTemporalConsistency = new DQ_TemporalConsistency();
        dqTemporalConsistency.addEvaluationMethod(dqEvaluationMethod);
        dqTemporalConsistency.addResult(dqConformanceResult);
        dqTemporalConsistency.finalizeClass();

        return dqTemporalConsistency;
    }

    DQ_NonQuantitativeAttributeCorrectness makeDQNonQuantitativeAttributeCorrectnessCount(String nameAttribute, Integer[] mappability, String now) {
        // number of incorrect commodities
        // mappability contains (0) overall number and (1) count of mappable entries

        DQ_MeasureReference dqMeasureReference = new DQ_MeasureReference();
        dqMeasureReference.addNameOfMeasure("number of incorrect attribute values");
        dqMeasureReference.addMeasureDescription("count of all attribute values where the value is incorrect (wrong name)");
        dqMeasureReference.finalizeClass();

        DQ_EvaluationMethod dqEvaluationMethod = new DQ_FullInspection();
        dqEvaluationMethod.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
        dqEvaluationMethod.addDateTime(now);
        dqEvaluationMethod.addEvaluationMethodDescription("count of all attribute values where the value is incorrect (wrong name)");
        dqEvaluationMethod.finalizeClass();

        MD_ScopeDescription mdScopeDescription = new MD_ScopeDescription();
        mdScopeDescription.addAttributes(nameAttribute);
        mdScopeDescription.finalizeClass();

        MD_Scope mdScopeAttribute = new MD_Scope();
        mdScopeAttribute.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.attribute));
        mdScopeAttribute.addLevelDescription(mdScopeDescription);
        mdScopeAttribute.finalizeClass();

        Record record = new Record();
        record.addField("" + (mappability[0] - mappability[1]));
        record.finalizeClass();

        DQ_QuantitativeResult dqQuantitativeResult = new DQ_QuantitativeResult();
        dqQuantitativeResult.addResultScope(mdScopeAttribute);
        dqQuantitativeResult.addValue(record);
        dqQuantitativeResult.addValueUnit("count of incorrect attribute values");
        dqQuantitativeResult.finalizeClass();

        DQ_NonQuantitativeAttributeCorrectness dqNonQuantitativeAttributeCorrectness = new DQ_NonQuantitativeAttributeCorrectness();
        dqNonQuantitativeAttributeCorrectness.addMeasure(dqMeasureReference);
        dqNonQuantitativeAttributeCorrectness.addEvaluationMethod(dqEvaluationMethod);
        dqNonQuantitativeAttributeCorrectness.addResult(dqQuantitativeResult);
        dqNonQuantitativeAttributeCorrectness.finalizeClass();

        return dqNonQuantitativeAttributeCorrectness;
    }

    DQ_NonQuantitativeAttributeCorrectness makeDQNonQuantitativeAttributeCorrectnessRate(String nameAttribute, Integer[] mappability, String now) {
        // rate of incorrect commodities
        // mappability contains (0) overall number and (1) count of mappable entries

        DQ_MeasureReference dqMeasureReference = new DQ_MeasureReference();
        dqMeasureReference.addNameOfMeasure("rate of incorrect attribute values");
        dqMeasureReference.addMeasureDescription("rate of all attribute values where the value is incorrect (wrong name)");
        dqMeasureReference.finalizeClass();

        DQ_EvaluationMethod dqEvaluationMethod = new DQ_FullInspection();
        dqEvaluationMethod.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
        dqEvaluationMethod.addDateTime(now);
        dqEvaluationMethod.addEvaluationMethodDescription("rate of all attribute values where the value is incorrect (wrong name)");
        dqEvaluationMethod.finalizeClass();

        MD_ScopeDescription mdScopeDescription = new MD_ScopeDescription();
        mdScopeDescription.addAttributes(nameAttribute);
        mdScopeDescription.finalizeClass();

        MD_Scope mdScopeAttribute = new MD_Scope();
        mdScopeAttribute.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.attribute));
        mdScopeAttribute.addLevelDescription(mdScopeDescription);
        mdScopeAttribute.finalizeClass();

        Record record = new Record();
        record.addField("" + ((double) (mappability[0] - mappability[1]) / (double) mappability[0]) * 100);
        record.finalizeClass();

        DQ_QuantitativeResult dqQuantitativeResult = new DQ_QuantitativeResult();
        dqQuantitativeResult.addResultScope(mdScopeAttribute);
        dqQuantitativeResult.addValue(record);
        dqQuantitativeResult.addValueUnit("%");
        dqQuantitativeResult.finalizeClass();

        DQ_NonQuantitativeAttributeCorrectness dqNonQuantitativeAttributeCorrectness = new DQ_NonQuantitativeAttributeCorrectness();
        dqNonQuantitativeAttributeCorrectness.addMeasure(dqMeasureReference);
        dqNonQuantitativeAttributeCorrectness.addEvaluationMethod(dqEvaluationMethod);
        dqNonQuantitativeAttributeCorrectness.addResult(dqQuantitativeResult);
        dqNonQuantitativeAttributeCorrectness.finalizeClass();

        return dqNonQuantitativeAttributeCorrectness;
    }

    Integer[] evaluateCommoditiesMappingPossibility(List<String> commoditiesAssessmentCols) {
        // evaluate the possibility of mapping
        // output: (0) overall count of values in commoditiesAssessmentCols
        //         (1) number of mappable entries

        Integer[] mappability = new Integer[2];
        mappability[0] = commoditiesAssessmentCols.size();
        mappability[1] = 0;
        for (String tmp : commoditiesAssessmentCols) {
            if (thematicMappingDictionary.get(tmp) != null) {
                mappability[1] = mappability[1] + 1;
            }
        }

        return mappability;
    }

    static List<String> getListFromLogicalIndex(List<String> vals, boolean[] idx) {
        // enable logical indexing of one dimensional array
        // vals is a list with arrays in each list element; number of elements must equal length of array idx

        List<String> valsExtracted = new ArrayList<>();
        for (int i = 0; i < idx.length; i++) {
            if (idx[i]) {
                valsExtracted.add(vals.get(i));
            }
        }

        return valsExtracted;
    }

    static double getMean(int[] values) {
        // calculate average of an integer list

        double sum = 0;
        for (int tmp : values) {
            sum += tmp;
        }
        return sum / values.length;
    }

    static double getQuantile(int[] values, double p) {
        // calculate empirical quantile of an integer list
        // quantiles are given as a fraction of 1 (e.g., 0.5 for median)
        // quantiles are calculated following Matlab method:
        // values in array get the empirical probabilities: p_i = 1/n * (i - .5)
        // p within the range of p_i lead to quantile values from piecewise linear interpolation between adjacent values
        // p out of the range of p_i give min/max of values

        int n = values.length;
        Arrays.sort(values);
        double[] pVals = new double[n];
        for (int i = 1; i <= n; i++) {
            pVals[i - 1] = (i - .5) / n;
        }

        double quantile;
        if (values.length == 1) {
            // if values consists of one entry only -> always return this value
            quantile = values[0];
        } else if (p <= pVals[0]) {
            // below minimal p return minimum of value array
            quantile = values[0];
        } else if (p >= pVals[n - 1]) {
            // above maximal p return maximum of value array
            quantile = values[n - 1];
        } else {
            // within p range -> piecewise interpolation
            int rankLower = (int) Math.floor(n*p + .5) - 1; // 0-based ranks
            int rankUpper = rankLower + 1;
            quantile = values[rankLower] + (values[rankUpper] - values[rankLower]) * (p - pVals[rankLower]) / (pVals[rankUpper] - pVals[rankLower]);
        }

        return quantile;
    }

    static boolean anyTrue(List<Boolean> list) {
        // true if any true element in list, false otherwise

        for (Boolean tmp : list) {
            if (tmp) {
                return true;
            }
        }
        return false;
    }

    static boolean allIdentical(int[] array, int target) {
        // true if all values in array equal to target

        for (int tmp : array) {
            if (tmp != target) {
                return false;
            }
        }
        return true;
    }

    static List<String> applyDictionary(List<String> list, HashMap<String, String> dictionary) {
        // apply hashmap dictionary on list of strings - mapping

        List<String> out = new ArrayList<>();
        for (String tmp : list) {
            out.add(dictionary.get(tmp));
        }

        return out;
    }

    TemporalRegularity getRegularity(List<String> list) {
        // test of regularity of strings - only possible for date in integer form
        // TODO: make datetime given as ZonedDateTime available

        TemporalRegularity tr = new TemporalRegularity();

        if (list.size()==0) {
            // no element included - no regularity
            tr.regularity = false;
            tr.resolution = -999;
            tr.begin = -999;
            tr.end = -999;
            return tr;
        }

        try {
            List<Integer> listInt = new ArrayList<>();
            for (String tmp : list) {
                int tmpInt = Integer.parseInt(tmp);
                listInt.add(tmpInt);
            }

            int n = listInt.size();

            int resolution = listInt.get(1) - listInt.get(0);
            for (int i = 2; i < n; i++) {
                int tmp = listInt.get(i) - listInt.get(i - 1);
                if (tmp != resolution) {
                    // no constant resolution
                    tr.regularity = false;
                    tr.resolution = -999;
                    tr.begin = Collections.min(listInt);
                    tr.end = Collections.max(listInt);
                    return tr;
                }
            }

            tr.begin = Collections.min(listInt);
            tr.end = Collections.max(listInt);
            tr.resolution = resolution;
            tr.regularity = true;

        } catch (NumberFormatException e) {
            tr.regularity = false;
            tr.resolution = -999;
            tr.begin = -999;
            tr.end = -999;
        }

        return tr;
    }
}


class CSVReader {
    // class includes interpretation of one header line
    String fileName;
    String splitChar;
    List<String> colNamesJoin;
    List<String> colNamesDefine;
    List<String> colNamesIgnore;

    List<String> header = new ArrayList<>();
    List<String[]> fileContentJoin = new ArrayList<>();
    List<String[]> fileContentDefine = new ArrayList<>();
    List<String> headerAssessment = new ArrayList<>();
    List<String[]> fileContentAssessment = new ArrayList<>();

    CSVReader(String fileName, String splitChar, List<String> colNamesJoin, List<String> colNamesDefine,
            List<String> colNamesIgnore) {
        this.fileName = fileName;
        this.splitChar = splitChar;
        this.colNamesJoin = colNamesJoin;
        this.colNamesDefine = colNamesDefine;
        this.colNamesIgnore = colNamesIgnore;
    }

    void getContent() {
        List<String> colNamesDefining = Stream.concat(colNamesJoin.stream(), colNamesDefine.stream()).collect(Collectors.toList());

        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            // read header line
            header = Arrays.asList(br.readLine().split(splitChar));

            List<Integer> idxColsJoin = new ArrayList<>();
            for (String colName : colNamesJoin) {
                idxColsJoin.add(header.indexOf(colName));
            }
            List<Integer> idxColsDefine = new ArrayList<>();
            for (String colName : colNamesDefine) {
                idxColsDefine.add(header.indexOf(colName));
            }
            List<Integer> idxColsAssessment = new ArrayList<>();
            for (int i = 0; i < header.size(); i++) {
                if (!colNamesDefining.contains(header.get(i)) && !colNamesIgnore.contains(header.get(i))) {
                    idxColsAssessment.add(i);
                    headerAssessment.add(header.get(i));
                }
            }

            while ((line = br.readLine()) != null) {
                String[] lineParted = line.split(splitChar, -1);
                String[] linePartedJoin = new String[colNamesJoin.size()];
                String[] linePartedDefine = new String[colNamesDefine.size()];
                String[] linePartedAssessment = new String[idxColsAssessment.size()];
                for (int i = 0; i < colNamesJoin.size(); i++) {
                    linePartedJoin[i] = lineParted[idxColsJoin.get(i)];
                }
                for (int i = 0; i < colNamesDefine.size(); i++) {
                    linePartedDefine[i] = lineParted[idxColsDefine.get(i)];
                }
                for (int i = 0; i < idxColsAssessment.size(); i++) {
                    linePartedAssessment[i] = lineParted[idxColsAssessment.get(i)];
                }
                fileContentJoin.add(linePartedJoin);
                fileContentDefine.add(linePartedDefine);
                fileContentAssessment.add(linePartedAssessment);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

//    int parseIntFromString(String content) {
//        // parse integer from string including empty strings
//        int value;
//        if (content.isBlank()) {
//            value = -999;
//        } else {
//            value = Integer.parseInt(content);
//        }
//        return value;
//    }
//
//    double parseDoubleFromString(String content) {
//        // parse integer from string including empty strings
//        double value;
//        if (content.isBlank()) {
//            value = -999.0;
//        } else {
//            value = Double.parseDouble(content);
//        }
//        return value;
//    }
}


class CsvMaskedContent {
    List<String> geographic;
    List<String> temporal;
    List<String> thematic;
    List<String[]> values;
    boolean[][] idxValues;

    CsvMaskedContent(List<String> geographic, List<String> temporal, List<String> thematic, List<String[]> values, boolean[][] idxValues) {
        this.geographic = geographic;
        this.temporal = temporal;
        this.thematic = thematic;
        this.values = values;
        this.idxValues = idxValues;
    }

    List<List<EmpiricalDistributionProperty>> getTemporalPerGeo() {
        // get statistical parameters of temporal units per geographical units for each attribute in this.values

        List<List<EmpiricalDistributionProperty>> temporalPerGeo = new ArrayList<>();
        List<String> geographicUnique = AsciiMetadata.getStringListUniqueMembers(geographic);

        int numGeographic = geographicUnique.size();
        int numFeatures = geographic.size();
        int numValueEntities = idxValues.length;

        boolean[][] idxGeographic = new boolean[numGeographic][numFeatures];
        for (int i = 0; i < numGeographic; i++) {
            // logical index of geographical features
            Arrays.fill(idxGeographic[i], false);
            for (int j = 0; j < numFeatures; j++) {
                if (geographic.get(j).equals(geographicUnique.get(i))) {
                    idxGeographic[i][j] = true;
                }
            }
        }

        int[][] numTemporal = new int[numValueEntities][numGeographic];
        // 2-D representation of years within numGeographic (inner order) and numFeatures (outer order)
//        List<List<String>> years = new ArrayList<>();

        for (int i = 0; i < numValueEntities; i++) {
            for (int j = 0; j < numGeographic; j++) {
                List<String> tmp = new ArrayList<>();
                for (int k = 0; k < numFeatures; k++) {
                    if (idxGeographic[j][k] && idxValues[i][k]) {
                        tmp.add(temporal.get(k));
                    }
                }
//                years.add(tmp);
                numTemporal[i][j] = AsciiMetadata.getStringListUniqueMembers(tmp).size();
            }
        }

        for (int i = 0; i < numValueEntities; i++) {
            List<EmpiricalDistributionProperty> tmp = new ArrayList<>();
            tmp.add(new EmpiricalDistributionProperty("mean", AsciiMetadata.getMean(numTemporal[i])));
            tmp.add(new EmpiricalDistributionProperty("min", AsciiMetadata.getQuantile(numTemporal[i], 0)));
            tmp.add(new EmpiricalDistributionProperty("5 % quantile", AsciiMetadata.getQuantile(numTemporal[i], .05)));
            tmp.add(new EmpiricalDistributionProperty("25 % quantile", AsciiMetadata.getQuantile(numTemporal[i], .25)));
            tmp.add(new EmpiricalDistributionProperty("50 % quantile", AsciiMetadata.getQuantile(numTemporal[i], .5)));
            tmp.add(new EmpiricalDistributionProperty("75 % quantile", AsciiMetadata.getQuantile(numTemporal[i], .75)));
            tmp.add(new EmpiricalDistributionProperty("95 % quantile", AsciiMetadata.getQuantile(numTemporal[i], .95)));
            tmp.add(new EmpiricalDistributionProperty("max", AsciiMetadata.getQuantile(numTemporal[i], 1)));

            temporalPerGeo.add(tmp);
        }

        return temporalPerGeo;
    }

    List<List<EmpiricalDistributionProperty>> getThematicPerGeo() {
        // get statistical parameters of thematic units per geographical units for each attribute in this.values

        List<List<EmpiricalDistributionProperty>> thematicPerGeo = new ArrayList<>();
        List<String> geographicUnique = AsciiMetadata.getStringListUniqueMembers(geographic);

        int numGeographic = geographicUnique.size();
        int numFeatures = geographic.size();
        int numValueEntities = idxValues.length;

        boolean[][] idxGeographic = new boolean[numGeographic][numFeatures];
        for (int i = 0; i < numGeographic; i++) {
            // logical index of geographical features
            Arrays.fill(idxGeographic[i], false);
            for (int j = 0; j < numFeatures; j++) {
                if (geographic.get(j).equals(geographicUnique.get(i))) {
                    idxGeographic[i][j] = true;
                }
            }
        }

        int[][] numThematic = new int[numValueEntities][numGeographic];
        // 2-D representation of commodities within numGeographic (inner order) and numFeatures (outer order)

        for (int i = 0; i < numValueEntities; i++) {
            for (int j = 0; j < numGeographic; j++) {
                List<String> tmp = new ArrayList<>();
                for (int k = 0; k < numFeatures; k++) {
                    if (idxGeographic[j][k] && idxValues[i][k]) {
                        tmp.add(thematic.get(k));
                    }
                }
                numThematic[i][j] = AsciiMetadata.getStringListUniqueMembers(tmp).size();
            }
        }

        for (int i = 0; i < numValueEntities; i++) {
            List<EmpiricalDistributionProperty> tmp = new ArrayList<>();
            tmp.add(new EmpiricalDistributionProperty("mean", AsciiMetadata.getMean(numThematic[i])));
            tmp.add(new EmpiricalDistributionProperty("min", AsciiMetadata.getQuantile(numThematic[i], 0)));
            tmp.add(new EmpiricalDistributionProperty("5 % quantile", AsciiMetadata.getQuantile(numThematic[i], .05)));
            tmp.add(new EmpiricalDistributionProperty("25 % quantile", AsciiMetadata.getQuantile(numThematic[i], .25)));
            tmp.add(new EmpiricalDistributionProperty("50 % quantile", AsciiMetadata.getQuantile(numThematic[i], .5)));
            tmp.add(new EmpiricalDistributionProperty("75 % quantile", AsciiMetadata.getQuantile(numThematic[i], .75)));
            tmp.add(new EmpiricalDistributionProperty("95 % quantile", AsciiMetadata.getQuantile(numThematic[i], .95)));
            tmp.add(new EmpiricalDistributionProperty("max", AsciiMetadata.getQuantile(numThematic[i], 1)));

            thematicPerGeo.add(tmp);
        }

        return thematicPerGeo;
    }

    List<List<EmpiricalDistributionProperty>> getThematicPerTemp() {
        // get statistical parameters of thematic units per temporal units for each attribute in this.values

        List<List<EmpiricalDistributionProperty>> thematicPerTemp = new ArrayList<>();
        List<String> temporalUnique = AsciiMetadata.getStringListUniqueMembers(temporal);

        int numTemporal = temporalUnique.size();
        int numFeatures = geographic.size();
        int numValueEntities = idxValues.length;

        boolean[][] idxTemporal = new boolean[numTemporal][numFeatures];
        for (int i = 0; i < numTemporal; i++) {
            // logical index of temporal features
            Arrays.fill(idxTemporal[i], false);
            for (int j = 0; j < numFeatures; j++) {
                if (temporal.get(j).equals(temporalUnique.get(i))) {
                    idxTemporal[i][j] = true;
                }
            }
        }

        int[][] numThematic = new int[numValueEntities][numTemporal];
        // 2-D representation of commodities within numTemporal (inner order) and numFeatures (outer order)

        for (int i = 0; i < numValueEntities; i++) {
            for (int j = 0; j < numTemporal; j++) {
                List<String> tmp = new ArrayList<>();
                for (int k = 0; k < numFeatures; k++) {
                    if (idxTemporal[j][k] && idxValues[i][k]) {
                        tmp.add(thematic.get(k));
                    }
                }
                numThematic[i][j] = AsciiMetadata.getStringListUniqueMembers(tmp).size();
            }
        }

        for (int i = 0; i < numValueEntities; i++) {
            List<EmpiricalDistributionProperty> tmp = new ArrayList<>();
            tmp.add(new EmpiricalDistributionProperty("mean", AsciiMetadata.getMean(numThematic[i])));
            tmp.add(new EmpiricalDistributionProperty("min", AsciiMetadata.getQuantile(numThematic[i], 0)));
            tmp.add(new EmpiricalDistributionProperty("5 % quantile", AsciiMetadata.getQuantile(numThematic[i], .05)));
            tmp.add(new EmpiricalDistributionProperty("25 % quantile", AsciiMetadata.getQuantile(numThematic[i], .25)));
            tmp.add(new EmpiricalDistributionProperty("50 % quantile", AsciiMetadata.getQuantile(numThematic[i], .5)));
            tmp.add(new EmpiricalDistributionProperty("75 % quantile", AsciiMetadata.getQuantile(numThematic[i], .75)));
            tmp.add(new EmpiricalDistributionProperty("95 % quantile", AsciiMetadata.getQuantile(numThematic[i], .95)));
            tmp.add(new EmpiricalDistributionProperty("max", AsciiMetadata.getQuantile(numThematic[i], 1)));

            thematicPerTemp.add(tmp);
        }

        return thematicPerTemp;
    }
}


class EmpiricalDistributionProperty {
    final String propertyName;
    final double value;

    public EmpiricalDistributionProperty(String propertyName, double value) {
        this.propertyName = propertyName;
        this.value = value;
    }
}


class TemporalRegularity {
    boolean regularity;
    int begin;
    int end;
    int resolution;

    public TemporalRegularity() {}
}
