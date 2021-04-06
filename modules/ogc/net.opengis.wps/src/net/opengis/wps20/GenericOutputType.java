/**
 */
package net.opengis.wps20;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Generic Output Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Description of a process Output. 
 * 
 * 						In this use, the DescriptionType shall describe a process output.
 * 					
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.GenericOutputType#getOutput <em>Output</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getGenericOutputType()
 * @model extendedMetaData="name='GenericOutputType' kind='elementOnly'"
 * @generated
 */
public interface GenericOutputType extends DescriptionType {
	/**
	 * Returns the value of the '<em><b>Output</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wps20.GenericOutputType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output</em>' containment reference list.
	 * @see net.opengis.wps20.Wps20Package#getGenericOutputType_Output()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Output' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<GenericOutputType> getOutput();

} // GenericOutputType
