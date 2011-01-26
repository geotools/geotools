/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.wps10.ComplexDataCombinationType;
import net.opengis.wps10.ComplexDataCombinationsType;
import net.opengis.wps10.SupportedComplexDataType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Supported Complex Data Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.SupportedComplexDataTypeImpl#getDefault <em>Default</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.SupportedComplexDataTypeImpl#getSupported <em>Supported</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SupportedComplexDataTypeImpl extends EObjectImpl implements SupportedComplexDataType {
    /**
     * The cached value of the '{@link #getDefault() <em>Default</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefault()
     * @generated
     * @ordered
     */
    protected ComplexDataCombinationType default_;

    /**
     * The cached value of the '{@link #getSupported() <em>Supported</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSupported()
     * @generated
     * @ordered
     */
    protected ComplexDataCombinationsType supported;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SupportedComplexDataTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.SUPPORTED_COMPLEX_DATA_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComplexDataCombinationType getDefault() {
        return default_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDefault(ComplexDataCombinationType newDefault, NotificationChain msgs) {
        ComplexDataCombinationType oldDefault = default_;
        default_ = newDefault;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__DEFAULT, oldDefault, newDefault);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefault(ComplexDataCombinationType newDefault) {
        if (newDefault != default_) {
            NotificationChain msgs = null;
            if (default_ != null)
                msgs = ((InternalEObject)default_).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__DEFAULT, null, msgs);
            if (newDefault != null)
                msgs = ((InternalEObject)newDefault).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__DEFAULT, null, msgs);
            msgs = basicSetDefault(newDefault, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__DEFAULT, newDefault, newDefault));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComplexDataCombinationsType getSupported() {
        return supported;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSupported(ComplexDataCombinationsType newSupported, NotificationChain msgs) {
        ComplexDataCombinationsType oldSupported = supported;
        supported = newSupported;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__SUPPORTED, oldSupported, newSupported);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSupported(ComplexDataCombinationsType newSupported) {
        if (newSupported != supported) {
            NotificationChain msgs = null;
            if (supported != null)
                msgs = ((InternalEObject)supported).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__SUPPORTED, null, msgs);
            if (newSupported != null)
                msgs = ((InternalEObject)newSupported).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__SUPPORTED, null, msgs);
            msgs = basicSetSupported(newSupported, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__SUPPORTED, newSupported, newSupported));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__DEFAULT:
                return basicSetDefault(null, msgs);
            case Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__SUPPORTED:
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
            case Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__DEFAULT:
                return getDefault();
            case Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__SUPPORTED:
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
            case Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__DEFAULT:
                setDefault((ComplexDataCombinationType)newValue);
                return;
            case Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__SUPPORTED:
                setSupported((ComplexDataCombinationsType)newValue);
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
            case Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__DEFAULT:
                setDefault((ComplexDataCombinationType)null);
                return;
            case Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__SUPPORTED:
                setSupported((ComplexDataCombinationsType)null);
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
            case Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__DEFAULT:
                return default_ != null;
            case Wps10Package.SUPPORTED_COMPLEX_DATA_TYPE__SUPPORTED:
                return supported != null;
        }
        return super.eIsSet(featureID);
    }

} //SupportedComplexDataTypeImpl
