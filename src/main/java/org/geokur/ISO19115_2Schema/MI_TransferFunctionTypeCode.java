/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115_2Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MI_TransferFunctionTypeCode", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
public class MI_TransferFunctionTypeCode {
    // codelist - can be extended

    public enum MI_TransferFunctionTypeCodes {
        linear,
        logarithmic,
        exponential;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mrc/2.0";

    @XmlAttribute
    public MI_TransferFunctionTypeCode.MI_TransferFunctionTypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MI_TransferFunctionTypeCode(){}

    public MI_TransferFunctionTypeCode(MI_TransferFunctionTypeCode.MI_TransferFunctionTypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MI_TransferFunctionTypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
