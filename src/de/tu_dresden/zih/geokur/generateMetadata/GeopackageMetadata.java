/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package de.tu_dresden.zih.geokur.generateMetadata;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.Instant;
import java.util.*;

public class GeopackageMetadata {

    public Integer getContentNum(String fileName) {
        // get number of contents in geopackage (later use in getContent)
        GeopackageMetadata gpkg = new GeopackageMetadata();
        Connection connection = gpkg.getConnection(fileName);
        Statement statement = gpkg.getStatement(connection);
        Integer contentCt = 0;

        try {
            ResultSet tableContent = statement.executeQuery("SELECT table_name FROM gpkg_contents");
            while (tableContent.next()) {
                contentCt++;
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return contentCt;
    }

    public List<Element> getContent(String fileName, Integer contentAct, Map<String, Namespace> ns) {
        // read geopackage file and put its metadata into list of elements

        GeopackageMetadata gpkg = new GeopackageMetadata();
        Connection connection = gpkg.getConnection(fileName);
        Statement statement = gpkg.getStatement(connection);

        UUID id_DS_Resource = UUID.randomUUID();
        UUID id_has = UUID.randomUUID();
        UUID id_MD_Metadata = UUID.randomUUID();
        UUID id_contact = UUID.randomUUID();
        UUID id_CI_Responsibility = UUID.randomUUID();
        UUID id_role = UUID.randomUUID();
        UUID id_party = UUID.randomUUID();
        UUID id_CI_Individual = UUID.randomUUID();
        UUID id_name = UUID.randomUUID();

        List<Element> geopackageMeta = new ArrayList<>();
        geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "contact", "CI_Responsibility", "role"},
                new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_contact, id_CI_Responsibility, id_role}, "resourceProvider", ns));
        geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "contact", "CI_Responsibility", "party", "CI_Individual", "name"},
                new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_contact, id_CI_Responsibility, id_party, id_CI_Individual, id_name}, System.getProperty("user.name"), ns));

        UUID id_metadataIdentifier = UUID.randomUUID();
        UUID id_MD_Identifier = UUID.randomUUID();
        UUID id_code = UUID.randomUUID();

        String tableName = gpkg.getTableName(statement, contentAct);
        geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "metadataIdentifier", "MD_Identifier", "code"},
                new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_metadataIdentifier, id_MD_Identifier, id_code}, tableName, ns));

        UUID id_identificationInfo = UUID.randomUUID();
        UUID id_MD_DataIdentification = UUID.randomUUID();
        UUID id_spatialRepresentationType = UUID.randomUUID();

        String dataType = gpkg.getDataType(statement, contentAct);
        switch (dataType) {
            case ("features"):
                geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "spatialRepresentationType"},
                        new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_spatialRepresentationType}, "vector", ns));
                break;
            case ("2d-gridded-coverage"):
                UUID id_spatialRepresentationInfo = UUID.randomUUID();
                UUID id_MD_GridSpatialRepresentation = UUID.randomUUID();
                UUID id_numberOfDimensions = UUID.randomUUID();
                UUID id_axisDimensionProperties = UUID.randomUUID();
                UUID id_MD_DimensionRow = UUID.randomUUID();
                UUID id_dimensionNameRow = UUID.randomUUID();
                UUID id_dimensionSizeRow = UUID.randomUUID();
                UUID id_MD_DimensionCol = UUID.randomUUID();
                UUID id_dimensionNameCol = UUID.randomUUID();
                UUID id_dimensionSizeCol = UUID.randomUUID();
                UUID id_cellGeometry = UUID.randomUUID();
                UUID id_transformationParameterAvailability = UUID.randomUUID();

                geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "spatialRepresentationType"},
                        new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_spatialRepresentationType}, "grid", ns));
                geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "numberOfDimensions"},
                        new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_numberOfDimensions}, "2", ns));
                geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "axisDimensionProperties", "MD_Dimension", "dimensionName"},
                        new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_axisDimensionProperties, id_MD_DimensionRow, id_dimensionNameRow}, "row", ns));
                geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "axisDimensionProperties", "MD_Dimension", "dimensionSize"},
                        new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_axisDimensionProperties, id_MD_DimensionRow, id_dimensionSizeRow}, "39", ns));
                geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "axisDimensionProperties", "MD_Dimension", "dimensionName"},
                        new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_axisDimensionProperties, id_MD_DimensionCol, id_dimensionNameCol}, "column", ns));
                geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "axisDimensionProperties", "MD_Dimension", "dimensionSize"},
                        new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_axisDimensionProperties, id_MD_DimensionCol, id_dimensionSizeCol}, "41", ns));
                geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "cellGeometry"},
                        new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_cellGeometry}, "area", ns));
                geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "transformationParameterAvailability"},
                        new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_transformationParameterAvailability}, "0", ns));
                break;
        }

        UUID id_description = UUID.randomUUID();

        String description = gpkg.getDescription(statement, contentAct);
        geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "metadataIdentifier", "MD_Identifier", "description"},
                new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_metadataIdentifier, id_MD_Identifier, id_description}, description, ns));

        UUID id_dateInfo = UUID.randomUUID();
        UUID id_CI_Date = UUID.randomUUID();
        UUID id_dateType = UUID.randomUUID();
        UUID id_date = UUID.randomUUID();
        UUID id_DateTime = UUID.randomUUID();

        Instant lastChange = gpkg.getLastChange(statement, contentAct); // ISO 8601 date
        geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "dateInfo", "CI_Date", "dateType"},
                new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_dateInfo, id_CI_Date, id_dateType}, "creation", ns));
        geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "dateInfo", "CI_Date", "date", "DateTime"},
                new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_dateInfo, id_CI_Date, id_date, id_DateTime}, lastChange.toString(), ns));

        UUID id_extent = UUID.randomUUID();
        UUID id_EX_Extent = UUID.randomUUID();
        UUID id_geographicElement = UUID.randomUUID();
        UUID id_EX_GeographicBoundingBox = UUID.randomUUID();
        UUID id_westBoundLongitude = UUID.randomUUID();
        UUID id_eastBoundLongitude = UUID.randomUUID();
        UUID id_southBoundLatitude = UUID.randomUUID();
        UUID id_northBoundLatitude = UUID.randomUUID();

        Integer[] extent = gpkg.getExtent(statement, contentAct);
        geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "westBoundLongitude"},
                new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_geographicElement, id_EX_GeographicBoundingBox, id_westBoundLongitude}, extent[0].toString(), ns));
        geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "eastBoundLongitude"},
                new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_geographicElement, id_EX_GeographicBoundingBox, id_eastBoundLongitude}, extent[1].toString(), ns));
        geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "southBoundLatitude"},
                new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_geographicElement, id_EX_GeographicBoundingBox, id_southBoundLatitude}, extent[2].toString(), ns));
        geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "northBoundLatitude"},
                new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_geographicElement, id_EX_GeographicBoundingBox, id_northBoundLatitude}, extent[3].toString(), ns));

        UUID id_referenceSystemInfo = UUID.randomUUID();
        UUID id_MD_ReferenceSystem = UUID.randomUUID();
        UUID id_referenceSystemIdentifier = UUID.randomUUID();
        UUID id_MD_IdentifierSRS = UUID.randomUUID();
        UUID id_codeSRS = UUID.randomUUID();
        UUID id_codeSpaceSRS = UUID.randomUUID();

        Integer srsID = gpkg.getSRSID(statement, contentAct);
        String srsOrganization = gpkg.getSRSOrganization(statement, srsID);
        Integer srsOrganizationCoordsysID = gpkg.getSRSOrganizationCoordsysID(statement, srsID);
        geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "referenceSystemInfo", "MD_ReferenceSystem", "referenceSystemIdentifier", "MD_Identifier", "code"},
                new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_referenceSystemInfo, id_MD_ReferenceSystem, id_referenceSystemIdentifier, id_MD_IdentifierSRS, id_codeSRS}, srsOrganization + "::" + srsOrganizationCoordsysID, ns));
        geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "referenceSystemInfo", "MD_ReferenceSystem", "referenceSystemIdentifier", "MD_Identifier", "codeSpace"},
                new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_referenceSystemInfo, id_MD_ReferenceSystem, id_referenceSystemIdentifier, id_MD_IdentifierSRS, id_codeSpaceSRS}, srsOrganization, ns));

        UUID id_descriptionSRS = UUID.randomUUID();

        String srsName = gpkg.getSRSName(statement, srsID);
        geopackageMeta.add(createNestedElement(new String[] {"DS_Resource", "has", "MD_Metadata", "referenceSystemInfo", "MD_ReferenceSystem", "referenceSystemIdentifier", "MD_Identifier", "description"},
                new UUID[] {id_DS_Resource, id_has, id_MD_Metadata, id_referenceSystemInfo, id_MD_ReferenceSystem, id_referenceSystemIdentifier, id_MD_IdentifierSRS, id_descriptionSRS}, srsName, ns));


        // read BLOB content of geopackage
        gpkg.getRasterContent(statement, tableName);


        return geopackageMeta;

        // code for building a complete tree with all available metadata fields included
        // get one content of a geopackage and build a DOM with metadata according to ISO 19115
