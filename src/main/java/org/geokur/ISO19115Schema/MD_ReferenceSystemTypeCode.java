/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_ReferenceSystemTypeCode", namespace = "http://standards.iso.org/iso/19115/-3/mrs/1.0")
public class MD_ReferenceSystemTypeCode {
    // codelist - can be extended

    public enum MD_ReferenceSystemTypeCodes {
        compoundEngineeringParametric,
        compoundEngineeringParametricTemporal,
        compoundEngineeringTemporal,
        compoundEngineeringVertical,
        compoundEngineeringVerticalTemporal,
        compoundGeodeticParametric,
        compoundGeodeticParametricTemporal,
        compoundGeographic2DTemporal,
        compoundGeographic2DVertical,
        compoundGeographicVerticalTemporal,
        compoundGeographic3DTemporal,
        compoundProjected2DParametric,
        compoundProjected2DParametricTemporal,
        compoundProjectedTemporal,
        compoundProjectedVertical,
        compoundProjectedVerticalTemporal,
        engineering,
        engineeringDesign,
        engineeringImage,
        geodeticGeographic2D,
        geodeticGeographic3D,
        geographicIdentifier,
        linear,
        parametric,
        projected,
        temporal,
        vertical;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mrs/1.0/codelists.html#MD_ReferenceSystemTypeCode";

    @XmlAttribute
    public MD_ReferenceSystemTypeCode.MD_ReferenceSystemTypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_ReferenceSystemTypeCode(){}

    public MD_ReferenceSystemTypeCode(MD_ReferenceSystemTypeCode.MD_ReferenceSystemTypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_ReferenceSystemTypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
