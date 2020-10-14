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

@XmlRootElement(name = "ObjectUsage")
public abstract class ObjectUsage extends IdentifiedObject {

    // occurrence and obligation
    private final String[] elementName = {"name", "identifier", "alias", "remarks",
            "usage"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1,
            Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, false, false, false,
            false};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElementWrapper(name = "usage")
    @XmlElementRef
    public List<ObjectDomain> usage;

    // variables for correct marshalling of specified classes
    public List<CRS> crs;

    public List<CoordinateOperation> coordinateOperation;

    public List<Datum> datum;

    public List<DatumEnsemble> datumEnsemble;

    // methods
    public void createUsage() {
        if (this.usage == null) {
            this.usage = new ArrayList<>();
        }
    }

    public void addUsage(ObjectDomain usage) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.usage.add(usage);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