//        UUID id_root = UUID.randomUUID();
//        Element geopackageStructure = new Element("root");
//        geopackageStructure.setAttribute("UUID", id_root.toString());

//        geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "contact", "CI_Responsibility", "role"},
//                new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_contact, id_CI_Responsibility, id_role}, "resourceProvider");
//        geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "contact", "CI_Responsibility", "party", "CI_Individual", "name"},
//                new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_contact, id_CI_Responsibility, id_party, id_CI_Individual, id_name}, System.getProperty("user.name"));
//        geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "metadataIdentifier", "MD_Identifier", "code"},
//                new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_metadataIdentifier, id_MD_Identifier, id_code}, tableName);
//                geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "spatialRepresentationType"},
//                        new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_spatialRepresentationType}, "vector");
//                geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "spatialRepresentationType"},
//                        new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_spatialRepresentationType}, "grid");
//                geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "numberOfDimensions"},
//                        new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_numberOfDimensions}, "2");
//                geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "axisDimensionProperties", "MD_Dimension", "dimensionName"},
//                        new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_axisDimensionProperties, id_MD_DimensionRow, id_dimensionNameRow}, "row");
//                geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "axisDimensionProperties", "MD_Dimension", "dimensionSize"},
//                        new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_axisDimensionProperties, id_MD_DimensionRow, id_dimensionSizeRow}, "39");
//                geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "axisDimensionProperties", "MD_Dimension", "dimensionName"},
//                        new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_axisDimensionProperties, id_MD_DimensionCol, id_dimensionNameCol}, "column");
//                geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "axisDimensionProperties", "MD_Dimension", "dimensionSize"},
//                        new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_axisDimensionProperties, id_MD_DimensionRow, id_dimensionSizeCol}, "41");
//                geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "cellGeometry"},
//                        new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_cellGeometry}, "area");
//                geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "spatialRepresentationInfo", "MD_GridSpatialRepresentation", "transformationParameterAvailability"},
//                        new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_spatialRepresentationInfo, id_MD_GridSpatialRepresentation, id_transformationParameterAvailability}, "0");
//        geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "metadataIdentifier", "MD_Identifier", "description"},
//                new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_metadataIdentifier, id_MD_Identifier, id_description}, description);
//        geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "dateInfo", "CI_Date", "dateType"},
//                new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_dateInfo, id_CI_Date, id_dateType}, "creation");
//        geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "dateInfo", "CI_Date", "date", "DateTime"},
//                new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_dateInfo, id_CI_Date, id_date, id_DateTime}, lastChange.toString());
//        geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "westBoundLongitude"},
//                new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_geographicElement, id_EX_GeographicBoundingBox, id_westBoundLongitude}, extent[0].toString());
//        geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "eastBoundLongitude"},
//                new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_geographicElement, id_EX_GeographicBoundingBox, id_eastBoundLongitude}, extent[1].toString());
//        geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "southBoundLatitude"},
//                new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_geographicElement, id_EX_GeographicBoundingBox, id_southBoundLatitude}, extent[2].toString());
//        geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "identificationInfo", "MD_DataIdentification", "extent", "EX_Extent", "geographicElement", "EX_GeographicBoundingBox", "northBoundLatitude"},
//                new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_identificationInfo, id_MD_DataIdentification, id_extent, id_EX_Extent, id_geographicElement, id_EX_GeographicBoundingBox, id_northBoundLatitude}, extent[3].toString());
//        geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "referenceSystemInfo", "MD_ReferenceSystem", "referenceSystemIdentifier", "MD_Identifier", "code"},
//                new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_referenceSystemInfo, id_MD_ReferenceSystem, id_referenceSystemIdentifier, id_MD_IdentifierSRS, id_codeSRS}, srsOrganization + "::" + srsOrganizationCoordsysID);
//        geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "referenceSystemInfo", "MD_ReferenceSystem", "referenceSystemIdentifier", "MD_Identifier", "codeSpace"},
//                new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_referenceSystemInfo, id_MD_ReferenceSystem, id_referenceSystemIdentifier, id_MD_IdentifierSRS, id_codeSpaceSRS}, srsOrganization);
//        geopackageStructure = complementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "referenceSystemInfo", "MD_ReferenceSystem", "referenceSystemIdentifier", "MD_Identifier", "description"},
//                new UUID[] {id_root, id_DS_Resource, id_has, id_MD_Metadata, id_referenceSystemInfo, id_MD_ReferenceSystem, id_referenceSystemIdentifier, id_MD_IdentifierSRS, id_descriptionSRS}, srsName);

