/**
 */
package net.opengis.gml311;

import java.math.BigDecimal;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Time Calendar Era Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * In every calendar, years are numbered relative to the date of a 
 *       reference event that defines a calendar era. 
 *       In this implementation, we omit the back-pointer "datingSystem".
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TimeCalendarEraType#getReferenceEvent <em>Reference Event</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeCalendarEraType#getReferenceDate <em>Reference Date</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeCalendarEraType#getJulianReference <em>Julian Reference</em>}</li>
 *   <li>{@link net.opengis.gml311.TimeCalendarEraType#getEpochOfUse <em>Epoch Of Use</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTimeCalendarEraType()
 * @model extendedMetaData="name='TimeCalendarEraType' kind='elementOnly'"
 * @generated
 */
public interface TimeCalendarEraType extends DefinitionType {
    /**
     * Returns the value of the '<em><b>Reference Event</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Name or description of a mythical or historic event which fixes the position of the base scale of the calendar era.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Reference Event</em>' containment reference.
     * @see #setReferenceEvent(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getTimeCalendarEraType_ReferenceEvent()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='referenceEvent' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getReferenceEvent();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeCalendarEraType#getReferenceEvent <em>Reference Event</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference Event</em>' containment reference.
     * @see #getReferenceEvent()
     * @generated
     */
    void setReferenceEvent(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Reference Date</b></em>' attribute.
     * The default value is <code>"0001-01-01"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Date of the referenceEvent expressed as a date in the given calendar. 
     *               In most calendars, this date is the origin (i.e., the first day) of the scale, but this is not always true.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Reference Date</em>' attribute.
     * @see #isSetReferenceDate()
     * @see #unsetReferenceDate()
     * @see #setReferenceDate(XMLGregorianCalendar)
     * @see net.opengis.gml311.Gml311Package#getTimeCalendarEraType_ReferenceDate()
     * @model default="0001-01-01" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Date"
     *        extendedMetaData="kind='element' name='referenceDate' namespace='##targetNamespace'"
     * @generated
     */
    XMLGregorianCalendar getReferenceDate();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeCalendarEraType#getReferenceDate <em>Reference Date</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Reference Date</em>' attribute.
     * @see #isSetReferenceDate()
     * @see #unsetReferenceDate()
     * @see #getReferenceDate()
     * @generated
     */
    void setReferenceDate(XMLGregorianCalendar value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.TimeCalendarEraType#getReferenceDate <em>Reference Date</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetReferenceDate()
     * @see #getReferenceDate()
     * @see #setReferenceDate(XMLGregorianCalendar)
     * @generated
     */
    void unsetReferenceDate();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.TimeCalendarEraType#getReferenceDate <em>Reference Date</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Reference Date</em>' attribute is set.
     * @see #unsetReferenceDate()
     * @see #getReferenceDate()
     * @see #setReferenceDate(XMLGregorianCalendar)
     * @generated
     */
    boolean isSetReferenceDate();

    /**
     * Returns the value of the '<em><b>Julian Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Julian date that corresponds to the reference date.  
     *               The Julian day numbering system is a temporal coordinate system that has an 
     *               origin earlier than any known calendar, 
     *               at noon on 1 January 4713 BC in the Julian proleptic calendar.  
     *               The Julian day number is an integer value; 
     *               the Julian date is a decimal value that allows greater resolution. 
     *               Transforming calendar dates to and from Julian dates provides a 
     *               relatively simple basis for transforming dates from one calendar to another.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Julian Reference</em>' attribute.
     * @see #setJulianReference(BigDecimal)
     * @see net.opengis.gml311.Gml311Package#getTimeCalendarEraType_JulianReference()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Decimal" required="true"
     *        extendedMetaData="kind='element' name='julianReference' namespace='##targetNamespace'"
     * @generated
     */
    BigDecimal getJulianReference();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeCalendarEraType#getJulianReference <em>Julian Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Julian Reference</em>' attribute.
     * @see #getJulianReference()
     * @generated
     */
    void setJulianReference(BigDecimal value);

    /**
     * Returns the value of the '<em><b>Epoch Of Use</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Period for which the calendar era was used as a basis for dating.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Epoch Of Use</em>' containment reference.
     * @see #setEpochOfUse(TimePeriodPropertyType)
     * @see net.opengis.gml311.Gml311Package#getTimeCalendarEraType_EpochOfUse()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='epochOfUse' namespace='##targetNamespace'"
     * @generated
     */
    TimePeriodPropertyType getEpochOfUse();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TimeCalendarEraType#getEpochOfUse <em>Epoch Of Use</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Epoch Of Use</em>' containment reference.
     * @see #getEpochOfUse()
     * @generated
     */
    void setEpochOfUse(TimePeriodPropertyType value);

} // TimeCalendarEraType
