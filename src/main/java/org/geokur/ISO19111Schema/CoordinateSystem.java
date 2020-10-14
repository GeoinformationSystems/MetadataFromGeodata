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

@XmlRootElement(name = "CoordinateSystem")
public abstract class CoordinateSystem extends IdentifiedObject {
    // TODO: implement concrete classes extending this class

    // occurrence and obligation
    private final String[] elementName = {"name", "identifier", "alias", "remarks",
            "axis"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1,
            Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, false, false, false,
            true};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElementWrapper(name = "axis")
    @XmlElementRef
    public List<SingleCRS> axis;

    // variables for correct marshalling of specified classes

    // methods
    public void createAxis() {
        if (this.axis == null) {
            this.axis = new ArrayList<>();
        }
    }

    public void addAxis(SingleCRS axis) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.axis.add(axis);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
