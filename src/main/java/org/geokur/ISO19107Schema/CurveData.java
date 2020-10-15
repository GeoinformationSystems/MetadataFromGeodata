/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19107Schema;

import org.geokur.ISO19103Schema.DirectPosition;
import org.geokur.ISO191xxProfile.MaximumOccurrenceException;
import org.geokur.ISO191xxProfile.ObligationException;
import org.geokur.ISO191xxProfile.ProfileException;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "CurveData")
public class CurveData extends OrientableData {

    // occurrence and obligation
    private final String[] elementName = {"rsid", "type", "segment", "orientation", "dataPoint", "knot", "segment"};
    private final int[] elementMax = {Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElement(name = "dataPoint")
    public List<DirectPosition> dataPoint;

    @XmlElementWrapper(name = "knot")
    @XmlElementRef
    public List<Knot> knot;

    @XmlElementWrapper(name = "segment")
    @XmlElementRef
    public List<CurveData> segment;

    // variables for correct marshalling of specified classes
    public List<LineData> lineData;

    public List<PolygonData> polygonData;

    // methods
    public CurveData() {}

    public void createDataPoint() {
        if (this.dataPoint == null) {
            this.dataPoint = new ArrayList<>();
        }
    }

    public void createKnot() {
        if (this.knot == null) {
            this.knot = new ArrayList<>();
        }
    }

    public void createSegment() {
        if (this.segment == null) {
            this.segment = new ArrayList<>();
        }
    }

    public void addDataPoint(DirectPosition dataPoint) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.dataPoint.add(dataPoint);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addKnot(Knot knot) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.knot.add(knot);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSegment(CurveData segment) {
        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.segment.add(segment);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void finalizeClass() {
        for (int i = 0; i < elementName.length; i++) {
            try {
                List<?> tempList = (List<?>) this.getClass().getField(elementName[i]).get(this);
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
