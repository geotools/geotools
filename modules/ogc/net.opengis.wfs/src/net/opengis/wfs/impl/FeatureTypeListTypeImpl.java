/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Collection;

import net.opengis.wfs.FeatureTypeListType;
import net.opengis.wfs.FeatureTypeType;
import net.opengis.wfs.OperationsType;
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
 * An implementation of the model object '<em><b>Feature Type List Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.FeatureTypeListTypeImpl#getOperations <em>Operations</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.FeatureTypeListTypeImpl#getFeatureType <em>Feature Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FeatureTypeListTypeImpl extends EObjectImpl implements FeatureTypeListType {
	/**
     * The cached value of the '{@link #getOperations() <em>Operations</em>}' containment reference.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getOperations()
     * @generated
     * @ordered
     */
	protected OperationsType operations;

	/**
     * The cached value of the '{@link #getFeatureType() <em>Feature Type</em>}' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getFeatureType()
     * @generated
     * @ordered
     */
	protected EList featureType;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected FeatureTypeListTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.FEATURE_TYPE_LIST_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public OperationsType getOperations() {
        return operations;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetOperations(OperationsType newOperations, NotificationChain msgs) {
        OperationsType oldOperations = operations;
        operations = newOperations;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WfsPackage.FEATURE_TYPE_LIST_TYPE__OPERATIONS, oldOperations, newOperations);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setOperations(OperationsType newOperations) {
        if (newOperations != operations) {
            NotificationChain msgs = null;
            if (operations != null)
                msgs = ((InternalEObject)operations).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WfsPackage.FEATURE_TYPE_LIST_TYPE__OPERATIONS, null, msgs);
            if (newOperations != null)
                msgs = ((InternalEObject)newOperations).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WfsPackage.FEATURE_TYPE_LIST_TYPE__OPERATIONS, null, msgs);
            msgs = basicSetOperations(newOperations, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.FEATURE_TYPE_LIST_TYPE__OPERATIONS, newOperations, newOperations));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getFeatureType() {
        if (featureType == null) {
            featureType = new EObjectContainmentEList(FeatureTypeType.class, this, WfsPackage.FEATURE_TYPE_LIST_TYPE__FEATURE_TYPE);
        }
        return featureType;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsPackage.FEATURE_TYPE_LIST_TYPE__OPERATIONS:
                return basicSetOperations(null, msgs);
            case WfsPackage.FEATURE_TYPE_LIST_TYPE__FEATURE_TYPE:
                return ((InternalEList)getFeatureType()).basicRemove(otherEnd, msgs);
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
            case WfsPackage.FEATURE_TYPE_LIST_TYPE__OPERATIONS:
                return getOperations();
            case WfsPackage.FEATURE_TYPE_LIST_TYPE__FEATURE_TYPE:
                return getFeatureType();
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
            case WfsPackage.FEATURE_TYPE_LIST_TYPE__OPERATIONS:
                setOperations((OperationsType)newValue);
                return;
            case WfsPackage.FEATURE_TYPE_LIST_TYPE__FEATURE_TYPE:
                getFeatureType().clear();
                getFeatureType().addAll((Collection)newValue);
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
            case WfsPackage.FEATURE_TYPE_LIST_TYPE__OPERATIONS:
                setOperations((OperationsType)null);
                return;
            case WfsPackage.FEATURE_TYPE_LIST_TYPE__FEATURE_TYPE:
                getFeatureType().clear();
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
            case WfsPackage.FEATURE_TYPE_LIST_TYPE__OPERATIONS:
                return operations != null;
            case WfsPackage.FEATURE_TYPE_LIST_TYPE__FEATURE_TYPE:
                return featureType != null && !featureType.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //FeatureTypeListTypeImpl