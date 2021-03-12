/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wps.v2_0;

import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.ows.v2_0.OWS;
import org.geotools.xsd.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and attributes in the
 * http://www.opengis.net/wps/2.0 schema.
 *
 * @generated
 */
public final class WPS extends XSD {

    /** singleton instance */
    private static final WPS instance = new WPS();

    /** Returns the singleton instance. */
    public static final WPS getInstance() {
        return instance;
    }

    /** private constructor */
    private WPS() {}

    @Override
    protected void addDependencies(Set<XSD> dependencies) {
        dependencies.add(OWS.getInstance());
    }

    /** Returns 'http://www.opengis.net/wps/2.0'. */
    @Override
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /** Returns the location of 'wps.xsd.'. */
    @Override
    public String getSchemaLocation() {
        return getClass().getResource("wps.xsd").toString();
    }

    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/wps/2.0";

    /* Type Definitions */
    /** @generated */
    public static final QName ComplexDataType =
            new QName("http://www.opengis.net/wps/2.0", "ComplexDataType");
    /** @generated */
    public static final QName DataDescriptionType =
            new QName("http://www.opengis.net/wps/2.0", "DataDescriptionType");
    /** @generated */
    public static final QName DataInputType =
            new QName("http://www.opengis.net/wps/2.0", "DataInputType");
    /** @generated */
    public static final QName DataOutputType =
            new QName("http://www.opengis.net/wps/2.0", "DataOutputType");
    /** @generated */
    public static final QName DataTransmissionModeType =
            new QName("http://www.opengis.net/wps/2.0", "DataTransmissionModeType");
    /** @generated */
    public static final QName DescriptionType =
            new QName("http://www.opengis.net/wps/2.0", "DescriptionType");
    /** @generated */
    public static final QName ExecuteRequestType =
            new QName("http://www.opengis.net/wps/2.0", "ExecuteRequestType");
    /** @generated */
    public static final QName GenericInputType =
            new QName("http://www.opengis.net/wps/2.0", "GenericInputType");
    /** @generated */
    public static final QName GenericOutputType =
            new QName("http://www.opengis.net/wps/2.0", "GenericOutputType");
    /** @generated */
    public static final QName GenericProcessType =
            new QName("http://www.opengis.net/wps/2.0", "GenericProcessType");
    /** @generated */
    public static final QName GetCapabilitiesType =
            new QName("http://www.opengis.net/wps/2.0", "GetCapabilitiesType");
    /** @generated */
    public static final QName InputDescriptionType =
            new QName("http://www.opengis.net/wps/2.0", "InputDescriptionType");
    /** @generated */
    public static final QName JobControlOptionsType =
            new QName("http://www.opengis.net/wps/2.0", "JobControlOptionsType");
    /** @generated */
    public static final QName LiteralDataDomainType =
            new QName("http://www.opengis.net/wps/2.0", "LiteralDataDomainType");
    /** @generated */
    public static final QName LiteralDataType =
            new QName("http://www.opengis.net/wps/2.0", "LiteralDataType");
    /** @generated */
    public static final QName OutputDefinitionType =
            new QName("http://www.opengis.net/wps/2.0", "OutputDefinitionType");
    /** @generated */
    public static final QName OutputDescriptionType =
            new QName("http://www.opengis.net/wps/2.0", "OutputDescriptionType");
    /** @generated */
    public static final QName ProcessDescriptionType =
            new QName("http://www.opengis.net/wps/2.0", "ProcessDescriptionType");
    /** @generated */
    public static final QName ProcessSummaryType =
            new QName("http://www.opengis.net/wps/2.0", "ProcessSummaryType");
    /** @generated */
    public static final QName ReferenceType =
            new QName("http://www.opengis.net/wps/2.0", "ReferenceType");
    /** @generated */
    public static final QName RequestBaseType =
            new QName("http://www.opengis.net/wps/2.0", "RequestBaseType");
    /** @generated */
    public static final QName WPSCapabilitiesType =
            new QName("http://www.opengis.net/wps/2.0", "WPSCapabilitiesType");
    /** @generated */
    public static final QName _BoundingBoxData =
            new QName("http://www.opengis.net/wps/2.0", "_BoundingBoxData");
    /** @generated */
    public static final QName _Contents = new QName("http://www.opengis.net/wps/2.0", "_Contents");
    /** @generated */
    public static final QName _Data = new QName("http://www.opengis.net/wps/2.0", "_Data");
    /** @generated */
    public static final QName _DescribeProcess =
            new QName("http://www.opengis.net/wps/2.0", "_DescribeProcess");
    /** @generated */
    public static final QName _Dismiss = new QName("http://www.opengis.net/wps/2.0", "_Dismiss");
    /** @generated */
    public static final QName _Format = new QName("http://www.opengis.net/wps/2.0", "_Format");
    /** @generated */
    public static final QName _GetResult =
            new QName("http://www.opengis.net/wps/2.0", "_GetResult");
    /** @generated */
    public static final QName _GetStatus =
            new QName("http://www.opengis.net/wps/2.0", "_GetStatus");
    /** @generated */
    public static final QName _LiteralValue =
            new QName("http://www.opengis.net/wps/2.0", "_LiteralValue");
    /** @generated */
    public static final QName _ProcessOffering =
            new QName("http://www.opengis.net/wps/2.0", "_ProcessOffering");
    /** @generated */
    public static final QName _ProcessOfferings =
            new QName("http://www.opengis.net/wps/2.0", "_ProcessOfferings");
    /** @generated */
    public static final QName _Result = new QName("http://www.opengis.net/wps/2.0", "_Result");
    /** @generated */
    public static final QName _StatusInfo =
            new QName("http://www.opengis.net/wps/2.0", "_StatusInfo");
    /** @generated */
    public static final QName _SupportedCRS =
            new QName("http://www.opengis.net/wps/2.0", "_SupportedCRS");
    /** @generated */
    public static final QName LiteralDataType_LiteralDataDomain =
            new QName("http://www.opengis.net/wps/2.0", "LiteralDataType_LiteralDataDomain");
    /** @generated */
    public static final QName ReferenceType_BodyReference =
            new QName("http://www.opengis.net/wps/2.0", "ReferenceType_BodyReference");
    /** @generated */
    public static final QName WPSCapabilitiesType_Extension =
            new QName("http://www.opengis.net/wps/2.0", "WPSCapabilitiesType_Extension");

