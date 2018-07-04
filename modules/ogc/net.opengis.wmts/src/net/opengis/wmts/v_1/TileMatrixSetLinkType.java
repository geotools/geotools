/**
 */
package net.opengis.wmts.v_1;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tile Matrix Set Link Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixSetLinkType#getTileMatrixSet <em>Tile Matrix Set</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixSetLinkType#getTileMatrixSetLimits <em>Tile Matrix Set Limits</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixSetLinkType()
 * @model extendedMetaData="name='TileMatrixSetLink_._type' kind='elementOnly'"
 * @generated
 */
public interface TileMatrixSetLinkType extends EObject {
    /**
     * Returns the value of the '<em><b>Tile Matrix Set</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to a tileMatrixSet
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Matrix Set</em>' attribute.
     * @see #setTileMatrixSet(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixSetLinkType_TileMatrixSet()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='TileMatrixSet' namespace='##targetNamespace'"
     * @generated
     */
    String getTileMatrixSet();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixSetLinkType#getTileMatrixSet <em>Tile Matrix Set</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tile Matrix Set</em>' attribute.
     * @see #getTileMatrixSet()
     * @generated
     */
    void setTileMatrixSet(String value);

    /**
     * Returns the value of the '<em><b>Tile Matrix Set Limits</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indices limits for this tileMatrixSet. The absence of this 
     * 						element means that tile row and tile col indices are only limited by 0 
     * 						and the corresponding tileMatrixSet maximum definitions.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Matrix Set Limits</em>' containment reference.
     * @see #setTileMatrixSetLimits(TileMatrixSetLimitsType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixSetLinkType_TileMatrixSetLimits()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='TileMatrixSetLimits' namespace='##targetNamespace'"
     * @generated
     */
    TileMatrixSetLimitsType getTileMatrixSetLimits();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixSetLinkType#getTileMatrixSetLimits <em>Tile Matrix Set Limits</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tile Matrix Set Limits</em>' containment reference.
     * @see #getTileMatrixSetLimits()
     * @generated
     */
    void setTileMatrixSetLimits(TileMatrixSetLimitsType value);

} // TileMatrixSetLinkType
