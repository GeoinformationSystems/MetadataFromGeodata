/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_ImagingConditionCode", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
public class MD_ImagingConditionCode {
    // codelist - can be extended

    public enum MD_ImagingConditionCodes {
        blurredImage,
        cloud,
        degradingObliquity,
        fog,
        heavySmokeOrDust,
        night,
        rain,
        semiDarkness,
        shadow,
        snow,
        terrainMasking;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mrc/2.0/codelists.html#MD_ImagingConditionCode";

    @XmlAttribute
    public MD_ImagingConditionCode.MD_ImagingConditionCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_ImagingConditionCode(){}

    public MD_ImagingConditionCode(MD_ImagingConditionCode.MD_ImagingConditionCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_ImagingConditionCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
