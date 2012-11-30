package org.geotools.ows.v2_0;


import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.xlink.XLINK;
import org.geotools.xml.XML;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.opengis.net/ows/2.0 schema.
 *
 * @generated
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
     * Returns 'http://www.opengis.net/ows/2.0'.
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
    public static final String NAMESPACE = "http://www.opengis.net/ows/2.0";
    
    /* Type Definitions */
    /** @generated */
    public static final QName AbstractReferenceBaseType = 
        new QName("http://www.opengis.net/ows/2.0","AbstractReferenceBaseType");
    /** @generated */
    public static final QName AcceptFormatsType = 
        new QName("http://www.opengis.net/ows/2.0","AcceptFormatsType");
    /** @generated */
    public static final QName AcceptVersionsType = 
        new QName("http://www.opengis.net/ows/2.0","AcceptVersionsType");
    /** @generated */
    public static final QName AdditionalParametersBaseType = 
        new QName("http://www.opengis.net/ows/2.0","AdditionalParametersBaseType");
    /** @generated */
    public static final QName AdditionalParametersType = 
        new QName("http://www.opengis.net/ows/2.0","AdditionalParametersType");
    /** @generated */
    public static final QName AddressType = 
        new QName("http://www.opengis.net/ows/2.0","AddressType");
    /** @generated */
    public static final QName BasicIdentificationType = 
        new QName("http://www.opengis.net/ows/2.0","BasicIdentificationType");
    /** @generated */
    public static final QName BoundingBoxType = 
        new QName("http://www.opengis.net/ows/2.0","BoundingBoxType");
    /** @generated */
    public static final QName CapabilitiesBaseType = 
        new QName("http://www.opengis.net/ows/2.0","CapabilitiesBaseType");
    /** @generated */
    public static final QName CodeType = 
        new QName("http://www.opengis.net/ows/2.0","CodeType");
    /** @generated */
    public static final QName ContactType = 
        new QName("http://www.opengis.net/ows/2.0","ContactType");
    /** @generated */
    public static final QName ContentsBaseType = 
        new QName("http://www.opengis.net/ows/2.0","ContentsBaseType");
    /** @generated */
    public static final QName DatasetDescriptionSummaryBaseType = 
        new QName("http://www.opengis.net/ows/2.0","DatasetDescriptionSummaryBaseType");
    /** @generated */
    public static final QName DescriptionType = 
        new QName("http://www.opengis.net/ows/2.0","DescriptionType");
    /** @generated */
    public static final QName DomainMetadataType = 
        new QName("http://www.opengis.net/ows/2.0","DomainMetadataType");
    /** @generated */
    public static final QName DomainType = 
        new QName("http://www.opengis.net/ows/2.0","DomainType");
    /** @generated */
    public static final QName ExceptionType = 
        new QName("http://www.opengis.net/ows/2.0","ExceptionType");
    /** @generated */
    public static final QName GetCapabilitiesType = 
        new QName("http://www.opengis.net/ows/2.0","GetCapabilitiesType");
    /** @generated */
    public static final QName GetResourceByIdType = 
        new QName("http://www.opengis.net/ows/2.0","GetResourceByIdType");
    /** @generated */
    public static final QName IdentificationType = 
        new QName("http://www.opengis.net/ows/2.0","IdentificationType");
    /** @generated */
    public static final QName KeywordsType = 
        new QName("http://www.opengis.net/ows/2.0","KeywordsType");
    /** @generated */
    public static final QName LanguageStringType = 
        new QName("http://www.opengis.net/ows/2.0","LanguageStringType");
    /** @generated */
    public static final QName ManifestType = 
        new QName("http://www.opengis.net/ows/2.0","ManifestType");
    /** @generated */
    public static final QName MetadataType = 
        new QName("http://www.opengis.net/ows/2.0","MetadataType");
    /** @generated */
    public static final QName MimeType = 
        new QName("http://www.opengis.net/ows/2.0","MimeType");
    /** @generated */
    public static final QName NilValueType = 
        new QName("http://www.opengis.net/ows/2.0","NilValueType");
    /** @generated */
    public static final QName OnlineResourceType = 
        new QName("http://www.opengis.net/ows/2.0","OnlineResourceType");
    /** @generated */
    public static final QName PositionType = 
        new QName("http://www.opengis.net/ows/2.0","PositionType");
    /** @generated */
    public static final QName PositionType2D = 
        new QName("http://www.opengis.net/ows/2.0","PositionType2D");
    /** @generated */
    public static final QName RangeType = 
        new QName("http://www.opengis.net/ows/2.0","RangeType");
    /** @generated */
    public static final QName ReferenceGroupType = 
        new QName("http://www.opengis.net/ows/2.0","ReferenceGroupType");
    /** @generated */
    public static final QName ReferenceType = 
        new QName("http://www.opengis.net/ows/2.0","ReferenceType");
    /** @generated */
    public static final QName RequestMethodType = 
        new QName("http://www.opengis.net/ows/2.0","RequestMethodType");
    /** @generated */
    public static final QName ResponsiblePartySubsetType = 
        new QName("http://www.opengis.net/ows/2.0","ResponsiblePartySubsetType");
    /** @generated */
    public static final QName ResponsiblePartyType = 
        new QName("http://www.opengis.net/ows/2.0","ResponsiblePartyType");
    /** @generated */
    public static final QName SectionsType = 
        new QName("http://www.opengis.net/ows/2.0","SectionsType");
    /** @generated */
    public static final QName ServiceReferenceType = 
        new QName("http://www.opengis.net/ows/2.0","ServiceReferenceType");
    /** @generated */
    public static final QName ServiceType = 
        new QName("http://www.opengis.net/ows/2.0","ServiceType");
    /** @generated */
    public static final QName TelephoneType = 
        new QName("http://www.opengis.net/ows/2.0","TelephoneType");
    /** @generated */
    public static final QName UnNamedDomainType = 
        new QName("http://www.opengis.net/ows/2.0","UnNamedDomainType");
    /** @generated */
    public static final QName UpdateSequenceType = 
        new QName("http://www.opengis.net/ows/2.0","UpdateSequenceType");
    /** @generated */
    public static final QName ValueType = 
        new QName("http://www.opengis.net/ows/2.0","ValueType");
    /** @generated */
    public static final QName VersionType = 
        new QName("http://www.opengis.net/ows/2.0","VersionType");
    /** @generated */
    public static final QName WGS84BoundingBoxType = 
        new QName("http://www.opengis.net/ows/2.0","WGS84BoundingBoxType");
    /** @generated */
    public static final QName _AdditionalParameter = 
        new QName("http://www.opengis.net/ows/2.0","_AdditionalParameter");
    /** @generated */
    public static final QName _AllowedValues = 
        new QName("http://www.opengis.net/ows/2.0","_AllowedValues");
    /** @generated */
    public static final QName _AnyValue = 
        new QName("http://www.opengis.net/ows/2.0","_AnyValue");
    /** @generated */
    public static final QName _DCP = 
        new QName("http://www.opengis.net/ows/2.0","_DCP");
    /** @generated */
    public static final QName _ExceptionReport = 
        new QName("http://www.opengis.net/ows/2.0","_ExceptionReport");
    /** @generated */
    public static final QName _HTTP = 
        new QName("http://www.opengis.net/ows/2.0","_HTTP");
    /** @generated */
    public static final QName _NoValues = 
        new QName("http://www.opengis.net/ows/2.0","_NoValues");
    /** @generated */
    public static final QName _Operation = 
        new QName("http://www.opengis.net/ows/2.0","_Operation");
    /** @generated */
    public static final QName _OperationsMetadata = 
        new QName("http://www.opengis.net/ows/2.0","_OperationsMetadata");
    /** @generated */
    public static final QName _ServiceIdentification = 
        new QName("http://www.opengis.net/ows/2.0","_ServiceIdentification");
    /** @generated */
    public static final QName _ServiceProvider = 
        new QName("http://www.opengis.net/ows/2.0","_ServiceProvider");
    /** @generated */
    public static final QName _ValuesReference = 
        new QName("http://www.opengis.net/ows/2.0","_ValuesReference");
    /** @generated */
    public static final QName _rangeClosure = 
        new QName("http://www.opengis.net/ows/2.0","_rangeClosure");
    /** @generated */
    public static final QName CapabilitiesBaseType_Languages = 
        new QName("http://www.opengis.net/ows/2.0","CapabilitiesBaseType_Languages");
    /** @generated */
    public static final QName GetCapabilitiesType_AcceptLanguages = 
        new QName("http://www.opengis.net/ows/2.0","GetCapabilitiesType_AcceptLanguages");

    /* Elements */
    /** @generated */
    public static final QName Abstract = 
        new QName("http://www.opengis.net/ows/2.0","Abstract");
    /** @generated */
    public static final QName AbstractMetaData = 
        new QName("http://www.opengis.net/ows/2.0","AbstractMetaData");
    /** @generated */
    public static final QName AbstractReferenceBase = 
        new QName("http://www.opengis.net/ows/2.0","AbstractReferenceBase");
    /** @generated */
    public static final QName AccessConstraints = 
        new QName("http://www.opengis.net/ows/2.0","AccessConstraints");
    /** @generated */
    public static final QName AdditionalParameter = 
        new QName("http://www.opengis.net/ows/2.0","AdditionalParameter");
    /** @generated */
    public static final QName AdditionalParameters = 
        new QName("http://www.opengis.net/ows/2.0","AdditionalParameters");
    /** @generated */
    public static final QName AllowedValues = 
        new QName("http://www.opengis.net/ows/2.0","AllowedValues");
    /** @generated */
    public static final QName AnyValue = 
        new QName("http://www.opengis.net/ows/2.0","AnyValue");
    /** @generated */
    public static final QName AvailableCRS = 
        new QName("http://www.opengis.net/ows/2.0","AvailableCRS");
    /** @generated */
    public static final QName BoundingBox = 
        new QName("http://www.opengis.net/ows/2.0","BoundingBox");
    /** @generated */
    public static final QName ContactInfo = 
        new QName("http://www.opengis.net/ows/2.0","ContactInfo");
    /** @generated */
    public static final QName DatasetDescriptionSummary = 
        new QName("http://www.opengis.net/ows/2.0","DatasetDescriptionSummary");
    /** @generated */
    public static final QName DataType = 
        new QName("http://www.opengis.net/ows/2.0","DataType");
    /** @generated */
    public static final QName DCP = 
        new QName("http://www.opengis.net/ows/2.0","DCP");
    /** @generated */
    public static final QName DefaultValue = 
        new QName("http://www.opengis.net/ows/2.0","DefaultValue");
    /** @generated */
    public static final QName Exception = 
        new QName("http://www.opengis.net/ows/2.0","Exception");
    /** @generated */
    public static final QName ExceptionReport = 
        new QName("http://www.opengis.net/ows/2.0","ExceptionReport");
    /** @generated */
    public static final QName ExtendedCapabilities = 
        new QName("http://www.opengis.net/ows/2.0","ExtendedCapabilities");
    /** @generated */
    public static final QName Fees = 
        new QName("http://www.opengis.net/ows/2.0","Fees");
    /** @generated */
    public static final QName GetCapabilities = 
        new QName("http://www.opengis.net/ows/2.0","GetCapabilities");
    /** @generated */
    public static final QName GetResourceByID = 
        new QName("http://www.opengis.net/ows/2.0","GetResourceByID");
    /** @generated */
    public static final QName HTTP = 
        new QName("http://www.opengis.net/ows/2.0","HTTP");
    /** @generated */
    public static final QName Identifier = 
        new QName("http://www.opengis.net/ows/2.0","Identifier");
    /** @generated */
    public static final QName IndividualName = 
        new QName("http://www.opengis.net/ows/2.0","IndividualName");
    /** @generated */
    public static final QName InputData = 
        new QName("http://www.opengis.net/ows/2.0","InputData");
    /** @generated */
    public static final QName Keywords = 
        new QName("http://www.opengis.net/ows/2.0","Keywords");
    /** @generated */
    public static final QName Language = 
        new QName("http://www.opengis.net/ows/2.0","Language");
    /** @generated */
    public static final QName Manifest = 
        new QName("http://www.opengis.net/ows/2.0","Manifest");
    /** @generated */
    public static final QName MaximumValue = 
        new QName("http://www.opengis.net/ows/2.0","MaximumValue");
    /** @generated */
    public static final QName Meaning = 
        new QName("http://www.opengis.net/ows/2.0","Meaning");
    /** @generated */
    public static final QName Metadata = 
        new QName("http://www.opengis.net/ows/2.0","Metadata");
    /** @generated */
    public static final QName MinimumValue = 
        new QName("http://www.opengis.net/ows/2.0","MinimumValue");
    /** @generated */
    public static final QName nilValue = 
        new QName("http://www.opengis.net/ows/2.0","nilValue");
    /** @generated */
    public static final QName NoValues = 
        new QName("http://www.opengis.net/ows/2.0","NoValues");
    /** @generated */
    public static final QName Operation = 
        new QName("http://www.opengis.net/ows/2.0","Operation");
    /** @generated */
    public static final QName OperationResponse = 
        new QName("http://www.opengis.net/ows/2.0","OperationResponse");
    /** @generated */
    public static final QName OperationsMetadata = 
        new QName("http://www.opengis.net/ows/2.0","OperationsMetadata");
    /** @generated */
    public static final QName OrganisationName = 
        new QName("http://www.opengis.net/ows/2.0","OrganisationName");
    /** @generated */
    public static final QName OtherSource = 
        new QName("http://www.opengis.net/ows/2.0","OtherSource");
    /** @generated */
    public static final QName OutputFormat = 
        new QName("http://www.opengis.net/ows/2.0","OutputFormat");
    /** @generated */
    public static final QName PointOfContact = 
        new QName("http://www.opengis.net/ows/2.0","PointOfContact");
    /** @generated */
    public static final QName PositionName = 
        new QName("http://www.opengis.net/ows/2.0","PositionName");
    /** @generated */
    public static final QName Range = 
        new QName("http://www.opengis.net/ows/2.0","Range");
    /** @generated */
    public static final QName Reference = 
        new QName("http://www.opengis.net/ows/2.0","Reference");
    /** @generated */
    public static final QName ReferenceGroup = 
        new QName("http://www.opengis.net/ows/2.0","ReferenceGroup");
    /** @generated */
    public static final QName ReferenceSystem = 
        new QName("http://www.opengis.net/ows/2.0","ReferenceSystem");
    /** @generated */
    public static final QName Resource = 
        new QName("http://www.opengis.net/ows/2.0","Resource");
    /** @generated */
    public static final QName Role = 
        new QName("http://www.opengis.net/ows/2.0","Role");
    /** @generated */
    public static final QName ServiceIdentification = 
        new QName("http://www.opengis.net/ows/2.0","ServiceIdentification");
    /** @generated */
    public static final QName ServiceProvider = 
        new QName("http://www.opengis.net/ows/2.0","ServiceProvider");
    /** @generated */
    public static final QName ServiceReference = 
        new QName("http://www.opengis.net/ows/2.0","ServiceReference");
    /** @generated */
    public static final QName Spacing = 
        new QName("http://www.opengis.net/ows/2.0","Spacing");
    /** @generated */
    public static final QName SupportedCRS = 
        new QName("http://www.opengis.net/ows/2.0","SupportedCRS");
    /** @generated */
    public static final QName Title = 
        new QName("http://www.opengis.net/ows/2.0","Title");
    /** @generated */
    public static final QName UOM = 
        new QName("http://www.opengis.net/ows/2.0","UOM");
    /** @generated */
    public static final QName Value = 
        new QName("http://www.opengis.net/ows/2.0","Value");
    /** @generated */
    public static final QName ValuesReference = 
        new QName("http://www.opengis.net/ows/2.0","ValuesReference");
    /** @generated */
    public static final QName WGS84BoundingBox = 
        new QName("http://www.opengis.net/ows/2.0","WGS84BoundingBox");

    /* Attributes */
    /** @generated */
    public static final QName rangeClosure = 
        new QName("http://www.opengis.net/ows/2.0","rangeClosure");
    /** @generated */
    public static final QName reference = 
        new QName("http://www.opengis.net/ows/2.0","reference");

}
    