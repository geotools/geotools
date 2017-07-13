/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Image Datum Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An image datum defines the origin of an image coordinate reference system, and is used in a local context only. For more information, see OGC Abstract Specification Topic 2. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.ImageDatumType#getPixelInCell <em>Pixel In Cell</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getImageDatumType()
 * @model extendedMetaData="name='ImageDatumType' kind='elementOnly'"
 * @generated
 */
public interface ImageDatumType extends AbstractDatumType {
    /**
     * Returns the value of the '<em><b>Pixel In Cell</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Pixel In Cell</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Pixel In Cell</em>' containment reference.
     * @see #setPixelInCell(PixelInCellType)
     * @see net.opengis.gml311.Gml311Package#getImageDatumType_PixelInCell()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='pixelInCell' namespace='##targetNamespace'"
     * @generated
     */
    PixelInCellType getPixelInCell();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ImageDatumType#getPixelInCell <em>Pixel In Cell</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Pixel In Cell</em>' containment reference.
     * @see #getPixelInCell()
     * @generated
     */
    void setPixelInCell(PixelInCellType value);

} // ImageDatumType
