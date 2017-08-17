/**
 */
package net.opengis.gml311;

import javax.xml.datatype.Duration;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Time Period Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TimePeriodType#getBeginPosition <em>Begin Position</em>}</li>
 *   <li>{@link net.opengis.gml311.TimePeriodType#getBegin <em>Begin</em>}</li>
 *   <li>{@link net.opengis.gml311.TimePeriodType#getEndPosition <em>End Position</em>}</li>
 *   <li>{@link net.opengis.gml311.TimePeriodType#getEnd <em>End</em>}</li>
 *   <li>{@link net.opengis.gml311.TimePeriodType#getDuration <em>Duration</em>}</li>
 *   <li>{@link net.opengis.gml311.TimePeriodType#getTimeInterval <em>Time Interval</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTimePeriodType()
 * @model extendedMetaData="name='TimePeriodType' kind='elementOnly'"
 * @generated
 */
public interface TimePeriodType extends AbstractTimeGeometricPrimitiveType {
    /**
     * Returns the value of the '<em><b>Begin Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Begin Position</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Begin Position</em>' containment reference.
     * @see #setBeginPosition(TimePositionType)
     * @see net.opengis.gml311.Gml311Package#getTimePeriodType_BeginPosition()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='beginPosition' namespace='##targetNamespace'"
     * @generated
     */
    TimePositionType getBeginPosition();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimePeriodType#getBeginPosition <em>Begin Position</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Begin Position</em>' containment reference.
     * @see #getBeginPosition()
     * @generated
     */
    void setBeginPosition(TimePositionType value);

    /**
     * Returns the value of the '<em><b>Begin</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Begin</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Begin</em>' containment reference.
     * @see #setBegin(TimeInstantPropertyType)
     * @see net.opengis.gml311.Gml311Package#getTimePeriodType_Begin()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='begin' namespace='##targetNamespace'"
     * @generated
     */
    TimeInstantPropertyType getBegin();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimePeriodType#getBegin <em>Begin</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Begin</em>' containment reference.
     * @see #getBegin()
     * @generated
     */
    void setBegin(TimeInstantPropertyType value);

    /**
     * Returns the value of the '<em><b>End Position</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>End Position</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>End Position</em>' containment reference.
     * @see #setEndPosition(TimePositionType)
     * @see net.opengis.gml311.Gml311Package#getTimePeriodType_EndPosition()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='endPosition' namespace='##targetNamespace'"
     * @generated
     */
    TimePositionType getEndPosition();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimePeriodType#getEndPosition <em>End Position</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>End Position</em>' containment reference.
     * @see #getEndPosition()
     * @generated
     */
    void setEndPosition(TimePositionType value);

    /**
     * Returns the value of the '<em><b>End</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>End</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>End</em>' containment reference.
     * @see #setEnd(TimeInstantPropertyType)
     * @see net.opengis.gml311.Gml311Package#getTimePeriodType_End()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='end' namespace='##targetNamespace'"
     * @generated
     */
    TimeInstantPropertyType getEnd();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimePeriodType#getEnd <em>End</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>End</em>' containment reference.
     * @see #getEnd()
     * @generated
     */
    void setEnd(TimeInstantPropertyType value);

    /**
     * Returns the value of the '<em><b>Duration</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element is an instance of the primitive xsd:duration simple type to 
     *       enable use of the ISO 8601 syntax for temporal length (e.g. P5DT4H30M). 
     *       It is a valid subtype of TimeDurationType according to section 3.14.6, 
     *       rule 2.2.4 in XML Schema, Part 1.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Duration</em>' attribute.
     * @see #setDuration(Duration)
     * @see net.opengis.gml311.Gml311Package#getTimePeriodType_Duration()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Duration"
     *        extendedMetaData="kind='element' name='duration' namespace='##targetNamespace'"
     * @generated
     */
    Duration getDuration();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimePeriodType#getDuration <em>Duration</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Duration</em>' attribute.
     * @see #getDuration()
     * @generated
     */
    void setDuration(Duration value);

    /**
     * Returns the value of the '<em><b>Time Interval</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element is a valid subtype of TimeDurationType 
     * 			according to section 3.14.6, rule 2.2.4 in XML Schema, Part 1.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Time Interval</em>' containment reference.
     * @see #setTimeInterval(TimeIntervalLengthType)
     * @see net.opengis.gml311.Gml311Package#getTimePeriodType_TimeInterval()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='timeInterval' namespace='##targetNamespace'"
     * @generated
     */
    TimeIntervalLengthType getTimeInterval();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimePeriodType#getTimeInterval <em>Time Interval</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Interval</em>' containment reference.
     * @see #getTimeInterval()
     * @generated
     */
    void setTimeInterval(TimeIntervalLengthType value);

} // TimePeriodType
