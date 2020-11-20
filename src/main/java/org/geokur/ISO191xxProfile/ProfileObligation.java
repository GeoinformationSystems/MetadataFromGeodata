/*
 * @author: Michael Wagner
 * @organization: TU Dresden
 * @contact: michael.wagner@tu-dresden.de
 */

package org.geokur.ISO191xxProfile;

public class ProfileObligation {
    public String[] DS_DataSet;
    public String[] SV_Service;
    public String[] DS_OtherAggregate;
    public String[] DS_StereoMate;
    public String[] DS_Initiative;
    public String[] DS_Series;
    public String[] DS_Platform;
    public String[] DS_Sensor;
    public String[] DS_ProductionSeries;
    public String[] MD_Metadata;
    public String[] MD_MetadataScope;
    public String[] MD_DataIdentification;
    public String[] MD_Keywords;
    public String[] MD_KeywordClass;
    public String[] MD_RepresentativeFraction;
    public String[] MD_Resolution;
    public String[] MD_Usage;
    public String[] MD_AssociatedResource;
    public String[] MD_Constraints;
    public String[] MD_LegalConstraints;
    public String[] MD_SecurityConstraints;
    public String[] MD_Releasability;
    public String[] LI_Lineage;
    public String[] LI_ProcessStep;
    public String[] LI_Source;
    public String[] MD_MaintenanceInformation;
    public String[] MD_GridSpatialRepresentation;
    public String[] MD_Georectified;
    public String[] MD_Georeferenceable;
    public String[] MD_VectorSpatialRepresentation;
    public String[] MD_Dimension;
    public String[] MD_GeometricObjects;
    public String[] MD_ReferenceSystem;
    public String[] MD_FeatureCatalogueDescription;
    public String[] MD_FeatureCatalogue;
    public String[] MD_CoverageDescription;
    public String[] MD_ImageDescription;
    public String[] MD_AttributeGroup;
    public String[] MD_RangeDimension;
    public String[] MD_SampleDimension;
    public String[] MD_Band;
    public String[] MD_FeatureTypeInfo;
    public String[] MD_PortrayalCatalogueReference;
    public String[] MD_Distribution;
    public String[] MD_DigitalTransferOptions;
    public String[] MD_Distributor;
    public String[] MD_Format;
    public String[] MD_Medium;
    public String[] MD_StandardOrderProcess;
    public String[] MD_MetadataExtensionInformation;
    public String[] MD_ExtendedElementInformation;
    public String[] MD_ApplicationSchemaInformation;
    public String[] SV_ServiceIdentification;
    public String[] SV_OperationMetadata;
    public String[] SV_OperationChainMetadata;
    public String[] SV_Parameter;
    public String[] SV_CoupledResource;
    public String[] EX_Extent;
    public String[] EX_BoundingPolygon;
    public String[] EX_GeographicBoundingBox;
    public String[] EX_GeographicDescription;
    public String[] EX_TemporalExtent;
    public String[] EX_SpatialTemporalExtent;
    public String[] EX_VerticalExtent;
    public String[] CI_Citation;
    public String[] CI_Responsibility;
    public String[] CI_Individual;
    public String[] CI_Organisation;
    public String[] CI_Address;
    public String[] CI_Contact;
    public String[] CI_Date;
    public String[] CI_OnlineResource;
    public String[] CI_Series;
    public String[] CI_Telephone;
    public String[] MD_Scope;
    public String[] MD_ScopeDescription;
    public String[] MD_Identifier;
    public String[] MD_BrowseGraphic;
    public String[] PT_FreeText;
    public String[] LocalisedCharacterString;
    public String[] PT_Locale;
    public String[] PT_LocaleContainer;
    public String[] DQ_DataQuality;
    public String[] DQ_CompletenessCommission;
    public String[] DQ_CompletenessOmission;
    public String[] DQ_ConceptualConsistency;
    public String[] DQ_DomainConsistency;
    public String[] DQ_FormatConsistency;
    public String[] DQ_TopologicalConsistency;
    public String[] DQ_AbsoluteExternalPositionalAccuracy;
    public String[] DQ_RelativeInternalPositionalAccuracy;
    public String[] DQ_GriddedDataPositionalAccuracy;
    public String[] DQ_AccuracyOfATimeMeasurement;
    public String[] DQ_TemporalConsistency;
    public String[] DQ_TemporalValidity;
    public String[] DQ_ThematicClassificationCorrectness;
    public String[] DQ_NonQuantitativeAttributeCorrectness;
    public String[] DQ_QuantitativeAttributeAccuracy;
    public String[] DQ_UsabilityElement;
    public String[] DQ_Confidence;
    public String[] DQ_Representativity;
    public String[] DQ_Homogeneity;
    public String[] DQ_MeasureReference;
    public String[] DQ_EvaluationMethod;
    public String[] DQ_FullInspection;
    public String[] DQ_IndirectEvaluation;
    public String[] DQ_SampleBasedInspection;
    public String[] DQ_AggregationDerivation;
    public String[] DQ_ConformanceResult;
    public String[] DQ_QuantitativeResult;
    public String[] DQ_DescriptiveResult;
    public String[] DQ_StandaloneQualityReportInformation;
    public String[] DQM_Measure;
    public String[] DQM_BasicMeasure;
    public String[] DQM_Parameter;
    public String[] DQM_Description;
    public String[] DQM_SourceReference;
    public String[] LE_Algorithm;
    public String[] LE_NominalResolution;
    public String[] LE_Processing;
    public String[] LE_ProcessParameter;
    public String[] LE_ProcessStepReport;
    public String[] LE_ProcessStep;
    public String[] LE_Source;
    public String[] MI_AcquisitionInformation;
    public String[] MI_Band;
    public String[] MI_CoverageDescription;
    public String[] MI_EnvironmentalRecord;
    public String[] MI_Event;
    public String[] MI_GCPCollection;
    public String[] MI_GCP;
    public String[] MI_GeolocationInformation;
    public String[] MI_Georectified;
    public String[] MI_Georeferenceable;
    public String[] MI_ImageDescription;
    public String[] MI_InstrumentEventList;
    public String[] MI_InstrumentEvent;
    public String[] MI_Instrument;
    public String[] MI_Metadata;
    public String[] MI_Objective;
    public String[] MI_Operation;
    public String[] MI_Plan;
    public String[] MI_PlatformPass;
    public String[] MI_Platform;
    public String[] MI_RangeElementDescription;
    public String[] MI_RequestedDate;
    public String[] MI_Requirement;
    public String[] MI_Revision;
    public String[] MI_Sensor;
    public String[] ObjectDomain;
    public String[] VerticalCRS;
    public String[] Conversion;
    public String[] DataEpoch;
    public String[] PointMotionOperation;
    public String[] OperationMethod;
    public String[] Formula;

