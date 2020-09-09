/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.generateMetadataDocument;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MetadataTreeTemplate implements CreateInterface {

    public MetadataTreeTemplate() {}

    public Element getElement(Map<String, Namespace> ns, String configFile, String obligation, String occurrence, List<String> elementChain, int indexChain, Writer logFileWriter) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, JDOMException, IOException {

        // read config file and define particular root element
        Document configDoc = new SAXBuilder().build(configFile);
        Element configRoot = configDoc.getRootElement();
        List<Element> configElements = configRoot.getChildren("element");

        String elementActName = configRoot.getName();
        String elementActNamespace = configRoot.getAttributeValue("namespace");
        Element elementActRoot = new Element(elementActName);
        elementActRoot.setNamespace(ns.get(elementActNamespace));
        List<Element> elementList = new ArrayList<>();

        elementActRoot.setAttribute("obligation", obligation);
        elementActRoot.setAttribute("occurrence", occurrence);

        // complement elementChain for finding circularity
        int elementChainSize = elementChain.size();
        if (elementChainSize > indexChain) {
            for (int i = indexChain; i < elementChainSize; i++) {
                // remove all (last) list members from list to match line from root to actual element
                elementChain.remove(elementChain.size()-1);
            }
        }
        if (elementChain.contains(elementActRoot.getName()) && elementChain.indexOf(elementActRoot.getName()) < indexChain) {
            // circularity elements will just be called, but no step inside is taken
            logFileWriter.append(String.valueOf(elementChain.indexOf(elementActRoot.getName()))).append(": ").append(String.valueOf(indexChain)).append(" -> removed circle\n");
            return elementActRoot;
        }
        elementChain.add(elementActRoot.getName());
        logFileWriter.append(String.valueOf(indexChain)).append(": ").append(String.valueOf(elementChain)).append("\n");

        indexChain++;


        // add each subelement to root element (recursive call)
        for (Element configElement : configElements) {
            boolean elementUsed = Boolean.parseBoolean(configElement.getChildren("used").get(0).getValue());
            if (elementUsed) {
                List<String> elementConfigFile = new ArrayList<>();
                if (configElement.getChildren("configFile").get(0).getChildren().size() > 0) {
                    // multiple specified classes -> multiple configFiles
                    for (Element configElementAct : configElement.getChildren("configFile").get(0).getChildren("configFileSpecified")) {
                        elementConfigFile.add(configElementAct.getValue());
                    }
                }
                else {
                    // no specified class
                    elementConfigFile.add(configElement.getChildren("configFile").get(0).getValue());
                }

                String elementName = configElement.getChildren("name").get(0).getValue();
                String elementNamespace = configElement.getChildren("namespace").get(0).getValue();
                Element meta = new Element(elementName);
                meta.setNamespace(ns.get(elementNamespace));

                String elementObligation = configElement.getChildren("obligation").get(0).getValue();
                meta.setAttribute("obligation", elementObligation);
                String elementOccurrence = configElement.getChildren("occurrence").get(0).getValue();
                meta.setAttribute("occurrence", elementOccurrence);

                for (String elementConfigFileAct : elementConfigFile) {
                    String configFilename = elementConfigFileAct.toLowerCase();

                    if (!(elementConfigFileAct.equals("") ||
                            (configFilename.contains("codelist") && configFilename.contains(".xml")) ||
                            (configFilename.contains("enumeration") && configFilename.contains(".xml")))) {
                        // recursive call if nested element available (still no particular metadata content)
                        logFileWriter.append("   ").append(elementConfigFileAct).append("\n");

                        Class<?> classAct = Class.forName("org.geokur.generateMetadataDocument.MetadataTreeTemplate");
                        Object classActObj = classAct.newInstance();
                        Method classMethodAct = classAct.getDeclaredMethod("getElement", Map.class, String.class, String.class, String.class, List.class, int.class, Writer.class);
                        Element testClassMethodObj = (Element) classMethodAct.invoke(classActObj, ns, "config/" + elementConfigFileAct, elementObligation, elementOccurrence, elementChain, indexChain, logFileWriter);
                        meta.addContent(testClassMethodObj);
                    }
                }
                elementList.add(meta);
            }
        }

        elementActRoot.addContent(elementList);

        return elementActRoot;
    }
}
