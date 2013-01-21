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
import net.opengis.cat.csw20.TransactionSummaryType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transaction Summary Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.TransactionSummaryTypeImpl#getTotalInserted <em>Total Inserted</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.TransactionSummaryTypeImpl#getTotalUpdated <em>Total Updated</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.TransactionSummaryTypeImpl#getTotalDeleted <em>Total Deleted</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.TransactionSummaryTypeImpl#getRequestId <em>Request Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TransactionSummaryTypeImpl extends EObjectImpl implements TransactionSummaryType {
    /**
     * The default value of the '{@link #getTotalInserted() <em>Total Inserted</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTotalInserted()
     * @generated
     * @ordered
     */
    protected static final BigInteger TOTAL_INSERTED_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTotalInserted() <em>Total Inserted</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTotalInserted()
     * @generated
     * @ordered
     */
    protected BigInteger totalInserted = TOTAL_INSERTED_EDEFAULT;

    /**
     * The default value of the '{@link #getTotalUpdated() <em>Total Updated</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTotalUpdated()
     * @generated
     * @ordered
     */
    protected static final BigInteger TOTAL_UPDATED_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTotalUpdated() <em>Total Updated</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTotalUpdated()
     * @generated
     * @ordered
     */
    protected BigInteger totalUpdated = TOTAL_UPDATED_EDEFAULT;

    /**
     * The default value of the '{@link #getTotalDeleted() <em>Total Deleted</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTotalDeleted()
     * @generated
     * @ordered
     */
    protected static final BigInteger TOTAL_DELETED_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTotalDeleted() <em>Total Deleted</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTotalDeleted()
     * @generated
     * @ordered
     */
    protected BigInteger totalDeleted = TOTAL_DELETED_EDEFAULT;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TransactionSummaryTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.TRANSACTION_SUMMARY_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getTotalInserted() {
        return totalInserted;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTotalInserted(BigInteger newTotalInserted) {
        BigInteger oldTotalInserted = totalInserted;
        totalInserted = newTotalInserted;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED, oldTotalInserted, totalInserted));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getTotalUpdated() {
        return totalUpdated;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTotalUpdated(BigInteger newTotalUpdated) {
        BigInteger oldTotalUpdated = totalUpdated;
        totalUpdated = newTotalUpdated;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED, oldTotalUpdated, totalUpdated));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getTotalDeleted() {
        return totalDeleted;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTotalDeleted(BigInteger newTotalDeleted) {
        BigInteger oldTotalDeleted = totalDeleted;
        totalDeleted = newTotalDeleted;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED, oldTotalDeleted, totalDeleted));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.TRANSACTION_SUMMARY_TYPE__REQUEST_ID, oldRequestId, requestId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Csw20Package.TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED:
                return getTotalInserted();
            case Csw20Package.TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED:
                return getTotalUpdated();
            case Csw20Package.TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED:
                return getTotalDeleted();
            case Csw20Package.TRANSACTION_SUMMARY_TYPE__REQUEST_ID:
                return getRequestId();
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
            case Csw20Package.TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED:
                setTotalInserted((BigInteger)newValue);
                return;
            case Csw20Package.TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED:
                setTotalUpdated((BigInteger)newValue);
                return;
            case Csw20Package.TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED:
                setTotalDeleted((BigInteger)newValue);
                return;
            case Csw20Package.TRANSACTION_SUMMARY_TYPE__REQUEST_ID:
                setRequestId((String)newValue);
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
            case Csw20Package.TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED:
                setTotalInserted(TOTAL_INSERTED_EDEFAULT);
                return;
            case Csw20Package.TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED:
                setTotalUpdated(TOTAL_UPDATED_EDEFAULT);
                return;
            case Csw20Package.TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED:
                setTotalDeleted(TOTAL_DELETED_EDEFAULT);
                return;
            case Csw20Package.TRANSACTION_SUMMARY_TYPE__REQUEST_ID:
                setRequestId(REQUEST_ID_EDEFAULT);
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
            case Csw20Package.TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED:
                return TOTAL_INSERTED_EDEFAULT == null ? totalInserted != null : !TOTAL_INSERTED_EDEFAULT.equals(totalInserted);
            case Csw20Package.TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED:
                return TOTAL_UPDATED_EDEFAULT == null ? totalUpdated != null : !TOTAL_UPDATED_EDEFAULT.equals(totalUpdated);
            case Csw20Package.TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED:
                return TOTAL_DELETED_EDEFAULT == null ? totalDeleted != null : !TOTAL_DELETED_EDEFAULT.equals(totalDeleted);
            case Csw20Package.TRANSACTION_SUMMARY_TYPE__REQUEST_ID:
                return REQUEST_ID_EDEFAULT == null ? requestId != null : !REQUEST_ID_EDEFAULT.equals(requestId);
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
        result.append(" (totalInserted: ");
        result.append(totalInserted);
        result.append(", totalUpdated: ");
        result.append(totalUpdated);
        result.append(", totalDeleted: ");
        result.append(totalDeleted);
        result.append(", requestId: ");
        result.append(requestId);
        result.append(')');
        return result.toString();
    }

} //TransactionSummaryTypeImpl
