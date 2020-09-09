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

@XmlRootElement(name = "SV_OperationMetadata", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
public class SV_OperationMetadata {

    // occurrence and obligation
    private final String[] elementName = {"operationName", "distributedComputingPlatform", "operationDescription", "invocationName", "connectPoint", "parameters", "dependsOn"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, true, false, false, true, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "operationName", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    public List<String> operationName;

    @XmlElementWrapper(name = "distributedComputingPlatform", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    @XmlElementRef
    public List<DCPList> distributedComputingPlatform;

    @XmlElement(name = "operationDescription", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    public List<String> operationDescription;

    @XmlElement(name = "invocationName", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    public List<String> invocationName;

    @XmlElementWrapper(name = "connectPoint", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    @XmlElementRef
    public List<CI_OnlineResource> connectPoint;

    @XmlElementWrapper(name = "parameters", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    @XmlElementRef
    public List<SV_Parameter> parameters;

    @XmlElementWrapper(name = "dependsOn", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    @XmlElementRef
    public List<SV_OperationMetadata> dependsOn;

    // methods
    public SV_OperationMetadata(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.SV_OperationMetadata);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.SV_OperationMetadata);
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

    public void createOperationName() {
        if (this.operationName == null) {
            this.operationName = new ArrayList<>();
        }
    }

    public void createDistributedComputingPlatform() {
        if (this.distributedComputingPlatform == null) {
            this.distributedComputingPlatform = new ArrayList<>();
        }
    }

    public void createOperationDescription() {
        if (this.operationDescription == null) {
            this.operationDescription = new ArrayList<>();
        }
    }

    public void createInvocationName() {
        if (this.invocationName == null) {
            this.invocationName = new ArrayList<>();
        }
    }

    public void createConnectPoint() {
        if (this.connectPoint == null) {
            this.connectPoint = new ArrayList<>();
        }
    }

    public void createParameters() {
        if (this.parameters == null) {
            this.parameters = new ArrayList<>();
        }
    }

    public void createDependsOn() {
        if (this.dependsOn == null) {
            this.dependsOn = new ArrayList<>();
        }
    }

    public void addOperationName(String operationName) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.operationName.add(operationName);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDistributedComputingPlatform(DCPList distributedComputingPlatform) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.distributedComputingPlatform.add(distributedComputingPlatform);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOperationDescription(String operationDescription) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.operationDescription.add(operationDescription);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addInvocationName(String invocationName) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.invocationName.add(invocationName);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addConnectPoint(CI_OnlineResource connectPoint) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.connectPoint.add(connectPoint);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addParameters(SV_Parameter parameters) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.parameters.add(parameters);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDependsOn(SV_OperationMetadata dependsOn) {
        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.dependsOn.add(dependsOn);
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
