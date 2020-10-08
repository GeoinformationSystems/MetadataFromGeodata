/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class BuildClassCode {
    // build Java classes from ISO 19115 via xml files with most properties

    public static void main(String[] argv) throws FileNotFoundException {
        Scanner classNamesFile = new Scanner(new FileReader("classes/a_classList"));
        List<String> classNames = new ArrayList<>();
        while (classNamesFile.hasNextLine()) {
            classNames.add(classNamesFile.nextLine());
        }

        for (String actClass : classNames) {

            System.out.println(actClass);

            String filename = "configISO/config_" + actClass + ".xml";
            List<String> names = new ArrayList<>();
            List<String> namesCamelCase = new ArrayList<>();
            List<String> obligations = new ArrayList<>();
            List<String> occurrences = new ArrayList<>();
            List<String> configFiles = new ArrayList<>();
            List<String> namespaces = new ArrayList<>();

            boolean configFilesAvailable = false;

            // get namespaces
            Scanner scanner = new Scanner(new FileReader("configISO/namespaces.txt"));
            HashMap<String, String> namespacesMap = new HashMap<>();
            while (scanner.hasNextLine()) {
                String[] columns = scanner.nextLine().split("\t");
                namespacesMap.put(columns[0], columns[1]);
            }

            // read xml document
            try {
                Document configDoc = new SAXBuilder().build(filename);
                Element configRoot = configDoc.getRootElement();
                Attribute configNamespace = configRoot.getAttribute("namespace");
                List<Element> configElements = configRoot.getChildren("element");
                int numElements = configElements.size();

                for (int i = 0; i < numElements; i++) {
                    Element elementName = configElements.get(i).getChild("name");
                    names.add(elementName.getValue());
                    namesCamelCase.add(names.get(i).substring(0, 1).toUpperCase() + names.get(i).substring(1));

                    Element elementObligation = configElements.get(i).getChild("obligation");
                    obligations.add(elementObligation.getValue());

                    Element elementOccurrence = configElements.get(i).getChild("occurrence");
                    occurrences.add(elementOccurrence.getValue());

                    Element elementConfigFile = configElements.get(i).getChild("configFile");
                    List<Element> elementConfigFileSpecified = elementConfigFile.getChildren("configFileSpecified");
                    if (elementConfigFileSpecified.size() > 0) {
                        // in the case of multiple config files only the first is taken
                        // -> manual correction in the case of abstract and specified classes necessary
                        elementConfigFile = elementConfigFileSpecified.get(0);
                    }
                    String temp = elementConfigFile.getValue();
                    String[] temp2 = temp.split("[.]");
                    temp = temp2[0];
                    temp = temp.replace("config_", "");
                    temp = temp.replace("codelist_", "");
                    temp = temp.replace("enumeration_", "");
                    configFiles.add(temp);
                    if (!elementConfigFile.getValue().equals("")) {
                        // wrapper necessary
                        configFilesAvailable = true;
                    }

                    List<Element> elementNamespace = configElements.get(i).getChildren("namespace");
                    namespaces.add(elementNamespace.get(0).getValue());
                }


                // write code blocks
                // header
                String filenameOut = "classes/" + actClass + ".java";
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filenameOut));
                fileWriter.append("/*\n");
                fileWriter.append(" * Copyright (c) 2020. Michael Wagner.\n");
                fileWriter.append(" * All rights reserved.\n");
                fileWriter.append(" */\n");
                fileWriter.append("\n");
                fileWriter.append("package org.geokur.ISO19115_2Schema;\n");
                fileWriter.append("\n");
                fileWriter.append("import org.geokur.ISO191xxProfile.MaximumOccurrenceException;\n");
                fileWriter.append("import org.geokur.ISO191xxProfile.ObligationException;\n");
                fileWriter.append("import org.geokur.ISO191xxProfile.ProfileException;\n");
                fileWriter.append("import org.geokur.ISO191xxProfile.ProfileReader;\n");
                fileWriter.append("\n");
                fileWriter.append("import javax.xml.bind.annotation.XmlElement;\n");
                if (configFilesAvailable) {
                    fileWriter.append("import javax.xml.bind.annotation.XmlElementRef;\n");
                    fileWriter.append("import javax.xml.bind.annotation.XmlElementWrapper;\n");
                }
                fileWriter.append("import javax.xml.bind.annotation.XmlRootElement;\n");
                fileWriter.append("import java.util.ArrayList;\n");
                fileWriter.append("import java.util.Arrays;\n");
                fileWriter.append("import java.util.List;\n");
                fileWriter.append("\n");

                // class and occurrence and obligation
                fileWriter.append("@XmlRootElement(name = \"" + actClass + "\", namespace = \"" + namespacesMap.get(configNamespace.getValue()) + "\")\n");
                fileWriter.append("public class " + actClass + " {");
                fileWriter.append("\n");
                fileWriter.append("\n");
                fileWriter.append("    // occurrence and obligation\n");
                fileWriter.append("    private final String[] elementName = {");
                for (int i = 0; i < numElements - 1; i++) {
                    fileWriter.append("\"" + names.get(i) + "\", ");
                }
                fileWriter.append("\"" + names.get(numElements - 1) + "\"};\n");
                fileWriter.append("    private final int[] elementMax = {");
                for (int i = 0; i < numElements - 1; i++) {
                    if (occurrences.get(i).equals("N")) {
                        fileWriter.append("Integer.MAX_VALUE, ");
                    } else {
                        fileWriter.append(occurrences.get(i) + ", ");
                    }
                }
                if (occurrences.get(numElements - 1).equals("N")) {
                    fileWriter.append("Integer.MAX_VALUE};\n");
                } else {
                    fileWriter.append(occurrences.get(numElements - 1) + "};\n");
                }
                fileWriter.append("    private final boolean[] elementObligation = {");
                for (int i = 0; i < numElements - 1; i++) {
                    if (obligations.get(i).equals("M")) {
                        fileWriter.append("true, ");
                    } else {
                        fileWriter.append("false, ");
                    }
                }
                if (obligations.get(numElements - 1).equals("M")) {
                    fileWriter.append("true};\n");
                } else {
                    fileWriter.append("false};\n");
                }
                fileWriter.append("\n");
                fileWriter.append("    private final String className = this.getClass().getSimpleName();\n");
                fileWriter.append("    private final boolean[] elementUsed = new boolean[elementName.length];\n");
                fileWriter.append("\n");

                // class variables
                fileWriter.append("    // class variables\n");
                for (int i = 0; i < numElements; i++) {
                    if (configFiles.get(i).equals("")) {
                        fileWriter.append("    @XmlElement(name = \"" + names.get(i) + "\", namespace = \"" + namespacesMap.get(namespaces.get(i)) + "\")\n");
                        fileWriter.append("    public List<String> " + names.get(i) + ";\n");
                        fileWriter.append("\n");
                    } else {
                        fileWriter.append("    @XmlElementWrapper(name = \"" + names.get(i) + "\", namespace = \"" + namespacesMap.get(namespaces.get(i)) + "\")\n");
                        fileWriter.append("    @XmlElementRef\n");
                        fileWriter.append("    public List<" + configFiles.get(i) + "> " + names.get(i) + ";\n");
                        fileWriter.append("\n");
                    }
                }

                // methods
                fileWriter.append("    // methods\n");
                fileWriter.append("    public " + actClass + "(){\n");
                fileWriter.append("        for (int i = 0; i < elementName.length; i++) {\n");
                fileWriter.append("            elementUsed[i] = true;\n");
                fileWriter.append("        }\n");
                fileWriter.append("\n");
                fileWriter.append("        // use profile (used elements and their obligation)\n");
                fileWriter.append("        if (ProfileReader.profile != null) {\n");
                fileWriter.append("            for (int i = 0; i < elementName.length; i++) {\n");
                fileWriter.append("                List<String> tempList = Arrays.asList(ProfileReader.profile.used." + actClass + ");\n");
                fileWriter.append("                if (!tempList.contains(elementName[i])) {\n");
                fileWriter.append("                    // element not used\n");
                fileWriter.append("                    elementUsed[i] = false;\n");
                fileWriter.append("                }\n");
                fileWriter.append("                tempList = Arrays.asList(ProfileReader.profile.obligation." + actClass + ");\n");
                fileWriter.append("                if (!tempList.contains(elementName[i])) {\n");
                fileWriter.append("                    // element not mandatory\n");
                fileWriter.append("                    elementObligation[i] = false;\n");
                fileWriter.append("                } else if (tempList.contains(elementName[i])) {\n");
                fileWriter.append("                    // element mandatory\n");
                fileWriter.append("                    elementObligation[i] = true;\n");
                fileWriter.append("                }\n");
                fileWriter.append("            }\n");
                fileWriter.append("        }\n");
                fileWriter.append("    }\n");
                fileWriter.append("\n");

                // create methods
                for (int i = 0; i < numElements; i++) {
                    fileWriter.append("    public void create" + namesCamelCase.get(i) + "() {\n");
                    fileWriter.append("        if (this." + names.get(i) + " == null) {\n");
                    fileWriter.append("            this." + names.get(i) + " = new ArrayList<>();\n");
                    fileWriter.append("        }\n");
                    fileWriter.append("    }\n");
                    fileWriter.append("\n");
                }

                // add methods
                for (int i = 0; i < numElements; i++) {
                    if (configFiles.get(i).equals("")) {
                        fileWriter.append("    public void add" + namesCamelCase.get(i) + "(String " + names.get(i) + ") {\n");
                    } else {
                        fileWriter.append("    public void add" + namesCamelCase.get(i) + "(" + configFiles.get(i) + " " + names.get(i) + ") {\n");
                    }
                    fileWriter.append("        int elementNum = " + i + ";\n");
                    fileWriter.append("        try {\n");
                    fileWriter.append("            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);\n");
                    fileWriter.append("            if (tempList.size() >= elementMax[elementNum]) {\n");
                    fileWriter.append("                throw new MaximumOccurrenceException(className + \" - \" + elementName[elementNum], elementMax[elementNum]);\n");
                    fileWriter.append("            } else {\n");
                    fileWriter.append("                this." + names.get(i) + ".add(" + names.get(i) + ");\n");
                    fileWriter.append("            }\n");
                    fileWriter.append("        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {\n");
                    fileWriter.append("            System.out.println(e.getMessage());\n");
                    fileWriter.append("        }\n");
                    fileWriter.append("    }\n");
                    fileWriter.append("\n");
                }

                // finalize method
                fileWriter.append("    public void finalizeClass() {\n");
                fileWriter.append("        for (int i = 0; i < elementName.length; i++) {\n");
                fileWriter.append("            try {\n");
                fileWriter.append("                List<?> tempList = (List<?>) this.getClass().getField(elementName[i]).get(this);\n");
                fileWriter.append("                if (!elementUsed[i] && tempList != null && !tempList.isEmpty()) {\n");
                fileWriter.append("                    // test profile use\n");
                fileWriter.append("                    throw new ProfileException(className + \" - \" + elementName[i]);\n");
                fileWriter.append("                }\n");
                fileWriter.append("                if (elementObligation[i] && (tempList == null || tempList.isEmpty())) {\n");
                fileWriter.append("                    // test filling and obligation of all variable lists\n");
                fileWriter.append("                    throw new ObligationException(className + \" - \" + elementName[i]);\n");
                fileWriter.append("                }\n");
                fileWriter.append("            } catch (ProfileException | ObligationException | NoSuchFieldException | IllegalAccessException e) {\n");
                fileWriter.append("                System.out.println(e.getMessage());\n");
                fileWriter.append("            }\n");
                fileWriter.append("        }\n");
                fileWriter.append("    }\n");
                fileWriter.append("}\n");

                fileWriter.close();

            } catch (IOException | JDOMException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
