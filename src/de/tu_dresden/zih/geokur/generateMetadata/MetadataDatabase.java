/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package de.tu_dresden.zih.geokur.generateMetadata;

import org.jdom2.Element;

import java.util.List;

public class MetadataDatabase {
//    public Element metadataDocRoot;

    // list variables for table with object links
    public List<Integer> objId;
    public List<String> objName;
    public List<Integer> objParentId;
    public List<Integer> objMetadataId;
    public List<String> objNamespace;
    public List<String> objObligation;
    public List<String> objOccurrence;

    // list variables for table with metadata content
    int mdIdAct = 0;
    public List<Integer> mdId;
    public List<String> mdName;
    public List<String> mdContent;

    // list variables for table with namespaces
    public List<String> namespaceName;
    public List<String> namespaceLink;

    public Element generateFromDocument(Element metadataParent) {
        // generate flat tables for use in database from nested document
        List<Element> metadataChildren = metadataParent.getChildren();
        if (metadataChildren != null) {
            for (Element childAct : metadataChildren) {

            }
        }
        else {
            mdIdAct++;
            mdId.add(mdIdAct);
            mdName.add();
        }
    }

    public void exportToDocument() {
        // export document from flat tables
    }
}
