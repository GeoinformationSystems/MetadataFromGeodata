/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115_2Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LE_ParameterDirectionCode", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
public class LE_ParameterDirectionCode {
    // codelist - can be extended
    // TODO: namespace for this codelist not found yet

    public enum LE_ParameterDirectionCodes {
        in,
        out,
        @XmlEnumValue("in/out") inout;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mac/2.0";

    @XmlAttribute
    public LE_ParameterDirectionCode.LE_ParameterDirectionCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public LE_ParameterDirectionCode(){}

    public LE_ParameterDirectionCode(LE_ParameterDirectionCode.LE_ParameterDirectionCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public LE_ParameterDirectionCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
