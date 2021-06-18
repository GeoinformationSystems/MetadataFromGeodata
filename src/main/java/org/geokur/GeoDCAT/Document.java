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

public class Document {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.FOAF + "Document");

    // mandatory properties

    // recommended properties
    public static final Property description = model.createProperty(NS.DCT, "description");
    public static final Property title = model.createProperty(NS.DCT, "title");

    // optional properties

}
