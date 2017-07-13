/**
 */
package net.opengis.wmts.v_1.impl;

import java.math.BigInteger;

import java.util.List;

import net.opengis.ows11.CodeType;

import net.opengis.ows11.impl.DescriptionTypeImpl;

import net.opengis.wmts.v_1.TileMatrixType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tile Matrix Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixTypeImpl#getScaleDenominator <em>Scale Denominator</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixTypeImpl#getTopLeftCorner <em>Top Left Corner</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixTypeImpl#getTileWidth <em>Tile Width</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixTypeImpl#getTileHeight <em>Tile Height</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixTypeImpl#getMatrixWidth <em>Matrix Width</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.TileMatrixTypeImpl#getMatrixHeight <em>Matrix Height</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TileMatrixTypeImpl extends DescriptionTypeImpl implements TileMatrixType {
    /**
     * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected CodeType identifier;

    /**
     * The default value of the '{@link #getScaleDenominator() <em>Scale Denominator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getScaleDenominator()
     * @generated
     * @ordered
     */
    protected static final double SCALE_DENOMINATOR_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getScaleDenominator() <em>Scale Denominator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getScaleDenominator()
     * @generated
     * @ordered
     */
    protected double scaleDenominator = SCALE_DENOMINATOR_EDEFAULT;

    /**
     * This is true if the Scale Denominator attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean scaleDenominatorESet;

    /**
     * The default value of the '{@link #getTopLeftCorner() <em>Top Left Corner</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTopLeftCorner()
     * @generated
     * @ordered
     */
    protected static final List<Double> TOP_LEFT_CORNER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTopLeftCorner() <em>Top Left Corner</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTopLeftCorner()
     * @generated
     * @ordered
     */
    protected List<Double> topLeftCorner = TOP_LEFT_CORNER_EDEFAULT;

    /**
     * The default value of the '{@link #getTileWidth() <em>Tile Width</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileWidth()
     * @generated
     * @ordered
     */
    protected static final BigInteger TILE_WIDTH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTileWidth() <em>Tile Width</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileWidth()
     * @generated
     * @ordered
     */
    protected BigInteger tileWidth = TILE_WIDTH_EDEFAULT;

    /**
     * The default value of the '{@link #getTileHeight() <em>Tile Height</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileHeight()
     * @generated
     * @ordered
     */
    protected static final BigInteger TILE_HEIGHT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTileHeight() <em>Tile Height</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileHeight()
     * @generated
     * @ordered
     */
    protected BigInteger tileHeight = TILE_HEIGHT_EDEFAULT;

    /**
     * The default value of the '{@link #getMatrixWidth() <em>Matrix Width</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMatrixWidth()
     * @generated
     * @ordered
     */
    protected static final BigInteger MATRIX_WIDTH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMatrixWidth() <em>Matrix Width</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMatrixWidth()
     * @generated
     * @ordered
     */
    protected BigInteger matrixWidth = MATRIX_WIDTH_EDEFAULT;

    /**
     * The default value of the '{@link #getMatrixHeight() <em>Matrix Height</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMatrixHeight()
     * @generated
     * @ordered
     */
    protected static final BigInteger MATRIX_HEIGHT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMatrixHeight() <em>Matrix Height</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMatrixHeight()
     * @generated
     * @ordered
     */
    protected BigInteger matrixHeight = MATRIX_HEIGHT_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TileMatrixTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.TILE_MATRIX_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getIdentifier() {
        return identifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetIdentifier(CodeType newIdentifier, NotificationChain msgs) {
        CodeType oldIdentifier = identifier;
        identifier = newIdentifier;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_TYPE__IDENTIFIER, oldIdentifier, newIdentifier);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIdentifier(CodeType newIdentifier) {
        if (newIdentifier != identifier) {
            NotificationChain msgs = null;
            if (identifier != null)
                msgs = ((InternalEObject)identifier).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.TILE_MATRIX_TYPE__IDENTIFIER, null, msgs);
            if (newIdentifier != null)
                msgs = ((InternalEObject)newIdentifier).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - wmtsv_1Package.TILE_MATRIX_TYPE__IDENTIFIER, null, msgs);
            msgs = basicSetIdentifier(newIdentifier, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_TYPE__IDENTIFIER, newIdentifier, newIdentifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getScaleDenominator() {
        return scaleDenominator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setScaleDenominator(double newScaleDenominator) {
        double oldScaleDenominator = scaleDenominator;
        scaleDenominator = newScaleDenominator;
        boolean oldScaleDenominatorESet = scaleDenominatorESet;
        scaleDenominatorESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_TYPE__SCALE_DENOMINATOR, oldScaleDenominator, scaleDenominator, !oldScaleDenominatorESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetScaleDenominator() {
        double oldScaleDenominator = scaleDenominator;
        boolean oldScaleDenominatorESet = scaleDenominatorESet;
        scaleDenominator = SCALE_DENOMINATOR_EDEFAULT;
        scaleDenominatorESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, wmtsv_1Package.TILE_MATRIX_TYPE__SCALE_DENOMINATOR, oldScaleDenominator, SCALE_DENOMINATOR_EDEFAULT, oldScaleDenominatorESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetScaleDenominator() {
        return scaleDenominatorESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<Double> getTopLeftCorner() {
        return topLeftCorner;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTopLeftCorner(List<Double> newTopLeftCorner) {
        List<Double> oldTopLeftCorner = topLeftCorner;
        topLeftCorner = newTopLeftCorner;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_TYPE__TOP_LEFT_CORNER, oldTopLeftCorner, topLeftCorner));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getTileWidth() {
        return tileWidth;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTileWidth(BigInteger newTileWidth) {
        BigInteger oldTileWidth = tileWidth;
        tileWidth = newTileWidth;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_TYPE__TILE_WIDTH, oldTileWidth, tileWidth));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getTileHeight() {
        return tileHeight;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTileHeight(BigInteger newTileHeight) {
        BigInteger oldTileHeight = tileHeight;
        tileHeight = newTileHeight;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_TYPE__TILE_HEIGHT, oldTileHeight, tileHeight));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getMatrixWidth() {
        return matrixWidth;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMatrixWidth(BigInteger newMatrixWidth) {
        BigInteger oldMatrixWidth = matrixWidth;
        matrixWidth = newMatrixWidth;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_TYPE__MATRIX_WIDTH, oldMatrixWidth, matrixWidth));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getMatrixHeight() {
        return matrixHeight;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMatrixHeight(BigInteger newMatrixHeight) {
        BigInteger oldMatrixHeight = matrixHeight;
        matrixHeight = newMatrixHeight;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.TILE_MATRIX_TYPE__MATRIX_HEIGHT, oldMatrixHeight, matrixHeight));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wmtsv_1Package.TILE_MATRIX_TYPE__IDENTIFIER:
                return basicSetIdentifier(null, msgs);
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
            case wmtsv_1Package.TILE_MATRIX_TYPE__IDENTIFIER:
                return getIdentifier();
            case wmtsv_1Package.TILE_MATRIX_TYPE__SCALE_DENOMINATOR:
                return getScaleDenominator();
            case wmtsv_1Package.TILE_MATRIX_TYPE__TOP_LEFT_CORNER:
                return getTopLeftCorner();
            case wmtsv_1Package.TILE_MATRIX_TYPE__TILE_WIDTH:
                return getTileWidth();
            case wmtsv_1Package.TILE_MATRIX_TYPE__TILE_HEIGHT:
                return getTileHeight();
            case wmtsv_1Package.TILE_MATRIX_TYPE__MATRIX_WIDTH:
                return getMatrixWidth();
            case wmtsv_1Package.TILE_MATRIX_TYPE__MATRIX_HEIGHT:
                return getMatrixHeight();
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
            case wmtsv_1Package.TILE_MATRIX_TYPE__IDENTIFIER:
                setIdentifier((CodeType)newValue);
                return;
            case wmtsv_1Package.TILE_MATRIX_TYPE__SCALE_DENOMINATOR:
                setScaleDenominator((Double)newValue);
                return;
            case wmtsv_1Package.TILE_MATRIX_TYPE__TOP_LEFT_CORNER:
                setTopLeftCorner((List<Double>)newValue);
                return;
            case wmtsv_1Package.TILE_MATRIX_TYPE__TILE_WIDTH:
                setTileWidth((BigInteger)newValue);
                return;
            case wmtsv_1Package.TILE_MATRIX_TYPE__TILE_HEIGHT:
                setTileHeight((BigInteger)newValue);
                return;
            case wmtsv_1Package.TILE_MATRIX_TYPE__MATRIX_WIDTH:
                setMatrixWidth((BigInteger)newValue);
                return;
            case wmtsv_1Package.TILE_MATRIX_TYPE__MATRIX_HEIGHT:
                setMatrixHeight((BigInteger)newValue);
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
            case wmtsv_1Package.TILE_MATRIX_TYPE__IDENTIFIER:
                setIdentifier((CodeType)null);
                return;
            case wmtsv_1Package.TILE_MATRIX_TYPE__SCALE_DENOMINATOR:
                unsetScaleDenominator();
                return;
            case wmtsv_1Package.TILE_MATRIX_TYPE__TOP_LEFT_CORNER:
                setTopLeftCorner(TOP_LEFT_CORNER_EDEFAULT);
                return;
            case wmtsv_1Package.TILE_MATRIX_TYPE__TILE_WIDTH:
                setTileWidth(TILE_WIDTH_EDEFAULT);
                return;
            case wmtsv_1Package.TILE_MATRIX_TYPE__TILE_HEIGHT:
                setTileHeight(TILE_HEIGHT_EDEFAULT);
                return;
            case wmtsv_1Package.TILE_MATRIX_TYPE__MATRIX_WIDTH:
                setMatrixWidth(MATRIX_WIDTH_EDEFAULT);
                return;
            case wmtsv_1Package.TILE_MATRIX_TYPE__MATRIX_HEIGHT:
                setMatrixHeight(MATRIX_HEIGHT_EDEFAULT);
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
            case wmtsv_1Package.TILE_MATRIX_TYPE__IDENTIFIER:
                return identifier != null;
            case wmtsv_1Package.TILE_MATRIX_TYPE__SCALE_DENOMINATOR:
                return isSetScaleDenominator();
            case wmtsv_1Package.TILE_MATRIX_TYPE__TOP_LEFT_CORNER:
                return TOP_LEFT_CORNER_EDEFAULT == null ? topLeftCorner != null : !TOP_LEFT_CORNER_EDEFAULT.equals(topLeftCorner);
            case wmtsv_1Package.TILE_MATRIX_TYPE__TILE_WIDTH:
                return TILE_WIDTH_EDEFAULT == null ? tileWidth != null : !TILE_WIDTH_EDEFAULT.equals(tileWidth);
            case wmtsv_1Package.TILE_MATRIX_TYPE__TILE_HEIGHT:
                return TILE_HEIGHT_EDEFAULT == null ? tileHeight != null : !TILE_HEIGHT_EDEFAULT.equals(tileHeight);
            case wmtsv_1Package.TILE_MATRIX_TYPE__MATRIX_WIDTH:
                return MATRIX_WIDTH_EDEFAULT == null ? matrixWidth != null : !MATRIX_WIDTH_EDEFAULT.equals(matrixWidth);
            case wmtsv_1Package.TILE_MATRIX_TYPE__MATRIX_HEIGHT:
                return MATRIX_HEIGHT_EDEFAULT == null ? matrixHeight != null : !MATRIX_HEIGHT_EDEFAULT.equals(matrixHeight);
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
        result.append(" (scaleDenominator: ");
        if (scaleDenominatorESet) result.append(scaleDenominator); else result.append("<unset>");
        result.append(", topLeftCorner: ");
        result.append(topLeftCorner);
        result.append(", tileWidth: ");
        result.append(tileWidth);
        result.append(", tileHeight: ");
        result.append(tileHeight);
        result.append(", matrixWidth: ");
        result.append(matrixWidth);
        result.append(", matrixHeight: ");
        result.append(matrixHeight);
        result.append(')');
        return result.toString();
    }

} //TileMatrixTypeImpl
