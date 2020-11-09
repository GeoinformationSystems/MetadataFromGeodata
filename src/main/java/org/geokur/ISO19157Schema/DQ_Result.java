/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19157Schema;

import org.geokur.ISO19115Schema.MD_Scope;
import org.geokur.ISO191xxProfile.MaximumOccurrenceException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "DQ_Result", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
public abstract class DQ_Result {

    // occurrence and obligation
    private final String[] elementName = {"resultScope", "dateTime"};
    private final int[] elementMax = {1, 1};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElementWrapper(name = "resultScope", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
    @XmlElementRef
    public List<MD_Scope> resultScope;

    @XmlElement(name = "dateTime", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
    public List<String> dateTime;

    // variables for correct marshalling of specified classes
    public List<DQ_ConformanceResult> conformanceResult;

    public List<DQ_QuantitativeResult> quantitativeResult;

    public List<DQ_DescriptiveResult> descriptiveResult;

    // methods
    public void addResultScope(MD_Scope resultScope) {
        if (this.resultScope == null) {
            this.resultScope = new ArrayList<>();
        }

        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.resultScope.add(resultScope);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDateTime(String dateTime) {
        if (this.dateTime == null) {
            this.dateTime = new ArrayList<>();
        }

        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.dateTime.add(dateTime);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
