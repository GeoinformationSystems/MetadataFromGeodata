/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO19103Schema.Coordinate;
import org.geokur.ISO19115_2Schema.MI_Georectified;
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

@XmlRootElement(name = "MD_Georectified", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
public class MD_Georectified extends MD_GridSpatialRepresentation {

    // occurrence and obligation
    private final String[] elementName = {"numberOfDimensions", "axisDimensionProperties", "cellGeometry", "transformationParameterAvailability", "checkPointAvailability", "checkPointDescription", "cornerPoints", "centrePoint", "pointInPixel", "transformationDimensionDescription", "transformationDimensionMapping"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, 1, 1, 1, 1, 4, 1, 1, 1, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, true, true, true, true, false, false, false, true, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "checkPointAvailability", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    public List<Boolean> checkPointAvailability;

    @XmlElement(name = "checkPointDescription", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    public List<String> checkPointDescription;

    @XmlElement(name = "cornerPoints", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    public List<Coordinate> cornerPoints;

    @XmlElement(name = "centrePoint", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    public List<Coordinate> centrePoint;

    @XmlElementWrapper(name = "pointInPixel", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    @XmlElementRef
    public List<MD_PixelOrientationCode> pointInPixel;

    @XmlElement(name = "transformationDimensionDescription", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    public List<String> transformationDimensionDescription;

    @XmlElement(name = "transformationDimensionMapping", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    public List<String> transformationDimensionMapping;

    // variables for correct marshalling of specified classes
    public List<MI_Georectified> georectified;

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
                } else if (tempList.contains(elementName[i])) {
                    // element mandatory
                    elementObligation[i] = true;
                }
            }
        }
    }

    public void addCheckPointAvailability(Boolean checkPointAvailability) {
        if (this.checkPointAvailability == null) {
            this.checkPointAvailability = new ArrayList<>();
        }

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
        if (this.checkPointDescription == null) {
            this.checkPointDescription = new ArrayList<>();
        }

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

    public void addCornerPoints(Coordinate cornerPoints) {
        if (this.cornerPoints == null) {
            this.cornerPoints = new ArrayList<>();
        }

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

    public void addCentrePoint(Coordinate centrePoint) {
        if (this.centrePoint == null) {
            this.centrePoint = new ArrayList<>();
        }

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

    public void addPointInPixel(MD_PixelOrientationCode pointInPixel) {
        if (this.pointInPixel == null) {
            this.pointInPixel = new ArrayList<>();
        }

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
        if (this.transformationDimensionDescription == null) {
            this.transformationDimensionDescription = new ArrayList<>();
        }

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
        if (this.transformationDimensionMapping == null) {
            this.transformationDimensionMapping = new ArrayList<>();
        }

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
