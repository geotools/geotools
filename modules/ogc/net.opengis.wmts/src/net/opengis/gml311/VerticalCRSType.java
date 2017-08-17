/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vertical CRS Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A 1D coordinate reference system used for recording heights or depths. Vertical CRSs make use of the direction of gravity to define the concept of height or depth, but the relationship with gravity may not be straightforward. By implication, ellipsoidal heights (h) cannot be captured in a vertical coordinate reference system. Ellipsoidal heights cannot exist independently, but only as an inseparable part of a 3D coordinate tuple defined in a geographic 3D coordinate reference system. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.VerticalCRSType#getUsesVerticalCS <em>Uses Vertical CS</em>}</li>
 *   <li>{@link net.opengis.gml311.VerticalCRSType#getUsesVerticalDatum <em>Uses Vertical Datum</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getVerticalCRSType()
 * @model extendedMetaData="name='VerticalCRSType' kind='elementOnly'"
 * @generated
 */
public interface VerticalCRSType extends AbstractReferenceSystemType {
    /**
     * Returns the value of the '<em><b>Uses Vertical CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the vertical coordinate system used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Vertical CS</em>' containment reference.
     * @see #setUsesVerticalCS(VerticalCSRefType)
     * @see net.opengis.gml311.Gml311Package#getVerticalCRSType_UsesVerticalCS()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='usesVerticalCS' namespace='##targetNamespace'"
     * @generated
     */
    VerticalCSRefType getUsesVerticalCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.VerticalCRSType#getUsesVerticalCS <em>Uses Vertical CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Vertical CS</em>' containment reference.
     * @see #getUsesVerticalCS()
     * @generated
     */
    void setUsesVerticalCS(VerticalCSRefType value);

    /**
     * Returns the value of the '<em><b>Uses Vertical Datum</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the vertical datum used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Vertical Datum</em>' containment reference.
     * @see #setUsesVerticalDatum(VerticalDatumRefType)
     * @see net.opengis.gml311.Gml311Package#getVerticalCRSType_UsesVerticalDatum()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='usesVerticalDatum' namespace='##targetNamespace'"
     * @generated
     */
    VerticalDatumRefType getUsesVerticalDatum();

    /**
     * Sets the value of the '{@link net.opengis.gml311.VerticalCRSType#getUsesVerticalDatum <em>Uses Vertical Datum</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Vertical Datum</em>' containment reference.
     * @see #getUsesVerticalDatum()
     * @generated
     */
    void setUsesVerticalDatum(VerticalDatumRefType value);

} // VerticalCRSType
