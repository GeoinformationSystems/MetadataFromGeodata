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

public class Kind {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.VCARD + "Kind");

    // mandatory properties

    // recommended properties
    public static final Property email = model.createProperty(NS.VCARD, "hasEmail");
    public static final Property name = model.createProperty(NS.VCARD, "fn");
    public static final Property url = model.createProperty(NS.VCARD, "hasURL");

    // optional properties
    public static final Property address = model.createProperty(NS.VCARD, "hasAddress");
    public static final Property affiliation = model.createProperty(NS.VCARD, "organizationName");
    public static final Property phone = model.createProperty(NS.VCARD, "hasTelephone");
}
