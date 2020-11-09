/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19111Schema;

import org.geokur.ISO191xxProfile.MaximumOccurrenceException;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "SingleCRS")
public abstract class SingleCRS extends CRS {

    // occurrence and obligation
    private final String[] elementName = {"name", "identifier", "alias", "remarks",
            "usage",
            "coordinateSystem", "datum", "datumEnsemble"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1,
            Integer.MAX_VALUE,
            1, 1, 1};
    private final boolean[] elementObligation = {true, false, false, false,
            false,
            true, false, false};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElementWrapper(name = "coordinateSystem")
    @XmlElementRef
    public List<CoordinateSystem> coordinateSystem;

    @XmlElementWrapper(name = "datum")
    @XmlElementRef
    public List<Datum> datum;

    @XmlElementWrapper(name = "datumEnsemble")
    @XmlElementRef
    public List<DatumEnsemble> datumEnsemble;

    // variables for correct marshalling of specified classes
    public List<VerticalCRS> verticalCRS;

    public List<DerivedCRS> derivedCRS;

    // methods
    public void addCoordinateSystem(CoordinateSystem coordinateSystem) {
        if (this.coordinateSystem == null) {
            this.coordinateSystem = new ArrayList<>();
        }

        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.coordinateSystem.add(coordinateSystem);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDatum(Datum datum) {
        if (this.datum == null) {
            this.datum = new ArrayList<>();
        }

        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.datum.add(datum);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDatumEnsemble(DatumEnsemble datumEnsemble) {
        if (this.datumEnsemble == null) {
            this.datumEnsemble = new ArrayList<>();
        }

        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.datumEnsemble.add(datumEnsemble);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
