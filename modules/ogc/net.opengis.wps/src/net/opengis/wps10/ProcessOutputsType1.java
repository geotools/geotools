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
 * A representation of the model object '<em><b>Process Outputs Type1</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ProcessOutputsType1#getOutput <em>Output</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getProcessOutputsType1()
 * @model extendedMetaData="name='ProcessOutputs_._1_._type' kind='elementOnly'"
 * @generated
 */
public interface ProcessOutputsType1 extends EObject {
    /**
     * Returns the value of the '<em><b>Output</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wps10.OutputDataType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of values of all the outputs produced by this process. It is not necessary to include an output until the Status is ProcessSucceeded.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Output</em>' containment reference list.
     * @see net.opengis.wps10.Wps10Package#getProcessOutputsType1_Output()
     * @model type="net.opengis.wps10.OutputDataType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='Output' namespace='##targetNamespace'"
     * @generated
     */
    EList getOutput();

} // ProcessOutputsType1
