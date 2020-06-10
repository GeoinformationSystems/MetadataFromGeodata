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
    JPanel centralPanelRight;
    JPanel bottomLineLeft;
    JPanel bottomLineRight;
    JLabel bottomLineLeftDatabase = new JLabel("Database: ");
    JLabel bottomLineRightDataset = new JLabel("Dataset: ");


    public GeoKurGUI (String title) {
        super(title);
        this.setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(330,300));
        this.setPreferredSize(new Dimension(600,400));
        this.setMaximumSize(new Dimension(800,500));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JFrame thisFrame = this; // for use in ActionListeners

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
                    File databaseFile = databaseChooser.getSelectedFile();
                    String databasePath = databaseFile.getPath();
                    String databaseName = databaseFile.getName();
                    if (databaseFile.exists()) {
                        System.out.println("The specified file " + databaseFile.getPath() + " already exists.");
                        JOptionPane.showMessageDialog(thisFrame, databaseName + " already exists. Nothing loaded.", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    else {
                        Connection connection = newDatabase(databasePath);
                        bottomLineLeftDatabase.setText("Database: " + databaseName);
                        bottomLineLeftDatabase.setToolTipText(databasePath);
                        datasetAdd.setEnabled(true);
                        datasetOpen.setEnabled(true);
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
                    File databaseFile = databaseChooser.getSelectedFile();
                    String databasePath = databaseFile.getPath();
                    String databaseName = databaseFile.getName();
                    Database database = openDatabase(databasePath);
                    bottomLineLeftDatabase.setText("Database: " + databaseName);
                    bottomLineLeftDatabase.setToolTipText(databasePath);
                    datasetAdd.setEnabled(true);
                    datasetOpen.setEnabled(true);

                    // draw list of all datasets in opened database including a dataset choose button
                    DefaultListModel<String> listDatasetString = new DefaultListModel<>();
                    for (String datasetAct : database.listAbsolutePath) {
                        listDatasetString.addElement(datasetAct);
                    }
                    final JList<String> listDatasets = new JList<>(listDatasetString);
                    listDatasets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    listDatasets.setLayoutOrientation(JList.VERTICAL);
                    listDatasets.setVisibleRowCount(12);
                    JScrollPane listDatasetsScrolling = new JScrollPane(listDatasets);
                    JButton openDatasetButton = new JButton("Open Dataset");
                    openDatasetButton.addActionListener(new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            if (listDatasets.getSelectedIndex() != -1 && listDatasets.getSelectedIndices().length==1) {
                                bottomLineRightDataset.setText("Dataset: " + listDatasets.getSelectedValue());
//                                centralPanel.removeAll();
                            }
                        }
                    });
//                    centralPanel.add(listDatasetsScrolling);
//                    centralPanel.add(openDatasetButton);
                }
            }
        });
        fileClose.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
//                centralPanel.removeAll();
                bottomLineLeftDatabase.setText("Database: ");
                bottomLineLeftDatabase.setToolTipText("");
                bottomLineRightDataset.setText("Dataset: ");
                bottomLineRightDataset.setToolTipText("");
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
                    File datasetFile = databaseChooser.getSelectedFile();
                    String datasetPath = datasetFile.getPath();
                    String datasetName = datasetFile.getName();
                    bottomLineRightDataset.setText("Dataset: " + datasetName);
                    bottomLineRightDataset.setToolTipText(datasetPath);
                }
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
        metadata = new JMenu("Metadata");
        metadata.add(metadataGenerate);
        metadata.add(metadataInvestigate);
        metadata.add(metadataEdit);
        metadata.add(new JSeparator());
        metadata.add(metadataDataQualityInvestigate);
        metadata.add(metadataDataQualityEdit);

        // provenance menu
        provenanceShow = new JMenuItem("Show Provenance");
        provenance = new JMenu("Provenance");
        provenance.add(provenanceShow);

        // pangaea menu
        pangaeaExport = new JMenuItem("Export to pangaea.de");
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

        centralPanelLeft = new JPanel();
        centralPanelRight = new JPanel();

        bottomLineLeft = new JPanel();
        bottomLineRight = new JPanel();

        // general layout
        GridBagConstraints cPanel = new GridBagConstraints();
        GridBagConstraints cCentralPanelLeft = new GridBagConstraints();
        GridBagConstraints cCentralPanelRight = new GridBagConstraints();
        GridBagConstraints cBottomLineLeft = new GridBagConstraints();
        GridBagConstraints cBottomLineRight = new GridBagConstraints();

        cPanel.weightx = 1;
        cPanel.weighty = 1;
        cPanel.fill = GridBagConstraints.BOTH;
