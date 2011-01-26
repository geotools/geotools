/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.gml3.v3_2.gmd;

import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.gml3.v3_2.gco.GCO;
import org.geotools.gml3.v3_2.gsr.GSR;
import org.geotools.gml3.v3_2.gss.GSS;
import org.geotools.gml3.v3_2.gts.GTS;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.isotc211.org/2005/gmd schema.
 *
 * @generated
 *
 * @source $URL$
 */
public final class GMD extends XSD {

    /** singleton instance */
    private static final GMD instance = new GMD();
    
    /**
     * Returns the singleton instance.
     */
    public static final GMD getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private GMD() {
    }
    
    protected void addDependencies(Set dependencies) {
       dependencies.add( GSS.getInstance() );
       dependencies.add( GTS.getInstance() );
       dependencies.add( GCO.getInstance() );
       dependencies.add( GSR.getInstance() );
    }
    
    /**
     * Returns 'http://www.isotc211.org/2005/gmd'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'gmd.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("gmd.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.isotc211.org/2005/gmd";
    
    /* Type Definitions */
    /** @generated */
    public static final QName AbstractDQ_Completeness_Type = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractDQ_Completeness_Type");
    /** @generated */
    public static final QName AbstractDQ_Element_Type = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractDQ_Element_Type");
    /** @generated */
    public static final QName AbstractDQ_LogicalConsistency_Type = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractDQ_LogicalConsistency_Type");
    /** @generated */
    public static final QName AbstractDQ_PositionalAccuracy_Type = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractDQ_PositionalAccuracy_Type");
    /** @generated */
    public static final QName AbstractDQ_Result_Type = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractDQ_Result_Type");
    /** @generated */
    public static final QName AbstractDQ_TemporalAccuracy_Type = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractDQ_TemporalAccuracy_Type");
    /** @generated */
    public static final QName AbstractDQ_ThematicAccuracy_Type = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractDQ_ThematicAccuracy_Type");
    /** @generated */
    public static final QName AbstractDS_Aggregate_Type = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractDS_Aggregate_Type");
    /** @generated */
    public static final QName AbstractEX_GeographicExtent_Type = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractEX_GeographicExtent_Type");
    /** @generated */
    public static final QName AbstractMD_ContentInformation_Type = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractMD_ContentInformation_Type");
    /** @generated */
    public static final QName AbstractMD_Identification_Type = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractMD_Identification_Type");
    /** @generated */
    public static final QName AbstractMD_SpatialRepresentation_Type = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractMD_SpatialRepresentation_Type");
    /** @generated */
    public static final QName AbstractRS_ReferenceSystem_Type = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractRS_ReferenceSystem_Type");
    /** @generated */
    public static final QName CI_Address_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Address_PropertyType");
    /** @generated */
    public static final QName CI_Address_Type = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Address_Type");
    /** @generated */
    public static final QName CI_Citation_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Citation_PropertyType");
    /** @generated */
    public static final QName CI_Citation_Type = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Citation_Type");
    /** @generated */
    public static final QName CI_Contact_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Contact_PropertyType");
    /** @generated */
    public static final QName CI_Contact_Type = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Contact_Type");
    /** @generated */
    public static final QName CI_Date_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Date_PropertyType");
    /** @generated */
    public static final QName CI_Date_Type = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Date_Type");
    /** @generated */
    public static final QName CI_DateTypeCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","CI_DateTypeCode_PropertyType");
    /** @generated */
    public static final QName CI_OnLineFunctionCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","CI_OnLineFunctionCode_PropertyType");
    /** @generated */
    public static final QName CI_OnlineResource_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","CI_OnlineResource_PropertyType");
    /** @generated */
    public static final QName CI_OnlineResource_Type = 
        new QName("http://www.isotc211.org/2005/gmd","CI_OnlineResource_Type");
    /** @generated */
    public static final QName CI_PresentationFormCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","CI_PresentationFormCode_PropertyType");
    /** @generated */
    public static final QName CI_ResponsibleParty_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","CI_ResponsibleParty_PropertyType");
    /** @generated */
    public static final QName CI_ResponsibleParty_Type = 
        new QName("http://www.isotc211.org/2005/gmd","CI_ResponsibleParty_Type");
    /** @generated */
    public static final QName CI_RoleCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","CI_RoleCode_PropertyType");
    /** @generated */
    public static final QName CI_Series_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Series_PropertyType");
    /** @generated */
    public static final QName CI_Series_Type = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Series_Type");
    /** @generated */
    public static final QName CI_Telephone_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Telephone_PropertyType");
    /** @generated */
    public static final QName CI_Telephone_Type = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Telephone_Type");
    /** @generated */
    public static final QName Country_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","Country_PropertyType");
    /** @generated */
    public static final QName DQ_AbsoluteExternalPositionalAccuracy_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_AbsoluteExternalPositionalAccuracy_PropertyType");
    /** @generated */
    public static final QName DQ_AbsoluteExternalPositionalAccuracy_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_AbsoluteExternalPositionalAccuracy_Type");
    /** @generated */
    public static final QName DQ_AccuracyOfATimeMeasurement_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_AccuracyOfATimeMeasurement_PropertyType");
    /** @generated */
    public static final QName DQ_AccuracyOfATimeMeasurement_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_AccuracyOfATimeMeasurement_Type");
    /** @generated */
    public static final QName DQ_Completeness_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_Completeness_PropertyType");
    /** @generated */
    public static final QName DQ_CompletenessCommission_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_CompletenessCommission_PropertyType");
    /** @generated */
    public static final QName DQ_CompletenessCommission_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_CompletenessCommission_Type");
    /** @generated */
    public static final QName DQ_CompletenessOmission_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_CompletenessOmission_PropertyType");
    /** @generated */
    public static final QName DQ_CompletenessOmission_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_CompletenessOmission_Type");
    /** @generated */
    public static final QName DQ_ConceptualConsistency_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_ConceptualConsistency_PropertyType");
    /** @generated */
    public static final QName DQ_ConceptualConsistency_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_ConceptualConsistency_Type");
    /** @generated */
    public static final QName DQ_ConformanceResult_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_ConformanceResult_PropertyType");
    /** @generated */
    public static final QName DQ_ConformanceResult_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_ConformanceResult_Type");
    /** @generated */
    public static final QName DQ_DataQuality_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_DataQuality_PropertyType");
    /** @generated */
    public static final QName DQ_DataQuality_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_DataQuality_Type");
    /** @generated */
    public static final QName DQ_DomainConsistency_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_DomainConsistency_PropertyType");
    /** @generated */
    public static final QName DQ_DomainConsistency_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_DomainConsistency_Type");
    /** @generated */
    public static final QName DQ_Element_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_Element_PropertyType");
    /** @generated */
    public static final QName DQ_EvaluationMethodTypeCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_EvaluationMethodTypeCode_PropertyType");
    /** @generated */
    public static final QName DQ_FormatConsistency_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_FormatConsistency_PropertyType");
    /** @generated */
    public static final QName DQ_FormatConsistency_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_FormatConsistency_Type");
    /** @generated */
    public static final QName DQ_GriddedDataPositionalAccuracy_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_GriddedDataPositionalAccuracy_PropertyType");
    /** @generated */
    public static final QName DQ_GriddedDataPositionalAccuracy_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_GriddedDataPositionalAccuracy_Type");
    /** @generated */
    public static final QName DQ_LogicalConsistency_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_LogicalConsistency_PropertyType");
    /** @generated */
    public static final QName DQ_NonQuantitativeAttributeAccuracy_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_NonQuantitativeAttributeAccuracy_PropertyType");
    /** @generated */
    public static final QName DQ_NonQuantitativeAttributeAccuracy_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_NonQuantitativeAttributeAccuracy_Type");
    /** @generated */
    public static final QName DQ_PositionalAccuracy_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_PositionalAccuracy_PropertyType");
    /** @generated */
    public static final QName DQ_QuantitativeAttributeAccuracy_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_QuantitativeAttributeAccuracy_PropertyType");
    /** @generated */
    public static final QName DQ_QuantitativeAttributeAccuracy_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_QuantitativeAttributeAccuracy_Type");
    /** @generated */
    public static final QName DQ_QuantitativeResult_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_QuantitativeResult_PropertyType");
    /** @generated */
    public static final QName DQ_QuantitativeResult_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_QuantitativeResult_Type");
    /** @generated */
    public static final QName DQ_RelativeInternalPositionalAccuracy_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_RelativeInternalPositionalAccuracy_PropertyType");
    /** @generated */
    public static final QName DQ_RelativeInternalPositionalAccuracy_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_RelativeInternalPositionalAccuracy_Type");
    /** @generated */
    public static final QName DQ_Result_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_Result_PropertyType");
    /** @generated */
    public static final QName DQ_Scope_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_Scope_PropertyType");
    /** @generated */
    public static final QName DQ_Scope_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_Scope_Type");
    /** @generated */
    public static final QName DQ_TemporalAccuracy_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_TemporalAccuracy_PropertyType");
    /** @generated */
    public static final QName DQ_TemporalConsistency_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_TemporalConsistency_PropertyType");
    /** @generated */
    public static final QName DQ_TemporalConsistency_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_TemporalConsistency_Type");
    /** @generated */
    public static final QName DQ_TemporalValidity_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_TemporalValidity_PropertyType");
    /** @generated */
    public static final QName DQ_TemporalValidity_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_TemporalValidity_Type");
    /** @generated */
    public static final QName DQ_ThematicAccuracy_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_ThematicAccuracy_PropertyType");
    /** @generated */
    public static final QName DQ_ThematicClassificationCorrectness_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_ThematicClassificationCorrectness_PropertyType");
    /** @generated */
    public static final QName DQ_ThematicClassificationCorrectness_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_ThematicClassificationCorrectness_Type");
    /** @generated */
    public static final QName DQ_TopologicalConsistency_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_TopologicalConsistency_PropertyType");
    /** @generated */
    public static final QName DQ_TopologicalConsistency_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_TopologicalConsistency_Type");
    /** @generated */
    public static final QName DS_Aggregate_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DS_Aggregate_PropertyType");
    /** @generated */
    public static final QName DS_Association_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DS_Association_PropertyType");
    /** @generated */
    public static final QName DS_Association_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DS_Association_Type");
    /** @generated */
    public static final QName DS_AssociationTypeCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DS_AssociationTypeCode_PropertyType");
    /** @generated */
    public static final QName DS_DataSet_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DS_DataSet_PropertyType");
    /** @generated */
    public static final QName DS_DataSet_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DS_DataSet_Type");
    /** @generated */
    public static final QName DS_Initiative_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DS_Initiative_PropertyType");
    /** @generated */
    public static final QName DS_Initiative_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DS_Initiative_Type");
    /** @generated */
    public static final QName DS_InitiativeTypeCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DS_InitiativeTypeCode_PropertyType");
    /** @generated */
    public static final QName DS_OtherAggregate_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DS_OtherAggregate_PropertyType");
    /** @generated */
    public static final QName DS_OtherAggregate_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DS_OtherAggregate_Type");
    /** @generated */
    public static final QName DS_Platform_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DS_Platform_PropertyType");
    /** @generated */
    public static final QName DS_Platform_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DS_Platform_Type");
    /** @generated */
    public static final QName DS_ProductionSeries_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DS_ProductionSeries_PropertyType");
    /** @generated */
    public static final QName DS_ProductionSeries_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DS_ProductionSeries_Type");
    /** @generated */
    public static final QName DS_Sensor_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DS_Sensor_PropertyType");
    /** @generated */
    public static final QName DS_Sensor_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DS_Sensor_Type");
    /** @generated */
    public static final QName DS_Series_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DS_Series_PropertyType");
    /** @generated */
    public static final QName DS_Series_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DS_Series_Type");
    /** @generated */
    public static final QName DS_StereoMate_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","DS_StereoMate_PropertyType");
    /** @generated */
    public static final QName DS_StereoMate_Type = 
        new QName("http://www.isotc211.org/2005/gmd","DS_StereoMate_Type");
    /** @generated */
    public static final QName EX_BoundingPolygon_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","EX_BoundingPolygon_PropertyType");
    /** @generated */
    public static final QName EX_BoundingPolygon_Type = 
        new QName("http://www.isotc211.org/2005/gmd","EX_BoundingPolygon_Type");
    /** @generated */
    public static final QName EX_Extent_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","EX_Extent_PropertyType");
    /** @generated */
    public static final QName EX_Extent_Type = 
        new QName("http://www.isotc211.org/2005/gmd","EX_Extent_Type");
    /** @generated */
    public static final QName EX_GeographicBoundingBox_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","EX_GeographicBoundingBox_PropertyType");
    /** @generated */
    public static final QName EX_GeographicBoundingBox_Type = 
        new QName("http://www.isotc211.org/2005/gmd","EX_GeographicBoundingBox_Type");
    /** @generated */
    public static final QName EX_GeographicDescription_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","EX_GeographicDescription_PropertyType");
    /** @generated */
    public static final QName EX_GeographicDescription_Type = 
        new QName("http://www.isotc211.org/2005/gmd","EX_GeographicDescription_Type");
    /** @generated */
    public static final QName EX_GeographicExtent_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","EX_GeographicExtent_PropertyType");
    /** @generated */
    public static final QName EX_SpatialTemporalExtent_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","EX_SpatialTemporalExtent_PropertyType");
    /** @generated */
    public static final QName EX_SpatialTemporalExtent_Type = 
        new QName("http://www.isotc211.org/2005/gmd","EX_SpatialTemporalExtent_Type");
    /** @generated */
    public static final QName EX_TemporalExtent_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","EX_TemporalExtent_PropertyType");
    /** @generated */
    public static final QName EX_TemporalExtent_Type = 
        new QName("http://www.isotc211.org/2005/gmd","EX_TemporalExtent_Type");
    /** @generated */
    public static final QName EX_VerticalExtent_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","EX_VerticalExtent_PropertyType");
    /** @generated */
    public static final QName EX_VerticalExtent_Type = 
        new QName("http://www.isotc211.org/2005/gmd","EX_VerticalExtent_Type");
    /** @generated */
    public static final QName LanguageCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","LanguageCode_PropertyType");
    /** @generated */
    public static final QName LI_Lineage_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","LI_Lineage_PropertyType");
    /** @generated */
    public static final QName LI_Lineage_Type = 
        new QName("http://www.isotc211.org/2005/gmd","LI_Lineage_Type");
    /** @generated */
    public static final QName LI_ProcessStep_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","LI_ProcessStep_PropertyType");
    /** @generated */
    public static final QName LI_ProcessStep_Type = 
        new QName("http://www.isotc211.org/2005/gmd","LI_ProcessStep_Type");
    /** @generated */
    public static final QName LI_Source_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","LI_Source_PropertyType");
    /** @generated */
    public static final QName LI_Source_Type = 
        new QName("http://www.isotc211.org/2005/gmd","LI_Source_Type");
    /** @generated */
    public static final QName LocalisedCharacterString_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","LocalisedCharacterString_PropertyType");
    /** @generated */
    public static final QName LocalisedCharacterString_Type = 
        new QName("http://www.isotc211.org/2005/gmd","LocalisedCharacterString_Type");
    /** @generated */
    public static final QName MD_AggregateInformation_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_AggregateInformation_PropertyType");
    /** @generated */
    public static final QName MD_AggregateInformation_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_AggregateInformation_Type");
    /** @generated */
    public static final QName MD_ApplicationSchemaInformation_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ApplicationSchemaInformation_PropertyType");
    /** @generated */
    public static final QName MD_ApplicationSchemaInformation_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ApplicationSchemaInformation_Type");
    /** @generated */
    public static final QName MD_Band_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Band_PropertyType");
    /** @generated */
    public static final QName MD_Band_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Band_Type");
    /** @generated */
    public static final QName MD_BrowseGraphic_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_BrowseGraphic_PropertyType");
    /** @generated */
    public static final QName MD_BrowseGraphic_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_BrowseGraphic_Type");
    /** @generated */
    public static final QName MD_CellGeometryCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_CellGeometryCode_PropertyType");
    /** @generated */
    public static final QName MD_CharacterSetCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_CharacterSetCode_PropertyType");
    /** @generated */
    public static final QName MD_ClassificationCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ClassificationCode_PropertyType");
    /** @generated */
    public static final QName MD_Constraints_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Constraints_PropertyType");
    /** @generated */
    public static final QName MD_Constraints_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Constraints_Type");
    /** @generated */
    public static final QName MD_ContentInformation_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ContentInformation_PropertyType");
    /** @generated */
    public static final QName MD_CoverageContentTypeCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_CoverageContentTypeCode_PropertyType");
    /** @generated */
    public static final QName MD_CoverageDescription_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_CoverageDescription_PropertyType");
    /** @generated */
    public static final QName MD_CoverageDescription_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_CoverageDescription_Type");
    /** @generated */
    public static final QName MD_DataIdentification_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_DataIdentification_PropertyType");
    /** @generated */
    public static final QName MD_DataIdentification_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_DataIdentification_Type");
    /** @generated */
    public static final QName MD_DatatypeCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_DatatypeCode_PropertyType");
    /** @generated */
    public static final QName MD_DigitalTransferOptions_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_DigitalTransferOptions_PropertyType");
    /** @generated */
    public static final QName MD_DigitalTransferOptions_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_DigitalTransferOptions_Type");
    /** @generated */
    public static final QName MD_Dimension_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Dimension_PropertyType");
    /** @generated */
    public static final QName MD_Dimension_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Dimension_Type");
    /** @generated */
    public static final QName MD_DimensionNameTypeCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_DimensionNameTypeCode_PropertyType");
    /** @generated */
    public static final QName MD_Distribution_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Distribution_PropertyType");
    /** @generated */
    public static final QName MD_Distribution_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Distribution_Type");
    /** @generated */
    public static final QName MD_DistributionUnits_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_DistributionUnits_PropertyType");
    /** @generated */
    public static final QName MD_Distributor_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Distributor_PropertyType");
    /** @generated */
    public static final QName MD_Distributor_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Distributor_Type");
    /** @generated */
    public static final QName MD_ExtendedElementInformation_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ExtendedElementInformation_PropertyType");
    /** @generated */
    public static final QName MD_ExtendedElementInformation_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ExtendedElementInformation_Type");
    /** @generated */
    public static final QName MD_FeatureCatalogueDescription_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_FeatureCatalogueDescription_PropertyType");
    /** @generated */
    public static final QName MD_FeatureCatalogueDescription_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_FeatureCatalogueDescription_Type");
    /** @generated */
    public static final QName MD_Format_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Format_PropertyType");
    /** @generated */
    public static final QName MD_Format_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Format_Type");
    /** @generated */
    public static final QName MD_GeometricObjects_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_GeometricObjects_PropertyType");
    /** @generated */
    public static final QName MD_GeometricObjects_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_GeometricObjects_Type");
    /** @generated */
    public static final QName MD_GeometricObjectTypeCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_GeometricObjectTypeCode_PropertyType");
    /** @generated */
    public static final QName MD_Georectified_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Georectified_PropertyType");
    /** @generated */
    public static final QName MD_Georectified_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Georectified_Type");
    /** @generated */
    public static final QName MD_Georeferenceable_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Georeferenceable_PropertyType");
    /** @generated */
    public static final QName MD_Georeferenceable_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Georeferenceable_Type");
    /** @generated */
    public static final QName MD_GridSpatialRepresentation_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_GridSpatialRepresentation_PropertyType");
    /** @generated */
    public static final QName MD_GridSpatialRepresentation_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_GridSpatialRepresentation_Type");
    /** @generated */
    public static final QName MD_Identification_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Identification_PropertyType");
    /** @generated */
    public static final QName MD_Identifier_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Identifier_PropertyType");
    /** @generated */
    public static final QName MD_Identifier_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Identifier_Type");
    /** @generated */
    public static final QName MD_ImageDescription_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ImageDescription_PropertyType");
    /** @generated */
    public static final QName MD_ImageDescription_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ImageDescription_Type");
    /** @generated */
    public static final QName MD_ImagingConditionCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ImagingConditionCode_PropertyType");
    /** @generated */
    public static final QName MD_Keywords_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Keywords_PropertyType");
    /** @generated */
    public static final QName MD_Keywords_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Keywords_Type");
    /** @generated */
    public static final QName MD_KeywordTypeCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_KeywordTypeCode_PropertyType");
    /** @generated */
    public static final QName MD_LegalConstraints_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_LegalConstraints_PropertyType");
    /** @generated */
    public static final QName MD_LegalConstraints_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_LegalConstraints_Type");
    /** @generated */
    public static final QName MD_MaintenanceFrequencyCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_MaintenanceFrequencyCode_PropertyType");
    /** @generated */
    public static final QName MD_MaintenanceInformation_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_MaintenanceInformation_PropertyType");
    /** @generated */
    public static final QName MD_MaintenanceInformation_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_MaintenanceInformation_Type");
    /** @generated */
    public static final QName MD_Medium_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Medium_PropertyType");
    /** @generated */
    public static final QName MD_Medium_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Medium_Type");
    /** @generated */
    public static final QName MD_MediumFormatCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_MediumFormatCode_PropertyType");
    /** @generated */
    public static final QName MD_MediumNameCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_MediumNameCode_PropertyType");
    /** @generated */
    public static final QName MD_Metadata_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Metadata_PropertyType");
    /** @generated */
    public static final QName MD_Metadata_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Metadata_Type");
    /** @generated */
    public static final QName MD_MetadataExtensionInformation_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_MetadataExtensionInformation_PropertyType");
    /** @generated */
    public static final QName MD_MetadataExtensionInformation_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_MetadataExtensionInformation_Type");
    /** @generated */
    public static final QName MD_ObligationCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ObligationCode_PropertyType");
    /** @generated */
    public static final QName MD_ObligationCode_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ObligationCode_Type");
    /** @generated */
    public static final QName MD_PixelOrientationCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_PixelOrientationCode_PropertyType");
    /** @generated */
    public static final QName MD_PixelOrientationCode_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_PixelOrientationCode_Type");
    /** @generated */
    public static final QName MD_PortrayalCatalogueReference_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_PortrayalCatalogueReference_PropertyType");
    /** @generated */
    public static final QName MD_PortrayalCatalogueReference_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_PortrayalCatalogueReference_Type");
    /** @generated */
    public static final QName MD_ProgressCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ProgressCode_PropertyType");
    /** @generated */
    public static final QName MD_RangeDimension_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_RangeDimension_PropertyType");
    /** @generated */
    public static final QName MD_RangeDimension_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_RangeDimension_Type");
    /** @generated */
    public static final QName MD_ReferenceSystem_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ReferenceSystem_PropertyType");
    /** @generated */
    public static final QName MD_ReferenceSystem_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ReferenceSystem_Type");
    /** @generated */
    public static final QName MD_RepresentativeFraction_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_RepresentativeFraction_PropertyType");
    /** @generated */
    public static final QName MD_RepresentativeFraction_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_RepresentativeFraction_Type");
    /** @generated */
    public static final QName MD_Resolution_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Resolution_PropertyType");
    /** @generated */
    public static final QName MD_Resolution_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Resolution_Type");
    /** @generated */
    public static final QName MD_RestrictionCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_RestrictionCode_PropertyType");
    /** @generated */
    public static final QName MD_ScopeCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ScopeCode_PropertyType");
    /** @generated */
    public static final QName MD_ScopeDescription_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ScopeDescription_PropertyType");
    /** @generated */
    public static final QName MD_ScopeDescription_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ScopeDescription_Type");
    /** @generated */
    public static final QName MD_SecurityConstraints_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_SecurityConstraints_PropertyType");
    /** @generated */
    public static final QName MD_SecurityConstraints_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_SecurityConstraints_Type");
    /** @generated */
    public static final QName MD_ServiceIdentification_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ServiceIdentification_PropertyType");
    /** @generated */
    public static final QName MD_ServiceIdentification_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ServiceIdentification_Type");
    /** @generated */
    public static final QName MD_SpatialRepresentation_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_SpatialRepresentation_PropertyType");
    /** @generated */
    public static final QName MD_SpatialRepresentationTypeCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_SpatialRepresentationTypeCode_PropertyType");
    /** @generated */
    public static final QName MD_StandardOrderProcess_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_StandardOrderProcess_PropertyType");
    /** @generated */
    public static final QName MD_StandardOrderProcess_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_StandardOrderProcess_Type");
    /** @generated */
    public static final QName MD_TopicCategoryCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_TopicCategoryCode_PropertyType");
    /** @generated */
    public static final QName MD_TopicCategoryCode_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_TopicCategoryCode_Type");
    /** @generated */
    public static final QName MD_TopologyLevelCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_TopologyLevelCode_PropertyType");
    /** @generated */
    public static final QName MD_Usage_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Usage_PropertyType");
    /** @generated */
    public static final QName MD_Usage_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Usage_Type");
    /** @generated */
    public static final QName MD_VectorSpatialRepresentation_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","MD_VectorSpatialRepresentation_PropertyType");
    /** @generated */
    public static final QName MD_VectorSpatialRepresentation_Type = 
        new QName("http://www.isotc211.org/2005/gmd","MD_VectorSpatialRepresentation_Type");
    /** @generated */
    public static final QName PT_FreeText_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","PT_FreeText_PropertyType");
    /** @generated */
    public static final QName PT_FreeText_Type = 
        new QName("http://www.isotc211.org/2005/gmd","PT_FreeText_Type");
    /** @generated */
    public static final QName PT_Locale_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","PT_Locale_PropertyType");
    /** @generated */
    public static final QName PT_Locale_Type = 
        new QName("http://www.isotc211.org/2005/gmd","PT_Locale_Type");
    /** @generated */
    public static final QName PT_LocaleContainer_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","PT_LocaleContainer_PropertyType");
    /** @generated */
    public static final QName PT_LocaleContainer_Type = 
        new QName("http://www.isotc211.org/2005/gmd","PT_LocaleContainer_Type");
    /** @generated */
    public static final QName RS_Identifier_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","RS_Identifier_PropertyType");
    /** @generated */
    public static final QName RS_Identifier_Type = 
        new QName("http://www.isotc211.org/2005/gmd","RS_Identifier_Type");
    /** @generated */
    public static final QName RS_ReferenceSystem_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","RS_ReferenceSystem_PropertyType");
    /** @generated */
    public static final QName URL_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmd","URL_PropertyType");

