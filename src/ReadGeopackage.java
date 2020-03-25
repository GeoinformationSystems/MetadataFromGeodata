/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

import org.jdom2.Element;

import java.sql.*;
import java.util.*;

public class ReadGeopackage {

    public Element getContent(String fileName) {
        Element geopackageStructure = new Element("root");

        ReadGeopackage gpkg = new ReadGeopackage();
        Connection connection = gpkg.getConnection(fileName);
        Statement statement = gpkg.getStatement(connection);

        List<String> tableNames = gpkg.getTableNames(statement);
        geopackageStructure = ComplementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "metadataIdentifier", "MD_Identifier", "code"}, tableNames);

        List<String> dataTypes = gpkg.getDataTypes(statement);
        geopackageStructure = ComplementNestedElement(geopackageStructure, new String[] {"root", "DS_Resource", "has", "MD_Metadata", "contact", "CI_Responsibility", "role"}, dataTypes);

        return geopackageStructure;
    }

    private Element CreateNestedElement(String[] nameChain, List<String> value) {
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
        int nameChainLength = nameChain.length;
        // ------------ //
        // add warning if nestedElement and nameChain do not contain the same String at first level
        Element complementElementTmp = CreateNestedElement(nameChain, value);
        for (int i = 1; i < nameChainLength - 1; i++) {
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


    private List<String> getTableNames(Statement stmt) {
        List<String> tableNames = new ArrayList<>();
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT table_name FROM gpkg_contents");
            while (tableContent.next()) {
                tableNames.add(tableContent.getString(1));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tableNames;
    }

    private List<String> getDataTypes(Statement stmt) {
        List<String> dataTypes = new ArrayList<>();
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT data_type FROM gpkg_contents");
            while (tableContent.next()) {
                dataTypes.add(tableContent.getString(1));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dataTypes;
    }

    private List<String> getIdentifier(Statement stmt) {
        List<String> identifier = new ArrayList<>();
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT identifier FROM gpkg_contents");
            while (tableContent.next()) {
                identifier.add(tableContent.getString(1));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return identifier;
    }

    private List<String> getDescription(Statement stmt) {
        List<String> description = new ArrayList<>();
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT description FROM gpkg_contents");
            while (tableContent.next()) {
                description.add(tableContent.getString(1));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return description;
    }

    private List<String> getLastChange(Statement stmt) {
        List<String> lastChange = new ArrayList<>();
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT last_change FROM gpkg_contents");
            while (tableContent.next()) {
                lastChange.add(tableContent.getString(1));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lastChange;
    }

    private List<Integer[]> getExtent(Statement stmt) {
        List<Integer[]> extent = new ArrayList<>();
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT min_x, min_y, max_x, max_y FROM gpkg_contents");
            while (tableContent.next()) {
                extent.add(new Integer[] {tableContent.getInt("min_x"), tableContent.getInt("min_y"), tableContent.getInt("max_x"), tableContent.getInt("max_y")});
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return extent;
    }

    private List<Integer> getSRSID(Statement stmt) {
        List<Integer> srsID = new ArrayList<>();
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT srs_id FROM gpkg_contents");
            while (tableContent.next()) {
                srsID.add(tableContent.getInt(1));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return srsID;
    }

    private List<String> getSRSName(Statement stmt, List<Integer> usedSRSID) {
        List<String> srsName = new ArrayList<>();
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT srs_id, srs_name FROM gpkg_spatial_ref_sys");
            List<Integer> srsIDAll = new ArrayList<>();
            List<String> srsNameAll = new ArrayList<>();
            while (tableContent.next()) {
                srsIDAll.add(tableContent.getInt("srs_id"));
                srsNameAll.add(tableContent.getString("srs_name"));
            }
            for (Integer usedSRSIDact : usedSRSID) {
                int idx = srsIDAll.indexOf(usedSRSIDact);
                srsName.add(srsNameAll.get(idx));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return srsName;
    }

    private List<String> getSRSOrganization(Statement stmt, List<Integer> usedSRSID) {
        List<String> srsOrganization = new ArrayList<>();
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT srs_id, organization FROM gpkg_spatial_ref_sys");
            List<Integer> srsIDAll = new ArrayList<>();
            List<String> srsOrganizationAll = new ArrayList<>();
            while (tableContent.next()) {
                srsIDAll.add(tableContent.getInt("srs_id"));
                srsOrganizationAll.add(tableContent.getString("organization"));
            }
            for (Integer usedSRSIDact : usedSRSID) {
                int idx = srsIDAll.indexOf(usedSRSIDact);
                srsOrganization.add(srsOrganizationAll.get(idx));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return srsOrganization;
    }

    private List<Integer> getSRSOrganizationCoordsysID(Statement stmt, List<Integer> usedSRSID) {
        List<Integer> srsOrganizationCoordsysID = new ArrayList<>();
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT srs_id, organization_coordsys_id FROM gpkg_spatial_ref_sys");
            List<Integer> srsIDAll = new ArrayList<>();
            List<Integer> srsOrganizationCoordsysIDAll = new ArrayList<>();
            while (tableContent.next()) {
                srsIDAll.add(tableContent.getInt("srs_id"));
                srsOrganizationCoordsysIDAll.add(tableContent.getInt("organization_coordsys_id"));
            }
            for (Integer usedSRSIDact : usedSRSID) {
                int idx = srsIDAll.indexOf(usedSRSIDact);
                srsOrganizationCoordsysID.add(srsOrganizationCoordsysIDAll.get(idx));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return srsOrganizationCoordsysID;
    }

    private List<String> getSRSDefinition(Statement stmt, List<Integer> usedSRSID) {
        List<String> srsDefinition = new ArrayList<>();
        try {
            ResultSet tableContent = stmt.executeQuery("SELECT srs_id, definition FROM gpkg_spatial_ref_sys");
            List<Integer> srsIDAll = new ArrayList<>();
            List<String> srsDefinitionAll = new ArrayList<>();
            while (tableContent.next()) {
                srsIDAll.add(tableContent.getInt("srs_id"));
                srsDefinitionAll.add(tableContent.getString("definition"));
            }
            for (Integer usedSRSIDact : usedSRSID) {
                int idx = srsIDAll.indexOf(usedSRSIDact);
                srsDefinition.add(srsDefinitionAll.get(idx));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return srsDefinition;
    }

    private List<List<String>> getTableColNames(Statement stmt, List<String> tableNames) {
        List<List<String>> tableColNames = new ArrayList<>();
        try {
            for (String tableName : tableNames) {
                ResultSet tableContent = stmt.executeQuery("SELECT * FROM " + tableName);
                ResultSetMetaData colNames = tableContent.getMetaData();
                List<String> colNamesAct = new ArrayList<>();
                for (int i = 1; i <= colNames.getColumnCount(); i++) {
                    colNamesAct.add(colNames.getColumnName(i));
                }
                tableColNames.add(colNamesAct);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tableColNames;
    }

    private List<Integer> getTableRowNum(Statement stmt, List<String> tableNames) {
        List<Integer> tableRowNum = new ArrayList<>();
        try {
            for (String tableName : tableNames) {
                ResultSet tableContent = stmt.executeQuery("SELECT * FROM " + tableName);
                int ct = 0;
                while (tableContent.next()) {
                    ++ct;
                }
                tableRowNum.add(ct);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tableRowNum;
    }


//    public static void main(String[] argv) {
//        String fileName = "NewZealandPacked.gpkg";
//        ReadGeopackage gpkg = new ReadGeopackage();
//        Connection connection = gpkg.getConnection(fileName);
//        Statement statement = gpkg.getStatement(connection);
//        List<String> tableNames = gpkg.getTableNames(statement);
//        List<String> dataTypes = gpkg.getDataTypes(statement);
//        List<String> identifier = gpkg.getIdentifier(statement);
//        List<String> description = gpkg.getDescription(statement);
//        List<String> lastChange = gpkg.getLastChange(statement);
//        List<Integer[]> extent = gpkg.getExtent(statement);
//        List<Integer> srsID = gpkg.getSRSID(statement);
//        List<String> srsName = gpkg.getSRSName(statement, srsID);
//        List<String> srsOrganization = gpkg.getSRSOrganization(statement, srsID);
//        List<Integer> srsOrganizationCoordsysID = gpkg.getSRSOrganizationCoordsysID(statement, srsID);
//        List<String> srsDefinition = gpkg.getSRSDefinition(statement, srsID);
//        List<List<String>> tableColNames = gpkg.getTableColNames(statement, tableNames);
//        List<Integer> tableRowNum = gpkg.getTableRowNum(statement, tableNames);
//
//        System.out.println(tableNames);
//        System.out.println(dataTypes);
//        System.out.println(identifier);
//        System.out.println(description);
//        System.out.println(lastChange);
//        System.out.println(extent);
//        System.out.println(extent.get(0)[1]);
//        System.out.println(srsID);
//        System.out.println(srsName);
//        System.out.println(srsOrganization);
//        System.out.println(srsOrganizationCoordsysID);
//        System.out.println(srsDefinition);
//        System.out.println(tableColNames);
//        System.out.println(tableRowNum);
//
//    }
}
