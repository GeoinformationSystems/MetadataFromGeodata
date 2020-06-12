/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package de.tu_dresden.zih.geokur.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class GeoKurGUI extends JFrame {
    JMenuBar menuBar;

    JMenu file;
    JMenuItem fileNew;
    JMenuItem fileOpen;
    JMenuItem fileClose;
    JMenuItem fileExit;

    JMenu dataset;
    JMenuItem datasetAdd;
    JMenuItem datasetOpen;
    JMenuItem datasetClose;

    JMenu metadata;
    JMenuItem metadataGenerate;
    JMenuItem metadataInvestigate;
    JMenuItem metadataEdit;
    JMenuItem metadataDataQualityInvestigate;
    JMenuItem metadataDataQualityEdit;

    JMenu provenance;
    JMenuItem provenanceShow;

    JMenu pangaea;
    JMenuItem pangaeaExport;

    JPanel panel;
    JPanel centralPanelLeft;
    JList<String> listDataset;
    JScrollPane listDatasetScrolling;
    JPanel centralPanelRight;
    JTabbedPane tabs;
    JPanel metadataTab;
    JPanel dataQualityTab;
    JPanel bottomLine;
    JPanel bottomLineLeft;
    JPanel bottomLineRight;
    JLabel bottomLineLeftDatabase = new JLabel("Database: ");
    JLabel bottomLineRightDataset = new JLabel("Dataset: ");

    File databaseFile;
    String databasePath;
    String databaseName;
    Database database;

    File datasetFile;
    String datasetPath;
    String datasetName;


    public GeoKurGUI (String title) {
        super(title);
        this.setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(330,300));
        this.setPreferredSize(new Dimension(600,400));
        this.setMaximumSize(new Dimension(800,500));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Image image = new ImageIcon("fig/GeoKurLogoSquare.png").getImage();
        this.setIconImage(image);

        // file menu
        fileNew = new JMenuItem("New Database");
        fileOpen = new JMenuItem("Open Database");
        fileClose = new JMenuItem("Close Database");
        fileExit = new JMenuItem("Exit Program");

        fileNew.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser databaseChooser = new JFileChooser();
                FileNameExtensionFilter filterDatabase = new FileNameExtensionFilter("databases", "db");
                databaseChooser.setFileFilter(filterDatabase);
                int i = databaseChooser.showSaveDialog(GeoKurGUI.this);
                if(i==JFileChooser.APPROVE_OPTION) {
                    if (databaseChooser.getSelectedFile().exists()) {
                        System.out.println("The specified file " + GeoKurGUI.this.getDatabasePath() + " already exists.");
                        JOptionPane.showMessageDialog(GeoKurGUI.this, GeoKurGUI.this.getDatabaseName() + " already exists. Nothing loaded.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    else {
                        GeoKurGUI.this.newDatabase(databaseChooser.getSelectedFile().toString());
                        GeoKurGUI.this.setDatabase(databaseChooser.getSelectedFile());
                    }
                }
            }
        });
        fileOpen.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser databaseChooser = new JFileChooser();
                FileNameExtensionFilter filterDatabase = new FileNameExtensionFilter("databases", "db");
                databaseChooser.setFileFilter(filterDatabase);
                int i = databaseChooser.showOpenDialog(GeoKurGUI.this);
                if(i==JFileChooser.APPROVE_OPTION){
                    GeoKurGUI.this.setDatabase(databaseChooser.getSelectedFile());
//                    GeoKurGUI.this.setDatabase(openDatabase(GeoKurGUI.this.getDatabaseFile().getPath()));
                }
            }
        });
        fileClose.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GeoKurGUI.this.setDatabase(null);
            }
        });
        fileExit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        file = new JMenu("File");
        file.add(fileNew);
        file.add(fileOpen);
        file.add(fileClose);
        file.add(fileExit);

        // dataset menu
        datasetAdd = new JMenuItem("Add Geodata Dataset");
        datasetOpen = new JMenuItem("Open Geodata Dataset");
        datasetClose = new JMenuItem("Close Dataset");
        datasetAdd.setEnabled(false);
        datasetOpen.setEnabled(false);
        datasetClose.setEnabled(false);

        datasetAdd.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser databaseChooser = new JFileChooser();
                FileNameExtensionFilter filterGeopackage = new FileNameExtensionFilter("geopackage", "gpkg");
                FileNameExtensionFilter filterShape = new FileNameExtensionFilter("shapefile", "shp");
                FileNameExtensionFilter filterNetcdf = new FileNameExtensionFilter("NetCDF", "nc");
                FileNameExtensionFilter filterAsciigrid = new FileNameExtensionFilter("asciigrid", "asc");
                FileNameExtensionFilter filterGeodata = new FileNameExtensionFilter("geodata", "gpkg", "shp", "nc", "asc");
                databaseChooser.setFileFilter(filterGeopackage);
                databaseChooser.setFileFilter(filterShape);
                databaseChooser.setFileFilter(filterNetcdf);
                databaseChooser.setFileFilter(filterAsciigrid);
                databaseChooser.setFileFilter(filterGeodata);
                int i = databaseChooser.showOpenDialog(GeoKurGUI.this);
                if(i==JFileChooser.APPROVE_OPTION){
                    GeoKurGUI.this.setDatasetFile(databaseChooser.getSelectedFile());
                }
            }
        });

        datasetOpen.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
