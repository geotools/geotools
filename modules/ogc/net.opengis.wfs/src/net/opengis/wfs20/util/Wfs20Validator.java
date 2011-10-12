/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.util;

import java.math.BigInteger;

import java.net.URI;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import net.opengis.wfs20.*;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeValidator;
import org.geotools.feature.FeatureCollection;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.sort.SortBy;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wfs20.Wfs20Package
 * @generated
 */
public class Wfs20Validator extends EObjectValidator {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final Wfs20Validator INSTANCE = new Wfs20Validator();

    /**
     * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.Diagnostic#getSource()
     * @see org.eclipse.emf.common.util.Diagnostic#getCode()
     * @generated
     */
    public static final String DIAGNOSTIC_SOURCE = "net.opengis.wfs20";

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
    public Wfs20Validator() {
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
      return Wfs20Package.eINSTANCE;
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
            case Wfs20Package.ABSTRACT_TRANSACTION_ACTION_TYPE:
                return validateAbstractTransactionActionType((AbstractTransactionActionType)value, diagnostics, context);
            case Wfs20Package.ABSTRACT_TYPE:
                return validateAbstractType((AbstractType)value, diagnostics, context);
            case Wfs20Package.ACTION_RESULTS_TYPE:
                return validateActionResultsType((ActionResultsType)value, diagnostics, context);
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE:
                return validateAdditionalObjectsType((AdditionalObjectsType)value, diagnostics, context);
            case Wfs20Package.ADDITIONAL_VALUES_TYPE:
                return validateAdditionalValuesType((AdditionalValuesType)value, diagnostics, context);
            case Wfs20Package.BASE_REQUEST_TYPE:
                return validateBaseRequestType((BaseRequestType)value, diagnostics, context);
            case Wfs20Package.CREATED_OR_MODIFIED_FEATURE_TYPE:
                return validateCreatedOrModifiedFeatureType((CreatedOrModifiedFeatureType)value, diagnostics, context);
            case Wfs20Package.CREATE_STORED_QUERY_RESPONSE_TYPE:
                return validateCreateStoredQueryResponseType((CreateStoredQueryResponseType)value, diagnostics, context);
            case Wfs20Package.CREATE_STORED_QUERY_TYPE:
                return validateCreateStoredQueryType((CreateStoredQueryType)value, diagnostics, context);
            case Wfs20Package.DELETE_TYPE:
                return validateDeleteType((DeleteType)value, diagnostics, context);
            case Wfs20Package.DESCRIBE_FEATURE_TYPE_TYPE:
                return validateDescribeFeatureTypeType((DescribeFeatureTypeType)value, diagnostics, context);
            case Wfs20Package.DESCRIBE_STORED_QUERIES_RESPONSE_TYPE:
                return validateDescribeStoredQueriesResponseType((DescribeStoredQueriesResponseType)value, diagnostics, context);
            case Wfs20Package.DESCRIBE_STORED_QUERIES_TYPE:
                return validateDescribeStoredQueriesType((DescribeStoredQueriesType)value, diagnostics, context);
            case Wfs20Package.DOCUMENT_ROOT:
                return validateDocumentRoot((DocumentRoot)value, diagnostics, context);
            case Wfs20Package.DROP_STORED_QUERY_TYPE:
                return validateDropStoredQueryType((DropStoredQueryType)value, diagnostics, context);
            case Wfs20Package.ELEMENT_TYPE:
                return validateElementType((ElementType)value, diagnostics, context);
            case Wfs20Package.EMPTY_TYPE:
                return validateEmptyType((EmptyType)value, diagnostics, context);
            case Wfs20Package.ENVELOPE_PROPERTY_TYPE:
                return validateEnvelopePropertyType((EnvelopePropertyType)value, diagnostics, context);
            case Wfs20Package.EXECUTION_STATUS_TYPE:
                return validateExecutionStatusType((ExecutionStatusType)value, diagnostics, context);
            case Wfs20Package.EXTENDED_DESCRIPTION_TYPE:
                return validateExtendedDescriptionType((ExtendedDescriptionType)value, diagnostics, context);
            case Wfs20Package.FEATURE_COLLECTION_TYPE:
                return validateFeatureCollectionType((FeatureCollectionType)value, diagnostics, context);
            case Wfs20Package.FEATURES_LOCKED_TYPE:
                return validateFeaturesLockedType((FeaturesLockedType)value, diagnostics, context);
            case Wfs20Package.FEATURES_NOT_LOCKED_TYPE:
                return validateFeaturesNotLockedType((FeaturesNotLockedType)value, diagnostics, context);
            case Wfs20Package.FEATURE_TYPE_LIST_TYPE:
                return validateFeatureTypeListType((FeatureTypeListType)value, diagnostics, context);
            case Wfs20Package.FEATURE_TYPE_TYPE:
                return validateFeatureTypeType((FeatureTypeType)value, diagnostics, context);
            case Wfs20Package.GET_CAPABILITIES_TYPE:
                return validateGetCapabilitiesType((GetCapabilitiesType)value, diagnostics, context);
            case Wfs20Package.GET_FEATURE_TYPE:
                return validateGetFeatureType((GetFeatureType)value, diagnostics, context);
            case Wfs20Package.GET_FEATURE_WITH_LOCK_TYPE:
                return validateGetFeatureWithLockType((GetFeatureWithLockType)value, diagnostics, context);
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE:
                return validateGetPropertyValueType((GetPropertyValueType)value, diagnostics, context);
            case Wfs20Package.INSERT_TYPE:
                return validateInsertType((InsertType)value, diagnostics, context);
            case Wfs20Package.LIST_STORED_QUERIES_RESPONSE_TYPE:
                return validateListStoredQueriesResponseType((ListStoredQueriesResponseType)value, diagnostics, context);
            case Wfs20Package.LIST_STORED_QUERIES_TYPE:
                return validateListStoredQueriesType((ListStoredQueriesType)value, diagnostics, context);
            case Wfs20Package.LOCK_FEATURE_RESPONSE_TYPE:
                return validateLockFeatureResponseType((LockFeatureResponseType)value, diagnostics, context);
            case Wfs20Package.LOCK_FEATURE_TYPE:
                return validateLockFeatureType((LockFeatureType)value, diagnostics, context);
            case Wfs20Package.MEMBER_PROPERTY_TYPE:
                return validateMemberPropertyType((MemberPropertyType)value, diagnostics, context);
            case Wfs20Package.METADATA_URL_TYPE:
                return validateMetadataURLType((MetadataURLType)value, diagnostics, context);
            case Wfs20Package.NATIVE_TYPE:
                return validateNativeType((NativeType)value, diagnostics, context);
            case Wfs20Package.NO_CRS_TYPE:
                return validateNoCRSType((NoCRSType)value, diagnostics, context);
            case Wfs20Package.OUTPUT_FORMAT_LIST_TYPE:
                return validateOutputFormatListType((OutputFormatListType)value, diagnostics, context);
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE:
                return validateParameterExpressionType((ParameterExpressionType)value, diagnostics, context);
            case Wfs20Package.PARAMETER_TYPE:
                return validateParameterType((ParameterType)value, diagnostics, context);
            case Wfs20Package.PROPERTY_NAME_TYPE:
                return validatePropertyNameType((PropertyNameType)value, diagnostics, context);
            case Wfs20Package.PROPERTY_TYPE:
                return validatePropertyType((PropertyType)value, diagnostics, context);
            case Wfs20Package.QUERY_EXPRESSION_TEXT_TYPE:
                return validateQueryExpressionTextType((QueryExpressionTextType)value, diagnostics, context);
            case Wfs20Package.QUERY_TYPE:
                return validateQueryType((QueryType)value, diagnostics, context);
            case Wfs20Package.REPLACE_TYPE:
                return validateReplaceType((ReplaceType)value, diagnostics, context);
            case Wfs20Package.SIMPLE_FEATURE_COLLECTION_TYPE:
                return validateSimpleFeatureCollectionType((SimpleFeatureCollectionType)value, diagnostics, context);
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE:
                return validateStoredQueryDescriptionType((StoredQueryDescriptionType)value, diagnostics, context);
            case Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE:
                return validateStoredQueryListItemType((StoredQueryListItemType)value, diagnostics, context);
            case Wfs20Package.STORED_QUERY_TYPE:
                return validateStoredQueryType((StoredQueryType)value, diagnostics, context);
            case Wfs20Package.TITLE_TYPE:
                return validateTitleType((TitleType)value, diagnostics, context);
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE:
                return validateTransactionResponseType((TransactionResponseType)value, diagnostics, context);
            case Wfs20Package.TRANSACTION_SUMMARY_TYPE:
                return validateTransactionSummaryType((TransactionSummaryType)value, diagnostics, context);
            case Wfs20Package.TRANSACTION_TYPE:
                return validateTransactionType((TransactionType)value, diagnostics, context);
            case Wfs20Package.TRUNCATED_RESPONSE_TYPE:
                return validateTruncatedResponseType((TruncatedResponseType)value, diagnostics, context);
            case Wfs20Package.TUPLE_TYPE:
                return validateTupleType((TupleType)value, diagnostics, context);
            case Wfs20Package.UPDATE_TYPE:
                return validateUpdateType((UpdateType)value, diagnostics, context);
            case Wfs20Package.VALUE_COLLECTION_TYPE:
                return validateValueCollectionType((ValueCollectionType)value, diagnostics, context);
            case Wfs20Package.VALUE_LIST_TYPE:
                return validateValueListType((ValueListType)value, diagnostics, context);
            case Wfs20Package.VALUE_REFERENCE_TYPE:
                return validateValueReferenceType((ValueReferenceType)value, diagnostics, context);
            case Wfs20Package.WFS_CAPABILITIES_TYPE:
                return validateWFSCapabilitiesType((WFSCapabilitiesType)value, diagnostics, context);
            case Wfs20Package.WSDL_TYPE:
                return validateWSDLType((WSDLType)value, diagnostics, context);
            case Wfs20Package.ALL_SOME_TYPE:
                return validateAllSomeType((AllSomeType)value, diagnostics, context);
            case Wfs20Package.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER0:
                return validateNonNegativeIntegerOrUnknownMember0((NonNegativeIntegerOrUnknownMember0)value, diagnostics, context);
            case Wfs20Package.RESOLVE_VALUE_TYPE:
                return validateResolveValueType((ResolveValueType)value, diagnostics, context);
            case Wfs20Package.RESULT_TYPE_TYPE:
                return validateResultTypeType((ResultTypeType)value, diagnostics, context);
            case Wfs20Package.STAR_STRING_TYPE:
                return validateStarStringType((StarStringType)value, diagnostics, context);
            case Wfs20Package.STATE_VALUE_TYPE_MEMBER0:
                return validateStateValueTypeMember0((StateValueTypeMember0)value, diagnostics, context);
            case Wfs20Package.UPDATE_ACTION_TYPE:
                return validateUpdateActionType((UpdateActionType)value, diagnostics, context);
            case Wfs20Package.ALL_SOME_TYPE_OBJECT:
                return validateAllSomeTypeObject((AllSomeType)value, diagnostics, context);
            case Wfs20Package.NON_NEGATIVE_INTEGER_OR_UNKNOWN:
                return validateNonNegativeIntegerOrUnknown(value, diagnostics, context);
            case Wfs20Package.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER0_OBJECT:
                return validateNonNegativeIntegerOrUnknownMember0Object((NonNegativeIntegerOrUnknownMember0)value, diagnostics, context);
            case Wfs20Package.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER1:
                return validateNonNegativeIntegerOrUnknownMember1((BigInteger)value, diagnostics, context);
            case Wfs20Package.POSITIVE_INTEGER_WITH_STAR:
                return validatePositiveIntegerWithStar(value, diagnostics, context);
            case Wfs20Package.RESOLVE_VALUE_TYPE_OBJECT:
                return validateResolveValueTypeObject((ResolveValueType)value, diagnostics, context);
            case Wfs20Package.RESULT_TYPE_TYPE_OBJECT:
                return validateResultTypeTypeObject((ResultTypeType)value, diagnostics, context);
            case Wfs20Package.RETURN_FEATURE_TYPES_LIST_TYPE:
                return validateReturnFeatureTypesListType((List<?>)value, diagnostics, context);
            case Wfs20Package.STAR_STRING_TYPE_OBJECT:
                return validateStarStringTypeObject((StarStringType)value, diagnostics, context);
            case Wfs20Package.STATE_VALUE_TYPE:
                return validateStateValueType(value, diagnostics, context);
            case Wfs20Package.STATE_VALUE_TYPE_MEMBER0_OBJECT:
                return validateStateValueTypeMember0Object((StateValueTypeMember0)value, diagnostics, context);
            case Wfs20Package.STATE_VALUE_TYPE_MEMBER1:
                return validateStateValueTypeMember1((String)value, diagnostics, context);
            case Wfs20Package.UPDATE_ACTION_TYPE_OBJECT:
                return validateUpdateActionTypeObject((UpdateActionType)value, diagnostics, context);
            case Wfs20Package.URI:
                return validateURI((URI)value, diagnostics, context);
            case Wfs20Package.MAP:
                return validateMap((Map)value, diagnostics, context);
            case Wfs20Package.FILTER:
                return validateFilter((Filter)value, diagnostics, context);
            case Wfs20Package.QNAME:
                return validateQName((QName)value, diagnostics, context);
            case Wfs20Package.SORT_BY:
                return validateSortBy((SortBy)value, diagnostics, context);
            case Wfs20Package.CALENDAR:
                return validateCalendar((Calendar)value, diagnostics, context);
            case Wfs20Package.FEATURE_COLLECTION:
                return validateFeatureCollection((FeatureCollection)value, diagnostics, context);
            case Wfs20Package.PROPERTY_NAME:
                return validatePropertyName((PropertyName)value, diagnostics, context);
            case Wfs20Package.FEATURE_ID:
                return validateFeatureId((FeatureId)value, diagnostics, context);
            default:
                return true;
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractTransactionActionType(AbstractTransactionActionType abstractTransactionActionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractTransactionActionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAbstractType(AbstractType abstractType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(abstractType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateActionResultsType(ActionResultsType actionResultsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(actionResultsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAdditionalObjectsType(AdditionalObjectsType additionalObjectsType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(additionalObjectsType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAdditionalValuesType(AdditionalValuesType additionalValuesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(additionalValuesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateBaseRequestType(BaseRequestType baseRequestType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(baseRequestType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCreatedOrModifiedFeatureType(CreatedOrModifiedFeatureType createdOrModifiedFeatureType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(createdOrModifiedFeatureType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCreateStoredQueryResponseType(CreateStoredQueryResponseType createStoredQueryResponseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(createStoredQueryResponseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCreateStoredQueryType(CreateStoredQueryType createStoredQueryType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(createStoredQueryType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDeleteType(DeleteType deleteType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(deleteType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDescribeFeatureTypeType(DescribeFeatureTypeType describeFeatureTypeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(describeFeatureTypeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDescribeStoredQueriesResponseType(DescribeStoredQueriesResponseType describeStoredQueriesResponseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(describeStoredQueriesResponseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateDescribeStoredQueriesType(DescribeStoredQueriesType describeStoredQueriesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(describeStoredQueriesType, diagnostics, context);
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
    public boolean validateDropStoredQueryType(DropStoredQueryType dropStoredQueryType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(dropStoredQueryType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateElementType(ElementType elementType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(elementType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEmptyType(EmptyType emptyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(emptyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateEnvelopePropertyType(EnvelopePropertyType envelopePropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(envelopePropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateExecutionStatusType(ExecutionStatusType executionStatusType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(executionStatusType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateExtendedDescriptionType(ExtendedDescriptionType extendedDescriptionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(extendedDescriptionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFeatureCollectionType(FeatureCollectionType featureCollectionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(featureCollectionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFeaturesLockedType(FeaturesLockedType featuresLockedType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(featuresLockedType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFeaturesNotLockedType(FeaturesNotLockedType featuresNotLockedType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(featuresNotLockedType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFeatureTypeListType(FeatureTypeListType featureTypeListType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(featureTypeListType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFeatureTypeType(FeatureTypeType featureTypeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(featureTypeType, diagnostics, context);
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
    public boolean validateGetFeatureType(GetFeatureType getFeatureType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(getFeatureType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGetFeatureWithLockType(GetFeatureWithLockType getFeatureWithLockType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(getFeatureWithLockType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateGetPropertyValueType(GetPropertyValueType getPropertyValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(getPropertyValueType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateInsertType(InsertType insertType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(insertType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateListStoredQueriesResponseType(ListStoredQueriesResponseType listStoredQueriesResponseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(listStoredQueriesResponseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateListStoredQueriesType(ListStoredQueriesType listStoredQueriesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(listStoredQueriesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLockFeatureResponseType(LockFeatureResponseType lockFeatureResponseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(lockFeatureResponseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateLockFeatureType(LockFeatureType lockFeatureType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(lockFeatureType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMemberPropertyType(MemberPropertyType memberPropertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(memberPropertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMetadataURLType(MetadataURLType metadataURLType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(metadataURLType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNativeType(NativeType nativeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(nativeType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNoCRSType(NoCRSType noCRSType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(noCRSType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateOutputFormatListType(OutputFormatListType outputFormatListType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(outputFormatListType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateParameterExpressionType(ParameterExpressionType parameterExpressionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(parameterExpressionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateParameterType(ParameterType parameterType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(parameterType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePropertyNameType(PropertyNameType propertyNameType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(propertyNameType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePropertyType(PropertyType propertyType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(propertyType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateQueryExpressionTextType(QueryExpressionTextType queryExpressionTextType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(queryExpressionTextType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateQueryType(QueryType queryType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(queryType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateReplaceType(ReplaceType replaceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(replaceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSimpleFeatureCollectionType(SimpleFeatureCollectionType simpleFeatureCollectionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(simpleFeatureCollectionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStoredQueryDescriptionType(StoredQueryDescriptionType storedQueryDescriptionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(storedQueryDescriptionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStoredQueryListItemType(StoredQueryListItemType storedQueryListItemType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(storedQueryListItemType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStoredQueryType(StoredQueryType storedQueryType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(storedQueryType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTitleType(TitleType titleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(titleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTransactionResponseType(TransactionResponseType transactionResponseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(transactionResponseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTransactionSummaryType(TransactionSummaryType transactionSummaryType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(transactionSummaryType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTransactionType(TransactionType transactionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(transactionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTruncatedResponseType(TruncatedResponseType truncatedResponseType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(truncatedResponseType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateTupleType(TupleType tupleType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(tupleType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUpdateType(UpdateType updateType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(updateType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateValueCollectionType(ValueCollectionType valueCollectionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(valueCollectionType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateValueListType(ValueListType valueListType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(valueListType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateValueReferenceType(ValueReferenceType valueReferenceType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(valueReferenceType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateWFSCapabilitiesType(WFSCapabilitiesType wfsCapabilitiesType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(wfsCapabilitiesType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateWSDLType(WSDLType wsdlType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validate_EveryDefaultConstraint(wsdlType, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAllSomeType(AllSomeType allSomeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNonNegativeIntegerOrUnknownMember0(NonNegativeIntegerOrUnknownMember0 nonNegativeIntegerOrUnknownMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateResolveValueType(ResolveValueType resolveValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateResultTypeType(ResultTypeType resultTypeType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStarStringType(StarStringType starStringType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStateValueTypeMember0(StateValueTypeMember0 stateValueTypeMember0, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUpdateActionType(UpdateActionType updateActionType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateAllSomeTypeObject(AllSomeType allSomeTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNonNegativeIntegerOrUnknown(Object nonNegativeIntegerOrUnknown, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateNonNegativeIntegerOrUnknown_MemberTypes(nonNegativeIntegerOrUnknown, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Non Negative Integer Or Unknown</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNonNegativeIntegerOrUnknown_MemberTypes(Object nonNegativeIntegerOrUnknown, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Wfs20Package.Literals.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER0.isInstance(nonNegativeIntegerOrUnknown)) {
                if (validateNonNegativeIntegerOrUnknownMember0((NonNegativeIntegerOrUnknownMember0)nonNegativeIntegerOrUnknown, tempDiagnostics, context)) return true;
            }
            if (Wfs20Package.Literals.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER1.isInstance(nonNegativeIntegerOrUnknown)) {
                if (validateNonNegativeIntegerOrUnknownMember1((BigInteger)nonNegativeIntegerOrUnknown, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Wfs20Package.Literals.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER0.isInstance(nonNegativeIntegerOrUnknown)) {
                if (validateNonNegativeIntegerOrUnknownMember0((NonNegativeIntegerOrUnknownMember0)nonNegativeIntegerOrUnknown, null, context)) return true;
            }
            if (Wfs20Package.Literals.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER1.isInstance(nonNegativeIntegerOrUnknown)) {
                if (validateNonNegativeIntegerOrUnknownMember1((BigInteger)nonNegativeIntegerOrUnknown, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNonNegativeIntegerOrUnknownMember0Object(NonNegativeIntegerOrUnknownMember0 nonNegativeIntegerOrUnknownMember0Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateNonNegativeIntegerOrUnknownMember1(BigInteger nonNegativeIntegerOrUnknownMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = xmlTypeValidator.validateNonNegativeInteger_Min(nonNegativeIntegerOrUnknownMember1, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePositiveIntegerWithStar(Object positiveIntegerWithStar, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validatePositiveIntegerWithStar_MemberTypes(positiveIntegerWithStar, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>Positive Integer With Star</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePositiveIntegerWithStar_MemberTypes(Object positiveIntegerWithStar, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (XMLTypePackage.Literals.POSITIVE_INTEGER.isInstance(positiveIntegerWithStar)) {
                if (xmlTypeValidator.validatePositiveInteger((BigInteger)positiveIntegerWithStar, tempDiagnostics, context)) return true;
            }
            if (Wfs20Package.Literals.STAR_STRING_TYPE.isInstance(positiveIntegerWithStar)) {
                if (validateStarStringType((StarStringType)positiveIntegerWithStar, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (XMLTypePackage.Literals.POSITIVE_INTEGER.isInstance(positiveIntegerWithStar)) {
                if (xmlTypeValidator.validatePositiveInteger((BigInteger)positiveIntegerWithStar, null, context)) return true;
            }
            if (Wfs20Package.Literals.STAR_STRING_TYPE.isInstance(positiveIntegerWithStar)) {
                if (validateStarStringType((StarStringType)positiveIntegerWithStar, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateResolveValueTypeObject(ResolveValueType resolveValueTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateResultTypeTypeObject(ResultTypeType resultTypeTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateReturnFeatureTypesListType(List<?> returnFeatureTypesListType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateReturnFeatureTypesListType_ItemType(returnFeatureTypesListType, diagnostics, context);
        return result;
    }

    /**
     * Validates the ItemType constraint of '<em>Return Feature Types List Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateReturnFeatureTypesListType_ItemType(List<?> returnFeatureTypesListType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = true;
        for (Iterator<?> i = returnFeatureTypesListType.iterator(); i.hasNext() && (result || diagnostics != null); ) {
            Object item = i.next();
            if (XMLTypePackage.Literals.QNAME.isInstance(item)) {
                result &= xmlTypeValidator.validateQName((QName)item, diagnostics, context);
            }
            else {
                result = false;
                reportDataValueTypeViolation(XMLTypePackage.Literals.QNAME, item, diagnostics, context);
            }
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStarStringTypeObject(StarStringType starStringTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStateValueType(Object stateValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateStateValueType_MemberTypes(stateValueType, diagnostics, context);
        return result;
    }

    /**
     * Validates the MemberTypes constraint of '<em>State Value Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStateValueType_MemberTypes(Object stateValueType, DiagnosticChain diagnostics, Map<Object, Object> context) {
        if (diagnostics != null) {
            BasicDiagnostic tempDiagnostics = new BasicDiagnostic();
            if (Wfs20Package.Literals.STATE_VALUE_TYPE_MEMBER0.isInstance(stateValueType)) {
                if (validateStateValueTypeMember0((StateValueTypeMember0)stateValueType, tempDiagnostics, context)) return true;
            }
            if (Wfs20Package.Literals.STATE_VALUE_TYPE_MEMBER1.isInstance(stateValueType)) {
                if (validateStateValueTypeMember1((String)stateValueType, tempDiagnostics, context)) return true;
            }
            for (Diagnostic diagnostic : tempDiagnostics.getChildren()) {
                diagnostics.add(diagnostic);
            }
        }
        else {
            if (Wfs20Package.Literals.STATE_VALUE_TYPE_MEMBER0.isInstance(stateValueType)) {
                if (validateStateValueTypeMember0((StateValueTypeMember0)stateValueType, null, context)) return true;
            }
            if (Wfs20Package.Literals.STATE_VALUE_TYPE_MEMBER1.isInstance(stateValueType)) {
                if (validateStateValueTypeMember1((String)stateValueType, null, context)) return true;
            }
        }
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStateValueTypeMember0Object(StateValueTypeMember0 stateValueTypeMember0Object, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStateValueTypeMember1(String stateValueTypeMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean result = validateStateValueTypeMember1_Pattern(stateValueTypeMember1, diagnostics, context);
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @see #validateStateValueTypeMember1_Pattern
     */
    public static final  PatternMatcher [][] STATE_VALUE_TYPE_MEMBER1__PATTERN__VALUES =
        new PatternMatcher [][] {
            new PatternMatcher [] {
                XMLTypeUtil.createPatternMatcher("other:\\w{2,}")
            }
        };

    /**
     * Validates the Pattern constraint of '<em>State Value Type Member1</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateStateValueTypeMember1_Pattern(String stateValueTypeMember1, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return validatePattern(Wfs20Package.Literals.STATE_VALUE_TYPE_MEMBER1, stateValueTypeMember1, STATE_VALUE_TYPE_MEMBER1__PATTERN__VALUES, diagnostics, context);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateUpdateActionTypeObject(UpdateActionType updateActionTypeObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateURI(URI uri, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateMap(Map map, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFilter(Filter filter, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateQName(QName qName, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateSortBy(SortBy sortBy, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateCalendar(Calendar calendar, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFeatureCollection(FeatureCollection featureCollection, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validatePropertyName(PropertyName propertyName, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean validateFeatureId(FeatureId featureId, DiagnosticChain diagnostics, Map<Object, Object> context) {
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

} //Wfs20Validator
