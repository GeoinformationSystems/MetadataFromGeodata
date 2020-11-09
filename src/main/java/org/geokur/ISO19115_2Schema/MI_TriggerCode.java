/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19115_2Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MI_TriggerCode", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
public class MI_TriggerCode {
    // codelist - can be extended

    public enum MI_TriggerCodes {
        automatic,
        manual,
        preProgrammed;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mac/2.0";

    @XmlAttribute
    public MI_TriggerCode.MI_TriggerCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MI_TriggerCode(){}

    public MI_TriggerCode(MI_TriggerCode.MI_TriggerCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MI_TriggerCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
