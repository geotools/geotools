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
 * A representation of the model object '<em><b>Additional Operators Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.AdditionalOperatorsType#getOperator <em>Operator</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getAdditionalOperatorsType()
 * @model extendedMetaData="name='AdditionalOperatorsType' kind='elementOnly'"
 * @generated
 */
public interface AdditionalOperatorsType extends EObject {
    /**
     * Returns the value of the '<em><b>Operator</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.ExtensionOperatorType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Operator</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operator</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getAdditionalOperatorsType_Operator()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Operator' namespace='##targetNamespace'"
     * @generated
     */
    EList<ExtensionOperatorType> getOperator();

} // AdditionalOperatorsType
