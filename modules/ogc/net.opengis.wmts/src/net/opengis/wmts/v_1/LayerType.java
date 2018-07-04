/**
 */
package net.opengis.wmts.v_1;

import net.opengis.ows11.DatasetDescriptionSummaryBaseType;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Layer Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.LayerType#getStyle <em>Style</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.LayerType#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.LayerType#getInfoFormat <em>Info Format</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.LayerType#getDimension <em>Dimension</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.LayerType#getTileMatrixSetLink <em>Tile Matrix Set Link</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.LayerType#getResourceURL <em>Resource URL</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getLayerType()
 * @model extendedMetaData="name='LayerType' kind='elementOnly'"
 * @generated
 */
public interface LayerType extends DatasetDescriptionSummaryBaseType {
    /**
     * Returns the value of the '<em><b>Style</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wmts.v_1.StyleType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Metadata about the styles of this layer
     * <!-- end-model-doc -->
     * @return the value of the '<em>Style</em>' containment reference list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getLayerType_Style()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Style' namespace='##targetNamespace'"
     * @generated
     */
    EList<StyleType> getStyle();

    /**
     * Returns the value of the '<em><b>Format</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Supported valid output MIME types for a tile
     * <!-- end-model-doc -->
     * @return the value of the '<em>Format</em>' attribute list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getLayerType_Format()
     * @model unique="false" dataType="net.opengis.ows11.MimeType" required="true"
     *        extendedMetaData="kind='element' name='Format' namespace='##targetNamespace'"
     * @generated
     */
    EList<String> getFormat();

    /**
     * Returns the value of the '<em><b>Info Format</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 							Supported valid output MIME types for a FeatureInfo. 
     * 							If there isn't any, The server do not support FeatureInfo requests
     * 							for this layer.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Info Format</em>' attribute list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getLayerType_InfoFormat()
     * @model unique="false" dataType="net.opengis.ows11.MimeType"
     *        extendedMetaData="kind='element' name='InfoFormat' namespace='##targetNamespace'"
     * @generated
     */
    EList<String> getInfoFormat();

    /**
     * Returns the value of the '<em><b>Dimension</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wmts.v_1.DimensionType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Extra dimensions for a tile and FeatureInfo requests.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Dimension</em>' containment reference list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getLayerType_Dimension()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Dimension' namespace='##targetNamespace'"
     * @generated
     */
    EList<DimensionType> getDimension();

    /**
     * Returns the value of the '<em><b>Tile Matrix Set Link</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wmts.v_1.TileMatrixSetLinkType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to a tileMatrixSet and limits
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Matrix Set Link</em>' containment reference list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getLayerType_TileMatrixSetLink()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='TileMatrixSetLink' namespace='##targetNamespace'"
     * @generated
     */
    EList<TileMatrixSetLinkType> getTileMatrixSetLink();

    /**
     * Returns the value of the '<em><b>Resource URL</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wmts.v_1.URLTemplateType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 								URL template to a tile or a FeatureInfo resource on 
     * 								resource oriented architectural style
     * 							
     * <!-- end-model-doc -->
     * @return the value of the '<em>Resource URL</em>' containment reference list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getLayerType_ResourceURL()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ResourceURL' namespace='##targetNamespace'"
     * @generated
     */
    EList<URLTemplateType> getResourceURL();

} // LayerType
