/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.wps10.CRSsType;
import net.opengis.wps10.DefaultType;
import net.opengis.wps10.SupportedCRSsType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Supported CR Ss Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.SupportedCRSsTypeImpl#getDefault <em>Default</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.SupportedCRSsTypeImpl#getSupported <em>Supported</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SupportedCRSsTypeImpl extends EObjectImpl implements SupportedCRSsType {
    /**
     * The cached value of the '{@link #getDefault() <em>Default</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefault()
     * @generated
     * @ordered
     */
    protected DefaultType default_;

    /**
     * The cached value of the '{@link #getSupported() <em>Supported</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSupported()
     * @generated
     * @ordered
     */
    protected CRSsType supported;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SupportedCRSsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.SUPPORTED_CR_SS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DefaultType getDefault() {
        return default_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDefault(DefaultType newDefault, NotificationChain msgs) {
        DefaultType oldDefault = default_;
        default_ = newDefault;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.SUPPORTED_CR_SS_TYPE__DEFAULT, oldDefault, newDefault);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefault(DefaultType newDefault) {
        if (newDefault != default_) {
            NotificationChain msgs = null;
            if (default_ != null)
                msgs = ((InternalEObject)default_).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.SUPPORTED_CR_SS_TYPE__DEFAULT, null, msgs);
            if (newDefault != null)
                msgs = ((InternalEObject)newDefault).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.SUPPORTED_CR_SS_TYPE__DEFAULT, null, msgs);
            msgs = basicSetDefault(newDefault, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.SUPPORTED_CR_SS_TYPE__DEFAULT, newDefault, newDefault));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CRSsType getSupported() {
        return supported;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSupported(CRSsType newSupported, NotificationChain msgs) {
        CRSsType oldSupported = supported;
        supported = newSupported;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.SUPPORTED_CR_SS_TYPE__SUPPORTED, oldSupported, newSupported);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSupported(CRSsType newSupported) {
        if (newSupported != supported) {
            NotificationChain msgs = null;
            if (supported != null)
                msgs = ((InternalEObject)supported).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.SUPPORTED_CR_SS_TYPE__SUPPORTED, null, msgs);
            if (newSupported != null)
                msgs = ((InternalEObject)newSupported).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.SUPPORTED_CR_SS_TYPE__SUPPORTED, null, msgs);
            msgs = basicSetSupported(newSupported, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.SUPPORTED_CR_SS_TYPE__SUPPORTED, newSupported, newSupported));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.SUPPORTED_CR_SS_TYPE__DEFAULT:
                return basicSetDefault(null, msgs);
            case Wps10Package.SUPPORTED_CR_SS_TYPE__SUPPORTED:
                return basicSetSupported(null, msgs);
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
            case Wps10Package.SUPPORTED_CR_SS_TYPE__DEFAULT:
                return getDefault();
            case Wps10Package.SUPPORTED_CR_SS_TYPE__SUPPORTED:
                return getSupported();
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
            case Wps10Package.SUPPORTED_CR_SS_TYPE__DEFAULT:
                setDefault((DefaultType)newValue);
                return;
            case Wps10Package.SUPPORTED_CR_SS_TYPE__SUPPORTED:
                setSupported((CRSsType)newValue);
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
            case Wps10Package.SUPPORTED_CR_SS_TYPE__DEFAULT:
                setDefault((DefaultType)null);
                return;
            case Wps10Package.SUPPORTED_CR_SS_TYPE__SUPPORTED:
                setSupported((CRSsType)null);
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
            case Wps10Package.SUPPORTED_CR_SS_TYPE__DEFAULT:
                return default_ != null;
            case Wps10Package.SUPPORTED_CR_SS_TYPE__SUPPORTED:
                return supported != null;
        }
        return super.eIsSet(featureID);
    }

} //SupportedCRSsTypeImpl
