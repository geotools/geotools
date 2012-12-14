/**
 */
package net.opengis.wcs20.impl;

import java.util.Collection;

import net.opengis.wcs20.ScaleToSizeType;
import net.opengis.wcs20.TargetAxisSizeType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scale To Size Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.ScaleToSizeTypeImpl#getTargetAxisSize <em>Target Axis Size</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScaleToSizeTypeImpl extends EObjectImpl implements ScaleToSizeType {
    /**
     * The cached value of the '{@link #getTargetAxisSize() <em>Target Axis Size</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetAxisSize()
     * @generated
     * @ordered
     */
    protected EList<TargetAxisSizeType> targetAxisSize;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ScaleToSizeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.SCALE_TO_SIZE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TargetAxisSizeType> getTargetAxisSize() {
        if (targetAxisSize == null) {
            targetAxisSize = new EObjectResolvingEList<TargetAxisSizeType>(TargetAxisSizeType.class, this, Wcs20Package.SCALE_TO_SIZE_TYPE__TARGET_AXIS_SIZE);
        }
        return targetAxisSize;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.SCALE_TO_SIZE_TYPE__TARGET_AXIS_SIZE:
                return getTargetAxisSize();
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
            case Wcs20Package.SCALE_TO_SIZE_TYPE__TARGET_AXIS_SIZE:
                getTargetAxisSize().clear();
                getTargetAxisSize().addAll((Collection<? extends TargetAxisSizeType>)newValue);
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
            case Wcs20Package.SCALE_TO_SIZE_TYPE__TARGET_AXIS_SIZE:
                getTargetAxisSize().clear();
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
            case Wcs20Package.SCALE_TO_SIZE_TYPE__TARGET_AXIS_SIZE:
                return targetAxisSize != null && !targetAxisSize.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ScaleToSizeTypeImpl
