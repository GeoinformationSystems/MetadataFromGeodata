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
import org.geokur.ISO19108Schema.TM_Period;
import org.geokur.ISO19115Schema.*;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.ArrayList;
import java.util.List;

public class MappedMetadata {
    Model model;
    String fileType;

    public MappedMetadata(Model model, String fileType) {
        this.model = model;
        this.fileType = fileType;
    }

    public Model fillModel(DS_Resource dsResource) {
        // map metadata from ISO schema to GeoDCAT rdf


        for (MD_Metadata metadataAct : dsResource.has) {
            // loop over all datasets in metadata
            Resource tmpResource;
            List<Resource> tmpResourceList;
            String tmpString;
            List<String> tmpList;
            Literal tmpLiteral;
            List<Literal> tmpLiteralList;

            String linkBase = "https://link/to/";
            String datasetID = metadataAct.metadataIdentifier.get(0).code.get(0);
            Resource dataset = model.createResource(linkBase + datasetID); //TODO: implement correct link base
            dataset.addProperty(RDF.type, Dataset.resourceInstance);

            tmpString = mapDatasetTitle(metadataAct);
            if (tmpString != null) {
                dataset.addProperty(Dataset.title, tmpString);
            }

            tmpString = mapDatasetDescription(metadataAct);
            if (tmpString != null) {
                dataset.addProperty(Dataset.description, tmpString);
            }

            tmpResource = mapDatasetContactPoint(metadataAct);
            if (tmpResource != null) {
                dataset.addProperty(Dataset.contactPoint, tmpResource);
            }

            dataset.addProperty(Dataset.datasetDistribution, mapDatasetDistribution(metadataAct));

            tmpList = mapDatasetKeywordTag(metadataAct);
            if (tmpList != null) {
                for (String keywordAct : tmpList) {
                    dataset.addProperty(Dataset.keywordTag, keywordAct);
                }
            }

            tmpResource = mapDatasetSpatialGeographicCoverage(metadataAct);
            if (tmpResource != null) {
                dataset.addProperty(Dataset.spatialGeographicCoverage, tmpResource);
            }

            tmpResource = mapDatasetTemporalCoverage(metadataAct);
            if (tmpResource != null) {
                dataset.addProperty(Dataset.temporalCoverage, tmpResource);
            }

            tmpResource = mapDatasetThemeCategory(metadataAct);
            if (tmpResource != null) {
                dataset.addProperty(Dataset.themeCategory, tmpResource);
            }

            tmpLiteral = mapDatasetCreationDate(metadataAct);
            if (tmpLiteral != null) {
                dataset.addProperty(Dataset.creationDate, tmpLiteral);
            }

            tmpResourceList = mapDatasetDocumentation(metadataAct);
            if (tmpResourceList.size() > 0) {
                for (Resource resource : tmpResourceList) {
                    dataset.addProperty(Dataset.documentation, resource);
                }
            }

            tmpString = mapDatasetIdentifier(metadataAct);
            if (tmpString != null) {
                dataset.addProperty(Dataset.identifier, tmpString);
            }

            tmpResource = mapDatasetIsVersionOf(metadataAct);
            if (tmpResource != null) {
                dataset.addProperty(Dataset.isVersionOf, tmpResource);
            }

            tmpResourceList = mapDatasetLandingPage(metadataAct);
            if (tmpResourceList.size() > 0) {
                for (Resource resource : tmpResourceList) {
                    dataset.addProperty(Dataset.landingPage, resource);
                }
            }

            tmpResourceList = mapDatasetOtherIdentifier(metadataAct);
            if (tmpResourceList.size() > 0) {
                for (Resource resource : tmpResourceList) {
                    dataset.addProperty(Dataset.otherIdentifier, resource);
                }
            }

            tmpResourceList = mapDatasetRelatedResource(dsResource.partOf, linkBase);
            if (tmpResourceList.size() > 0) {
                for (Resource resource : tmpResourceList) {
                    dataset.addProperty(Dataset.relatedResource, resource);
                }
            }

            tmpResource = mapDatasetReferenceSystem(metadataAct);
            if (tmpResource != null) {
                dataset.addProperty(Dataset.referenceSystem, tmpResource);
            }

            try {
                String srsID = metadataAct.referenceSystemInfo.get(0).referenceSystemIdentifier.get(0).code.get(0).toLowerCase();
                CoordinateReferenceSystem referenceSystem = CRS.decode(srsID);
                String unit = referenceSystem.getCoordinateSystem().getAxis(1).getUnit().toString();

                if (fileType.equals("TIFF") && unit.equals("°")) {
                    // spatial resolution is given as angular distance
                    tmpResource = mapDatasetSpatialResolutionAsAngularDistance(metadataAct);
                    if (tmpResource != null) {
                        dataset.addProperty(Dataset.spatialResolution, tmpResource);
                    }
                }
                else {
                    // spatial resolution is given as metric distance
                    tmpResource = mapDatasetSpatialResolutionAsDistance(metadataAct);
                    if (tmpResource != null) {
                        dataset.addProperty(Dataset.spatialResolutionInMeters, tmpResource);
                    }
                }
            } catch (FactoryException | NullPointerException ignore) {}

            tmpResource = mapDatasetSpatialResolutionAsEquivalentScale(metadataAct);
            if (tmpResource != null) {
                dataset.addProperty(Dataset.spatialResolution, tmpResource);
            }

            tmpLiteralList = mapDatasetTemporalResolution(metadataAct);
            if (tmpLiteralList.size() > 0) {
                for (Literal literal : tmpLiteralList) {
                    dataset.addLiteral(Dataset.temporalResolution, literal);
                }
            }

            tmpResource = mapDatasetWasGeneratedBy(metadataAct);
            if (tmpResource != null) {
                dataset.addProperty(Dataset.wasGeneratedBy, tmpResource);
            }
        }

        return model;
    }

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

