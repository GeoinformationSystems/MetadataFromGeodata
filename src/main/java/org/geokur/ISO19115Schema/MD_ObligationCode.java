/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_ObligationCode", namespace = "http://standards.iso.org/iso/19115/-3/mex/1.0")
public class MD_ObligationCode {
    // enumeration - cannot be extended

    public enum MD_ObligationCodes {
        mandatory,
        optional,
        conditional;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mex/1.0/codelists.html#MD_ObligationCode";

    @XmlAttribute
    public MD_ObligationCode.MD_ObligationCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_ObligationCode(){}

    public MD_ObligationCode(MD_ObligationCode.MD_ObligationCodes codeListValue) {
        this.codeListValue = codeListValue;
    }
}
