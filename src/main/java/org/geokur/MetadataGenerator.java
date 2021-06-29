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
import org.geotools.referencing.CRS;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import javax.validation.constraints.Null;
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
            Files.deleteIfExists(new File(properties.outRDF).toPath());
        } catch (IOException ignored) {}


        // read profile json file
        if (!properties.profileFilename.equals("-999")) {
            // profile given
            ProfileReader.setProfile(properties.profileFilename);
        }
        else {
            // no profile given - take standard
            ProfileReader.setProfileStandard();
        }


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
        if (!properties.outRDF.isEmpty() && metadata != null) {
            model.setNsPrefixes(NS.getNS());

            MappedMetadata mappedMetadata = new MappedMetadata(model, fileType, properties.rdfLinkBase);
            model = mappedMetadata.fillModel(metadata);

            try {
                OutputStream outputStream = new FileOutputStream(properties.outRDF);
                RDFDataMgr.write(outputStream, model, Lang.TURTLE);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                properties.setProfileFilename("-999");
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

            // rdfLinkBase can be empty if no rdf output is requested - otherwise mandatory
            idx = propertyName.indexOf("rdflinkbase");
            if (!properties.outRDF.isEmpty() && idx == -1) {
                throw new ListContentException("rdfLinkBase", filenameProperties);
            }
            else {
                properties.setRdfLinkBase(propertyContent.get(idx));
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
