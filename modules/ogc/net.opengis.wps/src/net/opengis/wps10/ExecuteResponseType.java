/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Execute Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ExecuteResponseType#getProcess <em>Process</em>}</li>
 *   <li>{@link net.opengis.wps10.ExecuteResponseType#getStatus <em>Status</em>}</li>
 *   <li>{@link net.opengis.wps10.ExecuteResponseType#getDataInputs <em>Data Inputs</em>}</li>
 *   <li>{@link net.opengis.wps10.ExecuteResponseType#getOutputDefinitions <em>Output Definitions</em>}</li>
 *   <li>{@link net.opengis.wps10.ExecuteResponseType#getProcessOutputs <em>Process Outputs</em>}</li>
 *   <li>{@link net.opengis.wps10.ExecuteResponseType#getServiceInstance <em>Service Instance</em>}</li>
 *   <li>{@link net.opengis.wps10.ExecuteResponseType#getStatusLocation <em>Status Location</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getExecuteResponseType()
 * @model extendedMetaData="name='ExecuteResponse_._type' kind='elementOnly'"
 * @generated
 */
public interface ExecuteResponseType extends ResponseBaseType {
    /**
     * Returns the value of the '<em><b>Process</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Process description from the ProcessOfferings section of the GetCapabilities response.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Process</em>' containment reference.
     * @see #setProcess(ProcessBriefType)
     * @see net.opengis.wps10.Wps10Package#getExecuteResponseType_Process()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Process' namespace='##targetNamespace'"
     * @generated
     */
    ProcessBriefType getProcess();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ExecuteResponseType#getProcess <em>Process</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Process</em>' containment reference.
     * @see #getProcess()
     * @generated
     */
    void setProcess(ProcessBriefType value);

    /**
     * Returns the value of the '<em><b>Status</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Execution status of this process.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Status</em>' containment reference.
     * @see #setStatus(StatusType)
     * @see net.opengis.wps10.Wps10Package#getExecuteResponseType_Status()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Status' namespace='##targetNamespace'"
     * @generated
     */
    StatusType getStatus();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ExecuteResponseType#getStatus <em>Status</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Status</em>' containment reference.
     * @see #getStatus()
     * @generated
     */
    void setStatus(StatusType value);

    /**
     * Returns the value of the '<em><b>Data Inputs</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Inputs that were provided as part of the execute request. This element shall be omitted unless the lineage attribute of the execute request is set to "true".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Data Inputs</em>' containment reference.
     * @see #setDataInputs(DataInputsType1)
     * @see net.opengis.wps10.Wps10Package#getExecuteResponseType_DataInputs()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='DataInputs' namespace='##targetNamespace'"
     * @generated
     */
    DataInputsType1 getDataInputs();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ExecuteResponseType#getDataInputs <em>Data Inputs</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Inputs</em>' containment reference.
     * @see #getDataInputs()
     * @generated
     */
    void setDataInputs(DataInputsType1 value);

    /**
     * Returns the value of the '<em><b>Output Definitions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Complete list of Output data types that were requested as part of the Execute request. This element shall be omitted unless the lineage attribute of the execute request is set to "true".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Output Definitions</em>' containment reference.
     * @see #setOutputDefinitions(OutputDefinitionsType)
     * @see net.opengis.wps10.Wps10Package#getExecuteResponseType_OutputDefinitions()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='OutputDefinitions' namespace='##targetNamespace'"
     * @generated
     */
    OutputDefinitionsType getOutputDefinitions();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ExecuteResponseType#getOutputDefinitions <em>Output Definitions</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Output Definitions</em>' containment reference.
     * @see #getOutputDefinitions()
     * @generated
     */
    void setOutputDefinitions(OutputDefinitionsType value);

    /**
     * Returns the value of the '<em><b>Process Outputs</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * List of values of the Process output parameters. Normally there would be at least one output when the process has completed successfully. If the process has not finished executing, the implementer can choose to include whatever final results are ready at the time the Execute response is provided. If the reference locations of outputs are known in advance, these URLs may be provided before they are populated.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Process Outputs</em>' containment reference.
     * @see #setProcessOutputs(ProcessOutputsType1)
     * @see net.opengis.wps10.Wps10Package#getExecuteResponseType_ProcessOutputs()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ProcessOutputs' namespace='##targetNamespace'"
     * @generated
     */
    ProcessOutputsType1 getProcessOutputs();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ExecuteResponseType#getProcessOutputs <em>Process Outputs</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Process Outputs</em>' containment reference.
     * @see #getProcessOutputs()
     * @generated
     */
    void setProcessOutputs(ProcessOutputsType1 value);

    /**
     * Returns the value of the '<em><b>Service Instance</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This attribute shall contain the GetCapabilities URL of the WPS service which was invoked
     * <!-- end-model-doc -->
     * @return the value of the '<em>Service Instance</em>' attribute.
     * @see #setServiceInstance(String)
     * @see net.opengis.wps10.Wps10Package#getExecuteResponseType_ServiceInstance()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='attribute' name='serviceInstance'"
     * @generated
     */
    String getServiceInstance();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ExecuteResponseType#getServiceInstance <em>Service Instance</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service Instance</em>' attribute.
     * @see #getServiceInstance()
     * @generated
     */
    void setServiceInstance(String value);

    /**
     * Returns the value of the '<em><b>Status Location</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The URL referencing the location from which the ExecuteResponse can be retrieved. If "status" is "true" in the Execute request, the ExecuteResponse should also be found here as soon as the process returns the initial response to the client. It should persist at this location as long as the outputs are accessible from the server. The outputs may be stored for as long as the implementer of the server decides. If the process takes a long time, this URL can be repopulated on an ongoing basis in order to keep the client updated on progress. Before the process has succeeded, the ExecuteResponse contains information about the status of the process, including whether or not processing has started, and the percentage completed. It may also optionally contain the inputs and any ProcessStartedType interim results. When the process has succeeded, the ExecuteResponse found at this URL shall contain the output values or references to them.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Status Location</em>' attribute.
     * @see #setStatusLocation(String)
     * @see net.opengis.wps10.Wps10Package#getExecuteResponseType_StatusLocation()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='statusLocation'"
     * @generated
     */
    String getStatusLocation();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ExecuteResponseType#getStatusLocation <em>Status Location</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Status Location</em>' attribute.
     * @see #getStatusLocation()
     * @generated
     */
    void setStatusLocation(String value);

} // ExecuteResponseType
