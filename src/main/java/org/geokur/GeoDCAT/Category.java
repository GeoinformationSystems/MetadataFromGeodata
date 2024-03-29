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

public class Category {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.SKOS + "Concept");

    // mandatory properties
    public static final Property preferredLabel = model.createProperty(NS.SKOS, "prefLabel");

    // recommended properties

    // optional properties
    public static final Property categoryScheme = model.createProperty(NS.SKOS, "inScheme");
}
