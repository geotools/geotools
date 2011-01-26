/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import net.opengis.ows11.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Ows11FactoryImpl extends EFactoryImpl implements Ows11Factory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static Ows11Factory init() {
        try {
            Ows11Factory theOws11Factory = (Ows11Factory)EPackage.Registry.INSTANCE.getEFactory("http://www.opengis.net/ows/1.1"); 
            if (theOws11Factory != null) {
                return theOws11Factory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new Ows11FactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Ows11FactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case Ows11Package.ABSTRACT_REFERENCE_BASE_TYPE: return createAbstractReferenceBaseType();
            case Ows11Package.ACCEPT_FORMATS_TYPE: return createAcceptFormatsType();
            case Ows11Package.ACCEPT_VERSIONS_TYPE: return createAcceptVersionsType();
            case Ows11Package.ADDRESS_TYPE: return createAddressType();
            case Ows11Package.ALLOWED_VALUES_TYPE: return createAllowedValuesType();
            case Ows11Package.ANY_VALUE_TYPE: return createAnyValueType();
            case Ows11Package.BASIC_IDENTIFICATION_TYPE: return createBasicIdentificationType();
            case Ows11Package.BOUNDING_BOX_TYPE: return createBoundingBoxType();
            case Ows11Package.CAPABILITIES_BASE_TYPE: return createCapabilitiesBaseType();
            case Ows11Package.CODE_TYPE: return createCodeType();
            case Ows11Package.CONTACT_TYPE: return createContactType();
            case Ows11Package.CONTENTS_BASE_TYPE: return createContentsBaseType();
            case Ows11Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE: return createDatasetDescriptionSummaryBaseType();
            case Ows11Package.DCP_TYPE: return createDCPType();
            case Ows11Package.DESCRIPTION_TYPE: return createDescriptionType();
            case Ows11Package.DOCUMENT_ROOT: return createDocumentRoot();
            case Ows11Package.DOMAIN_METADATA_TYPE: return createDomainMetadataType();
            case Ows11Package.DOMAIN_TYPE: return createDomainType();
            case Ows11Package.EXCEPTION_REPORT_TYPE: return createExceptionReportType();
            case Ows11Package.EXCEPTION_TYPE: return createExceptionType();
            case Ows11Package.GET_CAPABILITIES_TYPE: return createGetCapabilitiesType();
            case Ows11Package.GET_RESOURCE_BY_ID_TYPE: return createGetResourceByIdType();
            case Ows11Package.HTTP_TYPE: return createHTTPType();
            case Ows11Package.IDENTIFICATION_TYPE: return createIdentificationType();
            case Ows11Package.KEYWORDS_TYPE: return createKeywordsType();
            case Ows11Package.LANGUAGE_STRING_TYPE: return createLanguageStringType();
            case Ows11Package.MANIFEST_TYPE: return createManifestType();
            case Ows11Package.METADATA_TYPE: return createMetadataType();
            case Ows11Package.NO_VALUES_TYPE: return createNoValuesType();
            case Ows11Package.ONLINE_RESOURCE_TYPE: return createOnlineResourceType();
            case Ows11Package.OPERATIONS_METADATA_TYPE: return createOperationsMetadataType();
            case Ows11Package.OPERATION_TYPE: return createOperationType();
            case Ows11Package.RANGE_TYPE: return createRangeType();
            case Ows11Package.REFERENCE_GROUP_TYPE: return createReferenceGroupType();
            case Ows11Package.REFERENCE_TYPE: return createReferenceType();
            case Ows11Package.REQUEST_METHOD_TYPE: return createRequestMethodType();
            case Ows11Package.RESPONSIBLE_PARTY_SUBSET_TYPE: return createResponsiblePartySubsetType();
            case Ows11Package.RESPONSIBLE_PARTY_TYPE: return createResponsiblePartyType();
            case Ows11Package.SECTIONS_TYPE: return createSectionsType();
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE: return createServiceIdentificationType();
            case Ows11Package.SERVICE_PROVIDER_TYPE: return createServiceProviderType();
            case Ows11Package.SERVICE_REFERENCE_TYPE: return createServiceReferenceType();
            case Ows11Package.TELEPHONE_TYPE: return createTelephoneType();
            case Ows11Package.UN_NAMED_DOMAIN_TYPE: return createUnNamedDomainType();
            case Ows11Package.VALUES_REFERENCE_TYPE: return createValuesReferenceType();
            case Ows11Package.VALUE_TYPE: return createValueType();
            case Ows11Package.WGS84_BOUNDING_BOX_TYPE: return createWGS84BoundingBoxType();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
            case Ows11Package.RANGE_CLOSURE_TYPE:
                return createRangeClosureTypeFromString(eDataType, initialValue);
            case Ows11Package.MIME_TYPE:
                return createMimeTypeFromString(eDataType, initialValue);
            case Ows11Package.POSITION_TYPE:
                return createPositionTypeFromString(eDataType, initialValue);
            case Ows11Package.POSITION_TYPE2_D:
                return createPositionType2DFromString(eDataType, initialValue);
            case Ows11Package.RANGE_CLOSURE_TYPE_OBJECT:
                return createRangeClosureTypeObjectFromString(eDataType, initialValue);
            case Ows11Package.SERVICE_TYPE:
                return createServiceTypeFromString(eDataType, initialValue);
            case Ows11Package.UPDATE_SEQUENCE_TYPE:
                return createUpdateSequenceTypeFromString(eDataType, initialValue);
            case Ows11Package.VERSION_TYPE:
                return createVersionTypeFromString(eDataType, initialValue);
            case Ows11Package.VERSION_TYPE1:
                return createVersionType1FromString(eDataType, initialValue);
            case Ows11Package.MAP:
                return createMapFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
            case Ows11Package.RANGE_CLOSURE_TYPE:
                return convertRangeClosureTypeToString(eDataType, instanceValue);
            case Ows11Package.MIME_TYPE:
                return convertMimeTypeToString(eDataType, instanceValue);
            case Ows11Package.POSITION_TYPE:
                return convertPositionTypeToString(eDataType, instanceValue);
            case Ows11Package.POSITION_TYPE2_D:
                return convertPositionType2DToString(eDataType, instanceValue);
            case Ows11Package.RANGE_CLOSURE_TYPE_OBJECT:
                return convertRangeClosureTypeObjectToString(eDataType, instanceValue);
            case Ows11Package.SERVICE_TYPE:
                return convertServiceTypeToString(eDataType, instanceValue);
            case Ows11Package.UPDATE_SEQUENCE_TYPE:
                return convertUpdateSequenceTypeToString(eDataType, instanceValue);
            case Ows11Package.VERSION_TYPE:
                return convertVersionTypeToString(eDataType, instanceValue);
            case Ows11Package.VERSION_TYPE1:
                return convertVersionType1ToString(eDataType, instanceValue);
            case Ows11Package.MAP:
                return convertMapToString(eDataType, instanceValue);
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
    public AcceptVersionsType createAcceptVersionsType() {
        AcceptVersionsTypeImpl acceptVersionsType = new AcceptVersionsTypeImpl();
        return acceptVersionsType;
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
    public List createPositionTypeFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        List result = new ArrayList();
        for (StringTokenizer stringTokenizer = new StringTokenizer(initialValue); stringTokenizer.hasMoreTokens(); ) {
            String item = stringTokenizer.nextToken();
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
        List list = (List)instanceValue;
        if (list.isEmpty()) return "";
        StringBuffer result = new StringBuffer();
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            result.append(XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.DOUBLE, i.next()));
            result.append(' ');
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List createPositionType2DFromString(EDataType eDataType, String initialValue) {
        return createPositionTypeFromString(Ows11Package.Literals.POSITION_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertPositionType2DToString(EDataType eDataType, Object instanceValue) {
        return convertPositionTypeToString(Ows11Package.Literals.POSITION_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeClosureType createRangeClosureTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createRangeClosureTypeFromString(Ows11Package.Literals.RANGE_CLOSURE_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertRangeClosureTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertRangeClosureTypeToString(Ows11Package.Literals.RANGE_CLOSURE_TYPE, instanceValue);
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
	public Map createMapFromString(EDataType eDataType, String initialValue) {
        return (Map)super.createFromString(eDataType, initialValue);
    }

				/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String convertMapToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

				/**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Ows11Package getOws11Package() {
        return (Ows11Package)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    public static Ows11Package getPackage() {
        return Ows11Package.eINSTANCE;
    }

} //Ows11FactoryImpl
