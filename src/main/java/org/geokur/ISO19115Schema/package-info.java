/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

@javax.xml.bind.annotation.XmlSchema(
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns ={@XmlNs(prefix="mdb", namespaceURI="http://standards.iso.org/iso/19115/-3/mdb/1.0"),
                @XmlNs(prefix="cat", namespaceURI="http://standards.iso.org/iso/19115/-3/cat/1.0"),
                @XmlNs(prefix="cit", namespaceURI="http://standards.iso.org/iso/19115/-3/cit/1.0"),
                @XmlNs(prefix="gco", namespaceURI="http://standards.iso.org/iso/19115/-3/gco/1.0"),
                @XmlNs(prefix="gcx", namespaceURI="http://standards.iso.org/iso/19115/-3/gcx/1.0"),
                @XmlNs(prefix="gex", namespaceURI="http://standards.iso.org/iso/19115/-3/gex/1.0"),
                @XmlNs(prefix="gmw", namespaceURI="http://standards.iso.org/iso/19115/-3/gmw/1.0"),
                @XmlNs(prefix="lan", namespaceURI="http://standards.iso.org/iso/19115/-3/lan/1.0"),
                @XmlNs(prefix="mac", namespaceURI="http://standards.iso.org/iso/19115/-3/mac/1.0"),
                @XmlNs(prefix="mas", namespaceURI="http://standards.iso.org/iso/19115/-3/mas/1.0"),
                @XmlNs(prefix="mcc", namespaceURI="http://standards.iso.org/iso/19115/-3/mcc/1.0"),
                @XmlNs(prefix="mco", namespaceURI="http://standards.iso.org/iso/19115/-3/mco/1.0"),
                @XmlNs(prefix="md1", namespaceURI="http://standards.iso.org/iso/19115/-3/md1/1.0"),
                @XmlNs(prefix="md2", namespaceURI="http://standards.iso.org/iso/19115/-3/md2/1.0"),
                @XmlNs(prefix="mda", namespaceURI="http://standards.iso.org/iso/19115/-3/mda/1.0"),
                @XmlNs(prefix="mds", namespaceURI="http://standards.iso.org/iso/19115/-3/mds/1.0"),
                @XmlNs(prefix="mdt", namespaceURI="http://standards.iso.org/iso/19115/-3/mdt/1.0"),
                @XmlNs(prefix="mex", namespaceURI="http://standards.iso.org/iso/19115/-3/mex/1.0"),
                @XmlNs(prefix="mmi", namespaceURI="http://standards.iso.org/iso/19115/-3/mmi/1.0"),
                @XmlNs(prefix="mpc", namespaceURI="http://standards.iso.org/iso/19115/-3/mpc/1.0"),
                @XmlNs(prefix="mrc", namespaceURI="http://standards.iso.org/iso/19115/-3/mrc/1.0"),
                @XmlNs(prefix="mrd", namespaceURI="http://standards.iso.org/iso/19115/-3/mrd/1.0"),
                @XmlNs(prefix="mri", namespaceURI="http://standards.iso.org/iso/19115/-3/mri/1.0"),
                @XmlNs(prefix="mrl", namespaceURI="http://standards.iso.org/iso/19115/-3/mrl/1.0"),
                @XmlNs(prefix="mrs", namespaceURI="http://standards.iso.org/iso/19115/-3/mrs/1.0"),
                @XmlNs(prefix="msr", namespaceURI="http://standards.iso.org/iso/19115/-3/msr/1.0"),
                @XmlNs(prefix="srv", namespaceURI="http://standards.iso.org/iso/19115/-3/srv/2.0"),
                @XmlNs(prefix="dqc", namespaceURI="http://standards.iso.org/iso/19157/-2/dqc/1.0"),
                @XmlNs(prefix="xs", namespaceURI="http://www.w3.org/2001/XMLSchema")
        })

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;

