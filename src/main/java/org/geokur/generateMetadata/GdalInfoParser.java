/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

import org.geokur.ISO19103Schema.Coordinate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GdalInfoParser {
    private final String[] tikaMetadataSplit;
    int dimensionality;
    int numBands;
    private Integer[] idxBands;
    private List<Boolean> idxIndent;
    Coordinate cornerLL;
    Coordinate cornerLR;
    Coordinate cornerUR;
    Coordinate cornerUL;

    public GdalInfoParser(String tikaMetadata) {
        tikaMetadataSplit = tikaMetadata.split("\n");
        getDimensionality();
        getIndentationIndex();
    }

    private void getDimensionality() {
        // get number of dimensions and bands

        for (String line : tikaMetadataSplit) {
            Pattern pattern = Pattern.compile("^size is", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String[] parts = removeNullEntries(line.split("[ ,]"));
                dimensionality = parts.length - 2;
                break;
            }
        }

        numBands = 0;
        List<Integer> idxBandsList = new ArrayList<>();
        int ct = -1;
        for (String line : tikaMetadataSplit) {
            ct++;
            Pattern pattern = Pattern.compile("^band", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                numBands++;
                idxBandsList.add(ct);
            }
        }
        idxBands = idxBandsList.toArray(new Integer[0]);
    }

    private void getIndentationIndex() {
        // get index list of indentation in gdalinfo result

        idxIndent = new ArrayList<>();
        for (String line : tikaMetadataSplit) {
            Pattern pattern = Pattern.compile("^\\s", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            idxIndent.add(matcher.find());
        }
    }

    public int[] getSize() {
        // get raster size nx by ny

        int[] size = new int[dimensionality];
        for (String line : tikaMetadataSplit) {
            Pattern pattern = Pattern.compile("^size is", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String[] parts = removeNullEntries(line.split("[ ,]"));
                for (int i = 0; i < dimensionality; i++) {
                    size[i] = Integer.parseInt(parts[2 + i]);
                }
                break;
            }
        }

        return size;
    }

    public Extent getExtent() {
        // get raster extent

        Extent extent = new Extent();

        int idxCornerCoordinates = -1;
        int ct = -1;
        for (String line : tikaMetadataSplit) {
            ct++;
            Pattern pattern = Pattern.compile("^corner coordinates:", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                idxCornerCoordinates = ct;
                break;
            }
        }

        if (idxCornerCoordinates != -1) {
            // put the next four lines after "Corner Coordinates:" into a new list
            String[] metaCornerCoordinates = Arrays.copyOfRange(tikaMetadataSplit, idxCornerCoordinates + 1, idxCornerCoordinates + 5);

            for (String line : metaCornerCoordinates) {
                if (line.toLowerCase().contains("lower left")) {
                    String[] parts = line.split("[()]");
                    String[] coords = parts[1].split(",");
                    extent.west = Double.parseDouble(coords[0]);
                    extent.south = Double.parseDouble(coords[1]);

                    cornerLL = new Coordinate();
                    cornerLL.addX(Double.parseDouble((coords[0])));
                    cornerLL.addY(Double.parseDouble((coords[1])));
                    cornerLL.finalizeClass();
                }

                if (line.toLowerCase().contains("lower right")) {
                    String[] parts = line.split("[()]");
                    String[] coords = parts[1].split(",");

                    cornerLR = new Coordinate();
                    cornerLR.addX(Double.parseDouble((coords[0])));
                    cornerLR.addY(Double.parseDouble((coords[1])));
                    cornerLR.finalizeClass();
                }

                if (line.toLowerCase().contains("upper right")) {
                    String[] parts = line.split("[()]");
                    String[] coords = parts[1].split(",");
                    extent.east = Double.parseDouble(coords[0]);
                    extent.north = Double.parseDouble(coords[1]);

                    cornerUR = new Coordinate();
                    cornerUR.addX(Double.parseDouble((coords[0])));
                    cornerUR.addY(Double.parseDouble((coords[1])));
                    cornerUR.finalizeClass();
                }

                if (line.toLowerCase().contains("upper left")) {
                    String[] parts = line.split("[()]");
                    String[] coords = parts[1].split(",");

                    cornerUL = new Coordinate();
                    cornerUL.addX(Double.parseDouble((coords[0])));
                    cornerUL.addY(Double.parseDouble((coords[1])));
                    cornerUL.finalizeClass();
                }
            }
        }

        return extent;
    }

    public SRSDescription getSRSDescription() {
        // obtain parameters of srs description

        SRSDescription srsDescription = new SRSDescription();
        int idxSRS = -1;
        int ct = -1;
        for (String line : tikaMetadataSplit) {
            ct++;
            Pattern pattern = Pattern.compile("^coordinate system is:", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                idxSRS = ct + 1;
            }
        }

        if (idxSRS == -1) {
            // no description of srs available -> stop evaluation here
            return srsDescription;
        }

        // parse srs description
        List<String> metaSRS = new ArrayList<>();
        for (int i = idxSRS; i < tikaMetadataSplit.length; i++) {
            metaSRS.add(tikaMetadataSplit[i]);
            if (i >= tikaMetadataSplit.length - 1 || !idxIndent.get(i + 1)) {
                // end of indented lines and therefore end of srs description
                String[] tmp = metaSRS.get(0).split("\"");
                srsDescription.setSrsName(tmp[1]);
                break;
            }
        }

        for (String line : metaSRS) {
            Pattern pattern = Pattern.compile("^\\s{4}\\bid\\b", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String[] tmp = removeNullEntries(line.split("[ ,\"\\[\\]]"));
                srsDescription.setSrsOrganization(tmp[1]);
                srsDescription.setSrsOrganizationCoordsysID(Integer.parseInt(tmp[2]));
                break;
            }
        }

        return srsDescription;
    }

    public double[] getResolution() {
        // get x and y resolution of raster data

        double[] resolution = new double[dimensionality];
        for (String line : tikaMetadataSplit) {
            Pattern pattern = Pattern.compile("^pixel size", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String[] parts = removeNullEntries(line.split("[=(),]"));
                for (int i = 0; i < dimensionality; i++) {
                    resolution[i] = Double.parseDouble(parts[1 + i]);
                }
                break;
            }
        }

        return resolution;
    }

    public String getCellRepresentation() {
        // get the fashion of cell representation (point or area)

        String cellRepresentation = "";
        int idxMetadata = -1;
        int ct = -1;
        for (String line : tikaMetadataSplit) {
            ct++;
            Pattern pattern = Pattern.compile("^metadata:", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                idxMetadata = ct + 1;
            }
        }

        if (idxMetadata == -1) {
            // no description of srs available -> stop evaluation here
            return cellRepresentation;
        }

        // parse metadata element
        List<String> metadata = new ArrayList<>();
        for (int i = idxMetadata; i < tikaMetadataSplit.length; i++) {
            metadata.add(tikaMetadataSplit[i]);
            if (i >= tikaMetadataSplit.length - 1 || !idxIndent.get(i + 1)) {
                // end of indented lines and therefore end of metadata element
                break;
            }
        }

        for (String line : metadata) {
            Pattern pattern = Pattern.compile("^\\s{2}area_or_point=", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String[] tmp = removeNullEntries(line.split("="));
                cellRepresentation = tmp[1];
                break;
            }
        }

        return cellRepresentation;
    }

    public double[] getNoDataValue() {
        // get NoData values for each band

        double[] noDataValue = new double[numBands];
        for (String line : tikaMetadataSplit) {
            for (int i = 0; i < numBands; i++) {
                Pattern pattern = Pattern.compile("^band " + (i + 1), Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    // get sublist of band element
                    List<String> band = new ArrayList<>();
                    for (int j = idxBands[i]; j < tikaMetadataSplit.length; j++) {
                        // sublist with all indented lines after band
                        band.add(tikaMetadataSplit[j]);
                        if (j >= tikaMetadataSplit.length - 1 || !idxIndent.get(j + 1)) {
                            // end of indented lines and therefore end of band element
                            break;
                        }
                    }

                    // parse band element
                    for (String bandLine: band) {
                        Pattern pattern1 = Pattern.compile("nodata value", Pattern.CASE_INSENSITIVE);
                        Matcher matcher1 = pattern1.matcher(bandLine);
                        if (matcher1.find()) {
                            String[] noDataString = removeNullEntries(bandLine.split("[ =]"));
                            noDataValue[i] = Double.parseDouble(noDataString[noDataString.length - 1]);
                        }
                    }
                }
            }
        }

        return noDataValue;
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

}
