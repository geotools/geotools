/**
 */
package net.opengis.wcs20.impl;

import net.opengis.wcs20.TargetAxisExtentType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Target Axis Extent Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.TargetAxisExtentTypeImpl#getAxis <em>Axis</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.TargetAxisExtentTypeImpl#getLow <em>Low</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.TargetAxisExtentTypeImpl#getHigh <em>High</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TargetAxisExtentTypeImpl extends EObjectImpl implements TargetAxisExtentType {
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
     * The default value of the '{@link #getLow() <em>Low</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLow()
     * @generated
     * @ordered
     */
    protected static final double LOW_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getLow() <em>Low</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLow()
     * @generated
     * @ordered
     */
    protected double low = LOW_EDEFAULT;

    /**
     * The default value of the '{@link #getHigh() <em>High</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHigh()
     * @generated
     * @ordered
     */
    protected static final double HIGH_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getHigh() <em>High</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHigh()
     * @generated
     * @ordered
     */
    protected double high = HIGH_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TargetAxisExtentTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.TARGET_AXIS_EXTENT_TYPE;
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.TARGET_AXIS_EXTENT_TYPE__AXIS, oldAxis, axis));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getLow() {
        return low;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLow(double newLow) {
        double oldLow = low;
        low = newLow;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.TARGET_AXIS_EXTENT_TYPE__LOW, oldLow, low));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getHigh() {
        return high;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHigh(double newHigh) {
        double oldHigh = high;
        high = newHigh;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.TARGET_AXIS_EXTENT_TYPE__HIGH, oldHigh, high));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.TARGET_AXIS_EXTENT_TYPE__AXIS:
                return getAxis();
            case Wcs20Package.TARGET_AXIS_EXTENT_TYPE__LOW:
                return getLow();
            case Wcs20Package.TARGET_AXIS_EXTENT_TYPE__HIGH:
                return getHigh();
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
            case Wcs20Package.TARGET_AXIS_EXTENT_TYPE__AXIS:
                setAxis((String)newValue);
                return;
            case Wcs20Package.TARGET_AXIS_EXTENT_TYPE__LOW:
                setLow((Double)newValue);
                return;
            case Wcs20Package.TARGET_AXIS_EXTENT_TYPE__HIGH:
                setHigh((Double)newValue);
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
            case Wcs20Package.TARGET_AXIS_EXTENT_TYPE__AXIS:
                setAxis(AXIS_EDEFAULT);
                return;
            case Wcs20Package.TARGET_AXIS_EXTENT_TYPE__LOW:
                setLow(LOW_EDEFAULT);
                return;
            case Wcs20Package.TARGET_AXIS_EXTENT_TYPE__HIGH:
                setHigh(HIGH_EDEFAULT);
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
            case Wcs20Package.TARGET_AXIS_EXTENT_TYPE__AXIS:
                return AXIS_EDEFAULT == null ? axis != null : !AXIS_EDEFAULT.equals(axis);
            case Wcs20Package.TARGET_AXIS_EXTENT_TYPE__LOW:
                return low != LOW_EDEFAULT;
            case Wcs20Package.TARGET_AXIS_EXTENT_TYPE__HIGH:
                return high != HIGH_EDEFAULT;
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
        result.append(", low: ");
        result.append(low);
        result.append(", high: ");
        result.append(high);
        result.append(')');
        return result.toString();
    }

} //TargetAxisExtentTypeImpl
