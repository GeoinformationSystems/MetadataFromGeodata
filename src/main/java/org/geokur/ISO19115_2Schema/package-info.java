/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

@javax.xml.bind.annotation.XmlSchema(
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns ={@XmlNs(prefix="mac", namespaceURI="http://standards.iso.org/iso/19115/-3/mac/2.0"),
                @XmlNs(prefix="mrc", namespaceURI="http://standards.iso.org/iso/19115/-3/mrc/2.0"),
                @XmlNs(prefix="mrl", namespaceURI="http://standards.iso.org/iso/19115/-3/mrl/2.0"),
                @XmlNs(prefix="msr", namespaceURI="http://standards.iso.org/iso/19115/-3/msr/2.0")
        })

package org.geokur.ISO19115_2Schema;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;