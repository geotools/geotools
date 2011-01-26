/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import java.util.Collection;

import net.opengis.wcs10.ValueEnumBaseType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Value Enum Base Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.ValueEnumBaseTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ValueEnumBaseTypeImpl#getInterval <em>Interval</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.ValueEnumBaseTypeImpl#getSingleValue <em>Single Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ValueEnumBaseTypeImpl extends EObjectImpl implements ValueEnumBaseType {
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
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected ValueEnumBaseTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.VALUE_ENUM_BASE_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public FeatureMap getGroup() {
		if (group == null) {
			group = new BasicFeatureMap(this, Wcs10Package.VALUE_ENUM_BASE_TYPE__GROUP);
		}
		return group;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getInterval() {
		return getGroup().list(Wcs10Package.Literals.VALUE_ENUM_BASE_TYPE__INTERVAL);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getSingleValue() {
		return getGroup().list(Wcs10Package.Literals.VALUE_ENUM_BASE_TYPE__SINGLE_VALUE);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.VALUE_ENUM_BASE_TYPE__GROUP:
				return ((InternalEList)getGroup()).basicRemove(otherEnd, msgs);
			case Wcs10Package.VALUE_ENUM_BASE_TYPE__INTERVAL:
				return ((InternalEList)getInterval()).basicRemove(otherEnd, msgs);
			case Wcs10Package.VALUE_ENUM_BASE_TYPE__SINGLE_VALUE:
				return ((InternalEList)getSingleValue()).basicRemove(otherEnd, msgs);
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
			case Wcs10Package.VALUE_ENUM_BASE_TYPE__GROUP:
				if (coreType) return getGroup();
				return ((FeatureMap.Internal)getGroup()).getWrapper();
			case Wcs10Package.VALUE_ENUM_BASE_TYPE__INTERVAL:
				return getInterval();
			case Wcs10Package.VALUE_ENUM_BASE_TYPE__SINGLE_VALUE:
				return getSingleValue();
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
			case Wcs10Package.VALUE_ENUM_BASE_TYPE__GROUP:
				((FeatureMap.Internal)getGroup()).set(newValue);
				return;
			case Wcs10Package.VALUE_ENUM_BASE_TYPE__INTERVAL:
				getInterval().clear();
				getInterval().addAll((Collection)newValue);
				return;
			case Wcs10Package.VALUE_ENUM_BASE_TYPE__SINGLE_VALUE:
				getSingleValue().clear();
				getSingleValue().addAll((Collection)newValue);
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
			case Wcs10Package.VALUE_ENUM_BASE_TYPE__GROUP:
				getGroup().clear();
				return;
			case Wcs10Package.VALUE_ENUM_BASE_TYPE__INTERVAL:
				getInterval().clear();
				return;
			case Wcs10Package.VALUE_ENUM_BASE_TYPE__SINGLE_VALUE:
				getSingleValue().clear();
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
			case Wcs10Package.VALUE_ENUM_BASE_TYPE__GROUP:
				return group != null && !group.isEmpty();
			case Wcs10Package.VALUE_ENUM_BASE_TYPE__INTERVAL:
				return !getInterval().isEmpty();
			case Wcs10Package.VALUE_ENUM_BASE_TYPE__SINGLE_VALUE:
				return !getSingleValue().isEmpty();
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
		result.append(" (group: ");
		result.append(group);
		result.append(')');
		return result.toString();
	}

} //ValueEnumBaseTypeImpl
