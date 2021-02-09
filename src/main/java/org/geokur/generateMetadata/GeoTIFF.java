/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.gdal.GDALParser;
import org.apache.tika.sax.BodyContentHandler;
import org.geokur.ISO19103Schema.Coordinate;
import org.geotools.data.DataSourceException;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GeoTIFF {
    String fileName;
    int dimensionality;
    int numBands;
    int[] size;
    Extent extent;
    Coordinate cornerLL;
    Coordinate cornerLR;
    Coordinate cornerUR;
    Coordinate cornerUL;
    SRSDescription srsDescription;
    String cellRepresentation;
    boolean resolutionIsotropy;
    double resolutionIsotropic;
    double[] resolutionAnisotropic;
    double[] bandsNoDataValue;


    public GeoTIFF(String fileName) {
        this.fileName = fileName;
    }

    void open() {
        File file = new File(fileName);

        GDALParser parser = new GDALParser();
        BodyContentHandler handler = new BodyContentHandler();
        org.apache.tika.metadata.Metadata metaTika = new org.apache.tika.metadata.Metadata();
        ParseContext context = new ParseContext();

        try {
            InputStream stream = new FileInputStream(file);
            parser.parse(stream, handler, metaTika, context);
            String parsedStuff = handler.toString();

            GdalInfoParser gdalInfoParser = new GdalInfoParser(parsedStuff);
            dimensionality = gdalInfoParser.dimensionality;
            numBands = gdalInfoParser.numBands;
            size = gdalInfoParser.getSize();
            extent = gdalInfoParser.getExtent();
            cornerLL = gdalInfoParser.cornerLL;
            cornerLR = gdalInfoParser.cornerLR;
            cornerUR = gdalInfoParser.cornerUR;
            cornerUL = gdalInfoParser.cornerUL;
            srsDescription = gdalInfoParser.getSRSDescription();
            cellRepresentation = gdalInfoParser.getCellRepresentation();
            resolutionAnisotropic = gdalInfoParser.getResolution();
            bandsNoDataValue = gdalInfoParser.getNoDataValue();


            if (Math.abs(resolutionAnisotropic[0]) - Math.abs(resolutionAnisotropic[1]) > 1e-10) {
                // resolution is anisotropic
                resolutionIsotropy = false;
            } else {
                // resolution is isotropic
                resolutionIsotropy = true;
                resolutionIsotropic = Math.abs(resolutionAnisotropic[0]);
            }

        } catch (IOException | SAXException | TikaException e) {
            System.out.println(e.getMessage());
        }

        //TODO: ??????????????????
        try {
            GeoTiffReader reader = new GeoTiffReader(file);
            System.out.println(reader.toString());
        } catch (DataSourceException e) {
            System.out.println(e.getMessage());
        }

    }
}
