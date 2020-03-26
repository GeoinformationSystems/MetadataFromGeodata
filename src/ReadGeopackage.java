/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

import org.jdom2.Element;

import java.sql.*;
import java.util.*;

public class ReadGeopackage {

    public Integer getContentNum(String fileName) {
        // get number of contents in geopackage (later use in getContent)
        ReadGeopackage gpkg = new ReadGeopackage();
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

    public Element getContent(String fileName, Integer contentAct) {
        // get one content of a geopackage and build a DOM with metadata according to ISO 19115
        Element geopackageStructure = new Element("root");

        ReadGeopackage gpkg = new ReadGeopackage();
        Connection connection = gpkg.getConnection(fileName);
        Statement statement = gpkg.getStatement(connection);

        String tableName = gpkg.getTableName(statement, contentAct);
        geopackageStructure = ComplementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "metadataIdentifier", "MD_Identifier", "code"}, tableName);

        String dataTypes = gpkg.getDataType(statement, contentAct);
        switch (dataTypes) {
            case ("features"):
                geopackageStructure = ComplementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "contact", "CI_Responsibility", "role"}, dataTypes);
                break;
        }

        String identifier = gpkg.getIdentifier(statement, contentAct);
        geopackageStructure = ComplementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "test1"}, identifier);

        String description = gpkg.getDescription(statement, contentAct);
        geopackageStructure = ComplementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "test2"}, description);

        String lastChange = gpkg.getLastChange(statement, contentAct);
        geopackageStructure = ComplementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "test3"}, lastChange);

        Integer[] extent = gpkg.getExtent(statement, contentAct);
        geopackageStructure = ComplementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata"}, extent);

        Integer srsID = gpkg.getSRSID(statement, contentAct);
        geopackageStructure = ComplementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata"}, srsID);

        String srsName = gpkg.getSRSName(statement, srsID);
        geopackageStructure = ComplementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata"}, srsName);

        String srsOrganization = gpkg.getSRSOrganization(statement, srsID);
        geopackageStructure = ComplementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata"}, srsOrganization);

        Integer srsOrganizationCoordsysID = gpkg.getSRSOrganizationCoordsysID(statement, srsID);
        geopackageStructure = ComplementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata"}, srsOrganizationCoordsysID);

        String srsDefinition = gpkg.getSRSDefinition(statement, srsID);
        geopackageStructure = ComplementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata"}, srsDefinition);

        List<String> tableColNames = gpkg.getTableColNames(statement, tableName);
        geopackageStructure = ComplementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata"}, tableColNames);

        Integer tableRowNum = gpkg.getTableRowNum(statement, tableName);
        geopackageStructure = ComplementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata"}, tableRowNum);


        return geopackageStructure;
    }

    private Element CreateNestedElement(String[] nameChain, String value) {
        // creation of one metadata entry (whole nested chain to be included in overall metadata using ComplementNestedElement)
        int nameChainLength = nameChain.length;
        Element element = new Element(nameChain[nameChainLength - 1]);
        element.addContent(value);
        Element elementTmp;
        for (int i = nameChainLength - 2; i >= 0; i--) {
            elementTmp = element;
            element.removeChild(nameChain[i + 1]);
            element = new Element(nameChain[i]);
            element.addContent(elementTmp);
        }

        return element;
    }

    private Element ComplementNestedElement(Element nestedElement, String[] nameChain, String value) {
        // create actual metadata and add to overall structure in nested elements (DOM)
        int nameChainLength = nameChain.length;
        Element complementElementTmp = CreateNestedElement(nameChain, value);
        for (int i = 1; i < nameChainLength; i++) {
            if (nestedElement.getChild(nameChain[i])==null) {
                // the actual element is not available -> stop here and complement element
                nestedElement.addContent(complementElementTmp.getChild(nameChain[i]).clone());

                while (!(nestedElement.getParent()==null)) {
                    nestedElement = nestedElement.getParentElement();
                }
                break;
            }
            else {
                // the actual element is available -> dive into child
                complementElementTmp = complementElementTmp.getChild(nameChain[i]);
                nestedElement = nestedElement.getChild(nameChain[i]);
            }

        }

        return nestedElement;
    }

    private Element CreateNestedElement(String[] nameChain, List<String> value) {
        // creation of one metadata entry (whole nested chain to be included in overall metadata using ComplementNestedElement)
        int nameChainLength = nameChain.length;
        Element element = new Element(nameChain[nameChainLength - 1]);
        for (String valueAct : value) {
            element.addContent(valueAct);
        }
        Element elementTmp;
        for (int i = nameChainLength - 2; i >= 0; i--) {
            elementTmp = element;
            element.removeChild(nameChain[i + 1]);
            element = new Element(nameChain[i]);
            element.addContent(elementTmp);
        }

        return element;
    }

    private Element ComplementNestedElement(Element nestedElement, String[] nameChain, List<String> value) {
        // create actual metadata and add to overall structure in nested elements (DOM)
        int nameChainLength = nameChain.length;
        Element complementElementTmp = CreateNestedElement(nameChain, value);
        for (int i = 1; i < nameChainLength; i++) {
            if (nestedElement.getChild(nameChain[i])==null) {
                // the actual element is not available -> stop here and complement element
                nestedElement.addContent(complementElementTmp.getChild(nameChain[i]).clone());

                while (!(nestedElement.getParent()==null)) {
                    nestedElement = nestedElement.getParentElement();
                }
                break;
            }
            else {
                // the actual element is available -> dive into child
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

    private String getLastChange(Statement stmt, Integer contentAct) {
        String lastChange = null;
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT last_change FROM gpkg_contents");
            for (int i = 0; i < contentAct; i++) {
                tableContent.next();
            }
            lastChange = tableContent.getString(1);
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
            extent = new Integer[] {tableContent.getInt("min_x"), tableContent.getInt("min_y"), tableContent.getInt("max_x"), tableContent.getInt("max_y")};
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

}