    public void setDS_DataSet(String[] DS_DataSet) {
        this.DS_DataSet = DS_DataSet;
    }

    public void setSV_Service(String[] SV_Service) {
        this.SV_Service = SV_Service;
    }

    public void setDS_OtherAggregate(String[] DS_OtherAggregate) {
        this.DS_OtherAggregate = DS_OtherAggregate;
    }

    public void setDS_StereoMate(String[] DS_StereoMate) {
        this.DS_StereoMate = DS_StereoMate;
    }

    public void setDS_Initiative(String[] DS_Initiative) {
        this.DS_Initiative = DS_Initiative;
    }

    public void setDS_Series(String[] DS_Series) {
        this.DS_Series = DS_Series;
    }

    public void setDS_Platform(String[] DS_Platform) {
        this.DS_Platform = DS_Platform;
    }

    public void setDS_Sensor(String[] DS_Sensor) {
        this.DS_Sensor = DS_Sensor;
    }

    public void setDS_ProductionSeries(String[] DS_ProductionSeries) {
        this.DS_ProductionSeries = DS_ProductionSeries;
    }

    public void setMD_Metadata(String[] MD_Metadata) {
        this.MD_Metadata = MD_Metadata;
    }

    public void setMD_MetadataScope(String[] MD_MetadataScope) {
        this.MD_MetadataScope = MD_MetadataScope;
    }

    public void setMD_DataIdentification(String[] MD_DataIdentification) {
        this.MD_DataIdentification = MD_DataIdentification;
    }

    public void setMD_Keywords(String[] MD_Keywords) {
        this.MD_Keywords = MD_Keywords;
    }

