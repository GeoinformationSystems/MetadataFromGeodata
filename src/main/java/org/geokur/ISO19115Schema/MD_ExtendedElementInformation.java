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

@XmlRootElement(name = "MD_ExtendedElementInformation", namespace = "http://standards.iso.org/iso/19115/-3/mex/1.0")
public class MD_ExtendedElementInformation {

    // occurrence and obligation
    private final String[] elementName = {"name", "definition", "obligation", "condition", "dataType", "maximumOccurence", "domainValue", "parentEntity", "rule", "rationale", "source", "conceptName", "code"};
    private final int[] elementMax = {1, 1, 1, 1, 1, 1, 1, Integer.MAX_VALUE, 1, 1, Integer.MAX_VALUE, 1, 1};
    private final boolean[] elementObligation = {false, true, false, false, true, false, false, true, true, false, true, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "name", namespace = "http://standards.iso.org/iso/19115/-3/mex/1.0")
    public List<String> name;

    @XmlElement(name = "definition", namespace = "http://standards.iso.org/iso/19115/-3/mex/1.0")
    public List<String> definition;

    @XmlElementWrapper(name = "obligation", namespace = "http://standards.iso.org/iso/19115/-3/mex/1.0")
    @XmlElementRef
    public List<MD_ObligationCode> obligation;

    @XmlElement(name = "condition", namespace = "http://standards.iso.org/iso/19115/-3/mex/1.0")
    public List<String> condition;

    @XmlElementWrapper(name = "dataType", namespace = "http://standards.iso.org/iso/19115/-3/mex/1.0")
    @XmlElementRef
    public List<MD_DatatypeCode> dataType;

    @XmlElement(name = "maximumOccurence", namespace = "http://standards.iso.org/iso/19115/-3/mex/1.0")
    public List<String> maximumOccurence;

    @XmlElement(name = "domainValue", namespace = "http://standards.iso.org/iso/19115/-3/mex/1.0")
    public List<String> domainValue;

    @XmlElement(name = "parentEntity", namespace = "http://standards.iso.org/iso/19115/-3/mex/1.0")
    public List<String> parentEntity;

    @XmlElement(name = "rule", namespace = "http://standards.iso.org/iso/19115/-3/mex/1.0")
    public List<String> rule;

    @XmlElement(name = "rationale", namespace = "http://standards.iso.org/iso/19115/-3/mex/1.0")
    public List<String> rationale;

    @XmlElementWrapper(name = "source", namespace = "http://standards.iso.org/iso/19115/-3/mex/1.0")
    @XmlElementRef
    public List<CI_Responsibility> source;

    @XmlElement(name = "conceptName", namespace = "http://standards.iso.org/iso/19115/-3/mex/1.0")
    public List<String> conceptName;

    @XmlElement(name = "code", namespace = "http://standards.iso.org/iso/19115/-3/mex/1.0")
    public List<String> code;

    // methods
    public MD_ExtendedElementInformation(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MD_ExtendedElementInformation);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MD_ExtendedElementInformation);
                if (!tempList.contains(elementName[i])) {
                    // element not mandatory
                    elementObligation[i] = false;
                }
            }
        }
    }

    public void createName() {
        if (this.name == null) {
            this.name = new ArrayList<>();
        }
    }

    public void createDefinition() {
        if (this.definition == null) {
            this.definition = new ArrayList<>();
        }
    }

    public void createObligation() {
        if (this.obligation == null) {
            this.obligation = new ArrayList<>();
        }
    }

    public void createCondition() {
        if (this.condition == null) {
            this.condition = new ArrayList<>();
        }
    }

    public void createDataType() {
        if (this.dataType == null) {
            this.dataType = new ArrayList<>();
        }
    }

    public void createMaximumOccurence() {
        if (this.maximumOccurence == null) {
            this.maximumOccurence = new ArrayList<>();
        }
    }

    public void createDomainValue() {
        if (this.domainValue == null) {
            this.domainValue = new ArrayList<>();
        }
    }

    public void createParentEntity() {
        if (this.parentEntity == null) {
            this.parentEntity = new ArrayList<>();
        }
    }

    public void createRule() {
        if (this.rule == null) {
            this.rule = new ArrayList<>();
        }
    }

    public void createRationale() {
        if (this.rationale == null) {
            this.rationale = new ArrayList<>();
        }
    }

    public void createSource() {
        if (this.source == null) {
            this.source = new ArrayList<>();
        }
    }

    public void createConceptName() {
        if (this.conceptName == null) {
            this.conceptName = new ArrayList<>();
        }
    }

    public void createCode() {
        if (this.code == null) {
            this.code = new ArrayList<>();
        }
    }

    public void addName(String name) {
        int elementNum = 0;
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

    public void addDefinition(String definition) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.definition.add(definition);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addObligation(MD_ObligationCode obligation) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.obligation.add(obligation);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCondition(String condition) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.condition.add(condition);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDataType(MD_DatatypeCode dataType) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.dataType.add(dataType);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMaximumOccurence(String maximumOccurence) {
        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.maximumOccurence.add(maximumOccurence);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDomainValue(String domainValue) {
        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.domainValue.add(domainValue);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addParentEntity(String parentEntity) {
        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.parentEntity.add(parentEntity);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addRule(String rule) {
        int elementNum = 8;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.rule.add(rule);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addRationale(String rationale) {
        int elementNum = 9;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.rationale.add(rationale);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSource(CI_Responsibility source) {
        int elementNum = 10;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.source.add(source);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addConceptName(String conceptName) {
        int elementNum = 11;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.conceptName.add(conceptName);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addCode(String code) {
        int elementNum = 12;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.code.add(code);
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
