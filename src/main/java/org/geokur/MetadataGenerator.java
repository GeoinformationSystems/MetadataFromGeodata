/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD4;
import org.geokur.GeoDCAT.*;
import org.geokur.ISO19108Schema.TM_Period;
import org.geokur.ISO19115Schema.*;
import org.geokur.ISO191xxProfile.ProfileReader;
import org.geokur.generateMetadata.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class MetadataGenerator {
    static Model model = ModelFactory.createDefaultModel();
    static String fileType = null;

    public static void main(String[] argv) {
        // main method for testing metadata generation from geofiles

        if (argv.length == 0) {
            System.out.println("No attribute given. Please name a properties file.");
            System.exit(8);
        }

        String filenameProperties = argv[0];
        Properties properties = readProperties(filenameProperties);
        if (properties == null) {
            // error in properties file
            System.exit(8);
        }

        String displayFile = "File: " + properties.geodata;
        String displayLine = "-".repeat(displayFile.length());
        System.out.println(displayLine);
        System.out.println(displayFile);
        System.out.println(displayLine);

        // remove out files if existing (test cases)
        try {
            Files.deleteIfExists(new File(properties.outXML).toPath());
            Files.deleteIfExists(new File(properties.outDB).toPath());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        // read profile json file
        ProfileReader.setProfile(properties.profileFilename);

        // read metadata and instantiate according classes
        DS_Resource metadata;
        String[] fileNameExtension = properties.geodata.split("\\.");
        switch (fileNameExtension[fileNameExtension.length - 1]) {
            case "shp":
                fileType = "SHP";
                metadata = new ShapeMetadata(properties, new DS_DataSet()).getMetadata();
                break;
            case "gpkg":
                fileType = "GPKG";
                metadata = new GeopackageMetadata(properties, new DS_DataSet()).getMetadata();
                break;
            case "csv":
                fileType = "CSV";
                metadata = new AsciiMetadata(properties, new DS_DataSet()).getMetadata();
                break;
            case "tif":
            case "tiff":
                fileType = "TIFF";
                metadata = new GeoTIFFMetadata(properties, new DS_DataSet()).getMetadata();
                break;
            default:
                // file format not supported -> return empty document
                metadata = null;
                break;
        }

        // marshal to xml file
        try {
            JAXBContext contextObj = JAXBContext.newInstance(DS_DataSet.class);
            Marshaller marshaller = contextObj.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(metadata, new FileOutputStream(properties.outXML));
        } catch (FileNotFoundException | JAXBException e) {
//            System.out.println(e.getMessage());
            e.printStackTrace();
        }


        // order xml file to SQLite database
        // read xml file with JDOM2 library in order to get a document
        if (!properties.outDB.isEmpty()) {
            try {
                Document doc = new SAXBuilder().build(properties.outXML);
                Element docRoot = doc.getRootElement();
                MetadataDatabase metadataDatabase = new MetadataDatabase();
                metadataDatabase.generateFlatFromElement(docRoot);
                Database database = new Database(properties.outDB);
                database.createNewDatabase();
                database.addToDatabase(properties.geodata);
                database.writeMetadataToDatabase(properties.geodata, metadataDatabase);
            } catch (IOException | JDOMException e) {
//            System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }


        // map to linked data object
        if (!properties.outRDF.isEmpty()) {
            model.setNsPrefixes(NS.getNS());

            assert metadata != null;
            for (MD_Metadata metadataAct : metadata.has) {
                // loop over all datasets in metadata
                Resource tmpResource;
                List<Resource> tmpResourceList;
                String tmpString;
                List<String> tmpList;
                Literal tmpLiteral;

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

                tmpResourceList = mapDatasetRelatedResource(metadata.partOf, linkBase);
                if (tmpResourceList.size() > 0) {
                    for (Resource resource : tmpResourceList) {
                        dataset.addProperty(Dataset.relatedResource, resource);
                    }
                }

//            tmpResource = mapDatasetReferenceSystem(metadataAct);
//            if (tmpResource != null) {
//                dataset.addProperty(Dataset.referenceSystem, tmpResource);
//            }
//            tmpResource = mapDatasetSpatialResolutionAsAngularDistance(metadataAct);
//            if (tmpResource != null) {
//                dataset.addProperty(Dataset.spatialResolution, tmpResource);
//            }
//            tmpResource = mapDatasetSpatialResolutionAsDistance(metadataAct);
//            if (tmpResource != null) {
//                dataset.addProperty(Dataset.spatialResolution, tmpResource);
//            }
//            tmpResource = mapDatasetSpatialResolutionAsEquivalentScale(metadataAct);
//            if (tmpResource != null) {
//                dataset.addProperty(Dataset.spatialResolution, tmpResource);
//            }
//            tmpLiteral = mapDatasetTemporalResolution(metadataAct);
//            if (tmpLiteral != null) {
//                dataset.addProperty(Dataset.temporalResolution, tmpLiteral);
//            }
//            tmpResource = mapDatasetWasGeneratedBy(metadataAct);
//            if (tmpResource != null) {
//                dataset.addProperty(Dataset.wasGeneratedBy, tmpResource);
//            }
//            tmpResource = mapDatasetWasUsedBy(metadataAct);
//            if (tmpResource != null) {
//                dataset.addProperty(Dataset.wasUsedBy, tmpResource);
//            }
            }

            try {
                OutputStream outputStream = new FileOutputStream("test.ttl");
                RDFDataMgr.write(outputStream, model, Lang.TURTLE);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String mapDatasetTitle(MD_Metadata mdMetadata) {
        // 1 dct:title
        String datasetTitle;
        try {
            datasetTitle = mdMetadata.identificationInfo.get(0).citation.get(0).title.get(0);
        } catch (NullPointerException e) {
            datasetTitle = null;
        }
        return datasetTitle;
    }

    private static String mapDatasetDescription(MD_Metadata mdMetadata) {
        // 2 dct:description
        String datasetDescription;
        try {
            datasetDescription = mdMetadata.identificationInfo.get(0).abstractElement.get(0);
        } catch (NullPointerException e) {
            datasetDescription = null;
        }
        return datasetDescription;
    }

    private static Resource mapDatasetContactPoint(MD_Metadata mdMetadata) {
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

    private static Resource mapDatasetDistribution(MD_Metadata mdMetadata) {
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

    private static List<String> mapDatasetKeywordTag(MD_Metadata mdMetadata) {
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

    private static Resource mapDatasetSpatialGeographicCoverage(MD_Metadata mdMetadata) {
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

    private static Resource mapDatasetTemporalCoverage(MD_Metadata mdMetadata) {
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

    private static Resource mapDatasetThemeCategory(MD_Metadata mdMetadata) {
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

    private static Literal mapDatasetCreationDate(MD_Metadata mdMetadata) {
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

    private static List<Resource> mapDatasetDocumentation(MD_Metadata mdMetadata) {
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

    private static String mapDatasetIdentifier(MD_Metadata mdMetadata) {
        // 11 dct:identifier
        String datasetIdentifier;
        try {
            datasetIdentifier = mdMetadata.metadataIdentifier.get(0).code.get(0);
        } catch (NullPointerException e) {
            datasetIdentifier = null;
        }
        return datasetIdentifier;
    }

    private static Resource mapDatasetIsVersionOf(MD_Metadata mdMetadata) {
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

    private static List<Resource> mapDatasetLandingPage(MD_Metadata mdMetadata) {
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

    private static List<Resource> mapDatasetOtherIdentifier(MD_Metadata mdMetadata) {
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

    private static List<Resource> mapDatasetRelatedResource(List<DS_Aggregate> dsAggregates, String linkBase) {
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

    private static Resource mapDatasetReferenceSystem(MD_Metadata mdMetadata) {
        // 16 - dct:conformsTo
        Resource datasetReferenceSystem;
        try {
            datasetReferenceSystem = model.createResource("http://www.opengis.net/def/crs/EPSG/0/"); //TODO: add EPSG number
        } catch (NullPointerException e) {
            datasetReferenceSystem = null;
        }
        return datasetReferenceSystem;
    }

    private static Resource mapDatasetSpatialResolutionAsAngularDistance(MD_Metadata mdMetadata) {
        // 17 - dqv:hasQualityMeasurement -> geodcat:spatialResolutionAsAngularDistance -> dqv:value
        Resource datasetSpatialResolutionAsAngularDistance;
        try {
            datasetSpatialResolutionAsAngularDistance = model.createResource()
                    .addProperty(RDF.type, QualityMeasurement.resourceInstance)
                    .addProperty(QualityMeasurement.isMeasurementOf, SpatialResolutionAsAngularDistance.resourceInstance)
                    .addProperty(QualityMeasurement.unitOfMeasure, "degree")
                    .addProperty(QualityMeasurement.value, "NA");
        } catch (NullPointerException e) {
            datasetSpatialResolutionAsAngularDistance = null;
        }
        return datasetSpatialResolutionAsAngularDistance;
    }

    private static Resource mapDatasetSpatialResolutionAsDistance(MD_Metadata mdMetadata) {
        // 18 - dqv:hasQualityMeasurement -> geodcat:spatialResolutionAsDistance -> dqv:value
        Resource datasetSpatialResolutionAsDistance;
        try {
            datasetSpatialResolutionAsDistance = model.createResource()
                    .addProperty(RDF.type, QualityMeasurement.resourceInstance)
                    .addProperty(QualityMeasurement.isMeasurementOf, SpatialResolutionAsDistance.resourceInstance)
                    .addProperty(QualityMeasurement.unitOfMeasure, "degree")
                    .addProperty(QualityMeasurement.value, "NA");
        } catch (NullPointerException e) {
            datasetSpatialResolutionAsDistance = null;
        }
        return datasetSpatialResolutionAsDistance;
    }

    private static Resource mapDatasetSpatialResolutionAsEquivalentScale(MD_Metadata mdMetadata) {
        // 19 - dqv:hasQualityMeasurement -> geodcat:spatialResolutionAsScale -> dqv:value
        Resource datasetSpatialResolutionAsEquivalentScale;
        try {
            datasetSpatialResolutionAsEquivalentScale = model.createResource()
                    .addProperty(RDF.type, QualityMeasurement.resourceInstance)
                    .addProperty(QualityMeasurement.isMeasurementOf, SpatialResolutionAsEquivalentScale.resourceInstance)
                    .addProperty(QualityMeasurement.unitOfMeasure, "degree")
                    .addProperty(QualityMeasurement.value, "NA");
        } catch (NullPointerException e) {
            datasetSpatialResolutionAsEquivalentScale = null;
        }
        return datasetSpatialResolutionAsEquivalentScale;
    }

    private static Literal mapDatasetTemporalResolution(MD_Metadata mdMetadata) {
        // 20 dcat:temporalResolution
        Literal datasetTemporalResolution;
        try {
            datasetTemporalResolution = model.createTypedLiteral("NA", XSDDatatype.XSDduration);
        } catch (NullPointerException e) {
            datasetTemporalResolution = null;
        }
        return datasetTemporalResolution;
    }

    private static Resource mapDatasetWasGeneratedBy(MD_Metadata mdMetadata) {
        // 21 prov:wasGeneratedBy
        Resource datasetWasGeneratedBy;
        try {
            datasetWasGeneratedBy = model.createResource("NA");
        } catch (NullPointerException e) {
            datasetWasGeneratedBy = null;
        }
        return datasetWasGeneratedBy;
    }

    private static Resource mapDatasetWasUsedBy(MD_Metadata mdMetadata) {
        // 22 prov:wasGeneratedBy
        Resource datasetWasUsedBy;
        try {
            datasetWasUsedBy = model.createResource("NA");
        } catch (NullPointerException e) {
            datasetWasUsedBy = null;
        }
        return datasetWasUsedBy;
    }

    private static Properties readProperties(String filenameProperties) {
        // read properties file

        Properties properties = new Properties();

        String line;
        List<String> propertyName = new ArrayList<>();
        List<String> propertyContent = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filenameProperties));

            while ((line = br.readLine()) != null) {
                if (line.isBlank() || line.charAt(0) == '#') {
                    // comment - ignore
                    continue;
                }
                String[] lineParted = line.split("=", -1);
                propertyName.add(lineParted[0].trim().toLowerCase());
                propertyContent.add(lineParted[1].trim());
            }
            br.close();

            int idx;
            List<Integer> idx2;

            // profile can be empty -> standard profile with all elements available
            idx = propertyName.indexOf("profile");
            if (idx == -1) {
                System.out.println("No profile given - standard taken");
                properties.setProfileFilename("config/profileISOall.json");
            } else {
                properties.setProfileFilename(propertyContent.get(idx));
            }

            idx = propertyName.indexOf("geodata");
            if (idx == -1) {
                throw new ListContentException("geodata", filenameProperties);
            }
            properties.setGeodata(propertyContent.get(idx));

            idx = propertyName.indexOf("outxml");
            if (idx == -1) {
                throw new ListContentException("outXML", filenameProperties);
            }
            properties.setOutXML(propertyContent.get(idx));

            // outDB can be empty - no ListContentException and no db output
            idx = propertyName.indexOf("outdb");
            if (idx == -1) {
                properties.setOutDB("");
            }
            else {
                properties.setOutDB(propertyContent.get(idx));
            }

            // outRDF can be empty - no ListContentException and no rdf output
            idx = propertyName.indexOf("outrdf");
            if (idx == -1) {
                properties.setOutRDF("");
            }
            else {
                properties.setOutRDF(propertyContent.get(idx));
            }

            // asciiColNameIgnore can be empty - no ListContentException
            idx2 = getIndices(propertyName, "allowedfileformat");
            List<String> allowedFileFormats = new ArrayList<>();
            for (int i : idx2) {
                allowedFileFormats.add(propertyContent.get(i));
            }
            properties.setAllowedFileFormat(allowedFileFormats);

            String filename = properties.geodata;
            if (filename.startsWith("csv", filename.length() - 3)) {
                // in case of a csv file additional data shall/might be given

                idx = propertyName.indexOf("geodatareference");
                if (idx == -1) {
                    throw new ListContentException("geodataReference", filenameProperties);
                }
                properties.setGeodataReference(propertyContent.get(idx));

                idx2 = getIndices(propertyName, "geotablename");
                List<String> geoTableNames = new ArrayList<>();
                for (int i : idx2) {
                    geoTableNames.add(propertyContent.get(i));
                }
                if (idx2.size() == 0) {
                    throw new ListContentException("geoTableName", filenameProperties);
                }
                properties.setGeoTableNames(geoTableNames);

                idx2 = getIndices(propertyName, "geocolnamejoin");
                List<String> geoColNamesJoin = new ArrayList<>();
                for (int i : idx2) {
                    geoColNamesJoin.add(propertyContent.get(i));
                }
                if (idx2.size() == 0) {
                    throw new ListContentException("geoColNameJoin", filenameProperties);
                }
                properties.setGeoColNamesJoin(geoColNamesJoin);

                idx2 = getIndices(propertyName, "asciicolnamejoin");
                List<String> asciiColNamesJoin = new ArrayList<>();
                for (int i : idx2) {
                    asciiColNamesJoin.add(propertyContent.get(i));
                }
                if (idx2.size() == 0) {
                    throw new ListContentException("asciiColNameJoin", filenameProperties);
                }
                properties.setAsciiColNamesJoin(asciiColNamesJoin);

                idx2 = getIndices(propertyName, "asciicolnamedefine");
                List<String> asciiColNamesDefine = new ArrayList<>();
                for (int i : idx2) {
                    asciiColNamesDefine.add(propertyContent.get(i));
                }
                if (idx2.size() == 0) {
                    throw new ListContentException("asciiColNameDefine", filenameProperties);
                }
                properties.setAsciiColNamesDefine(asciiColNamesDefine);

                idx2 = getIndices(propertyName, "descriptionasciicolnamedefine");
                List<String> descriptionAsciiColNamesDefine = new ArrayList<>();
                for (int i : idx2) {
                    descriptionAsciiColNamesDefine.add(propertyContent.get(i));
                }
                if (idx2.size() == 0) {
                    throw new ListContentException("descriptionAsciiColNameDefine", filenameProperties);
                }
                properties.setDescriptionAsciiColNamesDefine(descriptionAsciiColNamesDefine);

                if (properties.asciiColNamesDefine.size() != properties.descriptionAsciiColNamesDefine.size()) {
                    // each ascii column definition must have a description
                    throw new Exception("Number of asciiColNameDefine and descriptionAsciiColNameDefine must be the same");
                }

                // asciiColNameIgnore can be empty - no ListContentException
                idx2 = getIndices(propertyName, "asciicolnameignore");
                List<String> asciiColNamesIgnore = new ArrayList<>();
                for (int i : idx2) {
                    asciiColNamesIgnore.add(propertyContent.get(i));
                }
                properties.setAsciiColNamesIgnore(asciiColNamesIgnore);

                // get postgres properties (if postgredsUse = true - otherwise do not use postgres database)
                // postgresUse is optional
                idx = propertyName.indexOf("postgresuse");
                if (idx == -1) {
                    properties.setPostgresUse(false);
                } else {
                    properties.setPostgresUse(Boolean.parseBoolean(propertyContent.get(idx)));
                }

                if (properties.postgresUse) {
                    idx = propertyName.indexOf("postgreshostname");
                    if (idx == -1) {
                        throw new ListContentException("postgresHostname", filenameProperties);
                    }
                    properties.setPostgresHostname(propertyContent.get(idx));

                    idx = propertyName.indexOf("postgresdatabase");
                    if (idx == -1) {
                        throw new ListContentException("postgresDatabase", filenameProperties);
                    }
                    properties.setPostgresDatabase(propertyContent.get(idx));

                    idx = propertyName.indexOf("postgresuser");
                    if (idx == -1) {
                        throw new ListContentException("postgresUser", filenameProperties);
                    }
                    properties.setPostgresUser(propertyContent.get(idx));

                    // postgresPasswd can be empty - no ListContentException
                    idx = propertyName.indexOf("postgrespasswd");
                    if (idx == -1) {
                        properties.setPostgresPasswd("");
                    } else {
                        properties.setPostgresPasswd(propertyContent.get(idx));
                    }

                    idx = propertyName.indexOf("postgrestable");
                    if (idx == -1) {
                        throw new ListContentException("postgresTable", filenameProperties);
                    }
                    properties.setPostgresTable(propertyContent.get(idx));

                    idx = propertyName.indexOf("coljoinnumerical");
                    if (idx == -1) {
                        throw new ListContentException("colJoinNumerical", filenameProperties);
                    }
                    properties.setColJoinNumerical(Boolean.parseBoolean(propertyContent.get(idx)));

                    idx2 = getIndices(propertyName, "coljoinsequential");
                    List<Boolean> colJoinSequential = new ArrayList<>();
                    for (int i : idx2) {
                        colJoinSequential.add(Boolean.parseBoolean(propertyContent.get(i)));
                    }
                    if (idx2.size() == 0) {
                        throw new ListContentException("colJoinSequential", filenameProperties);
                    }
                    properties.setColJoinSequential(colJoinSequential);
                }

                // apply thematic mapping if desired
                idx = propertyName.indexOf("thematicmapping");
                if (idx == -1) {
                    properties.setThematicMapping(false);
                } else {
                    properties.setThematicMapping(Boolean.parseBoolean(propertyContent.get(idx)));
                }

                if (properties.thematicMapping) {
                    idx = propertyName.indexOf("thematicmappingfile");
                    if (idx == -1) {
                        throw new ListContentException("thematicMappingFile", filenameProperties);
                    }
                    properties.setThematicMappingFile(propertyContent.get(idx));

                    idx = propertyName.indexOf("thematicmappingcolfrom");
                    if (idx == -1) {
                        throw new ListContentException("thematicMappingColFrom", filenameProperties);
                    }
                    properties.setThematicMappingColFrom(propertyContent.get(idx));

                    idx = propertyName.indexOf("thematicmappingcolto");
                    if (idx == -1) {
                        throw new ListContentException("thematicMappingColTo", filenameProperties);
                    }
                    properties.setThematicMappingColTo(propertyContent.get(idx));
                }
            }

        } catch (Exception e) {
            properties = null;
            System.out.println(e.getMessage());
        }

        return properties;
    }

    private static List<Integer> getIndices(List<String> list, String target) {
        // get list of indices for matching targets
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(target)) {
                indices.add(i);
            }
        }

        return indices;
    }
}
