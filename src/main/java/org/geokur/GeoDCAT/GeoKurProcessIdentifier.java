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

public class GeoKurProcessIdentifier extends Activity {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.PROV + "GeoKurProcessIdentifier");

    // mandatory properties

    // recommended properties

    // optional properties
    public static final Property hasGeospatialCategory = model.createProperty(NS.GEOKUR, "hasGeospatialCategory");
    public static final Property significance = model.createProperty(NS.GEOKUR, "hasSignificance");

}
