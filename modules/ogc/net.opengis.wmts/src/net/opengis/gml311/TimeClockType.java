/**
 */
package net.opengis.gml311;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Time Clock Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A clock provides a basis for defining temporal position within a day. 
 *       A clock must be used with a calendar in order to provide a complete description of a temporal position 
 *       within a specific day.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TimeClockType#getReferenceEvent <em>Reference Event</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeClockType#getReferenceTime <em>Reference Time</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeClockType#getUtcReference <em>Utc Reference</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeClockType#getDateBasis <em>Date Basis</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTimeClockType()
 * @model extendedMetaData="name='TimeClockType' kind='elementOnly'"
 * @generated
 */
public interface TimeClockType extends AbstractTimeReferenceSystemType {
    /**
     * Returns the value of the '<em><b>Reference Event</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Name or description of an event, such as solar noon or sunrise, 
     *               which fixes the position of the base scale of the clock.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Reference Event</em>' containment reference.
     * @see #setReferenceEvent(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getTimeClockType_ReferenceEvent()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='referenceEvent' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getReferenceEvent();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeClockType#getReferenceEvent <em>Reference Event</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference Event</em>' containment reference.
     * @see #getReferenceEvent()
     * @generated
     */
    void setReferenceEvent(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Reference Time</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * time of day associated with the reference event expressed as 
     *               a time of day in the given clock. The reference time is usually the origin of the clock scale.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Reference Time</em>' attribute.
     * @see #setReferenceTime(XMLGregorianCalendar)
     * @see net.opengis.gml311.Gml311Package#getTimeClockType_ReferenceTime()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Time" required="true"
     *        extendedMetaData="kind='element' name='referenceTime' namespace='##targetNamespace'"
     * @generated
     */
    XMLGregorianCalendar getReferenceTime();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeClockType#getReferenceTime <em>Reference Time</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference Time</em>' attribute.
     * @see #getReferenceTime()
     * @generated
     */
    void setReferenceTime(XMLGregorianCalendar value);

    /**
     * Returns the value of the '<em><b>Utc Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 24 hour local or UTC time that corresponds to the reference time.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Utc Reference</em>' attribute.
     * @see #setUtcReference(XMLGregorianCalendar)
     * @see net.opengis.gml311.Gml311Package#getTimeClockType_UtcReference()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Time" required="true"
     *        extendedMetaData="kind='element' name='utcReference' namespace='##targetNamespace'"
     * @generated
     */
    XMLGregorianCalendar getUtcReference();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeClockType#getUtcReference <em>Utc Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Utc Reference</em>' attribute.
     * @see #getUtcReference()
     * @generated
     */
    void setUtcReference(XMLGregorianCalendar value);

    /**
     * Returns the value of the '<em><b>Date Basis</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.TimeCalendarPropertyType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Date Basis</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Date Basis</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getTimeClockType_DateBasis()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='dateBasis' namespace='##targetNamespace'"
     * @generated
     */
    EList<TimeCalendarPropertyType> getDateBasis();

} // TimeClockType
