/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur;

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
    public static void main(String[] argv) {
        // main method for testing metadata generation from geofiles

        if (argv.length == 0) {
            System.out.println("No attribute given. Please name a properties file.");
            System.exit(8);
        }

        String filenameProperties = argv[0];
        Properties properties = readProperties(filenameProperties);

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
//                metadata = new ShapeMetadata(properties.geodata, new DS_DataSet()).getMetadata();
                metadata = new ShapeMetadata(properties, new DS_DataSet()).getMetadata();
                break;
            case "gpkg":
//                metadata = new GeopackageMetadata(properties.geodata, new DS_DataSet()).getMetadata();
                metadata = new GeopackageMetadata(properties, new DS_DataSet()).getMetadata();
                break;
            case "csv":
                metadata = new AsciiMetadata(properties, new DS_DataSet()).getMetadata();
                break;
            case "tif":
            case "tiff":
//                metadata = new GeoTIFFMetadata(properties.geodata, new DS_DataSet()).getMetadata();
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

        // marshal to json file
//        try {
//            jakarta.xml.bind.JAXBContext contextObj = jakarta.xml.bind.JAXBContext.newInstance(DS_DataSet.class);
//            jakarta.xml.bind.Marshaller marshaller = contextObj.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            marshaller.setProperty("eclipselink.media-type", "application/json");
//            marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
//            marshaller.marshal(metadata, new FileOutputStream("test.json"));
            // TODO: jaxb.properties must be copied in the target folder with all classes
            // TODO: manifest?
            // JAXBContext loaded from other classloader

//            JAXBContext contextObj = JAXBContext.newInstance(DS_DataSet.class);
//            Marshaller marshaller = contextObj.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
//            marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
//            marshaller.marshal(metadata, new FileOutputStream("test.json"));
//        } catch (FileNotFoundException | JAXBException e) {
//            e.printStackTrace();
//        }

        // order xml file to SQLite database
        // read xml file with JDOM2 library in order to get a document
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

            idx = propertyName.indexOf("profile");
            if (idx == -1) {
                throw new ListContentException("profile", filenameProperties);
            }
            properties.setProfileFilename(propertyContent.get(idx));
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
            idx = propertyName.indexOf("outdb");
            if (idx == -1) {
                throw new ListContentException("outDB", filenameProperties);
            }
            properties.setOutDB(propertyContent.get(idx));

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

        } catch (IOException | ListContentException e) {
            properties.setProfileFilename(null);
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

//class Properties {
//    String profileFilename;
//    String filename;
//    String filenameXml;
//    String filenameDB;
//    String geodataReference;
//    List<String> geoTableNames;
//    List<String> geoColNamesJoin;
//    List<String> asciiColNamesJoin;
//    List<String> asciiColNamesDefine;
//    List<String> descriptionAsciiColNamesDefine;
//    List<String> asciiColNamesIgnore;
//    String postgresHostname;
//    String postgresDatabase;
//    String postgresUser;
//    String postgresPasswd;
//
//
//    Properties(){}
//
//    public void setProfileFilename(String profileFilename) {
//        this.profileFilename = profileFilename;
//    }
//
//    public void setFilename(String filename) {
//        this.filename = filename;
//    }
//
//    public void setFilenameXml(String filenameXml) {
//        this.filenameXml = filenameXml;
//    }
//
//    public void setFilenameDB(String filenameDB) {
//        this.filenameDB = filenameDB;
//    }
//
//    public void setGeodataReference(String geodataReference) {
//        this.geodataReference = geodataReference;
//    }
//
//    public void setGeoTableNames(List<String> geoTableNames) {
//        this.geoTableNames = geoTableNames;
//    }
//
//    public void setGeoColNamesJoin(List<String> geoColNamesJoin) {
//        this.geoColNamesJoin = geoColNamesJoin;
//    }
//
//    public void setAsciiColNamesJoin(List<String> asciiColNamesJoin) {
//        this.asciiColNamesJoin = asciiColNamesJoin;
//    }
//
//    public void setAsciiColNamesDefine(List<String> asciiColNamesDefine) {
//        this.asciiColNamesDefine = asciiColNamesDefine;
//    }
//
//    public void setDescriptionAsciiColNamesDefine(List<String> descriptionAsciiColNamesDefine) {
//        this.descriptionAsciiColNamesDefine = descriptionAsciiColNamesDefine;
//    }
//
//    public void setAsciiColNamesIgnore(List<String> asciiColNamesIgnore) {
//        this.asciiColNamesIgnore = asciiColNamesIgnore;
//    }
//
//    public void setPostgresHostname(String postgresHostname) {
//        this.postgresHostname = postgresHostname;
//    }
//
//    public void setPostgresDatabase(String postgresDatabase) {
//        this.postgresDatabase = postgresDatabase;
//    }
//
//    public void setPostgresUser(String postgresUser) {
//        this.postgresUser = postgresUser;
//    }
//
//    public void setPostgresPasswd(String postgresPasswd) {
//        this.postgresPasswd = postgresPasswd;
//    }
//}