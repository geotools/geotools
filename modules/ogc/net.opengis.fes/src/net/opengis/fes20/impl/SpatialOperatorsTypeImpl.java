/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import java.util.Collection;

import net.opengis.fes20.Fes20Package;
import net.opengis.fes20.SpatialOperatorType;
import net.opengis.fes20.SpatialOperatorsType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spatial Operators Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.SpatialOperatorsTypeImpl#getSpatialOperator <em>Spatial Operator</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SpatialOperatorsTypeImpl extends EObjectImpl implements SpatialOperatorsType {
    /**
     * The cached value of the '{@link #getSpatialOperator() <em>Spatial Operator</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSpatialOperator()
     * @generated
     * @ordered
     */
    protected EList<SpatialOperatorType> spatialOperator;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SpatialOperatorsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.SPATIAL_OPERATORS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SpatialOperatorType> getSpatialOperator() {
        if (spatialOperator == null) {
            spatialOperator = new EObjectContainmentEList<SpatialOperatorType>(SpatialOperatorType.class, this, Fes20Package.SPATIAL_OPERATORS_TYPE__SPATIAL_OPERATOR);
        }
        return spatialOperator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Fes20Package.SPATIAL_OPERATORS_TYPE__SPATIAL_OPERATOR:
                return ((InternalEList<?>)getSpatialOperator()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Fes20Package.SPATIAL_OPERATORS_TYPE__SPATIAL_OPERATOR:
                return getSpatialOperator();
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
            case Fes20Package.SPATIAL_OPERATORS_TYPE__SPATIAL_OPERATOR:
                getSpatialOperator().clear();
                getSpatialOperator().addAll((Collection<? extends SpatialOperatorType>)newValue);
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
            case Fes20Package.SPATIAL_OPERATORS_TYPE__SPATIAL_OPERATOR:
                getSpatialOperator().clear();
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
            case Fes20Package.SPATIAL_OPERATORS_TYPE__SPATIAL_OPERATOR:
                return spatialOperator != null && !spatialOperator.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //SpatialOperatorsTypeImpl
