/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_MediumFormatCode", namespace = "http://standards.iso.org/iso/19115/-3/mrd/1.0")
public class MD_MediumFormatCode {
    // codelist - can be extended

    public enum MD_MediumFormatCodes {
        cpio,
        tar,
        highSierra,
        iso9660,
        iso9660RockRidge,
        iso9660AppleHFS,
        udf;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mrd/1.0/codelists.html#MD_MediumFormatCode";

    @XmlAttribute
    public MD_MediumFormatCode.MD_MediumFormatCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_MediumFormatCode(){}

    public MD_MediumFormatCode(MD_MediumFormatCode.MD_MediumFormatCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_MediumFormatCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
