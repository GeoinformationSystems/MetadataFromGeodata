/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19157Schema;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "DQ_DataEvaluation", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
public abstract class DQ_DataEvaluation extends DQ_EvaluationMethod {

    // variables for correct marshalling of specified classes
    public List<DQ_FullInspection> fullInspection;

    public List<DQ_IndirectEvaluation> indirectEvaluation;

    public List<DQ_SampleBasedInspection> sampleBasedInspection;
}
