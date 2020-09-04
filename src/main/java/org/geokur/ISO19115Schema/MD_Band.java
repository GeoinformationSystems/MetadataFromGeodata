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

@XmlRootElement(name = "MD_Band", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
public class MD_Band {

    // occurrence and obligation
    private final String[] elementName = {"sequenceIdentifier", "description", "name", "maxValue", "minValue", "units", "scaleFactor", "offset", "meanValue", "numberOfValues", "standardDeviation", "otherPropertyType", "otherProperty", "bitsPerValue", "boundMax", "boundMin", "boundUnit", "peakResponse", "toneGradation"};
    private final int[] elementMax = {1, 1, Integer.MAX_VALUE, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private final boolean[] elementObligation = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "sequenceIdentifier", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> sequenceIdentifier;

    @XmlElement(name = "description", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> description;

    @XmlElementWrapper(name = "name", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    @XmlElementRef
    public List<MD_Identifier> name;

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
                }
            }
        }
    }

    public void createSequenceIdentifier() {
        if (this.sequenceIdentifier == null) {
            this.sequenceIdentifier = new ArrayList<>();
        }
    }

    public void createDescription() {
        if (this.description == null) {
            this.description = new ArrayList<>();
        }
    }

    public void createName() {
        if (this.name == null) {
            this.name = new ArrayList<>();
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

    public void addSequenceIdentifier(String sequenceIdentifier) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.sequenceIdentifier.add(sequenceIdentifier);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDescription(String description) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.description.add(description);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addName(MD_Identifier name) {
        int elementNum = 2;
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
