/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wms.v1_3;

import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.opengis.net/wms schema.
 *
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wms/src/main/java/org/geotools/wms/v1_3/WMS.java $
 */
public final class WMS extends XSD {

    /** singleton instance */
    private static final WMS instance = new WMS();
    
    /**
     * Returns the singleton instance.
     */
    public static final WMS getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private WMS() {
    }
    
    protected void addDependencies(Set dependencies) {
       //TODO: add dependencies here
    }
    
    /**
     * Returns 'http://www.opengis.net/wms'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'capabilities_1_3_0.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("capabilities_1_3_0.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/wms";
    
    /* Type Definitions */
    /** @generated */
    public static final QName latitudeType = 
        new QName("http://www.opengis.net/wms","latitudeType");
    /** @generated */
    public static final QName longitudeType = 
        new QName("http://www.opengis.net/wms","longitudeType");
    /** @generated */
    public static final QName OperationType = 
        new QName("http://www.opengis.net/wms","OperationType");
    /** @generated */
    public static final QName _Attribution = 
        new QName("http://www.opengis.net/wms","_Attribution");
    /** @generated */
    public static final QName _AuthorityURL = 
        new QName("http://www.opengis.net/wms","_AuthorityURL");
    /** @generated */
    public static final QName _BoundingBox = 
        new QName("http://www.opengis.net/wms","_BoundingBox");
    /** @generated */
    public static final QName _Capability = 
        new QName("http://www.opengis.net/wms","_Capability");
    /** @generated */
    public static final QName _ContactAddress = 
        new QName("http://www.opengis.net/wms","_ContactAddress");
    /** @generated */
    public static final QName _ContactInformation = 
        new QName("http://www.opengis.net/wms","_ContactInformation");
    /** @generated */
    public static final QName _ContactPersonPrimary = 
        new QName("http://www.opengis.net/wms","_ContactPersonPrimary");
    /** @generated */
    public static final QName _DataURL = 
        new QName("http://www.opengis.net/wms","_DataURL");
    /** @generated */
    public static final QName _DCPType = 
        new QName("http://www.opengis.net/wms","_DCPType");
    /** @generated */
    public static final QName _Dimension = 
        new QName("http://www.opengis.net/wms","_Dimension");
    /** @generated */
    public static final QName _EX_GeographicBoundingBox = 
        new QName("http://www.opengis.net/wms","_EX_GeographicBoundingBox");
    /** @generated */
    public static final QName _Exception = 
        new QName("http://www.opengis.net/wms","_Exception");
    /** @generated */
    public static final QName _FeatureListURL = 
        new QName("http://www.opengis.net/wms","_FeatureListURL");
    /** @generated */
    public static final QName _Get = 
        new QName("http://www.opengis.net/wms","_Get");
    /** @generated */
    public static final QName _HTTP = 
        new QName("http://www.opengis.net/wms","_HTTP");
    /** @generated */
    public static final QName _Identifier = 
        new QName("http://www.opengis.net/wms","_Identifier");
    /** @generated */
    public static final QName _Keyword = 
        new QName("http://www.opengis.net/wms","_Keyword");
    /** @generated */
    public static final QName _KeywordList = 
        new QName("http://www.opengis.net/wms","_KeywordList");
    /** @generated */
    public static final QName _Layer = 
        new QName("http://www.opengis.net/wms","_Layer");
    /** @generated */
    public static final QName _LegendURL = 
        new QName("http://www.opengis.net/wms","_LegendURL");
    /** @generated */
    public static final QName _LogoURL = 
        new QName("http://www.opengis.net/wms","_LogoURL");
    /** @generated */
    public static final QName _MetadataURL = 
        new QName("http://www.opengis.net/wms","_MetadataURL");
    /** @generated */
    public static final QName _OnlineResource = 
        new QName("http://www.opengis.net/wms","_OnlineResource");
    /** @generated */
    public static final QName _Post = 
        new QName("http://www.opengis.net/wms","_Post");
    /** @generated */
    public static final QName _Request = 
        new QName("http://www.opengis.net/wms","_Request");
    /** @generated */
    public static final QName _Service = 
        new QName("http://www.opengis.net/wms","_Service");
    /** @generated */
    public static final QName _Style = 
        new QName("http://www.opengis.net/wms","_Style");
    /** @generated */
    public static final QName _StyleSheetURL = 
        new QName("http://www.opengis.net/wms","_StyleSheetURL");
    /** @generated */
    public static final QName _StyleURL = 
        new QName("http://www.opengis.net/wms","_StyleURL");
    /** @generated */
    public static final QName _WMS_Capabilities = 
        new QName("http://www.opengis.net/wms","_WMS_Capabilities");

