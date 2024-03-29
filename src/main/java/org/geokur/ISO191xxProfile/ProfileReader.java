/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO191xxProfile;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ProfileReader {
    public static Profile profile;

    public ProfileReader() {}

    public static void setProfile(String profileFilename) {
        // read profile json file

        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader(profileFilename));
            profile = gson.fromJson(reader, Profile.class);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setProfileStandard() {
        // put in standard profile from ISO

        ProfileUsed profileUsed = new ProfileUsed();
        ProfileObligation profileObligation = new ProfileObligation();

        profileUsed.setDS_DataSet(new String[] {"has", "partOf"});
        profileUsed.setSV_Service(new String[] {"has", "partOf"});
        profileUsed.setDS_OtherAggregate(new String[] {"has", "partOf", "composedOf"});
        profileUsed.setDS_StereoMate(new String[] {"has", "partOf", "composedOf"});
        profileUsed.setDS_Initiative(new String[] {"has", "partOf", "composedOf"});
        profileUsed.setDS_Series(new String[] {"has", "partOf", "composedOf"});
        profileUsed.setDS_Platform(new String[] {"has", "partOf", "composedOf"});
        profileUsed.setDS_Sensor(new String[] {"has", "partOf", "composedOf"});
        profileUsed.setDS_ProductionSeries(new String[] {"has", "partOf", "composedOf"});
        profileUsed.setMD_Metadata(new String[] {"metadataIdentifier", "defaultLocale", "parentMetadata", "contact", "dateInfo", "metadataStandard", "metadataProfile", "alternativeMetadataReference", "otherLocale", "metadataLinkage", "spatialRepresentationInfo", "referenceSystemInfo", "metadataExtensionInfo", "identificationInfo", "contentInfo", "distributionInfo", "dataQualityInfo", "portrayalCatalogueInfo", "metadataConstraints", "applicationSchemaInfo", "metadataMaintenance", "resourceLineage", "metadataScope"});
        profileUsed.setMD_MetadataScope(new String[] {"resourceScope", "name"});
        profileUsed.setMD_DataIdentification(new String[] {"citation", "abstract", "purpose", "credit", "status", "pointOfContact", "spatialRepresentationType", "spatialResolution", "temporalResolution", "topicCategory", "extent", "additionalDocumentation", "processingLevel", "resourceMaintenance", "graphicOverview", "resourceFormat", "descriptiveKeywords", "resourceSpecificUsage", "resourceConstraints", "associatedResource", "defaultLocale", "otherLocale", "environmentalDescription", "supplementalInformation"});
        profileUsed.setMD_Keywords(new String[] {"keyword", "type", "thesaurusName", "keywordClass"});
        profileUsed.setMD_KeywordClass(new String[] {"className", "conceptIdentifier", "ontology"});
        profileUsed.setMD_RepresentativeFraction(new String[] {"denominator"});
        profileUsed.setMD_Resolution(new String[] {"equivalentScale", "distance", "vertical", "angularDistance", "lefelOfDetail"});
        profileUsed.setMD_Usage(new String[] {"specificUsage", "usageDateTime", "userDeterminedLimitations", "userContactInfo", "response", "additionalDocumentation", "identifiedIssues"});
        profileUsed.setMD_AssociatedResource(new String[] {"name", "associationType", "initiativeType", "metadataReference"});
        profileUsed.setMD_Constraints(new String[] {"useLimitation", "constraintApplicationScope", "graphic", "reference", "releasability", "responsibleParty"});
        profileUsed.setMD_LegalConstraints(new String[] {"useLimitation", "constraintApplicationScope", "graphic", "reference", "releasability", "responsibleParty", "accessConstraints", "useConstraints", "otherConstraints"});
        profileUsed.setMD_SecurityConstraints(new String[] {"useLimitation", "constraintApplicationScope", "graphic", "reference", "releasability", "responsibleParty", "classification", "userNote", "classificationSystem", "handlingDescription"});
        profileUsed.setMD_Releasability(new String[] {"addressee", "statement", "disseminationConstraints"});
        profileUsed.setLI_Lineage(new String[] {"statement", "scope", "additionalDocumentation", "processStep", "source"});
        profileUsed.setLI_ProcessStep(new String[] {"description", "rationale", "stepDateTime", "processor", "reference", "scope", "source"});
        profileUsed.setLI_Source(new String[] {"description", "sourceSpatialResolution", "sourceReferenceSystem", "sourceCitation", "sourceMetadata", "scope", "sourceStep"});
        profileUsed.setMD_MaintenanceInformation(new String[] {"maintenanceAndUpdateFrequency", "maintenanceDate", "userDefinedMaintenanceFrequency", "maintenanceScope", "maintenanceNote", "contact"});
        profileUsed.setMD_GridSpatialRepresentation(new String[] {"numberOfDimensions", "axisDimensionProperties", "cellGeometry", "transformationParameterAvailability"});
        profileUsed.setMD_Georectified(new String[] {"numberOfDimensions", "axisDimensionProperties", "cellGeometry", "transformationParameterAvailability", "checkPointAvailability", "checkPointDescription", "cornerPoints", "centrePoint", "pointInPixel", "transformationDimensionDescription", "transformationDimensionMapping"});
        profileUsed.setMD_Georeferenceable(new String[] {"numberOfDimensions", "axisDimensionProperties", "cellGeometry", "transformationParameterAvailability", "controlPointAvailability", "orientationParameterAvailability", "orientationParameterDescription", "georeferencesParameters", "parameterCitation"});
        profileUsed.setMD_VectorSpatialRepresentation(new String[] {"topologyLevel", "geometricObjects"});
        profileUsed.setMD_Dimension(new String[] {"dimensionName", "dimensionSize", "resolution", "dimensionTitle", "dimensionDescription"});
        profileUsed.setMD_GeometricObjects(new String[] {"geometricObjectType", "geometricObjectCount"});
        profileUsed.setMD_ReferenceSystem(new String[] {"referenceSystemIdentifier", "referenceSystemType"});
        profileUsed.setMD_FeatureCatalogueDescription(new String[] {"complianceCode", "locale", "includeWithDataset", "featureTypes", "featureCatalogueCitation"});
        profileUsed.setMD_FeatureCatalogue(new String[] {"featureCatalogue"});
        profileUsed.setMD_CoverageDescription(new String[] {"attributeDescription", "processingLevelCode", "attributeGroup"});
        profileUsed.setMD_ImageDescription(new String[] {"attributeDescription", "processingLevelCode", "attributeGroup", "illuminationElevationAngle", "illuminationAzimuthAngle", "imagingCondition", "imageQualityCode", "cloudCoverPercentage", "compressionGenerationQuantity", "triangulationIndicator", "radiometricCalibrationDataAvailability", "cameraCalibrationInformationAvailability", "filmDistortionInformationAvailability", "lensDistortionInformationAvailability"});
        profileUsed.setMD_AttributeGroup(new String[] {"contentType", "attribute"});
        profileUsed.setMD_RangeDimension(new String[] {"sequenceIdentifier", "description", "name"});
        profileUsed.setMD_SampleDimension(new String[] {"sequenceIdentifier", "description", "name", "maxValue", "minValue", "units", "scaleFactor", "offset", "meanValue", "numberOfValues", "standardDeciation", "otherPropertyType", "bitsPerValue"});
        profileUsed.setMD_Band(new String[] {"sequenceIdentifier", "description", "name", "maxValue", "minValue", "units", "scaleFactor", "offset", "meanValue", "numberOfValues", "standardDeciation", "otherPropertyType", "bitsPerValue", "boundMax", "boundMin", "boundUnit", "peakResponse", "toneGradation"});
        profileUsed.setMD_FeatureTypeInfo(new String[] {"featureTypeName", "featureInstanceCount"});
        profileUsed.setMD_PortrayalCatalogueReference(new String[] {"portrayalCatalogueCitation"});
        profileUsed.setMD_Distribution(new String[] {"description", "distributionFormat", "distributor", "transferOptions"});
        profileUsed.setMD_DigitalTransferOptions(new String[] {"unitsOfDistribtion", "transferSize", "onLine", "offLine", "transferFrequency", "distributionFormat"});
        profileUsed.setMD_Distributor(new String[] {"distributorContact", "distributionOrderProcess", "distributorFormat", "distributorTransferOptions"});
        profileUsed.setMD_Format(new String[] {"formatSpecificationCitation", "amendmentNumber", "fileDecompressionTechnique", "medium", "formatDistributor"});
        profileUsed.setMD_Medium(new String[] {"name", "density", "densityUnits", "volumes", "mediumFormat", "mediumNote", "identifier"});
        profileUsed.setMD_StandardOrderProcess(new String[] {"fees", "plannedAvailableDateTime", "orderingInstructions", "turnaround", "orderOptionsType", "orderOptions"});
        profileUsed.setMD_MetadataExtensionInformation(new String[] {"extensionOnLineResource", "extendedElementInformation"});
        profileUsed.setMD_ExtendedElementInformation(new String[] {"name", "definition", "obligation", "condition", "dataType", "maximumOccurrence", "domainValue", "parentEntity", "rule", "rationale", "source", "conceptName", "code"});
        profileUsed.setMD_ApplicationSchemaInformation(new String[] {"name", "schemaLanguage", "constraintLanguage", "schemaAscii", "graphicsFile", "softwareDevelopmentFile", "softwareDevelopmentFileFormat"});
        profileUsed.setSV_ServiceIdentification(new String[] {"citation", "abstract", "purpose", "credit", "status", "pointOfContact", "spatialRepresentationType", "spatialResolution", "temporalResolution", "topicCategory", "extent", "additionalDocumentation", "processingLevel", "resourceMaintenance", "graphicOverview", "resourceFormat", "descriptiveKeywords", "resourceSpecificUsage", "resourceConstraints", "associatedResource", "serviceType", "serviceTypeVersion", "accessProperties", "couplingType", "coupledResource", "operatedDataset", "profile", "serviceStandard", "containsOperations", "operatedOn", "containsChain"});
        profileUsed.setSV_OperationMetadata(new String[] {"operationName", "distributedComputingPlatform", "operationDescription", "invocationName", "connectPoint", "parameters", "dependsOn"});
        profileUsed.setSV_OperationChainMetadata(new String[] {"name", "description", "operation"});
        profileUsed.setSV_Parameter(new String[] {"name", "direction", "description", "optionality", "repeatability"});
        profileUsed.setSV_CoupledResource(new String[] {"scopedName", "resourceReference", "resource", "operation"});
        profileUsed.setEX_Extent(new String[] {"description", "geographicElement", "temporalElement", "verticalElement"});
        profileUsed.setEX_BoundingPolygon(new String[] {"extentTypeCode", "polygon"});
        profileUsed.setEX_GeographicBoundingBox(new String[] {"extentTypeCode", "westBoundLongitude", "eastBoundLongitude", "southBoundLatitude", "northBoundLatitude"});
        profileUsed.setEX_GeographicDescription(new String[] {"extentTypeCode", "geographicIdentifier"});
        profileUsed.setEX_TemporalExtent(new String[] {"extent"});
        profileUsed.setEX_SpatialTemporalExtent(new String[] {"extent", "verticalExtent", "spatialExtent"});
        profileUsed.setEX_VerticalExtent(new String[] {"minimumValue", "maximumValue", "verticalCRS", "verticalCRSId"});
        profileUsed.setCI_Citation(new String[] {"title", "alternateTitle", "date", "edition", "editionDate", "identifier", "citedResponsibleParty", "presentationForm", "series", "otherCitationDetails", "ISBN", "ISSN", "onlineResource", "graphic"});
        profileUsed.setCI_Responsibility(new String[] {"role", "extent", "party"});
        profileUsed.setCI_Individual(new String[] {"name", "contactInfo", "positionName"});
        profileUsed.setCI_Organisation(new String[] {"name", "contactInfo", "logo", "individual"});
        profileUsed.setCI_Address(new String[] {"deliveryPoint", "city", "administrativeArea", "postalCode", "country", "electronicMailAddress"});
        profileUsed.setCI_Contact(new String[] {"phone", "address", "onlineResource", "hoursOfService", "contactInstructions", "contactType"});
        profileUsed.setCI_Date(new String[] {"date", "dateType"});
        profileUsed.setCI_OnlineResource(new String[] {"linkage", "protocol", "applicationProfile", "name", "description", "function", "protocolRequest"});
        profileUsed.setCI_Series(new String[] {"name", "issueIdentification", "page"});
        profileUsed.setCI_Telephone(new String[] {"number", "numberType"});
        profileUsed.setMD_Scope(new String[] {"level", "extent", "levelDescription"});
        profileUsed.setMD_ScopeDescription(new String[] {"attributes", "features", "featureInstances", "attributeInstances", "dataset", "other"});
        profileUsed.setMD_Identifier(new String[] {"authority", "code", "codeSpace", "version", "description"});
        profileUsed.setMD_BrowseGraphic(new String[] {"fileName", "fileDescription", "fileType", "imageConstraints", "linkage"});
        profileUsed.setPT_FreeText(new String[] {"textGroup"});
        profileUsed.setLocalisedCharacterString(new String[] {"locale"});
        profileUsed.setPT_Locale(new String[] {"language", "country", "characterEncoding"});
        profileUsed.setPT_LocaleContainer(new String[] {"description", "locale", "date", "responsibleParty", "localisedString"});
        profileUsed.setDQ_DataQuality(new String[] {"scope", "report", "standaloneQualityReport"});
        profileUsed.setDQ_CompletenessCommission(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"});
        profileUsed.setDQ_CompletenessOmission(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"});
        profileUsed.setDQ_ConceptualConsistency(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"});
        profileUsed.setDQ_DomainConsistency(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"});
        profileUsed.setDQ_FormatConsistency(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"});
        profileUsed.setDQ_TopologicalConsistency(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"});
        profileUsed.setDQ_AbsoluteExternalPositionalAccuracy(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"});
        profileUsed.setDQ_RelativeInternalPositionalAccuracy(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"});
        profileUsed.setDQ_GriddedDataPositionalAccuracy(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"});
        profileUsed.setDQ_AccuracyOfATimeMeasurement(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"});
        profileUsed.setDQ_TemporalConsistency(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"});
        profileUsed.setDQ_TemporalValidity(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"});
        profileUsed.setDQ_ThematicClassificationCorrectness(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"});
        profileUsed.setDQ_NonQuantitativeAttributeCorrectness(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"});
        profileUsed.setDQ_QuantitativeAttributeAccuracy(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"});
        profileUsed.setDQ_UsabilityElement(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement"});
        profileUsed.setDQ_Confidence(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement", "relatedElement"});
        profileUsed.setDQ_Representativity(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement", "relatedElement"});
        profileUsed.setDQ_Homogeneity(new String[] {"standaloneQualityReportDetails", "measure", "evaluationMethod", "result", "derivedElement", "relatedElement"});
        profileUsed.setDQ_MeasureReference(new String[] {"measureIdentification", "nameOfMeasure", "measureDescription"});
        profileUsed.setDQ_EvaluationMethod(new String[] {"evaluationMethodType", "evaluationMethodDescription", "evaluationProcedure", "referenceDoc", "dateTime"});
        profileUsed.setDQ_FullInspection(new String[] {"evaluationMethodType", "evaluationMethodDescription", "evaluationProcedure", "referenceDoc", "dateTime"});
        profileUsed.setDQ_IndirectEvaluation(new String[] {"evaluationMethodType", "evaluationMethodDescription", "evaluationProcedure", "referenceDoc", "dateTime", "deductiveSource"});
        profileUsed.setDQ_SampleBasedInspection(new String[] {"evaluationMethodType", "evaluationMethodDescription", "evaluationProcedure", "referenceDoc", "dateTime", "samplingScheme", "lotDescription", "samplingRatio"});
        profileUsed.setDQ_AggregationDerivation(new String[] {"evaluationMethodType", "evaluationMethodDescription", "evaluationProcedure", "referenceDoc", "dateTime"});
        profileUsed.setDQ_ConformanceResult(new String[] {"resultScope", "dateTime", "specification", "explanation", "pass"});
        profileUsed.setDQ_QuantitativeResult(new String[] {"resultScope", "dateTime", "value", "valueUnit", "valueRecordType"});
        profileUsed.setDQ_DescriptiveResult(new String[] {"resultScope", "dateTime", "statement"});
        profileUsed.setDQ_StandaloneQualityReportInformation(new String[] {"reportReference", "abstract"});
        profileUsed.setMI_Metadata(new String[] {"acquisitionInformation"});
        profileUsed.setMI_AcquisitionInformation(new String[] {"scope", "acquisitionPlan", "acquisitionRequirement", "environmentalConditions", "instrument", "objective", "operation", "platform"});
        profileUsed.setMI_EnvironmentalRecord(new String[] {"averageAirTemperature", "maxRelativeHumidity", "maxAltitude", "meteorologicalConditions", "solarAzimuth", "solarElevation"});
        profileUsed.setMI_Instrument(new String[] {"citation", "identifier", "type", "description", "otherProperty", "otherPropertyType", "mountedOn", "sensor", "history"});
        profileUsed.setMI_Sensor(new String[] {"citation", "identifier", "type", "description", "otherProperty", "otherPropertyType", "mountedOn", "sensor", "history", "hosted"});
        profileUsed.setMI_Objective(new String[] {"identifier", "priority", "type", "function", "extent", "objectiveOccurence", "pass", "sensingInstrument"});
        profileUsed.setMI_Operation(new String[] {"description", "citation", "identifier", "status", "type", "otherProperty", "otherPropertyType", "childOperation", "objective", "parentOperation", "plan", "platform", "significantEvent"});
        profileUsed.setMI_Plan(new String[] {"type", "status", "citation", "operation", "satisfiedRequirement"});
        profileUsed.setMI_Event(new String[] {"identifier", "trigger", "context", "sequence", "time", "expectedObjective", "relatedPass", "relatedInstrument"});
        profileUsed.setMI_Platform(new String[] {"citation", "identifier", "description", "sponsor", "otherProperty", "otherPropertyType", "instrument"});
        profileUsed.setMI_PlatformPass(new String[] {"identifier", "extent", "relatedEvent"});
        profileUsed.setMI_RequestedDate(new String[] {"requestedDateOfCollection", "latestAcceptableDate"});
        profileUsed.setMI_Requirement(new String[] {"citation", "identifier", "requestor", "recipient", "priority", "requestedDate", "expiryDate", "satisfiedPlan"});
        profileUsed.setMI_InstrumentEventList(new String[] {"citation", "description", "locale", "constraints", "instrumentationEvent"});
        profileUsed.setMI_InstrumentEvent(new String[] {"citation", "description", "extent", "type", "revisionHistory"});
        profileUsed.setMI_Revision(new String[] {"description", "responsibleParty", "dateInfo"});
        profileUsed.setLE_Algorithm(new String[] {"citation", "description"});
        profileUsed.setLE_NominalResolution(new String[] {"scanningResolution", "groundResolution"});
        profileUsed.setLE_Processing(new String[] {"identifier", "softwareReference", "procedureDescription", "documentation", "runTimeParameters", "documentation", "runTimeParameters", "otherProperty", "otherPropertyType", "algorithm"});
        profileUsed.setLE_ProcessParameter(new String[] {"name", "direction", "description", "optionality", "repeatability", "valueType", "value", "resource"});
        profileUsed.setLE_ProcessStep(new String[] {"output", "processingInformation", "report", "description", "rationale", "stepDateTime", "processor", "reference", "scope", "source"});
        profileUsed.setLE_ProcessStepReport(new String[] {"name", "description", "fileType"});
        profileUsed.setLE_Source(new String[] {"processedLevel", "resolution", "description", "sourceSpatialResolution", "sourceReferenceSystem", "sourceCitation", "sourceMetadata", "scope", "sourceStep"});
        profileUsed.setMI_Georectified(new String[] {"checkPoint", "numberOfDimensions", "axisDimensionProperties", "cellGeometry", "transformationParameterAvailability", "checkPointAvailability", "checkPointDescription", "cornerPoints", "centrePoint", "pointInPixel", "transformationDimensionDescription", "transformationDimensionMapping"});
        profileUsed.setMI_Georeferenceable(new String[] {"geolocationInformation", "numberOfDimensions", "axisDimensionProperties", "cellGeometry", "transformationParameterAvailability", "controlPointAvailability", "orientationParameterAvailability", "orientationParameterDescription", "georeferencesParameters", "parameterCitation"});
        profileUsed.setMI_GeolocationInformation(new String[] {"qualityInfo"});
        profileUsed.setMI_GCPCollection(new String[] {"collectionIdentification", "collectionName", "coordinateReferenceSystem", "gcp"});
        profileUsed.setMI_GCP(new String[] {"geographicCoordinates", "accuracyReport"});
        profileUsed.setMI_Band(new String[] {"bandBoundaryDefinition", "nominalSpatialResolution", "transferFunctionType", "transmittedPolarisation", "detectedPolarisation"});
        profileUsed.setMI_CoverageDescription(new String[] {"rangeElementDescription", "attributeDescription", "processingLevelCode", "attributeGroup"});
        profileUsed.setMI_ImageDescription(new String[] {"rangeElementDescription", "attributeDescription", "processingLevelCode", "attributeGroup", "illuminationElevationAngle", "illuminationAzimuthAngle", "imagingCondition", "imageQualityCode", "cloudCoverPercentage", "compressionGenerationQuantity", "triangulationIndicator", "radiometricCalibrationDataAvailability", "cameraCalibrationInformationAvailability", "filmDistortionInformationAvailability", "lensDistortionInformationAvailability"});
        profileUsed.setMI_RangeElementDescription(new String[] {"name", "definition", "rangeElement"});
        profileUsed.setObjectDomain(new String[] {"scope", "validity"});
        profileUsed.setVerticalCRS(new String[] {"name", "identifier", "alias", "remarks", "usage", "coordinateSystem", "datum", "datumEnsemble", "geoidModel", "velocityModel"});
        profileUsed.setConversion(new String[] {"name", "identifier", "alias", "remarks", "usage", "sourceCRS", "targetCRS", "interpolationCRS", "sourceCoordinateEpoch", "targetCoordinateEpoch", "operationVersion", "coordinateOperationAccuracy", "method", "parameterValue"});
        profileUsed.setDataEpoch(new String[] {"coordinateEpoch"});
        profileUsed.setPointMotionOperation(new String[] {"name", "identifier", "alias", "remarks", "usage", "sourceCRS", "targetCRS", "interpolationCRS", "sourceCoordinateEpoch", "targetCoordinateEpoch", "operationVersion", "coordinateOperationAccuracy", "method", "parameterValue"});
        profileUsed.setOperationMethod(new String[] {"name", "identifier", "alias", "remarks", "parameter", "formulaReference"});
        profileUsed.setFormula(new String[] {"formula", "formulaCitation"});
        profileUsed.setTM_Instant(new String[] {"position"});
        profileUsed.setTM_Period(new String[] {"begin", "end"});

        profileObligation.setDS_DataSet(new String[] {});
        profileObligation.setSV_Service(new String[] {});
        profileObligation.setDS_OtherAggregate(new String[] {});
        profileObligation.setDS_StereoMate(new String[] {});
        profileObligation.setDS_Initiative(new String[] {});
        profileObligation.setDS_Series(new String[] {});
        profileObligation.setDS_Platform(new String[] {});
        profileObligation.setDS_Sensor(new String[] {});
        profileObligation.setDS_ProductionSeries(new String[] {});
        profileObligation.setMD_Metadata(new String[] {});
        profileObligation.setMD_MetadataScope(new String[] {});
        profileObligation.setMD_DataIdentification(new String[] {});
        profileObligation.setMD_Keywords(new String[] {});
        profileObligation.setMD_KeywordClass(new String[] {});
        profileObligation.setMD_RepresentativeFraction(new String[] {});
        profileObligation.setMD_Resolution(new String[] {});
        profileObligation.setMD_Usage(new String[] {});
        profileObligation.setMD_AssociatedResource(new String[] {});
        profileObligation.setMD_Constraints(new String[] {});
        profileObligation.setMD_LegalConstraints(new String[] {});
        profileObligation.setMD_SecurityConstraints(new String[] {});
        profileObligation.setMD_Releasability(new String[] {});
        profileObligation.setLI_Lineage(new String[] {});
        profileObligation.setLI_ProcessStep(new String[] {});
        profileObligation.setLI_Source(new String[] {});
        profileObligation.setMD_MaintenanceInformation(new String[] {});
        profileObligation.setMD_GridSpatialRepresentation(new String[] {});
        profileObligation.setMD_Georectified(new String[] {});
        profileObligation.setMD_Georeferenceable(new String[] {});
        profileObligation.setMD_VectorSpatialRepresentation(new String[] {});
        profileObligation.setMD_Dimension(new String[] {});
        profileObligation.setMD_GeometricObjects(new String[] {});
        profileObligation.setMD_ReferenceSystem(new String[] {});
        profileObligation.setMD_FeatureCatalogueDescription(new String[] {});
        profileObligation.setMD_FeatureCatalogue(new String[] {});
        profileObligation.setMD_CoverageDescription(new String[] {});
        profileObligation.setMD_ImageDescription(new String[] {});
        profileObligation.setMD_AttributeGroup(new String[] {});
        profileObligation.setMD_RangeDimension(new String[] {});
        profileObligation.setMD_SampleDimension(new String[] {});
        profileObligation.setMD_Band(new String[] {});
        profileObligation.setMD_FeatureTypeInfo(new String[] {});
        profileObligation.setMD_PortrayalCatalogueReference(new String[] {});
        profileObligation.setMD_Distribution(new String[] {});
        profileObligation.setMD_DigitalTransferOptions(new String[] {});
        profileObligation.setMD_Distributor(new String[] {});
        profileObligation.setMD_Format(new String[] {});
        profileObligation.setMD_Medium(new String[] {});
        profileObligation.setMD_StandardOrderProcess(new String[] {});
        profileObligation.setMD_MetadataExtensionInformation(new String[] {});
        profileObligation.setMD_ExtendedElementInformation(new String[] {});
        profileObligation.setMD_ApplicationSchemaInformation(new String[] {});
        profileObligation.setSV_ServiceIdentification(new String[] {});
        profileObligation.setSV_OperationMetadata(new String[] {});
        profileObligation.setSV_OperationChainMetadata(new String[] {});
        profileObligation.setSV_Parameter(new String[] {});
        profileObligation.setSV_CoupledResource(new String[] {});
        profileObligation.setEX_Extent(new String[] {});
        profileObligation.setEX_BoundingPolygon(new String[] {});
        profileObligation.setEX_GeographicBoundingBox(new String[] {});
        profileObligation.setEX_GeographicDescription(new String[] {});
        profileObligation.setEX_TemporalExtent(new String[] {});
        profileObligation.setEX_SpatialTemporalExtent(new String[] {});
        profileObligation.setEX_VerticalExtent(new String[] {});
        profileObligation.setCI_Citation(new String[] {});
        profileObligation.setCI_Responsibility(new String[] {});
        profileObligation.setCI_Individual(new String[] {});
        profileObligation.setCI_Organisation(new String[] {});
        profileObligation.setCI_Address(new String[] {});
        profileObligation.setCI_Contact(new String[] {});
        profileObligation.setCI_Date(new String[] {});
        profileObligation.setCI_OnlineResource(new String[] {});
        profileObligation.setCI_Series(new String[] {});
        profileObligation.setCI_Telephone(new String[] {});
        profileObligation.setMD_Scope(new String[] {});
        profileObligation.setMD_ScopeDescription(new String[] {});
        profileObligation.setMD_Identifier(new String[] {});
        profileObligation.setMD_BrowseGraphic(new String[] {});
        profileObligation.setPT_FreeText(new String[] {});
        profileObligation.setLocalisedCharacterString(new String[] {});
        profileObligation.setPT_Locale(new String[] {});
        profileObligation.setPT_LocaleContainer(new String[] {});
        profileObligation.setDQ_DataQuality(new String[] {});
        profileObligation.setDQ_CompletenessCommission(new String[] {});
        profileObligation.setDQ_CompletenessOmission(new String[] {});
        profileObligation.setDQ_ConceptualConsistency(new String[] {});
        profileObligation.setDQ_DomainConsistency(new String[] {});
        profileObligation.setDQ_FormatConsistency(new String[] {});
        profileObligation.setDQ_TopologicalConsistency(new String[] {});
        profileObligation.setDQ_AbsoluteExternalPositionalAccuracy(new String[] {});
        profileObligation.setDQ_RelativeInternalPositionalAccuracy(new String[] {});
        profileObligation.setDQ_GriddedDataPositionalAccuracy(new String[] {});
        profileObligation.setDQ_AccuracyOfATimeMeasurement(new String[] {});
        profileObligation.setDQ_TemporalConsistency(new String[] {});
        profileObligation.setDQ_TemporalValidity(new String[] {});
        profileObligation.setDQ_ThematicClassificationCorrectness(new String[] {});
        profileObligation.setDQ_NonQuantitativeAttributeCorrectness(new String[] {});
        profileObligation.setDQ_QuantitativeAttributeAccuracy(new String[] {});
        profileObligation.setDQ_UsabilityElement(new String[] {});
        profileObligation.setDQ_Confidence(new String[] {});
        profileObligation.setDQ_Representativity(new String[] {});
        profileObligation.setDQ_Homogeneity(new String[] {});
        profileObligation.setDQ_MeasureReference(new String[] {});
        profileObligation.setDQ_EvaluationMethod(new String[] {});
        profileObligation.setDQ_FullInspection(new String[] {});
        profileObligation.setDQ_IndirectEvaluation(new String[] {});
        profileObligation.setDQ_SampleBasedInspection(new String[] {});
        profileObligation.setDQ_AggregationDerivation(new String[] {});
        profileObligation.setDQ_ConformanceResult(new String[] {});
        profileObligation.setDQ_QuantitativeResult(new String[] {});
        profileObligation.setDQ_DescriptiveResult(new String[] {});
        profileObligation.setDQ_StandaloneQualityReportInformation(new String[] {});
        profileObligation.setMI_Metadata(new String[] {});
        profileObligation.setMI_AcquisitionInformation(new String[] {});
        profileObligation.setMI_EnvironmentalRecord(new String[] {});
        profileObligation.setMI_Instrument(new String[] {});
        profileObligation.setMI_Sensor(new String[] {});
        profileObligation.setMI_Objective(new String[] {});
        profileObligation.setMI_Operation(new String[] {});
        profileObligation.setMI_Plan(new String[] {});
        profileObligation.setMI_Event(new String[] {});
        profileObligation.setMI_Platform(new String[] {});
        profileObligation.setMI_PlatformPass(new String[] {});
        profileObligation.setMI_RequestedDate(new String[] {});
        profileObligation.setMI_Requirement(new String[] {});
        profileObligation.setMI_InstrumentEventList(new String[] {});
        profileObligation.setMI_InstrumentEvent(new String[] {});
        profileObligation.setMI_Revision(new String[] {});
        profileObligation.setLE_Algorithm(new String[] {});
        profileObligation.setLE_NominalResolution(new String[] {});
        profileObligation.setLE_Processing(new String[] {});
        profileObligation.setLE_ProcessParameter(new String[] {});
        profileObligation.setLE_ProcessStep(new String[] {});
        profileObligation.setLE_ProcessStepReport(new String[] {});
        profileObligation.setLE_Source(new String[] {});
        profileObligation.setMI_Georectified(new String[] {});
        profileObligation.setMI_Georeferenceable(new String[] {});
        profileObligation.setMI_GeolocationInformation(new String[] {});
        profileObligation.setMI_GCPCollection(new String[] {});
        profileObligation.setMI_GCP(new String[] {});
        profileObligation.setMI_Band(new String[] {});
        profileObligation.setMI_CoverageDescription(new String[] {});
        profileObligation.setMI_ImageDescription(new String[] {});
        profileObligation.setMI_RangeElementDescription(new String[] {});
        profileObligation.setObjectDomain(new String[] {});
        profileObligation.setVerticalCRS(new String[] {});
        profileObligation.setConversion(new String[] {});
        profileObligation.setDataEpoch(new String[] {});
        profileObligation.setPointMotionOperation(new String[] {});
        profileObligation.setOperationMethod(new String[] {});
        profileObligation.setFormula(new String[] {});
        profileObligation.setTM_Instant(new String[] {});
        profileObligation.setTM_Period(new String[] {});

        profile = new Profile();
        profile.setUsed(profileUsed);
        profile.setObligation(profileObligation);
    }
}
