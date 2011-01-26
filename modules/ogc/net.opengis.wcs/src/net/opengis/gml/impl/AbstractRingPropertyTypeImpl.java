/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml.impl;

import net.opengis.gml.AbstractRingPropertyType;
import net.opengis.gml.GmlPackage;

import net.opengis.gml.LinearRingType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Ring Property Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.gml.impl.AbstractRingPropertyTypeImpl#getLinearRing <em>Linear Ring</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AbstractRingPropertyTypeImpl extends EObjectImpl implements AbstractRingPropertyType {
    /**
	 * The cached value of the '{@link #getLinearRing() <em>Linear Ring</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getLinearRing()
	 * @generated
	 * @ordered
	 */
    protected LinearRingType linearRing;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected AbstractRingPropertyTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return GmlPackage.Literals.ABSTRACT_RING_PROPERTY_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public LinearRingType getLinearRing() {
		return linearRing;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetLinearRing(LinearRingType newLinearRing, NotificationChain msgs) {
		LinearRingType oldLinearRing = linearRing;
		linearRing = newLinearRing;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GmlPackage.ABSTRACT_RING_PROPERTY_TYPE__LINEAR_RING, oldLinearRing, newLinearRing);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLinearRing(LinearRingType newLinearRing) {
		if (newLinearRing != linearRing) {
			NotificationChain msgs = null;
			if (linearRing != null)
				msgs = ((InternalEObject)linearRing).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - GmlPackage.ABSTRACT_RING_PROPERTY_TYPE__LINEAR_RING, null, msgs);
			if (newLinearRing != null)
				msgs = ((InternalEObject)newLinearRing).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - GmlPackage.ABSTRACT_RING_PROPERTY_TYPE__LINEAR_RING, null, msgs);
			msgs = basicSetLinearRing(newLinearRing, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GmlPackage.ABSTRACT_RING_PROPERTY_TYPE__LINEAR_RING, newLinearRing, newLinearRing));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GmlPackage.ABSTRACT_RING_PROPERTY_TYPE__LINEAR_RING:
				return basicSetLinearRing(null, msgs);
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
			case GmlPackage.ABSTRACT_RING_PROPERTY_TYPE__LINEAR_RING:
				return getLinearRing();
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
			case GmlPackage.ABSTRACT_RING_PROPERTY_TYPE__LINEAR_RING:
				setLinearRing((LinearRingType)newValue);
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
			case GmlPackage.ABSTRACT_RING_PROPERTY_TYPE__LINEAR_RING:
				setLinearRing((LinearRingType)null);
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
			case GmlPackage.ABSTRACT_RING_PROPERTY_TYPE__LINEAR_RING:
				return linearRing != null;
		}
		return super.eIsSet(featureID);
	}

} //AbstractRingPropertyTypeImpl
