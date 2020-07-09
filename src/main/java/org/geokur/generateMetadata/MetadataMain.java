/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.generateMetadata;

import org.jdom2.Document;

public class MetadataMain {
    public static void main(String[] argv) {
        // main method to generate metadata from geodata
//        String geopackageName = "rasterExample.gpkg";
        String geopackageName = "TestGeopackage.gpkg";
        String documentVariant = "minimal";
        boolean writeLogFile = false;
        boolean writeXMLFile = false;

        System.out.println("-----------------");
        System.out.println("Generate Metadata");
        System.out.println("-----------------");
        System.out.println("File chosen: " + geopackageName);
        System.out.println("Write log file: " + writeLogFile);
        System.out.println();

        Metadata tmp = new Metadata();
        Document doc = tmp.create(geopackageName, documentVariant, writeLogFile, writeXMLFile);
    }
}
