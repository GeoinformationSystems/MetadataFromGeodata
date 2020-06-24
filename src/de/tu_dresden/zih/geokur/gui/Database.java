/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package de.tu_dresden.zih.geokur.gui;

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

    public Connection connection;
    public Statement statement;
    public List<Integer> listFileNumber = new ArrayList<>();
    public List<String> listFileUUID = new ArrayList<>();
    public List<String> listFileName = new ArrayList<>();
    public List<String> listFilePath = new ArrayList<>();
    public List<String> listTableName = new ArrayList<>();
    public String pathtype;


    public Database(){}

    public Database(Connection connection, Statement statement) {
        this.connection = connection;
        this.statement = statement;
    }

//    public Database(Connection connection, Statement statement, List<Integer> listFileNumber, List<String> listFileUUID,
//                    List<String> listFileName, List<String> listFilePath, List<String> listTableName){
//        this(connection, statement);
//        this.listFileNumber = listFileNumber;
//        this.listFileUUID = listFileUUID;
//        this.listFileName = listFileName;
//        this.listFilePath = listFilePath;
//        this.listTableName = listTableName;
//    }

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

    public String getPathtype() {
        return pathtype;
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
        String datasetTableName = datasetFileName + "_" + numberAdd;

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
    }

    public void removeFromDatabase(String datasetFilePath) {
        // todo: change to uuid as dataset identifier
        // remove dataset from database

        int indexRemove = listFilePath.indexOf(datasetFilePath);
        String sql = "DELETE FROM datasets WHERE file_path = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, datasetFilePath);
            preparedStatement.executeUpdate();
            listFileNumber.remove(indexRemove);
            listFileUUID.remove(indexRemove);
            listFileName.remove(indexRemove);
            listFilePath.remove(indexRemove);
            listTableName.remove(indexRemove);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
