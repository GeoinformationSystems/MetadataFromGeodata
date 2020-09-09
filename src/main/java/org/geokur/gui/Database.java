/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.gui;

import org.geokur.generateMetadataDocument.MetadataDatabase;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

public class Database {
    // database class for use in GeoKur database management
    // connection: database connection using jdbc
    // statement: connection statement from jdbc
    // listFileNumber: list of increasing numbers for each datasets
    // listFileUUID: list of UUID for each dataset in database
    // listFileName: list of filenames for each dataset in database
    // listFilePath: list of paths including filenames for each dataset in database
    // listTableName: list of table names for each dataset (built via filename_filenumber)
    // pathtype: "absolute" or "relative" paths

    public String databasePath;
    public Connection connection;
    public Statement statement;
    public List<Integer> listFileNumber = new ArrayList<>();
    public List<String> listFileUUID = new ArrayList<>();
    public List<String> listFileName = new ArrayList<>();
    public List<String> listFilePath = new ArrayList<>();
    public List<String> listTableName = new ArrayList<>();
    public String pathtype;


//    public Database(){}

    public Database(String databasePath) {
        this.databasePath = databasePath;
    }

//    public Database(Connection connection, Statement statement) {
//        this.connection = connection;
//        this.statement = statement;
//    }

//    public Database(Connection connection, Statement statement, List<Integer> listFileNumber, List<String> listFileUUID,
//                    List<String> listFileName, List<String> listFilePath, List<String> listTableName){
//        this(connection, statement);
//        this.listFileNumber = listFileNumber;
//        this.listFileUUID = listFileUUID;
//        this.listFileName = listFileName;
//        this.listFilePath = listFilePath;
//        this.listTableName = listTableName;
//    }

