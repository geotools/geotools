/**
 */
package net.opengis.ows20.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.opengis.ows20.*;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;

import org.w3.xlink.ActuateType;
import org.w3.xlink.ShowType;
import org.w3.xlink.TypeType;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see net.opengis.ows20.Ows20Package
 * @generated
 */
public class Ows20Validator extends EObjectValidator {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final Ows20Validator INSTANCE = new Ows20Validator();

    /**
     * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.Diagnostic#getSource()
     * @see org.eclipse.emf.common.util.Diagnostic#getCode()
     * @generated
     */
    public static final String DIAGNOSTIC_SOURCE = "net.opengis.ows20";

    /**
     * A constant with a fixed name that can be used as the base value for additional hand written constants.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

    /**
     * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

    /**
     * The cached base package validator.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected XMLTypeValidator xmlTypeValidator;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Ows20Validator() {
        super();
        xmlTypeValidator = XMLTypeValidator.INSTANCE;
    }

    /**
     * Returns the package of this validator switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EPackage getEPackage() {
      return Ows20Package.eINSTANCE;
    }

    /**
     * Calls <code>validateXXX</code> for the corresponding classifier of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
        switch (classifierID) {
            case Ows20Package.ABSTRACT_REFERENCE_BASE_TYPE:
                return validateAbstractReferenceBaseType((AbstractReferenceBaseType)value, diagnostics, context);
            case Ows20Package.ACCEPT_FORMATS_TYPE:
                return validateAcceptFormatsType((AcceptFormatsType)value, diagnostics, context);
            case Ows20Package.ACCEPT_LANGUAGES_TYPE:
                return validateAcceptLanguagesType((AcceptLanguagesType)value, diagnostics, context);
            case Ows20Package.ACCEPT_VERSIONS_TYPE:
                return validateAcceptVersionsType((AcceptVersionsType)value, diagnostics, context);
            case Ows20Package.ADDITIONAL_PARAMETERS_BASE_TYPE:
                return validateAdditionalParametersBaseType((AdditionalParametersBaseType)value, diagnostics, context);
            case Ows20Package.ADDITIONAL_PARAMETERS_TYPE:
                return validateAdditionalParametersType((AdditionalParametersType)value, diagnostics, context);
            case Ows20Package.ADDITIONAL_PARAMETER_TYPE:
                return validateAdditionalParameterType((AdditionalParameterType)value, diagnostics, context);
            case Ows20Package.ADDRESS_TYPE:
                return validateAddressType((AddressType)value, diagnostics, context);
            case Ows20Package.ALLOWED_VALUES_TYPE:
                return validateAllowedValuesType((AllowedValuesType)value, diagnostics, context);
            case Ows20Package.ANY_VALUE_TYPE:
                return validateAnyValueType((AnyValueType)value, diagnostics, context);
            case Ows20Package.BASIC_IDENTIFICATION_TYPE:
                return validateBasicIdentificationType((BasicIdentificationType)value, diagnostics, context);
            case Ows20Package.BOUNDING_BOX_TYPE:
                return validateBoundingBoxType((BoundingBoxType)value, diagnostics, context);
            case Ows20Package.CAPABILITIES_BASE_TYPE:
                return validateCapabilitiesBaseType((CapabilitiesBaseType)value, diagnostics, context);
            case Ows20Package.CODE_TYPE:
                return validateCodeType((CodeType)value, diagnostics, context);
            case Ows20Package.CONTACT_TYPE:
                return validateContactType((ContactType)value, diagnostics, context);
            case Ows20Package.CONTENTS_BASE_TYPE:
                return validateContentsBaseType((ContentsBaseType)value, diagnostics, context);
            case Ows20Package.DATASET_DESCRIPTION_SUMMARY_BASE_TYPE:
                return validateDatasetDescriptionSummaryBaseType((DatasetDescriptionSummaryBaseType)value, diagnostics, context);
            case Ows20Package.DCP_TYPE:
                return validateDCPType((DCPType)value, diagnostics, context);
            case Ows20Package.DESCRIPTION_TYPE:
                return validateDescriptionType((DescriptionType)value, diagnostics, context);
            case Ows20Package.DOCUMENT_ROOT:
                return validateDocumentRoot((DocumentRoot)value, diagnostics, context);
            case Ows20Package.DOMAIN_METADATA_TYPE:
                return validateDomainMetadataType((DomainMetadataType)value, diagnostics, context);
            case Ows20Package.DOMAIN_TYPE:
                return validateDomainType((DomainType)value, diagnostics, context);
            case Ows20Package.EXCEPTION_REPORT_TYPE:
                return validateExceptionReportType((ExceptionReportType)value, diagnostics, context);
            case Ows20Package.EXCEPTION_TYPE:
                return validateExceptionType((ExceptionType)value, diagnostics, context);
            case Ows20Package.GET_CAPABILITIES_TYPE:
                return validateGetCapabilitiesType((GetCapabilitiesType)value, diagnostics, context);
            case Ows20Package.GET_RESOURCE_BY_ID_TYPE:
                return validateGetResourceByIdType((GetResourceByIdType)value, diagnostics, context);
            case Ows20Package.HTTP_TYPE:
                return validateHTTPType((HTTPType)value, diagnostics, context);
            case Ows20Package.IDENTIFICATION_TYPE:
                return validateIdentificationType((IdentificationType)value, diagnostics, context);
            case Ows20Package.KEYWORDS_TYPE:
                return validateKeywordsType((KeywordsType)value, diagnostics, context);
            case Ows20Package.LANGUAGE_STRING_TYPE:
                return validateLanguageStringType((LanguageStringType)value, diagnostics, context);
            case Ows20Package.LANGUAGES_TYPE:
                return validateLanguagesType((LanguagesType)value, diagnostics, context);
            case Ows20Package.MANIFEST_TYPE:
                return validateManifestType((ManifestType)value, diagnostics, context);
            case Ows20Package.METADATA_TYPE:
                return validateMetadataType((MetadataType)value, diagnostics, context);
            case Ows20Package.NIL_VALUE_TYPE:
                return validateNilValueType((NilValueType)value, diagnostics, context);
            case Ows20Package.NO_VALUES_TYPE:
                return validateNoValuesType((NoValuesType)value, diagnostics, context);
            case Ows20Package.ONLINE_RESOURCE_TYPE:
                return validateOnlineResourceType((OnlineResourceType)value, diagnostics, context);
            case Ows20Package.OPERATIONS_METADATA_TYPE:
                return validateOperationsMetadataType((OperationsMetadataType)value, diagnostics, context);
            case Ows20Package.OPERATION_TYPE:
                return validateOperationType((OperationType)value, diagnostics, context);
            case Ows20Package.RANGE_TYPE:
                return validateRangeType((RangeType)value, diagnostics, context);
            case Ows20Package.REFERENCE_GROUP_TYPE:
                return validateReferenceGroupType((ReferenceGroupType)value, diagnostics, context);
            case Ows20Package.REFERENCE_TYPE:
                return validateReferenceType((ReferenceType)value, diagnostics, context);
            case Ows20Package.REQUEST_METHOD_TYPE:
                return validateRequestMethodType((RequestMethodType)value, diagnostics, context);
            case Ows20Package.RESPONSIBLE_PARTY_SUBSET_TYPE:
                return validateResponsiblePartySubsetType((ResponsiblePartySubsetType)value, diagnostics, context);
            case Ows20Package.RESPONSIBLE_PARTY_TYPE:
                return validateResponsiblePartyType((ResponsiblePartyType)value, diagnostics, context);
            case Ows20Package.SECTIONS_TYPE:
                return validateSectionsType((SectionsType)value, diagnostics, context);
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE:
                return validateServiceIdentificationType((ServiceIdentificationType)value, diagnostics, context);
            case Ows20Package.SERVICE_PROVIDER_TYPE:
                return validateServiceProviderType((ServiceProviderType)value, diagnostics, context);
            case Ows20Package.SERVICE_REFERENCE_TYPE:
                return validateServiceReferenceType((ServiceReferenceType)value, diagnostics, context);
            case Ows20Package.TELEPHONE_TYPE:
                return validateTelephoneType((TelephoneType)value, diagnostics, context);
            case Ows20Package.UN_NAMED_DOMAIN_TYPE:
                return validateUnNamedDomainType((UnNamedDomainType)value, diagnostics, context);
            case Ows20Package.VALUES_REFERENCE_TYPE:
                return validateValuesReferenceType((ValuesReferenceType)value, diagnostics, context);
            case Ows20Package.VALUE_TYPE:
                return validateValueType((ValueType)value, diagnostics, context);
            case Ows20Package.WGS84_BOUNDING_BOX_TYPE:
                return validateWGS84BoundingBoxType((WGS84BoundingBoxType)value, diagnostics, context);
            case Ows20Package.RANGE_CLOSURE_TYPE:
                return validateRangeClosureType((RangeClosureType)value, diagnostics, context);
            case Ows20Package.MIME_TYPE:
                return validateMimeType((String)value, diagnostics, context);
            case Ows20Package.POSITION_TYPE:
                return validatePositionType((List<?>)value, diagnostics, context);
            case Ows20Package.POSITION_TYPE2_D:
                return validatePositionType2D((List<?>)value, diagnostics, context);
            case Ows20Package.RANGE_CLOSURE_TYPE_OBJECT:
                return validateRangeClosureTypeObject((RangeClosureType)value, diagnostics, context);
            case Ows20Package.SERVICE_TYPE:
                return validateServiceType((String)value, diagnostics, context);
            case Ows20Package.UPDATE_SEQUENCE_TYPE:
                return validateUpdateSequenceType((String)value, diagnostics, context);
            case Ows20Package.VERSION_TYPE:
                return validateVersionType((String)value, diagnostics, context);
            case Ows20Package.VERSION_TYPE1:
                return validateVersionType1((String)value, diagnostics, context);
            case Ows20Package.ARCROLE_TYPE:
                return validateArcroleType((String)value, diagnostics, context);
            case Ows20Package.HREF_TYPE:
                return validateHrefType((String)value, diagnostics, context);
            case Ows20Package.ROLE_TYPE:
                return validateRoleType((String)value, diagnostics, context);
            case Ows20Package.TITLE_ATTR_TYPE:
                return validateTitleAttrType((String)value, diagnostics, context);
            case Ows20Package.ARCROLE_TYPE_1:
                return validateArcroleType_1((String)value, diagnostics, context);
            case Ows20Package.HREF_TYPE_1:
                return validateHrefType_1((String)value, diagnostics, context);
            case Ows20Package.ROLE_TYPE_1:
                return validateRoleType_1((String)value, diagnostics, context);
            case Ows20Package.TITLE_ATTR_TYPE_1:
                return validateTitleAttrType_1((String)value, diagnostics, context);
            case Ows20Package.ARCROLE_TYPE_2:
                return validateArcroleType_2((String)value, diagnostics, context);
            case Ows20Package.HREF_TYPE_2:
                return validateHrefType_2((String)value, diagnostics, context);
            case Ows20Package.ROLE_TYPE_2:
                return validateRoleType_2((String)value, diagnostics, context);
            case Ows20Package.TITLE_ATTR_TYPE_2:
                return validateTitleAttrType_2((String)value, diagnostics, context);
            case Ows20Package.ACTUATE_TYPE:
                return validateActuateType((ActuateType)value, diagnostics, context);
            case Ows20Package.SHOW_TYPE:
                return validateShowType((ShowType)value, diagnostics, context);
            case Ows20Package.TYPE_TYPE:
                return validateTypeType((TypeType)value, diagnostics, context);
            default:
                return true;
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractReferenceBaseType(AbstractReferenceBaseType abstractReferenceBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractReferenceBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAcceptFormatsType(AcceptFormatsType acceptFormatsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(acceptFormatsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAcceptLanguagesType(AcceptLanguagesType acceptLanguagesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(acceptLanguagesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAcceptVersionsType(AcceptVersionsType acceptVersionsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(acceptVersionsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAdditionalParametersBaseType(AdditionalParametersBaseType additionalParametersBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(additionalParametersBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAdditionalParametersType(AdditionalParametersType additionalParametersType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(additionalParametersType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAdditionalParameterType(AdditionalParameterType additionalParameterType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(additionalParameterType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAddressType(AddressType addressType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(addressType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAllowedValuesType(AllowedValuesType allowedValuesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(allowedValuesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAnyValueType(AnyValueType anyValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(anyValueType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBasicIdentificationType(BasicIdentificationType basicIdentificationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(basicIdentificationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBoundingBoxType(BoundingBoxType boundingBoxType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(boundingBoxType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCapabilitiesBaseType(CapabilitiesBaseType capabilitiesBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(capabilitiesBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCodeType(CodeType codeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(codeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateContactType(ContactType contactType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(contactType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateContentsBaseType(ContentsBaseType contentsBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(contentsBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDatasetDescriptionSummaryBaseType(DatasetDescriptionSummaryBaseType datasetDescriptionSummaryBaseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(datasetDescriptionSummaryBaseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDCPType(DCPType dcpType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(dcpType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDescriptionType(DescriptionType descriptionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(descriptionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDocumentRoot(DocumentRoot documentRoot, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(documentRoot, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDomainMetadataType(DomainMetadataType domainMetadataType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(domainMetadataType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDomainType(DomainType domainType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(domainType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateExceptionReportType(ExceptionReportType exceptionReportType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(exceptionReportType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateExceptionType(ExceptionType exceptionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(exceptionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGetCapabilitiesType(GetCapabilitiesType getCapabilitiesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(getCapabilitiesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGetResourceByIdType(GetResourceByIdType getResourceByIdType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(getResourceByIdType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateHTTPType(HTTPType httpType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(httpType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateIdentificationType(IdentificationType identificationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(identificationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateKeywordsType(KeywordsType keywordsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(keywordsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLanguageStringType(LanguageStringType languageStringType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(languageStringType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLanguagesType(LanguagesType languagesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(languagesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateManifestType(ManifestType manifestType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(manifestType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMetadataType(MetadataType metadataType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(metadataType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNilValueType(NilValueType nilValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(nilValueType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNoValuesType(NoValuesType noValuesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(noValuesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOnlineResourceType(OnlineResourceType onlineResourceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(onlineResourceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOperationsMetadataType(OperationsMetadataType operationsMetadataType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(operationsMetadataType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOperationType(OperationType operationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(operationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRangeType(RangeType rangeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(rangeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateReferenceGroupType(ReferenceGroupType referenceGroupType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(referenceGroupType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateReferenceType(ReferenceType referenceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(referenceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRequestMethodType(RequestMethodType requestMethodType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(requestMethodType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateResponsiblePartySubsetType(ResponsiblePartySubsetType responsiblePartySubsetType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(responsiblePartySubsetType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateResponsiblePartyType(ResponsiblePartyType responsiblePartyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(responsiblePartyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSectionsType(SectionsType sectionsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(sectionsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateServiceIdentificationType(ServiceIdentificationType serviceIdentificationType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(serviceIdentificationType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateServiceProviderType(ServiceProviderType serviceProviderType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(serviceProviderType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateServiceReferenceType(ServiceReferenceType serviceReferenceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(serviceReferenceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTelephoneType(TelephoneType telephoneType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(telephoneType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUnNamedDomainType(UnNamedDomainType unNamedDomainType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(unNamedDomainType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateValuesReferenceType(ValuesReferenceType valuesReferenceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(valuesReferenceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateValueType(ValueType valueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(valueType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateWGS84BoundingBoxType(WGS84BoundingBoxType wgs84BoundingBoxType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(wgs84BoundingBoxType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRangeClosureType(RangeClosureType rangeClosureType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMimeType(String mimeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateMimeType_Pattern(mimeType, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateMimeType_Pattern
     */
    public static final  PatternMatcher [][] MIME_TYPE__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher("(application|audio|image|text|video|message|multipart|model)/.+(;\\s*.+=.+)*")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Mime Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMimeType_Pattern(String mimeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validatePattern(Ows20Package.Literals.MIME_TYPE, mimeType, MIME_TYPE__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePositionType(List<?> positionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validatePositionType_ItemType(positionType, diagnostics, context);
        return result;
    }

    /**
     * Validates the ItemType constraint of '<em>Position Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePositionType_ItemType(List<?> positionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = true;
        for (Iterator<?> i = positionType.iterator(); i.hasNext() && (result || diagnostics != null); ) {
            Object item = i.next();
            if (XMLTypePackage.Literals.DOUBLE.isInstance(item)) {
                result &= xmlTypeValidator.validateDouble((Double)item, diagnostics, context);
            }
            else {
                result = false;
                reportDataValueTypeViolation(XMLTypePackage.Literals.DOUBLE, item, diagnostics, context);
            }
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePositionType2D(List<?> positionType2D, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validatePositionType_ItemType(positionType2D, diagnostics, context);
        if (result || diagnostics != null) result &= validatePositionType2D_MinLength(positionType2D, diagnostics, context);
        if (result || diagnostics != null) result &= validatePositionType2D_MaxLength(positionType2D, diagnostics, context);
        return result;
    }

    /**
     * Validates the MinLength constraint of '<em>Position Type2 D</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePositionType2D_MinLength(List<?> positionType2D, DiagnosticChain diagnostics, Map<Object, Object> context) {
        int length = positionType2D.size();
        boolean result = length >= 2;
        if (!result && diagnostics != null)
            reportMinLengthViolation(Ows20Package.Literals.POSITION_TYPE2_D, positionType2D, length, 2, diagnostics, context);
        return result;
    }

    /**
     * Validates the MaxLength constraint of '<em>Position Type2 D</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePositionType2D_MaxLength(List<?> positionType2D, DiagnosticChain diagnostics, Map<Object, Object> context) {
        int length = positionType2D.size();
        boolean result = length <= 2;
        if (!result && diagnostics != null)
            reportMaxLengthViolation(Ows20Package.Literals.POSITION_TYPE2_D, positionType2D, length, 2, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRangeClosureTypeObject(RangeClosureType rangeClosureTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateServiceType(String serviceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUpdateSequenceType(String updateSequenceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVersionType(String versionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateVersionType_Pattern(versionType, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateVersionType_Pattern
     */
    public static final  PatternMatcher [][] VERSION_TYPE__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher("\\d+\\.\\d?\\d\\.\\d?\\d")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Version Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVersionType_Pattern(String versionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validatePattern(Ows20Package.Literals.VERSION_TYPE, versionType, VERSION_TYPE__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVersionType1(String versionType1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateVersionType1_Pattern(versionType1, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateVersionType1_Pattern
     */
    public static final  PatternMatcher [][] VERSION_TYPE1__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher("\\d+\\.\\d?\\d\\.\\d?\\d")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>Version Type1</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateVersionType1_Pattern(String versionType1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validatePattern(Ows20Package.Literals.VERSION_TYPE1, versionType1, VERSION_TYPE1__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArcroleType(String arcroleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateHrefType(String hrefType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRoleType(String roleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTitleAttrType(String titleAttrType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArcroleType_1(String arcroleType_1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateHrefType_1(String hrefType_1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRoleType_1(String roleType_1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTitleAttrType_1(String titleAttrType_1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateArcroleType_2(String arcroleType_2, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateHrefType_2(String hrefType_2, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateRoleType_2(String roleType_2, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTitleAttrType_2(String titleAttrType_2, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateActuateType(ActuateType actuateType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateShowType(ShowType showType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTypeType(TypeType typeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * Returns the resource locator that will be used to fetch messages for this validator's diagnostics.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ResourceLocator getResourceLocator() {
        // TODO
        // Specialize this to return a resource locator for messages specific to this validator.
        // Ensure that you remove @generated or mark it @generated NOT
        return super.getResourceLocator();
    }

} //Ows20Validator
