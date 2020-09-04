/*
 * Copyright (c) 2020. Michael Wagner.
 * All rights reserved.
 */

package org.geokur.ISO19115Schema;

public class Profile {
    Used used;
    Obligation obligation;

    public void setUsed(Used used) {
        this.used = used;
    }

    public void setObligation(Obligation obligation) {
        this.obligation = obligation;
    }
}

class Used{
    String[] DS_DataSet;
    String[] SV_Service;
    String[] DS_OtherAggregate;
    String[] DS_StereoMate;
    String[] DS_Initiative;
    String[] DS_Series;
    String[] DS_Platform;
    String[] DS_Sensor;
    String[] DS_ProductionSeries;
    String[] MD_Metadata;
    String[] MD_MetadataScope;
    String[] MD_DataIdentification;
    String[] MD_Keywords;
    String[] MD_KeywordClass;
    String[] MD_RepresentativeFraction;
    String[] MD_Resolution;
    String[] MD_Usage;
    String[] MD_AssociatedResource;
    String[] MD_Constraints;
    String[] MD_LegalConstraints;
    String[] MD_SecurityConstraints;
    String[] MD_Releasability;
    String[] LI_Lineage;
    String[] LI_ProcessStep;
    String[] LI_Source;
    String[] MD_MaintenanceInformation;
    String[] MD_GridSpatialRepresentation;
    String[] MD_Georectified;
    String[] MD_Georeferenceable;
    String[] MD_VectorSpatialRepresentation;
    String[] MD_Dimension;
    String[] MD_GeometricObjects;
    String[] MD_ReferenceSystem;
    String[] MD_FeatureCatalogueDescription;
    String[] MD_FeatureCatalogue;
    String[] MD_CoverageDescription;
    String[] MD_ImageDescription;
    String[] MD_AttributeGroup;
    String[] MD_RangeDimension;
    String[] MD_SampleDimension;
    String[] MD_Band;
    String[] MD_FeatureTypeInfo;
    String[] MD_PortrayalCatalogueReference;
    String[] MD_Distribution;
    String[] MD_DigitalTransferOptions;
    String[] MD_Distributor;
    String[] MD_Format;
    String[] MD_Medium;
    String[] MD_StandardOrderProcess;
    String[] MD_MetadataExtensionInformation;
    String[] MD_ExtendedElementInformation;
    String[] MD_ApplicationSchemaInformation;
    String[] SV_ServiceIdentification;
    String[] SV_OperationMetadata;
    String[] SV_OperationChainMetadata;
    String[] SV_Parameter;
    String[] SV_CoupledResource;
    String[] EX_Extent;
    String[] EX_BoundingPolygon;
    String[] EX_GeographicBoundingBox;
    String[] EX_GeographicDescription;
    String[] EX_TemporalExtent;
    String[] EX_SpatialTemporalExtent;
    String[] EX_VerticalExtent;
    String[] CI_Citation;
    String[] CI_Responsibility;
    String[] CI_Individual;
    String[] CI_Organisation;
    String[] CI_Address;
    String[] CI_Contact;
    String[] CI_Date;
    String[] CI_OnlineResource;
    String[] CI_Series;
    String[] CI_Telephone;
    String[] MD_Scope;
    String[] MD_ScopeDescription;
    String[] MD_Identifier;
    String[] MD_BrowseGraphic;
    String[] PT_FreeText;
    String[] LocalisedCharacterString;
    String[] PT_Locale;
    String[] PT_LocaleContainer;
    String[] DQ_DataQuality;
    String[] DQ_CompletenessCommission;
    String[] DQ_CompletenessOmission;
    String[] DQ_ConceptualConsistency;
    String[] DQ_DomainConsistency;
    String[] DQ_FormatConsistency;
    String[] DQ_TopologicalConsistency;
    String[] DQ_AbsoluteExternalPositionalAccuracy;
    String[] DQ_RelativeInternalPositionalAccuracy;
    String[] DQ_GriddedDataPositionalAccuracy;
    String[] DQ_AccuracyOfATimeMeasure;
    String[] DQ_TemporalConsistency;
    String[] DQ_TemporalValidity;
    String[] DQ_ThematicClassificationCorrectness;
    String[] DQ_NonQuantitativeAttributeCorrectness;
    String[] DQ_QuantitativeAttributeCorrectness;
    String[] DQ_UsabilityElement;
    String[] DQ_Confidence;
    String[] DQ_Representativity;
    String[] DQ_Homogeneity;
    String[] DQ_MeasureReference;
    String[] DQ_EvaluationMethod;
    String[] DQ_FullInspection;
    String[] DQ_IndirectEvaluation;
    String[] DQ_SampleBasedInspection;
    String[] DQ_AggregationDerivation;
    String[] DQ_ConformanceResult;
    String[] DQ_QuantitativeResult;
    String[] DQ_DescriptiveResult;
    String[] DQ_StandaloneQualityReportInformation;

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

