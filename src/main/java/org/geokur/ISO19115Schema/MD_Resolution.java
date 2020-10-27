/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO191xxProfile.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "MD_Resolution", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
public class MD_Resolution {
    // union class - means only one variable allowed

    // occurrence and obligation
    private final String[] elementName = {"equivalentScale", "distance", "vertical", "angularDistance", "levelOfDetail"};
    private final int[] elementMax = {1, 1, 1, 1, 1};
    private final boolean[] elementObligation = {false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "equivalentScale", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<MD_RepresentativeFraction> equivalentScale;

    @XmlElement(name = "distance", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<Double> distance;

    @XmlElement(name = "vertical", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<Double> vertical;

    @XmlElement(name = "angularDistance", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<Double> angularDistance;

    @XmlElement(name = "levelOfDetail", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> levelOfDetail;

    // methods
    public MD_Resolution(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_Resolution);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_Resolution);
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

    public void addEquivalentScale(MD_RepresentativeFraction equivalentScale) {
        try {
            if (this.distance != null || this.vertical != null || this.angularDistance != null || this.levelOfDetail != null) {
                throw new UnionException(className);
            } else {
                if (this.equivalentScale == null) {
                    this.equivalentScale = new ArrayList<>();
                }
            }
        } catch (UnionException e) {
            System.out.println(e.getMessage());
        }

        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.equivalentScale.add(equivalentScale);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDistance(Double distance) {
        try {
            if (this.equivalentScale != null || this.vertical != null || this.angularDistance != null || this.levelOfDetail != null) {
                throw new UnionException(className);
            } else {
                if (this.distance == null) {
                    this.distance = new ArrayList<>();
                }
            }
        } catch (UnionException e) {
            System.out.println(e.getMessage());
        }

        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.distance.add(distance);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addVertical(Double vertical) {
        try {
            if (this.equivalentScale != null || this.distance != null || this.angularDistance != null || this.levelOfDetail != null) {
                throw new UnionException(className);
            } else {
                if (this.vertical == null) {
                    this.vertical = new ArrayList<>();
                }
            }
        } catch (UnionException e) {
            System.out.println(e.getMessage());
        }

        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.vertical.add(vertical);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAngularDistance(Double angularDistance) {
        try {
            if (this.equivalentScale != null || this.distance != null || this.vertical != null || this.levelOfDetail != null) {
                throw new UnionException(className);
            } else {
                if (this.angularDistance == null) {
                    this.angularDistance = new ArrayList<>();
                }
            }
        } catch (UnionException e) {
            System.out.println(e.getMessage());
        }

        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.angularDistance.add(angularDistance);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addLevelOfDetail(String levelOfDetail) {
        try {
            if (this.equivalentScale != null || this.distance != null || this.vertical != null || this.angularDistance != null) {
                throw new UnionException(className);
            } else {
                if (this.levelOfDetail == null) {
                    this.levelOfDetail = new ArrayList<>();
                }
            }
        } catch (UnionException e) {
            System.out.println(e.getMessage());
        }

        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.levelOfDetail.add(levelOfDetail);
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
