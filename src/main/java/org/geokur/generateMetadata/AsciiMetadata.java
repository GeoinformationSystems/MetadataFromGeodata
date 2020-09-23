/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.generateMetadata;

import org.geokur.ISO19115Schema.*;
import org.geokur.ISO19157Schema.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AsciiMetadata implements Metadata {
    String fileName;
    DS_DataSet dsDataSet;
    String fileNameGeodata;
    List<String> geoTableName;
    List<String> geoColNameJoin;
    List<String> asciiColNameJoin;
    List<String> asciiColNameDefinePrimary;
    List<String> asciiColNameDefineSecondary;
    List<String> asciiColNameIgnore;

    public AsciiMetadata(String fileName, DS_DataSet dsDataSet) {
        this.fileName = fileName;
        this.dsDataSet = dsDataSet;
    }

    public void defineProperties(String fileNameGeodata, List<String> geoTableName, List<String> geoColNameJoin, List<String> asciiColNameJoin,
                                 List<String> asciiColNameDefinePrimary, List<String> asciiColNameDefineSecondary, List<String> asciiColNameIgnore) {
         // definition of additional data necessary for analysis

        this.fileNameGeodata = fileNameGeodata;
        this.geoTableName = geoTableName;
        this.geoColNameJoin = geoColNameJoin;
        this.asciiColNameJoin = asciiColNameJoin;
        this.asciiColNameDefinePrimary = asciiColNameDefinePrimary;
        this.asciiColNameDefineSecondary = asciiColNameDefineSecondary;
        this.asciiColNameIgnore = asciiColNameIgnore;
    }

    public DS_Resource getMetadata() {
        // read ascii file in combination with some geodata and put its metadata into DS_Resource

        // open geopackage as sqlite database
        GeopackageMetadata gpkg = new GeopackageMetadata(fileNameGeodata);
        Connection connection = gpkg.getConnection();
        Statement statement = gpkg.getStatement(connection);

        //TODO: loop over geoTableName

        // read geopackage content
        List<List<String>> geoColContent = new ArrayList<>();
        for (String geoColNameJoinAct : geoColNameJoin) {
            geoColContent.add(gpkg.getTableColContent(statement, geoTableName.get(0), geoColNameJoinAct));
        }

        // read all relevant ascii content (concatenate all column names for the sake of performance)
        CSVReader csvContent = new CSVReader(fileName, ",", asciiColNameJoin, asciiColNameDefinePrimary, asciiColNameDefineSecondary, asciiColNameIgnore);
        csvContent.getContent();

        // get current list of combined join criteria (concatenate all columns given in geoColNameJoin)
        // double entries are removed
        List<String> joinCritGeo = new ArrayList<>();
        for (int geoCt1 = 0; geoCt1 < geoColContent.get(0).size(); geoCt1++) {
            // loop over lines in geodata
            StringBuilder joinCritGeoAct = new StringBuilder();
            for (List<String> strings : geoColContent) {
                // loop over join criteria in geodata and concatenate to
                joinCritGeoAct.append(strings.get(geoCt1));
            }
            joinCritGeo.add(joinCritGeoAct.toString());
        }
        joinCritGeo = getStringListUniqueMembers(joinCritGeo);

        // get indices of all relevant combinations in asciiColNameJoin
        List<Integer> idxRelevant = new ArrayList<>();
        for (int csvCt1 = 0; csvCt1 < csvContent.fileContentJoin.size(); csvCt1++) {
            // loop over all entries in csv
            StringBuilder joinCritActCsv = new StringBuilder();
            for (int csvCt2 = 0; csvCt2 < asciiColNameJoin.size(); csvCt2++) {
                // loop over all join criteria in csv
                joinCritActCsv.append(csvContent.fileContentJoin.get(csvCt1)[csvCt2]);
            }
            if (joinCritGeo.contains(joinCritActCsv.toString())) {
                // this combination of criteria is valid for current geodata
                idxRelevant.add(csvCt1);
            }
        }

        // get a list of relevant csv data in current geodata
        List<String[]> csvContentMaskedDefinePrimary = new ArrayList<>();
        List<String[]> csvContentMaskedDefineSecondary = new ArrayList<>();
        List<String[]> csvContentMaskedAssessment = new ArrayList<>();
        for (Integer idxRelevantAct : idxRelevant) {
            csvContentMaskedDefinePrimary.add(csvContent.fileContentDefinePrimary.get(idxRelevantAct));
            csvContentMaskedDefineSecondary.add(csvContent.fileContentDefineSecondary.get(idxRelevantAct));
            csvContentMaskedAssessment.add(csvContent.fileContentAssessment.get(idxRelevantAct));
        }

        // get number/rate of missing values in each assessed column of ascii data
        int numDataAll = csvContentMaskedAssessment.size();
        Integer[] numDataAssessment = new Integer[csvContent.headerAssessment.size()];
        Arrays.fill(numDataAssessment, 0); // preallocate with zero
        for (String[] tmp : csvContentMaskedAssessment) {
            int i = -1;
            for (String tmp2 : tmp) {
                i++;
                if (!tmp2.equals("")) {
                    numDataAssessment[i]++;
                }
            }
        }

        // get (1) basic information

        // get (2) reference system

        // get (3) structure of spatial data

        // get (4) data quality
        // completenessOmission of data in attribute rows - number of missing data
        List<DQ_CompletenessOmission> dqCompletenessOmissionsCount = new ArrayList<>();
        for (int i = 0; i < csvContent.headerAssessment.size(); i++) {
            dqCompletenessOmissionsCount.add(makeDQCompletenessOmission(csvContent.headerAssessment.get(i), numDataAll, numDataAssessment[i], "count"));
        }

        // completenessOmission of data in attribute rows - rate of missing data
        List<DQ_CompletenessOmission> dqCompletenessOmissionsRate = new ArrayList<>();
        for (int i = 0; i < csvContent.headerAssessment.size(); i++) {
            dqCompletenessOmissionsRate.add(makeDQCompletenessOmission(csvContent.headerAssessment.get(i), numDataAll, numDataAssessment[i], "rate"));
        }

        // completenessCommission

        MD_Scope mdScope = new MD_Scope();
        mdScope.createLevel();
        mdScope.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.dataset));
        mdScope.finalizeClass();

        String now = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

        CI_Date ciDate = new CI_Date();
        ciDate.createDate();
        ciDate.addDate(now);
        ciDate.createDateType();
        ciDate.addDateType(new CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes.creation));
        ciDate.finalizeClass();

        CI_Citation ciCitation = new CI_Citation();
        ciCitation.createTitle();
        ciCitation.addTitle("Reporting as standalone quality report");
        ciCitation.createDate();
        ciCitation.addDate(ciDate);
        ciCitation.finalizeClass();

        DQ_StandaloneQualityReportInformation dqStandaloneQualityReportInformation = new DQ_StandaloneQualityReportInformation();
        dqStandaloneQualityReportInformation.createReportReference();
        dqStandaloneQualityReportInformation.addReportReference(ciCitation);
        dqStandaloneQualityReportInformation.createAbstract();
        dqStandaloneQualityReportInformation.addAbstract("The standalone quality report attached to this quality evaluation is providing more details on the derivation and aggregation method.");
        dqStandaloneQualityReportInformation.finalizeClass();

        DQ_DataQuality dqDataQuality = new DQ_DataQuality();
        dqDataQuality.createScope();
        dqDataQuality.addScope(mdScope);
        dqDataQuality.createStandaloneQualityReport();
        dqDataQuality.addStandaloneQualityReport(dqStandaloneQualityReportInformation);
        dqDataQuality.createReport();
        for (DQ_CompletenessOmission dqCompletenessOmission : dqCompletenessOmissionsCount) {
            dqDataQuality.addReport(dqCompletenessOmission);
        }
        for (DQ_CompletenessOmission dqCompletenessOmission : dqCompletenessOmissionsRate) {
            dqDataQuality.addReport(dqCompletenessOmission);
        }
        dqDataQuality.finalizeClass();

        // get (5) metadata contact

        // get (6) provenance


        // aggregate all data in MD_Metadata
        MD_Metadata mdMetadata = new MD_Metadata();
        mdMetadata.createDataQualityInfo();
        mdMetadata.addDataQualityInfo(dqDataQuality);
        mdMetadata.finalizeClass();

        dsDataSet.createHas();
        dsDataSet.addHas(mdMetadata);
        dsDataSet.finalizeClass();

        return dsDataSet;
    }


    ////////////////////
    // helper methods //
    ////////////////////

    static List<String> getStringListUniqueMembers(List<String> list) {
        // return unique members of a list
        List<String> listUnique = new ArrayList<>();
        for (String listEntry : list) {
            if (!listUnique.contains(listEntry)) {
                listUnique.add(listEntry);
            }
        }
        Collections.sort(listUnique);
        return listUnique;
    }

    static DQ_CompletenessOmission makeDQCompletenessOmission(String nameAttribute, int numDataTarget, int numDataActual, String method) {
        DQ_MeasureReference dqMeasureReference = new DQ_MeasureReference();
        if (method.equals("count")) {
            dqMeasureReference.createNameOfMeasure();
            dqMeasureReference.addNameOfMeasure("number of missing items");
            dqMeasureReference.createMeasureDescription();
            dqMeasureReference.addMeasureDescription("count of all items that should have been in the data set or sample and are missing");
        } else if (method.equals("rate")) {
            dqMeasureReference.createNameOfMeasure();
            dqMeasureReference.addNameOfMeasure("rate of missing items");
            dqMeasureReference.createMeasureDescription();
            dqMeasureReference.addMeasureDescription("rate of all items that should have been in the data set or sample and are missing");
        }
        dqMeasureReference.finalizeClass();

        DQ_EvaluationMethod dqEvaluationMethod = new DQ_FullInspection();
        dqEvaluationMethod.createEvaluationMethodType();
        dqEvaluationMethod.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
        dqEvaluationMethod.createEvaluationMethodDescription();
        if (method.equals("count")) {
            dqEvaluationMethod.addEvaluationMethodDescription("count of missing items in the data set");
        } else if (method.equals("rate")) {
            dqEvaluationMethod.addEvaluationMethodDescription("rate of missing items in the data set");
        }
        dqEvaluationMethod.finalizeClass();

        MD_ScopeDescription mdScopeDescription = new MD_ScopeDescription();
        mdScopeDescription.createAttributes();
        mdScopeDescription.addAttributes(nameAttribute);
        mdScopeDescription.finalizeClass();

        MD_Scope mdScopeAttribute = new MD_Scope();
        mdScopeAttribute.createLevel();
        mdScopeAttribute.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.attribute));
        mdScopeAttribute.createLevelDescription();
        mdScopeAttribute.addLevelDescription(mdScopeDescription);
        mdScopeAttribute.finalizeClass();

        String stringMissing;
        if (method.equals("count")) {
            stringMissing = Integer.toString(numDataTarget - numDataActual);
        } else if (method.equals("rate")) {
            stringMissing = Double.toString(100 - (double) numDataActual/numDataTarget * 100);
        } else {
            stringMissing = "";
        }

        DQ_QuantitativeResult dqQuantitativeResult = new DQ_QuantitativeResult();
        dqQuantitativeResult.createResultScope();
        dqQuantitativeResult.addResultScope(mdScopeAttribute);
        dqQuantitativeResult.createValue();
        dqQuantitativeResult.addValue(stringMissing);
        dqQuantitativeResult.createValueUnit();
        if (method.equals("count")) {
            dqQuantitativeResult.addValueUnit("same unit as in " + nameAttribute + "column");
        } else if (method.equals("rate")) {
            dqQuantitativeResult.addValueUnit("%");
        }
        dqQuantitativeResult.finalizeClass();

        DQ_CompletenessOmission dqCompletenessOmission = new DQ_CompletenessOmission();
        dqCompletenessOmission.createMeasure();
        dqCompletenessOmission.addMeasure(dqMeasureReference);
        dqCompletenessOmission.createEvaluationMethod();
        dqCompletenessOmission.addEvaluationMethod(dqEvaluationMethod);
        dqCompletenessOmission.createResult();
        dqCompletenessOmission.addResult(dqQuantitativeResult);
        dqCompletenessOmission.finalizeClass();

        return dqCompletenessOmission;
    }

    static DQ_CompletenessCommission makeDQCompletenessCommission(String nameAttribute, int numDataTarget, int numDataActual, String method) {
        //TODO: add completenessCommission features
        DQ_CompletenessCommission dqCompletenessCommission = new DQ_CompletenessCommission();
        dqCompletenessCommission.finalizeClass();

        return dqCompletenessCommission;
    }
}