    /* Elements */
    /** @generated */
    public static final QName BoundingBoxData =
            new QName("http://www.opengis.net/wps/2.0", "BoundingBoxData");
    /** @generated */
    public static final QName Capabilities =
            new QName("http://www.opengis.net/wps/2.0", "Capabilities");
    /** @generated */
    public static final QName ComplexData =
            new QName("http://www.opengis.net/wps/2.0", "ComplexData");
    /** @generated */
    public static final QName Contents = new QName("http://www.opengis.net/wps/2.0", "Contents");
    /** @generated */
    public static final QName Data = new QName("http://www.opengis.net/wps/2.0", "Data");
    /** @generated */
    public static final QName DataDescription =
            new QName("http://www.opengis.net/wps/2.0", "DataDescription");
    /** @generated */
    public static final QName DescribeProcess =
            new QName("http://www.opengis.net/wps/2.0", "DescribeProcess");
    /** @generated */
    public static final QName Dismiss = new QName("http://www.opengis.net/wps/2.0", "Dismiss");
    /** @generated */
    public static final QName Execute = new QName("http://www.opengis.net/wps/2.0", "Execute");
    /** @generated */
    public static final QName ExpirationDate =
            new QName("http://www.opengis.net/wps/2.0", "ExpirationDate");
    /** @generated */
    public static final QName Format = new QName("http://www.opengis.net/wps/2.0", "Format");
    /** @generated */
    public static final QName GenericProcess =
            new QName("http://www.opengis.net/wps/2.0", "GenericProcess");
    /** @generated */
    public static final QName GetCapabilities =
            new QName("http://www.opengis.net/wps/2.0", "GetCapabilities");
    /** @generated */
    public static final QName GetResult = new QName("http://www.opengis.net/wps/2.0", "GetResult");
    /** @generated */
    public static final QName GetStatus = new QName("http://www.opengis.net/wps/2.0", "GetStatus");
    /** @generated */
    public static final QName JobID = new QName("http://www.opengis.net/wps/2.0", "JobID");
    /** @generated */
    public static final QName LiteralData =
            new QName("http://www.opengis.net/wps/2.0", "LiteralData");
    /** @generated */
    public static final QName LiteralValue =
            new QName("http://www.opengis.net/wps/2.0", "LiteralValue");
    /** @generated */
    public static final QName Process = new QName("http://www.opengis.net/wps/2.0", "Process");
    /** @generated */
    public static final QName ProcessOffering =
            new QName("http://www.opengis.net/wps/2.0", "ProcessOffering");
    /** @generated */
    public static final QName ProcessOfferings =
            new QName("http://www.opengis.net/wps/2.0", "ProcessOfferings");
    /** @generated */
    public static final QName Reference = new QName("http://www.opengis.net/wps/2.0", "Reference");
    /** @generated */
    public static final QName Result = new QName("http://www.opengis.net/wps/2.0", "Result");
    /** @generated */
    public static final QName StatusInfo =
            new QName("http://www.opengis.net/wps/2.0", "StatusInfo");
    /** @generated */
    public static final QName SupportedCRS =
            new QName("http://www.opengis.net/wps/2.0", "SupportedCRS");

    /* Attributes */

}
