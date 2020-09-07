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

@XmlRootElement(name = "MD_SecurityConstraints", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
public class MD_SecurityConstraints extends MD_Constraints {

    // occurrence and obligation
    private final String[] elementName = {"useLimitation", "constraintApplicationScope", "graphic", "reference", "releasability", "responsibleParty", "classification", "userNote", "classificationSystem", "handlingDescription"};
    private final int[] elementMax = {Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, 1, 1, 1};
    private final boolean[] elementObligation = {false, false, false, false, false, false, true, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "classification", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
    @XmlElementRef
    public List<MD_ClassificationCode> classification;

    @XmlElement(name = "userNote", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
    public List<String> userNote;

    @XmlElement(name = "classificationSystem", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
    public List<String> classificationSystem;

    @XmlElement(name = "handlingDescription", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
    public List<String> handlingDescription;

    // methods
    public MD_SecurityConstraints(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_SecurityConstraints);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_SecurityConstraints);
                if (!tempList.contains(elementName[i])) {
                    // element not mandatory
                    elementObligation[i] = false;
                }
            }
        }
    }

    public void createClassification() {
        if (this.classification == null) {
            this.classification = new ArrayList<>();
        }
    }

    public void createUserNote() {
        if (this.userNote == null) {
            this.userNote = new ArrayList<>();
        }
    }

    public void createClassificationSystem() {
        if (this.classificationSystem == null) {
            this.classificationSystem = new ArrayList<>();
        }
    }

    public void createHandlingDescription() {
        if (this.handlingDescription == null) {
            this.handlingDescription = new ArrayList<>();
        }
    }

    public void addClassification(MD_ClassificationCode classification) {
        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.classification.add(classification);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addUserNote(String userNote) {
        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.userNote.add(userNote);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addClassificationSystem(String classificationSystem) {
        int elementNum = 8;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.classificationSystem.add(classificationSystem);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addHandlingDescription(String handlingDescription) {
        int elementNum = 9;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.handlingDescription.add(handlingDescription);
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
