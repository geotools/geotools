/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import java.util.Collection;

import net.opengis.gml.CodeListType;

import net.opengis.wcs10.SupportedFormatsType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Supported Formats Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.SupportedFormatsTypeImpl#getFormats <em>Formats</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.SupportedFormatsTypeImpl#getNativeFormat <em>Native Format</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SupportedFormatsTypeImpl extends EObjectImpl implements SupportedFormatsType {
    /**
	 * The cached value of the '{@link #getFormats() <em>Formats</em>}' containment reference list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getFormats()
	 * @generated
	 * @ordered
	 */
    protected EList formats;

    /**
	 * The default value of the '{@link #getNativeFormat() <em>Native Format</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getNativeFormat()
	 * @generated
	 * @ordered
	 */
    protected static final String NATIVE_FORMAT_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getNativeFormat() <em>Native Format</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getNativeFormat()
	 * @generated
	 * @ordered
	 */
    protected String nativeFormat = NATIVE_FORMAT_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected SupportedFormatsTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.SUPPORTED_FORMATS_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getFormats() {
		if (formats == null) {
			formats = new EObjectContainmentEList(CodeListType.class, this, Wcs10Package.SUPPORTED_FORMATS_TYPE__FORMATS);
		}
		return formats;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getNativeFormat() {
		return nativeFormat;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setNativeFormat(String newNativeFormat) {
		String oldNativeFormat = nativeFormat;
		nativeFormat = newNativeFormat;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.SUPPORTED_FORMATS_TYPE__NATIVE_FORMAT, oldNativeFormat, nativeFormat));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.SUPPORTED_FORMATS_TYPE__FORMATS:
				return ((InternalEList)getFormats()).basicRemove(otherEnd, msgs);
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
			case Wcs10Package.SUPPORTED_FORMATS_TYPE__FORMATS:
				return getFormats();
			case Wcs10Package.SUPPORTED_FORMATS_TYPE__NATIVE_FORMAT:
				return getNativeFormat();
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
			case Wcs10Package.SUPPORTED_FORMATS_TYPE__FORMATS:
				getFormats().clear();
				getFormats().addAll((Collection)newValue);
				return;
			case Wcs10Package.SUPPORTED_FORMATS_TYPE__NATIVE_FORMAT:
				setNativeFormat((String)newValue);
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
			case Wcs10Package.SUPPORTED_FORMATS_TYPE__FORMATS:
				getFormats().clear();
				return;
			case Wcs10Package.SUPPORTED_FORMATS_TYPE__NATIVE_FORMAT:
				setNativeFormat(NATIVE_FORMAT_EDEFAULT);
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
			case Wcs10Package.SUPPORTED_FORMATS_TYPE__FORMATS:
				return formats != null && !formats.isEmpty();
			case Wcs10Package.SUPPORTED_FORMATS_TYPE__NATIVE_FORMAT:
				return NATIVE_FORMAT_EDEFAULT == null ? nativeFormat != null : !NATIVE_FORMAT_EDEFAULT.equals(nativeFormat);
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
		result.append(" (nativeFormat: ");
		result.append(nativeFormat);
		result.append(')');
		return result.toString();
	}

} //SupportedFormatsTypeImpl
