/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.*;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.util.*;

public class Geopackage {
    String fileName;
    int contentAct;
    String contentName;
    File file;
    Statement statement;
    DataStore dataStore;
    SimpleFeatureCollection collection;
    SimpleFeatureCollection collectionTransform;
    CoordinateReferenceSystem srcCRS;
    String srcCRSepsg;
    String geometryType = "geometry";
    boolean markerTransform;
    List<Geometry> geometriesOrig = new ArrayList<>();
    List<Geometry> geometriesWGS84 = new ArrayList<>();
    List<Geometry> geometriesUTM = new ArrayList<>();
    List<Integer> zonesUTM = new ArrayList<>();
    double polygonPerKm2;

    public Geopackage(String fileName) {
        this.fileName = fileName;
    }

    public Geopackage(String fileName, int contentAct) {
        this.fileName = fileName;
        this.contentAct = contentAct;
    }

    public Geopackage(String fileName, String contentName) {
        this.fileName = fileName;
        this.contentName = contentName;
        this.contentAct = -999;
    }

    // overloaded method open
    void open() {
        openCore();
    }

    void open(int contentAct) {
        this.contentAct = contentAct;
        openCore();
    }

    void open(String contentName) {
        this.contentName = contentName;
        this.contentAct = -999;
        openCore();
    }

    void openCore() {
        // open geopackage and get the collection and a collection transformed to standard WGS84

        file = new File(fileName);

        // open geopackage as sqlite database
        Connection connection = getConnection();
        statement = getStatement(connection);

        // open geopackage with geotools
        Map<String, String> params = new HashMap<>();
        params.put("dbtype", "geopkg");
        params.put("database", file.toString());

        try {
            dataStore = DataStoreFinder.getDataStore(params);
            if (contentAct!=-999) {
                // defined number of layer in geopackage
                String typeName = dataStore.getTypeNames()[contentAct];
                collection = dataStore.getFeatureSource(typeName).getFeatures();
            } else {
                // defined name of layer in geopackage
                collection = dataStore.getFeatureSource(contentName).getFeatures();
            }
            srcCRS = collection.getSchema().getCoordinateReferenceSystem();

            MathTransform mathTransform = CRS.findMathTransform(srcCRS, DefaultGeographicCRS.WGS84, true);
            // transform to WGS84 for standard extent lon/lat
            if (!mathTransform.isIdentity()) {
                // transformation necessary
                markerTransform = true;
                collectionTransform = project(collection, "epsg:4326");
                findEpsgCode(srcCRS);
            } else {
                // special case for WGS84 - no test of CRS and no transformation necessary
                markerTransform = false;
                collectionTransform = collection;
                srcCRSepsg = "4326";
            }

            // get type of geometry
            SimpleFeatureIterator collectionIterator = collection.features();
            SimpleFeature simpleFeature = collectionIterator.next();
            String geometryTypeString = simpleFeature.getDefaultGeometry().toString().toLowerCase();
            if (geometryTypeString.contains("polygon")) {
                geometryType = "polygon";
            } else if (geometryTypeString.contains("line")) {
                geometryType = "line";
            } else if (geometryTypeString.contains("point")) {
                geometryType = "point";
            }
            collectionIterator.close();

            // reproject data
            projectToWGS84();
            projectToUTM();

        } catch (IOException | FactoryException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findEpsgCode(CoordinateReferenceSystem srcCRS) {
        // find correct epsg code for existing data

        String authority = "EPSG";

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
        int idxMatching = matchingLevenshteinDistance.indexOf(Collections.min(matchingLevenshteinDistance));
        srcCRSepsg = matchingEPSGCode.get(idxMatching);
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

    private void projectToWGS84() {
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
                    geometriesWGS84.add(JTS.transform(geometriesOrig.get(ct), mathTransformWGS84));
                }
                zonesUTM.add(findUTMZone(geometriesWGS84.get(ct)));
            }
            iterator.close();

        } catch (FactoryException | TransformException e) {
            System.out.println(e.getMessage());
        }
    }

    private void projectToUTM() {
        // reproject to UTM at correct zone

        // always use EPSG code for south hemisphere (no negative coordinates possible)
        try {
            for (int i = 0; i < geometriesWGS84.size(); i++) {
                CoordinateReferenceSystem UTMCRS = CRS.decode("EPSG:" + "327" + String.format("%02d", zonesUTM.get(i)));
                MathTransform mathTransformUTM = CRS.findMathTransform(DefaultGeographicCRS.WGS84, UTMCRS, true);
                geometriesUTM.add(JTS.transform(geometriesWGS84.get(i), mathTransformUTM));
            }
        } catch (FactoryException | TransformException e) {
            System.out.println(e.getMessage());
        }
    }

