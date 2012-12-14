/**
 */
package net.opengis.wcs20.impl;

import net.opengis.wcs20.TargetAxisSizeType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Target Axis Size Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.TargetAxisSizeTypeImpl#getAxis <em>Axis</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.TargetAxisSizeTypeImpl#getTargetSize <em>Target Size</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TargetAxisSizeTypeImpl extends EObjectImpl implements TargetAxisSizeType {
    /**
     * The default value of the '{@link #getAxis() <em>Axis</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAxis()
     * @generated
     * @ordered
     */
    protected static final String AXIS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAxis() <em>Axis</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAxis()
     * @generated
     * @ordered
     */
    protected String axis = AXIS_EDEFAULT;

    /**
     * The default value of the '{@link #getTargetSize() <em>Target Size</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetSize()
     * @generated
     * @ordered
     */
    protected static final double TARGET_SIZE_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getTargetSize() <em>Target Size</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetSize()
     * @generated
     * @ordered
     */
    protected double targetSize = TARGET_SIZE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TargetAxisSizeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.TARGET_AXIS_SIZE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getAxis() {
        return axis;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAxis(String newAxis) {
        String oldAxis = axis;
        axis = newAxis;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.TARGET_AXIS_SIZE_TYPE__AXIS, oldAxis, axis));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getTargetSize() {
        return targetSize;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTargetSize(double newTargetSize) {
        double oldTargetSize = targetSize;
        targetSize = newTargetSize;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.TARGET_AXIS_SIZE_TYPE__TARGET_SIZE, oldTargetSize, targetSize));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.TARGET_AXIS_SIZE_TYPE__AXIS:
                return getAxis();
            case Wcs20Package.TARGET_AXIS_SIZE_TYPE__TARGET_SIZE:
                return getTargetSize();
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
            case Wcs20Package.TARGET_AXIS_SIZE_TYPE__AXIS:
                setAxis((String)newValue);
                return;
            case Wcs20Package.TARGET_AXIS_SIZE_TYPE__TARGET_SIZE:
                setTargetSize((Double)newValue);
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
            case Wcs20Package.TARGET_AXIS_SIZE_TYPE__AXIS:
                setAxis(AXIS_EDEFAULT);
                return;
            case Wcs20Package.TARGET_AXIS_SIZE_TYPE__TARGET_SIZE:
                setTargetSize(TARGET_SIZE_EDEFAULT);
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
            case Wcs20Package.TARGET_AXIS_SIZE_TYPE__AXIS:
                return AXIS_EDEFAULT == null ? axis != null : !AXIS_EDEFAULT.equals(axis);
            case Wcs20Package.TARGET_AXIS_SIZE_TYPE__TARGET_SIZE:
                return targetSize != TARGET_SIZE_EDEFAULT;
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
        result.append(" (axis: ");
        result.append(axis);
        result.append(", targetSize: ");
        result.append(targetSize);
        result.append(')');
        return result.toString();
    }

} //TargetAxisSizeTypeImpl
