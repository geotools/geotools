/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.util.Collection;

import net.opengis.wfs.InsertResultsType;
import net.opengis.wfs.InsertedFeatureType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Insert Results Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.InsertResultsTypeImpl#getFeature <em>Feature</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InsertResultsTypeImpl extends EObjectImpl implements InsertResultsType {
	/**
     * The cached value of the '{@link #getFeature() <em>Feature</em>}' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getFeature()
     * @generated
     * @ordered
     */
	protected EList feature;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected InsertResultsTypeImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.INSERT_RESULTS_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getFeature() {
        if (feature == null) {
            feature = new EObjectContainmentEList(InsertedFeatureType.class, this, WfsPackage.INSERT_RESULTS_TYPE__FEATURE);
        }
        return feature;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsPackage.INSERT_RESULTS_TYPE__FEATURE:
                return ((InternalEList)getFeature()).basicRemove(otherEnd, msgs);
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
            case WfsPackage.INSERT_RESULTS_TYPE__FEATURE:
                return getFeature();
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
            case WfsPackage.INSERT_RESULTS_TYPE__FEATURE:
                getFeature().clear();
                getFeature().addAll((Collection)newValue);
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
            case WfsPackage.INSERT_RESULTS_TYPE__FEATURE:
                getFeature().clear();
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
            case WfsPackage.INSERT_RESULTS_TYPE__FEATURE:
                return feature != null && !feature.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //InsertResultsTypeImpl