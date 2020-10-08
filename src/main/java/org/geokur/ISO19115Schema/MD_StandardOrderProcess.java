/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO19103Schema.Record;
import org.geokur.ISO19103Schema.RecordType;
import org.geokur.ISO191xxProfile.MaximumOccurrenceException;
import org.geokur.ISO191xxProfile.ObligationException;
import org.geokur.ISO191xxProfile.ProfileException;
import org.geokur.ISO191xxProfile.ProfileReader;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "MD_StandardOrderProcess", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
public class MD_StandardOrderProcess {

    // occurrence and obligation
    private final String[] elementName = {"fees", "plannedAvailablDateTime", "orderingInstructions", "turnaround", "orderOptionsType", "orderOptions"};
    private final int[] elementMax = {1, 1, 1, 1, 1, 1};
    private final boolean[] elementObligation = {false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "fees", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    public List<String> fees;

    @XmlElement(name = "plannedAvailablDateTime", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    public List<String> plannedAvailablDateTime;

    @XmlElement(name = "orderingInstructions", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    public List<String> orderingInstructions;

    @XmlElement(name = "turnaround", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    public List<String> turnaround;

    @XmlElement(name = "orderOptionsType", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    public List<RecordType> orderOptionsType;

    @XmlElement(name = "orderOptions", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    public List<Record> orderOptions;

    // methods
    public MD_StandardOrderProcess(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_StandardOrderProcess);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_StandardOrderProcess);
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

    public void createFees() {
        if (this.fees == null) {
            this.fees = new ArrayList<>();
        }
    }

    public void createPlannedAvailablDateTime() {
        if (this.plannedAvailablDateTime == null) {
            this.plannedAvailablDateTime = new ArrayList<>();
        }
    }

    public void createOrderingInstructions() {
        if (this.orderingInstructions == null) {
            this.orderingInstructions = new ArrayList<>();
        }
    }

    public void createTurnaround() {
        if (this.turnaround == null) {
            this.turnaround = new ArrayList<>();
        }
    }

    public void createOrderOptionsType() {
        if (this.orderOptionsType == null) {
            this.orderOptionsType = new ArrayList<>();
        }
    }

    public void createOrderOptions() {
        if (this.orderOptions == null) {
            this.orderOptions = new ArrayList<>();
        }
    }

    public void addFees(String fees) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.fees.add(fees);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPlannedAvailablDateTime(String plannedAvailablDateTime) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.plannedAvailablDateTime.add(plannedAvailablDateTime);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOrderingInstructions(String orderingInstructions) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.orderingInstructions.add(orderingInstructions);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTurnaround(String turnaround) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.turnaround.add(turnaround);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOrderOptionsType(RecordType orderOptionsType) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.orderOptionsType.add(orderOptionsType);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOrderOptions(Record orderOptions) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.orderOptions.add(orderOptions);
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
