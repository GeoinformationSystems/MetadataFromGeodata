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

        String filenameProperties = argv[0];
        Properties properties = readProperties(filenameProperties);

        // remove out files if existing (test cases)
        try {
            Files.deleteIfExists(new File(properties.filenameXml).toPath());
            Files.deleteIfExists(new File(properties.filenameDB).toPath());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        // read profile json file
        ProfileReader.setProfile(properties.profileFilename);

        // read metadata and instantiate according classes
        DS_Resource metadata;
        String[] fileNameExtension = properties.filename.split("\\.");
        // todo: add other geodata types
        switch (fileNameExtension[fileNameExtension.length - 1]) {
            case "shp":
                System.out.println("-------------");
                System.out.println("Shape content ");
                System.out.println("-------------");
                metadata = new ShapeMetadata(properties.filename, new DS_DataSet()).getMetadata();
                break;
            case "gpkg":
                System.out.println("------------------");
                System.out.println("Geopackage content");
                System.out.println("------------------");
                metadata = new GeopackageMetadata(properties.filename, new DS_DataSet()).getMetadata();
                break;
            case "csv":
                System.out.println("-------------");
                System.out.println("Ascii content");
                System.out.println("-------------");
                AsciiMetadata asciiMetadata = new AsciiMetadata(properties.filename, new DS_DataSet());
                asciiMetadata.defineProperties(properties.geodataReference, properties.geoTableNames, properties.geoColNamesJoin,
                        properties.asciiColNamesJoin, properties.asciiColNamesDefine, properties.asciiColNamesIgnore,
                        properties.descriptionAsciiColNamesDefine);
                metadata = asciiMetadata.getMetadata();
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
            marshaller.marshal(metadata, new FileOutputStream(properties.filenameXml));
        } catch (JAXBException | FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        // order xml file to SQLite database
        // read xml file with JDOM2 library in order to get a documentl
        try {
            Document doc = new SAXBuilder().build(properties.filenameXml);
            Element docRoot = doc.getRootElement();
            MetadataDatabase metadataDatabase = new MetadataDatabase();
            metadataDatabase.generateFlatFromElement(docRoot);
            Database database = new Database(properties.filenameDB);
            database.createNewDatabase();
            database.addToDatabase(properties.filename);
            database.writeMetadataToDatabase(properties.filename, metadataDatabase);
        } catch (IOException | JDOMException e) {
            System.out.println(e.getMessage());
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

            int idx;
            idx = propertyName.indexOf("profile");
            if (idx == -1) {
                throw new ListContentException("profile", filenameProperties);
            }
            properties.setProfileFilename(propertyContent.get(idx));
            idx = propertyName.indexOf("geodata");
            if (idx == -1) {
                throw new ListContentException("geodata", filenameProperties);
            }
            properties.setFilename(propertyContent.get(idx));
            idx = propertyName.indexOf("outxml");
            if (idx == -1) {
                throw new ListContentException("outXML", filenameProperties);
            }
            properties.setFilenameXml(propertyContent.get(idx));
            idx = propertyName.indexOf("outdb");
            if (idx == -1) {
                throw new ListContentException("outDB", filenameProperties);
            }
            properties.setFilenameDB(propertyContent.get(idx));

            String filename = properties.filename;
            if (filename.substring(filename.length() - 3, filename.length()).equals("csv")) {
                // in case of a csv file additional data shall/might be given
                List<Integer> idx2;

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

class Properties {
    String profileFilename;
    String filename;
    String filenameXml;
    String filenameDB;
    String geodataReference;
    List<String> geoTableNames;
    List<String> geoColNamesJoin;
    List<String> asciiColNamesJoin;
    List<String> asciiColNamesDefine;
    List<String> descriptionAsciiColNamesDefine;
    List<String> asciiColNamesIgnore;


    Properties(){}

    public void setProfileFilename(String profileFilename) {
        this.profileFilename = profileFilename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFilenameXml(String filenameXml) {
        this.filenameXml = filenameXml;
    }

    public void setFilenameDB(String filenameDB) {
        this.filenameDB = filenameDB;
    }

    public void setGeodataReference(String geodataReference) {
        this.geodataReference = geodataReference;
    }

    public void setGeoTableNames(List<String> geoTableNames) {
        this.geoTableNames = geoTableNames;
    }

    public void setGeoColNamesJoin(List<String> geoColNamesJoin) {
        this.geoColNamesJoin = geoColNamesJoin;
    }

    public void setAsciiColNamesJoin(List<String> asciiColNamesJoin) {
        this.asciiColNamesJoin = asciiColNamesJoin;
    }

    public void setAsciiColNamesDefine(List<String> asciiColNamesDefine) {
        this.asciiColNamesDefine = asciiColNamesDefine;
    }

    public void setDescriptionAsciiColNamesDefine(List<String> descriptionAsciiColNamesDefine) {
        this.descriptionAsciiColNamesDefine = descriptionAsciiColNamesDefine;
    }

    public void setAsciiColNamesIgnore(List<String> asciiColNamesIgnore) {
        this.asciiColNamesIgnore = asciiColNamesIgnore;
    }
}