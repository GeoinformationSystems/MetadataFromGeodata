/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.GeoDCAT;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD4;
import org.geokur.ISO19103Schema.Record;
import org.geokur.ISO19103Schema.RecordEntry;
import org.geokur.ISO19108Schema.TM_Instant;
import org.geokur.ISO19108Schema.TM_Period;
import org.geokur.ISO19115Schema.*;
import org.geokur.ISO19157Schema.*;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.ArrayList;
import java.util.List;

public class MappedMetadata {
    Model model;
    String fileType;
    String rdfLinkBase;

    public MappedMetadata(Model model, String fileType, String rdfLinkBase) {
        this.model = model;
        this.fileType = fileType;
        this.rdfLinkBase = rdfLinkBase;
    }

    public Model fillModel(DS_Resource dsResource) {
        // map metadata from ISO schema to GeoDCAT rdf

        if (fileType.equalsIgnoreCase("SHP") || fileType.equalsIgnoreCase("TIFF")) {
            // simple standard for shapefiles and geotiff

            MD_Metadata metadataAct = dsResource.has.get(0);

            String datasetID = metadataAct.metadataIdentifier.get(0).code.get(0);
            Resource dataset = model.createResource(rdfLinkBase + datasetID)
                    .addProperty(RDF.type, Dataset.resourceInstance);

            fillDatasetTitle(dataset, mapDatasetTitle(metadataAct));
            fillDatasetDescription(dataset, mapDatasetDescription(metadataAct));
            fillDatasetContactPoint(dataset, mapDatasetContactPoint(metadataAct));
            fillDatasetDistribution(dataset, mapDatasetDistribution(metadataAct));
            fillDatasetKeywordTag(dataset, mapDatasetKeywordTag(metadataAct, 0));
            fillDatasetSpatialGeographicCoverage(dataset, mapDatasetSpatialGeographicCoverage(metadataAct));
            fillDatasetTemporalCoverage(dataset, mapDatasetTemporalCoverage(metadataAct, 0));
            fillDatasetThemeCategory(dataset, mapDatasetThemeCategory(metadataAct));
            fillDatasetCreationDate(dataset, mapDatasetCreationDate(metadataAct));
            fillDatasetDocumentation(dataset, mapDatasetDocumentation(metadataAct));
            fillDatasetIdentifier(dataset, mapDatasetIdentifier(metadataAct));
            fillDatasetIsVersionOf(dataset, mapDatasetIsVersionOf(metadataAct));
            fillDatasetLandingPage(dataset, mapDatasetLandingPage(metadataAct));
            fillDatasetOtherIdentifier(dataset, mapDatasetOtherIdentifier(metadataAct));
            fillDatasetRelatedResource(dataset, mapDatasetRelatedResource(dsResource.partOf, rdfLinkBase));
            fillDatasetReferenceSystem(dataset, mapDatasetReferenceSystem(metadataAct));
            fillDatasetSpatialResolution(dataset, metadataAct);
            fillDatasetSpatialResolutionAsEquivalentScale(dataset, mapDatasetSpatialResolutionAsEquivalentScale(metadataAct));
            fillDatasetTemporalResolution(dataset, mapDatasetTemporalResolution(metadataAct, 0));
            fillDatasetWasGeneratedBy(dataset, mapDatasetWasGeneratedBy(metadataAct));
        }
        else if (fileType.equalsIgnoreCase("GPKG")) {
            // modelling of gpkg with one superior dataset and subdatasets for each layer

            String fileName = dsResource.has.get(0).identificationInfo.get(0).citation.get(0).title.get(0); // filename
            Resource dataset = model.createResource(fileName)
                    .addProperty(RDF.type, Dataset.resourceInstance);

            for (MD_Metadata metadataAct : dsResource.has) {
                // loop over all datasets in metadata

                Resource subDataset = model.createResource(getLayerName(metadataAct, true))
                        .addProperty(RDF.type, Dataset.resourceInstance)
                        .addProperty(Dataset.relatedResource, dataset);

                fillDatasetTitle(subDataset, getLayerName(metadataAct, false)); // title of subdataset as layer name
                fillDatasetDescription(subDataset, mapDatasetDescription(metadataAct));
                fillDatasetContactPoint(subDataset, mapDatasetContactPoint(metadataAct));
                fillDatasetDistribution(subDataset, mapDatasetDistribution(metadataAct));
                fillDatasetKeywordTag(subDataset, mapDatasetKeywordTag(metadataAct, 0));
                fillDatasetSpatialGeographicCoverage(subDataset, mapDatasetSpatialGeographicCoverage(metadataAct));
                fillDatasetTemporalCoverage(subDataset, mapDatasetTemporalCoverage(metadataAct, 0));
                fillDatasetThemeCategory(subDataset, mapDatasetThemeCategory(metadataAct));
                fillDatasetCreationDate(subDataset, mapDatasetCreationDate(metadataAct));
                fillDatasetDocumentation(subDataset, mapDatasetDocumentation(metadataAct));
                fillDatasetIdentifier(subDataset, mapDatasetIdentifier(metadataAct));
                fillDatasetIsVersionOf(subDataset, mapDatasetIsVersionOf(metadataAct));
                fillDatasetLandingPage(subDataset, mapDatasetLandingPage(metadataAct));
                fillDatasetOtherIdentifier(subDataset, mapDatasetOtherIdentifier(metadataAct));
                fillDatasetRelatedResource(subDataset, mapDatasetRelatedResource(dsResource.partOf, rdfLinkBase));
                fillDatasetReferenceSystem(subDataset, mapDatasetReferenceSystem(metadataAct));
                fillDatasetSpatialResolution(subDataset, metadataAct);
                fillDatasetSpatialResolutionAsEquivalentScale(subDataset, mapDatasetSpatialResolutionAsEquivalentScale(metadataAct));
                fillDatasetTemporalResolution(subDataset, mapDatasetTemporalResolution(metadataAct, 0));
                fillDatasetWasGeneratedBy(subDataset, mapDatasetWasGeneratedBy(metadataAct));
            }
        }
        else if (fileType.equalsIgnoreCase("CSV")) {
            // modelling of tabular data with the general file as superior dataset und subdatasets for each layer in
            // corresponding geopackage file and subsubdatasets for each content column

            String fileName = dsResource.has.get(0).identificationInfo.get(0).citation.get(0).title.get(0); // filename
            Resource dataset = model.createResource(fileName)
                    .addProperty(RDF.type, Dataset.resourceInstance);

            int ct = -1;
            for (MD_Metadata metadataAct : dsResource.has) {
                // loop over all datasets in metadata

                ct++;
                Resource subDataset = model.createResource(getLayerName(metadataAct, true))
                        .addProperty(RDF.type, Dataset.resourceInstance)
                        .addProperty(Dataset.relatedResource, dataset);

                fillDatasetTitle(subDataset, getLayerName(metadataAct, false)); // title of subdataset as layer name
                fillDatasetDescription(subDataset, mapDatasetDescription(metadataAct));
                fillDatasetContactPoint(subDataset, mapDatasetContactPoint(metadataAct));
                fillDatasetDistribution(subDataset, mapDatasetDistribution(metadataAct));
                fillDatasetSpatialGeographicCoverage(subDataset, mapDatasetSpatialGeographicCoverage(metadataAct));
                fillDatasetThemeCategory(subDataset, mapDatasetThemeCategory(metadataAct));
                fillDatasetCreationDate(subDataset, mapDatasetCreationDate(metadataAct));
                fillDatasetDocumentation(subDataset, mapDatasetDocumentation(metadataAct));
                fillDatasetIdentifier(subDataset, mapDatasetIdentifier(metadataAct));
                fillDatasetIsVersionOf(subDataset, mapDatasetIsVersionOf(metadataAct));
                fillDatasetLandingPage(subDataset, mapDatasetLandingPage(metadataAct));
                fillDatasetOtherIdentifier(subDataset, mapDatasetOtherIdentifier(metadataAct));
                fillDatasetRelatedResource(subDataset, mapDatasetRelatedResource(dsResource.partOf, rdfLinkBase));
                fillDatasetReferenceSystem(subDataset, mapDatasetReferenceSystem(metadataAct));
                fillDatasetSpatialResolution(subDataset, metadataAct);
                fillDatasetSpatialResolutionAsEquivalentScale(subDataset, mapDatasetSpatialResolutionAsEquivalentScale(metadataAct));
                fillDatasetWasGeneratedBy(subDataset, mapDatasetWasGeneratedBy(metadataAct));
                fillDatasetHasQualityMeasurement(subDataset, mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                        null, null, "polygons per area",
                        NS.GEOQUAL + "representativityAsPolygonsPerArea"));

                // get all content column titles for subsubdatasets
                List<String> contentColumnNames = getContentColumnNames(metadataAct, true);
                List<String> contentColumnNamesWoDescription = getContentColumnNames(metadataAct, false);
                for (int i = 0; i < contentColumnNames.size(); i++) {
                    Resource subsubDataset = model.createResource(contentColumnNames.get(i) + " " + ct)
                            .addProperty(RDF.type, Dataset.resourceInstance)
                            .addProperty(Dataset.relatedResource, subDataset);
                    fillDatasetKeywordTag(subsubDataset, mapDatasetKeywordTag(metadataAct, i));
                    fillDatasetTemporalCoverage(subsubDataset, mapDatasetTemporalCoverage(metadataAct, i));
                    fillDatasetTemporalResolution(subsubDataset, mapDatasetTemporalResolution(metadataAct, i));

                    List<Resource> qualityMeasurements = new ArrayList<>();
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_CompletenessOmission.class,
                            contentColumnNamesWoDescription.get(i), null, "number of missing items",
                            NS.GEOQUAL + "completenessOmissionAsNumberOfMissingItems"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_CompletenessOmission.class,
                            contentColumnNamesWoDescription.get(i), null, "rate of missing items",
                            NS.GEOQUAL + "completenessOmissionAsRateOfMissingItems"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_CompletenessCommission.class,
                            contentColumnNamesWoDescription.get(i), null, "number of excess items",
                            NS.GEOQUAL + "completenessCommissionAsNumberOfExcessItems"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_CompletenessCommission.class,
                            contentColumnNamesWoDescription.get(i), null, "rate of excess items",
                            NS.GEOQUAL + "completenessCommissionAsRateOfExcessItems"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_NonQuantitativeAttributeCorrectness.class,
                            contentColumnNamesWoDescription.get(i), null, "number of incorrect attribute values",
                            NS.GEOQUAL + "nonQuantitativeAttributeCorrectnessAsNumberOfIncorrectAttributeValues"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_NonQuantitativeAttributeCorrectness.class,
                            contentColumnNamesWoDescription.get(i), null, "rate of incorrect attribute values",
                            NS.GEOQUAL + "nonQuantitativeAttributeCorrectnessAsRateOfIncorrectAttributeValues"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), null, "number of different spatial elements",
                            NS.GEOQUAL + "representativityAsNumberOfDifferentSpatialElements"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), null, "number of different temporal elements",
                            NS.GEOQUAL + "representativityAsNumberOfDifferentTemporalElements"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), null, "number of different thematic elements",
                            NS.GEOQUAL + "representativityAsNumberOfDifferentThematicElements"));

                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "mean", "distribution parameters of different temporal elements per geographical unit",
                            NS.GEOQUAL + "representativityAsNumberOfTemporalElementsPerGeographicalUnitMean"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "min", "distribution parameters of different temporal elements per geographical unit",
                            NS.GEOQUAL + "representativityAsNumberOfTemporalElementsPerGeographicalUnitMin"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "5 % quantile", "distribution parameters of different temporal elements per geographical unit",
                            NS.GEOQUAL + "representativityAsNumberOfTemporalElementsPerGeographicalUnitQuantile05"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "25 % quantile", "distribution parameters of different temporal elements per geographical unit",
                            NS.GEOQUAL + "representativityAsNumberOfTemporalElementsPerGeographicalUnitQuantile25"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "50 % quantile", "distribution parameters of different temporal elements per geographical unit",
                            NS.GEOQUAL + "representativityAsNumberOfTemporalElementsPerGeographicalUnitQuantile50"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "75 % quantile", "distribution parameters of different temporal elements per geographical unit",
                            NS.GEOQUAL + "representativityAsNumberOfTemporalElementsPerGeographicalUnitQuantile75"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "95 % quantile", "distribution parameters of different temporal elements per geographical unit",
                            NS.GEOQUAL + "representativityAsNumberOfTemporalElementsPerGeographicalUnitQuantile95"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "max", "distribution parameters of different temporal elements per geographical unit",
                            NS.GEOQUAL + "representativityAsNumberOfTemporalElementsPerGeographicalUnitMax"));

                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "mean", "distribution parameters of different thematic elements per geographical unit",
                            NS.GEOQUAL + "representativityAsNumberOfThematicElementsPerGeographicalUnitMean"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "min", "distribution parameters of different thematic elements per geographical unit",
                            NS.GEOQUAL + "representativityAsNumberOfThematicElementsPerGeographicalUnitMin"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "5 % quantile", "distribution parameters of different thematic elements per geographical unit",
                            NS.GEOQUAL + "representativityAsNumberOfThematicElementsPerGeographicalUnitQuantile05"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "25 % quantile", "distribution parameters of different thematic elements per geographical unit",
                            NS.GEOQUAL + "representativityAsNumberOfThematicElementsPerGeographicalUnitQuantile25"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "50 % quantile", "distribution parameters of different thematic elements per geographical unit",
                            NS.GEOQUAL + "representativityAsNumberOfThematicElementsPerGeographicalUnitQuantile50"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "75 % quantile", "distribution parameters of different thematic elements per geographical unit",
                            NS.GEOQUAL + "representativityAsNumberOfThematicElementsPerGeographicalUnitQuantile75"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "95 % quantile", "distribution parameters of different thematic elements per geographical unit",
                            NS.GEOQUAL + "representativityAsNumberOfThematicElementsPerGeographicalUnitQuantile95"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "max", "distribution parameters of different thematic elements per geographical unit",
                            NS.GEOQUAL + "representativityAsNumberOfThematicElementsPerGeographicalUnitMax"));

                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "mean", "distribution parameters of different thematic elements per temporal unit",
                            NS.GEOQUAL + "representativityAsNumberOfThematicElementsPerTemporalUnitMean"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "min", "distribution parameters of different thematic elements per temporal unit",
                            NS.GEOQUAL + "representativityAsNumberOfThematicElementsPerTemporalUnitMin"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "5 % quantile", "distribution parameters of different thematic elements per temporal unit",
                            NS.GEOQUAL + "representativityAsNumberOfThematicElementsPerTemporalUnitQuantile05"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "25 % quantile", "distribution parameters of different thematic elements per temporal unit",
                            NS.GEOQUAL + "representativityAsNumberOfThematicElementsPerTemporalUnitQuantile25"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "50 % quantile", "distribution parameters of different thematic elements per temporal unit",
                            NS.GEOQUAL + "representativityAsNumberOfThematicElementsPerTemporalUnitQuantile50"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "75 % quantile", "distribution parameters of different thematic elements per temporal unit",
                            NS.GEOQUAL + "representativityAsNumberOfThematicElementsPerTemporalUnitQuantile75"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "95 % quantile", "distribution parameters of different thematic elements per temporal unit",
                            NS.GEOQUAL + "representativityAsNumberOfThematicElementsPerGeographicalUnitQuantile95"));
                    qualityMeasurements.add(mapDatasetHasQualityMeasurement(metadataAct, DQ_Representativity.class,
                            contentColumnNamesWoDescription.get(i), "max", "distribution parameters of different thematic elements per temporal unit",
                            NS.GEOQUAL + "representativityAsNumberOfThematicElementsPerTemporalUnitMax"));

                    fillDatasetHasQualityMeasurement(subsubDataset, qualityMeasurements);
                }
            }
        }

        return model;
    }


    // helper fill methods
    private void fillDatasetTitle(Resource dataset, String string) {
        if (string != null) {
            dataset.addProperty(Dataset.title, string);
        }
    }

    private void fillDatasetDescription(Resource dataset, String string) {
        if (string != null) {
            dataset.addProperty(Dataset.description, string);
        }
    }

    private void fillDatasetContactPoint(Resource dataset, Resource resource) {
        if (resource != null) {
            dataset.addProperty(Dataset.contactPoint, resource);
        }
    }

    private void fillDatasetDistribution(Resource dataset, Resource resource) {
        dataset.addProperty(Dataset.datasetDistribution, resource);
    }

    private void fillDatasetKeywordTag(Resource dataset, List<String> strings) {
        if (strings != null) {
            for (String keywordAct : strings) {
                dataset.addProperty(Dataset.keywordTag, keywordAct);
            }
        }
    }

    private void fillDatasetSpatialGeographicCoverage(Resource dataset, List<Resource> resources) {
        if (resources.size() > 0) {
            for (Resource resource : resources) {
                dataset.addProperty(Dataset.spatialGeographicCoverage, resource);
            }
        }
    }

    private void fillDatasetTemporalCoverage(Resource dataset, Resource resource) {
        if (resource != null) {
            dataset.addProperty(Dataset.temporalCoverage, resource);
        }
    }

    private void fillDatasetThemeCategory(Resource dataset, Resource resource) {
        if (resource != null) {
            dataset.addProperty(Dataset.themeCategory, resource);
        }
    }

    private void fillDatasetCreationDate(Resource dataset, Literal literal) {
        if (literal != null) {
            dataset.addProperty(Dataset.creationDate, literal);
        }
    }

    private void fillDatasetDocumentation(Resource dataset, List<Resource> resources) {
        if (resources.size() > 0) {
            for (Resource resource : resources) {
                dataset.addProperty(Dataset.documentation, resource);
            }
        }
    }

    private void fillDatasetIdentifier(Resource dataset, String string) {
        if (string != null) {
            dataset.addProperty(Dataset.identifier, string);
        }
    }

    private void fillDatasetIsVersionOf(Resource dataset, Resource resource) {
        if (resource != null) {
            dataset.addProperty(Dataset.isVersionOf, resource);
        }
    }

    private void fillDatasetLandingPage(Resource dataset, List<Resource> resources) {
        if (resources.size() > 0) {
            for (Resource resource : resources) {
                dataset.addProperty(Dataset.landingPage, resource);
            }
        }
    }

    private void fillDatasetOtherIdentifier(Resource dataset, List<Resource> resources) {
        if (resources.size() > 0) {
            for (Resource resource : resources) {
                dataset.addProperty(Dataset.otherIdentifier, resource);
            }
        }
    }

    private void fillDatasetRelatedResource(Resource dataset, List<Resource> resources) {
        if (resources.size() > 0) {
            for (Resource resource : resources) {
                dataset.addProperty(Dataset.relatedResource, resource);
            }
        }
    }

    private void fillDatasetReferenceSystem(Resource dataset, Resource resource) {
        if (resource != null) {
            dataset.addProperty(Dataset.referenceSystem, resource);
        }
    }

    private void fillDatasetSpatialResolution(Resource dataset, MD_Metadata metadataAct) {
        try {
            String srsID = metadataAct.referenceSystemInfo.get(0).referenceSystemIdentifier.get(0).code.get(0).toLowerCase();
            CoordinateReferenceSystem referenceSystem = CRS.decode(srsID);
            String unit = referenceSystem.getCoordinateSystem().getAxis(1).getUnit().toString();

            Resource resource;
            if (fileType.equals("TIFF") && unit.equals("°")) {
                // spatial resolution is given as angular distance
                resource = mapDatasetSpatialResolutionAsAngularDistance(metadataAct);
                if (resource != null) {
                    dataset.addProperty(Dataset.spatialResolution, resource);
                }
            }
            else {
                // spatial resolution is given as metric distance
                resource = mapDatasetSpatialResolutionAsDistance(metadataAct);
                if (resource != null) {
                    dataset.addProperty(Dataset.spatialResolutionInMeters, resource);
                }
            }
        } catch (FactoryException | NullPointerException ignore) {}
    }

    private void fillDatasetSpatialResolutionAsEquivalentScale(Resource dataset, Resource resource) {
        if (resource != null) {
            dataset.addProperty(Dataset.spatialResolution, resource);
        }
    }

    private void fillDatasetTemporalResolution(Resource dataset, Literal literal) {
        if (literal != null) {
            dataset.addLiteral(Dataset.temporalResolution, literal);
        }
    }

    private void fillDatasetWasGeneratedBy(Resource dataset, Resource resource) {
        if (resource != null) {
            dataset.addProperty(Dataset.wasGeneratedBy, resource);
        }
    }

    private void fillDatasetHasQualityMeasurement(Resource dataset, Resource resource) {
        if (resource != null) {
            dataset.addProperty(Dataset.hasQualityMeasurement, resource);
        }
    }

    private void fillDatasetHasQualityMeasurement(Resource dataset, List<Resource> resources) {
        if (resources.size() > 0) {
            for (Resource resource : resources) {
                if (resource != null) {
                    // skip null entries
                    dataset.addProperty(Dataset.hasQualityMeasurement, resource);
                }
            }
        }
    }


    // helper mapping methods
    private String mapDatasetTitle(MD_Metadata mdMetadata) {
        // 1 dct:title
        String datasetTitle;
        try {
            datasetTitle = mdMetadata.identificationInfo.get(0).citation.get(0).title.get(0);
        } catch (NullPointerException e) {
            datasetTitle = null;
        }
        return datasetTitle;
    }

    private String mapDatasetDescription(MD_Metadata mdMetadata) {
        // 2 dct:description
        String datasetDescription;
        try {
            datasetDescription = mdMetadata.identificationInfo.get(0).abstractElement.get(0);
        } catch (NullPointerException e) {
            datasetDescription = null;
        }
        return datasetDescription;
    }

    private Resource mapDatasetContactPoint(MD_Metadata mdMetadata) {
        // 3 dcat:contactPoint
        Resource datasetContactPoint;
        try {
            if (mdMetadata.identificationInfo.get(0).pointOfContact.get(0).party.get(0) instanceof CI_Individual) {
                datasetContactPoint = model.createResource()
                        .addProperty(RDF.type, VCARD4.Individual)
                        .addProperty(VCARD4.fn, mdMetadata.identificationInfo.get(0).pointOfContact.get(0).party.get(0).name.get(0))
                        .addProperty(VCARD4.url, mdMetadata.identificationInfo.get(0).pointOfContact.get(0).party.get(0).contactInfo.get(0).onlineResource.get(0).linkage.get(0));
            } else {
                datasetContactPoint = model.createResource()
                        .addProperty(RDF.type, VCARD4.Organization)
                        .addProperty(VCARD4.fn, mdMetadata.identificationInfo.get(0).pointOfContact.get(0).party.get(0).name.get(0))
                        .addProperty(VCARD4.url, mdMetadata.identificationInfo.get(0).pointOfContact.get(0).party.get(0).contactInfo.get(0).onlineResource.get(0).linkage.get(0));
            }
        } catch (NullPointerException e) {
            datasetContactPoint = null;
        }
        return datasetContactPoint;
    }

    private Resource mapDatasetDistribution(MD_Metadata mdMetadata) {
        // 4 dcat:distribution
        Resource datasetDistribution;
        datasetDistribution = model.createResource();
        datasetDistribution.addProperty(RDF.type, Dataset.resourceInstance);
        datasetDistribution.addProperty(Distribution.format, model.createResource("http://publications.europa.eu/resource/authority/file-type/" + fileType));
        String licence;
        try {
            for (MD_Constraints access : mdMetadata.identificationInfo.get(0).resourceConstraints) {
                // look for right resourceConstraints member
                if (access instanceof MD_LegalConstraints && ((MD_LegalConstraints) access).accessConstraints.get(0).codeListValue.toString().equalsIgnoreCase("licence")) {
                    licence = access.reference.get(0).onlineResource.get(0).linkage.get(0);
                    datasetDistribution.addProperty(Distribution.licence, model.createResource(licence));
                }
            }
        } catch (NullPointerException ignore) {}
        return datasetDistribution;
    }

    private List<String> mapDatasetKeywordTag(MD_Metadata mdMetadata, int counter) {
        // 5 dcat:keyword
        List<String> datasetKeywordTag = new ArrayList<>();
        try {
            for (String keyword : mdMetadata.identificationInfo.get(0).descriptiveKeywords.get(counter).keyword) {
                if (!keyword.toLowerCase().contains("attributename")) {
                    datasetKeywordTag.add(keyword);
                }
            }
//            for (MD_Keywords keywords : mdMetadata.identificationInfo.get(0).descriptiveKeywords) {
//                // all keyword types included (also theme, which is again used in 8 dcat:theme)
//                if (keywords.keyword == null) {
//                    continue;
//                }
//                datasetKeywordTag.addAll(keywords.keyword);
//            }
        } catch (NullPointerException e) {
            datasetKeywordTag = null;
        }
        return datasetKeywordTag;
    }

    private List<Resource> mapDatasetSpatialGeographicCoverage(MD_Metadata mdMetadata) {
        // 6 dct:spatial
        List<Resource> datasetSpatialGeographicCoverages = new ArrayList<>();
        List<String> epsgNumbers = new ArrayList<>();
        try {
            if (mdMetadata.identificationInfo.get(0).extent.get(0).geographicElement.get(0) instanceof EX_GeographicBoundingBox) {
                epsgNumbers.add(mdMetadata.identificationInfo.get(0).extent.get(0).description.get(0).split(":")[1]);

                double west = ((EX_GeographicBoundingBox) mdMetadata.identificationInfo.get(0).extent.get(0).geographicElement.get(0)).westBoundLongitude.get(0);
                double east = ((EX_GeographicBoundingBox) mdMetadata.identificationInfo.get(0).extent.get(0).geographicElement.get(0)).eastBoundLongitude.get(0);
                double south = ((EX_GeographicBoundingBox) mdMetadata.identificationInfo.get(0).extent.get(0).geographicElement.get(0)).southBoundLatitude.get(0);
                double north = ((EX_GeographicBoundingBox) mdMetadata.identificationInfo.get(0).extent.get(0).geographicElement.get(0)).northBoundLatitude.get(0);
                String bboxWkt = "POLYGON ((" + west + " " + south + ", " + east + " " + south + ", " + east + " " + north + ", " + west + " " + north + ", " + west + " " + south + "))";

                Resource datasetSpatialGeographicCoverage = model.createResource()
                        .addProperty(RDF.type, Location.resourceInstance)
                        .addProperty(Location.boundingBox, model.createTypedLiteral(bboxWkt, NS.GSP + "wktLiteral"))
                        .addProperty(Location.geometry, model.createResource("http://www.opengis.net/def/crs/EPSG/0/" + epsgNumbers.get(0)));
                datasetSpatialGeographicCoverages.add(datasetSpatialGeographicCoverage);
            }
        } catch (NullPointerException ignore) {}
        try {
            if (mdMetadata.identificationInfo.get(0).extent.size() > 1 &&
                    mdMetadata.identificationInfo.get(0).extent.get(1).geographicElement.get(0) instanceof EX_GeographicBoundingBox) {
                epsgNumbers.add(mdMetadata.identificationInfo.get(0).extent.get(1).description.get(0).split(":")[1]);

                if (!epsgNumbers.get(0).equals(epsgNumbers.get(1))) {
                    double west = ((EX_GeographicBoundingBox) mdMetadata.identificationInfo.get(0).extent.get(1).geographicElement.get(0)).westBoundLongitude.get(0);
                    double east = ((EX_GeographicBoundingBox) mdMetadata.identificationInfo.get(0).extent.get(1).geographicElement.get(0)).eastBoundLongitude.get(0);
                    double south = ((EX_GeographicBoundingBox) mdMetadata.identificationInfo.get(0).extent.get(1).geographicElement.get(0)).southBoundLatitude.get(0);
                    double north = ((EX_GeographicBoundingBox) mdMetadata.identificationInfo.get(0).extent.get(1).geographicElement.get(0)).northBoundLatitude.get(0);
                    String bboxWkt = "POLYGON ((" + west + " " + south + ", " + east + " " + south + ", " + east + " " + north + ", " + west + " " + north + ", " + west + " " + south + "))";

                    Resource datasetSpatialGeographicCoverage = model.createResource()
                            .addProperty(RDF.type, Location.resourceInstance)
                            .addProperty(Location.boundingBox, model.createTypedLiteral(bboxWkt, NS.GSP + "wktLiteral"))
                            .addProperty(Location.geometry, model.createResource("http://www.opengis.net/def/crs/EPSG/0/" + epsgNumbers.get(1)));
                    datasetSpatialGeographicCoverages.add(datasetSpatialGeographicCoverage);
                }
            }
        } catch (NullPointerException ignore) {}

        return datasetSpatialGeographicCoverages;
    }

    private Resource mapDatasetTemporalCoverage(MD_Metadata mdMetadata, int counter) {
        // 7 dct:temporal
        Resource datasetTemporalCoverage;
        try {
            if (mdMetadata.identificationInfo.get(0).extent.get(counter).temporalElement.get(0).extent.get(0) instanceof TM_Period) {
                // with time regularity a period is given
                TM_Period tmPeriod = (TM_Period) mdMetadata.identificationInfo.get(0).extent.get(counter).temporalElement.get(0).extent.get(0);
                String begin = tmPeriod.begin.get(0).position.get(0);
                String end = tmPeriod.end.get(0).position.get(0);
                datasetTemporalCoverage = model.createResource()
                        .addProperty(RDF.type, PeriodOfTime.resourceInstance)
                        .addProperty(PeriodOfTime.startDate, model.createTypedLiteral(begin, XSDDatatype.XSDdateTime))
                        .addProperty(PeriodOfTime.endDate, model.createTypedLiteral(end, XSDDatatype.XSDdateTime));
            }
            else {
                // with no time regularity singular time instants are given
                List<TM_Instant> tmInstants = new ArrayList<>();
                for (EX_TemporalExtent exTemporalExtent : mdMetadata.identificationInfo.get(0).extent.get(counter).temporalElement) {
                    tmInstants.add((TM_Instant) exTemporalExtent.extent.get(0));
                }
                datasetTemporalCoverage = model.createResource()
                        .addProperty(RDF.type, Date.resourceInstance);
                for (TM_Instant tmInstant : tmInstants) {
                    datasetTemporalCoverage.addProperty(Date.date, model.createTypedLiteral(tmInstant.position.get(0), XSDDatatype.XSDdateTime));
                }
            }
        } catch (NullPointerException e) {
            datasetTemporalCoverage = null;
        }
        return datasetTemporalCoverage;
    }

    private Resource mapDatasetThemeCategory(MD_Metadata mdMetadata) {
        // 8 dcat:theme
        Resource datasetThemeCategory = null;
        try {
            for (MD_Keywords keywords : mdMetadata.identificationInfo.get(0).descriptiveKeywords) {
                // here only keywords with type theme are interpreted
                if (keywords.type.get(0).codeListValue.toString().equalsIgnoreCase("theme")) {
                    String uri = keywords.keywordClass.get(0).conceptIdentifier.get(0);
                    String ontologyUrl = keywords.keywordClass.get(0).ontology.get(0).onlineResource.get(0).linkage.get(0);
                    datasetThemeCategory = model.createResource(ontologyUrl + uri);
                }
            }
        } catch (NullPointerException e) {
            datasetThemeCategory = null;
        }
        return datasetThemeCategory;
    }

    private Literal mapDatasetCreationDate(MD_Metadata mdMetadata) {
        // 9 dct:created
        Literal datasetCreationDate;
        try {
            String creationDate = null;
            for (CI_Date date : mdMetadata.dateInfo) {
                // look for creation date
                if (date.dateType.get(0).codeListValue == CI_DateTypeCode.CI_DateTypeCodes.creation) {
                    creationDate = date.date.get(0);
                }
            }

            datasetCreationDate = model.createTypedLiteral(creationDate, XSDDatatype.XSDdateTime);
        } catch (NullPointerException e) {
            datasetCreationDate = null;
        }
        return datasetCreationDate;
    }

    private List<Resource> mapDatasetDocumentation(MD_Metadata mdMetadata) {
        // 10 foaf:page
        List<Resource> datasetDocumentations = new ArrayList<>();
        try {
            for (CI_Citation citation : mdMetadata.identificationInfo.get(0).additionalDocumentation) {
                String citationAct = citation.onlineResource.get(0).linkage.get(0);
                datasetDocumentations.add(model.createResource(citationAct));
            }
        } catch (NullPointerException ignore) {}
        return datasetDocumentations;
    }

    private String mapDatasetIdentifier(MD_Metadata mdMetadata) {
        // 11 dct:identifier
        String datasetIdentifier;
        try {
            datasetIdentifier = mdMetadata.metadataIdentifier.get(0).code.get(0);
        } catch (NullPointerException e) {
            datasetIdentifier = null;
        }
        return datasetIdentifier;
    }

    private Resource mapDatasetIsVersionOf(MD_Metadata mdMetadata) {
        // 12 dct:isVersionOf
        Resource datasetIsVersionOf = null;
        try {
            for (MD_AssociatedResource associatedResource : mdMetadata.identificationInfo.get(0).associatedResource) {
                if (associatedResource.associationType.get(0).codeListValue == DS_AssociationTypeCode.DS_AssociationTypeCodes.revisionOf) {
                    String linkage = associatedResource.name.get(0).onlineResource.get(0).linkage.get(0);
                    datasetIsVersionOf = model.createResource(linkage);
                }
            }
        } catch (NullPointerException e) {
            datasetIsVersionOf = null;
        }
        return datasetIsVersionOf;
    }

    private List<Resource> mapDatasetLandingPage(MD_Metadata mdMetadata) {
        // 13 dcat:landingPage
        List<Resource> datasetLandingPage = new ArrayList<>();
        try {
            for (CI_Citation citation : mdMetadata.identificationInfo.get(0).additionalDocumentation) {
                String citationAct = citation.onlineResource.get(0).linkage.get(0);
                datasetLandingPage.add(model.createResource(citationAct));
            }
        } catch (NullPointerException ignore) {}
        return datasetLandingPage;
    }

    private List<Resource> mapDatasetOtherIdentifier(MD_Metadata mdMetadata) {
        // 14 adms:identifier
        List<Resource> datasetOtherIdentifier = new ArrayList<>();
        try {
            for (CI_Citation citation : mdMetadata.identificationInfo.get(0).additionalDocumentation) {
                String citationAct = citation.onlineResource.get(0).linkage.get(0);
                datasetOtherIdentifier.add(model.createResource(citationAct));
            }
        } catch (NullPointerException ignore) {}
        return datasetOtherIdentifier;
    }

    private List<Resource> mapDatasetRelatedResource(List<DS_Aggregate> dsAggregates, String linkBase) {
        // 15 dct:relation
        List<Resource> datasetRelatedResources = new ArrayList<>();
        try {
            List<String> linkIDparts = new ArrayList<>();
            for (DS_Aggregate dsAggregate : dsAggregates) {
                for (MD_Metadata metadata : dsAggregate.has) {
                    linkIDparts.add(linkBase + metadata.metadataIdentifier.get(0).code.get(0));
                }
            }
            for (String linkIDpart : linkIDparts) {
                datasetRelatedResources.add(model.createResource(linkIDpart));
            }
        } catch (NullPointerException ignore) {}
        return datasetRelatedResources;
    }

    private Resource mapDatasetReferenceSystem(MD_Metadata mdMetadata) {
        // 16 - dct:conformsTo
        Resource datasetReferenceSystem = null;
        try {
            String srsID = mdMetadata.referenceSystemInfo.get(0).referenceSystemIdentifier.get(0).code.get(0).toLowerCase();
            if (srsID.contains("epsg")) {
                String epsgNumber = srsID.split(":")[1]; // always has the form EPSG:xxx coming from this package
                datasetReferenceSystem = model.createResource("http://www.opengis.net/def/crs/EPSG/0/" + epsgNumber);
            }
        } catch (NullPointerException ignore) {}
        return datasetReferenceSystem;
    }

    private Resource mapDatasetSpatialResolutionAsAngularDistance(MD_Metadata mdMetadata) {
        // 17 - dqv:hasQualityMeasurement -> geodcat:spatialResolutionAsAngularDistance -> dqv:value
        Resource datasetSpatialResolutionAsAngularDistance;
        try {
            List<Double> resolutions = new ArrayList<>();
            for (MD_Resolution mdResolution : mdMetadata.identificationInfo.get(0).spatialResolution) {
                resolutions.add(mdResolution.angularDistance.get(0));
            }

            datasetSpatialResolutionAsAngularDistance = model.createResource()
                    .addProperty(RDF.type, QualityMeasurement.resourceInstance)
                    .addProperty(QualityMeasurement.unitOfMeasure, model.createResource("http://www.qudt.org/vocab/unit/DEG"));
            if (resolutions.size() == 1) {
                // resolution isotropic
                datasetSpatialResolutionAsAngularDistance.addProperty(QualityMeasurement.isMeasurementOf, SpatialResolutionAsAngularDistance.resourceInstance)
                        .addLiteral(QualityMeasurement.value, resolutions.get(0));
            }
            else {
                // resolution anisotropic
                datasetSpatialResolutionAsAngularDistance.addProperty(QualityMeasurement.isMeasurementOf, SpatialResolutionAsAngularDistance.resourceInstance)
                        .addLiteral(QualityMeasurement.value, resolutions.get(0))
                        .addLiteral(QualityMeasurement.value, resolutions.get(1));
            }
        } catch (NullPointerException e) {
            datasetSpatialResolutionAsAngularDistance = null;
        }
        return datasetSpatialResolutionAsAngularDistance;
    }

    private Resource mapDatasetSpatialResolutionAsDistance(MD_Metadata mdMetadata) {
        // 18 - dqv:hasQualityMeasurement -> geodcat:spatialResolutionAsDistance -> dqv:value
        Resource datasetSpatialResolutionAsDistance;
        try {
            List<Double> resolutions = new ArrayList<>();
            for (MD_Resolution mdResolution : mdMetadata.identificationInfo.get(0).spatialResolution) {
                resolutions.add(mdResolution.distance.get(0));
            }

            datasetSpatialResolutionAsDistance = model.createResource()
                    .addProperty(RDF.type, QualityMeasurement.resourceInstance)
                    .addProperty(QualityMeasurement.unitOfMeasure, model.createResource("http://www.qudt.org/vocab/unit/M"));
            if (resolutions.size() == 1) {
                // resolution isotropic
                datasetSpatialResolutionAsDistance.addProperty(QualityMeasurement.isMeasurementOf, SpatialResolutionAsDistance.resourceInstance)
                        .addLiteral(QualityMeasurement.value, resolutions.get(0));
            }
            else {
                // resolution anisotropic
                datasetSpatialResolutionAsDistance.addProperty(QualityMeasurement.isMeasurementOf, SpatialResolutionAsDistance.resourceInstance)
                        .addLiteral(QualityMeasurement.value, resolutions.get(0))
                        .addLiteral(QualityMeasurement.value, resolutions.get(1));
            }
        } catch (NullPointerException e) {
            datasetSpatialResolutionAsDistance = null;
        }
        return datasetSpatialResolutionAsDistance;
    }

    private Resource mapDatasetSpatialResolutionAsEquivalentScale(MD_Metadata mdMetadata) {
        // 19 - dqv:hasQualityMeasurement -> geodcat:spatialResolutionAsScale -> dqv:value
        Resource datasetSpatialResolutionAsEquivalentScale;
        try {
            List<Double> resolutions = new ArrayList<>();
            for (MD_Resolution mdResolution : mdMetadata.identificationInfo.get(0).spatialResolution) {
                resolutions.add(1.0 / mdResolution.equivalentScale.get(0).denominator.get(0));
            }

            datasetSpatialResolutionAsEquivalentScale = model.createResource()
                    .addProperty(RDF.type, QualityMeasurement.resourceInstance);
            if (resolutions.size() == 1) {
                // resolution isotropic
                datasetSpatialResolutionAsEquivalentScale.addProperty(QualityMeasurement.isMeasurementOf, SpatialResolutionAsEquivalentScale.resourceInstance)
                        .addLiteral(QualityMeasurement.value, resolutions.get(0));
            }
            else {
                // resolution anisotropic
                datasetSpatialResolutionAsEquivalentScale.addProperty(QualityMeasurement.isMeasurementOf, SpatialResolutionAsEquivalentScale.resourceInstance)
                        .addLiteral(QualityMeasurement.value, resolutions.get(0))
                        .addLiteral(QualityMeasurement.value, resolutions.get(1));
            }
        } catch (NullPointerException e) {
            datasetSpatialResolutionAsEquivalentScale = null;
        }
        return datasetSpatialResolutionAsEquivalentScale;
    }

    private Literal mapDatasetTemporalResolution(MD_Metadata mdMetadata, int counter) {
        // 20 dcat:temporalResolution
        Literal temporalResolution;
        try {
            String temporalResolutionString = mdMetadata.identificationInfo.get(0).temporalResolution.get(counter);
            if (!temporalResolutionString.contains("-999")) {
                temporalResolution = model.createTypedLiteral(temporalResolutionString, XSDDatatype.XSDduration);
            }
            else {
                temporalResolution = null;
            }
        } catch (NullPointerException e) {
            temporalResolution = null;
        }
        return temporalResolution;
    }

    private Resource mapDatasetWasGeneratedBy(MD_Metadata mdMetadata) {
        // 21 prov:wasGeneratedBy
        Resource datasetWasGeneratedBy;
        String description;
        String onlineResource;
        String used;
        try {
            description = mdMetadata.resourceLineage.get(0).processStep.get(0).description.get(0);
        } catch (NullPointerException e) {
            description = null;
        }
        try {
            onlineResource = mdMetadata.resourceLineage.get(0).processStep.get(0).reference.get(0).onlineResource.get(0).linkage.get(0);
        } catch (NullPointerException e) {
            onlineResource = null;
        }
        try {
            used = mdMetadata.resourceLineage.get(0).source.get(0).sourceCitation.get(0).onlineResource.get(0).linkage.get(0);
        } catch (NullPointerException e) {
            used = null;
        }

        if (description != null || onlineResource != null || used != null) {
            datasetWasGeneratedBy = model.createResource()
                    .addProperty(RDF.type, GeoKurProcessIdentifier.resourceInstance);
        } else {
            // no lineage available
            return null;
        }

        if (description != null) {
            datasetWasGeneratedBy.addProperty(GeoKurProcessIdentifier.generated, description);
        }
        if (onlineResource != null) {
            datasetWasGeneratedBy.addProperty(GeoKurProcessIdentifier.hasGeospatialCategory, onlineResource);
        }
        if (used != null) {
            datasetWasGeneratedBy.addProperty(GeoKurProcessIdentifier.used, used);
        }

        return datasetWasGeneratedBy;
    }

    private Resource mapDatasetHasQualityMeasurement(MD_Metadata mdMetadata, Class<?> type, String attribute, String fieldName, String nameOfMeasure, String nameOfMetric) {
        // 24 dqv:hasQualityMeasurement
        Resource hasQualityMeasurement = null;
        List<DQ_Element> dqElements = mdMetadata.dataQualityInfo.get(0).report;
        for (DQ_Element dqElement : dqElements) {
            if (attribute == null && fieldName == null) {
                try {
                    if (type.isInstance(dqElement)
                            && dqElement.measure.get(0).nameOfMeasure.get(0).equalsIgnoreCase(nameOfMeasure)) {
                        String value = ((DQ_QuantitativeResult) dqElement.result.get(0)).value.get(0).field.get(0).getValue();
                        hasQualityMeasurement = model.createResource()
                                .addProperty(RDF.type, QualityMeasurement.resourceInstance)
                                .addProperty(QualityMeasurement.isMeasurementOf, model.createResource(nameOfMetric))
                                .addProperty(QualityMeasurement.value, value);
                        break;
                    }
                } catch (NullPointerException ignore) {}
            }
            else if (fieldName == null) {
                try {
                    if (type.isInstance(dqElement)
                            && dqElement.result.get(0).resultScope.get(0).levelDescription.get(0).attributes.get(0).equalsIgnoreCase(attribute)
                            && dqElement.measure.get(0).nameOfMeasure.get(0).equalsIgnoreCase(nameOfMeasure)) {
                        String value = ((DQ_QuantitativeResult) dqElement.result.get(0)).value.get(0).field.get(0).getValue();
                        hasQualityMeasurement = model.createResource()
                                .addProperty(RDF.type, QualityMeasurement.resourceInstance)
                                .addProperty(QualityMeasurement.isMeasurementOf, model.createResource(nameOfMetric))
                                .addProperty(QualityMeasurement.value, value);
                        break;
                    }
                } catch (NullPointerException ignore) {}
            }
            else {
                try {
                    if (type.isInstance(dqElement)
                            && dqElement.result.get(0).resultScope.get(0).levelDescription.get(0).attributes.get(0).equalsIgnoreCase(attribute)
                            && dqElement.measure.get(0).nameOfMeasure.get(0).equalsIgnoreCase(nameOfMeasure)) {
                        List<RecordEntry> recordEntries = ((DQ_QuantitativeResult) dqElement.result.get(0)).value.get(0).field;
                        for (RecordEntry recordEntry : recordEntries) {
                            if (recordEntry.getFieldName().equalsIgnoreCase(fieldName)) {
                                String value = recordEntry.getValue();
                                hasQualityMeasurement = model.createResource()
                                        .addProperty(RDF.type, QualityMeasurement.resourceInstance)
                                        .addProperty(QualityMeasurement.isMeasurementOf, model.createResource(nameOfMetric))
                                        .addProperty(QualityMeasurement.value, value);
                            }
                        }
                        break;
                    }
                } catch (NullPointerException ignore) {}
            }
        }

        return hasQualityMeasurement;
    }

    private String getLayerName(MD_Metadata mdMetadata, boolean includeDescription) {
        // obtain layer name of geopackages
        String layerName = "";
        String[] environmentalDescriptions = ((MD_DataIdentification) mdMetadata.identificationInfo.get(0)).environmentalDescription.get(0).split(";");
        for (String environmentalDescription : environmentalDescriptions) {
            if (environmentalDescription.toLowerCase().contains("layer name")) {
                if (includeDescription) {
                    layerName = environmentalDescription.replace("layer name", "layerName");
                }
                else {
                    layerName = environmentalDescription.split(":")[1].trim();
                }
            }
        }

        return layerName;
    }

    private List<String> getContentColumnNames(MD_Metadata mdMetadata, boolean includeDescription) {
        // obtain content column names of tabular data
        List<String> columnNames = new ArrayList<>();
        List<EX_Extent> exExtents = mdMetadata.identificationInfo.get(0).extent;
        for (EX_Extent exExtent : exExtents) {
            if (includeDescription) {
                columnNames.add(exExtent.description.get(0));
            }
            else {
                columnNames.add(exExtent.description.get(0).split(":")[1].trim());
            }
        }

        return columnNames;
    }
}
