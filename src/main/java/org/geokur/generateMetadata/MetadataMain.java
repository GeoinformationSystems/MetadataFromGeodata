/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.generateMetadata;

import org.jdom2.Document;

public class MetadataMain {
    public static void main(String[] argv) {
        // main method to generate metadata from geodata
//        String fileName = "rasterExample.gpkg";
//        String fileName = "TestGeopackage.gpkg";
        String fileName = "paraguay.gpkg";
//        String fileName = "TestPointsShape.shp";
//        String fileName = "paraguay.csv";
        String documentVariant = "minimal";
        boolean writeLogFile = false;
        boolean writeXMLFile = true;

        Metadata metadata = new Metadata();
        Document doc = metadata.create(fileName, documentVariant, writeLogFile, writeXMLFile);
    }
}
