/**
 */
package net.opengis.wcs20.impl;

import java.util.Collection;

import net.opengis.wcs20.InterpolationAxesType;
import net.opengis.wcs20.InterpolationAxisType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Interpolation Axes Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.InterpolationAxesTypeImpl#getInterpolationAxis <em>Interpolation Axis</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InterpolationAxesTypeImpl extends EObjectImpl implements InterpolationAxesType {
    /**
     * The cached value of the '{@link #getInterpolationAxis() <em>Interpolation Axis</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInterpolationAxis()
     * @generated
     * @ordered
     */
    protected EList<InterpolationAxisType> interpolationAxis;
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected InterpolationAxesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.INTERPOLATION_AXES_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<InterpolationAxisType> getInterpolationAxis() {
        if (interpolationAxis == null) {
            interpolationAxis = new EObjectResolvingEList<InterpolationAxisType>(InterpolationAxisType.class, this, Wcs20Package.INTERPOLATION_AXES_TYPE__INTERPOLATION_AXIS);
        }
        return interpolationAxis;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.INTERPOLATION_AXES_TYPE__INTERPOLATION_AXIS:
                return getInterpolationAxis();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Wcs20Package.INTERPOLATION_AXES_TYPE__INTERPOLATION_AXIS:
                getInterpolationAxis().clear();
                getInterpolationAxis().addAll((Collection<? extends InterpolationAxisType>)newValue);
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
            case Wcs20Package.INTERPOLATION_AXES_TYPE__INTERPOLATION_AXIS:
                getInterpolationAxis().clear();
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
            case Wcs20Package.INTERPOLATION_AXES_TYPE__INTERPOLATION_AXIS:
                return interpolationAxis != null && !interpolationAxis.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //InterpolationAxesTypeImpl
