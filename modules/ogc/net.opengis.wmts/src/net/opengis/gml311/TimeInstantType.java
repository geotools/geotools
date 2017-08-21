/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Time Instant Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Omit back-pointers begunBy, endedBy.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TimeInstantType#getTimePosition <em>Time Position</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTimeInstantType()
 * @model extendedMetaData="name='TimeInstantType' kind='elementOnly'"
 * @generated
 */
public interface TimeInstantType extends AbstractTimeGeometricPrimitiveType {
    /**
     * Returns the value of the '<em><b>Time Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Direct representation of a temporal position
     * <!-- end-model-doc -->
     * @return the value of the '<em>Time Position</em>' containment reference.
     * @see #setTimePosition(TimePositionType)
     * @see net.opengis.gml311.Gml311Package#getTimeInstantType_TimePosition()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='timePosition' namespace='##targetNamespace'"
     * @generated
     */
    TimePositionType getTimePosition();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeInstantType#getTimePosition <em>Time Position</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Position</em>' containment reference.
     * @see #getTimePosition()
     * @generated
     */
    void setTimePosition(TimePositionType value);

} // TimeInstantType
