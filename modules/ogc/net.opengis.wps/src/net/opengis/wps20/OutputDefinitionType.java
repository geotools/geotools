/**
 */
package net.opengis.wps20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Output Definition Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 				This structure contains information elements that describe the format and transmission mode
 * 				of the output data that is delivered by a process execution
 * 			
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.OutputDefinitionType#getOutput <em>Output</em>}</li>
 *   <li>{@link net.opengis.wps20.OutputDefinitionType#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link net.opengis.wps20.OutputDefinitionType#getId <em>Id</em>}</li>
 *   <li>{@link net.opengis.wps20.OutputDefinitionType#getMimeType <em>Mime Type</em>}</li>
 *   <li>{@link net.opengis.wps20.OutputDefinitionType#getSchema <em>Schema</em>}</li>
 *   <li>{@link net.opengis.wps20.OutputDefinitionType#getTransmission <em>Transmission</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getOutputDefinitionType()
 * @model extendedMetaData="name='OutputDefinitionType' kind='elementOnly'"
 * @generated
 */
public interface OutputDefinitionType extends EObject {
	/**
	 * Returns the value of the '<em><b>Output</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output</em>' containment reference.
	 * @see #setOutput(OutputDefinitionType)
	 * @see net.opengis.wps20.Wps20Package#getOutputDefinitionType_Output()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Output' namespace='##targetNamespace'"
	 * @generated
	 */
	OutputDefinitionType getOutput();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.OutputDefinitionType#getOutput <em>Output</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Output</em>' containment reference.
	 * @see #getOutput()
	 * @generated
	 */
	void setOutput(OutputDefinitionType value);

	/**
	 * Returns the value of the '<em><b>Encoding</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					Encoding procedure or character set used (e.g. raw, base64, or UTF-8).
	 * 				
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Encoding</em>' attribute.
	 * @see #setEncoding(String)
	 * @see net.opengis.wps20.Wps20Package#getOutputDefinitionType_Encoding()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='encoding'"
	 * @generated
	 */
	String getEncoding();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.OutputDefinitionType#getEncoding <em>Encoding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Encoding</em>' attribute.
	 * @see #getEncoding()
	 * @generated
	 */
	void setEncoding(String value);

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					Identifier of this output.
	 * 				
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see net.opengis.wps20.Wps20Package#getOutputDefinitionType_Id()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
	 *        extendedMetaData="kind='attribute' name='id'"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.OutputDefinitionType#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Mime Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					Media type of the data.
	 * 				
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Mime Type</em>' attribute.
	 * @see #setMimeType(String)
	 * @see net.opengis.wps20.Wps20Package#getOutputDefinitionType_MimeType()
	 * @model dataType="net.opengis.ows20.MimeType"
	 *        extendedMetaData="kind='attribute' name='mimeType'"
	 * @generated
	 */
	String getMimeType();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.OutputDefinitionType#getMimeType <em>Mime Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mime Type</em>' attribute.
	 * @see #getMimeType()
	 * @generated
	 */
	void setMimeType(String value);

	/**
	 * Returns the value of the '<em><b>Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					Identification of the data schema.
	 * 				
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Schema</em>' attribute.
	 * @see #setSchema(String)
	 * @see net.opengis.wps20.Wps20Package#getOutputDefinitionType_Schema()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='schema'"
	 * @generated
	 */
	String getSchema();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.OutputDefinitionType#getSchema <em>Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schema</em>' attribute.
	 * @see #getSchema()
	 * @generated
	 */
	void setSchema(String value);

	/**
	 * Returns the value of the '<em><b>Transmission</b></em>' attribute.
	 * The literals are from the enumeration {@link net.opengis.wps20.DataTransmissionModeType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The desired transmission mode for this output
	 * 				
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Transmission</em>' attribute.
	 * @see net.opengis.wps20.DataTransmissionModeType
	 * @see #isSetTransmission()
	 * @see #unsetTransmission()
	 * @see #setTransmission(DataTransmissionModeType)
	 * @see net.opengis.wps20.Wps20Package#getOutputDefinitionType_Transmission()
	 * @model unsettable="true"
	 *        extendedMetaData="kind='attribute' name='transmission'"
	 * @generated
	 */
	DataTransmissionModeType getTransmission();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.OutputDefinitionType#getTransmission <em>Transmission</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transmission</em>' attribute.
	 * @see net.opengis.wps20.DataTransmissionModeType
	 * @see #isSetTransmission()
	 * @see #unsetTransmission()
	 * @see #getTransmission()
	 * @generated
	 */
	void setTransmission(DataTransmissionModeType value);

	/**
	 * Unsets the value of the '{@link net.opengis.wps20.OutputDefinitionType#getTransmission <em>Transmission</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetTransmission()
	 * @see #getTransmission()
	 * @see #setTransmission(DataTransmissionModeType)
	 * @generated
	 */
	void unsetTransmission();

	/**
	 * Returns whether the value of the '{@link net.opengis.wps20.OutputDefinitionType#getTransmission <em>Transmission</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Transmission</em>' attribute is set.
	 * @see #unsetTransmission()
	 * @see #getTransmission()
	 * @see #setTransmission(DataTransmissionModeType)
	 * @generated
	 */
	boolean isSetTransmission();

} // OutputDefinitionType
