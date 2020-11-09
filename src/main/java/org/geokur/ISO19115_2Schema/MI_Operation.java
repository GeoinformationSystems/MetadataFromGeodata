/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19115_2Schema;

import org.geokur.ISO19103Schema.Record;
import org.geokur.ISO19103Schema.RecordType;
import org.geokur.ISO19115Schema.CI_Citation;
import org.geokur.ISO19115Schema.MD_Identifier;
import org.geokur.ISO19115Schema.MD_ProgressCode;
import org.geokur.ISO191xxProfile.MaximumOccurrenceException;
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

@XmlRootElement(name = "MI_Operation", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
public class MI_Operation {

    // occurrence and obligation
    private final String[] elementName = {"description", "citation", "identifier", "status", "type", "otherProperty", "otherPropertyType", "childOperation", "objective", "parentOperation", "plan", "platform", "significantEvent"};
    private final int[] elementMax = {1, 1, 1, 1, 1, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {false, false, false, true, false, false, false, false, false, true, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "description", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    public List<String> description;

    @XmlElementWrapper(name = "citation", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<CI_Citation> citation;

    @XmlElementWrapper(name = "identifier", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MD_Identifier> identifier;

    @XmlElementWrapper(name = "status", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MD_ProgressCode> status;

    @XmlElementWrapper(name = "type", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MI_OperationTypeCode> type;

    @XmlElement(name = "otherProperty", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    public List<Record> otherProperty;

    @XmlElement(name = "otherPropertyType", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    public List<RecordType> otherPropertyType;

    @XmlElementWrapper(name = "childOperation", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MI_Operation> childOperation;

    @XmlElementWrapper(name = "objective", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MI_Objective> objective;

    @XmlElementWrapper(name = "parentOperation", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MI_Operation> parentOperation;

    @XmlElementWrapper(name = "plan", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MI_Plan> plan;

    @XmlElementWrapper(name = "platform", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MI_Platform> platform;

    @XmlElementWrapper(name = "significantEvent", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
    @XmlElementRef
    public List<MI_Event> significantEvent;

    // methods
    public MI_Operation(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.MI_Operation);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.MI_Operation);
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

    public void addDescription(String description) {
        if (this.description == null) {
            this.description = new ArrayList<>();
        }

        int elementNum = 0;
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

    public void addCitation(CI_Citation citation) {
        if (this.citation == null) {
            this.citation = new ArrayList<>();
        }

        int elementNum = 1;
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

    public void addIdentifier(MD_Identifier identifier) {
        if (this.identifier == null) {
            this.identifier = new ArrayList<>();
        }

        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.identifier.add(identifier);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addStatus(MD_ProgressCode status) {
        if (this.status == null) {
            this.status = new ArrayList<>();
        }

        int elementNum = 3;
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

    public void addType(MI_OperationTypeCode type) {
        if (this.type == null) {
            this.type = new ArrayList<>();
        }

        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.type.add(type);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOtherProperty(Record otherProperty) {
        if (this.otherProperty == null) {
            this.otherProperty = new ArrayList<>();
        }

        int elementNum = 5;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.otherProperty.add(otherProperty);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addOtherPropertyType(RecordType otherPropertyType) {
        if (this.otherPropertyType == null) {
            this.otherPropertyType = new ArrayList<>();
        }

        int elementNum = 6;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.otherPropertyType.add(otherPropertyType);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addChildOperation(MI_Operation childOperation) {
        if (this.childOperation == null) {
            this.childOperation = new ArrayList<>();
        }

        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.childOperation.add(childOperation);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addObjective(MI_Objective objective) {
        if (this.objective == null) {
            this.objective = new ArrayList<>();
        }

        int elementNum = 8;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.objective.add(objective);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addParentOperation(MI_Operation parentOperation) {
        if (this.parentOperation == null) {
            this.parentOperation = new ArrayList<>();
        }

        int elementNum = 9;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.parentOperation.add(parentOperation);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPlan(MI_Plan plan) {
        if (this.plan == null) {
            this.plan = new ArrayList<>();
        }

        int elementNum = 10;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.plan.add(plan);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPlatform(MI_Platform platform) {
        if (this.platform == null) {
            this.platform = new ArrayList<>();
        }

        int elementNum = 11;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.platform.add(platform);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSignificantEvent(MI_Event significantEvent) {
        if (this.significantEvent == null) {
            this.significantEvent = new ArrayList<>();
        }

        int elementNum = 12;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.significantEvent.add(significantEvent);
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
