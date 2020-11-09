/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19115_2Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MI_ObjectiveTypeCode", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
public class MI_ObjectiveTypeCode {
    // codelist - can be extended

    public enum MI_ObjectiveTypeCodes {
        instantaneousCollection,
        persistentView,
        survey;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mac/2.0";

    @XmlAttribute
    public MI_ObjectiveTypeCode.MI_ObjectiveTypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MI_ObjectiveTypeCode(){}

    public MI_ObjectiveTypeCode(MI_ObjectiveTypeCode.MI_ObjectiveTypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MI_ObjectiveTypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
