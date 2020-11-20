/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_RestrictionCode", namespace = "http://standards.iso.org/iso/19115/-3/mco/1.0")
public class MD_RestrictionCode {
    // codelist - can be extended

    public enum MD_RestrictionCodes {
        copyright,
        patent,
        patentPending,
        trademark,
        licence,
        intellectualPropertyRights,
        restricted,
        otherRestrictions,
        unrestricted,
        licenceUnrestricted,
        licenceEndUser,
        licenceDistributor,
        @XmlEnumValue("private") privateCode,
        statutory,
        confidential,
        SBU,
        @XmlEnumValue("in-confidence") inconfidence;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mco/1.0/codelists.html#MD_RestrictionCode";

    @XmlAttribute
    public MD_RestrictionCode.MD_RestrictionCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_RestrictionCode(){}

    public MD_RestrictionCode(MD_RestrictionCode.MD_RestrictionCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_RestrictionCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
