/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.wps10.ProcessBriefType;
import net.opengis.wps10.WSDLType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Process Brief Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.ProcessBriefTypeImpl#getProfile <em>Profile</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.ProcessBriefTypeImpl#getWSDL <em>WSDL</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.ProcessBriefTypeImpl#getProcessVersion <em>Process Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProcessBriefTypeImpl extends DescriptionTypeImpl implements ProcessBriefType {
    /**
     * The default value of the '{@link #getProfile() <em>Profile</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProfile()
     * @generated
     * @ordered
     */
    protected static final String PROFILE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProfile() <em>Profile</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProfile()
     * @generated
     * @ordered
     */
    protected String profile = PROFILE_EDEFAULT;

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
     * The default value of the '{@link #getProcessVersion() <em>Process Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProcessVersion()
     * @generated
     * @ordered
     */
    protected static final String PROCESS_VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProcessVersion() <em>Process Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProcessVersion()
     * @generated
     * @ordered
     */
    protected String processVersion = PROCESS_VERSION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ProcessBriefTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.PROCESS_BRIEF_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProfile() {
        return profile;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProfile(String newProfile) {
        String oldProfile = profile;
        profile = newProfile;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.PROCESS_BRIEF_TYPE__PROFILE, oldProfile, profile));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.PROCESS_BRIEF_TYPE__WSDL, oldWSDL, newWSDL);
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
                msgs = ((InternalEObject)wSDL).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.PROCESS_BRIEF_TYPE__WSDL, null, msgs);
            if (newWSDL != null)
                msgs = ((InternalEObject)newWSDL).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.PROCESS_BRIEF_TYPE__WSDL, null, msgs);
            msgs = basicSetWSDL(newWSDL, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.PROCESS_BRIEF_TYPE__WSDL, newWSDL, newWSDL));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProcessVersion() {
        return processVersion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProcessVersion(String newProcessVersion) {
        String oldProcessVersion = processVersion;
        processVersion = newProcessVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.PROCESS_BRIEF_TYPE__PROCESS_VERSION, oldProcessVersion, processVersion));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.PROCESS_BRIEF_TYPE__WSDL:
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
            case Wps10Package.PROCESS_BRIEF_TYPE__PROFILE:
                return getProfile();
            case Wps10Package.PROCESS_BRIEF_TYPE__WSDL:
                return getWSDL();
            case Wps10Package.PROCESS_BRIEF_TYPE__PROCESS_VERSION:
                return getProcessVersion();
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
            case Wps10Package.PROCESS_BRIEF_TYPE__PROFILE:
                setProfile((String)newValue);
                return;
            case Wps10Package.PROCESS_BRIEF_TYPE__WSDL:
                setWSDL((WSDLType)newValue);
                return;
            case Wps10Package.PROCESS_BRIEF_TYPE__PROCESS_VERSION:
                setProcessVersion((String)newValue);
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
            case Wps10Package.PROCESS_BRIEF_TYPE__PROFILE:
                setProfile(PROFILE_EDEFAULT);
                return;
            case Wps10Package.PROCESS_BRIEF_TYPE__WSDL:
                setWSDL((WSDLType)null);
                return;
            case Wps10Package.PROCESS_BRIEF_TYPE__PROCESS_VERSION:
                setProcessVersion(PROCESS_VERSION_EDEFAULT);
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
            case Wps10Package.PROCESS_BRIEF_TYPE__PROFILE:
                return PROFILE_EDEFAULT == null ? profile != null : !PROFILE_EDEFAULT.equals(profile);
            case Wps10Package.PROCESS_BRIEF_TYPE__WSDL:
                return wSDL != null;
            case Wps10Package.PROCESS_BRIEF_TYPE__PROCESS_VERSION:
                return PROCESS_VERSION_EDEFAULT == null ? processVersion != null : !PROCESS_VERSION_EDEFAULT.equals(processVersion);
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
        result.append(" (profile: ");
        result.append(profile);
        result.append(", processVersion: ");
        result.append(processVersion);
        result.append(')');
        return result.toString();
    }

} //ProcessBriefTypeImpl
