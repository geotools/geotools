/**
 */
package net.opengis.wmts.v_1.impl;

import java.math.BigInteger;

import java.util.Collection;

import net.opengis.wmts.v_1.DimensionNameValueType;
import net.opengis.wmts.v_1.GetTileType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Tile Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.GetTileTypeImpl#getLayer <em>Layer</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.GetTileTypeImpl#getStyle <em>Style</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.GetTileTypeImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.GetTileTypeImpl#getDimensionNameValue <em>Dimension Name Value</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.GetTileTypeImpl#getTileMatrixSet <em>Tile Matrix Set</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.GetTileTypeImpl#getTileMatrix <em>Tile Matrix</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.GetTileTypeImpl#getTileRow <em>Tile Row</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.GetTileTypeImpl#getTileCol <em>Tile Col</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.GetTileTypeImpl#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.GetTileTypeImpl#getVersion <em>Version</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GetTileTypeImpl extends MinimalEObjectImpl.Container implements GetTileType {
    /**
     * The default value of the '{@link #getLayer() <em>Layer</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLayer()
     * @generated
     * @ordered
     */
    protected static final String LAYER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLayer() <em>Layer</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLayer()
     * @generated
     * @ordered
     */
    protected String layer = LAYER_EDEFAULT;

    /**
     * The default value of the '{@link #getStyle() <em>Style</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStyle()
     * @generated
     * @ordered
     */
    protected static final String STYLE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getStyle() <em>Style</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStyle()
     * @generated
     * @ordered
     */
    protected String style = STYLE_EDEFAULT;

    /**
     * The default value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected static final String FORMAT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFormat() <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormat()
     * @generated
     * @ordered
     */
    protected String format = FORMAT_EDEFAULT;

    /**
     * The cached value of the '{@link #getDimensionNameValue() <em>Dimension Name Value</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDimensionNameValue()
     * @generated
     * @ordered
     */
    protected EList<DimensionNameValueType> dimensionNameValue;

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
     * The default value of the '{@link #getTileRow() <em>Tile Row</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileRow()
     * @generated
     * @ordered
     */
    protected static final BigInteger TILE_ROW_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTileRow() <em>Tile Row</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileRow()
     * @generated
     * @ordered
     */
    protected BigInteger tileRow = TILE_ROW_EDEFAULT;

    /**
     * The default value of the '{@link #getTileCol() <em>Tile Col</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileCol()
     * @generated
     * @ordered
     */
    protected static final BigInteger TILE_COL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTileCol() <em>Tile Col</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTileCol()
     * @generated
     * @ordered
     */
    protected BigInteger tileCol = TILE_COL_EDEFAULT;

    /**
     * The default value of the '{@link #getService() <em>Service</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getService()
     * @generated
     * @ordered
     */
    protected static final String SERVICE_EDEFAULT = "WMTS";

    /**
     * The cached value of the '{@link #getService() <em>Service</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getService()
     * @generated
     * @ordered
     */
    protected String service = SERVICE_EDEFAULT;

    /**
     * This is true if the Service attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean serviceESet;

    /**
     * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected static final String VERSION_EDEFAULT = "1.0.0";

    /**
     * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected String version = VERSION_EDEFAULT;

    /**
     * This is true if the Version attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean versionESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GetTileTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.GET_TILE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLayer() {
        return layer;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLayer(String newLayer) {
        String oldLayer = layer;
        layer = newLayer;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.GET_TILE_TYPE__LAYER, oldLayer, layer));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getStyle() {
        return style;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStyle(String newStyle) {
        String oldStyle = style;
        style = newStyle;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.GET_TILE_TYPE__STYLE, oldStyle, style));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFormat() {
        return format;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFormat(String newFormat) {
        String oldFormat = format;
        format = newFormat;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.GET_TILE_TYPE__FORMAT, oldFormat, format));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DimensionNameValueType> getDimensionNameValue() {
        if (dimensionNameValue == null) {
            dimensionNameValue = new EObjectContainmentEList<DimensionNameValueType>(DimensionNameValueType.class, this, wmtsv_1Package.GET_TILE_TYPE__DIMENSION_NAME_VALUE);
        }
        return dimensionNameValue;
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
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.GET_TILE_TYPE__TILE_MATRIX_SET, oldTileMatrixSet, tileMatrixSet));
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
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.GET_TILE_TYPE__TILE_MATRIX, oldTileMatrix, tileMatrix));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getTileRow() {
        return tileRow;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTileRow(BigInteger newTileRow) {
        BigInteger oldTileRow = tileRow;
        tileRow = newTileRow;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.GET_TILE_TYPE__TILE_ROW, oldTileRow, tileRow));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getTileCol() {
        return tileCol;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTileCol(BigInteger newTileCol) {
        BigInteger oldTileCol = tileCol;
        tileCol = newTileCol;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.GET_TILE_TYPE__TILE_COL, oldTileCol, tileCol));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getService() {
        return service;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setService(String newService) {
        String oldService = service;
        service = newService;
        boolean oldServiceESet = serviceESet;
        serviceESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.GET_TILE_TYPE__SERVICE, oldService, service, !oldServiceESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetService() {
        String oldService = service;
        boolean oldServiceESet = serviceESet;
        service = SERVICE_EDEFAULT;
        serviceESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, wmtsv_1Package.GET_TILE_TYPE__SERVICE, oldService, SERVICE_EDEFAULT, oldServiceESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetService() {
        return serviceESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getVersion() {
        return version;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVersion(String newVersion) {
        String oldVersion = version;
        version = newVersion;
        boolean oldVersionESet = versionESet;
        versionESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, wmtsv_1Package.GET_TILE_TYPE__VERSION, oldVersion, version, !oldVersionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetVersion() {
        String oldVersion = version;
        boolean oldVersionESet = versionESet;
        version = VERSION_EDEFAULT;
        versionESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, wmtsv_1Package.GET_TILE_TYPE__VERSION, oldVersion, VERSION_EDEFAULT, oldVersionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetVersion() {
        return versionESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wmtsv_1Package.GET_TILE_TYPE__DIMENSION_NAME_VALUE:
                return ((InternalEList<?>)getDimensionNameValue()).basicRemove(otherEnd, msgs);
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
            case wmtsv_1Package.GET_TILE_TYPE__LAYER:
                return getLayer();
            case wmtsv_1Package.GET_TILE_TYPE__STYLE:
                return getStyle();
            case wmtsv_1Package.GET_TILE_TYPE__FORMAT:
                return getFormat();
            case wmtsv_1Package.GET_TILE_TYPE__DIMENSION_NAME_VALUE:
                return getDimensionNameValue();
            case wmtsv_1Package.GET_TILE_TYPE__TILE_MATRIX_SET:
                return getTileMatrixSet();
            case wmtsv_1Package.GET_TILE_TYPE__TILE_MATRIX:
                return getTileMatrix();
            case wmtsv_1Package.GET_TILE_TYPE__TILE_ROW:
                return getTileRow();
            case wmtsv_1Package.GET_TILE_TYPE__TILE_COL:
                return getTileCol();
            case wmtsv_1Package.GET_TILE_TYPE__SERVICE:
                return getService();
            case wmtsv_1Package.GET_TILE_TYPE__VERSION:
                return getVersion();
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
            case wmtsv_1Package.GET_TILE_TYPE__LAYER:
                setLayer((String)newValue);
                return;
            case wmtsv_1Package.GET_TILE_TYPE__STYLE:
                setStyle((String)newValue);
                return;
            case wmtsv_1Package.GET_TILE_TYPE__FORMAT:
                setFormat((String)newValue);
                return;
            case wmtsv_1Package.GET_TILE_TYPE__DIMENSION_NAME_VALUE:
                getDimensionNameValue().clear();
                getDimensionNameValue().addAll((Collection<? extends DimensionNameValueType>)newValue);
                return;
            case wmtsv_1Package.GET_TILE_TYPE__TILE_MATRIX_SET:
                setTileMatrixSet((String)newValue);
                return;
            case wmtsv_1Package.GET_TILE_TYPE__TILE_MATRIX:
                setTileMatrix((String)newValue);
                return;
            case wmtsv_1Package.GET_TILE_TYPE__TILE_ROW:
                setTileRow((BigInteger)newValue);
                return;
            case wmtsv_1Package.GET_TILE_TYPE__TILE_COL:
                setTileCol((BigInteger)newValue);
                return;
            case wmtsv_1Package.GET_TILE_TYPE__SERVICE:
                setService((String)newValue);
                return;
            case wmtsv_1Package.GET_TILE_TYPE__VERSION:
                setVersion((String)newValue);
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
            case wmtsv_1Package.GET_TILE_TYPE__LAYER:
                setLayer(LAYER_EDEFAULT);
                return;
            case wmtsv_1Package.GET_TILE_TYPE__STYLE:
                setStyle(STYLE_EDEFAULT);
                return;
            case wmtsv_1Package.GET_TILE_TYPE__FORMAT:
                setFormat(FORMAT_EDEFAULT);
                return;
            case wmtsv_1Package.GET_TILE_TYPE__DIMENSION_NAME_VALUE:
                getDimensionNameValue().clear();
                return;
            case wmtsv_1Package.GET_TILE_TYPE__TILE_MATRIX_SET:
                setTileMatrixSet(TILE_MATRIX_SET_EDEFAULT);
                return;
            case wmtsv_1Package.GET_TILE_TYPE__TILE_MATRIX:
                setTileMatrix(TILE_MATRIX_EDEFAULT);
                return;
            case wmtsv_1Package.GET_TILE_TYPE__TILE_ROW:
                setTileRow(TILE_ROW_EDEFAULT);
                return;
            case wmtsv_1Package.GET_TILE_TYPE__TILE_COL:
                setTileCol(TILE_COL_EDEFAULT);
                return;
            case wmtsv_1Package.GET_TILE_TYPE__SERVICE:
                unsetService();
                return;
            case wmtsv_1Package.GET_TILE_TYPE__VERSION:
                unsetVersion();
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
            case wmtsv_1Package.GET_TILE_TYPE__LAYER:
                return LAYER_EDEFAULT == null ? layer != null : !LAYER_EDEFAULT.equals(layer);
            case wmtsv_1Package.GET_TILE_TYPE__STYLE:
                return STYLE_EDEFAULT == null ? style != null : !STYLE_EDEFAULT.equals(style);
            case wmtsv_1Package.GET_TILE_TYPE__FORMAT:
                return FORMAT_EDEFAULT == null ? format != null : !FORMAT_EDEFAULT.equals(format);
            case wmtsv_1Package.GET_TILE_TYPE__DIMENSION_NAME_VALUE:
                return dimensionNameValue != null && !dimensionNameValue.isEmpty();
            case wmtsv_1Package.GET_TILE_TYPE__TILE_MATRIX_SET:
                return TILE_MATRIX_SET_EDEFAULT == null ? tileMatrixSet != null : !TILE_MATRIX_SET_EDEFAULT.equals(tileMatrixSet);
            case wmtsv_1Package.GET_TILE_TYPE__TILE_MATRIX:
                return TILE_MATRIX_EDEFAULT == null ? tileMatrix != null : !TILE_MATRIX_EDEFAULT.equals(tileMatrix);
            case wmtsv_1Package.GET_TILE_TYPE__TILE_ROW:
                return TILE_ROW_EDEFAULT == null ? tileRow != null : !TILE_ROW_EDEFAULT.equals(tileRow);
            case wmtsv_1Package.GET_TILE_TYPE__TILE_COL:
                return TILE_COL_EDEFAULT == null ? tileCol != null : !TILE_COL_EDEFAULT.equals(tileCol);
            case wmtsv_1Package.GET_TILE_TYPE__SERVICE:
                return isSetService();
            case wmtsv_1Package.GET_TILE_TYPE__VERSION:
                return isSetVersion();
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
        result.append(" (layer: ");
        result.append(layer);
        result.append(", style: ");
        result.append(style);
        result.append(", format: ");
        result.append(format);
        result.append(", tileMatrixSet: ");
        result.append(tileMatrixSet);
        result.append(", tileMatrix: ");
        result.append(tileMatrix);
        result.append(", tileRow: ");
        result.append(tileRow);
        result.append(", tileCol: ");
        result.append(tileCol);
        result.append(", service: ");
        if (serviceESet) result.append(service); else result.append("<unset>");
        result.append(", version: ");
        if (versionESet) result.append(version); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //GetTileTypeImpl
