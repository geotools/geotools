/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Envelope With Time Period Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Envelope that includes also a temporal extent.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.gml.EnvelopeWithTimePeriodType#getTimePosition <em>Time Position</em>}</li>
 *   <li>{@link net.opengis.gml.EnvelopeWithTimePeriodType#getFrame <em>Frame</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.gml.GmlPackage#getEnvelopeWithTimePeriodType()
 * @model extendedMetaData="name='EnvelopeWithTimePeriodType' kind='elementOnly'"
 * @generated
 */
public interface EnvelopeWithTimePeriodType extends EnvelopeType {
    /**
	 * Returns the value of the '<em><b>Time Position</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.gml.TimePositionType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Direct representation of a temporal position.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Time Position</em>' containment reference list.
	 * @see net.opengis.gml.GmlPackage#getEnvelopeWithTimePeriodType_TimePosition()
	 * @model type="net.opengis.gml.TimePositionType" containment="true" lower="2" upper="2"
	 *        extendedMetaData="kind='element' name='timePosition' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getTimePosition();

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
	 * @see net.opengis.gml.GmlPackage#getEnvelopeWithTimePeriodType_Frame()
	 * @model default="#ISO-8601" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='frame'"
	 * @generated
	 */
    String getFrame();

    /**
	 * Sets the value of the '{@link net.opengis.gml.EnvelopeWithTimePeriodType#getFrame <em>Frame</em>}' attribute.
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
	 * Unsets the value of the '{@link net.opengis.gml.EnvelopeWithTimePeriodType#getFrame <em>Frame</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetFrame()
	 * @see #getFrame()
	 * @see #setFrame(String)
	 * @generated
	 */
    void unsetFrame();

    /**
	 * Returns whether the value of the '{@link net.opengis.gml.EnvelopeWithTimePeriodType#getFrame <em>Frame</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Frame</em>' attribute is set.
	 * @see #unsetFrame()
	 * @see #getFrame()
	 * @see #setFrame(String)
	 * @generated
	 */
    boolean isSetFrame();

} // EnvelopeWithTimePeriodType
