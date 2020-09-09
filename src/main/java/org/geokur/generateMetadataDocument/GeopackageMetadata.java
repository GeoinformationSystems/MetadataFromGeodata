/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.generateMetadataDocument;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GeopackageMetadata {

    public Integer getContentNum(String fileName) {
        // get number of contents in geopackage (later use in getContent)
        GeopackageMetadata gpkg = new GeopackageMetadata();
        Connection connection = gpkg.getConnection(fileName);
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

    public List<Element> getContent(String fileName, Integer contentAct, List<Element> content, Map<String, Namespace> ns) {
        // read geopackage file and put its metadata into list of elements

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
        GeopackageMetadata gpkg = new GeopackageMetadata();
        Connection connection = gpkg.getConnection(fileName);
        Statement statement = gpkg.getStatement(connection);

        // open geopackage with geotools
        @SuppressWarnings("rawtypes")
        Map params = new HashMap();
        //noinspection unchecked
        params.put("dbtype", "geopkg");
        //noinspection unchecked
        params.put("database", geopackageFile.toString());

        CoordinateReferenceSystem srcCRS;
        boolean markerTransform = false;
        SimpleFeatureCollection collection;
        SimpleFeatureCollection collectionTransform;

        try {
            DataStore dataStore = DataStoreFinder.getDataStore(params);
            String typeName = dataStore.getTypeNames()[contentAct];
            collection = dataStore.getFeatureSource(typeName).getFeatures();
            srcCRS = collection.getSchema().getCoordinateReferenceSystem();

            collectionTransform = collection;

            try {
                MathTransform mathTransform = CRS.findMathTransform(srcCRS, DefaultGeographicCRS.WGS84, true);
                // transform to WGS84 for standard extent lon/lat
                if (!mathTransform.isIdentity()) {
                    markerTransform = true;
                    collectionTransform = project(collection, "epsg:4326");
                }
            } catch (FactoryException ignored) {
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
                        MathTransform mathTransform = CRS.findMathTransform(srcCRS, actCRS, true);
                        if (mathTransform.isIdentity()) {
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

            NestedElement nestedElement = new NestedElement();


            // allocate all UUID for elements used here
            // two instances of the same metadata class get the same UUID
            // -> only the whole chain is unique, not the occurrence of one particular UUID
            UUID id_DS_DataSet = UUID.randomUUID();
            UUID id_has = UUID.randomUUID();
            UUID id_MD_Metadata = UUID.randomUUID();
            UUID id_contact = UUID.randomUUID();
            UUID id_CI_Responsibility = UUID.randomUUID();
            UUID id_role = UUID.randomUUID();
            UUID id_party = UUID.randomUUID();
            UUID id_CI_Individual = UUID.randomUUID();
            UUID id_name = UUID.randomUUID();

            UUID id_metadataIdentifier = UUID.randomUUID();
            UUID id_MD_Identifier = UUID.randomUUID();
            UUID id_code = UUID.randomUUID();

            UUID id_identificationInfo = UUID.randomUUID();
            UUID id_MD_DataIdentification = UUID.randomUUID();
            UUID id_environmentDescription = UUID.randomUUID();
            UUID id_citation = UUID.randomUUID();
            UUID id_CI_Citation = UUID.randomUUID();
            UUID id_title = UUID.randomUUID();
            UUID id_date = UUID.randomUUID();
            UUID id_CI_Date = UUID.randomUUID();
            UUID id_dateType = UUID.randomUUID();
            UUID id_DateTime = UUID.randomUUID();

            UUID id_spatialRepresentationType = UUID.randomUUID();

            UUID id_description = UUID.randomUUID();

            UUID id_dateInfo = UUID.randomUUID();

            UUID id_CI_Date2 = UUID.randomUUID();

            UUID id_extent = UUID.randomUUID();
            UUID id_EX_Extent = UUID.randomUUID();
            UUID id_geographicElement = UUID.randomUUID();
            UUID id_EX_GeographicBoundingBox = UUID.randomUUID();
            UUID id_westBoundLongitude = UUID.randomUUID();
            UUID id_eastBoundLongitude = UUID.randomUUID();
            UUID id_southBoundLatitude = UUID.randomUUID();
            UUID id_northBoundLatitude = UUID.randomUUID();

            UUID id_EX_Extent2 = UUID.randomUUID();

            UUID id_referenceSystemInfo = UUID.randomUUID();
            UUID id_MD_ReferenceSystem = UUID.randomUUID();
            UUID id_referenceSystemIdentifier = UUID.randomUUID();
            UUID id_codeSpace = UUID.randomUUID();


            // get (1) basic information
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "contact", "CI_Responsibility", "role"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_contact, id_CI_Responsibility, id_role}, "resourceProvider", ns));
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "contact", "CI_Responsibility", "party", "CI_Individual", "name"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_contact, id_CI_Responsibility, id_party, id_CI_Individual, id_name}, System.getProperty("user.name"), ns));

            String identifierCode = "pid:" + UUID.randomUUID().toString();
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "metadataIdentifier", "MD_Identifier", "code"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_metadataIdentifier, id_MD_Identifier, id_code}, identifierCode, ns));

            String description = gpkg.getDescription(statement, contentAct);
            if (description.length() > 0) {
                // add only non empty descriptions
                content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "metadataIdentifier", "MD_Identifier", "description"},
                        new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_metadataIdentifier, id_MD_Identifier, id_description}, description, ns));
            }

            Instant lastChange = gpkg.getLastChange(statement, contentAct); // ISO 8601 date
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "dateInfo", "CI_Date", "dateType"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_dateInfo, id_CI_Date, id_dateType}, "creation", ns));
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "dateInfo", "CI_Date", "date", "DateTime"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_dateInfo, id_CI_Date, id_date, id_DateTime}, lastChange.toString(), ns));

            ZonedDateTime lastModified = ZonedDateTime.ofInstant(Instant.ofEpochMilli(geopackageFile.lastModified()), ZoneId.systemDefault()); // local timezone
            lastModified = lastModified.withZoneSameInstant(ZoneId.of("UTC")); // convert to UTC timezone
            String lastModifiedString = lastModified.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME); // datetime in ISO 8601 format

            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "dateInfo", "CI_Date", "dateType"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_dateInfo, id_CI_Date2, id_dateType}, "lastUpdate", ns));
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "dateInfo", "CI_Date", "date", "DateTime"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_dateInfo, id_CI_Date2, id_date, id_DateTime}, lastModifiedString, ns));


            // get (2) reference system
            Integer srsID = gpkg.getSRSID(statement, contentAct);
            String srsOrganization = gpkg.getSRSOrganization(statement, srsID);
            Integer srsOrganizationCoordsysID = gpkg.getSRSOrganizationCoordsysID(statement, srsID);
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "referenceSystemInfo", "MD_ReferenceSystem", "referenceSystemIdentifier", "MD_Identifier", "code"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_referenceSystemInfo, id_MD_ReferenceSystem, id_referenceSystemIdentifier, id_MD_Identifier, id_code}, srsOrganization + ":" + srsOrganizationCoordsysID, ns));
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "referenceSystemInfo", "MD_ReferenceSystem", "referenceSystemIdentifier", "MD_Identifier", "codeSpace"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_referenceSystemInfo, id_MD_ReferenceSystem, id_referenceSystemIdentifier, id_MD_Identifier, id_codeSpace}, srsOrganization, ns));

            String srsName = gpkg.getSRSName(statement, srsID);
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "referenceSystemInfo", "MD_ReferenceSystem", "referenceSystemIdentifier", "MD_Identifier", "description"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_referenceSystemInfo, id_MD_ReferenceSystem, id_referenceSystemIdentifier, id_MD_Identifier, id_description}, srsName, ns));


            // get (3) structure of spatial data
            String now = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

            // TODO: informative title available?
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "citation", "CI_Citation", "title"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_citation, id_CI_Citation, id_title}, "", ns));
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "citation", "CI_Citation", "date", "CI_Date", "dateType"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_citation, id_CI_Citation, id_date, id_CI_Date, id_dateType}, "creation", ns));
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "citation", "CI_Citation", "date", "CI_Date", "date", "dateTime"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_citation, id_CI_Citation, id_date, id_CI_Date, id_date, id_DateTime}, now, ns));

            String dataType = gpkg.getDataType(statement, contentAct);
            List<Double> extent_origCRS;
            List<Double> extent;

            switch (dataType) {
                case ("features"):
                    content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "spatialRepresentationType"},
                            new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_spatialRepresentationType}, "vector", ns));

                    // read BLOB content of vector data in geopackage
