/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

@javax.xml.bind.annotation.XmlSchema(
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns ={@XmlNs(prefix="mdq", namespaceURI="http://standards.iso.org/iso/19157/-2/mdq/1.0"),
                @XmlNs(prefix="dqm", namespaceURI="http://standards.iso.org/iso/19157/-2/dqm/1.0"),
                @XmlNs(prefix="xs", namespaceURI="http://www.w3.org/2001/XMLSchema")
        })

package org.geokur.ISO19157Schema;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;