/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package de.tu_dresden.zih.geokur.trimXML;

import org.jdom2.Attribute;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class EmptyFieldsTrimMark implements DeleteInterface {

    public EmptyFieldsTrimMark() {}

    public Element removeElements(Element rootElement) {
        List<String> nonProtectedAttributes = new ArrayList<>();
        nonProtectedAttributes.add("obligation");
        nonProtectedAttributes.add("occurrence");

        for (int i = 0; i < rootElement.getChildren().size(); i++) {
            Element rootElementAct = rootElement.getChildren().get(i);
            if (rootElementAct.getChildren().size()>0) {
                // rootElementAct has children -> dive into these
                DeleteInterface rootElementActChildrenInst = new EmptyFieldsTrimMark();
                rootElementAct = rootElementActChildrenInst.removeElements(rootElementAct);

                // if all children have attribute delete="true" - mark parent as delete="true"
                boolean deleteMarker = true;
                for (int j = 0; j < rootElementAct.getChildren().size(); j++) {
                    String deleteMarkerInStruct = rootElementAct.getChildren().get(j).getAttributeValue("delete");
                    if (deleteMarkerInStruct != null && deleteMarkerInStruct.equals("false")) {
                        deleteMarker = false;
                    }
                }
                if (deleteMarker) {
                    rootElementAct.setAttribute("delete", "true");
                }
                else {
                    rootElementAct.setAttribute("delete", "false");
                }
            }
            else {
                // rootElementAct is the deepest Element on this branch
                // -> check for content
                // attributes that do not mark elements to be protected from deletion: obligation, occurrence
                List<Attribute> rootElementActAtt = rootElementAct.getAttributes();
                List<String> rootElementActAttName = new ArrayList<>();
                for (Attribute rootElementActAttAct : rootElementActAtt) {
                    rootElementActAttName.add(rootElementActAttAct.getName());
                }
                boolean attributeMarker = isOnlyMember(rootElementActAttName, nonProtectedAttributes);

                if (rootElementAct.getValue().isEmpty() && attributeMarker) {
                    rootElementAct.setAttribute("delete", "true");
                }
                else {
                    rootElementAct.setAttribute("delete", "false");
                }
            }

        }

        return rootElement;
    }


    public boolean isOnlyMember(List<String> source, List<String> target) {
        // Test whether a list of strings in source only contains strings from list target
        boolean onlyMember = true;

        for (String sourceAct : source) {
            List<Boolean> sourceMarkAct = new ArrayList<>();
            for (String targetAct : target) {
                sourceMarkAct.add(sourceAct.equalsIgnoreCase(targetAct));
            }
            if (!sourceMarkAct.contains(true)) {
                onlyMember = false;
            }
        }

        return onlyMember;
    }
}
