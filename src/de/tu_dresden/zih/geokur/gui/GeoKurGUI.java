/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package de.tu_dresden.zih.geokur.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GeoKurGUI extends JFrame {
    JMenuBar menuBar;

    JMenu fileMenu;
    JMenuItem fileNew;
    JMenuItem fileOpen;
    JMenuItem fileClose;
    JMenuItem fileExit;

    JMenu datasetMenu;
    JMenuItem datasetAdd;
    JMenuItem datasetOpen;
    JMenuItem datasetFind;
    JMenuItem datasetClose;
    JMenuItem datasetRemoveCurrent;
    JMenuItem datasetRemove;

    JMenu metadataMenu;
    JMenuItem metadataGenerate;
    JMenuItem metadataInvestigate;
    JMenuItem metadataEdit;
    JMenuItem metadataDataQualityInvestigate;
    JMenuItem metadataDataQualityEdit;

    JMenu provenanceMenu;
    JMenuItem provenanceShow;

    JMenu pangaeaMenu;
    JMenuItem pangaeaExport;

    JPanel panel;
    JSplitPane centralPanel;
    JPanel centralPanelLeft;
    DefaultListModel<String> listDatasetString;
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
                GeoKurGUI.this.newDatabase();
            }
        });
        fileOpen.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GeoKurGUI.this.openDatabase();
            }
        });
        fileClose.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GeoKurGUI.this.closeDatabase();
            }
        });
        fileExit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        fileMenu = new JMenu("File");
        fileMenu.add(fileNew);
        fileMenu.add(fileOpen);
        fileMenu.add(fileClose);
        fileMenu.add(fileExit);

        // dataset menu
        datasetAdd = new JMenuItem("Add Geodata Dataset");
        datasetOpen = new JMenuItem("Open Geodata Dataset");
        datasetFind = new JMenuItem("Find moved Dataset");
        datasetClose = new JMenuItem("Close Dataset");
        datasetRemoveCurrent = new JMenuItem("Remove current Dataset");
        datasetRemove = new JMenuItem("Remove Dataset");
        this.setDatasetEnable(false);

        datasetAdd.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GeoKurGUI.this.addDataset();
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
        datasetRemoveCurrent.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GeoKurGUI.this.removeCurrentDataset();
            }
        });
        datasetRemove.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GeoKurGUI.this.removeDatasets();
            }
        });
        // todo: add datasetFind for finding physically moved files -> incl. warning or metadata comparison, and update names in dataset table
        // todo: add datasetRemove for database change (incl. shown list) -> use extra window with checkmark list for all entries

        datasetMenu = new JMenu("Dataset");
        datasetMenu.add(datasetAdd);
        datasetMenu.add(datasetOpen);
        datasetMenu.add(datasetFind);
        datasetMenu.add(datasetClose);
        datasetMenu.add(datasetRemoveCurrent);
        datasetMenu.add(datasetRemove);

        // metadata menu
        metadataGenerate = new JMenuItem("Generate Metadata");
        metadataInvestigate = new JMenuItem("Investigate Metadata");
        metadataEdit = new JMenuItem("Editing Metadata");
        metadataDataQualityInvestigate = new JMenuItem("Investigate Data Quality");
        metadataDataQualityEdit = new JMenuItem("Edit Data Quality");
        this.setMetadataEnable(false);
        metadataGenerate.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // todo: proper adding of metadata
//                Metadata.create(GeoKurGUI.this.datasetPath, false);
            }
        });

        metadataMenu = new JMenu("Metadata");
        metadataMenu.add(metadataGenerate);
        metadataMenu.add(metadataInvestigate);
        metadataMenu.add(metadataEdit);
        metadataMenu.addSeparator();
        metadataMenu.add(metadataDataQualityInvestigate);
        metadataMenu.add(metadataDataQualityEdit);

        // provenance menu
        provenanceShow = new JMenuItem("Show Provenance");
        provenanceShow.setEnabled(false);

        provenanceMenu = new JMenu("Provenance");
        provenanceMenu.add(provenanceShow);

        // pangaea menu
        pangaeaExport = new JMenuItem("Export to pangaea.de");
        pangaeaExport.setEnabled(false);

        pangaeaMenu = new JMenu("pangaea.de");
        pangaeaMenu.add(pangaeaExport);

        // menu bar
        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(datasetMenu);
        menuBar.add(metadataMenu);
        menuBar.add(provenanceMenu);
        menuBar.add(pangaeaMenu);

        // main panel
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        centralPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

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
        GridBagConstraints cCentralPanel = new GridBagConstraints();
