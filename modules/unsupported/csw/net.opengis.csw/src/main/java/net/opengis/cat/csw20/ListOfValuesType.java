/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>List Of Values Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.ListOfValuesType#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getListOfValuesType()
 * @model extendedMetaData="name='ListOfValuesType' kind='elementOnly'"
 * @generated
 */
public interface ListOfValuesType extends EObject {
    /**
     * Returns the value of the '<em><b>Value</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getListOfValuesType_Value()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Value' namespace='##targetNamespace'"
     * @generated
     */
    EList<EObject> getValue();

} // ListOfValuesType
