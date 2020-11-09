/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19115_2Schema;

import org.geokur.ISO191xxProfile.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "LE_NominalResolution", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
public class LE_NominalResolution {
    // union class - means only one variable allowed

    // occurrence and obligation
    private final String[] elementName = {"scanningResolution", "groundResolution"};
    private final int[] elementMax = {1, 1};
    private final boolean[] elementObligation = {true, true};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "scanningResolution", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    public List<String> scanningResolution;

    @XmlElement(name = "groundResolution", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    public List<String> groundResolution;

    // methods
    public LE_NominalResolution(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.LE_NominalResolution);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.LE_NominalResolution);
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

    public void addScanningResolution(String scanningResolution) {
        try {
            if (this.groundResolution != null) {
                throw new UnionException(className);
            } else {
                if (this.scanningResolution == null) {
                    this.scanningResolution = new ArrayList<>();
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
                this.scanningResolution.add(scanningResolution);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addGroundResolution(String groundResolution) {
        try {
            if (this.scanningResolution != null) {
                throw new UnionException(className);
            } else {
                if (this.groundResolution == null) {
                    this.groundResolution = new ArrayList<>();
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
                this.groundResolution.add(groundResolution);
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
