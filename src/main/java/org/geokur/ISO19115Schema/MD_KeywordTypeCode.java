/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_KeywordTypeCode", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
public class MD_KeywordTypeCode {
    // codelist - can be extended

    public enum MD_KeywordTypeCodes {
        discipline,
        place,
        stratum,
        temporal,
        theme,
        featureType,
        instrument,
        platform,
        process,
        project,
        service,
        product,
        subTopicCategory,
        taxon;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mri/1.0/codelists.html#MD_KeywordTypeCode";

    @XmlAttribute
    public MD_KeywordTypeCode.MD_KeywordTypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_KeywordTypeCode(){}

    public MD_KeywordTypeCode(MD_KeywordTypeCode.MD_KeywordTypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_KeywordTypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