    /* Elements */
    /** @generated */
    public static final QName _ExtendedCapabilities = 
        new QName("http://www.opengis.net/wms","_ExtendedCapabilities");
    /** @generated */
    public static final QName _ExtendedOperation = 
        new QName("http://www.opengis.net/wms","_ExtendedOperation");
    /** @generated */
    public static final QName Abstract = 
        new QName("http://www.opengis.net/wms","Abstract");
    /** @generated */
    public static final QName AccessConstraints = 
        new QName("http://www.opengis.net/wms","AccessConstraints");
    /** @generated */
    public static final QName Address = 
        new QName("http://www.opengis.net/wms","Address");
    /** @generated */
    public static final QName AddressType = 
        new QName("http://www.opengis.net/wms","AddressType");
    /** @generated */
    public static final QName Attribution = 
        new QName("http://www.opengis.net/wms","Attribution");
    /** @generated */
    public static final QName AuthorityURL = 
        new QName("http://www.opengis.net/wms","AuthorityURL");
    /** @generated */
    public static final QName BoundingBox = 
        new QName("http://www.opengis.net/wms","BoundingBox");
    /** @generated */
    public static final QName Capability = 
        new QName("http://www.opengis.net/wms","Capability");
    /** @generated */
    public static final QName City = 
        new QName("http://www.opengis.net/wms","City");
    /** @generated */
    public static final QName ContactAddress = 
        new QName("http://www.opengis.net/wms","ContactAddress");
    /** @generated */
    public static final QName ContactElectronicMailAddress = 
        new QName("http://www.opengis.net/wms","ContactElectronicMailAddress");
    /** @generated */
    public static final QName ContactFacsimileTelephone = 
        new QName("http://www.opengis.net/wms","ContactFacsimileTelephone");
    /** @generated */
    public static final QName ContactInformation = 
        new QName("http://www.opengis.net/wms","ContactInformation");
    /** @generated */
    public static final QName ContactOrganization = 
        new QName("http://www.opengis.net/wms","ContactOrganization");
    /** @generated */
    public static final QName ContactPerson = 
        new QName("http://www.opengis.net/wms","ContactPerson");
    /** @generated */
    public static final QName ContactPersonPrimary = 
        new QName("http://www.opengis.net/wms","ContactPersonPrimary");
    /** @generated */
    public static final QName ContactPosition = 
        new QName("http://www.opengis.net/wms","ContactPosition");
    /** @generated */
    public static final QName ContactVoiceTelephone = 
        new QName("http://www.opengis.net/wms","ContactVoiceTelephone");
    /** @generated */
    public static final QName Country = 
        new QName("http://www.opengis.net/wms","Country");
    /** @generated */
    public static final QName CRS = 
        new QName("http://www.opengis.net/wms","CRS");
    /** @generated */
    public static final QName DataURL = 
        new QName("http://www.opengis.net/wms","DataURL");
    /** @generated */
    public static final QName DCPType = 
        new QName("http://www.opengis.net/wms","DCPType");
    /** @generated */
    public static final QName Dimension = 
        new QName("http://www.opengis.net/wms","Dimension");
    /** @generated */
    public static final QName EX_GeographicBoundingBox = 
        new QName("http://www.opengis.net/wms","EX_GeographicBoundingBox");
    /** @generated */
    public static final QName Exception = 
        new QName("http://www.opengis.net/wms","Exception");
    /** @generated */
    public static final QName FeatureListURL = 
        new QName("http://www.opengis.net/wms","FeatureListURL");
    /** @generated */
    public static final QName Fees = 
        new QName("http://www.opengis.net/wms","Fees");
    /** @generated */
    public static final QName Format = 
        new QName("http://www.opengis.net/wms","Format");
    /** @generated */
    public static final QName Get = 
        new QName("http://www.opengis.net/wms","Get");
    /** @generated */
    public static final QName GetCapabilities = 
        new QName("http://www.opengis.net/wms","GetCapabilities");
    /** @generated */
    public static final QName GetFeatureInfo = 
        new QName("http://www.opengis.net/wms","GetFeatureInfo");
    /** @generated */
    public static final QName GetMap = 
        new QName("http://www.opengis.net/wms","GetMap");
    /** @generated */
    public static final QName HTTP = 
        new QName("http://www.opengis.net/wms","HTTP");
    /** @generated */
    public static final QName Identifier = 
        new QName("http://www.opengis.net/wms","Identifier");
    /** @generated */
    public static final QName Keyword = 
        new QName("http://www.opengis.net/wms","Keyword");
    /** @generated */
    public static final QName KeywordList = 
        new QName("http://www.opengis.net/wms","KeywordList");
    /** @generated */
    public static final QName Layer = 
        new QName("http://www.opengis.net/wms","Layer");
    /** @generated */
    public static final QName LayerLimit = 
        new QName("http://www.opengis.net/wms","LayerLimit");
    /** @generated */
    public static final QName LegendURL = 
        new QName("http://www.opengis.net/wms","LegendURL");
    /** @generated */
    public static final QName LogoURL = 
        new QName("http://www.opengis.net/wms","LogoURL");
    /** @generated */
    public static final QName MaxHeight = 
        new QName("http://www.opengis.net/wms","MaxHeight");
    /** @generated */
    public static final QName MaxScaleDenominator = 
        new QName("http://www.opengis.net/wms","MaxScaleDenominator");
    /** @generated */
    public static final QName MaxWidth = 
        new QName("http://www.opengis.net/wms","MaxWidth");
    /** @generated */
    public static final QName MetadataURL = 
        new QName("http://www.opengis.net/wms","MetadataURL");
    /** @generated */
    public static final QName MinScaleDenominator = 
        new QName("http://www.opengis.net/wms","MinScaleDenominator");
    /** @generated */
    public static final QName Name = 
        new QName("http://www.opengis.net/wms","Name");
    /** @generated */
    public static final QName OnlineResource = 
        new QName("http://www.opengis.net/wms","OnlineResource");
    /** @generated */
    public static final QName Post = 
        new QName("http://www.opengis.net/wms","Post");
    /** @generated */
    public static final QName PostCode = 
        new QName("http://www.opengis.net/wms","PostCode");
    /** @generated */
    public static final QName Request = 
        new QName("http://www.opengis.net/wms","Request");
    /** @generated */
    public static final QName Service = 
        new QName("http://www.opengis.net/wms","Service");
    /** @generated */
    public static final QName StateOrProvince = 
        new QName("http://www.opengis.net/wms","StateOrProvince");
    /** @generated */
    public static final QName Style = 
        new QName("http://www.opengis.net/wms","Style");
    /** @generated */
    public static final QName StyleSheetURL = 
        new QName("http://www.opengis.net/wms","StyleSheetURL");
    /** @generated */
    public static final QName StyleURL = 
        new QName("http://www.opengis.net/wms","StyleURL");
    /** @generated */
    public static final QName Title = 
        new QName("http://www.opengis.net/wms","Title");
    /** @generated */
    public static final QName WMS_Capabilities = 
        new QName("http://www.opengis.net/wms","WMS_Capabilities");

    /* Attributes */

}
    
