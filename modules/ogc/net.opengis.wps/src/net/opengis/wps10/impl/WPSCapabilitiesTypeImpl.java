/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.ows11.impl.CapabilitiesBaseTypeImpl;

import net.opengis.wps10.LanguagesType1;
import net.opengis.wps10.ProcessOfferingsType;
import net.opengis.wps10.WPSCapabilitiesType;
import net.opengis.wps10.WSDLType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>WPS Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.WPSCapabilitiesTypeImpl#getProcessOfferings <em>Process Offerings</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.WPSCapabilitiesTypeImpl#getLanguages <em>Languages</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.WPSCapabilitiesTypeImpl#getWSDL <em>WSDL</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.WPSCapabilitiesTypeImpl#getLang <em>Lang</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.WPSCapabilitiesTypeImpl#getService <em>Service</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WPSCapabilitiesTypeImpl extends CapabilitiesBaseTypeImpl implements WPSCapabilitiesType {
    /**
     * The cached value of the '{@link #getProcessOfferings() <em>Process Offerings</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProcessOfferings()
     * @generated
     * @ordered
     */
    protected ProcessOfferingsType processOfferings;

    /**
     * The cached value of the '{@link #getLanguages() <em>Languages</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLanguages()
     * @generated
     * @ordered
     */
    protected LanguagesType1 languages;

    /**
     * The cached value of the '{@link #getWSDL() <em>WSDL</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWSDL()
     * @generated
     * @ordered
     */
    protected WSDLType wSDL;

    /**
     * The default value of the '{@link #getLang() <em>Lang</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLang()
     * @generated
     * @ordered
     */
    protected static final String LANG_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLang() <em>Lang</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLang()
     * @generated
     * @ordered
     */
    protected String lang = LANG_EDEFAULT;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected WPSCapabilitiesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.WPS_CAPABILITIES_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProcessOfferingsType getProcessOfferings() {
        return processOfferings;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetProcessOfferings(ProcessOfferingsType newProcessOfferings, NotificationChain msgs) {
        ProcessOfferingsType oldProcessOfferings = processOfferings;
        processOfferings = newProcessOfferings;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.WPS_CAPABILITIES_TYPE__PROCESS_OFFERINGS, oldProcessOfferings, newProcessOfferings);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProcessOfferings(ProcessOfferingsType newProcessOfferings) {
        if (newProcessOfferings != processOfferings) {
            NotificationChain msgs = null;
            if (processOfferings != null)
                msgs = ((InternalEObject)processOfferings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.WPS_CAPABILITIES_TYPE__PROCESS_OFFERINGS, null, msgs);
            if (newProcessOfferings != null)
                msgs = ((InternalEObject)newProcessOfferings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.WPS_CAPABILITIES_TYPE__PROCESS_OFFERINGS, null, msgs);
            msgs = basicSetProcessOfferings(newProcessOfferings, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.WPS_CAPABILITIES_TYPE__PROCESS_OFFERINGS, newProcessOfferings, newProcessOfferings));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LanguagesType1 getLanguages() {
        return languages;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLanguages(LanguagesType1 newLanguages, NotificationChain msgs) {
        LanguagesType1 oldLanguages = languages;
        languages = newLanguages;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.WPS_CAPABILITIES_TYPE__LANGUAGES, oldLanguages, newLanguages);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLanguages(LanguagesType1 newLanguages) {
        if (newLanguages != languages) {
            NotificationChain msgs = null;
            if (languages != null)
                msgs = ((InternalEObject)languages).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.WPS_CAPABILITIES_TYPE__LANGUAGES, null, msgs);
            if (newLanguages != null)
                msgs = ((InternalEObject)newLanguages).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.WPS_CAPABILITIES_TYPE__LANGUAGES, null, msgs);
            msgs = basicSetLanguages(newLanguages, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.WPS_CAPABILITIES_TYPE__LANGUAGES, newLanguages, newLanguages));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WSDLType getWSDL() {
        return wSDL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetWSDL(WSDLType newWSDL, NotificationChain msgs) {
        WSDLType oldWSDL = wSDL;
        wSDL = newWSDL;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.WPS_CAPABILITIES_TYPE__WSDL, oldWSDL, newWSDL);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWSDL(WSDLType newWSDL) {
        if (newWSDL != wSDL) {
            NotificationChain msgs = null;
            if (wSDL != null)
                msgs = ((InternalEObject)wSDL).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.WPS_CAPABILITIES_TYPE__WSDL, null, msgs);
            if (newWSDL != null)
                msgs = ((InternalEObject)newWSDL).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.WPS_CAPABILITIES_TYPE__WSDL, null, msgs);
            msgs = basicSetWSDL(newWSDL, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.WPS_CAPABILITIES_TYPE__WSDL, newWSDL, newWSDL));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLang() {
        return lang;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLang(String newLang) {
        String oldLang = lang;
        lang = newLang;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.WPS_CAPABILITIES_TYPE__LANG, oldLang, lang));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.WPS_CAPABILITIES_TYPE__SERVICE, oldService, service, !oldServiceESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wps10Package.WPS_CAPABILITIES_TYPE__SERVICE, oldService, SERVICE_EDEFAULT, oldServiceESet));
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
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.WPS_CAPABILITIES_TYPE__PROCESS_OFFERINGS:
                return basicSetProcessOfferings(null, msgs);
            case Wps10Package.WPS_CAPABILITIES_TYPE__LANGUAGES:
                return basicSetLanguages(null, msgs);
            case Wps10Package.WPS_CAPABILITIES_TYPE__WSDL:
                return basicSetWSDL(null, msgs);
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
            case Wps10Package.WPS_CAPABILITIES_TYPE__PROCESS_OFFERINGS:
                return getProcessOfferings();
            case Wps10Package.WPS_CAPABILITIES_TYPE__LANGUAGES:
                return getLanguages();
            case Wps10Package.WPS_CAPABILITIES_TYPE__WSDL:
                return getWSDL();
            case Wps10Package.WPS_CAPABILITIES_TYPE__LANG:
                return getLang();
            case Wps10Package.WPS_CAPABILITIES_TYPE__SERVICE:
                return getService();
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
            case Wps10Package.WPS_CAPABILITIES_TYPE__PROCESS_OFFERINGS:
                setProcessOfferings((ProcessOfferingsType)newValue);
                return;
            case Wps10Package.WPS_CAPABILITIES_TYPE__LANGUAGES:
                setLanguages((LanguagesType1)newValue);
                return;
            case Wps10Package.WPS_CAPABILITIES_TYPE__WSDL:
                setWSDL((WSDLType)newValue);
                return;
            case Wps10Package.WPS_CAPABILITIES_TYPE__LANG:
                setLang((String)newValue);
                return;
            case Wps10Package.WPS_CAPABILITIES_TYPE__SERVICE:
                setService((String)newValue);
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
            case Wps10Package.WPS_CAPABILITIES_TYPE__PROCESS_OFFERINGS:
                setProcessOfferings((ProcessOfferingsType)null);
                return;
            case Wps10Package.WPS_CAPABILITIES_TYPE__LANGUAGES:
                setLanguages((LanguagesType1)null);
                return;
            case Wps10Package.WPS_CAPABILITIES_TYPE__WSDL:
                setWSDL((WSDLType)null);
                return;
            case Wps10Package.WPS_CAPABILITIES_TYPE__LANG:
                setLang(LANG_EDEFAULT);
                return;
            case Wps10Package.WPS_CAPABILITIES_TYPE__SERVICE:
                unsetService();
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
            case Wps10Package.WPS_CAPABILITIES_TYPE__PROCESS_OFFERINGS:
                return processOfferings != null;
            case Wps10Package.WPS_CAPABILITIES_TYPE__LANGUAGES:
                return languages != null;
            case Wps10Package.WPS_CAPABILITIES_TYPE__WSDL:
                return wSDL != null;
            case Wps10Package.WPS_CAPABILITIES_TYPE__LANG:
                return LANG_EDEFAULT == null ? lang != null : !LANG_EDEFAULT.equals(lang);
            case Wps10Package.WPS_CAPABILITIES_TYPE__SERVICE:
                return isSetService();
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
        result.append(" (lang: ");
        result.append(lang);
        result.append(", service: ");
        if (serviceESet) result.append(service); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //WPSCapabilitiesTypeImpl
