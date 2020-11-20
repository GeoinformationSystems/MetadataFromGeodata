/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115_2Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MI_GeometryTypeCode", namespace = "http://standards.iso.org/iso/19115/-3/mac/2.0")
public class MI_GeometryTypeCode {
    // codelist - can be extended

    public enum MI_GeometryTypeCodes {
        areal,
        linear,
        point,
        strip;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/mac/2.0";

    @XmlAttribute
    public MI_GeometryTypeCode.MI_GeometryTypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MI_GeometryTypeCode(){}

    public MI_GeometryTypeCode(MI_GeometryTypeCode.MI_GeometryTypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MI_GeometryTypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
