/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19157Schema;

import org.geokur.ISO19115Schema.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "DQM_Measure", namespace = "http://standards.iso.org/iso/19157/-2/dqm/1.0")
public class DQM_Measure {

    // occurrence and obligation
    private final String[] elementName = {"measureIdentifier", "name", "alias", "elementNameElement", "definition", "description", "valueType", "valueStructure", "example", "basicMeasure", "sourceReference", "parameter"};
    private final String[] elementNameProfile = {"measureIdentifier", "name", "alias", "elementName", "definition", "description", "valueType", "valueStructure", "example", "basicMeasure", "sourceReference", "parameter"};  // true profile name of elements
    private final int[] elementMax = {1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, 1, 1, 1, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, true, false, true, true, false, true, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "measureIdentifier", namespace = "http://standards.iso.org/iso/19157/-2/dqm/1.0")
    @XmlElementRef
    public List<MD_Identifier> measureIdentifier;

    @XmlElement(name = "name", namespace = "http://standards.iso.org/iso/19157/-2/dqm/1.0")
    public List<String> name;

    @XmlElement(name = "alias", namespace = "http://standards.iso.org/iso/19157/-2/dqm/1.0")
    public List<String> alias;

    @XmlElement(name = "elementName", namespace = "http://standards.iso.org/iso/19157/-2/dqm/1.0")
    public List<String> elementNameElement;

    @XmlElement(name = "definition", namespace = "http://standards.iso.org/iso/19157/-2/dqm/1.0")
    public List<String> definition;

    @XmlElementWrapper(name = "description", namespace = "http://standards.iso.org/iso/19157/-2/dqm/1.0")
    @XmlElementRef
    public List<DQM_Description> description;

    @XmlElement(name = "valueType", namespace = "http://standards.iso.org/iso/19157/-2/dqm/1.0")
    public List<String> valueType;

    @XmlElement(name = "valueStructure", namespace = "http://standards.iso.org/iso/19157/-2/dqm/1.0")
    public List<DQM_ValueStructure> valueStructure;

    @XmlElementWrapper(name = "example", namespace = "http://standards.iso.org/iso/19157/-2/dqm/1.0")
    @XmlElementRef
    public List<DQM_Description> example;

    @XmlElementWrapper(name = "basicMeasure", namespace = "http://standards.iso.org/iso/19157/-2/dqm/1.0")
    @XmlElementRef
    public List<DQM_BasicMeasure> basicMeasure;

    @XmlElementWrapper(name = "sourceReference", namespace = "http://standards.iso.org/iso/19157/-2/dqm/1.0")
    @XmlElementRef
    public List<DQM_SourceReference> sourceReference;

    @XmlElementWrapper(name = "parameter", namespace = "http://standards.iso.org/iso/19157/-2/dqm/1.0")
    @XmlElementRef
    public List<DQM_Parameter> parameter;

    // methods
    public DQM_Measure(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.DQM_Measure);
                if (!tempList.contains(elementNameProfile[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.DQM_Measure);
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

    public void createMeasureIdentifier() {
        if (this.measureIdentifier == null) {
            this.measureIdentifier = new ArrayList<>();
        }
    }

    public void createName() {
        if (this.name == null) {
            this.name = new ArrayList<>();
        }
    }

    public void createAlias() {
        if (this.alias == null) {
            this.alias = new ArrayList<>();
        }
    }

    public void createElementName() {
        if (this.elementNameElement == null) {
            this.elementNameElement = new ArrayList<>();
        }
    }

    public void createDefinition() {
        if (this.definition == null) {
            this.definition = new ArrayList<>();
        }
    }

    public void createDescription() {
        if (this.description == null) {
            this.description = new ArrayList<>();
        }
    }

    public void createValueType() {
        if (this.valueType == null) {
            this.valueType = new ArrayList<>();
        }
    }

    public void createValueStructure() {
        if (this.valueStructure == null) {
            this.valueStructure = new ArrayList<>();
        }
    }

    public void createExample() {
        if (this.example == null) {
            this.example = new ArrayList<>();
        }
    }

    public void createBasicMeasure() {
        if (this.basicMeasure == null) {
            this.basicMeasure = new ArrayList<>();
        }
    }

    public void createSourceReference() {
        if (this.sourceReference == null) {
            this.sourceReference = new ArrayList<>();
        }
    }

    public void createParameter() {
        if (this.parameter == null) {
            this.parameter = new ArrayList<>();
        }
    }

    public void addMeasureIdentifier(MD_Identifier measureIdentifier) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.measureIdentifier.add(measureIdentifier);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addName(String name) {
        int elementNum = 1;
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

    public void addAlias(String alias) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.alias.add(alias);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addElementName(String elementNameElement) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.elementNameElement.add(elementNameElement);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDefinition(String definition) {
        int elementNum = 4;
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

    public void addDescription(DQM_Description description) {
        int elementNum = 5;
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

    public void addValueType(String valueType) {
        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.valueType.add(valueType);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addValueStructure(DQM_ValueStructure valueStructure) {
        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.valueStructure.add(valueStructure);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addExample(DQM_Description example) {
        int elementNum = 8;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.example.add(example);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addBasicMeasure(DQM_BasicMeasure basicMeasure) {
        int elementNum = 9;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.basicMeasure.add(basicMeasure);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSourceReference(DQM_SourceReference sourceReference) {
        int elementNum = 10;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.sourceReference.add(sourceReference);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addParameter(DQM_Parameter parameter) {
        int elementNum = 11;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.parameter.add(parameter);
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
