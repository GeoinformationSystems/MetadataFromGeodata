/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19107Schema;

import org.geokur.ISO19103Schema.DirectPosition;
import org.geokur.ISO191xxProfile.MaximumOccurrenceException;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "CurveData")
public abstract class CurveData extends OrientableData {

    // occurrence and obligation
    private final String[] elementName = {"rsid", "type", "segment", "orientation", "dataPoint", "knot", "segment", "interpolation"};
    private final int[] elementMax = {Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 1};
    private final boolean[] elementObligation = {false, false, false, false, false, false, false, true};

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

    @XmlElementWrapper(name = "interpolation")
    @XmlElementRef
    public List<CurveInterpolation> interpolation;

    // variables for correct marshalling of specified classes
    public List<LineData> lineData;

    // methods
    public CurveData() {}

    public void addDataPoint(DirectPosition dataPoint) {
        if (this.dataPoint == null) {
            this.dataPoint = new ArrayList<>();
        }

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
        if (this.knot == null) {
            this.knot = new ArrayList<>();
        }

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
        if (this.segment == null) {
            this.segment = new ArrayList<>();
        }

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
}
