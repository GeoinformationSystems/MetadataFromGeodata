/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.generateMetadataDocument;

import org.apache.commons.io.output.NullWriter;
import org.geokur.trimXML.DeleteInterface;
import org.geokur.trimXML.EmptyFieldsTrimMark;
import org.geokur.trimXML.MetadataTreeTrimmed;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.jdom2.Namespace.getNamespace;

public class Metadata {
    // define all namespaces and root element MD_Metadata (27 from ISO 19115 and 3 from ISO 19157)

    public Document create(String geodataFileName, String documentVariant, boolean writeLogFile, boolean writeXMLFile) {
        // creation of a jdom2 document with all nested metadata fields
        // geodataFileName is the filename
        // documentVariant: either "all", "marked" or "minimal"
        // writeLogFile: if true a log file with all classes gone through is written
        // writeXMLFile: if true accompanying xml files are written

        // Setting the system-wide default at startup time
        System.setProperty("org.geotools.referencing.forceXY", "true");

        Document doc = new Document();
        Document docMarked = new Document();
        Document docMinimal = new Document();
        String geodataType;

        System.out.println("-----------------");
        System.out.println("Generate Metadata");
        System.out.println("-----------------");
        System.out.println("File chosen: " + geodataFileName);
        System.out.println("Write log file: " + writeLogFile);
        System.out.println();

        // todo: allow externally given geodata type for cases where file extension is not self-explanatory
        String[] geodataFileNameSplit = geodataFileName.split("\\.");
        switch (geodataFileNameSplit[geodataFileNameSplit.length - 1]) {
            case "gpkg": geodataType = "geopackage";
                break;
            case "shp":  geodataType = "shape";
                break;
            case "csv": geodataType = "ascii";
                break;
            default:
                // file format not supported -> return empty document
                return doc;
        }


        try {
            FileReader nsFR = new FileReader("config/namespaces.txt");
            BufferedReader nsBR = new BufferedReader(nsFR);
            nsBR.readLine(); // read and skip one headerline
            String lineAct;
            List<Namespace> namespacesList = new ArrayList<>();
            while ((lineAct = nsBR.readLine()) != null) {
                String[] lineActArray = lineAct.split("\t");
                namespacesList.add(getNamespace(lineActArray[0], lineActArray[1]));
            }

            // provide ns.get("namespaceAbbreviation") for corresponding namespace variable
            Map<String, Namespace> ns = new HashMap<>();
            for (Namespace value : namespacesList) {
                ns.put(value.getPrefix(), value);
            }

            // todo: add other geodata types
            // todo: in the case of geopackage: allow adding information to existing dataset (according to UUID?)
            List<Element> content = null;
            switch (geodataType) {
                case "geopackage":
                    // content from geopackage
                    // one geopackage is one dataset - multiple content in one geopackage result in multiple MD_Metadata

                    content = new ArrayList<>();

                    GeopackageMetadata geopackageConnection = new GeopackageMetadata();
                    Integer contentNum = geopackageConnection.getContentNum(geodataFileName);
                    for (int i = 0; i < contentNum; i++) {
                        content = geopackageConnection.getContent(geodataFileName, i, content, ns);
                    }
                    break;
                case "shape":
                    // content from shape file
                    ShapeMetadata shapeConnection = new ShapeMetadata();
                    content = shapeConnection.getContent(geodataFileName, ns);
                    break;
                case "ascii":
                    // content from ascii file spatially referring to a geofile (e.g., geopackage)
                    break;
            }

            // start linked list with element names (as string)
            List<String> elementChain = new ArrayList<>();
            int indexChain = 0;

            // extra log file for all element chains
            Writer logFileWriter;
            String dateNow = new SimpleDateFormat("yyyyMMdd-HHmm").format(new Date());
            if (writeLogFile) {
                FileOutputStream logFOS = new FileOutputStream(dateNow + "_WriteMetadataXML.log");
                OutputStreamWriter logOSW = new OutputStreamWriter(logFOS, StandardCharsets.UTF_8);
                logFileWriter = new BufferedWriter(logOSW);
            }
            else {
                logFileWriter = new NullWriter();
            }
            dateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            logFileWriter.append("WriteMetadataXML ").append(dateNow).append(":\n");

            // time measurement
            long startTime = System.nanoTime();

            // create root element and add namespace declarations
            CreateInterface rootElementInst = new MetadataTreeTemplate();
            Element rootElement = rootElementInst.getElement(ns, "config/config_DS_DataSet.xml", "M", "1", elementChain, indexChain, logFileWriter);
            for (Namespace namespace : namespacesList) {
                rootElement.addNamespaceDeclaration(namespace);
            }

            // close log file
            logFileWriter.close();
            double estimatedTime = (System.nanoTime() - startTime) / 1e9;
            System.out.println("estimated Time: " + estimatedTime + " s");

            // fill metadata from geopackage into document object model from CreateFieldsXML
            MetadataTree rootElementFilledInst = new MetadataTree();
            assert content != null; //todo: possibly remove assertion in production version
            Element rootElementFilled = rootElementFilledInst.fillElements(rootElement, content, ns);
            doc.setRootElement(rootElementFilled);

            // remove elements without content from JDOM document
            // 1. mark for deletion
            DeleteInterface rootElementMarkedInst = new EmptyFieldsTrimMark();
            Element rootElementMarked = rootElementMarkedInst.removeElements(rootElement.clone());
            docMarked.setRootElement(rootElementMarked);
            // 2. delete
            DeleteInterface rootElementMinimalInst = new MetadataTreeTrimmed();
            Element rootElementMinimal = rootElementMinimalInst.removeElements(rootElementMarked.clone());
            docMinimal.setRootElement(rootElementMinimal);


            if (writeXMLFile) {
                // output to file
                XMLOutputter out = new XMLOutputter();
                Format outFormat = Format.getPrettyFormat();
                outFormat.setEncoding("UTF-8");
                out.setFormat(outFormat);
                // write to file
                out.output(docMinimal, new FileOutputStream("metadataGeoKurMandatoryMinimal.xml"));
                out.output(doc, new FileOutputStream("metadataGeoKurMandatory.xml"));
                //out.output(docMinimal, System.out);
            }

        }
        catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | JDOMException e) {
            System.out.println(e.getMessage());
        }


        switch (documentVariant) {
            case "all": return doc;
            case "marked": return docMarked;
            default: return docMinimal;
        }
    }
}
