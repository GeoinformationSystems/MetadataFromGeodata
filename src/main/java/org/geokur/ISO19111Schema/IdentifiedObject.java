/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19111Schema;

import org.geokur.ISO19115Schema.MD_Identifier;
import org.geokur.ISO191xxProfile.MaximumOccurrenceException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "IdentifiedObject")
public abstract class IdentifiedObject {

    // occurrence and obligation
    private final String[] elementName = {"name", "identifier", "alias", "remarks"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1};
    private final boolean[] elementObligation = {true, false, false, false};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElementWrapper(name = "name")
    @XmlElementRef
    public List<MD_Identifier> name;

    @XmlElementWrapper(name = "identifier")
    @XmlElementRef
    public List<MD_Identifier> identifier;

    @XmlElement(name = "alias")
    public List<String> alias;

    @XmlElement(name = "remarks")
    public List<String> remarks;

    // variables for correct marshalling of specified classes
    public List<ObjectUsage> objectUsage;

    public List<CoordinateSystem> coordinateSystem;

    public List<GeneralOperationParameter> generalOperationParameter;

    public List<OperationMethod> operationMethod;

    // methods
    public void createName() {
        if (this.name == null) {
            this.name = new ArrayList<>();
        }
    }

    public void createIdentifier() {
        if (this.identifier == null) {
            this.identifier = new ArrayList<>();
        }
    }

    public void createAlias() {
        if (this.alias == null) {
            this.alias = new ArrayList<>();
        }
    }

    public void createRemarks() {
        if (this.remarks == null) {
            this.remarks = new ArrayList<>();
        }
    }

    public void addName(MD_Identifier name) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.name.add(name);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addIdentifier(MD_Identifier identifier) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.identifier.add(identifier);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAlias(String alias) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.alias.add(alias);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addRemarks(String remarks) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.remarks.add(remarks);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
