/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import net.opengis.gml.CodeType;

import net.opengis.wcs10.OutputType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Output Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.OutputTypeImpl#getCrs <em>Crs</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.OutputTypeImpl#getFormat <em>Format</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OutputTypeImpl extends EObjectImpl implements OutputType {
    /**
	 * The cached value of the '{@link #getCrs() <em>Crs</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getCrs()
	 * @generated
	 * @ordered
	 */
    protected CodeType crs;

    /**
	 * The cached value of the '{@link #getFormat() <em>Format</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getFormat()
	 * @generated
	 * @ordered
	 */
    protected CodeType format;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected OutputTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.OUTPUT_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public CodeType getCrs() {
		return crs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetCrs(CodeType newCrs, NotificationChain msgs) {
		CodeType oldCrs = crs;
		crs = newCrs;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.OUTPUT_TYPE__CRS, oldCrs, newCrs);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCrs(CodeType newCrs) {
		if (newCrs != crs) {
			NotificationChain msgs = null;
			if (crs != null)
				msgs = ((InternalEObject)crs).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.OUTPUT_TYPE__CRS, null, msgs);
			if (newCrs != null)
				msgs = ((InternalEObject)newCrs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.OUTPUT_TYPE__CRS, null, msgs);
			msgs = basicSetCrs(newCrs, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.OUTPUT_TYPE__CRS, newCrs, newCrs));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public CodeType getFormat() {
		return format;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetFormat(CodeType newFormat, NotificationChain msgs) {
		CodeType oldFormat = format;
		format = newFormat;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.OUTPUT_TYPE__FORMAT, oldFormat, newFormat);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFormat(CodeType newFormat) {
		if (newFormat != format) {
			NotificationChain msgs = null;
			if (format != null)
				msgs = ((InternalEObject)format).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.OUTPUT_TYPE__FORMAT, null, msgs);
			if (newFormat != null)
				msgs = ((InternalEObject)newFormat).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.OUTPUT_TYPE__FORMAT, null, msgs);
			msgs = basicSetFormat(newFormat, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.OUTPUT_TYPE__FORMAT, newFormat, newFormat));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.OUTPUT_TYPE__CRS:
				return basicSetCrs(null, msgs);
			case Wcs10Package.OUTPUT_TYPE__FORMAT:
				return basicSetFormat(null, msgs);
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
			case Wcs10Package.OUTPUT_TYPE__CRS:
				return getCrs();
			case Wcs10Package.OUTPUT_TYPE__FORMAT:
				return getFormat();
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
			case Wcs10Package.OUTPUT_TYPE__CRS:
				setCrs((CodeType)newValue);
				return;
			case Wcs10Package.OUTPUT_TYPE__FORMAT:
				setFormat((CodeType)newValue);
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
			case Wcs10Package.OUTPUT_TYPE__CRS:
				setCrs((CodeType)null);
				return;
			case Wcs10Package.OUTPUT_TYPE__FORMAT:
				setFormat((CodeType)null);
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
			case Wcs10Package.OUTPUT_TYPE__CRS:
				return crs != null;
			case Wcs10Package.OUTPUT_TYPE__FORMAT:
				return format != null;
		}
		return super.eIsSet(featureID);
	}

} //OutputTypeImpl
