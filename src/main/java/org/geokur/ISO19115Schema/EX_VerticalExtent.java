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

@XmlRootElement(name = "EX_VerticalExtent", namespace = "http://standards.iso.org/iso/19115/-3/gex/1.0")
public class EX_VerticalExtent {

    // occurrence and obligation
    private final String[] elementName = {"minimumValue", "maximumValue", "verticalCRS", "verticalCRSId"};
    private final int[] elementMax = {1, 1, 1, 1};
    private final boolean[] elementObligation = {true, true, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "minimumValue", namespace = "http://standards.iso.org/iso/19115/-3/gex/1.0")
    public List<String> minimumValue;

    @XmlElement(name = "maximumValue", namespace = "http://standards.iso.org/iso/19115/-3/gex/1.0")
    public List<String> maximumValue;

    @XmlElement(name = "verticalCRS", namespace = "http://standards.iso.org/iso/19115/-3/gex/1.0")
    public List<String> verticalCRS;

    @XmlElementWrapper(name = "verticalCRSId", namespace = "http://standards.iso.org/iso/19115/-3/gex/1.0")
    @XmlElementRef
    public List<MD_ReferenceSystem> verticalCRSId;

    // methods
    public EX_VerticalExtent(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.EX_VerticalExtent);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.EX_VerticalExtent);
                if (!tempList.contains(elementName[i])) {
                    // element not mandatory
                    elementObligation[i] = false;
                } else if (tempList.contains(elementName[i])) {
                    // element mandatory
                    elementObligation[i] = true;
                }
            }
        }
    }

    public void createMinimumValue() {
        if (this.minimumValue == null) {
            this.minimumValue = new ArrayList<>();
        }
    }

    public void createMaximumValue() {
        if (this.maximumValue == null) {
            this.maximumValue = new ArrayList<>();
        }
    }

    public void createVerticalCRS() {
        if (this.verticalCRS == null) {
            this.verticalCRS = new ArrayList<>();
        }
    }

    public void createVerticalCRSId() {
        if (this.verticalCRSId == null) {
            this.verticalCRSId = new ArrayList<>();
        }
    }

    public void addMinimumValue(String minimumValue) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.minimumValue.add(minimumValue);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMaximumValue(String maximumValue) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.maximumValue.add(maximumValue);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addVerticalCRS(String verticalCRS) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.verticalCRS.add(verticalCRS);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addVerticalCRSId(MD_ReferenceSystem verticalCRSId) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.verticalCRSId.add(verticalCRSId);
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
