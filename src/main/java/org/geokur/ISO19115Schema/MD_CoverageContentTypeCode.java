/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_CoverageContentTypeCode", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
public class MD_CoverageContentTypeCode {
    // codelist - can be extended

    public enum MD_CoverageContentTypeCodes {
        image,
        thematicClassification,
        physicalMeasurement,
        auxillaryInformation,
        qualityInformation,
        referenceInformation,
        modelResult,
        coordinate;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mrc/2.0/codelists.html#MD_CoverageContentTypeCode";

    @XmlAttribute
    public MD_CoverageContentTypeCode.MD_CoverageContentTypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_CoverageContentTypeCode(){}

    public MD_CoverageContentTypeCode(MD_CoverageContentTypeCode.MD_CoverageContentTypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_CoverageContentTypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
