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

public class Catalogue {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.DCAT + "Catalogue");

    // mandatory properties
    public static final Property dataset = model.createProperty(NS.DCAT, "dataset");
    public static final Property description = model.createProperty(NS.DCT, "description");
    public static final Property publisher = model.createProperty(NS.DCT, "publisher");
    public static final Property title = model.createProperty(NS.DCT, "title");

    // recommended properties
    public static final Property homepage = model.createProperty(NS.FOAF, "homepage");
    public static final Property language = model.createProperty(NS.DCT, "language");
    public static final Property licence = model.createProperty(NS.DCT, "license");
    public static final Property releaseDate = model.createProperty(NS.DCT, "issued");
    public static final Property spatialGeographic = model.createProperty(NS.DCT, "spatial");
    public static final Property themes = model.createProperty(NS.DCAT, "themeTaxonomy");
    public static final Property updateModificationDate = model.createProperty(NS.DCT, "modified");

    // optional properties
    public static final Property accessRights = model.createProperty(NS.DCT, "accessRights");
    public static final Property conformsTo = model.createProperty(NS.DCT, "conformsTo");
    public static final Property contactPoint = model.createProperty(NS.DCAT, "contactPoint");
    public static final Property catalogue = model.createProperty(NS.DCAT, "catalog");
    public static final Property creationDate = model.createProperty(NS.DCT, "created");
    public static final Property creator = model.createProperty(NS.DCT, "creator");
    public static final Property hasPart = model.createProperty(NS.DCT, "hasPart");
    public static final Property identifier = model.createProperty(NS.DCT, "identifier");
    public static final Property isPartOf = model.createProperty(NS.DCT, "isPartOf");
    public static final Property keywordTag = model.createProperty(NS.DCAT, "keyword");
    public static final Property qualifiedAttribution = model.createProperty(NS.PROV, "qualifiedAttribution");
    public static final Property record = model.createProperty(NS.DCAT, "record");
    public static final Property rights = model.createProperty(NS.DCT, "rights");
    public static final Property referenceSystem = model.createProperty(NS.DCT, "conformsTo");
    public static final Property rightsHolder = model.createProperty(NS.DCT, "rightsHolder");
    public static final Property temporalCoverage = model.createProperty(NS.DCT, "temporal");
    public static final Property service = model.createProperty(NS.DCAT, "service");
    public static final Property themeCategory = model.createProperty(NS.DCAT, "theme");
    public static final Property topicCategory = model.createProperty(NS.DCT, "subject");
    public static final Property wasUsedBy = model.createProperty(NS.PROV, "wasUsedBy");
    public static final Property custodian = model.createProperty(NS.GEODCAT, "custodian");
    public static final Property distributor = model.createProperty(NS.GEODCAT, "distributor");
    public static final Property originator = model.createProperty(NS.GEODCAT, "originator");
    public static final Property principalInvestogator = model.createProperty(NS.GEODCAT, "principalInvestogator");
    public static final Property processor = model.createProperty(NS.GEODCAT, "processor");
    public static final Property resourceProvider = model.createProperty(NS.GEODCAT, "resourceProvider");
    public static final Property user = model.createProperty(NS.GEODCAT, "user");
}
