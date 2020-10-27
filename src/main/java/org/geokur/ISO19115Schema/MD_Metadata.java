/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import org.geokur.ISO19115_2Schema.MI_Metadata;
import org.geokur.ISO19157Schema.*;
import org.geokur.ISO191xxProfile.MaximumOccurrenceException;
import org.geokur.ISO191xxProfile.ObligationException;
import org.geokur.ISO191xxProfile.ProfileException;
import org.geokur.ISO191xxProfile.ProfileReader;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "MD_Metadata", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
public class MD_Metadata {

    // occurrence and obligation
    private final String[] elementName = {"metadataIdentifier", "defaultLocale", "parentMetadata", "contact", "dateInfo", "metadataStandard", "metadataProfile", "alternativeMetadataReference", "otherLocale", "metadataLinkage", "spatialRepresentationInfo", "referenceSystemInfo", "metadataExtensionInfo", "identificationInfo", "contentInfo", "distributionInfo", "dataQualityInfo", "portrayalCatalogueInfo", "metadataConstraints", "applicationSchemaInfo", "metadataMaintenance", "resourceLineage", "metadataScope"};
    private final int[] elementMax = {1, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {false, false, false, true, true, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "metadataIdentifier", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<MD_Identifier> metadataIdentifier;

    @XmlElementWrapper(name = "defaultLocale", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<PT_Locale> defaultLocale;

    @XmlElementWrapper(name = "parentMetadata", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<CI_Citation> parentMetadata;

    @XmlElementWrapper(name = "contact", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<CI_Responsibility> contact;

    @XmlElementWrapper(name = "dateInfo", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<CI_Date> dateInfo;

    @XmlElementWrapper(name = "metadataStandard", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<CI_Citation> metadataStandard;

    @XmlElementWrapper(name = "metadataProfile", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<CI_Citation> metadataProfile;

    @XmlElementWrapper(name = "alternativeMetadataReference", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<CI_Citation> alternativeMetadataReference;

    @XmlElementWrapper(name = "otherLocale", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<PT_Locale> otherLocale;

    @XmlElementWrapper(name = "metadataLinkage", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<CI_OnlineResource> metadataLinkage;

    @XmlElementWrapper(name = "spatialRepresentationInfo", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<MD_GridSpatialRepresentation> spatialRepresentationInfo;

    @XmlElementWrapper(name = "referenceSystemInfo", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<MD_ReferenceSystem> referenceSystemInfo;

    @XmlElementWrapper(name = "metadataExtensionInfo", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<MD_MetadataExtensionInformation> metadataExtensionInfo;

    @XmlElementWrapper(name = "identificationInfo", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<MD_Identification> identificationInfo;

    @XmlElementWrapper(name = "contentInfo", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<MD_FeatureCatalogueDescription> contentInfo;

    @XmlElementWrapper(name = "distributionInfo", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<MD_Distribution> distributionInfo;

    @XmlElementWrapper(name = "dataQualityInfo", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<DQ_DataQuality> dataQualityInfo;

    @XmlElementWrapper(name = "portrayalCatalogueInfo", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<MD_PortrayalCatalogueReference> portrayalCatalogueInfo;

    @XmlElementWrapper(name = "metadataConstraints", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<MD_Constraints> metadataConstraints;

    @XmlElementWrapper(name = "applicationSchemaInfo", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<MD_ApplicationSchemaInformation> applicationSchemaInfo;

    @XmlElementWrapper(name = "metadataMaintenance", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<MD_MaintenanceInformation> metadataMaintenance;

    @XmlElementWrapper(name = "resourceLineage", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<LI_Lineage> resourceLineage;

    @XmlElementWrapper(name = "metadataScope", namespace = "http://standards.iso.org/iso/19115/-3/mdb/2.0")
    @XmlElementRef
    public List<MD_MetadataScope> metadataScope;

    // variables for correct marshalling of specified classes
    public List<MI_Metadata> metadata;

    // methods
    public MD_Metadata(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_Metadata);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_Metadata);
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

    public void addMetadataIdentifier(MD_Identifier metadataIdentifier) {
        if (this.metadataIdentifier == null) {
            this.metadataIdentifier = new ArrayList<>();
        }

        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.metadataIdentifier.add(metadataIdentifier);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDefaultLocale(PT_Locale defaultLocale) {
        if (this.defaultLocale == null) {
            this.defaultLocale = new ArrayList<>();
        }

        int elementNum = 1;
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

    public void addParentMetadata(CI_Citation parentMetadata) {
        if (this.parentMetadata == null) {
            this.parentMetadata = new ArrayList<>();
        }

        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.parentMetadata.add(parentMetadata);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addContact(CI_Responsibility contact) {
        if (this.contact == null) {
            this.contact = new ArrayList<>();
        }

        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.contact.add(contact);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDateInfo(CI_Date dateInfo) {
        if (this.dateInfo == null) {
            this.dateInfo = new ArrayList<>();
        }

        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.dateInfo.add(dateInfo);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMetadataStandard(CI_Citation metadataStandard) {
        if (this.metadataStandard == null) {
            this.metadataStandard = new ArrayList<>();
        }

        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.metadataStandard.add(metadataStandard);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMetadataProfile(CI_Citation metadataProfile) {
        if (this.metadataProfile == null) {
            this.metadataProfile = new ArrayList<>();
        }

        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.metadataProfile.add(metadataProfile);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addAlternativeMetadataReference(CI_Citation alternativeMetadataReference) {
        if (this.alternativeMetadataReference == null) {
            this.alternativeMetadataReference = new ArrayList<>();
        }

        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.alternativeMetadataReference.add(alternativeMetadataReference);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOtherLocale(PT_Locale otherLocale) {
        if (this.otherLocale == null) {
            this.otherLocale = new ArrayList<>();
        }

        int elementNum = 8;
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

    public void addMetadataLinkage(CI_OnlineResource metadataLinkage) {
        if (this.metadataLinkage == null) {
            this.metadataLinkage = new ArrayList<>();
        }

        int elementNum = 9;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.metadataLinkage.add(metadataLinkage);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSpatialRepresentationInfo(MD_GridSpatialRepresentation spatialRepresentationInfo) {
        if (this.spatialRepresentationInfo == null) {
            this.spatialRepresentationInfo = new ArrayList<>();
        }

        int elementNum = 10;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.spatialRepresentationInfo.add(spatialRepresentationInfo);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addReferenceSystemInfo(MD_ReferenceSystem referenceSystemInfo) {
        if (this.referenceSystemInfo == null) {
            this.referenceSystemInfo = new ArrayList<>();
        }

        int elementNum = 11;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.referenceSystemInfo.add(referenceSystemInfo);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMetadataExtensionInfo(MD_MetadataExtensionInformation metadataExtensionInfo) {
        if (this.metadataExtensionInfo == null) {
            this.metadataExtensionInfo = new ArrayList<>();
        }

        int elementNum = 12;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.metadataExtensionInfo.add(metadataExtensionInfo);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addIdentificationInfo(MD_Identification identificationInfo) {
        if (this.identificationInfo == null) {
            this.identificationInfo = new ArrayList<>();
        }

        int elementNum = 13;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.identificationInfo.add(identificationInfo);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addContentInfo(MD_FeatureCatalogueDescription contentInfo) {
        if (this.contentInfo == null) {
            this.contentInfo = new ArrayList<>();
        }

        int elementNum = 14;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.contentInfo.add(contentInfo);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDistributionInfo(MD_Distribution distributionInfo) {
        if (this.distributionInfo == null) {
            this.distributionInfo = new ArrayList<>();
        }

        int elementNum = 15;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.distributionInfo.add(distributionInfo);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDataQualityInfo(DQ_DataQuality dataQualityInfo) {
        if (this.dataQualityInfo == null) {
            this.dataQualityInfo = new ArrayList<>();
        }

        int elementNum = 16;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.dataQualityInfo.add(dataQualityInfo);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPortrayalCatalogueInfo(MD_PortrayalCatalogueReference portrayalCatalogueInfo) {
        if (this.portrayalCatalogueInfo == null) {
            this.portrayalCatalogueInfo = new ArrayList<>();
        }

        int elementNum = 17;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.portrayalCatalogueInfo.add(portrayalCatalogueInfo);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMetadataConstraints(MD_Constraints metadataConstraints) {
        if (this.metadataConstraints == null) {
            this.metadataConstraints = new ArrayList<>();
        }

        int elementNum = 18;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.metadataConstraints.add(metadataConstraints);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addApplicationSchemaInfo(MD_ApplicationSchemaInformation applicationSchemaInfo) {
        if (this.applicationSchemaInfo == null) {
            this.applicationSchemaInfo = new ArrayList<>();
        }

        int elementNum = 19;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.applicationSchemaInfo.add(applicationSchemaInfo);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMetadataMaintenance(MD_MaintenanceInformation metadataMaintenance) {
        if (this.metadataMaintenance == null) {
            this.metadataMaintenance = new ArrayList<>();
        }

        int elementNum = 20;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.metadataMaintenance.add(metadataMaintenance);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addResourceLineage(LI_Lineage resourceLineage) {
        if (this.resourceLineage == null) {
            this.resourceLineage = new ArrayList<>();
        }

        int elementNum = 21;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.resourceLineage.add(resourceLineage);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMetadataScope(MD_MetadataScope metadataScope) {
        if (this.metadataScope == null) {
            this.metadataScope = new ArrayList<>();
        }

        int elementNum = 22;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.metadataScope.add(metadataScope);
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
