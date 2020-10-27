/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19107Schema;

import org.geokur.ISO191xxProfile.MaximumOccurrenceException;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "SurfaceData")
public abstract class SurfaceData extends OrientableData {

    // occurrence and obligation
    private final String[] elementName = {"rsid", "type", "segment", "orientation", "boundary", "spanningSurface", "interpolation"};
    private final int[] elementMax = {Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, 1};
    private final boolean[] elementObligation = {false, false, false, false, true, false, true};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElementWrapper(name = "boundary")
    @XmlElementRef
    public List<CurveData> boundary;

    @XmlElementWrapper(name = "spanningSurface")
    @XmlElementRef
    public List<SurfaceData> spanningSurface;

    @XmlElementWrapper(name = "interpolation")
    @XmlElementRef
    public List<SurfaceInterpolation> interpolation;

    // variables for correct marshalling of specified classes
    public List<PolygonData> polygonData;

    // methods
    public SurfaceData() {}

    public void addBoundary(CurveData boundary) {
        if (this.boundary == null) {
            this.boundary = new ArrayList<>();
        }

        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.boundary.add(boundary);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSpanningSurface(SurfaceData spanningSurface) {
        if (this.spanningSurface == null) {
            this.spanningSurface = new ArrayList<>();
        }

        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.spanningSurface.add(spanningSurface);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addInterpolation(SurfaceInterpolation interpolation) {
        if (this.interpolation == null) {
            this.interpolation = new ArrayList<>();
        }

        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.interpolation.add(interpolation);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
