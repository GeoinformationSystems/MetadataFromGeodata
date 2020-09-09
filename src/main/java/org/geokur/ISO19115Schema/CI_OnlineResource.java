/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

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

@XmlRootElement(name = "CI_OnlineResource", namespace = "http://standards.iso.org/iso/19115/-3/cit/1.0")
public class CI_OnlineResource {

    // occurrence and obligation
    private final String[] elementName = {"linkage", "protocol", "applicationProfile", "name", "description", "function", "protocolRequest"};
    private final int[] elementMax = {1, 1, 1, 1, 1, 1, 1};
    private final boolean[] elementObligation = {true, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "linkage", namespace = "http://standards.iso.org/iso/19115/-3/cit/1.0")
    public List<String> linkage;

    @XmlElement(name = "protocol", namespace = "http://standards.iso.org/iso/19115/-3/cit/1.0")
    public List<String> protocol;

    @XmlElement(name = "applicationProfile", namespace = "http://standards.iso.org/iso/19115/-3/cit/1.0")
    public List<String> applicationProfile;

    @XmlElement(name = "name", namespace = "http://standards.iso.org/iso/19115/-3/cit/1.0")
    public List<String> name;

    @XmlElement(name = "description", namespace = "http://standards.iso.org/iso/19115/-3/cit/1.0")
    public List<String> description;

    @XmlElementWrapper(name = "function", namespace = "http://standards.iso.org/iso/19115/-3/cit/1.0")
    @XmlElementRef
    public List<CI_OnLineFunctionCode> function;

    @XmlElement(name = "protocolRequest", namespace = "http://standards.iso.org/iso/19115/-3/cit/1.0")
    public List<String> protocolRequest;

    // methods
    public CI_OnlineResource(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.CI_OnlineResource);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.CI_OnlineResource);
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

    public void createLinkage() {
        if (this.linkage == null) {
            this.linkage = new ArrayList<>();
        }
    }

    public void createProtocol() {
        if (this.protocol == null) {
            this.protocol = new ArrayList<>();
        }
    }

    public void createApplicationProfile() {
        if (this.applicationProfile == null) {
            this.applicationProfile = new ArrayList<>();
        }
    }

    public void createName() {
        if (this.name == null) {
            this.name = new ArrayList<>();
        }
    }

    public void createDescription() {
        if (this.description == null) {
            this.description = new ArrayList<>();
        }
    }

    public void createFunction() {
        if (this.function == null) {
            this.function = new ArrayList<>();
        }
    }

    public void createProtocolRequest() {
        if (this.protocolRequest == null) {
            this.protocolRequest = new ArrayList<>();
        }
    }

    public void addLinkage(String linkage) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.linkage.add(linkage);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addProtocol(String protocol) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.protocol.add(protocol);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addApplicationProfile(String applicationProfile) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.applicationProfile.add(applicationProfile);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addName(String name) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.name.add(name);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDescription(String description) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.description.add(description);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addFunction(CI_OnLineFunctionCode function) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.function.add(function);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addProtocolRequest(String protocolRequest) {
        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.protocolRequest.add(protocolRequest);
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
