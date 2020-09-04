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

@XmlRootElement(name = "SV_CoupledResource", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
public class SV_CoupledResource {

    // occurrence and obligation
    private final String[] elementName = {"scopedName", "resourceReference", "resource", "operation"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1};
    private final boolean[] elementObligation = {false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "scopedName", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    public List<String> scopedName;

    @XmlElementWrapper(name = "resourceReference", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    @XmlElementRef
    public List<CI_Citation> resourceReference;

    @XmlElementWrapper(name = "resource", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    @XmlElementRef
    public List<MD_DataIdentification> resource;

    @XmlElementWrapper(name = "operation", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
    @XmlElementRef
    public List<SV_OperationMetadata> operation;

    // methods
    public SV_CoupledResource(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.SV_CoupledResource);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.SV_CoupledResource);
                if (!tempList.contains(elementName[i])) {
                    // element not mandatory
                    elementObligation[i] = false;
                }
            }
        }
    }

    public void createScopedName() {
        if (this.scopedName == null) {
            this.scopedName = new ArrayList<>();
        }
    }

    public void createResourceReference() {
        if (this.resourceReference == null) {
            this.resourceReference = new ArrayList<>();
        }
    }

    public void createResource() {
        if (this.resource == null) {
            this.resource = new ArrayList<>();
        }
    }

    public void createOperation() {
        if (this.operation == null) {
            this.operation = new ArrayList<>();
        }
    }

    public void addScopedName(String scopedName) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.scopedName.add(scopedName);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addResourceReference(CI_Citation resourceReference) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.resourceReference.add(resourceReference);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addResource(MD_DataIdentification resource) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.resource.add(resource);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOperation(SV_OperationMetadata operation) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.operation.add(operation);
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
