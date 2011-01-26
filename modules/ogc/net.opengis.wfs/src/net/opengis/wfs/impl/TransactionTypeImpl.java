/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Collection;

import net.opengis.wfs.AllSomeType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.WfsPackage;

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
 *   <li>{@link net.opengis.wfs.impl.TransactionTypeImpl#getLockId <em>Lock Id</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.TransactionTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.TransactionTypeImpl#getInsert <em>Insert</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.TransactionTypeImpl#getUpdate <em>Update</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.TransactionTypeImpl#getDelete <em>Delete</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.TransactionTypeImpl#getNative <em>Native</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.TransactionTypeImpl#getReleaseAction <em>Release Action</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TransactionTypeImpl extends BaseRequestTypeImpl implements TransactionType {
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
     * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getGroup()
     * @generated
     * @ordered
     */
	protected FeatureMap group;

	/**
     * The default value of the '{@link #getReleaseAction() <em>Release Action</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getReleaseAction()
     * @generated
     * @ordered
     */
	protected static final AllSomeType RELEASE_ACTION_EDEFAULT = AllSomeType.ALL_LITERAL;

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
	protected EClass eStaticClass() {
        return WfsPackage.Literals.TRANSACTION_TYPE;
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.TRANSACTION_TYPE__LOCK_ID, oldLockId, lockId));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public FeatureMap getGroup() {
        if (group == null) {
            group = new BasicFeatureMap(this, WfsPackage.TRANSACTION_TYPE__GROUP);
        }
        return group;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getInsert() {
        return getGroup().list(WfsPackage.Literals.TRANSACTION_TYPE__INSERT);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getUpdate() {
        return getGroup().list(WfsPackage.Literals.TRANSACTION_TYPE__UPDATE);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getDelete() {
        return getGroup().list(WfsPackage.Literals.TRANSACTION_TYPE__DELETE);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getNative() {
        return getGroup().list(WfsPackage.Literals.TRANSACTION_TYPE__NATIVE);
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.TRANSACTION_TYPE__RELEASE_ACTION, oldReleaseAction, releaseAction, !oldReleaseActionESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, WfsPackage.TRANSACTION_TYPE__RELEASE_ACTION, oldReleaseAction, RELEASE_ACTION_EDEFAULT, oldReleaseActionESet));
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsPackage.TRANSACTION_TYPE__GROUP:
                return ((InternalEList)getGroup()).basicRemove(otherEnd, msgs);
            case WfsPackage.TRANSACTION_TYPE__INSERT:
                return ((InternalEList)getInsert()).basicRemove(otherEnd, msgs);
            case WfsPackage.TRANSACTION_TYPE__UPDATE:
                return ((InternalEList)getUpdate()).basicRemove(otherEnd, msgs);
            case WfsPackage.TRANSACTION_TYPE__DELETE:
                return ((InternalEList)getDelete()).basicRemove(otherEnd, msgs);
            case WfsPackage.TRANSACTION_TYPE__NATIVE:
                return ((InternalEList)getNative()).basicRemove(otherEnd, msgs);
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
            case WfsPackage.TRANSACTION_TYPE__LOCK_ID:
                return getLockId();
            case WfsPackage.TRANSACTION_TYPE__GROUP:
                if (coreType) return getGroup();
                return ((FeatureMap.Internal)getGroup()).getWrapper();
            case WfsPackage.TRANSACTION_TYPE__INSERT:
                return getInsert();
            case WfsPackage.TRANSACTION_TYPE__UPDATE:
                return getUpdate();
            case WfsPackage.TRANSACTION_TYPE__DELETE:
                return getDelete();
            case WfsPackage.TRANSACTION_TYPE__NATIVE:
                return getNative();
            case WfsPackage.TRANSACTION_TYPE__RELEASE_ACTION:
                return getReleaseAction();
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
            case WfsPackage.TRANSACTION_TYPE__LOCK_ID:
                setLockId((String)newValue);
                return;
            case WfsPackage.TRANSACTION_TYPE__GROUP:
                ((FeatureMap.Internal)getGroup()).set(newValue);
                return;
            case WfsPackage.TRANSACTION_TYPE__INSERT:
                getInsert().clear();
                getInsert().addAll((Collection)newValue);
                return;
            case WfsPackage.TRANSACTION_TYPE__UPDATE:
                getUpdate().clear();
                getUpdate().addAll((Collection)newValue);
                return;
            case WfsPackage.TRANSACTION_TYPE__DELETE:
                getDelete().clear();
                getDelete().addAll((Collection)newValue);
                return;
            case WfsPackage.TRANSACTION_TYPE__NATIVE:
                getNative().clear();
                getNative().addAll((Collection)newValue);
                return;
            case WfsPackage.TRANSACTION_TYPE__RELEASE_ACTION:
                setReleaseAction((AllSomeType)newValue);
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
            case WfsPackage.TRANSACTION_TYPE__LOCK_ID:
                setLockId(LOCK_ID_EDEFAULT);
                return;
            case WfsPackage.TRANSACTION_TYPE__GROUP:
                getGroup().clear();
                return;
            case WfsPackage.TRANSACTION_TYPE__INSERT:
                getInsert().clear();
                return;
            case WfsPackage.TRANSACTION_TYPE__UPDATE:
                getUpdate().clear();
                return;
            case WfsPackage.TRANSACTION_TYPE__DELETE:
                getDelete().clear();
                return;
            case WfsPackage.TRANSACTION_TYPE__NATIVE:
                getNative().clear();
                return;
            case WfsPackage.TRANSACTION_TYPE__RELEASE_ACTION:
                unsetReleaseAction();
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
            case WfsPackage.TRANSACTION_TYPE__LOCK_ID:
                return LOCK_ID_EDEFAULT == null ? lockId != null : !LOCK_ID_EDEFAULT.equals(lockId);
            case WfsPackage.TRANSACTION_TYPE__GROUP:
                return group != null && !group.isEmpty();
            case WfsPackage.TRANSACTION_TYPE__INSERT:
                return !getInsert().isEmpty();
            case WfsPackage.TRANSACTION_TYPE__UPDATE:
                return !getUpdate().isEmpty();
            case WfsPackage.TRANSACTION_TYPE__DELETE:
                return !getDelete().isEmpty();
            case WfsPackage.TRANSACTION_TYPE__NATIVE:
                return !getNative().isEmpty();
            case WfsPackage.TRANSACTION_TYPE__RELEASE_ACTION:
                return isSetReleaseAction();
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
        result.append(", group: ");
        result.append(group);
        result.append(", releaseAction: ");
        if (releaseActionESet) result.append(releaseAction); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //TransactionTypeImpl