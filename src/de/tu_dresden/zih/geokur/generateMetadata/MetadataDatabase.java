/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package de.tu_dresden.zih.geokur.generateMetadata;

import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.ArrayList;
import java.util.List;

public class MetadataDatabase {
//    public Element metadataDocRoot;

    // list variables for table with object links
    int childIdAct = 1;
    public List<Integer> objId = new ArrayList<>();
    public List<String> objName = new ArrayList<>();
    public List<String> objNamespace = new ArrayList<>();
    public List<Integer> objParentId = new ArrayList<>();
    public List<String> objParentName = new ArrayList<>();
    public List<Integer> objMetadataId = new ArrayList<>();
    public List<String> objObligation = new ArrayList<>();
    public List<String> objOccurrence = new ArrayList<>();

    // list variables for table with metadata content
    int mdIdAct = 0;
    public List<Integer> mdId = new ArrayList<>();
    public List<String> mdName = new ArrayList<>();
    public List<String> mdContent = new ArrayList<>();

    // list variables for table with namespaces
    public List<Namespace> namespaces = new ArrayList<>();
    public List<String> namespacePrefix = new ArrayList<>();
    public List<String> namespaceURI = new ArrayList<>();

    public void generateFromDocument(Element metadataParent) {
        // generate flat tables for use in database from nested document

        int parentIdAct = childIdAct;
        if (childIdAct == 1) {
            // first call -> get information about root element
            fillObjLists(null, metadataParent, parentIdAct, null);
        }
        List<Element> metadataChildren = metadataParent.getChildren();

        Namespace namespaceAct = metadataParent.getNamespace();
        if (!namespaces.contains(namespaceAct)) {
            // new namespace to be added to list
            namespaces.add(namespaceAct);
            namespacePrefix.add(namespaceAct.getPrefix());
            namespaceURI.add(namespaceAct.getURI());
        }

        if (!metadataChildren.isEmpty()) {
            for (Element childAct : metadataChildren) {
                childIdAct++;
                // get children of actual child -> if none, add metadata content
                List<Element> metadataChildrenChildren = childAct.getChildren();
                if (metadataChildrenChildren.isEmpty()) {
                    mdIdAct++;
                    fillMdLists(childAct);
                    fillObjLists(metadataParent, childAct, parentIdAct, mdIdAct);
                }

                fillObjLists(metadataParent, childAct, parentIdAct, null);
                generateFromDocument(childAct);
            }
        }
    }

    public void fillObjLists(Element parentAct, Element childAct, int parentIdAct, Integer metadataId) {
        // helper method for adding elements to object list

        objId.add(childIdAct);
        objName.add(childAct.getName());
        objNamespace.add(childAct.getNamespacePrefix());
        if (parentAct != null) {
            objParentId.add(parentIdAct);
            objParentName.add(parentAct.getName());
        }
        else {
            objParentId.add(null);
            objParentName.add(null);
        }
        objMetadataId.add(metadataId);
        objObligation.add(childAct.getAttributeValue("obligation"));
        objOccurrence.add(childAct.getAttributeValue("occurrence"));
    }

    public void fillMdLists(Element childAct) {
        // helper method for adding elements to metadata list

        mdId.add(mdIdAct);
        mdName.add(childAct.getName());
        mdContent.add(childAct.getValue());
    }

//    public void exportToDocument() {
//        // export document from flat tables
//    }
}
