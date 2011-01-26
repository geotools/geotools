/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.math.BigInteger;

import net.opengis.wfs.GetFeatureWithLockType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Feature With Lock Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.GetFeatureWithLockTypeImpl#getExpiry <em>Expiry</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetFeatureWithLockTypeImpl extends GetFeatureTypeImpl implements GetFeatureWithLockType {
	/**
     * The default value of the '{@link #getExpiry() <em>Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getExpiry()
     * @generated
     * @ordered
     */
	protected static final BigInteger EXPIRY_EDEFAULT = new BigInteger("5");

	/**
     * The cached value of the '{@link #getExpiry() <em>Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getExpiry()
     * @generated
     * @ordered
     */
	protected BigInteger expiry = EXPIRY_EDEFAULT;

	/**
     * This is true if the Expiry attribute has been set.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	protected boolean expiryESet;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected GetFeatureWithLockTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.GET_FEATURE_WITH_LOCK_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public BigInteger getExpiry() {
        return expiry;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setExpiry(BigInteger newExpiry) {
        BigInteger oldExpiry = expiry;
        expiry = newExpiry;
        boolean oldExpiryESet = expiryESet;
        expiryESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY, oldExpiry, expiry, !oldExpiryESet));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void unsetExpiry() {
        BigInteger oldExpiry = expiry;
        boolean oldExpiryESet = expiryESet;
        expiry = EXPIRY_EDEFAULT;
        expiryESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY, oldExpiry, EXPIRY_EDEFAULT, oldExpiryESet));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public boolean isSetExpiry() {
        return expiryESet;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY:
                return getExpiry();
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
            case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY:
                setExpiry((BigInteger)newValue);
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
            case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY:
                unsetExpiry();
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
            case WfsPackage.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY:
                return isSetExpiry();
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
        result.append(" (expiry: ");
        if (expiryESet) result.append(expiry); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //GetFeatureWithLockTypeImpl