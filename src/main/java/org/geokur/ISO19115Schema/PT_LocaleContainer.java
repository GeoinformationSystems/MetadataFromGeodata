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

@XmlRootElement(name = "PT_LocaleContainer", namespace = "http://standards.iso.org/iso/19115/-3/lan/1.0")
public class PT_LocaleContainer {

    // occurrence and obligation
    private final String[] elementName = {"description", "locale", "date", "responsibleParty", "localisedString"};
    private final int[] elementMax = {1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, true, true, true, true};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "description", namespace = "http://standards.iso.org/iso/19115/-3/lan/1.0")
    public List<String> description;

    @XmlElementWrapper(name = "locale", namespace = "http://standards.iso.org/iso/19115/-3/lan/1.0")
    @XmlElementRef
    public List<PT_Locale> locale;

    @XmlElementWrapper(name = "date", namespace = "http://standards.iso.org/iso/19115/-3/lan/1.0")
    @XmlElementRef
    public List<CI_Date> date;

    @XmlElementWrapper(name = "responsibleParty", namespace = "http://standards.iso.org/iso/19115/-3/lan/1.0")
    @XmlElementRef
    public List<CI_Responsibility> responsibleParty;

    @XmlElementWrapper(name = "localisedString", namespace = "http://standards.iso.org/iso/19115/-3/lan/1.0")
    @XmlElementRef
    public List<LocalisedCharacterString> localisedString;

    // methods
    public PT_LocaleContainer(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.PT_LocaleContainer);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.PT_LocaleContainer);
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

    public void createDate() {
        if (this.date == null) {
            this.date = new ArrayList<>();
        }
    }

    public void createResponsibleParty() {
        if (this.responsibleParty == null) {
            this.responsibleParty = new ArrayList<>();
        }
    }

    public void createLocalisedString() {
        if (this.localisedString == null) {
            this.localisedString = new ArrayList<>();
        }
    }

    public void addDescription(String description) {
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

    public void addLocale(PT_Locale locale) {
        int elementNum = 1;
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

    public void addDate(CI_Date date) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.date.add(date);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addResponsibleParty(CI_Responsibility responsibleParty) {
        int elementNum = 3;
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

    public void addLocalisedString(LocalisedCharacterString localisedString) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.localisedString.add(localisedString);
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
