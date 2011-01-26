/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import java.util.Collection;

import net.opengis.wcs10.TimeSequenceType;
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
 * An implementation of the model object '<em><b>Time Sequence Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.TimeSequenceTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.TimeSequenceTypeImpl#getTimePosition <em>Time Position</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.TimeSequenceTypeImpl#getTimePeriod <em>Time Period</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TimeSequenceTypeImpl extends EObjectImpl implements TimeSequenceType {
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
    protected TimeSequenceTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.TIME_SEQUENCE_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public FeatureMap getGroup() {
		if (group == null) {
			group = new BasicFeatureMap(this, Wcs10Package.TIME_SEQUENCE_TYPE__GROUP);
		}
		return group;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getTimePosition() {
		return getGroup().list(Wcs10Package.Literals.TIME_SEQUENCE_TYPE__TIME_POSITION);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getTimePeriod() {
		return getGroup().list(Wcs10Package.Literals.TIME_SEQUENCE_TYPE__TIME_PERIOD);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.TIME_SEQUENCE_TYPE__GROUP:
				return ((InternalEList)getGroup()).basicRemove(otherEnd, msgs);
			case Wcs10Package.TIME_SEQUENCE_TYPE__TIME_POSITION:
				return ((InternalEList)getTimePosition()).basicRemove(otherEnd, msgs);
			case Wcs10Package.TIME_SEQUENCE_TYPE__TIME_PERIOD:
				return ((InternalEList)getTimePeriod()).basicRemove(otherEnd, msgs);
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
			case Wcs10Package.TIME_SEQUENCE_TYPE__GROUP:
				if (coreType) return getGroup();
				return ((FeatureMap.Internal)getGroup()).getWrapper();
			case Wcs10Package.TIME_SEQUENCE_TYPE__TIME_POSITION:
				return getTimePosition();
			case Wcs10Package.TIME_SEQUENCE_TYPE__TIME_PERIOD:
				return getTimePeriod();
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
			case Wcs10Package.TIME_SEQUENCE_TYPE__GROUP:
				((FeatureMap.Internal)getGroup()).set(newValue);
				return;
			case Wcs10Package.TIME_SEQUENCE_TYPE__TIME_POSITION:
				getTimePosition().clear();
				getTimePosition().addAll((Collection)newValue);
				return;
			case Wcs10Package.TIME_SEQUENCE_TYPE__TIME_PERIOD:
				getTimePeriod().clear();
				getTimePeriod().addAll((Collection)newValue);
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
			case Wcs10Package.TIME_SEQUENCE_TYPE__GROUP:
				getGroup().clear();
				return;
			case Wcs10Package.TIME_SEQUENCE_TYPE__TIME_POSITION:
				getTimePosition().clear();
				return;
			case Wcs10Package.TIME_SEQUENCE_TYPE__TIME_PERIOD:
				getTimePeriod().clear();
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
			case Wcs10Package.TIME_SEQUENCE_TYPE__GROUP:
				return group != null && !group.isEmpty();
			case Wcs10Package.TIME_SEQUENCE_TYPE__TIME_POSITION:
				return !getTimePosition().isEmpty();
			case Wcs10Package.TIME_SEQUENCE_TYPE__TIME_PERIOD:
				return !getTimePeriod().isEmpty();
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

} //TimeSequenceTypeImpl
