/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_ClassificationCode", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
public class MD_ClassificationCode {
    // codelist - can be extended

    public enum MD_ClassificationCodes {
        unclassified,
        restricted,
        confidential,
        secret,
        topSecret,
        SBU,
        forOfficialUseOnly,
        @XmlEnumValue("protected") protectedCode,
        limitedDistribution
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mco/1.0/codelists.html#MD_ClassificationCode";

    @XmlAttribute
    public MD_ClassificationCode.MD_ClassificationCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_ClassificationCode(){}

    public MD_ClassificationCode(MD_ClassificationCode.MD_ClassificationCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_ClassificationCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
