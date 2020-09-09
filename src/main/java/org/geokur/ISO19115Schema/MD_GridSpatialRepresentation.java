/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

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

@XmlRootElement(name = "MD_GridSpatialRepresentation", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
public class MD_GridSpatialRepresentation extends MD_SpatialRepresentation {

    // occurrence and obligation
    private final String[] elementName = {"numberOfDimensions", "axisDimensionProperties", "cellGeometry", "transformationParameterAvailability"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, 1, 1};
    private final boolean[] elementObligation = {true, true, true, true};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "numberOfDimensions", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    public List<String> numberOfDimensions;

    @XmlElementWrapper(name = "axisDimensionProperties", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    @XmlElementRef
    public List<MD_Dimension> axisDimensionProperties;

    @XmlElementWrapper(name = "cellGeometry", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    @XmlElementRef
    public List<MD_CellGeometryCode> cellGeometry;

    @XmlElement(name = "transformationParameterAvailability", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    public List<String> transformationParameterAvailability;

    // variables for correct marshalling of specified classes
    public List<MD_Georectified> georectified;

    public List<MD_Georeferenceable> georeferenceable;

    // methods
    public MD_GridSpatialRepresentation(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_GridSpatialRepresentation);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_GridSpatialRepresentation);
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

    public void createNumberOfDimensions() {
        if (this.numberOfDimensions == null) {
            this.numberOfDimensions = new ArrayList<>();
        }
    }

    public void createAxisDimensionProperties() {
        if (this.axisDimensionProperties == null) {
            this.axisDimensionProperties = new ArrayList<>();
        }
    }

    public void createCellGeometry() {
        if (this.cellGeometry == null) {
            this.cellGeometry = new ArrayList<>();
        }
    }

    public void createTransformationParameterAvailability() {
        if (this.transformationParameterAvailability == null) {
            this.transformationParameterAvailability = new ArrayList<>();
        }
    }

    public void addNumberOfDimensions(String numberOfDimensions) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.numberOfDimensions.add(numberOfDimensions);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAxisDimensionProperties(MD_Dimension axisDimensionProperties) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.axisDimensionProperties.add(axisDimensionProperties);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCellGeometry(MD_CellGeometryCode cellGeometry) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.cellGeometry.add(cellGeometry);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTransformationParameterAvailability(String transformationParameterAvailability) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.transformationParameterAvailability.add(transformationParameterAvailability);
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
