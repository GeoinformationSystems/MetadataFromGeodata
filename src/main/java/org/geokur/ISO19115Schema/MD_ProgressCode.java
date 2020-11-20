/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_ProgressCode", namespace = "http://standards.iso.org/iso/19115/-3/mcc/1.0")
public class MD_ProgressCode {
    // codelist - can be extended

    public enum MD_ProgressCodes {
        completed,
        historicalArchive,
        obsolete,
        onGoing,
        planned,
        required,
        underDevelopment,
        @XmlEnumValue("final") finalCode,
        pending,
        retired,
        superseded,
        tentative,
        valid,
        accepted,
        notAccepted,
        withdrawn,
        proposed,
        deprecated;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mcc/1.0/codelists.html#MD_ProgressCode";

    @XmlAttribute
    public MD_ProgressCode.MD_ProgressCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_ProgressCode(){}

    public MD_ProgressCode(MD_ProgressCode.MD_ProgressCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_ProgressCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
