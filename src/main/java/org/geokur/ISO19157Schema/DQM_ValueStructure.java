/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19157Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DQM_ValueStructure", namespace = "http://standards.iso.org/iso/19157/-2/dqm/1.0")
public class DQM_ValueStructure {
    // codelist - can be extended

    public enum DQM_ValueStructures {
        bag,
        set,
        sequence,
        table,
        matrix,
        coverage;
    }

    @XmlAttribute
    final String codeList = "https://standards.iso.org/iso/19157/-2/dqm/1.0/codelists.html";

    @XmlAttribute
    public DQM_ValueStructure.DQM_ValueStructures codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public DQM_ValueStructure(){}

    public DQM_ValueStructure(DQM_ValueStructure.DQM_ValueStructures codeListValue) {
        this.codeListValue = codeListValue;
    }

    public DQM_ValueStructure(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
