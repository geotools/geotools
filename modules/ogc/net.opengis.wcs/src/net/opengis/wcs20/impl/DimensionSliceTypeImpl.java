/**
 */
package net.opengis.wcs20.impl;

import net.opengis.wcs20.DimensionSliceType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dimension Slice Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.DimensionSliceTypeImpl#getSlicePoint <em>Slice Point</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DimensionSliceTypeImpl extends DimensionSubsetTypeImpl implements DimensionSliceType {
    /**
     * The default value of the '{@link #getSlicePoint() <em>Slice Point</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSlicePoint()
     * @generated
     * @ordered
     */
    protected static final String SLICE_POINT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSlicePoint() <em>Slice Point</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSlicePoint()
     * @generated
     * @ordered
     */
    protected String slicePoint = SLICE_POINT_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DimensionSliceTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.DIMENSION_SLICE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSlicePoint() {
        return slicePoint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSlicePoint(String newSlicePoint) {
        String oldSlicePoint = slicePoint;
        slicePoint = newSlicePoint;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.DIMENSION_SLICE_TYPE__SLICE_POINT, oldSlicePoint, slicePoint));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.DIMENSION_SLICE_TYPE__SLICE_POINT:
                return getSlicePoint();
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
            case Wcs20Package.DIMENSION_SLICE_TYPE__SLICE_POINT:
                setSlicePoint((String)newValue);
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
            case Wcs20Package.DIMENSION_SLICE_TYPE__SLICE_POINT:
                setSlicePoint(SLICE_POINT_EDEFAULT);
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
            case Wcs20Package.DIMENSION_SLICE_TYPE__SLICE_POINT:
                return SLICE_POINT_EDEFAULT == null ? slicePoint != null : !SLICE_POINT_EDEFAULT.equals(slicePoint);
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
        result.append(" (slicePoint: ");
        result.append(slicePoint);
        result.append(')');
        return result.toString();
    }

} //DimensionSliceTypeImpl
