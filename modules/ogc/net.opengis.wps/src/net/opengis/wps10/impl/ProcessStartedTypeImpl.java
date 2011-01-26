/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import java.math.BigInteger;

import net.opengis.wps10.ProcessStartedType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Process Started Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.ProcessStartedTypeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.ProcessStartedTypeImpl#getPercentCompleted <em>Percent Completed</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProcessStartedTypeImpl extends EObjectImpl implements ProcessStartedType {
    /**
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final String VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected String value = VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getPercentCompleted() <em>Percent Completed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPercentCompleted()
     * @generated
     * @ordered
     */
    protected static final BigInteger PERCENT_COMPLETED_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPercentCompleted() <em>Percent Completed</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPercentCompleted()
     * @generated
     * @ordered
     */
    protected BigInteger percentCompleted = PERCENT_COMPLETED_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ProcessStartedTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.PROCESS_STARTED_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValue(String newValue) {
        String oldValue = value;
        value = newValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.PROCESS_STARTED_TYPE__VALUE, oldValue, value));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getPercentCompleted() {
        return percentCompleted;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPercentCompleted(BigInteger newPercentCompleted) {
        BigInteger oldPercentCompleted = percentCompleted;
        percentCompleted = newPercentCompleted;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.PROCESS_STARTED_TYPE__PERCENT_COMPLETED, oldPercentCompleted, percentCompleted));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wps10Package.PROCESS_STARTED_TYPE__VALUE:
                return getValue();
            case Wps10Package.PROCESS_STARTED_TYPE__PERCENT_COMPLETED:
                return getPercentCompleted();
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
            case Wps10Package.PROCESS_STARTED_TYPE__VALUE:
                setValue((String)newValue);
                return;
            case Wps10Package.PROCESS_STARTED_TYPE__PERCENT_COMPLETED:
                setPercentCompleted((BigInteger)newValue);
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
            case Wps10Package.PROCESS_STARTED_TYPE__VALUE:
                setValue(VALUE_EDEFAULT);
                return;
            case Wps10Package.PROCESS_STARTED_TYPE__PERCENT_COMPLETED:
                setPercentCompleted(PERCENT_COMPLETED_EDEFAULT);
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
            case Wps10Package.PROCESS_STARTED_TYPE__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
            case Wps10Package.PROCESS_STARTED_TYPE__PERCENT_COMPLETED:
                return PERCENT_COMPLETED_EDEFAULT == null ? percentCompleted != null : !PERCENT_COMPLETED_EDEFAULT.equals(percentCompleted);
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
        result.append(" (value: ");
        result.append(value);
        result.append(", percentCompleted: ");
        result.append(percentCompleted);
        result.append(')');
        return result.toString();
    }

} //ProcessStartedTypeImpl
