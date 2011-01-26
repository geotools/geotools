/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import net.opengis.wcs10.ExceptionType;
import net.opengis.wcs10.RequestType;
import net.opengis.wcs10.VendorSpecificCapabilitiesType;
import net.opengis.wcs10.WCSCapabilityType;
import net.opengis.wcs10.Wcs10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>WCS Capability Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.WCSCapabilityTypeImpl#getRequest <em>Request</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.WCSCapabilityTypeImpl#getException <em>Exception</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.WCSCapabilityTypeImpl#getVendorSpecificCapabilities <em>Vendor Specific Capabilities</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.WCSCapabilityTypeImpl#getUpdateSequence <em>Update Sequence</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.WCSCapabilityTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WCSCapabilityTypeImpl extends EObjectImpl implements WCSCapabilityType {
    /**
	 * The cached value of the '{@link #getRequest() <em>Request</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getRequest()
	 * @generated
	 * @ordered
	 */
    protected RequestType request;

    /**
	 * The cached value of the '{@link #getException() <em>Exception</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getException()
	 * @generated
	 * @ordered
	 */
    protected ExceptionType exception;

    /**
	 * The cached value of the '{@link #getVendorSpecificCapabilities() <em>Vendor Specific Capabilities</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getVendorSpecificCapabilities()
	 * @generated
	 * @ordered
	 */
    protected VendorSpecificCapabilitiesType vendorSpecificCapabilities;

    /**
	 * The default value of the '{@link #getUpdateSequence() <em>Update Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getUpdateSequence()
	 * @generated
	 * @ordered
	 */
    protected static final String UPDATE_SEQUENCE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getUpdateSequence() <em>Update Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getUpdateSequence()
	 * @generated
	 * @ordered
	 */
    protected String updateSequence = UPDATE_SEQUENCE_EDEFAULT;

    /**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
    protected static final String VERSION_EDEFAULT = "1.0.0";

    /**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
    protected String version = VERSION_EDEFAULT;

    /**
	 * This is true if the Version attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean versionESet;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected WCSCapabilityTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.WCS_CAPABILITY_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public RequestType getRequest() {
		return request;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetRequest(RequestType newRequest, NotificationChain msgs) {
		RequestType oldRequest = request;
		request = newRequest;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.WCS_CAPABILITY_TYPE__REQUEST, oldRequest, newRequest);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRequest(RequestType newRequest) {
		if (newRequest != request) {
			NotificationChain msgs = null;
			if (request != null)
				msgs = ((InternalEObject)request).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.WCS_CAPABILITY_TYPE__REQUEST, null, msgs);
			if (newRequest != null)
				msgs = ((InternalEObject)newRequest).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.WCS_CAPABILITY_TYPE__REQUEST, null, msgs);
			msgs = basicSetRequest(newRequest, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.WCS_CAPABILITY_TYPE__REQUEST, newRequest, newRequest));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ExceptionType getException() {
		return exception;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetException(ExceptionType newException, NotificationChain msgs) {
		ExceptionType oldException = exception;
		exception = newException;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.WCS_CAPABILITY_TYPE__EXCEPTION, oldException, newException);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setException(ExceptionType newException) {
		if (newException != exception) {
			NotificationChain msgs = null;
			if (exception != null)
				msgs = ((InternalEObject)exception).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.WCS_CAPABILITY_TYPE__EXCEPTION, null, msgs);
			if (newException != null)
				msgs = ((InternalEObject)newException).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.WCS_CAPABILITY_TYPE__EXCEPTION, null, msgs);
			msgs = basicSetException(newException, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.WCS_CAPABILITY_TYPE__EXCEPTION, newException, newException));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public VendorSpecificCapabilitiesType getVendorSpecificCapabilities() {
		return vendorSpecificCapabilities;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetVendorSpecificCapabilities(VendorSpecificCapabilitiesType newVendorSpecificCapabilities, NotificationChain msgs) {
		VendorSpecificCapabilitiesType oldVendorSpecificCapabilities = vendorSpecificCapabilities;
		vendorSpecificCapabilities = newVendorSpecificCapabilities;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.WCS_CAPABILITY_TYPE__VENDOR_SPECIFIC_CAPABILITIES, oldVendorSpecificCapabilities, newVendorSpecificCapabilities);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setVendorSpecificCapabilities(VendorSpecificCapabilitiesType newVendorSpecificCapabilities) {
		if (newVendorSpecificCapabilities != vendorSpecificCapabilities) {
			NotificationChain msgs = null;
			if (vendorSpecificCapabilities != null)
				msgs = ((InternalEObject)vendorSpecificCapabilities).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.WCS_CAPABILITY_TYPE__VENDOR_SPECIFIC_CAPABILITIES, null, msgs);
			if (newVendorSpecificCapabilities != null)
				msgs = ((InternalEObject)newVendorSpecificCapabilities).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.WCS_CAPABILITY_TYPE__VENDOR_SPECIFIC_CAPABILITIES, null, msgs);
			msgs = basicSetVendorSpecificCapabilities(newVendorSpecificCapabilities, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.WCS_CAPABILITY_TYPE__VENDOR_SPECIFIC_CAPABILITIES, newVendorSpecificCapabilities, newVendorSpecificCapabilities));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getUpdateSequence() {
		return updateSequence;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setUpdateSequence(String newUpdateSequence) {
		String oldUpdateSequence = updateSequence;
		updateSequence = newUpdateSequence;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.WCS_CAPABILITY_TYPE__UPDATE_SEQUENCE, oldUpdateSequence, updateSequence));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getVersion() {
		return version;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		boolean oldVersionESet = versionESet;
		versionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.WCS_CAPABILITY_TYPE__VERSION, oldVersion, version, !oldVersionESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetVersion() {
		String oldVersion = version;
		boolean oldVersionESet = versionESet;
		version = VERSION_EDEFAULT;
		versionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs10Package.WCS_CAPABILITY_TYPE__VERSION, oldVersion, VERSION_EDEFAULT, oldVersionESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetVersion() {
		return versionESet;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wcs10Package.WCS_CAPABILITY_TYPE__REQUEST:
				return basicSetRequest(null, msgs);
			case Wcs10Package.WCS_CAPABILITY_TYPE__EXCEPTION:
				return basicSetException(null, msgs);
			case Wcs10Package.WCS_CAPABILITY_TYPE__VENDOR_SPECIFIC_CAPABILITIES:
				return basicSetVendorSpecificCapabilities(null, msgs);
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
			case Wcs10Package.WCS_CAPABILITY_TYPE__REQUEST:
				return getRequest();
			case Wcs10Package.WCS_CAPABILITY_TYPE__EXCEPTION:
				return getException();
			case Wcs10Package.WCS_CAPABILITY_TYPE__VENDOR_SPECIFIC_CAPABILITIES:
				return getVendorSpecificCapabilities();
			case Wcs10Package.WCS_CAPABILITY_TYPE__UPDATE_SEQUENCE:
				return getUpdateSequence();
			case Wcs10Package.WCS_CAPABILITY_TYPE__VERSION:
				return getVersion();
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
			case Wcs10Package.WCS_CAPABILITY_TYPE__REQUEST:
				setRequest((RequestType)newValue);
				return;
			case Wcs10Package.WCS_CAPABILITY_TYPE__EXCEPTION:
				setException((ExceptionType)newValue);
				return;
			case Wcs10Package.WCS_CAPABILITY_TYPE__VENDOR_SPECIFIC_CAPABILITIES:
				setVendorSpecificCapabilities((VendorSpecificCapabilitiesType)newValue);
				return;
			case Wcs10Package.WCS_CAPABILITY_TYPE__UPDATE_SEQUENCE:
				setUpdateSequence((String)newValue);
				return;
			case Wcs10Package.WCS_CAPABILITY_TYPE__VERSION:
				setVersion((String)newValue);
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
			case Wcs10Package.WCS_CAPABILITY_TYPE__REQUEST:
				setRequest((RequestType)null);
				return;
			case Wcs10Package.WCS_CAPABILITY_TYPE__EXCEPTION:
				setException((ExceptionType)null);
				return;
			case Wcs10Package.WCS_CAPABILITY_TYPE__VENDOR_SPECIFIC_CAPABILITIES:
				setVendorSpecificCapabilities((VendorSpecificCapabilitiesType)null);
				return;
			case Wcs10Package.WCS_CAPABILITY_TYPE__UPDATE_SEQUENCE:
				setUpdateSequence(UPDATE_SEQUENCE_EDEFAULT);
				return;
			case Wcs10Package.WCS_CAPABILITY_TYPE__VERSION:
				unsetVersion();
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
			case Wcs10Package.WCS_CAPABILITY_TYPE__REQUEST:
				return request != null;
			case Wcs10Package.WCS_CAPABILITY_TYPE__EXCEPTION:
				return exception != null;
			case Wcs10Package.WCS_CAPABILITY_TYPE__VENDOR_SPECIFIC_CAPABILITIES:
				return vendorSpecificCapabilities != null;
			case Wcs10Package.WCS_CAPABILITY_TYPE__UPDATE_SEQUENCE:
				return UPDATE_SEQUENCE_EDEFAULT == null ? updateSequence != null : !UPDATE_SEQUENCE_EDEFAULT.equals(updateSequence);
			case Wcs10Package.WCS_CAPABILITY_TYPE__VERSION:
				return isSetVersion();
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
		result.append(" (updateSequence: ");
		result.append(updateSequence);
		result.append(", version: ");
		if (versionESet) result.append(version); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //WCSCapabilityTypeImpl
