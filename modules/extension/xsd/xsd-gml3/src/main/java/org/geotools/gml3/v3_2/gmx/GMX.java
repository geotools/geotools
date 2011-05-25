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
package org.geotools.gml3.v3_2.gmx;

import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.gml3.v3_2.gco.GCO;
import org.geotools.gml3.v3_2.gmd.GMD;
import org.geotools.xlink.XLINK;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.isotc211.org/2005/gmx schema.
 *
 * @generated
 *
 *
 * @source $URL$
 */
public final class GMX extends XSD {

    /** singleton instance */
    private static final GMX instance = new GMX();
    
    /**
     * Returns the singleton instance.
     */
    public static final GMX getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private GMX() {
    }
    
    protected void addDependencies(Set dependencies) {
        dependencies.add( XLINK.getInstance() );
        dependencies.add( GCO.getInstance() );
        dependencies.add( GMD.getInstance() );
    }
    
    /**
     * Returns 'http://www.isotc211.org/2005/gmx'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'gmx.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("gmx.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.isotc211.org/2005/gmx";
    
    /* Type Definitions */
    /** @generated */
    public static final QName AbstractCT_Catalogue_Type = 
        new QName("http://www.isotc211.org/2005/gmx","AbstractCT_Catalogue_Type");
    /** @generated */
    public static final QName AbstractMX_File_Type = 
        new QName("http://www.isotc211.org/2005/gmx","AbstractMX_File_Type");
    /** @generated */
    public static final QName Anchor_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","Anchor_PropertyType");
    /** @generated */
    public static final QName Anchor_Type = 
        new QName("http://www.isotc211.org/2005/gmx","Anchor_Type");
    /** @generated */
    public static final QName BaseUnit_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","BaseUnit_PropertyType");
    /** @generated */
    public static final QName ClAlternativeExpression_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ClAlternativeExpression_PropertyType");
    /** @generated */
    public static final QName ClAlternativeExpression_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ClAlternativeExpression_Type");
    /** @generated */
    public static final QName CodeAlternativeExpression_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CodeAlternativeExpression_PropertyType");
    /** @generated */
    public static final QName CodeAlternativeExpression_Type = 
        new QName("http://www.isotc211.org/2005/gmx","CodeAlternativeExpression_Type");
    /** @generated */
    public static final QName CodeDefinition_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CodeDefinition_PropertyType");
    /** @generated */
    public static final QName CodeDefinition_Type = 
        new QName("http://www.isotc211.org/2005/gmx","CodeDefinition_Type");
    /** @generated */
    public static final QName CodeListDictionary_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CodeListDictionary_PropertyType");
    /** @generated */
    public static final QName CodeListDictionary_Type = 
        new QName("http://www.isotc211.org/2005/gmx","CodeListDictionary_Type");
    /** @generated */
    public static final QName ConventionalUnit_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ConventionalUnit_PropertyType");
    /** @generated */
    public static final QName CoordinateSystemAlt_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CoordinateSystemAlt_PropertyType");
    /** @generated */
    public static final QName CoordinateSystemAlt_Type = 
        new QName("http://www.isotc211.org/2005/gmx","CoordinateSystemAlt_Type");
    /** @generated */
    public static final QName CoordinateSystemAxisAlt_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CoordinateSystemAxisAlt_PropertyType");
    /** @generated */
    public static final QName CoordinateSystemAxisAlt_Type = 
        new QName("http://www.isotc211.org/2005/gmx","CoordinateSystemAxisAlt_Type");
    /** @generated */
    public static final QName CrsAlt_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CrsAlt_PropertyType");
    /** @generated */
    public static final QName CrsAlt_Type = 
        new QName("http://www.isotc211.org/2005/gmx","CrsAlt_Type");
    /** @generated */
    public static final QName CT_Catalogue_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CT_Catalogue_PropertyType");
    /** @generated */
    public static final QName CT_Codelist_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CT_Codelist_PropertyType");
    /** @generated */
    public static final QName CT_CodelistCatalogue_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CT_CodelistCatalogue_PropertyType");
    /** @generated */
    public static final QName CT_CodelistCatalogue_Type = 
        new QName("http://www.isotc211.org/2005/gmx","CT_CodelistCatalogue_Type");
    /** @generated */
    public static final QName CT_CodelistValue_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CT_CodelistValue_PropertyType");
    /** @generated */
    public static final QName CT_CoordinateSystem_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CT_CoordinateSystem_PropertyType");
    /** @generated */
    public static final QName CT_CoordinateSystemAxis_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CT_CoordinateSystemAxis_PropertyType");
    /** @generated */
    public static final QName CT_CRS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CT_CRS_PropertyType");
    /** @generated */
    public static final QName CT_CrsCatalogue_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CT_CrsCatalogue_PropertyType");
    /** @generated */
    public static final QName CT_CrsCatalogue_Type = 
        new QName("http://www.isotc211.org/2005/gmx","CT_CrsCatalogue_Type");
    /** @generated */
    public static final QName CT_Datum_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CT_Datum_PropertyType");
    /** @generated */
    public static final QName CT_Ellipsoid_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CT_Ellipsoid_PropertyType");
    /** @generated */
    public static final QName CT_Operation_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CT_Operation_PropertyType");
    /** @generated */
    public static final QName CT_OperationMethod_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CT_OperationMethod_PropertyType");
    /** @generated */
    public static final QName CT_OperationParameters_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CT_OperationParameters_PropertyType");
    /** @generated */
    public static final QName CT_PrimeMeridian_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CT_PrimeMeridian_PropertyType");
    /** @generated */
    public static final QName CT_UomCatalogue_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","CT_UomCatalogue_PropertyType");
    /** @generated */
    public static final QName CT_UomCatalogue_Type = 
        new QName("http://www.isotc211.org/2005/gmx","CT_UomCatalogue_Type");
    /** @generated */
    public static final QName DatumAlt_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","DatumAlt_PropertyType");
    /** @generated */
    public static final QName DatumAlt_Type = 
        new QName("http://www.isotc211.org/2005/gmx","DatumAlt_Type");
    /** @generated */
    public static final QName DerivedUnit_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","DerivedUnit_PropertyType");
    /** @generated */
    public static final QName EllipsoidAlt_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","EllipsoidAlt_PropertyType");
    /** @generated */
    public static final QName EllipsoidAlt_Type = 
        new QName("http://www.isotc211.org/2005/gmx","EllipsoidAlt_Type");
    /** @generated */
    public static final QName FileName_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","FileName_PropertyType");
    /** @generated */
    public static final QName FileName_Type = 
        new QName("http://www.isotc211.org/2005/gmx","FileName_Type");
    /** @generated */
    public static final QName MimeFileType_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","MimeFileType_PropertyType");
    /** @generated */
    public static final QName MimeFileType_Type = 
        new QName("http://www.isotc211.org/2005/gmx","MimeFileType_Type");
    /** @generated */
    public static final QName ML_AffineCS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_AffineCS_PropertyType");
    /** @generated */
    public static final QName ML_AffineCS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_AffineCS_Type");
    /** @generated */
    public static final QName ML_BaseUnit_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_BaseUnit_PropertyType");
    /** @generated */
    public static final QName ML_BaseUnit_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_BaseUnit_Type");
    /** @generated */
    public static final QName ML_CartesianCS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CartesianCS_PropertyType");
    /** @generated */
    public static final QName ML_CartesianCS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CartesianCS_Type");
    /** @generated */
    public static final QName ML_CodeDefinition_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CodeDefinition_PropertyType");
    /** @generated */
    public static final QName ML_CodeDefinition_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CodeDefinition_Type");
    /** @generated */
    public static final QName ML_CodeListDictionary_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CodeListDictionary_PropertyType");
    /** @generated */
    public static final QName ML_CodeListDictionary_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CodeListDictionary_Type");
    /** @generated */
    public static final QName ML_CompoundCRS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CompoundCRS_PropertyType");
    /** @generated */
    public static final QName ML_CompoundCRS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CompoundCRS_Type");
    /** @generated */
    public static final QName ML_ConcatenatedOperation_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_ConcatenatedOperation_PropertyType");
    /** @generated */
    public static final QName ML_ConcatenatedOperation_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_ConcatenatedOperation_Type");
    /** @generated */
    public static final QName ML_ConventionalUnit_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_ConventionalUnit_PropertyType");
    /** @generated */
    public static final QName ML_ConventionalUnit_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_ConventionalUnit_Type");
    /** @generated */
    public static final QName ML_Conversion_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_Conversion_PropertyType");
    /** @generated */
    public static final QName ML_Conversion_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_Conversion_Type");
    /** @generated */
    public static final QName ML_CoordinateSystemAxis_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CoordinateSystemAxis_PropertyType");
    /** @generated */
    public static final QName ML_CoordinateSystemAxis_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CoordinateSystemAxis_Type");
    /** @generated */
    public static final QName ML_CylindricalCS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CylindricalCS_PropertyType");
    /** @generated */
    public static final QName ML_CylindricalCS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CylindricalCS_Type");
    /** @generated */
    public static final QName ML_DerivedCRS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_DerivedCRS_PropertyType");
    /** @generated */
    public static final QName ML_DerivedCRS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_DerivedCRS_Type");
    /** @generated */
    public static final QName ML_DerivedUnit_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_DerivedUnit_PropertyType");
    /** @generated */
    public static final QName ML_DerivedUnit_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_DerivedUnit_Type");
    /** @generated */
    public static final QName ML_Ellipsoid_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_Ellipsoid_PropertyType");
    /** @generated */
    public static final QName ML_Ellipsoid_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_Ellipsoid_Type");
    /** @generated */
    public static final QName ML_EllipsoidalCS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_EllipsoidalCS_PropertyType");
    /** @generated */
    public static final QName ML_EllipsoidalCS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_EllipsoidalCS_Type");
    /** @generated */
    public static final QName ML_EngineeringCRS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_EngineeringCRS_PropertyType");
    /** @generated */
    public static final QName ML_EngineeringCRS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_EngineeringCRS_Type");
    /** @generated */
    public static final QName ML_EngineeringDatum_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_EngineeringDatum_PropertyType");
    /** @generated */
    public static final QName ML_EngineeringDatum_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_EngineeringDatum_Type");
    /** @generated */
    public static final QName ML_GeodeticCRS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_GeodeticCRS_PropertyType");
    /** @generated */
    public static final QName ML_GeodeticCRS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_GeodeticCRS_Type");
    /** @generated */
    public static final QName ML_GeodeticDatum_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_GeodeticDatum_PropertyType");
    /** @generated */
    public static final QName ML_GeodeticDatum_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_GeodeticDatum_Type");
    /** @generated */
    public static final QName ML_ImageCRS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_ImageCRS_PropertyType");
    /** @generated */
    public static final QName ML_ImageCRS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_ImageCRS_Type");
    /** @generated */
    public static final QName ML_ImageDatum_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_ImageDatum_PropertyType");
    /** @generated */
    public static final QName ML_ImageDatum_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_ImageDatum_Type");
    /** @generated */
    public static final QName ML_LinearCS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_LinearCS_PropertyType");
    /** @generated */
    public static final QName ML_LinearCS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_LinearCS_Type");
    /** @generated */
    public static final QName ML_OperationMethod_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_OperationMethod_PropertyType");
    /** @generated */
    public static final QName ML_OperationMethod_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_OperationMethod_Type");
    /** @generated */
    public static final QName ML_OperationParameter_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_OperationParameter_PropertyType");
    /** @generated */
    public static final QName ML_OperationParameter_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_OperationParameter_Type");
    /** @generated */
    public static final QName ML_OperationParameterGroup_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_OperationParameterGroup_PropertyType");
    /** @generated */
    public static final QName ML_OperationParameterGroup_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_OperationParameterGroup_Type");
    /** @generated */
    public static final QName ML_PassThroughOperation_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_PassThroughOperation_PropertyType");
    /** @generated */
    public static final QName ML_PassThroughOperation_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_PassThroughOperation_Type");
    /** @generated */
    public static final QName ML_PolarCS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_PolarCS_PropertyType");
    /** @generated */
    public static final QName ML_PolarCS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_PolarCS_Type");
    /** @generated */
    public static final QName ML_PrimeMeridian_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_PrimeMeridian_PropertyType");
    /** @generated */
    public static final QName ML_PrimeMeridian_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_PrimeMeridian_Type");
    /** @generated */
    public static final QName ML_ProjectedCRS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_ProjectedCRS_PropertyType");
    /** @generated */
    public static final QName ML_ProjectedCRS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_ProjectedCRS_Type");
    /** @generated */
    public static final QName ML_SphericalCS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_SphericalCS_PropertyType");
    /** @generated */
    public static final QName ML_SphericalCS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_SphericalCS_Type");
    /** @generated */
    public static final QName ML_TemporalCRS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_TemporalCRS_PropertyType");
    /** @generated */
    public static final QName ML_TemporalCRS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_TemporalCRS_Type");
    /** @generated */
    public static final QName ML_TemporalDatum_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_TemporalDatum_PropertyType");
    /** @generated */
    public static final QName ML_TemporalDatum_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_TemporalDatum_Type");
    /** @generated */
    public static final QName ML_TimeCS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_TimeCS_PropertyType");
    /** @generated */
    public static final QName ML_TimeCS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_TimeCS_Type");
    /** @generated */
    public static final QName ML_Transformation_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_Transformation_PropertyType");
    /** @generated */
    public static final QName ML_Transformation_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_Transformation_Type");
    /** @generated */
    public static final QName ML_UnitDefinition_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_UnitDefinition_PropertyType");
    /** @generated */
    public static final QName ML_UnitDefinition_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_UnitDefinition_Type");
    /** @generated */
    public static final QName ML_UserDefinedCS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_UserDefinedCS_PropertyType");
    /** @generated */
    public static final QName ML_UserDefinedCS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_UserDefinedCS_Type");
    /** @generated */
    public static final QName ML_VerticalCRS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_VerticalCRS_PropertyType");
    /** @generated */
    public static final QName ML_VerticalCRS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_VerticalCRS_Type");
    /** @generated */
    public static final QName ML_VerticalCS_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_VerticalCS_PropertyType");
    /** @generated */
    public static final QName ML_VerticalCS_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_VerticalCS_Type");
    /** @generated */
    public static final QName ML_VerticalDatum_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","ML_VerticalDatum_PropertyType");
    /** @generated */
    public static final QName ML_VerticalDatum_Type = 
        new QName("http://www.isotc211.org/2005/gmx","ML_VerticalDatum_Type");
    /** @generated */
    public static final QName MX_Aggregate_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","MX_Aggregate_PropertyType");
    /** @generated */
    public static final QName MX_Aggregate_Type = 
        new QName("http://www.isotc211.org/2005/gmx","MX_Aggregate_Type");
    /** @generated */
    public static final QName MX_DataFile_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","MX_DataFile_PropertyType");
    /** @generated */
    public static final QName MX_DataFile_Type = 
        new QName("http://www.isotc211.org/2005/gmx","MX_DataFile_Type");
    /** @generated */
    public static final QName MX_DataSet_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","MX_DataSet_PropertyType");
    /** @generated */
    public static final QName MX_DataSet_Type = 
        new QName("http://www.isotc211.org/2005/gmx","MX_DataSet_Type");
    /** @generated */
    public static final QName MX_File_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","MX_File_PropertyType");
    /** @generated */
    public static final QName MX_ScopeCode_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","MX_ScopeCode_PropertyType");
    /** @generated */
    public static final QName MX_SupportFile_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","MX_SupportFile_PropertyType");
    /** @generated */
    public static final QName MX_SupportFile_Type = 
        new QName("http://www.isotc211.org/2005/gmx","MX_SupportFile_Type");
    /** @generated */
    public static final QName OperationAlt_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","OperationAlt_PropertyType");
    /** @generated */
    public static final QName OperationAlt_Type = 
        new QName("http://www.isotc211.org/2005/gmx","OperationAlt_Type");
    /** @generated */
    public static final QName OperationMethodAlt_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","OperationMethodAlt_PropertyType");
    /** @generated */
    public static final QName OperationMethodAlt_Type = 
        new QName("http://www.isotc211.org/2005/gmx","OperationMethodAlt_Type");
    /** @generated */
    public static final QName OperationParameterAlt_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","OperationParameterAlt_PropertyType");
    /** @generated */
    public static final QName OperationParameterAlt_Type = 
        new QName("http://www.isotc211.org/2005/gmx","OperationParameterAlt_Type");
    /** @generated */
    public static final QName PrimeMeridianAlt_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","PrimeMeridianAlt_PropertyType");
    /** @generated */
    public static final QName PrimeMeridianAlt_Type = 
        new QName("http://www.isotc211.org/2005/gmx","PrimeMeridianAlt_Type");
    /** @generated */
    public static final QName UnitDefinition_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","UnitDefinition_PropertyType");
    /** @generated */
    public static final QName UomAlternativeExpression_PropertyType = 
        new QName("http://www.isotc211.org/2005/gmx","UomAlternativeExpression_PropertyType");
    /** @generated */
    public static final QName UomAlternativeExpression_Type = 
        new QName("http://www.isotc211.org/2005/gmx","UomAlternativeExpression_Type");

