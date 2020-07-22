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
        String fileName = "TestPointsShapeETRS.shp";
        String documentVariant = "minimal";
        boolean writeLogFile = false;
        boolean writeXMLFile = true;

        System.out.println("-----------------");
        System.out.println("Generate Metadata");
        System.out.println("-----------------");
        System.out.println("File chosen: " + fileName);
        System.out.println("Write log file: " + writeLogFile);
        System.out.println();

        Metadata tmp = new Metadata();
        Document doc = tmp.create(fileName, documentVariant, writeLogFile, writeXMLFile);
    }
}
