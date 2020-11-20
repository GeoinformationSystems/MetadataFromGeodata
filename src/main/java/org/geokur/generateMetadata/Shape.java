/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Shape {
    File shapeFile;
    List<File> shapeFiles = new ArrayList<>();
    List<String> shapeFilesString = new ArrayList<>();
    String allFiles;
    double size = 0;

    public Shape(File shapeFile) {
        this.shapeFile = shapeFile;
    }

    public void getShapeFileList() {
        String fileExtensions = "{shp,shx,dbf,prj,sbn,sbx,fbn,fbx,ain,aih,ixs,mxs,atx,shp.xml,cpg,qix}";
        String fileAbsolutePath = shapeFile.getAbsolutePath();
        Path dir = Paths.get(fileAbsolutePath.substring(0, fileAbsolutePath.lastIndexOf(File.separator)));
        String shapeFileName = shapeFile.getName();
        String fileBase = shapeFileName.substring(0, shapeFileName.lastIndexOf(".shp"));

        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(dir, fileBase + "." + fileExtensions);
            for (Path path : stream) {
                File fileAct = path.getFileName().toFile();
                shapeFiles.add(fileAct);
                shapeFilesString.add(fileAct.getName());
                size = size + fileAct.length();
            }

            allFiles = String.join(", ", shapeFilesString);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
