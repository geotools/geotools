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
 * A representation of the model object '<em><b>Grid Limits Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.gml.GridLimitsType#getGridEnvelope <em>Grid Envelope</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.gml.GmlPackage#getGridLimitsType()
 * @model extendedMetaData="name='GridLimitsType' kind='elementOnly'"
 * @generated
 */
public interface GridLimitsType extends EObject {
    /**
	 * Returns the value of the '<em><b>Grid Envelope</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Grid Envelope</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Grid Envelope</em>' containment reference.
	 * @see #setGridEnvelope(GridEnvelopeType)
	 * @see net.opengis.gml.GmlPackage#getGridLimitsType_GridEnvelope()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='GridEnvelope' namespace='##targetNamespace'"
	 * @generated
	 */
    GridEnvelopeType getGridEnvelope();

    /**
	 * Sets the value of the '{@link net.opengis.gml.GridLimitsType#getGridEnvelope <em>Grid Envelope</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Grid Envelope</em>' containment reference.
	 * @see #getGridEnvelope()
	 * @generated
	 */
    void setGridEnvelope(GridEnvelopeType value);

} // GridLimitsType
