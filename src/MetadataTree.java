/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.ArrayList;
import java.util.List;

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

    public Element fillElements(Element allFields, List<Element> metadata) {
        // Complement existing document with contents from Elements in metadata

        for (Element metadataAct : metadata) {
            allFields = complementNestedElement(allFields, metadataAct);
        }

        return allFields;
    }


    private Element complementNestedElement(Element allFields, Element metadata) {
//        Namespace allFieldsNamespace = allFields.getNamespace();
        if (!allFields.getName().equals(metadata.getName())) {
            // allFields and metadata must have the same first element
            System.out.println("Metadata element " + metadata.getName() + " not equal to tree base " + allFields.getName());
            return allFields;
        }
        else if(metadata.getAttribute("UUID")==null) {
            // metadata must have an UUID for the first element (and for all other, although not relevant here)
            System.out.println("Metadata element has no attribute UUID: not valid.");
            return allFields;
        }
        else if (allFields.getAttribute("UUID")==null) {
            // no UUID assigned yet
            allFields.setAttribute("UUID", metadata.getAttributeValue("UUID"));
        }

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

        String metadataValue = metadataCopy.getValue();

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
}
