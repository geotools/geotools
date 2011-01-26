/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.wps10.DefaultType2;
import net.opengis.wps10.LanguagesType;
import net.opengis.wps10.LanguagesType1;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Languages Type1</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.LanguagesType1Impl#getDefault <em>Default</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.LanguagesType1Impl#getSupported <em>Supported</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LanguagesType1Impl extends EObjectImpl implements LanguagesType1 {
    /**
     * The cached value of the '{@link #getDefault() <em>Default</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefault()
     * @generated
     * @ordered
     */
    protected DefaultType2 default_;

    /**
     * The cached value of the '{@link #getSupported() <em>Supported</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSupported()
     * @generated
     * @ordered
     */
    protected LanguagesType supported;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LanguagesType1Impl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.LANGUAGES_TYPE1;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DefaultType2 getDefault() {
        return default_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDefault(DefaultType2 newDefault, NotificationChain msgs) {
        DefaultType2 oldDefault = default_;
        default_ = newDefault;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.LANGUAGES_TYPE1__DEFAULT, oldDefault, newDefault);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefault(DefaultType2 newDefault) {
        if (newDefault != default_) {
            NotificationChain msgs = null;
            if (default_ != null)
                msgs = ((InternalEObject)default_).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.LANGUAGES_TYPE1__DEFAULT, null, msgs);
            if (newDefault != null)
                msgs = ((InternalEObject)newDefault).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.LANGUAGES_TYPE1__DEFAULT, null, msgs);
            msgs = basicSetDefault(newDefault, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.LANGUAGES_TYPE1__DEFAULT, newDefault, newDefault));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LanguagesType getSupported() {
        return supported;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSupported(LanguagesType newSupported, NotificationChain msgs) {
        LanguagesType oldSupported = supported;
        supported = newSupported;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.LANGUAGES_TYPE1__SUPPORTED, oldSupported, newSupported);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSupported(LanguagesType newSupported) {
        if (newSupported != supported) {
            NotificationChain msgs = null;
            if (supported != null)
                msgs = ((InternalEObject)supported).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.LANGUAGES_TYPE1__SUPPORTED, null, msgs);
            if (newSupported != null)
                msgs = ((InternalEObject)newSupported).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.LANGUAGES_TYPE1__SUPPORTED, null, msgs);
            msgs = basicSetSupported(newSupported, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.LANGUAGES_TYPE1__SUPPORTED, newSupported, newSupported));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.LANGUAGES_TYPE1__DEFAULT:
                return basicSetDefault(null, msgs);
            case Wps10Package.LANGUAGES_TYPE1__SUPPORTED:
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
            case Wps10Package.LANGUAGES_TYPE1__DEFAULT:
                return getDefault();
            case Wps10Package.LANGUAGES_TYPE1__SUPPORTED:
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
            case Wps10Package.LANGUAGES_TYPE1__DEFAULT:
                setDefault((DefaultType2)newValue);
                return;
            case Wps10Package.LANGUAGES_TYPE1__SUPPORTED:
                setSupported((LanguagesType)newValue);
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
            case Wps10Package.LANGUAGES_TYPE1__DEFAULT:
                setDefault((DefaultType2)null);
                return;
            case Wps10Package.LANGUAGES_TYPE1__SUPPORTED:
                setSupported((LanguagesType)null);
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
            case Wps10Package.LANGUAGES_TYPE1__DEFAULT:
                return default_ != null;
            case Wps10Package.LANGUAGES_TYPE1__SUPPORTED:
                return supported != null;
        }
        return super.eIsSet(featureID);
    }

} //LanguagesType1Impl
