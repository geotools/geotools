/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.impl;

import java.util.Collection;

import net.opengis.ows11.CodeType;
import net.opengis.ows11.Ows11Package;
import net.opengis.ows11.ServiceIdentificationType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Service Identification Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows11.impl.ServiceIdentificationTypeImpl#getServiceType <em>Service Type</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.ServiceIdentificationTypeImpl#getServiceTypeVersion <em>Service Type Version</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.ServiceIdentificationTypeImpl#getProfile <em>Profile</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.ServiceIdentificationTypeImpl#getFees <em>Fees</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.ServiceIdentificationTypeImpl#getAccessConstraints <em>Access Constraints</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ServiceIdentificationTypeImpl extends DescriptionTypeImpl implements ServiceIdentificationType {
    /**
     * The cached value of the '{@link #getServiceType() <em>Service Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServiceType()
     * @generated
     * @ordered
     */
    protected CodeType serviceType;

    /**
     * The cached value of the '{@link #getServiceTypeVersion() <em>Service Type Version</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServiceTypeVersion()
     * @generated
     * @ordered
     */
    protected EList serviceTypeVersion;

    /**
     * The cached value of the '{@link #getProfile() <em>Profile</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProfile()
     * @generated
     * @ordered
     */
    protected EList profile;

    /**
     * The default value of the '{@link #getFees() <em>Fees</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFees()
     * @generated
     * @ordered
     */
    protected static final String FEES_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFees() <em>Fees</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFees()
     * @generated
     * @ordered
     */
    protected String fees = FEES_EDEFAULT;

    /**
     * The cached value of the '{@link #getAccessConstraints() <em>Access Constraints</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAccessConstraints()
     * @generated
     * @ordered
     */
    protected EList accessConstraints;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ServiceIdentificationTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Ows11Package.Literals.SERVICE_IDENTIFICATION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getServiceType() {
        return serviceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetServiceType(CodeType newServiceType, NotificationChain msgs) {
        CodeType oldServiceType = serviceType;
        serviceType = newServiceType;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows11Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE, oldServiceType, newServiceType);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServiceType(CodeType newServiceType) {
        if (newServiceType != serviceType) {
            NotificationChain msgs = null;
            if (serviceType != null)
                msgs = ((InternalEObject)serviceType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows11Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE, null, msgs);
            if (newServiceType != null)
                msgs = ((InternalEObject)newServiceType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows11Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE, null, msgs);
            msgs = basicSetServiceType(newServiceType, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE, newServiceType, newServiceType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getServiceTypeVersion() {
        if (serviceTypeVersion == null) {
            serviceTypeVersion = new EDataTypeEList(String.class, this, Ows11Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION);
        }
        return serviceTypeVersion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getProfile() {
        if (profile == null) {
            profile = new EDataTypeEList(String.class, this, Ows11Package.SERVICE_IDENTIFICATION_TYPE__PROFILE);
        }
        return profile;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFees() {
        return fees;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFees(String newFees) {
        String oldFees = fees;
        fees = newFees;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.SERVICE_IDENTIFICATION_TYPE__FEES, oldFees, fees));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getAccessConstraints() {
        if (accessConstraints == null) {
            accessConstraints = new EDataTypeEList(String.class, this, Ows11Package.SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS);
        }
        return accessConstraints;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE:
                return basicSetServiceType(null, msgs);
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
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE:
                return getServiceType();
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION:
                return getServiceTypeVersion();
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__PROFILE:
                return getProfile();
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__FEES:
                return getFees();
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS:
                return getAccessConstraints();
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
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE:
                setServiceType((CodeType)newValue);
                return;
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION:
                getServiceTypeVersion().clear();
                getServiceTypeVersion().addAll((Collection)newValue);
                return;
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__PROFILE:
                getProfile().clear();
                getProfile().addAll((Collection)newValue);
                return;
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__FEES:
                setFees((String)newValue);
                return;
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS:
                getAccessConstraints().clear();
                getAccessConstraints().addAll((Collection)newValue);
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
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE:
                setServiceType((CodeType)null);
                return;
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION:
                getServiceTypeVersion().clear();
                return;
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__PROFILE:
                getProfile().clear();
                return;
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__FEES:
                setFees(FEES_EDEFAULT);
                return;
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS:
                getAccessConstraints().clear();
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
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE:
                return serviceType != null;
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION:
                return serviceTypeVersion != null && !serviceTypeVersion.isEmpty();
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__PROFILE:
                return profile != null && !profile.isEmpty();
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__FEES:
                return FEES_EDEFAULT == null ? fees != null : !FEES_EDEFAULT.equals(fees);
            case Ows11Package.SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS:
                return accessConstraints != null && !accessConstraints.isEmpty();
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
        result.append(" (serviceTypeVersion: ");
        result.append(serviceTypeVersion);
        result.append(", profile: ");
        result.append(profile);
        result.append(", fees: ");
        result.append(fees);
        result.append(", accessConstraints: ");
        result.append(accessConstraints);
        result.append(')');
        return result.toString();
    }

} //ServiceIdentificationTypeImpl
