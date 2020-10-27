/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO19115_2Schema.LE_Source;
import org.geokur.ISO191xxProfile.MaximumOccurrenceException;
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

@XmlRootElement(name = "LI_Source", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
public class LI_Source {

    // occurrence and obligation
    private final String[] elementName = {"description", "sourceSpatialResolution", "sourceReferenceSystem", "sourceCitation", "sourceMetadata", "scope", "sourceStep"};
    private final int[] elementMax = {1, 1, 1, 1, Integer.MAX_VALUE, 1, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "description", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    public List<String> description;

    @XmlElementWrapper(name = "sourceSpatialResolution", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    @XmlElementRef
    public List<MD_Resolution> sourceSpatialResolution;

    @XmlElementWrapper(name = "sourceReferenceSystem", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    @XmlElementRef
    public List<MD_ReferenceSystem> sourceReferenceSystem;

    @XmlElementWrapper(name = "sourceCitation", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    @XmlElementRef
    public List<CI_Citation> sourceCitation;

    @XmlElementWrapper(name = "sourceMetadata", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    @XmlElementRef
    public List<CI_Citation> sourceMetadata;

    @XmlElementWrapper(name = "scope", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    @XmlElementRef
    public List<MD_Scope> scope;

    @XmlElementWrapper(name = "sourceStep", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    @XmlElementRef
    public List<LI_ProcessStep> sourceStep;

    // variables for correct marshalling of specified classes
    public List<LE_Source> source;

    // methods
    public LI_Source(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.LI_Source);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.LI_Source);
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

    public void addDescription(String description) {
        if (this.description == null) {
            this.description = new ArrayList<>();
        }

        int elementNum = 0;
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

    public void addSourceSpatialResolution(MD_Resolution sourceSpatialResolution) {
        if (this.sourceSpatialResolution == null) {
            this.sourceSpatialResolution = new ArrayList<>();
        }

        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.sourceSpatialResolution.add(sourceSpatialResolution);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSourceReferenceSystem(MD_ReferenceSystem sourceReferenceSystem) {
        if (this.sourceReferenceSystem == null) {
            this.sourceReferenceSystem = new ArrayList<>();
        }

        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.sourceReferenceSystem.add(sourceReferenceSystem);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSourceCitation(CI_Citation sourceCitation) {
        if (this.sourceCitation == null) {
            this.sourceCitation = new ArrayList<>();
        }

        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.sourceCitation.add(sourceCitation);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSourceMetadata(CI_Citation sourceMetadata) {
        if (this.sourceMetadata == null) {
            this.sourceMetadata = new ArrayList<>();
        }

        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.sourceMetadata.add(sourceMetadata);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addScope(MD_Scope scope) {
        if (this.scope == null) {
            this.scope = new ArrayList<>();
        }

        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.scope.add(scope);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSourceStep(LI_ProcessStep sourceStep) {
        if (this.sourceStep == null) {
            this.sourceStep = new ArrayList<>();
        }

        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.sourceStep.add(sourceStep);
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
