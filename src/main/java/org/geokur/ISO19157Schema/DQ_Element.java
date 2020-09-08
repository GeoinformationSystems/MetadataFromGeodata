/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19157Schema;

import org.geokur.ISO19115Schema.MaximumOccurrenceException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "DQ_Element", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
public abstract class DQ_Element {

    // occurrence and obligation
    private final String[] elementName = {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"};
    private final int[] elementMax = {1, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE};
    private final boolean[] elementObligation = {false, false, false, true, false};

    private final String className = this.getClass().getSimpleName();
    private final boolean[] elementUsed = new boolean[elementName.length];

    // class variables
    @XmlElement(name = "standaloneQualityReportDetails", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
    public List<String> standaloneQualityReportDetails;

    @XmlElementWrapper(name = "measure", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
    @XmlElementRef
    public List<DQ_MeasureReference> measure;

    @XmlElementWrapper(name = "evaluationMethod", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
    @XmlElementRef
    public List<DQ_EvaluationMethod> evaluationMethod;

    @XmlElementWrapper(name = "result", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
    @XmlElementRef
    public List<DQ_Result> result;

    @XmlElementWrapper(name = "derivedElement", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
    @XmlElementRef
    public List<DQ_Element> derivedElement;

    // variables for correct marshalling of specified classes
    public List<DQ_Completeness> completeness;

    public List<DQ_LogicalConsistency> logicalConsistency;

    public List<DQ_PositionalAccuracy> positionalAccuracy;

    public List<DQ_TemporalQuality> temporalQuality;

    public List<DQ_ThematicAccuracy> thematicAccuracy;

    public List<DQ_UsabilityElement> usabilityElement;

    public List<DQ_Metaquality> metaquality;

    // methods
    public void createStandaloneQualityReportDetails() {
        if (this.standaloneQualityReportDetails == null) {
            this.standaloneQualityReportDetails = new ArrayList<>();
        }
    }

    public void createMeasure() {
        if (this.measure == null) {
            this.measure = new ArrayList<>();
        }
    }

    public void createEvaluationMethod() {
        if (this.evaluationMethod == null) {
            this.evaluationMethod = new ArrayList<>();
        }
    }

    public void createResult() {
        if (this.result == null) {
            this.result = new ArrayList<>();
        }
    }

    public void createDerivedElement() {
        if (this.derivedElement == null) {
            this.derivedElement = new ArrayList<>();
        }
    }

    public void addStandaloneQualityReportDetails(String standaloneQualityReportDetails) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.standaloneQualityReportDetails.add(standaloneQualityReportDetails);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addMeasure(DQ_MeasureReference measure) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.measure.add(measure);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addEvaluationMethod(DQ_EvaluationMethod evaluationMethod) {
        int elementNum = 2;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.evaluationMethod.add(evaluationMethod);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addResult(DQ_Result result) {
        int elementNum = 3;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.result.add(result);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addDerivedElement(DQ_Element derivedElement) {
        int elementNum = 4;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.derivedElement.add(derivedElement);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
