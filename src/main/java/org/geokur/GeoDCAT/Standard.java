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

public class Standard {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.DCT + "Standard");

    // mandatory properties

    // recommended properties
    public static final Property description = model.createProperty(NS.DCT, "description");
    public static final Property identifier = model.createProperty(NS.DCT, "identifier");
    public static final Property referenceRegister = model.createProperty(NS.SKOS, "inSCheme");
    public static final Property releaseDate = model.createProperty(NS.DCT, "issued");
    public static final Property title = model.createProperty(NS.DCT, "title");
    public static final Property type = model.createProperty(NS.DCT, "type");
    public static final Property version = model.createProperty(NS.OWL, "versionInfo");

    // optional properties
    public static final Property creationDate = model.createProperty(NS.DCT, "created");
    public static final Property updateModificationDate = model.createProperty(NS.DCT, "modified");
}
