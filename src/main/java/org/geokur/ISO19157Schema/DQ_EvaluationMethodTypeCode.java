/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19157Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DQ_EvaluationMethodTypeCode", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
public class DQ_EvaluationMethodTypeCode {
    // codelist - can be extended

    public enum DQ_EvaluationMethodTypeCodes {
        directInternal,
        directExternal,
        indirect;
    }

    @XmlAttribute
    final String codeList = "https://standards.iso.org/iso/19157/-2/mdq/1.0/codelists.html";

    @XmlAttribute
    public DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public DQ_EvaluationMethodTypeCode(){}

    public DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public DQ_EvaluationMethodTypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
