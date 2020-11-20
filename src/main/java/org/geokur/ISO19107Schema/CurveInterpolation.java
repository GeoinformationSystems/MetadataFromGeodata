/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19107Schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CurveInterpolation")
public class CurveInterpolation {
    // codelist - can be extended

    public enum CurveInterpolations {
        compositecurve,
        productcurve,
        linear,
        geodesic,
        rhumb,
        circular,
        spiral,
        elliptical,
        conic,
        polynomialspline,
        bezierspline,
        bspline,
        nurbs,
        product,
        composite;
    }

    @XmlAttribute
    final String codeList = "";

    @XmlAttribute
    public CurveInterpolation.CurveInterpolations codeListValue;
    @XmlAttribute(name = "codeListValue")
    public String codeListValueNew;

    public CurveInterpolation(){}

    public CurveInterpolation(CurveInterpolation.CurveInterpolations codeListValue) {
        this.codeListValue = codeListValue;
    }

    public CurveInterpolation(String codeListValueNew) {
        this.codeListValueNew = codeListValueNew;
    }
}
