/**
 */
package net.opengis.wmts.v_1.impl;

import java.util.Collection;

import net.opengis.wmts.v_1.TileMatrixLimitsType;
import net.opengis.wmts.v_1.TileMatrixSetLimitsType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tile Matrix Set Limits Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixSetLimitsTypeImpl#getTileMatrixLimits <em>Tile Matrix Limits</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TileMatrixSetLimitsTypeImpl extends MinimalEObjectImpl.Container implements TileMatrixSetLimitsType {
    /**
     * The cached value of the '{@link #getTileMatrixLimits() <em>Tile Matrix Limits</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileMatrixLimits()
     * @generated
     * @ordered
     */
    protected EList<TileMatrixLimitsType> tileMatrixLimits;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TileMatrixSetLimitsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.TILE_MATRIX_SET_LIMITS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TileMatrixLimitsType> getTileMatrixLimits() {
        if (tileMatrixLimits == null) {
            tileMatrixLimits = new EObjectContainmentEList<TileMatrixLimitsType>(TileMatrixLimitsType.class, this, wmtsv_1Package.TILE_MATRIX_SET_LIMITS_TYPE__TILE_MATRIX_LIMITS);
        }
        return tileMatrixLimits;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wmtsv_1Package.TILE_MATRIX_SET_LIMITS_TYPE__TILE_MATRIX_LIMITS:
                return ((InternalEList<?>)getTileMatrixLimits()).basicRemove(otherEnd, msgs);
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
            case wmtsv_1Package.TILE_MATRIX_SET_LIMITS_TYPE__TILE_MATRIX_LIMITS:
                return getTileMatrixLimits();
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
            case wmtsv_1Package.TILE_MATRIX_SET_LIMITS_TYPE__TILE_MATRIX_LIMITS:
                getTileMatrixLimits().clear();
                getTileMatrixLimits().addAll((Collection<? extends TileMatrixLimitsType>)newValue);
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
            case wmtsv_1Package.TILE_MATRIX_SET_LIMITS_TYPE__TILE_MATRIX_LIMITS:
                getTileMatrixLimits().clear();
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
            case wmtsv_1Package.TILE_MATRIX_SET_LIMITS_TYPE__TILE_MATRIX_LIMITS:
                return tileMatrixLimits != null && !tileMatrixLimits.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //TileMatrixSetLimitsTypeImpl
