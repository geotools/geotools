/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import net.opengis.wcs10.RangeSetType;
import net.opengis.wcs10.RangeSetType1;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Range Set Type1</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.RangeSetType1Impl#getRangeSet <em>Range Set</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RangeSetType1Impl extends EObjectImpl implements RangeSetType1 {
    /**
	 * The cached value of the '{@link #getRangeSet() <em>Range Set</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRangeSet()
	 * @generated
	 * @ordered
	 */
    protected RangeSetType rangeSet;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected RangeSetType1Impl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.RANGE_SET_TYPE1;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public RangeSetType getRangeSet() {
		return rangeSet;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetRangeSet(RangeSetType newRangeSet, NotificationChain msgs) {
		RangeSetType oldRangeSet = rangeSet;
		rangeSet = newRangeSet;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.RANGE_SET_TYPE1__RANGE_SET, oldRangeSet, newRangeSet);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRangeSet(RangeSetType newRangeSet) {
		if (newRangeSet != rangeSet) {
			NotificationChain msgs = null;
			if (rangeSet != null)
				msgs = ((InternalEObject)rangeSet).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.RANGE_SET_TYPE1__RANGE_SET, null, msgs);
			if (newRangeSet != null)
				msgs = ((InternalEObject)newRangeSet).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.RANGE_SET_TYPE1__RANGE_SET, null, msgs);
			msgs = basicSetRangeSet(newRangeSet, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.RANGE_SET_TYPE1__RANGE_SET, newRangeSet, newRangeSet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.RANGE_SET_TYPE1__RANGE_SET:
				return basicSetRangeSet(null, msgs);
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
			case Wcs10Package.RANGE_SET_TYPE1__RANGE_SET:
				return getRangeSet();
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
			case Wcs10Package.RANGE_SET_TYPE1__RANGE_SET:
				setRangeSet((RangeSetType)newValue);
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
			case Wcs10Package.RANGE_SET_TYPE1__RANGE_SET:
				setRangeSet((RangeSetType)null);
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
			case Wcs10Package.RANGE_SET_TYPE1__RANGE_SET:
				return rangeSet != null;
		}
		return super.eIsSet(featureID);
	}

} //RangeSetType1Impl
