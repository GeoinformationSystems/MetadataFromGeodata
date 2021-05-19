/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

import org.geokur.ISO19103Schema.Coordinate;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.gce.geotiff.GeoTiffFormatFactorySpi;
import org.geotools.referencing.CRS;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeoTIFF {
    String fileName;
    int dimensionality = 2;
    int numBands;
    int[] size = new int[dimensionality];
    Extent extent;
    Coordinate cornerLL = new Coordinate();
    Coordinate cornerLR = new Coordinate();
    Coordinate cornerUR = new Coordinate();
    Coordinate cornerUL = new Coordinate();
    SRSDescription srsDescription;
    String cellRepresentation;
    boolean resolutionIsotropy;
    double resolutionIsotropic;
    double[] resolutionAnisotropic;
    Double[] bandsNoDataValue;
    int[] numNoData;
    double[] bandsMinValues;
    double[] bandsMaxValues;
    int numCells;


    public GeoTIFF(String fileName) {
        this.fileName = fileName;
    }

    void open() {
        File file = new File(fileName);

//        AbstractGridFormat format = GridFormatFinder.findFormat(file);
        GeoTiffFormatFactorySpi geoTiffFormat = new GeoTiffFormatFactorySpi();
        AbstractGridFormat format = geoTiffFormat.createFormat();
        GridCoverage2DReader reader = format.getReader(file);

        try {
            GridCoverage2D coverage = reader.read(null);
            GridGeometry2D geometry = coverage.getGridGeometry();

            int axisX = geometry.axisDimensionX;
            int axisY = geometry.axisDimensionY;
            size[axisX] = geometry.getGridRange().getHigh(axisX) + 1;
            size[axisY] = geometry.getGridRange().getHigh(axisY) + 1;
            numCells = size[axisX] * size[axisY];

            Envelope envelope = coverage.getEnvelope();
            cornerLL.addX(envelope.getMinimum(axisX));
            cornerLL.addY(envelope.getMinimum(axisY));
            cornerLL.finalizeClass();
            cornerLR.addX(envelope.getMaximum(axisX));
            cornerLR.addY(envelope.getMinimum(axisY));
            cornerLR.finalizeClass();
            cornerUR.addX(envelope.getMaximum(axisX));
            cornerUR.addY(envelope.getMaximum(axisY));
            cornerUR.finalizeClass();
            cornerUL.addX(envelope.getMinimum(axisX));
            cornerUL.addY(envelope.getMaximum(axisY));
            cornerUL.finalizeClass();

            extent = new Extent();
            extent.west = envelope.getMinimum(axisX);
            extent.east = envelope.getMaximum(axisX);
            extent.south = envelope.getMinimum(axisY);
            extent.north = envelope.getMaximum(axisY);

            CoordinateReferenceSystem srcCRS = coverage.getCoordinateReferenceSystem2D();
            String srcCRSWKT = srcCRS.toWKT();
            srsDescription = getSrsDescription(srcCRSWKT.split("\n"));

            cellRepresentation = "Area";

            resolutionAnisotropic = reader.getResolutionLevels()[0];
            if (Math.abs(resolutionAnisotropic[0]) - Math.abs(resolutionAnisotropic[1]) > 1e-4) {
                // resolution is anisotropic
                resolutionIsotropy = false;
            } else {
                // resolution is isotropic
                resolutionIsotropy = true;
                resolutionIsotropic = Math.abs(resolutionAnisotropic[0]);
            }

            // content analysis of GeoTIFF
            numBands = coverage.getNumSampleDimensions();
            bandsNoDataValue = new Double[numBands];
            boolean nodataTag = true;
            for (int i = 0; i < numBands; i++) {
                // all bands should have the same nodata value https://svn.osgeo.org/gdal/trunk/gdal/frmts/gtiff/frmt_gtiff.html
                if (coverage.getSampleDimension(i).getNoDataValues() == null) {
                    // no TIFFTAG_GDAL_NODATA ASCII tag (code 42113) available
                    // -> also in tif image no nodata cells -> no test necessary
                    bandsNoDataValue[i] = null;
                    nodataTag = false;
                } else {
                    bandsNoDataValue[i] = coverage.getSampleDimension(i).getNoDataValues()[0];
                }
            }

            numNoData = new int[numBands];
            Arrays.fill(numNoData, 0);

            bandsMinValues = new double[numBands];
            bandsMaxValues = new double[numBands];

            Raster image = coverage.getRenderedImage().getData();
            double[] imageData = new double[size[axisX]]; // read whole rows
            double[] imageDataForMin = new double[size[axisX]]; // same as imageData with all nodata filled as Double.MAX_VALUE
            double[] imageDataForMax = new double[size[axisX]]; // same as imageData with all nodata filled as Double.MIN_VALUE
            double[] imageMinValues = new double[size[axisY]];
            double[] imageMaxValues = new double[size[axisY]];
            for (int i = 0; i < numBands; i++) {
                // get count of missing values for each band
                System.out.println("Read values of all GeoTIFF rows.");
                System.out.println("Depending on size this may take a while.");
                for (int j = 0; j < size[axisY]; j++) {
                    if (j%5000 == 0) {
                        System.out.println(size[axisY] + ": " + j);
                    }
                    image.getSamples(0, j, size[axisX], 1, i, imageData);
                    System.arraycopy(imageData, 0, imageDataForMin, 0, imageData.length);
                    System.arraycopy(imageData, 0, imageDataForMax, 0, imageData.length);
                    for (int k = 0; k < imageData.length; k++) {
                        double tmp = imageData[k];
                        if (nodataTag && Math.abs(tmp - bandsNoDataValue[i]) < Math.ulp(bandsNoDataValue[i])) {
                            // nodata values only possible, if nodataTag true
                            numNoData[i] = numNoData[i] + 1;
                            imageDataForMin[k] = Double.MAX_VALUE;
                            imageDataForMax[k] = Double.MIN_VALUE;
                        }
                    }
                    imageMinValues[j] = min(imageDataForMin);
                    imageMaxValues[j] = max(imageDataForMax);
                }
                bandsMinValues[i] = min(imageMinValues);
                bandsMaxValues[i] = max(imageMaxValues);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private double[][][] createRandomMatrix(int sizeX, int sizeY, int sizeZ) {
        // create matrix filled with random numbers

        double[][][] matrix = new double[sizeX][sizeY][sizeZ];
        Random random = new Random();
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                for (int k = 0; k < sizeZ; k++) {
                    matrix[i][j][k] = random.nextDouble();
                }
            }
        }

        return matrix;
    }

    private String findEpsgCode(CoordinateReferenceSystem srcCRS) {
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

        return matchingEPSGCode.get(idxMatching);
    }

    public SRSDescription getSrsDescription(String[] crsWKT) {
        // try to obtain crs authority and id from crs in wkt format

        SRSDescription srsDescription = new SRSDescription();

        // get indentation width in actual strings
        String indentationString = "";
        for (String line : crsWKT) {
            Pattern pattern = Pattern.compile("^\\s+\\w");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                indentationString = line.substring(matcher.start(), matcher.end() - 1);
                break;
            }
        }

        // get srs description
        for (String line : crsWKT) {
            String stringID = "^" + indentationString + "ID\\[";
            Pattern patternID = Pattern.compile(stringID, Pattern.CASE_INSENSITIVE);
            Matcher matcherID = patternID.matcher(line);

            String stringAUTH = "^" + indentationString + "AUTHORITY\\[";
            Pattern patternAUTH = Pattern.compile(stringAUTH, Pattern.CASE_INSENSITIVE);
            Matcher matcherAUTH = patternAUTH.matcher(line);

            if (matcherID.find() || matcherAUTH.find()) {
                String[] tmp = removeNullEntries(line.split("[ ,\"\\[\\]]"));
                srsDescription.setSrsOrganization(tmp[tmp.length - 2]);
                srsDescription.setSrsOrganizationCoordsysID(Integer.parseInt(tmp[tmp.length - 1]));
            }
        }

        String[] tmp = removeNullEntries(crsWKT[0].split("[,\"\\[\\]]"));
        srsDescription.setSrsName(tmp[tmp.length - 1]);

        return srsDescription;
    }

    public String[] removeNullEntries(String[] input) {
        // remove empty strings in input array

        List<String> outList = new ArrayList<>();
        for (String inputLine : input) {
            if (!inputLine.isBlank()) {
                outList.add(inputLine);
            }
        }

        return outList.toArray(new String[0]);
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

    private double min(double... numbers) {
        return Arrays.stream(numbers).min().orElse(Double.MIN_VALUE);
    }

    private double max(double... numbers) {
        return Arrays.stream(numbers).max().orElse(Double.MAX_VALUE);
    }
}
