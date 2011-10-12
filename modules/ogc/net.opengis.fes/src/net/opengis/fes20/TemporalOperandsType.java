/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Temporal Operands Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.TemporalOperandsType#getTemporalOperand <em>Temporal Operand</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getTemporalOperandsType()
 * @model extendedMetaData="name='TemporalOperandsType' kind='elementOnly'"
 * @generated
 */
public interface TemporalOperandsType extends EObject {
    /**
     * Returns the value of the '<em><b>Temporal Operand</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.TemporalOperandType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal Operand</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Temporal Operand</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getTemporalOperandsType_TemporalOperand()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='TemporalOperand' namespace='##targetNamespace'"
     * @generated
     */
    EList<TemporalOperandType> getTemporalOperand();

} // TemporalOperandsType