    public void setMD_KeywordClass(String[] MD_KeywordClass) {
        this.MD_KeywordClass = MD_KeywordClass;
    }

    public void setMD_RepresentativeFraction(String[] MD_RepresentativeFraction) {
        this.MD_RepresentativeFraction = MD_RepresentativeFraction;
    }

    public void setMD_Resolution(String[] MD_Resolution) {
        this.MD_Resolution = MD_Resolution;
    }

    public void setMD_Usage(String[] MD_Usage) {
        this.MD_Usage = MD_Usage;
    }

    public void setMD_AssociatedResource(String[] MD_AssociatedResource) {
        this.MD_AssociatedResource = MD_AssociatedResource;
    }

    public void setMD_Constraints(String[] MD_Constraints) {
        this.MD_Constraints = MD_Constraints;
    }

    public void setMD_LegalConstraints(String[] MD_LegalConstraints) {
        this.MD_LegalConstraints = MD_LegalConstraints;
    }

    public void setMD_SecurityConstraints(String[] MD_SecurityConstraints) {
        this.MD_SecurityConstraints = MD_SecurityConstraints;
    }

    public void setMD_Releasability(String[] MD_Releasability) {
        this.MD_Releasability = MD_Releasability;
    }

    public void setLI_Lineage(String[] LI_Lineage) {
        this.LI_Lineage = LI_Lineage;
    }

    public void setLI_ProcessStep(String[] LI_ProcessStep) {
        this.LI_ProcessStep = LI_ProcessStep;
    }

    public void setLI_Source(String[] LI_Source) {
        this.LI_Source = LI_Source;
    }

    public void setMD_MaintenanceInformation(String[] MD_MaintenanceInformation) {
        this.MD_MaintenanceInformation = MD_MaintenanceInformation;
    }

    public void setMD_GridSpatialRepresentation(String[] MD_GridSpatialRepresentation) {
        this.MD_GridSpatialRepresentation = MD_GridSpatialRepresentation;
    }

    public void setMD_Georectified(String[] MD_Georectified) {
        this.MD_Georectified = MD_Georectified;
    }

    public void setMD_Georeferenceable(String[] MD_Georeferenceable) {
        this.MD_Georeferenceable = MD_Georeferenceable;
    }

    public void setMD_VectorSpatialRepresentation(String[] MD_VectorSpatialRepresentation) {
        this.MD_VectorSpatialRepresentation = MD_VectorSpatialRepresentation;
    }

    public void setMD_Dimension(String[] MD_Dimension) {
        this.MD_Dimension = MD_Dimension;
    }

    public void setMD_GeometricObjects(String[] MD_GeometricObjects) {
        this.MD_GeometricObjects = MD_GeometricObjects;
    }

    public void setMD_ReferenceSystem(String[] MD_ReferenceSystem) {
        this.MD_ReferenceSystem = MD_ReferenceSystem;
    }

    public void setMD_FeatureCatalogueDescription(String[] MD_FeatureCatalogueDescription) {
        this.MD_FeatureCatalogueDescription = MD_FeatureCatalogueDescription;
    }

    public void setMD_FeatureCatalogue(String[] MD_FeatureCatalogue) {
        this.MD_FeatureCatalogue = MD_FeatureCatalogue;
    }

    public void setMD_CoverageDescription(String[] MD_CoverageDescription) {
        this.MD_CoverageDescription = MD_CoverageDescription;
    }

    public void setMD_ImageDescription(String[] MD_ImageDescription) {
        this.MD_ImageDescription = MD_ImageDescription;
    }

    public void setMD_AttributeGroup(String[] MD_AttributeGroup) {
        this.MD_AttributeGroup = MD_AttributeGroup;
    }

    public void setMD_RangeDimension(String[] MD_RangeDimension) {
        this.MD_RangeDimension = MD_RangeDimension;
    }

    public void setMD_SampleDimension(String[] MD_SampleDimension) {
        this.MD_SampleDimension = MD_SampleDimension;
    }

    public void setMD_Band(String[] MD_Band) {
        this.MD_Band = MD_Band;
    }

    public void setMD_FeatureTypeInfo(String[] MD_FeatureTypeInfo) {
        this.MD_FeatureTypeInfo = MD_FeatureTypeInfo;
    }

