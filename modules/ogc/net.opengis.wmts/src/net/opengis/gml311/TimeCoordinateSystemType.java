/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Time Coordinate System Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A temporal coordinate system is based on a continuous interval scale defined in terms of a single time interval.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TimeCoordinateSystemType#getOriginPosition <em>Origin Position</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeCoordinateSystemType#getOrigin <em>Origin</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeCoordinateSystemType#getInterval <em>Interval</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTimeCoordinateSystemType()
 * @model extendedMetaData="name='TimeCoordinateSystemType' kind='elementOnly'"
 * @generated
 */
public interface TimeCoordinateSystemType extends AbstractTimeReferenceSystemType {
    /**
     * Returns the value of the '<em><b>Origin Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Origin Position</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Origin Position</em>' containment reference.
     * @see #setOriginPosition(TimePositionType)
     * @see net.opengis.gml311.Gml311Package#getTimeCoordinateSystemType_OriginPosition()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='originPosition' namespace='##targetNamespace'"
     * @generated
     */
    TimePositionType getOriginPosition();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeCoordinateSystemType#getOriginPosition <em>Origin Position</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Origin Position</em>' containment reference.
     * @see #getOriginPosition()
     * @generated
     */
    void setOriginPosition(TimePositionType value);

    /**
     * Returns the value of the '<em><b>Origin</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Origin</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Origin</em>' containment reference.
     * @see #setOrigin(TimeInstantPropertyType)
     * @see net.opengis.gml311.Gml311Package#getTimeCoordinateSystemType_Origin()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='origin' namespace='##targetNamespace'"
     * @generated
     */
    TimeInstantPropertyType getOrigin();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeCoordinateSystemType#getOrigin <em>Origin</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Origin</em>' containment reference.
     * @see #getOrigin()
     * @generated
     */
    void setOrigin(TimeInstantPropertyType value);

    /**
     * Returns the value of the '<em><b>Interval</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Interval</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Interval</em>' containment reference.
     * @see #setInterval(TimeIntervalLengthType)
     * @see net.opengis.gml311.Gml311Package#getTimeCoordinateSystemType_Interval()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='interval' namespace='##targetNamespace'"
     * @generated
     */
    TimeIntervalLengthType getInterval();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeCoordinateSystemType#getInterval <em>Interval</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Interval</em>' containment reference.
     * @see #getInterval()
     * @generated
     */
    void setInterval(TimeIntervalLengthType value);

} // TimeCoordinateSystemType
