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

@XmlRootElement(name = "MD_LegalConstraints", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
public class MD_LegalConstraints {

    // occurrence and obligation
    private final String[] elementName = {"useLimitation", "constraintApplicationScope", "graphic", "reference", "releasability", "responsibleParty", "accessConstraints", "useConstraints", "otherConstraints"};
    private final int[] elementMax = {Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {false, false, false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "useLimitation", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
    public List<String> useLimitation;

    @XmlElementWrapper(name = "constraintApplicationScope", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
    @XmlElementRef
    public List<MD_Scope> constraintApplicationScope;

    @XmlElementWrapper(name = "graphic", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
    @XmlElementRef
    public List<MD_BrowseGraphic> graphic;

    @XmlElementWrapper(name = "reference", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
    @XmlElementRef
    public List<CI_Citation> reference;

    @XmlElementWrapper(name = "releasability", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
    @XmlElementRef
    public List<MD_Releasability> releasability;

    @XmlElementWrapper(name = "responsibleParty", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
    @XmlElementRef
    public List<CI_Responsibility> responsibleParty;

    @XmlElementWrapper(name = "accessConstraints", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
    @XmlElementRef
    public List<MD_RestrictionCode> accessConstraints;

    @XmlElementWrapper(name = "useConstraints", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
    @XmlElementRef
    public List<MD_RestrictionCode> useConstraints;

    @XmlElement(name = "otherConstraints", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
    public List<String> otherConstraints;

    // methods
    public MD_LegalConstraints(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_LegalConstraints);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_LegalConstraints);
                if (!tempList.contains(elementName[i])) {
                    // element not mandatory
                    elementObligation[i] = false;
                }
            }
        }
    }

    public void createUseLimitation() {
        if (this.useLimitation == null) {
            this.useLimitation = new ArrayList<>();
        }
    }

    public void createConstraintApplicationScope() {
        if (this.constraintApplicationScope == null) {
            this.constraintApplicationScope = new ArrayList<>();
        }
    }

    public void createGraphic() {
        if (this.graphic == null) {
            this.graphic = new ArrayList<>();
        }
    }

    public void createReference() {
        if (this.reference == null) {
            this.reference = new ArrayList<>();
        }
    }

    public void createReleasability() {
        if (this.releasability == null) {
            this.releasability = new ArrayList<>();
        }
    }

    public void createResponsibleParty() {
        if (this.responsibleParty == null) {
            this.responsibleParty = new ArrayList<>();
        }
    }

    public void createAccessConstraints() {
        if (this.accessConstraints == null) {
            this.accessConstraints = new ArrayList<>();
        }
    }

    public void createUseConstraints() {
        if (this.useConstraints == null) {
            this.useConstraints = new ArrayList<>();
        }
    }

    public void createOtherConstraints() {
        if (this.otherConstraints == null) {
            this.otherConstraints = new ArrayList<>();
        }
    }

    public void addUseLimitation(String useLimitation) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.useLimitation.add(useLimitation);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addConstraintApplicationScope(MD_Scope constraintApplicationScope) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.constraintApplicationScope.add(constraintApplicationScope);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addGraphic(MD_BrowseGraphic graphic) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.graphic.add(graphic);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addReference(CI_Citation reference) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.reference.add(reference);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addReleasability(MD_Releasability releasability) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.releasability.add(releasability);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addResponsibleParty(CI_Responsibility responsibleParty) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.responsibleParty.add(responsibleParty);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAccessConstraints(MD_RestrictionCode accessConstraints) {
        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.accessConstraints.add(accessConstraints);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addUseConstraints(MD_RestrictionCode useConstraints) {
        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.useConstraints.add(useConstraints);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOtherConstraints(String otherConstraints) {
        int elementNum = 8;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.otherConstraints.add(otherConstraints);
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
