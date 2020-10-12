/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115_2Schema;

import org.geokur.ISO19115Schema.CI_Citation;
import org.geokur.ISO19115Schema.MD_Constraints;
import org.geokur.ISO19115Schema.PT_Locale;
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

@XmlRootElement(name = "MI_InstrumentEventList", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
public class MI_InstrumentEventList {

    // occurrence and obligation
    private final String[] elementName = {"citation", "description", "locale", "constraints", "instrumentationEvent"};
    private final int[] elementMax = {Integer.MAX_VALUE, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {false, true, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "citation", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<CI_Citation> citation;

    @XmlElement(name = "description", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    public List<String> description;

    @XmlElementWrapper(name = "locale", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<PT_Locale> locale;

    @XmlElementWrapper(name = "constraints", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MD_Constraints> constraints;

    @XmlElementWrapper(name = "instrumentationEvent", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MI_InstrumentEvent> instrumentationEvent;

    // methods
    public MI_InstrumentEventList(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MI_InstrumentEventList);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MI_InstrumentEventList);
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

    public void createCitation() {
        if (this.citation == null) {
            this.citation = new ArrayList<>();
        }
    }

    public void createDescription() {
        if (this.description == null) {
            this.description = new ArrayList<>();
        }
    }

    public void createLocale() {
        if (this.locale == null) {
            this.locale = new ArrayList<>();
        }
    }

    public void createConstraints() {
        if (this.constraints == null) {
            this.constraints = new ArrayList<>();
        }
    }

    public void createInstrumentationEvent() {
        if (this.instrumentationEvent == null) {
            this.instrumentationEvent = new ArrayList<>();
        }
    }

    public void addCitation(CI_Citation citation) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.citation.add(citation);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDescription(String description) {
        int elementNum = 1;
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

    public void addLocale(PT_Locale locale) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.locale.add(locale);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addConstraints(MD_Constraints constraints) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.constraints.add(constraints);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addInstrumentationEvent(MI_InstrumentEvent instrumentationEvent) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.instrumentationEvent.add(instrumentationEvent);
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
