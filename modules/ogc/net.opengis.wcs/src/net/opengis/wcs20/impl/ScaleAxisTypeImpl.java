/**
 */
package net.opengis.wcs20.impl;

import net.opengis.wcs20.ScaleAxisType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scale Axis Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.ScaleAxisTypeImpl#getAxis <em>Axis</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.ScaleAxisTypeImpl#getScaleFactor <em>Scale Factor</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScaleAxisTypeImpl extends EObjectImpl implements ScaleAxisType {
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
     * The default value of the '{@link #getScaleFactor() <em>Scale Factor</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getScaleFactor()
     * @generated
     * @ordered
     */
    protected static final double SCALE_FACTOR_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getScaleFactor() <em>Scale Factor</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getScaleFactor()
     * @generated
     * @ordered
     */
    protected double scaleFactor = SCALE_FACTOR_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ScaleAxisTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.SCALE_AXIS_TYPE;
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.SCALE_AXIS_TYPE__AXIS, oldAxis, axis));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getScaleFactor() {
        return scaleFactor;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setScaleFactor(double newScaleFactor) {
        double oldScaleFactor = scaleFactor;
        scaleFactor = newScaleFactor;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.SCALE_AXIS_TYPE__SCALE_FACTOR, oldScaleFactor, scaleFactor));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.SCALE_AXIS_TYPE__AXIS:
                return getAxis();
            case Wcs20Package.SCALE_AXIS_TYPE__SCALE_FACTOR:
                return getScaleFactor();
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
            case Wcs20Package.SCALE_AXIS_TYPE__AXIS:
                setAxis((String)newValue);
                return;
            case Wcs20Package.SCALE_AXIS_TYPE__SCALE_FACTOR:
                setScaleFactor((Double)newValue);
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
            case Wcs20Package.SCALE_AXIS_TYPE__AXIS:
                setAxis(AXIS_EDEFAULT);
                return;
            case Wcs20Package.SCALE_AXIS_TYPE__SCALE_FACTOR:
                setScaleFactor(SCALE_FACTOR_EDEFAULT);
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
            case Wcs20Package.SCALE_AXIS_TYPE__AXIS:
                return AXIS_EDEFAULT == null ? axis != null : !AXIS_EDEFAULT.equals(axis);
            case Wcs20Package.SCALE_AXIS_TYPE__SCALE_FACTOR:
                return scaleFactor != SCALE_FACTOR_EDEFAULT;
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
        result.append(", scaleFactor: ");
        result.append(scaleFactor);
        result.append(')');
        return result.toString();
    }

} //ScaleAxisTypeImpl
