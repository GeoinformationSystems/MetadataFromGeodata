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

@XmlRootElement(name = "DQ_AggregationDerivation", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
public class DQ_AggregationDerivation {

    // occurrence and obligation
    private final String[] elementName = {"evaluationMethodType", "evaluationMethodDescription", "evaluationProcedure", "referenceDoc", "dateTime"};
    private final int[] elementMax = {1, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {false, false, false, false, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "evaluationMethodType", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
    public List<String> evaluationMethodType;

    @XmlElement(name = "evaluationMethodDescription", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
    public List<String> evaluationMethodDescription;

    @XmlElementWrapper(name = "evaluationProcedure", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
    @XmlElementRef
    public List<CI_Citation> evaluationProcedure;

    @XmlElementWrapper(name = "referenceDoc", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
    @XmlElementRef
    public List<CI_Citation> referenceDoc;

    @XmlElement(name = "dateTime", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
    public List<String> dateTime;

    // methods
    public DQ_AggregationDerivation(){
        for (int i = 0; i < elementName.length; i++) {
            elementUsed[i] = true;
        }

        // use profile (used elements and their obligation)
        if (ProfileReader.profile != null) {
            for (int i = 0; i < elementName.length; i++) {
                List<String> tempList = Arrays.asList(ProfileReader.profile.used.DQ_AggregationDerivation);
                if (!tempList.contains(elementName[i])) {
                    // element not used
                    elementUsed[i] = false;
                }
                tempList = Arrays.asList(ProfileReader.profile.obligation.DQ_AggregationDerivation);
                if (!tempList.contains(elementName[i])) {
                    // element not mandatory
                    elementObligation[i] = false;
                }
            }
        }
    }

    public void createEvaluationMethodType() {
        if (this.evaluationMethodType == null) {
            this.evaluationMethodType = new ArrayList<>();
        }
    }

    public void createEvaluationMethodDescription() {
        if (this.evaluationMethodDescription == null) {
            this.evaluationMethodDescription = new ArrayList<>();
        }
    }

    public void createEvaluationProcedure() {
        if (this.evaluationProcedure == null) {
            this.evaluationProcedure = new ArrayList<>();
        }
    }

    public void createReferenceDoc() {
        if (this.referenceDoc == null) {
            this.referenceDoc = new ArrayList<>();
        }
    }

    public void createDateTime() {
        if (this.dateTime == null) {
            this.dateTime = new ArrayList<>();
        }
    }

    public void addEvaluationMethodType(String evaluationMethodType) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.evaluationMethodType.add(evaluationMethodType);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addEvaluationMethodDescription(String evaluationMethodDescription) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.evaluationMethodDescription.add(evaluationMethodDescription);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addEvaluationProcedure(CI_Citation evaluationProcedure) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.evaluationProcedure.add(evaluationProcedure);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addReferenceDoc(CI_Citation referenceDoc) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.referenceDoc.add(referenceDoc);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDateTime(String dateTime) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.dateTime.add(dateTime);
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
