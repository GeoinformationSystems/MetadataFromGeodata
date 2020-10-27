/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.generateMetadata;

import org.geokur.ISO19103Schema.Record;
import org.geokur.ISO19103Schema.RecordType;
import org.geokur.ISO19115Schema.*;
import org.geokur.ISO19115_2Schema.MI_AcquisitionInformation;
import org.geokur.ISO19115_2Schema.MI_Metadata;
import org.geokur.ISO19115_2Schema.MI_Operation;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GeopackageMetadata implements Metadata {

    String fileName;
    DS_DataSet dsDataSet;

    public GeopackageMetadata(String fileName) {
        this.fileName = fileName;
    }

    public GeopackageMetadata(String fileName, DS_DataSet dsDataSet) {
        this(fileName);
        this.dsDataSet = dsDataSet;
    }

    public Integer getNumGeodata() {
        // get number of datasets in a geopackage (later use in getMetadata)

        GeopackageMetadata gpkg = new GeopackageMetadata(fileName);
        Connection connection = gpkg.getConnection();
        Statement statement = gpkg.getStatement(connection);
        Integer contentCt = 0;

        try {
            ResultSet tableContent = statement.executeQuery("SELECT table_name FROM gpkg_contents");
            while (tableContent.next()) {
                contentCt++;
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return contentCt;
    }

    public DS_Resource getMetadata() {
        // read geopackage file and put its metadata into DS_Resource

        DS_DataSet metadataDataSet = new DS_DataSet();
        int numDataSet = new GeopackageMetadata(fileName).getNumGeodata();
        for (int i = 0; i < numDataSet; i++) {
            System.out.println("--------------------");
            System.out.println("Geopackage content " + i);
            System.out.println("--------------------");
            metadataDataSet = new GeopackageMetadata(fileName, metadataDataSet).getSingleMetadata(i);
        }
        return metadataDataSet;
    }

    public DS_DataSet getSingleMetadata(int contentAct) {
        // read geopackage file and put one of its metadata into DS_Resource
        //
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

        File geopackageFile = new File(fileName);

        // open geopackage as sqlite database
        GeopackageMetadata gpkg = new GeopackageMetadata(fileName);
        Connection connection = gpkg.getConnection();
        Statement statement = gpkg.getStatement(connection);

        // open geopackage with geotools
        Map<String, String> params = new HashMap<>();
        params.put("dbtype", "geopkg");
        params.put("database", geopackageFile.toString());

        CoordinateReferenceSystem srcCRS;
        boolean markerTransform;
        SimpleFeatureCollection collection;
        SimpleFeatureCollection collectionTransform;

        try {
            DataStore dataStore = DataStoreFinder.getDataStore(params);
            String typeName = dataStore.getTypeNames()[contentAct];
            collection = dataStore.getFeatureSource(typeName).getFeatures();
            srcCRS = collection.getSchema().getCoordinateReferenceSystem();

            MathTransform mathTransform = CRS.findMathTransform(srcCRS, DefaultGeographicCRS.WGS84, true);
            // transform to WGS84 for standard extent lon/lat
            if (!mathTransform.isIdentity()) {
                markerTransform = true;
                collectionTransform = project(collection, "epsg:4326");
            } else {
                markerTransform = false;
                collectionTransform = collection;
            }

            // find correct epsg code for existing data
            String authority = "EPSG";
            String srcCRSepsg;
            if (markerTransform) {
                Set<String> supportedCodes = CRS.getSupportedCodes(authority);
                String srcCRSName = srcCRS.getName().toString();

                List<String> matchingEPSGCode = new ArrayList<>();
                List<Integer> matchingLevenshteinDistance = new ArrayList<>();

                System.out.println("Comparison of CRS to find the correct EPSG identifier");
                System.out.print("[          ]");
                int ct = 0;
                int ct2 = 0;
                for (String supportedCode : supportedCodes) {
                    ct++;
                    if (!supportedCode.matches("[0-9]+")) {
                        // only interpret numerical epsg codes (the first supported code might be WGS84 as text)
                        continue;
                    }
                    if ((ct % (supportedCodes.size() / 10)) == 0) {
                        // construct wait bar on console output
                        ct2++;
                        System.out.print("\b\b\b\b\b\b\b\b\b\b\b");
                        for (int i = 1; i <= ct2; i++) {
                            System.out.print("#");
                        }
                        for (int i = ct2 + 1; i <= 10; i++) {
                            System.out.print(" ");
                        }
                        System.out.print("]");
                    }
                    try {
                        CoordinateReferenceSystem actCRS = CRS.decode(authority + ":" + supportedCode);
                        MathTransform mathTransformFind = CRS.findMathTransform(srcCRS, actCRS, true);
                        if (mathTransformFind.isIdentity()) {
                            matchingEPSGCode.add(supportedCode);
                            String actCRSName = actCRS.getName().toString();
                            matchingLevenshteinDistance.add(levenshteinDistance(srcCRSName, actCRSName));
                        }
                    } catch (FactoryException ignored) {
                    }
                }
                System.out.println();
                int idxMatching = matchingLevenshteinDistance.indexOf(Collections.min(matchingLevenshteinDistance));
                srcCRSepsg = matchingEPSGCode.get(idxMatching);
            }
            else {
                // special case for WGS84 - no test of CRS
                srcCRSepsg = "4326";
            }


            // get (1) basic information
            CI_Individual ciIndividual = new CI_Individual();
            ciIndividual.addName(System.getProperty("user.name"));
            ciIndividual.finalizeClass();

            CI_Responsibility ciResponsibility = new CI_Responsibility();
            ciResponsibility.addRole(new CI_RoleCode(CI_RoleCode.CI_RoleCodes.resourceProvider));
            ciResponsibility.addParty(ciIndividual);
            ciResponsibility.finalizeClass();

            String identifierCode = "pid:" + UUID.randomUUID().toString();
            String description = gpkg.getDescription(statement, contentAct);

            MD_Identifier mdIdentifier = new MD_Identifier();
            mdIdentifier.addCode(identifierCode);
            if (description.length() > 0) {
                // add only non empty descriptions
                mdIdentifier.addDescription(description);
            }
            mdIdentifier.finalizeClass();

            Instant lastChange = gpkg.getLastChange(statement, contentAct); // ISO 8601 date

            CI_Date ciDateCreation = new CI_Date();
            ciDateCreation.addDateType(new CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes.creation));
            ciDateCreation.addDate(lastChange.toString());
            ciDateCreation.finalizeClass();

            ZonedDateTime lastModified = ZonedDateTime.ofInstant(Instant.ofEpochMilli(geopackageFile.lastModified()), ZoneId.systemDefault()); // local timezone
            lastModified = lastModified.withZoneSameInstant(ZoneId.of("UTC")); // convert to UTC timezone
            String lastModifiedString = lastModified.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME); // datetime in ISO 8601 format

            CI_Date ciDateLastModified = new CI_Date();
            ciDateLastModified.addDateType(new CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes.lastUpdate));
            ciDateLastModified.addDate(lastModifiedString);
            ciDateLastModified.finalizeClass();


            // get (2) reference system
            Integer srsID = gpkg.getSRSID(statement, contentAct);
            String srsOrganization = gpkg.getSRSOrganization(statement, srsID);
            Integer srsOrganizationCoordsysID = gpkg.getSRSOrganizationCoordsysID(statement, srsID);
            String srsName = gpkg.getSRSName(statement, srsID);

            MD_Identifier mdIdentifier_MD_ReferenceSystem = new MD_Identifier();
            mdIdentifier_MD_ReferenceSystem.addCode(srsOrganization + ":" + srsOrganizationCoordsysID);
            mdIdentifier_MD_ReferenceSystem.addCodeSpace(srsOrganization);
            mdIdentifier_MD_ReferenceSystem.addDescription(srsName);
            mdIdentifier_MD_ReferenceSystem.finalizeClass();


            // get (3) structure of spatial data
            String now = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

            CI_Date ciDate = new CI_Date();
            ciDate.addDateType(new CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes.creation));
            ciDate.addDate(now);
            ciDate.finalizeClass();

            // TODO: informative title available?
            CI_Citation ciCitation = new CI_Citation();
            ciCitation.addTitle("");
            ciCitation.addDate(ciDate);
            ciCitation.finalizeClass();

            String dataType = getDataType(statement, contentAct);
            Extent extentOrigCRS;
            Extent extent;

            MD_DataIdentification mdDataIdentification = new MD_DataIdentification();
            mdDataIdentification.addCitation(ciCitation);

            switch (dataType) {
                case "features":
                    mdDataIdentification.addSpatialRepresentationType(new MD_SpatialRepresentationTypeCode(MD_SpatialRepresentationTypeCode.MD_SpatialRepresentationTypeCodes.vector));

                    // get spatial extent
                    extentOrigCRS = getExtent(collection);
                    extent = getExtent(collectionTransform);

                    break;

                case "2d-gridded-coverage":
                    extentOrigCRS = getExtent(statement, contentAct);
                    extent = extentOrigCRS; // TODO: insert correct transforming of CRS in case of raster data

                    break;
                default:
                    extentOrigCRS = new Extent(-999.0);
                    extent = new Extent(-999.0);
                    break;
            }

            String tableName = gpkg.getTableName(statement, contentAct);
            String environmentalDescription = "file name: " + fileName + "; "
                    + "layer name: " + tableName + "; "
                    + "file size: " + (int) geopackageFile.length() + " B; "
                    + "os: " + System.getProperty("os.name");

            mdDataIdentification.addEnvironmentalDescription(environmentalDescription);

            EX_GeographicBoundingBox exGeographicBoundingBox = new EX_GeographicBoundingBox();
            exGeographicBoundingBox.addWestBoundLongitude(extent.west);
            exGeographicBoundingBox.addEastBoundLongitude(extent.east);
            exGeographicBoundingBox.addSouthBoundLatitude(extent.south);
            exGeographicBoundingBox.addNorthBoundLatitude(extent.north);
            exGeographicBoundingBox.finalizeClass();

            EX_Extent exExtent = new EX_Extent();
            exExtent.addDescription("geographical extent in WGS84, EPSG:4326");
            exExtent.addGeographicElement(exGeographicBoundingBox);
            exExtent.finalizeClass();

            EX_GeographicBoundingBox exGeographicBoundingBoxOrigCRS = new EX_GeographicBoundingBox();
            exGeographicBoundingBoxOrigCRS.addWestBoundLongitude(extentOrigCRS.west);
            exGeographicBoundingBoxOrigCRS.addEastBoundLongitude(extentOrigCRS.east);
            exGeographicBoundingBoxOrigCRS.addSouthBoundLatitude(extentOrigCRS.south);
            exGeographicBoundingBoxOrigCRS.addNorthBoundLatitude(extentOrigCRS.north);
            exGeographicBoundingBoxOrigCRS.finalizeClass();
            
            EX_Extent exExtentOrigCRS = new EX_Extent();
            exExtentOrigCRS.addDescription("geographical extent in data CRS, EPSG:" + srcCRSepsg);
            exExtentOrigCRS.addGeographicElement(exGeographicBoundingBoxOrigCRS);
            exExtentOrigCRS.finalizeClass();

            mdDataIdentification.addExtent(exExtent);
            mdDataIdentification.addExtent(exExtentOrigCRS);
            mdDataIdentification.finalizeClass();


            // get (4) data quality


            // get (5) metadata contact
            CI_Citation ciCitationMetadataStandard = new CI_Citation();
            ciCitationMetadataStandard.addTitle("ISO 19115-1");
            ciCitationMetadataStandard.addEdition("First edition 2014-04-01");
            ciCitationMetadataStandard.finalizeClass();


            // get (6) provenance

//            Record record = new Record();
//            record.createField();
//            record.addField("key 01", "value 01");
//            record.addField("key 02", "value 02");
//            record.finalizeClass();
//
//            RecordType recordType = new RecordType();
//            recordType.createField();
//            recordType.addField("key 01", "String");
//            recordType.addField("key 02", "String");
//            recordType.finalizeClass();
//
//            MI_Operation miOperation = new MI_Operation();
//            miOperation.createOtherProperty();
//            miOperation.addOtherProperty(record);
//            miOperation.createOtherPropertyType();
//            miOperation.addOtherPropertyType(recordType);
//
//            MI_AcquisitionInformation miAcquisitionInformation = new MI_AcquisitionInformation();
//            miAcquisitionInformation.createOperation();
//            miAcquisitionInformation.addOperation(miOperation);
//
//            MI_Metadata miMetadata = new MI_Metadata();
//            miMetadata.createAcquisitionInformation();
//            miMetadata.addAcquisitionInformation(miAcquisitionInformation);
//            miMetadata.createContact();
//            miMetadata.addContact(ciResponsibility);


            // aggregate all data in MD_Metadata
            MD_Metadata mdMetadata = new MD_Metadata();
            mdMetadata.addContact(ciResponsibility);
            mdMetadata.addMetadataIdentifier(mdIdentifier);
            mdMetadata.addDateInfo(ciDateCreation);
            mdMetadata.addDateInfo(ciDateLastModified);
            mdMetadata.addIdentificationInfo(mdDataIdentification);
            mdMetadata.addMetadataStandard(ciCitationMetadataStandard);
            mdMetadata.finalizeClass();

            dsDataSet.addHas(mdMetadata);
            dsDataSet.finalizeClass();

            // close/dispose database -> no more connection to collections
            dataStore.dispose();

        } catch (IOException | FactoryException e) {
            System.out.println(e.getMessage());
        }

        return dsDataSet;
    }


    ////////////////////
    // helper methods //
    ////////////////////
    Connection getConnection() {
        Connection connection = null;
        try {
            String url = "jdbc:sqlite:" + fileName;
            connection = DriverManager.getConnection(url);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    Statement getStatement(Connection connection) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return stmt;
    }

    private SimpleFeatureCollection project(SimpleFeatureCollection collection, String epsgIdentifier) {
        // reprojecting SimpleFeatureCollection to desired EPSG Identifier
        // epsgIdentifier is given in the form "epsg:4326" (or any other valid ID)

        SimpleFeatureCollection outCollection = null;
        try {
            outCollection = new ReprojectingFeatureCollection(collection, CRS.decode(epsgIdentifier));
        } catch(FactoryException e) {
            System.out.println(e.getMessage());
        }
        return outCollection;
    }

    private String getDescription(Statement stmt, Integer contentAct) {
        String description = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT description FROM gpkg_contents");
            for (int i = 0; i < contentAct; i++) {
                tableContent.next();
            }
            description = tableContent.getString(1);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return description;
    }

    private Instant getLastChange(Statement stmt, Integer contentAct) {
        // get date from ISO 8601 format in standard geopackage
        Instant lastChange = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT last_change FROM gpkg_contents");
            for (int i = 0; i < contentAct; i++) {
                tableContent.next();
            }
            String lastChangeString = tableContent.getString(1);
            lastChange = Instant.parse(lastChangeString);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lastChange;
    }

    private Integer getSRSID(Statement stmt, Integer contentAct) {
        Integer srsID = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT srs_id FROM gpkg_contents");
            for (int i = 0; i < contentAct; i++) {
                tableContent.next();
            }
            srsID = tableContent.getInt(1);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return srsID;
    }

    private String getSRSName(Statement stmt, Integer usedSRSID) {
        String srsName = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT srs_id, srs_name FROM gpkg_spatial_ref_sys");
            List<Integer> srsIDAll = new ArrayList<>();
            List<String> srsNameAll = new ArrayList<>();
            while (tableContent.next()) {
                srsIDAll.add(tableContent.getInt("srs_id"));
                srsNameAll.add(tableContent.getString("srs_name"));
            }
            int idx = srsIDAll.indexOf(usedSRSID);
            srsName = srsNameAll.get(idx);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return srsName;
    }

    private String getSRSOrganization(Statement stmt, Integer usedSRSID) {
        String srsOrganization = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT srs_id, organization FROM gpkg_spatial_ref_sys");
            List<Integer> srsIDAll = new ArrayList<>();
            List<String> srsOrganizationAll = new ArrayList<>();
            while (tableContent.next()) {
                srsIDAll.add(tableContent.getInt("srs_id"));
                srsOrganizationAll.add(tableContent.getString("organization"));
            }
            int idx = srsIDAll.indexOf(usedSRSID);
            srsOrganization = srsOrganizationAll.get(idx);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return srsOrganization;
    }

    private Integer getSRSOrganizationCoordsysID(Statement stmt, Integer usedSRSID) {
        Integer srsOrganizationCoordsysID = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT srs_id, organization_coordsys_id FROM gpkg_spatial_ref_sys");
            List<Integer> srsIDAll = new ArrayList<>();
            List<Integer> srsOrganizationCoordsysIDAll = new ArrayList<>();
            while (tableContent.next()) {
                srsIDAll.add(tableContent.getInt("srs_id"));
                srsOrganizationCoordsysIDAll.add(tableContent.getInt("organization_coordsys_id"));
            }
            int idx = srsIDAll.indexOf(usedSRSID);
            srsOrganizationCoordsysID = srsOrganizationCoordsysIDAll.get(idx);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return srsOrganizationCoordsysID;
    }

    private String getDataType(Statement stmt, Integer contentAct) {
        String dataType = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT data_type FROM gpkg_contents");
            for (int i = 0; i < contentAct; i++) {
                tableContent.next();
            }
            dataType = tableContent.getString(1);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dataType;
    }

    static class Extent {
        Double west;
        Double east;
        Double south;
        Double north;

        public Extent(){}

        public Extent(Double missingValue) {
            west = missingValue;
            east = missingValue;
            south = missingValue;
            north = missingValue;
        }
    }

    private Extent getExtent(SimpleFeatureCollection collection) {
        // get extent of feature collection

        ReferencedEnvelope envelope = collection.getBounds();

        Extent extent = new Extent();
        extent.west = envelope.getMinX();
        extent.east = envelope.getMaxX();
        extent.south = envelope.getMinY();
        extent.north = envelope.getMaxY();

        return extent;
    }

    private Extent getExtent(Statement stmt, Integer contentAct) {
        // get extent of geopackage from gpkg_contents

        Extent extent = new Extent();

        try {
            ResultSet tableContent = stmt.executeQuery("SELECT min_x, max_x, min_y, max_y FROM gpkg_contents");
            for (int i = 0; i < contentAct; i++) {
                tableContent.next();
            }
            extent.west = tableContent.getDouble("min_x");
            extent.east = tableContent.getDouble("max_x");
            extent.south = tableContent.getDouble("min_y");
            extent.north = tableContent.getDouble("max_y");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return extent;
    }

    private String getTableName(Statement stmt, Integer contentAct) {
        String tableName = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT table_name FROM gpkg_contents");
            for (int i = 0; i <= contentAct; i++) {
                tableContent.next();
            }
            tableName = tableContent.getString(1);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tableName;
    }

    List<String> getTableColContent(Statement stmt, String tableName, String colName) {
        // read column content from specific table

        List<String> colContent = new ArrayList<>();
        try {
            ResultSet col = stmt.executeQuery("SELECT " + colName + " FROM " + tableName);
            while (col.next()) {
                colContent.add(col.getString(colName));
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return colContent;
    }

    private int levenshteinDistance(String x, String y) {
        // calculation of Levenshtein distance (edit distance)
        // source https://www.baeldung.com/java-levenshtein-distance

        int[][] dp = new int[x.length() + 1][y.length() + 1];

        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                }
                else if (j == 0) {
                    dp[i][j] = i;
                }
                else {
                    dp[i][j] = min(dp[i - 1][j - 1] + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }
        return dp[x.length()][y.length()];
    }

    private int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    private int min(int... numbers) {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }

    public static byte[] unpack(byte val) {
        byte[] result = new byte[8];
        for(int i = 0; i < 8; i++) {
            result[i] = (byte) ((val >> (7 - i)) & 1);
        }
        return result;
    }
}
