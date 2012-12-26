/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import java.util.Collection;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.DeleteType;
import net.opengis.cat.csw20.InsertType;
import net.opengis.cat.csw20.TransactionType;
import net.opengis.cat.csw20.UpdateType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transaction Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.TransactionTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.TransactionTypeImpl#getInsert <em>Insert</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.TransactionTypeImpl#getUpdate <em>Update</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.TransactionTypeImpl#getDelete <em>Delete</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.TransactionTypeImpl#getRequestId <em>Request Id</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.TransactionTypeImpl#isVerboseResponse <em>Verbose Response</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TransactionTypeImpl extends RequestBaseTypeImpl implements TransactionType {
    /**
     * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap group;

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
     * The default value of the '{@link #isVerboseResponse() <em>Verbose Response</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isVerboseResponse()
     * @generated
     * @ordered
     */
    protected static final boolean VERBOSE_RESPONSE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isVerboseResponse() <em>Verbose Response</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isVerboseResponse()
     * @generated
     * @ordered
     */
    protected boolean verboseResponse = VERBOSE_RESPONSE_EDEFAULT;

    /**
     * This is true if the Verbose Response attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean verboseResponseESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TransactionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.TRANSACTION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getGroup() {
        if (group == null) {
            group = new BasicFeatureMap(this, Csw20Package.TRANSACTION_TYPE__GROUP);
        }
        return group;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<InsertType> getInsert() {
        return getGroup().list(Csw20Package.Literals.TRANSACTION_TYPE__INSERT);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<UpdateType> getUpdate() {
        return getGroup().list(Csw20Package.Literals.TRANSACTION_TYPE__UPDATE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DeleteType> getDelete() {
        return getGroup().list(Csw20Package.Literals.TRANSACTION_TYPE__DELETE);
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
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.TRANSACTION_TYPE__REQUEST_ID, oldRequestId, requestId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isVerboseResponse() {
        return verboseResponse;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVerboseResponse(boolean newVerboseResponse) {
        boolean oldVerboseResponse = verboseResponse;
        verboseResponse = newVerboseResponse;
        boolean oldVerboseResponseESet = verboseResponseESet;
        verboseResponseESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.TRANSACTION_TYPE__VERBOSE_RESPONSE, oldVerboseResponse, verboseResponse, !oldVerboseResponseESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetVerboseResponse() {
        boolean oldVerboseResponse = verboseResponse;
        boolean oldVerboseResponseESet = verboseResponseESet;
        verboseResponse = VERBOSE_RESPONSE_EDEFAULT;
        verboseResponseESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Csw20Package.TRANSACTION_TYPE__VERBOSE_RESPONSE, oldVerboseResponse, VERBOSE_RESPONSE_EDEFAULT, oldVerboseResponseESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetVerboseResponse() {
        return verboseResponseESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.TRANSACTION_TYPE__GROUP:
                return ((InternalEList<?>)getGroup()).basicRemove(otherEnd, msgs);
            case Csw20Package.TRANSACTION_TYPE__INSERT:
                return ((InternalEList<?>)getInsert()).basicRemove(otherEnd, msgs);
            case Csw20Package.TRANSACTION_TYPE__UPDATE:
                return ((InternalEList<?>)getUpdate()).basicRemove(otherEnd, msgs);
            case Csw20Package.TRANSACTION_TYPE__DELETE:
                return ((InternalEList<?>)getDelete()).basicRemove(otherEnd, msgs);
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
            case Csw20Package.TRANSACTION_TYPE__GROUP:
                if (coreType) return getGroup();
                return ((FeatureMap.Internal)getGroup()).getWrapper();
            case Csw20Package.TRANSACTION_TYPE__INSERT:
                return getInsert();
            case Csw20Package.TRANSACTION_TYPE__UPDATE:
                return getUpdate();
            case Csw20Package.TRANSACTION_TYPE__DELETE:
                return getDelete();
            case Csw20Package.TRANSACTION_TYPE__REQUEST_ID:
                return getRequestId();
            case Csw20Package.TRANSACTION_TYPE__VERBOSE_RESPONSE:
                return isVerboseResponse();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Csw20Package.TRANSACTION_TYPE__GROUP:
                ((FeatureMap.Internal)getGroup()).set(newValue);
                return;
            case Csw20Package.TRANSACTION_TYPE__INSERT:
                getInsert().clear();
                getInsert().addAll((Collection<? extends InsertType>)newValue);
                return;
            case Csw20Package.TRANSACTION_TYPE__UPDATE:
                getUpdate().clear();
                getUpdate().addAll((Collection<? extends UpdateType>)newValue);
                return;
            case Csw20Package.TRANSACTION_TYPE__DELETE:
                getDelete().clear();
                getDelete().addAll((Collection<? extends DeleteType>)newValue);
                return;
            case Csw20Package.TRANSACTION_TYPE__REQUEST_ID:
                setRequestId((String)newValue);
                return;
            case Csw20Package.TRANSACTION_TYPE__VERBOSE_RESPONSE:
                setVerboseResponse((Boolean)newValue);
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
            case Csw20Package.TRANSACTION_TYPE__GROUP:
                getGroup().clear();
                return;
            case Csw20Package.TRANSACTION_TYPE__INSERT:
                getInsert().clear();
                return;
            case Csw20Package.TRANSACTION_TYPE__UPDATE:
                getUpdate().clear();
                return;
            case Csw20Package.TRANSACTION_TYPE__DELETE:
                getDelete().clear();
                return;
            case Csw20Package.TRANSACTION_TYPE__REQUEST_ID:
                setRequestId(REQUEST_ID_EDEFAULT);
                return;
            case Csw20Package.TRANSACTION_TYPE__VERBOSE_RESPONSE:
                unsetVerboseResponse();
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
            case Csw20Package.TRANSACTION_TYPE__GROUP:
                return group != null && !group.isEmpty();
            case Csw20Package.TRANSACTION_TYPE__INSERT:
                return !getInsert().isEmpty();
            case Csw20Package.TRANSACTION_TYPE__UPDATE:
                return !getUpdate().isEmpty();
            case Csw20Package.TRANSACTION_TYPE__DELETE:
                return !getDelete().isEmpty();
            case Csw20Package.TRANSACTION_TYPE__REQUEST_ID:
                return REQUEST_ID_EDEFAULT == null ? requestId != null : !REQUEST_ID_EDEFAULT.equals(requestId);
            case Csw20Package.TRANSACTION_TYPE__VERBOSE_RESPONSE:
                return isSetVerboseResponse();
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
        result.append(" (group: ");
        result.append(group);
        result.append(", requestId: ");
        result.append(requestId);
        result.append(", verboseResponse: ");
        if (verboseResponseESet) result.append(verboseResponse); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //TransactionTypeImpl
