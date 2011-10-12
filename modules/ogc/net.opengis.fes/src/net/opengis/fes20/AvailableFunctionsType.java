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
 * A representation of the model object '<em><b>Available Functions Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.AvailableFunctionsType#getFunction <em>Function</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getAvailableFunctionsType()
 * @model extendedMetaData="name='AvailableFunctionsType' kind='elementOnly'"
 * @generated
 */
public interface AvailableFunctionsType extends EObject {
    /**
     * Returns the value of the '<em><b>Function</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.AvailableFunctionType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Function</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Function</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getAvailableFunctionsType_Function()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Function' namespace='##targetNamespace'"
     * @generated
     */
    EList<AvailableFunctionType> getFunction();

} // AvailableFunctionsType
