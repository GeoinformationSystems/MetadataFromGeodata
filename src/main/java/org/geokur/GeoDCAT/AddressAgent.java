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

public class AddressAgent {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.LOCN + "Address");

    // mandatory properties

    // recommended properties
    public static final Property administrativeArea = model.createProperty(NS.LOCN, "adminUnitL2");
    public static final Property city = model.createProperty(NS.LOCN, "postName");
    public static final Property country = model.createProperty(NS.LOCN, "adminUnitL1");
    public static final Property postalCode = model.createProperty(NS.LOCN, "postCode");
    public static final Property streetAddress = model.createProperty(NS.LOCN, "thoroughfare");

    // optional properties

}
