/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO191xxProfile.MaximumOccurrenceException;
import org.geokur.ISO191xxProfile.ObligationException;
import org.geokur.ISO191xxProfile.ProfileException;
import org.geokur.ISO191xxProfile.ProfileReader;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "MD_VectorSpatialRepresentation", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
public class MD_VectorSpatialRepresentation extends MD_SpatialRepresentation {

    // occurrence and obligation
    private final String[] elementName = {"topologyLevel", "geometricObjects"};
    private final int[] elementMax = {1, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "topologyLevel", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    @XmlElementRef
    public List<MD_TopologyLevelCode> topologyLevel;

    @XmlElementWrapper(name = "geometricObjects", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    @XmlElementRef
    public List<MD_GeometricObjects> geometricObjects;

    // methods
    public MD_VectorSpatialRepresentation(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_VectorSpatialRepresentation);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_VectorSpatialRepresentation);
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

    public void addTopologyLevel(MD_TopologyLevelCode topologyLevel) {
        if (this.topologyLevel == null) {
            this.topologyLevel = new ArrayList<>();
        }

        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.topologyLevel.add(topologyLevel);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addGeometricObjects(MD_GeometricObjects geometricObjects) {
        if (this.geometricObjects == null) {
            this.geometricObjects = new ArrayList<>();
        }

        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.geometricObjects.add(geometricObjects);
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