//        GridBagConstraints cCentralPanelLeft = new GridBagConstraints();
//        GridBagConstraints cCentralPanelRight = new GridBagConstraints();
        GridBagConstraints cSep = new GridBagConstraints();
        GridBagConstraints cBottomLine = new GridBagConstraints();
        GridBagConstraints cBottomLineLeft = new GridBagConstraints();
        GridBagConstraints cBottomLineRight = new GridBagConstraints();

        cPanel.weightx = 1;
        cPanel.weighty = 1;
        cPanel.fill = GridBagConstraints.BOTH;

        cCentralPanel.gridx = 0;
        cCentralPanel.gridy = 0;
        cCentralPanel.weightx = 1;
        cCentralPanel.weighty = 1;
        cCentralPanel.fill = GridBagConstraints.BOTH;

//        cCentralPanelLeft.gridx = 0;
//        cCentralPanelLeft.gridy = 0;
//        cCentralPanelLeft.weightx = 1;
//        cCentralPanelLeft.weighty = 1;
//        cCentralPanelLeft.fill = GridBagConstraints.BOTH;
//        cCentralPanelRight.gridx = 1;
//        cCentralPanelRight.gridy = 0;
//        cCentralPanelRight.weightx = 1;
//        cCentralPanelRight.weighty = 1;
//        cCentralPanelRight.fill = GridBagConstraints.BOTH;

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
//        panel.add(centralPanelLeft, cCentralPanelLeft);
//        panel.add(centralPanelRight, cCentralPanelRight);
        centralPanelLeft.setMinimumSize(new Dimension(50,0));
        centralPanelRight.setMinimumSize(new Dimension(50,0));
        centralPanel.setResizeWeight(0.3);
        centralPanel.setLeftComponent(centralPanelLeft);
        centralPanel.setRightComponent(centralPanelRight);
        panel.add(centralPanel, cCentralPanel);
        panel.add(sep, cSep);
        panel.add(bottomLine, cBottomLine);

        this.setJMenuBar(menuBar);
        this.add(panel, cPanel);
    }


    // the following setter/getter methods enable a listener for the state of specific variables and actions to be
    // performed for different cases (provide a central place for variables)

    public void setDatabase() {
        // setting the database via file
        // get path and name and set bottomlines according to database state
        // enable/disable the dataset menu
        // fill list with all datasets in current database, if any
        // put list in scrolling pane
        // as centralPanelLeft is in GridBagLayout also provide GridBagConstraints

        if (this.databaseFile != null) {
            this.databasePath = this.databaseFile.getPath();
            this.databaseName = this.databaseFile.getName();
            this.setDatasetEnable(true);

            this.listDatasetString = new DefaultListModel<>();
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
            listDataset.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton()==MouseEvent.BUTTON1) {
                        // double click on actual dataset
                        GeoKurGUI.this.setDatasetFile(GeoKurGUI.this.getDatabase().listFilePath.get(listDataset.getSelectedIndex()));
                    }
                }
            });
            listDataset.addKeyListener(new KeyAdapter() {
               @Override
               public void keyReleased(KeyEvent keyEvent) {
                   if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                       // set actual dataset via Enter key
                       GeoKurGUI.this.setDatasetFile(GeoKurGUI.this.getDatabase().listFilePath.get(listDataset.getSelectedIndex()));
                   }
               }
           });

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
            this.setDatasetEnable(false);
            this.setDatasetFile(null);
            this.centralPanelLeft.removeAll();
            this.tabs.removeAll();
        }
        bottomLineLeftDatabase.setToolTipText(databasePath);
        bottomLineLeftDatabase.setText("Database: " + databaseName);
    }

    public void setDatasetFile(String datasetFilePath) {
        // setting the actual dataset
        // get path and name and set bottomlines according to dataset state
        // enable/disable close dataset menu item
        // in case of a new dataset add to list and to database

        if (datasetFilePath != null) {
            this.datasetFile = new File(datasetFilePath);
            this.datasetPath = datasetFile.getPath();
            this.datasetName = datasetFile.getName();
            this.setMetadataEnable(true);
            // todo: additional list with all pathnames as indicator for particular datasets as name twins should be allowed
            if (!this.listDatasetString.contains(datasetName)) {
                this.listDatasetString.addElement(this.datasetName);
                this.database.addToDatabase(datasetFilePath);
            }
        }
        else {
            this.datasetFile = null;
            this.datasetPath = "path to opened geodata dataset";
            this.datasetName = "";
            this.setMetadataEnable(false);
        }
        bottomLineRightDataset.setText("Dataset: " + datasetName);
        bottomLineRightDataset.setToolTipText(datasetPath);
    }

    public void setDatasetEnable(boolean enableOn) {
        // enable/disable the dataset menu items

        if (enableOn) {
            datasetAdd.setEnabled(true);
            datasetOpen.setEnabled(true);
            datasetFind.setEnabled(true);
            datasetClose.setEnabled(true);
            datasetRemoveCurrent.setEnabled(true);
            datasetRemove.setEnabled(true);
        }
        else {
            datasetAdd.setEnabled(false);
            datasetOpen.setEnabled(false);
            datasetFind.setEnabled(false);
            datasetClose.setEnabled(false);
            datasetRemoveCurrent.setEnabled(false);
            datasetRemove.setEnabled(false);
        }
    }

    public void setMetadataEnable(boolean enableOn) {
        // enable/disable the metadata menu items

        if (enableOn) {
            metadataGenerate.setEnabled(true);
            metadataInvestigate.setEnabled(true);
            metadataEdit.setEnabled(true);
            metadataDataQualityInvestigate.setEnabled(true);
            metadataDataQualityEdit.setEnabled(true);
        }
        else {
            metadataGenerate.setEnabled(false);
            metadataInvestigate.setEnabled(false);
            metadataEdit.setEnabled(false);
            metadataDataQualityInvestigate.setEnabled(false);
            metadataDataQualityEdit.setEnabled(false);
        }
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

    public void newDatabase() {
        // establish new database and connect to it
        // create new sqlite database

        JFileChooser databaseChooser = new JFileChooser();
        FileNameExtensionFilter filterDatabase = new FileNameExtensionFilter("databases", "db");
        databaseChooser.setFileFilter(filterDatabase);
        int dc = databaseChooser.showSaveDialog(GeoKurGUI.this);
        if(dc == JFileChooser.APPROVE_OPTION) {
            if (databaseChooser.getSelectedFile().exists()) {
                System.out.println("The specified file " + GeoKurGUI.this.getDatabasePath() + " already exists.");
                JOptionPane.showMessageDialog(GeoKurGUI.this,
                        GeoKurGUI.this.getDatabaseName() + " already exists. Nothing loaded.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
            else {
                GeoKurGUI.this.databasePath = databaseChooser.getSelectedFile().toString();

                Connection connection;
                Statement statement;

                String sql = "CREATE TABLE IF NOT EXISTS datasets (\n"
                        + "number integer NOT NULL PRIMARY KEY UNIQUE,\n"
                        + "uuid text NOT NULL UNIQUE,\n"
                        + "file_name text NOT NULL,\n"
                        + "file_path text NOT NULL UNIQUE,\n"
                        + "table_name text NOT NULL\n"
                        + ");";

                String sqlInsert = "INSERT INTO datasets(number, uuid, file_name, file_path, table_name) VALUES (?,?,?,?,?)";

                try {
                    List<Integer> listFileNumber = new ArrayList<>();
                    List<String> listFileUUID = new ArrayList<>();
                    List<String> listFileName = new ArrayList<>();
                    List<String> listFilePath = new ArrayList<>();
                    List<String> listTableName = new ArrayList<>();

                    String url = "jdbc:sqlite:" + GeoKurGUI.this.databasePath;
                    connection = DriverManager.getConnection(url);
                    if (connection != null) {
                        statement = connection.createStatement();
                        statement.execute(sql);

                        for (int i = 1; i < 36; i++) {
                            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
                            listFileNumber.add(i);
                            listFileUUID.add(UUID.randomUUID().toString());
                            listFileName.add("dataset" + i + ".gpkg");
                            listFilePath.add("/path/to/dataset" + i + ".gpkg");
                            listTableName.add("dataset" + i + ".gpkg_" + i);
                            preparedStatement.setInt(1,listFileNumber.get(i - 1));
                            preparedStatement.setString(2, listFileUUID.get(i - 1));
                            preparedStatement.setString(3, listFileName.get(i - 1));
                            preparedStatement.setString(4, listFilePath.get(i - 1));
                            preparedStatement.setString(5, listTableName.get(i - 1));
                            preparedStatement.executeUpdate();
                        }
                        GeoKurGUI.this.database = new Database(connection, statement, listFileNumber, listFileUUID, listFileName, listFilePath, listTableName);
                        System.out.println("A new database has been created.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                GeoKurGUI.this.databaseFile = databaseChooser.getSelectedFile();
                GeoKurGUI.this.setDatabase();
            }
        }
    }

    public void openDatabase() {
        // connect to existing database and fetch variables: statement, list of filenames, list of file paths, list of tablenames

        if (this.database != null) {
            this.database = null;
            this.setDatasetFile(null);
            this.centralPanelLeft.removeAll();
            this.tabs.removeAll();
        }

        JFileChooser databaseChooser = new JFileChooser();
        FileNameExtensionFilter filterDatabase = new FileNameExtensionFilter("databases", "db");
        databaseChooser.setFileFilter(filterDatabase);
        int dc = databaseChooser.showOpenDialog(GeoKurGUI.this);
        if (dc == JFileChooser.APPROVE_OPTION) {
            GeoKurGUI.this.databasePath = databaseChooser.getSelectedFile().toString();
            GeoKurGUI.this.databaseFile = databaseChooser.getSelectedFile();

            GeoKurGUI.this.database = new Database();

            Connection connection;
            Statement statement;

            try {
                String url = "jdbc:sqlite:" + GeoKurGUI.this.databasePath;
                connection = DriverManager.getConnection(url);
                GeoKurGUI.this.database.connection = connection;
                statement = connection.createStatement();
                GeoKurGUI.this.database.statement = statement;
                ResultSet databaseContent = statement.executeQuery("SELECT * FROM datasets");
                while (databaseContent.next()) {
                    GeoKurGUI.this.database.listFileNumber.add(databaseContent.getInt("number"));
                    GeoKurGUI.this.database.listFileUUID.add(databaseContent.getString("uuid"));
                    GeoKurGUI.this.database.listFileName.add(databaseContent.getString("file_name"));
                    GeoKurGUI.this.database.listFilePath.add(databaseContent.getString("file_path"));
                    GeoKurGUI.this.database.listTableName.add(databaseContent.getString("table_name"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            GeoKurGUI.this.setDatabase();
        }
    }

    public void closeDatabase() {
        // close current database

        GeoKurGUI.this.database = null;
        GeoKurGUI.this.databaseFile = null;
        GeoKurGUI.this.setDatabase();
    }

    public void addDataset() {
        // add new dataset to database

        JFileChooser databaseChooser = new JFileChooser();
        // todo: change filter sorting, possibly using FileFilter and addChoosableFileFilter
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
        int dc = databaseChooser.showOpenDialog(GeoKurGUI.this);
        if(dc == JFileChooser.APPROVE_OPTION){
            String datasetFilePath = databaseChooser.getSelectedFile().toString();
            if (GeoKurGUI.this.database.listFilePath.contains(datasetFilePath)) {
                System.out.println("The specified dataset " + datasetFilePath + " is already in the database.");
                JOptionPane.showMessageDialog(GeoKurGUI.this,
                        datasetFilePath + " already in the database. Nothing added.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
            else {
                GeoKurGUI.this.setDatasetFile(databaseChooser.getSelectedFile().toString());
            }
        }
    }

    public void removeCurrentDataset() {
        // remove currently set dataset from database

        int cd = JOptionPane.showConfirmDialog(GeoKurGUI.this,
                "Do you really want to remove the dataset " + this.datasetName + "?",
                "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (cd == JOptionPane.YES_OPTION) {
            this.database.removeFromDatabase(this.datasetPath);
            GeoKurGUI.this.listDatasetString.removeElement(this.datasetName);
            GeoKurGUI.this.setDatasetFile(null);
        }
    }

    public void removeDatasets() {
        // remove multiple datasets via checkmarks


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
