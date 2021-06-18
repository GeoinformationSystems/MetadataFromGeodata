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

public class Checksum {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.SPDX + "Checksum");

    // mandatory properties
    public static final Property algorithm = model.createProperty(NS.SPDX, "algorithm");
    public static final Property checksumValue = model.createProperty(NS.SPDX, "checksumValue");

    // recommended properties

    // optional properties

}
