/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.ISO19157Schema;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "DQ_LogicalConsistency", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
public abstract class DQ_LogicalConsistency extends DQ_Element {

    // variables for correct marshalling of specified classes
    public List<DQ_ConceptualConsistency> conceptualConsistency;

    public List<DQ_DomainConsistency> domainConsistency;

    public List<DQ_FormatConsistency> formatConsistency;

    public List<DQ_TopologicalConsistency> topologicalConsistency;

}
