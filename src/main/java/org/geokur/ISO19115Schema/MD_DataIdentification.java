/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "MD_DataIdentification", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
public class MD_DataIdentification {

    // occurrence and obligation
    // TODO: Is "abstract" possible here?
    private final String[] elementName = {"citation", "abstract", "purpose", "credit", "status", "pointOfContact", "spatialRepresentationType", "spatialResolution", "temporalResolution", "topicCategory", "extent", "additionalDocumentation", "processingLevel", "resourceMaintenance", "graphicOverview", "resourceFormat", "descriptiveKeywords", "resourceSpecificUsage", "resourceConstraints", "associatedResource", "defaultLocale", "otherLocale", "environmentalDescription", "supplementalInformation"};
    private final int[] elementMax = {1, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, 1};
    private final boolean[] elementObligation = {true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "citation", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<CI_Citation> citation;

    @XmlElement(name = "abstract", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> abstractElement;

    @XmlElement(name = "purpose", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> purpose;

    @XmlElement(name = "credit", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> credit;

    @XmlElementWrapper(name = "status", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<MD_ProgressCode> status;

    @XmlElementWrapper(name = "pointOfContact", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<CI_Responsibility> pointOfContact;

    @XmlElementWrapper(name = "spatialRepresentationType", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<MD_SpatialRepresentationTypeCode> spatialRepresentationType;

    @XmlElementWrapper(name = "spatialResolution", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<MD_Resolution> spatialResolution;

    @XmlElement(name = "temporalResolution", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> temporalResolution;

    @XmlElementWrapper(name = "topicCategory", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<MD_TopicCategoryCode> topicCategory;

    @XmlElementWrapper(name = "extent", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<EX_Extent> extent;

    @XmlElementWrapper(name = "additionalDocumentation", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<CI_Citation> additionalDocumentation;

    @XmlElementWrapper(name = "processingLevel", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<MD_Identifier> processingLevel;

    @XmlElementWrapper(name = "resourceMaintenance", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<MD_MaintenanceInformation> resourceMaintenance;

    @XmlElementWrapper(name = "graphicOverview", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<MD_BrowseGraphic> graphicOverview;

    @XmlElementWrapper(name = "resourceFormat", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<MD_Format> resourceFormat;

    @XmlElementWrapper(name = "descriptiveKeywords", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<MD_Keywords> descriptiveKeywords;

    @XmlElementWrapper(name = "resourceSpecificUsage", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<MD_Usage> resourceSpecificUsage;

    @XmlElementWrapper(name = "resourceConstraints", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<MD_Constraints> resourceConstraints;

    @XmlElementWrapper(name = "associatedResource", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<MD_AssociatedResource> associatedResource;

    @XmlElementWrapper(name = "defaultLocale", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<PT_Locale> defaultLocale;

    @XmlElementWrapper(name = "otherLocale", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<PT_Locale> otherLocale;

    @XmlElement(name = "environmentalDescription", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> environmentalDescription;

    @XmlElement(name = "supplementalInformation", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> supplementalInformation;

    // methods
    public MD_DataIdentification(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_DataIdentification);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_DataIdentification);
                if (!tempList.contains(elementName[i])) {
                    // element not mandatory
                    elementObligation[i] = false;
                }
            }
        }
    }

    public void createCitation() {
        if (this.citation == null) {
            this.citation = new ArrayList<>();
        }
    }

    public void createAbstract() {
        if (this.abstractElement == null) {
            this.abstractElement = new ArrayList<>();
        }
    }

    public void createPurpose() {
        if (this.purpose == null) {
            this.purpose = new ArrayList<>();
        }
    }

    public void createCredit() {
        if (this.credit == null) {
            this.credit = new ArrayList<>();
        }
    }

    public void createStatus() {
        if (this.status == null) {
            this.status = new ArrayList<>();
        }
    }

    public void createPointOfContact() {
        if (this.pointOfContact == null) {
            this.pointOfContact = new ArrayList<>();
        }
    }

    public void createSpatialRepresentationType() {
        if (this.spatialRepresentationType == null) {
            this.spatialRepresentationType = new ArrayList<>();
        }
    }

    public void createSpatialResolution() {
        if (this.spatialResolution == null) {
            this.spatialResolution = new ArrayList<>();
        }
    }

    public void createTemporalResolution() {
        if (this.temporalResolution == null) {
            this.temporalResolution = new ArrayList<>();
        }
    }

    public void createTopicCategory() {
        if (this.topicCategory == null) {
            this.topicCategory = new ArrayList<>();
        }
    }

    public void createExtent() {
        if (this.extent == null) {
            this.extent = new ArrayList<>();
        }
    }

    public void createAdditionalDocumentation() {
        if (this.additionalDocumentation == null) {
            this.additionalDocumentation = new ArrayList<>();
        }
    }

    public void createProcessingLevel() {
        if (this.processingLevel == null) {
            this.processingLevel = new ArrayList<>();
        }
    }

    public void createResourceMaintenance() {
        if (this.resourceMaintenance == null) {
            this.resourceMaintenance = new ArrayList<>();
        }
    }

    public void createGraphicOverview() {
        if (this.graphicOverview == null) {
            this.graphicOverview = new ArrayList<>();
        }
    }

    public void createResourceFormat() {
        if (this.resourceFormat == null) {
            this.resourceFormat = new ArrayList<>();
        }
    }

    public void createDescriptiveKeywords() {
        if (this.descriptiveKeywords == null) {
            this.descriptiveKeywords = new ArrayList<>();
        }
    }

    public void createResourceSpecificUsage() {
        if (this.resourceSpecificUsage == null) {
            this.resourceSpecificUsage = new ArrayList<>();
        }
    }

    public void createResourceConstraints() {
        if (this.resourceConstraints == null) {
            this.resourceConstraints = new ArrayList<>();
        }
    }

    public void createAssociatedResource() {
        if (this.associatedResource == null) {
            this.associatedResource = new ArrayList<>();
        }
    }

    public void createDefaultLocale() {
        if (this.defaultLocale == null) {
            this.defaultLocale = new ArrayList<>();
        }
    }

    public void createOtherLocale() {
        if (this.otherLocale == null) {
            this.otherLocale = new ArrayList<>();
        }
    }

    public void createEnvironmentalDescription() {
        if (this.environmentalDescription == null) {
            this.environmentalDescription = new ArrayList<>();
        }
    }

    public void createSupplementalInformation() {
        if (this.supplementalInformation == null) {
            this.supplementalInformation = new ArrayList<>();
        }
    }

    public void addCitation(CI_Citation citation) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.citation.add(citation);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAbstract(String abstractElement) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.abstractElement.add(abstractElement);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPurpose(String purpose) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.purpose.add(purpose);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCredit(String credit) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.credit.add(credit);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addStatus(MD_ProgressCode status) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.status.add(status);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPointOfContact(CI_Responsibility pointOfContact) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.pointOfContact.add(pointOfContact);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSpatialRepresentationType(MD_SpatialRepresentationTypeCode spatialRepresentationType) {
        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.spatialRepresentationType.add(spatialRepresentationType);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSpatialResolution(MD_Resolution spatialResolution) {
        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.spatialResolution.add(spatialResolution);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTemporalResolution(String temporalResolution) {
        int elementNum = 8;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.temporalResolution.add(temporalResolution);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTopicCategory(MD_TopicCategoryCode topicCategory) {
        int elementNum = 9;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.topicCategory.add(topicCategory);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addExtent(EX_Extent extent) {
        int elementNum = 10;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.extent.add(extent);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAdditionalDocumentation(CI_Citation additionalDocumentation) {
        int elementNum = 11;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.additionalDocumentation.add(additionalDocumentation);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addProcessingLevel(MD_Identifier processingLevel) {
        int elementNum = 12;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.processingLevel.add(processingLevel);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addResourceMaintenance(MD_MaintenanceInformation resourceMaintenance) {
        int elementNum = 13;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.resourceMaintenance.add(resourceMaintenance);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addGraphicOverview(MD_BrowseGraphic graphicOverview) {
        int elementNum = 14;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.graphicOverview.add(graphicOverview);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addResourceFormat(MD_Format resourceFormat) {
        int elementNum = 15;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.resourceFormat.add(resourceFormat);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDescriptiveKeywords(MD_Keywords descriptiveKeywords) {
        int elementNum = 16;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.descriptiveKeywords.add(descriptiveKeywords);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addResourceSpecificUsage(MD_Usage resourceSpecificUsage) {
        int elementNum = 17;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.resourceSpecificUsage.add(resourceSpecificUsage);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addResourceConstraints(MD_Constraints resourceConstraints) {
        int elementNum = 18;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.resourceConstraints.add(resourceConstraints);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAssociatedResource(MD_AssociatedResource associatedResource) {
        int elementNum = 19;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.associatedResource.add(associatedResource);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDefaultLocale(PT_Locale defaultLocale) {
        int elementNum = 20;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.defaultLocale.add(defaultLocale);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOtherLocale(PT_Locale otherLocale) {
        int elementNum = 21;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.otherLocale.add(otherLocale);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addEnvironmentalDescription(String environmentalDescription) {
        int elementNum = 22;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.environmentalDescription.add(environmentalDescription);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSupplementalInformation(String supplementalInformation) {
        int elementNum = 23;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.supplementalInformation.add(supplementalInformation);
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
