/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO191xxProfile.MaximumOccurrenceException;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "CI_Party", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
public abstract class CI_Party {

    // occurrence and obligation
    private final String[] elementName = {"name", "contactInfo"};
    private final int[] elementMax = {1, Integer.MAX_VALUE};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElement(name = "name", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    public List<String> name;

    @XmlElementWrapper(name = "contactInfo", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    @XmlElementRef
    public List<CI_Contact> contactInfo;

    // variables for correct marshalling of specified classes
    public List<CI_Individual> individual;

    public List<CI_Organisation> organisation;

    // methods
    public void addName(String name) {
        if (this.name == null) {
            this.name = new ArrayList<>();
        }

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

    public void addContactInfo(CI_Contact contactInfo) {
        if (this.contactInfo == null) {
            this.contactInfo = new ArrayList<>();
        }

        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.contactInfo.add(contactInfo);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
