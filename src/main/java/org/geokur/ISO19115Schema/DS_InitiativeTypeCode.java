/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DS_InitiativeTypeCode", namespace = "http://standards.iso.org/iso/19115/-3/mri/1.0")
public class DS_InitiativeTypeCode {
    // codelist - can be extended

    public enum DS_InitiativeTypeCodes {
        campaign,
        collection,
        exercise,
        experiment,
        investigation,
        mission,
        sensor,
        operation,
        platform,
        process,
        program,
        project,
        study,
        task,
        trial;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mri/1.0/codelists.html#DS_InitiativeTypeCode";

    @XmlAttribute
    public DS_InitiativeTypeCode.DS_InitiativeTypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public DS_InitiativeTypeCode(){}

    public DS_InitiativeTypeCode(DS_InitiativeTypeCode.DS_InitiativeTypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public DS_InitiativeTypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
