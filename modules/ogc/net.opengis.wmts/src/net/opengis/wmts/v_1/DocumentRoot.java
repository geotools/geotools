/**
 */
package net.opengis.wmts.v_1;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getBinaryPayload <em>Binary Payload</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getCapabilities <em>Capabilities</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getDimension <em>Dimension</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getDimensionNameValue <em>Dimension Name Value</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getFeatureInfoResponse <em>Feature Info Response</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getGetFeatureInfo <em>Get Feature Info</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getGetTile <em>Get Tile</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getLayer <em>Layer</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getLegendURL <em>Legend URL</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getStyle <em>Style</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getTextPayload <em>Text Payload</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getTheme <em>Theme</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getThemes <em>Themes</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getTileMatrix <em>Tile Matrix</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getTileMatrixLimits <em>Tile Matrix Limits</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getTileMatrixSet <em>Tile Matrix Set</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getTileMatrixSetLimits <em>Tile Matrix Set Limits</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DocumentRoot#getTileMatrixSetLink <em>Tile Matrix Set Link</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XMLNS Prefix Map</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>XMLNS Prefix Map</em>' map.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_XMLNSPrefixMap()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>" transient="true"
     *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
     * @generated
     */
    EMap<String, String> getXMLNSPrefixMap();

    /**
     * Returns the value of the '<em><b>XSI Schema Location</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XSI Schema Location</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>XSI Schema Location</em>' map.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_XSISchemaLocation()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>" transient="true"
     *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
     * @generated
     */
    EMap<String, String> getXSISchemaLocation();

    /**
     * Returns the value of the '<em><b>Binary Payload</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Binary Payload</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Binary Payload</em>' containment reference.
     * @see #setBinaryPayload(BinaryPayloadType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_BinaryPayload()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BinaryPayload' namespace='##targetNamespace'"
     * @generated
     */
    BinaryPayloadType getBinaryPayload();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getBinaryPayload <em>Binary Payload</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Binary Payload</em>' containment reference.
     * @see #getBinaryPayload()
     * @generated
     */
    void setBinaryPayload(BinaryPayloadType value);

    /**
     * Returns the value of the '<em><b>Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * XML defines the WMTS GetCapabilities operation response. 
     * 			ServiceMetadata document provides clients with service metadata about a specific service 
     * 			instance, including metadata about the tightly-coupled data served. If the server 
     * 			does not implement the updateSequence parameter, the server SHALL always 
     * 			return the complete Capabilities document, without the updateSequence parameter. 
     * 			When the server implements the updateSequence parameter and the 
     * 			GetCapabilities operation request included the updateSequence parameter 
     * 			with the current value, the server SHALL return this element with only the 
     * 			"version" and "updateSequence" attributes. Otherwise, all optional elements 
     * 			SHALL be included or not depending on the actual value of the Contents 
     * 			parameter in the GetCapabilities operation request.
     * 			
     * <!-- end-model-doc -->
     * @return the value of the '<em>Capabilities</em>' containment reference.
     * @see #setCapabilities(CapabilitiesType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_Capabilities()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Capabilities' namespace='##targetNamespace'"
     * @generated
     */
    CapabilitiesType getCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getCapabilities <em>Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Capabilities</em>' containment reference.
     * @see #getCapabilities()
     * @generated
     */
    void setCapabilities(CapabilitiesType value);

    /**
     * Returns the value of the '<em><b>Dimension</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 				Metadata about a particular dimension that the tiles of 
     * 				a layer are available.
     * 			
     * <!-- end-model-doc -->
     * @return the value of the '<em>Dimension</em>' containment reference.
     * @see #setDimension(DimensionType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_Dimension()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Dimension' namespace='##targetNamespace'"
     * @generated
     */
    DimensionType getDimension();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getDimension <em>Dimension</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Dimension</em>' containment reference.
     * @see #getDimension()
     * @generated
     */
    void setDimension(DimensionType value);

    /**
     * Returns the value of the '<em><b>Dimension Name Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Dimension Name Value</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Dimension Name Value</em>' containment reference.
     * @see #setDimensionNameValue(DimensionNameValueType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_DimensionNameValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DimensionNameValue' namespace='##targetNamespace'"
     * @generated
     */
    DimensionNameValueType getDimensionNameValue();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getDimensionNameValue <em>Dimension Name Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Dimension Name Value</em>' containment reference.
     * @see #getDimensionNameValue()
     * @generated
     */
    void setDimensionNameValue(DimensionNameValueType value);

    /**
     * Returns the value of the '<em><b>Feature Info Response</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature Info Response</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Info Response</em>' containment reference.
     * @see #setFeatureInfoResponse(FeatureInfoResponseType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_FeatureInfoResponse()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='FeatureInfoResponse' namespace='##targetNamespace'"
     * @generated
     */
    FeatureInfoResponseType getFeatureInfoResponse();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getFeatureInfoResponse <em>Feature Info Response</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Info Response</em>' containment reference.
     * @see #getFeatureInfoResponse()
     * @generated
     */
    void setFeatureInfoResponse(FeatureInfoResponseType value);

    /**
     * Returns the value of the '<em><b>Get Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * WMTS GetCapabilities operation request.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Get Capabilities</em>' containment reference.
     * @see #setGetCapabilities(GetCapabilitiesType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_GetCapabilities()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetCapabilities' namespace='##targetNamespace'"
     * @generated
     */
    GetCapabilitiesType getGetCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Capabilities</em>' containment reference.
     * @see #getGetCapabilities()
     * @generated
     */
    void setGetCapabilities(GetCapabilitiesType value);

    /**
     * Returns the value of the '<em><b>Get Feature Info</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Get Feature Info</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Get Feature Info</em>' containment reference.
     * @see #setGetFeatureInfo(GetFeatureInfoType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_GetFeatureInfo()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetFeatureInfo' namespace='##targetNamespace'"
     * @generated
     */
    GetFeatureInfoType getGetFeatureInfo();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getGetFeatureInfo <em>Get Feature Info</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Feature Info</em>' containment reference.
     * @see #getGetFeatureInfo()
     * @generated
     */
    void setGetFeatureInfo(GetFeatureInfoType value);

    /**
     * Returns the value of the '<em><b>Get Tile</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Get Tile</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Get Tile</em>' containment reference.
     * @see #setGetTile(GetTileType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_GetTile()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetTile' namespace='##targetNamespace'"
     * @generated
     */
    GetTileType getGetTile();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getGetTile <em>Get Tile</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Tile</em>' containment reference.
     * @see #getGetTile()
     * @generated
     */
    void setGetTile(GetTileType value);

    /**
     * Returns the value of the '<em><b>Layer</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Layer</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Layer</em>' containment reference.
     * @see #setLayer(LayerType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_Layer()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Layer' namespace='##targetNamespace' affiliation='http://www.opengis.net/ows/1.1#DatasetDescriptionSummary'"
     * @generated
     */
    LayerType getLayer();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getLayer <em>Layer</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Layer</em>' containment reference.
     * @see #getLayer()
     * @generated
     */
    void setLayer(LayerType value);

    /**
     * Returns the value of the '<em><b>Legend URL</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *         Zero or more LegendURL elements may be provided, providing an
     *         image(s) of a legend relevant to each Style of a Layer.  The Format
     *         element indicates the MIME type of the legend. minScaleDenominator
     *         and maxScaleDenominator attributes may be provided to indicate to
     *         the client which scale(s) (inclusive) the legend image is appropriate
     *         for.  (If provided, these values must exactly match the scale
     *         denominators of available TileMatrixes.)  width and height
     *         attributes may be provided to assist client applications in laying
     *         out space to display the legend.
     *       
     * <!-- end-model-doc -->
     * @return the value of the '<em>Legend URL</em>' containment reference.
     * @see #setLegendURL(LegendURLType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_LegendURL()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='LegendURL' namespace='##targetNamespace'"
     * @generated
     */
    LegendURLType getLegendURL();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getLegendURL <em>Legend URL</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Legend URL</em>' containment reference.
     * @see #getLegendURL()
     * @generated
     */
    void setLegendURL(LegendURLType value);

    /**
     * Returns the value of the '<em><b>Style</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Style</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Style</em>' containment reference.
     * @see #setStyle(StyleType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_Style()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Style' namespace='##targetNamespace'"
     * @generated
     */
    StyleType getStyle();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getStyle <em>Style</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Style</em>' containment reference.
     * @see #getStyle()
     * @generated
     */
    void setStyle(StyleType value);

    /**
     * Returns the value of the '<em><b>Text Payload</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Text Payload</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Text Payload</em>' containment reference.
     * @see #setTextPayload(TextPayloadType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_TextPayload()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TextPayload' namespace='##targetNamespace'"
     * @generated
     */
    TextPayloadType getTextPayload();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getTextPayload <em>Text Payload</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Text Payload</em>' containment reference.
     * @see #getTextPayload()
     * @generated
     */
    void setTextPayload(TextPayloadType value);

    /**
     * Returns the value of the '<em><b>Theme</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Theme</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Theme</em>' containment reference.
     * @see #setTheme(ThemeType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_Theme()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Theme' namespace='##targetNamespace'"
     * @generated
     */
    ThemeType getTheme();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getTheme <em>Theme</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Theme</em>' containment reference.
     * @see #getTheme()
     * @generated
     */
    void setTheme(ThemeType value);

    /**
     * Returns the value of the '<em><b>Themes</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 				Provides a set of hierarchical themes that the 
     * 				client can use to categorize the layers by.
     * 			
     * <!-- end-model-doc -->
     * @return the value of the '<em>Themes</em>' containment reference.
     * @see #setThemes(ThemesType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_Themes()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Themes' namespace='##targetNamespace'"
     * @generated
     */
    ThemesType getThemes();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getThemes <em>Themes</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Themes</em>' containment reference.
     * @see #getThemes()
     * @generated
     */
    void setThemes(ThemesType value);

    /**
     * Returns the value of the '<em><b>Tile Matrix</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Describes a particular tile matrix.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Matrix</em>' containment reference.
     * @see #setTileMatrix(TileMatrixType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_TileMatrix()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TileMatrix' namespace='##targetNamespace'"
     * @generated
     */
    TileMatrixType getTileMatrix();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getTileMatrix <em>Tile Matrix</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tile Matrix</em>' containment reference.
     * @see #getTileMatrix()
     * @generated
     */
    void setTileMatrix(TileMatrixType value);

    /**
     * Returns the value of the '<em><b>Tile Matrix Limits</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Metadata describing the limits of a TileMatrix 
     * 						for this layer.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Matrix Limits</em>' containment reference.
     * @see #setTileMatrixLimits(TileMatrixLimitsType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_TileMatrixLimits()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TileMatrixLimits' namespace='##targetNamespace'"
     * @generated
     */
    TileMatrixLimitsType getTileMatrixLimits();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getTileMatrixLimits <em>Tile Matrix Limits</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tile Matrix Limits</em>' containment reference.
     * @see #getTileMatrixLimits()
     * @generated
     */
    void setTileMatrixLimits(TileMatrixLimitsType value);

    /**
     * Returns the value of the '<em><b>Tile Matrix Set</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Describes a particular set of tile matrices.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Matrix Set</em>' containment reference.
     * @see #setTileMatrixSet(TileMatrixSetType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_TileMatrixSet()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TileMatrixSet' namespace='##targetNamespace'"
     * @generated
     */
    TileMatrixSetType getTileMatrixSet();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getTileMatrixSet <em>Tile Matrix Set</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tile Matrix Set</em>' containment reference.
     * @see #getTileMatrixSet()
     * @generated
     */
    void setTileMatrixSet(TileMatrixSetType value);

    /**
     * Returns the value of the '<em><b>Tile Matrix Set Limits</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 				Metadata about a the limits of the tile row and tile col indices.
     * 			
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Matrix Set Limits</em>' containment reference.
     * @see #setTileMatrixSetLimits(TileMatrixSetLimitsType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_TileMatrixSetLimits()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TileMatrixSetLimits' namespace='##targetNamespace'"
     * @generated
     */
    TileMatrixSetLimitsType getTileMatrixSetLimits();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getTileMatrixSetLimits <em>Tile Matrix Set Limits</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tile Matrix Set Limits</em>' containment reference.
     * @see #getTileMatrixSetLimits()
     * @generated
     */
    void setTileMatrixSetLimits(TileMatrixSetLimitsType value);

    /**
     * Returns the value of the '<em><b>Tile Matrix Set Link</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Metadata about the TileMatrixSet reference.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Matrix Set Link</em>' containment reference.
     * @see #setTileMatrixSetLink(TileMatrixSetLinkType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDocumentRoot_TileMatrixSetLink()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TileMatrixSetLink' namespace='##targetNamespace'"
     * @generated
     */
    TileMatrixSetLinkType getTileMatrixSetLink();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DocumentRoot#getTileMatrixSetLink <em>Tile Matrix Set Link</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tile Matrix Set Link</em>' containment reference.
     * @see #getTileMatrixSetLink()
     * @generated
     */
    void setTileMatrixSetLink(TileMatrixSetLinkType value);

} // DocumentRoot
