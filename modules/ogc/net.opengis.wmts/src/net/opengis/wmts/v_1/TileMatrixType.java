/**
 */
package net.opengis.wmts.v_1;

import java.math.BigInteger;

import java.util.List;

import net.opengis.ows11.CodeType;
import net.opengis.ows11.DescriptionType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tile Matrix Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixType#getScaleDenominator <em>Scale Denominator</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixType#getTopLeftCorner <em>Top Left Corner</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixType#getTileWidth <em>Tile Width</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixType#getTileHeight <em>Tile Height</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixType#getMatrixWidth <em>Matrix Width</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.TileMatrixType#getMatrixHeight <em>Matrix Height</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixType()
 * @model extendedMetaData="name='TileMatrix_._type' kind='elementOnly'"
 * @generated
 */
public interface TileMatrixType extends DescriptionType {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Tile matrix identifier. Typically an abreviation of 
     * 								the ScaleDenominator value or its equivalent pixel size
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference.
     * @see #setIdentifier(CodeType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixType_Identifier()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    CodeType getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixType#getIdentifier <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' containment reference.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(CodeType value);

    /**
     * Returns the value of the '<em><b>Scale Denominator</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Scale denominator level of this tile matrix
     * <!-- end-model-doc -->
     * @return the value of the '<em>Scale Denominator</em>' attribute.
     * @see #isSetScaleDenominator()
     * @see #unsetScaleDenominator()
     * @see #setScaleDenominator(double)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixType_ScaleDenominator()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double" required="true"
     *        extendedMetaData="kind='element' name='ScaleDenominator' namespace='##targetNamespace'"
     * @generated
     */
    double getScaleDenominator();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixType#getScaleDenominator <em>Scale Denominator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scale Denominator</em>' attribute.
     * @see #isSetScaleDenominator()
     * @see #unsetScaleDenominator()
     * @see #getScaleDenominator()
     * @generated
     */
    void setScaleDenominator(double value);

    /**
     * Unsets the value of the '{@link net.opengis.wmts.v_1.TileMatrixType#getScaleDenominator <em>Scale Denominator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetScaleDenominator()
     * @see #getScaleDenominator()
     * @see #setScaleDenominator(double)
     * @generated
     */
    void unsetScaleDenominator();

    /**
     * Returns whether the value of the '{@link net.opengis.wmts.v_1.TileMatrixType#getScaleDenominator <em>Scale Denominator</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Scale Denominator</em>' attribute is set.
     * @see #unsetScaleDenominator()
     * @see #getScaleDenominator()
     * @see #setScaleDenominator(double)
     * @generated
     */
    boolean isSetScaleDenominator();

    /**
     * Returns the value of the '<em><b>Top Left Corner</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 									Position in CRS coordinates of the top-left corner of this tile matrix. 
     * 									This are the  precise coordinates of the top left corner of top left 
     * 									pixel of the 0,0 tile in SupportedCRS coordinates of this TileMatrixSet.
     * 								
     * <!-- end-model-doc -->
     * @return the value of the '<em>Top Left Corner</em>' attribute.
     * @see #setTopLeftCorner(List)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixType_TopLeftCorner()
     * @model dataType="net.opengis.ows11.PositionType" required="true" many="false"
     *        extendedMetaData="kind='element' name='TopLeftCorner' namespace='##targetNamespace'"
     * @generated
     */
    List<Double> getTopLeftCorner();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixType#getTopLeftCorner <em>Top Left Corner</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Top Left Corner</em>' attribute.
     * @see #getTopLeftCorner()
     * @generated
     */
    void setTopLeftCorner(List<Double> value);

    /**
     * Returns the value of the '<em><b>Tile Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Width of each tile of this tile matrix in pixels.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Width</em>' attribute.
     * @see #setTileWidth(BigInteger)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixType_TileWidth()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
     *        extendedMetaData="kind='element' name='TileWidth' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getTileWidth();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixType#getTileWidth <em>Tile Width</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tile Width</em>' attribute.
     * @see #getTileWidth()
     * @generated
     */
    void setTileWidth(BigInteger value);

    /**
     * Returns the value of the '<em><b>Tile Height</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Height of each tile of this tile matrix in pixels
     * <!-- end-model-doc -->
     * @return the value of the '<em>Tile Height</em>' attribute.
     * @see #setTileHeight(BigInteger)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixType_TileHeight()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
     *        extendedMetaData="kind='element' name='TileHeight' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getTileHeight();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixType#getTileHeight <em>Tile Height</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tile Height</em>' attribute.
     * @see #getTileHeight()
     * @generated
     */
    void setTileHeight(BigInteger value);

    /**
     * Returns the value of the '<em><b>Matrix Width</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Width of the matrix (number of tiles in width)
     * <!-- end-model-doc -->
     * @return the value of the '<em>Matrix Width</em>' attribute.
     * @see #setMatrixWidth(BigInteger)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixType_MatrixWidth()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
     *        extendedMetaData="kind='element' name='MatrixWidth' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getMatrixWidth();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixType#getMatrixWidth <em>Matrix Width</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Matrix Width</em>' attribute.
     * @see #getMatrixWidth()
     * @generated
     */
    void setMatrixWidth(BigInteger value);

    /**
     * Returns the value of the '<em><b>Matrix Height</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Height of the matrix (number of tiles in height)
     * <!-- end-model-doc -->
     * @return the value of the '<em>Matrix Height</em>' attribute.
     * @see #setMatrixHeight(BigInteger)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getTileMatrixType_MatrixHeight()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
     *        extendedMetaData="kind='element' name='MatrixHeight' namespace='##targetNamespace'"
     * @generated
     */
    BigInteger getMatrixHeight();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.TileMatrixType#getMatrixHeight <em>Matrix Height</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Matrix Height</em>' attribute.
     * @see #getMatrixHeight()
     * @generated
     */
    void setMatrixHeight(BigInteger value);

} // TileMatrixType
