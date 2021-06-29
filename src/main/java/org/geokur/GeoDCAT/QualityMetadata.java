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

public class QualityMetadata {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.DQV + "QualityMetadata");

    // mandatory properties

    // recommended properties

    // optional properties
    public static final Property labelOfConfidenceValue = model.createProperty(NS.RDFS, "label");
    public static final Property value = model.createProperty(NS.DQV, "value");
    public static final Property thematicRepresentativity = model.createProperty(NS.GEOKUR, "hasThematicRepresentativity");
    public static final Property spatialRepresentativity = model.createProperty(NS.GEOKUR, "hasSpatialRepresentativity");
    public static final Property temporalRepresentativity = model.createProperty(NS.GEOKUR, "hasTemporalRepresentativity");
    public static final Property sourceName = model.createProperty(NS.GEOKUR, "hasSourceName");
    public static final Property sourceType = model.createProperty(NS.GEOKUR, "hasSourceType");
    public static final Property link = model.createProperty(NS.GEOKUR, "link");

}
