/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Directed Observation At Distance Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.DirectedObservationAtDistanceType#getDistance <em>Distance</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getDirectedObservationAtDistanceType()
 * @model extendedMetaData="name='DirectedObservationAtDistanceType' kind='elementOnly'"
 * @generated
 */
public interface DirectedObservationAtDistanceType extends DirectedObservationType {
    /**
     * Returns the value of the '<em><b>Distance</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Distance</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Distance</em>' containment reference.
     * @see #setDistance(MeasureType)
     * @see net.opengis.gml311.Gml311Package#getDirectedObservationAtDistanceType_Distance()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='distance' namespace='##targetNamespace'"
     * @generated
     */
    MeasureType getDistance();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DirectedObservationAtDistanceType#getDistance <em>Distance</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Distance</em>' containment reference.
     * @see #getDistance()
     * @generated
     */
    void setDistance(MeasureType value);

} // DirectedObservationAtDistanceType
