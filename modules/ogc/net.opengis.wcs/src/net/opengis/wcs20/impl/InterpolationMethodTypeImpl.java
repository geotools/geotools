/**
 */
package net.opengis.wcs20.impl;

import net.opengis.wcs20.InterpolationMethodType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Interpolation Method Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.InterpolationMethodTypeImpl#getInterpolationMethod <em>Interpolation Method</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InterpolationMethodTypeImpl extends EObjectImpl implements InterpolationMethodType {
    /**
     * The default value of the '{@link #getInterpolationMethod() <em>Interpolation Method</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInterpolationMethod()
     * @generated
     * @ordered
     */
    protected static final String INTERPOLATION_METHOD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getInterpolationMethod() <em>Interpolation Method</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInterpolationMethod()
     * @generated
     * @ordered
     */
    protected String interpolationMethod = INTERPOLATION_METHOD_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected InterpolationMethodTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.INTERPOLATION_METHOD_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getInterpolationMethod() {
        return interpolationMethod;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInterpolationMethod(String newInterpolationMethod) {
        String oldInterpolationMethod = interpolationMethod;
        interpolationMethod = newInterpolationMethod;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.INTERPOLATION_METHOD_TYPE__INTERPOLATION_METHOD, oldInterpolationMethod, interpolationMethod));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.INTERPOLATION_METHOD_TYPE__INTERPOLATION_METHOD:
                return getInterpolationMethod();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Wcs20Package.INTERPOLATION_METHOD_TYPE__INTERPOLATION_METHOD:
                setInterpolationMethod((String)newValue);
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
            case Wcs20Package.INTERPOLATION_METHOD_TYPE__INTERPOLATION_METHOD:
                setInterpolationMethod(INTERPOLATION_METHOD_EDEFAULT);
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
            case Wcs20Package.INTERPOLATION_METHOD_TYPE__INTERPOLATION_METHOD:
                return INTERPOLATION_METHOD_EDEFAULT == null ? interpolationMethod != null : !INTERPOLATION_METHOD_EDEFAULT.equals(interpolationMethod);
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
        result.append(" (interpolationMethod: ");
        result.append(interpolationMethod);
        result.append(')');
        return result.toString();
    }

} //InterpolationMethodTypeImpl
