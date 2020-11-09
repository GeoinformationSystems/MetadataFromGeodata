/*
 * Copyright 2020
 * @author: Michael Wagner
 */

package org.geokur.generateMetadata;

import org.geokur.ISO19103Schema.Record;
import org.geokur.ISO19115Schema.*;
import org.geokur.ISO19157Schema.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
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

        //TODO: allow shape file as geodata source

        // open geopackage as sqlite database
        Geopackage gpkg = new Geopackage(fileNameGeodata);
        Connection connection = gpkg.getConnection();
        Statement statement = gpkg.getStatement(connection);

        for (String geoTableNameAct : geoTableName) {

            // read geopackage content
            List<List<String>> geoColContent = new ArrayList<>();
            for (String geoColNameJoinAct : geoColNameJoin) {
//                geoColContent.add(gpkg.getTableColContent(statement, geoTableName.get(0), geoColNameJoinAct));
                geoColContent.add(gpkg.getTableColContent(statement, geoTableNameAct, geoColNameJoinAct));
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
            List<String[]> csvContentMaskedJoin = new ArrayList<>(); // for excess items also join parameters necessary
            List<String[]> csvContentMaskedDefinePrimary = new ArrayList<>();
            List<String[]> csvContentMaskedDefineSecondary = new ArrayList<>();
            List<String[]> csvContentMaskedAssessment = new ArrayList<>();
            for (Integer idxRelevantAct : idxRelevant) {
                csvContentMaskedJoin.add(csvContent.fileContentJoin.get(idxRelevantAct));
                csvContentMaskedDefinePrimary.add(csvContent.fileContentDefinePrimary.get(idxRelevantAct));
                csvContentMaskedDefineSecondary.add(csvContent.fileContentDefineSecondary.get(idxRelevantAct));
                csvContentMaskedAssessment.add(csvContent.fileContentAssessment.get(idxRelevantAct));
            }

            // get number/rate of missing values in each assessed column of ascii data
            int numDataAll = csvContentMaskedAssessment.size();
            int[] numDataAssessment = new int[csvContent.headerAssessment.size()];
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
            // TODO: get missing values within secondary criteria (date with increment)

            // get number/rate of excess items in each assessed column of ascii data -> concatenate all definition rows
            String joinDelimiter = "##";
            List<String> joinCritAsciiDefined = new ArrayList<>();
            for (int i = 0; i < csvContentMaskedDefinePrimary.size(); i++) {
                StringBuilder joinCritAsciiAct = new StringBuilder();
                for (String tmp : csvContentMaskedJoin.get(i)) {
                    joinCritAsciiAct.append(joinDelimiter);
                    joinCritAsciiAct.append(tmp);
                }
                for (String tmp : csvContentMaskedDefinePrimary.get(i)) {
                    joinCritAsciiAct.append(joinDelimiter); // delimiter between fields for minimizing cluttered entries
                    joinCritAsciiAct.append(tmp);
                }
                for (String tmp : csvContentMaskedDefineSecondary.get(i)) {
                    joinCritAsciiAct.append(joinDelimiter);
                    joinCritAsciiAct.append(tmp);
                }
                joinCritAsciiDefined.add(joinCritAsciiAct.toString());
            }
            List<List<Integer>> joinCritAsciiDefinedIndices = getStringOccurrenceIndices(joinCritAsciiDefined);
            int[] excessDataAll = new int[csvContent.headerAssessment.size()]; // excess data (0 means no duplicate, 1 means no duplicate)
            Arrays.fill(excessDataAll, 0);
            for (int ctAssessmentColumn = 0; ctAssessmentColumn < csvContent.headerAssessment.size(); ctAssessmentColumn++) {
                // loop over assessment columns
                for (List<Integer> idxElementAct : joinCritAsciiDefinedIndices) {
                    // loop over index list
                    if (idxElementAct.size() == 1) {
                        // only one index available -> no duplicates
                        continue;
                    }
                    int[] entriesAct = new int[idxElementAct.size()];
                    Arrays.fill(entriesAct, 0);
                    for (int ctDuplicates = 0; ctDuplicates < idxElementAct.size(); ctDuplicates++) {
                        // loop over potentially duplicate entries
                        if (!csvContentMaskedAssessment.get(idxElementAct.get(ctDuplicates))[ctAssessmentColumn].equals("")) {
                            // entry in particular row/column available
                            entriesAct[ctDuplicates] = 1;
                        }
                    }
                    int entriesActSum = 0;
                    for (int entriesTmp : entriesAct) {
                        entriesActSum = entriesActSum + entriesTmp;
                    }
                    if (entriesActSum > 0) {
                        // only add to excess data if some data in this column is available
                        excessDataAll[ctAssessmentColumn] = excessDataAll[ctAssessmentColumn] + entriesActSum - 1;
                    }
                }
            }


            // get (1) basic information
            String identifierCode = "pid:" + UUID.randomUUID().toString();

            MD_Identifier mdIdentifier = new MD_Identifier();
            mdIdentifier.addCode(identifierCode);
            mdIdentifier.finalizeClass();

            CI_Individual ciIndividual = new CI_Individual();
            ciIndividual.addName(System.getProperty("user.name"));
            ciIndividual.finalizeClass();

            CI_Responsibility ciResponsibility = new CI_Responsibility();
            ciResponsibility.addRole(new CI_RoleCode(CI_RoleCode.CI_RoleCodes.resourceProvider));
            ciResponsibility.addParty(ciIndividual);
            ciResponsibility.finalizeClass();

            File csvFile = new File(fileName);

            ZonedDateTime lastModified = ZonedDateTime.ofInstant(Instant.ofEpochMilli(csvFile.lastModified()), ZoneId.systemDefault()); // local timezone
            lastModified = lastModified.withZoneSameInstant(ZoneId.of("UTC")); // convert to UTC timezone
            String lastModifiedString = lastModified.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME); // datetime in ISO 8601 format

            CI_Date ciDateLastModified = new CI_Date();
            ciDateLastModified.addDateType(new CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes.lastUpdate));
            ciDateLastModified.addDate(lastModifiedString);
            ciDateLastModified.finalizeClass();


            // get (2) reference system


            // get (3) structure of spatial data
            String now = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

            CI_Date ciDate = new CI_Date();
            ciDate.addDate(now);
            ciDate.addDateType(new CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes.creation));
            ciDate.finalizeClass();

            // TODO: informative title available?
            CI_Citation ciCitation = new CI_Citation();
            ciCitation.addTitle("");
            ciCitation.addDate(ciDate);
            ciCitation.finalizeClass();

            MD_DataIdentification mdDataIdentification = new MD_DataIdentification();
            mdDataIdentification.addCitation(ciCitation);

            String environmentalDescription = "file name: " + fileName + "; "
                    + "layer name: " + geoTableNameAct + "; "
                    + "file size: " + (int) csvFile.length() + " B; "
                    + "os: " + System.getProperty("os.name");

            mdDataIdentification.addSpatialRepresentationType(new MD_SpatialRepresentationTypeCode(MD_SpatialRepresentationTypeCode.MD_SpatialRepresentationTypeCodes.textTable));
            mdDataIdentification.addEnvironmentalDescription(environmentalDescription);
            mdDataIdentification.finalizeClass();


            // get (4) data quality
            // completenessOmission of data in attribute rows - count of missing data
            List<DQ_CompletenessOmission> dqCompletenessOmissionsCount = new ArrayList<>();
            for (int i = 0; i < csvContent.headerAssessment.size(); i++) {
                if ((numDataAll - numDataAssessment[i]) > 0) {
                    dqCompletenessOmissionsCount.add(makeDQCompletenessOmission(csvContent.headerAssessment.get(i), numDataAll, numDataAssessment[i], "count"));
                }
            }

            // completenessOmission of data in attribute rows - rate of missing data
            List<DQ_CompletenessOmission> dqCompletenessOmissionsRate = new ArrayList<>();
            for (int i = 0; i < csvContent.headerAssessment.size(); i++) {
                if ((numDataAll - numDataAssessment[i]) > 0) {
                    dqCompletenessOmissionsRate.add(makeDQCompletenessOmission(csvContent.headerAssessment.get(i), numDataAll, numDataAssessment[i], "rate"));
                }
            }

            // completenessCommission of data in attribute rows - count of excess items
            List<DQ_CompletenessCommission> dqCompletenessCommissionsCount = new ArrayList<>();
            for (int i = 0; i < csvContent.headerAssessment.size(); i++) {
                if (excessDataAll[i] > 0) {
                    dqCompletenessCommissionsCount.add(makeDQCompletenessCommission(csvContent.headerAssessment.get(i), numDataAll, excessDataAll[i], "count"));
                }
            }

            // completenessCommission of data in attribute rows - rate of excess items
            List<DQ_CompletenessCommission> dqCompletenessCommissionsRate = new ArrayList<>();
            for (int i = 0; i < csvContent.headerAssessment.size(); i++) {
                if (excessDataAll[i] > 0) {
                    dqCompletenessCommissionsRate.add(makeDQCompletenessCommission(csvContent.headerAssessment.get(i), numDataAll, excessDataAll[i], "rate"));
                }
            }

            // frame around data quality fields
            MD_Scope mdScope = new MD_Scope();
            mdScope.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.dataset));
            mdScope.finalizeClass();

            CI_Date ciDateReport = new CI_Date();
            ciDateReport.addDate(now);
            ciDateReport.addDateType(new CI_DateTypeCode(CI_DateTypeCode.CI_DateTypeCodes.creation));
            ciDateReport.finalizeClass();

            CI_Citation ciCitationReport = new CI_Citation();
            ciCitationReport.addTitle("Reporting as standalone quality report");
            ciCitationReport.addDate(ciDateReport);
            ciCitationReport.finalizeClass();

            DQ_StandaloneQualityReportInformation dqStandaloneQualityReportInformation = new DQ_StandaloneQualityReportInformation();
            dqStandaloneQualityReportInformation.addReportReference(ciCitationReport);
            dqStandaloneQualityReportInformation.addAbstract("The standalone quality report attached to this quality evaluation is providing more details on the derivation and aggregation method.");
            dqStandaloneQualityReportInformation.finalizeClass();

            DQ_DataQuality dqDataQuality = new DQ_DataQuality();
            dqDataQuality.addScope(mdScope);
            dqDataQuality.addStandaloneQualityReport(dqStandaloneQualityReportInformation);
            for (DQ_CompletenessOmission dqCompletenessOmission : dqCompletenessOmissionsCount) {
                dqDataQuality.addReport(dqCompletenessOmission);
            }
            for (DQ_CompletenessOmission dqCompletenessOmission : dqCompletenessOmissionsRate) {
                dqDataQuality.addReport(dqCompletenessOmission);
            }
            for (DQ_CompletenessCommission dqCompletenessCommission : dqCompletenessCommissionsCount) {
                dqDataQuality.addReport(dqCompletenessCommission);
            }
            for (DQ_CompletenessCommission dqCompletenessCommission : dqCompletenessCommissionsRate) {
                dqDataQuality.addReport(dqCompletenessCommission);
            }
            dqDataQuality.finalizeClass();


            // get (5) metadata contact
            CI_Citation ciCitationMetadataStandard = new CI_Citation();
            ciCitationMetadataStandard.addTitle("ISO 19115-1");
            ciCitationMetadataStandard.addEdition("First edition 2014-04-01");
            ciCitationMetadataStandard.finalizeClass();


            // get (6) provenance


            // aggregate all data in MD_Metadata
            MD_Metadata mdMetadata = new MD_Metadata();
            mdMetadata.addMetadataIdentifier(mdIdentifier);
            mdMetadata.addContact(ciResponsibility);
            mdMetadata.addDateInfo(ciDateLastModified);
            mdMetadata.addIdentificationInfo(mdDataIdentification);
            mdMetadata.addMetadataStandard(ciCitationMetadataStandard);

            mdMetadata.addDataQualityInfo(dqDataQuality);
            mdMetadata.finalizeClass();

            dsDataSet.addHas(mdMetadata);
        }
        dsDataSet.finalizeClass();

        return dsDataSet;
    }


    ////////////////////
    // helper methods //
    ////////////////////

    static List<Integer> getIntListUniqueMembers(List<Integer> list) {
        // return unique members of a integer list (sorting as occurrence in original list)
        return list.stream().distinct().collect(Collectors.toList());
    }

    static List<String> getStringListUniqueMembers(List<String> list) {
        // return unique members of a string list (sorting as occurrence in original list)
        return list.stream().distinct().collect(Collectors.toList());
    }

    static List<List<Integer>> getIntOccurrenceIndices(List<Integer> list) {
        // return indices of occurrence
        List<Integer> listUnique = getIntListUniqueMembers(list);
        List<List<Integer>> indicesUnique = new ArrayList<>();
        for (int listUniqueAct : listUnique) {
            List<Integer> idx = new ArrayList<>();
            int idxAct = list.indexOf(listUniqueAct);
            idx.add(idxAct);
            int ct = 0;

            List<Integer> tmp1Sub = list.subList(idxAct + 1, list.size());
            idxAct = tmp1Sub.indexOf(listUniqueAct);
            while (idxAct != -1) {
                ct++;
                idx.add(idxAct + idx.get(ct - 1) + 1);
                tmp1Sub = tmp1Sub.subList(idxAct + 1, tmp1Sub.size());
                idxAct = tmp1Sub.indexOf(listUniqueAct);
            }
            indicesUnique.add(idx);
        }
        return indicesUnique;
    }

    static List<List<Integer>> getStringOccurrenceIndices(List<String> list) {
        // return indices of occurrence
        List<String> listUnique = getStringListUniqueMembers(list);
        List<List<Integer>> indicesUnique = new ArrayList<>();
        for (String listUniqueAct : listUnique) {
            List<Integer> idx = new ArrayList<>();
            int idxAct = list.indexOf(listUniqueAct);
            idx.add(idxAct);
            int ct = 0;

            List<String> tmp1Sub = list.subList(idxAct + 1, list.size());
            idxAct = tmp1Sub.indexOf(listUniqueAct);
            while (idxAct != -1) {
                ct++;
                idx.add(idxAct + idx.get(ct - 1) + 1);
                tmp1Sub = tmp1Sub.subList(idxAct + 1, tmp1Sub.size());
                idxAct = tmp1Sub.indexOf(listUniqueAct);
            }
            indicesUnique.add(idx);
        }
        return indicesUnique;
    }

    static DQ_CompletenessOmission makeDQCompletenessOmission(String nameAttribute, int numDataTarget, int numDataActual, String method) {
        // instantiate DQ_CompletenessOmission class for count or rate (defined by "method" argument) of missing values
        DQ_MeasureReference dqMeasureReference = new DQ_MeasureReference();
        if (method.equals("count")) {
            dqMeasureReference.addNameOfMeasure("number of missing items");
            dqMeasureReference.addMeasureDescription("count of all items that should have been in the data set or sample and are missing");
        } else if (method.equals("rate")) {
            dqMeasureReference.addNameOfMeasure("rate of missing items");
            dqMeasureReference.addMeasureDescription("rate of all items that should have been in the data set or sample and are missing");
        }
        dqMeasureReference.finalizeClass();

        DQ_EvaluationMethod dqEvaluationMethod = new DQ_FullInspection();
        dqEvaluationMethod.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
        if (method.equals("count")) {
            dqEvaluationMethod.addEvaluationMethodDescription("count of missing items in the data set");
        } else if (method.equals("rate")) {
            dqEvaluationMethod.addEvaluationMethodDescription("rate of missing items in the data set");
        }
        dqEvaluationMethod.finalizeClass();

        MD_ScopeDescription mdScopeDescription = new MD_ScopeDescription();
        mdScopeDescription.addAttributes(nameAttribute);
        mdScopeDescription.finalizeClass();

        MD_Scope mdScopeAttribute = new MD_Scope();
        mdScopeAttribute.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.attribute));
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

        Record record = new Record();
        record.addField(stringMissing);
        record.addField("value", stringMissing);
        record.finalizeClass();

        DQ_QuantitativeResult dqQuantitativeResult = new DQ_QuantitativeResult();
        dqQuantitativeResult.addResultScope(mdScopeAttribute);
        dqQuantitativeResult.addValue(record);
        if (method.equals("count")) {
            dqQuantitativeResult.addValueUnit("same unit as in " + nameAttribute + " column");
        } else if (method.equals("rate")) {
            dqQuantitativeResult.addValueUnit("%");
        }
        dqQuantitativeResult.finalizeClass();

        DQ_CompletenessOmission dqCompletenessOmission = new DQ_CompletenessOmission();
        dqCompletenessOmission.addMeasure(dqMeasureReference);
        dqCompletenessOmission.addEvaluationMethod(dqEvaluationMethod);
        dqCompletenessOmission.addResult(dqQuantitativeResult);
        dqCompletenessOmission.finalizeClass();

        return dqCompletenessOmission;
    }

    static DQ_CompletenessCommission makeDQCompletenessCommission(String nameAttribute, int numDataTarget, int numExcessDataActual, String method) {
        // instantiate DQ_CompletenessCommission class for count or rate (defined by "method" argument) of excess items
        DQ_MeasureReference dqMeasureReference = new DQ_MeasureReference();
        if (method.equals("count")) {
            dqMeasureReference.addNameOfMeasure("number of excess items");
            dqMeasureReference.addMeasureDescription("number of items within the data set or sample that should not have been present");
        } else if (method.equals("rate")) {
            dqMeasureReference.addNameOfMeasure("rate of excess items");
            dqMeasureReference.addMeasureDescription("number of excess items in the data set or sample in relation to the number of items that should have been present");
        }
        dqMeasureReference.finalizeClass();

        DQ_EvaluationMethod dqEvaluationMethod = new DQ_FullInspection();
        dqEvaluationMethod.addEvaluationMethodType(new DQ_EvaluationMethodTypeCode(DQ_EvaluationMethodTypeCode.DQ_EvaluationMethodTypeCodes.directInternal));
        if (method.equals("count")) {
            dqEvaluationMethod.addEvaluationMethodDescription("count of excess items in the data set");
        } else if (method.equals("rate")) {
            dqEvaluationMethod.addEvaluationMethodDescription("rate of excess items in the data set");
        }
        dqEvaluationMethod.finalizeClass();

        MD_ScopeDescription mdScopeDescription = new MD_ScopeDescription();
        mdScopeDescription.addAttributes(nameAttribute);
        mdScopeDescription.finalizeClass();

        MD_Scope mdScopeAttribute = new MD_Scope();
        mdScopeAttribute.addLevel(new MD_ScopeCode(MD_ScopeCode.MD_ScopeCodes.attribute));
        mdScopeAttribute.addLevelDescription(mdScopeDescription);
        mdScopeAttribute.finalizeClass();

        String stringMissing;
        if (method.equals("count")) {
            stringMissing = Integer.toString(numExcessDataActual);
        } else if (method.equals("rate")) {
            stringMissing = Double.toString((double) numExcessDataActual/numDataTarget * 100);
        } else {
            stringMissing = "";
        }

        Record record = new Record();
        record.addField("value", stringMissing);
        record.finalizeClass();

        DQ_QuantitativeResult dqQuantitativeResult = new DQ_QuantitativeResult();
        dqQuantitativeResult.addResultScope(mdScopeAttribute);
        dqQuantitativeResult.addValue(record);
        if (method.equals("count")) {
            dqQuantitativeResult.addValueUnit("same unit as in " + nameAttribute + " column");
        } else if (method.equals("rate")) {
            dqQuantitativeResult.addValueUnit("%");
        }
        dqQuantitativeResult.finalizeClass();

        DQ_CompletenessCommission dqCompletenessCommission = new DQ_CompletenessCommission();
        dqCompletenessCommission.addMeasure(dqMeasureReference);
        dqCompletenessCommission.addEvaluationMethod(dqEvaluationMethod);
        dqCompletenessCommission.addResult(dqQuantitativeResult);
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

        String line;

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
