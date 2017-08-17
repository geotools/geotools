/**
 */
package net.opengis.wmts.v_1.impl;

import net.opengis.wmts.v_1.BinaryPayloadType;
import net.opengis.wmts.v_1.CapabilitiesType;
import net.opengis.wmts.v_1.DimensionNameValueType;
import net.opengis.wmts.v_1.DimensionType;
import net.opengis.wmts.v_1.DocumentRoot;
import net.opengis.wmts.v_1.FeatureInfoResponseType;
import net.opengis.wmts.v_1.GetCapabilitiesType;
import net.opengis.wmts.v_1.GetFeatureInfoType;
import net.opengis.wmts.v_1.GetTileType;
import net.opengis.wmts.v_1.LayerType;
import net.opengis.wmts.v_1.LegendURLType;
import net.opengis.wmts.v_1.StyleType;
import net.opengis.wmts.v_1.TextPayloadType;
import net.opengis.wmts.v_1.ThemeType;
import net.opengis.wmts.v_1.ThemesType;
import net.opengis.wmts.v_1.TileMatrixLimitsType;
import net.opengis.wmts.v_1.TileMatrixSetLimitsType;
import net.opengis.wmts.v_1.TileMatrixSetLinkType;
import net.opengis.wmts.v_1.TileMatrixSetType;
import net.opengis.wmts.v_1.TileMatrixType;
import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getBinaryPayload <em>Binary Payload</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getCapabilities <em>Capabilities</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getDimension <em>Dimension</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getDimensionNameValue <em>Dimension Name Value</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getFeatureInfoResponse <em>Feature Info Response</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getGetFeatureInfo <em>Get Feature Info</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getGetTile <em>Get Tile</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getLayer <em>Layer</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getLegendURL <em>Legend URL</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getStyle <em>Style</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getTextPayload <em>Text Payload</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getTheme <em>Theme</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getThemes <em>Themes</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getTileMatrix <em>Tile Matrix</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getTileMatrixLimits <em>Tile Matrix Limits</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getTileMatrixSet <em>Tile Matrix Set</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getTileMatrixSetLimits <em>Tile Matrix Set Limits</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.impl.DocumentRootImpl#getTileMatrixSetLink <em>Tile Matrix Set Link</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DocumentRootImpl extends MinimalEObjectImpl.Container implements DocumentRoot {
    /**
     * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMixed()
     * @generated
     * @ordered
     */
    protected FeatureMap mixed;

    /**
     * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix Map</em>}' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXMLNSPrefixMap()
     * @generated
     * @ordered
     */
    protected EMap<String, String> xMLNSPrefixMap;

    /**
     * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXSISchemaLocation()
     * @generated
     * @ordered
     */
    protected EMap<String, String> xSISchemaLocation;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DocumentRootImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return wmtsv_1Package.Literals.DOCUMENT_ROOT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getMixed() {
        if (mixed == null) {
            mixed = new BasicFeatureMap(this, wmtsv_1Package.DOCUMENT_ROOT__MIXED);
        }
        return mixed;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EMap<String, String> getXMLNSPrefixMap() {
        if (xMLNSPrefixMap == null) {
            xMLNSPrefixMap = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, wmtsv_1Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        }
        return xMLNSPrefixMap;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EMap<String, String> getXSISchemaLocation() {
        if (xSISchemaLocation == null) {
            xSISchemaLocation = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, wmtsv_1Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        }
        return xSISchemaLocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BinaryPayloadType getBinaryPayload() {
        return (BinaryPayloadType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__BINARY_PAYLOAD, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBinaryPayload(BinaryPayloadType newBinaryPayload, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__BINARY_PAYLOAD, newBinaryPayload, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBinaryPayload(BinaryPayloadType newBinaryPayload) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__BINARY_PAYLOAD, newBinaryPayload);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CapabilitiesType getCapabilities() {
        return (CapabilitiesType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__CAPABILITIES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetCapabilities(CapabilitiesType newCapabilities, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__CAPABILITIES, newCapabilities, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCapabilities(CapabilitiesType newCapabilities) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__CAPABILITIES, newCapabilities);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DimensionType getDimension() {
        return (DimensionType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__DIMENSION, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDimension(DimensionType newDimension, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__DIMENSION, newDimension, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDimension(DimensionType newDimension) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__DIMENSION, newDimension);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DimensionNameValueType getDimensionNameValue() {
        return (DimensionNameValueType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__DIMENSION_NAME_VALUE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDimensionNameValue(DimensionNameValueType newDimensionNameValue, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__DIMENSION_NAME_VALUE, newDimensionNameValue, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDimensionNameValue(DimensionNameValueType newDimensionNameValue) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__DIMENSION_NAME_VALUE, newDimensionNameValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureInfoResponseType getFeatureInfoResponse() {
        return (FeatureInfoResponseType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__FEATURE_INFO_RESPONSE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetFeatureInfoResponse(FeatureInfoResponseType newFeatureInfoResponse, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__FEATURE_INFO_RESPONSE, newFeatureInfoResponse, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFeatureInfoResponse(FeatureInfoResponseType newFeatureInfoResponse) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__FEATURE_INFO_RESPONSE, newFeatureInfoResponse);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetCapabilitiesType getGetCapabilities() {
        return (GetCapabilitiesType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGetCapabilities(GetCapabilitiesType newGetCapabilities, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGetCapabilities(GetCapabilitiesType newGetCapabilities) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetFeatureInfoType getGetFeatureInfo() {
        return (GetFeatureInfoType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__GET_FEATURE_INFO, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGetFeatureInfo(GetFeatureInfoType newGetFeatureInfo, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__GET_FEATURE_INFO, newGetFeatureInfo, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGetFeatureInfo(GetFeatureInfoType newGetFeatureInfo) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__GET_FEATURE_INFO, newGetFeatureInfo);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetTileType getGetTile() {
        return (GetTileType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__GET_TILE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGetTile(GetTileType newGetTile, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__GET_TILE, newGetTile, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGetTile(GetTileType newGetTile) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__GET_TILE, newGetTile);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LayerType getLayer() {
        return (LayerType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__LAYER, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLayer(LayerType newLayer, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__LAYER, newLayer, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLayer(LayerType newLayer) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__LAYER, newLayer);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LegendURLType getLegendURL() {
        return (LegendURLType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__LEGEND_URL, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLegendURL(LegendURLType newLegendURL, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__LEGEND_URL, newLegendURL, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLegendURL(LegendURLType newLegendURL) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__LEGEND_URL, newLegendURL);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StyleType getStyle() {
        return (StyleType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__STYLE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetStyle(StyleType newStyle, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__STYLE, newStyle, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStyle(StyleType newStyle) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__STYLE, newStyle);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TextPayloadType getTextPayload() {
        return (TextPayloadType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__TEXT_PAYLOAD, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTextPayload(TextPayloadType newTextPayload, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__TEXT_PAYLOAD, newTextPayload, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTextPayload(TextPayloadType newTextPayload) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__TEXT_PAYLOAD, newTextPayload);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ThemeType getTheme() {
        return (ThemeType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__THEME, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTheme(ThemeType newTheme, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__THEME, newTheme, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTheme(ThemeType newTheme) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__THEME, newTheme);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ThemesType getThemes() {
        return (ThemesType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__THEMES, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetThemes(ThemesType newThemes, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__THEMES, newThemes, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setThemes(ThemesType newThemes) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__THEMES, newThemes);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TileMatrixType getTileMatrix() {
        return (TileMatrixType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__TILE_MATRIX, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTileMatrix(TileMatrixType newTileMatrix, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__TILE_MATRIX, newTileMatrix, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTileMatrix(TileMatrixType newTileMatrix) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__TILE_MATRIX, newTileMatrix);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TileMatrixLimitsType getTileMatrixLimits() {
        return (TileMatrixLimitsType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__TILE_MATRIX_LIMITS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTileMatrixLimits(TileMatrixLimitsType newTileMatrixLimits, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__TILE_MATRIX_LIMITS, newTileMatrixLimits, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTileMatrixLimits(TileMatrixLimitsType newTileMatrixLimits) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__TILE_MATRIX_LIMITS, newTileMatrixLimits);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TileMatrixSetType getTileMatrixSet() {
        return (TileMatrixSetType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__TILE_MATRIX_SET, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTileMatrixSet(TileMatrixSetType newTileMatrixSet, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__TILE_MATRIX_SET, newTileMatrixSet, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTileMatrixSet(TileMatrixSetType newTileMatrixSet) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__TILE_MATRIX_SET, newTileMatrixSet);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TileMatrixSetLimitsType getTileMatrixSetLimits() {
        return (TileMatrixSetLimitsType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__TILE_MATRIX_SET_LIMITS, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTileMatrixSetLimits(TileMatrixSetLimitsType newTileMatrixSetLimits, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__TILE_MATRIX_SET_LIMITS, newTileMatrixSetLimits, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTileMatrixSetLimits(TileMatrixSetLimitsType newTileMatrixSetLimits) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__TILE_MATRIX_SET_LIMITS, newTileMatrixSetLimits);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TileMatrixSetLinkType getTileMatrixSetLink() {
        return (TileMatrixSetLinkType)getMixed().get(wmtsv_1Package.Literals.DOCUMENT_ROOT__TILE_MATRIX_SET_LINK, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTileMatrixSetLink(TileMatrixSetLinkType newTileMatrixSetLink, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(wmtsv_1Package.Literals.DOCUMENT_ROOT__TILE_MATRIX_SET_LINK, newTileMatrixSetLink, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTileMatrixSetLink(TileMatrixSetLinkType newTileMatrixSetLink) {
        ((FeatureMap.Internal)getMixed()).set(wmtsv_1Package.Literals.DOCUMENT_ROOT__TILE_MATRIX_SET_LINK, newTileMatrixSetLink);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case wmtsv_1Package.DOCUMENT_ROOT__MIXED:
                return ((InternalEList<?>)getMixed()).basicRemove(otherEnd, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return ((InternalEList<?>)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return ((InternalEList<?>)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__BINARY_PAYLOAD:
                return basicSetBinaryPayload(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__CAPABILITIES:
                return basicSetCapabilities(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__DIMENSION:
                return basicSetDimension(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__DIMENSION_NAME_VALUE:
                return basicSetDimensionNameValue(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__FEATURE_INFO_RESPONSE:
                return basicSetFeatureInfoResponse(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                return basicSetGetCapabilities(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__GET_FEATURE_INFO:
                return basicSetGetFeatureInfo(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__GET_TILE:
                return basicSetGetTile(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__LAYER:
                return basicSetLayer(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__LEGEND_URL:
                return basicSetLegendURL(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__STYLE:
                return basicSetStyle(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__TEXT_PAYLOAD:
                return basicSetTextPayload(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__THEME:
                return basicSetTheme(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__THEMES:
                return basicSetThemes(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX:
                return basicSetTileMatrix(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_LIMITS:
                return basicSetTileMatrixLimits(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_SET:
                return basicSetTileMatrixSet(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_SET_LIMITS:
                return basicSetTileMatrixSetLimits(null, msgs);
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_SET_LINK:
                return basicSetTileMatrixSetLink(null, msgs);
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
            case wmtsv_1Package.DOCUMENT_ROOT__MIXED:
                if (coreType) return getMixed();
                return ((FeatureMap.Internal)getMixed()).getWrapper();
            case wmtsv_1Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                if (coreType) return getXMLNSPrefixMap();
                else return getXMLNSPrefixMap().map();
            case wmtsv_1Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                if (coreType) return getXSISchemaLocation();
                else return getXSISchemaLocation().map();
            case wmtsv_1Package.DOCUMENT_ROOT__BINARY_PAYLOAD:
                return getBinaryPayload();
            case wmtsv_1Package.DOCUMENT_ROOT__CAPABILITIES:
                return getCapabilities();
            case wmtsv_1Package.DOCUMENT_ROOT__DIMENSION:
                return getDimension();
            case wmtsv_1Package.DOCUMENT_ROOT__DIMENSION_NAME_VALUE:
                return getDimensionNameValue();
            case wmtsv_1Package.DOCUMENT_ROOT__FEATURE_INFO_RESPONSE:
                return getFeatureInfoResponse();
            case wmtsv_1Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                return getGetCapabilities();
            case wmtsv_1Package.DOCUMENT_ROOT__GET_FEATURE_INFO:
                return getGetFeatureInfo();
            case wmtsv_1Package.DOCUMENT_ROOT__GET_TILE:
                return getGetTile();
            case wmtsv_1Package.DOCUMENT_ROOT__LAYER:
                return getLayer();
            case wmtsv_1Package.DOCUMENT_ROOT__LEGEND_URL:
                return getLegendURL();
            case wmtsv_1Package.DOCUMENT_ROOT__STYLE:
                return getStyle();
            case wmtsv_1Package.DOCUMENT_ROOT__TEXT_PAYLOAD:
                return getTextPayload();
            case wmtsv_1Package.DOCUMENT_ROOT__THEME:
                return getTheme();
            case wmtsv_1Package.DOCUMENT_ROOT__THEMES:
                return getThemes();
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX:
                return getTileMatrix();
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_LIMITS:
                return getTileMatrixLimits();
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_SET:
                return getTileMatrixSet();
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_SET_LIMITS:
                return getTileMatrixSetLimits();
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_SET_LINK:
                return getTileMatrixSetLink();
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
            case wmtsv_1Package.DOCUMENT_ROOT__MIXED:
                ((FeatureMap.Internal)getMixed()).set(newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                ((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                ((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__BINARY_PAYLOAD:
                setBinaryPayload((BinaryPayloadType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__CAPABILITIES:
                setCapabilities((CapabilitiesType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__DIMENSION:
                setDimension((DimensionType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__DIMENSION_NAME_VALUE:
                setDimensionNameValue((DimensionNameValueType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__FEATURE_INFO_RESPONSE:
                setFeatureInfoResponse((FeatureInfoResponseType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                setGetCapabilities((GetCapabilitiesType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__GET_FEATURE_INFO:
                setGetFeatureInfo((GetFeatureInfoType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__GET_TILE:
                setGetTile((GetTileType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__LAYER:
                setLayer((LayerType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__LEGEND_URL:
                setLegendURL((LegendURLType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__STYLE:
                setStyle((StyleType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__TEXT_PAYLOAD:
                setTextPayload((TextPayloadType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__THEME:
                setTheme((ThemeType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__THEMES:
                setThemes((ThemesType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX:
                setTileMatrix((TileMatrixType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_LIMITS:
                setTileMatrixLimits((TileMatrixLimitsType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_SET:
                setTileMatrixSet((TileMatrixSetType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_SET_LIMITS:
                setTileMatrixSetLimits((TileMatrixSetLimitsType)newValue);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_SET_LINK:
                setTileMatrixSetLink((TileMatrixSetLinkType)newValue);
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
            case wmtsv_1Package.DOCUMENT_ROOT__MIXED:
                getMixed().clear();
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                getXMLNSPrefixMap().clear();
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                getXSISchemaLocation().clear();
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__BINARY_PAYLOAD:
                setBinaryPayload((BinaryPayloadType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__CAPABILITIES:
                setCapabilities((CapabilitiesType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__DIMENSION:
                setDimension((DimensionType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__DIMENSION_NAME_VALUE:
                setDimensionNameValue((DimensionNameValueType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__FEATURE_INFO_RESPONSE:
                setFeatureInfoResponse((FeatureInfoResponseType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                setGetCapabilities((GetCapabilitiesType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__GET_FEATURE_INFO:
                setGetFeatureInfo((GetFeatureInfoType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__GET_TILE:
                setGetTile((GetTileType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__LAYER:
                setLayer((LayerType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__LEGEND_URL:
                setLegendURL((LegendURLType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__STYLE:
                setStyle((StyleType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__TEXT_PAYLOAD:
                setTextPayload((TextPayloadType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__THEME:
                setTheme((ThemeType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__THEMES:
                setThemes((ThemesType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX:
                setTileMatrix((TileMatrixType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_LIMITS:
                setTileMatrixLimits((TileMatrixLimitsType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_SET:
                setTileMatrixSet((TileMatrixSetType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_SET_LIMITS:
                setTileMatrixSetLimits((TileMatrixSetLimitsType)null);
                return;
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_SET_LINK:
                setTileMatrixSetLink((TileMatrixSetLinkType)null);
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
            case wmtsv_1Package.DOCUMENT_ROOT__MIXED:
                return mixed != null && !mixed.isEmpty();
            case wmtsv_1Package.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
            case wmtsv_1Package.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
            case wmtsv_1Package.DOCUMENT_ROOT__BINARY_PAYLOAD:
                return getBinaryPayload() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__CAPABILITIES:
                return getCapabilities() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__DIMENSION:
                return getDimension() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__DIMENSION_NAME_VALUE:
                return getDimensionNameValue() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__FEATURE_INFO_RESPONSE:
                return getFeatureInfoResponse() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__GET_CAPABILITIES:
                return getGetCapabilities() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__GET_FEATURE_INFO:
                return getGetFeatureInfo() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__GET_TILE:
                return getGetTile() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__LAYER:
                return getLayer() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__LEGEND_URL:
                return getLegendURL() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__STYLE:
                return getStyle() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__TEXT_PAYLOAD:
                return getTextPayload() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__THEME:
                return getTheme() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__THEMES:
                return getThemes() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX:
                return getTileMatrix() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_LIMITS:
                return getTileMatrixLimits() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_SET:
                return getTileMatrixSet() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_SET_LIMITS:
                return getTileMatrixSetLimits() != null;
            case wmtsv_1Package.DOCUMENT_ROOT__TILE_MATRIX_SET_LINK:
                return getTileMatrixSetLink() != null;
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
        result.append(" (mixed: ");
        result.append(mixed);
        result.append(')');
        return result.toString();
    }

} //DocumentRootImpl
