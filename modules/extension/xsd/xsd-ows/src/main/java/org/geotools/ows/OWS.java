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
package org.geotools.ows;

import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xlink.XLINK;
import org.geotools.xml.XSD;


/**
 * This interface contains the qualified names of all the types,elements, and
 * attributes in the http://www.opengis.net/ows schema.
 *
 * @generated
 *
 * @source $URL$
 */
public final class OWS extends XSD {
    /** singleton instance */
    private static final OWS instance = new OWS();

    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/ows";

    /* Type Definitions */
    /** @generated */
    public static final QName AcceptFormatsType = new QName("http://www.opengis.net/ows",
            "AcceptFormatsType");

    /** @generated */
    public static final QName AcceptVersionsType = new QName("http://www.opengis.net/ows",
            "AcceptVersionsType");

    /** @generated */
    public static final QName AddressType = new QName("http://www.opengis.net/ows", "AddressType");

    /** @generated */
    public static final QName BoundingBoxType = new QName("http://www.opengis.net/ows",
            "BoundingBoxType");

    /** @generated */
    public static final QName CapabilitiesBaseType = new QName("http://www.opengis.net/ows",
            "CapabilitiesBaseType");

    /** @generated */
    public static final QName CodeType = new QName("http://www.opengis.net/ows", "CodeType");

    /** @generated */
    public static final QName ContactType = new QName("http://www.opengis.net/ows", "ContactType");

    /** @generated */
    public static final QName DescriptionType = new QName("http://www.opengis.net/ows",
            "DescriptionType");

    /** @generated */
    public static final QName DomainType = new QName("http://www.opengis.net/ows", "DomainType");

    /** @generated */
    public static final QName ExceptionType = new QName("http://www.opengis.net/ows",
            "ExceptionType");

    /** @generated */
    public static final QName GetCapabilitiesType = new QName("http://www.opengis.net/ows",
            "GetCapabilitiesType");

    /** @generated */
    public static final QName IdentificationType = new QName("http://www.opengis.net/ows",
            "IdentificationType");

    /** @generated */
    public static final QName KeywordsType = new QName("http://www.opengis.net/ows", "KeywordsType");

    /** @generated */
    public static final QName MetadataType = new QName("http://www.opengis.net/ows", "MetadataType");

    /** @generated */
    public static final QName MimeType = new QName("http://www.opengis.net/ows", "MimeType");

    /** @generated */
    public static final QName OnlineResourceType = new QName("http://www.opengis.net/ows",
            "OnlineResourceType");

    /** @generated */
    public static final QName PositionType = new QName("http://www.opengis.net/ows", "PositionType");

    /** @generated */
    public static final QName PositionType2D = new QName("http://www.opengis.net/ows",
            "PositionType2D");

    /** @generated */
    public static final QName RequestMethodType = new QName("http://www.opengis.net/ows",
            "RequestMethodType");

    /** @generated */
    public static final QName ResponsiblePartySubsetType = new QName("http://www.opengis.net/ows",
            "ResponsiblePartySubsetType");

    /** @generated */
    public static final QName ResponsiblePartyType = new QName("http://www.opengis.net/ows",
            "ResponsiblePartyType");

    /** @generated */
    public static final QName SectionsType = new QName("http://www.opengis.net/ows", "SectionsType");

    /** @generated */
    public static final QName ServiceType = new QName("http://www.opengis.net/ows", "ServiceType");

    /** @generated */
    public static final QName TelephoneType = new QName("http://www.opengis.net/ows",
            "TelephoneType");

    /** @generated */
    public static final QName UpdateSequenceType = new QName("http://www.opengis.net/ows",
            "UpdateSequenceType");

    /** @generated */
    public static final QName VersionType = new QName("http://www.opengis.net/ows", "VersionType");

    /** @generated */
    public static final QName WGS84BoundingBoxType = new QName("http://www.opengis.net/ows",
            "WGS84BoundingBoxType");

    /** @generated */
    public static final QName _DCP = new QName("http://www.opengis.net/ows", "_DCP");

    /** @generated */
    public static final QName _ExceptionReport = new QName("http://www.opengis.net/ows",
            "_ExceptionReport");

    /** @generated */
    public static final QName _HTTP = new QName("http://www.opengis.net/ows", "_HTTP");

    /** @generated */
    public static final QName _Operation = new QName("http://www.opengis.net/ows", "_Operation");

    /** @generated */
    public static final QName _OperationsMetadata = new QName("http://www.opengis.net/ows",
            "_OperationsMetadata");

