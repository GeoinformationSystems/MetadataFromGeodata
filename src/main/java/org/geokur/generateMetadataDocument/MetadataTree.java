/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.generateMetadataDocument;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
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

public class MetadataTree {

    public MetadataTree() {}

    public Element fillElements(Element allFields, List<Element> metadata, Map<String, Namespace> ns) {
        // Complement existing document with contents from Elements in metadata
        // Here invoke complement for multiple single elements

        for (Element metadataAct : metadata) {
            allFields = complementNestedElement(allFields, metadataAct, ns);
        }

        return allFields;
    }


    private Element complementNestedElement(Element allFields, Element metadata, Map<String, Namespace> ns) {
        // Complement of a single element into given element tree

        if (!allFields.getName().equals(metadata.getName())) {
            // allFields and metadata must have the same first element
            System.out.println("de.tu_dresden.zih.geokur.generateMetadata.Metadata element " + metadata.getName() + " not equal to tree base " + allFields.getName());
            return allFields;
        }
        else if(metadata.getAttribute("UUID")==null) {
            // metadata must have an UUID for the first element (and for all other, although not relevant here)
            System.out.println("de.tu_dresden.zih.geokur.generateMetadata.Metadata element has no attribute UUID: not valid.");
            return allFields;
        }
        else if (allFields.getAttribute("UUID")==null) {
            // no UUID assigned yet
            allFields.setAttribute("UUID", metadata.getAttributeValue("UUID"));
        }

        // get name chain of the actual element
        List<String> nameChain = new ArrayList<>();
        List<String> idChain = new ArrayList<>();

        Element metadataCopy = metadata;

        nameChain.add(metadataCopy.getName());
        idChain.add(metadataCopy.getAttributeValue("UUID"));

        List<Element> metadataList;

        while (metadataCopy.getChildren().size()>0) {
            metadataList = metadataCopy.getChildren();
            metadataCopy = metadataList.get(0);
            nameChain.add(metadataCopy.getName());
            idChain.add(metadataCopy.getAttributeValue("UUID"));
        }
        String metadataValue = metadataCopy.getValue(); // last element

        // proof for codelist/enumeration compliance
        Element configRootElement = null;
        if (nameChain.size()%2==0) {
            // some data fields contain uneven numbers of nameChain (e.g., date) -> these are never codelists or enumerations
            String configFileLast = "config/config_" + nameChain.get(nameChain.size() - 2) + ".xml";
            try {
                configRootElement = new SAXBuilder().build(configFileLast).getRootElement();
            } catch (JDOMException | IOException e) {
                System.out.println(e.getMessage());
            }
        }

        if (configRootElement != null) {
            List<Element> configRootChildren = configRootElement.getChildren("element");
            String configFile = getConfigFile(configRootChildren, nameChain.get(nameChain.size() - 1));

            // codelist
            if (configFile.contains("codelist") && configFile.contains(".xml")) {
                // codelists contain normally allowed entries, but can be extended
                // if field with codelist contains non-original element an information is thrown
                Codelist codelist = new Codelist("config/" + configFile);
                List<List<String>> codelistProps = codelist.getEntries();
                List<String> codelistRoot = codelistProps.get(0);
                List<String> codelistEntries = codelistProps.get(1);
                if (!codelistEntries.contains(metadataValue)) {
                    System.out.println("Entry for codelist " + codelistRoot.get(0) + " contains unknown element: " + metadataValue);
                    System.out.println("\tAs codelists are extensible it is allowed but no regular case.");
                }

                // create subelement for codelist as codelists always have an extra element for its own
                Element metaSub = new Element(codelistRoot.get(0));
                metaSub.setNamespace(ns.get("cat"));
                metaSub.setAttribute("codeList", codelistRoot.get(1) + "/codelists.html#" + codelistRoot.get(0));
                metaSub.setAttribute("codeListValue", metadataValue);
                metadataCopy.removeContent();
                metadataCopy.addContent(metaSub);

                // build up new metadata and nameChain variables with added codelist element
                nameChain.add(codelistRoot.get(0));
                while (metadataCopy.getParentElement()!=null) {
                    metadataCopy = metadataCopy.getParentElement();
                }
                metadata = metadataCopy;
            }

            // enumeration
            else if (configFile.contains("enumeration") && configFile.contains(".xml")) {
                // enumerations are similar to codelists, but cannot be extended
                // if field with enumeration contains non-original element an error is thrown
                Codelist enumeration = new Codelist("config/" + configFile);
                List<List<String>> enumerationProps = enumeration.getEntries();
                List<String> enumerationRoot = enumerationProps.get(0);
                List<String> enumerationEntries = enumerationProps.get(1);
                try {
                    if (!enumerationEntries.contains(metadataValue)) {
                        throw new EnumerationException(enumerationRoot.get(0), metadataValue);
                    }
                }
                catch (EnumerationException e) {
                    System.out.println(e.getMessage());
                    System.exit(8); // maybe change to exception propagation to main?
                }

                // create subelement for enumeration as enumerations always have an extra element for its own
                Element metaSub = new Element(enumerationRoot.get(0));
                metaSub.setNamespace(ns.get("cat"));
                metaSub.setAttribute("codeList", enumerationRoot.get(1) + "/codelists.html#" + enumerationRoot.get(0));
                metaSub.setAttribute("codeListValue", metadataValue);
                metadataCopy.removeContent();
                metadataCopy.addContent(metaSub);

                // build up new metadata and nameChain variables with added codelist element
                nameChain.add(enumerationRoot.get(0));
                while (metadataCopy.getParentElement()!=null) {
                    metadataCopy = metadataCopy.getParentElement();
                }
                metadata = metadataCopy;
            }
        }


        // add element to overall list
        int nameChainLength = nameChain.size();
        List<String> allFieldsChildrenNames;
        List<Namespace> allFieldsChildrenNamespaces;

        int idx;
        boolean addValue = true;
        List<Element> allFieldsChildren;
        List<String> allFieldsChildrenUUID = new ArrayList<>();
        int idxUUID;

        for (int i = 1; i < nameChainLength; i++) {
            allFieldsChildrenNames = new ArrayList<>();
            allFieldsChildrenNamespaces = new ArrayList<>();
            for (Element allFieldsChildrenAct : allFields.getChildren()) {
                allFieldsChildrenNames.add(allFieldsChildrenAct.getName());
                allFieldsChildrenNamespaces.add(allFieldsChildrenAct.getNamespace());
            }
            idx = allFieldsChildrenNames.indexOf(nameChain.get(i));

            metadata = metadata.getChildren().get(0).clone();

            if (idx==-1) {
                // Element not available in overall list
                // codelists/enumerations always land here as they are not included yet in de.tu_dresden.zih.geokur.generateMetadata.MetadataTreeTemplate.java
                allFields.addContent(metadata);
                addValue = false;
                break;
            }
            else {
                // Element available in overall list
                allFieldsChildren = allFields.getChildren(allFieldsChildrenNames.get(idx), allFieldsChildrenNamespaces.get(idx));
                for (Element allFieldsChildrenAct : allFieldsChildren) {
                    if (allFieldsChildrenAct.getAttribute("UUID")!=null) {
                        // actual element already has UUID otherwise the following list stays empty
                        allFieldsChildrenUUID.add(allFieldsChildrenAct.getAttributeValue("UUID"));
                    }
                }
                idxUUID = allFieldsChildrenUUID.indexOf(idChain.get(i));

                if (allFieldsChildrenUUID.isEmpty()) {
                    // Element available but has no UUID - fill element
                    allFields = allFields.getChild(allFieldsChildrenNames.get(idx), allFieldsChildrenNamespaces.get(idx));
                    allFields.setAttribute("UUID", idChain.get(i));
                }
                else if (idxUUID == -1) {
                    // Element available and has UUID but the wrong one
                    allFields.addContent(metadata);
                    addValue = false;
                    break;
                }
                else {
                    // Element available and the right UUID
                    allFields = allFieldsChildren.get(idxUUID);
                }
                allFieldsChildrenUUID = new ArrayList<>();
            }
        }

        if (addValue) {
            allFields.addContent(metadataValue);
        }

        while (!(allFields.getParent()==null)) {
            allFields = allFields.getParentElement();
        }

        return allFields;
    }


    private String getConfigFile(List<Element> inElement, String inElementName) {
        // get obligation and occurrence properties of a particular element in a list
        String configFileName = null;

        for (Element inElementAct : inElement) {
            if (inElementAct.getChildText("name").equals(inElementName)) {
                configFileName = inElementAct.getChildText("configFile");
                break;
            }
        }

        return configFileName;
    }
}
