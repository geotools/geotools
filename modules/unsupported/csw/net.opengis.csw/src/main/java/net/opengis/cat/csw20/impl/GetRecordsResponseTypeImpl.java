/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.GetRecordsResponseType;
import net.opengis.cat.csw20.RequestStatusType;
import net.opengis.cat.csw20.SearchResultsType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Records Response Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordsResponseTypeImpl#getRequestId <em>Request Id</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordsResponseTypeImpl#getSearchStatus <em>Search Status</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordsResponseTypeImpl#getSearchResults <em>Search Results</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.GetRecordsResponseTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetRecordsResponseTypeImpl extends EObjectImpl implements GetRecordsResponseType {
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
     * The cached value of the '{@link #getSearchStatus() <em>Search Status</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSearchStatus()
     * @generated
     * @ordered
     */
    protected RequestStatusType searchStatus;

    /**
     * The cached value of the '{@link #getSearchResults() <em>Search Results</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSearchResults()
     * @generated
     * @ordered
     */
    protected SearchResultsType searchResults;

    /**
     * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected static final String VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected String version = VERSION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GetRecordsResponseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.GET_RECORDS_RESPONSE_TYPE;
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
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORDS_RESPONSE_TYPE__REQUEST_ID, oldRequestId, requestId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RequestStatusType getSearchStatus() {
        return searchStatus;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSearchStatus(RequestStatusType newSearchStatus, NotificationChain msgs) {
        RequestStatusType oldSearchStatus = searchStatus;
        searchStatus = newSearchStatus;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_STATUS, oldSearchStatus, newSearchStatus);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSearchStatus(RequestStatusType newSearchStatus) {
        if (newSearchStatus != searchStatus) {
            NotificationChain msgs = null;
            if (searchStatus != null)
                msgs = ((InternalEObject)searchStatus).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_STATUS, null, msgs);
            if (newSearchStatus != null)
                msgs = ((InternalEObject)newSearchStatus).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_STATUS, null, msgs);
            msgs = basicSetSearchStatus(newSearchStatus, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_STATUS, newSearchStatus, newSearchStatus));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SearchResultsType getSearchResults() {
        return searchResults;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSearchResults(SearchResultsType newSearchResults, NotificationChain msgs) {
        SearchResultsType oldSearchResults = searchResults;
        searchResults = newSearchResults;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_RESULTS, oldSearchResults, newSearchResults);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSearchResults(SearchResultsType newSearchResults) {
        if (newSearchResults != searchResults) {
            NotificationChain msgs = null;
            if (searchResults != null)
                msgs = ((InternalEObject)searchResults).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_RESULTS, null, msgs);
            if (newSearchResults != null)
                msgs = ((InternalEObject)newSearchResults).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_RESULTS, null, msgs);
            msgs = basicSetSearchResults(newSearchResults, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_RESULTS, newSearchResults, newSearchResults));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getVersion() {
        return version;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVersion(String newVersion) {
        String oldVersion = version;
        version = newVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.GET_RECORDS_RESPONSE_TYPE__VERSION, oldVersion, version));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_STATUS:
                return basicSetSearchStatus(null, msgs);
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_RESULTS:
                return basicSetSearchResults(null, msgs);
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
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__REQUEST_ID:
                return getRequestId();
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_STATUS:
                return getSearchStatus();
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_RESULTS:
                return getSearchResults();
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__VERSION:
                return getVersion();
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
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__REQUEST_ID:
                setRequestId((String)newValue);
                return;
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_STATUS:
                setSearchStatus((RequestStatusType)newValue);
                return;
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_RESULTS:
                setSearchResults((SearchResultsType)newValue);
                return;
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__VERSION:
                setVersion((String)newValue);
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
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__REQUEST_ID:
                setRequestId(REQUEST_ID_EDEFAULT);
                return;
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_STATUS:
                setSearchStatus((RequestStatusType)null);
                return;
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_RESULTS:
                setSearchResults((SearchResultsType)null);
                return;
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__VERSION:
                setVersion(VERSION_EDEFAULT);
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
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__REQUEST_ID:
                return REQUEST_ID_EDEFAULT == null ? requestId != null : !REQUEST_ID_EDEFAULT.equals(requestId);
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_STATUS:
                return searchStatus != null;
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__SEARCH_RESULTS:
                return searchResults != null;
            case Csw20Package.GET_RECORDS_RESPONSE_TYPE__VERSION:
                return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
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
        result.append(" (requestId: ");
        result.append(requestId);
        result.append(", version: ");
        result.append(version);
        result.append(')');
        return result.toString();
    }

} //GetRecordsResponseTypeImpl
