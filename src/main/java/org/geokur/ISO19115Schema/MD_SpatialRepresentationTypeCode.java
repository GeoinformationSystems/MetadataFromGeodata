/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_SpatialRepresentationTypeCode", namespace = "http://standards.iso.org/iso/19115/-3/mcc/1.0")
public class MD_SpatialRepresentationTypeCode {
    // codelist - can be extended

    public enum MD_SpatialRepresentationTypeCodes {
        vector,
        grid,
        textTable,
        tin,
        stereoModel,
        video;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mcc/1.0/codelists.html#MD_SpatialRepresentationTypeCode";

    @XmlAttribute
    public MD_SpatialRepresentationTypeCode.MD_SpatialRepresentationTypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_SpatialRepresentationTypeCode(){}

    public MD_SpatialRepresentationTypeCode(MD_SpatialRepresentationTypeCode.MD_SpatialRepresentationTypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_SpatialRepresentationTypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
