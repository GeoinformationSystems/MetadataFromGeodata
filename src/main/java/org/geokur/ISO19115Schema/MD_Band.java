/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO191xxProfile.ObligationException;
import org.geokur.ISO191xxProfile.ProfileException;
import org.geokur.ISO191xxProfile.ProfileReader;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "MD_Band", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
public class MD_Band extends MD_SampleDimension {

    // occurrence and obligation
    private final String[] elementName = {"sequenceIdentifier", "description", "name", "maxValue", "minValue", "units", "scaleFactor", "offset", "meanValue", "numberOfValues", "standardDeviation", "otherPropertyType", "otherProperty", "bitsPerValue", "boundMax", "boundMin", "boundUnit", "peakResponse", "toneGradation"};
    private final int[] elementMax = {1, 1, Integer.MAX_VALUE, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private final boolean[] elementObligation = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "boundMax", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> boundMax;

    @XmlElement(name = "boundMin", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> boundMin;

    @XmlElement(name = "boundUnit", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> boundUnit;

    @XmlElement(name = "peakResponse", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> peakResponse;

    @XmlElement(name = "toneGradation", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> toneGradation;

    // methods
    public MD_Band(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_Band);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_Band);
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

    public void createBoundMax() {
        if (this.boundMax == null) {
            this.boundMax = new ArrayList<>();
        }
    }

    public void createBoundMin() {
        if (this.boundMin == null) {
            this.boundMin = new ArrayList<>();
        }
    }

    public void createBoundUnit() {
        if (this.boundUnit == null) {
            this.boundUnit = new ArrayList<>();
        }
    }

    public void createPeakResponse() {
        if (this.peakResponse == null) {
            this.peakResponse = new ArrayList<>();
        }
    }

    public void createToneGradation() {
        if (this.toneGradation == null) {
            this.toneGradation = new ArrayList<>();
        }
    }

    public void addBoundMax(String boundMax) {
        int elementNum = 14;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.boundMax.add(boundMax);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addBoundMin(String boundMin) {
        int elementNum = 15;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.boundMin.add(boundMin);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addBoundUnit(String boundUnit) {
        int elementNum = 16;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.boundUnit.add(boundUnit);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPeakResponse(String peakResponse) {
        int elementNum = 17;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.peakResponse.add(peakResponse);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addToneGradation(String toneGradation) {
        int elementNum = 18;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.toneGradation.add(toneGradation);
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
