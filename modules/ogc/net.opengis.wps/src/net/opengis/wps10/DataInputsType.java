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
 * A representation of the model object '<em><b>Data Inputs Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.DataInputsType#getInput <em>Input</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getDataInputsType()
 * @model extendedMetaData="name='DataInputs_._type' kind='elementOnly'"
 * @generated
 */
public interface DataInputsType extends EObject {
    /**
     * Returns the value of the '<em><b>Input</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wps10.InputDescriptionType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of one or more descriptions of the inputs that can be accepted by this process, including all required and optional inputs.  Where an input is optional because a default value exists, that default value must be identified in the "ows:Abstract" element for that input, except in the case of LiteralData, where the default must be indicated in the corresponding ows:DefaultValue element. Where an input is optional because it depends on the value(s) of other inputs, this must be indicated in the ows:Abstract element for that input.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Input</em>' containment reference list.
     * @see net.opengis.wps10.Wps10Package#getDataInputsType_Input()
     * @model type="net.opengis.wps10.InputDescriptionType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='Input'"
     * @generated
     */
    EList getInput();

} // DataInputsType
