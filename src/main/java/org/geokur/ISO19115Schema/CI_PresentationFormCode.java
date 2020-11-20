/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CI_PresentationFormCode", namespace = "http://standards.iso.org/iso/19115/-3/cit/2.0")
public class CI_PresentationFormCode {
    // codelist - can be extended

    public enum CI_PresentationFormCodes {
        documentDigital,
        documentHardcopy,
        imageDigital,
        imageHardcopy,
        mapDigital,
        mapHardcopy,
        modelDigital,
        modelHardcopy,
        profileDigital,
        profileHardcopy,
        tableDigital,
        tableHardcopy,
        videoDigital,
        videoHardcopy,
        audioDigital,
        audioHardcopy,
        multimediaDigital,
        multimediaHardcopy,
        physicalSample,
        diagramDigital,
        diagramHardcopy;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/cit/2.0/codelists.html#CI_PresentationFormCode";

    @XmlAttribute
    public CI_PresentationFormCode.CI_PresentationFormCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public CI_PresentationFormCode(){}

    public CI_PresentationFormCode(CI_PresentationFormCode.CI_PresentationFormCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public CI_PresentationFormCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
