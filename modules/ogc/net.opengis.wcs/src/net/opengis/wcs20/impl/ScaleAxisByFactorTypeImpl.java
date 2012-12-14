/**
 */
package net.opengis.wcs20.impl;

import java.util.Collection;

import net.opengis.wcs20.ScaleAxisByFactorType;
import net.opengis.wcs20.ScaleAxisType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scale Axis By Factor Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.ScaleAxisByFactorTypeImpl#getScaleAxis <em>Scale Axis</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScaleAxisByFactorTypeImpl extends EObjectImpl implements ScaleAxisByFactorType {
    /**
     * The cached value of the '{@link #getScaleAxis() <em>Scale Axis</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getScaleAxis()
     * @generated
     * @ordered
     */
    protected EList<ScaleAxisType> scaleAxis;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ScaleAxisByFactorTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.SCALE_AXIS_BY_FACTOR_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ScaleAxisType> getScaleAxis() {
        if (scaleAxis == null) {
            scaleAxis = new EObjectResolvingEList<ScaleAxisType>(ScaleAxisType.class, this, Wcs20Package.SCALE_AXIS_BY_FACTOR_TYPE__SCALE_AXIS);
        }
        return scaleAxis;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.SCALE_AXIS_BY_FACTOR_TYPE__SCALE_AXIS:
                return getScaleAxis();
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
            case Wcs20Package.SCALE_AXIS_BY_FACTOR_TYPE__SCALE_AXIS:
                getScaleAxis().clear();
                getScaleAxis().addAll((Collection<? extends ScaleAxisType>)newValue);
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
            case Wcs20Package.SCALE_AXIS_BY_FACTOR_TYPE__SCALE_AXIS:
                getScaleAxis().clear();
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
            case Wcs20Package.SCALE_AXIS_BY_FACTOR_TYPE__SCALE_AXIS:
                return scaleAxis != null && !scaleAxis.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ScaleAxisByFactorTypeImpl
