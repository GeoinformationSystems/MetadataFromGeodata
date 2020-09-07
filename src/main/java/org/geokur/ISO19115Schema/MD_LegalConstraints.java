/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "MD_LegalConstraints", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
public class MD_LegalConstraints extends MD_Constraints {

    // occurrence and obligation
    private final String[] elementName = {"useLimitation", "constraintApplicationScope", "graphic", "reference", "releasability", "responsibleParty", "accessConstraints", "useConstraints", "otherConstraints"};
    private final int[] elementMax = {Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {false, false, false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "accessConstraints", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
    @XmlElementRef
    public List<MD_RestrictionCode> accessConstraints;

    @XmlElementWrapper(name = "useConstraints", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
    @XmlElementRef
    public List<MD_RestrictionCode> useConstraints;

    @XmlElement(name = "otherConstraints", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
    public List<String> otherConstraints;

    // methods
    public MD_LegalConstraints(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_LegalConstraints);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_LegalConstraints);
                if (!tempList.contains(elementName[i])) {
                    // element not mandatory
                    elementObligation[i] = false;
                }
            }
        }
    }

    public void createAccessConstraints() {
        if (this.accessConstraints == null) {
            this.accessConstraints = new ArrayList<>();
        }
    }

    public void createUseConstraints() {
        if (this.useConstraints == null) {
            this.useConstraints = new ArrayList<>();
        }
    }

    public void createOtherConstraints() {
        if (this.otherConstraints == null) {
            this.otherConstraints = new ArrayList<>();
        }
    }

    public void addAccessConstraints(MD_RestrictionCode accessConstraints) {
        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.accessConstraints.add(accessConstraints);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addUseConstraints(MD_RestrictionCode useConstraints) {
        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.useConstraints.add(useConstraints);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOtherConstraints(String otherConstraints) {
        int elementNum = 8;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.otherConstraints.add(otherConstraints);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void finalizeClass() {
        for (int i = 0; i < elementName.length; i++) {
            try {
                List<?> tempList = (List<?>) this.getClass().getField(elementName[i]).get(this);
                if (!elementUsed[i] && tempList != null && !tempList.isEmpty()) {
                    // test profile use
                    throw new ProfileException(className + " - " + elementName[i]);
                }
                if (elementObligation[i] && (tempList == null || tempList.isEmpty())) {
                    // test filling and obligation of all variable lists
                    throw new ObligationException(className + " - " + elementName[i]);
                }
            } catch (ProfileException | ObligationException | NoSuchFieldException | IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
