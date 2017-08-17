/**
 */
package net.opengis.wmts.v_1.impl;

import java.util.Collection;

import net.opengis.ows11.impl.ContentsBaseTypeImpl;

import net.opengis.wmts.v_1.ContentsType;
import net.opengis.wmts.v_1.TileMatrixSetType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Contents Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.ContentsTypeImpl#getTileMatrixSet <em>Tile Matrix Set</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ContentsTypeImpl extends ContentsBaseTypeImpl implements ContentsType {
    /**
     * The cached value of the '{@link #getTileMatrixSet() <em>Tile Matrix Set</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileMatrixSet()
     * @generated
     * @ordered
     */
    protected EList<TileMatrixSetType> tileMatrixSet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ContentsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.CONTENTS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TileMatrixSetType> getTileMatrixSet() {
        if (tileMatrixSet == null) {
            tileMatrixSet = new EObjectContainmentEList<TileMatrixSetType>(TileMatrixSetType.class, this, wmtsv_1Package.CONTENTS_TYPE__TILE_MATRIX_SET);
        }
        return tileMatrixSet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wmtsv_1Package.CONTENTS_TYPE__TILE_MATRIX_SET:
                return ((InternalEList<?>)getTileMatrixSet()).basicRemove(otherEnd, msgs);
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
            case wmtsv_1Package.CONTENTS_TYPE__TILE_MATRIX_SET:
                return getTileMatrixSet();
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
            case wmtsv_1Package.CONTENTS_TYPE__TILE_MATRIX_SET:
                getTileMatrixSet().clear();
                getTileMatrixSet().addAll((Collection<? extends TileMatrixSetType>)newValue);
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
            case wmtsv_1Package.CONTENTS_TYPE__TILE_MATRIX_SET:
                getTileMatrixSet().clear();
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
            case wmtsv_1Package.CONTENTS_TYPE__TILE_MATRIX_SET:
                return tileMatrixSet != null && !tileMatrixSet.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ContentsTypeImpl
