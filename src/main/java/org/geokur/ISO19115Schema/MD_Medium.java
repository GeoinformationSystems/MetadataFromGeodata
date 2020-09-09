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

@XmlRootElement(name = "MD_Medium", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
public class MD_Medium {

    // occurrence and obligation
    private final String[] elementName = {"name", "density", "densityUnits", "volumes", "mediumFormat", "mediumNote", "identifier"};
    private final int[] elementMax = {1, 1, 1, 1, Integer.MAX_VALUE, 1, 1};
    private final boolean[] elementObligation = {false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "name", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    @XmlElementRef
    public List<CI_Citation> name;

    @XmlElement(name = "density", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    public List<String> density;

    @XmlElement(name = "densityUnits", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    public List<String> densityUnits;

    @XmlElement(name = "volumes", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    public List<String> volumes;

    @XmlElementWrapper(name = "mediumFormat", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    @XmlElementRef
    public List<MD_MediumFormatCode> mediumFormat;

    @XmlElement(name = "mediumNote", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    public List<String> mediumNote;

    @XmlElementWrapper(name = "identifier", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
    @XmlElementRef
    public List<MD_Identifier> identifier;

    // methods
    public MD_Medium(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_Medium);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_Medium);
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

    public void createName() {
        if (this.name == null) {
            this.name = new ArrayList<>();
        }
    }

    public void createDensity() {
        if (this.density == null) {
            this.density = new ArrayList<>();
        }
    }

    public void createDensityUnits() {
        if (this.densityUnits == null) {
            this.densityUnits = new ArrayList<>();
        }
    }

    public void createVolumes() {
        if (this.volumes == null) {
            this.volumes = new ArrayList<>();
        }
    }

    public void createMediumFormat() {
        if (this.mediumFormat == null) {
            this.mediumFormat = new ArrayList<>();
        }
    }

    public void createMediumNote() {
        if (this.mediumNote == null) {
            this.mediumNote = new ArrayList<>();
        }
    }

    public void createIdentifier() {
        if (this.identifier == null) {
            this.identifier = new ArrayList<>();
        }
    }

    public void addName(CI_Citation name) {
        int elementNum = 0;
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

    public void addDensity(String density) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.density.add(density);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDensityUnits(String densityUnits) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.densityUnits.add(densityUnits);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addVolumes(String volumes) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.volumes.add(volumes);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMediumFormat(MD_MediumFormatCode mediumFormat) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.mediumFormat.add(mediumFormat);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMediumNote(String mediumNote) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.mediumNote.add(mediumNote);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addIdentifier(MD_Identifier identifier) {
        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.identifier.add(identifier);
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
