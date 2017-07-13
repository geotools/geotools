/**
 */
package net.opengis.wmts.v_1;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tile Matrix Limits Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixLimitsType#getTileMatrix <em>Tile Matrix</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixLimitsType#getMinTileRow <em>Min Tile Row</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixLimitsType#getMaxTileRow <em>Max Tile Row</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixLimitsType#getMinTileCol <em>Min Tile Col</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixLimitsType#getMaxTileCol <em>Max Tile Col</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixLimitsType()
 * @model extendedMetaData="name='TileMatrixLimits_._type' kind='elementOnly'"
 * @generated
 */
public interface TileMatrixLimitsType extends EObject {
    /**
     * Returns the value of the '<em><b>Tile Matrix</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to a TileMatrix identifier
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Matrix</em>' attribute.
     * @see #setTileMatrix(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixLimitsType_TileMatrix()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='TileMatrix' namespace='##targetNamespace'"
     * @generated
     */
    String getTileMatrix();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixLimitsType#getTileMatrix <em>Tile Matrix</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tile Matrix</em>' attribute.
     * @see #getTileMatrix()
     * @generated
     */
    void setTileMatrix(String value);

    /**
     * Returns the value of the '<em><b>Min Tile Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Minimum tile row index valid for this 
     * 						layer. From 0 to maxTileRow
     * <!-- end-model-doc -->
     * @return the value of the '<em>Min Tile Row</em>' attribute.
     * @see #setMinTileRow(BigInteger)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixLimitsType_MinTileRow()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
     *        extendedMetaData="kind='element' name='MinTileRow' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getMinTileRow();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixLimitsType#getMinTileRow <em>Min Tile Row</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Min Tile Row</em>' attribute.
     * @see #getMinTileRow()
     * @generated
     */
    void setMinTileRow(BigInteger value);

    /**
     * Returns the value of the '<em><b>Max Tile Row</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Maximim tile row index valid for this 
     * 						layer. From minTileRow to matrixWidth-1 of the tileMatrix 
     * 						section of this tileMatrixSet
     * <!-- end-model-doc -->
     * @return the value of the '<em>Max Tile Row</em>' attribute.
     * @see #setMaxTileRow(BigInteger)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixLimitsType_MaxTileRow()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
     *        extendedMetaData="kind='element' name='MaxTileRow' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getMaxTileRow();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixLimitsType#getMaxTileRow <em>Max Tile Row</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Max Tile Row</em>' attribute.
     * @see #getMaxTileRow()
     * @generated
     */
    void setMaxTileRow(BigInteger value);

    /**
     * Returns the value of the '<em><b>Min Tile Col</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Minimum tile column index valid for this 
     * 						layer. From 0 to maxTileCol
     * <!-- end-model-doc -->
     * @return the value of the '<em>Min Tile Col</em>' attribute.
     * @see #setMinTileCol(BigInteger)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixLimitsType_MinTileCol()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
     *        extendedMetaData="kind='element' name='MinTileCol' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getMinTileCol();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixLimitsType#getMinTileCol <em>Min Tile Col</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Min Tile Col</em>' attribute.
     * @see #getMinTileCol()
     * @generated
     */
    void setMinTileCol(BigInteger value);

    /**
     * Returns the value of the '<em><b>Max Tile Col</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Maximim tile column index valid for this layer. 
     * 						From minTileCol to tileHeight-1 of the tileMatrix section 
     * 						of this tileMatrixSet.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Max Tile Col</em>' attribute.
     * @see #setMaxTileCol(BigInteger)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixLimitsType_MaxTileCol()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
     *        extendedMetaData="kind='element' name='MaxTileCol' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getMaxTileCol();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixLimitsType#getMaxTileCol <em>Max Tile Col</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Max Tile Col</em>' attribute.
     * @see #getMaxTileCol()
     * @generated
     */
    void setMaxTileCol(BigInteger value);

} // TileMatrixLimitsType
