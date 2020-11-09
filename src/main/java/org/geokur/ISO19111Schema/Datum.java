/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19111Schema;

import org.geokur.ISO19115Schema.CI_Date;
import org.geokur.ISO191xxProfile.MaximumOccurrenceException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Datum")
public abstract class Datum extends ObjectUsage {
    // TODO: implement concrete classes extending this class

    // occurrence and obligation
    private final String[] elementName = {"name", "identifier", "alias", "remarks",
            "usage",
            "anchorDefinition", "publicationDate", "conventionalRS"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1,
            Integer.MAX_VALUE,
            1, 1, 1};
    private final boolean[] elementObligation = {true, false, false, false,
            false,
            false, false, false};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElement(name = "anchorDefinition")
    public List<String> anchorDefinition;

    @XmlElementWrapper(name = "publicationDate")
    @XmlElementRef
    public List<CI_Date> publicationDate;

    @XmlElementWrapper(name = "conventionalRS")
    @XmlElementRef
    public List<IdentifiedObject> conventionalRS;

    // variables for correct marshalling of specified classes

    // methods
    public void addAnchorDefinition(String anchorDefinition) {
        if (this.anchorDefinition == null) {
            this.anchorDefinition = new ArrayList<>();
        }

        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.anchorDefinition.add(anchorDefinition);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPublicationDate(CI_Date publicationDate) {
        if (this.publicationDate == null) {
            this.publicationDate = new ArrayList<>();
        }

        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.publicationDate.add(publicationDate);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addConventionalRS(IdentifiedObject conventionalRS) {
        if (this.conventionalRS == null) {
            this.conventionalRS = new ArrayList<>();
        }

        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.conventionalRS.add(conventionalRS);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
