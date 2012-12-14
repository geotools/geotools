/**
 */
package net.opengis.wcs20.impl;

import java.util.Collection;

import net.opengis.wcs20.ScaleToExtentType;
import net.opengis.wcs20.TargetAxisExtentType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scale To Extent Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.ScaleToExtentTypeImpl#getTargetAxisExtent <em>Target Axis Extent</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScaleToExtentTypeImpl extends EObjectImpl implements ScaleToExtentType {
    /**
     * The cached value of the '{@link #getTargetAxisExtent() <em>Target Axis Extent</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetAxisExtent()
     * @generated
     * @ordered
     */
    protected EList<TargetAxisExtentType> targetAxisExtent;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ScaleToExtentTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.SCALE_TO_EXTENT_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TargetAxisExtentType> getTargetAxisExtent() {
        if (targetAxisExtent == null) {
            targetAxisExtent = new EObjectResolvingEList<TargetAxisExtentType>(TargetAxisExtentType.class, this, Wcs20Package.SCALE_TO_EXTENT_TYPE__TARGET_AXIS_EXTENT);
        }
        return targetAxisExtent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.SCALE_TO_EXTENT_TYPE__TARGET_AXIS_EXTENT:
                return getTargetAxisExtent();
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
            case Wcs20Package.SCALE_TO_EXTENT_TYPE__TARGET_AXIS_EXTENT:
                getTargetAxisExtent().clear();
                getTargetAxisExtent().addAll((Collection<? extends TargetAxisExtentType>)newValue);
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
            case Wcs20Package.SCALE_TO_EXTENT_TYPE__TARGET_AXIS_EXTENT:
                getTargetAxisExtent().clear();
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
            case Wcs20Package.SCALE_TO_EXTENT_TYPE__TARGET_AXIS_EXTENT:
                return targetAxisExtent != null && !targetAxisExtent.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ScaleToExtentTypeImpl
