/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "DS_Aggregate", namespace = "http://standards.iso.org/iso/19115/-3/mda/1.0")
public abstract class DS_Aggregate extends DS_Resource {

    // variables for correct marshalling of specified classes
    public List<DS_OtherAggregate> otherAggregate;

    public List<DS_Initiative> initiative;

    public List<DS_Series> series;
}
