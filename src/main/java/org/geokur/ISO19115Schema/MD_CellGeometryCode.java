/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_CellGeometryCode", namespace = "http://standards.iso.org/iso/19115/-3/msr/2.0")
public class MD_CellGeometryCode {
    // codelist - can be extended

    public enum MD_CellGeometryCodes {
        point,
        area,
        voxel,
        stratum;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/msr/2.0/codelists.html#MD_CellGeometryCode";

    @XmlAttribute
    public MD_CellGeometryCode.MD_CellGeometryCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_CellGeometryCode(){}

    public MD_CellGeometryCode(MD_CellGeometryCode.MD_CellGeometryCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_CellGeometryCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
