/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import net.opengis.wfs20.AbstractTransactionActionType;
import net.opengis.wfs20.AllSomeType;
import net.opengis.wfs20.TransactionType;
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
 * An implementation of the model object '<em><b>Transaction Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.TransactionTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.TransactionTypeImpl#getAbstractTransactionActionGroup <em>Abstract Transaction Action Group</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.TransactionTypeImpl#getAbstractTransactionAction <em>Abstract Transaction Action</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.TransactionTypeImpl#getLockId <em>Lock Id</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.TransactionTypeImpl#getReleaseAction <em>Release Action</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.TransactionTypeImpl#getSrsName <em>Srs Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TransactionTypeImpl extends BaseRequestTypeImpl implements TransactionType {
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
     * The default value of the '{@link #getReleaseAction() <em>Release Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReleaseAction()
     * @generated
     * @ordered
     */
    protected static final AllSomeType RELEASE_ACTION_EDEFAULT = AllSomeType.ALL;

    /**
     * The cached value of the '{@link #getReleaseAction() <em>Release Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReleaseAction()
     * @generated
     * @ordered
     */
    protected AllSomeType releaseAction = RELEASE_ACTION_EDEFAULT;

    /**
     * This is true if the Release Action attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean releaseActionESet;

    /**
     * The default value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
    protected static final String SRS_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
    protected String srsName = SRS_NAME_EDEFAULT;

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
        return Wfs20Package.Literals.TRANSACTION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getGroup() {
        if (group == null) {
            group = new BasicFeatureMap(this, Wfs20Package.TRANSACTION_TYPE__GROUP);
        }
        return group;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getAbstractTransactionActionGroup() {
        return (FeatureMap)getGroup().<FeatureMap.Entry>list(Wfs20Package.Literals.TRANSACTION_TYPE__ABSTRACT_TRANSACTION_ACTION_GROUP);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AbstractTransactionActionType> getAbstractTransactionAction() {
        return getAbstractTransactionActionGroup().list(Wfs20Package.Literals.TRANSACTION_TYPE__ABSTRACT_TRANSACTION_ACTION);
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.TRANSACTION_TYPE__LOCK_ID, oldLockId, lockId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AllSomeType getReleaseAction() {
        return releaseAction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReleaseAction(AllSomeType newReleaseAction) {
        AllSomeType oldReleaseAction = releaseAction;
        releaseAction = newReleaseAction == null ? RELEASE_ACTION_EDEFAULT : newReleaseAction;
        boolean oldReleaseActionESet = releaseActionESet;
        releaseActionESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.TRANSACTION_TYPE__RELEASE_ACTION, oldReleaseAction, releaseAction, !oldReleaseActionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetReleaseAction() {
        AllSomeType oldReleaseAction = releaseAction;
        boolean oldReleaseActionESet = releaseActionESet;
        releaseAction = RELEASE_ACTION_EDEFAULT;
        releaseActionESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.TRANSACTION_TYPE__RELEASE_ACTION, oldReleaseAction, RELEASE_ACTION_EDEFAULT, oldReleaseActionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetReleaseAction() {
        return releaseActionESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSrsName() {
        return srsName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSrsName(String newSrsName) {
        String oldSrsName = srsName;
        srsName = newSrsName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.TRANSACTION_TYPE__SRS_NAME, oldSrsName, srsName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wfs20Package.TRANSACTION_TYPE__GROUP:
                return ((InternalEList<?>)getGroup()).basicRemove(otherEnd, msgs);
            case Wfs20Package.TRANSACTION_TYPE__ABSTRACT_TRANSACTION_ACTION_GROUP:
                return ((InternalEList<?>)getAbstractTransactionActionGroup()).basicRemove(otherEnd, msgs);
            case Wfs20Package.TRANSACTION_TYPE__ABSTRACT_TRANSACTION_ACTION:
                return ((InternalEList<?>)getAbstractTransactionAction()).basicRemove(otherEnd, msgs);
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
            case Wfs20Package.TRANSACTION_TYPE__GROUP:
                if (coreType) return getGroup();
                return ((FeatureMap.Internal)getGroup()).getWrapper();
            case Wfs20Package.TRANSACTION_TYPE__ABSTRACT_TRANSACTION_ACTION_GROUP:
                if (coreType) return getAbstractTransactionActionGroup();
                return ((FeatureMap.Internal)getAbstractTransactionActionGroup()).getWrapper();
            case Wfs20Package.TRANSACTION_TYPE__ABSTRACT_TRANSACTION_ACTION:
                return getAbstractTransactionAction();
            case Wfs20Package.TRANSACTION_TYPE__LOCK_ID:
                return getLockId();
            case Wfs20Package.TRANSACTION_TYPE__RELEASE_ACTION:
                return getReleaseAction();
            case Wfs20Package.TRANSACTION_TYPE__SRS_NAME:
                return getSrsName();
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
            case Wfs20Package.TRANSACTION_TYPE__GROUP:
                ((FeatureMap.Internal)getGroup()).set(newValue);
                return;
            case Wfs20Package.TRANSACTION_TYPE__ABSTRACT_TRANSACTION_ACTION_GROUP:
                ((FeatureMap.Internal)getAbstractTransactionActionGroup()).set(newValue);
                return;
            case Wfs20Package.TRANSACTION_TYPE__LOCK_ID:
                setLockId((String)newValue);
                return;
            case Wfs20Package.TRANSACTION_TYPE__RELEASE_ACTION:
                setReleaseAction((AllSomeType)newValue);
                return;
            case Wfs20Package.TRANSACTION_TYPE__SRS_NAME:
                setSrsName((String)newValue);
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
            case Wfs20Package.TRANSACTION_TYPE__GROUP:
                getGroup().clear();
                return;
            case Wfs20Package.TRANSACTION_TYPE__ABSTRACT_TRANSACTION_ACTION_GROUP:
                getAbstractTransactionActionGroup().clear();
                return;
            case Wfs20Package.TRANSACTION_TYPE__LOCK_ID:
                setLockId(LOCK_ID_EDEFAULT);
                return;
            case Wfs20Package.TRANSACTION_TYPE__RELEASE_ACTION:
                unsetReleaseAction();
                return;
            case Wfs20Package.TRANSACTION_TYPE__SRS_NAME:
                setSrsName(SRS_NAME_EDEFAULT);
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
            case Wfs20Package.TRANSACTION_TYPE__GROUP:
                return group != null && !group.isEmpty();
            case Wfs20Package.TRANSACTION_TYPE__ABSTRACT_TRANSACTION_ACTION_GROUP:
                return !getAbstractTransactionActionGroup().isEmpty();
            case Wfs20Package.TRANSACTION_TYPE__ABSTRACT_TRANSACTION_ACTION:
                return !getAbstractTransactionAction().isEmpty();
            case Wfs20Package.TRANSACTION_TYPE__LOCK_ID:
                return LOCK_ID_EDEFAULT == null ? lockId != null : !LOCK_ID_EDEFAULT.equals(lockId);
            case Wfs20Package.TRANSACTION_TYPE__RELEASE_ACTION:
                return isSetReleaseAction();
            case Wfs20Package.TRANSACTION_TYPE__SRS_NAME:
                return SRS_NAME_EDEFAULT == null ? srsName != null : !SRS_NAME_EDEFAULT.equals(srsName);
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
        result.append(", lockId: ");
        result.append(lockId);
        result.append(", releaseAction: ");
        if (releaseActionESet) result.append(releaseAction); else result.append("<unset>");
        result.append(", srsName: ");
        result.append(srsName);
        result.append(')');
        return result.toString();
    }

} //TransactionTypeImpl
