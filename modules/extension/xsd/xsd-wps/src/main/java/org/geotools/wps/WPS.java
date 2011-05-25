/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wps;


import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.gml2.GML;
import org.geotools.ows.v1_1.OWS;
import org.geotools.xml.SchemaLocationResolver;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.opengis.net/wps/1.0.0 schema.
 *
 * @generated
 *
 *
 * @source $URL$
 */
public final class WPS extends XSD {

    /** singleton instance */
    private static final WPS instance = new WPS();
    
    /**
     * Returns the singleton instance.
     */
    public static final WPS getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private WPS() {
    }
    
    protected void addDependencies(Set dependencies) {
       dependencies.add( OWS.getInstance() );
    }
    
    /**
     * Returns 'http://www.opengis.net/wps/1.0.0'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'wpsAll.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("wpsAll.xsd").toString();
    }
    
    @Override
    public SchemaLocationResolver createSchemaLocationResolver() {
        return new SchemaLocationResolver(this,"common");
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/wps/1.0.0";
    
    /* Type Definitions */
    /** @generated */
    public static final QName ComplexDataCombinationsType = 
        new QName("http://www.opengis.net/wps/1.0.0","ComplexDataCombinationsType");
    /** @generated */
    public static final QName ComplexDataCombinationType = 
        new QName("http://www.opengis.net/wps/1.0.0","ComplexDataCombinationType");
    /** @generated */
    public static final QName ComplexDataDescriptionType = 
        new QName("http://www.opengis.net/wps/1.0.0","ComplexDataDescriptionType");
    /** @generated */
    public static final QName ComplexDataType = 
        new QName("http://www.opengis.net/wps/1.0.0","ComplexDataType");
    /** @generated */
    public static final QName CRSsType = 
        new QName("http://www.opengis.net/wps/1.0.0","CRSsType");
    /** @generated */
    public static final QName DataInputsType = 
        new QName("http://www.opengis.net/wps/1.0.0","DataInputsType");
    /** @generated */
    public static final QName DataType = 
        new QName("http://www.opengis.net/wps/1.0.0","DataType");
    /** @generated */
    public static final QName DescriptionType = 
        new QName("http://www.opengis.net/wps/1.0.0","DescriptionType");
    /** @generated */
    public static final QName DocumentOutputDefinitionType = 
        new QName("http://www.opengis.net/wps/1.0.0","DocumentOutputDefinitionType");
    /** @generated */
    public static final QName InputDescriptionType = 
        new QName("http://www.opengis.net/wps/1.0.0","InputDescriptionType");
    /** @generated */
    public static final QName InputReferenceType = 
        new QName("http://www.opengis.net/wps/1.0.0","InputReferenceType");
    /** @generated */
    public static final QName InputType = 
        new QName("http://www.opengis.net/wps/1.0.0","InputType");
    /** @generated */
    public static final QName LanguagesType = 
        new QName("http://www.opengis.net/wps/1.0.0","LanguagesType");
    /** @generated */
    public static final QName LiteralDataType = 
        new QName("http://www.opengis.net/wps/1.0.0","LiteralDataType");
    /** @generated */
    public static final QName LiteralInputType = 
        new QName("http://www.opengis.net/wps/1.0.0","LiteralInputType");
    /** @generated */
    public static final QName LiteralOutputType = 
        new QName("http://www.opengis.net/wps/1.0.0","LiteralOutputType");
    /** @generated */
    public static final QName OutputDataType = 
        new QName("http://www.opengis.net/wps/1.0.0","OutputDataType");
    /** @generated */
    public static final QName OutputDefinitionsType = 
        new QName("http://www.opengis.net/wps/1.0.0","OutputDefinitionsType");
    /** @generated */
    public static final QName OutputDefinitionType = 
        new QName("http://www.opengis.net/wps/1.0.0","OutputDefinitionType");
    /** @generated */
    public static final QName OutputDescriptionType = 
        new QName("http://www.opengis.net/wps/1.0.0","OutputDescriptionType");
    /** @generated */
    public static final QName OutputReferenceType = 
        new QName("http://www.opengis.net/wps/1.0.0","OutputReferenceType");
    /** @generated */
    public static final QName ProcessBriefType = 
        new QName("http://www.opengis.net/wps/1.0.0","ProcessBriefType");
    /** @generated */
    public static final QName ProcessDescriptionType = 
        new QName("http://www.opengis.net/wps/1.0.0","ProcessDescriptionType");
    /** @generated */
    public static final QName ProcessFailedType = 
        new QName("http://www.opengis.net/wps/1.0.0","ProcessFailedType");
    /** @generated */
    public static final QName ProcessStartedType = 
        new QName("http://www.opengis.net/wps/1.0.0","ProcessStartedType");
    /** @generated */
    public static final QName RequestBaseType = 
        new QName("http://www.opengis.net/wps/1.0.0","RequestBaseType");
    /** @generated */
    public static final QName ResponseBaseType = 
        new QName("http://www.opengis.net/wps/1.0.0","ResponseBaseType");
    /** @generated */
    public static final QName ResponseDocumentType = 
        new QName("http://www.opengis.net/wps/1.0.0","ResponseDocumentType");
    /** @generated */
    public static final QName ResponseFormType = 
        new QName("http://www.opengis.net/wps/1.0.0","ResponseFormType");
    /** @generated */
    public static final QName StatusType = 
        new QName("http://www.opengis.net/wps/1.0.0","StatusType");
    /** @generated */
    public static final QName SupportedComplexDataInputType = 
        new QName("http://www.opengis.net/wps/1.0.0","SupportedComplexDataInputType");
    /** @generated */
    public static final QName SupportedComplexDataType = 
        new QName("http://www.opengis.net/wps/1.0.0","SupportedComplexDataType");
    /** @generated */
    public static final QName SupportedCRSsType = 
        new QName("http://www.opengis.net/wps/1.0.0","SupportedCRSsType");
    /** @generated */
    public static final QName SupportedUOMsType = 
        new QName("http://www.opengis.net/wps/1.0.0","SupportedUOMsType");
    /** @generated */
    public static final QName UOMsType = 
        new QName("http://www.opengis.net/wps/1.0.0","UOMsType");
    /** @generated */
    public static final QName ValuesReferenceType = 
        new QName("http://www.opengis.net/wps/1.0.0","ValuesReferenceType");
    /** @generated */
    public static final QName WPSCapabilitiesType = 
        new QName("http://www.opengis.net/wps/1.0.0","WPSCapabilitiesType");
    /** @generated */
    public static final QName _DescribeProcess = 
        new QName("http://www.opengis.net/wps/1.0.0","_DescribeProcess");
    /** @generated */
    public static final QName _Execute = 
        new QName("http://www.opengis.net/wps/1.0.0","_Execute");
    /** @generated */
    public static final QName _ExecuteResponse = 
        new QName("http://www.opengis.net/wps/1.0.0","_ExecuteResponse");
    /** @generated */
    public static final QName _GetCapabilities = 
        new QName("http://www.opengis.net/wps/1.0.0","_GetCapabilities");
    /** @generated */
    public static final QName _Languages = 
        new QName("http://www.opengis.net/wps/1.0.0","_Languages");

