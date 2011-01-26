/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import net.opengis.wcs10.DCPTypeType;
import net.opengis.wcs10.HTTPType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DCP Type Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.DCPTypeTypeImpl#getHTTP <em>HTTP</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DCPTypeTypeImpl extends EObjectImpl implements DCPTypeType {
    /**
	 * The cached value of the '{@link #getHTTP() <em>HTTP</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getHTTP()
	 * @generated
	 * @ordered
	 */
    protected HTTPType hTTP;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected DCPTypeTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.DCP_TYPE_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public HTTPType getHTTP() {
		return hTTP;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetHTTP(HTTPType newHTTP, NotificationChain msgs) {
		HTTPType oldHTTP = hTTP;
		hTTP = newHTTP;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.DCP_TYPE_TYPE__HTTP, oldHTTP, newHTTP);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setHTTP(HTTPType newHTTP) {
		if (newHTTP != hTTP) {
			NotificationChain msgs = null;
			if (hTTP != null)
				msgs = ((InternalEObject)hTTP).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.DCP_TYPE_TYPE__HTTP, null, msgs);
			if (newHTTP != null)
				msgs = ((InternalEObject)newHTTP).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.DCP_TYPE_TYPE__HTTP, null, msgs);
			msgs = basicSetHTTP(newHTTP, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.DCP_TYPE_TYPE__HTTP, newHTTP, newHTTP));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.DCP_TYPE_TYPE__HTTP:
				return basicSetHTTP(null, msgs);
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
			case Wcs10Package.DCP_TYPE_TYPE__HTTP:
				return getHTTP();
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
			case Wcs10Package.DCP_TYPE_TYPE__HTTP:
				setHTTP((HTTPType)newValue);
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
			case Wcs10Package.DCP_TYPE_TYPE__HTTP:
				setHTTP((HTTPType)null);
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
			case Wcs10Package.DCP_TYPE_TYPE__HTTP:
				return hTTP != null;
		}
		return super.eIsSet(featureID);
	}

} //DCPTypeTypeImpl
