/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur;

import org.geokur.ISO19115Schema.*;
import org.geokur.ISO191xxProfile.ProfileReader;
import org.geokur.generateMetadata.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MetadataGenerator {
    public static void main(String[] argv) {
        // main method for testing metadata generation from geofiles

        String profileFilename = "config/profile_GeoKur.json";

//        String fileName = "rasterExample.gpkg";
//        String fileName = "TestGeopackage.gpkg";
        String fileName = "paraguay.gpkg";
//        String fileName = "TestPointsShape.shp";
//        String fileName = "TestPointsShapeETRS.shp";
//        String fileName = "paraguay.csv";


        // read profile json file
        ProfileReader.setProfile(profileFilename);

        // read metadata and instantiate according classes
        DS_Resource metadata;
        String[] fileNameExtension = fileName.split("\\.");
        // todo: add other geodata types
        switch (fileNameExtension[fileNameExtension.length - 1]) {
            case "shp":
                System.out.println("-------------");
                System.out.println("Shape content ");
                System.out.println("-------------");
                metadata = new ShapeMetadata(fileName, new DS_DataSet()).getMetadata();
                break;
            case "gpkg":
                System.out.println("------------------");
                System.out.println("Geopackage content");
                System.out.println("------------------");
                metadata = new GeopackageMetadata(fileName, new DS_DataSet()).getMetadata();
                break;
            default:
                // file format not supported -> return empty document
                metadata = null;
                break;
        }

        // marshal to xml file
        try {
            JAXBContext contextObj = JAXBContext.newInstance(DS_DataSet.class);
            Marshaller marshaller = contextObj.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(metadata, new FileOutputStream("ds_resource.xml"));
        } catch (JAXBException | FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
