/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115_2Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MI_PolarisationOrientationCode", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
public class MI_PolarisationOrientationCode {
    // codelist - can be extended

    public enum MI_PolarisationOrientationCodes {
        horizontal,
        vertical,
        leftCircular,
        rightCircular,
        theta,
        phi;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mrc/2.0";

    @XmlAttribute
    public MI_PolarisationOrientationCode.MI_PolarisationOrientationCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MI_PolarisationOrientationCode(){}

    public MI_PolarisationOrientationCode(MI_PolarisationOrientationCode.MI_PolarisationOrientationCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MI_PolarisationOrientationCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
