/**
 */
package net.opengis.wmts.v_1.impl;

import net.opengis.wmts.v_1.TileMatrixSetLimitsType;
import net.opengis.wmts.v_1.TileMatrixSetLinkType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tile Matrix Set Link Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixSetLinkTypeImpl#getTileMatrixSet <em>Tile Matrix Set</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixSetLinkTypeImpl#getTileMatrixSetLimits <em>Tile Matrix Set Limits</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TileMatrixSetLinkTypeImpl extends MinimalEObjectImpl.Container implements TileMatrixSetLinkType {
    /**
     * The default value of the '{@link #getTileMatrixSet() <em>Tile Matrix Set</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileMatrixSet()
     * @generated
     * @ordered
     */
    protected static final String TILE_MATRIX_SET_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTileMatrixSet() <em>Tile Matrix Set</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileMatrixSet()
     * @generated
     * @ordered
     */
    protected String tileMatrixSet = TILE_MATRIX_SET_EDEFAULT;

    /**
     * The cached value of the '{@link #getTileMatrixSetLimits() <em>Tile Matrix Set Limits</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileMatrixSetLimits()
     * @generated
     * @ordered
     */
    protected TileMatrixSetLimitsType tileMatrixSetLimits;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TileMatrixSetLinkTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.TILE_MATRIX_SET_LINK_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTileMatrixSet() {
        return tileMatrixSet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTileMatrixSet(String newTileMatrixSet) {
        String oldTileMatrixSet = tileMatrixSet;
        tileMatrixSet = newTileMatrixSet;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET, oldTileMatrixSet, tileMatrixSet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TileMatrixSetLimitsType getTileMatrixSetLimits() {
        return tileMatrixSetLimits;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTileMatrixSetLimits(TileMatrixSetLimitsType newTileMatrixSetLimits, NotificationChain msgs) {
        TileMatrixSetLimitsType oldTileMatrixSetLimits = tileMatrixSetLimits;
        tileMatrixSetLimits = newTileMatrixSetLimits;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET_LIMITS, oldTileMatrixSetLimits, newTileMatrixSetLimits);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTileMatrixSetLimits(TileMatrixSetLimitsType newTileMatrixSetLimits) {
        if (newTileMatrixSetLimits != tileMatrixSetLimits) {
            NotificationChain msgs = null;
            if (tileMatrixSetLimits != null)
                msgs = ((InternalEObject)tileMatrixSetLimits).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET_LIMITS, null, msgs);
            if (newTileMatrixSetLimits != null)
                msgs = ((InternalEObject)newTileMatrixSetLimits).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET_LIMITS, null, msgs);
            msgs = basicSetTileMatrixSetLimits(newTileMatrixSetLimits, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET_LIMITS, newTileMatrixSetLimits, newTileMatrixSetLimits));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wmtsv_1Package.TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET_LIMITS:
                return basicSetTileMatrixSetLimits(null, msgs);
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
            case wmtsv_1Package.TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET:
                return getTileMatrixSet();
            case wmtsv_1Package.TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET_LIMITS:
                return getTileMatrixSetLimits();
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
            case wmtsv_1Package.TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET:
                setTileMatrixSet((String)newValue);
                return;
            case wmtsv_1Package.TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET_LIMITS:
                setTileMatrixSetLimits((TileMatrixSetLimitsType)newValue);
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
            case wmtsv_1Package.TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET:
                setTileMatrixSet(TILE_MATRIX_SET_EDEFAULT);
                return;
            case wmtsv_1Package.TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET_LIMITS:
                setTileMatrixSetLimits((TileMatrixSetLimitsType)null);
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
            case wmtsv_1Package.TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET:
                return TILE_MATRIX_SET_EDEFAULT == null ? tileMatrixSet != null : !TILE_MATRIX_SET_EDEFAULT.equals(tileMatrixSet);
            case wmtsv_1Package.TILE_MATRIX_SET_LINK_TYPE__TILE_MATRIX_SET_LIMITS:
                return tileMatrixSetLimits != null;
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
        result.append(" (tileMatrixSet: ");
        result.append(tileMatrixSet);
        result.append(')');
        return result.toString();
    }

} //TileMatrixSetLinkTypeImpl
