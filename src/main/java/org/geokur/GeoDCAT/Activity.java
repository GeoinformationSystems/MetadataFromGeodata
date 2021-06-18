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

public class Activity {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.PROV + "Activity");

    // mandatory properties
    public static final Property generated = model.createProperty(NS.PROV, "generated");
    public static final Property qualifiedAssociation = model.createProperty(NS.PROV, "qualifiedAssociation");
    public static final Property used = model.createProperty(NS.PROV, "used");

    // recommended properties

    // optional properties

}