    List<FeatureDescriptor> getCenterArea() {
        // get centerpoints and area approximations of polygons
        // further get attributes

        List<FeatureDescriptor> featureDescriptors = new ArrayList<>();

        // get names of attributes
        List<AttributeDescriptor> attributeDescriptorList = collectionTransform.getSchema().getAttributeDescriptors();
        List<Integer> attributeUsage = new ArrayList<>();
        List<String> attributeNames = new ArrayList<>();
        int ct = -1;
        for (AttributeDescriptor attributeDescriptor : attributeDescriptorList) {
            ct++;
            String typeName = attributeDescriptor.getType().getName().toString();
            if (!typeName.equals("geom")) {
                attributeUsage.add(ct);
                attributeNames.add(attributeDescriptor.getLocalName());
            }
        }

        Point centerWGS84;
        double areaKm2WGS84;
        Point centerUTM = null;
        double areaKm2UTM = 0.0;

        try {
            SimpleFeatureIterator collectionIterator = collection.features();
            int ct2 = -1;
            while (collectionIterator.hasNext()) {
                // loop over all features in particular table in geopackage
                ct2++;

                List<String> attributeValues = new ArrayList<>();

                SimpleFeature simpleFeature = collectionIterator.next();
                for (Integer attributeAct : attributeUsage) {
                    // get attributes of actual feature
                    attributeValues.add(simpleFeature.getAttribute(attributeAct).toString());
                }

                Geometry geometryActWGS84;
                if (markerTransform) {
                    // fine granular transformation to EPSG:4326
                    CoordinateReferenceSystem srcCRS = collection.getSchema().getCoordinateReferenceSystem();
                    MathTransform mathTransformWGS84 = CRS.findMathTransform(srcCRS, DefaultGeographicCRS.WGS84, true);
                    Geometry geometryAct = (Geometry) simpleFeature.getDefaultGeometry();
                    geometryActWGS84 = JTS.transform(geometryAct.getBoundary(), mathTransformWGS84);
                } else {
                    // no transformation necessary
                    geometryActWGS84 = (Geometry) simpleFeature.getDefaultGeometry();
                }

                centerWGS84 = geometryActWGS84.getCentroid();
                double areaDegWGS84 = ((Geometry) simpleFeature.getDefaultGeometry()).getArea();
                areaKm2WGS84 = Math.toRadians(areaDegWGS84) * 637100; // approximation from degree area (WGS84) to km^2

                // transformation to UTM projection for better area calculation
                // get UTM zone and valid transformation
                int zoneUTM = (int) Math.ceil((centerWGS84.getX() + 180) / 6);
                if (zoneUTM == 0) {
                    zoneUTM = 1;
                }
                // always use EPSG code for south hemisphere (no negative coordinates possible)
                CoordinateReferenceSystem UTMCRS = CRS.decode("EPSG:" + "327" + String.format("%02d", zoneUTM));
                MathTransform mathTransformUTM = CRS.findMathTransform(DefaultGeographicCRS.WGS84, UTMCRS, true);

                // transform polygon geometry to valid UTM zone and calculate area
                Geometry geometryActUTM = JTS.transform(geometryActWGS84, mathTransformUTM);
                Coordinate[] coordinates = geometryActUTM.getCoordinates();
                Coordinate[] coordinatesClosed = getCoordinatesClosed(coordinates);
                GeometryFactory geometryFactory = new GeometryFactory();
                Polygon polygon = geometryFactory.createPolygon(coordinatesClosed);

                centerUTM = polygon.getCentroid();
                areaKm2UTM = polygon.getArea() / 1e6;

                FeatureDescriptor featureDescriptor = new FeatureDescriptor(ct2, centerWGS84, centerUTM, areaKm2WGS84,
                        areaKm2UTM, attributeNames, attributeValues);
                featureDescriptors.add(featureDescriptor);
            }
            collectionIterator.close();

        } catch (FactoryException | TransformException e) {
            System.out.println(e.getMessage());
        }

        return featureDescriptors;
    }

