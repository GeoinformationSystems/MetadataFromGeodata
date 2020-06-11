/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package de.tu_dresden.zih.geokur.gui;

import java.sql.Connection;
import java.sql.Statement;
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
}
