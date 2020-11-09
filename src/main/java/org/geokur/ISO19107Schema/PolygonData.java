/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19107Schema;

import org.geokur.ISO191xxProfile.ObligationException;
import org.geokur.ISO191xxProfile.ProfileException;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "PolygonData")
public class PolygonData extends SurfaceData {

    // occurrence and obligation
    private final String[] elementName = {"rsid", "type", "segment", "orientation", "boundary", "spanningSurface", "interpolation"};
    private final int[] elementMax = {Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {false, false, false, false, true, false, false};

    private final String className = this.getClass().getSimpleName();

    // methods
    public PolygonData() {}

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

        // additionally test, whether each boundary in the polygon has minimum three points and is closed
        // if less than 3 points -> boundary is removed
        // if not closed, copy the first point to close the polygon
        // it is not tested whether all point lay on a line
        for (CurveData curveData : boundary) {
            // test
            try {
                if (curveData.dataPoint.size() < 3) {
                    boundary.remove(curveData);
                    throw new Exception("2D-boundary defined with " + curveData.dataPoint.size() + " points\n" +
                            "minimum are 3 points\n" +
                            "this boundary is removed");
                } else {
                    List<Double> firstPoint = curveData.dataPoint.get(0).coordinate.get(0).getCoordinates();
                    List<Double> lastPoint = curveData.dataPoint.get(curveData.dataPoint.size() - 1).coordinate.get(0).getCoordinates();
                    if (!firstPoint.equals(lastPoint)) {
                        curveData.dataPoint.add(curveData.dataPoint.get(0));
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
