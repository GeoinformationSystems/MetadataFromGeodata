/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SV_CouplingType", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
public class SV_CouplingType {
    // codelist - can be extended

    public enum SV_CouplingTypes {
        loose,
        mixed,
        tight;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/srv/2.0/codelists.html#SV_CouplingType";

    @XmlAttribute
    public SV_CouplingType.SV_CouplingTypes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public SV_CouplingType(){}

    public SV_CouplingType(SV_CouplingType.SV_CouplingTypes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public SV_CouplingType(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
