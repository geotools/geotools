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
    
    /* Type Definitions */
    /** @generated */
    public static final QName CapabilitiesType = 
        new QName("http://www.opengis.net/wcs/2.0","CapabilitiesType");
    /** @generated */
    public static final QName ContentsType = 
        new QName("http://www.opengis.net/wcs/2.0","ContentsType");
    /** @generated */
    public static final QName CoverageDescriptionsType = 
        new QName("http://www.opengis.net/wcs/2.0","CoverageDescriptionsType");
    /** @generated */
    public static final QName CoverageDescriptionType = 
        new QName("http://www.opengis.net/wcs/2.0","CoverageDescriptionType");
    /** @generated */
    public static final QName CoverageOfferingsType = 
        new QName("http://www.opengis.net/wcs/2.0","CoverageOfferingsType");
    /** @generated */
    public static final QName CoverageSubtypeParentType = 
        new QName("http://www.opengis.net/wcs/2.0","CoverageSubtypeParentType");
    /** @generated */
    public static final QName CoverageSummaryType = 
        new QName("http://www.opengis.net/wcs/2.0","CoverageSummaryType");
    /** @generated */
    public static final QName DescribeCoverageType = 
        new QName("http://www.opengis.net/wcs/2.0","DescribeCoverageType");
    /** @generated */
    public static final QName DimensionSliceType = 
        new QName("http://www.opengis.net/wcs/2.0","DimensionSliceType");
    /** @generated */
    public static final QName DimensionSubsetType = 
        new QName("http://www.opengis.net/wcs/2.0","DimensionSubsetType");
    /** @generated */
    public static final QName DimensionTrimType = 
        new QName("http://www.opengis.net/wcs/2.0","DimensionTrimType");
    /** @generated */
    public static final QName ExtensionType = 
        new QName("http://www.opengis.net/wcs/2.0","ExtensionType");
    /** @generated */
    public static final QName GetCapabilitiesType = 
        new QName("http://www.opengis.net/wcs/2.0","GetCapabilitiesType");
    /** @generated */
    public static final QName GetCoverageType = 
        new QName("http://www.opengis.net/wcs/2.0","GetCoverageType");
    /** @generated */
    public static final QName OfferedCoverageType = 
        new QName("http://www.opengis.net/wcs/2.0","OfferedCoverageType");
    /** @generated */
    public static final QName RequestBaseType = 
        new QName("http://www.opengis.net/wcs/2.0","RequestBaseType");
    /** @generated */
    public static final QName ServiceMetadataType = 
        new QName("http://www.opengis.net/wcs/2.0","ServiceMetadataType");
    /** @generated */
    public static final QName ServiceParametersType = 
        new QName("http://www.opengis.net/wcs/2.0","ServiceParametersType");
    /** @generated */
    public static final QName VersionStringType = 
        new QName("http://www.opengis.net/wcs/2.0","VersionStringType");

    /* Elements */
    /** @generated */
    public static final QName Capabilities = 
        new QName("http://www.opengis.net/wcs/2.0","Capabilities");
    /** @generated */
    public static final QName Contents = 
        new QName("http://www.opengis.net/wcs/2.0","Contents");
    /** @generated */
    public static final QName CoverageDescription = 
        new QName("http://www.opengis.net/wcs/2.0","CoverageDescription");
    /** @generated */
    public static final QName CoverageDescriptions = 
        new QName("http://www.opengis.net/wcs/2.0","CoverageDescriptions");
    /** @generated */
    public static final QName CoverageId = 
        new QName("http://www.opengis.net/wcs/2.0","CoverageId");
    /** @generated */
    public static final QName CoverageOfferings = 
        new QName("http://www.opengis.net/wcs/2.0","CoverageOfferings");
    /** @generated */
    public static final QName CoverageSubtype = 
        new QName("http://www.opengis.net/wcs/2.0","CoverageSubtype");
    /** @generated */
    public static final QName CoverageSubtypeParent = 
        new QName("http://www.opengis.net/wcs/2.0","CoverageSubtypeParent");
    /** @generated */
    public static final QName CoverageSummary = 
        new QName("http://www.opengis.net/wcs/2.0","CoverageSummary");
    /** @generated */
    public static final QName DescribeCoverage = 
        new QName("http://www.opengis.net/wcs/2.0","DescribeCoverage");
    /** @generated */
    public static final QName DimensionSlice = 
        new QName("http://www.opengis.net/wcs/2.0","DimensionSlice");
    /** @generated */
    public static final QName DimensionSubset = 
        new QName("http://www.opengis.net/wcs/2.0","DimensionSubset");
    /** @generated */
    public static final QName DimensionTrim = 
        new QName("http://www.opengis.net/wcs/2.0","DimensionTrim");
    /** @generated */
    public static final QName Extension = 
        new QName("http://www.opengis.net/wcs/2.0","Extension");
    /** @generated */
    public static final QName GetCapabilities = 
        new QName("http://www.opengis.net/wcs/2.0","GetCapabilities");
    /** @generated */
    public static final QName GetCoverage = 
        new QName("http://www.opengis.net/wcs/2.0","GetCoverage");
    /** @generated */
    public static final QName OfferedCoverage = 
        new QName("http://www.opengis.net/wcs/2.0","OfferedCoverage");
    /** @generated */
    public static final QName ServiceMetadata = 
        new QName("http://www.opengis.net/wcs/2.0","ServiceMetadata");
    /** @generated */
    public static final QName ServiceParameters = 
        new QName("http://www.opengis.net/wcs/2.0","ServiceParameters");

    /* Attributes */

}
    