/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_GeometricObjectTypeCode", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
public class MD_GeometricObjectTypeCode {
    // codelist - can be extended

    public enum MD_GeometricObjectTypeCodes {
        complex,
        composite,
        curve,
        point,
        solid,
        surface;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/msr/2.0/codelists.html#MD_GeometricObjectTypeCode";

    @XmlAttribute
    public MD_GeometricObjectTypeCode.MD_GeometricObjectTypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_GeometricObjectTypeCode(){}

    public MD_GeometricObjectTypeCode(MD_GeometricObjectTypeCode.MD_GeometricObjectTypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_GeometricObjectTypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
