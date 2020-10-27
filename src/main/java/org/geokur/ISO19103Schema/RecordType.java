/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19103Schema;

import org.geokur.ISO191xxProfile.InstantiationCallException;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "RecordType")
public class RecordType {

    // class variables
    @XmlElement(name = "field")
    public List<RecordTypeEntry> field;

    // methods
    public RecordType() {}

    public void addField(String fieldName, String fieldType) {
        if (this.field == null) {
            this.field = new ArrayList<>();
        }

        RecordTypeEntry recordTypeEntry = new RecordTypeEntry();
        recordTypeEntry.setFieldName(fieldName);
        recordTypeEntry.setFieldType(fieldType);
        this.field.add(recordTypeEntry);
    }

    public void finalizeClass() {
        // only test, whether all variables exist and are filled
        try {
            if (this.field == null || this.field.size() == 0) {
                throw new InstantiationCallException("Record", "field");
            }
            for (RecordTypeEntry recordTypeEntry : this.field) {
                if (recordTypeEntry.fieldName == null) {
                    throw new InstantiationCallException("field", "fieldName");
                }
                if (recordTypeEntry.fieldType == null) {
                    throw new InstantiationCallException("field", "fieldType");
                }
            }
        } catch (InstantiationCallException e) {
            System.out.println(e.getMessage());
        }
    }
}

class RecordTypeEntry {
    @XmlAttribute(name = "name")
    String fieldName;

    @XmlValue
    String fieldType;

    void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
}