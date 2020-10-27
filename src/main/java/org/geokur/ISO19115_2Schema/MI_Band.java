/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115_2Schema;

import org.geokur.ISO19115Schema.MD_Band;
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

@XmlRootElement(name = "MI_Band", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
public class MI_Band extends MD_Band {

    // occurrence and obligation
    private final String[] elementName = {"bandBoundaryDefinition", "nominalSpatialResolution", "transferFunctionType", "transmittedPolarisation", "detectedPolarisation"};
    private final int[] elementMax = {1, 1, 1, 1, 1};
    private final boolean[] elementObligation = {false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "bandBoundaryDefinition", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
    @XmlElementRef
    public List<MI_BandDefinition> bandBoundaryDefinition;

    @XmlElement(name = "nominalSpatialResolution", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
    public List<String> nominalSpatialResolution;

    @XmlElementWrapper(name = "transferFunctionType", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
    @XmlElementRef
    public List<MI_TransferFunctionTypeCode> transferFunctionType;

    @XmlElementWrapper(name = "transmittedPolarisation", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
    @XmlElementRef
    public List<MI_PolarisationOrientationCode> transmittedPolarisation;

    @XmlElementWrapper(name = "detectedPolarisation", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
    @XmlElementRef
    public List<MI_PolarisationOrientationCode> detectedPolarisation;

    // methods
    public MI_Band(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MI_Band);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MI_Band);
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

    public void addBandBoundaryDefinition(MI_BandDefinition bandBoundaryDefinition) {
        if (this.bandBoundaryDefinition == null) {
            this.bandBoundaryDefinition = new ArrayList<>();
        }

        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.bandBoundaryDefinition.add(bandBoundaryDefinition);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addNominalSpatialResolution(String nominalSpatialResolution) {
        if (this.nominalSpatialResolution == null) {
            this.nominalSpatialResolution = new ArrayList<>();
        }

        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.nominalSpatialResolution.add(nominalSpatialResolution);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTransferFunctionType(MI_TransferFunctionTypeCode transferFunctionType) {
        if (this.transferFunctionType == null) {
            this.transferFunctionType = new ArrayList<>();
        }

        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.transferFunctionType.add(transferFunctionType);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTransmittedPolarisation(MI_PolarisationOrientationCode transmittedPolarisation) {
        if (this.transmittedPolarisation == null) {
            this.transmittedPolarisation = new ArrayList<>();
        }

        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.transmittedPolarisation.add(transmittedPolarisation);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDetectedPolarisation(MI_PolarisationOrientationCode detectedPolarisation) {
        if (this.detectedPolarisation == null) {
            this.detectedPolarisation = new ArrayList<>();
        }

        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.detectedPolarisation.add(detectedPolarisation);
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