    /** @generated NOT */
    public static final QName _Languages_Default = 
        new QName("http://www.opengis.net/wps/1.0.0","_Languages_Default");
    
    /** @generated */
    public static final QName _ProcessDescriptions = 
        new QName("http://www.opengis.net/wps/1.0.0","_ProcessDescriptions");
    /** @generated */
    public static final QName _ProcessOfferings = 
        new QName("http://www.opengis.net/wps/1.0.0","_ProcessOfferings");
    /** @generated */
    public static final QName _WSDL = 
        new QName("http://www.opengis.net/wps/1.0.0","_WSDL");
    /** @generated */
    public static final QName InputReferenceType_Header = 
        new QName("http://www.opengis.net/wps/1.0.0","InputReferenceType_Header");
    /** @generated */
    public static final QName InputReferenceType_BodyReference = 
        new QName("http://www.opengis.net/wps/1.0.0","InputReferenceType_BodyReference");
    /** @generated */
    public static final QName ProcessDescriptionType_DataInputs = 
        new QName("http://www.opengis.net/wps/1.0.0","ProcessDescriptionType_DataInputs");
    /** @generated */
    public static final QName ProcessDescriptionType_ProcessOutputs = 
        new QName("http://www.opengis.net/wps/1.0.0","ProcessDescriptionType_ProcessOutputs");
    /** @generated */
    public static final QName SupportedCRSsType_Default = 
        new QName("http://www.opengis.net/wps/1.0.0","SupportedCRSsType_Default");
    /** @generated */
    public static final QName SupportedUOMsType_Default = 
        new QName("http://www.opengis.net/wps/1.0.0","SupportedUOMsType_Default");

    /* Elements */
    /** @generated */
    public static final QName Capabilities = 
        new QName("http://www.opengis.net/wps/1.0.0","Capabilities");
    /** @generated */
    public static final QName DescribeProcess = 
        new QName("http://www.opengis.net/wps/1.0.0","DescribeProcess");
    /** @generated */
    public static final QName Execute = 
        new QName("http://www.opengis.net/wps/1.0.0","Execute");
    /** @generated */
    public static final QName ExecuteResponse = 
        new QName("http://www.opengis.net/wps/1.0.0","ExecuteResponse");
    /** generated NOT */
    public static final QName ExecuteResponse_ProcessOutputs = 
        new QName("http://www.opengis.net/wps/1.0.0","ExecuteResponse_ProcessOutputs");
    /** @generated */
    public static final QName GetCapabilities = 
        new QName("http://www.opengis.net/wps/1.0.0","GetCapabilities");
    /** @generated */
    public static final QName Languages = 
        new QName("http://www.opengis.net/wps/1.0.0","Languages");
    /** @generated */
    public static final QName ProcessDescriptions = 
        new QName("http://www.opengis.net/wps/1.0.0","ProcessDescriptions");
    /** @generated */
    public static final QName ProcessOfferings = 
        new QName("http://www.opengis.net/wps/1.0.0","ProcessOfferings");
    /** @generated */
    public static final QName WSDL = 
        new QName("http://www.opengis.net/wps/1.0.0","WSDL");

    /* Attributes */
    /** @generated */
    public static final QName processVersion = 
        new QName("http://www.opengis.net/wps/1.0.0","processVersion");

}
    
