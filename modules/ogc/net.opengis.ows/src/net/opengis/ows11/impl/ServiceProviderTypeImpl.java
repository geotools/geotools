/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.impl;

import net.opengis.ows11.OnlineResourceType;
import net.opengis.ows11.Ows11Package;
import net.opengis.ows11.ResponsiblePartySubsetType;
import net.opengis.ows11.ServiceProviderType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Service Provider Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows11.impl.ServiceProviderTypeImpl#getProviderName <em>Provider Name</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.ServiceProviderTypeImpl#getProviderSite <em>Provider Site</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.ServiceProviderTypeImpl#getServiceContact <em>Service Contact</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ServiceProviderTypeImpl extends EObjectImpl implements ServiceProviderType {
    /**
     * The default value of the '{@link #getProviderName() <em>Provider Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProviderName()
     * @generated
     * @ordered
     */
    protected static final String PROVIDER_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProviderName() <em>Provider Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProviderName()
     * @generated
     * @ordered
     */
    protected String providerName = PROVIDER_NAME_EDEFAULT;

    /**
     * The cached value of the '{@link #getProviderSite() <em>Provider Site</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProviderSite()
     * @generated
     * @ordered
     */
    protected OnlineResourceType providerSite;

    /**
     * The cached value of the '{@link #getServiceContact() <em>Service Contact</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServiceContact()
     * @generated
     * @ordered
     */
    protected ResponsiblePartySubsetType serviceContact;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ServiceProviderTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Ows11Package.Literals.SERVICE_PROVIDER_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProviderName(String newProviderName) {
        String oldProviderName = providerName;
        providerName = newProviderName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.SERVICE_PROVIDER_TYPE__PROVIDER_NAME, oldProviderName, providerName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OnlineResourceType getProviderSite() {
        return providerSite;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetProviderSite(OnlineResourceType newProviderSite, NotificationChain msgs) {
        OnlineResourceType oldProviderSite = providerSite;
        providerSite = newProviderSite;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows11Package.SERVICE_PROVIDER_TYPE__PROVIDER_SITE, oldProviderSite, newProviderSite);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProviderSite(OnlineResourceType newProviderSite) {
        if (newProviderSite != providerSite) {
            NotificationChain msgs = null;
            if (providerSite != null)
                msgs = ((InternalEObject)providerSite).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows11Package.SERVICE_PROVIDER_TYPE__PROVIDER_SITE, null, msgs);
            if (newProviderSite != null)
                msgs = ((InternalEObject)newProviderSite).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows11Package.SERVICE_PROVIDER_TYPE__PROVIDER_SITE, null, msgs);
            msgs = basicSetProviderSite(newProviderSite, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.SERVICE_PROVIDER_TYPE__PROVIDER_SITE, newProviderSite, newProviderSite));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResponsiblePartySubsetType getServiceContact() {
        return serviceContact;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetServiceContact(ResponsiblePartySubsetType newServiceContact, NotificationChain msgs) {
        ResponsiblePartySubsetType oldServiceContact = serviceContact;
        serviceContact = newServiceContact;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows11Package.SERVICE_PROVIDER_TYPE__SERVICE_CONTACT, oldServiceContact, newServiceContact);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServiceContact(ResponsiblePartySubsetType newServiceContact) {
        if (newServiceContact != serviceContact) {
            NotificationChain msgs = null;
            if (serviceContact != null)
                msgs = ((InternalEObject)serviceContact).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows11Package.SERVICE_PROVIDER_TYPE__SERVICE_CONTACT, null, msgs);
            if (newServiceContact != null)
                msgs = ((InternalEObject)newServiceContact).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows11Package.SERVICE_PROVIDER_TYPE__SERVICE_CONTACT, null, msgs);
            msgs = basicSetServiceContact(newServiceContact, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.SERVICE_PROVIDER_TYPE__SERVICE_CONTACT, newServiceContact, newServiceContact));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows11Package.SERVICE_PROVIDER_TYPE__PROVIDER_SITE:
                return basicSetProviderSite(null, msgs);
            case Ows11Package.SERVICE_PROVIDER_TYPE__SERVICE_CONTACT:
                return basicSetServiceContact(null, msgs);
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
            case Ows11Package.SERVICE_PROVIDER_TYPE__PROVIDER_NAME:
                return getProviderName();
            case Ows11Package.SERVICE_PROVIDER_TYPE__PROVIDER_SITE:
                return getProviderSite();
            case Ows11Package.SERVICE_PROVIDER_TYPE__SERVICE_CONTACT:
                return getServiceContact();
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
            case Ows11Package.SERVICE_PROVIDER_TYPE__PROVIDER_NAME:
                setProviderName((String)newValue);
                return;
            case Ows11Package.SERVICE_PROVIDER_TYPE__PROVIDER_SITE:
                setProviderSite((OnlineResourceType)newValue);
                return;
            case Ows11Package.SERVICE_PROVIDER_TYPE__SERVICE_CONTACT:
                setServiceContact((ResponsiblePartySubsetType)newValue);
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
            case Ows11Package.SERVICE_PROVIDER_TYPE__PROVIDER_NAME:
                setProviderName(PROVIDER_NAME_EDEFAULT);
                return;
            case Ows11Package.SERVICE_PROVIDER_TYPE__PROVIDER_SITE:
                setProviderSite((OnlineResourceType)null);
                return;
            case Ows11Package.SERVICE_PROVIDER_TYPE__SERVICE_CONTACT:
                setServiceContact((ResponsiblePartySubsetType)null);
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
            case Ows11Package.SERVICE_PROVIDER_TYPE__PROVIDER_NAME:
                return PROVIDER_NAME_EDEFAULT == null ? providerName != null : !PROVIDER_NAME_EDEFAULT.equals(providerName);
            case Ows11Package.SERVICE_PROVIDER_TYPE__PROVIDER_SITE:
                return providerSite != null;
            case Ows11Package.SERVICE_PROVIDER_TYPE__SERVICE_CONTACT:
                return serviceContact != null;
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
        result.append(" (providerName: ");
        result.append(providerName);
        result.append(')');
        return result.toString();
    }

} //ServiceProviderTypeImpl
