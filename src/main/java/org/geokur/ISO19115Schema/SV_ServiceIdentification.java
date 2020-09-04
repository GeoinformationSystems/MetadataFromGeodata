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

@XmlRootElement(name = "SV_ServiceIdentification", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
public class SV_ServiceIdentification {

    // occurrence and obligation
    private final String[] elementName = {"citation", "abstract", "purpose", "credit", "status", "pointOfContact", "spatialRepresentationType", "spatialResolution", "temporalResolution", "topicCategory", "extent", "additionalDocumentation", "processingLevel", "resourceMaintenance", "graphicOverview", "resourceFormat", "descriptiveKeywords", "resourceSpecificUsage", "resourceConstraints", "associatedResource", "serviceType", "serviceTypeVersion", "accessProperties", "couplingType", "coupledResource", "operatedDataset", "profile", "serviceStandard", "containsOperations", "operatesOn", "containsChain"};
    private final int[] elementMax = {1, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "citation", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    @XmlElementRef
    public List<CI_Citation> citation;

    @XmlElement(name = "abstract", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> abstract;

    @XmlElement(name = "purpose", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> purpose;

    @XmlElement(name = "credit", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> credit;

    @XmlElement(name = "status", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
    public List<String> status;

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
    public List<enumeration_MD_TopicCategoryCode> topicCategory;

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

    @XmlElement(name = "serviceType", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    public List<String> serviceType;

    @XmlElement(name = "serviceTypeVersion", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    public List<String> serviceTypeVersion;

    @XmlElementWrapper(name = "accessProperties", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    @XmlElementRef
    public List<MD_StandardOrderProcess> accessProperties;

    @XmlElementWrapper(name = "couplingType", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    @XmlElementRef
    public List<SV_CouplingType> couplingType;

    @XmlElementWrapper(name = "coupledResource", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    @XmlElementRef
    public List<SV_CoupledResource> coupledResource;

    @XmlElementWrapper(name = "operatedDataset", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    @XmlElementRef
    public List<CI_Citation> operatedDataset;

    @XmlElementWrapper(name = "profile", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    @XmlElementRef
    public List<CI_Citation> profile;

    @XmlElementWrapper(name = "serviceStandard", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    @XmlElementRef
    public List<CI_Citation> serviceStandard;

    @XmlElementWrapper(name = "containsOperations", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    @XmlElementRef
    public List<SV_OperationMetadata> containsOperations;

    @XmlElementWrapper(name = "operatesOn", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    @XmlElementRef
    public List<MD_DataIdentification> operatesOn;

    @XmlElementWrapper(name = "containsChain", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    @XmlElementRef
    public List<SV_OperationChainMetadata> containsChain;

    // methods
    public SV_ServiceIdentification(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.SV_ServiceIdentification);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.SV_ServiceIdentification);
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
        if (this.abstract == null) {
            this.abstract = new ArrayList<>();
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

    public void createServiceType() {
        if (this.serviceType == null) {
            this.serviceType = new ArrayList<>();
        }
    }

    public void createServiceTypeVersion() {
        if (this.serviceTypeVersion == null) {
            this.serviceTypeVersion = new ArrayList<>();
        }
    }

    public void createAccessProperties() {
        if (this.accessProperties == null) {
            this.accessProperties = new ArrayList<>();
        }
    }

    public void createCouplingType() {
        if (this.couplingType == null) {
            this.couplingType = new ArrayList<>();
        }
    }

    public void createCoupledResource() {
        if (this.coupledResource == null) {
            this.coupledResource = new ArrayList<>();
        }
    }

    public void createOperatedDataset() {
        if (this.operatedDataset == null) {
            this.operatedDataset = new ArrayList<>();
        }
    }

    public void createProfile() {
        if (this.profile == null) {
            this.profile = new ArrayList<>();
        }
    }

    public void createServiceStandard() {
        if (this.serviceStandard == null) {
            this.serviceStandard = new ArrayList<>();
        }
    }

    public void createContainsOperations() {
        if (this.containsOperations == null) {
            this.containsOperations = new ArrayList<>();
        }
    }

    public void createOperatesOn() {
        if (this.operatesOn == null) {
            this.operatesOn = new ArrayList<>();
        }
    }

    public void createContainsChain() {
        if (this.containsChain == null) {
            this.containsChain = new ArrayList<>();
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

    public void addAbstract(String abstract) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.abstract.add(abstract);
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

    public void addStatus(String status) {
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

    public void addTopicCategory(enumeration_MD_TopicCategoryCode topicCategory) {
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

    public void addServiceType(String serviceType) {
        int elementNum = 20;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.serviceType.add(serviceType);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addServiceTypeVersion(String serviceTypeVersion) {
        int elementNum = 21;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.serviceTypeVersion.add(serviceTypeVersion);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAccessProperties(MD_StandardOrderProcess accessProperties) {
        int elementNum = 22;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.accessProperties.add(accessProperties);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCouplingType(SV_CouplingType couplingType) {
        int elementNum = 23;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.couplingType.add(couplingType);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCoupledResource(SV_CoupledResource coupledResource) {
        int elementNum = 24;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.coupledResource.add(coupledResource);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOperatedDataset(CI_Citation operatedDataset) {
        int elementNum = 25;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.operatedDataset.add(operatedDataset);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addProfile(CI_Citation profile) {
        int elementNum = 26;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.profile.add(profile);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addServiceStandard(CI_Citation serviceStandard) {
        int elementNum = 27;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.serviceStandard.add(serviceStandard);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addContainsOperations(SV_OperationMetadata containsOperations) {
        int elementNum = 28;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.containsOperations.add(containsOperations);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOperatesOn(MD_DataIdentification operatesOn) {
        int elementNum = 29;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.operatesOn.add(operatesOn);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addContainsChain(SV_OperationChainMetadata containsChain) {
        int elementNum = 30;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.containsChain.add(containsChain);
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
