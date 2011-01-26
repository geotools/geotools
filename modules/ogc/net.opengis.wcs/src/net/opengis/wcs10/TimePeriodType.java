/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import net.opengis.gml.TimePositionType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Time Period Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is a variation of the GML TimePeriod, which allows the beginning and end of a time-period to be expressed in short-form inline using the begin/endPosition element, which allows an identifiable TimeInstant to be defined simultaneously with using it, or by reference, using xlinks on the begin/end elements.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.TimePeriodType#getBeginPosition <em>Begin Position</em>}</li>
 *   <li>{@link net.opengis.wcs10.TimePeriodType#getEndPosition <em>End Position</em>}</li>
 *   <li>{@link net.opengis.wcs10.TimePeriodType#getTimeResolution <em>Time Resolution</em>}</li>
 *   <li>{@link net.opengis.wcs10.TimePeriodType#getFrame <em>Frame</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getTimePeriodType()
 * @model extendedMetaData="name='TimePeriodType' kind='elementOnly'"
 * @generated
 */
public interface TimePeriodType extends EObject {
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
	 * @see net.opengis.wcs10.Wcs10Package#getTimePeriodType_BeginPosition()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='beginPosition' namespace='##targetNamespace'"
	 * @generated
	 */
    TimePositionType getBeginPosition();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.TimePeriodType#getBeginPosition <em>Begin Position</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Begin Position</em>' containment reference.
	 * @see #getBeginPosition()
	 * @generated
	 */
    void setBeginPosition(TimePositionType value);

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
	 * @see net.opengis.wcs10.Wcs10Package#getTimePeriodType_EndPosition()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='endPosition' namespace='##targetNamespace'"
	 * @generated
	 */
    TimePositionType getEndPosition();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.TimePeriodType#getEndPosition <em>End Position</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Position</em>' containment reference.
	 * @see #getEndPosition()
	 * @generated
	 */
    void setEndPosition(TimePositionType value);

    /**
	 * Returns the value of the '<em><b>Time Resolution</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Resolution</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Time Resolution</em>' attribute.
	 * @see #setTimeResolution(Object)
	 * @see net.opengis.wcs10.Wcs10Package#getTimePeriodType_TimeResolution()
	 * @model dataType="net.opengis.gml.TimeDurationType"
	 *        extendedMetaData="kind='element' name='timeResolution' namespace='##targetNamespace'"
	 * @generated
	 */
    Object getTimeResolution();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.TimePeriodType#getTimeResolution <em>Time Resolution</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time Resolution</em>' attribute.
	 * @see #getTimeResolution()
	 * @generated
	 */
    void setTimeResolution(Object value);

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
	 * @see net.opengis.wcs10.Wcs10Package#getTimePeriodType_Frame()
	 * @model default="#ISO-8601" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='frame'"
	 * @generated
	 */
    String getFrame();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.TimePeriodType#getFrame <em>Frame</em>}' attribute.
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
	 * Unsets the value of the '{@link net.opengis.wcs10.TimePeriodType#getFrame <em>Frame</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetFrame()
	 * @see #getFrame()
	 * @see #setFrame(String)
	 * @generated
	 */
    void unsetFrame();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.TimePeriodType#getFrame <em>Frame</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Frame</em>' attribute is set.
	 * @see #unsetFrame()
	 * @see #getFrame()
	 * @see #setFrame(String)
	 * @generated
	 */
    boolean isSetFrame();

} // TimePeriodType
