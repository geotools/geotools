/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.v1_1;


import java.util.Set;
import javax.xml.namespace.QName;

import org.geotools.xlink.XLINK;
import org.geotools.xml.XML;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.opengis.net/ows/1.1 schema.
 *
 * @generated
 *
 * @source $URL$
 */
public final class OWS extends XSD {

    /** singleton instance */
    private static final OWS instance = new OWS();
    
    /**
     * Returns the singleton instance.
     */
    public static final OWS getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private OWS() {
    }
    
    protected void addDependencies(Set dependencies) {
        dependencies.add( XML.getInstance() );
        dependencies.add( XLINK.getInstance() );
    }
    
    /**
     * Returns 'http://www.opengis.net/ows/1.1'.
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
    
    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/ows/1.1";
    
    /* Type Definitions */
    /** @generated */
    public static final QName AbstractReferenceBaseType = 
        new QName("http://www.opengis.net/ows/1.1","AbstractReferenceBaseType");
    /** @generated */
    public static final QName AcceptFormatsType = 
        new QName("http://www.opengis.net/ows/1.1","AcceptFormatsType");
    /** @generated */
    public static final QName AcceptVersionsType = 
        new QName("http://www.opengis.net/ows/1.1","AcceptVersionsType");
    /** @generated */
    public static final QName AddressType = 
        new QName("http://www.opengis.net/ows/1.1","AddressType");
    /** @generated */
    public static final QName BasicIdentificationType = 
        new QName("http://www.opengis.net/ows/1.1","BasicIdentificationType");
    /** @generated */
    public static final QName BoundingBoxType = 
        new QName("http://www.opengis.net/ows/1.1","BoundingBoxType");
    /** @generated */
    public static final QName CapabilitiesBaseType = 
        new QName("http://www.opengis.net/ows/1.1","CapabilitiesBaseType");
    /** @generated */
    public static final QName CodeType = 
        new QName("http://www.opengis.net/ows/1.1","CodeType");
    /** @generated */
    public static final QName ContactType = 
        new QName("http://www.opengis.net/ows/1.1","ContactType");
    /** @generated */
    public static final QName ContentsBaseType = 
        new QName("http://www.opengis.net/ows/1.1","ContentsBaseType");
    /** @generated */
    public static final QName DatasetDescriptionSummaryBaseType = 
        new QName("http://www.opengis.net/ows/1.1","DatasetDescriptionSummaryBaseType");
    /** @generated */
    public static final QName DescriptionType = 
        new QName("http://www.opengis.net/ows/1.1","DescriptionType");
    /** @generated */
    public static final QName DomainMetadataType = 
        new QName("http://www.opengis.net/ows/1.1","DomainMetadataType");
    /** @generated */
    public static final QName DomainType = 
        new QName("http://www.opengis.net/ows/1.1","DomainType");
    /** @generated */
    public static final QName ExceptionType = 
        new QName("http://www.opengis.net/ows/1.1","ExceptionType");
    /** @generated */
    public static final QName GetCapabilitiesType = 
        new QName("http://www.opengis.net/ows/1.1","GetCapabilitiesType");
    /** @generated */
    public static final QName GetResourceByIdType = 
        new QName("http://www.opengis.net/ows/1.1","GetResourceByIdType");
    /** @generated */
    public static final QName IdentificationType = 
        new QName("http://www.opengis.net/ows/1.1","IdentificationType");
    /** @generated */
    public static final QName KeywordsType = 
        new QName("http://www.opengis.net/ows/1.1","KeywordsType");
    /** @generated */
    public static final QName LanguageStringType = 
        new QName("http://www.opengis.net/ows/1.1","LanguageStringType");
    /** @generated */
    public static final QName ManifestType = 
        new QName("http://www.opengis.net/ows/1.1","ManifestType");
    /** @generated */
    public static final QName MetadataType = 
        new QName("http://www.opengis.net/ows/1.1","MetadataType");
    /** @generated */
    public static final QName MimeType = 
        new QName("http://www.opengis.net/ows/1.1","MimeType");
    /** @generated */
    public static final QName OnlineResourceType = 
        new QName("http://www.opengis.net/ows/1.1","OnlineResourceType");
    /** @generated */
    public static final QName PositionType = 
        new QName("http://www.opengis.net/ows/1.1","PositionType");
    /** @generated */
    public static final QName PositionType2D = 
        new QName("http://www.opengis.net/ows/1.1","PositionType2D");
    /** @generated */
    public static final QName RangeType = 
        new QName("http://www.opengis.net/ows/1.1","RangeType");
    /** @generated */
    public static final QName ReferenceGroupType = 
        new QName("http://www.opengis.net/ows/1.1","ReferenceGroupType");
    /** @generated */
    public static final QName ReferenceType = 
        new QName("http://www.opengis.net/ows/1.1","ReferenceType");
    /** @generated */
    public static final QName RequestMethodType = 
        new QName("http://www.opengis.net/ows/1.1","RequestMethodType");
    /** @generated */
    public static final QName ResponsiblePartySubsetType = 
        new QName("http://www.opengis.net/ows/1.1","ResponsiblePartySubsetType");
    /** @generated */
    public static final QName ResponsiblePartyType = 
        new QName("http://www.opengis.net/ows/1.1","ResponsiblePartyType");
    /** @generated */
    public static final QName SectionsType = 
        new QName("http://www.opengis.net/ows/1.1","SectionsType");
    /** @generated */
    public static final QName ServiceReferenceType = 
        new QName("http://www.opengis.net/ows/1.1","ServiceReferenceType");
    /** @generated */
    public static final QName ServiceType = 
        new QName("http://www.opengis.net/ows/1.1","ServiceType");
    /** @generated */
    public static final QName TelephoneType = 
        new QName("http://www.opengis.net/ows/1.1","TelephoneType");
    /** @generated */
    public static final QName UnNamedDomainType = 
        new QName("http://www.opengis.net/ows/1.1","UnNamedDomainType");
    /** @generated */
    public static final QName UpdateSequenceType = 
        new QName("http://www.opengis.net/ows/1.1","UpdateSequenceType");
    /** @generated */
    public static final QName ValueType = 
        new QName("http://www.opengis.net/ows/1.1","ValueType");
    /** @generated */
    public static final QName VersionType = 
        new QName("http://www.opengis.net/ows/1.1","VersionType");
    /** @generated */
    public static final QName WGS84BoundingBoxType = 
        new QName("http://www.opengis.net/ows/1.1","WGS84BoundingBoxType");
    /** @generated */
    public static final QName _AllowedValues = 
        new QName("http://www.opengis.net/ows/1.1","_AllowedValues");
    /** @generated */
    public static final QName _AnyValue = 
        new QName("http://www.opengis.net/ows/1.1","_AnyValue");
    /** @generated */
    public static final QName _DCP = 
        new QName("http://www.opengis.net/ows/1.1","_DCP");
    /** @generated */
    public static final QName _ExceptionReport = 
        new QName("http://www.opengis.net/ows/1.1","_ExceptionReport");
    /** @generated */
    public static final QName _HTTP = 
        new QName("http://www.opengis.net/ows/1.1","_HTTP");
    /** @generated */
    public static final QName _NoValues = 
        new QName("http://www.opengis.net/ows/1.1","_NoValues");
    /** @generated */
    public static final QName _Operation = 
        new QName("http://www.opengis.net/ows/1.1","_Operation");
    /** @generated */
    public static final QName _OperationsMetadata = 
        new QName("http://www.opengis.net/ows/1.1","_OperationsMetadata");
    /** @generated */
    public static final QName _ServiceIdentification = 
        new QName("http://www.opengis.net/ows/1.1","_ServiceIdentification");
    /** @generated */
    public static final QName _ServiceProvider = 
        new QName("http://www.opengis.net/ows/1.1","_ServiceProvider");
    /** @generated */
    public static final QName _ValuesReference = 
        new QName("http://www.opengis.net/ows/1.1","_ValuesReference");

