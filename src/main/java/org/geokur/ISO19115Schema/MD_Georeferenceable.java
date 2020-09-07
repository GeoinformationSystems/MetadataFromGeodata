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

@XmlRootElement(name = "MD_Georeferenceable", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
public class MD_Georeferenceable extends MD_GridSpatialRepresentation {

    // occurrence and obligation
    private final String[] elementName = {"numberOfDimensions", "axisDimensionProperties", "cellGeometry", "transformationParameterAvailability", "controlPointAvailability", "orientationParameterAvailability", "orientationParameterDescription", "georeferencesParameters", "parameterCitation"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, 1, 1, 1, 1, 1, 1, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, true, true, true, true, true, false, true, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "controlPointAvailability", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    public List<String> controlPointAvailability;

    @XmlElement(name = "orientationParameterAvailability", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    public List<String> orientationParameterAvailability;

    @XmlElement(name = "orientationParameterDescription", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    public List<String> orientationParameterDescription;

    @XmlElement(name = "georeferencedParameters", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    public List<String> georeferencedParameters;

    @XmlElementWrapper(name = "parameterCitation", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    @XmlElementRef
    public List<CI_Citation> parameterCitation;

    // methods
    public MD_Georeferenceable(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_Georeferenceable);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_Georeferenceable);
                if (!tempList.contains(elementName[i])) {
                    // element not mandatory
                    elementObligation[i] = false;
                }
            }
        }
    }

    public void createControlPointAvailability() {
        if (this.controlPointAvailability == null) {
            this.controlPointAvailability = new ArrayList<>();
        }
    }

    public void createOrientationParameterAvailability() {
        if (this.orientationParameterAvailability == null) {
            this.orientationParameterAvailability = new ArrayList<>();
        }
    }

    public void createOrientationParameterDescription() {
        if (this.orientationParameterDescription == null) {
            this.orientationParameterDescription = new ArrayList<>();
        }
    }

    public void createGeoreferencedParameters() {
        if (this.georeferencedParameters == null) {
            this.georeferencedParameters = new ArrayList<>();
        }
    }

    public void createParameterCitation() {
        if (this.parameterCitation == null) {
            this.parameterCitation = new ArrayList<>();
        }
    }

    public void addControlPointAvailability(String controlPointAvailability) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.controlPointAvailability.add(controlPointAvailability);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOrientationParameterAvailability(String orientationParameterAvailability) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.orientationParameterAvailability.add(orientationParameterAvailability);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOrientationParameterDescription(String orientationParameterDescription) {
        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.orientationParameterDescription.add(orientationParameterDescription);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addGeoreferencedParameters(String georeferencedParameters) {
        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.georeferencedParameters.add(georeferencedParameters);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addParameterCitation(CI_Citation parameterCitation) {
        int elementNum = 8;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.parameterCitation.add(parameterCitation);
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
