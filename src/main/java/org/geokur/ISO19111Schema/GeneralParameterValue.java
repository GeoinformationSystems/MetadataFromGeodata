/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19111Schema;

import org.geokur.ISO191xxProfile.MaximumOccurrenceException;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "GeneralParameterValue")
public abstract class GeneralParameterValue {

    // occurrence and obligation
    private final String[] elementName = {"parameter"};
    private final int[] elementMax = {1};
    private final boolean[] elementObligation = {true};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElementWrapper(name = "parameter")
    @XmlElementRef
    public List<GeneralOperationParameter> parameter;

    // methods
    public void addParameter(GeneralOperationParameter parameter) {
        if (this.parameter == null) {
            this.parameter = new ArrayList<>();
        }

        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.parameter.add(parameter);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
