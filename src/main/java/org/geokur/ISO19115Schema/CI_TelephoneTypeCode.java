/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CI_TelephoneTypeCode", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
public class CI_TelephoneTypeCode {
    // codelist - can be extended

    public enum CI_TelephoneTypeCodes {
        voice,
        fax,
        sms;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/cit/2.0/codelists.html#CI_TelephoneTypeCode";

    @XmlAttribute
    public CI_TelephoneTypeCode.CI_TelephoneTypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public CI_TelephoneTypeCode(){}

    public CI_TelephoneTypeCode(CI_TelephoneTypeCode.CI_TelephoneTypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public CI_TelephoneTypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
