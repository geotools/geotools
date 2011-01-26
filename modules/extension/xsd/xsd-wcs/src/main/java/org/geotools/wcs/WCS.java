package org.geotools.wcs;

import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.gml4wcs.GML;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and
 * attributes in the http://www.opengis.net/wcs schema.
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
        super.addDependencies(dependencies);

        dependencies.add(GML.getInstance());
    }

    /**
     * Returns 'http://www.opengis.net/wcs'.
     */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /**
     * Returns the location of 'getCoverage.xsd.'.
     */
    public String getSchemaLocation() {
        return getClass().getResource("getCoverage.xsd").toString();
    }

    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/wcs";

    /* Type Definitions */
    /** @generated */
    public static final QName AbstractDescriptionBaseType = new QName(
            "http://www.opengis.net/wcs", "AbstractDescriptionBaseType");

    /** @generated */
    public static final QName AbstractDescriptionType = new QName(
            "http://www.opengis.net/wcs", "AbstractDescriptionType");

    /** @generated */
    public static final QName AddressType = new QName(
            "http://www.opengis.net/wcs", "AddressType");

//    /** @generated */
//    public static final QName AxisSubsetType = new QName(
//            "http://www.opengis.net/wcs", "AxisSubsetType");

    /** @generated */
    public static final QName AxisDescriptionType = new QName(
            "http://www.opengis.net/wcs", "AxisDescriptionType");

    /** @generated */
    public static final QName CapabilitiesSectionType = new QName(
            "http://www.opengis.net/wcs", "CapabilitiesSectionType");

    /** @generated */
    public static final QName ContactType = new QName(
            "http://www.opengis.net/wcs", "ContactType");

    /** @generated */
    public static final QName CoverageOfferingBriefType = new QName(
            "http://www.opengis.net/wcs", "CoverageOfferingBriefType");

    /** @generated */
    public static final QName CoverageOfferingType = new QName(
            "http://www.opengis.net/wcs", "CoverageOfferingType");

    /** @generated */
    public static final QName DCPTypeType = new QName(
            "http://www.opengis.net/wcs", "DCPTypeType");

    /** @generated */
    public static final QName DomainSetType = new QName(
            "http://www.opengis.net/wcs", "DomainSetType");

    /** @generated */
    public static final QName DomainSubsetType = new QName(
            "http://www.opengis.net/wcs", "DomainSubsetType");

    /** @generated */
    public static final QName InterpolationMethodType = new QName(
            "http://www.opengis.net/wcs", "InterpolationMethodType");

    /** @generated */
    public static final QName intervalType = new QName(
            "http://www.opengis.net/wcs", "intervalType");

    /** @generated */
    public static final QName LonLatEnvelopeBaseType = new QName(
            "http://www.opengis.net/wcs", "LonLatEnvelopeBaseType");

    /** @generated */
    public static final QName LonLatEnvelopeType = new QName(
            "http://www.opengis.net/wcs", "LonLatEnvelopeType");

    /** @generated */
    public static final QName MetadataAssociationType = new QName(
            "http://www.opengis.net/wcs", "MetadataAssociationType");

    /** @generated */
    public static final QName MetadataLinkType = new QName(
            "http://www.opengis.net/wcs", "MetadataLinkType");

    /** @generated */
    public static final QName OnlineResourceType = new QName(
            "http://www.opengis.net/wcs", "OnlineResourceType");

    /** @generated */
    public static final QName OutputType = new QName(
            "http://www.opengis.net/wcs", "OutputType");

    /** @generated */
    public static final QName RangeSetType = new QName(
            "http://www.opengis.net/wcs", "RangeSetType");

    /** @generated */
    public static final QName RangeSubsetType = new QName(
            "http://www.opengis.net/wcs", "RangeSubsetType");

    /** @generated */
    public static final QName ResponsiblePartyType = new QName(
            "http://www.opengis.net/wcs", "ResponsiblePartyType");

    /** @generated */
    public static final QName ServiceType = new QName(
            "http://www.opengis.net/wcs", "ServiceType");

    /** @generated */
    public static final QName SpatialDomainType = new QName(
            "http://www.opengis.net/wcs", "SpatialDomainType");

    /** @generated */
    public static final QName SpatialSubsetType = new QName(
            "http://www.opengis.net/wcs", "SpatialSubsetType");

    /** @generated */
    public static final QName SupportedCRSsType = new QName(
            "http://www.opengis.net/wcs", "SupportedCRSsType");

    /** @generated */
    public static final QName SupportedFormatsType = new QName(
            "http://www.opengis.net/wcs", "SupportedFormatsType");

    /** @generated */
    public static final QName SupportedInterpolationsType = new QName(
            "http://www.opengis.net/wcs", "SupportedInterpolationsType");

    /** @generated */
    public static final QName TelephoneType = new QName(
            "http://www.opengis.net/wcs", "TelephoneType");

    /** @generated */
    public static final QName TimePeriodType = new QName(
            "http://www.opengis.net/wcs", "TimePeriodType");

    /** @generated */
    public static final QName TimeSequenceType = new QName(
            "http://www.opengis.net/wcs", "TimeSequenceType");

    /** @generated */
    public static final QName TypedLiteralType = new QName(
            "http://www.opengis.net/wcs", "TypedLiteralType");

    /** @generated */
    public static final QName valueEnumBaseType = new QName(
            "http://www.opengis.net/wcs", "valueEnumBaseType");

    /** @generated */
    public static final QName valueEnumType = new QName(
            "http://www.opengis.net/wcs", "valueEnumType");

    /** @generated */
    public static final QName valueRangeType = new QName(
            "http://www.opengis.net/wcs", "valueRangeType");

    /** @generated */
    public static final QName WCS_CapabilitiesType = new QName(
            "http://www.opengis.net/wcs", "WCS_CapabilitiesType");

    /** @generated */
    public static final QName WCSCapabilityType = new QName(
            "http://www.opengis.net/wcs", "WCSCapabilityType");

    /** @generated */
    public static final QName _axisDescription = new QName(
            "http://www.opengis.net/wcs", "_axisDescription");

    /** @generated */
    public static final QName _ContentMetadata = new QName(
            "http://www.opengis.net/wcs", "_ContentMetadata");

    /** @generated */
    public static final QName _CoverageDescription = new QName(
            "http://www.opengis.net/wcs", "_CoverageDescription");

    /** @generated */
    public static final QName _DescribeCoverage = new QName(
            "http://www.opengis.net/wcs", "_DescribeCoverage");

    /** @generated */
    public static final QName _GetCapabilities = new QName(
            "http://www.opengis.net/wcs", "_GetCapabilities");

    /** @generated */
    public static final QName _GetCoverage = new QName(
            "http://www.opengis.net/wcs", "_GetCoverage");

    /** @generated */
    public static final QName _keywords = new QName(
            "http://www.opengis.net/wcs", "_keywords");

    /** @generated */
    public static final QName _rangeSet = new QName(
            "http://www.opengis.net/wcs", "_rangeSet");

    /** @generated */
    public static final QName AxisDescriptionType_values = new QName(
            "http://www.opengis.net/wcs", "AxisDescriptionType_values");

    /** @generated */
    public static final QName DCPTypeType_HTTP = new QName(
            "http://www.opengis.net/wcs", "DCPTypeType_HTTP");

    /** @generated */
    public static final QName RangeSubsetType_axisSubset = new QName(
            "http://www.opengis.net/wcs", "RangeSubsetType_axisSubset");

    /** @generated */
    public static final QName WCSCapabilityType_Request = new QName(
            "http://www.opengis.net/wcs", "WCSCapabilityType_Request");

    /** @generated */
    public static final QName WCSCapabilityType_Exception = new QName(
            "http://www.opengis.net/wcs", "WCSCapabilityType_Exception");

    /** @generated */
    public static final QName WCSCapabilityType_VendorSpecificCapabilities = new QName(
            "http://www.opengis.net/wcs",
            "WCSCapabilityType_VendorSpecificCapabilities");

    /* Elements */
    /** @generated */
    public static final QName axisDescription = new QName(
            "http://www.opengis.net/wcs", "axisDescription");

    /** @generated */
    public static final QName AxisDescription = new QName(
            "http://www.opengis.net/wcs", "AxisDescription");

    /** @generated */
    public static final QName Capability = new QName(
            "http://www.opengis.net/wcs", "Capability");

    /** @generated */
    public static final QName ContentMetadata = new QName(
            "http://www.opengis.net/wcs", "ContentMetadata");

    /** @generated */
    public static final QName CoverageDescription = new QName(
            "http://www.opengis.net/wcs", "CoverageDescription");

    /** @generated */
    public static final QName CoverageOffering = new QName(
            "http://www.opengis.net/wcs", "CoverageOffering");

    /** @generated */
    public static final QName CoverageOfferingBrief = new QName(
            "http://www.opengis.net/wcs", "CoverageOfferingBrief");

    /** @generated */
    public static final QName DescribeCoverage = new QName(
            "http://www.opengis.net/wcs", "DescribeCoverage");

    /** @generated */
    public static final QName description = new QName(
            "http://www.opengis.net/wcs", "description");

    /** @generated */
    public static final QName domainSet = new QName(
            "http://www.opengis.net/wcs", "domainSet");

    /** @generated */
    public static final QName formats = new QName("http://www.opengis.net/wcs",
            "formats");

    /** @generated */
    public static final QName GetCapabilities = new QName(
            "http://www.opengis.net/wcs", "GetCapabilities");

    /** @generated */
    public static final QName GetCoverage = new QName(
            "http://www.opengis.net/wcs", "GetCoverage");

    /** @generated */
    public static final QName interpolationMethod = new QName(
            "http://www.opengis.net/wcs", "interpolationMethod");

    /** @generated */
    public static final QName interval = new QName(
            "http://www.opengis.net/wcs", "interval");

    /** @generated */
    public static final QName keywords = new QName(
            "http://www.opengis.net/wcs", "keywords");

    /** @generated */
    public static final QName lonLatEnvelope = new QName(
            "http://www.opengis.net/wcs", "lonLatEnvelope");

    /** @generated */
    public static final QName metadataLink = new QName(
            "http://www.opengis.net/wcs", "metadataLink");

    /** @generated */
    public static final QName name = new QName("http://www.opengis.net/wcs",
            "name");

    /** @generated */
    public static final QName rangeSet = new QName(
            "http://www.opengis.net/wcs", "rangeSet");

    /** @generated */
    public static final QName RangeSet = new QName(
            "http://www.opengis.net/wcs", "RangeSet");

    /** @generated */
    public static final QName Service = new QName("http://www.opengis.net/wcs",
            "Service");

    /** @generated */
    public static final QName singleValue = new QName(
            "http://www.opengis.net/wcs", "singleValue");

    /** @generated */
    public static final QName spatialDomain = new QName(
            "http://www.opengis.net/wcs", "spatialDomain");

    /** @generated */
    public static final QName spatialSubset = new QName(
            "http://www.opengis.net/wcs", "spatialSubset");

    /** @generated */
    public static final QName supportedCRSs = new QName(
            "http://www.opengis.net/wcs", "supportedCRSs");

    /** @generated */
    public static final QName supportedFormats = new QName(
            "http://www.opengis.net/wcs", "supportedFormats");

    /** @generated */
    public static final QName supportedInterpolations = new QName(
            "http://www.opengis.net/wcs", "supportedInterpolations");

    /** @generated */
    public static final QName temporalDomain = new QName(
            "http://www.opengis.net/wcs", "temporalDomain");

    /** @generated */
    public static final QName temporalSubset = new QName(
            "http://www.opengis.net/wcs", "temporalSubset");

    /** @generated */
    public static final QName timePeriod = new QName(
            "http://www.opengis.net/wcs", "timePeriod");

    /** @generated */
    public static final QName TimeSequence = new QName(
            "http://www.opengis.net/wcs", "TimeSequence");

    /** @generated */
    public static final QName WCS_Capabilities = new QName(
            "http://www.opengis.net/wcs", "WCS_Capabilities");

    /* Attributes */
    /** @generated */
    public static final QName closure = new QName("http://www.opengis.net/wcs",
            "closure");

    /** @generated */
    public static final QName semantic = new QName(
            "http://www.opengis.net/wcs", "semantic");

    /** @generated */
    public static final QName type = new QName("http://www.opengis.net/wcs",
            "type");

}
