/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19107Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GeometryType")
public class GeometryType {
    // codelist - can be extended

    public enum GeometryTypes {
        empty,
        geometry,
        point,
        curve,
        surface,
        line,
        geodesic,
        polygon,
        collection,
        splinecurve,
        splinesurface,
        splinesolid,
        thumb;
    }

    @XmlAttribute
    public GeometryType.GeometryTypes codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public GeometryType(){}

    public GeometryType(GeometryType.GeometryTypes codeListValue) {
        this.codeListValue = codeListValue;
    }

    public GeometryType(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
