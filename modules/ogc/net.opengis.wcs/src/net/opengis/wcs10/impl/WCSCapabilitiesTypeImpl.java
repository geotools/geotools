/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10.impl;

import net.opengis.wcs10.ContentMetadataType;
import net.opengis.wcs10.ServiceType;
import net.opengis.wcs10.WCSCapabilitiesType;
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
 * An implementation of the model object '<em><b>WCS Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs10.impl.WCSCapabilitiesTypeImpl#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.WCSCapabilitiesTypeImpl#getCapability <em>Capability</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.WCSCapabilitiesTypeImpl#getContentMetadata <em>Content Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.WCSCapabilitiesTypeImpl#getUpdateSequence <em>Update Sequence</em>}</li>
 *   <li>{@link net.opengis.wcs10.impl.WCSCapabilitiesTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WCSCapabilitiesTypeImpl extends EObjectImpl implements WCSCapabilitiesType {
    /**
	 * The cached value of the '{@link #getService() <em>Service</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getService()
	 * @generated
	 * @ordered
	 */
    protected ServiceType service;

    /**
	 * The cached value of the '{@link #getCapability() <em>Capability</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getCapability()
	 * @generated
	 * @ordered
	 */
    protected WCSCapabilityType capability;

    /**
	 * The cached value of the '{@link #getContentMetadata() <em>Content Metadata</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getContentMetadata()
	 * @generated
	 * @ordered
	 */
    protected ContentMetadataType contentMetadata;

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
    protected WCSCapabilitiesTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs10Package.Literals.WCS_CAPABILITIES_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ServiceType getService() {
		return service;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetService(ServiceType newService, NotificationChain msgs) {
		ServiceType oldService = service;
		service = newService;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.WCS_CAPABILITIES_TYPE__SERVICE, oldService, newService);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setService(ServiceType newService) {
		if (newService != service) {
			NotificationChain msgs = null;
			if (service != null)
				msgs = ((InternalEObject)service).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.WCS_CAPABILITIES_TYPE__SERVICE, null, msgs);
			if (newService != null)
				msgs = ((InternalEObject)newService).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.WCS_CAPABILITIES_TYPE__SERVICE, null, msgs);
			msgs = basicSetService(newService, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.WCS_CAPABILITIES_TYPE__SERVICE, newService, newService));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public WCSCapabilityType getCapability() {
		return capability;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetCapability(WCSCapabilityType newCapability, NotificationChain msgs) {
		WCSCapabilityType oldCapability = capability;
		capability = newCapability;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.WCS_CAPABILITIES_TYPE__CAPABILITY, oldCapability, newCapability);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCapability(WCSCapabilityType newCapability) {
		if (newCapability != capability) {
			NotificationChain msgs = null;
			if (capability != null)
				msgs = ((InternalEObject)capability).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.WCS_CAPABILITIES_TYPE__CAPABILITY, null, msgs);
			if (newCapability != null)
				msgs = ((InternalEObject)newCapability).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.WCS_CAPABILITIES_TYPE__CAPABILITY, null, msgs);
			msgs = basicSetCapability(newCapability, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.WCS_CAPABILITIES_TYPE__CAPABILITY, newCapability, newCapability));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ContentMetadataType getContentMetadata() {
		return contentMetadata;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetContentMetadata(ContentMetadataType newContentMetadata, NotificationChain msgs) {
		ContentMetadataType oldContentMetadata = contentMetadata;
		contentMetadata = newContentMetadata;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs10Package.WCS_CAPABILITIES_TYPE__CONTENT_METADATA, oldContentMetadata, newContentMetadata);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setContentMetadata(ContentMetadataType newContentMetadata) {
		if (newContentMetadata != contentMetadata) {
			NotificationChain msgs = null;
			if (contentMetadata != null)
				msgs = ((InternalEObject)contentMetadata).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.WCS_CAPABILITIES_TYPE__CONTENT_METADATA, null, msgs);
			if (newContentMetadata != null)
				msgs = ((InternalEObject)newContentMetadata).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs10Package.WCS_CAPABILITIES_TYPE__CONTENT_METADATA, null, msgs);
			msgs = basicSetContentMetadata(newContentMetadata, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.WCS_CAPABILITIES_TYPE__CONTENT_METADATA, newContentMetadata, newContentMetadata));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.WCS_CAPABILITIES_TYPE__UPDATE_SEQUENCE, oldUpdateSequence, updateSequence));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs10Package.WCS_CAPABILITIES_TYPE__VERSION, oldVersion, version, !oldVersionESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs10Package.WCS_CAPABILITIES_TYPE__VERSION, oldVersion, VERSION_EDEFAULT, oldVersionESet));
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
			case Wcs10Package.WCS_CAPABILITIES_TYPE__SERVICE:
				return basicSetService(null, msgs);
			case Wcs10Package.WCS_CAPABILITIES_TYPE__CAPABILITY:
				return basicSetCapability(null, msgs);
			case Wcs10Package.WCS_CAPABILITIES_TYPE__CONTENT_METADATA:
				return basicSetContentMetadata(null, msgs);
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
			case Wcs10Package.WCS_CAPABILITIES_TYPE__SERVICE:
				return getService();
			case Wcs10Package.WCS_CAPABILITIES_TYPE__CAPABILITY:
				return getCapability();
			case Wcs10Package.WCS_CAPABILITIES_TYPE__CONTENT_METADATA:
				return getContentMetadata();
			case Wcs10Package.WCS_CAPABILITIES_TYPE__UPDATE_SEQUENCE:
				return getUpdateSequence();
			case Wcs10Package.WCS_CAPABILITIES_TYPE__VERSION:
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
			case Wcs10Package.WCS_CAPABILITIES_TYPE__SERVICE:
				setService((ServiceType)newValue);
				return;
			case Wcs10Package.WCS_CAPABILITIES_TYPE__CAPABILITY:
				setCapability((WCSCapabilityType)newValue);
				return;
			case Wcs10Package.WCS_CAPABILITIES_TYPE__CONTENT_METADATA:
				setContentMetadata((ContentMetadataType)newValue);
				return;
			case Wcs10Package.WCS_CAPABILITIES_TYPE__UPDATE_SEQUENCE:
				setUpdateSequence((String)newValue);
				return;
			case Wcs10Package.WCS_CAPABILITIES_TYPE__VERSION:
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
			case Wcs10Package.WCS_CAPABILITIES_TYPE__SERVICE:
				setService((ServiceType)null);
				return;
			case Wcs10Package.WCS_CAPABILITIES_TYPE__CAPABILITY:
				setCapability((WCSCapabilityType)null);
				return;
			case Wcs10Package.WCS_CAPABILITIES_TYPE__CONTENT_METADATA:
				setContentMetadata((ContentMetadataType)null);
				return;
			case Wcs10Package.WCS_CAPABILITIES_TYPE__UPDATE_SEQUENCE:
				setUpdateSequence(UPDATE_SEQUENCE_EDEFAULT);
				return;
			case Wcs10Package.WCS_CAPABILITIES_TYPE__VERSION:
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
			case Wcs10Package.WCS_CAPABILITIES_TYPE__SERVICE:
				return service != null;
			case Wcs10Package.WCS_CAPABILITIES_TYPE__CAPABILITY:
				return capability != null;
			case Wcs10Package.WCS_CAPABILITIES_TYPE__CONTENT_METADATA:
				return contentMetadata != null;
			case Wcs10Package.WCS_CAPABILITIES_TYPE__UPDATE_SEQUENCE:
				return UPDATE_SEQUENCE_EDEFAULT == null ? updateSequence != null : !UPDATE_SEQUENCE_EDEFAULT.equals(updateSequence);
			case Wcs10Package.WCS_CAPABILITIES_TYPE__VERSION:
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

} //WCSCapabilitiesTypeImpl
