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
public class SV_ServiceIdentification extends MD_Identification {

    // occurrence and obligation
    private final String[] elementName = {"citation", "abstractElement", "purpose", "credit", "status", "pointOfContact", "spatialRepresentationType", "spatialResolution", "temporalResolution", "topicCategory", "extent", "additionalDocumentation", "processingLevel", "resourceMaintenance", "graphicOverview", "resourceFormat", "descriptiveKeywords", "resourceSpecificUsage", "resourceConstraints", "associatedResource", "serviceType", "serviceTypeVersion", "accessProperties", "couplingType", "coupledResource", "operatedDataset", "profile", "serviceStandard", "containsOperations", "operatesOn", "containsChain"};
    private final String[] elementNameProfile = {"citation", "abstract", "purpose", "credit", "status", "pointOfContact", "spatialRepresentationType", "spatialResolution", "temporalResolution", "topicCategory", "extent", "additionalDocumentation", "processingLevel", "resourceMaintenance", "graphicOverview", "resourceFormat", "descriptiveKeywords", "resourceSpecificUsage", "resourceConstraints", "associatedResource", "serviceType", "serviceTypeVersion", "accessProperties", "couplingType", "coupledResource", "operatedDataset", "profile", "serviceStandard", "containsOperations", "operatesOn", "containsChain"}; // true profile name of elements
    private final int[] elementMax = {1, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
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
                if (!tempList.contains(elementNameProfile[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.SV_ServiceIdentification);
                if (!tempList.contains(elementNameProfile[i])) {
                    // element not mandatory
                    elementObligation[i] = false;
                } else if (tempList.contains(elementName[i])) {
                    // element mandatory
                    elementObligation[i] = true;
                }
            }
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
                    throw new ProfileException(className + " - " + elementNameProfile[i]);
                }
                if (elementObligation[i] && (tempList == null || tempList.isEmpty())) {
                    // test filling and obligation of all variable lists
                    throw new ObligationException(className + " - " + elementNameProfile[i]);
                }
            } catch (ProfileException | ObligationException | NoSuchFieldException | IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
