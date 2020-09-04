/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MD_TopologyLevelCode", namespace = "http://standards.iso.org/iso/19115/-3/msr/1.0")
public class MD_TopologyLevelCode {
    // codelist - can be extended

    public enum MD_TopologyLevelCodes {
        geometryOnly,
        topology1D,
        planarGraph,
        fullPlanarGraph,
        surfaceGraph,
        fullSurfaceGraph,
        topology3D,
        fullTopology3D,
        @XmlEnumValue("abstract") abstractCode;
    }

    @XmlAttribute
    final String codeList = "http://standards.iso.org/iso/19115/-3/msr/1.0/codelists.html#MD_TopologyLevelCode";

    @XmlAttribute
    public MD_TopologyLevelCode.MD_TopologyLevelCodes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public MD_TopologyLevelCode(){}

    public MD_TopologyLevelCode(MD_TopologyLevelCode.MD_TopologyLevelCodes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public MD_TopologyLevelCode(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
