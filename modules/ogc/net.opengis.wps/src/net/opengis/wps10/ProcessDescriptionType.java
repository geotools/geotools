/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Full description of a process.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ProcessDescriptionType#getDataInputs <em>Data Inputs</em>}</li>
 *   <li>{@link net.opengis.wps10.ProcessDescriptionType#getProcessOutputs <em>Process Outputs</em>}</li>
 *   <li>{@link net.opengis.wps10.ProcessDescriptionType#isStatusSupported <em>Status Supported</em>}</li>
 *   <li>{@link net.opengis.wps10.ProcessDescriptionType#isStoreSupported <em>Store Supported</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getProcessDescriptionType()
 * @model extendedMetaData="name='ProcessDescriptionType' kind='elementOnly'"
 * @generated
 */
public interface ProcessDescriptionType extends ProcessBriefType {
    /**
     * Returns the value of the '<em><b>Data Inputs</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * List of the inputs to this process. In almost all cases, at least one process input is required. However, no process inputs may be identified when all the inputs are predetermined fixed resources.  In this case, those resources shall be identified in the ows:Abstract element that describes the process.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Data Inputs</em>' containment reference.
     * @see #setDataInputs(DataInputsType)
     * @see net.opengis.wps10.Wps10Package#getProcessDescriptionType_DataInputs()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='DataInputs'"
     * @generated
     */
    DataInputsType getDataInputs();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ProcessDescriptionType#getDataInputs <em>Data Inputs</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Inputs</em>' containment reference.
     * @see #getDataInputs()
     * @generated
     */
    void setDataInputs(DataInputsType value);

    /**
     * Returns the value of the '<em><b>Process Outputs</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * List of outputs which will or can result from executing the process.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Process Outputs</em>' containment reference.
     * @see #setProcessOutputs(ProcessOutputsType)
     * @see net.opengis.wps10.Wps10Package#getProcessDescriptionType_ProcessOutputs()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='ProcessOutputs'"
     * @generated
     */
    ProcessOutputsType getProcessOutputs();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ProcessDescriptionType#getProcessOutputs <em>Process Outputs</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Process Outputs</em>' containment reference.
     * @see #getProcessOutputs()
     * @generated
     */
    void setProcessOutputs(ProcessOutputsType value);

    /**
     * Returns the value of the '<em><b>Status Supported</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates if ongoing status information can be provided for this process.  If "true", the Status element of the stored Execute response document shall be kept up to date.  If "false" then the Status element shall not be updated until processing is complete. By default, status information is not provided for this process.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Status Supported</em>' attribute.
     * @see #isSetStatusSupported()
     * @see #unsetStatusSupported()
     * @see #setStatusSupported(boolean)
     * @see net.opengis.wps10.Wps10Package#getProcessDescriptionType_StatusSupported()
     * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='attribute' name='statusSupported'"
     * @generated
     */
    boolean isStatusSupported();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ProcessDescriptionType#isStatusSupported <em>Status Supported</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Status Supported</em>' attribute.
     * @see #isSetStatusSupported()
     * @see #unsetStatusSupported()
     * @see #isStatusSupported()
     * @generated
     */
    void setStatusSupported(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.wps10.ProcessDescriptionType#isStatusSupported <em>Status Supported</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetStatusSupported()
     * @see #isStatusSupported()
     * @see #setStatusSupported(boolean)
     * @generated
     */
    void unsetStatusSupported();

    /**
     * Returns whether the value of the '{@link net.opengis.wps10.ProcessDescriptionType#isStatusSupported <em>Status Supported</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Status Supported</em>' attribute is set.
     * @see #unsetStatusSupported()
     * @see #isStatusSupported()
     * @see #setStatusSupported(boolean)
     * @generated
     */
    boolean isSetStatusSupported();

    /**
     * Returns the value of the '<em><b>Store Supported</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates if ComplexData outputs from this process can be stored by the WPS server as web-accessible resources. If "storeSupported" is "true", the Execute operation request may include "asReference" equals "true" for any complex output, directing that the output of the process be stored so that the client can retrieve it as required. By default for this process, storage is not supported and all outputs are returned encoded in the Execute response.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Store Supported</em>' attribute.
     * @see #isSetStoreSupported()
     * @see #unsetStoreSupported()
     * @see #setStoreSupported(boolean)
     * @see net.opengis.wps10.Wps10Package#getProcessDescriptionType_StoreSupported()
     * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='attribute' name='storeSupported'"
     * @generated
     */
    boolean isStoreSupported();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ProcessDescriptionType#isStoreSupported <em>Store Supported</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Store Supported</em>' attribute.
     * @see #isSetStoreSupported()
     * @see #unsetStoreSupported()
     * @see #isStoreSupported()
     * @generated
     */
    void setStoreSupported(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.wps10.ProcessDescriptionType#isStoreSupported <em>Store Supported</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetStoreSupported()
     * @see #isStoreSupported()
     * @see #setStoreSupported(boolean)
     * @generated
     */
    void unsetStoreSupported();

    /**
     * Returns whether the value of the '{@link net.opengis.wps10.ProcessDescriptionType#isStoreSupported <em>Store Supported</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Store Supported</em>' attribute is set.
     * @see #unsetStoreSupported()
     * @see #isStoreSupported()
     * @see #setStoreSupported(boolean)
     * @generated
     */
    boolean isSetStoreSupported();

} // ProcessDescriptionType
