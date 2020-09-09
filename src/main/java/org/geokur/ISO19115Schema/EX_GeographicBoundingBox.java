/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO191xxProfile.ObligationException;
import org.geokur.ISO191xxProfile.ProfileException;
import org.geokur.ISO191xxProfile.ProfileReader;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "EX_GeographicBoundingBox", namespace = "http://standards.iso.org/iso/19115/-3/gex/1.0")
public class EX_GeographicBoundingBox extends EX_GeographicExtent {

    // occurrence and obligation
    private final String[] elementName = {"extentTypeCode", "westBoundLongitude", "eastBoundLongitude", "southBoundLatitude", "northBoundLatitude"};
    private final int[] elementMax = {1, 1, 1, 1, 1};
    private final boolean[] elementObligation = {false, true, true, true, true};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "westBoundLongitude", namespace = "http://standards.iso.org/iso/19115/-3/gex/1.0")
    public List<String> westBoundLongitude;

    @XmlElement(name = "eastBoundLongitude", namespace = "http://standards.iso.org/iso/19115/-3/gex/1.0")
    public List<String> eastBoundLongitude;

    @XmlElement(name = "southBoundLatitude", namespace = "http://standards.iso.org/iso/19115/-3/gex/1.0")
    public List<String> southBoundLatitude;

    @XmlElement(name = "northBoundLatitude", namespace = "http://standards.iso.org/iso/19115/-3/gex/1.0")
    public List<String> northBoundLatitude;

    // methods
    public EX_GeographicBoundingBox(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.EX_GeographicBoundingBox);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.EX_GeographicBoundingBox);
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

    public void createWestBoundLongitude() {
        if (this.westBoundLongitude == null) {
            this.westBoundLongitude = new ArrayList<>();
        }
    }

    public void createEastBoundLongitude() {
        if (this.eastBoundLongitude == null) {
            this.eastBoundLongitude = new ArrayList<>();
        }
    }

    public void createSouthBoundLatitude() {
        if (this.southBoundLatitude == null) {
            this.southBoundLatitude = new ArrayList<>();
        }
    }

    public void createNorthBoundLatitude() {
        if (this.northBoundLatitude == null) {
            this.northBoundLatitude = new ArrayList<>();
        }
    }

    public void addWestBoundLongitude(String westBoundLongitude) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.westBoundLongitude.add(westBoundLongitude);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addEastBoundLongitude(String eastBoundLongitude) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.eastBoundLongitude.add(eastBoundLongitude);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSouthBoundLatitude(String southBoundLatitude) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.southBoundLatitude.add(southBoundLatitude);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addNorthBoundLatitude(String northBoundLatitude) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.northBoundLatitude.add(northBoundLatitude);
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
