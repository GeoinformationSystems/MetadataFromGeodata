/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_CharacterSetCode")
public class MD_CharacterSetCode {
    // codelist - can be extended

    public enum MD_CharacterSetCodes {
        @XmlEnumValue("US-ASCII") US_ASCII,
        @XmlEnumValue("ISO-8859-1") ISO_8859_1,
        @XmlEnumValue("ISO-8859-2") ISO_8859_2,
        @XmlEnumValue("ISO-8859-3") ISO_8859_3,
        @XmlEnumValue("ISO-8859-4") ISO_8859_4,
        @XmlEnumValue("ISO-8859-5") ISO_8859_5,
        @XmlEnumValue("ISO-8859-6") ISO_8859_6,
        @XmlEnumValue("ISO-8859-7") ISO_8859_7,
        @XmlEnumValue("ISO-8859-8") ISO_8859_8,
        @XmlEnumValue("ISO-8859-9") ISO_8859_9,
        @XmlEnumValue("ISO-8859-10") ISO_8859_10,
        @XmlEnumValue("Shift_JIS") Shift_JIS,
        @XmlEnumValue("EUC-JP") EUC_JP,
        @XmlEnumValue("ISO-2022-KR") ISO_2022_KR,
        @XmlEnumValue("EUC-KR") EUC_KR,
        @XmlEnumValue("ISO-2022-JP") ISO_2022_JP,
        @XmlEnumValue("ISO-2022-JP-2") ISO_2022_JP_2,
        @XmlEnumValue("ISO-8859-6-E") ISO_8859_6_E,
        @XmlEnumValue("ISO-8859-6-I") ISO_8859_6_I,
        @XmlEnumValue("ISO-8859-8-E") ISO_8859_8_E,
        @XmlEnumValue("ISO-8859-8-I") ISO_8859_8_I,
        @XmlEnumValue("GB2312") GB2312,
        @XmlEnumValue("Big5") Big5,
        @XmlEnumValue("KOI8-R") KOI8_R
    }

    @XmlAttribute
    final String codeList = "https://www.iana.org/assignments/character-sets/character-sets.xhtml";

    @XmlAttribute
    public MD_CharacterSetCode.MD_CharacterSetCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_CharacterSetCode(){}

    public MD_CharacterSetCode(MD_CharacterSetCode.MD_CharacterSetCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_CharacterSetCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
