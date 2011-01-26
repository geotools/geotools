/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import java.util.Collection;

import net.opengis.wcs10.AxisSubsetType;
import net.opengis.wcs10.RangeSubsetType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Range Subset Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.RangeSubsetTypeImpl#getAxisSubset <em>Axis Subset</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RangeSubsetTypeImpl extends EObjectImpl implements RangeSubsetType {
    /**
	 * The cached value of the '{@link #getAxisSubset() <em>Axis Subset</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getAxisSubset()
	 * @generated
	 * @ordered
	 */
    protected EList axisSubset;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected RangeSubsetTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.RANGE_SUBSET_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getAxisSubset() {
		if (axisSubset == null) {
			axisSubset = new EObjectContainmentEList(AxisSubsetType.class, this, Wcs10Package.RANGE_SUBSET_TYPE__AXIS_SUBSET);
		}
		return axisSubset;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.RANGE_SUBSET_TYPE__AXIS_SUBSET:
				return ((InternalEList)getAxisSubset()).basicRemove(otherEnd, msgs);
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
			case Wcs10Package.RANGE_SUBSET_TYPE__AXIS_SUBSET:
				return getAxisSubset();
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
			case Wcs10Package.RANGE_SUBSET_TYPE__AXIS_SUBSET:
				getAxisSubset().clear();
				getAxisSubset().addAll((Collection)newValue);
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
			case Wcs10Package.RANGE_SUBSET_TYPE__AXIS_SUBSET:
				getAxisSubset().clear();
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
			case Wcs10Package.RANGE_SUBSET_TYPE__AXIS_SUBSET:
				return axisSubset != null && !axisSubset.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //RangeSubsetTypeImpl
