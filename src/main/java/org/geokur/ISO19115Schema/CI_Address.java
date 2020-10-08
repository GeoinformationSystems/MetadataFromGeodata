/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO191xxProfile.MaximumOccurrenceException;
import org.geokur.ISO191xxProfile.ObligationException;
import org.geokur.ISO191xxProfile.ProfileException;
import org.geokur.ISO191xxProfile.ProfileReader;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "CI_Address", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
public class CI_Address {

    // occurrence and obligation
    private final String[] elementName = {"deliveryPoint", "city", "administrativeArea", "postalCode", "country", "electronicMailAddress"};
    private final int[] elementMax = {Integer.MAX_VALUE, 1, 1, 1, 1, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "deliveryPoint", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    public List<String> deliveryPoint;

    @XmlElement(name = "city", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    public List<String> city;

    @XmlElement(name = "administrativeArea", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    public List<String> administrativeArea;

    @XmlElement(name = "postalCode", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    public List<String> postalCode;

    @XmlElement(name = "country", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    public List<String> country;

    @XmlElement(name = "electronicMailAddress", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
    public List<String> electronicMailAddress;

    // methods
    public CI_Address(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.CI_Address);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.CI_Address);
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

    public void createDeliveryPoint() {
        if (this.deliveryPoint == null) {
            this.deliveryPoint = new ArrayList<>();
        }
    }

    public void createCity() {
        if (this.city == null) {
            this.city = new ArrayList<>();
        }
    }

    public void createAdministrativeArea() {
        if (this.administrativeArea == null) {
            this.administrativeArea = new ArrayList<>();
        }
    }

    public void createPostalCode() {
        if (this.postalCode == null) {
            this.postalCode = new ArrayList<>();
        }
    }

    public void createCountry() {
        if (this.country == null) {
            this.country = new ArrayList<>();
        }
    }

    public void createElectronicMailAddress() {
        if (this.electronicMailAddress == null) {
            this.electronicMailAddress = new ArrayList<>();
        }
    }

    public void addDeliveryPoint(String deliveryPoint) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.deliveryPoint.add(deliveryPoint);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCity(String city) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.city.add(city);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAdministrativeArea(String administrativeArea) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.administrativeArea.add(administrativeArea);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPostalCode(String postalCode) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.postalCode.add(postalCode);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCountry(String country) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.country.add(country);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addElectronicMailAddress(String electronicMailAddress) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.electronicMailAddress.add(electronicMailAddress);
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
