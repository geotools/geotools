/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import java.util.Collection;

import net.opengis.gml.TimePositionType;

import net.opengis.wcs10.LonLatEnvelopeType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Lon Lat Envelope Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.LonLatEnvelopeTypeImpl#getTimePosition <em>Time Position</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LonLatEnvelopeTypeImpl extends LonLatEnvelopeBaseTypeImpl implements LonLatEnvelopeType {
    /**
	 * The cached value of the '{@link #getTimePosition() <em>Time Position</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getTimePosition()
	 * @generated
	 * @ordered
	 */
    protected EList timePosition;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected LonLatEnvelopeTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.LON_LAT_ENVELOPE_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getTimePosition() {
		if (timePosition == null) {
			timePosition = new EObjectContainmentEList(TimePositionType.class, this, Wcs10Package.LON_LAT_ENVELOPE_TYPE__TIME_POSITION);
		}
		return timePosition;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.LON_LAT_ENVELOPE_TYPE__TIME_POSITION:
				return ((InternalEList)getTimePosition()).basicRemove(otherEnd, msgs);
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
			case Wcs10Package.LON_LAT_ENVELOPE_TYPE__TIME_POSITION:
				return getTimePosition();
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
			case Wcs10Package.LON_LAT_ENVELOPE_TYPE__TIME_POSITION:
				getTimePosition().clear();
				getTimePosition().addAll((Collection)newValue);
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
			case Wcs10Package.LON_LAT_ENVELOPE_TYPE__TIME_POSITION:
				getTimePosition().clear();
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
			case Wcs10Package.LON_LAT_ENVELOPE_TYPE__TIME_POSITION:
				return timePosition != null && !timePosition.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //LonLatEnvelopeTypeImpl
