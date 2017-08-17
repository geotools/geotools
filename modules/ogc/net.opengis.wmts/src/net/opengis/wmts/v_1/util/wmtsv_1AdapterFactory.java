/**
 */
package net.opengis.wmts.v_1.util;

import net.opengis.ows11.CapabilitiesBaseType;
import net.opengis.ows11.ContentsBaseType;
import net.opengis.ows11.DatasetDescriptionSummaryBaseType;
import net.opengis.ows11.DescriptionType;
import net.opengis.ows11.OnlineResourceType;

import net.opengis.wmts.v_1.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wmts.v_1.wmtsv_1Package
 * @generated
 */
public class wmtsv_1AdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static wmtsv_1Package modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public wmtsv_1AdapterFactory() {
        if (modelPackage == null) {
            modelPackage = wmtsv_1Package.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected wmtsv_1Switch<Adapter> modelSwitch =
        new wmtsv_1Switch<Adapter>() {
            @Override
            public Adapter caseBinaryPayloadType(BinaryPayloadType object) {
                return createBinaryPayloadTypeAdapter();
            }
            @Override
            public Adapter caseCapabilitiesType(CapabilitiesType object) {
                return createCapabilitiesTypeAdapter();
            }
            @Override
            public Adapter caseContentsType(ContentsType object) {
                return createContentsTypeAdapter();
            }
            @Override
            public Adapter caseDimensionNameValueType(DimensionNameValueType object) {
                return createDimensionNameValueTypeAdapter();
            }
            @Override
            public Adapter caseDimensionType(DimensionType object) {
                return createDimensionTypeAdapter();
            }
            @Override
            public Adapter caseDocumentRoot(DocumentRoot object) {
                return createDocumentRootAdapter();
            }
            @Override
            public Adapter caseFeatureInfoResponseType(FeatureInfoResponseType object) {
                return createFeatureInfoResponseTypeAdapter();
            }
            @Override
            public Adapter caseGetCapabilitiesType(GetCapabilitiesType object) {
                return createGetCapabilitiesTypeAdapter();
            }
            @Override
            public Adapter caseGetFeatureInfoType(GetFeatureInfoType object) {
                return createGetFeatureInfoTypeAdapter();
            }
            @Override
            public Adapter caseGetTileType(GetTileType object) {
                return createGetTileTypeAdapter();
            }
            @Override
            public Adapter caseLayerType(LayerType object) {
                return createLayerTypeAdapter();
            }
            @Override
            public Adapter caseLegendURLType(LegendURLType object) {
                return createLegendURLTypeAdapter();
            }
            @Override
            public Adapter caseStyleType(StyleType object) {
                return createStyleTypeAdapter();
            }
            @Override
            public Adapter caseTextPayloadType(TextPayloadType object) {
                return createTextPayloadTypeAdapter();
            }
            @Override
            public Adapter caseThemesType(ThemesType object) {
                return createThemesTypeAdapter();
            }
            @Override
            public Adapter caseThemeType(ThemeType object) {
                return createThemeTypeAdapter();
            }
            @Override
            public Adapter caseTileMatrixLimitsType(TileMatrixLimitsType object) {
                return createTileMatrixLimitsTypeAdapter();
            }
            @Override
            public Adapter caseTileMatrixSetLimitsType(TileMatrixSetLimitsType object) {
                return createTileMatrixSetLimitsTypeAdapter();
            }
            @Override
            public Adapter caseTileMatrixSetLinkType(TileMatrixSetLinkType object) {
                return createTileMatrixSetLinkTypeAdapter();
            }
            @Override
            public Adapter caseTileMatrixSetType(TileMatrixSetType object) {
                return createTileMatrixSetTypeAdapter();
            }
            @Override
            public Adapter caseTileMatrixType(TileMatrixType object) {
                return createTileMatrixTypeAdapter();
            }
            @Override
            public Adapter caseURLTemplateType(URLTemplateType object) {
                return createURLTemplateTypeAdapter();
            }
            @Override
            public Adapter caseCapabilitiesBaseType(CapabilitiesBaseType object) {
                return createCapabilitiesBaseTypeAdapter();
            }
            @Override
            public Adapter caseContentsBaseType(ContentsBaseType object) {
                return createContentsBaseTypeAdapter();
            }
            @Override
            public Adapter caseDescriptionType(DescriptionType object) {
                return createDescriptionTypeAdapter();
            }
            @Override
            public Adapter caseOws11_GetCapabilitiesType(net.opengis.ows11.GetCapabilitiesType object) {
                return createOws11_GetCapabilitiesTypeAdapter();
            }
            @Override
            public Adapter caseDatasetDescriptionSummaryBaseType(DatasetDescriptionSummaryBaseType object) {
                return createDatasetDescriptionSummaryBaseTypeAdapter();
            }
            @Override
            public Adapter caseOnlineResourceType(OnlineResourceType object) {
                return createOnlineResourceTypeAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.BinaryPayloadType <em>Binary Payload Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.BinaryPayloadType
     * @generated
     */
    public Adapter createBinaryPayloadTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.CapabilitiesType <em>Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.CapabilitiesType
     * @generated
     */
    public Adapter createCapabilitiesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.ContentsType <em>Contents Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.ContentsType
     * @generated
     */
    public Adapter createContentsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.DimensionNameValueType <em>Dimension Name Value Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.DimensionNameValueType
     * @generated
     */
    public Adapter createDimensionNameValueTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.DimensionType <em>Dimension Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.DimensionType
     * @generated
     */
    public Adapter createDimensionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.DocumentRoot
     * @generated
     */
    public Adapter createDocumentRootAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.FeatureInfoResponseType <em>Feature Info Response Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.FeatureInfoResponseType
     * @generated
     */
    public Adapter createFeatureInfoResponseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.GetCapabilitiesType
     * @generated
     */
    public Adapter createGetCapabilitiesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.GetFeatureInfoType <em>Get Feature Info Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.GetFeatureInfoType
     * @generated
     */
    public Adapter createGetFeatureInfoTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.GetTileType <em>Get Tile Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.GetTileType
     * @generated
     */
    public Adapter createGetTileTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.LayerType <em>Layer Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.LayerType
     * @generated
     */
    public Adapter createLayerTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.LegendURLType <em>Legend URL Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.LegendURLType
     * @generated
     */
    public Adapter createLegendURLTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.StyleType <em>Style Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.StyleType
     * @generated
     */
    public Adapter createStyleTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.TextPayloadType <em>Text Payload Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.TextPayloadType
     * @generated
     */
    public Adapter createTextPayloadTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.ThemesType <em>Themes Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.ThemesType
     * @generated
     */
    public Adapter createThemesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.ThemeType <em>Theme Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.ThemeType
     * @generated
     */
    public Adapter createThemeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.TileMatrixLimitsType <em>Tile Matrix Limits Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.TileMatrixLimitsType
     * @generated
     */
    public Adapter createTileMatrixLimitsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.TileMatrixSetLimitsType <em>Tile Matrix Set Limits Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.TileMatrixSetLimitsType
     * @generated
     */
    public Adapter createTileMatrixSetLimitsTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.TileMatrixSetLinkType <em>Tile Matrix Set Link Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.TileMatrixSetLinkType
     * @generated
     */
    public Adapter createTileMatrixSetLinkTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.TileMatrixSetType <em>Tile Matrix Set Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.TileMatrixSetType
     * @generated
     */
    public Adapter createTileMatrixSetTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.TileMatrixType <em>Tile Matrix Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.TileMatrixType
     * @generated
     */
    public Adapter createTileMatrixTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wmts.v_1.URLTemplateType <em>URL Template Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wmts.v_1.URLTemplateType
     * @generated
     */
    public Adapter createURLTemplateTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows11.CapabilitiesBaseType <em>Capabilities Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows11.CapabilitiesBaseType
     * @generated
     */
    public Adapter createCapabilitiesBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows11.ContentsBaseType <em>Contents Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows11.ContentsBaseType
     * @generated
     */
    public Adapter createContentsBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows11.DescriptionType <em>Description Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows11.DescriptionType
     * @generated
     */
    public Adapter createDescriptionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows11.GetCapabilitiesType <em>Get Capabilities Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows11.GetCapabilitiesType
     * @generated
     */
    public Adapter createOws11_GetCapabilitiesTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows11.DatasetDescriptionSummaryBaseType <em>Dataset Description Summary Base Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows11.DatasetDescriptionSummaryBaseType
     * @generated
     */
    public Adapter createDatasetDescriptionSummaryBaseTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.ows11.OnlineResourceType <em>Online Resource Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.ows11.OnlineResourceType
     * @generated
     */
    public Adapter createOnlineResourceTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //wmtsv_1AdapterFactory
