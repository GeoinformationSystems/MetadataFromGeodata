/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class EnumerationException extends RuntimeException {
    String enumerationName;
    String entry;

    public EnumerationException(String enumerationName, String entry) {
        this.enumerationName = enumerationName;
        this.entry = entry;
    }

    public String getMessage() {
        return("\nEnumeration Exception occurred\n" +
                entry + " no valid item in enumeration " + enumerationName +
                "\nProgram exit");
    }
}

public class CreateFieldsXML implements CreateInterface {

    public CreateFieldsXML() {}

    public Element getElement(Map<String, Namespace> ns, String configFile, Element content, List<String> elementChain, int indexChain, Writer logFileWriter) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, JDOMException, IOException {

        // read config file and define particular root element
        Document configDoc = new SAXBuilder().build(configFile);
        Element configRoot = configDoc.getRootElement();
        List<Element> configElements = configRoot.getChildren("element");

        String elementActName = configRoot.getName();
        String elementActNamespace = configRoot.getAttributeValue("namespace");
        Element elementActRoot = new Element(elementActName);
        elementActRoot.setNamespace(ns.get(elementActNamespace));
        List<Element> elementList = new ArrayList<>();

        // get actual relevant content
        // attention: nesting over each entry - class and subelement
        // -> here: first level
        Element contentAct;
        if (!(content.getChild(elementActName)==null)) {
            contentAct = content.getChild(elementActName);

        }
        else {
            contentAct = new Element("XXX");
        }

        // complement elementChain for finding circularity
        int elementChainSize = elementChain.size();
        if (elementChainSize > indexChain) {
            for (int i = indexChain; i < elementChainSize; i++) {
                // remove all (last) list members from list to match line from root to actual element
                elementChain.remove(elementChain.size()-1);
            }
        }
        if (elementChain.indexOf(elementActRoot.getName())!=-1 && elementChain.indexOf(elementActRoot.getName()) < indexChain) {
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

                // relevant content
                // -> here: second level
                // from here on the string array in the nested element can be relevant (multiple entries)
                Element contentActSub;
                if (!(contentAct.getChild(elementName)==null)) {
                    contentActSub = contentAct.getChild(elementName);
                }
                else {
                    contentActSub = new Element("XXX");
                }

                boolean elementListAdd = true;
                for (String elementConfigFileAct : elementConfigFile) {
                    String configFilename = elementConfigFileAct.toLowerCase();
                    List<Content> contentActSubContent = contentActSub.getContent();
                    List<String> contentActSubValues = new ArrayList<>();
                    for (Content contentActSubContentAct : contentActSubContent) {
                        if (!(contentActSubContentAct.getValue().length()==0)) {
                            contentActSubValues.add(contentActSubContentAct.getValue());
                        }
                    }

                    if (!(elementConfigFileAct.equals("") ||
                            (configFilename.contains("codelist") && configFilename.contains(".xml")) ||
                            (configFilename.contains("enumeration") && configFilename.contains(".xml")))) {
                        // recursive call if nested element available (still no particular metadata content)
                        logFileWriter.append("   ").append(elementConfigFileAct).append("\n");

                        Class<?> classAct = Class.forName("CreateFieldsXML");
                        Object classActObj = classAct.newInstance();
                        Method classMethodAct = classAct.getDeclaredMethod("getElement", Map.class, String.class, Element.class, List.class, int.class, Writer.class);
                        Element testClassMethodObj = (Element) classMethodAct.invoke(classActObj, ns, "config/" + elementConfigFileAct, contentActSub, elementChain, indexChain, logFileWriter);
                        meta.addContent(testClassMethodObj);
                    }
                    ///////////////////////////
                    // Just for finding all entry elements
//                    else {
//                        elementListAdd = false;
//                        Element metaMultiple = new Element(elementName);
//                        metaMultiple.setNamespace(ns.get(elementNamespace));
//                        metaMultiple.addContent("XX_entry_XX");
//                        elementList.add(metaMultiple);
//                    }
                    ///////////////////////////
                    else if (contentActSubValues.size()>0) {
                        // add content into xml element if available
                        if (configFilename.contains("codelist") && configFilename.contains(".xml")) {
                            // codelists contain normally allowed entries, but can be extended
                            // if field with codelist contains non-original element an information is thrown
                            ReadCodelist codelist = new ReadCodelist("config/" + elementConfigFileAct);
                            List<List<String>> codelistProps = codelist.getEntries();
                            List<String> codelistRoot = codelistProps.get(0);
                            List<String> codelistEntries = codelistProps.get(1);
                            List<Element> metaSubAll = new ArrayList<>();

                            for (String contentActSubPart : contentActSubValues) {
                                if (!codelistEntries.contains(contentActSubPart)) {
                                    System.out.println("Entry for codelist " + codelistRoot.get(0) + " contains unknown element: " + contentActSubPart);
                                    System.out.println("\tAs codelists are extensible it is allowed but no regular case.");
                                }

                                // create subelement(s) for codelist
                                // possible as codelist always lowest element in one hierarchical branch
                                Element metaSub = new Element(codelistRoot.get(0));
                                metaSub.setNamespace(ns.get("cat"));
                                metaSub.setAttribute("codeList", codelistRoot.get(1) + "/codelists.html#" + codelistRoot.get(0));
                                metaSub.setAttribute("codeListValue", contentActSubPart);
                                metaSubAll.add(metaSub);
                            }

                            meta.addContent(metaSubAll);

                        }
                        else if (configFilename.contains("enumeration") && configFilename.contains(".xml")) {
                            // enumerations are similar to codelists, but cannot be extended
                            // if field with enumeration contains non-original element an error is thrown
                            ReadCodelist enumeration = new ReadCodelist("config/" + elementConfigFileAct);
                            List<List<String>> enumerationProps = enumeration.getEntries();
                            List<String> enumerationRoot = enumerationProps.get(0);
                            List<String> enumerationEntries = enumerationProps.get(1);
                            List<Element> metaSubAll = new ArrayList<>();

                            for (String contentActSubPart : contentActSubValues) {
                                try {
                                    if (!enumerationEntries.contains(contentActSubPart)) {
                                        throw new EnumerationException(enumerationRoot.get(0), contentActSubPart);
                                    }
                                }
                                catch (EnumerationException ex) {
                                    System.out.println(ex.getMessage());
                                    System.exit(8); // maybe change to exception propagation to main?
                                }

                                // create subelement for codelist
                                // possible as codelist always lowest element in one hierarchical branch
                                Element metaSub = new Element(enumerationRoot.get(0));
                                metaSub.setNamespace(ns.get("cat"));
                                metaSub.addContent(contentActSubPart);
                                metaSubAll.add(metaSub);
                            }
                            meta.addContent(metaSubAll);
                        }
                        else {
                            // regular elements are always included (without attributes)
                            for (String contentActSubPart : contentActSubValues) {
                                elementListAdd = false;
                                Element metaMultiple = new Element(elementName);
                                metaMultiple.setNamespace(ns.get(elementNamespace));
                                metaMultiple.addContent(contentActSubPart);
                                elementList.add(metaMultiple);
                            }
                        }
                    }
                }
                if (elementListAdd) {
                    elementList.add(meta);
                }
            }
        }

        elementActRoot.addContent(elementList);

        return elementActRoot;
    }
}
