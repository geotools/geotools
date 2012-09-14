/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.net.URI;
import java.util.Calendar;
import java.util.List;

import java.util.Map;
import java.util.Set;
import javax.xml.datatype.Duration;
import javax.xml.namespace.QName;
import net.opengis.cat.csw20.AcknowledgementType;
import net.opengis.cat.csw20.BriefRecordType;
import net.opengis.cat.csw20.CapabilitiesType;
import net.opengis.cat.csw20.ConceptualSchemeType;
import net.opengis.cat.csw20.Csw20Factory;
import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.DCMIRecordType;
import net.opengis.cat.csw20.DeleteType;
import net.opengis.cat.csw20.DescribeRecordResponseType;
import net.opengis.cat.csw20.DescribeRecordType;
import net.opengis.cat.csw20.DistributedSearchType;
import net.opengis.cat.csw20.DomainValuesType;
import net.opengis.cat.csw20.EchoedRequestType;
import net.opengis.cat.csw20.ElementSetNameType;
import net.opengis.cat.csw20.ElementSetType;
import net.opengis.cat.csw20.EmptyType;
import net.opengis.cat.csw20.GetCapabilitiesType;
import net.opengis.cat.csw20.GetDomainResponseType;
import net.opengis.cat.csw20.GetDomainType;
import net.opengis.cat.csw20.GetRecordByIdType;
import net.opengis.cat.csw20.GetRecordsResponseType;
import net.opengis.cat.csw20.GetRecordsType;
import net.opengis.cat.csw20.HarvestResponseType;
import net.opengis.cat.csw20.HarvestType;
import net.opengis.cat.csw20.InsertResultType;
import net.opengis.cat.csw20.InsertType;
import net.opengis.cat.csw20.ListOfValuesType;
import net.opengis.cat.csw20.QueryConstraintType;
import net.opengis.cat.csw20.QueryType;
import net.opengis.cat.csw20.RangeOfValuesType;
import net.opengis.cat.csw20.RecordPropertyType;
import net.opengis.cat.csw20.RecordType;
import net.opengis.cat.csw20.RequestStatusType;
import net.opengis.cat.csw20.ResultType;
import net.opengis.cat.csw20.SchemaComponentType;
import net.opengis.cat.csw20.SearchResultsType;
import net.opengis.cat.csw20.SimpleLiteral;
import net.opengis.cat.csw20.SummaryRecordType;
import net.opengis.cat.csw20.TransactionResponseType;
import net.opengis.cat.csw20.TransactionSummaryType;
import net.opengis.cat.csw20.TransactionType;
import net.opengis.cat.csw20.UpdateType;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.opengis.filter.sort.SortBy;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Csw20FactoryImpl extends EFactoryImpl implements Csw20Factory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static Csw20Factory init() {
        try {
            Csw20Factory theCsw20Factory = (Csw20Factory)EPackage.Registry.INSTANCE.getEFactory("http:///net/opengis/cat/csw20.ecore"); 
            if (theCsw20Factory != null) {
                return theCsw20Factory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new Csw20FactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Csw20FactoryImpl() {
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
            case Csw20Package.ACKNOWLEDGEMENT_TYPE: return createAcknowledgementType();
            case Csw20Package.BRIEF_RECORD_TYPE: return createBriefRecordType();
            case Csw20Package.CAPABILITIES_TYPE: return createCapabilitiesType();
            case Csw20Package.CONCEPTUAL_SCHEME_TYPE: return createConceptualSchemeType();
            case Csw20Package.DELETE_TYPE: return createDeleteType();
            case Csw20Package.DESCRIBE_RECORD_RESPONSE_TYPE: return createDescribeRecordResponseType();
            case Csw20Package.DESCRIBE_RECORD_TYPE: return createDescribeRecordType();
            case Csw20Package.DISTRIBUTED_SEARCH_TYPE: return createDistributedSearchType();
            case Csw20Package.DOMAIN_VALUES_TYPE: return createDomainValuesType();
            case Csw20Package.ECHOED_REQUEST_TYPE: return createEchoedRequestType();
            case Csw20Package.ELEMENT_SET_NAME_TYPE: return createElementSetNameType();
            case Csw20Package.EMPTY_TYPE: return createEmptyType();
            case Csw20Package.GET_CAPABILITIES_TYPE: return createGetCapabilitiesType();
            case Csw20Package.GET_DOMAIN_RESPONSE_TYPE: return createGetDomainResponseType();
            case Csw20Package.GET_DOMAIN_TYPE: return createGetDomainType();
            case Csw20Package.GET_RECORD_BY_ID_TYPE: return createGetRecordByIdType();
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE: return createGetRecordsResponseType();
            case Csw20Package.GET_RECORDS_TYPE: return createGetRecordsType();
            case Csw20Package.HARVEST_RESPONSE_TYPE: return createHarvestResponseType();
            case Csw20Package.HARVEST_TYPE: return createHarvestType();
            case Csw20Package.INSERT_RESULT_TYPE: return createInsertResultType();
            case Csw20Package.INSERT_TYPE: return createInsertType();
            case Csw20Package.LIST_OF_VALUES_TYPE: return createListOfValuesType();
            case Csw20Package.QUERY_CONSTRAINT_TYPE: return createQueryConstraintType();
            case Csw20Package.QUERY_TYPE: return createQueryType();
            case Csw20Package.RANGE_OF_VALUES_TYPE: return createRangeOfValuesType();
            case Csw20Package.RECORD_PROPERTY_TYPE: return createRecordPropertyType();
            case Csw20Package.REQUEST_STATUS_TYPE: return createRequestStatusType();
            case Csw20Package.SCHEMA_COMPONENT_TYPE: return createSchemaComponentType();
            case Csw20Package.SEARCH_RESULTS_TYPE: return createSearchResultsType();
            case Csw20Package.TRANSACTION_RESPONSE_TYPE: return createTransactionResponseType();
            case Csw20Package.TRANSACTION_SUMMARY_TYPE: return createTransactionSummaryType();
            case Csw20Package.TRANSACTION_TYPE: return createTransactionType();
            case Csw20Package.UPDATE_TYPE: return createUpdateType();
            case Csw20Package.DCMI_RECORD_TYPE: return createDCMIRecordType();
            case Csw20Package.RECORD_TYPE: return createRecordType();
            case Csw20Package.SIMPLE_LITERAL: return createSimpleLiteral();
            case Csw20Package.SUMMARY_RECORD_TYPE: return createSummaryRecordType();
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
            case Csw20Package.ELEMENT_SET_TYPE:
                return createElementSetTypeFromString(eDataType, initialValue);
            case Csw20Package.RESULT_TYPE:
                return createResultTypeFromString(eDataType, initialValue);
            case Csw20Package.TYPE_NAME_LIST_TYPE:
                return createTypeNameListTypeFromString(eDataType, initialValue);
            case Csw20Package.SERVICE_TYPE:
                return createServiceTypeFromString(eDataType, initialValue);
            case Csw20Package.TYPE_NAME_LIST_TYPE_1:
                return createTypeNameListType_1FromString(eDataType, initialValue);
            case Csw20Package.SERVICE_TYPE_1:
                return createServiceType_1FromString(eDataType, initialValue);
            case Csw20Package.VERSION_TYPE:
                return createVersionTypeFromString(eDataType, initialValue);
            case Csw20Package.CALENDAR:
                return createCalendarFromString(eDataType, initialValue);
            case Csw20Package.SET:
                return createSetFromString(eDataType, initialValue);
            case Csw20Package.URI:
                return createURIFromString(eDataType, initialValue);
            case Csw20Package.QNAME:
                return createQNameFromString(eDataType, initialValue);
            case Csw20Package.DURATION:
                return createDurationFromString(eDataType, initialValue);
            case Csw20Package.MAP:
                return createMapFromString(eDataType, initialValue);
            case Csw20Package.SORT_BY_ARRAY:
                return createSortByArrayFromString(eDataType, initialValue);
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
            case Csw20Package.ELEMENT_SET_TYPE:
                return convertElementSetTypeToString(eDataType, instanceValue);
            case Csw20Package.RESULT_TYPE:
                return convertResultTypeToString(eDataType, instanceValue);
            case Csw20Package.TYPE_NAME_LIST_TYPE:
                return convertTypeNameListTypeToString(eDataType, instanceValue);
            case Csw20Package.SERVICE_TYPE:
                return convertServiceTypeToString(eDataType, instanceValue);
            case Csw20Package.TYPE_NAME_LIST_TYPE_1:
                return convertTypeNameListType_1ToString(eDataType, instanceValue);
            case Csw20Package.SERVICE_TYPE_1:
                return convertServiceType_1ToString(eDataType, instanceValue);
            case Csw20Package.VERSION_TYPE:
                return convertVersionTypeToString(eDataType, instanceValue);
            case Csw20Package.CALENDAR:
                return convertCalendarToString(eDataType, instanceValue);
            case Csw20Package.SET:
                return convertSetToString(eDataType, instanceValue);
            case Csw20Package.URI:
                return convertURIToString(eDataType, instanceValue);
            case Csw20Package.QNAME:
                return convertQNameToString(eDataType, instanceValue);
            case Csw20Package.DURATION:
                return convertDurationToString(eDataType, instanceValue);
            case Csw20Package.MAP:
                return convertMapToString(eDataType, instanceValue);
            case Csw20Package.SORT_BY_ARRAY:
                return convertSortByArrayToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AcknowledgementType createAcknowledgementType() {
        AcknowledgementTypeImpl acknowledgementType = new AcknowledgementTypeImpl();
        return acknowledgementType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BriefRecordType createBriefRecordType() {
        BriefRecordTypeImpl briefRecordType = new BriefRecordTypeImpl();
        return briefRecordType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CapabilitiesType createCapabilitiesType() {
        CapabilitiesTypeImpl capabilitiesType = new CapabilitiesTypeImpl();
        return capabilitiesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConceptualSchemeType createConceptualSchemeType() {
        ConceptualSchemeTypeImpl conceptualSchemeType = new ConceptualSchemeTypeImpl();
        return conceptualSchemeType;
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
    public DescribeRecordResponseType createDescribeRecordResponseType() {
        DescribeRecordResponseTypeImpl describeRecordResponseType = new DescribeRecordResponseTypeImpl();
        return describeRecordResponseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DescribeRecordType createDescribeRecordType() {
        DescribeRecordTypeImpl describeRecordType = new DescribeRecordTypeImpl();
        return describeRecordType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DistributedSearchType createDistributedSearchType() {
        DistributedSearchTypeImpl distributedSearchType = new DistributedSearchTypeImpl();
        return distributedSearchType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DomainValuesType createDomainValuesType() {
        DomainValuesTypeImpl domainValuesType = new DomainValuesTypeImpl();
        return domainValuesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EchoedRequestType createEchoedRequestType() {
        EchoedRequestTypeImpl echoedRequestType = new EchoedRequestTypeImpl();
        return echoedRequestType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ElementSetNameType createElementSetNameType() {
        ElementSetNameTypeImpl elementSetNameType = new ElementSetNameTypeImpl();
        return elementSetNameType;
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
    public GetCapabilitiesType createGetCapabilitiesType() {
        GetCapabilitiesTypeImpl getCapabilitiesType = new GetCapabilitiesTypeImpl();
        return getCapabilitiesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetDomainResponseType createGetDomainResponseType() {
        GetDomainResponseTypeImpl getDomainResponseType = new GetDomainResponseTypeImpl();
        return getDomainResponseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetDomainType createGetDomainType() {
        GetDomainTypeImpl getDomainType = new GetDomainTypeImpl();
        return getDomainType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetRecordByIdType createGetRecordByIdType() {
        GetRecordByIdTypeImpl getRecordByIdType = new GetRecordByIdTypeImpl();
        return getRecordByIdType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetRecordsResponseType createGetRecordsResponseType() {
        GetRecordsResponseTypeImpl getRecordsResponseType = new GetRecordsResponseTypeImpl();
        return getRecordsResponseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetRecordsType createGetRecordsType() {
        GetRecordsTypeImpl getRecordsType = new GetRecordsTypeImpl();
        return getRecordsType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public HarvestResponseType createHarvestResponseType() {
        HarvestResponseTypeImpl harvestResponseType = new HarvestResponseTypeImpl();
        return harvestResponseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public HarvestType createHarvestType() {
        HarvestTypeImpl harvestType = new HarvestTypeImpl();
        return harvestType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InsertResultType createInsertResultType() {
        InsertResultTypeImpl insertResultType = new InsertResultTypeImpl();
        return insertResultType;
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
    public ListOfValuesType createListOfValuesType() {
        ListOfValuesTypeImpl listOfValuesType = new ListOfValuesTypeImpl();
        return listOfValuesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QueryConstraintType createQueryConstraintType() {
        QueryConstraintTypeImpl queryConstraintType = new QueryConstraintTypeImpl();
        return queryConstraintType;
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
    public RangeOfValuesType createRangeOfValuesType() {
        RangeOfValuesTypeImpl rangeOfValuesType = new RangeOfValuesTypeImpl();
        return rangeOfValuesType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RecordPropertyType createRecordPropertyType() {
        RecordPropertyTypeImpl recordPropertyType = new RecordPropertyTypeImpl();
        return recordPropertyType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RequestStatusType createRequestStatusType() {
        RequestStatusTypeImpl requestStatusType = new RequestStatusTypeImpl();
        return requestStatusType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SchemaComponentType createSchemaComponentType() {
        SchemaComponentTypeImpl schemaComponentType = new SchemaComponentTypeImpl();
        return schemaComponentType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SearchResultsType createSearchResultsType() {
        SearchResultsTypeImpl searchResultsType = new SearchResultsTypeImpl();
        return searchResultsType;
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
    public UpdateType createUpdateType() {
        UpdateTypeImpl updateType = new UpdateTypeImpl();
        return updateType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DCMIRecordType createDCMIRecordType() {
        DCMIRecordTypeImpl dcmiRecordType = new DCMIRecordTypeImpl();
        return dcmiRecordType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RecordType createRecordType() {
        RecordTypeImpl recordType = new RecordTypeImpl();
        return recordType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SimpleLiteral createSimpleLiteral() {
        SimpleLiteralImpl simpleLiteral = new SimpleLiteralImpl();
        return simpleLiteral;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SummaryRecordType createSummaryRecordType() {
        SummaryRecordTypeImpl summaryRecordType = new SummaryRecordTypeImpl();
        return summaryRecordType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ElementSetType createElementSetTypeFromString(EDataType eDataType, String initialValue) {
        ElementSetType result = ElementSetType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertElementSetTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResultType createResultTypeFromString(EDataType eDataType, String initialValue) {
        ResultType result = ResultType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertResultTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public List<QName> createTypeNameListTypeFromString(EDataType eDataType, String initialValue) {
        return (List<QName>)super.createFromString(initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTypeNameListTypeToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createServiceTypeFromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertServiceTypeToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public List<QName> createTypeNameListType_1FromString(EDataType eDataType, String initialValue) {
        return (List<QName>)super.createFromString(initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTypeNameListType_1ToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String createServiceType_1FromString(EDataType eDataType, String initialValue) {
        return (String)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertServiceType_1ToString(EDataType eDataType, Object instanceValue) {
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
    public Set<?> createSetFromString(EDataType eDataType, String initialValue) {
        return (Set<?>)super.createFromString(initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSetToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(instanceValue);
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
    public Duration createDurationFromString(EDataType eDataType, String initialValue) {
        return (Duration)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertDurationToString(EDataType eDataType, Object instanceValue) {
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
    public SortBy[] createSortByArrayFromString(EDataType eDataType, String initialValue) {
        return (SortBy[])super.createFromString(initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertSortByArrayToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Csw20Package getCsw20Package() {
        return (Csw20Package)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static Csw20Package getPackage() {
        return Csw20Package.eINSTANCE;
    }

} //Csw20FactoryImpl
