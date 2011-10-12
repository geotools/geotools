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
 * A representation of the model object '<em><b>Arguments Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.ArgumentsType#getArgument <em>Argument</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getArgumentsType()
 * @model extendedMetaData="name='ArgumentsType' kind='elementOnly'"
 * @generated
 */
public interface ArgumentsType extends EObject {
    /**
     * Returns the value of the '<em><b>Argument</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.ArgumentType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Argument</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Argument</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getArgumentsType_Argument()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Argument' namespace='##targetNamespace'"
     * @generated
     */
    EList<ArgumentType> getArgument();

} // ArgumentsType