    public void setMD_PortrayalCatalogueReference(String[] MD_PortrayalCatalogueReference) {
        this.MD_PortrayalCatalogueReference = MD_PortrayalCatalogueReference;
    }

    public void setMD_Distribution(String[] MD_Distribution) {
        this.MD_Distribution = MD_Distribution;
    }

    public void setMD_DigitalTransferOptions(String[] MD_DigitalTransferOptions) {
        this.MD_DigitalTransferOptions = MD_DigitalTransferOptions;
    }

    public void setMD_Distributor(String[] MD_Distributor) {
        this.MD_Distributor = MD_Distributor;
    }

    public void setMD_Format(String[] MD_Format) {
        this.MD_Format = MD_Format;
    }

    public void setMD_Medium(String[] MD_Medium) {
        this.MD_Medium = MD_Medium;
    }

    public void setMD_StandardOrderProcess(String[] MD_StandardOrderProcess) {
        this.MD_StandardOrderProcess = MD_StandardOrderProcess;
    }

    public void setMD_MetadataExtensionInformation(String[] MD_MetadataExtensionInformation) {
        this.MD_MetadataExtensionInformation = MD_MetadataExtensionInformation;
    }

    public void setMD_ExtendedElementInformation(String[] MD_ExtendedElementInformation) {
        this.MD_ExtendedElementInformation = MD_ExtendedElementInformation;
    }

    public void setMD_ApplicationSchemaInformation(String[] MD_ApplicationSchemaInformation) {
        this.MD_ApplicationSchemaInformation = MD_ApplicationSchemaInformation;
    }

    public void setSV_ServiceIdentification(String[] SV_ServiceIdentification) {
        this.SV_ServiceIdentification = SV_ServiceIdentification;
    }

    public void setSV_OperationMetadata(String[] SV_OperationMetadata) {
        this.SV_OperationMetadata = SV_OperationMetadata;
    }

    public void setSV_OperationChainMetadata(String[] SV_OperationChainMetadata) {
        this.SV_OperationChainMetadata = SV_OperationChainMetadata;
    }

    public void setSV_Parameter(String[] SV_Parameter) {
        this.SV_Parameter = SV_Parameter;
    }

    public void setSV_CoupledResource(String[] SV_CoupledResource) {
        this.SV_CoupledResource = SV_CoupledResource;
    }

    public void setEX_Extent(String[] EX_Extent) {
        this.EX_Extent = EX_Extent;
    }

    public void setEX_BoundingPolygon(String[] EX_BoundingPolygon) {
        this.EX_BoundingPolygon = EX_BoundingPolygon;
    }

    public void setEX_GeographicBoundingBox(String[] EX_GeographicBoundingBox) {
        this.EX_GeographicBoundingBox = EX_GeographicBoundingBox;
    }

    public void setEX_GeographicDescription(String[] EX_GeographicDescription) {
        this.EX_GeographicDescription = EX_GeographicDescription;
    }

    public void setEX_TemporalExtent(String[] EX_TemporalExtent) {
        this.EX_TemporalExtent = EX_TemporalExtent;
    }

    public void setEX_SpatialTemporalExtent(String[] EX_SpatialTemporalExtent) {
        this.EX_SpatialTemporalExtent = EX_SpatialTemporalExtent;
    }

    public void setEX_VerticalExtent(String[] EX_VerticalExtent) {
        this.EX_VerticalExtent = EX_VerticalExtent;
    }

    public void setCI_Citation(String[] CI_Citation) {
        this.CI_Citation = CI_Citation;
    }

    public void setCI_Responsibility(String[] CI_Responsibility) {
        this.CI_Responsibility = CI_Responsibility;
    }

    public void setCI_Individual(String[] CI_Individual) {
        this.CI_Individual = CI_Individual;
    }

    public void setCI_Organisation(String[] CI_Organisation) {
        this.CI_Organisation = CI_Organisation;
    }

    public void setCI_Address(String[] CI_Address) {
        this.CI_Address = CI_Address;
    }

    public void setCI_Contact(String[] CI_Contact) {
        this.CI_Contact = CI_Contact;
    }

