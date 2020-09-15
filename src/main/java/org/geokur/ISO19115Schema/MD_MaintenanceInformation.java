/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

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

@XmlRootElement(name = "MD_MaintenanceInformation", namespace = "http://standards.iso.org/iso/19115/-3/mmi/1.0")
public class MD_MaintenanceInformation {

    // occurrence and obligation
    private final String[] elementName = {"maintenanceAndUpdateFrequency", "maintenanceDate", "userDefinedMaintenanceFrequency", "maintenanceScope", "maintenanceNote", "contact"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "maintenanceAndUpdateFrequency", namespace = "http://standards.iso.org/iso/19115/-3/mmi/1.0")
    @XmlElementRef
    public List<MD_MaintenanceFrequencyCode> maintenanceAndUpdateFrequency;

    @XmlElementWrapper(name = "maintenanceDate", namespace = "http://standards.iso.org/iso/19115/-3/mmi/1.0")
    @XmlElementRef
    public List<CI_Date> maintenanceDate;

    @XmlElement(name = "userDefinedMaintenanceFrequency", namespace = "http://standards.iso.org/iso/19115/-3/mmi/1.0")
    public List<String> userDefinedMaintenanceFrequency;

    @XmlElementWrapper(name = "maintenanceScope", namespace = "http://standards.iso.org/iso/19115/-3/mmi/1.0")
    @XmlElementRef
    public List<MD_Scope> maintenanceScope;

    @XmlElement(name = "maintenanceNote", namespace = "http://standards.iso.org/iso/19115/-3/mmi/1.0")
    public List<String> maintenanceNote;

    @XmlElementWrapper(name = "contact", namespace = "http://standards.iso.org/iso/19115/-3/mmi/1.0")
    @XmlElementRef
    public List<CI_Responsibility> contact;

    // methods
    public MD_MaintenanceInformation(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_MaintenanceInformation);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_MaintenanceInformation);
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

    public void createMaintenanceAndUpdateFrequency() {
        if (this.maintenanceAndUpdateFrequency == null) {
            this.maintenanceAndUpdateFrequency = new ArrayList<>();
        }
    }

    public void createMaintenanceDate() {
        if (this.maintenanceDate == null) {
            this.maintenanceDate = new ArrayList<>();
        }
    }

    public void createUserDefinedMaintenanceFrequency() {
        if (this.userDefinedMaintenanceFrequency == null) {
            this.userDefinedMaintenanceFrequency = new ArrayList<>();
        }
    }

    public void createMaintenanceScope() {
        if (this.maintenanceScope == null) {
            this.maintenanceScope = new ArrayList<>();
        }
    }

    public void createMaintenanceNote() {
        if (this.maintenanceNote == null) {
            this.maintenanceNote = new ArrayList<>();
        }
    }

    public void createContact() {
        if (this.contact == null) {
            this.contact = new ArrayList<>();
        }
    }

    public void addMaintenanceAndUpdateFrequency(MD_MaintenanceFrequencyCode maintenanceAndUpdateFrequency) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.maintenanceAndUpdateFrequency.add(maintenanceAndUpdateFrequency);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMaintenanceDate(CI_Date maintenanceDate) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.maintenanceDate.add(maintenanceDate);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addUserDefinedMaintenanceFrequency(String userDefinedMaintenanceFrequency) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.userDefinedMaintenanceFrequency.add(userDefinedMaintenanceFrequency);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMaintenanceScope(MD_Scope maintenanceScope) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.maintenanceScope.add(maintenanceScope);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMaintenanceNote(String maintenanceNote) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.maintenanceNote.add(maintenanceNote);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addContact(CI_Responsibility contact) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.contact.add(contact);
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
