/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19115_2Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MI_EventTypeCode", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
public class MI_EventTypeCode {
    // codelist - can be extended
    // TODO: namespace for this codelist not found yet

    public enum MI_EventTypeCodes {
        announcement,
        calibration,
        calibrationCoefficientUpdate,
        dataLoss,
        fatal,
        manoeuvre,
        missingData,
        notice,
        prelaunch,
        severe,
        switchOff,
        switchOn;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mac/2.0";

    @XmlAttribute
    public MI_EventTypeCode.MI_EventTypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MI_EventTypeCode(){}

    public MI_EventTypeCode(MI_EventTypeCode.MI_EventTypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MI_EventTypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
