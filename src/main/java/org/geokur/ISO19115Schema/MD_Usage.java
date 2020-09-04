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

@XmlRootElement(name = "MD_Usage", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
public class MD_Usage {

    // occurrence and obligation
    private final String[] elementName = {"specificUsage", "usageDateTime", "userDeterminedLimitations", "userContactInfo", "response", "additionalDocumentation", "identifiedIssues"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "specificUsage", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> specificUsage;

    @XmlElement(name = "usageDateTime", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> usageDateTime;

    @XmlElement(name = "userDeterminedLimitations", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> userDeterminedLimitations;

    @XmlElementWrapper(name = "userContactInfo", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<CI_Responsibility> userContactInfo;

    @XmlElement(name = "response", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> response;

    @XmlElementWrapper(name = "additionalDocumentation", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<CI_Citation> additionalDocumentation;

    @XmlElementWrapper(name = "identifiedIssues", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<CI_Citation> identifiedIssues;

    // methods
    public MD_Usage(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_Usage);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_Usage);
                if (!tempList.contains(elementName[i])) {
                    // element not mandatory
                    elementObligation[i] = false;
                }
            }
        }
    }

    public void createSpecificUsage() {
        if (this.specificUsage == null) {
            this.specificUsage = new ArrayList<>();
        }
    }

    public void createUsageDateTime() {
        if (this.usageDateTime == null) {
            this.usageDateTime = new ArrayList<>();
        }
    }

    public void createUserDeterminedLimitations() {
        if (this.userDeterminedLimitations == null) {
            this.userDeterminedLimitations = new ArrayList<>();
        }
    }

    public void createUserContactInfo() {
        if (this.userContactInfo == null) {
            this.userContactInfo = new ArrayList<>();
        }
    }

    public void createResponse() {
        if (this.response == null) {
            this.response = new ArrayList<>();
        }
    }

    public void createAdditionalDocumentation() {
        if (this.additionalDocumentation == null) {
            this.additionalDocumentation = new ArrayList<>();
        }
    }

    public void createIdentifiedIssues() {
        if (this.identifiedIssues == null) {
            this.identifiedIssues = new ArrayList<>();
        }
    }

    public void addSpecificUsage(String specificUsage) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.specificUsage.add(specificUsage);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addUsageDateTime(String usageDateTime) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.usageDateTime.add(usageDateTime);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addUserDeterminedLimitations(String userDeterminedLimitations) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.userDeterminedLimitations.add(userDeterminedLimitations);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addUserContactInfo(CI_Responsibility userContactInfo) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.userContactInfo.add(userContactInfo);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addResponse(String response) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.response.add(response);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAdditionalDocumentation(CI_Citation additionalDocumentation) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.additionalDocumentation.add(additionalDocumentation);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addIdentifiedIssues(CI_Citation identifiedIssues) {
        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.identifiedIssues.add(identifiedIssues);
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
