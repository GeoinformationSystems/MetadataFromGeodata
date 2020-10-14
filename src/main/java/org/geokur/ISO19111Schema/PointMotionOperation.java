/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19111Schema;

import org.geokur.ISO191xxProfile.ObligationException;
import org.geokur.ISO191xxProfile.ProfileException;
import org.geokur.ISO191xxProfile.ProfileReader;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "PointMotionOperation")
public class PointMotionOperation extends SingleOperation {

    // occurrence and obligation
    private final String[] elementName = {"name", "identifier", "alias", "remarks",
            "usage",
            "sourceCRS", "targetCRS", "interpolationCRS", "sourceCoordinateEpoch", "targetCoordinateEpoch", "operationVersion", "coordinateOperationAccuracy",
            "method", "parameterValue"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1,
            Integer.MAX_VALUE,
            1, 1, 1, 1, 1, 1, Integer.MAX_VALUE,
            1, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, false, false, false,
            false,
            false, false, false, false, false, true, false,
            true, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // methods
    public PointMotionOperation(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.PointMotionOperation);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.PointMotionOperation);
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
