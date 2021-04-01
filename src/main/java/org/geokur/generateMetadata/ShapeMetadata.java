/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

import org.geokur.ISO19115Schema.*;
import org.geokur.ISO19157Schema.*;
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
import org.opengis.referencing.ReferenceIdentifier;
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

    Properties properties;
    String fileName;
    DS_DataSet dsDataSet;
    String geometryType = "geometry";
    boolean markerTransform;
    List<Geometry> geometriesOrig = new ArrayList<>(); // geometries in original CRS
    List<Geometry> geometriesWGS84 = new ArrayList<>(); // geometries in EPSG:4326
    List<Geometry> geometriesUTM = new ArrayList<>(); // geometries in best fitting UTM zone (ETRS89)
    List<Geometry> geometriesUTMStandard = new ArrayList<>(); // like UTM, but with one zone only for resolution calculation
    List<Integer> zonesUTM = new ArrayList<>();
    int zoneUTMStandard;

    public ShapeMetadata(Properties properties, DS_DataSet dsDataSet) {
        this.properties = properties;
        this.fileName = properties.geodata;
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
            String geometryTypeString = simpleFeature.getDefaultGeometry().toString().toLowerCase();
            if (geometryTypeString.contains("polygon")) {
                geometryType = "polygon";
            } else if (geometryTypeString.contains("line")) {
                geometryType = "line";
            } else if (geometryTypeString.contains("point")) {
                geometryType = "point";
            }
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
                matchingLevenshteinDistance.add(Integer.MAX_VALUE); // initialize

                System.out.println("Comparison of CRS to find the correct EPSG identifier");
//                System.out.print("[          ]");
//                int ct = 0;
//                int ct2 = 0;
                for (String supportedCode : supportedCodes) {
//                    ct++;
                    if (!supportedCode.matches("[0-9]+")) {
                        // only interpret numerical epsg codes (the first supported code might be WGS84 as text)
                        continue;
                    }
//                    if ((ct % (supportedCodes.size() / 10)) == 0) {
//                        // construct wait bar on console output
//                        ct2++;
//                        System.out.print("\b\b\b\b\b\b\b\b\b\b\b");
//                        for (int i = 1; i <= ct2; i++) {
//                            System.out.print("#");
//                        }
//                        for (int i = ct2 + 1; i <= 10; i++) {
//                            System.out.print(" ");
//                        }
//                        System.out.print("]");
//                    }
                    try {
                        CoordinateReferenceSystem actCRS = CRS.decode(authority + ":" + supportedCode);
                        // usage of MathTransform unsafe -> if possible do not use
//                        MathTransform mathTransformFind = CRS.findMathTransform(srcCRS, actCRS, true);
//                        if (mathTransformFind.isIdentity()) {
//                            matchingEPSGCode.add(supportedCode);
//                            String actCRSName = actCRS.getName().toString();
//                            matchingLevenshteinDistance.add(levenshteinDistance(srcCRSName, actCRSName));
//                        }
                        int levDist = levenshteinDistance(srcCRS.getName().getCode().toLowerCase().replaceAll("[ _/]", ""),
                                actCRS.getName().getCode().toLowerCase().replaceAll("[ _/]", ""));
                        if (levDist < matchingLevenshteinDistance.get(0)) {
                            // new minimum
                            matchingLevenshteinDistance.clear();
                            matchingLevenshteinDistance.add(levDist);
                            matchingEPSGCode.clear();
                            matchingEPSGCode.add(supportedCode);
                        } else if (levDist == matchingLevenshteinDistance.get(0)) {
                            // add to minimum list
                            matchingLevenshteinDistance.add(levDist);
                            matchingEPSGCode.add(supportedCode);
                        }
                    } catch (FactoryException ignored) {
                    }
                }
//                int idxMatching = matchingLevenshteinDistance.indexOf(Collections.min(matchingLevenshteinDistance));
                if (matchingLevenshteinDistance.size() > 1) {
                    System.out.println("More than one appropriate EPSG number were found. The first one is taken");
                }
                srcCRSepsg = matchingEPSGCode.get(0);
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
            if (markerTransform && geometryType.equals("polygon")) {
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

            // get spatial resolution
            MD_Resolution mdResolution = new MD_Resolution();
            mdResolution.addDistance(getResolution(collection));
            mdResolution.finalizeClass();

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
            mdDataIdentification.addSpatialRepresentationType(new MD_SpatialRepresentationTypeCode(MD_SpatialRepresentationTypeCode.MD_SpatialRepresentationTypeCodes.vector));
            mdDataIdentification.addSpatialResolution(mdResolution);
            mdDataIdentification.finalizeClass();


            // get (4) data quality
            System.out.println("Data Quality:");

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
            mdMetadata.addContact(ciResponsibility);
            mdMetadata.addMetadataIdentifier(mdIdentifier);
            mdMetadata.addDateInfo(ciDateCreation);
            mdMetadata.addDateInfo(ciDateLastModified);
            mdMetadata.addIdentificationInfo(mdDataIdentification);
            mdMetadata.addReferenceSystemInfo(mdReferenceSystem);
            mdMetadata.addMetadataStandard(ciCitationMetadataStandard);
            mdMetadata.addDataQualityInfo(dqDataQuality);
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

    private int findUTMZone(Geometry geometry) {
        // find correct UTM zone for areal and other calculations

        int zoneUTM;
        zoneUTM = (int) Math.ceil((geometry.getCentroid().getX() + 180) / 6);
        if (zoneUTM == 0) {
            zoneUTM = 1;
        }

        return zoneUTM;
    }

    private void projectToWGS84(SimpleFeatureCollection collection) {
        // reproject to WGS84 if necessary

        SimpleFeatureIterator iterator = collection.features();
        CoordinateReferenceSystem srcCRS = collection.getSchema().getCoordinateReferenceSystem();
        try {
            MathTransform mathTransformWGS84 = CRS.findMathTransform(srcCRS, DefaultGeographicCRS.WGS84, true);

            int ct = -1;
            while (iterator.hasNext()) {
                ct++;
                SimpleFeature simpleFeature = iterator.next();
                geometriesOrig.add((Geometry) simpleFeature.getDefaultGeometry());
                if (!markerTransform) {
                    geometriesWGS84.add(geometriesOrig.get(ct));
                } else {
                    if (geometryType.equals("polygon")) {
                        Coordinate[] coordinatesWGS84 = JTS.transform(geometriesOrig.get(ct).getBoundary(), mathTransformWGS84).getCoordinates();
                        GeometryFactory geometryFactory = new GeometryFactory();
                        geometriesWGS84.add(geometryFactory.createPolygon(coordinatesWGS84));
//                        geometriesWGS84.add(JTS.transform(geometriesOrig.get(ct).getBoundary(), mathTransformWGS84));
                    } else {
                        geometriesWGS84.add(JTS.transform(geometriesOrig.get(ct), mathTransformWGS84));
                    }
                }
                zonesUTM.add(findUTMZone(geometriesWGS84.get(ct)));
            }
            iterator.close();

            zoneUTMStandard = median(zonesUTM.toArray(new Integer[0]));

        } catch (FactoryException | TransformException e) {
            System.out.println(e.getMessage());
        }
    }

    private void projectToUTM() {
        // reproject to UTM at correct zone and to median zone

        // always use EPSG code for south hemisphere (no negative coordinates possible)
        try {
            for (int i = 0; i < geometriesWGS84.size(); i++) {
                CoordinateReferenceSystem UTMCRS = CRS.decode("EPSG:" + "327" + String.format("%02d", zonesUTM.get(i)));
                MathTransform mathTransformUTM = CRS.findMathTransform(DefaultGeographicCRS.WGS84, UTMCRS, true);
                if (geometryType.equals("polygon")) {
                    Coordinate[] coordinatesUTM = JTS.transform(geometriesWGS84.get(i).getBoundary(), mathTransformUTM).getCoordinates();
                    GeometryFactory geometryFactory = new GeometryFactory();
                    geometriesUTM.add(geometryFactory.createPolygon(coordinatesUTM));
//                    geometriesUTM.add(JTS.transform(geometriesWGS84.get(i).getBoundary(), mathTransformUTM));
                } else {
                    geometriesUTM.add(JTS.transform(geometriesWGS84.get(i), mathTransformUTM));
                }

                CoordinateReferenceSystem UTMCRSStandard = CRS.decode("EPSG:" + "327" + String.format("%02d", zoneUTMStandard));
                MathTransform mathTransformUTMStandard = CRS.findMathTransform(DefaultGeographicCRS.WGS84, UTMCRSStandard, true);
                if (geometryType.equals("polygon")) {
                    Coordinate[] coordinatesUTM = JTS.transform(geometriesWGS84.get(i).getBoundary(), mathTransformUTMStandard).getCoordinates();
                    GeometryFactory geometryFactory = new GeometryFactory();
                    geometriesUTMStandard.add(geometryFactory.createPolygon(coordinatesUTM));
//                    geometriesUTMStandard.add(JTS.transform(geometriesWGS84.get(i).getBoundary(), mathTransformUTMStandard));
                } else {
                    geometriesUTMStandard.add(JTS.transform(geometriesWGS84.get(i), mathTransformUTMStandard));
                }
            }
        } catch (FactoryException | TransformException e) {
            System.out.println(e.getMessage());
        }
    }

    private double getResolution(SimpleFeatureCollection collection) {
        // get resolution
        // polygons/lines: median of distance between adjacent border points over all polygons
        // points: mean distance between points calculated as sqrt(A/n) - with area A of convex hull over all n points

        projectToWGS84(collection);
        projectToUTM();

        List<Double> distances = new ArrayList<>();
        if (geometryType.equals("polygon") || geometryType.equals("line")) {
            // for polygons and lines
            for (Geometry geometryAct : geometriesUTM) {
                Coordinate[] tmp = geometryAct.getCoordinates();
                for (int i = 0; i < tmp.length - 1; i++) {
                    distances.add(Math.sqrt(Math.pow(tmp[i].x - tmp[i + 1].x, 2) + Math.pow(tmp[i].y - tmp[i + 1].y, 2)) / 1e3); // distance in km
                }
            }
        } else {
            // for points
            List<Coordinate> points = new ArrayList<>();
            for (Geometry geometryAct : geometriesUTMStandard) {
                points.add((new CoordinateXY(geometryAct.getCoordinate().x, geometryAct.getCoordinate().y)));
            }

            Coordinate[] pointsConvHull = getConvexHull(points);
            GeometryFactory geometryFactory = new GeometryFactory();
            Polygon polygon = geometryFactory.createPolygon(pointsConvHull);

            // correction factor of distance depending on number of points
            // due to misinterpreting of convex hull -> additional buffer necessary
            int n = geometriesUTMStandard.size();
            double factorCorrection = Math.sqrt(n / Math.pow(Math.sqrt(n) - 1, 2));

            distances.add(factorCorrection * Math.sqrt(polygon.getArea() / n / 1e6)); // corrected distance in km
        }

        return median(distances.toArray(new Double[0]));
    }

    private double median(Double[] values) {
        // calculation of the median of a list

        double medianVal;
        int numValues = values.length;
        Arrays.sort(values);
        if (numValues % 2 == 1) {
            // odd number of elements
            int medianIdx = numValues/2; // always similar to Math.floor
            medianVal = values[medianIdx];
        } else {
            // even number of elements - mean between elements in the middle
            int medianIdxUpper = numValues/2;
            int medianIdxLower = medianIdxUpper - 1;
            medianVal = (values[medianIdxUpper] + values[medianIdxLower]) / 2;
        }

        return medianVal;
    }

    private int median(Integer[] values) {
        // calculation of the median of a list

        int medianVal;
        int numValues = values.length;
        Arrays.sort(values);
        if (numValues % 2 == 1) {
            // odd number of elements
            int medianIdx = numValues/2; // always similar to Math.floor
            medianVal = values[medianIdx];
        } else {
            // even number of elements - mean between elements in the middle
            int medianIdxUpper = numValues/2;
            int medianIdxLower = medianIdxUpper - 1;
            medianVal = (values[medianIdxUpper] + values[medianIdxLower]) / 2;
        }

        return medianVal;
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

    private Coordinate[] getConvexHull(List<Coordinate> points) {
        // calculate convex hull following Jarvis march algorithm (gift wrapping)

        List<Coordinate> edgePoints = new ArrayList<>();
        double yMax = Double.NEGATIVE_INFINITY;
        int idxNorthest = 0;
        int ct = -1;

        // get northest point as starting point
        for (Coordinate tmp : points) {
            ct++;
            if (tmp.y > yMax) {
                yMax = tmp.y;
                idxNorthest = ct;
            }
        }
        edgePoints.add(points.get(idxNorthest));
        points.remove(idxNorthest);

        // go counter clockwise around points, the next point has the lowest angle with Math.atan2 function
        List<Double> anglesDetected = new ArrayList<>();
        anglesDetected.add(-180.0); // no angle for first value
        ct = -1;
        do {
            ct++;
            Coordinate lastPoint = edgePoints.get(edgePoints.size() - 1);
            List<Double> angles = new ArrayList<>();
            for (Coordinate point : points) {
                angles.add(Math.atan2(point.y - lastPoint.y, point.x - lastPoint.x) * 180 / Math.PI);
            }

            // get logical array with true if larger than previous detected angle
            List<Boolean> angleCondition = new ArrayList<>();
            for (Double angle : angles) {
                if (angle > anglesDetected.get(anglesDetected.size() - 1)) {
                    angleCondition.add(true);
                } else {
                    angleCondition.add(false);
                }
            }

            // find lowest angle from list, but larger than previous angle
            int idxNext = 0;
            double angleMin = Double.MAX_VALUE;
            for (int i = 0; i < angles.size(); i++) {
                if (angleCondition.get(i) && angles.get(i) < angleMin) {
                    angleMin = angles.get(i);
                    idxNext = i;
                }
            }

            edgePoints.add(points.get(idxNext));
            anglesDetected.add(angles.get(idxNext));
            points.remove(idxNext);

            if (ct == 0) {
                // add first point after first iteration
                // it has to be available for the search of the last edge of the convex hull
                points.add(edgePoints.get(0));
            }

        } while (!edgePoints.get(edgePoints.size() - 1).equals(edgePoints.get(0)));

        Coordinate[] edgeCoordinates = new Coordinate[edgePoints.size()];
        for (int i = 0; i < edgePoints.size(); i++) {
            edgeCoordinates[i] = edgePoints.get(i);
        }

        return edgeCoordinates;
    }

    DQ_FormatConsistency makeDQFormatConsistency(List<String> allowedFileFormat, String now) {
        // adherence to data format given, if shp available in properties.allowedFileFormat

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
        dqConformanceResult.addPass(allowedFileFormat.stream().anyMatch("shp"::equalsIgnoreCase));
        dqConformanceResult.finalizeClass();

        DQ_FormatConsistency dqFormatConsistency = new DQ_FormatConsistency();
        dqFormatConsistency.addEvaluationMethod(dqEvaluationMethod);
        dqFormatConsistency.addResult(dqConformanceResult);
        dqFormatConsistency.finalizeClass();

        return dqFormatConsistency;
    }
}