    /* Elements */
    /** @generated */
    public static final QName Abstract = 
        new QName("http://www.opengis.net/ows/1.1","Abstract");
    /** @generated */
    public static final QName AbstractMetaData = 
        new QName("http://www.opengis.net/ows/1.1","AbstractMetaData");
    /** @generated */
    public static final QName AbstractReferenceBase = 
        new QName("http://www.opengis.net/ows/1.1","AbstractReferenceBase");
    /** @generated */
    public static final QName AccessConstraints = 
        new QName("http://www.opengis.net/ows/1.1","AccessConstraints");
    /** @generated */
    public static final QName AllowedValues = 
        new QName("http://www.opengis.net/ows/1.1","AllowedValues");
    /** @generated */
    public static final QName AnyValue = 
        new QName("http://www.opengis.net/ows/1.1","AnyValue");
    /** @generated */
    public static final QName AvailableCRS = 
        new QName("http://www.opengis.net/ows/1.1","AvailableCRS");
    /** @generated */
    public static final QName BoundingBox = 
        new QName("http://www.opengis.net/ows/1.1","BoundingBox");
    /** @generated */
    public static final QName ContactInfo = 
        new QName("http://www.opengis.net/ows/1.1","ContactInfo");
    /** @generated */
    public static final QName DatasetDescriptionSummary = 
        new QName("http://www.opengis.net/ows/1.1","DatasetDescriptionSummary");
    /** @generated */
    public static final QName DataType = 
        new QName("http://www.opengis.net/ows/1.1","DataType");
    /** @generated */
    public static final QName DCP = 
        new QName("http://www.opengis.net/ows/1.1","DCP");
    /** @generated */
    public static final QName DefaultValue = 
        new QName("http://www.opengis.net/ows/1.1","DefaultValue");
    /** @generated */
    public static final QName Exception = 
        new QName("http://www.opengis.net/ows/1.1","Exception");
    /** @generated */
    public static final QName ExceptionReport = 
        new QName("http://www.opengis.net/ows/1.1","ExceptionReport");
    /** @generated */
    public static final QName ExtendedCapabilities = 
        new QName("http://www.opengis.net/ows/1.1","ExtendedCapabilities");
    /** @generated */
    public static final QName Fees = 
        new QName("http://www.opengis.net/ows/1.1","Fees");
    /** @generated */
    public static final QName GetCapabilities = 
        new QName("http://www.opengis.net/ows/1.1","GetCapabilities");
    /** @generated */
    public static final QName GetResourceByID = 
        new QName("http://www.opengis.net/ows/1.1","GetResourceByID");
    /** @generated */
    public static final QName HTTP = 
        new QName("http://www.opengis.net/ows/1.1","HTTP");
    /** @generated */
    public static final QName Identifier = 
        new QName("http://www.opengis.net/ows/1.1","Identifier");
    /** @generated */
    public static final QName IndividualName = 
        new QName("http://www.opengis.net/ows/1.1","IndividualName");
    /** @generated */
    public static final QName InputData = 
        new QName("http://www.opengis.net/ows/1.1","InputData");
    /** @generated */
    public static final QName Keywords = 
        new QName("http://www.opengis.net/ows/1.1","Keywords");
    /** @generated */
    public static final QName Language = 
        new QName("http://www.opengis.net/ows/1.1","Language");
    /** @generated */
    public static final QName Manifest = 
        new QName("http://www.opengis.net/ows/1.1","Manifest");
    /** @generated */
    public static final QName MaximumValue = 
        new QName("http://www.opengis.net/ows/1.1","MaximumValue");
    /** @generated */
    public static final QName Meaning = 
        new QName("http://www.opengis.net/ows/1.1","Meaning");
    /** @generated */
    public static final QName Metadata = 
        new QName("http://www.opengis.net/ows/1.1","Metadata");
    /** @generated */
    public static final QName MinimumValue = 
        new QName("http://www.opengis.net/ows/1.1","MinimumValue");
    /** @generated */
    public static final QName NoValues = 
        new QName("http://www.opengis.net/ows/1.1","NoValues");
    /** @generated */
    public static final QName Operation = 
        new QName("http://www.opengis.net/ows/1.1","Operation");
    /** @generated */
    public static final QName OperationResponse = 
        new QName("http://www.opengis.net/ows/1.1","OperationResponse");
    /** @generated */
    public static final QName OperationsMetadata = 
        new QName("http://www.opengis.net/ows/1.1","OperationsMetadata");
    /** @generated */
    public static final QName OrganisationName = 
        new QName("http://www.opengis.net/ows/1.1","OrganisationName");
    /** @generated */
    public static final QName OtherSource = 
        new QName("http://www.opengis.net/ows/1.1","OtherSource");
    /** @generated */
    public static final QName OutputFormat = 
        new QName("http://www.opengis.net/ows/1.1","OutputFormat");
    /** @generated */
    public static final QName PointOfContact = 
        new QName("http://www.opengis.net/ows/1.1","PointOfContact");
    /** @generated */
    public static final QName PositionName = 
        new QName("http://www.opengis.net/ows/1.1","PositionName");
    /** @generated */
    public static final QName Range = 
        new QName("http://www.opengis.net/ows/1.1","Range");
    /** @generated */
    public static final QName Reference = 
        new QName("http://www.opengis.net/ows/1.1","Reference");
    /** @generated */
    public static final QName ReferenceGroup = 
        new QName("http://www.opengis.net/ows/1.1","ReferenceGroup");
    /** @generated */
    public static final QName ReferenceSystem = 
        new QName("http://www.opengis.net/ows/1.1","ReferenceSystem");
    /** @generated */
    public static final QName Resource = 
        new QName("http://www.opengis.net/ows/1.1","Resource");
    /** @generated */
    public static final QName Role = 
        new QName("http://www.opengis.net/ows/1.1","Role");
    /** @generated */
    public static final QName ServiceIdentification = 
        new QName("http://www.opengis.net/ows/1.1","ServiceIdentification");
    /** @generated */
    public static final QName ServiceProvider = 
        new QName("http://www.opengis.net/ows/1.1","ServiceProvider");
    /** @generated */
    public static final QName ServiceReference = 
        new QName("http://www.opengis.net/ows/1.1","ServiceReference");
    /** @generated */
    public static final QName Spacing = 
        new QName("http://www.opengis.net/ows/1.1","Spacing");
    /** @generated */
    public static final QName SupportedCRS = 
        new QName("http://www.opengis.net/ows/1.1","SupportedCRS");
    /** @generated */
    public static final QName Title = 
        new QName("http://www.opengis.net/ows/1.1","Title");
    /** @generated */
    public static final QName UOM = 
        new QName("http://www.opengis.net/ows/1.1","UOM");
    /** @generated */
    public static final QName Value = 
        new QName("http://www.opengis.net/ows/1.1","Value");
    /** @generated */
    public static final QName ValuesReference = 
        new QName("http://www.opengis.net/ows/1.1","ValuesReference");
    /** @generated */
    public static final QName WGS84BoundingBox = 
        new QName("http://www.opengis.net/ows/1.1","WGS84BoundingBox");

    /* Attributes */
    /** @generated */
    public static final QName rangeClosure = 
        new QName("http://www.opengis.net/ows/1.1","rangeClosure");
    /** @generated */
    public static final QName reference = 
        new QName("http://www.opengis.net/ows/1.1","reference");

}
    
