/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.GeoDCAT;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class QualityMeasurement {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.DQV + "QualityMeasurement");

    // mandatory properties

    // recommended properties
    public static final Property isMeasurementOf = model.createProperty(NS.DQV, "isMeasurementOf");
    public static final Property unitOfMeasure = model.createProperty(NS.SDMX_ATTRIBUTE, "unitMeasure");
    public static final Property value = model.createProperty(NS.DQV, "value");

    // optional properties

}
