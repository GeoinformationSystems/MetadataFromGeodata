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

public class Attribution {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.PROV + "Attribution");

    // mandatory properties
    public static final Property agent = model.createProperty(NS.PROV, "agent");
    public static final Property hadRole = model.createProperty(NS.DCAT, "hadRole");

    // recommended properties

    // optional properties

}