    public void setCI_Date(String[] CI_Date) {
        this.CI_Date = CI_Date;
    }

    public void setCI_OnlineResource(String[] CI_OnlineResource) {
        this.CI_OnlineResource = CI_OnlineResource;
    }

    public void setCI_Series(String[] CI_Series) {
        this.CI_Series = CI_Series;
    }

    public void setCI_Telephone(String[] CI_Telephone) {
        this.CI_Telephone = CI_Telephone;
    }

    public void setMD_Scope(String[] MD_Scope) {
        this.MD_Scope = MD_Scope;
    }

    public void setMD_ScopeDescription(String[] MD_ScopeDescription) {
        this.MD_ScopeDescription = MD_ScopeDescription;
    }

    public void setMD_Identifier(String[] MD_Identifier) {
        this.MD_Identifier = MD_Identifier;
    }

    public void setMD_BrowseGraphic(String[] MD_BrowseGraphic) {
        this.MD_BrowseGraphic = MD_BrowseGraphic;
    }

    public void setPT_FreeText(String[] PT_FreeText) {
        this.PT_FreeText = PT_FreeText;
    }

    public void setLocalisedCharacterString(String[] localisedCharacterString) {
        LocalisedCharacterString = localisedCharacterString;
    }

    public void setPT_Locale(String[] PT_Locale) {
        this.PT_Locale = PT_Locale;
    }

    public void setPT_LocaleContainer(String[] PT_LocaleContainer) {
        this.PT_LocaleContainer = PT_LocaleContainer;
    }

    public void setDQ_DataQuality(String[] DQ_DataQuality) {
        this.DQ_DataQuality = DQ_DataQuality;
    }

    public void setDQ_CompletenessCommission(String[] DQ_CompletenessCommission) {
        this.DQ_CompletenessCommission = DQ_CompletenessCommission;
    }

    public void setDQ_CompletenessOmission(String[] DQ_CompletenessOmission) {
        this.DQ_CompletenessOmission = DQ_CompletenessOmission;
    }

    public void setDQ_ConceptualConsistency(String[] DQ_ConceptualConsistency) {
        this.DQ_ConceptualConsistency = DQ_ConceptualConsistency;
    }

    public void setDQ_DomainConsistency(String[] DQ_DomainConsistency) {
        this.DQ_DomainConsistency = DQ_DomainConsistency;
    }

    public void setDQ_FormatConsistency(String[] DQ_FormatConsistency) {
        this.DQ_FormatConsistency = DQ_FormatConsistency;
    }

    public void setDQ_TopologicalConsistency(String[] DQ_TopologicalConsistency) {
        this.DQ_TopologicalConsistency = DQ_TopologicalConsistency;
    }

    public void setDQ_AbsoluteExternalPositionalAccuracy(String[] DQ_AbsoluteExternalPositionalAccuracy) {
        this.DQ_AbsoluteExternalPositionalAccuracy = DQ_AbsoluteExternalPositionalAccuracy;
    }

    public void setDQ_RelativeInternalPositionalAccuracy(String[] DQ_RelativeInternalPositionalAccuracy) {
        this.DQ_RelativeInternalPositionalAccuracy = DQ_RelativeInternalPositionalAccuracy;
    }

    public void setDQ_GriddedDataPositionalAccuracy(String[] DQ_GriddedDataPositionalAccuracy) {
        this.DQ_GriddedDataPositionalAccuracy = DQ_GriddedDataPositionalAccuracy;
    }

    public void setDQ_AccuracyOfATimeMeasurement(String[] DQ_AccuracyOfATimeMeasurement) {
        this.DQ_AccuracyOfATimeMeasurement = DQ_AccuracyOfATimeMeasurement;
    }

    public void setDQ_TemporalConsistency(String[] DQ_TemporalConsistency) {
        this.DQ_TemporalConsistency = DQ_TemporalConsistency;
    }

    public void setDQ_TemporalValidity(String[] DQ_TemporalValidity) {
        this.DQ_TemporalValidity = DQ_TemporalValidity;
    }

    public void setDQ_ThematicClassificationCorrectness(String[] DQ_ThematicClassificationCorrectness) {
        this.DQ_ThematicClassificationCorrectness = DQ_ThematicClassificationCorrectness;
    }

