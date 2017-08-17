/**
 */
package net.opengis.wmts.v_1;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Tile Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.GetTileType#getLayer <em>Layer</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.GetTileType#getStyle <em>Style</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.GetTileType#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.GetTileType#getDimensionNameValue <em>Dimension Name Value</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.GetTileType#getTileMatrixSet <em>Tile Matrix Set</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.GetTileType#getTileMatrix <em>Tile Matrix</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.GetTileType#getTileRow <em>Tile Row</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.GetTileType#getTileCol <em>Tile Col</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.GetTileType#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.GetTileType#getVersion <em>Version</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetTileType()
 * @model extendedMetaData="name='GetTile_._type' kind='elementOnly'"
 * @generated
 */
public interface GetTileType extends EObject {
    /**
     * Returns the value of the '<em><b>Layer</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A layer identifier has to be referenced
     * <!-- end-model-doc -->
     * @return the value of the '<em>Layer</em>' attribute.
     * @see #setLayer(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetTileType_Layer()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='Layer' namespace='##targetNamespace'"
     * @generated
     */
    String getLayer();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.GetTileType#getLayer <em>Layer</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Layer</em>' attribute.
     * @see #getLayer()
     * @generated
     */
    void setLayer(String value);

    /**
     * Returns the value of the '<em><b>Style</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A style identifier has to be referenced.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Style</em>' attribute.
     * @see #setStyle(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetTileType_Style()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='Style' namespace='##targetNamespace'"
     * @generated
     */
    String getStyle();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.GetTileType#getStyle <em>Style</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Style</em>' attribute.
     * @see #getStyle()
     * @generated
     */
    void setStyle(String value);

    /**
     * Returns the value of the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Output format of the tile
     * <!-- end-model-doc -->
     * @return the value of the '<em>Format</em>' attribute.
     * @see #setFormat(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetTileType_Format()
     * @model dataType="net.opengis.ows11.MimeType" required="true"
     *        extendedMetaData="kind='element' name='Format' namespace='##targetNamespace'"
     * @generated
     */
    String getFormat();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.GetTileType#getFormat <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Format</em>' attribute.
     * @see #getFormat()
     * @generated
     */
    void setFormat(String value);

    /**
     * Returns the value of the '<em><b>Dimension Name Value</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wmts.v_1.DimensionNameValueType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Dimension name and value
     * <!-- end-model-doc -->
     * @return the value of the '<em>Dimension Name Value</em>' containment reference list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetTileType_DimensionNameValue()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='DimensionNameValue' namespace='##targetNamespace'"
     * @generated
     */
    EList<DimensionNameValueType> getDimensionNameValue();

    /**
     * Returns the value of the '<em><b>Tile Matrix Set</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A TileMatrixSet identifier has to be referenced
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Matrix Set</em>' attribute.
     * @see #setTileMatrixSet(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetTileType_TileMatrixSet()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='TileMatrixSet' namespace='##targetNamespace'"
     * @generated
     */
    String getTileMatrixSet();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.GetTileType#getTileMatrixSet <em>Tile Matrix Set</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tile Matrix Set</em>' attribute.
     * @see #getTileMatrixSet()
     * @generated
     */
    void setTileMatrixSet(String value);

    /**
     * Returns the value of the '<em><b>Tile Matrix</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A TileMatrix identifier has to be referenced
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Matrix</em>' attribute.
     * @see #setTileMatrix(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetTileType_TileMatrix()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='TileMatrix' namespace='##targetNamespace'"
     * @generated
     */
    String getTileMatrix();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.GetTileType#getTileMatrix <em>Tile Matrix</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tile Matrix</em>' attribute.
     * @see #getTileMatrix()
     * @generated
     */
    void setTileMatrix(String value);

    /**
     * Returns the value of the '<em><b>Tile Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Row index of tile matrix
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Row</em>' attribute.
     * @see #setTileRow(BigInteger)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetTileType_TileRow()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger" required="true"
     *        extendedMetaData="kind='element' name='TileRow' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getTileRow();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.GetTileType#getTileRow <em>Tile Row</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tile Row</em>' attribute.
     * @see #getTileRow()
     * @generated
     */
    void setTileRow(BigInteger value);

    /**
     * Returns the value of the '<em><b>Tile Col</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Column index of tile matrix
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Col</em>' attribute.
     * @see #setTileCol(BigInteger)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetTileType_TileCol()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger" required="true"
     *        extendedMetaData="kind='element' name='TileCol' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getTileCol();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.GetTileType#getTileCol <em>Tile Col</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tile Col</em>' attribute.
     * @see #getTileCol()
     * @generated
     */
    void setTileCol(BigInteger value);

    /**
     * Returns the value of the '<em><b>Service</b></em>' attribute.
     * The default value is <code>"WMTS"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Service</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Service</em>' attribute.
     * @see #isSetService()
     * @see #unsetService()
     * @see #setService(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetTileType_Service()
     * @model default="WMTS" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='service'"
     * @generated
     */
    String getService();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.GetTileType#getService <em>Service</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service</em>' attribute.
     * @see #isSetService()
     * @see #unsetService()
     * @see #getService()
     * @generated
     */
    void setService(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wmts.v_1.GetTileType#getService <em>Service</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetService()
     * @see #getService()
     * @see #setService(String)
     * @generated
     */
    void unsetService();

    /**
     * Returns whether the value of the '{@link net.opengis.wmts.v_1.GetTileType#getService <em>Service</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Service</em>' attribute is set.
     * @see #unsetService()
     * @see #getService()
     * @see #setService(String)
     * @generated
     */
    boolean isSetService();

    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * The default value is <code>"1.0.0"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #isSetVersion()
     * @see #unsetVersion()
     * @see #setVersion(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetTileType_Version()
     * @model default="1.0.0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='version'"
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.GetTileType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #isSetVersion()
     * @see #unsetVersion()
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wmts.v_1.GetTileType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetVersion()
     * @see #getVersion()
     * @see #setVersion(String)
     * @generated
     */
    void unsetVersion();

    /**
     * Returns whether the value of the '{@link net.opengis.wmts.v_1.GetTileType#getVersion <em>Version</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Version</em>' attribute is set.
     * @see #unsetVersion()
     * @see #getVersion()
     * @see #setVersion(String)
     * @generated
     */
    boolean isSetVersion();

} // GetTileType
