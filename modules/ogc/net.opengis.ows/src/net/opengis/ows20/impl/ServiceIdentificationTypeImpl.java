/**
 */
package net.opengis.ows20.impl;

import java.util.Collection;

import net.opengis.ows20.CodeType;
import net.opengis.ows20.ServiceIdentificationType;
import net.opengis.ows20.Ows20Package;

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
 *   <li>{@link net.opengis.ows20.impl.ServiceIdentificationTypeImpl#getServiceType <em>Service Type</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.ServiceIdentificationTypeImpl#getServiceTypeVersion <em>Service Type Version</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.ServiceIdentificationTypeImpl#getProfile <em>Profile</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.ServiceIdentificationTypeImpl#getFees <em>Fees</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.ServiceIdentificationTypeImpl#getAccessConstraints <em>Access Constraints</em>}</li>
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
     * The default value of the '{@link #getServiceTypeVersion() <em>Service Type Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServiceTypeVersion()
     * @generated
     * @ordered
     */
    protected static final String SERVICE_TYPE_VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getServiceTypeVersion() <em>Service Type Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServiceTypeVersion()
     * @generated
     * @ordered
     */
    protected String serviceTypeVersion = SERVICE_TYPE_VERSION_EDEFAULT;

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
     * The default value of the '{@link #getAccessConstraints() <em>Access Constraints</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAccessConstraints()
     * @generated
     * @ordered
     */
    protected static final String ACCESS_CONSTRAINTS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAccessConstraints() <em>Access Constraints</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAccessConstraints()
     * @generated
     * @ordered
     */
    protected String accessConstraints = ACCESS_CONSTRAINTS_EDEFAULT;

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
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.SERVICE_IDENTIFICATION_TYPE;
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows20Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE, oldServiceType, newServiceType);
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
                msgs = ((InternalEObject)serviceType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows20Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE, null, msgs);
            if (newServiceType != null)
                msgs = ((InternalEObject)newServiceType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows20Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE, null, msgs);
            msgs = basicSetServiceType(newServiceType, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE, newServiceType, newServiceType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getServiceTypeVersion() {
        return serviceTypeVersion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServiceTypeVersion(String newServiceTypeVersion) {
        String oldServiceTypeVersion = serviceTypeVersion;
        serviceTypeVersion = newServiceTypeVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION, oldServiceTypeVersion, serviceTypeVersion));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.SERVICE_IDENTIFICATION_TYPE__PROFILE, oldProfile, profile));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.SERVICE_IDENTIFICATION_TYPE__FEES, oldFees, fees));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAccessConstraints() {
        return accessConstraints;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAccessConstraints(String newAccessConstraints) {
        String oldAccessConstraints = accessConstraints;
        accessConstraints = newAccessConstraints;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS, oldAccessConstraints, accessConstraints));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE:
                return basicSetServiceType(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE:
                return getServiceType();
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION:
                return getServiceTypeVersion();
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__PROFILE:
                return getProfile();
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__FEES:
                return getFees();
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS:
                return getAccessConstraints();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE:
                setServiceType((CodeType)newValue);
                return;
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION:
                setServiceTypeVersion((String)newValue);
                return;
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__PROFILE:
                setProfile((String)newValue);
                return;
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__FEES:
                setFees((String)newValue);
                return;
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS:
                setAccessConstraints((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE:
                setServiceType((CodeType)null);
                return;
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION:
                setServiceTypeVersion(SERVICE_TYPE_VERSION_EDEFAULT);
                return;
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__PROFILE:
                setProfile(PROFILE_EDEFAULT);
                return;
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__FEES:
                setFees(FEES_EDEFAULT);
                return;
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS:
                setAccessConstraints(ACCESS_CONSTRAINTS_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE:
                return serviceType != null;
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__SERVICE_TYPE_VERSION:
                return SERVICE_TYPE_VERSION_EDEFAULT == null ? serviceTypeVersion != null : !SERVICE_TYPE_VERSION_EDEFAULT.equals(serviceTypeVersion);
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__PROFILE:
                return PROFILE_EDEFAULT == null ? profile != null : !PROFILE_EDEFAULT.equals(profile);
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__FEES:
                return FEES_EDEFAULT == null ? fees != null : !FEES_EDEFAULT.equals(fees);
            case Ows20Package.SERVICE_IDENTIFICATION_TYPE__ACCESS_CONSTRAINTS:
                return ACCESS_CONSTRAINTS_EDEFAULT == null ? accessConstraints != null : !ACCESS_CONSTRAINTS_EDEFAULT.equals(accessConstraints);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
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
