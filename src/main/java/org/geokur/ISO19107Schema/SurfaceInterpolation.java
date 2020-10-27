/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19107Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SurfaceInterpolation")
public class SurfaceInterpolation {
    // codelist - can be extended

    public enum SurfaceInterpolations {
        none,
        planar,
        linear,
        spherical,
        triangular,
        parametricCurve,
        polynomialSpline,
        nurbs,
        bezierSpline,
        elliptical,
        conic,
        tin,
        triangulatedSpline;
    }

    @XmlAttribute
    final String codeList = "";

    @XmlAttribute
    public SurfaceInterpolation.SurfaceInterpolations codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public SurfaceInterpolation(){}

    public SurfaceInterpolation(SurfaceInterpolation.SurfaceInterpolations codeListValue) {
        this.codeListValue = codeListValue;
    }

    public SurfaceInterpolation(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