    /* Elements */
    /** @generated */
    public static final QName AbstractDQ_Completeness = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractDQ_Completeness");
    /** @generated */
    public static final QName AbstractDQ_Element = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractDQ_Element");
    /** @generated */
    public static final QName AbstractDQ_LogicalConsistency = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractDQ_LogicalConsistency");
    /** @generated */
    public static final QName AbstractDQ_PositionalAccuracy = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractDQ_PositionalAccuracy");
    /** @generated */
    public static final QName AbstractDQ_Result = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractDQ_Result");
    /** @generated */
    public static final QName AbstractDQ_TemporalAccuracy = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractDQ_TemporalAccuracy");
    /** @generated */
    public static final QName AbstractDQ_ThematicAccuracy = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractDQ_ThematicAccuracy");
    /** @generated */
    public static final QName AbstractDS_Aggregate = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractDS_Aggregate");
    /** @generated */
    public static final QName AbstractEX_GeographicExtent = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractEX_GeographicExtent");
    /** @generated */
    public static final QName AbstractMD_ContentInformation = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractMD_ContentInformation");
    /** @generated */
    public static final QName AbstractMD_Identification = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractMD_Identification");
    /** @generated */
    public static final QName AbstractMD_SpatialRepresentation = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractMD_SpatialRepresentation");
    /** @generated */
    public static final QName AbstractRS_ReferenceSystem = 
        new QName("http://www.isotc211.org/2005/gmd","AbstractRS_ReferenceSystem");
    /** @generated */
    public static final QName CI_Address = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Address");
    /** @generated */
    public static final QName CI_Citation = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Citation");
    /** @generated */
    public static final QName CI_Contact = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Contact");
    /** @generated */
    public static final QName CI_Date = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Date");
    /** @generated */
    public static final QName CI_DateTypeCode = 
        new QName("http://www.isotc211.org/2005/gmd","CI_DateTypeCode");
    /** @generated */
    public static final QName CI_OnLineFunctionCode = 
        new QName("http://www.isotc211.org/2005/gmd","CI_OnLineFunctionCode");
    /** @generated */
    public static final QName CI_OnlineResource = 
        new QName("http://www.isotc211.org/2005/gmd","CI_OnlineResource");
    /** @generated */
    public static final QName CI_PresentationFormCode = 
        new QName("http://www.isotc211.org/2005/gmd","CI_PresentationFormCode");
    /** @generated */
    public static final QName CI_ResponsibleParty = 
        new QName("http://www.isotc211.org/2005/gmd","CI_ResponsibleParty");
    /** @generated */
    public static final QName CI_RoleCode = 
        new QName("http://www.isotc211.org/2005/gmd","CI_RoleCode");
    /** @generated */
    public static final QName CI_Series = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Series");
    /** @generated */
    public static final QName CI_Telephone = 
        new QName("http://www.isotc211.org/2005/gmd","CI_Telephone");
    /** @generated */
    public static final QName Country = 
        new QName("http://www.isotc211.org/2005/gmd","Country");
    /** @generated */
    public static final QName DQ_AbsoluteExternalPositionalAccuracy = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_AbsoluteExternalPositionalAccuracy");
    /** @generated */
    public static final QName DQ_AccuracyOfATimeMeasurement = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_AccuracyOfATimeMeasurement");
    /** @generated */
    public static final QName DQ_CompletenessCommission = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_CompletenessCommission");
    /** @generated */
    public static final QName DQ_CompletenessOmission = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_CompletenessOmission");
    /** @generated */
    public static final QName DQ_ConceptualConsistency = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_ConceptualConsistency");
    /** @generated */
    public static final QName DQ_ConformanceResult = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_ConformanceResult");
    /** @generated */
    public static final QName DQ_DataQuality = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_DataQuality");
    /** @generated */
    public static final QName DQ_DomainConsistency = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_DomainConsistency");
    /** @generated */
    public static final QName DQ_EvaluationMethodTypeCode = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_EvaluationMethodTypeCode");
    /** @generated */
    public static final QName DQ_FormatConsistency = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_FormatConsistency");
    /** @generated */
    public static final QName DQ_GriddedDataPositionalAccuracy = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_GriddedDataPositionalAccuracy");
    /** @generated */
    public static final QName DQ_NonQuantitativeAttributeAccuracy = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_NonQuantitativeAttributeAccuracy");
    /** @generated */
    public static final QName DQ_QuantitativeAttributeAccuracy = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_QuantitativeAttributeAccuracy");
    /** @generated */
    public static final QName DQ_QuantitativeResult = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_QuantitativeResult");
    /** @generated */
    public static final QName DQ_RelativeInternalPositionalAccuracy = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_RelativeInternalPositionalAccuracy");
    /** @generated */
    public static final QName DQ_Scope = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_Scope");
    /** @generated */
    public static final QName DQ_TemporalConsistency = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_TemporalConsistency");
    /** @generated */
    public static final QName DQ_TemporalValidity = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_TemporalValidity");
    /** @generated */
    public static final QName DQ_ThematicClassificationCorrectness = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_ThematicClassificationCorrectness");
    /** @generated */
    public static final QName DQ_TopologicalConsistency = 
        new QName("http://www.isotc211.org/2005/gmd","DQ_TopologicalConsistency");
    /** @generated */
    public static final QName DS_Association = 
        new QName("http://www.isotc211.org/2005/gmd","DS_Association");
    /** @generated */
    public static final QName DS_AssociationTypeCode = 
        new QName("http://www.isotc211.org/2005/gmd","DS_AssociationTypeCode");
    /** @generated */
    public static final QName DS_DataSet = 
        new QName("http://www.isotc211.org/2005/gmd","DS_DataSet");
    /** @generated */
    public static final QName DS_Initiative = 
        new QName("http://www.isotc211.org/2005/gmd","DS_Initiative");
    /** @generated */
    public static final QName DS_InitiativeTypeCode = 
        new QName("http://www.isotc211.org/2005/gmd","DS_InitiativeTypeCode");
    /** @generated */
    public static final QName DS_OtherAggregate = 
        new QName("http://www.isotc211.org/2005/gmd","DS_OtherAggregate");
    /** @generated */
    public static final QName DS_Platform = 
        new QName("http://www.isotc211.org/2005/gmd","DS_Platform");
    /** @generated */
    public static final QName DS_ProductionSeries = 
        new QName("http://www.isotc211.org/2005/gmd","DS_ProductionSeries");
    /** @generated */
    public static final QName DS_Sensor = 
        new QName("http://www.isotc211.org/2005/gmd","DS_Sensor");
    /** @generated */
    public static final QName DS_Series = 
        new QName("http://www.isotc211.org/2005/gmd","DS_Series");
    /** @generated */
    public static final QName DS_StereoMate = 
        new QName("http://www.isotc211.org/2005/gmd","DS_StereoMate");
    /** @generated */
    public static final QName EX_BoundingPolygon = 
        new QName("http://www.isotc211.org/2005/gmd","EX_BoundingPolygon");
    /** @generated */
    public static final QName EX_Extent = 
        new QName("http://www.isotc211.org/2005/gmd","EX_Extent");
    /** @generated */
    public static final QName EX_GeographicBoundingBox = 
        new QName("http://www.isotc211.org/2005/gmd","EX_GeographicBoundingBox");
    /** @generated */
    public static final QName EX_GeographicDescription = 
        new QName("http://www.isotc211.org/2005/gmd","EX_GeographicDescription");
    /** @generated */
    public static final QName EX_SpatialTemporalExtent = 
        new QName("http://www.isotc211.org/2005/gmd","EX_SpatialTemporalExtent");
    /** @generated */
    public static final QName EX_TemporalExtent = 
        new QName("http://www.isotc211.org/2005/gmd","EX_TemporalExtent");
    /** @generated */
    public static final QName EX_VerticalExtent = 
        new QName("http://www.isotc211.org/2005/gmd","EX_VerticalExtent");
    /** @generated */
    public static final QName LanguageCode = 
        new QName("http://www.isotc211.org/2005/gmd","LanguageCode");
    /** @generated */
    public static final QName LI_Lineage = 
        new QName("http://www.isotc211.org/2005/gmd","LI_Lineage");
    /** @generated */
    public static final QName LI_ProcessStep = 
        new QName("http://www.isotc211.org/2005/gmd","LI_ProcessStep");
    /** @generated */
    public static final QName LI_Source = 
        new QName("http://www.isotc211.org/2005/gmd","LI_Source");
    /** @generated */
    public static final QName LocalisedCharacterString = 
        new QName("http://www.isotc211.org/2005/gmd","LocalisedCharacterString");
    /** @generated */
    public static final QName MD_AggregateInformation = 
        new QName("http://www.isotc211.org/2005/gmd","MD_AggregateInformation");
    /** @generated */
    public static final QName MD_ApplicationSchemaInformation = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ApplicationSchemaInformation");
    /** @generated */
    public static final QName MD_Band = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Band");
    /** @generated */
    public static final QName MD_BrowseGraphic = 
        new QName("http://www.isotc211.org/2005/gmd","MD_BrowseGraphic");
    /** @generated */
    public static final QName MD_CellGeometryCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_CellGeometryCode");
    /** @generated */
    public static final QName MD_CharacterSetCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_CharacterSetCode");
    /** @generated */
    public static final QName MD_ClassificationCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ClassificationCode");
    /** @generated */
    public static final QName MD_Constraints = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Constraints");
    /** @generated */
    public static final QName MD_CoverageContentTypeCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_CoverageContentTypeCode");
    /** @generated */
    public static final QName MD_CoverageDescription = 
        new QName("http://www.isotc211.org/2005/gmd","MD_CoverageDescription");
    /** @generated */
    public static final QName MD_DataIdentification = 
        new QName("http://www.isotc211.org/2005/gmd","MD_DataIdentification");
    /** @generated */
    public static final QName MD_DatatypeCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_DatatypeCode");
    /** @generated */
    public static final QName MD_DigitalTransferOptions = 
        new QName("http://www.isotc211.org/2005/gmd","MD_DigitalTransferOptions");
    /** @generated */
    public static final QName MD_Dimension = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Dimension");
    /** @generated */
    public static final QName MD_DimensionNameTypeCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_DimensionNameTypeCode");
    /** @generated */
    public static final QName MD_Distribution = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Distribution");
    /** @generated */
    public static final QName MD_DistributionUnits = 
        new QName("http://www.isotc211.org/2005/gmd","MD_DistributionUnits");
    /** @generated */
    public static final QName MD_Distributor = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Distributor");
    /** @generated */
    public static final QName MD_ExtendedElementInformation = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ExtendedElementInformation");
    /** @generated */
    public static final QName MD_FeatureCatalogueDescription = 
        new QName("http://www.isotc211.org/2005/gmd","MD_FeatureCatalogueDescription");
    /** @generated */
    public static final QName MD_Format = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Format");
    /** @generated */
    public static final QName MD_GeometricObjects = 
        new QName("http://www.isotc211.org/2005/gmd","MD_GeometricObjects");
    /** @generated */
    public static final QName MD_GeometricObjectTypeCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_GeometricObjectTypeCode");
    /** @generated */
    public static final QName MD_Georectified = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Georectified");
    /** @generated */
    public static final QName MD_Georeferenceable = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Georeferenceable");
    /** @generated */
    public static final QName MD_GridSpatialRepresentation = 
        new QName("http://www.isotc211.org/2005/gmd","MD_GridSpatialRepresentation");
    /** @generated */
    public static final QName MD_Identifier = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Identifier");
    /** @generated */
    public static final QName MD_ImageDescription = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ImageDescription");
    /** @generated */
    public static final QName MD_ImagingConditionCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ImagingConditionCode");
    /** @generated */
    public static final QName MD_Keywords = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Keywords");
    /** @generated */
    public static final QName MD_KeywordTypeCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_KeywordTypeCode");
    /** @generated */
    public static final QName MD_LegalConstraints = 
        new QName("http://www.isotc211.org/2005/gmd","MD_LegalConstraints");
    /** @generated */
    public static final QName MD_MaintenanceFrequencyCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_MaintenanceFrequencyCode");
    /** @generated */
    public static final QName MD_MaintenanceInformation = 
        new QName("http://www.isotc211.org/2005/gmd","MD_MaintenanceInformation");
    /** @generated */
    public static final QName MD_Medium = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Medium");
    /** @generated */
    public static final QName MD_MediumFormatCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_MediumFormatCode");
    /** @generated */
    public static final QName MD_MediumNameCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_MediumNameCode");
    /** @generated */
    public static final QName MD_Metadata = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Metadata");
    /** @generated */
    public static final QName MD_MetadataExtensionInformation = 
        new QName("http://www.isotc211.org/2005/gmd","MD_MetadataExtensionInformation");
    /** @generated */
    public static final QName MD_ObligationCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ObligationCode");
    /** @generated */
    public static final QName MD_PixelOrientationCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_PixelOrientationCode");
    /** @generated */
    public static final QName MD_PortrayalCatalogueReference = 
        new QName("http://www.isotc211.org/2005/gmd","MD_PortrayalCatalogueReference");
    /** @generated */
    public static final QName MD_ProgressCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ProgressCode");
    /** @generated */
    public static final QName MD_RangeDimension = 
        new QName("http://www.isotc211.org/2005/gmd","MD_RangeDimension");
    /** @generated */
    public static final QName MD_ReferenceSystem = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ReferenceSystem");
    /** @generated */
    public static final QName MD_RepresentativeFraction = 
        new QName("http://www.isotc211.org/2005/gmd","MD_RepresentativeFraction");
    /** @generated */
    public static final QName MD_Resolution = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Resolution");
    /** @generated */
    public static final QName MD_RestrictionCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_RestrictionCode");
    /** @generated */
    public static final QName MD_ScopeCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ScopeCode");
    /** @generated */
    public static final QName MD_ScopeDescription = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ScopeDescription");
    /** @generated */
    public static final QName MD_SecurityConstraints = 
        new QName("http://www.isotc211.org/2005/gmd","MD_SecurityConstraints");
    /** @generated */
    public static final QName MD_ServiceIdentification = 
        new QName("http://www.isotc211.org/2005/gmd","MD_ServiceIdentification");
    /** @generated */
    public static final QName MD_SpatialRepresentationTypeCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_SpatialRepresentationTypeCode");
    /** @generated */
    public static final QName MD_StandardOrderProcess = 
        new QName("http://www.isotc211.org/2005/gmd","MD_StandardOrderProcess");
    /** @generated */
    public static final QName MD_TopicCategoryCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_TopicCategoryCode");
    /** @generated */
    public static final QName MD_TopologyLevelCode = 
        new QName("http://www.isotc211.org/2005/gmd","MD_TopologyLevelCode");
    /** @generated */
    public static final QName MD_Usage = 
        new QName("http://www.isotc211.org/2005/gmd","MD_Usage");
    /** @generated */
    public static final QName MD_VectorSpatialRepresentation = 
        new QName("http://www.isotc211.org/2005/gmd","MD_VectorSpatialRepresentation");
    /** @generated */
    public static final QName PT_FreeText = 
        new QName("http://www.isotc211.org/2005/gmd","PT_FreeText");
    /** @generated */
    public static final QName PT_Locale = 
        new QName("http://www.isotc211.org/2005/gmd","PT_Locale");
    /** @generated */
    public static final QName PT_LocaleContainer = 
        new QName("http://www.isotc211.org/2005/gmd","PT_LocaleContainer");
    /** @generated */
    public static final QName RS_Identifier = 
        new QName("http://www.isotc211.org/2005/gmd","RS_Identifier");
    /** @generated */
    public static final QName URL = 
        new QName("http://www.isotc211.org/2005/gmd","URL");

    /* Attributes */

}
    
