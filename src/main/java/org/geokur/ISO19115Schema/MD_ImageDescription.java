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
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "MD_ImageDescription", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
public class MD_ImageDescription extends MD_CoverageDescription {

    // occurrence and obligation
    private final String[] elementName = {"attributeDescription", "processingLevelCode", "attributeGroup", "illuminationElevationAngle", "illuminationAzimuthAngle", "imagingCondition", "imageQualityCode", "cloudCoverPercentage", "compressionGenerationQuantity", "triangulationIndicator", "radiometricCalibrationDataAvailability", "cameraCalibrationInformationAvailability", "filmDistortionInformationAvailability", "lensDistortionInformationAvailability"};
    private final int[] elementMax = {1, 1, Integer.MAX_VALUE, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private final boolean[] elementObligation = {true, false, false, false, false, false, false, false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "illuminationElevationAngle", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> illuminationElevationAngle;

    @XmlElement(name = "illuminationAzimuthAngle", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> illuminationAzimuthAngle;

    @XmlElementWrapper(name = "imagingCondition", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    @XmlElementRef
    public List<MD_ImagingConditionCode> imagingCondition;

    @XmlElementWrapper(name = "imageQualityCode", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    @XmlElementRef
    public List<MD_Identifier> imageQualityCode;

    @XmlElement(name = "cloudCoverPercentage", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> cloudCoverPercentage;

    @XmlElement(name = "compressionGenerationQuantity", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> compressionGenerationQuantity;

    @XmlElement(name = "triangulationIndicator", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> triangulationIndicator;

    @XmlElement(name = "radiometricCalibrationDataAvailability", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> radiometricCalibrationDataAvailability;

    @XmlElement(name = "cameraCalibrationInformationAvailability", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> cameraCalibrationInformationAvailability;

    @XmlElement(name = "filmDistortionInformationAvailability", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> filmDistortionInformationAvailability;

    @XmlElement(name = "lensDistortionInformationAvailability", namespace = "http://standards.iso.org/iso/19115/-3/mrc/1.0")
    public List<String> lensDistortionInformationAvailability;

    // methods
    public MD_ImageDescription(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_ImageDescription);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_ImageDescription);
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

    public void createIlluminationElevationAngle() {
        if (this.illuminationElevationAngle == null) {
            this.illuminationElevationAngle = new ArrayList<>();
        }
    }

    public void createIlluminationAzimuthAngle() {
        if (this.illuminationAzimuthAngle == null) {
            this.illuminationAzimuthAngle = new ArrayList<>();
        }
    }

    public void createImagingCondition() {
        if (this.imagingCondition == null) {
            this.imagingCondition = new ArrayList<>();
        }
    }

    public void createImageQualityCode() {
        if (this.imageQualityCode == null) {
            this.imageQualityCode = new ArrayList<>();
        }
    }

    public void createCloudCoverPercentage() {
        if (this.cloudCoverPercentage == null) {
            this.cloudCoverPercentage = new ArrayList<>();
        }
    }

    public void createCompressionGenerationQuantity() {
        if (this.compressionGenerationQuantity == null) {
            this.compressionGenerationQuantity = new ArrayList<>();
        }
    }

    public void createTriangulationIndicator() {
        if (this.triangulationIndicator == null) {
            this.triangulationIndicator = new ArrayList<>();
        }
    }

    public void createRadiometricCalibrationDataAvailability() {
        if (this.radiometricCalibrationDataAvailability == null) {
            this.radiometricCalibrationDataAvailability = new ArrayList<>();
        }
    }

    public void createCameraCalibrationInformationAvailability() {
        if (this.cameraCalibrationInformationAvailability == null) {
            this.cameraCalibrationInformationAvailability = new ArrayList<>();
        }
    }

    public void createFilmDistortionInformationAvailability() {
        if (this.filmDistortionInformationAvailability == null) {
            this.filmDistortionInformationAvailability = new ArrayList<>();
        }
    }

    public void createLensDistortionInformationAvailability() {
        if (this.lensDistortionInformationAvailability == null) {
            this.lensDistortionInformationAvailability = new ArrayList<>();
        }
    }

    public void addIlluminationElevationAngle(String illuminationElevationAngle) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.illuminationElevationAngle.add(illuminationElevationAngle);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addIlluminationAzimuthAngle(String illuminationAzimuthAngle) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.illuminationAzimuthAngle.add(illuminationAzimuthAngle);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addImagingCondition(MD_ImagingConditionCode imagingCondition) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.imagingCondition.add(imagingCondition);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addImageQualityCode(MD_Identifier imageQualityCode) {
        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.imageQualityCode.add(imageQualityCode);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCloudCoverPercentage(String cloudCoverPercentage) {
        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.cloudCoverPercentage.add(cloudCoverPercentage);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCompressionGenerationQuantity(String compressionGenerationQuantity) {
        int elementNum = 8;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.compressionGenerationQuantity.add(compressionGenerationQuantity);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTriangulationIndicator(String triangulationIndicator) {
        int elementNum = 9;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.triangulationIndicator.add(triangulationIndicator);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addRadiometricCalibrationDataAvailability(String radiometricCalibrationDataAvailability) {
        int elementNum = 10;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.radiometricCalibrationDataAvailability.add(radiometricCalibrationDataAvailability);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCameraCalibrationInformationAvailability(String cameraCalibrationInformationAvailability) {
        int elementNum = 11;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.cameraCalibrationInformationAvailability.add(cameraCalibrationInformationAvailability);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addFilmDistortionInformationAvailability(String filmDistortionInformationAvailability) {
        int elementNum = 12;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.filmDistortionInformationAvailability.add(filmDistortionInformationAvailability);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addLensDistortionInformationAvailability(String lensDistortionInformationAvailability) {
        int elementNum = 13;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.lensDistortionInformationAvailability.add(lensDistortionInformationAvailability);
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
