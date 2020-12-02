/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur;

import org.geokur.ISO19115Schema.*;
import org.geokur.ISO191xxProfile.ProfileReader;
import org.geokur.generateMetadata.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MetadataGenerator {
    public static void main(String[] argv) {
        // main method for testing metadata generation from geofiles

        String profileFilename = "config/profile_GeoKur.json";

//        String fileName = "rasterExample.gpkg";
//        String fileName = "TestGeopackage.gpkg";
//        String fileName = "paraguay.gpkg";
//        String fileName = "TestPointsShape.shp";
//        String fileName = "TestPointsShapeETRS.shp";
        String fileName = "paraguay.csv";

        String fileNameXML = "ds_resource.xml";
        String fileNameDB = "ds_resource.db";

        // remove out files if existing (test cases)
        try {
            Files.deleteIfExists(new File(fileNameXML).toPath());
            Files.deleteIfExists(new File(fileNameDB).toPath());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


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
            case "csv":
                System.out.println("-------------");
                System.out.println("Ascii content");
                System.out.println("-------------");
                AsciiMetadata asciiMetadata = new AsciiMetadata(fileName, new DS_DataSet());
                List<String> geoTableName = new ArrayList<>();
                geoTableName.add("level_1");
                geoTableName.add("level_2");
                geoTableName.add("level_3");
                List<String> geoColNameJoin = new ArrayList<>();
                geoColNameJoin.add("ahID");
                geoColNameJoin.add("geoID");
                List<String> asciiColNameJoin = new ArrayList<>();
                asciiColNameJoin.add("ahID");
                asciiColNameJoin.add("geoID");
                List<String> asciiColNameDefine = new ArrayList<>();
                asciiColNameDefine.add("commodityID");
                asciiColNameDefine.add("year");
                List<String> descriptionAsciiColNameDefine = new ArrayList<>();
                descriptionAsciiColNameDefine.add("thematic");
                descriptionAsciiColNameDefine.add("temporal");
                List<String> asciiColNameIgnore = new ArrayList<>();
                asciiColNameIgnore.add("id");
                asciiColNameIgnore.add("tabID");
                asciiMetadata.defineProperties("paraguay.gpkg", geoTableName, geoColNameJoin, asciiColNameJoin, asciiColNameDefine, asciiColNameIgnore, descriptionAsciiColNameDefine);
                metadata = asciiMetadata.getMetadata();
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
            marshaller.marshal(metadata, new FileOutputStream(fileNameXML));
        } catch (JAXBException | FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        // order xml file to SQLite database
        // read xml file with JDOM2 library in order to get a document
        try {
            Document doc = new SAXBuilder().build(fileNameXML);
            Element docRoot = doc.getRootElement();
            MetadataDatabase metadataDatabase = new MetadataDatabase();
            metadataDatabase.generateFlatFromElement(docRoot);
            Database database = new Database(fileNameDB);
            database.createNewDatabase();
            database.addToDatabase(fileName);
            database.writeMetadataToDatabase(fileName, metadataDatabase);
        } catch (IOException | JDOMException e) {
            System.out.println(e.getMessage());
        }
    }
}
