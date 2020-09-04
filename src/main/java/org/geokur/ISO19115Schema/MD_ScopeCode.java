/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_ScopeCode", namespace = "http://standards.iso.org/iso/19115/-3/mcc/1.0")
public class MD_ScopeCode {
    // codelist - can be extended

    public enum MD_ScopeCodes {
        attribute,
        attributeType,
        collectionHardware,
        collectionSession,
        dataset,
        series,
        nonGeographicDataset,
        dimensionGroup,
        feature,
        featureType,
        propertyType,
        fieldSession,
        software,
        service,
        model,
        tile,
        metadata,
        initiative,
        sample,
        document,
        repository,
        aggregate,
        product,
        collection,
        coverage,
        application;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mcc/1.0/codelists.html#MD_ScopeCode";

    @XmlAttribute
    public MD_ScopeCode.MD_ScopeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_ScopeCode(){}

    public MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_ScopeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
