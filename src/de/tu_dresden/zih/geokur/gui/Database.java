/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package de.tu_dresden.zih.geokur.gui;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Database {
    public Connection connection;
    public Statement statement;
    public List<Integer> listFileNumber = new ArrayList<>();
    public List<String> listFileUUID = new ArrayList<>();
    public List<String> listFileName = new ArrayList<>();
    public List<String> listFilePath = new ArrayList<>();
    public List<String> listTableName = new ArrayList<>();
    // todo: allow for relative path for the ability to move a whole project

    public Database(){}

    public Database(Connection connection, Statement statement) {
        this.connection = connection;
        this.statement = statement;
    }

    public Database(Connection connection, Statement statement, List<Integer> listFileNumber, List<String> listFileUUID,
                    List<String> listFileName, List<String> listFilePath, List<String> listTableName){
        this(connection, statement);
        this.listFileNumber = listFileNumber;
        this.listFileUUID = listFileUUID;
        this.listFileName = listFileName;
        this.listFilePath = listFilePath;
        this.listTableName = listTableName;
    }

    public void addToDatabase(String datasetFilePath) {
        // adding dataset to database (new line in datasets table)
        // UUID for new dataset is assigned here

        int numberAdd = 1;
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
