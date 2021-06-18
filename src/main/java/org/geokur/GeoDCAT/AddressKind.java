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

public class AddressKind {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.VCARD + "Address");

    // mandatory properties

    // recommended properties
    public static final Property administrativeArea = model.createProperty(NS.VCARD, "region");
    public static final Property city = model.createProperty(NS.VCARD, "locality");
    public static final Property country = model.createProperty(NS.VCARD, "country-name");
    public static final Property postalCode = model.createProperty(NS.VCARD, "postal-code");
    public static final Property streetAddress = model.createProperty(NS.VCARD, "street-address");

    // optional properties

}
