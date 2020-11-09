/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19115_2Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MI_OperationTypeCode", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
public class MI_OperationTypeCode {
    // codelist - can be extended

    public enum MI_OperationTypeCodes {
        real,
        simulated,
        synthesized;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mac/2.0";

    @XmlAttribute
    public MI_OperationTypeCode.MI_OperationTypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MI_OperationTypeCode(){}

    public MI_OperationTypeCode(MI_OperationTypeCode.MI_OperationTypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MI_OperationTypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
