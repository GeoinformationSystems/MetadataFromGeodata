/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.jdom2.Namespace.getNamespace;

public class WriteMetadataXML {
    public static void main(String[] argv) throws IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, JDOMException {
        // define all namespaces and root element MD_Metadata (27 from ISO 19115 and 3 from ISO 19157)
        String[][] namespacesSet = {
                {"mdb", "http://standards.iso.org/iso/19115/-3/mdb/1.0"},
                {"cat", "http://standards.iso.org/iso/19115/-3/cat/1.0"},
                {"cit", "http://standards.iso.org/iso/19115/-3/cit/1.0"},
                {"gco", "http://standards.iso.org/iso/19115/-3/gco/1.0"},
                {"gcx", "http://standards.iso.org/iso/19115/-3/gcx/1.0"},
                {"gex", "http://standards.iso.org/iso/19115/-3/gex/1.0"},
                {"gmw", "http://standards.iso.org/iso/19115/-3/gmw/1.0"},
                {"lan", "http://standards.iso.org/iso/19115/-3/lan/1.0"},
                {"mac", "http://standards.iso.org/iso/19115/-3/mac/1.0"},
                {"mas", "http://standards.iso.org/iso/19115/-3/mas/1.0"},
                {"mcc", "http://standards.iso.org/iso/19115/-3/mcc/1.0"},
                {"mco", "http://standards.iso.org/iso/19115/-3/mco/1.0"},
                {"md1", "http://standards.iso.org/iso/19115/-3/md1/1.0"},
                {"md2", "http://standards.iso.org/iso/19115/-3/md2/1.0"},
                {"mda", "http://standards.iso.org/iso/19115/-3/mda/1.0"},
                {"mds", "http://standards.iso.org/iso/19115/-3/mds/1.0"},
                {"mdt", "http://standards.iso.org/iso/19115/-3/mdt/1.0"},
                {"mex", "http://standards.iso.org/iso/19115/-3/mex/1.0"},
                {"mmi", "http://standards.iso.org/iso/19115/-3/mmi/1.0"},
                {"mpc", "http://standards.iso.org/iso/19115/-3/mpc/1.0"},
                {"mrc", "http://standards.iso.org/iso/19115/-3/mrc/1.0"},
                {"mrd", "http://standards.iso.org/iso/19115/-3/mrd/1.0"},
                {"mri", "http://standards.iso.org/iso/19115/-3/mri/1.0"},
                {"mrl", "http://standards.iso.org/iso/19115/-3/mrl/1.0"},
                {"mrs", "http://standards.iso.org/iso/19115/-3/mrs/1.0"},
                {"msr", "http://standards.iso.org/iso/19115/-3/msr/1.0"},
                {"srv", "http://standards.iso.org/iso/19115/-3/srv/2.0"},
                {"dqc", "http://standards.iso.org/iso/19157/-2/dqc/1.0"},
                {"mdq", "http://standards.iso.org/iso/19157/-2/mdq/1.0"},
                {"dqm", "http://standards.iso.org/iso/19157/-2/dqm/1.0"}
        };
        List<Namespace> namespacesList = new ArrayList<>();
        for (String[] strings : namespacesSet) {
            namespacesList.add(getNamespace(strings[0], strings[1]));
        }

        // provide ns.get("namespaceAbbreviation") for corresponding namespace variable
        Map<String, Namespace> ns = new HashMap<>();
        for (int i = 0; i < namespacesSet.length; i++) {
            ns.put(namespacesSet[i][0], namespacesList.get(i));
        }

        // content from arbitrary geopackage
        // one geopackage is one dataset - multiple content in one geopackage result in different MD_Metadata
        ReadGeopackage geopackageConnection = new ReadGeopackage();
        Integer contentNum = geopackageConnection.getContentNum("NewZealandPacked.gpkg"); //////////////////////////add different contents
        Element content = geopackageConnection.getContent("NewZealandPacked.gpkg", 1);

        // start linked list with element names (as string)
        List<String> elementChain = new ArrayList<>();
        int indexChain = 0;

        // extra log file for all element chains
        String dateNow = new SimpleDateFormat("yyyyMMdd-HHmm").format(new Date());
        Writer logFileWriter = new FileWriter(dateNow + "_WriteMetadataXML.log");
        dateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        logFileWriter.append("WriteMetadataXML ").append(dateNow).append(":\n");

        // time measurement
        long startTime = System.nanoTime();

        // create root element and add namespace declarations
        CreateInterface rootElementInst = new CreateFieldsXML();
        Element rootElement = rootElementInst.getElement(ns, "config/config_DS_Resource.xml", content, elementChain, indexChain, logFileWriter);
        for (Namespace namespace : namespacesList) {
            rootElement.addNamespaceDeclaration(namespace);
        }
        Document doc = new Document();
        doc.setRootElement(rootElement);

        // close log file
        logFileWriter.close();
        double estimatedTime = (System.nanoTime() - startTime) / 1e9;
        System.out.println("estimated Time: " + estimatedTime + " s");

        // remove elements without content from JDOM document
        // 1. mark for deletion
        DeleteInterface rootElementMarkedInst = new MarkForDeletionEmptyFieldsXML();
        Element rootElementMarked = rootElementMarkedInst.removeElements(rootElement.clone());
        Document docMarked = new Document();
        docMarked.setRootElement(rootElementMarked);
        // 2. delete
        DeleteInterface rootElementMinimalInst = new DeleteEmptyFieldsXML();
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
