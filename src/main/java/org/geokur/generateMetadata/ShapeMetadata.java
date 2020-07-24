/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.generateMetadata;

import org.geotools.data.*;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import java.io.IOException;
import java.io.File;
import java.nio.file.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ShapeMetadata {
    public List<Element> getContent(String fileName, Integer contentAct, Map<String, Namespace> ns) {
        // read shape file and put its metadata into list of elements
        // contentAct should always be 0 in the case of shapefiles

        List<Element> content = new ArrayList<>();

        File file = new File(fileName);

        try {
            FileDataStore dataStore = FileDataStoreFinder.getDataStore(file);
            String typeName = dataStore.getTypeNames()[contentAct];
            SimpleFeatureCollection collection = dataStore.getFeatureSource().getFeatures();
            CoordinateReferenceSystem srcCRS = collection.getSchema().getCoordinateReferenceSystem();
            SimpleFeatureCollection collectionTransform = collection;
            boolean markerTransform = false;
            try {
                MathTransform mathTransform = CRS.findMathTransform(srcCRS, DefaultGeographicCRS.WGS84, true);
                // transform to WGS84 for standard extent lon/lat
                if (!mathTransform.isIdentity()) {
                    markerTransform = true;
                    collectionTransform = project(collection, "epsg:4326");
                }
            } catch (FactoryException e) {
                System.out.println(e.getMessage());
            }

            /*
            // write output to console
            try(SimpleFeatureIterator itr = collection.features()) {
                while(itr.hasNext()) {
                    SimpleFeature feature = itr.next();
                    System.out.println(feature.getDefaultGeometryProperty().getValue());
                }
            }
            System.out.println();
            try(SimpleFeatureIterator itr = collectionTransform.features()) {
                while(itr.hasNext()) {
                    SimpleFeature feature = itr.next();
                    System.out.println(feature.getDefaultGeometryProperty().getValue());
                }
            }
            */

            NestedElement nestedElement = new NestedElement();
            // TODO: further look into ISO19115_Workbook for getting additional metadata out of shapefile

            UUID id_DS_Resource = UUID.randomUUID();
            UUID id_has = UUID.randomUUID();
            UUID id_MD_Metadata = UUID.randomUUID();
            UUID id_contact = UUID.randomUUID();
            UUID id_CI_Responsibility = UUID.randomUUID();
            UUID id_role = UUID.randomUUID();
            UUID id_party = UUID.randomUUID();
            UUID id_CI_Individual = UUID.randomUUID();
            UUID id_name = UUID.randomUUID();

            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "contact", "CI_Responsibility", "role"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_contact, id_CI_Responsibility, id_role}, "resourceProvider", ns));
            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "contact", "CI_Responsibility", "party", "CI_Individual", "name"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_contact, id_CI_Responsibility, id_party, id_CI_Individual, id_name}, System.getProperty("user.name"), ns));

            UUID id_metadataIdentifier = UUID.randomUUID();
            UUID id_MD_Identifier = UUID.randomUUID();
            UUID id_code = UUID.randomUUID();

            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "metadataIdentifier", "MD_Identifier", "code"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_metadataIdentifier, id_MD_Identifier, id_code}, typeName, ns));

            UUID id_identificationInfo = UUID.randomUUID();
            UUID id_MD_DataIdentification = UUID.randomUUID();
            UUID id_environmentDescription = UUID.randomUUID();
            UUID id_spatialRepresentationType = UUID.randomUUID();

            // find all files belonging to actual shape file -> base name plus allowed extensions
            Shape shape = new Shape(file);
            shape.getShapeFileList();

            String environmentalDescription = "file name: " + fileName + "\n"
                    + "all shape format files: " + shape.allFiles + "\n"
                    + "file size: " + (int) shape.size + " B\n"
                    + "os: " + System.getProperty("os.name");
            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "environmentalDescription"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_environmentDescription}, environmentalDescription, ns));
            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "spatialRepresentationType"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_spatialRepresentationType}, "vector", ns));

            UUID id_dateInfo = UUID.randomUUID();
            UUID id_CI_Date = UUID.randomUUID();
            UUID id_dateType = UUID.randomUUID();
            UUID id_date = UUID.randomUUID();
            UUID id_DateTime = UUID.randomUUID();

            ZonedDateTime lastModified = ZonedDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault()); // local timezone
            lastModified = lastModified.withZoneSameInstant(ZoneId.of("UTC")); // convert to UTC timezone
            String lastModifiedString = lastModified.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME); // datetime in ISO 8601 format

            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "dateInfo", "CI_Date", "dateType"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_dateInfo, id_CI_Date, id_dateType}, "creation", ns));
            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "dateInfo", "CI_Date", "date", "DateTime"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_dateInfo, id_CI_Date, id_date, id_DateTime}, lastModifiedString, ns));


            UUID id_referenceSystemInfo = UUID.randomUUID();
            UUID id_MD_ReferenceSystem = UUID.randomUUID();
            UUID id_referenceSystemIdentifier = UUID.randomUUID();
            UUID id_MD_IdentifierCRS = UUID.randomUUID();
            UUID id_codeCRS = UUID.randomUUID();
            UUID id_codeSpaceCRS = UUID.randomUUID();
            UUID id_descriptionCRS = UUID.randomUUID();

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

            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "referenceSystemInfo", "MD_ReferenceSystem", "referenceSystemIdentifier", "MD_Identifier", "code"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_referenceSystemInfo, id_MD_ReferenceSystem, id_referenceSystemIdentifier, id_MD_IdentifierCRS, id_codeCRS}, authority + "::" + srcCRSepsg, ns));
            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "referenceSystemInfo", "MD_ReferenceSystem", "referenceSystemIdentifier", "MD_Identifier", "codeSpace"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_referenceSystemInfo, id_MD_ReferenceSystem, id_referenceSystemIdentifier, id_MD_IdentifierCRS, id_codeSpaceCRS}, authority, ns));
            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "referenceSystemInfo", "MD_ReferenceSystem", "referenceSystemIdentifier", "MD_Identifier", "description"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_referenceSystemInfo, id_MD_ReferenceSystem, id_referenceSystemIdentifier, id_MD_IdentifierCRS, id_descriptionCRS}, srcCRS.getName().toString(), ns));

            UUID id_extent = UUID.randomUUID();
            UUID id_EX_Extent = UUID.randomUUID();
            UUID id_description = UUID.randomUUID();
            UUID id_geographicElement = UUID.randomUUID();
            UUID id_EX_GeographicBoundingBox = UUID.randomUUID();
            UUID id_westBoundLongitude = UUID.randomUUID();
            UUID id_eastBoundLongitude = UUID.randomUUID();
            UUID id_southBoundLatitude = UUID.randomUUID();
            UUID id_northBoundLatitude = UUID.randomUUID();

            List<Double> extent = getExtent(collectionTransform);

            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "description"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_description}, "geographical extent in WGS84, EPSG:4326",ns));
            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "westBoundLongitude"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_geographicElement, id_EX_GeographicBoundingBox, id_westBoundLongitude}, extent.get(0).toString(), ns));
            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "eastBoundLongitude"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_geographicElement, id_EX_GeographicBoundingBox, id_eastBoundLongitude}, extent.get(1).toString(), ns));
            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "southBoundLatitude"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_geographicElement, id_EX_GeographicBoundingBox, id_southBoundLatitude}, extent.get(2).toString(), ns));
            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "northBoundLatitude"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_geographicElement, id_EX_GeographicBoundingBox, id_northBoundLatitude}, extent.get(3).toString(), ns));

            UUID id_EX_Extent_origCRS = UUID.randomUUID();
            UUID id_description_origCRS = UUID.randomUUID();
            UUID id_geographicElement_origCRS = UUID.randomUUID();
            UUID id_EX_GeographicBoundingBox_origCRS = UUID.randomUUID();
            UUID id_westBoundLongitude_origCRS = UUID.randomUUID();
            UUID id_eastBoundLongitude_origCRS = UUID.randomUUID();
            UUID id_southBoundLatitude_origCRS = UUID.randomUUID();
            UUID id_northBoundLatitude_origCRS = UUID.randomUUID();

            List<Double> extent_origCRS = getExtent(collection);

            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "description"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent_origCRS, id_description_origCRS}, "geographical extent in data CRS, EPSG:" + srcCRSepsg, ns));
            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "westBoundLongitude"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent_origCRS, id_geographicElement_origCRS, id_EX_GeographicBoundingBox_origCRS, id_westBoundLongitude_origCRS}, extent_origCRS.get(0).toString(), ns));
            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "eastBoundLongitude"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent_origCRS, id_geographicElement_origCRS, id_EX_GeographicBoundingBox_origCRS, id_eastBoundLongitude_origCRS}, extent_origCRS.get(1).toString(), ns));
            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "southBoundLatitude"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent_origCRS, id_geographicElement_origCRS, id_EX_GeographicBoundingBox_origCRS, id_southBoundLatitude_origCRS}, extent_origCRS.get(2).toString(), ns));
            content.add(nestedElement.create(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "northBoundLatitude"},
                    new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent_origCRS, id_geographicElement_origCRS, id_EX_GeographicBoundingBox_origCRS, id_northBoundLatitude_origCRS}, extent_origCRS.get(3).toString(), ns));


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return content;
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

    private List<Double> getExtent(FeatureCollection<SimpleFeatureType, SimpleFeature> collection) {
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
}
