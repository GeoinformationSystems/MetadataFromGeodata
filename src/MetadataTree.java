/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

//    public Element fillElements(Element allFields, Element metadata) {
    public Element fillElements(Element allFields, List<Element> metadata) {
        // Complement existing document with contents from Elements in metadata

        for (Element metadataAct : metadata) {
            allFields = complementNestedElement(allFields, metadataAct);
        }

        return allFields;
    }


    private Element complementNestedElement(Element allFields, Element metadata) {
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

        for (int i = 1; i < nameChainLength; i++) {
            allFieldsChildrenNames = new ArrayList<>();
            allFieldsChildrenNamespaces = new ArrayList<>();
            for (Element allFieldsChildrenAct : allFields.getChildren()) {
                allFieldsChildrenNames.add(allFieldsChildrenAct.getName());
                allFieldsChildrenNamespaces.add(allFieldsChildrenAct.getNamespace());
            }
            idx = allFieldsChildrenNames.indexOf(nameChain.get(i));

            metadata = metadata.getChildren().get(0).clone();

            if (idx>=0) {
                // Element available in overall list
                allFields = allFields.getChild(allFieldsChildrenNames.get(idx), allFieldsChildrenNamespaces.get(idx));
            }
            else {
                // Element not available in overall list
                allFields.addContent(metadata);
                addValue = false;
                break;
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
