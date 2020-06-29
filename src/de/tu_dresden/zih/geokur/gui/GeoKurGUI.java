/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package de.tu_dresden.zih.geokur.gui;

import de.tu_dresden.zih.geokur.generateMetadata.Metadata;
import de.tu_dresden.zih.geokur.generateMetadata.MetadataDatabase;
import org.jdom2.Document;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class GeoKurGUI extends JFrame {
    JMenuBar menuBar;

    JMenu fileMenu;
    JMenuItem fileNew;
    JMenuItem fileOpen;
    JMenuItem fileClose;
    JMenuItem fileProperties;
    JMenuItem fileExit;

    JMenu datasetMenu;
    JMenuItem datasetAdd;
    JMenuItem datasetOpen;
    JMenuItem datasetFind;
    JMenuItem datasetClose;
    JMenuItem datasetRemoveCurrent;
    JMenuItem datasetRemove;
    // todo: add a tool for dataset import from another database (maybe even bulk import)

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
    DefaultListModel<String> listDatasetPathString;
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
    String pathBase;
    JLabel pathBaseName = new JLabel("Path Base: ");

    File datasetFile;
    String datasetPath;
    String datasetName;

    Metadata metadata;
    MetadataDatabase metadataDatabase;


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
        fileProperties = new JMenuItem("Database Properties");
        fileExit = new JMenuItem("Exit Program");

        fileNew.addActionListener(actionEvent -> GeoKurGUI.this.newDatabase());
        fileOpen.addActionListener(actionEvent -> GeoKurGUI.this.openDatabase());
        fileClose.addActionListener(actionEvent -> GeoKurGUI.this.closeDatabase());
        fileProperties.addActionListener(actionEvent -> GeoKurGUI.this.setDatabaseProperties());
        fileExit.addActionListener(actionEvent -> System.exit(0));

        fileMenu = new JMenu("File");
        fileMenu.add(fileNew);
        fileMenu.add(fileOpen);
        fileMenu.add(fileClose);
        fileMenu.add(fileProperties);
        fileMenu.add(fileExit);

        // dataset menu
        datasetAdd = new JMenuItem("Add Geodata Dataset");
        datasetOpen = new JMenuItem("Open Geodata Dataset");
        datasetFind = new JMenuItem("Find moved Dataset");
        datasetClose = new JMenuItem("Close Dataset");
        datasetRemoveCurrent = new JMenuItem("Remove current Dataset");
        datasetRemove = new JMenuItem("Remove multiple Datasets");
        this.setDatasetEnable(false);

        datasetAdd.addActionListener(actionEvent -> GeoKurGUI.this.addDataset());
        datasetOpen.addActionListener(actionEvent -> {});
        datasetClose.addActionListener(actionEvent -> GeoKurGUI.this.setDatasetFile(null));
        datasetRemoveCurrent.addActionListener(actionEvent -> GeoKurGUI.this.removeCurrentDataset());
        datasetRemove.addActionListener(actionEvent -> GeoKurGUI.this.removeDatasets());
        // todo: add datasetFind for finding physically moved files -> incl. warning or metadata comparison, and update names in dataset table

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

        metadataGenerate.addActionListener(actionEvent -> GeoKurGUI.this.generateMetadata());
        metadataInvestigate.addActionListener(actionEvent -> GeoKurGUI.this.investigateMetadata());
        metadataEdit.addActionListener(actionEvent -> GeoKurGUI.this.editMetadata());
        metadataDataQualityInvestigate.addActionListener(actionEvent -> GeoKurGUI.this.investigateDataQuality());
        metadataDataQualityEdit.addActionListener(actionEvent -> GeoKurGUI.this.editDataQuality());

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
            this.listDatasetPathString = new DefaultListModel<>();
            if (database != null && database.listTableName != null) {
                for (String datasetAct : GeoKurGUI.this.getDatabase().listFileName) {
                    listDatasetString.addElement(datasetAct);
                }
                for (String datasetPathAct : GeoKurGUI.this.getDatabase().listFilePath) {
                    listDatasetPathString.addElement(datasetPathAct);
                }
                metadataTab = new JPanel();
                dataQualityTab = new JPanel();
                tabs.addTab("Metadata", metadataTab);
                tabs.addTab("Data Quality", dataQualityTab);
            }
            else {
                listDatasetString.addElement("");
                listDatasetPathString.addElement("");
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
            if (!this.listDatasetString.contains(datasetName)) {
                this.listDatasetString.addElement(this.datasetName);
                this.listDatasetPathString.addElement(this.datasetPath);
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

        datasetAdd.setEnabled(enableOn);
        datasetOpen.setEnabled(enableOn);
        datasetFind.setEnabled(enableOn);
        datasetClose.setEnabled(enableOn);
        datasetRemoveCurrent.setEnabled(enableOn);
        datasetRemove.setEnabled(enableOn);
    }

    public void setMetadataEnable(boolean enableOn) {
        // enable/disable the metadata menu items

        metadataGenerate.setEnabled(enableOn);
        metadataInvestigate.setEnabled(enableOn);
        metadataEdit.setEnabled(enableOn);
        metadataDataQualityInvestigate.setEnabled(enableOn);
        metadataDataQualityEdit.setEnabled(enableOn);
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
        // absolute paths are taken as standard in a new database

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
                GeoKurGUI.this.database = new Database(GeoKurGUI.this.databasePath);
                GeoKurGUI.this.database.createNewDatabase();
                GeoKurGUI.this.databaseFile = databaseChooser.getSelectedFile();
                GeoKurGUI.this.setDatabase();

                // fill dummy data for testing purposes
                for (int i = 1; i < 36; i++) {
                    GeoKurGUI.this.setDatasetFile("/path/to/dataset" + i + ".gpkg");
                }
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

            GeoKurGUI.this.database = new Database(GeoKurGUI.this.databasePath);
            GeoKurGUI.this.database.openDatabase();
            GeoKurGUI.this.setDatabase();
        }
    }

    public void closeDatabase() {
        // close current database

        GeoKurGUI.this.database = null;
        GeoKurGUI.this.databaseFile = null;
        GeoKurGUI.this.setDatabase();
    }

    public void setDatabaseProperties() {
        // set properties of current database

        // todo: add a button for choosing whether or not an xml file alongside the geodata file(s) is generated
        // absolute path (true) or relative path (false)
        boolean absPathProp = GeoKurGUI.this.database.pathtype.equals("absolute");
        AtomicBoolean pathtypeChange = new AtomicBoolean(false);

        // new window
        JDialog propertyFrame = new JDialog(GeoKurGUI.this, "Database Properties", true);
        propertyFrame.setLayout(new GridBagLayout());

        // radio buttons and directory chooser in own JPanel in JScrollPane
        JRadioButton absolutePath = new JRadioButton("Use absolute path specifications");
        JRadioButton relativePath = new JRadioButton("Use relative path specifications");
        absolutePath.setSelected(absPathProp);
        relativePath.setSelected(!absPathProp);
        ButtonGroup pathtype = new ButtonGroup();
        pathtype.add(absolutePath);
        pathtype.add(relativePath);
        relativePath.addActionListener(actionEvent -> {
            if (GeoKurGUI.this.database.pathtype.equals("absolute")) {
                JFileChooser pathBaseChooser = new JFileChooser();
                pathBaseChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int pbc = pathBaseChooser.showDialog(GeoKurGUI.this, "Choose Path Base");
                if (pbc == JFileChooser.APPROVE_OPTION) {
                    GeoKurGUI.this.pathBase = pathBaseChooser.getSelectedFile().toString();
                    GeoKurGUI.this.pathBaseName.setText("Path Base: " + pathBase);
                    pathtypeChange.set(true);
                }
            }
        });
        absolutePath.addActionListener(actionEvent -> {
            if (GeoKurGUI.this.database.pathtype.equals("relative")) {
                JFileChooser pathBaseChooser = new JFileChooser();
                pathBaseChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int pbc = pathBaseChooser.showDialog(GeoKurGUI.this, "Choose Path Base");
                if (pbc == JFileChooser.APPROVE_OPTION) {
                    GeoKurGUI.this.pathBase = pathBaseChooser.getSelectedFile().toString();
                    GeoKurGUI.this.pathBaseName.setText("Path Base: " + pathBase);
                    pathtypeChange.set(true);
                }
            }
        });
        JPanel propertyPane = new JPanel();
        propertyPane.setLayout(new GridBagLayout());
        GridBagConstraints cPropertyScrollPane = new GridBagConstraints();
        GridBagConstraints cAbsolutePath = new GridBagConstraints();
        GridBagConstraints cRelativePath = new GridBagConstraints();
        GridBagConstraints cPathBaseName = new GridBagConstraints();
        cPropertyScrollPane.gridy = 0;
        cPropertyScrollPane.gridwidth = 2;
        cPropertyScrollPane.weightx = 1;
        cPropertyScrollPane.weighty = 1;
        cPropertyScrollPane.fill = GridBagConstraints.BOTH;
        cAbsolutePath.gridy = 0;
        cAbsolutePath.weightx = 1;
        cAbsolutePath.fill = GridBagConstraints.BOTH;
        cRelativePath.gridy = 1;
        cRelativePath.weightx = 1;
        cRelativePath.fill = GridBagConstraints.BOTH;
        cPathBaseName.gridy = 2;
        cPathBaseName.weightx = 1;
        cPathBaseName.fill = GridBagConstraints.BOTH;
        propertyPane.add(absolutePath, cAbsolutePath);
        propertyPane.add(relativePath, cRelativePath);
        propertyPane.add(pathBaseName, cPathBaseName);
        JScrollPane propertyScrollPane = new JScrollPane();
        propertyScrollPane.setViewportView(propertyPane);

        // buttons
        JButton applyButton = new JButton("Apply");
        JButton cancelButton = new JButton("Cancel");
        GridBagConstraints cApplyButton = new GridBagConstraints();
        GridBagConstraints cCancelButton = new GridBagConstraints();
        cApplyButton.gridx = 0;
        cApplyButton.gridy = 1;
        cApplyButton.weightx = .5;
        cApplyButton.fill = GridBagConstraints.BOTH;
        cCancelButton.gridx = 1;
        cCancelButton.gridy = 1;
        cCancelButton.weightx = .5;
        cCancelButton.fill = GridBagConstraints.BOTH;
        applyButton.addActionListener(actionEvent -> {
            if (pathtypeChange.get() && GeoKurGUI.this.database.pathtype.equals("absolute")) {
                // absolute to relative path specification
                GeoKurGUI.this.database.setPathtype("relative", GeoKurGUI.this.pathBase);
                propertyFrame.dispose();
            }
            else if (pathtypeChange.get() && GeoKurGUI.this.database.pathtype.equals("relative")) {
                // relative to absolute path specification
                GeoKurGUI.this.database.setPathtype("absolute", GeoKurGUI.this.pathBase);
                propertyFrame.dispose();
            }
        });
        cancelButton.addActionListener(actionEvent -> propertyFrame.dispose());

        // whole property window
        propertyFrame.add(propertyScrollPane, cPropertyScrollPane);
        propertyFrame.add(applyButton, cApplyButton);
        propertyFrame.add(cancelButton, cCancelButton);
        propertyFrame.setMinimumSize(new Dimension(300, 400));
        propertyFrame.pack();
        propertyFrame.setVisible(true);
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

        if (this.datasetFile != null) {
            Object[] options = {"Yes", "Cancel"};
            int cd = JOptionPane.showOptionDialog(GeoKurGUI.this,
                    "Do you really want to remove the dataset " + this.datasetName + "?",
                    "Remove Dataset", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[1]);
            if (cd == JOptionPane.YES_OPTION) {
                this.database.removeFromDatabase(this.datasetPath);
                GeoKurGUI.this.listDatasetString.removeElement(this.datasetName);
                GeoKurGUI.this.listDatasetPathString.removeElement(this.datasetPath);
                GeoKurGUI.this.setDatasetFile(null);
            }
        }
        else {
            JOptionPane.showMessageDialog(GeoKurGUI.this,
                    "Please choose the current dataset.",
                    "Remove Dataset", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void removeDatasets() {
        // remove multiple datasets via list in extra window

        JList<String> listDatasetRemove = new JList<>(listDatasetString);
        listDatasetRemove.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listDatasetRemove.setLayoutOrientation(JList.VERTICAL);

        JDialog removeFrame = new JDialog(GeoKurGUI.this, "Remove Datasets", true);
        removeFrame.setLayout(new GridBagLayout());
        JScrollPane listDatasetRemoveScrolled = new JScrollPane(listDatasetRemove);
        JButton cancelButton = new JButton("Cancel");
        JButton removeButton = new JButton("Remove");
        cancelButton.addActionListener(actionEvent -> removeFrame.dispose());
        removeButton.addActionListener(actionEvent -> {
            int[] indexRemove = listDatasetRemove.getSelectedIndices();
            if (indexRemove.length > 0) {
                for (int i = indexRemove.length - 1; i >= 0; i--) {
                    GeoKurGUI.this.database.removeFromDatabase(GeoKurGUI.this.listDatasetPathString.get(indexRemove[i]));
                    GeoKurGUI.this.listDatasetString.removeElementAt(indexRemove[i]);
                    GeoKurGUI.this.listDatasetPathString.removeElementAt(indexRemove[i]);
                    GeoKurGUI.this.setDatasetFile(null);
                }
            }
            removeFrame.dispose();
        });
        GridBagConstraints cListDatasetRemoveScrolled = new GridBagConstraints();
        GridBagConstraints cCancelButton = new GridBagConstraints();
        GridBagConstraints cRemoveButton = new GridBagConstraints();
        cListDatasetRemoveScrolled.gridwidth = 2;
        cListDatasetRemoveScrolled.gridy = 0;
        cListDatasetRemoveScrolled.weightx = 1;
        cListDatasetRemoveScrolled.weighty = 1;
        cListDatasetRemoveScrolled.fill = GridBagConstraints.BOTH;
        cCancelButton.gridx = 0;
        cCancelButton.gridy = 1;
        cCancelButton.weightx = 1;
        cCancelButton.fill = GridBagConstraints.BOTH;
        cRemoveButton.gridx = 1;
        cRemoveButton.gridy = 1;
        cRemoveButton.weightx = 1;
        cRemoveButton.fill = GridBagConstraints.BOTH;

        removeFrame.add(listDatasetRemoveScrolled, cListDatasetRemoveScrolled);
        removeFrame.add(cancelButton, cCancelButton);
        removeFrame.add(removeButton, cRemoveButton);
        removeFrame.setMinimumSize(new Dimension(200,400));
        removeFrame.pack();
        removeFrame.setVisible(true);
    }

    public void generateMetadata() {
        // generating and collection of metadata

        metadata = new Metadata();
        Document metadataDoc = metadata.create(GeoKurGUI.this.datasetPath, "minimal", false, false);
        if (metadataDoc.getContentSize() == 0) {
            JOptionPane.showMessageDialog(GeoKurGUI.this,
                    "No metadata available.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        }
        else {
            metadataDatabase = new MetadataDatabase();
            metadataDatabase.generateFromDocument(metadataDoc.getRootElement());
        }
    }

    public void investigateMetadata() {
        // investigate metadata
    }

    public void editMetadata() {
        // edit metadata
    }

    public void investigateDataQuality() {
        // investigate data quality metadata
    }

    public void editDataQuality() {
        // edit data quality metadata
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
