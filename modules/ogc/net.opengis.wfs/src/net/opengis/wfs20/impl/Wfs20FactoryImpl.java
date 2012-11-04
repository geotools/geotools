/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.math.BigInteger;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import java.util.Map;
import javax.xml.namespace.QName;

import net.opengis.wfs20.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecore.util.Diagnostician;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.geotools.feature.FeatureCollection;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.sort.SortBy;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Wfs20FactoryImpl extends EFactoryImpl implements Wfs20Factory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static Wfs20Factory init() {
        try {
            Wfs20Factory theWfs20Factory = (Wfs20Factory)EPackage.Registry.INSTANCE.getEFactory("http://www.opengis.net/wfs/2.0"); 
            if (theWfs20Factory != null) {
                return theWfs20Factory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new Wfs20FactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wfs20FactoryImpl() {
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
            case Wfs20Package.ABSTRACT_TYPE: return createAbstractType();
            case Wfs20Package.ACTION_RESULTS_TYPE: return createActionResultsType();
            case Wfs20Package.ADDITIONAL_OBJECTS_TYPE: return createAdditionalObjectsType();
            case Wfs20Package.ADDITIONAL_VALUES_TYPE: return createAdditionalValuesType();
            case Wfs20Package.CREATED_OR_MODIFIED_FEATURE_TYPE: return createCreatedOrModifiedFeatureType();
            case Wfs20Package.CREATE_STORED_QUERY_RESPONSE_TYPE: return createCreateStoredQueryResponseType();
            case Wfs20Package.CREATE_STORED_QUERY_TYPE: return createCreateStoredQueryType();
            case Wfs20Package.DELETE_TYPE: return createDeleteType();
            case Wfs20Package.DESCRIBE_FEATURE_TYPE_TYPE: return createDescribeFeatureTypeType();
            case Wfs20Package.DESCRIBE_STORED_QUERIES_RESPONSE_TYPE: return createDescribeStoredQueriesResponseType();
            case Wfs20Package.DESCRIBE_STORED_QUERIES_TYPE: return createDescribeStoredQueriesType();
            case Wfs20Package.DOCUMENT_ROOT: return createDocumentRoot();
            case Wfs20Package.DROP_STORED_QUERY_TYPE: return createDropStoredQueryType();
            case Wfs20Package.ELEMENT_TYPE: return createElementType();
            case Wfs20Package.EMPTY_TYPE: return createEmptyType();
            case Wfs20Package.ENVELOPE_PROPERTY_TYPE: return createEnvelopePropertyType();
            case Wfs20Package.EXECUTION_STATUS_TYPE: return createExecutionStatusType();
            case Wfs20Package.EXTENDED_DESCRIPTION_TYPE: return createExtendedDescriptionType();
            case Wfs20Package.FEATURE_COLLECTION_TYPE: return createFeatureCollectionType();
            case Wfs20Package.FEATURES_LOCKED_TYPE: return createFeaturesLockedType();
            case Wfs20Package.FEATURES_NOT_LOCKED_TYPE: return createFeaturesNotLockedType();
            case Wfs20Package.FEATURE_TYPE_LIST_TYPE: return createFeatureTypeListType();
            case Wfs20Package.FEATURE_TYPE_TYPE: return createFeatureTypeType();
            case Wfs20Package.GET_CAPABILITIES_TYPE: return createGetCapabilitiesType();
            case Wfs20Package.GET_FEATURE_TYPE: return createGetFeatureType();
            case Wfs20Package.GET_FEATURE_WITH_LOCK_TYPE: return createGetFeatureWithLockType();
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE: return createGetPropertyValueType();
            case Wfs20Package.INSERT_TYPE: return createInsertType();
            case Wfs20Package.LIST_STORED_QUERIES_RESPONSE_TYPE: return createListStoredQueriesResponseType();
            case Wfs20Package.LIST_STORED_QUERIES_TYPE: return createListStoredQueriesType();
            case Wfs20Package.LOCK_FEATURE_RESPONSE_TYPE: return createLockFeatureResponseType();
            case Wfs20Package.LOCK_FEATURE_TYPE: return createLockFeatureType();
            case Wfs20Package.MEMBER_PROPERTY_TYPE: return createMemberPropertyType();
            case Wfs20Package.METADATA_URL_TYPE: return createMetadataURLType();
            case Wfs20Package.NATIVE_TYPE: return createNativeType();
            case Wfs20Package.NO_CRS_TYPE: return createNoCRSType();
            case Wfs20Package.OUTPUT_FORMAT_LIST_TYPE: return createOutputFormatListType();
            case Wfs20Package.PARAMETER_EXPRESSION_TYPE: return createParameterExpressionType();
            case Wfs20Package.PARAMETER_TYPE: return createParameterType();
            case Wfs20Package.PROPERTY_NAME_TYPE: return createPropertyNameType();
            case Wfs20Package.PROPERTY_TYPE: return createPropertyType();
            case Wfs20Package.QUERY_EXPRESSION_TEXT_TYPE: return createQueryExpressionTextType();
            case Wfs20Package.QUERY_TYPE: return createQueryType();
            case Wfs20Package.REPLACE_TYPE: return createReplaceType();
            case Wfs20Package.SIMPLE_FEATURE_COLLECTION_TYPE: return createSimpleFeatureCollectionType();
            case Wfs20Package.STORED_QUERY_DESCRIPTION_TYPE: return createStoredQueryDescriptionType();
            case Wfs20Package.STORED_QUERY_LIST_ITEM_TYPE: return createStoredQueryListItemType();
            case Wfs20Package.STORED_QUERY_TYPE: return createStoredQueryType();
            case Wfs20Package.TITLE_TYPE: return createTitleType();
            case Wfs20Package.TRANSACTION_RESPONSE_TYPE: return createTransactionResponseType();
            case Wfs20Package.TRANSACTION_SUMMARY_TYPE: return createTransactionSummaryType();
            case Wfs20Package.TRANSACTION_TYPE: return createTransactionType();
            case Wfs20Package.TRUNCATED_RESPONSE_TYPE: return createTruncatedResponseType();
            case Wfs20Package.TUPLE_TYPE: return createTupleType();
            case Wfs20Package.UPDATE_TYPE: return createUpdateType();
            case Wfs20Package.VALUE_COLLECTION_TYPE: return createValueCollectionType();
            case Wfs20Package.VALUE_LIST_TYPE: return createValueListType();
            case Wfs20Package.VALUE_REFERENCE_TYPE: return createValueReferenceType();
            case Wfs20Package.WFS_CAPABILITIES_TYPE: return createWFSCapabilitiesType();
            case Wfs20Package.WSDL_TYPE: return createWSDLType();
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
            case Wfs20Package.ALL_SOME_TYPE:
                return createAllSomeTypeFromString(eDataType, initialValue);
            case Wfs20Package.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER0:
                return createNonNegativeIntegerOrUnknownMember0FromString(eDataType, initialValue);
            case Wfs20Package.RESOLVE_VALUE_TYPE:
                return createResolveValueTypeFromString(eDataType, initialValue);
            case Wfs20Package.RESULT_TYPE_TYPE:
                return createResultTypeTypeFromString(eDataType, initialValue);
            case Wfs20Package.STAR_STRING_TYPE:
                return createStarStringTypeFromString(eDataType, initialValue);
            case Wfs20Package.STATE_VALUE_TYPE_MEMBER0:
                return createStateValueTypeMember0FromString(eDataType, initialValue);
            case Wfs20Package.UPDATE_ACTION_TYPE:
                return createUpdateActionTypeFromString(eDataType, initialValue);
            case Wfs20Package.ALL_SOME_TYPE_OBJECT:
                return createAllSomeTypeObjectFromString(eDataType, initialValue);
            case Wfs20Package.NON_NEGATIVE_INTEGER_OR_UNKNOWN:
                return createNonNegativeIntegerOrUnknownFromString(eDataType, initialValue);
            case Wfs20Package.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER0_OBJECT:
                return createNonNegativeIntegerOrUnknownMember0ObjectFromString(eDataType, initialValue);
            case Wfs20Package.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER1:
                return createNonNegativeIntegerOrUnknownMember1FromString(eDataType, initialValue);
            case Wfs20Package.POSITIVE_INTEGER_WITH_STAR:
                return createPositiveIntegerWithStarFromString(eDataType, initialValue);
            case Wfs20Package.RESOLVE_VALUE_TYPE_OBJECT:
                return createResolveValueTypeObjectFromString(eDataType, initialValue);
            case Wfs20Package.RESULT_TYPE_TYPE_OBJECT:
                return createResultTypeTypeObjectFromString(eDataType, initialValue);
            case Wfs20Package.RETURN_FEATURE_TYPES_LIST_TYPE:
                return createReturnFeatureTypesListTypeFromString(eDataType, initialValue);
            case Wfs20Package.STAR_STRING_TYPE_OBJECT:
                return createStarStringTypeObjectFromString(eDataType, initialValue);
            case Wfs20Package.STATE_VALUE_TYPE:
                return createStateValueTypeFromString(eDataType, initialValue);
            case Wfs20Package.STATE_VALUE_TYPE_MEMBER0_OBJECT:
                return createStateValueTypeMember0ObjectFromString(eDataType, initialValue);
            case Wfs20Package.STATE_VALUE_TYPE_MEMBER1:
                return createStateValueTypeMember1FromString(eDataType, initialValue);
            case Wfs20Package.UPDATE_ACTION_TYPE_OBJECT:
                return createUpdateActionTypeObjectFromString(eDataType, initialValue);
            case Wfs20Package.URI:
                return createURIFromString(eDataType, initialValue);
            case Wfs20Package.MAP:
                return createMapFromString(eDataType, initialValue);
            case Wfs20Package.FILTER:
                return createFilterFromString(eDataType, initialValue);
            case Wfs20Package.QNAME:
                return createQNameFromString(eDataType, initialValue);
            case Wfs20Package.SORT_BY:
                return createSortByFromString(eDataType, initialValue);
            case Wfs20Package.CALENDAR:
                return createCalendarFromString(eDataType, initialValue);
            case Wfs20Package.FEATURE_COLLECTION:
                return createFeatureCollectionFromString(eDataType, initialValue);
            case Wfs20Package.PROPERTY_NAME:
                return createPropertyNameFromString(eDataType, initialValue);
            case Wfs20Package.FEATURE_ID:
                return createFeatureIdFromString(eDataType, initialValue);
            case Wfs20Package.COLLECTION:
                return createCollectionFromString(eDataType, initialValue);
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
            case Wfs20Package.ALL_SOME_TYPE:
                return convertAllSomeTypeToString(eDataType, instanceValue);
            case Wfs20Package.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER0:
                return convertNonNegativeIntegerOrUnknownMember0ToString(eDataType, instanceValue);
            case Wfs20Package.RESOLVE_VALUE_TYPE:
                return convertResolveValueTypeToString(eDataType, instanceValue);
            case Wfs20Package.RESULT_TYPE_TYPE:
                return convertResultTypeTypeToString(eDataType, instanceValue);
            case Wfs20Package.STAR_STRING_TYPE:
                return convertStarStringTypeToString(eDataType, instanceValue);
            case Wfs20Package.STATE_VALUE_TYPE_MEMBER0:
                return convertStateValueTypeMember0ToString(eDataType, instanceValue);
            case Wfs20Package.UPDATE_ACTION_TYPE:
                return convertUpdateActionTypeToString(eDataType, instanceValue);
            case Wfs20Package.ALL_SOME_TYPE_OBJECT:
                return convertAllSomeTypeObjectToString(eDataType, instanceValue);
            case Wfs20Package.NON_NEGATIVE_INTEGER_OR_UNKNOWN:
                return convertNonNegativeIntegerOrUnknownToString(eDataType, instanceValue);
            case Wfs20Package.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER0_OBJECT:
                return convertNonNegativeIntegerOrUnknownMember0ObjectToString(eDataType, instanceValue);
            case Wfs20Package.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER1:
                return convertNonNegativeIntegerOrUnknownMember1ToString(eDataType, instanceValue);
            case Wfs20Package.POSITIVE_INTEGER_WITH_STAR:
                return convertPositiveIntegerWithStarToString(eDataType, instanceValue);
            case Wfs20Package.RESOLVE_VALUE_TYPE_OBJECT:
                return convertResolveValueTypeObjectToString(eDataType, instanceValue);
            case Wfs20Package.RESULT_TYPE_TYPE_OBJECT:
                return convertResultTypeTypeObjectToString(eDataType, instanceValue);
            case Wfs20Package.RETURN_FEATURE_TYPES_LIST_TYPE:
                return convertReturnFeatureTypesListTypeToString(eDataType, instanceValue);
            case Wfs20Package.STAR_STRING_TYPE_OBJECT:
                return convertStarStringTypeObjectToString(eDataType, instanceValue);
            case Wfs20Package.STATE_VALUE_TYPE:
                return convertStateValueTypeToString(eDataType, instanceValue);
            case Wfs20Package.STATE_VALUE_TYPE_MEMBER0_OBJECT:
                return convertStateValueTypeMember0ObjectToString(eDataType, instanceValue);
            case Wfs20Package.STATE_VALUE_TYPE_MEMBER1:
                return convertStateValueTypeMember1ToString(eDataType, instanceValue);
            case Wfs20Package.UPDATE_ACTION_TYPE_OBJECT:
                return convertUpdateActionTypeObjectToString(eDataType, instanceValue);
            case Wfs20Package.URI:
                return convertURIToString(eDataType, instanceValue);
            case Wfs20Package.MAP:
                return convertMapToString(eDataType, instanceValue);
            case Wfs20Package.FILTER:
                return convertFilterToString(eDataType, instanceValue);
            case Wfs20Package.QNAME:
                return convertQNameToString(eDataType, instanceValue);
            case Wfs20Package.SORT_BY:
                return convertSortByToString(eDataType, instanceValue);
            case Wfs20Package.CALENDAR:
                return convertCalendarToString(eDataType, instanceValue);
            case Wfs20Package.FEATURE_COLLECTION:
                return convertFeatureCollectionToString(eDataType, instanceValue);
            case Wfs20Package.PROPERTY_NAME:
                return convertPropertyNameToString(eDataType, instanceValue);
            case Wfs20Package.FEATURE_ID:
                return convertFeatureIdToString(eDataType, instanceValue);
            case Wfs20Package.COLLECTION:
                return convertCollectionToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractType createAbstractType() {
        AbstractTypeImpl abstractType = new AbstractTypeImpl();
        return abstractType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ActionResultsType createActionResultsType() {
        ActionResultsTypeImpl actionResultsType = new ActionResultsTypeImpl();
        return actionResultsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AdditionalObjectsType createAdditionalObjectsType() {
        AdditionalObjectsTypeImpl additionalObjectsType = new AdditionalObjectsTypeImpl();
        return additionalObjectsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AdditionalValuesType createAdditionalValuesType() {
        AdditionalValuesTypeImpl additionalValuesType = new AdditionalValuesTypeImpl();
        return additionalValuesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CreatedOrModifiedFeatureType createCreatedOrModifiedFeatureType() {
        CreatedOrModifiedFeatureTypeImpl createdOrModifiedFeatureType = new CreatedOrModifiedFeatureTypeImpl();
        return createdOrModifiedFeatureType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CreateStoredQueryResponseType createCreateStoredQueryResponseType() {
        CreateStoredQueryResponseTypeImpl createStoredQueryResponseType = new CreateStoredQueryResponseTypeImpl();
        return createStoredQueryResponseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CreateStoredQueryType createCreateStoredQueryType() {
        CreateStoredQueryTypeImpl createStoredQueryType = new CreateStoredQueryTypeImpl();
        return createStoredQueryType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DeleteType createDeleteType() {
        DeleteTypeImpl deleteType = new DeleteTypeImpl();
        return deleteType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DescribeFeatureTypeType createDescribeFeatureTypeType() {
        DescribeFeatureTypeTypeImpl describeFeatureTypeType = new DescribeFeatureTypeTypeImpl();
        return describeFeatureTypeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DescribeStoredQueriesResponseType createDescribeStoredQueriesResponseType() {
        DescribeStoredQueriesResponseTypeImpl describeStoredQueriesResponseType = new DescribeStoredQueriesResponseTypeImpl();
        return describeStoredQueriesResponseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DescribeStoredQueriesType createDescribeStoredQueriesType() {
        DescribeStoredQueriesTypeImpl describeStoredQueriesType = new DescribeStoredQueriesTypeImpl();
        return describeStoredQueriesType;
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
    public DropStoredQueryType createDropStoredQueryType() {
        DropStoredQueryTypeImpl dropStoredQueryType = new DropStoredQueryTypeImpl();
        return dropStoredQueryType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ElementType createElementType() {
        ElementTypeImpl elementType = new ElementTypeImpl();
        return elementType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EmptyType createEmptyType() {
        EmptyTypeImpl emptyType = new EmptyTypeImpl();
        return emptyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EnvelopePropertyType createEnvelopePropertyType() {
        EnvelopePropertyTypeImpl envelopePropertyType = new EnvelopePropertyTypeImpl();
        return envelopePropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExecutionStatusType createExecutionStatusType() {
        ExecutionStatusTypeImpl executionStatusType = new ExecutionStatusTypeImpl();
        return executionStatusType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExtendedDescriptionType createExtendedDescriptionType() {
        ExtendedDescriptionTypeImpl extendedDescriptionType = new ExtendedDescriptionTypeImpl();
        return extendedDescriptionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureCollectionType createFeatureCollectionType() {
        FeatureCollectionTypeImpl featureCollectionType = new FeatureCollectionTypeImpl();
        return featureCollectionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeaturesLockedType createFeaturesLockedType() {
        FeaturesLockedTypeImpl featuresLockedType = new FeaturesLockedTypeImpl();
        return featuresLockedType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeaturesNotLockedType createFeaturesNotLockedType() {
        FeaturesNotLockedTypeImpl featuresNotLockedType = new FeaturesNotLockedTypeImpl();
        return featuresNotLockedType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureTypeListType createFeatureTypeListType() {
        FeatureTypeListTypeImpl featureTypeListType = new FeatureTypeListTypeImpl();
        return featureTypeListType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureTypeType createFeatureTypeType() {
        FeatureTypeTypeImpl featureTypeType = new FeatureTypeTypeImpl();
        return featureTypeType;
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
    public GetFeatureType createGetFeatureType() {
        GetFeatureTypeImpl getFeatureType = new GetFeatureTypeImpl();
        return getFeatureType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetFeatureWithLockType createGetFeatureWithLockType() {
        GetFeatureWithLockTypeImpl getFeatureWithLockType = new GetFeatureWithLockTypeImpl();
        return getFeatureWithLockType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetPropertyValueType createGetPropertyValueType() {
        GetPropertyValueTypeImpl getPropertyValueType = new GetPropertyValueTypeImpl();
        return getPropertyValueType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InsertType createInsertType() {
        InsertTypeImpl insertType = new InsertTypeImpl();
        return insertType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ListStoredQueriesResponseType createListStoredQueriesResponseType() {
        ListStoredQueriesResponseTypeImpl listStoredQueriesResponseType = new ListStoredQueriesResponseTypeImpl();
        return listStoredQueriesResponseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ListStoredQueriesType createListStoredQueriesType() {
        ListStoredQueriesTypeImpl listStoredQueriesType = new ListStoredQueriesTypeImpl();
        return listStoredQueriesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LockFeatureResponseType createLockFeatureResponseType() {
        LockFeatureResponseTypeImpl lockFeatureResponseType = new LockFeatureResponseTypeImpl();
        return lockFeatureResponseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LockFeatureType createLockFeatureType() {
        LockFeatureTypeImpl lockFeatureType = new LockFeatureTypeImpl();
        return lockFeatureType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MemberPropertyType createMemberPropertyType() {
        MemberPropertyTypeImpl memberPropertyType = new MemberPropertyTypeImpl();
        return memberPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MetadataURLType createMetadataURLType() {
        MetadataURLTypeImpl metadataURLType = new MetadataURLTypeImpl();
        return metadataURLType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NativeType createNativeType() {
        NativeTypeImpl nativeType = new NativeTypeImpl();
        return nativeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NoCRSType createNoCRSType() {
        NoCRSTypeImpl noCRSType = new NoCRSTypeImpl();
        return noCRSType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OutputFormatListType createOutputFormatListType() {
        OutputFormatListTypeImpl outputFormatListType = new OutputFormatListTypeImpl();
        return outputFormatListType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ParameterExpressionType createParameterExpressionType() {
        ParameterExpressionTypeImpl parameterExpressionType = new ParameterExpressionTypeImpl();
        return parameterExpressionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ParameterType createParameterType() {
        ParameterTypeImpl parameterType = new ParameterTypeImpl();
        return parameterType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PropertyNameType createPropertyNameType() {
        PropertyNameTypeImpl propertyNameType = new PropertyNameTypeImpl();
        return propertyNameType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PropertyType createPropertyType() {
        PropertyTypeImpl propertyType = new PropertyTypeImpl();
        return propertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QueryExpressionTextType createQueryExpressionTextType() {
        QueryExpressionTextTypeImpl queryExpressionTextType = new QueryExpressionTextTypeImpl();
        return queryExpressionTextType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QueryType createQueryType() {
        QueryTypeImpl queryType = new QueryTypeImpl();
        return queryType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ReplaceType createReplaceType() {
        ReplaceTypeImpl replaceType = new ReplaceTypeImpl();
        return replaceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SimpleFeatureCollectionType createSimpleFeatureCollectionType() {
        SimpleFeatureCollectionTypeImpl simpleFeatureCollectionType = new SimpleFeatureCollectionTypeImpl();
        return simpleFeatureCollectionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StoredQueryDescriptionType createStoredQueryDescriptionType() {
        StoredQueryDescriptionTypeImpl storedQueryDescriptionType = new StoredQueryDescriptionTypeImpl();
        return storedQueryDescriptionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StoredQueryListItemType createStoredQueryListItemType() {
        StoredQueryListItemTypeImpl storedQueryListItemType = new StoredQueryListItemTypeImpl();
        return storedQueryListItemType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StoredQueryType createStoredQueryType() {
        StoredQueryTypeImpl storedQueryType = new StoredQueryTypeImpl();
        return storedQueryType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TitleType createTitleType() {
        TitleTypeImpl titleType = new TitleTypeImpl();
        return titleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TransactionResponseType createTransactionResponseType() {
        TransactionResponseTypeImpl transactionResponseType = new TransactionResponseTypeImpl();
        return transactionResponseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TransactionSummaryType createTransactionSummaryType() {
        TransactionSummaryTypeImpl transactionSummaryType = new TransactionSummaryTypeImpl();
        return transactionSummaryType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TransactionType createTransactionType() {
        TransactionTypeImpl transactionType = new TransactionTypeImpl();
        return transactionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TruncatedResponseType createTruncatedResponseType() {
        TruncatedResponseTypeImpl truncatedResponseType = new TruncatedResponseTypeImpl();
        return truncatedResponseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TupleType createTupleType() {
        TupleTypeImpl tupleType = new TupleTypeImpl();
        return tupleType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UpdateType createUpdateType() {
        UpdateTypeImpl updateType = new UpdateTypeImpl();
        return updateType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueCollectionType createValueCollectionType() {
        ValueCollectionTypeImpl valueCollectionType = new ValueCollectionTypeImpl();
        return valueCollectionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueListType createValueListType() {
        ValueListTypeImpl valueListType = new ValueListTypeImpl();
        return valueListType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueReferenceType createValueReferenceType() {
        ValueReferenceTypeImpl valueReferenceType = new ValueReferenceTypeImpl();
        return valueReferenceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WFSCapabilitiesType createWFSCapabilitiesType() {
        WFSCapabilitiesTypeImpl wfsCapabilitiesType = new WFSCapabilitiesTypeImpl();
        return wfsCapabilitiesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WSDLType createWSDLType() {
        WSDLTypeImpl wsdlType = new WSDLTypeImpl();
        return wsdlType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllSomeType createAllSomeTypeFromString(EDataType eDataType, String initialValue) {
        AllSomeType result = AllSomeType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertAllSomeTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NonNegativeIntegerOrUnknownMember0 createNonNegativeIntegerOrUnknownMember0FromString(EDataType eDataType, String initialValue) {
        NonNegativeIntegerOrUnknownMember0 result = NonNegativeIntegerOrUnknownMember0.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertNonNegativeIntegerOrUnknownMember0ToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResolveValueType createResolveValueTypeFromString(EDataType eDataType, String initialValue) {
        ResolveValueType result = ResolveValueType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertResolveValueTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResultTypeType createResultTypeTypeFromString(EDataType eDataType, String initialValue) {
        ResultTypeType result = ResultTypeType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertResultTypeTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StarStringType createStarStringTypeFromString(EDataType eDataType, String initialValue) {
        StarStringType result = StarStringType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertStarStringTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StateValueTypeMember0 createStateValueTypeMember0FromString(EDataType eDataType, String initialValue) {
        StateValueTypeMember0 result = StateValueTypeMember0.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertStateValueTypeMember0ToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UpdateActionType createUpdateActionTypeFromString(EDataType eDataType, String initialValue) {
        UpdateActionType result = UpdateActionType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertUpdateActionTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllSomeType createAllSomeTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createAllSomeTypeFromString(Wfs20Package.Literals.ALL_SOME_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertAllSomeTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertAllSomeTypeToString(Wfs20Package.Literals.ALL_SOME_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createNonNegativeIntegerOrUnknownFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        Object result = null;
        RuntimeException exception = null;
        try {
            result = createNonNegativeIntegerOrUnknownMember0FromString(Wfs20Package.Literals.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER0, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = createNonNegativeIntegerOrUnknownMember1FromString(Wfs20Package.Literals.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER1, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        if (result != null || exception == null) return result;
    
        throw exception;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertNonNegativeIntegerOrUnknownToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        if (Wfs20Package.Literals.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER0.isInstance(instanceValue)) {
            try {
                String value = convertNonNegativeIntegerOrUnknownMember0ToString(Wfs20Package.Literals.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER0, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (Wfs20Package.Literals.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER1.isInstance(instanceValue)) {
            try {
                String value = convertNonNegativeIntegerOrUnknownMember1ToString(Wfs20Package.Literals.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER1, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NonNegativeIntegerOrUnknownMember0 createNonNegativeIntegerOrUnknownMember0ObjectFromString(EDataType eDataType, String initialValue) {
        return createNonNegativeIntegerOrUnknownMember0FromString(Wfs20Package.Literals.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER0, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertNonNegativeIntegerOrUnknownMember0ObjectToString(EDataType eDataType, Object instanceValue) {
        return convertNonNegativeIntegerOrUnknownMember0ToString(Wfs20Package.Literals.NON_NEGATIVE_INTEGER_OR_UNKNOWN_MEMBER0, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger createNonNegativeIntegerOrUnknownMember1FromString(EDataType eDataType, String initialValue) {
        return (BigInteger)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.NON_NEGATIVE_INTEGER, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertNonNegativeIntegerOrUnknownMember1ToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.NON_NEGATIVE_INTEGER, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createPositiveIntegerWithStarFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        Object result = null;
        RuntimeException exception = null;
        try {
            result = XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.POSITIVE_INTEGER, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = createStarStringTypeFromString(Wfs20Package.Literals.STAR_STRING_TYPE, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        if (result != null || exception == null) return result;
    
        throw exception;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertPositiveIntegerWithStarToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        if (XMLTypePackage.Literals.POSITIVE_INTEGER.isInstance(instanceValue)) {
            try {
                String value = XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.POSITIVE_INTEGER, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (Wfs20Package.Literals.STAR_STRING_TYPE.isInstance(instanceValue)) {
            try {
                String value = convertStarStringTypeToString(Wfs20Package.Literals.STAR_STRING_TYPE, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResolveValueType createResolveValueTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createResolveValueTypeFromString(Wfs20Package.Literals.RESOLVE_VALUE_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertResolveValueTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertResolveValueTypeToString(Wfs20Package.Literals.RESOLVE_VALUE_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResultTypeType createResultTypeTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createResultTypeTypeFromString(Wfs20Package.Literals.RESULT_TYPE_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertResultTypeTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertResultTypeTypeToString(Wfs20Package.Literals.RESULT_TYPE_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<QName> createReturnFeatureTypesListTypeFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        List<QName> result = new ArrayList<QName>();
        for (String item : split(initialValue)) {
            result.add((QName)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.QNAME, item));
        }
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertReturnFeatureTypesListTypeToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        List<?> list = (List<?>)instanceValue;
        if (list.isEmpty()) return "";
        StringBuffer result = new StringBuffer();
        for (Object item : list) {
            result.append(XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.QNAME, item));
            result.append(' ');
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StarStringType createStarStringTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createStarStringTypeFromString(Wfs20Package.Literals.STAR_STRING_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertStarStringTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertStarStringTypeToString(Wfs20Package.Literals.STAR_STRING_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object createStateValueTypeFromString(EDataType eDataType, String initialValue) {
        if (initialValue == null) return null;
        Object result = null;
        RuntimeException exception = null;
        try {
            result = createStateValueTypeMember0FromString(Wfs20Package.Literals.STATE_VALUE_TYPE_MEMBER0, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        try {
            result = createStateValueTypeMember1FromString(Wfs20Package.Literals.STATE_VALUE_TYPE_MEMBER1, initialValue);
            if (result != null && Diagnostician.INSTANCE.validate(eDataType, result, null, null)) {
                return result;
            }
        }
        catch (RuntimeException e) {
            exception = e;
        }
        if (result != null || exception == null) return result;
    
        throw exception;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertStateValueTypeToString(EDataType eDataType, Object instanceValue) {
        if (instanceValue == null) return null;
        if (Wfs20Package.Literals.STATE_VALUE_TYPE_MEMBER0.isInstance(instanceValue)) {
            try {
                String value = convertStateValueTypeMember0ToString(Wfs20Package.Literals.STATE_VALUE_TYPE_MEMBER0, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        if (Wfs20Package.Literals.STATE_VALUE_TYPE_MEMBER1.isInstance(instanceValue)) {
            try {
                String value = convertStateValueTypeMember1ToString(Wfs20Package.Literals.STATE_VALUE_TYPE_MEMBER1, instanceValue);
                if (value != null) return value;
            }
            catch (Exception e) {
                // Keep trying other member types until all have failed.
            }
        }
        throw new IllegalArgumentException("Invalid value: '"+instanceValue+"' for datatype :"+eDataType.getName());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StateValueTypeMember0 createStateValueTypeMember0ObjectFromString(EDataType eDataType, String initialValue) {
        return createStateValueTypeMember0FromString(Wfs20Package.Literals.STATE_VALUE_TYPE_MEMBER0, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertStateValueTypeMember0ObjectToString(EDataType eDataType, Object instanceValue) {
        return convertStateValueTypeMember0ToString(Wfs20Package.Literals.STATE_VALUE_TYPE_MEMBER0, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createStateValueTypeMember1FromString(EDataType eDataType, String initialValue) {
        return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertStateValueTypeMember1ToString(EDataType eDataType, Object instanceValue) {
        return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UpdateActionType createUpdateActionTypeObjectFromString(EDataType eDataType, String initialValue) {
        return createUpdateActionTypeFromString(Wfs20Package.Literals.UPDATE_ACTION_TYPE, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertUpdateActionTypeObjectToString(EDataType eDataType, Object instanceValue) {
        return convertUpdateActionTypeToString(Wfs20Package.Literals.UPDATE_ACTION_TYPE, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public URI createURIFromString(EDataType eDataType, String initialValue) {
        return (URI)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertURIToString(EDataType eDataType, Object instanceValue) {
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
    public Filter createFilterFromString(EDataType eDataType, String initialValue) {
        return (Filter)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertFilterToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QName createQNameFromString(EDataType eDataType, String initialValue) {
        return (QName)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertQNameToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SortBy createSortByFromString(EDataType eDataType, String initialValue) {
        return (SortBy)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSortByToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Calendar createCalendarFromString(EDataType eDataType, String initialValue) {
        return (Calendar)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertCalendarToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureCollection createFeatureCollectionFromString(EDataType eDataType, String initialValue) {
        return (FeatureCollection)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertFeatureCollectionToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PropertyName createPropertyNameFromString(EDataType eDataType, String initialValue) {
        return (PropertyName)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertPropertyNameToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureId createFeatureIdFromString(EDataType eDataType, String initialValue) {
        return (FeatureId)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertFeatureIdToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Collection createCollectionFromString(EDataType eDataType, String initialValue) {
        return (Collection)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertCollectionToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wfs20Package getWfs20Package() {
        return (Wfs20Package)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static Wfs20Package getPackage() {
        return Wfs20Package.eINSTANCE;
    }

} //Wfs20FactoryImpl
