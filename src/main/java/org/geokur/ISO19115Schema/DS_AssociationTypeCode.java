/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DS_AssociationTypeCode", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
public class DS_AssociationTypeCode {
    // codelist - can be extended

    public enum DS_AssociationTypeCodes {
        crossReference,
        largerWorkCitation,
        partOfSeamlessDatabase,
        stereoMate,
        isComposedOf,
        collectiveTitle,
        series,
        dependency,
        revisionOf;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mri/1.0/codelists.html#DS_AssociationTypeCode";

    @XmlAttribute
    public DS_AssociationTypeCode.DS_AssociationTypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public DS_AssociationTypeCode(){}

    public DS_AssociationTypeCode(DS_AssociationTypeCode.DS_AssociationTypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public DS_AssociationTypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