    double getResolution() {
        // get resolution
        // polygons: median of distance between adjacent border points over all polygons
        // TODO: lines: median of distance between adjacent line points over all lines
        // TODO: points: median of distance between all 2-point combinations

        List<Double> distances = new ArrayList<>();

        try {
            SimpleFeatureIterator collectionIterator = collection.features();
            while (collectionIterator.hasNext()) {
                // loop over all features in particular table in geopackage

                SimpleFeature simpleFeature = collectionIterator.next();

                Point centerWGS84 = ((Geometry) simpleFeature.getDefaultGeometry()).getCentroid();

                // transformation to UTM projection for better area calculation
                // get UTM zone and valid transformation
                int zoneUTM = (int) Math.ceil((centerWGS84.getX() + 180) / 6);
                if (zoneUTM == 0) {
                    zoneUTM = 1;
                }
                // always use EPSG code for south hemisphere (no negative coordinates possible)
                CoordinateReferenceSystem UTMCRS = CRS.decode("EPSG:" + "327" + String.format("%02d", zoneUTM));
                MathTransform mathTransformUTM = CRS.findMathTransform(DefaultGeographicCRS.WGS84, UTMCRS, true);

                if (geometryType.equals("polygon")) {
                    // transform polygon geometry to valid UTM zone and calculate area
                    Geometry geometryAct = (Geometry) simpleFeature.getDefaultGeometry();
                    Geometry geometryActUTM = JTS.transform(geometryAct.getBoundary(), mathTransformUTM);
                    Coordinate[] coordinates = geometryActUTM.getCoordinates();
                }
            }
            collectionIterator.close();

        } catch (FactoryException | TransformException e) {
            System.out.println(e.getMessage());
        }

        return 1.0;
    }

    void getPolygonPerArea(List<Double> areaKm2UTMAll) {
        // get quality criteria as number of polygons per 1000 square kilometer

        polygonPerKm2 = 0;
        if (geometryType.equals("polygon")) {
            double areaKm2UTMAllSum = 0;
            for (double areaAct : areaKm2UTMAll) {
                areaKm2UTMAllSum = areaKm2UTMAllSum + areaAct;
            }
            polygonPerKm2 = areaKm2UTMAll.size() / areaKm2UTMAllSum * 1000;
        }
    }


    ////////////////////
    // helper methods //
    ////////////////////
    void dispose() {
        // close/dispose database -> no more connection to collections
        dataStore.dispose();
    }

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

    int getContentIndex(String tableName) {
        // get index of table with tableName

        int contentIndex = -999;

        File file = new File(this.fileName);
        Map<String, String> params = new HashMap<>();
        params.put("dbtype", "geopkg");
        params.put("database", file.toString());

        try {
            dataStore = DataStoreFinder.getDataStore(params);
            String[] typeNames = dataStore.getTypeNames();
            contentIndex = Arrays.asList(typeNames).indexOf(tableName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return contentIndex;
    }


    SimpleFeatureCollection project(SimpleFeatureCollection collection, String epsgIdentifier) {
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

    Coordinate[] getCoordinatesClosed(Coordinate[] coordinates) {
        // close coordinate rings for full polygons
        // TODO: handle multipolygons

        int coordinatesLast = coordinates.length - 1;
        Coordinate[] coordinatesClosed = null;
        if (coordinates[0] != coordinates[coordinatesLast]) {
            // coordinates necessarily have to form a closed ring
            if (Math.abs(coordinates[0].x - coordinates[coordinatesLast].x) <= 2e-16 &&
                    Math.abs(coordinates[0].y - coordinates[coordinatesLast].y) <= 2e-16) {
                // basically the same values, but not identical
                coordinatesClosed = coordinates;
                coordinatesClosed[coordinatesLast] = coordinatesClosed[0];
            } else {
                // close polygon ring with adding the first coordinate
                coordinatesClosed = new Coordinate[coordinates.length + 1];
                System.arraycopy(coordinates, 0, coordinatesClosed, 0, coordinates.length);
                coordinatesClosed[coordinates.length] = coordinates[0];
            }
        }
        return coordinatesClosed;
    }

    String getDescription(Statement stmt, Integer contentAct) {
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

    Instant getLastChange(Statement stmt, Integer contentAct) {
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

    Integer getSRSID(Statement stmt, Integer contentAct) {
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

    String getSRSName(Statement stmt, Integer usedSRSID) {
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

    String getSRSOrganization(Statement stmt, Integer usedSRSID) {
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

    Integer getSRSOrganizationCoordsysID(Statement stmt, Integer usedSRSID) {
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

    String getDataType(Statement stmt, Integer contentAct) {
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

    Extent getExtent(SimpleFeatureCollection collection) {
        // get extent of feature collection

        ReferencedEnvelope envelope = collection.getBounds();

        Extent extent = new Extent();
        extent.west = envelope.getMinX();
        extent.east = envelope.getMaxX();
        extent.south = envelope.getMinY();
        extent.north = envelope.getMaxY();

        return extent;
    }

    Extent getExtentReproject(SimpleFeatureCollection collection) {
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

    Extent getExtent(Statement stmt, Integer contentAct) {
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

    String getTableName(Statement stmt, Integer contentAct) {
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

    int levenshteinDistance(String x, String y) {
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
