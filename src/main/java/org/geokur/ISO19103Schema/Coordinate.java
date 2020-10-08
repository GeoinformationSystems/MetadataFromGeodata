/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19103Schema;

import org.geokur.ISO191xxProfile.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Coordinate")
public class Coordinate {

    // occurrence and obligation
    private final String[] elementName = {"x", "y", "z", "t"};
    private final int[] elementMax = {1, 1, 1, 1};
    private final boolean[] elementObligation = {true, false, false, false};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElement(name = "x")
    public List<Double> x;

    @XmlElement(name = "y")
    public List<Double> y;

    @XmlElement(name = "z")
    public List<Double> z;

    @XmlElement(name = "t")
    public List<Double> t;

    private Integer dimension;

    // methods
    public Coordinate() {
        this.dimension = 0;
    }

    public void createX() {
        if (this.x == null) {
            this.x = new ArrayList<>();
            this.dimension++;
        }
    }

    public void createY() {
        if (this.y == null) {
            this.y = new ArrayList<>();
            this.dimension++;
        }
    }

    public void createZ() {
        if (this.z == null) {
            this.z = new ArrayList<>();
            this.dimension++;
        }
    }

    public void createT() {
        if (this.t == null) {
            this.t = new ArrayList<>();
            this.dimension++;
        }
    }

    public void addX(Double x) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.x.add(x);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addY(Double y) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.y.add(y);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addZ(Double z) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.z.add(z);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addT(Double t) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.t.add(t);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public Integer getDimension() {
        return dimension;
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
