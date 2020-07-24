/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.generateMetadata;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NestedElement {

    public Element create(String[] nameChain, UUID[] idChain, String value, Map<String, Namespace> ns) {
        // creation of one metadata entry (whole nested chain to be included in overall metadata using ComplementNestedElement)
        int nameChainLength = nameChain.length;

        // get all namespaces from nameChain from original config files
        // additional add information about obligation and occurrence
        List<Namespace> namespaceList = new ArrayList<>();
        List<String> obligation = new ArrayList<>();
        List<String> occurrence = new ArrayList<>();
        String configFile;
        Element configRootElement = null;
        String configRootNamespace = null;

        obligation.add("M"); // fill first entry for DS_Resource (not in files explicitly given)
        occurrence.add("1");

        for (int i = 0; i < java.lang.Math.floor((double) nameChainLength/2); i++) {
            configFile = "config/config_" + nameChain[i*2] + ".xml";
            try {
                configRootElement = new SAXBuilder().build(configFile).getRootElement();
            }
            catch (JDOMException | IOException e) {
                System.out.println(e.getMessage());
            }

            if (configRootElement != null) {
                configRootNamespace = configRootElement.getAttributeValue("namespace");
                List<Element> configRootChildren = configRootElement.getChildren("element");
                List<String> obligationOccurrence = getObligationOccurrence(configRootChildren, nameChain[2*i + 1]);

                obligation.add(obligationOccurrence.get(0)); // two times because subelements of classes have the same obligation and occurrence as the class itself
                obligation.add(obligationOccurrence.get(0));
                occurrence.add(obligationOccurrence.get(1));
                occurrence.add(obligationOccurrence.get(1));
            }

            namespaceList.add(ns.get(configRootNamespace)); // two times because subelements of classes have the same namespace as the class itself
            namespaceList.add(ns.get(configRootNamespace));
        }
        if (namespaceList.size()==nameChainLength-1) {
            // add one last element - uneven lengths occur in case of codelists and enumerations -> hard coded namespace xs
            namespaceList.add(ns.get("xs"));
        }

        // build element chain with particular metadata
        Element element = new Element(nameChain[nameChainLength - 1]);
        element.setNamespace(namespaceList.get(nameChainLength - 1));
        element.addContent(value);
        element.setAttribute("obligation", obligation.get(nameChainLength - 1));
        element.setAttribute("occurrence", occurrence.get(nameChainLength - 1));
        element.setAttribute("UUID", idChain[nameChainLength - 1].toString());
        Element elementTmp;
        for (int i = nameChainLength - 2; i >= 0; i--) {
            elementTmp = element;
            element.removeChild(nameChain[i + 1]);
            element = new Element(nameChain[i]);
            element.setNamespace(namespaceList.get(i));
            element.addContent(elementTmp);
            element.setAttribute("obligation", obligation.get(i));
            element.setAttribute("occurrence", occurrence.get(i));
            element.setAttribute("UUID", idChain[i].toString());
        }

        return element;
    }

    public Element complementNestedElement(Element nestedElement, String[] nameChain, UUID[] idChain, String value, Map<String, Namespace> ns) {
        // create actual metadata and add to overall structure in nested elements (DOM)
        
        int nameChainLength = nameChain.length;
        NestedElement nestedElementHere = new NestedElement();
        Element complementElementTmp = nestedElementHere.create(nameChain, idChain, value, ns);
        for (int i = 1; i < nameChainLength; i++) {
            if (nestedElement.getChild(nameChain[i])==null) {
                // the actual element is not available -> stop here and complement element
                nestedElement.addContent(complementElementTmp.getChild(nameChain[i]).clone());

                while (!(nestedElement.getParent()==null)) {
                    nestedElement = nestedElement.getParentElement();
                }
                break;
            }
            else if (!nestedElement.getChild(nameChain[i]).getAttributeValue("UUID").equals(idChain[i].toString())) {
                // the actual element is available, but with a wrong UUID -> stop here and complement element
                nestedElement.addContent(complementElementTmp.getChild(nameChain[i]).clone());

                while (!(nestedElement.getParent()==null)) {
                    nestedElement = nestedElement.getParentElement();
                }
                break;
            }
            else {
                // the actual element with the correct UUID is available -> dive into child
                complementElementTmp = complementElementTmp.getChild(nameChain[i]);
                nestedElement = nestedElement.getChild(nameChain[i]);
            }
        }

        return nestedElement;
    }

    private List<String> getObligationOccurrence(List<Element> inElement, String inElementName) {
        // get obligation and occurrence properties of a particular element in a list
        List<String> obligationOccurrence = new ArrayList<>();

        for (Element inElementAct : inElement) {
            if (inElementAct.getChildText("name").equals(inElementName)) {
                obligationOccurrence.add(inElementAct.getChildText("obligation"));
                obligationOccurrence.add(inElementAct.getChildText("occurrence"));
                break;
            }
        }
        if (obligationOccurrence.isEmpty()) {
            System.out.println("For element " + inElementName + " no obligation and occurrence properties were found.");
        }

        return obligationOccurrence;
    }
}
