/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import net.opengis.wfs.NativeType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Native Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.NativeTypeImpl#isSafeToIgnore <em>Safe To Ignore</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.NativeTypeImpl#getVendorId <em>Vendor Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class NativeTypeImpl extends EObjectImpl implements NativeType {
	/**
     * The default value of the '{@link #isSafeToIgnore() <em>Safe To Ignore</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #isSafeToIgnore()
     * @generated
     * @ordered
     */
	protected static final boolean SAFE_TO_IGNORE_EDEFAULT = false;

	/**
     * The cached value of the '{@link #isSafeToIgnore() <em>Safe To Ignore</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #isSafeToIgnore()
     * @generated
     * @ordered
     */
	protected boolean safeToIgnore = SAFE_TO_IGNORE_EDEFAULT;

	/**
     * This is true if the Safe To Ignore attribute has been set.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
	protected boolean safeToIgnoreESet;

	/**
     * The default value of the '{@link #getVendorId() <em>Vendor Id</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getVendorId()
     * @generated
     * @ordered
     */
	protected static final String VENDOR_ID_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getVendorId() <em>Vendor Id</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getVendorId()
     * @generated
     * @ordered
     */
	protected String vendorId = VENDOR_ID_EDEFAULT;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected NativeTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.NATIVE_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public boolean isSafeToIgnore() {
        return safeToIgnore;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setSafeToIgnore(boolean newSafeToIgnore) {
        boolean oldSafeToIgnore = safeToIgnore;
        safeToIgnore = newSafeToIgnore;
        boolean oldSafeToIgnoreESet = safeToIgnoreESet;
        safeToIgnoreESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.NATIVE_TYPE__SAFE_TO_IGNORE, oldSafeToIgnore, safeToIgnore, !oldSafeToIgnoreESet));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void unsetSafeToIgnore() {
        boolean oldSafeToIgnore = safeToIgnore;
        boolean oldSafeToIgnoreESet = safeToIgnoreESet;
        safeToIgnore = SAFE_TO_IGNORE_EDEFAULT;
        safeToIgnoreESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, WfsPackage.NATIVE_TYPE__SAFE_TO_IGNORE, oldSafeToIgnore, SAFE_TO_IGNORE_EDEFAULT, oldSafeToIgnoreESet));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public boolean isSetSafeToIgnore() {
        return safeToIgnoreESet;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getVendorId() {
        return vendorId;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setVendorId(String newVendorId) {
        String oldVendorId = vendorId;
        vendorId = newVendorId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.NATIVE_TYPE__VENDOR_ID, oldVendorId, vendorId));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsPackage.NATIVE_TYPE__SAFE_TO_IGNORE:
                return isSafeToIgnore() ? Boolean.TRUE : Boolean.FALSE;
            case WfsPackage.NATIVE_TYPE__VENDOR_ID:
                return getVendorId();
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
            case WfsPackage.NATIVE_TYPE__SAFE_TO_IGNORE:
                setSafeToIgnore(((Boolean)newValue).booleanValue());
                return;
            case WfsPackage.NATIVE_TYPE__VENDOR_ID:
                setVendorId((String)newValue);
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
            case WfsPackage.NATIVE_TYPE__SAFE_TO_IGNORE:
                unsetSafeToIgnore();
                return;
            case WfsPackage.NATIVE_TYPE__VENDOR_ID:
                setVendorId(VENDOR_ID_EDEFAULT);
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
            case WfsPackage.NATIVE_TYPE__SAFE_TO_IGNORE:
                return isSetSafeToIgnore();
            case WfsPackage.NATIVE_TYPE__VENDOR_ID:
                return VENDOR_ID_EDEFAULT == null ? vendorId != null : !VENDOR_ID_EDEFAULT.equals(vendorId);
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
        result.append(" (safeToIgnore: ");
        if (safeToIgnoreESet) result.append(safeToIgnore); else result.append("<unset>");
        result.append(", vendorId: ");
        result.append(vendorId);
        result.append(')');
        return result.toString();
    }

} //NativeTypeImpl