    public void setDQ_AccuracyOfATimeMeasure(String[] DQ_AccuracyOfATimeMeasure) {
        this.DQ_AccuracyOfATimeMeasure = DQ_AccuracyOfATimeMeasure;
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

    public void setDQ_QuantitativeAttributeCorrectness(String[] DQ_QuantitativeAttributeCorrectness) {
        this.DQ_QuantitativeAttributeCorrectness = DQ_QuantitativeAttributeCorrectness;
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
}

class Obligation{
    String[] DS_DataSet;
    String[] SV_Service;
    String[] DS_OtherAggregate;
    String[] DS_StereoMate;
    String[] DS_Initiative;
    String[] DS_Series;
    String[] DS_Platform;
    String[] DS_Sensor;
    String[] DS_ProductionSeries;
    String[] MD_Metadata;
    String[] MD_MetadataScope;
    String[] MD_DataIdentification;
    String[] MD_Keywords;
    String[] MD_KeywordClass;
    String[] MD_RepresentativeFraction;
    String[] MD_Resolution;
    String[] MD_Usage;
    String[] MD_AssociatedResource;
    String[] MD_Constraints;
    String[] MD_LegalConstraints;
    String[] MD_SecurityConstraints;
    String[] MD_Releasability;
    String[] LI_Lineage;
    String[] LI_ProcessStep;
    String[] LI_Source;
    String[] MD_MaintenanceInformation;
    String[] MD_GridSpatialRepresentation;
    String[] MD_Georectified;
    String[] MD_Georeferenceable;
    String[] MD_VectorSpatialRepresentation;
    String[] MD_Dimension;
    String[] MD_GeometricObjects;
    String[] MD_ReferenceSystem;
    String[] MD_FeatureCatalogueDescription;
    String[] MD_FeatureCatalogue;
    String[] MD_CoverageDescription;
    String[] MD_ImageDescription;
    String[] MD_AttributeGroup;
    String[] MD_RangeDimension;
    String[] MD_SampleDimension;
    String[] MD_Band;
    String[] MD_FeatureTypeInfo;
    String[] MD_PortrayalCatalogueReference;
    String[] MD_Distribution;
    String[] MD_DigitalTransferOptions;
    String[] MD_Distributor;
    String[] MD_Format;
    String[] MD_Medium;
    String[] MD_StandardOrderProcess;
    String[] MD_MetadataExtensionInformation;
    String[] MD_ExtendedElementInformation;
    String[] MD_ApplicationSchemaInformation;
    String[] SV_ServiceIdentification;
    String[] SV_OperationMetadata;
    String[] SV_OperationChainMetadata;
    String[] SV_Parameter;
    String[] SV_CoupledResource;
    String[] EX_Extent;
    String[] EX_BoundingPolygon;
    String[] EX_GeographicBoundingBox;
    String[] EX_GeographicDescription;
    String[] EX_TemporalExtent;
    String[] EX_SpatialTemporalExtent;
    String[] EX_VerticalExtent;
    String[] CI_Citation;
    String[] CI_Responsibility;
    String[] CI_Individual;
    String[] CI_Organisation;
    String[] CI_Address;
    String[] CI_Contact;
    String[] CI_Date;
    String[] CI_OnlineResource;
    String[] CI_Series;
    String[] CI_Telephone;
    String[] MD_Scope;
    String[] MD_ScopeDescription;
    String[] MD_Identifier;
    String[] MD_BrowseGraphic;
    String[] PT_FreeText;
    String[] LocalisedCharacterString;
    String[] PT_Locale;
    String[] PT_LocaleContainer;
    String[] DQ_DataQuality;
    String[] DQ_CompletenessCommission;
    String[] DQ_CompletenessOmission;
    String[] DQ_ConceptualConsistency;
    String[] DQ_DomainConsistency;
    String[] DQ_FormatConsistency;
    String[] DQ_TopologicalConsistency;
    String[] DQ_AbsoluteExternalPositionalAccuracy;
    String[] DQ_RelativeInternalPositionalAccuracy;
    String[] DQ_GriddedDataPositionalAccuracy;
    String[] DQ_AccuracyOfATimeMeasure;
    String[] DQ_TemporalConsistency;
    String[] DQ_TemporalValidity;
    String[] DQ_ThematicClassificationCorrectness;
    String[] DQ_NonQuantitativeAttributeCorrectness;
    String[] DQ_QuantitativeAttributeCorrectness;
    String[] DQ_UsabilityElement;
    String[] DQ_Confidence;
    String[] DQ_Representativity;
    String[] DQ_Homogeneity;
    String[] DQ_MeasureReference;
    String[] DQ_EvaluationMethod;
    String[] DQ_FullInspection;
    String[] DQ_IndirectEvaluation;
    String[] DQ_SampleBasedInspection;
    String[] DQ_AggregationDerivation;
    String[] DQ_ConformanceResult;
    String[] DQ_QuantitativeResult;
    String[] DQ_DescriptiveResult;
    String[] DQ_StandaloneQualityReportInformation;

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

    public void setDQ_AccuracyOfATimeMeasure(String[] DQ_AccuracyOfATimeMeasure) {
        this.DQ_AccuracyOfATimeMeasure = DQ_AccuracyOfATimeMeasure;
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

    public void setDQ_QuantitativeAttributeCorrectness(String[] DQ_QuantitativeAttributeCorrectness) {
        this.DQ_QuantitativeAttributeCorrectness = DQ_QuantitativeAttributeCorrectness;
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
}