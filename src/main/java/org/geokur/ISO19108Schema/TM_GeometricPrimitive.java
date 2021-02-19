/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19108Schema;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "TM_GeometricPrimitive", namespace = "http://www.opengis.net/gml/3.2")
public abstract class TM_GeometricPrimitive extends TM_Primitive {

    // variables for correct marshalling of specified classes
    public List<TM_Instant> instant;

    public List<TM_Period> period;
}