class CSVReader {
    // class includes interpretation of one header line
    String fileName;
    String splitChar;
    List<String> colNamesJoin;
    List<String> colNamesDefinePrimary;
    List<String> colNamesDefineSecondary;
    List<String> colNamesIgnore;

    List<String> header = new ArrayList<>();
    List<String[]> fileContentJoin = new ArrayList<>();
    List<String[]> fileContentDefinePrimary = new ArrayList<>();
    List<String[]> fileContentDefineSecondary = new ArrayList<>();
    List<String> headerAssessment = new ArrayList<>();
    List<String[]> fileContentAssessment = new ArrayList<>();

    CSVReader(String fileName, String splitChar, List<String> colNamesJoin, List<String> colNamesDefinePrimary,
            List<String> colNamesDefineSecondary, List<String> colNamesIgnore) {
        this.fileName = fileName;
        this.splitChar = splitChar;
        this.colNamesJoin = colNamesJoin;
        this.colNamesDefinePrimary = colNamesDefinePrimary;
        this.colNamesDefineSecondary = colNamesDefineSecondary;
        this.colNamesIgnore = colNamesIgnore;
    }

    void getContent() {
        List<String> colNamesDefining = Stream.concat(colNamesJoin.stream(), colNamesDefinePrimary.stream()).collect(Collectors.toList());
        colNamesDefining = Stream.concat(colNamesDefining.stream(), colNamesDefineSecondary.stream()).collect(Collectors.toList());

        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            // read header line
            header = Arrays.asList(br.readLine().split(splitChar));

            List<Integer> idxColsJoin = new ArrayList<>();
            for (String colName : colNamesJoin) {
                idxColsJoin.add(header.indexOf(colName));
            }
            List<Integer> idxColsDefinePrimary = new ArrayList<>();
            for (String colName : colNamesDefinePrimary) {
                idxColsDefinePrimary.add(header.indexOf(colName));
            }
            List<Integer> idxColsDefineSecondary = new ArrayList<>();
            for (String colName : colNamesDefineSecondary) {
                idxColsDefineSecondary.add(header.indexOf(colName));
            }
            List<Integer> idxColsAssessment = new ArrayList<>();
            for (int i = 0; i < header.size(); i++) {
                if (!colNamesDefining.contains(header.get(i)) && !colNamesIgnore.contains(header.get(i))) {
                    idxColsAssessment.add(i);
                    headerAssessment.add(header.get(i));
                }
            }

            while ((line = br.readLine()) != null) {
                String[] lineParted = line.split(splitChar, -1);
                String[] linePartedJoin = new String[colNamesJoin.size()];
                String[] linePartedDefinePrimary = new String[colNamesDefinePrimary.size()];
                String[] linePartedDefineSecondary = new String[colNamesDefineSecondary.size()];
                String[] linePartedAssessment = new String[idxColsAssessment.size()];
                for (int i = 0; i < colNamesJoin.size(); i++) {
                    linePartedJoin[i] = lineParted[idxColsJoin.get(i)];
                }
                for (int i = 0; i < colNamesDefinePrimary.size(); i++) {
                    linePartedDefinePrimary[i] = lineParted[idxColsDefinePrimary.get(i)];
                }
                for (int i = 0; i < colNamesDefineSecondary.size(); i++) {
                    linePartedDefineSecondary[i] = lineParted[idxColsDefineSecondary.get(i)];
                }
                for (int i = 0; i < idxColsAssessment.size(); i++) {
                    linePartedAssessment[i] = lineParted[idxColsAssessment.get(i)];
                }
                fileContentJoin.add(linePartedJoin);
                fileContentDefinePrimary.add(linePartedDefinePrimary);
                fileContentDefineSecondary.add(linePartedDefineSecondary);
                fileContentAssessment.add(linePartedAssessment);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

//    int parseIntFromString(String content) {
//        // parse integer from string including empty strings
//        int value;
//        if (content.isBlank()) {
//            value = -999;
//        } else {
//            value = Integer.parseInt(content);
//        }
//        return value;
//    }
//
//    double parseDoubleFromString(String content) {
//        // parse integer from string including empty strings
//        double value;
//        if (content.isBlank()) {
//            value = -999.0;
//        } else {
//            value = Double.parseDouble(content);
//        }
//        return value;
//    }
}
