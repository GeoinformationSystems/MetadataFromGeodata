/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

import org.geokur.ISO19115Schema.*;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.*;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
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

            // find out whether we have a polygon or points
            SimpleFeatureIterator iterator = collection.features();
            SimpleFeature simpleFeature = iterator.next();
            String geometryType = simpleFeature.getDefaultGeometry().toString().toLowerCase();
            boolean markerPoint = geometryType.contains("point");
            iterator.close();


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
            mdIdentifier_MD_ReferenceSystem.addCode(authority + "::" + srcCRSepsg);
            mdIdentifier_MD_ReferenceSystem.addCodeSpace(authority);
            mdIdentifier_MD_ReferenceSystem.addDescription(srcCRS.getName().toString());
            mdIdentifier_MD_ReferenceSystem.finalizeClass();

            MD_ReferenceSystem mdReferenceSystem = new MD_ReferenceSystem();
            mdReferenceSystem.addReferenceSystemIdentifier(mdIdentifier_MD_ReferenceSystem);
            mdReferenceSystem.finalizeClass();


            // get (3) structure of spatial data
            System.out.println("Structure of Spatial Data:");

            Extent extent;
            if (markerTransform && !markerPoint) {
                // polygons need finer granular reprojecting for extent calculation
                extent = getExtentReproject(collection);
            } else if (markerTransform) {
                extent = getExtent(collectionTransform);
            } else {
                extent = getExtent(collection);
            }

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

            Extent extentOrigCRS = getExtent(collection);

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

            // find all files belonging to actual shape file -> base name plus allowed extensions
            Shape shape = new Shape(file);
            shape.getShapeFileList();

            String environmentalDescription = "file name: " + fileName + "; "
                    + "all shape format files: " + shape.allFiles + "; "
                    + "file size: " + (int) shape.size + " B; "
                    + "os: " + System.getProperty("os.name");

            MD_DataIdentification mdDataIdentification = new MD_DataIdentification();
            mdDataIdentification.addCitation(ciCitation);
            mdDataIdentification.addEnvironmentalDescription(environmentalDescription);
            mdDataIdentification.addExtent(exExtent);
            mdDataIdentification.addExtent(exExtentOrigCRS);
            mdDataIdentification.finalizeClass();


            // get (4) data quality
            System.out.println("Data Quality:");


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
            mdMetadata.finalizeClass();

            System.out.println();

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

    private Extent getExtent(SimpleFeatureCollection collection) {
        // get extent of feature collection

        Envelope envelope = collection.getBounds();

        Extent extent = new Extent();
        extent.west = envelope.getMinX();
        extent.east = envelope.getMaxX();
        extent.south = envelope.getMinY();
        extent.north = envelope.getMaxY();

        return extent;
    }

    private Extent getExtentReproject(SimpleFeatureCollection collection) {
        // get extent of feature collection while reprojected

        CoordinateReferenceSystem srcCRS = collection.getSchema().getCoordinateReferenceSystem();
        Envelope envelope = new Envelope();

        try {
            SimpleFeatureIterator collectionIterator = collection.features();
            while (collectionIterator.hasNext()) {
                // loop over all features and extent envelope
                SimpleFeature simpleFeature = collectionIterator.next();

                // transformation to target projection
                MathTransform mathTransform = CRS.findMathTransform(srcCRS, DefaultGeographicCRS.WGS84, true);
                Geometry geometryAct = (Geometry) simpleFeature.getDefaultGeometry();
                Geometry geometryActTransform = JTS.transform(geometryAct.getBoundary(), mathTransform);
                envelope.expandToInclude(geometryActTransform.getEnvelopeInternal());
            }
            collectionIterator.close();

        } catch (FactoryException | TransformException e) {
            System.out.println(e.getMessage());
        }

        Extent extent = new Extent();
        extent.west = envelope.getMinX();
        extent.east = envelope.getMaxX();
        extent.south = envelope.getMinY();
        extent.north = envelope.getMaxY();

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
