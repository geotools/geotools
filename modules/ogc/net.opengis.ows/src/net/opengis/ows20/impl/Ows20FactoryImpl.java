/**
 */
package net.opengis.ows20.impl;

import java.util.ArrayList;
import java.util.List;

import net.opengis.ows20.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.w3.xlink.ActuateType;
import org.w3.xlink.ShowType;
import org.w3.xlink.TypeType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Ows20FactoryImpl extends EFactoryImpl implements Ows20Factory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static Ows20Factory init() {
        try {
            Ows20Factory theOws20Factory = (Ows20Factory)EPackage.Registry.INSTANCE.getEFactory("http://www.opengis.net/ows/2.0"); 
            if (theOws20Factory != null) {
                return theOws20Factory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new Ows20FactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Ows20FactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case Ows20Package.ABSTRACT_REFERENCE_BASE_TYPE: return createAbstractReferenceBaseType();
            case Ows20Package.ACCEPT_FORMATS_TYPE: return createAcceptFormatsType();
            case Ows20Package.ACCEPT_LANGUAGES_TYPE: return createAcceptLanguagesType();
            case Ows20Package.ACCEPT_VERSIONS_TYPE: return createAcceptVersionsType();
            case Ows20Package.ADDITIONAL_PARAMETERS_BASE_TYPE: return createAdditionalParametersBaseType();
            case Ows20Package.ADDITIONAL_PARAMETERS_TYPE: return createAdditionalParametersType();
            case Ows20Package.ADDITIONAL_PARAMETER_TYPE: return createAdditionalParameterType();
            case Ows20Package.ADDRESS_TYPE: return createAddressType();
            case Ows20Package.ALLOWED_VALUES_TYPE: return createAllowedValuesType();
            case Ows20Package.ANY_VALUE_TYPE: return createAnyValueType();
            case Ows20Package.BASIC_IDENTIFICATION_TYPE: return createBasicIdentificationType();
            case Ows20Package.BOUNDING_BOX_TYPE: return createBoundingBoxType();
            case Ows20Package.CAPABILITIES_BASE_TYPE: return createCapabilitiesBaseType();
            case Ows20Package.CODE_TYPE: return createCodeType();
            case Ows20Package.CONTACT_TYPE: return createContactType();
            case Ows20Package.CONTENTS_BASE_TYPE: return createContentsBaseType();
            case Ows20Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE: return createDatasetDescriptionSummaryBaseType();
            case Ows20Package.DCP_TYPE: return createDCPType();
            case Ows20Package.DESCRIPTION_TYPE: return createDescriptionType();
            case Ows20Package.DOCUMENT_ROOT: return createDocumentRoot();
            case Ows20Package.DOMAIN_METADATA_TYPE: return createDomainMetadataType();
            case Ows20Package.DOMAIN_TYPE: return createDomainType();
            case Ows20Package.EXCEPTION_REPORT_TYPE: return createExceptionReportType();
            case Ows20Package.EXCEPTION_TYPE: return createExceptionType();
            case Ows20Package.GET_CAPABILITIES_TYPE: return createGetCapabilitiesType();
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE: return createGetResourceByIdType();
            case Ows20Package.HTTP_TYPE: return createHTTPType();
            case Ows20Package.IDENTIFICATION_TYPE: return createIdentificationType();
            case Ows20Package.KEYWORDS_TYPE: return createKeywordsType();
            case Ows20Package.LANGUAGE_STRING_TYPE: return createLanguageStringType();
            case Ows20Package.LANGUAGES_TYPE: return createLanguagesType();
            case Ows20Package.MANIFEST_TYPE: return createManifestType();
            case Ows20Package.METADATA_TYPE: return createMetadataType();
            case Ows20Package.NIL_VALUE_TYPE: return createNilValueType();
            case Ows20Package.NO_VALUES_TYPE: return createNoValuesType();
            case Ows20Package.ONLINE_RESOURCE_TYPE: return createOnlineResourceType();
            case Ows20Package.OPERATIONS_METADATA_TYPE: return createOperationsMetadataType();
            case Ows20Package.OPERATION_TYPE: return createOperationType();
            case Ows20Package.RANGE_TYPE: return createRangeType();
            case Ows20Package.REFERENCE_GROUP_TYPE: return createReferenceGroupType();
            case Ows20Package.REFERENCE_TYPE: return createReferenceType();
            case Ows20Package.REQUEST_METHOD_TYPE: return createRequestMethodType();
            case Ows20Package.RESPONSIBLE_PARTY_SUBSET_TYPE: return createResponsiblePartySubsetType();
            case Ows20Package.RESPONSIBLE_PARTY_TYPE: return createResponsiblePartyType();
            case Ows20Package.SECTIONS_TYPE: return createSectionsType();
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE: return createServiceIdentificationType();
            case Ows20Package.SERVICE_PROVIDER_TYPE: return createServiceProviderType();
            case Ows20Package.SERVICE_REFERENCE_TYPE: return createServiceReferenceType();
            case Ows20Package.TELEPHONE_TYPE: return createTelephoneType();
            case Ows20Package.UN_NAMED_DOMAIN_TYPE: return createUnNamedDomainType();
            case Ows20Package.VALUES_REFERENCE_TYPE: return createValuesReferenceType();
            case Ows20Package.VALUE_TYPE: return createValueType();
            case Ows20Package.WGS84_BOUNDING_BOX_TYPE: return createWGS84BoundingBoxType();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
            case Ows20Package.RANGE_CLOSURE_TYPE:
                return createRangeClosureTypeFromString(eDataType, initialValue);
            case Ows20Package.MIME_TYPE:
                return createMimeTypeFromString(eDataType, initialValue);
            case Ows20Package.POSITION_TYPE:
                return createPositionTypeFromString(eDataType, initialValue);
            case Ows20Package.POSITION_TYPE2_D:
                return createPositionType2DFromString(eDataType, initialValue);
            case Ows20Package.RANGE_CLOSURE_TYPE_OBJECT:
                return createRangeClosureTypeObjectFromString(eDataType, initialValue);
            case Ows20Package.SERVICE_TYPE:
                return createServiceTypeFromString(eDataType, initialValue);
            case Ows20Package.UPDATE_SEQUENCE_TYPE:
                return createUpdateSequenceTypeFromString(eDataType, initialValue);
            case Ows20Package.VERSION_TYPE:
                return createVersionTypeFromString(eDataType, initialValue);
            case Ows20Package.VERSION_TYPE1:
                return createVersionType1FromString(eDataType, initialValue);
            case Ows20Package.ARCROLE_TYPE:
                return createArcroleTypeFromString(eDataType, initialValue);
            case Ows20Package.HREF_TYPE:
                return createHrefTypeFromString(eDataType, initialValue);
            case Ows20Package.ROLE_TYPE:
                return createRoleTypeFromString(eDataType, initialValue);
            case Ows20Package.TITLE_ATTR_TYPE:
                return createTitleAttrTypeFromString(eDataType, initialValue);
            case Ows20Package.ARCROLE_TYPE_1:
                return createArcroleType_1FromString(eDataType, initialValue);
            case Ows20Package.HREF_TYPE_1:
                return createHrefType_1FromString(eDataType, initialValue);
            case Ows20Package.ROLE_TYPE_1:
                return createRoleType_1FromString(eDataType, initialValue);
            case Ows20Package.TITLE_ATTR_TYPE_1:
                return createTitleAttrType_1FromString(eDataType, initialValue);
            case Ows20Package.ARCROLE_TYPE_2:
                return createArcroleType_2FromString(eDataType, initialValue);
            case Ows20Package.HREF_TYPE_2:
                return createHrefType_2FromString(eDataType, initialValue);
            case Ows20Package.ROLE_TYPE_2:
                return createRoleType_2FromString(eDataType, initialValue);
            case Ows20Package.TITLE_ATTR_TYPE_2:
                return createTitleAttrType_2FromString(eDataType, initialValue);
            case Ows20Package.ACTUATE_TYPE:
                return createActuateTypeFromString(eDataType, initialValue);
            case Ows20Package.SHOW_TYPE:
                return createShowTypeFromString(eDataType, initialValue);
            case Ows20Package.TYPE_TYPE:
                return createTypeTypeFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
            case Ows20Package.RANGE_CLOSURE_TYPE:
                return convertRangeClosureTypeToString(eDataType, instanceValue);
            case Ows20Package.MIME_TYPE:
                return convertMimeTypeToString(eDataType, instanceValue);
            case Ows20Package.POSITION_TYPE:
                return convertPositionTypeToString(eDataType, instanceValue);
            case Ows20Package.POSITION_TYPE2_D:
                return convertPositionType2DToString(eDataType, instanceValue);
            case Ows20Package.RANGE_CLOSURE_TYPE_OBJECT:
                return convertRangeClosureTypeObjectToString(eDataType, instanceValue);
            case Ows20Package.SERVICE_TYPE:
                return convertServiceTypeToString(eDataType, instanceValue);
            case Ows20Package.UPDATE_SEQUENCE_TYPE:
                return convertUpdateSequenceTypeToString(eDataType, instanceValue);
            case Ows20Package.VERSION_TYPE:
                return convertVersionTypeToString(eDataType, instanceValue);
            case Ows20Package.VERSION_TYPE1:
                return convertVersionType1ToString(eDataType, instanceValue);
            case Ows20Package.ARCROLE_TYPE:
                return convertArcroleTypeToString(eDataType, instanceValue);
            case Ows20Package.HREF_TYPE:
                return convertHrefTypeToString(eDataType, instanceValue);
            case Ows20Package.ROLE_TYPE:
                return convertRoleTypeToString(eDataType, instanceValue);
            case Ows20Package.TITLE_ATTR_TYPE:
                return convertTitleAttrTypeToString(eDataType, instanceValue);
            case Ows20Package.ARCROLE_TYPE_1:
                return convertArcroleType_1ToString(eDataType, instanceValue);
            case Ows20Package.HREF_TYPE_1:
                return convertHrefType_1ToString(eDataType, instanceValue);
            case Ows20Package.ROLE_TYPE_1:
                return convertRoleType_1ToString(eDataType, instanceValue);
            case Ows20Package.TITLE_ATTR_TYPE_1:
                return convertTitleAttrType_1ToString(eDataType, instanceValue);
            case Ows20Package.ARCROLE_TYPE_2:
                return convertArcroleType_2ToString(eDataType, instanceValue);
            case Ows20Package.HREF_TYPE_2:
                return convertHrefType_2ToString(eDataType, instanceValue);
            case Ows20Package.ROLE_TYPE_2:
                return convertRoleType_2ToString(eDataType, instanceValue);
            case Ows20Package.TITLE_ATTR_TYPE_2:
                return convertTitleAttrType_2ToString(eDataType, instanceValue);
            case Ows20Package.ACTUATE_TYPE:
                return convertActuateTypeToString(eDataType, instanceValue);
            case Ows20Package.SHOW_TYPE:
                return convertShowTypeToString(eDataType, instanceValue);
            case Ows20Package.TYPE_TYPE:
                return convertTypeTypeToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractReferenceBaseType createAbstractReferenceBaseType() {
        AbstractReferenceBaseTypeImpl abstractReferenceBaseType = new AbstractReferenceBaseTypeImpl();
        return abstractReferenceBaseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AcceptFormatsType createAcceptFormatsType() {
        AcceptFormatsTypeImpl acceptFormatsType = new AcceptFormatsTypeImpl();
        return acceptFormatsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AcceptLanguagesType createAcceptLanguagesType() {
        AcceptLanguagesTypeImpl acceptLanguagesType = new AcceptLanguagesTypeImpl();
        return acceptLanguagesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AcceptVersionsType createAcceptVersionsType() {
        AcceptVersionsTypeImpl acceptVersionsType = new AcceptVersionsTypeImpl();
        return acceptVersionsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AdditionalParametersBaseType createAdditionalParametersBaseType() {
        AdditionalParametersBaseTypeImpl additionalParametersBaseType = new AdditionalParametersBaseTypeImpl();
        return additionalParametersBaseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AdditionalParametersType createAdditionalParametersType() {
        AdditionalParametersTypeImpl additionalParametersType = new AdditionalParametersTypeImpl();
        return additionalParametersType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AdditionalParameterType createAdditionalParameterType() {
        AdditionalParameterTypeImpl additionalParameterType = new AdditionalParameterTypeImpl();
        return additionalParameterType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AddressType createAddressType() {
        AddressTypeImpl addressType = new AddressTypeImpl();
        return addressType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllowedValuesType createAllowedValuesType() {
        AllowedValuesTypeImpl allowedValuesType = new AllowedValuesTypeImpl();
        return allowedValuesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AnyValueType createAnyValueType() {
        AnyValueTypeImpl anyValueType = new AnyValueTypeImpl();
        return anyValueType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BasicIdentificationType createBasicIdentificationType() {
        BasicIdentificationTypeImpl basicIdentificationType = new BasicIdentificationTypeImpl();
        return basicIdentificationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BoundingBoxType createBoundingBoxType() {
        BoundingBoxTypeImpl boundingBoxType = new BoundingBoxTypeImpl();
        return boundingBoxType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CapabilitiesBaseType createCapabilitiesBaseType() {
        CapabilitiesBaseTypeImpl capabilitiesBaseType = new CapabilitiesBaseTypeImpl();
        return capabilitiesBaseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType createCodeType() {
        CodeTypeImpl codeType = new CodeTypeImpl();
        return codeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ContactType createContactType() {
        ContactTypeImpl contactType = new ContactTypeImpl();
        return contactType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ContentsBaseType createContentsBaseType() {
        ContentsBaseTypeImpl contentsBaseType = new ContentsBaseTypeImpl();
        return contentsBaseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DatasetDescriptionSummaryBaseType createDatasetDescriptionSummaryBaseType() {
        DatasetDescriptionSummaryBaseTypeImpl datasetDescriptionSummaryBaseType = new DatasetDescriptionSummaryBaseTypeImpl();
        return datasetDescriptionSummaryBaseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DCPType createDCPType() {
        DCPTypeImpl dcpType = new DCPTypeImpl();
        return dcpType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DescriptionType createDescriptionType() {
        DescriptionTypeImpl descriptionType = new DescriptionTypeImpl();
        return descriptionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DocumentRoot createDocumentRoot() {
        DocumentRootImpl documentRoot = new DocumentRootImpl();
        return documentRoot;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainMetadataType createDomainMetadataType() {
        DomainMetadataTypeImpl domainMetadataType = new DomainMetadataTypeImpl();
        return domainMetadataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainType createDomainType() {
        DomainTypeImpl domainType = new DomainTypeImpl();
        return domainType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExceptionReportType createExceptionReportType() {
        ExceptionReportTypeImpl exceptionReportType = new ExceptionReportTypeImpl();
        return exceptionReportType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExceptionType createExceptionType() {
        ExceptionTypeImpl exceptionType = new ExceptionTypeImpl();
        return exceptionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetCapabilitiesType createGetCapabilitiesType() {
        GetCapabilitiesTypeImpl getCapabilitiesType = new GetCapabilitiesTypeImpl();
        return getCapabilitiesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetResourceByIdType createGetResourceByIdType() {
        GetResourceByIdTypeImpl getResourceByIdType = new GetResourceByIdTypeImpl();
        return getResourceByIdType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public HTTPType createHTTPType() {
        HTTPTypeImpl httpType = new HTTPTypeImpl();
        return httpType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IdentificationType createIdentificationType() {
        IdentificationTypeImpl identificationType = new IdentificationTypeImpl();
        return identificationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public KeywordsType createKeywordsType() {
        KeywordsTypeImpl keywordsType = new KeywordsTypeImpl();
        return keywordsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LanguageStringType createLanguageStringType() {
        LanguageStringTypeImpl languageStringType = new LanguageStringTypeImpl();
        return languageStringType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LanguagesType createLanguagesType() {
        LanguagesTypeImpl languagesType = new LanguagesTypeImpl();
        return languagesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ManifestType createManifestType() {
        ManifestTypeImpl manifestType = new ManifestTypeImpl();
        return manifestType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MetadataType createMetadataType() {
        MetadataTypeImpl metadataType = new MetadataTypeImpl();
        return metadataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NilValueType createNilValueType() {
        NilValueTypeImpl nilValueType = new NilValueTypeImpl();
        return nilValueType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NoValuesType createNoValuesType() {
        NoValuesTypeImpl noValuesType = new NoValuesTypeImpl();
        return noValuesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OnlineResourceType createOnlineResourceType() {
        OnlineResourceTypeImpl onlineResourceType = new OnlineResourceTypeImpl();
        return onlineResourceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationsMetadataType createOperationsMetadataType() {
        OperationsMetadataTypeImpl operationsMetadataType = new OperationsMetadataTypeImpl();
        return operationsMetadataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationType createOperationType() {
        OperationTypeImpl operationType = new OperationTypeImpl();
        return operationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeType createRangeType() {
        RangeTypeImpl rangeType = new RangeTypeImpl();
        return rangeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ReferenceGroupType createReferenceGroupType() {
        ReferenceGroupTypeImpl referenceGroupType = new ReferenceGroupTypeImpl();
        return referenceGroupType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ReferenceType createReferenceType() {
        ReferenceTypeImpl referenceType = new ReferenceTypeImpl();
        return referenceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RequestMethodType createRequestMethodType() {
        RequestMethodTypeImpl requestMethodType = new RequestMethodTypeImpl();
        return requestMethodType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResponsiblePartySubsetType createResponsiblePartySubsetType() {
        ResponsiblePartySubsetTypeImpl responsiblePartySubsetType = new ResponsiblePartySubsetTypeImpl();
        return responsiblePartySubsetType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResponsiblePartyType createResponsiblePartyType() {
        ResponsiblePartyTypeImpl responsiblePartyType = new ResponsiblePartyTypeImpl();
        return responsiblePartyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SectionsType createSectionsType() {
        SectionsTypeImpl sectionsType = new SectionsTypeImpl();
        return sectionsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceIdentificationType createServiceIdentificationType() {
        ServiceIdentificationTypeImpl serviceIdentificationType = new ServiceIdentificationTypeImpl();
        return serviceIdentificationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceProviderType createServiceProviderType() {
        ServiceProviderTypeImpl serviceProviderType = new ServiceProviderTypeImpl();
        return serviceProviderType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceReferenceType createServiceReferenceType() {
        ServiceReferenceTypeImpl serviceReferenceType = new ServiceReferenceTypeImpl();
        return serviceReferenceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TelephoneType createTelephoneType() {
        TelephoneTypeImpl telephoneType = new TelephoneTypeImpl();
        return telephoneType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UnNamedDomainType createUnNamedDomainType() {
        UnNamedDomainTypeImpl unNamedDomainType = new UnNamedDomainTypeImpl();
        return unNamedDomainType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValuesReferenceType createValuesReferenceType() {
        ValuesReferenceTypeImpl valuesReferenceType = new ValuesReferenceTypeImpl();
        return valuesReferenceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueType createValueType() {
        ValueTypeImpl valueType = new ValueTypeImpl();
        return valueType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WGS84BoundingBoxType createWGS84BoundingBoxType() {
        WGS84BoundingBoxTypeImpl wgs84BoundingBoxType = new WGS84BoundingBoxTypeImpl();
        return wgs84BoundingBoxType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeClosureType createRangeClosureTypeFromString(EDataType eDataType, String initialValue) {
        RangeClosureType result = RangeClosureType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertRangeClosureTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createMimeTypeFromString(EDataType eDataType, String initialValue) {
        return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertMimeTypeToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<Double> createPositionTypeFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        List<Double> result = new ArrayList<Double>();
        for (String item : split(initialValue)) {
            result.add((Double)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.DOUBLE, item));
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertPositionTypeToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        List<?> list = (List<?>)instanceValue;
        if (list.isEmpty()) return "";
        StringBuffer result = new StringBuffer();
        for (Object item : list) {
            result.append(XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DOUBLE, item));
            result.append(' ');
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<Double> createPositionType2DFromString(EDataType eDataType, String initialValue) {
        return createPositionTypeFromString(Ows20Package.Literals.POSITION_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertPositionType2DToString(EDataType eDataType, Object instanceValue) {
        return convertPositionTypeToString(Ows20Package.Literals.POSITION_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeClosureType createRangeClosureTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createRangeClosureTypeFromString(Ows20Package.Literals.RANGE_CLOSURE_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertRangeClosureTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertRangeClosureTypeToString(Ows20Package.Literals.RANGE_CLOSURE_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createServiceTypeFromString(EDataType eDataType, String initialValue) {
        return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertServiceTypeToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createUpdateSequenceTypeFromString(EDataType eDataType, String initialValue) {
        return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertUpdateSequenceTypeToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createVersionTypeFromString(EDataType eDataType, String initialValue) {
        return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertVersionTypeToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createVersionType1FromString(EDataType eDataType, String initialValue) {
        return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertVersionType1ToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createArcroleTypeFromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertArcroleTypeToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createHrefTypeFromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertHrefTypeToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createRoleTypeFromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertRoleTypeToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createTitleAttrTypeFromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTitleAttrTypeToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createArcroleType_1FromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertArcroleType_1ToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createHrefType_1FromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertHrefType_1ToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createRoleType_1FromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertRoleType_1ToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createTitleAttrType_1FromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTitleAttrType_1ToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createArcroleType_2FromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertArcroleType_2ToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createHrefType_2FromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertHrefType_2ToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createRoleType_2FromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertRoleType_2ToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createTitleAttrType_2FromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTitleAttrType_2ToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ActuateType createActuateTypeFromString(EDataType eDataType, String initialValue) {
        return (ActuateType)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertActuateTypeToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ShowType createShowTypeFromString(EDataType eDataType, String initialValue) {
        return (ShowType)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertShowTypeToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TypeType createTypeTypeFromString(EDataType eDataType, String initialValue) {
        return (TypeType)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTypeTypeToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Ows20Package getOws20Package() {
        return (Ows20Package)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static Ows20Package getPackage() {
        return Ows20Package.eINSTANCE;
    }

} //_2FactoryImpl
