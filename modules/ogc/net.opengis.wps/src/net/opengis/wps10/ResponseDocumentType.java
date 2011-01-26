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
 * A representation of the model object '<em><b>Response Document Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ResponseDocumentType#getOutput <em>Output</em>}</li>
 *   <li>{@link net.opengis.wps10.ResponseDocumentType#isLineage <em>Lineage</em>}</li>
 *   <li>{@link net.opengis.wps10.ResponseDocumentType#isStatus <em>Status</em>}</li>
 *   <li>{@link net.opengis.wps10.ResponseDocumentType#isStoreExecuteResponse <em>Store Execute Response</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getResponseDocumentType()
 * @model extendedMetaData="name='ResponseDocumentType' kind='elementOnly'"
 * @generated
 */
public interface ResponseDocumentType extends EObject {
    /**
     * Returns the value of the '<em><b>Output</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wps10.DocumentOutputDefinitionType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of definitions of the outputs (or parameters) requested from the process. These outputs are not normally identified, unless the client is specifically requesting a limited subset of outputs, and/or is requesting output formats and/or schemas and/or encodings different from the defaults and selected from the alternatives identified in the process description, or wishes to customize the descriptive information about the output.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Output</em>' containment reference list.
     * @see net.opengis.wps10.Wps10Package#getResponseDocumentType_Output()
     * @model type="net.opengis.wps10.DocumentOutputDefinitionType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='Output' namespace='##targetNamespace'"
     * @generated
     */
    EList getOutput();

    /**
     * Returns the value of the '<em><b>Lineage</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates if the Execute operation response shall include the DataInputs and OutputDefinitions elements.  If lineage is "true" the server shall include in the execute response a complete copy of the DataInputs and OutputDefinition elements as received in the execute request.  If lineage is "false" then these elements shall be omitted from the response.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Lineage</em>' attribute.
     * @see #isSetLineage()
     * @see #unsetLineage()
     * @see #setLineage(boolean)
     * @see net.opengis.wps10.Wps10Package#getResponseDocumentType_Lineage()
     * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='attribute' name='lineage'"
     * @generated
     */
    boolean isLineage();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ResponseDocumentType#isLineage <em>Lineage</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lineage</em>' attribute.
     * @see #isSetLineage()
     * @see #unsetLineage()
     * @see #isLineage()
     * @generated
     */
    void setLineage(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.wps10.ResponseDocumentType#isLineage <em>Lineage</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetLineage()
     * @see #isLineage()
     * @see #setLineage(boolean)
     * @generated
     */
    void unsetLineage();

    /**
     * Returns whether the value of the '{@link net.opengis.wps10.ResponseDocumentType#isLineage <em>Lineage</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Lineage</em>' attribute is set.
     * @see #unsetLineage()
     * @see #isLineage()
     * @see #setLineage(boolean)
     * @generated
     */
    boolean isSetLineage();

    /**
     * Returns the value of the '<em><b>Status</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates if the stored execute response document shall be updated to provide ongoing reports on the status of execution.  If status is "true" and storeExecuteResponse is "true" (and the server has indicated that both storeSupported and statusSupported are "true")  then the Status element of the execute response document stored at executeResponseLocation is kept up to date by the process.  While the execute response contains ProcessAccepted, ProcessStarted, or ProcessPaused, updates shall be made to the executeResponse document until either the process completes successfully (in which case ProcessSucceeded is populated), or the process fails (in which case ProcessFailed is populated).  If status is "false" then the Status element shall not be updated until the process either completes successfully or fails.  If status="true" and storeExecuteResponse is "false" then the service shall raise an exception.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Status</em>' attribute.
     * @see #isSetStatus()
     * @see #unsetStatus()
     * @see #setStatus(boolean)
     * @see net.opengis.wps10.Wps10Package#getResponseDocumentType_Status()
     * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='attribute' name='status'"
     * @generated
     */
    boolean isStatus();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ResponseDocumentType#isStatus <em>Status</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Status</em>' attribute.
     * @see #isSetStatus()
     * @see #unsetStatus()
     * @see #isStatus()
     * @generated
     */
    void setStatus(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.wps10.ResponseDocumentType#isStatus <em>Status</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetStatus()
     * @see #isStatus()
     * @see #setStatus(boolean)
     * @generated
     */
    void unsetStatus();

    /**
     * Returns whether the value of the '{@link net.opengis.wps10.ResponseDocumentType#isStatus <em>Status</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Status</em>' attribute is set.
     * @see #unsetStatus()
     * @see #isStatus()
     * @see #setStatus(boolean)
     * @generated
     */
    boolean isSetStatus();

    /**
     * Returns the value of the '<em><b>Store Execute Response</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates if the execute response document shall be stored.  If "true" then the executeResponseLocation attribute in the execute response becomes mandatory, which will point to the location where the executeResponseDocument is stored.  The service shall respond immediately to the request and return an executeResponseDocument containing the executeResponseLocation and the status element which has five possible subelements (choice):ProcessAccepted, ProcessStarted, ProcessPaused, ProcessFailed and ProcessSucceeded, which are chosen and populated as follows:   1) If the process is completed when the initial executeResponseDocument is returned, the element ProcessSucceeded is populated with the process results.  2) If the process already failed when the initial executeResponseDocument is returned, the element ProcessFailed is populated with the Exception.  3) If the process has been paused when the initial executeResponseDocument is returned, the element ProcessPaused is populated.  4) If the process has been accepted when the initial executeResponseDocument is returned, the element ProcessAccepted is populated, including percentage information. 5) If the process execution is ongoing when the initial executeResponseDocument is returned, the element ProcessStarted is populated.  In case 3, 4, and 5, if status updating is requested, updates are made to the executeResponseDocument at the executeResponseLocation until either the process completes successfully or fails.  Regardless, once the process completes successfully, the ProcessSucceeded element is populated, and if it fails, the ProcessFailed element is populated.
     * Specifies if the Execute operation response shall be returned quickly with status information, or not returned until process execution is complete. This parameter shall not be included unless the corresponding "statusSupported" parameter is included and is "true" in the ProcessDescription for this process.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Store Execute Response</em>' attribute.
     * @see #isSetStoreExecuteResponse()
     * @see #unsetStoreExecuteResponse()
     * @see #setStoreExecuteResponse(boolean)
     * @see net.opengis.wps10.Wps10Package#getResponseDocumentType_StoreExecuteResponse()
     * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='attribute' name='storeExecuteResponse'"
     * @generated
     */
    boolean isStoreExecuteResponse();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ResponseDocumentType#isStoreExecuteResponse <em>Store Execute Response</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Store Execute Response</em>' attribute.
     * @see #isSetStoreExecuteResponse()
     * @see #unsetStoreExecuteResponse()
     * @see #isStoreExecuteResponse()
     * @generated
     */
    void setStoreExecuteResponse(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.wps10.ResponseDocumentType#isStoreExecuteResponse <em>Store Execute Response</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetStoreExecuteResponse()
     * @see #isStoreExecuteResponse()
     * @see #setStoreExecuteResponse(boolean)
     * @generated
     */
    void unsetStoreExecuteResponse();

    /**
     * Returns whether the value of the '{@link net.opengis.wps10.ResponseDocumentType#isStoreExecuteResponse <em>Store Execute Response</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Store Execute Response</em>' attribute is set.
     * @see #unsetStoreExecuteResponse()
     * @see #isStoreExecuteResponse()
     * @see #setStoreExecuteResponse(boolean)
     * @generated
     */
    boolean isSetStoreExecuteResponse();

} // ResponseDocumentType
