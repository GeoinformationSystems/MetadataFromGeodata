/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19111Schema;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "CRS")
public abstract class CRS extends ObjectUsage {

    // occurrence and obligation
    private final String[] elementName = {"name", "identifier", "alias", "remarks",
            "usage"};
    private final int[] elementMax = {1, Integer.MAX_VALUE, Integer.MAX_VALUE, 1,
            Integer.MAX_VALUE};
    private final boolean[] elementObligation = {true, false, false, false,
            false};

    private final String className = this.getClass().getSimpleName();

    // variables for correct marshalling of specified classes
    public List<SingleCRS> singleCRS;
}
