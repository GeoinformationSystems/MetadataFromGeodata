/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19111Schema;

import org.geokur.ISO19157Schema.DQ_PositionalAccuracy;
import org.geokur.ISO191xxProfile.MaximumOccurrenceException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "CoordinateOperation")
public abstract class CoordinateOperation extends ObjectUsage {

    // occurrence and obligation
    private final String[] elementName = {"name", "identifier", "alias", "remarks",
            "usage",
            "sourceCRS", "targetCRS", "interpolationCRS", "sourceCoordinateEpoch", "targetCoordinateEpoch", "operationVersion", "coordinateOperationAccuracy"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1,
            Integer.MAX_VALUE,
            1, 1, 1, 1, 1, 1, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, false, false, false,
            false,
            false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElementWrapper(name = "sourceCRS")
    @XmlElementRef
    public List<CRS> sourceCRS;

    @XmlElementWrapper(name = "targetCRS")
    @XmlElementRef
    public List<CRS> targetCRS;

    @XmlElementWrapper(name = "interpolationCRS")
    @XmlElementRef
    public List<CRS> interpolationCRS;

    @XmlElementWrapper(name = "sourceCoordinateEpoch")
    @XmlElementRef
    public List<DataEpoch> sourceCoordinateEpoch;

    @XmlElementWrapper(name = "targetCoordinateEpoch")
    @XmlElementRef
    public List<DataEpoch> targetCoordinateEpoch;

    @XmlElement(name = "operationVersion")
    public List<String> operationVersion;

    @XmlElementWrapper(name = "coordinateOperationAccuracy")
    @XmlElementRef
    public List<DQ_PositionalAccuracy> coordinateOperationAccuracy;

    // variables for correct marshalling of specified classes
    public List<SingleOperation> singleOperation;

    // methods
    public void addSourceCRS(CRS sourceCRS) {
        if (this.sourceCRS == null) {
            this.sourceCRS = new ArrayList<>();
        }

        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.sourceCRS.add(sourceCRS);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTargetCRS(CRS targetCRS) {
        if (this.targetCRS == null) {
            this.targetCRS = new ArrayList<>();
        }

        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.targetCRS.add(targetCRS);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addInterpolationCRS(CRS interpolationCRS) {
        if (this.interpolationCRS == null) {
            this.interpolationCRS = new ArrayList<>();
        }

        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.interpolationCRS.add(interpolationCRS);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSourceCoordinateEpoch(DataEpoch sourceCoordinateEpoch) {
        if (this.sourceCoordinateEpoch == null) {
            this.sourceCoordinateEpoch = new ArrayList<>();
        }

        int elementNum = 8;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.sourceCoordinateEpoch.add(sourceCoordinateEpoch);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTargetCoordinateEpoch(DataEpoch targetCoordinateEpoch) {
        if (this.targetCoordinateEpoch == null) {
            this.targetCoordinateEpoch = new ArrayList<>();
        }

        int elementNum = 9;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.targetCoordinateEpoch.add(targetCoordinateEpoch);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOperationVersion(String operationVersion) {
        if (this.operationVersion == null) {
            this.operationVersion = new ArrayList<>();
        }

        int elementNum = 10;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.operationVersion.add(operationVersion);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCoordinateOperationAccuracy(DQ_PositionalAccuracy coordinateOperationAccuracy) {
        if (this.coordinateOperationAccuracy == null) {
            this.coordinateOperationAccuracy = new ArrayList<>();
        }

        int elementNum = 11;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.coordinateOperationAccuracy.add(coordinateOperationAccuracy);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
