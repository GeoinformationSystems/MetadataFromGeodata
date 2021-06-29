/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

import java.util.List;

public class Properties {
    public String profileFilename;
    public String geodata;
    public String outXML;
    public String outDB;
    public String outRDF;
    public String rdfLinkBase;
    public List<String> allowedFileFormat;
    public String geodataReference;
    public List<String> geoTableNames;
    public List<String> geoColNamesJoin;
    public List<String> asciiColNamesJoin;
    public List<String> asciiColNamesDefine;
    public List<String> descriptionAsciiColNamesDefine;
    public List<String> asciiColNamesIgnore;
    public boolean postgresUse;
    public String postgresHostname;
    public String postgresDatabase;
    public String postgresUser;
    public String postgresPasswd;
    public String postgresTable;
    public boolean colJoinNumerical;
    public List<Boolean> colJoinSequential;
    public boolean thematicMapping;
    public String thematicMappingFile;
    public String thematicMappingColFrom;
    public String thematicMappingColTo;


    public Properties(){}

    public void setProfileFilename(String profileFilename) {
        this.profileFilename = profileFilename;
    }

    public void setGeodata(String geodata) {
        this.geodata = geodata;
    }

    public void setOutXML(String outXML) {
        this.outXML = outXML;
    }

    public void setOutDB(String outDB) {
        this.outDB = outDB;
    }

    public void setOutRDF(String outRDF) {
        this.outRDF = outRDF;
    }

    public void setRdfLinkBase(String rdfLinkBase) {
        this.rdfLinkBase = rdfLinkBase;
    }

    public void setAllowedFileFormat(List<String> allowedFileFormat) {
        this.allowedFileFormat = allowedFileFormat;
    }

    public void setGeodataReference(String geodataReference) {
        this.geodataReference = geodataReference;
    }

    public void setGeoTableNames(List<String> geoTableNames) {
        this.geoTableNames = geoTableNames;
    }

    public void setGeoColNamesJoin(List<String> geoColNamesJoin) {
        this.geoColNamesJoin = geoColNamesJoin;
    }

    public void setAsciiColNamesJoin(List<String> asciiColNamesJoin) {
        this.asciiColNamesJoin = asciiColNamesJoin;
    }

    public void setAsciiColNamesDefine(List<String> asciiColNamesDefine) {
        this.asciiColNamesDefine = asciiColNamesDefine;
    }

    public void setDescriptionAsciiColNamesDefine(List<String> descriptionAsciiColNamesDefine) {
        this.descriptionAsciiColNamesDefine = descriptionAsciiColNamesDefine;
    }

    public void setAsciiColNamesIgnore(List<String> asciiColNamesIgnore) {
        this.asciiColNamesIgnore = asciiColNamesIgnore;
    }

    public void setPostgresUse(boolean postgresUse) {
        this.postgresUse = postgresUse;
    }

    public void setPostgresHostname(String postgresHostname) {
        this.postgresHostname = postgresHostname;
    }

    public void setPostgresDatabase(String postgresDatabase) {
        this.postgresDatabase = postgresDatabase;
    }

    public void setPostgresUser(String postgresUser) {
        this.postgresUser = postgresUser;
    }

    public void setPostgresPasswd(String postgresPasswd) {
        this.postgresPasswd = postgresPasswd;
    }

    public void setPostgresTable(String postgresTable) {
        this.postgresTable = postgresTable;
    }

    public void setColJoinNumerical(boolean colJoinNumerical) {
        this.colJoinNumerical = colJoinNumerical;
    }

    public void setColJoinSequential(List<Boolean> colJoinSequential) {
        this.colJoinSequential = colJoinSequential;
    }

    public void setThematicMapping(boolean thematicMapping) {
        this.thematicMapping = thematicMapping;
    }

    public void setThematicMappingFile(String thematicMappingFile) {
        this.thematicMappingFile = thematicMappingFile;
    }

    public void setThematicMappingColFrom(String thematicMappingColFrom) {
        this.thematicMappingColFrom = thematicMappingColFrom;
    }

    public void setThematicMappingColTo(String thematicMappingColTo) {
        this.thematicMappingColTo = thematicMappingColTo;
    }
}
