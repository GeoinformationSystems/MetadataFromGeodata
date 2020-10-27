/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19111Schema;

import org.geokur.ISO191xxProfile.MaximumOccurrenceException;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "SingleOperation")
public abstract class SingleOperation extends CoordinateOperation {

    // occurrence and obligation
    private final String[] elementName = {"name", "identifier", "alias", "remarks",
            "usage",
            "sourceCRS", "targetCRS", "interpolationCRS", "sourceCoordinateEpoch", "targetCoordinateEpoch", "operationVersion", "coordinateOperationAccuracy",
            "method", "parameterValue"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1,
            Integer.MAX_VALUE,
            1, 1, 1, 1, 1, 1, Integer.MAX_VALUE,
            1, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, false, false, false,
            false,
            false, false, false, false, false, false, false,
            true, false};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElementWrapper(name = "method")
    @XmlElementRef
    public List<OperationMethod> method;

    @XmlElementWrapper(name = "parameterValue")
    @XmlElementRef
    public List<GeneralParameterValue> parameterValue;

    // variables for correct marshalling of specified classes
    public List<Conversion> conversion;

    public List<PointMotionOperation> pointMotionOperation;

    // methods
    public void addMethod(OperationMethod method) {
        if (this.method == null) {
            this.method = new ArrayList<>();
        }

        int elementNum = 12;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.method.add(method);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addParameterValue(GeneralParameterValue parameterValue) {
        if (this.parameterValue == null) {
            this.parameterValue = new ArrayList<>();
        }

        int elementNum = 13;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.parameterValue.add(parameterValue);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
