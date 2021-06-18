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

public class Relationship {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.DCAT + "Relationship");

    // mandatory properties
    public static final Property hadRole = model.createProperty(NS.DCAT, "hadRole");
    public static final Property relation = model.createProperty(NS.DCT, "relation");

    // recommended properties

    // optional properties

}
