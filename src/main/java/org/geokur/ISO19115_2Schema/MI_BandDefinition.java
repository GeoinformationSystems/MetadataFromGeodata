/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115_2Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MI_BandDefinition", namespace = "http://standards.iso.org/iso/19115/-3/mrc/2.0")
public class MI_BandDefinition {
    // codelist - can be extended

    public enum MI_BandDefinitions {
        @XmlEnumValue("3dB") code3dB,
        halfMaximum,
        fiftyPercent,
        oneOverE,
        equivalentWidth;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mrc/2.0";

    @XmlAttribute
    public MI_BandDefinition.MI_BandDefinitions codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MI_BandDefinition(){}

    public MI_BandDefinition(MI_BandDefinition.MI_BandDefinitions codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MI_BandDefinition(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
