/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19115_2Schema;

import org.geokur.ISO191xxProfile.MaximumOccurrenceException;
import org.geokur.ISO191xxProfile.ObligationException;
import org.geokur.ISO191xxProfile.ProfileException;
import org.geokur.ISO191xxProfile.ProfileReader;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "MI_EnvironmentalRecord", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
public class MI_EnvironmentalRecord {

    // occurrence and obligation
    private final String[] elementName = {"averageAirTemperature", "maxRelativeHumidity", "maxAltitude", "meteorologicalConditions", "solarAzimuth", "solarElevation"};
    private final int[] elementMax = {1, 1, 1, 1, 1, 1};
    private final boolean[] elementObligation = {false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "averageAirTemperature", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    public List<String> averageAirTemperature;

    @XmlElement(name = "maxRelativeHumidity", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    public List<String> maxRelativeHumidity;

    @XmlElement(name = "maxAltitude", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    public List<String> maxAltitude;

    @XmlElement(name = "meteorologicalConditions", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    public List<String> meteorologicalConditions;

    @XmlElement(name = "solarAzimuth", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    public List<String> solarAzimuth;

    @XmlElement(name = "solarElevation", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    public List<String> solarElevation;

    // methods
    public MI_EnvironmentalRecord(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MI_EnvironmentalRecord);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MI_EnvironmentalRecord);
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

    public void addAverageAirTemperature(String averageAirTemperature) {
        if (this.averageAirTemperature == null) {
            this.averageAirTemperature = new ArrayList<>();
        }

        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.averageAirTemperature.add(averageAirTemperature);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMaxRelativeHumidity(String maxRelativeHumidity) {
        if (this.maxRelativeHumidity == null) {
            this.maxRelativeHumidity = new ArrayList<>();
        }

        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.maxRelativeHumidity.add(maxRelativeHumidity);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMaxAltitude(String maxAltitude) {
        if (this.maxAltitude == null) {
            this.maxAltitude = new ArrayList<>();
        }

        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.maxAltitude.add(maxAltitude);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMeteorologicalConditions(String meteorologicalConditions) {
        if (this.meteorologicalConditions == null) {
            this.meteorologicalConditions = new ArrayList<>();
        }

        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.meteorologicalConditions.add(meteorologicalConditions);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSolarAzimuth(String solarAzimuth) {
        if (this.solarAzimuth == null) {
            this.solarAzimuth = new ArrayList<>();
        }

        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.solarAzimuth.add(solarAzimuth);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSolarElevation(String solarElevation) {
        if (this.solarElevation == null) {
            this.solarElevation = new ArrayList<>();
        }

        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.solarElevation.add(solarElevation);
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
