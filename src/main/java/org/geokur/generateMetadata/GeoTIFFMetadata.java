/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

import org.geokur.ISO19115Schema.*;
import org.geokur.ISO19157Schema.DQ_DataQuality;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class GeoTIFFMetadata implements Metadata {
    String fileName;
    DS_DataSet dsDataSet;

    public GeoTIFFMetadata(String fileName, DS_DataSet dsDataSet) {
        this.fileName = fileName;
        this.dsDataSet = dsDataSet;
    }

    public DS_Resource getMetadata() {
        // read GeoTIFF file and put its metadata into DS_Resource
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

        File file = new File(fileName);

        GeoTIFF geoTIFF = new GeoTIFF(fileName);
        geoTIFF.open();

        try {
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

            MD_Identifier mdIdentifier = new MD_Identifier();
            mdIdentifier.addCode(identifierCode);
            mdIdentifier.finalizeClass();

            ZonedDateTime creation = ZonedDateTime.parse(Files.readAttributes(file.toPath(), BasicFileAttributes.class).creationTime().toString());
            creation = creation.withZoneSameInstant(ZoneId.of("UTC")); // convert to UTC timezone
            String creationString = creation.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME); // datetime in ISO 8601 format

            ZonedDateTime lastModified = ZonedDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault()); // local timezone
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

            String srsOrganization = geoTIFF.srsDescription.getSrsOrganization();
            Integer srsOrganizationCoordsysID = geoTIFF.srsDescription.getSrsOrganizationCoordsysID();
            String srsName = geoTIFF.srsDescription.getSrsName();

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

            CI_Citation ciCitation = new CI_Citation();
            ciCitation.addTitle("");
            ciCitation.addDate(ciDate);
            ciCitation.finalizeClass();

            // get spatial resolution
            MD_Resolution mdResolution = new MD_Resolution();
            MD_Resolution mdResolution2 = new MD_Resolution();
            if (geoTIFF.resolutionIsotropy) {
                mdResolution.addDistance(geoTIFF.resolutionIsotropic);
            } else {
                mdResolution.addDistance(geoTIFF.resolutionAnisotropic[0]);
                mdResolution2.addDistance(geoTIFF.resolutionAnisotropic[1]);
                mdResolution2.finalizeClass();
            }
            mdResolution.finalizeClass();

            String environmentalDescription = "file name: " + fileName + "; "
                    + "file size: " + (int) file.length() + " B; "
                    + "os: " + System.getProperty("os.name");

            EX_GeographicBoundingBox exGeographicBoundingBoxOrigCRS = new EX_GeographicBoundingBox();
            exGeographicBoundingBoxOrigCRS.addWestBoundLongitude(geoTIFF.extent.west);
            exGeographicBoundingBoxOrigCRS.addEastBoundLongitude(geoTIFF.extent.east);
            exGeographicBoundingBoxOrigCRS.addSouthBoundLatitude(geoTIFF.extent.south);
            exGeographicBoundingBoxOrigCRS.addNorthBoundLatitude(geoTIFF.extent.north);
            exGeographicBoundingBoxOrigCRS.finalizeClass();

            EX_Extent exExtentOrigCRS = new EX_Extent();
            exExtentOrigCRS.addDescription("geographical extent in data CRS, EPSG:" + geoTIFF.srsDescription.getSrsOrganizationCoordsysID());
            exExtentOrigCRS.addGeographicElement(exGeographicBoundingBoxOrigCRS);
            exExtentOrigCRS.finalizeClass();

            MD_DataIdentification mdDataIdentification = new MD_DataIdentification();
            mdDataIdentification.addCitation(ciCitation);
            mdDataIdentification.addSpatialRepresentationType(new MD_SpatialRepresentationTypeCode(MD_SpatialRepresentationTypeCode.MD_SpatialRepresentationTypeCodes.grid));
            mdDataIdentification.addSpatialResolution(mdResolution);
            if (!geoTIFF.resolutionIsotropy) {
                // isotropy leads to two different resolution values
                mdDataIdentification.addSpatialResolution(mdResolution2);
            }
            mdDataIdentification.addEnvironmentalDescription(environmentalDescription);
            mdDataIdentification.addExtent(exExtentOrigCRS);
            mdDataIdentification.finalizeClass();

            MD_Dimension mdDimensionLon = new MD_Dimension();
            mdDimensionLon.addDimensionName(new MD_DimensionNameTypeCode(MD_DimensionNameTypeCode.MD_DimensionNameTypeCodes.column));
            mdDimensionLon.addDimensionSize(geoTIFF.size[0]);
            mdDimensionLon.addDimensionTitle("longitude");
            mdDimensionLon.finalizeClass();

            MD_Dimension mdDimensionLat = new MD_Dimension();
            mdDimensionLat.addDimensionName(new MD_DimensionNameTypeCode(MD_DimensionNameTypeCode.MD_DimensionNameTypeCodes.row));
            mdDimensionLat.addDimensionSize(geoTIFF.size[1]);
            mdDimensionLat.addDimensionTitle("latitude");
            mdDimensionLat.finalizeClass();

            MD_Georectified mdGeorectified = new MD_Georectified();
            mdGeorectified.addNumberOfDimensions(geoTIFF.dimensionality);
            mdGeorectified.addAxisDimensionProperties(mdDimensionLon);
            mdGeorectified.addAxisDimensionProperties(mdDimensionLat);
            if (geoTIFF.cellRepresentation.toLowerCase().contains("area")) {
                mdGeorectified.addCellGeometry(new MD_CellGeometryCode(MD_CellGeometryCode.MD_CellGeometryCodes.area));
            } else if(geoTIFF.cellRepresentation.toLowerCase().contains("point")) {
                mdGeorectified.addCellGeometry(new MD_CellGeometryCode(MD_CellGeometryCode.MD_CellGeometryCodes.point));
            }
            mdGeorectified.addTransformationParameterAvailability(geoTIFF.srsDescription.getSrsName() != null);
            mdGeorectified.addCheckPointAvailability(false);
            mdGeorectified.addCornerPoints(geoTIFF.cornerLL);
            mdGeorectified.addCornerPoints(geoTIFF.cornerLR);
            mdGeorectified.addCornerPoints(geoTIFF.cornerUR);
            mdGeorectified.addCornerPoints(geoTIFF.cornerUL);
            mdGeorectified.addPointInPixel(new MD_PixelOrientationCode(MD_PixelOrientationCode.MD_PixelOrientationCodes.lowerLeft));
            mdGeorectified.finalizeClass();


            // get (4) data quality
            System.out.println("Data Quality:");

            MD_Scope mdScope = new MD_Scope();
            mdScope.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.dataset));
            mdScope.finalizeClass();



            DQ_DataQuality dqDataQuality = new DQ_DataQuality();
            dqDataQuality.addScope(mdScope);
//            dqDataQuality.addReport();
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
            mdMetadata.addContact(ciResponsibility);
            mdMetadata.addMetadataIdentifier(mdIdentifier);
            mdMetadata.addDateInfo(ciDateCreation);
            mdMetadata.addDateInfo(ciDateLastModified);
            mdMetadata.addIdentificationInfo(mdDataIdentification);
            mdMetadata.addSpatialRepresentationInfo(mdGeorectified);
            mdMetadata.addReferenceSystemInfo(mdReferenceSystem);
            mdMetadata.addMetadataStandard(ciCitationMetadataStandard);

            mdMetadata.addDataQualityInfo(dqDataQuality);
            mdMetadata.finalizeClass();

            dsDataSet.addHas(mdMetadata);
            dsDataSet.finalizeClass();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return dsDataSet;
    }
}
