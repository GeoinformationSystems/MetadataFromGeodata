/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115_2Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MI_ContextCode", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
public class MI_ContextCode {
    // codelist - can be extended

    public enum MI_ContextCodes {
        acquisition,
        pass,
        wayPoint;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mac/2.0";

    @XmlAttribute
    public MI_ContextCode.MI_ContextCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MI_ContextCode(){}

    public MI_ContextCode(MI_ContextCode.MI_ContextCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MI_ContextCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
