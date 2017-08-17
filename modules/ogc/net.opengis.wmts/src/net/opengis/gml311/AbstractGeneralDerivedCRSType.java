/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract General Derived CRS Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A coordinate reference system that is defined by its coordinate conversion from another coordinate reference system (not by a datum). This abstract complexType shall not be used, extended, or restricted, in an Application Schema, to define a concrete subtype with a meaning equivalent to a concrete subtype specified in this document. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractGeneralDerivedCRSType#getBaseCRS <em>Base CRS</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractGeneralDerivedCRSType#getDefinedByConversion <em>Defined By Conversion</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractGeneralDerivedCRSType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractGeneralDerivedCRSType' kind='elementOnly'"
 * @generated
 */
public interface AbstractGeneralDerivedCRSType extends AbstractReferenceSystemType {
    /**
     * Returns the value of the '<em><b>Base CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the coordinate reference system used by this derived CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Base CRS</em>' containment reference.
     * @see #setBaseCRS(CoordinateReferenceSystemRefType)
     * @see net.opengis.gml311.Gml311Package#getAbstractGeneralDerivedCRSType_BaseCRS()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='baseCRS' namespace='##targetNamespace'"
     * @generated
     */
    CoordinateReferenceSystemRefType getBaseCRS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractGeneralDerivedCRSType#getBaseCRS <em>Base CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Base CRS</em>' containment reference.
     * @see #getBaseCRS()
     * @generated
     */
    void setBaseCRS(CoordinateReferenceSystemRefType value);

    /**
     * Returns the value of the '<em><b>Defined By Conversion</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the coordinate conversion used to define this derived CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Defined By Conversion</em>' containment reference.
     * @see #setDefinedByConversion(GeneralConversionRefType)
     * @see net.opengis.gml311.Gml311Package#getAbstractGeneralDerivedCRSType_DefinedByConversion()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='definedByConversion' namespace='##targetNamespace'"
     * @generated
     */
    GeneralConversionRefType getDefinedByConversion();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractGeneralDerivedCRSType#getDefinedByConversion <em>Defined By Conversion</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Defined By Conversion</em>' containment reference.
     * @see #getDefinedByConversion()
     * @generated
     */
    void setDefinedByConversion(GeneralConversionRefType value);

} // AbstractGeneralDerivedCRSType