//                Database database = openDatabase(databasePath);
//                JList<String> listDatasets = new JList<>(listDatasetString);
//                listDatasets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//                listDatasets.setLayoutOrientation(JList.VERTICAL);
//                listDatasets.setVisibleRowCount(12);
//                JScrollPane listDatasetsScrolling = new JScrollPane(listDatasets);
            }
        });
        datasetClose.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GeoKurGUI.this.setDatasetFile(null);
            }
        });

        dataset = new JMenu("Dataset");
        dataset.add(datasetAdd);
        dataset.add(datasetOpen);
        dataset.add(datasetClose);

        // metadata menu
        metadataGenerate = new JMenuItem("Generate Metadata");
        metadataInvestigate = new JMenuItem("Investigate Metadata");
        metadataEdit = new JMenuItem("Editing Metadata");
        metadataDataQualityInvestigate = new JMenuItem("Investigate Data Quality");
        metadataDataQualityEdit = new JMenuItem("Edit Data Quality");
        metadataGenerate.setEnabled(false);
        metadataInvestigate.setEnabled(false);
        metadataEdit.setEnabled(false);
        metadataDataQualityInvestigate.setEnabled(false);
        metadataDataQualityEdit.setEnabled(false);

        metadata = new JMenu("Metadata");
        metadata.add(metadataGenerate);
        metadata.add(metadataInvestigate);
        metadata.add(metadataEdit);
        metadata.addSeparator();
        metadata.add(metadataDataQualityInvestigate);
        metadata.add(metadataDataQualityEdit);

        // provenance menu
        provenanceShow = new JMenuItem("Show Provenance");
        provenanceShow.setEnabled(false);

        provenance = new JMenu("Provenance");
        provenance.add(provenanceShow);

        // pangaea menu
        pangaeaExport = new JMenuItem("Export to pangaea.de");
        pangaeaExport.setEnabled(false);

        pangaea = new JMenu("pangaea.de");
        pangaea.add(pangaeaExport);

        // menu bar
        menuBar = new JMenuBar();
        menuBar.add(file);
        menuBar.add(dataset);
        menuBar.add(metadata);
        menuBar.add(provenance);
        menuBar.add(pangaea);

        // main panel
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        centralPanelLeft = new JPanel(new GridBagLayout());
        centralPanelRight = new JPanel(new GridBagLayout());

        tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        GridBagConstraints cTabs = new GridBagConstraints();
        cTabs.weightx = 1;
        cTabs.weighty = 1;
        cTabs.fill = GridBagConstraints.BOTH;
        centralPanelRight.add(tabs, cTabs);

        JSeparator sep = new JSeparator();

        bottomLine = new JPanel(new GridBagLayout());
        bottomLineLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomLineRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // general layout
        GridBagConstraints cPanel = new GridBagConstraints();
        GridBagConstraints cCentralPanelLeft = new GridBagConstraints();
        GridBagConstraints cCentralPanelRight = new GridBagConstraints();
        GridBagConstraints cSep = new GridBagConstraints();
        GridBagConstraints cBottomLine = new GridBagConstraints();
        GridBagConstraints cBottomLineLeft = new GridBagConstraints();
        GridBagConstraints cBottomLineRight = new GridBagConstraints();

        cPanel.weightx = 1;
        cPanel.weighty = 1;
        cPanel.fill = GridBagConstraints.BOTH;

        cCentralPanelLeft.gridx = 0;
        cCentralPanelLeft.gridy = 0;
        cCentralPanelLeft.weightx = 1;
        cCentralPanelLeft.weighty = 1;
        cCentralPanelLeft.fill = GridBagConstraints.BOTH;

        cCentralPanelRight.gridx = 1;
        cCentralPanelRight.gridy = 0;
        cCentralPanelRight.weightx = 1;
        cCentralPanelRight.weighty = 1;
        cCentralPanelRight.fill = GridBagConstraints.BOTH;
        cSep.gridx = 0;
        cSep.gridwidth = 2;
        cSep.gridy = 1;
        cSep.fill = GridBagConstraints.HORIZONTAL;
        cBottomLine.gridx = 0;
        cBottomLine.gridy = 2;
        cBottomLine.gridwidth = 2;
        cBottomLine.fill = GridBagConstraints.BOTH;
        cBottomLineLeft.gridx = 0;
        cBottomLineLeft.gridy = 0;
        cBottomLineLeft.weightx = .5;
        cBottomLineLeft.fill = GridBagConstraints.BOTH;
        cBottomLineRight.gridx = 1;
        cBottomLineRight.gridy = 0;
        cBottomLineRight.weightx = .5;
        cBottomLineRight.fill = GridBagConstraints.BOTH;

        // bottom line
        bottomLineLeftDatabase.setToolTipText("path to opened database");
        bottomLineLeft.add(bottomLineLeftDatabase);

        bottomLineRightDataset.setToolTipText("path to opened geodata dataset");
        bottomLineRight.add(bottomLineRightDataset);

        bottomLine.add(bottomLineLeft, cBottomLineLeft);
        bottomLine.add(bottomLineRight, cBottomLineRight);

        // add to frame / main panel
        panel.add(centralPanelLeft, cCentralPanelLeft);
        panel.add(centralPanelRight, cCentralPanelRight);
        panel.add(sep, cSep);
        panel.add(bottomLine, cBottomLine);

        this.setJMenuBar(menuBar);
        this.add(panel, cPanel);
    }


    // the following setter/getter methods enable a listener for the state of specific variables and actions to be
    // performed for different cases (provide a central place for variables)

    public void setDatabase(File databaseFile) {
        // setting the database via file
        // get path and name and set bottomlines according to database state
        // enable/disable the dataset menu
        // fill list with all datasets in current database, if any
        // put list in scrolling pane
        // as centralPanelLeft is in GridBagLayout also provide GridBagConstraints

        this.databaseFile = databaseFile;
        if (databaseFile != null) {
            this.databasePath = databaseFile.getPath();
            this.databaseName = databaseFile.getName();
            datasetAdd.setEnabled(true);
            datasetOpen.setEnabled(true);

            this.database = this.openDatabase(this.getDatabasePath());
            DefaultListModel<String> listDatasetString = new DefaultListModel<>();
            if (database != null && database.listTableName != null) {
                for (String datasetAct : GeoKurGUI.this.getDatabase().listFileName) {
                    listDatasetString.addElement(datasetAct);
                }
                metadataTab = new JPanel();
                dataQualityTab = new JPanel();
                tabs.addTab("Metadata", metadataTab);
                tabs.addTab("Data Quality", dataQualityTab);
            }
            else {
                listDatasetString.addElement("");
            }
            this.listDataset = new JList<>(listDatasetString);
            listDataset.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            listDataset.setLayoutOrientation(JList.VERTICAL);

            GridBagConstraints cListDataset = new GridBagConstraints();
            cListDataset.weightx = 1;
            cListDataset.weighty = 1;
            cListDataset.fill = GridBagConstraints.BOTH;
            this.listDatasetScrolling = new JScrollPane(listDataset);
            centralPanelLeft.add(listDatasetScrolling);
            centralPanelLeft.add(listDatasetScrolling, cListDataset);
        }
        else {
            this.databasePath = "path to opened database";
            this.databaseName = "";
            this.database = null;
            datasetAdd.setEnabled(false);
            datasetOpen.setEnabled(false);
            this.setDatasetFile(null);
            this.centralPanelLeft.removeAll();
            this.centralPanelRight.removeAll();
        }
        bottomLineLeftDatabase.setToolTipText(databasePath);
        bottomLineLeftDatabase.setText("Database: " + databaseName);
    }

    public void setDatasetFile(File datasetFile) {
        // setting the actual dataset
        // get path and name and set bottomlines according to dataset state
        // enable/disable close dataset menu item

        this.datasetFile = datasetFile;
        if (datasetFile != null) {
            this.datasetPath = datasetFile.getPath();
            this.datasetName = datasetFile.getName();
            datasetClose.setEnabled(true);
        }
        else {
            this.datasetPath = "path to opened geodata dataset";
            this.datasetName = "";
            datasetClose.setEnabled(false);
        }
        bottomLineRightDataset.setText("Dataset: " + datasetName);
        bottomLineRightDataset.setToolTipText(datasetPath);
    }

    public File getDatabaseFile() {
        return databaseFile;
    }

    public String getDatabasePath() {
        return databasePath;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public Database getDatabase() {
        return database;
    }

    public File getDatasetFile() {
        return datasetFile;
    }

    public String getDatasetPath() {
        return datasetPath;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public Database newDatabase(String databasePath) {
        // establish new database and connect to it
//        String[] databasePathSplit = databasePath.split("/");
//        String databaseName = databasePathSplit[databasePathSplit.length - 1];

        // create new sqlite database
        Connection connection = null;
        Statement statement = null;

        String sql = "CREATE TABLE IF NOT EXISTS datasets (\n"
                + "id integer NOT NULL PRIMARY KEY,\n"
                + "file_name text NOT NULL,\n"
                + "absolute_path text NOT NULL,\n"
                + "table_name text NOT NULL\n"
                + ");";

        String sqlInsert = "INSERT INTO datasets(id, file_name, absolute_path, table_name) VALUES (?,?,?,?)";

        try {
            String url = "jdbc:sqlite:" + databasePath;
            connection = DriverManager.getConnection(url);
            if (connection != null) {
                System.out.println("A new database has been created.");
                statement = connection.createStatement();
                statement.execute(sql);

                for (int i = 1; i < 100; i++) {
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
                    preparedStatement.setInt(1, i);
                    preparedStatement.setString(2, "dataset_var2_" + i);
                    preparedStatement.setString(3, "/path/to/dataset_var2_" + i);
                    preparedStatement.setString(4, i + "_" + "dataset_var2_" + i);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Database(connection, statement);
    }


    public Database openDatabase(String databasePath) {
        // connect to existing database and fetch variables: statement, list of filenames, list of absolute paths, list of tablenames
        Database database = new Database();

        Connection connection;
        Statement statement;
        java.util.List<String> listFileName = new ArrayList<>();
        java.util.List<String> listAbsolutePath = new ArrayList<>();
        java.util.List<String> listTableName = new ArrayList<>();

        try {
            String url = "jdbc:sqlite:" + databasePath;
            connection = DriverManager.getConnection(url);
            database.connection = connection;
            statement = connection.createStatement();
            database.statement = statement;
            ResultSet databaseContent = statement.executeQuery("SELECT * FROM datasets");
            while (databaseContent.next()) {
                listFileName.add(databaseContent.getString("file_name"));
                listAbsolutePath.add(databaseContent.getString("absolute_path"));
                listTableName.add(databaseContent.getString("table_name"));
            }
            database.listFileName = listFileName;
            database.listAbsolutePath = listAbsolutePath;
            database.listTableName = listTableName;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return database;
    }


    public static void main(String[] args) {
        try {
            // Check if Nimbus is supported and get its class name
            for (UIManager.LookAndFeelInfo lafInfo : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(lafInfo.getName())) {
                    // set some preferences for
//                    UIManager.put("nimbusBase", new Color(42, 97, 147));
//                    UIManager.put("nimbusFocus", new Color(231, 204, 117));
                    UIManager.put("control", new Color(243, 243, 243));

                    UIManager.setLookAndFeel(lafInfo.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                // If Nimbus is not available, set to the default Java (metal) look and feel
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        GeoKurGUI gkGui = new GeoKurGUI("GeoKur Metadata Generator");
        gkGui.pack();
        gkGui.setVisible(true);
    }
}
