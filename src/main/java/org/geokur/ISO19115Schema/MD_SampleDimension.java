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

@XmlRootElement(name = "MD_SampleDimension", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
public class MD_SampleDimension extends MD_RangeDimension {

    // occurrence and obligation
    private final String[] elementName = {"sequenceIdentifier", "description", "name", "maxValue", "minValue", "units", "scaleFactor", "offset", "meanValue", "numberOfValues", "standardDeviation", "otherPropertyType", "otherProperty", "bitsPerValue"};
    private final int[] elementMax = {1, 1, Integer.MAX_VALUE, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private final boolean[] elementObligation = {false, false, false, false, false, false, false, false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "maxValue", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> maxValue;

    @XmlElement(name = "minValue", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> minValue;

    @XmlElement(name = "units", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> units;

    @XmlElement(name = "scaleFactor", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> scaleFactor;

    @XmlElement(name = "offset", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> offset;

    @XmlElement(name = "meanValue", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> meanValue;

    @XmlElement(name = "numberOfValues", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> numberOfValues;

    @XmlElement(name = "standardDeviation", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> standardDeviation;

    @XmlElement(name = "otherPropertyType", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> otherPropertyType;

    @XmlElement(name = "otherProperty", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> otherProperty;

    @XmlElement(name = "bitsPerValue", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> bitsPerValue;

    // variables for correct marshalling of specified classes
    public List<MD_Band> band;

    // methods
    public MD_SampleDimension(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_SampleDimension);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_SampleDimension);
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

    public void createMaxValue() {
        if (this.maxValue == null) {
            this.maxValue = new ArrayList<>();
        }
    }

    public void createMinValue() {
        if (this.minValue == null) {
            this.minValue = new ArrayList<>();
        }
    }

    public void createUnits() {
        if (this.units == null) {
            this.units = new ArrayList<>();
        }
    }

    public void createScaleFactor() {
        if (this.scaleFactor == null) {
            this.scaleFactor = new ArrayList<>();
        }
    }

    public void createOffset() {
        if (this.offset == null) {
            this.offset = new ArrayList<>();
        }
    }

    public void createMeanValue() {
        if (this.meanValue == null) {
            this.meanValue = new ArrayList<>();
        }
    }

    public void createNumberOfValues() {
        if (this.numberOfValues == null) {
            this.numberOfValues = new ArrayList<>();
        }
    }

    public void createStandardDeviation() {
        if (this.standardDeviation == null) {
            this.standardDeviation = new ArrayList<>();
        }
    }

    public void createOtherPropertyType() {
        if (this.otherPropertyType == null) {
            this.otherPropertyType = new ArrayList<>();
        }
    }

    public void createOtherProperty() {
        if (this.otherProperty == null) {
            this.otherProperty = new ArrayList<>();
        }
    }

    public void createBitsPerValue() {
        if (this.bitsPerValue == null) {
            this.bitsPerValue = new ArrayList<>();
        }
    }

    public void addMaxValue(String maxValue) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.maxValue.add(maxValue);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMinValue(String minValue) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.minValue.add(minValue);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addUnits(String units) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.units.add(units);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addScaleFactor(String scaleFactor) {
        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.scaleFactor.add(scaleFactor);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOffset(String offset) {
        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.offset.add(offset);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMeanValue(String meanValue) {
        int elementNum = 8;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.meanValue.add(meanValue);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addNumberOfValues(String numberOfValues) {
        int elementNum = 9;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.numberOfValues.add(numberOfValues);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addStandardDeviation(String standardDeviation) {
        int elementNum = 10;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.standardDeviation.add(standardDeviation);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOtherPropertyType(String otherPropertyType) {
        int elementNum = 11;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.otherPropertyType.add(otherPropertyType);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOtherProperty(String otherProperty) {
        int elementNum = 12;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.otherProperty.add(otherProperty);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addBitsPerValue(String bitsPerValue) {
        int elementNum = 13;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.bitsPerValue.add(bitsPerValue);
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
