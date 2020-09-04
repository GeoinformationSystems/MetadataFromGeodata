/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "DS_Resource", namespace = "http://standards.iso.org/iso/19115/-3/mda/1.0")
public abstract class DS_Resource {

    // occurrence and obligation
    private final String[] elementName = {"has", "partOf"};
    private final int[] elementMax = {Integer.MAX_VALUE, Integer.MAX_VALUE};

    private final String className = this.getClass().getSimpleName();

    // class variables
    @XmlElementWrapper(name = "has", namespace = "http://standards.iso.org/iso/19115/-3/mda/1.0")
    @XmlElementRef
    public List<MD_Metadata> has;

    @XmlElementWrapper(name = "partOf", namespace = "http://standards.iso.org/iso/19115/-3/mda/1.0")
    @XmlElementRef
    public List<DS_Aggregate> partOf;

    @XmlElementWrapper(name = "composedOf", namespace = "http://standards.iso.org/iso/19115/-3/mda/1.0")
    @XmlElementRef
    public List<DS_Resource> composedOf;

    // variables for correct marshalling of specified classes
    @XmlElementWrapper(name = "dataSet", namespace = "http://standards.iso.org/iso/19115/-3/mda/1.0")
    @XmlElementRef
    public List<DS_DataSet> dataSet;

    @XmlElementWrapper(name = "service", namespace = "http://standards.iso.org/iso/19115/-3/mda/1.0")
    @XmlElementRef
    public List<SV_Service> service;

    @XmlElementWrapper(name = "aggregate", namespace = "http://standards.iso.org/iso/19115/-3/mda/1.0")
    @XmlElementRef
    public List<DS_Aggregate> aggregate;

    @XmlElementWrapper(name = "otherAggregate", namespace = "http://standards.iso.org/iso/19115/-3/mda/1.0")
    @XmlElementRef
    public List<DS_OtherAggregate> otherAggregate;

    @XmlElementWrapper(name = "stereoMate", namespace = "http://standards.iso.org/iso/19115/-3/mda/1.0")
    @XmlElementRef
    public List<DS_StereoMate> stereoMate;

    @XmlElementWrapper(name = "initiative", namespace = "http://standards.iso.org/iso/19115/-3/mda/1.0")
    @XmlElementRef
    public List<DS_Initiative> initiative;

    @XmlElementWrapper(name = "series", namespace = "http://standards.iso.org/iso/19115/-3/mda/1.0")
    @XmlElementRef
    public List<DS_Series> series;

    @XmlElementWrapper(name = "platform", namespace = "http://standards.iso.org/iso/19115/-3/mda/1.0")
    @XmlElementRef
    public List<DS_Platform> platform;

    @XmlElementWrapper(name = "sensor", namespace = "http://standards.iso.org/iso/19115/-3/mda/1.0")
    @XmlElementRef
    public List<DS_Sensor> sensor;

    @XmlElementWrapper(name = "productionSeries", namespace = "http://standards.iso.org/iso/19115/-3/mda/1.0")
    @XmlElementRef
    public List<DS_ProductionSeries> productionSeries;

    // methods
    public void createHas() {
        if (this.has == null) {
            this.has = new ArrayList<>();
        }
    }

    public void createPartOf() {
        if (this.partOf == null) {
            this.partOf = new ArrayList<>();
        }
    }

    public void addHas(MD_Metadata has) {
        int elementNum = 0;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.has.add(has);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPartOf(DS_Aggregate partOf) {
        int elementNum = 1;
        try {
            List<?> tempList = (List<?>) this.getClass().getField(elementName[elementNum]).get(this);
            if (tempList.size() >= elementMax[elementNum]) {
                throw new MaximumOccurrenceException(className + " - " + elementName[elementNum], elementMax[elementNum]);
            } else {
                this.partOf.add(partOf);
            }
        } catch (MaximumOccurrenceException | NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
