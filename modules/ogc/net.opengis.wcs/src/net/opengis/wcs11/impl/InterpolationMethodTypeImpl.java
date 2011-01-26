/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import net.opengis.wcs11.InterpolationMethodType;
import net.opengis.wcs11.Wcs111Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Interpolation Method Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.InterpolationMethodTypeImpl#getNullResistance <em>Null Resistance</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InterpolationMethodTypeImpl extends InterpolationMethodBaseTypeImpl implements InterpolationMethodType {
    /**
     * The default value of the '{@link #getNullResistance() <em>Null Resistance</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNullResistance()
     * @generated
     * @ordered
     */
    protected static final String NULL_RESISTANCE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getNullResistance() <em>Null Resistance</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNullResistance()
     * @generated
     * @ordered
     */
    protected String nullResistance = NULL_RESISTANCE_EDEFAULT;

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
    protected EClass eStaticClass() {
        return Wcs111Package.Literals.INTERPOLATION_METHOD_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getNullResistance() {
        return nullResistance;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNullResistance(String newNullResistance) {
        String oldNullResistance = nullResistance;
        nullResistance = newNullResistance;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.INTERPOLATION_METHOD_TYPE__NULL_RESISTANCE, oldNullResistance, nullResistance));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs111Package.INTERPOLATION_METHOD_TYPE__NULL_RESISTANCE:
                return getNullResistance();
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
            case Wcs111Package.INTERPOLATION_METHOD_TYPE__NULL_RESISTANCE:
                setNullResistance((String)newValue);
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
            case Wcs111Package.INTERPOLATION_METHOD_TYPE__NULL_RESISTANCE:
                setNullResistance(NULL_RESISTANCE_EDEFAULT);
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
            case Wcs111Package.INTERPOLATION_METHOD_TYPE__NULL_RESISTANCE:
                return NULL_RESISTANCE_EDEFAULT == null ? nullResistance != null : !NULL_RESISTANCE_EDEFAULT.equals(nullResistance);
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
        result.append(" (nullResistance: ");
        result.append(nullResistance);
        result.append(')');
        return result.toString();
    }

} //InterpolationMethodTypeImpl
