/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SV_ParameterDirection", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
public class SV_ParameterDirection {
    // enumeration - cannot be extended

    public enum SV_ParameterDirections {
        in,
        out,
        @XmlEnumValue("in/out") inout;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/srv/2.0/codelists.html#SV_ParameterDirection";

    @XmlAttribute
    public SV_ParameterDirection.SV_ParameterDirections codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public SV_ParameterDirection(){}

    public SV_ParameterDirection(SV_ParameterDirection.SV_ParameterDirections codeListValue) {
        this.codeListValue = codeListValue;
    }
}
