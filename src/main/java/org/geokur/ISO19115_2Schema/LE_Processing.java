/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19115_2Schema;

import org.geokur.ISO19103Schema.Record;
import org.geokur.ISO19103Schema.RecordType;
import org.geokur.ISO19115Schema.CI_Citation;
import org.geokur.ISO19115Schema.MD_Identifier;
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

@XmlRootElement(name = "LE_Processing", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
public class LE_Processing {

    // occurrence and obligation
    private final String[] elementName = {"identifier", "softwareReference", "procedureDescription", "documentation", "runTimeParameters", "otherProperty", "otherPropertyType", "algorithm"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1, 1, 1, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, false, false, false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElementWrapper(name = "identifier", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    @XmlElementRef
    public List<MD_Identifier> identifier;

    @XmlElementWrapper(name = "softwareReference", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    @XmlElementRef
    public List<CI_Citation> softwareReference;

    @XmlElement(name = "procedureDescription", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    public List<String> procedureDescription;

    @XmlElementWrapper(name = "documentation", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    @XmlElementRef
    public List<CI_Citation> documentation;

    @XmlElement(name = "runTimeParameters", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    public List<String> runTimeParameters;

    @XmlElementWrapper(name = "otherProperty", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    @XmlElementRef
    public List<Record> otherProperty;

    @XmlElementWrapper(name = "otherPropertyType", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    @XmlElementRef
    public List<RecordType> otherPropertyType;

    @XmlElementWrapper(name = "algorithm", namespace = "http://standards.iso.org/iso/19115/-3/mrl/2.0")
    @XmlElementRef
    public List<LE_Algorithm> algorithm;

    // methods
    public LE_Processing(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.LE_Processing);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.LE_Processing);
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

    public void addIdentifier(MD_Identifier identifier) {
        if (this.identifier == null) {
            this.identifier = new ArrayList<>();
        }

        int elementNum = 0;
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

    public void addSoftwareReference(CI_Citation softwareReference) {
        if (this.softwareReference == null) {
            this.softwareReference = new ArrayList<>();
        }

        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.softwareReference.add(softwareReference);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addProcedureDescription(String procedureDescription) {
        if (this.procedureDescription == null) {
            this.procedureDescription = new ArrayList<>();
        }

        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.procedureDescription.add(procedureDescription);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDocumentation(CI_Citation documentation) {
        if (this.documentation == null) {
            this.documentation = new ArrayList<>();
        }

        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.documentation.add(documentation);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addRunTimeParameters(String runTimeParameters) {
        if (this.runTimeParameters == null) {
            this.runTimeParameters = new ArrayList<>();
        }

        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.runTimeParameters.add(runTimeParameters);
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

    public void addAlgorithm(LE_Algorithm algorithm) {
        if (this.algorithm == null) {
            this.algorithm = new ArrayList<>();
        }

        int elementNum = 7;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.algorithm.add(algorithm);
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
