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

public class Location {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.DCT + "Location");

    // mandatory properties

    // recommended properties
    public static final Property boundingBox = model.createProperty(NS.DCAT, "bbox");
    public static final Property centroid = model.createProperty(NS.DCAT, "centroid");

    // optional properties
    public static final Property gazetteer = model.createProperty(NS.SKOS, "inScheme");
    public static final Property geographicIdentifier = model.createProperty(NS.DCT, "identifier");
    public static final Property geographicName = model.createProperty(NS.SKOS, "prefLabel");
    public static final Property geometry = model.createProperty(NS.LOCN, "geometry");
}
