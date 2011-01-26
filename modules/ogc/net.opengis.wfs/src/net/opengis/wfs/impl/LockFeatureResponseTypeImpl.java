/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import net.opengis.wfs.FeaturesLockedType;
import net.opengis.wfs.FeaturesNotLockedType;
import net.opengis.wfs.LockFeatureResponseType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Lock Feature Response Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.LockFeatureResponseTypeImpl#getLockId <em>Lock Id</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.LockFeatureResponseTypeImpl#getFeaturesLocked <em>Features Locked</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.LockFeatureResponseTypeImpl#getFeaturesNotLocked <em>Features Not Locked</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LockFeatureResponseTypeImpl extends EObjectImpl implements LockFeatureResponseType {
	/**
     * The default value of the '{@link #getLockId() <em>Lock Id</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getLockId()
     * @generated
     * @ordered
     */
	protected static final String LOCK_ID_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getLockId() <em>Lock Id</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getLockId()
     * @generated
     * @ordered
     */
	protected String lockId = LOCK_ID_EDEFAULT;

	/**
     * The cached value of the '{@link #getFeaturesLocked() <em>Features Locked</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getFeaturesLocked()
     * @generated
     * @ordered
     */
	protected FeaturesLockedType featuresLocked;

	/**
     * The cached value of the '{@link #getFeaturesNotLocked() <em>Features Not Locked</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getFeaturesNotLocked()
     * @generated
     * @ordered
     */
	protected FeaturesNotLockedType featuresNotLocked;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected LockFeatureResponseTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.LOCK_FEATURE_RESPONSE_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getLockId() {
        return lockId;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setLockId(String newLockId) {
        String oldLockId = lockId;
        lockId = newLockId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__LOCK_ID, oldLockId, lockId));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public FeaturesLockedType getFeaturesLocked() {
        return featuresLocked;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetFeaturesLocked(FeaturesLockedType newFeaturesLocked, NotificationChain msgs) {
        FeaturesLockedType oldFeaturesLocked = featuresLocked;
        featuresLocked = newFeaturesLocked;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED, oldFeaturesLocked, newFeaturesLocked);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setFeaturesLocked(FeaturesLockedType newFeaturesLocked) {
        if (newFeaturesLocked != featuresLocked) {
            NotificationChain msgs = null;
            if (featuresLocked != null)
                msgs = ((InternalEObject)featuresLocked).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED, null, msgs);
            if (newFeaturesLocked != null)
                msgs = ((InternalEObject)newFeaturesLocked).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED, null, msgs);
            msgs = basicSetFeaturesLocked(newFeaturesLocked, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED, newFeaturesLocked, newFeaturesLocked));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public FeaturesNotLockedType getFeaturesNotLocked() {
        return featuresNotLocked;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetFeaturesNotLocked(FeaturesNotLockedType newFeaturesNotLocked, NotificationChain msgs) {
        FeaturesNotLockedType oldFeaturesNotLocked = featuresNotLocked;
        featuresNotLocked = newFeaturesNotLocked;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED, oldFeaturesNotLocked, newFeaturesNotLocked);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setFeaturesNotLocked(FeaturesNotLockedType newFeaturesNotLocked) {
        if (newFeaturesNotLocked != featuresNotLocked) {
            NotificationChain msgs = null;
            if (featuresNotLocked != null)
                msgs = ((InternalEObject)featuresNotLocked).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED, null, msgs);
            if (newFeaturesNotLocked != null)
                msgs = ((InternalEObject)newFeaturesNotLocked).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED, null, msgs);
            msgs = basicSetFeaturesNotLocked(newFeaturesNotLocked, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED, newFeaturesNotLocked, newFeaturesNotLocked));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED:
                return basicSetFeaturesLocked(null, msgs);
            case WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED:
                return basicSetFeaturesNotLocked(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__LOCK_ID:
                return getLockId();
            case WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED:
                return getFeaturesLocked();
            case WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED:
                return getFeaturesNotLocked();
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
            case WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__LOCK_ID:
                setLockId((String)newValue);
                return;
            case WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED:
                setFeaturesLocked((FeaturesLockedType)newValue);
                return;
            case WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED:
                setFeaturesNotLocked((FeaturesNotLockedType)newValue);
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
            case WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__LOCK_ID:
                setLockId(LOCK_ID_EDEFAULT);
                return;
            case WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED:
                setFeaturesLocked((FeaturesLockedType)null);
                return;
            case WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED:
                setFeaturesNotLocked((FeaturesNotLockedType)null);
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
            case WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__LOCK_ID:
                return LOCK_ID_EDEFAULT == null ? lockId != null : !LOCK_ID_EDEFAULT.equals(lockId);
            case WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_LOCKED:
                return featuresLocked != null;
            case WfsPackage.LOCK_FEATURE_RESPONSE_TYPE__FEATURES_NOT_LOCKED:
                return featuresNotLocked != null;
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
        result.append(" (lockId: ");
        result.append(lockId);
        result.append(')');
        return result.toString();
    }

} //LockFeatureResponseTypeImpl