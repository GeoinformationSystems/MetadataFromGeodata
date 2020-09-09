/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO191xxProfile.ObligationException;
import org.geokur.ISO191xxProfile.ProfileException;
import org.geokur.ISO191xxProfile.ProfileReader;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "MD_Dimension", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
public class MD_Dimension {

    // occurrence and obligation
    private final String[] elementName = {"dimensionName", "dimensionSize", "resolution", "dimensionTitle", "dimensionDescription"};
    private final int[] elementMax = {1, 1, 1, 1, 1};
    private final boolean[] elementObligation = {true, true, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "dimensionName", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    @XmlElementRef
    public List<MD_DimensionNameTypeCode> dimensionName;

    @XmlElement(name = "dimensionSize", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    public List<String> dimensionSize;

    @XmlElement(name = "resolution", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    public List<String> resolution;

    @XmlElement(name = "dimensionTitle", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    public List<String> dimensionTitle;

    @XmlElement(name = "dimensionDescription", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    public List<String> dimensionDescription;

    // methods
    public MD_Dimension(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_Dimension);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_Dimension);
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

    public void createDimensionName() {
        if (this.dimensionName == null) {
            this.dimensionName = new ArrayList<>();
        }
    }

    public void createDimensionSize() {
        if (this.dimensionSize == null) {
            this.dimensionSize = new ArrayList<>();
        }
    }

    public void createResolution() {
        if (this.resolution == null) {
            this.resolution = new ArrayList<>();
        }
    }

    public void createDimensionTitle() {
        if (this.dimensionTitle == null) {
            this.dimensionTitle = new ArrayList<>();
        }
    }

    public void createDimensionDescription() {
        if (this.dimensionDescription == null) {
            this.dimensionDescription = new ArrayList<>();
        }
    }

    public void addDimensionName(MD_DimensionNameTypeCode dimensionName) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.dimensionName.add(dimensionName);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDimensionSize(String dimensionSize) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.dimensionSize.add(dimensionSize);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addResolution(String resolution) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.resolution.add(resolution);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDimensionTitle(String dimensionTitle) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.dimensionTitle.add(dimensionTitle);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDimensionDescription(String dimensionDescription) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.dimensionDescription.add(dimensionDescription);
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
