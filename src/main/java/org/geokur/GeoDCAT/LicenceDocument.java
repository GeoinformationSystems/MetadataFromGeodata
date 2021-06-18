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

public class LicenceDocument {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.DCT + "LicenseDocument");

    // mandatory properties

    // recommended properties
    public static final Property licenceType = model.createProperty(NS.DCT, "type");

    // optional properties
    public static final Property licenceText = model.createProperty(NS.RDFS, "label");
}
