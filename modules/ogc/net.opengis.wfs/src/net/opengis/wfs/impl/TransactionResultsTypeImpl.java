/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Collection;

import net.opengis.wfs.ActionType;
import net.opengis.wfs.TransactionResultsType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transaction Results Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.TransactionResultsTypeImpl#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.TransactionResultsTypeImpl#getAction <em>Action</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TransactionResultsTypeImpl extends EObjectImpl implements TransactionResultsType {
	/**
     * The default value of the '{@link #getHandle() <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getHandle()
     * @generated
     * @ordered
     */
	protected static final String HANDLE_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getHandle() <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getHandle()
     * @generated
     * @ordered
     */
	protected String handle = HANDLE_EDEFAULT;

	/**
     * The cached value of the '{@link #getAction() <em>Action</em>}' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getAction()
     * @generated
     * @ordered
     */
	protected EList action;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected TransactionResultsTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.TRANSACTION_RESULTS_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getHandle() {
        return handle;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setHandle(String newHandle) {
        String oldHandle = handle;
        handle = newHandle;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.TRANSACTION_RESULTS_TYPE__HANDLE, oldHandle, handle));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getAction() {
        if (action == null) {
            action = new EObjectContainmentEList(ActionType.class, this, WfsPackage.TRANSACTION_RESULTS_TYPE__ACTION);
        }
        return action;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsPackage.TRANSACTION_RESULTS_TYPE__ACTION:
                return ((InternalEList)getAction()).basicRemove(otherEnd, msgs);
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
            case WfsPackage.TRANSACTION_RESULTS_TYPE__HANDLE:
                return getHandle();
            case WfsPackage.TRANSACTION_RESULTS_TYPE__ACTION:
                return getAction();
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
            case WfsPackage.TRANSACTION_RESULTS_TYPE__HANDLE:
                setHandle((String)newValue);
                return;
            case WfsPackage.TRANSACTION_RESULTS_TYPE__ACTION:
                getAction().clear();
                getAction().addAll((Collection)newValue);
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
            case WfsPackage.TRANSACTION_RESULTS_TYPE__HANDLE:
                setHandle(HANDLE_EDEFAULT);
                return;
            case WfsPackage.TRANSACTION_RESULTS_TYPE__ACTION:
                getAction().clear();
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
            case WfsPackage.TRANSACTION_RESULTS_TYPE__HANDLE:
                return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT.equals(handle);
            case WfsPackage.TRANSACTION_RESULTS_TYPE__ACTION:
                return action != null && !action.isEmpty();
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
        result.append(" (handle: ");
        result.append(handle);
        result.append(')');
        return result.toString();
    }

} //TransactionResultsTypeImpl