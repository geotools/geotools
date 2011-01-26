/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10.impl;

import java.util.List;

import java.util.Map;
import net.opengis.ows10.AcceptFormatsType;
import net.opengis.ows10.AcceptVersionsType;
import net.opengis.ows10.AddressType;
import net.opengis.ows10.BoundingBoxType;
import net.opengis.ows10.CapabilitiesBaseType;
import net.opengis.ows10.CodeType;
import net.opengis.ows10.ContactType;
import net.opengis.ows10.DCPType;
import net.opengis.ows10.DescriptionType;
import net.opengis.ows10.DocumentRoot;
import net.opengis.ows10.DomainType;
import net.opengis.ows10.ExceptionReportType;
import net.opengis.ows10.ExceptionType;
import net.opengis.ows10.GetCapabilitiesType;
import net.opengis.ows10.HTTPType;
import net.opengis.ows10.IdentificationType;
import net.opengis.ows10.KeywordsType;
import net.opengis.ows10.MetadataType;
import net.opengis.ows10.OnlineResourceType;
import net.opengis.ows10.OperationType;
import net.opengis.ows10.OperationsMetadataType;
import net.opengis.ows10.Ows10Factory;
import net.opengis.ows10.Ows10Package;
import net.opengis.ows10.RequestMethodType;
import net.opengis.ows10.ResponsiblePartySubsetType;
import net.opengis.ows10.ResponsiblePartyType;
import net.opengis.ows10.SectionsType;
import net.opengis.ows10.ServiceIdentificationType;
import net.opengis.ows10.ServiceProviderType;
import net.opengis.ows10.TelephoneType;
import net.opengis.ows10.WGS84BoundingBoxType;
import net.opengis.ows10.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Ows10FactoryImpl extends EFactoryImpl implements Ows10Factory {
	/**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public static Ows10Factory init() {
        try {
            Ows10Factory theOws10Factory = (Ows10Factory)EPackage.Registry.INSTANCE.getEFactory("http://www.opengis.net/ows"); 
            if (theOws10Factory != null) {
                return theOws10Factory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new Ows10FactoryImpl();
    }

	/**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Ows10FactoryImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case Ows10Package.ACCEPT_FORMATS_TYPE: return createAcceptFormatsType();
            case Ows10Package.ACCEPT_VERSIONS_TYPE: return createAcceptVersionsType();
            case Ows10Package.ADDRESS_TYPE: return createAddressType();
            case Ows10Package.BOUNDING_BOX_TYPE: return createBoundingBoxType();
            case Ows10Package.CAPABILITIES_BASE_TYPE: return createCapabilitiesBaseType();
            case Ows10Package.CODE_TYPE: return createCodeType();
            case Ows10Package.CONTACT_TYPE: return createContactType();
            case Ows10Package.DCP_TYPE: return createDCPType();
            case Ows10Package.DESCRIPTION_TYPE: return createDescriptionType();
            case Ows10Package.DOCUMENT_ROOT: return createDocumentRoot();
            case Ows10Package.DOMAIN_TYPE: return createDomainType();
            case Ows10Package.EXCEPTION_REPORT_TYPE: return createExceptionReportType();
            case Ows10Package.EXCEPTION_TYPE: return createExceptionType();
            case Ows10Package.GET_CAPABILITIES_TYPE: return createGetCapabilitiesType();
            case Ows10Package.HTTP_TYPE: return createHTTPType();
            case Ows10Package.IDENTIFICATION_TYPE: return createIdentificationType();
            case Ows10Package.KEYWORDS_TYPE: return createKeywordsType();
            case Ows10Package.METADATA_TYPE: return createMetadataType();
            case Ows10Package.ONLINE_RESOURCE_TYPE: return createOnlineResourceType();
            case Ows10Package.OPERATION_TYPE: return createOperationType();
            case Ows10Package.OPERATIONS_METADATA_TYPE: return createOperationsMetadataType();
            case Ows10Package.REQUEST_METHOD_TYPE: return createRequestMethodType();
            case Ows10Package.RESPONSIBLE_PARTY_SUBSET_TYPE: return createResponsiblePartySubsetType();
            case Ows10Package.RESPONSIBLE_PARTY_TYPE: return createResponsiblePartyType();
            case Ows10Package.SECTIONS_TYPE: return createSectionsType();
            case Ows10Package.SERVICE_IDENTIFICATION_TYPE: return createServiceIdentificationType();
            case Ows10Package.SERVICE_PROVIDER_TYPE: return createServiceProviderType();
            case Ows10Package.TELEPHONE_TYPE: return createTelephoneType();
            case Ows10Package.WGS84_BOUNDING_BOX_TYPE: return createWGS84BoundingBoxType();
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
            case Ows10Package.MIME_TYPE:
                return createMimeTypeFromString(eDataType, initialValue);
            case Ows10Package.VERSION_TYPE:
                return createVersionTypeFromString(eDataType, initialValue);
            case Ows10Package.POSITION_TYPE:
                return createPositionTypeFromString(eDataType, initialValue);
            case Ows10Package.UPDATE_SEQUENCE_TYPE:
                return createUpdateSequenceTypeFromString(eDataType, initialValue);
            case Ows10Package.MAP:
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
            case Ows10Package.MIME_TYPE:
                return convertMimeTypeToString(eDataType, instanceValue);
            case Ows10Package.VERSION_TYPE:
                return convertVersionTypeToString(eDataType, instanceValue);
            case Ows10Package.POSITION_TYPE:
                return convertPositionTypeToString(eDataType, instanceValue);
            case Ows10Package.UPDATE_SEQUENCE_TYPE:
                return convertUpdateSequenceTypeToString(eDataType, instanceValue);
            case Ows10Package.MAP:
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
	public MetadataType createMetadataType() {
        MetadataTypeImpl metadataType = new MetadataTypeImpl();
        return metadataType;
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
	public OperationType createOperationType() {
        OperationTypeImpl operationType = new OperationTypeImpl();
        return operationType;
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
	public TelephoneType createTelephoneType() {
        TelephoneTypeImpl telephoneType = new TelephoneTypeImpl();
        return telephoneType;
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
	public String createMimeTypeFromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String convertMimeTypeToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String createVersionTypeFromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String convertVersionTypeToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public List createPositionTypeFromString(EDataType eDataType, String initialValue) {
        return (List)super.createFromString(eDataType, initialValue);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String convertPositionTypeToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String createUpdateSequenceTypeFromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String convertUpdateSequenceTypeToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
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
	public Ows10Package getOws10Package() {
        return (Ows10Package)getEPackage();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
	public static Ows10Package getPackage() {
        return Ows10Package.eINSTANCE;
    }

} //Ows10FactoryImpl
