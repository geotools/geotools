/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Time Position Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *       Indeterminate time values are also allowed, as described in ISO 19108. The indeterminatePosition
 *       attribute can be used alone or it can qualify a specific value for temporal position (e.g. before
 *       2002-12, after 1019624400). For time values that identify position within a calendar, the
 *       calendarEraName attribute provides the name of the calendar era to which the date is
 *       referenced (e.g. the Meiji era of the Japanese calendar).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.gml.TimePositionType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml.TimePositionType#getCalendarEraName <em>Calendar Era Name</em>}</li>
 *   <li>{@link net.opengis.gml.TimePositionType#getFrame <em>Frame</em>}</li>
 *   <li>{@link net.opengis.gml.TimePositionType#getIndeterminatePosition <em>Indeterminate Position</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.gml.GmlPackage#getTimePositionType()
 * @model extendedMetaData="name='TimePositionType' kind='simple'"
 * @generated
 */
public interface TimePositionType extends EObject {
    /**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(Object)
	 * @see net.opengis.gml.GmlPackage#getTimePositionType_Value()
	 * @model dataType="net.opengis.gml.TemporalPositionType"
	 *        extendedMetaData="name=':0' kind='simple'"
	 * @generated
	 */
    Object getValue();

    /**
	 * Sets the value of the '{@link net.opengis.gml.TimePositionType#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
    void setValue(Object value);

    /**
	 * Returns the value of the '<em><b>Calendar Era Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Calendar Era Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Calendar Era Name</em>' attribute.
	 * @see #setCalendarEraName(String)
	 * @see net.opengis.gml.GmlPackage#getTimePositionType_CalendarEraName()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='calendarEraName'"
	 * @generated
	 */
    String getCalendarEraName();

    /**
	 * Sets the value of the '{@link net.opengis.gml.TimePositionType#getCalendarEraName <em>Calendar Era Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Calendar Era Name</em>' attribute.
	 * @see #getCalendarEraName()
	 * @generated
	 */
    void setCalendarEraName(String value);

    /**
	 * Returns the value of the '<em><b>Frame</b></em>' attribute.
	 * The default value is <code>"#ISO-8601"</code>.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Frame</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Frame</em>' attribute.
	 * @see #isSetFrame()
	 * @see #unsetFrame()
	 * @see #setFrame(String)
	 * @see net.opengis.gml.GmlPackage#getTimePositionType_Frame()
	 * @model default="#ISO-8601" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='frame'"
	 * @generated
	 */
    String getFrame();

    /**
	 * Sets the value of the '{@link net.opengis.gml.TimePositionType#getFrame <em>Frame</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Frame</em>' attribute.
	 * @see #isSetFrame()
	 * @see #unsetFrame()
	 * @see #getFrame()
	 * @generated
	 */
    void setFrame(String value);

    /**
	 * Unsets the value of the '{@link net.opengis.gml.TimePositionType#getFrame <em>Frame</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetFrame()
	 * @see #getFrame()
	 * @see #setFrame(String)
	 * @generated
	 */
    void unsetFrame();

    /**
	 * Returns whether the value of the '{@link net.opengis.gml.TimePositionType#getFrame <em>Frame</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Frame</em>' attribute is set.
	 * @see #unsetFrame()
	 * @see #getFrame()
	 * @see #setFrame(String)
	 * @generated
	 */
    boolean isSetFrame();

    /**
	 * Returns the value of the '<em><b>Indeterminate Position</b></em>' attribute.
	 * The default value is <code>"after"</code>.
	 * The literals are from the enumeration {@link net.opengis.gml.TimeIndeterminateValueType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Indeterminate Position</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Indeterminate Position</em>' attribute.
	 * @see net.opengis.gml.TimeIndeterminateValueType
	 * @see #isSetIndeterminatePosition()
	 * @see #unsetIndeterminatePosition()
	 * @see #setIndeterminatePosition(TimeIndeterminateValueType)
	 * @see net.opengis.gml.GmlPackage#getTimePositionType_IndeterminatePosition()
	 * @model default="after" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='indeterminatePosition'"
	 * @generated
	 */
    TimeIndeterminateValueType getIndeterminatePosition();

    /**
	 * Sets the value of the '{@link net.opengis.gml.TimePositionType#getIndeterminatePosition <em>Indeterminate Position</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Indeterminate Position</em>' attribute.
	 * @see net.opengis.gml.TimeIndeterminateValueType
	 * @see #isSetIndeterminatePosition()
	 * @see #unsetIndeterminatePosition()
	 * @see #getIndeterminatePosition()
	 * @generated
	 */
    void setIndeterminatePosition(TimeIndeterminateValueType value);

    /**
	 * Unsets the value of the '{@link net.opengis.gml.TimePositionType#getIndeterminatePosition <em>Indeterminate Position</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetIndeterminatePosition()
	 * @see #getIndeterminatePosition()
	 * @see #setIndeterminatePosition(TimeIndeterminateValueType)
	 * @generated
	 */
    void unsetIndeterminatePosition();

    /**
	 * Returns whether the value of the '{@link net.opengis.gml.TimePositionType#getIndeterminatePosition <em>Indeterminate Position</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Indeterminate Position</em>' attribute is set.
	 * @see #unsetIndeterminatePosition()
	 * @see #getIndeterminatePosition()
	 * @see #setIndeterminatePosition(TimeIndeterminateValueType)
	 * @generated
	 */
    boolean isSetIndeterminatePosition();

} // TimePositionType