    /* Elements */
    /** @generated */
    public static final QName AbstractCT_Catalogue = 
        new QName("http://www.isotc211.org/2005/gmx","AbstractCT_Catalogue");
    /** @generated */
    public static final QName AbstractMX_File = 
        new QName("http://www.isotc211.org/2005/gmx","AbstractMX_File");
    /** @generated */
    public static final QName Anchor = 
        new QName("http://www.isotc211.org/2005/gmx","Anchor");
    /** @generated */
    public static final QName ClAlternativeExpression = 
        new QName("http://www.isotc211.org/2005/gmx","ClAlternativeExpression");
    /** @generated */
    public static final QName CodeAlternativeExpression = 
        new QName("http://www.isotc211.org/2005/gmx","CodeAlternativeExpression");
    /** @generated */
    public static final QName CodeDefinition = 
        new QName("http://www.isotc211.org/2005/gmx","CodeDefinition");
    /** @generated */
    public static final QName CodeListDictionary = 
        new QName("http://www.isotc211.org/2005/gmx","CodeListDictionary");
    /** @generated */
    public static final QName CoordinateSystemAlt = 
        new QName("http://www.isotc211.org/2005/gmx","CoordinateSystemAlt");
    /** @generated */
    public static final QName CoordinateSystemAxisAlt = 
        new QName("http://www.isotc211.org/2005/gmx","CoordinateSystemAxisAlt");
    /** @generated */
    public static final QName CrsAlt = 
        new QName("http://www.isotc211.org/2005/gmx","CrsAlt");
    /** @generated */
    public static final QName CT_CodelistCatalogue = 
        new QName("http://www.isotc211.org/2005/gmx","CT_CodelistCatalogue");
    /** @generated */
    public static final QName CT_CrsCatalogue = 
        new QName("http://www.isotc211.org/2005/gmx","CT_CrsCatalogue");
    /** @generated */
    public static final QName CT_UomCatalogue = 
        new QName("http://www.isotc211.org/2005/gmx","CT_UomCatalogue");
    /** @generated */
    public static final QName DatumAlt = 
        new QName("http://www.isotc211.org/2005/gmx","DatumAlt");
    /** @generated */
    public static final QName EllipsoidAlt = 
        new QName("http://www.isotc211.org/2005/gmx","EllipsoidAlt");
    /** @generated */
    public static final QName FileName = 
        new QName("http://www.isotc211.org/2005/gmx","FileName");
    /** @generated */
    public static final QName MimeFileType = 
        new QName("http://www.isotc211.org/2005/gmx","MimeFileType");
    /** @generated */
    public static final QName ML_AffineCS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_AffineCS");
    /** @generated */
    public static final QName ML_BaseUnit = 
        new QName("http://www.isotc211.org/2005/gmx","ML_BaseUnit");
    /** @generated */
    public static final QName ML_CartesianCS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CartesianCS");
    /** @generated */
    public static final QName ML_CodeDefinition = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CodeDefinition");
    /** @generated */
    public static final QName ML_CodeListDictionary = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CodeListDictionary");
    /** @generated */
    public static final QName ML_CompoundCRS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CompoundCRS");
    /** @generated */
    public static final QName ML_ConcatenatedOperation = 
        new QName("http://www.isotc211.org/2005/gmx","ML_ConcatenatedOperation");
    /** @generated */
    public static final QName ML_ConventionalUnit = 
        new QName("http://www.isotc211.org/2005/gmx","ML_ConventionalUnit");
    /** @generated */
    public static final QName ML_Conversion = 
        new QName("http://www.isotc211.org/2005/gmx","ML_Conversion");
    /** @generated */
    public static final QName ML_CoordinateSystemAxis = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CoordinateSystemAxis");
    /** @generated */
    public static final QName ML_CylindricalCS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_CylindricalCS");
    /** @generated */
    public static final QName ML_DerivedCRS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_DerivedCRS");
    /** @generated */
    public static final QName ML_DerivedUnit = 
        new QName("http://www.isotc211.org/2005/gmx","ML_DerivedUnit");
    /** @generated */
    public static final QName ML_Ellipsoid = 
        new QName("http://www.isotc211.org/2005/gmx","ML_Ellipsoid");
    /** @generated */
    public static final QName ML_EllipsoidalCS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_EllipsoidalCS");
    /** @generated */
    public static final QName ML_EngineeringCRS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_EngineeringCRS");
    /** @generated */
    public static final QName ML_EngineeringDatum = 
        new QName("http://www.isotc211.org/2005/gmx","ML_EngineeringDatum");
    /** @generated */
    public static final QName ML_GeodeticCRS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_GeodeticCRS");
    /** @generated */
    public static final QName ML_GeodeticDatum = 
        new QName("http://www.isotc211.org/2005/gmx","ML_GeodeticDatum");
    /** @generated */
    public static final QName ML_ImageCRS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_ImageCRS");
    /** @generated */
    public static final QName ML_ImageDatum = 
        new QName("http://www.isotc211.org/2005/gmx","ML_ImageDatum");
    /** @generated */
    public static final QName ML_LinearCS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_LinearCS");
    /** @generated */
    public static final QName ML_OperationMethod = 
        new QName("http://www.isotc211.org/2005/gmx","ML_OperationMethod");
    /** @generated */
    public static final QName ML_OperationParameter = 
        new QName("http://www.isotc211.org/2005/gmx","ML_OperationParameter");
    /** @generated */
    public static final QName ML_OperationParameterGroup = 
        new QName("http://www.isotc211.org/2005/gmx","ML_OperationParameterGroup");
    /** @generated */
    public static final QName ML_PassThroughOperation = 
        new QName("http://www.isotc211.org/2005/gmx","ML_PassThroughOperation");
    /** @generated */
    public static final QName ML_PolarCS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_PolarCS");
    /** @generated */
    public static final QName ML_PrimeMeridian = 
        new QName("http://www.isotc211.org/2005/gmx","ML_PrimeMeridian");
    /** @generated */
    public static final QName ML_ProjectedCRS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_ProjectedCRS");
    /** @generated */
    public static final QName ML_SphericalCS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_SphericalCS");
    /** @generated */
    public static final QName ML_TemporalCRS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_TemporalCRS");
    /** @generated */
    public static final QName ML_TemporalDatum = 
        new QName("http://www.isotc211.org/2005/gmx","ML_TemporalDatum");
    /** @generated */
    public static final QName ML_TimeCS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_TimeCS");
    /** @generated */
    public static final QName ML_Transformation = 
        new QName("http://www.isotc211.org/2005/gmx","ML_Transformation");
    /** @generated */
    public static final QName ML_UnitDefinition = 
        new QName("http://www.isotc211.org/2005/gmx","ML_UnitDefinition");
    /** @generated */
    public static final QName ML_UserDefinedCS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_UserDefinedCS");
    /** @generated */
    public static final QName ML_VerticalCRS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_VerticalCRS");
    /** @generated */
    public static final QName ML_VerticalCS = 
        new QName("http://www.isotc211.org/2005/gmx","ML_VerticalCS");
    /** @generated */
    public static final QName ML_VerticalDatum = 
        new QName("http://www.isotc211.org/2005/gmx","ML_VerticalDatum");
    /** @generated */
    public static final QName MX_Aggregate = 
        new QName("http://www.isotc211.org/2005/gmx","MX_Aggregate");
    /** @generated */
    public static final QName MX_DataFile = 
        new QName("http://www.isotc211.org/2005/gmx","MX_DataFile");
    /** @generated */
    public static final QName MX_DataSet = 
        new QName("http://www.isotc211.org/2005/gmx","MX_DataSet");
    /** @generated */
    public static final QName MX_ScopeCode = 
        new QName("http://www.isotc211.org/2005/gmx","MX_ScopeCode");
    /** @generated */
    public static final QName MX_SupportFile = 
        new QName("http://www.isotc211.org/2005/gmx","MX_SupportFile");
    /** @generated */
    public static final QName OperationAlt = 
        new QName("http://www.isotc211.org/2005/gmx","OperationAlt");
    /** @generated */
    public static final QName OperationMethodAlt = 
        new QName("http://www.isotc211.org/2005/gmx","OperationMethodAlt");
    /** @generated */
    public static final QName OperationParameterAlt = 
        new QName("http://www.isotc211.org/2005/gmx","OperationParameterAlt");
    /** @generated */
    public static final QName PrimeMeridianAlt = 
        new QName("http://www.isotc211.org/2005/gmx","PrimeMeridianAlt");
    /** @generated */
    public static final QName UomAlternativeExpression = 
        new QName("http://www.isotc211.org/2005/gmx","UomAlternativeExpression");

    /* Attributes */

}
    
