/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package de.tu_dresden.zih.geokur.gui;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Database {
    public Connection connection;
    public Statement statement;
    public List<String> listFileName;
    public List<String> listAbsolutePath;
    public List<String> listTableName;

    public Database(){}

    public Database(Connection connection, Statement statement) {
        this.connection = connection;
        this.statement = statement;
    }

    public Database(Connection connection, Statement statement, List<String> listFileName, List<String> listAbsolutePath, List<String> listTableName){
        this(connection, statement);
        this.listFileName = listFileName;
        this.listAbsolutePath = listAbsolutePath;
        this.listTableName = listTableName;
    }

    public void addToDatabase(String datasetFilePath) {
        // adding dataset to database (new line in datasets table)

        int idAdd = 1;

        String sql1 = "SELECT id FROM datasets";
        try {
            // get the current highest id - the added dataset gets highest id + 1
            ResultSet id = this.statement.executeQuery(sql1);
            List<Integer> idList = new ArrayList<>();
            while (id.next()) {
                idList.add(id.getInt(1));
            }
            idAdd = Collections.max(idList) + 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String[] tmp = datasetFilePath.split("/");
        String datasetFileName = tmp[tmp.length - 1];

        String sql2 = "INSERT INTO datasets(id, file_name, absolute_path, table_name) VALUES(?,?,?,?)";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql2);
            preparedStatement.setInt(1, idAdd);
            preparedStatement.setString(2, datasetFileName);
            preparedStatement.setString(3, datasetFilePath);
            preparedStatement.setString(4, datasetFileName + "_" + idAdd);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeFromDatabase(String datasetFilePath) {
        // remove dataset from database

        String sql = "DELETE FROM datasets WHERE absolute_path = ?";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, datasetFilePath);
            preparedStatement.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
