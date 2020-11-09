/*
 * Copyright 2020
 * @author: Michael Wagner
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

@XmlRootElement(name = "MD_KeywordClass", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
public class MD_KeywordClass {

    // occurrence and obligation
    private final String[] elementName = {"classNameElement", "conceptIdentifier", "ontology"};
    private final String[] elementNameProfile = {"className", "conceptIdentifier", "ontology"}; // true profile name of elements
    private final int[] elementMax = {1, 1, 1};
    private final boolean[] elementObligation = {true, false, true};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "className", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> classNameElement;

    @XmlElement(name = "conceptIdentifier", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> conceptIdentifier;

    @XmlElementWrapper(name = "ontology", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<CI_Citation> ontology;

    // methods
    public MD_KeywordClass(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_KeywordClass);
                if (!tempList.contains(elementNameProfile[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_KeywordClass);
                if (!tempList.contains(elementNameProfile[i])) {
                    // element not mandatory
                    elementObligation[i] = false;
                } else if (tempList.contains(elementName[i])) {
                    // element mandatory
                    elementObligation[i] = true;
                }
            }
        }
    }

    public void addClassName(String classNameElement) {
        if (this.classNameElement == null) {
            this.classNameElement = new ArrayList<>();
        }

        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.classNameElement.add(classNameElement);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addConceptIdentifier(String conceptIdentifier) {
        if (this.conceptIdentifier == null) {
            this.conceptIdentifier = new ArrayList<>();
        }

        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.conceptIdentifier.add(conceptIdentifier);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOntology(CI_Citation ontology) {
        if (this.ontology == null) {
            this.ontology = new ArrayList<>();
        }

        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.ontology.add(ontology);
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
                    throw new ProfileException(className + " - " + elementNameProfile[i]);
                }
                if (elementObligation[i] && (tempList == null || tempList.isEmpty())) {
                    // test filling and obligation of all variable lists
                    throw new ObligationException(className + " - " + elementNameProfile[i]);
                }
            } catch (ProfileException | ObligationException | NoSuchFieldException | IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
