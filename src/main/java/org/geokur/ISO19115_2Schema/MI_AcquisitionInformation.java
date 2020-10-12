/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115_2Schema;

import org.geokur.ISO19115Schema.MD_Scope;
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

@XmlRootElement(name = "MI_AcquisitionInformation", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
public class MI_AcquisitionInformation {

    // occurrence and obligation
    private final String[] elementName = {"scope", "acquisitionPlan", "acquisitionRequirement", "environmentalConditions", "instrument", "objective", "operation", "platform"};
    private final int[] elementMax = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {false, false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "scope", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MD_Scope> scope;

    @XmlElementWrapper(name = "acquisitionPlan", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MI_Plan> acquisitionPlan;

    @XmlElementWrapper(name = "acquisitionRequirement", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MI_Requirement> acquisitionRequirement;

    @XmlElementWrapper(name = "environmentalConditions", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MI_EnvironmentalRecord> environmentalConditions;

    @XmlElementWrapper(name = "instrument", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MI_Instrument> instrument;

    @XmlElementWrapper(name = "objective", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MI_Objective> objective;

    @XmlElementWrapper(name = "operation", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MI_Operation> operation;

    @XmlElementWrapper(name = "platform", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MI_Platform> platform;

    // methods
    public MI_AcquisitionInformation(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MI_AcquisitionInformation);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MI_AcquisitionInformation);
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

    public void createScope() {
        if (this.scope == null) {
            this.scope = new ArrayList<>();
        }
    }

    public void createAcquisitionPlan() {
        if (this.acquisitionPlan == null) {
            this.acquisitionPlan = new ArrayList<>();
        }
    }

    public void createAcquisitionRequirement() {
        if (this.acquisitionRequirement == null) {
            this.acquisitionRequirement = new ArrayList<>();
        }
    }

    public void createEnvironmentalConditions() {
        if (this.environmentalConditions == null) {
            this.environmentalConditions = new ArrayList<>();
        }
    }

    public void createInstrument() {
        if (this.instrument == null) {
            this.instrument = new ArrayList<>();
        }
    }

    public void createObjective() {
        if (this.objective == null) {
            this.objective = new ArrayList<>();
        }
    }

    public void createOperation() {
        if (this.operation == null) {
            this.operation = new ArrayList<>();
        }
    }

    public void createPlatform() {
        if (this.platform == null) {
            this.platform = new ArrayList<>();
        }
    }

    public void addScope(MD_Scope scope) {
        int elementNum = 0;
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

    public void addAcquisitionPlan(MI_Plan acquisitionPlan) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.acquisitionPlan.add(acquisitionPlan);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAcquisitionRequirement(MI_Requirement acquisitionRequirement) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.acquisitionRequirement.add(acquisitionRequirement);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addEnvironmentalConditions(MI_EnvironmentalRecord environmentalConditions) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.environmentalConditions.add(environmentalConditions);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addInstrument(MI_Instrument instrument) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.instrument.add(instrument);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addObjective(MI_Objective objective) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.objective.add(objective);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOperation(MI_Operation operation) {
        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.operation.add(operation);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPlatform(MI_Platform platform) {
        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.platform.add(platform);
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
