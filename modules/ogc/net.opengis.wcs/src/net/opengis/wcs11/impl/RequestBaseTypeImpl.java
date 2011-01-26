/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import java.util.HashMap;
import java.util.Map;
import net.opengis.wcs11.RequestBaseType;
import net.opengis.wcs11.Wcs11Package;
import net.opengis.wcs11.Wcs111Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Request Base Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.RequestBaseTypeImpl#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.RequestBaseTypeImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.RequestBaseTypeImpl#getBaseUrl <em>Base Url</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.RequestBaseTypeImpl#getExtendedProperties <em>Extended Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RequestBaseTypeImpl extends EObjectImpl implements RequestBaseType {
    /**
	 * The default value of the '{@link #getService() <em>Service</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getService()
	 * @generated
	 * @ordered
	 */
    protected static final String SERVICE_EDEFAULT = "WCS";

    /**
	 * The cached value of the '{@link #getService() <em>Service</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getService()
	 * @generated
	 * @ordered
	 */
    protected String service = SERVICE_EDEFAULT;

    /**
	 * This is true if the Service attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean serviceESet;

    /**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
    protected static final String VERSION_EDEFAULT = "1.1.1";

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
	 * The default value of the '{@link #getBaseUrl() <em>Base Url</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getBaseUrl()
	 * @generated
	 * @ordered
	 */
    protected static final String BASE_URL_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getBaseUrl() <em>Base Url</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getBaseUrl()
	 * @generated
	 * @ordered
	 */
    protected String baseUrl = BASE_URL_EDEFAULT;

    /**
	 * The default value of the '{@link #getExtendedProperties() <em>Extended Properties</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtendedProperties()
	 * @generated
	 * @ordered
	 */
	protected static final Map EXTENDED_PROPERTIES_EDEFAULT = null;

				/**
	 * The cached value of the '{@link #getExtendedProperties() <em>Extended Properties</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtendedProperties()
	 * @generated NOT
	 * @ordered
	 */
	protected Map extendedProperties = new HashMap();

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected RequestBaseTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
		return Wcs11Package.Literals.REQUEST_BASE_TYPE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getService() {
		return service;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setService(String newService) {
		String oldService = service;
		service = newService;
		boolean oldServiceESet = serviceESet;
		serviceESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs11Package.REQUEST_BASE_TYPE__SERVICE, oldService, service, !oldServiceESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetService() {
		String oldService = service;
		boolean oldServiceESet = serviceESet;
		service = SERVICE_EDEFAULT;
		serviceESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs11Package.REQUEST_BASE_TYPE__SERVICE, oldService, SERVICE_EDEFAULT, oldServiceESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetService() {
		return serviceESet;
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs11Package.REQUEST_BASE_TYPE__VERSION, oldVersion, version, !oldVersionESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wcs11Package.REQUEST_BASE_TYPE__VERSION, oldVersion, VERSION_EDEFAULT, oldVersionESet));
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
    public String getBaseUrl() {
		return baseUrl;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setBaseUrl(String newBaseUrl) {
		String oldBaseUrl = baseUrl;
		baseUrl = newBaseUrl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs11Package.REQUEST_BASE_TYPE__BASE_URL, oldBaseUrl, baseUrl));
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map getExtendedProperties() {
		return extendedProperties;
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExtendedProperties(Map newExtendedProperties) {
		Map oldExtendedProperties = extendedProperties;
		extendedProperties = newExtendedProperties;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wcs11Package.REQUEST_BASE_TYPE__EXTENDED_PROPERTIES, oldExtendedProperties, extendedProperties));
	}

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Wcs11Package.REQUEST_BASE_TYPE__SERVICE:
				return getService();
			case Wcs11Package.REQUEST_BASE_TYPE__VERSION:
				return getVersion();
			case Wcs11Package.REQUEST_BASE_TYPE__BASE_URL:
				return getBaseUrl();
			case Wcs11Package.REQUEST_BASE_TYPE__EXTENDED_PROPERTIES:
				return getExtendedProperties();
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
			case Wcs11Package.REQUEST_BASE_TYPE__SERVICE:
				setService((String)newValue);
				return;
			case Wcs11Package.REQUEST_BASE_TYPE__VERSION:
				setVersion((String)newValue);
				return;
			case Wcs11Package.REQUEST_BASE_TYPE__BASE_URL:
				setBaseUrl((String)newValue);
				return;
			case Wcs11Package.REQUEST_BASE_TYPE__EXTENDED_PROPERTIES:
				setExtendedProperties((Map)newValue);
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
			case Wcs11Package.REQUEST_BASE_TYPE__SERVICE:
				unsetService();
				return;
			case Wcs11Package.REQUEST_BASE_TYPE__VERSION:
				unsetVersion();
				return;
			case Wcs11Package.REQUEST_BASE_TYPE__BASE_URL:
				setBaseUrl(BASE_URL_EDEFAULT);
				return;
			case Wcs11Package.REQUEST_BASE_TYPE__EXTENDED_PROPERTIES:
				setExtendedProperties(EXTENDED_PROPERTIES_EDEFAULT);
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
			case Wcs11Package.REQUEST_BASE_TYPE__SERVICE:
				return isSetService();
			case Wcs11Package.REQUEST_BASE_TYPE__VERSION:
				return isSetVersion();
			case Wcs11Package.REQUEST_BASE_TYPE__BASE_URL:
				return BASE_URL_EDEFAULT == null ? baseUrl != null : !BASE_URL_EDEFAULT.equals(baseUrl);
			case Wcs11Package.REQUEST_BASE_TYPE__EXTENDED_PROPERTIES:
				return EXTENDED_PROPERTIES_EDEFAULT == null ? extendedProperties != null : !EXTENDED_PROPERTIES_EDEFAULT.equals(extendedProperties);
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
		result.append(" (service: ");
		if (serviceESet) result.append(service); else result.append("<unset>");
		result.append(", version: ");
		if (versionESet) result.append(version); else result.append("<unset>");
		result.append(", baseUrl: ");
		result.append(baseUrl);
		result.append(", extendedProperties: ");
		result.append(extendedProperties);
		result.append(')');
		return result.toString();
	}

} //RequestBaseTypeImpl
