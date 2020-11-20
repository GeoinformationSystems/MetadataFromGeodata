/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_DatatypeCode", namespace = "http://standards.iso.org/iso/19115/-3/mex/1.0")
public class MD_DatatypeCode {
    // codelist - can be extended

    public enum MD_DatatypeCodes {
        @XmlEnumValue("class") classCode,
        codelist,
        enumeration,
        codelistElement,
        abstractClass,
        aggregateClass,
        specifiedClass,
        datatypeClass,
        interfaceClass,
        unionClass,
        metaClass,
        typeClass,
        characterString,
        integer,
        association;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mex/1.0/codelists.html#MD_DatatypeCode";

    @XmlAttribute
    public MD_DatatypeCode.MD_DatatypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_DatatypeCode(){}

    public MD_DatatypeCode(MD_DatatypeCode.MD_DatatypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_DatatypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
