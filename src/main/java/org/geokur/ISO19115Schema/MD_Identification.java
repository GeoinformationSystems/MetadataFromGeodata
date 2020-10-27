/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO191xxProfile.MaximumOccurrenceException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "MD_Identification", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
public abstract class MD_Identification {

    // occurrence and obligation
    private final String[] elementName = {"citation", "abstractElement", "purpose", "credit", "status", "pointOfContact", "spatialRepresentationType", "spatialResolution", "temporalResolution", "topicCategory", "extent", "additionalDocumentation", "processingLevel", "resourceMaintenance", "graphicOverview", "resourceFormat", "descriptiveKeywords", "resourceSpecificUsage", "resourceConstraints", "associatedResource"};
    private final int[] elementMax = {1, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};

    private final String className = this.getClass().getSimpleName();

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

    // variables for correct marshalling of specified classes
    public List<MD_DataIdentification> dataIdentification;

    public List<SV_ServiceIdentification> serviceIdentification;

    // methods
    public void addCitation(CI_Citation citation) {
        if (this.citation == null) {
            this.citation = new ArrayList<>();
        }

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
        if (this.abstractElement == null) {
            this.abstractElement = new ArrayList<>();
        }

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
        if (this.purpose == null) {
            this.purpose = new ArrayList<>();
        }

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
        if (this.credit == null) {
            this.credit = new ArrayList<>();
        }

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
        if (this.status == null) {
            this.status = new ArrayList<>();
        }

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
        if (this.pointOfContact == null) {
            this.pointOfContact = new ArrayList<>();
        }

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
        if (this.spatialRepresentationType == null) {
            this.spatialRepresentationType = new ArrayList<>();
        }

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
        if (this.spatialResolution == null) {
            this.spatialResolution = new ArrayList<>();
        }

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
        if (this.temporalResolution == null) {
            this.temporalResolution = new ArrayList<>();
        }

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
        if (this.topicCategory == null) {
            this.topicCategory = new ArrayList<>();
        }

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
        if (this.extent == null) {
            this.extent = new ArrayList<>();
        }

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
        if (this.additionalDocumentation == null) {
            this.additionalDocumentation = new ArrayList<>();
        }

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
        if (this.processingLevel == null) {
            this.processingLevel = new ArrayList<>();
        }

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
        if (this.resourceMaintenance == null) {
            this.resourceMaintenance = new ArrayList<>();
        }

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
        if (this.graphicOverview == null) {
            this.graphicOverview = new ArrayList<>();
        }

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
        if (this.resourceFormat == null) {
            this.resourceFormat = new ArrayList<>();
        }

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
        if (this.descriptiveKeywords == null) {
            this.descriptiveKeywords = new ArrayList<>();
        }

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
        if (this.resourceSpecificUsage == null) {
            this.resourceSpecificUsage = new ArrayList<>();
        }

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
        if (this.resourceConstraints == null) {
            this.resourceConstraints = new ArrayList<>();
        }

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
        if (this.associatedResource == null) {
            this.associatedResource = new ArrayList<>();
        }

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
}
