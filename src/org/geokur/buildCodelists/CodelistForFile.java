/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.buildCodelists;
import java.io.*;

class FileFilterExtension implements FileFilter
{
    private final String[] allowedFileExtensions = new String[] {"txt"};

    public boolean accept(File file) {
        for (String extension : allowedFileExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}

public class CodelistForFile {
    public static void main(String[] argv) {
        try {
            File folder = new File("codelistsRaw");
            File[] listOfFiles = folder.listFiles(new FileFilterExtension());

            assert listOfFiles != null;

            for (File file : listOfFiles) {
                String[] filenameSplit = file.getName().split("[.]");
                String fileVariant = filenameSplit[0].replace("codelist_", "");
                System.out.println(fileVariant);

                String filenameIn = "codelistsRaw/codelist_" + fileVariant + ".txt";
                BufferedReader fileReader = new BufferedReader(new FileReader(filenameIn));

                String filenameOut = "configISO/codelist_" + fileVariant + ".xml";
                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filenameOut));
                fileWriter.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                fileWriter.newLine();
                fileWriter.append("<").append(fileVariant).append(" codespace=");

                String lineContent;
                int ct = 0;
                while ((lineContent = fileReader.readLine()) != null) {
                    ++ct;
                    if (ct == 1) {
                        fileWriter.append("\"").append(lineContent).append("\">");
                        fileWriter.newLine();
                    } else {
                        String[] lineContentSplit = lineContent.split("\t");
                        fileWriter.append("    <entry>");
                        fileWriter.newLine();
                        fileWriter.append("        <code>").append(lineContentSplit[0].trim()).append("</code>");
                        fileWriter.newLine();
                        fileWriter.append("        <definition>").append(lineContentSplit[1].trim()).append("</definition>");
                        fileWriter.newLine();
                        fileWriter.append("    </entry>");
                        fileWriter.newLine();
                    }
                }
                fileWriter.append("</").append(fileVariant).append(">");
                fileReader.close();
                fileWriter.close();
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
