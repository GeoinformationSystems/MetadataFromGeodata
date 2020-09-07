/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "DQ_DataEvaluation", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
public abstract class DQ_DataEvaluation extends DQ_EvaluationMethod {

    // variables for correct marshalling of specified classes
    @XmlElementWrapper(name = "fullInspection", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
    @XmlElementRef
    public List<DQ_FullInspection> fullInspection;

    @XmlElementWrapper(name = "indirectEvaluation", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
    @XmlElementRef
    public List<DQ_IndirectEvaluation> indirectEvaluation;

    @XmlElementWrapper(name = "sampleBasedInspection", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
    @XmlElementRef
    public List<DQ_SampleBasedInspection> sampleBasedInspection;
}
