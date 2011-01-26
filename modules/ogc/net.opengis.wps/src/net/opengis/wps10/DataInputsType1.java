/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Inputs Type1</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * List of the Inputs provided as part of the Execute Request.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.DataInputsType1#getInput <em>Input</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getDataInputsType1()
 * @model extendedMetaData="name='DataInputsType' kind='elementOnly'"
 * @generated
 */
public interface DataInputsType1 extends EObject {
    /**
     * Returns the value of the '<em><b>Input</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wps10.InputType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of one or more inputs to be used by the process, including each of the Inputs needed to execute the process.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Input</em>' containment reference list.
     * @see net.opengis.wps10.Wps10Package#getDataInputsType1_Input()
     * @model type="net.opengis.wps10.InputType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='Input' namespace='##targetNamespace'"
     * @generated
     */
    EList getInput();

} // DataInputsType1
