/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.math.BigInteger;

import net.opengis.fes20.AbstractQueryExpressionType;

import net.opengis.wfs20.AllSomeType;
import net.opengis.wfs20.LockFeatureType;
import net.opengis.wfs20.Wfs20Package;

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
 * An implementation of the model object '<em><b>Lock Feature Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.LockFeatureTypeImpl#getAbstractQueryExpressionGroup <em>Abstract Query Expression Group</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.LockFeatureTypeImpl#getAbstractQueryExpression <em>Abstract Query Expression</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.LockFeatureTypeImpl#getExpiry <em>Expiry</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.LockFeatureTypeImpl#getLockAction <em>Lock Action</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.LockFeatureTypeImpl#getLockId <em>Lock Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LockFeatureTypeImpl extends BaseRequestTypeImpl implements LockFeatureType {
    /**
     * The cached value of the '{@link #getAbstractQueryExpressionGroup() <em>Abstract Query Expression Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstractQueryExpressionGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap abstractQueryExpressionGroup;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LockFeatureTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.LOCK_FEATURE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getAbstractQueryExpressionGroup() {
        if (abstractQueryExpressionGroup == null) {
            abstractQueryExpressionGroup = new BasicFeatureMap(this, Wfs20Package.LOCK_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION_GROUP);
        }
        return abstractQueryExpressionGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AbstractQueryExpressionType> getAbstractQueryExpression() {
        return getAbstractQueryExpressionGroup().list(Wfs20Package.Literals.LOCK_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION);
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.LOCK_FEATURE_TYPE__EXPIRY, oldExpiry, expiry, !oldExpiryESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.LOCK_FEATURE_TYPE__EXPIRY, oldExpiry, EXPIRY_EDEFAULT, oldExpiryESet));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.LOCK_FEATURE_TYPE__LOCK_ACTION, oldLockAction, lockAction, !oldLockActionESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.LOCK_FEATURE_TYPE__LOCK_ACTION, oldLockAction, LOCK_ACTION_EDEFAULT, oldLockActionESet));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.LOCK_FEATURE_TYPE__LOCK_ID, oldLockId, lockId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wfs20Package.LOCK_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION_GROUP:
                return ((InternalEList<?>)getAbstractQueryExpressionGroup()).basicRemove(otherEnd, msgs);
            case Wfs20Package.LOCK_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION:
                return ((InternalEList<?>)getAbstractQueryExpression()).basicRemove(otherEnd, msgs);
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
            case Wfs20Package.LOCK_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION_GROUP:
                if (coreType) return getAbstractQueryExpressionGroup();
                return ((FeatureMap.Internal)getAbstractQueryExpressionGroup()).getWrapper();
            case Wfs20Package.LOCK_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION:
                return getAbstractQueryExpression();
            case Wfs20Package.LOCK_FEATURE_TYPE__EXPIRY:
                return getExpiry();
            case Wfs20Package.LOCK_FEATURE_TYPE__LOCK_ACTION:
                return getLockAction();
            case Wfs20Package.LOCK_FEATURE_TYPE__LOCK_ID:
                return getLockId();
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
            case Wfs20Package.LOCK_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION_GROUP:
                ((FeatureMap.Internal)getAbstractQueryExpressionGroup()).set(newValue);
                return;
            case Wfs20Package.LOCK_FEATURE_TYPE__EXPIRY:
                setExpiry((BigInteger)newValue);
                return;
            case Wfs20Package.LOCK_FEATURE_TYPE__LOCK_ACTION:
                setLockAction((AllSomeType)newValue);
                return;
            case Wfs20Package.LOCK_FEATURE_TYPE__LOCK_ID:
                setLockId((String)newValue);
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
            case Wfs20Package.LOCK_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION_GROUP:
                getAbstractQueryExpressionGroup().clear();
                return;
            case Wfs20Package.LOCK_FEATURE_TYPE__EXPIRY:
                unsetExpiry();
                return;
            case Wfs20Package.LOCK_FEATURE_TYPE__LOCK_ACTION:
                unsetLockAction();
                return;
            case Wfs20Package.LOCK_FEATURE_TYPE__LOCK_ID:
                setLockId(LOCK_ID_EDEFAULT);
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
            case Wfs20Package.LOCK_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION_GROUP:
                return abstractQueryExpressionGroup != null && !abstractQueryExpressionGroup.isEmpty();
            case Wfs20Package.LOCK_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION:
                return !getAbstractQueryExpression().isEmpty();
            case Wfs20Package.LOCK_FEATURE_TYPE__EXPIRY:
                return isSetExpiry();
            case Wfs20Package.LOCK_FEATURE_TYPE__LOCK_ACTION:
                return isSetLockAction();
            case Wfs20Package.LOCK_FEATURE_TYPE__LOCK_ID:
                return LOCK_ID_EDEFAULT == null ? lockId != null : !LOCK_ID_EDEFAULT.equals(lockId);
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
        result.append(" (abstractQueryExpressionGroup: ");
        result.append(abstractQueryExpressionGroup);
        result.append(", expiry: ");
        if (expiryESet) result.append(expiry); else result.append("<unset>");
        result.append(", lockAction: ");
        if (lockActionESet) result.append(lockAction); else result.append("<unset>");
        result.append(", lockId: ");
        result.append(lockId);
        result.append(')');
        return result.toString();
    }

} //LockFeatureTypeImpl
