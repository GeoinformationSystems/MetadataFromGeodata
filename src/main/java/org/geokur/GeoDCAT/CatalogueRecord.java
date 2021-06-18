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

public class CatalogueRecord {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.DCAT + "CatalogueRecord");

    // mandatory properties
    public static final Property primaryTopic = model.createProperty(NS.FOAF, "primaryTopic");
    public static final Property updateModificationDate = model.createProperty(NS.DCT, "modified");

    // recommended properties
    public static final Property applicationProfile = model.createProperty(NS.DCT, "conformsTo");
    public static final Property changeType = model.createProperty(NS.ADMS, "status");
    public static final Property listingDate = model.createProperty(NS.DCT, "issued");

    // optional properties
    public static final Property characterEncoding = model.createProperty(NS.CNT, "characterEncoding");
    public static final Property contactPoint = model.createProperty(NS.DCAT, "contactPoint");
    public static final Property creationDate = model.createProperty(NS.DCT, "created");
    public static final Property creator = model.createProperty(NS.DCT, "creator");
    public static final Property description = model.createProperty(NS.DCT, "description");
    public static final Property identifier = model.createProperty(NS.DCT, "identifier");
    public static final Property language = model.createProperty(NS.DCT, "language");
    public static final Property publisher = model.createProperty(NS.DCT, "publisher");
    public static final Property qualifiedAttribution = model.createProperty(NS.PROV, "qualifiedAttribution");
    public static final Property rightsHolder = model.createProperty(NS.DCT, "rightsHolder");
    public static final Property sourceMetadata = model.createProperty(NS.DCT, "source");
    public static final Property title = model.createProperty(NS.DCT, "title");
    public static final Property custodian = model.createProperty(NS.GEODCAT, "custodian");
    public static final Property distributor = model.createProperty(NS.GEODCAT, "distributor");
    public static final Property originator = model.createProperty(NS.GEODCAT, "originator");
    public static final Property principalInvestigator = model.createProperty(NS.GEODCAT, "principalInvestigator");
    public static final Property processor = model.createProperty(NS.GEODCAT, "processor");
    public static final Property resourceProvider = model.createProperty(NS.GEODCAT, "resourceProvider");
    public static final Property user = model.createProperty(NS.GEODCAT, "user");
}
