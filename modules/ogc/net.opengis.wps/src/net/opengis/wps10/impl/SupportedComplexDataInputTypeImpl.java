/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import java.math.BigInteger;

import net.opengis.wps10.SupportedComplexDataInputType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Supported Complex Data Input Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.SupportedComplexDataInputTypeImpl#getMaximumMegabytes <em>Maximum Megabytes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SupportedComplexDataInputTypeImpl extends SupportedComplexDataTypeImpl implements SupportedComplexDataInputType {
    /**
     * The default value of the '{@link #getMaximumMegabytes() <em>Maximum Megabytes</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaximumMegabytes()
     * @generated
     * @ordered
     */
    protected static final BigInteger MAXIMUM_MEGABYTES_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMaximumMegabytes() <em>Maximum Megabytes</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaximumMegabytes()
     * @generated
     * @ordered
     */
    protected BigInteger maximumMegabytes = MAXIMUM_MEGABYTES_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SupportedComplexDataInputTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.SUPPORTED_COMPLEX_DATA_INPUT_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getMaximumMegabytes() {
        return maximumMegabytes;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMaximumMegabytes(BigInteger newMaximumMegabytes) {
        BigInteger oldMaximumMegabytes = maximumMegabytes;
        maximumMegabytes = newMaximumMegabytes;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.SUPPORTED_COMPLEX_DATA_INPUT_TYPE__MAXIMUM_MEGABYTES, oldMaximumMegabytes, maximumMegabytes));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wps10Package.SUPPORTED_COMPLEX_DATA_INPUT_TYPE__MAXIMUM_MEGABYTES:
                return getMaximumMegabytes();
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
            case Wps10Package.SUPPORTED_COMPLEX_DATA_INPUT_TYPE__MAXIMUM_MEGABYTES:
                setMaximumMegabytes((BigInteger)newValue);
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
            case Wps10Package.SUPPORTED_COMPLEX_DATA_INPUT_TYPE__MAXIMUM_MEGABYTES:
                setMaximumMegabytes(MAXIMUM_MEGABYTES_EDEFAULT);
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
            case Wps10Package.SUPPORTED_COMPLEX_DATA_INPUT_TYPE__MAXIMUM_MEGABYTES:
                return MAXIMUM_MEGABYTES_EDEFAULT == null ? maximumMegabytes != null : !MAXIMUM_MEGABYTES_EDEFAULT.equals(maximumMegabytes);
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
        result.append(" (maximumMegabytes: ");
        result.append(maximumMegabytes);
        result.append(')');
        return result.toString();
    }

} //SupportedComplexDataInputTypeImpl
