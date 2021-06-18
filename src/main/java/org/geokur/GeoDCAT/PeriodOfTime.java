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

public class PeriodOfTime {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.DCT + "PeriodOfTime");

    // mandatory properties

    // recommended properties
    public static final Property startDate = model.createProperty(NS.DCAT, "startDate");
    public static final Property endDate = model.createProperty(NS.DCAT, "endDate");

    // optional properties
    public static final Property beginning = model.createProperty(NS.TIME, "hasBeginning");
    public static final Property end = model.createProperty(NS.TIME, "hasEnd");
}
