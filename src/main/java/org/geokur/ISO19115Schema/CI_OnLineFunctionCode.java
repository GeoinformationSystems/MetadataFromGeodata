/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CI_OnLineFunctionCode", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
public class CI_OnLineFunctionCode {
    // codelist - can be extended

    public enum CI_OnLineFunctionCodes {
        download,
        information,
        offlineAccess,
        order,
        search,
        completeMetadata,
        browseGraphic,
        upload,
        emailService,
        browsing,
        fileAccess;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/cit/2.0/codelists.html#CI_OnLineFunctionCode";

    @XmlAttribute
    public CI_OnLineFunctionCode.CI_OnLineFunctionCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public CI_OnLineFunctionCode(){}

    public CI_OnLineFunctionCode(CI_OnLineFunctionCode.CI_OnLineFunctionCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public CI_OnLineFunctionCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
