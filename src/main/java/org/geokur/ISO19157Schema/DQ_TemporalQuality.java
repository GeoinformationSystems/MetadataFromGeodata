/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO19157Schema;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "DQ_TemporalQuality", namespace = "http://standards.iso.org/iso/19157/-2/mdq/1.0")
public abstract class DQ_TemporalQuality extends DQ_Element {

    // variables for correct marshalling of specified classes
    public List<DQ_AccuracyOfATimeMeasurement> accuracyOfATimeMeasurement;

    public List<DQ_TemporalConsistency> temporalConsistency;

    public List<DQ_TemporalValidity> temporalValidity;
}
