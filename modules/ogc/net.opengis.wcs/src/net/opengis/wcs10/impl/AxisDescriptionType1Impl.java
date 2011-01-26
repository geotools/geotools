/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import net.opengis.wcs10.AxisDescriptionType;
import net.opengis.wcs10.AxisDescriptionType1;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Axis Description Type1</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.AxisDescriptionType1Impl#getAxisDescription <em>Axis Description</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AxisDescriptionType1Impl extends EObjectImpl implements AxisDescriptionType1 {
    /**
	 * The cached value of the '{@link #getAxisDescription() <em>Axis Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getAxisDescription()
	 * @generated
	 * @ordered
	 */
    protected AxisDescriptionType axisDescription;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected AxisDescriptionType1Impl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.AXIS_DESCRIPTION_TYPE1;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public AxisDescriptionType getAxisDescription() {
		return axisDescription;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetAxisDescription(AxisDescriptionType newAxisDescription, NotificationChain msgs) {
		AxisDescriptionType oldAxisDescription = axisDescription;
		axisDescription = newAxisDescription;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.AXIS_DESCRIPTION_TYPE1__AXIS_DESCRIPTION, oldAxisDescription, newAxisDescription);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAxisDescription(AxisDescriptionType newAxisDescription) {
		if (newAxisDescription != axisDescription) {
			NotificationChain msgs = null;
			if (axisDescription != null)
				msgs = ((InternalEObject)axisDescription).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.AXIS_DESCRIPTION_TYPE1__AXIS_DESCRIPTION, null, msgs);
			if (newAxisDescription != null)
				msgs = ((InternalEObject)newAxisDescription).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.AXIS_DESCRIPTION_TYPE1__AXIS_DESCRIPTION, null, msgs);
			msgs = basicSetAxisDescription(newAxisDescription, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.AXIS_DESCRIPTION_TYPE1__AXIS_DESCRIPTION, newAxisDescription, newAxisDescription));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.AXIS_DESCRIPTION_TYPE1__AXIS_DESCRIPTION:
				return basicSetAxisDescription(null, msgs);
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
			case Wcs10Package.AXIS_DESCRIPTION_TYPE1__AXIS_DESCRIPTION:
				return getAxisDescription();
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
			case Wcs10Package.AXIS_DESCRIPTION_TYPE1__AXIS_DESCRIPTION:
				setAxisDescription((AxisDescriptionType)newValue);
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
			case Wcs10Package.AXIS_DESCRIPTION_TYPE1__AXIS_DESCRIPTION:
				setAxisDescription((AxisDescriptionType)null);
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
			case Wcs10Package.AXIS_DESCRIPTION_TYPE1__AXIS_DESCRIPTION:
				return axisDescription != null;
		}
		return super.eIsSet(featureID);
	}

} //AxisDescriptionType1Impl
