/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wcs.v2_0;


import java.util.Set;
import javax.xml.namespace.QName;

import org.geotools.ows.v2_0.OWS;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.opengis.net/wcs/2.0 schema.
 *
 * @generated
 */
public final class WCS extends XSD {

    /** singleton instance */
    private static final WCS instance = new WCS();
    
    /**
     * Returns the singleton instance.
     */
    public static final WCS getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private WCS() {
    }
    
    protected void addDependencies(Set dependencies) {
       dependencies.add(OWS.getInstance());
    }
    
    /**
     * Returns 'http://www.opengis.net/wcs/2.0'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'wcsAll.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("wcsAll.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/wcs/2.0";
    public static final String NAMESPACE_RANGESUBSET = "http://www.opengis.net/wcs/range-subsetting/1.0";
    
    /* Type Definitions */
    /** @generated */
    public static final QName CapabilitiesType = 
        new QName(NAMESPACE,"CapabilitiesType");
    /** @generated */
    public static final QName ContentsType = 
        new QName(NAMESPACE,"ContentsType");
    /** @generated */
    public static final QName CoverageDescriptionsType = 
        new QName(NAMESPACE,"CoverageDescriptionsType");
    /** @generated */
    public static final QName CoverageDescriptionType = 
        new QName(NAMESPACE,"CoverageDescriptionType");
    /** @generated */
    public static final QName CoverageOfferingsType = 
        new QName(NAMESPACE,"CoverageOfferingsType");
    /** @generated */
    public static final QName CoverageSubtypeParentType = 
        new QName(NAMESPACE,"CoverageSubtypeParentType");
    /** @generated */
    public static final QName CoverageSummaryType = 
        new QName(NAMESPACE,"CoverageSummaryType");
    /** @generated */
    public static final QName DescribeCoverageType = 
        new QName(NAMESPACE,"DescribeCoverageType");
    /** @generated */
    public static final QName DimensionSliceType = 
        new QName(NAMESPACE,"DimensionSliceType");
    /** @generated */
    public static final QName DimensionSubsetType = 
        new QName(NAMESPACE,"DimensionSubsetType");
    /** @generated */
    public static final QName DimensionTrimType = 
        new QName(NAMESPACE,"DimensionTrimType");
    /** @generated */
    public static final QName ExtensionType = 
        new QName(NAMESPACE,"ExtensionType");
    /** @generated */
    public static final QName GetCapabilitiesType = 
        new QName(NAMESPACE,"GetCapabilitiesType");
    /** @generated */
    public static final QName GetCoverageType = 
        new QName(NAMESPACE,"GetCoverageType");
    /** @generated */
    public static final QName OfferedCoverageType = 
        new QName(NAMESPACE,"OfferedCoverageType");
    /** @generated */
    public static final QName RequestBaseType = 
        new QName(NAMESPACE,"RequestBaseType");
    /** @generated */
    public static final QName ServiceMetadataType = 
        new QName(NAMESPACE,"ServiceMetadataType");
    /** @generated */
    public static final QName ServiceParametersType = 
        new QName(NAMESPACE,"ServiceParametersType");
    /** @generated */
    public static final QName VersionStringType = 
        new QName(NAMESPACE,"VersionStringType");

    /* Elements */
    /** @generated */
    public static final QName Capabilities = 
        new QName(NAMESPACE,"Capabilities");
    /** @generated */
    public static final QName Contents = 
        new QName(NAMESPACE,"Contents");
    /** @generated */
    public static final QName CoverageDescription = 
        new QName(NAMESPACE,"CoverageDescription");
    /** @generated */
    public static final QName CoverageDescriptions = 
        new QName(NAMESPACE,"CoverageDescriptions");
    /** @generated */
    public static final QName CoverageId = 
        new QName(NAMESPACE,"CoverageId");
    /** @generated */
    public static final QName CoverageOfferings = 
        new QName(NAMESPACE,"CoverageOfferings");
    /** @generated */
    public static final QName CoverageSubtype = 
        new QName(NAMESPACE,"CoverageSubtype");
    /** @generated */
    public static final QName CoverageSubtypeParent = 
        new QName(NAMESPACE,"CoverageSubtypeParent");
    /** @generated */
    public static final QName CoverageSummary = 
        new QName(NAMESPACE,"CoverageSummary");
    /** @generated */
    public static final QName DescribeCoverage = 
        new QName(NAMESPACE,"DescribeCoverage");
    /** @generated */
    public static final QName DimensionSlice = 
        new QName(NAMESPACE,"DimensionSlice");
    /** @generated */
    public static final QName DimensionSubset = 
        new QName(NAMESPACE,"DimensionSubset");
    /** @generated */
    public static final QName DimensionTrim = 
        new QName(NAMESPACE,"DimensionTrim");
    /** @generated */
    public static final QName Extension = 
        new QName(NAMESPACE,"Extension");
    /** @generated */
    public static final QName GetCapabilities = 
        new QName(NAMESPACE,"GetCapabilities");
    /** @generated */
    public static final QName GetCoverage = 
        new QName(NAMESPACE,"GetCoverage");
    /** @generated */
    public static final QName OfferedCoverage = 
        new QName(NAMESPACE,"OfferedCoverage");
    /** @generated */
    public static final QName ServiceMetadata = 
        new QName(NAMESPACE,"ServiceMetadata");
    /** @generated */
    public static final QName ServiceParameters = 
        new QName(NAMESPACE,"ServiceParameters");
    

}
    