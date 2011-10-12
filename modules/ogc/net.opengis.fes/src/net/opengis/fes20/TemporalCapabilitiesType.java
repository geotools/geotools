/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Temporal Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.TemporalCapabilitiesType#getTemporalOperands <em>Temporal Operands</em>}</li>
 *   <li>{@link net.opengis.fes20.TemporalCapabilitiesType#getTemporalOperators <em>Temporal Operators</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getTemporalCapabilitiesType()
 * @model extendedMetaData="name='Temporal_CapabilitiesType' kind='elementOnly'"
 * @generated
 */
public interface TemporalCapabilitiesType extends EObject {
    /**
     * Returns the value of the '<em><b>Temporal Operands</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal Operands</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Temporal Operands</em>' containment reference.
     * @see #setTemporalOperands(TemporalOperandsType)
     * @see net.opengis.fes20.Fes20Package#getTemporalCapabilitiesType_TemporalOperands()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='TemporalOperands' namespace='##targetNamespace'"
     * @generated
     */
    TemporalOperandsType getTemporalOperands();

    /**
     * Sets the value of the '{@link net.opengis.fes20.TemporalCapabilitiesType#getTemporalOperands <em>Temporal Operands</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal Operands</em>' containment reference.
     * @see #getTemporalOperands()
     * @generated
     */
    void setTemporalOperands(TemporalOperandsType value);

    /**
     * Returns the value of the '<em><b>Temporal Operators</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal Operators</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Temporal Operators</em>' containment reference.
     * @see #setTemporalOperators(TemporalOperatorsType)
     * @see net.opengis.fes20.Fes20Package#getTemporalCapabilitiesType_TemporalOperators()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='TemporalOperators' namespace='##targetNamespace'"
     * @generated
     */
    TemporalOperatorsType getTemporalOperators();

    /**
     * Sets the value of the '{@link net.opengis.fes20.TemporalCapabilitiesType#getTemporalOperators <em>Temporal Operators</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal Operators</em>' containment reference.
     * @see #getTemporalOperators()
     * @generated
     */
    void setTemporalOperators(TemporalOperatorsType value);

} // TemporalCapabilitiesType
