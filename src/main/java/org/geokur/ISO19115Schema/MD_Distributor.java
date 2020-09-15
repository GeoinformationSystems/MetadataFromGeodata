/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO191xxProfile.MaximumOccurrenceException;
import org.geokur.ISO191xxProfile.ObligationException;
import org.geokur.ISO191xxProfile.ProfileException;
import org.geokur.ISO191xxProfile.ProfileReader;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "MD_Distributor", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
public class MD_Distributor {

    // occurrence and obligation
    private final String[] elementName = {"distributorContact", "distributionOrderProcess", "distributionFormat", "distributorTransferOptions"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "distributorContact", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    @XmlElementRef
    public List<CI_Responsibility> distributorContact;

    @XmlElementWrapper(name = "distributionOrderProcess", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    @XmlElementRef
    public List<MD_StandardOrderProcess> distributionOrderProcess;

    @XmlElementWrapper(name = "distributionFormat", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    @XmlElementRef
    public List<MD_Format> distributionFormat;

    @XmlElementWrapper(name = "distributorTransferOptions", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    @XmlElementRef
    public List<MD_DigitalTransferOptions> distributorTransferOptions;

    // methods
    public MD_Distributor(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_Distributor);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_Distributor);
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

    public void createDistributorContact() {
        if (this.distributorContact == null) {
            this.distributorContact = new ArrayList<>();
        }
    }

    public void createDistributionOrderProcess() {
        if (this.distributionOrderProcess == null) {
            this.distributionOrderProcess = new ArrayList<>();
        }
    }

    public void createDistributionFormat() {
        if (this.distributionFormat == null) {
            this.distributionFormat = new ArrayList<>();
        }
    }

    public void createDistributorTransferOptions() {
        if (this.distributorTransferOptions == null) {
            this.distributorTransferOptions = new ArrayList<>();
        }
    }

    public void addDistributorContact(CI_Responsibility distributorContact) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.distributorContact.add(distributorContact);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDistributionOrderProcess(MD_StandardOrderProcess distributionOrderProcess) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.distributionOrderProcess.add(distributionOrderProcess);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDistributionFormat(MD_Format distributionFormat) {
        int elementNum = 2;
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

    public void addDistributorTransferOptions(MD_DigitalTransferOptions distributorTransferOptions) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.distributorTransferOptions.add(distributorTransferOptions);
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
