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

public class CategoryScheme {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.SKOS + "ConceptScheme");

    // mandatory properties
    public static final Property title = model.createProperty(NS.DCT, "title");

    // recommended properties

    // optional properties
    public static final Property creationDate = model.createProperty(NS.DCT, "created");
    public static final Property releaseDate = model.createProperty(NS.DCT, "issued");
    public static final Property updateModificationDate = model.createProperty(NS.DCT, "modified");
    public static final Property version = model.createProperty(NS.OWL, "versionInfo");
}
