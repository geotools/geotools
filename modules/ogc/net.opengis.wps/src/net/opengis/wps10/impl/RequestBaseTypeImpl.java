/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.wps10.RequestBaseType;
import net.opengis.wps10.Wps10Package;

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
 *   <li>{@link net.opengis.wps10.impl.RequestBaseTypeImpl#getLanguage <em>Language</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.RequestBaseTypeImpl#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.RequestBaseTypeImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.RequestBaseTypeImpl#getBaseUrl <em>Base Url</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RequestBaseTypeImpl extends EObjectImpl implements RequestBaseType {
    /**
     * The default value of the '{@link #getLanguage() <em>Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLanguage()
     * @generated
     * @ordered
     */
    protected static final String LANGUAGE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLanguage() <em>Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLanguage()
     * @generated
     * @ordered
     */
    protected String language = LANGUAGE_EDEFAULT;

    /**
     * The default value of the '{@link #getService() <em>Service</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getService()
     * @generated
     * @ordered
     */
    protected static final String SERVICE_EDEFAULT = "WPS";

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
        return Wps10Package.Literals.REQUEST_BASE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLanguage() {
        return language;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLanguage(String newLanguage) {
        String oldLanguage = language;
        language = newLanguage;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.REQUEST_BASE_TYPE__LANGUAGE, oldLanguage, language));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.REQUEST_BASE_TYPE__SERVICE, oldService, service, !oldServiceESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wps10Package.REQUEST_BASE_TYPE__SERVICE, oldService, SERVICE_EDEFAULT, oldServiceESet));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.REQUEST_BASE_TYPE__VERSION, oldVersion, version, !oldVersionESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wps10Package.REQUEST_BASE_TYPE__VERSION, oldVersion, VERSION_EDEFAULT, oldVersionESet));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.REQUEST_BASE_TYPE__BASE_URL, oldBaseUrl, baseUrl));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wps10Package.REQUEST_BASE_TYPE__LANGUAGE:
                return getLanguage();
            case Wps10Package.REQUEST_BASE_TYPE__SERVICE:
                return getService();
            case Wps10Package.REQUEST_BASE_TYPE__VERSION:
                return getVersion();
            case Wps10Package.REQUEST_BASE_TYPE__BASE_URL:
                return getBaseUrl();
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
            case Wps10Package.REQUEST_BASE_TYPE__LANGUAGE:
                setLanguage((String)newValue);
                return;
            case Wps10Package.REQUEST_BASE_TYPE__SERVICE:
                setService((String)newValue);
                return;
            case Wps10Package.REQUEST_BASE_TYPE__VERSION:
                setVersion((String)newValue);
                return;
            case Wps10Package.REQUEST_BASE_TYPE__BASE_URL:
                setBaseUrl((String)newValue);
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
            case Wps10Package.REQUEST_BASE_TYPE__LANGUAGE:
                setLanguage(LANGUAGE_EDEFAULT);
                return;
            case Wps10Package.REQUEST_BASE_TYPE__SERVICE:
                unsetService();
                return;
            case Wps10Package.REQUEST_BASE_TYPE__VERSION:
                unsetVersion();
                return;
            case Wps10Package.REQUEST_BASE_TYPE__BASE_URL:
                setBaseUrl(BASE_URL_EDEFAULT);
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
            case Wps10Package.REQUEST_BASE_TYPE__LANGUAGE:
                return LANGUAGE_EDEFAULT == null ? language != null : !LANGUAGE_EDEFAULT.equals(language);
            case Wps10Package.REQUEST_BASE_TYPE__SERVICE:
                return isSetService();
            case Wps10Package.REQUEST_BASE_TYPE__VERSION:
                return isSetVersion();
            case Wps10Package.REQUEST_BASE_TYPE__BASE_URL:
                return BASE_URL_EDEFAULT == null ? baseUrl != null : !BASE_URL_EDEFAULT.equals(baseUrl);
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
        result.append(" (language: ");
        result.append(language);
        result.append(", service: ");
        if (serviceESet) result.append(service); else result.append("<unset>");
        result.append(", version: ");
        if (versionESet) result.append(version); else result.append("<unset>");
        result.append(", baseUrl: ");
        result.append(baseUrl);
        result.append(')');
        return result.toString();
    }

} //RequestBaseTypeImpl
