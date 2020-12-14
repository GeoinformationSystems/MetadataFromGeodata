/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

import org.geokur.ISO19103Schema.Record;
import org.geokur.ISO19115Schema.*;
import org.geokur.ISO19157Schema.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AsciiMetadata implements Metadata {
    String fileName;
    DS_DataSet dsDataSet;
    String fileNameGeodata;
    List<String> geoTableName;
    List<String> geoColNameJoin;
    List<String> asciiColNameJoin;
    List<String> asciiColNameDefine;
    List<String> asciiColNameIgnore;
    List<String> descriptionAsciiColNameDefine;

    public AsciiMetadata(String fileName, DS_DataSet dsDataSet) {
        this.fileName = fileName;
        this.dsDataSet = dsDataSet;
    }

    public void defineProperties(String fileNameGeodata, List<String> geoTableName, List<String> geoColNameJoin, List<String> asciiColNameJoin,
                                 List<String> asciiColNameDefine, List<String> asciiColNameIgnore, List<String> descriptionAsciiColNameDefine) {
        // definition of additional data necessary for analysis

        this.fileNameGeodata = fileNameGeodata;
        this.geoTableName = geoTableName;
        this.geoColNameJoin = geoColNameJoin;
        this.asciiColNameJoin = asciiColNameJoin;
        this.asciiColNameDefine = asciiColNameDefine;
        this.asciiColNameIgnore = asciiColNameIgnore;
        this.descriptionAsciiColNameDefine = descriptionAsciiColNameDefine;
    }

    public DS_Resource getMetadata() {
        // read ascii file in combination with some geodata and put its metadata into DS_Resource

        //TODO: allow shape file as geodata source

        // open geopackage
        Geopackage gpkg = new Geopackage(fileNameGeodata);
        Connection connection = gpkg.getConnection();
        Statement statement = gpkg.getStatement(connection);

        for (String geoTableNameAct : geoTableName) {

            // read geopackage content
            List<List<String>> geoColContent = new ArrayList<>();
            for (String geoColNameJoinAct : geoColNameJoin) {
                geoColContent.add(gpkg.getTableColContent(statement, geoTableNameAct, geoColNameJoinAct));
            }

            // read all relevant ascii content (concatenate all column names for the sake of performance)
            CSVReader csvContent = new CSVReader(fileName, ",", asciiColNameJoin, asciiColNameDefine, asciiColNameIgnore);
            csvContent.getContent();
            int csvNumAssessment = csvContent.headerAssessment.size();

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
            int numDataAll = csvContentMaskedAssessment.size();
            int[] numDataAssessment = new int[csvNumAssessment];
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
            int[] excessDataAll = new int[csvNumAssessment]; // excess data (0 means no duplicate, 1 means no duplicate)
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
            gpkg.getCenterArea();
            double polygonPerArea;
            if (gpkg.polygonSwitch) {
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

            // get index of either temporal or thematic columns in csvContentMaskedDefine
            int idxTemporal = descriptionAsciiColNameDefine.indexOf("temporal");
            int idxThematic = descriptionAsciiColNameDefine.indexOf("thematic");
            List<String> csvContentMaskedTemporal = new ArrayList<>();
            List<String> csvContentMaskedThematic = new ArrayList<>();
            for (String[] tmp : csvContentMaskedDefine) {
                csvContentMaskedTemporal.add(tmp[idxTemporal]);
                csvContentMaskedThematic.add(tmp[idxThematic]);
            }

            // get number of years per attribute
            List<Integer> numYearsAssessmentCols = new ArrayList<>();
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
            List<List<String>> yearsMaskedAssessment = new ArrayList<>();
            List<List<String>> yearsMaskedAssessmentUnique = new ArrayList<>();
            for (int i = 0; i < csvNumAssessment; i++) {
                // get available years of all columns of masked assessment data
                yearsMaskedAssessment.add(getListFromLogicalIndex(csvContentMaskedTemporal, idxMaskedRelevantAll[i]));
                yearsMaskedAssessmentUnique.add(getStringListUniqueMembers(yearsMaskedAssessment.get(i)));
                numYearsAssessmentCols.add(yearsMaskedAssessmentUnique.get(i).size());
            }

            // get number of commodities per attribute
            List<Integer> numCommoditiesAssessmentCols = new ArrayList<>();
            List<List<String>> commoditiesMaskedAssessment = new ArrayList<>();
            List<List<String>> commoditiesMaskedAssessmentUnique = new ArrayList<>();
            for (int i = 0; i < csvNumAssessment; i++) {
                // get available commodities of all columns of masked assessment data
                commoditiesMaskedAssessment.add(getListFromLogicalIndex(csvContentMaskedThematic, idxMaskedRelevantAll[i]));
                commoditiesMaskedAssessmentUnique.add(getStringListUniqueMembers(commoditiesMaskedAssessment.get(i)));
                numCommoditiesAssessmentCols.add(commoditiesMaskedAssessmentUnique.get(i).size());
            }

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
            List<List<EmpiricalDistributionProperty>> temporalPerGeo = csvMaskedContent.getTemporalPerGeo();

            // get distribution parameters of number of commodities per attribute over all spatial units
            List<List<EmpiricalDistributionProperty>> thematicPerGeo = csvMaskedContent.getThematicPerGeo();

            // get distribution parameters of number of commodities per attribute over all temporal units
            List<List<EmpiricalDistributionProperty>> thematicPerTemp = csvMaskedContent.getThematicPerTemp();



            ///////////////////////////////////
            // Instantiate Metadata Elements //
            ///////////////////////////////////

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

            ZonedDateTime lastModified = ZonedDateTime.ofInstant(Instant.ofEpochMilli(csvFile.lastModified()), ZoneId.systemDefault()); // local timezone
            lastModified = lastModified.withZoneSameInstant(ZoneId.of("UTC")); // convert to UTC timezone
            String lastModifiedString = lastModified.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME); // datetime in ISO 8601 format

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
                    dqCompletenessOmissionsCount.add(makeDQCompletenessOmission(csvContent.headerAssessment.get(i), numDataAll, numDataAssessment[i], "count"));
                }
            }

            // completenessOmission of data in attribute rows - rate of missing data
            List<DQ_CompletenessOmission> dqCompletenessOmissionsRate = new ArrayList<>();
            for (int i = 0; i < csvNumAssessment; i++) {
                if ((numDataAll - numDataAssessment[i]) > 0) {
                    dqCompletenessOmissionsRate.add(makeDQCompletenessOmission(csvContent.headerAssessment.get(i), numDataAll, numDataAssessment[i], "rate"));
                }
            }

            // completenessCommission of data in attribute rows - count of excess items
            List<DQ_CompletenessCommission> dqCompletenessCommissionsCount = new ArrayList<>();
            for (int i = 0; i < csvNumAssessment; i++) {
                if (excessDataAll[i] > 0) {
                    dqCompletenessCommissionsCount.add(makeDQCompletenessCommission(csvContent.headerAssessment.get(i), numDataAll, excessDataAll[i], "count"));
                }
            }

            // completenessCommission of data in attribute rows - rate of excess items
            List<DQ_CompletenessCommission> dqCompletenessCommissionsRate = new ArrayList<>();
            for (int i = 0; i < csvNumAssessment; i++) {
                if (excessDataAll[i] > 0) {
                    dqCompletenessCommissionsRate.add(makeDQCompletenessCommission(csvContent.headerAssessment.get(i), numDataAll, excessDataAll[i], "rate"));
                }
            }

            // metaquality - number of polygons per area
            DQ_Representativity dqRepresentativitySpatial = new DQ_Representativity();
            if (gpkg.polygonSwitch) {
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

            // metaquality - number of different years per attribute
            List<DQ_Representativity> dqRepresentativitiesTemporal = new ArrayList<>();
            for (int i = 0; i < csvNumAssessment; i++) {
                dqRepresentativitiesTemporal.add(makeDQRepresentativityTemporal(csvContent.headerAssessment.get(i), numYearsAssessmentCols.get(i), now));
            }

            // metaquality - number of different commodity elements per attribute
            List<DQ_Representativity> dqRepresentativitiesThematic = new ArrayList<>();
            for (int i = 0; i < csvNumAssessment; i++) {
                dqRepresentativitiesThematic.add(makeDQRepresentativityThematic(csvContent.headerAssessment.get(i), numCommoditiesAssessmentCols.get(i), now));
            }

            // metaquality - distribution parameters of different years per geographical unit per attribute
            List<DQ_Representativity> dqRepresentativitiesTempPerGeo = new ArrayList<>();
            for (int i = 0; i < csvNumAssessment; i++) {
                dqRepresentativitiesTempPerGeo.add(makeDQRepresentativityParamTempPerGeo(csvContent.headerAssessment.get(i), temporalPerGeo.get(i), now));
            }

            // metaquality - distribution parameters of different commodities per geographical unit per attribute
            List<DQ_Representativity> dqRepresentativitiesThematicPerGeo = new ArrayList<>();
            for (int i = 0; i < csvNumAssessment; i++) {
                dqRepresentativitiesThematicPerGeo.add(makeDQRepresentativityParamThematicPerGeo(csvContent.headerAssessment.get(i), thematicPerGeo.get(i), now));
            }

            // metaquality - distribution parameters of different commodities per temporal unit per attribute
            List<DQ_Representativity> dqRepresentativitiesThematicPerTemp = new ArrayList<>();
            for (int i = 0; i < csvNumAssessment; i++) {
                dqRepresentativitiesThematicPerTemp.add(makeDQRepresentativityParamThematicPerTemp(csvContent.headerAssessment.get(i), thematicPerTemp.get(i), now));
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

            if (gpkg.polygonSwitch) {
                dqDataQuality.addReport(dqRepresentativitySpatial);
            }
            for (DQ_Representativity dqRepresentativityTemporal : dqRepresentativitiesTemporal) {
                dqDataQuality.addReport(dqRepresentativityTemporal);
            }
            for (DQ_Representativity dqRepresentativityThematic : dqRepresentativitiesThematic) {
                dqDataQuality.addReport(dqRepresentativityThematic);
            }
            for (DQ_Representativity dqRepresentativityTempPerGeo : dqRepresentativitiesTempPerGeo) {
                dqDataQuality.addReport(dqRepresentativityTempPerGeo);
            }
            for (DQ_Representativity dqRepresentativityThematicPerGeo : dqRepresentativitiesThematicPerGeo) {
                dqDataQuality.addReport(dqRepresentativityThematicPerGeo);
            }
            for (DQ_Representativity dqRepresentativityThematicPerTemp : dqRepresentativitiesThematicPerTemp) {
                dqDataQuality.addReport(dqRepresentativityThematicPerTemp);
            }

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
            mdMetadata.addDateInfo(ciDateLastModified);
            mdMetadata.addIdentificationInfo(mdDataIdentification);
            mdMetadata.addMetadataStandard(ciCitationMetadataStandard);

            mdMetadata.addDataQualityInfo(dqDataQuality);
            mdMetadata.finalizeClass();

            dsDataSet.addHas(mdMetadata);
        }
        dsDataSet.finalizeClass();

        return dsDataSet;
    }


    ////////////////////
    // helper methods //
    ////////////////////

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
        record.addField("value", stringMissing);
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
        record.addField("value", stringMissing);
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
        record.addField("value", stringNumDataActual);
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
        record.addField("value", stringNumDataActual);
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
            tmp.add(new EmpiricalDistributionProperty("mean", getMean(numTemporal[i])));
            tmp.add(new EmpiricalDistributionProperty("min", getQuantile(numTemporal[i], 0)));
            tmp.add(new EmpiricalDistributionProperty("5 % quantile", getQuantile(numTemporal[i], .05)));
            tmp.add(new EmpiricalDistributionProperty("25 % quantile", getQuantile(numTemporal[i], .25)));
            tmp.add(new EmpiricalDistributionProperty("50 % quantile", getQuantile(numTemporal[i], .5)));
            tmp.add(new EmpiricalDistributionProperty("75 % quantile", getQuantile(numTemporal[i], .75)));
            tmp.add(new EmpiricalDistributionProperty("95 % quantile", getQuantile(numTemporal[i], .95)));
            tmp.add(new EmpiricalDistributionProperty("max", getQuantile(numTemporal[i], 1)));

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
            tmp.add(new EmpiricalDistributionProperty("mean", getMean(numThematic[i])));
            tmp.add(new EmpiricalDistributionProperty("min", getQuantile(numThematic[i], 0)));
            tmp.add(new EmpiricalDistributionProperty("5 % quantile", getQuantile(numThematic[i], .05)));
            tmp.add(new EmpiricalDistributionProperty("25 % quantile", getQuantile(numThematic[i], .25)));
            tmp.add(new EmpiricalDistributionProperty("50 % quantile", getQuantile(numThematic[i], .5)));
            tmp.add(new EmpiricalDistributionProperty("75 % quantile", getQuantile(numThematic[i], .75)));
            tmp.add(new EmpiricalDistributionProperty("95 % quantile", getQuantile(numThematic[i], .95)));
            tmp.add(new EmpiricalDistributionProperty("max", getQuantile(numThematic[i], 1)));

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
            tmp.add(new EmpiricalDistributionProperty("mean", getMean(numThematic[i])));
            tmp.add(new EmpiricalDistributionProperty("min", getQuantile(numThematic[i], 0)));
            tmp.add(new EmpiricalDistributionProperty("5 % quantile", getQuantile(numThematic[i], .05)));
            tmp.add(new EmpiricalDistributionProperty("25 % quantile", getQuantile(numThematic[i], .25)));
            tmp.add(new EmpiricalDistributionProperty("50 % quantile", getQuantile(numThematic[i], .5)));
            tmp.add(new EmpiricalDistributionProperty("75 % quantile", getQuantile(numThematic[i], .75)));
            tmp.add(new EmpiricalDistributionProperty("95 % quantile", getQuantile(numThematic[i], .95)));
            tmp.add(new EmpiricalDistributionProperty("max", getQuantile(numThematic[i], 1)));

            thematicPerTemp.add(tmp);
        }

        return thematicPerTemp;
    }

    double getMean(int[] values) {
        // calculate average of an integer list

        double sum = 0;
        for (int tmp : values) {
            sum += tmp;
        }
        return sum / values.length;
    }

    double getQuantile(int[] values, double p) {
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
        } else if (p < pVals[0]) {
            // below minimal p return minimum of value array
            quantile = values[0];
        } else if (p > pVals[n - 1]) {
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
}


class EmpiricalDistributionProperty {
    final String propertyName;
    final double value;

    public EmpiricalDistributionProperty(String propertyName, double value) {
        this.propertyName = propertyName;
        this.value = value;
    }
}
