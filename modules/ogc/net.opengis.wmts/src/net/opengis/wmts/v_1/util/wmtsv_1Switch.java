/**
 */
package net.opengis.wmts.v_1.util;

import net.opengis.ows11.CapabilitiesBaseType;
import net.opengis.ows11.ContentsBaseType;
import net.opengis.ows11.DatasetDescriptionSummaryBaseType;
import net.opengis.ows11.DescriptionType;
import net.opengis.ows11.OnlineResourceType;

import net.opengis.wmts.v_1.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see net.opengis.wmts.v_1.wmtsv_1Package
 * @generated
 */
public class wmtsv_1Switch<T> extends Switch<T> {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static wmtsv_1Package modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public wmtsv_1Switch() {
        if (modelPackage == null) {
            modelPackage = wmtsv_1Package.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param ePackage the package in question.
     * @return whether this is a switch for the given package.
     * @generated
     */
    @Override
    protected boolean isSwitchFor(EPackage ePackage) {
        return ePackage == modelPackage;
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case wmtsv_1Package.BINARY_PAYLOAD_TYPE: {
                BinaryPayloadType binaryPayloadType = (BinaryPayloadType)theEObject;
                T result = caseBinaryPayloadType(binaryPayloadType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.CAPABILITIES_TYPE: {
                CapabilitiesType capabilitiesType = (CapabilitiesType)theEObject;
                T result = caseCapabilitiesType(capabilitiesType);
                if (result == null) result = caseCapabilitiesBaseType(capabilitiesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.CONTENTS_TYPE: {
                ContentsType contentsType = (ContentsType)theEObject;
                T result = caseContentsType(contentsType);
                if (result == null) result = caseContentsBaseType(contentsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.DIMENSION_NAME_VALUE_TYPE: {
                DimensionNameValueType dimensionNameValueType = (DimensionNameValueType)theEObject;
                T result = caseDimensionNameValueType(dimensionNameValueType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.DIMENSION_TYPE: {
                DimensionType dimensionType = (DimensionType)theEObject;
                T result = caseDimensionType(dimensionType);
                if (result == null) result = caseDescriptionType(dimensionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.DOCUMENT_ROOT: {
                DocumentRoot documentRoot = (DocumentRoot)theEObject;
                T result = caseDocumentRoot(documentRoot);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.FEATURE_INFO_RESPONSE_TYPE: {
                FeatureInfoResponseType featureInfoResponseType = (FeatureInfoResponseType)theEObject;
                T result = caseFeatureInfoResponseType(featureInfoResponseType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.GET_CAPABILITIES_TYPE: {
                GetCapabilitiesType getCapabilitiesType = (GetCapabilitiesType)theEObject;
                T result = caseGetCapabilitiesType(getCapabilitiesType);
                if (result == null) result = caseOws11_GetCapabilitiesType(getCapabilitiesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.GET_FEATURE_INFO_TYPE: {
                GetFeatureInfoType getFeatureInfoType = (GetFeatureInfoType)theEObject;
                T result = caseGetFeatureInfoType(getFeatureInfoType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.GET_TILE_TYPE: {
                GetTileType getTileType = (GetTileType)theEObject;
                T result = caseGetTileType(getTileType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.LAYER_TYPE: {
                LayerType layerType = (LayerType)theEObject;
                T result = caseLayerType(layerType);
                if (result == null) result = caseDatasetDescriptionSummaryBaseType(layerType);
                if (result == null) result = caseDescriptionType(layerType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.LEGEND_URL_TYPE: {
                LegendURLType legendURLType = (LegendURLType)theEObject;
                T result = caseLegendURLType(legendURLType);
                if (result == null) result = caseOnlineResourceType(legendURLType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.STYLE_TYPE: {
                StyleType styleType = (StyleType)theEObject;
                T result = caseStyleType(styleType);
                if (result == null) result = caseDescriptionType(styleType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.TEXT_PAYLOAD_TYPE: {
                TextPayloadType textPayloadType = (TextPayloadType)theEObject;
                T result = caseTextPayloadType(textPayloadType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.THEMES_TYPE: {
                ThemesType themesType = (ThemesType)theEObject;
                T result = caseThemesType(themesType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.THEME_TYPE: {
                ThemeType themeType = (ThemeType)theEObject;
                T result = caseThemeType(themeType);
                if (result == null) result = caseDescriptionType(themeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.TILE_MATRIX_LIMITS_TYPE: {
                TileMatrixLimitsType tileMatrixLimitsType = (TileMatrixLimitsType)theEObject;
                T result = caseTileMatrixLimitsType(tileMatrixLimitsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.TILE_MATRIX_SET_LIMITS_TYPE: {
                TileMatrixSetLimitsType tileMatrixSetLimitsType = (TileMatrixSetLimitsType)theEObject;
                T result = caseTileMatrixSetLimitsType(tileMatrixSetLimitsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.TILE_MATRIX_SET_LINK_TYPE: {
                TileMatrixSetLinkType tileMatrixSetLinkType = (TileMatrixSetLinkType)theEObject;
                T result = caseTileMatrixSetLinkType(tileMatrixSetLinkType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.TILE_MATRIX_SET_TYPE: {
                TileMatrixSetType tileMatrixSetType = (TileMatrixSetType)theEObject;
                T result = caseTileMatrixSetType(tileMatrixSetType);
                if (result == null) result = caseDescriptionType(tileMatrixSetType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.TILE_MATRIX_TYPE: {
                TileMatrixType tileMatrixType = (TileMatrixType)theEObject;
                T result = caseTileMatrixType(tileMatrixType);
                if (result == null) result = caseDescriptionType(tileMatrixType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case wmtsv_1Package.URL_TEMPLATE_TYPE: {
                URLTemplateType urlTemplateType = (URLTemplateType)theEObject;
                T result = caseURLTemplateType(urlTemplateType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Binary Payload Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Binary Payload Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBinaryPayloadType(BinaryPayloadType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Capabilities Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Capabilities Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCapabilitiesType(CapabilitiesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Contents Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Contents Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseContentsType(ContentsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dimension Name Value Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dimension Name Value Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDimensionNameValueType(DimensionNameValueType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dimension Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dimension Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDimensionType(DimensionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Document Root</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Document Root</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDocumentRoot(DocumentRoot object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Info Response Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Info Response Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureInfoResponseType(FeatureInfoResponseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Get Capabilities Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Capabilities Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGetCapabilitiesType(GetCapabilitiesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Get Feature Info Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Feature Info Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGetFeatureInfoType(GetFeatureInfoType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Get Tile Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Tile Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseGetTileType(GetTileType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Layer Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Layer Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLayerType(LayerType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Legend URL Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Legend URL Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLegendURLType(LegendURLType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Style Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Style Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStyleType(StyleType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Text Payload Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Text Payload Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTextPayloadType(TextPayloadType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Themes Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Themes Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseThemesType(ThemesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Theme Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Theme Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseThemeType(ThemeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tile Matrix Limits Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tile Matrix Limits Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTileMatrixLimitsType(TileMatrixLimitsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tile Matrix Set Limits Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tile Matrix Set Limits Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTileMatrixSetLimitsType(TileMatrixSetLimitsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tile Matrix Set Link Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tile Matrix Set Link Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTileMatrixSetLinkType(TileMatrixSetLinkType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tile Matrix Set Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tile Matrix Set Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTileMatrixSetType(TileMatrixSetType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Tile Matrix Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Tile Matrix Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTileMatrixType(TileMatrixType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>URL Template Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>URL Template Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseURLTemplateType(URLTemplateType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Capabilities Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Capabilities Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCapabilitiesBaseType(CapabilitiesBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Contents Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Contents Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseContentsBaseType(ContentsBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Description Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Description Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDescriptionType(DescriptionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Get Capabilities Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Capabilities Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOws11_GetCapabilitiesType(net.opengis.ows11.GetCapabilitiesType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dataset Description Summary Base Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dataset Description Summary Base Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDatasetDescriptionSummaryBaseType(DatasetDescriptionSummaryBaseType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Online Resource Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Online Resource Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOnlineResourceType(OnlineResourceType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    @Override
    public T defaultCase(EObject object) {
        return null;
    }

} //wmtsv_1Switch
