/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115_2Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MI_PriorityCode", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
public class MI_PriorityCode {
    // codelist - can be extended

    public enum MI_PriorityCodes {
        critical,
        highImportance,
        mediumImportance,
        lowImportance;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mac/2.0";

    @XmlAttribute
    public MI_PriorityCode.MI_PriorityCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MI_PriorityCode(){}

    public MI_PriorityCode(MI_PriorityCode.MI_PriorityCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MI_PriorityCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
