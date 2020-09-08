/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19157Schema;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "DQ_ThematicAccuracy", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
public abstract class DQ_ThematicAccuracy extends DQ_Element {

    // variables for correct marshalling of specified classes
    public List<DQ_ThematicClassificationCorrectness> thematicClassificationCorrectness;

    public List<DQ_NonQuantitativeAttributeCorrectness> nonQuantitativeAttributeCorrectness;

    public List<DQ_QuantitativeAttributeAccuracy> quantitativeAttributeAccuracy;
}
