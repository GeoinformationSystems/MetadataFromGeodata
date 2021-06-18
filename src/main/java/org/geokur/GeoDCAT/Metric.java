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

public class Metric {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.DQV + "Metric");

    // mandatory properties

    // recommended properties
    public static final Property dimension = model.createProperty(NS.DQV, "inDimension");
    public static final Property expectedDataType = model.createProperty(NS.DQV, "expectedDataType");

    // optional properties

}