    public void createNewDatabase() {
        // create new database with necessary tables

        String sqlProp = "CREATE TABLE properties (\n"
                + "pathtype text NOT NULL\n"
                + ");";

        String sqlData = "CREATE TABLE datasets (\n"
                + "number integer NOT NULL PRIMARY KEY UNIQUE,\n"
                + "uuid text NOT NULL UNIQUE,\n"
                + "file_name text NOT NULL,\n"
                + "file_path text NOT NULL UNIQUE,\n"
                + "table_name text NOT NULL,\n"
                + "metadata blob\n"
                + ");";

        String sqlNamespace = "CREATE TABLE namespaces (\n"
                + "name text NOT NULL UNIQUE,\n"
                + "link text NOT NULL UNIQUE\n"
                + ");";

        String sqlPathtype = "INSERT INTO properties(pathtype) VALUES(?)";

        try {
            String url = "jdbc:sqlite:" + this.databasePath;
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();

            statement.execute(sqlProp);
            statement.execute(sqlData);
            statement.execute(sqlNamespace);

            PreparedStatement preparedStatement = connection.prepareStatement(sqlPathtype);
            this.pathtype = "absolute";
            preparedStatement.setString(1, this.pathtype);
            preparedStatement.executeUpdate();

            System.out.println("A new empty database has been created.");
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int openDatabase() {
        // open available database

        try {
            String url = "jdbc:sqlite:" + this.databasePath;
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
            ResultSet databaseContent = statement.executeQuery("SELECT * FROM datasets");
            while (databaseContent.next()) {
                listFileNumber.add(databaseContent.getInt("number"));
                listFileUUID.add(databaseContent.getString("uuid"));
                listFileName.add(databaseContent.getString("file_name"));
                listFilePath.add(databaseContent.getString("file_path"));
                listTableName.add(databaseContent.getString("table_name"));
            }
            ResultSet databaseProperties = statement.executeQuery("SELECT * FROM properties");
            pathtype = databaseProperties.getString("pathtype");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("File " + databasePath + " is no valid database.");
            return 8;
        }
        return 0;
    }

    public void setPathtype(String pathtypeNew, String referencePathString) {
        // adjust path specification from absolute to relative or vice versa

        Path referencePath = Paths.get(referencePathString);
        String sql = "UPDATE datasets SET file_path = ? WHERE file_path = ?";
        String sqlPathtype = "UPDATE properties SET pathtype = ?";
        if (!pathtypeNew.isEmpty() && !pathtypeNew.equals(this.pathtype)) {
            if (pathtypeNew.equals("relative")) {
                // absolute to relative
                for (String pathAct : listFilePath) {
                    Path relPath = referencePath.relativize(Paths.get(pathAct));
                    try {
                        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
                        preparedStatement.setString(1, relPath.toString());
                        preparedStatement.setString(2, pathAct);
                        preparedStatement.executeUpdate();
                        int index = listFilePath.indexOf(pathAct);
                        listFilePath.set(index, relPath.toString());
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            else {
                // relative to absolute
                for (String pathAct : listFilePath) {
                    Path absPath = referencePath.resolve(Paths.get(pathAct)).normalize();
                    try {
                        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
                        preparedStatement.setString(1, absPath.toString());
                        preparedStatement.setString(2, pathAct);
                        preparedStatement.executeUpdate();
                        int index = listFilePath.indexOf(pathAct);
                        listFilePath.set(index, absPath.toString());
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            try {
                PreparedStatement preparedStatement = this.connection.prepareStatement(sqlPathtype);
                preparedStatement.setString(1, pathtypeNew);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        this.pathtype = pathtypeNew;
    }

    public void addToDatabase(String datasetFilePath) {
        // adding dataset to database (new line in datasets table)
        // UUID for new dataset is assigned here

        int numberAdd = 0;
        String datasetUUID = UUID.randomUUID().toString();

        String sql1 = "SELECT number FROM datasets";
        try {
            // get the current highest number - the added dataset gets highest number + 1
            ResultSet number = this.statement.executeQuery(sql1);
            List<Integer> numberList = new ArrayList<>();
            while (number.next()) {
                numberList.add(number.getInt(1));
            }
            numberAdd = Collections.max(numberList) + 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchElementException e) {
            numberAdd = 1;
        }

        String[] tmp = datasetFilePath.split("/");
        String datasetFileName = tmp[tmp.length - 1];
        String datasetTableName = datasetFileName.replace(".", "_").replace(" ", "_") + "_" + numberAdd;

        listFileNumber.add(numberAdd);
        listFileUUID.add(datasetUUID);
        listFileName.add(datasetFileName);
        listFilePath.add(datasetFilePath);
        listTableName.add(datasetTableName);

        String sql2 = "INSERT INTO datasets(number, uuid, file_name, file_path, table_name) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql2);
            preparedStatement.setInt(1, numberAdd);
            preparedStatement.setString(2, datasetUUID);
            preparedStatement.setString(3, datasetFileName);
            preparedStatement.setString(4, datasetFilePath);
            preparedStatement.setString(5, datasetTableName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // add data specific table for metadata objects and metadata content
        String sql3 = "CREATE TABLE " + datasetTableName + "(\n"
                + "id integer NOT NULL PRIMARY KEY UNIQUE,\n"
                + "name text NOT NULL,\n"
                + "namespace text NOT NULL,\n"
                + "parent_id integer,\n"
                + "parent_name text,\n"
                + "metadata_id integer,\n"
                + "obligation text,\n"
                + "occurrence text\n"
                + ");";
        String sql4 = "CREATE TABLE " + datasetTableName + "_metadata" + "(\n"
                + "id integer NOT NULL PRIMARY KEY UNIQUE,\n"
                + "name text NOT NULL,\n"
                + "content text\n"
                + ");";
        try {
            this.statement.execute(sql3);
            this.statement.execute(sql4);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeFromDatabase(String datasetFilePath) {
        // todo: change to uuid as dataset identifier(?)
        // remove dataset from database

        int indexRemove = listFilePath.indexOf(datasetFilePath);
        String sql = "DELETE FROM datasets WHERE file_path = ?;";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, datasetFilePath);
            preparedStatement.executeUpdate();

            String sqlRemoveTable = "DROP TABLE " + listTableName.get(indexRemove) + ";";
            String sqlRemoveTableMetadata = "DROP TABLE " + listTableName.get(indexRemove) + "_metadata;";
            this.statement.execute(sqlRemoveTable);
            this.statement.execute(sqlRemoveTableMetadata);

            listFileNumber.remove(indexRemove);
            listFileUUID.remove(indexRemove);
            listFileName.remove(indexRemove);
            listFilePath.remove(indexRemove);
            listTableName.remove(indexRemove);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void writeMetadataToDatabase(String datasetFilePath, MetadataDatabase metadataDatabase) {
        // save flat metadata from MetadataDatabase class (method generateFromDocument) to database

        int indexMetadataAdd = listFilePath.indexOf(datasetFilePath);
        String tableName = listTableName.get(indexMetadataAdd);
        String tableNameMetadata = tableName + "_metadata";

        String sql = "INSERT INTO " + tableName + "(id, name, namespace, parent_id, parent_name, metadata_id, obligation, occurrence) VALUES(?,?,?,?,?,?,?,?)";
        String sql2 = "INSERT INTO " + tableNameMetadata + "(id, name, content) VALUES(?,?,?)";

        try {
            PreparedStatement preparedStatementObj = this.connection.prepareStatement(sql);
            PreparedStatement preparedStatementMd = this.connection.prepareStatement(sql2);

            // write table with object links
            for (int i = 0; i < metadataDatabase.objId.size(); i++) {
                preparedStatementObj.setObject(1, metadataDatabase.objId.get(i));
                preparedStatementObj.setObject(2, metadataDatabase.objName.get(i));
                preparedStatementObj.setObject(3, metadataDatabase.objNamespace.get(i));
                preparedStatementObj.setObject(4, metadataDatabase.objParentId.get(i));
                preparedStatementObj.setObject(5, metadataDatabase.objParentName.get(i));
                preparedStatementObj.setObject(6, metadataDatabase.objMetadataId.get(i));
                preparedStatementObj.setObject(7, metadataDatabase.objObligation.get(i));
                preparedStatementObj.setObject(8, metadataDatabase.objOccurrence.get(i));
                preparedStatementObj.executeUpdate();
            }

            // write table with metadata content
            for (int i = 0; i < metadataDatabase.mdId.size(); i++) {
                preparedStatementMd.setObject(1, metadataDatabase.mdId.get(i));
                preparedStatementMd.setObject(2, metadataDatabase.mdName.get(i));
                preparedStatementMd.setObject(3, metadataDatabase.mdContent.get(i));
                preparedStatementMd.executeUpdate();
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeMetadataFromDatabase(String datasetFilePath) {
        // remove all metadata from database

        int indexMetadataRemove = listFilePath.indexOf(datasetFilePath);
        String tableName = listTableName.get(indexMetadataRemove);
        String tableNameMetadata = tableName + "_metadata";

        String sql = "DELETE FROM " + tableName + ";";
        String sql2 = "DELETE FROM " + tableNameMetadata + ";";
        try {
            this.statement.execute(sql);
            this.statement.execute(sql2);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
