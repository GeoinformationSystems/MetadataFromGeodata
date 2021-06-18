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

public class Distribution {
    private static final Model model = ModelFactory.createDefaultModel();

    // resource instance
    public static final Resource resourceInstance = model.createResource(NS.DCAT + "Distribution");

    // mandatory properties
    public static final Property accessURL = model.createProperty(NS.DCAT, "accessURL");

    // recommended properties
    public static final Property availability = model.createProperty(NS.DCATAP, "availability");
    public static final Property description = model.createProperty(NS.DCT, "description");
    public static final Property format = model.createProperty(NS.DCT, "format");
    public static final Property licence = model.createProperty(NS.DCT, "license");

    // optional properties
    public static final Property accessRights = model.createProperty(NS.DCT, "accessRights");
    public static final Property accessService = model.createProperty(NS.DCAT, "accessService");
    public static final Property byteSize = model.createProperty(NS.DCAT, "byteSize");
    public static final Property checksum = model.createProperty(NS.SPDX, "checksum");
    public static final Property compressionFormat = model.createProperty(NS.DCAT, "compressFormat");
    public static final Property documentation = model.createProperty(NS.FOAF, "page");
    public static final Property downloadURL = model.createProperty(NS.DCAT, "downloadURL");
    public static final Property hasPolicy = model.createProperty(NS.ODRL, "hasPolicy");
    public static final Property language = model.createProperty(NS.DCT, "language");
    public static final Property linkedSchemas = model.createProperty(NS.DCT, "conformsTo");
    public static final Property mediaType = model.createProperty(NS.DCAT, "mediaType");
    public static final Property packagingFormat = model.createProperty(NS.DCAT, "packageFormat");
    public static final Property referenceSystem = model.createProperty(NS.DCT, "conformsTo");
    public static final Property releaseDate = model.createProperty(NS.DCT, "issued");
    public static final Property representationTechnique = model.createProperty(NS.ADMS, "representationTechnique");
    public static final Property rights = model.createProperty(NS.DCT, "rights");
    public static final Property spatialResolution = model.createProperty(NS.DQV, "hasQualityMeasurement");
    public static final Property spatialResolutionInMetres = model.createProperty(NS.DCAT, "spatialResolutionInMeters");
    public static final Property spatialResolutionAsText = model.createProperty(NS.RDFS, "comment");
    public static final Property status = model.createProperty(NS.ADMS, "status");
    public static final Property temporalResolution = model.createProperty(NS.DCAT, "temporalResolution");
    public static final Property title = model.createProperty(NS.DCT, "title");
    public static final Property updateModificationDate = model.createProperty(NS.DCT, "modified");
}