//        return geopackageStructure;
    }

    private Element createNestedElement(String[] nameChain, UUID[] idChain, String value, Map<String, Namespace> ns) {
        // creation of one metadata entry (whole nested chain to be included in overall metadata using ComplementNestedElement)
        int nameChainLength = nameChain.length;

        // get all namespaces from nameChain from original config files
        // additional add information about obligation and occurrence
        List<Namespace> namespaceList = new ArrayList<>();
        List<String> obligation = new ArrayList<>();
        List<String> occurrence = new ArrayList<>();
        String configFile;
        Element configRootElement = null;
        String configRootNamespace = null;

        obligation.add("M"); // fill first entry for DS_Resource (not in files explicitly given)
        occurrence.add("1");

        for (int i = 0; i < java.lang.Math.floor((double) nameChainLength/2); i++) {
            configFile = "config/config_" + nameChain[i*2] + ".xml";
            try {
                configRootElement = new SAXBuilder().build(configFile).getRootElement();
            }
            catch (JDOMException | IOException e) {
                System.out.println(e.getMessage());
            }

            if (configRootElement != null) {
                configRootNamespace = configRootElement.getAttributeValue("namespace");
                List<Element> configRootChildren = configRootElement.getChildren("element");
                List<String> obligationOccurrence = getObligationOccurrence(configRootChildren, nameChain[2*i + 1]);

                obligation.add(obligationOccurrence.get(0)); // two times because subelements of classes have the same obligation and occurrence as the class itself
                obligation.add(obligationOccurrence.get(0));
                occurrence.add(obligationOccurrence.get(1));
                occurrence.add(obligationOccurrence.get(1));
            }

            namespaceList.add(ns.get(configRootNamespace)); // two times because subelements of classes have the same namespace as the class itself
            namespaceList.add(ns.get(configRootNamespace));
        }
        if (namespaceList.size()==nameChainLength-1) {
            // add one last element - uneven lengths occur in case of codelists and enumerations -> hard coded namespace xs
            namespaceList.add(ns.get("xs"));
        }

        // build element chain with particular metadata
        Element element = new Element(nameChain[nameChainLength - 1]);
        element.setNamespace(namespaceList.get(nameChainLength - 1));
        element.addContent(value);
        element.setAttribute("obligation", obligation.get(nameChainLength - 1));
        element.setAttribute("occurrence", occurrence.get(nameChainLength - 1));
        element.setAttribute("UUID", idChain[nameChainLength - 1].toString());
        Element elementTmp;
        for (int i = nameChainLength - 2; i >= 0; i--) {
            elementTmp = element;
            element.removeChild(nameChain[i + 1]);
            element = new Element(nameChain[i]);
            element.setNamespace(namespaceList.get(i));
            element.addContent(elementTmp);
            element.setAttribute("obligation", obligation.get(i));
            element.setAttribute("occurrence", occurrence.get(i));
            element.setAttribute("UUID", idChain[i].toString());
        }

        return element;
    }

    private List<String> getObligationOccurrence(List<Element> inElement, String inElementName) {
        // get obligation and occurrence properties of a particular element in a list
        List<String> obligationOccurrence = new ArrayList<>();

        for (Element inElementAct : inElement) {
            if (inElementAct.getChildText("name").equals(inElementName)) {
                obligationOccurrence.add(inElementAct.getChildText("obligation"));
                obligationOccurrence.add(inElementAct.getChildText("occurrence"));
                break;
            }
        }
        if (obligationOccurrence.isEmpty()) {
            System.out.println("For element " + inElementName + " no obligation and occurrence properties were found.");
        }

        return obligationOccurrence;
    }

    private Element complementNestedElement(Element nestedElement, String[] nameChain, UUID[] idChain, String value, Map<String, Namespace> ns) {
        // create actual metadata and add to overall structure in nested elements (DOM)
        int nameChainLength = nameChain.length;
        Element complementElementTmp = createNestedElement(nameChain, idChain, value, ns);
        for (int i = 1; i < nameChainLength; i++) {
            if (nestedElement.getChild(nameChain[i])==null) {
                // the actual element is not available -> stop here and complement element
                nestedElement.addContent(complementElementTmp.getChild(nameChain[i]).clone());

                while (!(nestedElement.getParent()==null)) {
                    nestedElement = nestedElement.getParentElement();
                }
                break;
            }
            else if (!nestedElement.getChild(nameChain[i]).getAttributeValue("UUID").equals(idChain[i].toString())) {
                // the actual element is available, but with a wrong UUID -> stop here and complement element
                nestedElement.addContent(complementElementTmp.getChild(nameChain[i]).clone());

                while (!(nestedElement.getParent()==null)) {
                    nestedElement = nestedElement.getParentElement();
                }
                break;
            }
            else {
                // the actual element with the correct UUID is available -> dive into child
                complementElementTmp = complementElementTmp.getChild(nameChain[i]);
                nestedElement = nestedElement.getChild(nameChain[i]);
            }
        }

        return nestedElement;
    }


    private Connection getConnection(String fileName) {
        Connection connection = null;
        try {
            String url = "jdbc:sqlite:" + fileName;
            connection = DriverManager.getConnection(url);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    private Statement getStatement(Connection connection) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return stmt;
    }


    private String getTableName(Statement stmt, Integer contentAct) {
        String tableName = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT table_name FROM gpkg_contents");
            for (int i = 0; i < contentAct; i++) {
                tableContent.next();
            }
            tableName = tableContent.getString(1);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tableName;
    }

    private String getDataType(Statement stmt, Integer contentAct) {
        String dataType = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT data_type FROM gpkg_contents");
            for (int i = 0; i < contentAct; i++) {
                tableContent.next();
            }
            dataType = tableContent.getString(1);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dataType;
    }

    private String getIdentifier(Statement stmt, Integer contentAct) {
        String identifier = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT identifier FROM gpkg_contents");
            for (int i = 0; i < contentAct; i++) {
                tableContent.next();
            }
            identifier = tableContent.getString(1);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return identifier;
    }

    private String getDescription(Statement stmt, Integer contentAct) {
        String description = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT description FROM gpkg_contents");
            for (int i = 0; i < contentAct; i++) {
                tableContent.next();
            }
            description = tableContent.getString(1);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return description;
    }

    private Instant getLastChange(Statement stmt, Integer contentAct) {
        // get date from ISO 8601 format in standard geopackage
        Instant lastChange = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT last_change FROM gpkg_contents");
            for (int i = 0; i < contentAct; i++) {
                tableContent.next();
            }
            String lastChangeString = tableContent.getString(1);
            lastChange = Instant.parse(lastChangeString);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lastChange;
    }

    private Integer[] getExtent(Statement stmt, Integer contentAct) {
        Integer[] extent = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT min_x, min_y, max_x, max_y FROM gpkg_contents");
            for (int i = 0; i < contentAct; i++) {
                tableContent.next();
            }
            extent = new Integer[] {tableContent.getInt("min_x"), tableContent.getInt("max_x"), tableContent.getInt("min_y"), tableContent.getInt("max_y")};
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return extent;
    }

    private Integer getSRSID(Statement stmt, Integer contentAct) {
        Integer srsID = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT srs_id FROM gpkg_contents");
            for (int i = 0; i < contentAct; i++) {
                tableContent.next();
            }
            srsID = tableContent.getInt(1);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return srsID;
    }

    private String getSRSName(Statement stmt, Integer usedSRSID) {
        String srsName = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT srs_id, srs_name FROM gpkg_spatial_ref_sys");
            List<Integer> srsIDAll = new ArrayList<>();
            List<String> srsNameAll = new ArrayList<>();
            while (tableContent.next()) {
                srsIDAll.add(tableContent.getInt("srs_id"));
                srsNameAll.add(tableContent.getString("srs_name"));
            }
            int idx = srsIDAll.indexOf(usedSRSID);
            srsName = srsNameAll.get(idx);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return srsName;
    }

    private String getSRSOrganization(Statement stmt, Integer usedSRSID) {
        String srsOrganization = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT srs_id, organization FROM gpkg_spatial_ref_sys");
            List<Integer> srsIDAll = new ArrayList<>();
            List<String> srsOrganizationAll = new ArrayList<>();
            while (tableContent.next()) {
                srsIDAll.add(tableContent.getInt("srs_id"));
                srsOrganizationAll.add(tableContent.getString("organization"));
            }
            int idx = srsIDAll.indexOf(usedSRSID);
            srsOrganization = srsOrganizationAll.get(idx);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return srsOrganization;
    }

    private Integer getSRSOrganizationCoordsysID(Statement stmt, Integer usedSRSID) {
        Integer srsOrganizationCoordsysID = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT srs_id, organization_coordsys_id FROM gpkg_spatial_ref_sys");
            List<Integer> srsIDAll = new ArrayList<>();
            List<Integer> srsOrganizationCoordsysIDAll = new ArrayList<>();
            while (tableContent.next()) {
                srsIDAll.add(tableContent.getInt("srs_id"));
                srsOrganizationCoordsysIDAll.add(tableContent.getInt("organization_coordsys_id"));
            }
            int idx = srsIDAll.indexOf(usedSRSID);
            srsOrganizationCoordsysID = srsOrganizationCoordsysIDAll.get(idx);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return srsOrganizationCoordsysID;
    }

    private String getSRSDefinition(Statement stmt, Integer usedSRSID) {
        String srsDefinition = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT srs_id, definition FROM gpkg_spatial_ref_sys");
            List<Integer> srsIDAll = new ArrayList<>();
            List<String> srsDefinitionAll = new ArrayList<>();
            while (tableContent.next()) {
                srsIDAll.add(tableContent.getInt("srs_id"));
                srsDefinitionAll.add(tableContent.getString("definition"));
            }
            int idx = srsIDAll.indexOf(usedSRSID);
            srsDefinition = srsDefinitionAll.get(idx);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return srsDefinition;
    }

    private List<String> getTableColNames(Statement stmt, String tableName) {
        List<String> tableColNames = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData colNames = tableContent.getMetaData();
            List<String> colNamesAct = new ArrayList<>();
            for (int i = 1; i <= colNames.getColumnCount(); i++) {
                colNamesAct.add(colNames.getColumnName(i));
            }
            tableColNames = colNamesAct;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tableColNames;
    }

    private Integer getTableRowNum(Statement stmt, String tableName) {
        Integer tableRowNum = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT * FROM " + tableName);
            int ct = 0;
            while (tableContent.next()) {
                ++ct;
            }
            tableRowNum = ct;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tableRowNum;
    }


    private void getRasterContent(Statement stmt, String tableName) {
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT tile_data FROM " + tableName);
            if (tableContent.next()) {
                byte[] by = tableContent.getBytes(1);
                byte[] magicByte = Arrays.copyOfRange(by, 0, 2);
                String magic = new String(magicByte, StandardCharsets.UTF_8); // ascii GP

                byte versionByte = by[2];
                Integer version = (int) versionByte;
                byte flagsByte = by[3];
                byte[] flagsUnpack = unpack(flagsByte);
                int envHeader = (int) ((int) flagsUnpack[6] * Math.pow(2, 0) + (int) flagsUnpack[5] * Math.pow(2, 1) + flagsUnpack[4] * Math.pow(2, 2)); // envelope contents indicator
                String endianness;
                if (flagsUnpack[7]==0) {
                    // byte order for header values
                    endianness = "BIG_ENDIAN";
                }
                else {
                    endianness = "LITTLE_ENDIAN";
                }
                ByteBuffer srsIDByte = ByteBuffer.wrap(Arrays.copyOfRange(by, 4, 8));
                switch (endianness) {
                    case ("BIG_ENDIAN"):
                        srsIDByte.order(ByteOrder.BIG_ENDIAN);
                        break;
                    case ("LITTLE_ENDIAN"):
                        srsIDByte.order(ByteOrder.LITTLE_ENDIAN);
                }
                int srsID = srsIDByte.getInt();

                System.out.println("yes");

//            ByteBuffer bb = ByteBuffer.wrap(new byte[] {0,0,0,-128});
//            bb.order(ByteOrder.BIG_ENDIAN);
//            int v = bb.getInt();
//            byte[] bs = BitSet.valueOf(bb).toByteArray();




//                Blob blobContent = tableContent.getBlob("tile_data");
//                long blobLength = blobContent.length();
//
//                int pos = 1; // position is 1-based
//                int len = 10;
//                byte[] bytes = blobContent.getBytes(pos, len);
//
//                InputStream is = blobContent.getBinaryStream();
//                int b = is.read();
            }

            System.out.println("yes");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static byte[] unpack(byte val) {
        byte[] result = new byte[8];
        for(int i = 0; i < 8; i++) {
            result[i] = (byte) ((val >> (7 - i)) & 1);
        }
        return result;
    }


    public static byte pack(byte[] vals) {
        byte result = 0;
        for (byte bit : vals)
            result = (byte)((result << 1) | (bit & 1));
        return result;
    }

}
