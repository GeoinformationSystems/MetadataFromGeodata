/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

import org.geokur.ISO19103Schema.Record;
import org.geokur.ISO19115Schema.*;
import org.geokur.ISO19157Schema.*;

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

        Geopackage gpkg = new Geopackage(fileName);
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

        Geopackage gpkg = new Geopackage(fileName, contentAct);
        gpkg.open();
        List<FeatureDescriptor> featureDescriptors = gpkg.getCenterArea();
        List<Double> areaKm2UTM = new ArrayList<>();
        for (FeatureDescriptor featureDescriptor : featureDescriptors) {
            areaKm2UTM.add(featureDescriptor.getAreaKm2UTM());
        }
        gpkg.getPolygonPerArea(areaKm2UTM);

        // get (1) basic information
        System.out.println("Basic Information:");

        CI_Individual ciIndividual = new CI_Individual();
        ciIndividual.addName(System.getProperty("user.name"));
        ciIndividual.finalizeClass();

        CI_Responsibility ciResponsibility = new CI_Responsibility();
        ciResponsibility.addRole(new CI_RoleCode(CI_RoleCode.CI_RoleCodes.resourceProvider));
        ciResponsibility.addParty(ciIndividual);
        ciResponsibility.finalizeClass();

        String identifierCode = "pid:" + UUID.randomUUID().toString();
        String description = gpkg.getDescription(gpkg.statement, contentAct);

        MD_Identifier mdIdentifier = new MD_Identifier();
        mdIdentifier.addCode(identifierCode);
        if (description.length() > 0) {
            // add only non empty descriptions
            mdIdentifier.addDescription(description);
        }
        mdIdentifier.finalizeClass();

        Instant lastChange = gpkg.getLastChange(gpkg.statement, contentAct); // ISO 8601 date

        CI_Date ciDateCreation = new CI_Date();
        ciDateCreation.addDateType(new CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes.creation));
        ciDateCreation.addDate(lastChange.toString());
        ciDateCreation.finalizeClass();

        ZonedDateTime lastModified = ZonedDateTime.ofInstant(Instant.ofEpochMilli(gpkg.file.lastModified()), ZoneId.systemDefault()); // local timezone
        lastModified = lastModified.withZoneSameInstant(ZoneId.of("UTC")); // convert to UTC timezone
        String lastModifiedString = lastModified.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME); // datetime in ISO 8601 format

        CI_Date ciDateLastModified = new CI_Date();
        ciDateLastModified.addDateType(new CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes.lastUpdate));
        ciDateLastModified.addDate(lastModifiedString);
        ciDateLastModified.finalizeClass();


        // get (2) reference system
        System.out.println("Reference System:");

        Integer srsID = gpkg.getSRSID(gpkg.statement, contentAct);
        String srsOrganization = gpkg.getSRSOrganization(gpkg.statement, srsID);
        Integer srsOrganizationCoordsysID = gpkg.getSRSOrganizationCoordsysID(gpkg.statement, srsID);
        String srsName = gpkg.getSRSName(gpkg.statement, srsID);

        MD_Identifier mdIdentifier_MD_ReferenceSystem = new MD_Identifier();
        mdIdentifier_MD_ReferenceSystem.addCode(srsOrganization + ":" + srsOrganizationCoordsysID);
        mdIdentifier_MD_ReferenceSystem.addCodeSpace(srsOrganization);
        mdIdentifier_MD_ReferenceSystem.addDescription(srsName);
        mdIdentifier_MD_ReferenceSystem.finalizeClass();

        MD_ReferenceSystem mdReferenceSystem = new MD_ReferenceSystem();
        mdReferenceSystem.addReferenceSystemIdentifier(mdIdentifier_MD_ReferenceSystem);
        mdReferenceSystem.finalizeClass();


        // get (3) structure of spatial data
        System.out.println("Structure of Spatial Data:");

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

        String dataType = gpkg.getDataType(gpkg.statement, contentAct);
        Geopackage.Extent extentOrigCRS;
        Geopackage.Extent extent;

        MD_DataIdentification mdDataIdentification = new MD_DataIdentification();
        mdDataIdentification.addCitation(ciCitation);

        switch (dataType) {
            case "features":
                mdDataIdentification.addSpatialRepresentationType(new MD_SpatialRepresentationTypeCode(MD_SpatialRepresentationTypeCode.MD_SpatialRepresentationTypeCodes.vector));

                // get spatial extent
                extentOrigCRS = gpkg.getExtent(gpkg.collection);
                if (gpkg.markerTransform && gpkg.polygonSwitch) {
                    extent = gpkg.getExtentReproject(gpkg.collection);
                } else if (gpkg.markerTransform) {
                    extent = gpkg.getExtent(gpkg.collectionTransform);
                } else {
                    extent = gpkg.getExtent(gpkg.collection);
                }

                break;
            case "2d-gridded-coverage":
                extentOrigCRS = gpkg.getExtent(gpkg.statement, contentAct);
                extent = extentOrigCRS; // TODO: insert correct transforming of CRS in case of raster data

                break;
            default:
                extentOrigCRS = new Geopackage.Extent(-999.0);
                extent = new Geopackage.Extent(-999.0);
                break;
        }

        String tableName = gpkg.getTableName(gpkg.statement, contentAct);
        String environmentalDescription = "file name: " + fileName + "; "
                + "layer name: " + tableName + "; "
                + "file size: " + (int) gpkg.file.length() + " B; "
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
        exExtentOrigCRS.addDescription("geographical extent in data CRS, EPSG:" + gpkg.srcCRSepsg);
        exExtentOrigCRS.addGeographicElement(exGeographicBoundingBoxOrigCRS);
        exExtentOrigCRS.finalizeClass();

        mdDataIdentification.addExtent(exExtent);
        mdDataIdentification.addExtent(exExtentOrigCRS);
        mdDataIdentification.finalizeClass();


        // get (4) data quality
        System.out.println("Data Quality:");

        DQ_DataQuality dqDataQuality = new DQ_DataQuality();
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
            record.addField(String.format(Locale.ENGLISH, "%f", gpkg.polygonPerKm2));
            record.finalizeClass();

            DQ_QuantitativeResult dqQuantitativeResult = new DQ_QuantitativeResult();
            dqQuantitativeResult.addDateTime(now);
            dqQuantitativeResult.addValue(record);
            dqQuantitativeResult.addValueUnit("polygons per 1000 square km");

            DQ_Representativity dqRepresentativity = new DQ_Representativity();
            dqRepresentativity.addMeasure(dqMeasureReference);
            dqRepresentativity.addEvaluationMethod(dqFullInspection);
            dqRepresentativity.addResult(dqQuantitativeResult);
            dqRepresentativity.finalizeClass();

            MD_Scope mdScope = new MD_Scope();
            mdScope.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.dataset));
            mdScope.finalizeClass();

            dqDataQuality.addScope(mdScope);
            dqDataQuality.addReport(dqRepresentativity);
            dqDataQuality.finalizeClass();
        }


        // get (5) metadata contact
        System.out.println("Metadata Contact:");

        CI_Citation ciCitationMetadataStandard = new CI_Citation();
        ciCitationMetadataStandard.addTitle("ISO 19115-1");
        ciCitationMetadataStandard.addEdition("First edition 2014-04-01");
        ciCitationMetadataStandard.finalizeClass();


        // get (6) provenance


        // aggregate all data in MD_Metadata
        MD_Metadata mdMetadata = new MD_Metadata();
        mdMetadata.addContact(ciResponsibility);
        mdMetadata.addMetadataIdentifier(mdIdentifier);
        mdMetadata.addDateInfo(ciDateCreation);
        mdMetadata.addDateInfo(ciDateLastModified);
        mdMetadata.addIdentificationInfo(mdDataIdentification);
        mdMetadata.addReferenceSystemInfo(mdReferenceSystem);
        mdMetadata.addMetadataStandard(ciCitationMetadataStandard);
        if (gpkg.polygonSwitch) {
            mdMetadata.addDataQualityInfo(dqDataQuality);
        }
        mdMetadata.finalizeClass();

        dsDataSet.addHas(mdMetadata);
        dsDataSet.finalizeClass();

        gpkg.dispose();

        return dsDataSet;
    }


//    public static byte[] unpack(byte val) {
//        byte[] result = new byte[8];
//        for(int i = 0; i < 8; i++) {
//            result[i] = (byte) ((val >> (7 - i)) & 1);
//        }
//        return result;
//    }
}
