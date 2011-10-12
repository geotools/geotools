/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.math.BigInteger;

import net.opengis.wfs20.AllSomeType;
import net.opengis.wfs20.GetFeatureWithLockType;
import net.opengis.wfs20.Wfs20Package;

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
 *   <li>{@link net.opengis.wfs20.impl.GetFeatureWithLockTypeImpl#getExpiry <em>Expiry</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetFeatureWithLockTypeImpl#getLockAction <em>Lock Action</em>}</li>
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
    protected static final BigInteger EXPIRY_EDEFAULT = new BigInteger("300");

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
     * The default value of the '{@link #getLockAction() <em>Lock Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLockAction()
     * @generated
     * @ordered
     */
    protected static final AllSomeType LOCK_ACTION_EDEFAULT = AllSomeType.ALL;

    /**
     * The cached value of the '{@link #getLockAction() <em>Lock Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLockAction()
     * @generated
     * @ordered
     */
    protected AllSomeType lockAction = LOCK_ACTION_EDEFAULT;

    /**
     * This is true if the Lock Action attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean lockActionESet;

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
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.GET_FEATURE_WITH_LOCK_TYPE;
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY, oldExpiry, expiry, !oldExpiryESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY, oldExpiry, EXPIRY_EDEFAULT, oldExpiryESet));
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
    public AllSomeType getLockAction() {
        return lockAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLockAction(AllSomeType newLockAction) {
        AllSomeType oldLockAction = lockAction;
        lockAction = newLockAction == null ? LOCK_ACTION_EDEFAULT : newLockAction;
        boolean oldLockActionESet = lockActionESet;
        lockActionESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_FEATURE_WITH_LOCK_TYPE__LOCK_ACTION, oldLockAction, lockAction, !oldLockActionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetLockAction() {
        AllSomeType oldLockAction = lockAction;
        boolean oldLockActionESet = lockActionESet;
        lockAction = LOCK_ACTION_EDEFAULT;
        lockActionESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.GET_FEATURE_WITH_LOCK_TYPE__LOCK_ACTION, oldLockAction, LOCK_ACTION_EDEFAULT, oldLockActionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetLockAction() {
        return lockActionESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wfs20Package.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY:
                return getExpiry();
            case Wfs20Package.GET_FEATURE_WITH_LOCK_TYPE__LOCK_ACTION:
                return getLockAction();
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
            case Wfs20Package.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY:
                setExpiry((BigInteger)newValue);
                return;
            case Wfs20Package.GET_FEATURE_WITH_LOCK_TYPE__LOCK_ACTION:
                setLockAction((AllSomeType)newValue);
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
            case Wfs20Package.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY:
                unsetExpiry();
                return;
            case Wfs20Package.GET_FEATURE_WITH_LOCK_TYPE__LOCK_ACTION:
                unsetLockAction();
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
            case Wfs20Package.GET_FEATURE_WITH_LOCK_TYPE__EXPIRY:
                return isSetExpiry();
            case Wfs20Package.GET_FEATURE_WITH_LOCK_TYPE__LOCK_ACTION:
                return isSetLockAction();
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
        result.append(" (expiry: ");
        if (expiryESet) result.append(expiry); else result.append("<unset>");
        result.append(", lockAction: ");
        if (lockActionESet) result.append(lockAction); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //GetFeatureWithLockTypeImpl
