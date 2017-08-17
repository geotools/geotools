/**
 */
package net.opengis.wmts.v_1.impl;

import java.math.BigInteger;

import net.opengis.wmts.v_1.TileMatrixLimitsType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tile Matrix Limits Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixLimitsTypeImpl#getTileMatrix <em>Tile Matrix</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixLimitsTypeImpl#getMinTileRow <em>Min Tile Row</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixLimitsTypeImpl#getMaxTileRow <em>Max Tile Row</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixLimitsTypeImpl#getMinTileCol <em>Min Tile Col</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixLimitsTypeImpl#getMaxTileCol <em>Max Tile Col</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TileMatrixLimitsTypeImpl extends MinimalEObjectImpl.Container implements TileMatrixLimitsType {
    /**
     * The default value of the '{@link #getTileMatrix() <em>Tile Matrix</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileMatrix()
     * @generated
     * @ordered
     */
    protected static final String TILE_MATRIX_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTileMatrix() <em>Tile Matrix</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileMatrix()
     * @generated
     * @ordered
     */
    protected String tileMatrix = TILE_MATRIX_EDEFAULT;

    /**
     * The default value of the '{@link #getMinTileRow() <em>Min Tile Row</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinTileRow()
     * @generated
     * @ordered
     */
    protected static final BigInteger MIN_TILE_ROW_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMinTileRow() <em>Min Tile Row</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinTileRow()
     * @generated
     * @ordered
     */
    protected BigInteger minTileRow = MIN_TILE_ROW_EDEFAULT;

    /**
     * The default value of the '{@link #getMaxTileRow() <em>Max Tile Row</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaxTileRow()
     * @generated
     * @ordered
     */
    protected static final BigInteger MAX_TILE_ROW_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMaxTileRow() <em>Max Tile Row</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaxTileRow()
     * @generated
     * @ordered
     */
    protected BigInteger maxTileRow = MAX_TILE_ROW_EDEFAULT;

    /**
     * The default value of the '{@link #getMinTileCol() <em>Min Tile Col</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinTileCol()
     * @generated
     * @ordered
     */
    protected static final BigInteger MIN_TILE_COL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMinTileCol() <em>Min Tile Col</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinTileCol()
     * @generated
     * @ordered
     */
    protected BigInteger minTileCol = MIN_TILE_COL_EDEFAULT;

    /**
     * The default value of the '{@link #getMaxTileCol() <em>Max Tile Col</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaxTileCol()
     * @generated
     * @ordered
     */
    protected static final BigInteger MAX_TILE_COL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMaxTileCol() <em>Max Tile Col</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaxTileCol()
     * @generated
     * @ordered
     */
    protected BigInteger maxTileCol = MAX_TILE_COL_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TileMatrixLimitsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.TILE_MATRIX_LIMITS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTileMatrix() {
        return tileMatrix;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTileMatrix(String newTileMatrix) {
        String oldTileMatrix = tileMatrix;
        tileMatrix = newTileMatrix;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__TILE_MATRIX, oldTileMatrix, tileMatrix));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getMinTileRow() {
        return minTileRow;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMinTileRow(BigInteger newMinTileRow) {
        BigInteger oldMinTileRow = minTileRow;
        minTileRow = newMinTileRow;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MIN_TILE_ROW, oldMinTileRow, minTileRow));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getMaxTileRow() {
        return maxTileRow;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMaxTileRow(BigInteger newMaxTileRow) {
        BigInteger oldMaxTileRow = maxTileRow;
        maxTileRow = newMaxTileRow;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MAX_TILE_ROW, oldMaxTileRow, maxTileRow));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getMinTileCol() {
        return minTileCol;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMinTileCol(BigInteger newMinTileCol) {
        BigInteger oldMinTileCol = minTileCol;
        minTileCol = newMinTileCol;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MIN_TILE_COL, oldMinTileCol, minTileCol));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getMaxTileCol() {
        return maxTileCol;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMaxTileCol(BigInteger newMaxTileCol) {
        BigInteger oldMaxTileCol = maxTileCol;
        maxTileCol = newMaxTileCol;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MAX_TILE_COL, oldMaxTileCol, maxTileCol));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__TILE_MATRIX:
                return getTileMatrix();
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MIN_TILE_ROW:
                return getMinTileRow();
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MAX_TILE_ROW:
                return getMaxTileRow();
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MIN_TILE_COL:
                return getMinTileCol();
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MAX_TILE_COL:
                return getMaxTileCol();
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
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__TILE_MATRIX:
                setTileMatrix((String)newValue);
                return;
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MIN_TILE_ROW:
                setMinTileRow((BigInteger)newValue);
                return;
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MAX_TILE_ROW:
                setMaxTileRow((BigInteger)newValue);
                return;
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MIN_TILE_COL:
                setMinTileCol((BigInteger)newValue);
                return;
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MAX_TILE_COL:
                setMaxTileCol((BigInteger)newValue);
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
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__TILE_MATRIX:
                setTileMatrix(TILE_MATRIX_EDEFAULT);
                return;
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MIN_TILE_ROW:
                setMinTileRow(MIN_TILE_ROW_EDEFAULT);
                return;
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MAX_TILE_ROW:
                setMaxTileRow(MAX_TILE_ROW_EDEFAULT);
                return;
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MIN_TILE_COL:
                setMinTileCol(MIN_TILE_COL_EDEFAULT);
                return;
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MAX_TILE_COL:
                setMaxTileCol(MAX_TILE_COL_EDEFAULT);
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
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__TILE_MATRIX:
                return TILE_MATRIX_EDEFAULT == null ? tileMatrix != null : !TILE_MATRIX_EDEFAULT.equals(tileMatrix);
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MIN_TILE_ROW:
                return MIN_TILE_ROW_EDEFAULT == null ? minTileRow != null : !MIN_TILE_ROW_EDEFAULT.equals(minTileRow);
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MAX_TILE_ROW:
                return MAX_TILE_ROW_EDEFAULT == null ? maxTileRow != null : !MAX_TILE_ROW_EDEFAULT.equals(maxTileRow);
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MIN_TILE_COL:
                return MIN_TILE_COL_EDEFAULT == null ? minTileCol != null : !MIN_TILE_COL_EDEFAULT.equals(minTileCol);
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE__MAX_TILE_COL:
                return MAX_TILE_COL_EDEFAULT == null ? maxTileCol != null : !MAX_TILE_COL_EDEFAULT.equals(maxTileCol);
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
        result.append(" (tileMatrix: ");
        result.append(tileMatrix);
        result.append(", minTileRow: ");
        result.append(minTileRow);
        result.append(", maxTileRow: ");
        result.append(maxTileRow);
        result.append(", minTileCol: ");
        result.append(minTileCol);
        result.append(", maxTileCol: ");
        result.append(maxTileCol);
        result.append(')');
        return result.toString();
    }

} //TileMatrixLimitsTypeImpl
