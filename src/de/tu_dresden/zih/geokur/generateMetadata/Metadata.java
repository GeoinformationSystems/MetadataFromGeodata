/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package de.tu_dresden.zih.geokur.generateMetadata;

import de.tu_dresden.zih.geokur.trimXML.*;

import org.apache.commons.io.output.NullWriter;
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
    public static void main(String[] argv) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, JDOMException {
        // define all namespaces and root element MD_Metadata (27 from ISO 19115 and 3 from ISO 19157)
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

        // content from arbitrary geopackage
        // one geopackage is one dataset - multiple content in one geopackage result in different MD_Metadata
//        String geopackageName = "NewZealandPacked.gpkg";
        String geopackageName = "rasterExample.gpkg";
        GeopackageMetadata geopackageConnection = new GeopackageMetadata();
        Integer contentNum = geopackageConnection.getContentNum(geopackageName); //////////////////////////add different contents
//        Element content = geopackageConnection.getContent(geopackageName, 1);
        List<Element> content = geopackageConnection.getContent(geopackageName, 1, ns);

        // start linked list with element names (as string)
        List<String> elementChain = new ArrayList<>();
        int indexChain = 0;

        // extra log file for all element chains
        boolean writeLogFile = false;
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
//        Element rootElement = rootElementInst.getElement(ns, "config/config_DS_Resource.xml", content, elementChain, indexChain, logFileWriter);
        Element rootElement = rootElementInst.getElement(ns, "config/config_DS_Resource.xml", "M", "1", elementChain, indexChain, logFileWriter);
        for (Namespace namespace : namespacesList) {
            rootElement.addNamespaceDeclaration(namespace);
        }

        // close log file
        logFileWriter.close();
        double estimatedTime = (System.nanoTime() - startTime) / 1e9;
        System.out.println("estimated Time: " + estimatedTime + " s");

        // fill metadata from geopackage into document object model from CreateFieldsXML
        MetadataTree rootElementFilledInst = new MetadataTree();
//        Element docFilled = docFilledInst.fillElements(doc.getRootElement(), content.getChild("DS_Resource"));
        Element rootElementFilled = rootElementFilledInst.fillElements(rootElement, content, ns);

        Document doc = new Document();
        doc.setRootElement(rootElementFilled);

        // remove elements without content from JDOM document
        // 1. mark for deletion
        DeleteInterface rootElementMarkedInst = new EmptyFieldsTrimMark();
        Element rootElementMarked = rootElementMarkedInst.removeElements(rootElement.clone());
        Document docMarked = new Document();
        docMarked.setRootElement(rootElementMarked);
        // 2. delete
        DeleteInterface rootElementMinimalInst = new MetadataTreeTrimmed();
        Element rootElementMinimal = rootElementMinimalInst.removeElements(rootElementMarked.clone());
        Document docMinimal = new Document();
        docMinimal.setRootElement(rootElementMinimal);


        // output to file
        XMLOutputter out = new XMLOutputter();
        Format outFormat = Format.getPrettyFormat();
        outFormat.setEncoding("UTF-8");
        out.setFormat(outFormat);
        // write to file
        out.output(docMinimal, new FileOutputStream("metadataGeoKurMandatoryMinimal.xml"));
        out.output(doc, new FileOutputStream("metadataGeoKurMandatory.xml"));
//        System.out.println();
//        out.output(doc, System.out);
//        System.out.println();
//        out.output(docMarked, System.out);
//        System.out.println();
//        out.output(docMinimal, System.out);

    }
}
