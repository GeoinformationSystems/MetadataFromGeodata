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

@XmlRootElement(name = "MD_DigitalTransferOptions", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
public class MD_DigitalTransferOptions {

    // occurrence and obligation
    private final String[] elementName = {"unitsOfDistribution", "transferSize", "onLine", "offLine", "transferFrequency", "distributionFormat"};
    private final int[] elementMax = {1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "unitsOfDistribution", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    public List<String> unitsOfDistribution;

    @XmlElement(name = "transferSize", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    public List<String> transferSize;

    @XmlElementWrapper(name = "onLine", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    @XmlElementRef
    public List<CI_OnlineResource> onLine;

    @XmlElementWrapper(name = "offLine", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    @XmlElementRef
    public List<MD_Medium> offLine;

    @XmlElement(name = "transferFrequency", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    public List<String> transferFrequency;

    @XmlElementWrapper(name = "distributionFormat", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    @XmlElementRef
    public List<MD_Format> distributionFormat;

    // methods
    public MD_DigitalTransferOptions(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_DigitalTransferOptions);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_DigitalTransferOptions);
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

    public void createUnitsOfDistribution() {
        if (this.unitsOfDistribution == null) {
            this.unitsOfDistribution = new ArrayList<>();
        }
    }

    public void createTransferSize() {
        if (this.transferSize == null) {
            this.transferSize = new ArrayList<>();
        }
    }

    public void createOnLine() {
        if (this.onLine == null) {
            this.onLine = new ArrayList<>();
        }
    }

    public void createOffLine() {
        if (this.offLine == null) {
            this.offLine = new ArrayList<>();
        }
    }

    public void createTransferFrequency() {
        if (this.transferFrequency == null) {
            this.transferFrequency = new ArrayList<>();
        }
    }

    public void createDistributionFormat() {
        if (this.distributionFormat == null) {
            this.distributionFormat = new ArrayList<>();
        }
    }

    public void addUnitsOfDistribution(String unitsOfDistribution) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.unitsOfDistribution.add(unitsOfDistribution);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTransferSize(String transferSize) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.transferSize.add(transferSize);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOnLine(CI_OnlineResource onLine) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.onLine.add(onLine);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOffLine(MD_Medium offLine) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.offLine.add(offLine);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTransferFrequency(String transferFrequency) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.transferFrequency.add(transferFrequency);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDistributionFormat(MD_Format distributionFormat) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.distributionFormat.add(distributionFormat);
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
