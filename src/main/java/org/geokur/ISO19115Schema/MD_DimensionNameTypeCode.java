/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_DimensionNameTypeCode", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
public class MD_DimensionNameTypeCode {
    // codelist - can be extended

    public enum MD_DimensionNameTypeCodes {
        row,
        column,
        vertical,
        track,
        crossTrack,
        line,
        sample,
        time;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/msr/2.0/codelists.html#MD_DimensionNameTypeCode";

    @XmlAttribute
    public MD_DimensionNameTypeCode.MD_DimensionNameTypeCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_DimensionNameTypeCode(){}

    public MD_DimensionNameTypeCode(MD_DimensionNameTypeCode.MD_DimensionNameTypeCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_DimensionNameTypeCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
