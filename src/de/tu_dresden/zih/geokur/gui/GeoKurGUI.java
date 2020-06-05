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

    JPanel centralPanel;

    JPanel bottomLineLeft;
    JPanel bottomLineRight;
    JLabel bottomLineLeftDatabase = new JLabel("Database: ");
    JLabel bottomLineRightDataset = new JLabel("File: ");


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
                    File database = databaseChooser.getSelectedFile();
                    String databasePath = database.getPath();
                    String databaseName = database.getName();
                    bottomLineLeftDatabase.setText("Database: " + databaseName);
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
                    File database = databaseChooser.getSelectedFile();
                    String databasePath = database.getPath();
                    String databaseName = database.getName();
                    bottomLineLeftDatabase.setText("Database: " + databaseName);
                }
            }
        });
        fileClose.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                bottomLineLeftDatabase.setText("Database: ");
                bottomLineRightDataset.setText("File: ");
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
        centralPanel = new JPanel();

        // bottom line
        bottomLineLeftDatabase.setToolTipText("Opened Database");
        bottomLineLeft = new JPanel();
        bottomLineLeft.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottomLineLeft.add(bottomLineLeftDatabase);

        bottomLineRightDataset.setToolTipText("Opened Geodata Dataset");
        bottomLineRight = new JPanel();
        bottomLineRight.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomLineRight.add(bottomLineRightDataset);


        // add to frame
        this.setJMenuBar(menuBar);

        GridBagConstraints cCentralPanel = new GridBagConstraints();
        cCentralPanel.gridy = 0;
        cCentralPanel.gridwidth = 2;
        cCentralPanel.weighty = 1;
        GridBagConstraints cBottomLineLeft = new GridBagConstraints();
        cBottomLineLeft.weightx = .5;
        cBottomLineLeft.gridx = 0;
        cBottomLineLeft.gridy = 1;
        cBottomLineLeft.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints cBottomLineRight = new GridBagConstraints();
        cBottomLineRight.weightx = .5;
        cBottomLineRight.gridx = 1;
        cBottomLineRight.gridy = 1;
        cBottomLineRight.fill = GridBagConstraints.HORIZONTAL;

        this.add(centralPanel, cCentralPanel);
        this.add(bottomLineLeft, cBottomLineLeft);
        this.add(bottomLineRight, cBottomLineRight);

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
        gkGui.setVisible(true);
        gkGui.pack();
    }
}
