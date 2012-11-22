/**
 */
package net.opengis.wcs20.impl;

import net.opengis.wcs20.DimensionTrimType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dimension Trim Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.DimensionTrimTypeImpl#getTrimLow <em>Trim Low</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.DimensionTrimTypeImpl#getTrimHigh <em>Trim High</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DimensionTrimTypeImpl extends DimensionSubsetTypeImpl implements DimensionTrimType {
    /**
     * The default value of the '{@link #getTrimLow() <em>Trim Low</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTrimLow()
     * @generated
     * @ordered
     */
    protected static final String TRIM_LOW_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTrimLow() <em>Trim Low</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTrimLow()
     * @generated
     * @ordered
     */
    protected String trimLow = TRIM_LOW_EDEFAULT;

    /**
     * The default value of the '{@link #getTrimHigh() <em>Trim High</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTrimHigh()
     * @generated
     * @ordered
     */
    protected static final String TRIM_HIGH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTrimHigh() <em>Trim High</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTrimHigh()
     * @generated
     * @ordered
     */
    protected String trimHigh = TRIM_HIGH_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DimensionTrimTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.DIMENSION_TRIM_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTrimLow() {
        return trimLow;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTrimLow(String newTrimLow) {
        String oldTrimLow = trimLow;
        trimLow = newTrimLow;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.DIMENSION_TRIM_TYPE__TRIM_LOW, oldTrimLow, trimLow));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTrimHigh() {
        return trimHigh;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTrimHigh(String newTrimHigh) {
        String oldTrimHigh = trimHigh;
        trimHigh = newTrimHigh;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.DIMENSION_TRIM_TYPE__TRIM_HIGH, oldTrimHigh, trimHigh));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.DIMENSION_TRIM_TYPE__TRIM_LOW:
                return getTrimLow();
            case Wcs20Package.DIMENSION_TRIM_TYPE__TRIM_HIGH:
                return getTrimHigh();
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
            case Wcs20Package.DIMENSION_TRIM_TYPE__TRIM_LOW:
                setTrimLow((String)newValue);
                return;
            case Wcs20Package.DIMENSION_TRIM_TYPE__TRIM_HIGH:
                setTrimHigh((String)newValue);
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
            case Wcs20Package.DIMENSION_TRIM_TYPE__TRIM_LOW:
                setTrimLow(TRIM_LOW_EDEFAULT);
                return;
            case Wcs20Package.DIMENSION_TRIM_TYPE__TRIM_HIGH:
                setTrimHigh(TRIM_HIGH_EDEFAULT);
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
            case Wcs20Package.DIMENSION_TRIM_TYPE__TRIM_LOW:
                return TRIM_LOW_EDEFAULT == null ? trimLow != null : !TRIM_LOW_EDEFAULT.equals(trimLow);
            case Wcs20Package.DIMENSION_TRIM_TYPE__TRIM_HIGH:
                return TRIM_HIGH_EDEFAULT == null ? trimHigh != null : !TRIM_HIGH_EDEFAULT.equals(trimHigh);
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
        result.append(" (trimLow: ");
        result.append(trimLow);
        result.append(", trimHigh: ");
        result.append(trimHigh);
        result.append(')');
        return result.toString();
    }

} //DimensionTrimTypeImpl
