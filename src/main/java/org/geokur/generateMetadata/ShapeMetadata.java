/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.generateMetadata;

import org.geokur.ISO19115Schema.*;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ShapeMetadata implements Metadata {

    String fileName;
    DS_DataSet dsDataSet;

    public ShapeMetadata(String fileName, DS_DataSet dsDataSet) {
        this.fileName = fileName;
        this.dsDataSet = dsDataSet;
    }

    public DS_Resource getMetadata() {
        // read shape file and put its metadata into DS_Resource

        File file = new File(fileName);

        try {
            FileDataStore dataStore = FileDataStoreFinder.getDataStore(file);
            SimpleFeatureCollection collection = dataStore.getFeatureSource().getFeatures();

            // coordinate reference system
            CoordinateReferenceSystem srcCRS = collection.getSchema().getCoordinateReferenceSystem();
            SimpleFeatureCollection collectionTransform;
            boolean markerTransform;
            MathTransform mathTransform = CRS.findMathTransform(srcCRS, DefaultGeographicCRS.WGS84, true);
            // transform to WGS84 for standard extent lon/lat
            if (!mathTransform.isIdentity()) {
                markerTransform = true;
                collectionTransform = project(collection, "epsg:4326");
            } else {
                markerTransform = false;
                collectionTransform = collection;
            }

            CI_Individual ciIndividual = new CI_Individual();
            ciIndividual.createName();
            ciIndividual.addName(System.getProperty("user.name"));
            ciIndividual.finalizeClass();

            CI_Responsibility ciResponsibility = new CI_Responsibility();
            ciResponsibility.createRole();
            ciResponsibility.addRole(new CI_RoleCode(CI_RoleCode.CI_RoleCodes.resourceProvider));
            ciResponsibility.createParty();
            ciResponsibility.addParty(ciIndividual);
            ciResponsibility.finalizeClass();

            String identifierCode = "pid:" + UUID.randomUUID().toString();
            MD_Identifier mdIdentifier = new MD_Identifier();
            mdIdentifier.createCode();
            mdIdentifier.addCode(identifierCode);
            mdIdentifier.finalizeClass();

            ZonedDateTime lastModified = ZonedDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault()); // local timezone
            lastModified = lastModified.withZoneSameInstant(ZoneId.of("UTC")); // convert to UTC timezone
            String lastModifiedString = lastModified.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME); // datetime in ISO 8601 format

            CI_Date ciDate_MD_Metadata = new CI_Date();
            ciDate_MD_Metadata.createDateType();
            ciDate_MD_Metadata.addDateType(new CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes.creation));
            ciDate_MD_Metadata.createDate();
            ciDate_MD_Metadata.addDate(lastModifiedString);
            ciDate_MD_Metadata.finalizeClass();

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

            MD_Identifier mdIdentifier_MD_ReferenceSystem = new MD_Identifier();
            mdIdentifier_MD_ReferenceSystem.createCode();
            mdIdentifier_MD_ReferenceSystem.addCode(authority + "::" + srcCRSepsg);
            mdIdentifier_MD_ReferenceSystem.createCodeSpace();
            mdIdentifier_MD_ReferenceSystem.addCodeSpace(authority);
            mdIdentifier_MD_ReferenceSystem.createDescription();
            mdIdentifier_MD_ReferenceSystem.addDescription(srcCRS.getName().toString());
            mdIdentifier_MD_ReferenceSystem.finalizeClass();

            MD_ReferenceSystem mdReferenceSystem = new MD_ReferenceSystem();
            mdReferenceSystem.createReferenceSystemIdentifier();
            mdReferenceSystem.addReferenceSystemIdentifier(mdIdentifier_MD_ReferenceSystem);
            mdReferenceSystem.finalizeClass();

            Extent extent = getExtent(collectionTransform);

            EX_GeographicBoundingBox exGeographicBoundingBox = new EX_GeographicBoundingBox();
            exGeographicBoundingBox.createWestBoundLongitude();
            exGeographicBoundingBox.addWestBoundLongitude(extent.west.toString());
            exGeographicBoundingBox.createEastBoundLongitude();
            exGeographicBoundingBox.addEastBoundLongitude(extent.east.toString());
            exGeographicBoundingBox.createSouthBoundLatitude();
            exGeographicBoundingBox.addSouthBoundLatitude(extent.south.toString());
            exGeographicBoundingBox.createNorthBoundLatitude();
            exGeographicBoundingBox.addNorthBoundLatitude(extent.north.toString());
            exGeographicBoundingBox.finalizeClass();

            EX_Extent exExtent = new EX_Extent();
            exExtent.createDescription();
            exExtent.addDescription("geographical extent in WGS84, EPSG:4326");
            exExtent.createGeographicElement();
            exExtent.addGeographicElement(exGeographicBoundingBox);
            exExtent.finalizeClass();

            Extent extentOrigCRS = getExtent(collection);

            EX_GeographicBoundingBox exGeographicBoundingBoxOrigCRS = new EX_GeographicBoundingBox();
            exGeographicBoundingBoxOrigCRS.createWestBoundLongitude();
            exGeographicBoundingBoxOrigCRS.addWestBoundLongitude(extentOrigCRS.west.toString());
            exGeographicBoundingBoxOrigCRS.createEastBoundLongitude();
            exGeographicBoundingBoxOrigCRS.addEastBoundLongitude(extentOrigCRS.east.toString());
            exGeographicBoundingBoxOrigCRS.createSouthBoundLatitude();
            exGeographicBoundingBoxOrigCRS.addSouthBoundLatitude(extentOrigCRS.south.toString());
            exGeographicBoundingBoxOrigCRS.createNorthBoundLatitude();
            exGeographicBoundingBoxOrigCRS.addNorthBoundLatitude(extentOrigCRS.north.toString());
            exGeographicBoundingBoxOrigCRS.finalizeClass();

            EX_Extent exExtentOrigCRS = new EX_Extent();
            exExtentOrigCRS.createDescription();
            exExtentOrigCRS.addDescription("geographical extent in data CRS, EPSG:" + srcCRSepsg);
            exExtentOrigCRS.createGeographicElement();
            exExtentOrigCRS.addGeographicElement(exGeographicBoundingBoxOrigCRS);
            exExtentOrigCRS.finalizeClass();

            String now = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

            CI_Date ciDate = new CI_Date();
            ciDate.createDateType();
            ciDate.addDateType(new CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes.creation));
            ciDate.createDate();
            ciDate.addDate(now);
            ciDate.finalizeClass();

            // TODO: informative title available?
            CI_Citation ciCitation = new CI_Citation();
            ciCitation.createTitle();
            ciCitation.addTitle("");
            ciCitation.createDate();
            ciCitation.addDate(ciDate);
            ciCitation.finalizeClass();

            // find all files belonging to actual shape file -> base name plus allowed extensions
            Shape shape = new Shape(file);
            shape.getShapeFileList();

            String environmentalDescription = "file name: " + fileName + "; "
                    + "all shape format files: " + shape.allFiles + "; "
                    + "file size: " + (int) shape.size + " B; "
                    + "os: " + System.getProperty("os.name");

            MD_DataIdentification mdDataIdentification = new MD_DataIdentification();
            mdDataIdentification.createCitation();
            mdDataIdentification.addCitation(ciCitation);
            mdDataIdentification.createEnvironmentalDescription();
            mdDataIdentification.addEnvironmentalDescription(environmentalDescription);
            mdDataIdentification.createExtent();
            mdDataIdentification.addExtent(exExtent);
            mdDataIdentification.addExtent(exExtentOrigCRS);
            mdDataIdentification.finalizeClass();

            MD_Metadata mdMetadata = new MD_Metadata();
            mdMetadata.createContact();
            mdMetadata.addContact(ciResponsibility);
            mdMetadata.createMetadataIdentifier();
            mdMetadata.addMetadataIdentifier(mdIdentifier);
            mdMetadata.createIdentificationInfo();
            mdMetadata.addIdentificationInfo(mdDataIdentification);
            mdMetadata.createDateInfo();
            mdMetadata.addDateInfo(ciDate_MD_Metadata);
            mdMetadata.createReferenceSystemInfo();
            mdMetadata.addReferenceSystemInfo(mdReferenceSystem);
            mdMetadata.finalizeClass();

            System.out.println();

            dsDataSet.createHas();
            dsDataSet.addHas(mdMetadata);
            dsDataSet.finalizeClass();

        } catch (IOException | FactoryException e) {
            System.out.println(e.getMessage());
        }

        return dsDataSet;
    }


    ////////////////////
    // helper methods //
    ////////////////////
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

    private Extent getExtent(FeatureCollection<SimpleFeatureType, SimpleFeature> collection) {
        // get extent of feature collection

        ReferencedEnvelope envelope = collection.getBounds();

        Extent extent = new Extent();
        extent.west = envelope.getMinX();
        extent.east = envelope.getMaxX();
        extent.south = envelope.getMinY();
        extent.north = envelope.getMaxY();

        return extent;
    }

//    private List<Double> getExtent(FeatureCollection<SimpleFeatureType, SimpleFeature> collection) {
//        // get extent of feature collection
//        // return list contains in this order: west boundary, east boundary, south boundary, north boundary
//
//        List<Double> extent = new ArrayList<>();
//        ReferencedEnvelope envelope = collection.getBounds();
//        extent.add(envelope.getMinX());
//        extent.add(envelope.getMaxX());
//        extent.add(envelope.getMinY());
//        extent.add(envelope.getMaxY());
//
//        return extent;
//    }

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
