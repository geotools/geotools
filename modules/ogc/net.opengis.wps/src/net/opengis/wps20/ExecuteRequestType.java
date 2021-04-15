/**
 */
package net.opengis.wps20;

import net.opengis.ows20.CodeType;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Execute Request Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 				Schema for a WPS Execute operation request, to execute
 * 				one identified process with the given data and provide the requested
 * 				output data.
 * 			
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.ExecuteRequestType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wps20.ExecuteRequestType#getInput <em>Input</em>}</li>
 *   <li>{@link net.opengis.wps20.ExecuteRequestType#getOutput <em>Output</em>}</li>
 *   <li>{@link net.opengis.wps20.ExecuteRequestType#getMode <em>Mode</em>}</li>
 *   <li>{@link net.opengis.wps20.ExecuteRequestType#getResponse <em>Response</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getExecuteRequestType()
 * @model extendedMetaData="name='ExecuteRequestType' kind='elementOnly'"
 * @generated
 */
public interface ExecuteRequestType extends RequestBaseType {
	/**
	 * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								Identifier of the process to be executed. All valid process identifiers are
	 * 								listed in the wps:Contents section of the Capabilities document.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Identifier</em>' containment reference.
	 * @see #setIdentifier(CodeType)
	 * @see net.opengis.wps20.Wps20Package#getExecuteRequestType_Identifier()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='Identifier' namespace='http://www.opengis.net/ows/2.0'"
	 * @generated
	 */
	CodeType getIdentifier();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.ExecuteRequestType#getIdentifier <em>Identifier</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Identifier</em>' containment reference.
	 * @see #getIdentifier()
	 * @generated
	 */
	void setIdentifier(CodeType value);

	/**
	 * Returns the value of the '<em><b>Input</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wps20.DataInputType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								One or more input items to be used for process execution, including referenced or inline data.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Input</em>' containment reference list.
	 * @see net.opengis.wps20.Wps20Package#getExecuteRequestType_Input()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Input' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<DataInputType> getInput();

	/**
	 * Returns the value of the '<em><b>Output</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wps20.OutputDefinitionType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								Defines one or more output items to be delivered by the process execution.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Output</em>' containment reference list.
	 * @see net.opengis.wps20.Wps20Package#getExecuteRequestType_Output()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='Output' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<OutputDefinitionType> getOutput();

	/**
	 * Returns the value of the '<em><b>Mode</b></em>' attribute.
	 * The literals are from the enumeration {@link net.opengis.wps20.ModeType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							Desired execution mode.
	 * 						
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Mode</em>' attribute.
	 * @see net.opengis.wps20.ModeType
	 * @see #isSetMode()
	 * @see #unsetMode()
	 * @see #setMode(ModeType)
	 * @see net.opengis.wps20.Wps20Package#getExecuteRequestType_Mode()
	 * @model unsettable="true" required="true"
	 *        extendedMetaData="kind='attribute' name='mode'"
	 * @generated
	 */
	ModeType getMode();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.ExecuteRequestType#getMode <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mode</em>' attribute.
	 * @see net.opengis.wps20.ModeType
	 * @see #isSetMode()
	 * @see #unsetMode()
	 * @see #getMode()
	 * @generated
	 */
	void setMode(ModeType value);

	/**
	 * Unsets the value of the '{@link net.opengis.wps20.ExecuteRequestType#getMode <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMode()
	 * @see #getMode()
	 * @see #setMode(ModeType)
	 * @generated
	 */
	void unsetMode();

	/**
	 * Returns whether the value of the '{@link net.opengis.wps20.ExecuteRequestType#getMode <em>Mode</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Mode</em>' attribute is set.
	 * @see #unsetMode()
	 * @see #getMode()
	 * @see #setMode(ModeType)
	 * @generated
	 */
	boolean isSetMode();

	/**
	 * Returns the value of the '<em><b>Response</b></em>' attribute.
	 * The literals are from the enumeration {@link net.opengis.wps20.ResponseType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Response</em>' attribute.
	 * @see net.opengis.wps20.ResponseType
	 * @see #isSetResponse()
	 * @see #unsetResponse()
	 * @see #setResponse(ResponseType)
	 * @see net.opengis.wps20.Wps20Package#getExecuteRequestType_Response()
	 * @model unsettable="true" required="true"
	 *        extendedMetaData="kind='attribute' name='response'"
	 * @generated
	 */
	ResponseType getResponse();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.ExecuteRequestType#getResponse <em>Response</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Response</em>' attribute.
	 * @see net.opengis.wps20.ResponseType
	 * @see #isSetResponse()
	 * @see #unsetResponse()
	 * @see #getResponse()
	 * @generated
	 */
	void setResponse(ResponseType value);

	/**
	 * Unsets the value of the '{@link net.opengis.wps20.ExecuteRequestType#getResponse <em>Response</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetResponse()
	 * @see #getResponse()
	 * @see #setResponse(ResponseType)
	 * @generated
	 */
	void unsetResponse();

	/**
	 * Returns whether the value of the '{@link net.opengis.wps20.ExecuteRequestType#getResponse <em>Response</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Response</em>' attribute is set.
	 * @see #unsetResponse()
	 * @see #getResponse()
	 * @see #setResponse(ResponseType)
	 * @generated
	 */
	boolean isSetResponse();

} // ExecuteRequestType
