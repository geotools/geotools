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
 * A representation of the model object '<em><b>Temporal Operators Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.TemporalOperatorsType#getTemporalOperator <em>Temporal Operator</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getTemporalOperatorsType()
 * @model extendedMetaData="name='TemporalOperatorsType' kind='elementOnly'"
 * @generated
 */
public interface TemporalOperatorsType extends EObject {
    /**
     * Returns the value of the '<em><b>Temporal Operator</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.TemporalOperatorType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Temporal Operator</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Temporal Operator</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getTemporalOperatorsType_TemporalOperator()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='TemporalOperator' namespace='##targetNamespace'"
     * @generated
     */
    EList<TemporalOperatorType> getTemporalOperator();

} // TemporalOperatorsType