    private List<String> mapDatasetKeywordTag(MD_Metadata mdMetadata) {
        // 5 dcat:keyword
        List<String> datasetKeywordTag = new ArrayList<>();
        try {
            for (MD_Keywords keywords : mdMetadata.identificationInfo.get(0).descriptiveKeywords) {
                // all keyword types included (also theme, which is again used in 8 dcat:theme)
                if (keywords.keyword == null) {
                    continue;
                }
                datasetKeywordTag.addAll(keywords.keyword);
            }
        } catch (NullPointerException e) {
            datasetKeywordTag = null;
        }
        return datasetKeywordTag;
    }

    private Resource mapDatasetSpatialGeographicCoverage(MD_Metadata mdMetadata) {
        // 6 dct:spatial
        Resource datasetSpatialGeographicCoverage = null;
        try {
            if (mdMetadata.identificationInfo.get(0).extent.get(0).geographicElement.get(0) instanceof EX_GeographicBoundingBox) {
                double west = ((EX_GeographicBoundingBox) mdMetadata.identificationInfo.get(0).extent.get(0).geographicElement.get(0)).westBoundLongitude.get(0);
                double east = ((EX_GeographicBoundingBox) mdMetadata.identificationInfo.get(0).extent.get(0).geographicElement.get(0)).eastBoundLongitude.get(0);
                double south = ((EX_GeographicBoundingBox) mdMetadata.identificationInfo.get(0).extent.get(0).geographicElement.get(0)).southBoundLatitude.get(0);
                double north = ((EX_GeographicBoundingBox) mdMetadata.identificationInfo.get(0).extent.get(0).geographicElement.get(0)).northBoundLatitude.get(0);
                String bboxWkt = "POLYGON ((" + west + " " + south + ", " + east + " " + south + ", " + east + " " + north + ", " + west + " " + north + ", " + west + " " + south + "))";

                datasetSpatialGeographicCoverage = model.createResource()
                        .addProperty(RDF.type, Location.resourceInstance)
                        .addProperty(Location.boundingBox, model.createTypedLiteral(bboxWkt, NS.GSP + "wktLiteral"));
            }
        } catch (NullPointerException e) {
            datasetSpatialGeographicCoverage = null;
        }
        return datasetSpatialGeographicCoverage;
    }

    private Resource mapDatasetTemporalCoverage(MD_Metadata mdMetadata) {
        // 7 dct:temporal
        Resource datasetTemporalCoverage;
        try {
            TM_Period tmPeriod = (TM_Period) mdMetadata.identificationInfo.get(0).extent.get(0).temporalElement.get(0).extent.get(0);
            String begin = tmPeriod.begin.get(0).position.get(0);
            String end = tmPeriod.end.get(0).position.get(0);
            datasetTemporalCoverage = model.createResource()
                    .addProperty(RDF.type, PeriodOfTime.resourceInstance)
                    .addProperty(PeriodOfTime.startDate, model.createTypedLiteral(begin, XSDDatatype.XSDdateTime))
                    .addProperty(PeriodOfTime.endDate, model.createTypedLiteral(end, XSDDatatype.XSDdateTime));
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

    private List<Literal> mapDatasetTemporalResolution(MD_Metadata mdMetadata) {
        // 20 dcat:temporalResolution
        List<Literal> datasetTemporalResolutions = new ArrayList<>();
        try {
            List<String> temporalResolutions = new ArrayList<>(mdMetadata.identificationInfo.get(0).temporalResolution);
            for (String resolution : temporalResolutions) {
                datasetTemporalResolutions.add(model.createTypedLiteral(resolution, XSDDatatype.XSDduration));
            }
        } catch (NullPointerException ignore) {}
        return datasetTemporalResolutions;
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
}
