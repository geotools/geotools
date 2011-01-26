/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.math.BigInteger;

import net.opengis.wfs.TransactionSummaryType;
import net.opengis.wfs.WfsPackage;

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
 *   <li>{@link net.opengis.wfs.impl.TransactionSummaryTypeImpl#getTotalInserted <em>Total Inserted</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.TransactionSummaryTypeImpl#getTotalUpdated <em>Total Updated</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.TransactionSummaryTypeImpl#getTotalDeleted <em>Total Deleted</em>}</li>
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
	protected EClass eStaticClass() {
        return WfsPackage.Literals.TRANSACTION_SUMMARY_TYPE;
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED, oldTotalInserted, totalInserted));
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED, oldTotalUpdated, totalUpdated));
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED, oldTotalDeleted, totalDeleted));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED:
                return getTotalInserted();
            case WfsPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED:
                return getTotalUpdated();
            case WfsPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED:
                return getTotalDeleted();
        }
        return super.eGet(featureID, resolve, coreType);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case WfsPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED:
                setTotalInserted((BigInteger)newValue);
                return;
            case WfsPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED:
                setTotalUpdated((BigInteger)newValue);
                return;
            case WfsPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED:
                setTotalDeleted((BigInteger)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void eUnset(int featureID) {
        switch (featureID) {
            case WfsPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED:
                setTotalInserted(TOTAL_INSERTED_EDEFAULT);
                return;
            case WfsPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED:
                setTotalUpdated(TOTAL_UPDATED_EDEFAULT);
                return;
            case WfsPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED:
                setTotalDeleted(TOTAL_DELETED_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public boolean eIsSet(int featureID) {
        switch (featureID) {
            case WfsPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_INSERTED:
                return TOTAL_INSERTED_EDEFAULT == null ? totalInserted != null : !TOTAL_INSERTED_EDEFAULT.equals(totalInserted);
            case WfsPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_UPDATED:
                return TOTAL_UPDATED_EDEFAULT == null ? totalUpdated != null : !TOTAL_UPDATED_EDEFAULT.equals(totalUpdated);
            case WfsPackage.TRANSACTION_SUMMARY_TYPE__TOTAL_DELETED:
                return TOTAL_DELETED_EDEFAULT == null ? totalDeleted != null : !TOTAL_DELETED_EDEFAULT.equals(totalDeleted);
        }
        return super.eIsSet(featureID);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (totalInserted: ");
        result.append(totalInserted);
        result.append(", totalUpdated: ");
        result.append(totalUpdated);
        result.append(", totalDeleted: ");
        result.append(totalDeleted);
        result.append(')');
        return result.toString();
    }

} //TransactionSummaryTypeImpl