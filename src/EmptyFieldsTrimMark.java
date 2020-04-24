/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

import org.jdom2.Element;

public class EmptyFieldsTrimMark implements DeleteInterface {

    public EmptyFieldsTrimMark() {}

    public Element removeElements(Element rootElement) {

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
                if (rootElementAct.getValue().isEmpty() && rootElementAct.getAttributes().isEmpty()) {
                    rootElementAct.setAttribute("delete", "true");
                }
                else {
                    rootElementAct.setAttribute("delete", "false");
                }
            }

        }

        return rootElement;
    }
}
