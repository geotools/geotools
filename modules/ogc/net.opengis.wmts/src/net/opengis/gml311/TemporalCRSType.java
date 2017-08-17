/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Temporal CRS Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A 1D coordinate reference system used for the recording of time. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TemporalCRSType#getUsesTemporalCS <em>Uses Temporal CS</em>}</li>
 *   <li>{@link net.opengis.gml311.TemporalCRSType#getUsesTemporalDatum <em>Uses Temporal Datum</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTemporalCRSType()
 * @model extendedMetaData="name='TemporalCRSType' kind='elementOnly'"
 * @generated
 */
public interface TemporalCRSType extends AbstractReferenceSystemType {
    /**
     * Returns the value of the '<em><b>Uses Temporal CS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the temporal coordinate system used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Temporal CS</em>' containment reference.
     * @see #setUsesTemporalCS(TemporalCSRefType)
     * @see net.opengis.gml311.Gml311Package#getTemporalCRSType_UsesTemporalCS()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='usesTemporalCS' namespace='##targetNamespace'"
     * @generated
     */
    TemporalCSRefType getUsesTemporalCS();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TemporalCRSType#getUsesTemporalCS <em>Uses Temporal CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Temporal CS</em>' containment reference.
     * @see #getUsesTemporalCS()
     * @generated
     */
    void setUsesTemporalCS(TemporalCSRefType value);

    /**
     * Returns the value of the '<em><b>Uses Temporal Datum</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the temporal datum used by this CRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Temporal Datum</em>' containment reference.
     * @see #setUsesTemporalDatum(TemporalDatumRefType)
     * @see net.opengis.gml311.Gml311Package#getTemporalCRSType_UsesTemporalDatum()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='usesTemporalDatum' namespace='##targetNamespace'"
     * @generated
     */
    TemporalDatumRefType getUsesTemporalDatum();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TemporalCRSType#getUsesTemporalDatum <em>Uses Temporal Datum</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uses Temporal Datum</em>' containment reference.
     * @see #getUsesTemporalDatum()
     * @generated
     */
    void setUsesTemporalDatum(TemporalDatumRefType value);

} // TemporalCRSType
