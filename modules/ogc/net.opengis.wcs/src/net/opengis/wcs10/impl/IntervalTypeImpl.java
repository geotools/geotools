/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import net.opengis.wcs10.IntervalType;
import net.opengis.wcs10.TypedLiteralType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Interval Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.IntervalTypeImpl#getRes <em>Res</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IntervalTypeImpl extends ValueRangeTypeImpl implements IntervalType {
    /**
	 * The cached value of the '{@link #getRes() <em>Res</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRes()
	 * @generated
	 * @ordered
	 */
    protected TypedLiteralType res;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected IntervalTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.INTERVAL_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public TypedLiteralType getRes() {
		return res;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetRes(TypedLiteralType newRes, NotificationChain msgs) {
		TypedLiteralType oldRes = res;
		res = newRes;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.INTERVAL_TYPE__RES, oldRes, newRes);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRes(TypedLiteralType newRes) {
		if (newRes != res) {
			NotificationChain msgs = null;
			if (res != null)
				msgs = ((InternalEObject)res).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.INTERVAL_TYPE__RES, null, msgs);
			if (newRes != null)
				msgs = ((InternalEObject)newRes).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.INTERVAL_TYPE__RES, null, msgs);
			msgs = basicSetRes(newRes, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.INTERVAL_TYPE__RES, newRes, newRes));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.INTERVAL_TYPE__RES:
				return basicSetRes(null, msgs);
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
			case Wcs10Package.INTERVAL_TYPE__RES:
				return getRes();
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
			case Wcs10Package.INTERVAL_TYPE__RES:
				setRes((TypedLiteralType)newValue);
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
			case Wcs10Package.INTERVAL_TYPE__RES:
				setRes((TypedLiteralType)null);
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
			case Wcs10Package.INTERVAL_TYPE__RES:
				return res != null;
		}
		return super.eIsSet(featureID);
	}

} //IntervalTypeImpl
