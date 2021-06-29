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

public class Dataset {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.DCAT + "Dataset");

    // mandatory properties
    public static final Property description = model.createProperty(NS.DCT, "description");
    public static final Property title = model.createProperty(NS.DCT, "title");

    // recommended properties
    public static final Property contactPoint = model.createProperty(NS.DCAT, "contactPoint");
    public static final Property datasetDistribution = model.createProperty(NS.DCAT, "distribution");
    public static final Property keywordTag = model.createProperty(NS.DCAT, "keyword");
    public static final Property publisher = model.createProperty(NS.DCT, "publisher");
    public static final Property spatialGeographicCoverage = model.createProperty(NS.DCT, "spatial");
    public static final Property temporalCoverage = model.createProperty(NS.DCT, "temporal");
    public static final Property themeCategory = model.createProperty(NS.DCAT, "theme");

    // optional properties
    public static final Property accessRights = model.createProperty(NS.DCT, "accessRights");
    public static final Property creationDate = model.createProperty(NS.DCT, "created");
    public static final Property creator = model.createProperty(NS.DCT, "creator");
    public static final Property conformsTo = model.createProperty(NS.DCT, "conformsTo");
    public static final Property documentation = model.createProperty(NS.FOAF, "page");
    public static final Property frequency = model.createProperty(NS.DCT, "accrualPeriodicity");
    public static final Property hasVersion = model.createProperty(NS.DCT, "hasVersion");
    public static final Property identifier = model.createProperty(NS.DCT, "identifier");
    public static final Property isReferencedBy = model.createProperty(NS.DCT, "isReferencedBy");
    public static final Property isVersionOf = model.createProperty(NS.DCT, "isVersionOf");
    public static final Property landingPage = model.createProperty(NS.DCAT, "landingPage");
    public static final Property language = model.createProperty(NS.DCT, "language");
    public static final Property otherIdentifier = model.createProperty(NS.ADMS, "identifier");
    public static final Property provenance = model.createProperty(NS.DCT, "provenance");
    public static final Property qualifiedAttribution = model.createProperty(NS.PROV, "qualifiedAttribution");
    public static final Property qualifiedRelation = model.createProperty(NS.DCAT, "qualifiedRelation");
//    public static final Property relatedResource = model.createProperty(NS.DCT, "relation");
    // changed to meet the GeoKur metadata schema
    // dct:isPartOf is a subproperty of dct:relation -> still GeoDCAT compliant
    public static final Property relatedResource = model.createProperty(NS.DCT, "isPartOf");
    public static final Property releaseDate = model.createProperty(NS.DCT, "issued");
    public static final Property referenceSystem = model.createProperty(NS.DCT, "conformsTo");
    public static final Property rightsHolder = model.createProperty(NS.DCT, "rightsHolder");
    public static final Property sample = model.createProperty(NS.ADMS, "sample");
    public static final Property source = model.createProperty(NS.DCT, "source");
    public static final Property spatialResolution = model.createProperty(NS.DQV, "hasQualityMeasurement");
    public static final Property spatialResolutionInMeters = model.createProperty(NS.DCAT, "spatialResolutionInMeters");
    public static final Property spatialResolutionAsText = model.createProperty(NS.RDFS, "comment");
    public static final Property temporalResolution = model.createProperty(NS.DCAT, "temporalResolution");
    public static final Property topicCategory = model.createProperty(NS.DCT, "subject");
    public static final Property type = model.createProperty(NS.DCT, "type");
    public static final Property updateModificationDate = model.createProperty(NS.DCT, "modified");
    public static final Property version = model.createProperty(NS.OWL, "versionInfo");
    public static final Property versionNotes = model.createProperty(NS.ADMS, "versionNotes");
    public static final Property wasGeneratedBy = model.createProperty(NS.PROV, "wasGeneratedBy");
    public static final Property wasUsedBy = model.createProperty(NS.PROV, "wasUsedBy");
    public static final Property custodian = model.createProperty(NS.GEODCAT, "custodian");
    public static final Property distributor = model.createProperty(NS.GEODCAT, "distributor");
    public static final Property originator = model.createProperty(NS.GEODCAT, "originator");
    public static final Property principalInvestigator = model.createProperty(NS.GEODCAT, "principalInvestigator");
    public static final Property processor = model.createProperty(NS.GEODCAT, "processor");
    public static final Property resourceProvider = model.createProperty(NS.GEODCAT, "resourceProvider");
    public static final Property user = model.createProperty(NS.GEODCAT, "user");

    // additional properties from GeoKur profile
    public static final Property hasQualityAnnotation = model.createProperty(NS.DQV, "hasQualityAnnotation");
    public static final Property hasQualityMeasurement = model.createProperty(NS.DQV, "hasQualityMeasurement");
}
