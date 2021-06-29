/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19103Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class RecordEntry {
    @XmlAttribute(name = "name")
    String fieldName;

    @XmlValue
    String value;

    void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    void setValue(String value) {
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getValue() {
        return value;
    }
}