//        cPanel.insets = new Insets(10,10,10,10);

        cCentralPanelLeft.gridx = 0;
        cCentralPanelLeft.gridy = 0;
        cCentralPanelLeft.weightx = .3;
        cCentralPanelLeft.weighty = 1;
        cCentralPanelLeft.fill = GridBagConstraints.BOTH;
        cCentralPanelRight.gridx = 1;
        cCentralPanelRight.gridy = 0;
        cCentralPanelRight.weightx = .7;
        cCentralPanelRight.weighty = 1;
        cCentralPanelRight.fill = GridBagConstraints.BOTH;
        cBottomLineLeft.gridx = 0;
        cBottomLineLeft.gridy = 1;
        cBottomLineLeft.weightx = .5;
        cBottomLineLeft.fill = GridBagConstraints.BOTH;
        cBottomLineRight.gridx = 1;
        cBottomLineRight.gridy = 1;
        cBottomLineRight.weightx = .5;
        cBottomLineRight.fill = GridBagConstraints.BOTH;

        // bottom line
        bottomLineLeftDatabase.setToolTipText("Opened Database");
        bottomLineLeft.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottomLineLeft.add(bottomLineLeftDatabase);

        bottomLineRightDataset.setToolTipText("Opened Geodata Dataset");
        bottomLineRight.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomLineRight.add(bottomLineRightDataset);

        // add to frame / main panel
        this.setJMenuBar(menuBar);

        panel.add(centralPanelLeft, cCentralPanelLeft);
        panel.add(centralPanelRight, cCentralPanelRight);
        panel.add(bottomLineLeft, cBottomLineLeft);
        panel.add(bottomLineRight, cBottomLineRight);

        this.add(panel, cPanel);

//        GridBagConstraints cCentralPanel = new GridBagConstraints();
//        cCentralPanel.gridy = 0;
//        cCentralPanel.gridwidth = 1;
//        cCentralPanel.weighty = 1;
//        GridBagConstraints cBottomLineLeft = new GridBagConstraints();
//        cBottomLineLeft.weightx = .5;
//        cBottomLineLeft.gridx = 0;
//        cBottomLineLeft.gridy = 1;
//        cBottomLineLeft.fill = GridBagConstraints.HORIZONTAL;
//        GridBagConstraints cBottomLineRight = new GridBagConstraints();
//        cBottomLineRight.weightx = .5;
//        cBottomLineRight.gridx = 1;
//        cBottomLineRight.gridy = 1;
//        cBottomLineRight.fill = GridBagConstraints.HORIZONTAL;

//        this.add(centralPanel, cCentralPanel);
//        this.add(bottomLineLeft, cBottomLineLeft);
//        this.add(bottomLineRight, cBottomLineRight);
    }


    public Connection newDatabase(String databasePath) {
        // establish new database and connect to it
        String[] databasePathSplit = databasePath.split("/");
        String databaseName = databasePathSplit[databasePathSplit.length - 1];

        // create new sqlite database
        Connection connection = null;
        Statement statement;

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
                    preparedStatement.setString(2, "dataset" + i);
                    preparedStatement.setString(3, "/path/to/dataset" + i);
                    preparedStatement.setString(4, i + "_" + "dataset" + i);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
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


    public void openDataset(Database database) {

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
