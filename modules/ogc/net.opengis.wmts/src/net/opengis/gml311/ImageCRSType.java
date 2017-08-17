/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Image CRS Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An engineering coordinate reference system applied to locations in images. Image coordinate reference systems are treated as a separate sub-type because a separate user community exists for images with its own terms of reference. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.ImageCRSType#getUsesCartesianCS <em>Uses Cartesian CS</em>}</li>
 *   <li>{@link net.opengis.gml311.ImageCRSType#getUsesObliqueCartesianCS <em>Uses Oblique Cartesian CS</em>}</li>
 *   <li>{@link net.opengis.gml311.ImageCRSType#getUsesImageDatum <em>Uses Image Datum</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getImageCRSType()
 * @model extendedMetaData="name='ImageCRSType' kind='elementOnly'"
 * @generated
 */
public interface ImageCRSType extends AbstractReferenceSystemType {
    /**
     * Returns the value of the '<em><b>Uses Cartesian CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the Cartesian coordinate system used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Cartesian CS</em>' containment reference.
     * @see #setUsesCartesianCS(CartesianCSRefType)
     * @see net.opengis.gml311.Gml311Package#getImageCRSType_UsesCartesianCS()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='usesCartesianCS' namespace='##targetNamespace'"
     * @generated
     */
    CartesianCSRefType getUsesCartesianCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ImageCRSType#getUsesCartesianCS <em>Uses Cartesian CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Cartesian CS</em>' containment reference.
     * @see #getUsesCartesianCS()
     * @generated
     */
    void setUsesCartesianCS(CartesianCSRefType value);

    /**
     * Returns the value of the '<em><b>Uses Oblique Cartesian CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the oblique Cartesian coordinate system used by this CRS.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Oblique Cartesian CS</em>' containment reference.
     * @see #setUsesObliqueCartesianCS(ObliqueCartesianCSRefType)
     * @see net.opengis.gml311.Gml311Package#getImageCRSType_UsesObliqueCartesianCS()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='usesObliqueCartesianCS' namespace='##targetNamespace'"
     * @generated
     */
    ObliqueCartesianCSRefType getUsesObliqueCartesianCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ImageCRSType#getUsesObliqueCartesianCS <em>Uses Oblique Cartesian CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Oblique Cartesian CS</em>' containment reference.
     * @see #getUsesObliqueCartesianCS()
     * @generated
     */
    void setUsesObliqueCartesianCS(ObliqueCartesianCSRefType value);

    /**
     * Returns the value of the '<em><b>Uses Image Datum</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the image datum used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Image Datum</em>' containment reference.
     * @see #setUsesImageDatum(ImageDatumRefType)
     * @see net.opengis.gml311.Gml311Package#getImageCRSType_UsesImageDatum()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='usesImageDatum' namespace='##targetNamespace'"
     * @generated
     */
    ImageDatumRefType getUsesImageDatum();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ImageCRSType#getUsesImageDatum <em>Uses Image Datum</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Image Datum</em>' containment reference.
     * @see #getUsesImageDatum()
     * @generated
     */
    void setUsesImageDatum(ImageDatumRefType value);

} // ImageCRSType
