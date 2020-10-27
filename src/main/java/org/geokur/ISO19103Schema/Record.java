/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19103Schema;

import org.geokur.ISO191xxProfile.InstantiationCallException;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import java.util.*;

@XmlRootElement(name = "Record")
public class Record {

    // class variables
    @XmlElement(name = "field")
    public List<RecordEntry> field;

    // methods
    public Record() {}

    public void addField(String value) {
        if (this.field == null) {
            this.field = new ArrayList<>();
        }

        RecordEntry recordEntry = new RecordEntry();
        recordEntry.setValue(value);
        this.field.add(recordEntry);
    }

    public void addField(String fieldName, String value) {
        if (this.field == null) {
            this.field = new ArrayList<>();
        }

        RecordEntry recordEntry = new RecordEntry();
        recordEntry.setFieldName(fieldName);
        recordEntry.setValue(value);
        this.field.add(recordEntry);
    }

    public void finalizeClass() {
        // only test, whether all variables exist and are filled
        try {
            if (this.field == null || this.field.size() == 0) {
                throw new InstantiationCallException("Record", "field");
            }
            for (RecordEntry recordEntry : this.field) {
                if (recordEntry.fieldName == null) {
                    throw new InstantiationCallException("field", "fieldName");
                }
                if (recordEntry.value == null) {
                    throw new InstantiationCallException("field", "value");
                }
            }
        } catch (InstantiationCallException e) {
            System.out.println(e.getMessage());
        }
    }


    /*
    // variant with JAXBElement
    // -> results in a list with dynamic key and value
    // class variables
    @XmlAnyElement
    public List<JAXBElement<String>> record;

    // methods
    public Record(){}

    public void createRecord() {
        if (this.record == null) {
            this.record = new ArrayList<>();
        }
    }

    public void addRecord(String key, String value) {
        JAXBElement<String> tmp = new JAXBElement<>(new QName(key), String.class, value);
        this.record.add(tmp);
    }
    */

    /*
    // variant with key as attribute and value (custom XmlAdapter)
    // -> results in a list with one key and dynamic key-attribute and value
    // class variables
    @XmlElement(name = "Record")
    @XmlJavaTypeAdapter(HashMapAdapter.class)
    public HashMap<String, String> record;

    // methods
    public Record(){}

    public void createName() {
        if (this.record == null) {
            this.record = new HashMap<>();
        }
    }

    public void addName(String key, String value) {
                this.record.put(key, value);
    }
    */
}

class RecordEntry {
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
}