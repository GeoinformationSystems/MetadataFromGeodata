/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package de.tu_dresden.zih.geokur.trimXML;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class MetadataTreeTrimmed implements DeleteInterface {

    public MetadataTreeTrimmed() {}

    public Element removeElements(Element rootElement) {

        if (rootElement.getChildren().size()>0) {
            List<Element> childElements = new ArrayList<>();
            for (int i = 0; i < rootElement.getChildren().size(); i++) {
                childElements.add(i, rootElement.getChildren().get(i));
            }

            for (Element rootElementAct : childElements) {

                String deleteMarker = rootElementAct.getAttributeValue("delete");
                if (deleteMarker!=null && deleteMarker.equals("true")) {
                    rootElementAct.detach();
                }
                else {
                    rootElementAct.removeAttribute("delete");
                    DeleteInterface rootElementActChildrenInst = new MetadataTreeTrimmed();
                    rootElementActChildrenInst.removeElements(rootElementAct);
                }
            }
        }
        else {
            String deleteMarker = rootElement.getAttributeValue("delete");
            if (deleteMarker!=null && deleteMarker.equals("true")) {
                rootElement.detach();
            }
            else {
                rootElement.removeAttribute("delete");
            }
        }

        return rootElement;
    }
}
