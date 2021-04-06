/**
 */
package net.opengis.wps20;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Generic Process Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 						In this use, the DescriptionType shall describe process properties.
 * 					
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.GenericProcessType#getInput <em>Input</em>}</li>
 *   <li>{@link net.opengis.wps20.GenericProcessType#getOutput <em>Output</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getGenericProcessType()
 * @model extendedMetaData="name='GenericProcessType' kind='elementOnly'"
 * @generated
 */
public interface GenericProcessType extends DescriptionType {
	/**
	 * Returns the value of the '<em><b>Input</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wps20.GenericInputType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								A process can have zero or more inputs.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Input</em>' containment reference list.
	 * @see net.opengis.wps20.Wps20Package#getGenericProcessType_Input()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Input' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<GenericInputType> getInput();

	/**
	 * Returns the value of the '<em><b>Output</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wps20.GenericOutputType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								A process can have one or more outputs.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Output</em>' containment reference list.
	 * @see net.opengis.wps20.Wps20Package#getGenericProcessType_Output()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='Output' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<GenericOutputType> getOutput();

} // GenericProcessType