//                    List<Geometry> geometries = gpkg.getVectorGeometry(statement, tableName);
//                    List<Point> corners = getOuterRectangle(geometries);

                    // get spatial extent
                    extent_origCRS = getExtent(collection);
                    extent = getExtent(collectionTransform);

                    break;

                case ("2d-gridded-coverage"):
                    UUID id_spatialRepresentationInfo = UUID.randomUUID();
                    UUID id_MD_GridSpatialRepresentation = UUID.randomUUID();
                    UUID id_numberOfDimensions = UUID.randomUUID();
                    UUID id_axisDimensionProperties = UUID.randomUUID();
                    UUID id_MD_DimensionRow = UUID.randomUUID();
                    UUID id_dimensionNameRow = UUID.randomUUID();
                    UUID id_dimensionSizeRow = UUID.randomUUID();
                    UUID id_MD_DimensionCol = UUID.randomUUID();
                    UUID id_dimensionNameCol = UUID.randomUUID();
                    UUID id_dimensionSizeCol = UUID.randomUUID();
                    UUID id_cellGeometry = UUID.randomUUID();
                    UUID id_transformationParameterAvailability = UUID.randomUUID();

                    content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "spatialRepresentationType"},
                            new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_spatialRepresentationType}, "grid", ns));
                    content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "numberOfDimensions"},
                            new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_numberOfDimensions}, "2", ns));
                    content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "axisDimensionProperties", "MD_Dimension", "dimensionName"},
                            new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_axisDimensionProperties, id_MD_DimensionRow, id_dimensionNameRow}, "row", ns));
                    content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "axisDimensionProperties", "MD_Dimension", "dimensionSize"},
                            new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_axisDimensionProperties, id_MD_DimensionRow, id_dimensionSizeRow}, "39", ns));
                    content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "axisDimensionProperties", "MD_Dimension", "dimensionName"},
                            new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_axisDimensionProperties, id_MD_DimensionCol, id_dimensionNameCol}, "column", ns));
                    content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "axisDimensionProperties", "MD_Dimension", "dimensionSize"},
                            new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_axisDimensionProperties, id_MD_DimensionCol, id_dimensionSizeCol}, "41", ns));
                    content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "cellGeometry"},
                            new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_cellGeometry}, "area", ns));
                    content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "transformationParameterAvailability"},
                            new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_transformationParameterAvailability}, "0", ns));

                    extent_origCRS = gpkg.getExtent(statement, contentAct);
                    extent = extent_origCRS; // TODO: insert correct transforming of CRS in case of raster data

                    break;

                default:
                    extent_origCRS = new ArrayList<>();
                    extent = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        extent_origCRS.add((double) -999);
                        extent.add((double) -999);
                    }
                    break;
            }

            String tableName = gpkg.getTableName(statement, contentAct);
            String environmentalDescription = "file name: " + fileName + "\n"
                    + "layer name: " + tableName + "\n"
                    + "file size: " + (int) geopackageFile.length() + " B\n"
                    + "os: " + System.getProperty("os.name");

            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "environmentalDescription"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_environmentDescription}, environmentalDescription, ns));
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "description"},
                    new UUID[] {id_DS_DataSet, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_description}, "geographical extent in WGS84, EPSG:4326",ns));
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "westBoundLongitude"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_geographicElement, id_EX_GeographicBoundingBox, id_westBoundLongitude}, extent.get(0).toString(), ns));
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "eastBoundLongitude"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_geographicElement, id_EX_GeographicBoundingBox, id_eastBoundLongitude}, extent.get(1).toString(), ns));
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "southBoundLatitude"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_geographicElement, id_EX_GeographicBoundingBox, id_southBoundLatitude}, extent.get(2).toString(), ns));
            content.add(nestedElement.create(new String[]{"DS_DataSet", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "northBoundLatitude"},
                    new UUID[]{id_DS_DataSet, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_geographicElement, id_EX_GeographicBoundingBox, id_northBoundLatitude}, extent.get(3).toString(), ns));

            content.add(nestedElement.create(new String[] {"DS_DataSet", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "description"},
                    new UUID[] {id_DS_DataSet, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent2, id_description}, "geographical extent in data CRS, EPSG:" + srcCRSepsg, ns));
            content.add(nestedElement.create(new String[] {"DS_DataSet", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "westBoundLongitude"},
                    new UUID[] {id_DS_DataSet, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent2, id_geographicElement, id_EX_GeographicBoundingBox, id_westBoundLongitude}, extent_origCRS.get(0).toString(), ns));
            content.add(nestedElement.create(new String[] {"DS_DataSet", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "eastBoundLongitude"},
                    new UUID[] {id_DS_DataSet, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent2, id_geographicElement, id_EX_GeographicBoundingBox, id_eastBoundLongitude}, extent_origCRS.get(1).toString(), ns));
            content.add(nestedElement.create(new String[] {"DS_DataSet", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "southBoundLatitude"},
                    new UUID[] {id_DS_DataSet, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent2, id_geographicElement, id_EX_GeographicBoundingBox, id_southBoundLatitude}, extent_origCRS.get(2).toString(), ns));
            content.add(nestedElement.create(new String[] {"DS_DataSet", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "northBoundLatitude"},
                    new UUID[] {id_DS_DataSet, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent2, id_geographicElement, id_EX_GeographicBoundingBox, id_northBoundLatitude}, extent_origCRS.get(3).toString(), ns));


            // get (4) data quality


            // get (5) metadata contact


            // get (6) provenance


            // close/dispose database -> no more connection to collections
            dataStore.dispose();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return content;

    }

    @SuppressWarnings("SameParameterValue")
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

    @SuppressWarnings("unused")
    private List<String> getObligationOccurrence(List<Element> inElement, String inElementName) {
        // get obligation and occurrence properties of a particular element in a list
        List<String> obligationOccurrence = new ArrayList<>();

        for (Element inElementAct : inElement) {
            if (inElementAct.getChildText("name").equals(inElementName)) {
                obligationOccurrence.add(inElementAct.getChildText("obligation"));
                obligationOccurrence.add(inElementAct.getChildText("occurrence"));
                break;
            }
        }
        if (obligationOccurrence.isEmpty()) {
            System.out.println("For element " + inElementName + " no obligation and occurrence properties were found.");
        }

        return obligationOccurrence;
    }

    private Connection getConnection(String fileName) {
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

    private Statement getStatement(Connection connection) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return stmt;
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

    @SuppressWarnings("unused")
    private String getIdentifier(Statement stmt, Integer contentAct) {
        String identifier = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT identifier FROM gpkg_contents");
            for (int i = 0; i < contentAct; i++) {
                tableContent.next();
            }
            identifier = tableContent.getString(1);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return identifier;
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

    private List<Double> getExtent(Statement stmt, Integer contentAct) {
        // get extent of geopackage from gpkg_contents
        // return list contains in this order: west boundary, east boundary, south boundary, north boundary

        List<Double> extent = new ArrayList<>();
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT min_x, max_x, min_y, max_y FROM gpkg_contents");
            for (int i = 0; i < contentAct; i++) {
                tableContent.next();
            }
            extent.add(tableContent.getDouble("min_x"));
            extent.add(tableContent.getDouble("max_x"));
            extent.add(tableContent.getDouble("min_y"));
            extent.add(tableContent.getDouble("max_y"));
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return extent;
    }

    private List<Double> getExtent(SimpleFeatureCollection collection) {
        // get extent of feature collection
        // return list contains in this order: west boundary, east boundary, south boundary, north boundary

        List<Double> extent = new ArrayList<>();
        ReferencedEnvelope envelope = collection.getBounds();
        extent.add(envelope.getMinX());
        extent.add(envelope.getMaxX());
        extent.add(envelope.getMinY());
        extent.add(envelope.getMaxY());

        return extent;
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

    @SuppressWarnings("unused")
    private String getSRSDefinition(Statement stmt, Integer usedSRSID) {
        String srsDefinition = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT srs_id, definition FROM gpkg_spatial_ref_sys");
            List<Integer> srsIDAll = new ArrayList<>();
            List<String> srsDefinitionAll = new ArrayList<>();
            while (tableContent.next()) {
                srsIDAll.add(tableContent.getInt("srs_id"));
                srsDefinitionAll.add(tableContent.getString("definition"));
            }
            int idx = srsIDAll.indexOf(usedSRSID);
            srsDefinition = srsDefinitionAll.get(idx);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return srsDefinition;
    }

    @SuppressWarnings("unused")
    private List<String> getTableColNames(Statement stmt, String tableName) {
        List<String> tableColNames = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData colNames = tableContent.getMetaData();
            List<String> colNamesAct = new ArrayList<>();
            for (int i = 1; i <= colNames.getColumnCount(); i++) {
                colNamesAct.add(colNames.getColumnName(i));
            }
            tableColNames = colNamesAct;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tableColNames;
    }

    @SuppressWarnings("unused")
    private Integer getTableRowNum(Statement stmt, String tableName) {
        Integer tableRowNum = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT * FROM " + tableName);
            int ct = 0;
            while (tableContent.next()) {
                ++ct;
            }
            tableRowNum = ct;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tableRowNum;
    }

    @SuppressWarnings("unused")
    private List<Geometry> getVectorGeometry(Statement stmt, String tableName) {
        // get all vector geometries from a table
        // using only JTS library

        List<Geometry> geometries = new ArrayList<>();
        int position;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT geometry FROM " + tableName);
            while (tableContent.next()) {
                byte[] by = tableContent.getBytes(1);
                byte[] magicByte = Arrays.copyOfRange(by, 0, 2);
                String magic = new String(magicByte, StandardCharsets.UTF_8); // must be ascii GP
                if (!magic.equals("GP")) {
                    // no proper geopackage geometry BLOB
                    System.out.println("Table " + tableName + " has invalid geometry column.");
                    System.out.println("Geometry not loaded.");
                    geometries.add(null);
                    continue;
                }

                byte versionByte = by[2];
                Integer version = (int) versionByte;
                byte flagsByte = by[3];
                byte[] flagsUnpack = unpack(flagsByte);
                int envHeader = (int) ((int) flagsUnpack[6] * Math.pow(2, 0) + (int) flagsUnpack[5] * Math.pow(2, 1) + flagsUnpack[4] * Math.pow(2, 2)); // envelope contents indicator
                String endianness;
                if (flagsUnpack[7]==0) {
                    // byte order for header values
                    endianness = "BIG_ENDIAN";
                } else {
                    endianness = "LITTLE_ENDIAN";
                }

                // wrap bytes into ByteBuffer for enable byte order change
                ByteBuffer byBuffer = ByteBuffer.wrap(by);
                byBuffer.position(4); // forward to actual position

                switch (endianness) {
                    case "BIG_ENDIAN":
                        byBuffer.order(ByteOrder.BIG_ENDIAN);
                        break;
                    case "LITTLE_ENDIAN":
                        byBuffer.order(ByteOrder.LITTLE_ENDIAN);
                }
                int srsID = byBuffer.getInt();
                position = 8;

                double[] envelope;
                if (envHeader == 0) {
                    // 0: no envelope
                    envelope = null;
                } else if (envHeader == 1) {
                    // 1: envelope is [minx, maxx, miny, maxy], 32 bytes
                    envelope = new double[]{byBuffer.getDouble(), byBuffer.getDouble(), byBuffer.getDouble(), byBuffer.getDouble()};
                    position = position + 32;
                } else if (envHeader == 2 || envHeader == 3) {
                    // 2: envelope is [minx, maxx, miny, maxy, minm, maxm], 48 bytes
                    // 3: envelope is [minx, maxx, miny, maxy, minz, maxz], 48 bytes
                    envelope = new double[]{byBuffer.getDouble(), byBuffer.getDouble(), byBuffer.getDouble(), byBuffer.getDouble(), byBuffer.getDouble(), byBuffer.getDouble()};
                    position = position + 48;
                } else if (envHeader == 4) {
                    // 4: envelope is [minx, maxx, miny, maxy, minz, maxz, minm, maxm], 64 bytes
                    envelope = new double[]{byBuffer.getDouble(), byBuffer.getDouble(), byBuffer.getDouble(), byBuffer.getDouble(), byBuffer.getDouble(), byBuffer.getDouble(), byBuffer.getDouble(), byBuffer.getDouble()};
                    position = position + 64;
                } else {
                    // invalid envHeader
                    System.out.println("Invalid envelope contents indicator code");
                    geometries.add(null);
                    continue;
                }

                byte[] wkbBytes;
                // define precision and forward the spatial reference system ID to all geometries
                GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), srsID);
                WKBReader wkbReader = new WKBReader(geometryFactory);
                wkbBytes = Arrays.copyOfRange(by, position, by.length);
                try {
                    geometries.add(wkbReader.read(wkbBytes));
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return geometries;
    }

    @SuppressWarnings("unused")
    private List<Point> getOuterRectangle(List<Geometry> geometries) {
        // get the LL and UR corner coordinates incl. SRS ID

        List<Point> corners = new ArrayList<>();
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        for (Geometry geometry : geometries) {
            Envelope envelope = geometry.getEnvelopeInternal();
            x.add(envelope.getMaxX());
            x.add(envelope.getMinX());
            y.add(envelope.getMaxY());
            y.add(envelope.getMinY());
        }
        Point ll;
        // define precision and forward the spatial reference system ID to all geometries
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), geometries.get(0).getSRID());
        ll = geometryFactory.createPoint(new Coordinate(Collections.min(x),Collections.min(y)));
        Point ur;
        ur = geometryFactory.createPoint(new Coordinate(Collections.max(x),Collections.max(y)));
        corners.add(ll);
        corners.add(ur);

        return corners;
    }

    @SuppressWarnings("unused")
    private void getRasterContent(Statement stmt, String tableName) {
        // get raster geometry and content
        // TODO: reading geopackage raster data (tiles) for getting additional metadata

        try {
            ResultSet tableContent = stmt.executeQuery("SELECT tile_data FROM " + tableName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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

    @SuppressWarnings("unused")
    public static byte pack(byte[] val) {
        byte result = 0;
        for (byte bit : val)
            result = (byte)((result << 1) | (bit & 1));
        return result;
    }

}
