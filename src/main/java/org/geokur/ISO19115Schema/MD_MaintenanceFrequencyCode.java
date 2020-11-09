/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_MaintenanceFrequencyCode", namespace = "http://standards.iso.org/iso/19115/-3/mmi/1.0")
public class MD_MaintenanceFrequencyCode {
    // codelist - can be extended

    public enum MD_MaintenanceFrequencyCodes {
        continual,
        daily,
        weekly,
        fortnightly,
        monthly,
        quarterly,
        biannually,
        annually,
        asNeeded,
        irregular,
        notPlanned,
        unknown,
        periodic,
        semimonthly,
        biennially;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mmi/1.0/codelists.html#MD_MaintenanceFrequencyCode";

    @XmlAttribute
    public MD_MaintenanceFrequencyCode.MD_MaintenanceFrequencyCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_MaintenanceFrequencyCode(){}

    public MD_MaintenanceFrequencyCode(MD_MaintenanceFrequencyCode.MD_MaintenanceFrequencyCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_MaintenanceFrequencyCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
