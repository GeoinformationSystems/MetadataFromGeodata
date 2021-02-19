/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19108Schema;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "TM_Primitive", namespace = "http://www.opengis.net/gml/3.2")
public abstract class TM_Primitive extends TM_Object {

    // variables for correct marshalling of specified classes
    public List<TM_GeometricPrimitive> geometricPrimitive;
}
