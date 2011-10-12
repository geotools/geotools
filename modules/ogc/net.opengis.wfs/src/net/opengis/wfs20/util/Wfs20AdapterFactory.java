/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.util;

import net.opengis.fes20.AbstractAdhocQueryExpressionType;
import net.opengis.fes20.AbstractQueryExpressionType;

import net.opengis.ows11.CapabilitiesBaseType;

import net.opengis.wfs20.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wfs20.Wfs20Package
 * @generated
 */
public class Wfs20AdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static Wfs20Package modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Wfs20AdapterFactory() {
        if (modelPackage == null) {
            modelPackage = Wfs20Package.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected Wfs20Switch<Adapter> modelSwitch =
        new Wfs20Switch<Adapter>() {
            @Override
            public Adapter caseAbstractTransactionActionType(AbstractTransactionActionType object) {
                return createAbstractTransactionActionTypeAdapter();
            }
            @Override
            public Adapter caseAbstractType(AbstractType object) {
                return createAbstractTypeAdapter();
            }
            @Override
            public Adapter caseActionResultsType(ActionResultsType object) {
                return createActionResultsTypeAdapter();
            }
            @Override
            public Adapter caseAdditionalObjectsType(AdditionalObjectsType object) {
                return createAdditionalObjectsTypeAdapter();
            }
            @Override
            public Adapter caseAdditionalValuesType(AdditionalValuesType object) {
                return createAdditionalValuesTypeAdapter();
            }
            @Override
            public Adapter caseBaseRequestType(BaseRequestType object) {
                return createBaseRequestTypeAdapter();
            }
            @Override
            public Adapter caseCreatedOrModifiedFeatureType(CreatedOrModifiedFeatureType object) {
                return createCreatedOrModifiedFeatureTypeAdapter();
            }
            @Override
            public Adapter caseCreateStoredQueryResponseType(CreateStoredQueryResponseType object) {
                return createCreateStoredQueryResponseTypeAdapter();
            }
            @Override
            public Adapter caseCreateStoredQueryType(CreateStoredQueryType object) {
                return createCreateStoredQueryTypeAdapter();
            }
            @Override
            public Adapter caseDeleteType(DeleteType object) {
                return createDeleteTypeAdapter();
            }
            @Override
            public Adapter caseDescribeFeatureTypeType(DescribeFeatureTypeType object) {
                return createDescribeFeatureTypeTypeAdapter();
            }
            @Override
            public Adapter caseDescribeStoredQueriesResponseType(DescribeStoredQueriesResponseType object) {
                return createDescribeStoredQueriesResponseTypeAdapter();
            }
            @Override
            public Adapter caseDescribeStoredQueriesType(DescribeStoredQueriesType object) {
                return createDescribeStoredQueriesTypeAdapter();
            }
            @Override
            public Adapter caseDocumentRoot(DocumentRoot object) {
                return createDocumentRootAdapter();
            }
            @Override
            public Adapter caseDropStoredQueryType(DropStoredQueryType object) {
                return createDropStoredQueryTypeAdapter();
            }
            @Override
            public Adapter caseElementType(ElementType object) {
                return createElementTypeAdapter();
            }
            @Override
            public Adapter caseEmptyType(EmptyType object) {
                return createEmptyTypeAdapter();
            }
            @Override
            public Adapter caseEnvelopePropertyType(EnvelopePropertyType object) {
                return createEnvelopePropertyTypeAdapter();
            }
            @Override
            public Adapter caseExecutionStatusType(ExecutionStatusType object) {
                return createExecutionStatusTypeAdapter();
            }
            @Override
            public Adapter caseExtendedDescriptionType(ExtendedDescriptionType object) {
                return createExtendedDescriptionTypeAdapter();
            }
            @Override
            public Adapter caseFeatureCollectionType(FeatureCollectionType object) {
                return createFeatureCollectionTypeAdapter();
            }
            @Override
            public Adapter caseFeaturesLockedType(FeaturesLockedType object) {
                return createFeaturesLockedTypeAdapter();
            }
            @Override
            public Adapter caseFeaturesNotLockedType(FeaturesNotLockedType object) {
                return createFeaturesNotLockedTypeAdapter();
            }
            @Override
            public Adapter caseFeatureTypeListType(FeatureTypeListType object) {
                return createFeatureTypeListTypeAdapter();
            }
            @Override
            public Adapter caseFeatureTypeType(FeatureTypeType object) {
                return createFeatureTypeTypeAdapter();
            }
            @Override
            public Adapter caseGetCapabilitiesType(GetCapabilitiesType object) {
                return createGetCapabilitiesTypeAdapter();
            }
            @Override
            public Adapter caseGetFeatureType(GetFeatureType object) {
                return createGetFeatureTypeAdapter();
            }
            @Override
            public Adapter caseGetFeatureWithLockType(GetFeatureWithLockType object) {
                return createGetFeatureWithLockTypeAdapter();
            }
            @Override
            public Adapter caseGetPropertyValueType(GetPropertyValueType object) {
                return createGetPropertyValueTypeAdapter();
            }
            @Override
            public Adapter caseInsertType(InsertType object) {
                return createInsertTypeAdapter();
            }
            @Override
            public Adapter caseListStoredQueriesResponseType(ListStoredQueriesResponseType object) {
                return createListStoredQueriesResponseTypeAdapter();
            }
            @Override
            public Adapter caseListStoredQueriesType(ListStoredQueriesType object) {
                return createListStoredQueriesTypeAdapter();
            }
            @Override
            public Adapter caseLockFeatureResponseType(LockFeatureResponseType object) {
                return createLockFeatureResponseTypeAdapter();
            }
            @Override
            public Adapter caseLockFeatureType(LockFeatureType object) {
                return createLockFeatureTypeAdapter();
            }
            @Override
            public Adapter caseMemberPropertyType(MemberPropertyType object) {
                return createMemberPropertyTypeAdapter();
            }
            @Override
            public Adapter caseMetadataURLType(MetadataURLType object) {
                return createMetadataURLTypeAdapter();
            }
            @Override
            public Adapter caseNativeType(NativeType object) {
                return createNativeTypeAdapter();
            }
            @Override
            public Adapter caseNoCRSType(NoCRSType object) {
                return createNoCRSTypeAdapter();
            }
            @Override
            public Adapter caseOutputFormatListType(OutputFormatListType object) {
                return createOutputFormatListTypeAdapter();
            }
            @Override
            public Adapter caseParameterExpressionType(ParameterExpressionType object) {
                return createParameterExpressionTypeAdapter();
            }
            @Override
            public Adapter caseParameterType(ParameterType object) {
                return createParameterTypeAdapter();
            }
            @Override
            public Adapter casePropertyNameType(PropertyNameType object) {
                return createPropertyNameTypeAdapter();
            }
            @Override
            public Adapter casePropertyType(PropertyType object) {
                return createPropertyTypeAdapter();
            }
            @Override
            public Adapter caseQueryExpressionTextType(QueryExpressionTextType object) {
                return createQueryExpressionTextTypeAdapter();
            }
            @Override
            public Adapter caseQueryType(QueryType object) {
                return createQueryTypeAdapter();
            }
            @Override
            public Adapter caseReplaceType(ReplaceType object) {
                return createReplaceTypeAdapter();
            }
            @Override
            public Adapter caseSimpleFeatureCollectionType(SimpleFeatureCollectionType object) {
                return createSimpleFeatureCollectionTypeAdapter();
            }
            @Override
            public Adapter caseStoredQueryDescriptionType(StoredQueryDescriptionType object) {
                return createStoredQueryDescriptionTypeAdapter();
            }
            @Override
            public Adapter caseStoredQueryListItemType(StoredQueryListItemType object) {
                return createStoredQueryListItemTypeAdapter();
            }
            @Override
            public Adapter caseStoredQueryType(StoredQueryType object) {
                return createStoredQueryTypeAdapter();
            }
            @Override
            public Adapter caseTitleType(TitleType object) {
                return createTitleTypeAdapter();
            }
            @Override
            public Adapter caseTransactionResponseType(TransactionResponseType object) {
                return createTransactionResponseTypeAdapter();
            }
            @Override
            public Adapter caseTransactionSummaryType(TransactionSummaryType object) {
                return createTransactionSummaryTypeAdapter();
            }
            @Override
            public Adapter caseTransactionType(TransactionType object) {
                return createTransactionTypeAdapter();
            }
            @Override
            public Adapter caseTruncatedResponseType(TruncatedResponseType object) {
                return createTruncatedResponseTypeAdapter();
            }
            @Override
            public Adapter caseTupleType(TupleType object) {
                return createTupleTypeAdapter();
            }
            @Override
            public Adapter caseUpdateType(UpdateType object) {
                return createUpdateTypeAdapter();
            }
            @Override
            public Adapter caseValueCollectionType(ValueCollectionType object) {
                return createValueCollectionTypeAdapter();
            }
            @Override
            public Adapter caseValueListType(ValueListType object) {
                return createValueListTypeAdapter();
            }
            @Override
            public Adapter caseValueReferenceType(ValueReferenceType object) {
                return createValueReferenceTypeAdapter();
            }
            @Override
            public Adapter caseWFSCapabilitiesType(WFSCapabilitiesType object) {
                return createWFSCapabilitiesTypeAdapter();
            }
            @Override
            public Adapter caseWSDLType(WSDLType object) {
                return createWSDLTypeAdapter();
            }
            @Override
            public Adapter caseOws11_GetCapabilitiesType(net.opengis.ows11.GetCapabilitiesType object) {
                return createOws11_GetCapabilitiesTypeAdapter();
            }
            @Override
            public Adapter caseAbstractQueryExpressionType(AbstractQueryExpressionType object) {
                return createAbstractQueryExpressionTypeAdapter();
            }
            @Override
            public Adapter caseAbstractAdhocQueryExpressionType(AbstractAdhocQueryExpressionType object) {
                return createAbstractAdhocQueryExpressionTypeAdapter();
            }
            @Override
            public Adapter caseCapabilitiesBaseType(CapabilitiesBaseType object) {
                return createCapabilitiesBaseTypeAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.AbstractTransactionActionType <em>Abstract Transaction Action Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.AbstractTransactionActionType
     * @generated
     */
    public Adapter createAbstractTransactionActionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.AbstractType <em>Abstract Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.AbstractType
     * @generated
     */
    public Adapter createAbstractTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.ActionResultsType <em>Action Results Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.ActionResultsType
     * @generated
     */
    public Adapter createActionResultsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.AdditionalObjectsType <em>Additional Objects Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.AdditionalObjectsType
     * @generated
     */
    public Adapter createAdditionalObjectsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.AdditionalValuesType <em>Additional Values Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.AdditionalValuesType
     * @generated
     */
    public Adapter createAdditionalValuesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.BaseRequestType <em>Base Request Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.BaseRequestType
     * @generated
     */
    public Adapter createBaseRequestTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.CreatedOrModifiedFeatureType <em>Created Or Modified Feature Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.CreatedOrModifiedFeatureType
     * @generated
     */
    public Adapter createCreatedOrModifiedFeatureTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.CreateStoredQueryResponseType <em>Create Stored Query Response Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.CreateStoredQueryResponseType
     * @generated
     */
    public Adapter createCreateStoredQueryResponseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.CreateStoredQueryType <em>Create Stored Query Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.CreateStoredQueryType
     * @generated
     */
    public Adapter createCreateStoredQueryTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.DeleteType <em>Delete Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.DeleteType
     * @generated
     */
    public Adapter createDeleteTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.DescribeFeatureTypeType <em>Describe Feature Type Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.DescribeFeatureTypeType
     * @generated
     */
    public Adapter createDescribeFeatureTypeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.DescribeStoredQueriesResponseType <em>Describe Stored Queries Response Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.DescribeStoredQueriesResponseType
     * @generated
     */
    public Adapter createDescribeStoredQueriesResponseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.DescribeStoredQueriesType <em>Describe Stored Queries Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.DescribeStoredQueriesType
     * @generated
     */
    public Adapter createDescribeStoredQueriesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.DocumentRoot
     * @generated
     */
    public Adapter createDocumentRootAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.DropStoredQueryType <em>Drop Stored Query Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.DropStoredQueryType
     * @generated
     */
    public Adapter createDropStoredQueryTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.ElementType <em>Element Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.ElementType
     * @generated
     */
    public Adapter createElementTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.EmptyType <em>Empty Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.EmptyType
     * @generated
     */
    public Adapter createEmptyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.EnvelopePropertyType <em>Envelope Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.EnvelopePropertyType
     * @generated
     */
    public Adapter createEnvelopePropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.ExecutionStatusType <em>Execution Status Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.ExecutionStatusType
     * @generated
     */
    public Adapter createExecutionStatusTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.ExtendedDescriptionType <em>Extended Description Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.ExtendedDescriptionType
     * @generated
     */
    public Adapter createExtendedDescriptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.FeatureCollectionType <em>Feature Collection Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.FeatureCollectionType
     * @generated
     */
    public Adapter createFeatureCollectionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.FeaturesLockedType <em>Features Locked Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.FeaturesLockedType
     * @generated
     */
    public Adapter createFeaturesLockedTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.FeaturesNotLockedType <em>Features Not Locked Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.FeaturesNotLockedType
     * @generated
     */
    public Adapter createFeaturesNotLockedTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.FeatureTypeListType <em>Feature Type List Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.FeatureTypeListType
     * @generated
     */
    public Adapter createFeatureTypeListTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.FeatureTypeType <em>Feature Type Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.FeatureTypeType
     * @generated
     */
    public Adapter createFeatureTypeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.GetCapabilitiesType
     * @generated
     */
    public Adapter createGetCapabilitiesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.GetFeatureType <em>Get Feature Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.GetFeatureType
     * @generated
     */
    public Adapter createGetFeatureTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.GetFeatureWithLockType <em>Get Feature With Lock Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.GetFeatureWithLockType
     * @generated
     */
    public Adapter createGetFeatureWithLockTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.GetPropertyValueType <em>Get Property Value Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.GetPropertyValueType
     * @generated
     */
    public Adapter createGetPropertyValueTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.InsertType <em>Insert Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.InsertType
     * @generated
     */
    public Adapter createInsertTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.ListStoredQueriesResponseType <em>List Stored Queries Response Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.ListStoredQueriesResponseType
     * @generated
     */
    public Adapter createListStoredQueriesResponseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.ListStoredQueriesType <em>List Stored Queries Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.ListStoredQueriesType
     * @generated
     */
    public Adapter createListStoredQueriesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.LockFeatureResponseType <em>Lock Feature Response Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.LockFeatureResponseType
     * @generated
     */
    public Adapter createLockFeatureResponseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.LockFeatureType <em>Lock Feature Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.LockFeatureType
     * @generated
     */
    public Adapter createLockFeatureTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.MemberPropertyType <em>Member Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.MemberPropertyType
     * @generated
     */
    public Adapter createMemberPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.MetadataURLType <em>Metadata URL Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.MetadataURLType
     * @generated
     */
    public Adapter createMetadataURLTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.NativeType <em>Native Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.NativeType
     * @generated
     */
    public Adapter createNativeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.NoCRSType <em>No CRS Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.NoCRSType
     * @generated
     */
    public Adapter createNoCRSTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.OutputFormatListType <em>Output Format List Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.OutputFormatListType
     * @generated
     */
    public Adapter createOutputFormatListTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.ParameterExpressionType <em>Parameter Expression Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.ParameterExpressionType
     * @generated
     */
    public Adapter createParameterExpressionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.ParameterType <em>Parameter Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.ParameterType
     * @generated
     */
    public Adapter createParameterTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.PropertyNameType <em>Property Name Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.PropertyNameType
     * @generated
     */
    public Adapter createPropertyNameTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.PropertyType <em>Property Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.PropertyType
     * @generated
     */
    public Adapter createPropertyTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.QueryExpressionTextType <em>Query Expression Text Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.QueryExpressionTextType
     * @generated
     */
    public Adapter createQueryExpressionTextTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.QueryType <em>Query Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.QueryType
     * @generated
     */
    public Adapter createQueryTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.ReplaceType <em>Replace Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.ReplaceType
     * @generated
     */
    public Adapter createReplaceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.SimpleFeatureCollectionType <em>Simple Feature Collection Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.SimpleFeatureCollectionType
     * @generated
     */
    public Adapter createSimpleFeatureCollectionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.StoredQueryDescriptionType <em>Stored Query Description Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.StoredQueryDescriptionType
     * @generated
     */
    public Adapter createStoredQueryDescriptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.StoredQueryListItemType <em>Stored Query List Item Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.StoredQueryListItemType
     * @generated
     */
    public Adapter createStoredQueryListItemTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.StoredQueryType <em>Stored Query Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.StoredQueryType
     * @generated
     */
    public Adapter createStoredQueryTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.TitleType <em>Title Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.TitleType
     * @generated
     */
    public Adapter createTitleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.TransactionResponseType <em>Transaction Response Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.TransactionResponseType
     * @generated
     */
    public Adapter createTransactionResponseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.TransactionSummaryType <em>Transaction Summary Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.TransactionSummaryType
     * @generated
     */
    public Adapter createTransactionSummaryTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.TransactionType <em>Transaction Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.TransactionType
     * @generated
     */
    public Adapter createTransactionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.TruncatedResponseType <em>Truncated Response Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.TruncatedResponseType
     * @generated
     */
    public Adapter createTruncatedResponseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.TupleType <em>Tuple Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.TupleType
     * @generated
     */
    public Adapter createTupleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.UpdateType <em>Update Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.UpdateType
     * @generated
     */
    public Adapter createUpdateTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.ValueCollectionType <em>Value Collection Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.ValueCollectionType
     * @generated
     */
    public Adapter createValueCollectionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.ValueListType <em>Value List Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.ValueListType
     * @generated
     */
    public Adapter createValueListTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.ValueReferenceType <em>Value Reference Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.ValueReferenceType
     * @generated
     */
    public Adapter createValueReferenceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.WFSCapabilitiesType <em>WFS Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.WFSCapabilitiesType
     * @generated
     */
    public Adapter createWFSCapabilitiesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs20.WSDLType <em>WSDL Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs20.WSDLType
     * @generated
     */
    public Adapter createWSDLTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows11.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows11.GetCapabilitiesType
     * @generated
     */
    public Adapter createOws11_GetCapabilitiesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.fes20.AbstractQueryExpressionType <em>Abstract Query Expression Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.fes20.AbstractQueryExpressionType
     * @generated
     */
    public Adapter createAbstractQueryExpressionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.fes20.AbstractAdhocQueryExpressionType <em>Abstract Adhoc Query Expression Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.fes20.AbstractAdhocQueryExpressionType
     * @generated
     */
    public Adapter createAbstractAdhocQueryExpressionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows11.CapabilitiesBaseType <em>Capabilities Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows11.CapabilitiesBaseType
     * @generated
     */
    public Adapter createCapabilitiesBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //Wfs20AdapterFactory
