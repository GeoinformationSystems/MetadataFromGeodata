/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CI_DateTypeCode", namespace = "http://standards.iso.org/iso/19115/-3/cit/1.0")
public class CI_DateTypeCode {
    // codelist - can be extended

    public enum CI_DateTypeCodes {
        creation,
        publication,
        revision,
        expiry,
        lastUpdate,
        lastRevision,
        nextUpdate,
        unavailable,
        inForce,
        adopted,
        deprecated,
        superseded,
        validityBegins,
        validityExpires,
        released,
        distribution;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/cit/1.0/codelists.html#CI_DateTypeCode";

    @XmlAttribute
    public CI_DateTypeCode.CI_DateTypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public CI_DateTypeCode(){}

    public CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public CI_DateTypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
