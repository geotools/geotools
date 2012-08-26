/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import java.math.BigInteger;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.DistributedSearchType;
import net.opengis.cat.csw20.GetRecordsType;
import net.opengis.cat.csw20.ResultType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Records Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordsTypeImpl#getDistributedSearch <em>Distributed Search</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordsTypeImpl#getResponseHandler <em>Response Handler</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordsTypeImpl#getAny <em>Any</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordsTypeImpl#getMaxRecords <em>Max Records</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordsTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordsTypeImpl#getOutputSchema <em>Output Schema</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordsTypeImpl#getRequestId <em>Request Id</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordsTypeImpl#getResultType <em>Result Type</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordsTypeImpl#getStartPosition <em>Start Position</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordsTypeImpl#getQuery <em>Query</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetRecordsTypeImpl extends RequestBaseTypeImpl implements GetRecordsType {
    /**
     * The cached value of the '{@link #getDistributedSearch() <em>Distributed Search</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDistributedSearch()
     * @generated
     * @ordered
     */
    protected DistributedSearchType distributedSearch;

    /**
     * The default value of the '{@link #getResponseHandler() <em>Response Handler</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResponseHandler()
     * @generated
     * @ordered
     */
    protected static final String RESPONSE_HANDLER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getResponseHandler() <em>Response Handler</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResponseHandler()
     * @generated
     * @ordered
     */
    protected String responseHandler = RESPONSE_HANDLER_EDEFAULT;

    /**
     * The cached value of the '{@link #getAny() <em>Any</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAny()
     * @generated
     * @ordered
     */
    protected FeatureMap any;

    /**
     * The default value of the '{@link #getMaxRecords() <em>Max Records</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaxRecords()
     * @generated
     * @ordered
     */
    protected static final BigInteger MAX_RECORDS_EDEFAULT = new BigInteger("10");

    /**
     * The cached value of the '{@link #getMaxRecords() <em>Max Records</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaxRecords()
     * @generated
     * @ordered
     */
    protected BigInteger maxRecords = MAX_RECORDS_EDEFAULT;

    /**
     * This is true if the Max Records attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean maxRecordsESet;

    /**
     * The default value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected static final String OUTPUT_FORMAT_EDEFAULT = "application/xml";

    /**
     * The cached value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected String outputFormat = OUTPUT_FORMAT_EDEFAULT;

    /**
     * This is true if the Output Format attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean outputFormatESet;

    /**
     * The default value of the '{@link #getOutputSchema() <em>Output Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputSchema()
     * @generated
     * @ordered
     */
    protected static final String OUTPUT_SCHEMA_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getOutputSchema() <em>Output Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputSchema()
     * @generated
     * @ordered
     */
    protected String outputSchema = OUTPUT_SCHEMA_EDEFAULT;

    /**
     * The default value of the '{@link #getRequestId() <em>Request Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRequestId()
     * @generated
     * @ordered
     */
    protected static final String REQUEST_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRequestId() <em>Request Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRequestId()
     * @generated
     * @ordered
     */
    protected String requestId = REQUEST_ID_EDEFAULT;

    /**
     * The default value of the '{@link #getResultType() <em>Result Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResultType()
     * @generated
     * @ordered
     */
    protected static final ResultType RESULT_TYPE_EDEFAULT = ResultType.HITS;

    /**
     * The cached value of the '{@link #getResultType() <em>Result Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResultType()
     * @generated
     * @ordered
     */
    protected ResultType resultType = RESULT_TYPE_EDEFAULT;

    /**
     * This is true if the Result Type attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean resultTypeESet;

    /**
     * The default value of the '{@link #getStartPosition() <em>Start Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartPosition()
     * @generated
     * @ordered
     */
    protected static final BigInteger START_POSITION_EDEFAULT = new BigInteger("1");

    /**
     * The cached value of the '{@link #getStartPosition() <em>Start Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartPosition()
     * @generated
     * @ordered
     */
    protected BigInteger startPosition = START_POSITION_EDEFAULT;

    /**
     * This is true if the Start Position attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean startPositionESet;

    /**
     * The default value of the '{@link #getQuery() <em>Query</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getQuery()
     * @generated
     * @ordered
     */
    protected static final Object QUERY_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getQuery() <em>Query</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getQuery()
     * @generated
     * @ordered
     */
    protected Object query = QUERY_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GetRecordsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.GET_RECORDS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DistributedSearchType getDistributedSearch() {
        return distributedSearch;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDistributedSearch(DistributedSearchType newDistributedSearch, NotificationChain msgs) {
        DistributedSearchType oldDistributedSearch = distributedSearch;
        distributedSearch = newDistributedSearch;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORDS_TYPE__DISTRIBUTED_SEARCH, oldDistributedSearch, newDistributedSearch);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDistributedSearch(DistributedSearchType newDistributedSearch) {
        if (newDistributedSearch != distributedSearch) {
            NotificationChain msgs = null;
            if (distributedSearch != null)
                msgs = ((InternalEObject)distributedSearch).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.GET_RECORDS_TYPE__DISTRIBUTED_SEARCH, null, msgs);
            if (newDistributedSearch != null)
                msgs = ((InternalEObject)newDistributedSearch).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.GET_RECORDS_TYPE__DISTRIBUTED_SEARCH, null, msgs);
            msgs = basicSetDistributedSearch(newDistributedSearch, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORDS_TYPE__DISTRIBUTED_SEARCH, newDistributedSearch, newDistributedSearch));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getResponseHandler() {
        return responseHandler;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResponseHandler(String newResponseHandler) {
        String oldResponseHandler = responseHandler;
        responseHandler = newResponseHandler;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORDS_TYPE__RESPONSE_HANDLER, oldResponseHandler, responseHandler));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getAny() {
        if (any == null) {
            any = new BasicFeatureMap(this, Csw20Package.GET_RECORDS_TYPE__ANY);
        }
        return any;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getMaxRecords() {
        return maxRecords;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMaxRecords(BigInteger newMaxRecords) {
        BigInteger oldMaxRecords = maxRecords;
        maxRecords = newMaxRecords;
        boolean oldMaxRecordsESet = maxRecordsESet;
        maxRecordsESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORDS_TYPE__MAX_RECORDS, oldMaxRecords, maxRecords, !oldMaxRecordsESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetMaxRecords() {
        BigInteger oldMaxRecords = maxRecords;
        boolean oldMaxRecordsESet = maxRecordsESet;
        maxRecords = MAX_RECORDS_EDEFAULT;
        maxRecordsESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Csw20Package.GET_RECORDS_TYPE__MAX_RECORDS, oldMaxRecords, MAX_RECORDS_EDEFAULT, oldMaxRecordsESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetMaxRecords() {
        return maxRecordsESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getOutputFormat() {
        return outputFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOutputFormat(String newOutputFormat) {
        String oldOutputFormat = outputFormat;
        outputFormat = newOutputFormat;
        boolean oldOutputFormatESet = outputFormatESet;
        outputFormatESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORDS_TYPE__OUTPUT_FORMAT, oldOutputFormat, outputFormat, !oldOutputFormatESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetOutputFormat() {
        String oldOutputFormat = outputFormat;
        boolean oldOutputFormatESet = outputFormatESet;
        outputFormat = OUTPUT_FORMAT_EDEFAULT;
        outputFormatESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Csw20Package.GET_RECORDS_TYPE__OUTPUT_FORMAT, oldOutputFormat, OUTPUT_FORMAT_EDEFAULT, oldOutputFormatESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetOutputFormat() {
        return outputFormatESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getOutputSchema() {
        return outputSchema;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOutputSchema(String newOutputSchema) {
        String oldOutputSchema = outputSchema;
        outputSchema = newOutputSchema;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORDS_TYPE__OUTPUT_SCHEMA, oldOutputSchema, outputSchema));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRequestId(String newRequestId) {
        String oldRequestId = requestId;
        requestId = newRequestId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORDS_TYPE__REQUEST_ID, oldRequestId, requestId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResultType getResultType() {
        return resultType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResultType(ResultType newResultType) {
        ResultType oldResultType = resultType;
        resultType = newResultType == null ? RESULT_TYPE_EDEFAULT : newResultType;
        boolean oldResultTypeESet = resultTypeESet;
        resultTypeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORDS_TYPE__RESULT_TYPE, oldResultType, resultType, !oldResultTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetResultType() {
        ResultType oldResultType = resultType;
        boolean oldResultTypeESet = resultTypeESet;
        resultType = RESULT_TYPE_EDEFAULT;
        resultTypeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Csw20Package.GET_RECORDS_TYPE__RESULT_TYPE, oldResultType, RESULT_TYPE_EDEFAULT, oldResultTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetResultType() {
        return resultTypeESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getStartPosition() {
        return startPosition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStartPosition(BigInteger newStartPosition) {
        BigInteger oldStartPosition = startPosition;
        startPosition = newStartPosition;
        boolean oldStartPositionESet = startPositionESet;
        startPositionESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORDS_TYPE__START_POSITION, oldStartPosition, startPosition, !oldStartPositionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetStartPosition() {
        BigInteger oldStartPosition = startPosition;
        boolean oldStartPositionESet = startPositionESet;
        startPosition = START_POSITION_EDEFAULT;
        startPositionESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Csw20Package.GET_RECORDS_TYPE__START_POSITION, oldStartPosition, START_POSITION_EDEFAULT, oldStartPositionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetStartPosition() {
        return startPositionESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getQuery() {
        return query;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setQuery(Object newQuery) {
        Object oldQuery = query;
        query = newQuery;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORDS_TYPE__QUERY, oldQuery, query));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.GET_RECORDS_TYPE__DISTRIBUTED_SEARCH:
                return basicSetDistributedSearch(null, msgs);
            case Csw20Package.GET_RECORDS_TYPE__ANY:
                return ((InternalEList<?>)getAny()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Csw20Package.GET_RECORDS_TYPE__DISTRIBUTED_SEARCH:
                return getDistributedSearch();
            case Csw20Package.GET_RECORDS_TYPE__RESPONSE_HANDLER:
                return getResponseHandler();
            case Csw20Package.GET_RECORDS_TYPE__ANY:
                if (coreType) return getAny();
                return ((FeatureMap.Internal)getAny()).getWrapper();
            case Csw20Package.GET_RECORDS_TYPE__MAX_RECORDS:
                return getMaxRecords();
            case Csw20Package.GET_RECORDS_TYPE__OUTPUT_FORMAT:
                return getOutputFormat();
            case Csw20Package.GET_RECORDS_TYPE__OUTPUT_SCHEMA:
                return getOutputSchema();
            case Csw20Package.GET_RECORDS_TYPE__REQUEST_ID:
                return getRequestId();
            case Csw20Package.GET_RECORDS_TYPE__RESULT_TYPE:
                return getResultType();
            case Csw20Package.GET_RECORDS_TYPE__START_POSITION:
                return getStartPosition();
            case Csw20Package.GET_RECORDS_TYPE__QUERY:
                return getQuery();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Csw20Package.GET_RECORDS_TYPE__DISTRIBUTED_SEARCH:
                setDistributedSearch((DistributedSearchType)newValue);
                return;
            case Csw20Package.GET_RECORDS_TYPE__RESPONSE_HANDLER:
                setResponseHandler((String)newValue);
                return;
            case Csw20Package.GET_RECORDS_TYPE__ANY:
                ((FeatureMap.Internal)getAny()).set(newValue);
                return;
            case Csw20Package.GET_RECORDS_TYPE__MAX_RECORDS:
                setMaxRecords((BigInteger)newValue);
                return;
            case Csw20Package.GET_RECORDS_TYPE__OUTPUT_FORMAT:
                setOutputFormat((String)newValue);
                return;
            case Csw20Package.GET_RECORDS_TYPE__OUTPUT_SCHEMA:
                setOutputSchema((String)newValue);
                return;
            case Csw20Package.GET_RECORDS_TYPE__REQUEST_ID:
                setRequestId((String)newValue);
                return;
            case Csw20Package.GET_RECORDS_TYPE__RESULT_TYPE:
                setResultType((ResultType)newValue);
                return;
            case Csw20Package.GET_RECORDS_TYPE__START_POSITION:
                setStartPosition((BigInteger)newValue);
                return;
            case Csw20Package.GET_RECORDS_TYPE__QUERY:
                setQuery(newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case Csw20Package.GET_RECORDS_TYPE__DISTRIBUTED_SEARCH:
                setDistributedSearch((DistributedSearchType)null);
                return;
            case Csw20Package.GET_RECORDS_TYPE__RESPONSE_HANDLER:
                setResponseHandler(RESPONSE_HANDLER_EDEFAULT);
                return;
            case Csw20Package.GET_RECORDS_TYPE__ANY:
                getAny().clear();
                return;
            case Csw20Package.GET_RECORDS_TYPE__MAX_RECORDS:
                unsetMaxRecords();
                return;
            case Csw20Package.GET_RECORDS_TYPE__OUTPUT_FORMAT:
                unsetOutputFormat();
                return;
            case Csw20Package.GET_RECORDS_TYPE__OUTPUT_SCHEMA:
                setOutputSchema(OUTPUT_SCHEMA_EDEFAULT);
                return;
            case Csw20Package.GET_RECORDS_TYPE__REQUEST_ID:
                setRequestId(REQUEST_ID_EDEFAULT);
                return;
            case Csw20Package.GET_RECORDS_TYPE__RESULT_TYPE:
                unsetResultType();
                return;
            case Csw20Package.GET_RECORDS_TYPE__START_POSITION:
                unsetStartPosition();
                return;
            case Csw20Package.GET_RECORDS_TYPE__QUERY:
                setQuery(QUERY_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Csw20Package.GET_RECORDS_TYPE__DISTRIBUTED_SEARCH:
                return distributedSearch != null;
            case Csw20Package.GET_RECORDS_TYPE__RESPONSE_HANDLER:
                return RESPONSE_HANDLER_EDEFAULT == null ? responseHandler != null : !RESPONSE_HANDLER_EDEFAULT.equals(responseHandler);
            case Csw20Package.GET_RECORDS_TYPE__ANY:
                return any != null && !any.isEmpty();
            case Csw20Package.GET_RECORDS_TYPE__MAX_RECORDS:
                return isSetMaxRecords();
            case Csw20Package.GET_RECORDS_TYPE__OUTPUT_FORMAT:
                return isSetOutputFormat();
            case Csw20Package.GET_RECORDS_TYPE__OUTPUT_SCHEMA:
                return OUTPUT_SCHEMA_EDEFAULT == null ? outputSchema != null : !OUTPUT_SCHEMA_EDEFAULT.equals(outputSchema);
            case Csw20Package.GET_RECORDS_TYPE__REQUEST_ID:
                return REQUEST_ID_EDEFAULT == null ? requestId != null : !REQUEST_ID_EDEFAULT.equals(requestId);
            case Csw20Package.GET_RECORDS_TYPE__RESULT_TYPE:
                return isSetResultType();
            case Csw20Package.GET_RECORDS_TYPE__START_POSITION:
                return isSetStartPosition();
            case Csw20Package.GET_RECORDS_TYPE__QUERY:
                return QUERY_EDEFAULT == null ? query != null : !QUERY_EDEFAULT.equals(query);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (responseHandler: ");
        result.append(responseHandler);
        result.append(", any: ");
        result.append(any);
        result.append(", maxRecords: ");
        if (maxRecordsESet) result.append(maxRecords); else result.append("<unset>");
        result.append(", outputFormat: ");
        if (outputFormatESet) result.append(outputFormat); else result.append("<unset>");
        result.append(", outputSchema: ");
        result.append(outputSchema);
        result.append(", requestId: ");
        result.append(requestId);
        result.append(", resultType: ");
        if (resultTypeESet) result.append(resultType); else result.append("<unset>");
        result.append(", startPosition: ");
        if (startPositionESet) result.append(startPosition); else result.append("<unset>");
        result.append(", query: ");
        result.append(query);
        result.append(')');
        return result.toString();
    }

} //GetRecordsTypeImpl
