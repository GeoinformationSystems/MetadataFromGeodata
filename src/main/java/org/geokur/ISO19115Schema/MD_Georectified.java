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

@XmlRootElement(name = "MD_Georectified", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
public class MD_Georectified {

    // occurrence and obligation
    private final String[] elementName = {"numberOfDimensions", "axisDimensionProperties", "cellGeometry", "transformationParameterAvailability", "checkPointAvailability", "checkPointDescription", "cornerPoints", "centrePoint", "pointInPixel", "transformationDimensionDescription", "transformationDimensionMapping"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, 1, 1, 1, 1, Integer.MAX_VALUE, 1, 1, 1, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, true, true, true, true, false, false, false, true, false, false};

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

    @XmlElement(name = "checkPointAvailability", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    public List<String> checkPointAvailability;

    @XmlElement(name = "checkPointDescription", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    public List<String> checkPointDescription;

    @XmlElement(name = "cornerPoints", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    public List<String> cornerPoints;

    @XmlElement(name = "centrePoint", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    public List<String> centrePoint;

    @XmlElementWrapper(name = "pointInPixel", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    @XmlElementRef
    public List<enumeration_MD_PixelOrientationCode> pointInPixel;

    @XmlElement(name = "transformationDimensionDescription", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    public List<String> transformationDimensionDescription;

    @XmlElement(name = "transformationDimensionMapping", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
    public List<String> transformationDimensionMapping;

    // methods
    public MD_Georectified(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_Georectified);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_Georectified);
                if (!tempList.contains(elementName[i])) {
                    // element not mandatory
                    elementObligation[i] = false;
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

    public void createCheckPointAvailability() {
        if (this.checkPointAvailability == null) {
            this.checkPointAvailability = new ArrayList<>();
        }
    }

    public void createCheckPointDescription() {
        if (this.checkPointDescription == null) {
            this.checkPointDescription = new ArrayList<>();
        }
    }

    public void createCornerPoints() {
        if (this.cornerPoints == null) {
            this.cornerPoints = new ArrayList<>();
        }
    }

    public void createCentrePoint() {
        if (this.centrePoint == null) {
            this.centrePoint = new ArrayList<>();
        }
    }

    public void createPointInPixel() {
        if (this.pointInPixel == null) {
            this.pointInPixel = new ArrayList<>();
        }
    }

    public void createTransformationDimensionDescription() {
        if (this.transformationDimensionDescription == null) {
            this.transformationDimensionDescription = new ArrayList<>();
        }
    }

    public void createTransformationDimensionMapping() {
        if (this.transformationDimensionMapping == null) {
            this.transformationDimensionMapping = new ArrayList<>();
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

    public void addCheckPointAvailability(String checkPointAvailability) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.checkPointAvailability.add(checkPointAvailability);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCheckPointDescription(String checkPointDescription) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.checkPointDescription.add(checkPointDescription);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCornerPoints(String cornerPoints) {
        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.cornerPoints.add(cornerPoints);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCentrePoint(String centrePoint) {
        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.centrePoint.add(centrePoint);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPointInPixel(enumeration_MD_PixelOrientationCode pointInPixel) {
        int elementNum = 8;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.pointInPixel.add(pointInPixel);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTransformationDimensionDescription(String transformationDimensionDescription) {
        int elementNum = 9;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.transformationDimensionDescription.add(transformationDimensionDescription);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTransformationDimensionMapping(String transformationDimensionMapping) {
        int elementNum = 10;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.transformationDimensionMapping.add(transformationDimensionMapping);
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
