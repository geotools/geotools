/**
 */
package net.opengis.wps20;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Full description of a process. 
 * 
 * 						In this use, the DescriptionType shall describe process properties.
 * 					
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.ProcessDescriptionType#getInput <em>Input</em>}</li>
 *   <li>{@link net.opengis.wps20.ProcessDescriptionType#getOutput <em>Output</em>}</li>
 *   <li>{@link net.opengis.wps20.ProcessDescriptionType#getLang <em>Lang</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getProcessDescriptionType()
 * @model extendedMetaData="name='ProcessDescriptionType' kind='elementOnly'"
 * @generated
 */
public interface ProcessDescriptionType extends DescriptionType {
	/**
	 * Returns the value of the '<em><b>Input</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wps20.InputDescriptionType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								A process can have zero or more inputs.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Input</em>' containment reference list.
	 * @see net.opengis.wps20.Wps20Package#getProcessDescriptionType_Input()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Input' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<InputDescriptionType> getInput();

	/**
	 * Returns the value of the '<em><b>Output</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wps20.OutputDescriptionType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								A process can have one or more outputs.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Output</em>' containment reference list.
	 * @see net.opengis.wps20.Wps20Package#getProcessDescriptionType_Output()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='Output' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<OutputDescriptionType> getOutput();

	/**
	 * Returns the value of the '<em><b>Lang</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							Identifier of a language used by the data(set) contents.
	 * 							This language identifier shall be as specified in IETF RFC 4646. The
	 * 							language tags shall be either complete 5 character codes (e.g. "en-CA"),
	 * 							or abbreviated 2 character codes (e.g. "en"). In addition to the RFC
	 * 							4646 codes, the server shall support the single special value "*" which
	 * 							is used to indicate "any language".
	 * 						
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Lang</em>' attribute.
	 * @see #setLang(String)
	 * @see net.opengis.wps20.Wps20Package#getProcessDescriptionType_Lang()
	 * @model dataType="org.eclipse.emf.ecore.xml.namespace.LangType"
	 *        extendedMetaData="kind='attribute' name='lang' namespace='http://www.w3.org/XML/1998/namespace'"
	 * @generated
	 */
	String getLang();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.ProcessDescriptionType#getLang <em>Lang</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lang</em>' attribute.
	 * @see #getLang()
	 * @generated
	 */
	void setLang(String value);

} // ProcessDescriptionType
