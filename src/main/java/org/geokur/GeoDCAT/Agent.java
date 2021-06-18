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

public class Agent {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.FOAF + "Agent");

    // mandatory properties
    public static final Property name = model.createProperty(NS.FOAF, "name");

    // recommended properties
    public static final Property type = model.createProperty(NS.DCT, "type");

    // optional properties
    public static final Property address = model.createProperty(NS.LOCN, "address");
    public static final Property affiliation = model.createProperty(NS.ORG, "memberOf");
    public static final Property email = model.createProperty(NS.FOAF, "mbox");
    public static final Property phone = model.createProperty(NS.FOAF, "phone");
    public static final Property url = model.createProperty(NS.FOAF, "workplaceHomepage");
}
