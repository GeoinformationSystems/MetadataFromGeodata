/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115_2Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MI_SequenceCode", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
public class MI_SequenceCode {
    // codelist - can be extended

    public enum MI_SequenceCodes {
        start,
        end,
        instantaneous;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mac/2.0";

    @XmlAttribute
    public MI_SequenceCode.MI_SequenceCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MI_SequenceCode(){}

    public MI_SequenceCode(MI_SequenceCode.MI_SequenceCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MI_SequenceCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
