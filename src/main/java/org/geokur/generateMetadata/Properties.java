/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.generateMetadata;

import java.util.List;

public class Properties {
    public String profileFilename;
    public String filename;
    public String filenameXml;
    public String filenameDB;
    public String geodataReference;
    public List<String> geoTableNames;
    public List<String> geoColNamesJoin;
    public List<String> asciiColNamesJoin;
    public List<String> asciiColNamesDefine;
    public List<String> descriptionAsciiColNamesDefine;
    public List<String> asciiColNamesIgnore;
    public String postgresHostname;
    public String postgresDatabase;
    public String postgresUser;
    public String postgresPasswd;


    public Properties(){}

    public void setProfileFilename(String profileFilename) {
        this.profileFilename = profileFilename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFilenameXml(String filenameXml) {
        this.filenameXml = filenameXml;
    }

    public void setFilenameDB(String filenameDB) {
        this.filenameDB = filenameDB;
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
}
