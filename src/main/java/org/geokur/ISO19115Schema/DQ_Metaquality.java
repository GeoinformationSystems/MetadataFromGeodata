/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "DQ_Metaquality", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
public abstract class DQ_Metaquality extends DQ_Element {

    // occurrence and obligation
    private final String[] elementName = {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement", "relatedElement"};
    private final int[] elementMax = {1, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "relatedElement", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
    @XmlElementRef
    public List<DQ_Element> relatedElement;

    // methods
    public void createRelatedElement() {
        if (this.relatedElement == null) {
            this.relatedElement = new ArrayList<>();
        }
    }

    public void addRelatedElement(DQ_Element relatedElement) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.relatedElement.add(relatedElement);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
