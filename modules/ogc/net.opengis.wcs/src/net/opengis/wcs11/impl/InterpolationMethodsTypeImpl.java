/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import java.util.Collection;

import net.opengis.wcs11.InterpolationMethodType;
import net.opengis.wcs11.InterpolationMethodsType;
import net.opengis.wcs11.Wcs111Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Interpolation Methods Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.InterpolationMethodsTypeImpl#getInterpolationMethod <em>Interpolation Method</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.InterpolationMethodsTypeImpl#getDefault <em>Default</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InterpolationMethodsTypeImpl extends EObjectImpl implements InterpolationMethodsType {
    /**
     * The cached value of the '{@link #getInterpolationMethod() <em>Interpolation Method</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInterpolationMethod()
     * @generated
     * @ordered
     */
    protected EList interpolationMethod;

    /**
     * The default value of the '{@link #getDefault() <em>Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefault()
     * @generated
     * @ordered
     */
    protected static final String DEFAULT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDefault() <em>Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefault()
     * @generated
     * @ordered
     */
    protected String default_ = DEFAULT_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected InterpolationMethodsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wcs111Package.Literals.INTERPOLATION_METHODS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getInterpolationMethod() {
        if (interpolationMethod == null) {
            interpolationMethod = new EObjectContainmentEList(InterpolationMethodType.class, this, Wcs111Package.INTERPOLATION_METHODS_TYPE__INTERPOLATION_METHOD);
        }
        return interpolationMethod;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDefault() {
        return default_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefault(String newDefault) {
        String oldDefault = default_;
        default_ = newDefault;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.INTERPOLATION_METHODS_TYPE__DEFAULT, oldDefault, default_));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs111Package.INTERPOLATION_METHODS_TYPE__INTERPOLATION_METHOD:
                return ((InternalEList)getInterpolationMethod()).basicRemove(otherEnd, msgs);
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
            case Wcs111Package.INTERPOLATION_METHODS_TYPE__INTERPOLATION_METHOD:
                return getInterpolationMethod();
            case Wcs111Package.INTERPOLATION_METHODS_TYPE__DEFAULT:
                return getDefault();
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
            case Wcs111Package.INTERPOLATION_METHODS_TYPE__INTERPOLATION_METHOD:
                getInterpolationMethod().clear();
                getInterpolationMethod().addAll((Collection)newValue);
                return;
            case Wcs111Package.INTERPOLATION_METHODS_TYPE__DEFAULT:
                setDefault((String)newValue);
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
            case Wcs111Package.INTERPOLATION_METHODS_TYPE__INTERPOLATION_METHOD:
                getInterpolationMethod().clear();
                return;
            case Wcs111Package.INTERPOLATION_METHODS_TYPE__DEFAULT:
                setDefault(DEFAULT_EDEFAULT);
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
            case Wcs111Package.INTERPOLATION_METHODS_TYPE__INTERPOLATION_METHOD:
                return interpolationMethod != null && !interpolationMethod.isEmpty();
            case Wcs111Package.INTERPOLATION_METHODS_TYPE__DEFAULT:
                return DEFAULT_EDEFAULT == null ? default_ != null : !DEFAULT_EDEFAULT.equals(default_);
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
        result.append(" (default: ");
        result.append(default_);
        result.append(')');
        return result.toString();
    }

} //InterpolationMethodsTypeImpl
