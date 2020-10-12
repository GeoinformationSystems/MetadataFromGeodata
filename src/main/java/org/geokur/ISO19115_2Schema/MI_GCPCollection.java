/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115_2Schema;

import org.geokur.ISO19115Schema.MD_ReferenceSystem;
import org.geokur.ISO191xxProfile.MaximumOccurrenceException;
import org.geokur.ISO191xxProfile.ObligationException;
import org.geokur.ISO191xxProfile.ProfileException;
import org.geokur.ISO191xxProfile.ProfileReader;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "MI_GCPCollection", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
public class MI_GCPCollection extends MI_GeolocationInformation {

    // occurrence and obligation
    private final String[] elementName = {"collectionIdentification", "collectionName", "coordinateReferenceSystem", "gcp"};
    private final int[] elementMax = {1, 1, 1, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, true, true, true};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "collectionIdentification", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    public List<String> collectionIdentification;

    @XmlElement(name = "collectionName", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    public List<String> collectionName;

    @XmlElementWrapper(name = "coordinateReferenceSystem", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    @XmlElementRef
    public List<MD_ReferenceSystem> coordinateReferenceSystem;

    @XmlElementWrapper(name = "gcp", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
    @XmlElementRef
    public List<MI_GCP> gcp;

    // methods
    public MI_GCPCollection(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MI_GCPCollection);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MI_GCPCollection);
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

    public void createCollectionIdentification() {
        if (this.collectionIdentification == null) {
            this.collectionIdentification = new ArrayList<>();
        }
    }

    public void createCollectionName() {
        if (this.collectionName == null) {
            this.collectionName = new ArrayList<>();
        }
    }

    public void createCoordinateReferenceSystem() {
        if (this.coordinateReferenceSystem == null) {
            this.coordinateReferenceSystem = new ArrayList<>();
        }
    }

    public void createGcp() {
        if (this.gcp == null) {
            this.gcp = new ArrayList<>();
        }
    }

    public void addCollectionIdentification(String collectionIdentification) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.collectionIdentification.add(collectionIdentification);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCollectionName(String collectionName) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.collectionName.add(collectionName);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCoordinateReferenceSystem(MD_ReferenceSystem coordinateReferenceSystem) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.coordinateReferenceSystem.add(coordinateReferenceSystem);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addGcp(MI_GCP gcp) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.gcp.add(gcp);
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