    /** @generated */
    public static final QName _ServiceIdentification = new QName("http://www.opengis.net/ows",
            "_ServiceIdentification");

    /** @generated */
    public static final QName _ServiceProvider = new QName("http://www.opengis.net/ows",
            "_ServiceProvider");

    /* Elements */
    /** @generated */
    public static final QName Abstract = new QName("http://www.opengis.net/ows", "Abstract");

    /** @generated */
    public static final QName AbstractMetaData = new QName("http://www.opengis.net/ows",
            "AbstractMetaData");

    /** @generated */
    public static final QName AccessConstraints = new QName("http://www.opengis.net/ows",
            "AccessConstraints");

    /** @generated */
    public static final QName AvailableCRS = new QName("http://www.opengis.net/ows", "AvailableCRS");

    /** @generated */
    public static final QName BoundingBox = new QName("http://www.opengis.net/ows", "BoundingBox");

    /** @generated */
    public static final QName ContactInfo = new QName("http://www.opengis.net/ows", "ContactInfo");

    /** @generated */
    public static final QName DCP = new QName("http://www.opengis.net/ows", "DCP");

    /** @generated */
    public static final QName Exception = new QName("http://www.opengis.net/ows", "Exception");

    /** @generated */
    public static final QName ExceptionReport = new QName("http://www.opengis.net/ows",
            "ExceptionReport");

    /** @generated */
    public static final QName ExtendedCapabilities = new QName("http://www.opengis.net/ows",
            "ExtendedCapabilities");

    /** @generated */
    public static final QName Fees = new QName("http://www.opengis.net/ows", "Fees");

    /** @generated */
    public static final QName GetCapabilities = new QName("http://www.opengis.net/ows",
            "GetCapabilities");

    /** @generated */
    public static final QName HTTP = new QName("http://www.opengis.net/ows", "HTTP");

    /** @generated */
    public static final QName Identifier = new QName("http://www.opengis.net/ows", "Identifier");

    /** @generated */
    public static final QName IndividualName = new QName("http://www.opengis.net/ows",
            "IndividualName");

    /** @generated */
    public static final QName Keywords = new QName("http://www.opengis.net/ows", "Keywords");

    /** @generated */
    public static final QName Language = new QName("http://www.opengis.net/ows", "Language");

    /** @generated */
    public static final QName Metadata = new QName("http://www.opengis.net/ows", "Metadata");

    /** @generated */
    public static final QName Operation = new QName("http://www.opengis.net/ows", "Operation");

    /** @generated */
    public static final QName OperationsMetadata = new QName("http://www.opengis.net/ows",
            "OperationsMetadata");

    /** @generated */
    public static final QName OrganisationName = new QName("http://www.opengis.net/ows",
            "OrganisationName");

    /** @generated */
    public static final QName OutputFormat = new QName("http://www.opengis.net/ows", "OutputFormat");

    /** @generated */
    public static final QName PointOfContact = new QName("http://www.opengis.net/ows",
            "PointOfContact");

    /** @generated */
    public static final QName PositionName = new QName("http://www.opengis.net/ows", "PositionName");

    /** @generated */
    public static final QName Role = new QName("http://www.opengis.net/ows", "Role");

    /** @generated */
    public static final QName ServiceIdentification = new QName("http://www.opengis.net/ows",
            "ServiceIdentification");

    /** @generated */
    public static final QName ServiceProvider = new QName("http://www.opengis.net/ows",
            "ServiceProvider");

    /** @generated */
    public static final QName SupportedCRS = new QName("http://www.opengis.net/ows", "SupportedCRS");

    /** @generated */
    public static final QName Title = new QName("http://www.opengis.net/ows", "Title");

    /** @generated */
    public static final QName WGS84BoundingBox = new QName("http://www.opengis.net/ows",
            "WGS84BoundingBox");

    /**
     * private constructor
     */
    private OWS() {
    }

    /**
     * Returns the singleton instance.
     */
    public static final OWS getInstance() {
        return instance;
    }

    protected void addDependencies(Set dependencies) {
        dependencies.add(XLINK.getInstance());
        dependencies.add(OGC.getInstance());
    }

    /**
     * Returns 'http://www.opengis.net/ows'.
     */
    public String getNamespaceURI() {
        return NAMESPACE;
    }

    /**
     * Returns the location of 'owsAll.xsd.'.
     */
    public String getSchemaLocation() {
        return getClass().getResource("owsAll.xsd").toString();
    }

    /* Attributes */
}
