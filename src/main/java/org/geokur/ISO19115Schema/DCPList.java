/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DCPList", namespace = "http://standards.iso.org/iso/19115/-3/srv/2.0")
public class DCPList {
    // codelist - can be extended

    public enum DCPLists {
        XML,
        CORBA,
        JAVA,
        COM,
        SQL,
        SOAP,
        Z3950,
        HTTP,
        FTP,
        WebServices;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/srv/2.0/codelists.html#DCPList";

    @XmlAttribute
    public DCPList.DCPLists codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public DCPList(){}

    public DCPList(DCPList.DCPLists codeListValue) {
        this.codeListValue = codeListValue;
    }

    public DCPList(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