    public void setDQ_NonQuantitativeAttributeCorrectness(String[] DQ_NonQuantitativeAttributeCorrectness) {
        this.DQ_NonQuantitativeAttributeCorrectness = DQ_NonQuantitativeAttributeCorrectness;
    }

    public void setDQ_QuantitativeAttributeAccuracy(String[] DQ_QuantitativeAttributeAccuracy) {
        this.DQ_QuantitativeAttributeAccuracy = DQ_QuantitativeAttributeAccuracy;
    }

    public void setDQ_UsabilityElement(String[] DQ_UsabilityElement) {
        this.DQ_UsabilityElement = DQ_UsabilityElement;
    }

    public void setDQ_Confidence(String[] DQ_Confidence) {
        this.DQ_Confidence = DQ_Confidence;
    }

    public void setDQ_Representativity(String[] DQ_Representativity) {
        this.DQ_Representativity = DQ_Representativity;
    }

    public void setDQ_Homogeneity(String[] DQ_Homogeneity) {
        this.DQ_Homogeneity = DQ_Homogeneity;
    }

    public void setDQ_MeasureReference(String[] DQ_MeasureReference) {
        this.DQ_MeasureReference = DQ_MeasureReference;
    }

    public void setDQ_EvaluationMethod(String[] DQ_EvaluationMethod) {
        this.DQ_EvaluationMethod = DQ_EvaluationMethod;
    }

    public void setDQ_FullInspection(String[] DQ_FullInspection) {
        this.DQ_FullInspection = DQ_FullInspection;
    }

    public void setDQ_IndirectEvaluation(String[] DQ_IndirectEvaluation) {
        this.DQ_IndirectEvaluation = DQ_IndirectEvaluation;
    }

    public void setDQ_SampleBasedInspection(String[] DQ_SampleBasedInspection) {
        this.DQ_SampleBasedInspection = DQ_SampleBasedInspection;
    }

    public void setDQ_AggregationDerivation(String[] DQ_AggregationDerivation) {
        this.DQ_AggregationDerivation = DQ_AggregationDerivation;
    }

    public void setDQ_ConformanceResult(String[] DQ_ConformanceResult) {
        this.DQ_ConformanceResult = DQ_ConformanceResult;
    }

    public void setDQ_QuantitativeResult(String[] DQ_QuantitativeResult) {
        this.DQ_QuantitativeResult = DQ_QuantitativeResult;
    }

    public void setDQ_DescriptiveResult(String[] DQ_DescriptiveResult) {
        this.DQ_DescriptiveResult = DQ_DescriptiveResult;
    }

    public void setDQ_StandaloneQualityReportInformation(String[] DQ_StandaloneQualityReportInformation) {
        this.DQ_StandaloneQualityReportInformation = DQ_StandaloneQualityReportInformation;
    }

    public void setDQM_Measure(String[] DQM_Measure) {
        this.DQM_Measure = DQM_Measure;
    }

    public void setDQM_BasicMeasure(String[] DQM_BasicMeasure) {
        this.DQM_BasicMeasure = DQM_BasicMeasure;
    }

    public void setDQM_Parameter(String[] DQM_Parameter) {
        this.DQM_Parameter = DQM_Parameter;
    }

    public void setDQM_Description(String[] DQM_Description) {
        this.DQM_Description = DQM_Description;
    }

    public void setDQM_SourceReference(String[] DQM_SourceReference) {
        this.DQM_SourceReference = DQM_SourceReference;
    }

    public void setLE_Algorithm(String[] LE_Algorithm) {
        this.LE_Algorithm = LE_Algorithm;
    }

    public void setLE_NominalResolution(String[] LE_NominalResolution) {
        this.LE_NominalResolution = LE_NominalResolution;
    }

    public void setLE_Processing(String[] LE_Processing) {
        this.LE_Processing = LE_Processing;
    }

    public void setLE_ProcessParameter(String[] LE_ProcessParameter) {
        this.LE_ProcessParameter = LE_ProcessParameter;
    }

    public void setLE_ProcessStepReport(String[] LE_ProcessStepReport) {
        this.LE_ProcessStepReport = LE_ProcessStepReport;
    }

