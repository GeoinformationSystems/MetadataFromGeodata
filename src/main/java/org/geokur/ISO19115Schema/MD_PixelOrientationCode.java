/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_PixelOrientationCode", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
public class MD_PixelOrientationCode {
    // enumeration - cannot be extended

    public enum MD_PixelOrientationCodes {
        center,
        lowerLeft,
        lowerRight,
        upperRight,
        upperLeft;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/msr/2.0/codelists.html#MD_PixelOrientationCode";

    @XmlAttribute
    public MD_PixelOrientationCode.MD_PixelOrientationCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_PixelOrientationCode(){}

    public MD_PixelOrientationCode(MD_PixelOrientationCode.MD_PixelOrientationCodes codeListValue) {
        this.codeListValue = codeListValue;
    }
}
