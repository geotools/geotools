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
 * A representation of the model object '<em><b>Temporal Operator Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.TemporalOperatorType#getTemporalOperands <em>Temporal Operands</em>}</li>
 *   <li>{@link net.opengis.fes20.TemporalOperatorType#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getTemporalOperatorType()
 * @model extendedMetaData="name='TemporalOperatorType' kind='elementOnly'"
 * @generated
 */
public interface TemporalOperatorType extends EObject {
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
     * @see net.opengis.fes20.Fes20Package#getTemporalOperatorType_TemporalOperands()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='TemporalOperands' namespace='##targetNamespace'"
     * @generated
     */
    TemporalOperandsType getTemporalOperands();

    /**
     * Sets the value of the '{@link net.opengis.fes20.TemporalOperatorType#getTemporalOperands <em>Temporal Operands</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal Operands</em>' containment reference.
     * @see #getTemporalOperands()
     * @generated
     */
    void setTemporalOperands(TemporalOperandsType value);

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(Object)
     * @see net.opengis.fes20.Fes20Package#getTemporalOperatorType_Name()
     * @model dataType="net.opengis.fes20.TemporalOperatorNameType" required="true"
     *        extendedMetaData="kind='attribute' name='name'"
     * @generated
     */
    Object getName();

    /**
     * Sets the value of the '{@link net.opengis.fes20.TemporalOperatorType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(Object value);

} // TemporalOperatorType