    public void setLE_ProcessStep(String[] LE_ProcessStep) {
        this.LE_ProcessStep = LE_ProcessStep;
    }

    public void setLE_Source(String[] LE_Source) {
        this.LE_Source = LE_Source;
    }

    public void setMI_AcquisitionInformation(String[] MI_AcquisitionInformation) {
        this.MI_AcquisitionInformation = MI_AcquisitionInformation;
    }

    public void setMI_Band(String[] MI_Band) {
        this.MI_Band = MI_Band;
    }

    public void setMI_CoverageDescription(String[] MI_CoverageDescription) {
        this.MI_CoverageDescription = MI_CoverageDescription;
    }

    public void setMI_EnvironmentalRecord(String[] MI_EnvironmentalRecord) {
        this.MI_EnvironmentalRecord = MI_EnvironmentalRecord;
    }

    public void setMI_Event(String[] MI_Event) {
        this.MI_Event = MI_Event;
    }

    public void setMI_GCPCollection(String[] MI_GCPCollection) {
        this.MI_GCPCollection = MI_GCPCollection;
    }

    public void setMI_GCP(String[] MI_GCP) {
        this.MI_GCP = MI_GCP;
    }

    public void setMI_GeolocationInformation(String[] MI_GeolocationInformation) {
        this.MI_GeolocationInformation = MI_GeolocationInformation;
    }

    public void setMI_Georectified(String[] MI_Georectified) {
        this.MI_Georectified = MI_Georectified;
    }

    public void setMI_Georeferenceable(String[] MI_Georeferenceable) {
        this.MI_Georeferenceable = MI_Georeferenceable;
    }

    public void setMI_ImageDescription(String[] MI_ImageDescription) {
        this.MI_ImageDescription = MI_ImageDescription;
    }

    public void setMI_InstrumentEventList(String[] MI_InstrumentEventList) {
        this.MI_InstrumentEventList = MI_InstrumentEventList;
    }

    public void setMI_InstrumentEvent(String[] MI_InstrumentEvent) {
        this.MI_InstrumentEvent = MI_InstrumentEvent;
    }

    public void setMI_Instrument(String[] MI_Instrument) {
        this.MI_Instrument = MI_Instrument;
    }

    public void setMI_Metadata(String[] MI_Metadata) {
        this.MI_Metadata = MI_Metadata;
    }

    public void setMI_Objective(String[] MI_Objective) {
        this.MI_Objective = MI_Objective;
    }

    public void setMI_Operation(String[] MI_Operation) {
        this.MI_Operation = MI_Operation;
    }

    public void setMI_Plan(String[] MI_Plan) {
        this.MI_Plan = MI_Plan;
    }

    public void setMI_PlatformPass(String[] MI_PlatformPass) {
        this.MI_PlatformPass = MI_PlatformPass;
    }

    public void setMI_Platform(String[] MI_Platform) {
        this.MI_Platform = MI_Platform;
    }

    public void setMI_RangeElementDescription(String[] MI_RangeElementDescription) {
        this.MI_RangeElementDescription = MI_RangeElementDescription;
    }

    public void setMI_RequestedDate(String[] MI_RequestedDate) {
        this.MI_RequestedDate = MI_RequestedDate;
    }

    public void setMI_Requirement(String[] MI_Requirement) {
        this.MI_Requirement = MI_Requirement;
    }

    public void setMI_Revision(String[] MI_Revision) {
        this.MI_Revision = MI_Revision;
    }

    public void setMI_Sensor(String[] MI_Sensor) {
        this.MI_Sensor = MI_Sensor;
    }

    public void setObjectDomain(String[] objectDomain) {
        ObjectDomain = objectDomain;
    }

    public void setVerticalCRS(String[] verticalCRS) {
        VerticalCRS = verticalCRS;
    }

    public void setConversion(String[] conversion) {
        Conversion = conversion;
    }

    public void setDataEpoch(String[] dataEpoch) {
        DataEpoch = dataEpoch;
    }

    public void setPointMotionOperation(String[] pointMotionOperation) {
        PointMotionOperation = pointMotionOperation;
    }

    public void setOperationMethod(String[] operationMethod) {
        OperationMethod = operationMethod;
    }

    public void setFormula(String[] formula) {
        Formula = formula;
    }
}
