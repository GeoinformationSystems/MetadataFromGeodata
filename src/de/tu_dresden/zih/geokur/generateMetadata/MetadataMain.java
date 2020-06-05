/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package de.tu_dresden.zih.geokur.generateMetadata;

public class MetadataMain {
    public static void main(String[] argv) {
        // main method to generate metadata from geodata
        String geopackageName = System.getProperty("geopackageName");
        boolean writeLogFile = Boolean.parseBoolean(System.getProperty("writeLogFile"));

        System.out.println("-----------------");
        System.out.println("Generate Metadata");
        System.out.println("-----------------");
        System.out.println("File chosen: " + geopackageName);
        System.out.println("Write log file: " + writeLogFile);
        System.out.println();

        Metadata.create(geopackageName, writeLogFile);
    }
}
