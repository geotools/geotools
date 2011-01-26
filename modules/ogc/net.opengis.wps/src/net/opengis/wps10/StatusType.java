/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Status Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Description of the status of process execution.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.StatusType#getProcessAccepted <em>Process Accepted</em>}</li>
 *   <li>{@link net.opengis.wps10.StatusType#getProcessStarted <em>Process Started</em>}</li>
 *   <li>{@link net.opengis.wps10.StatusType#getProcessPaused <em>Process Paused</em>}</li>
 *   <li>{@link net.opengis.wps10.StatusType#getProcessSucceeded <em>Process Succeeded</em>}</li>
 *   <li>{@link net.opengis.wps10.StatusType#getProcessFailed <em>Process Failed</em>}</li>
 *   <li>{@link net.opengis.wps10.StatusType#getCreationTime <em>Creation Time</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getStatusType()
 * @model extendedMetaData="name='StatusType' kind='elementOnly'"
 * @generated
 */
public interface StatusType extends EObject {
    /**
     * Returns the value of the '<em><b>Process Accepted</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates that this process has been accepted by the server, but is in a queue and has not yet started to execute. The contents of this human-readable text string is left open to definition by each server implementation, but is expected to include any messages the server may wish to let the clients know. Such information could include how long the queue is, or any warning conditions that may have been encountered. The client may display this text to a human user.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Process Accepted</em>' attribute.
     * @see #setProcessAccepted(String)
     * @see net.opengis.wps10.Wps10Package#getStatusType_ProcessAccepted()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='ProcessAccepted' namespace='##targetNamespace'"
     * @generated
     */
    String getProcessAccepted();

    /**
     * Sets the value of the '{@link net.opengis.wps10.StatusType#getProcessAccepted <em>Process Accepted</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Process Accepted</em>' attribute.
     * @see #getProcessAccepted()
     * @generated
     */
    void setProcessAccepted(String value);

    /**
     * Returns the value of the '<em><b>Process Started</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates that this process has been accepted by the server, and processing has begun.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Process Started</em>' containment reference.
     * @see #setProcessStarted(ProcessStartedType)
     * @see net.opengis.wps10.Wps10Package#getStatusType_ProcessStarted()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ProcessStarted' namespace='##targetNamespace'"
     * @generated
     */
    ProcessStartedType getProcessStarted();

    /**
     * Sets the value of the '{@link net.opengis.wps10.StatusType#getProcessStarted <em>Process Started</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Process Started</em>' containment reference.
     * @see #getProcessStarted()
     * @generated
     */
    void setProcessStarted(ProcessStartedType value);

    /**
     * Returns the value of the '<em><b>Process Paused</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates that this process has been  accepted by the server, and processing has started but subsequently been paused by the server.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Process Paused</em>' containment reference.
     * @see #setProcessPaused(ProcessStartedType)
     * @see net.opengis.wps10.Wps10Package#getStatusType_ProcessPaused()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ProcessPaused' namespace='##targetNamespace'"
     * @generated
     */
    ProcessStartedType getProcessPaused();

    /**
     * Sets the value of the '{@link net.opengis.wps10.StatusType#getProcessPaused <em>Process Paused</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Process Paused</em>' containment reference.
     * @see #getProcessPaused()
     * @generated
     */
    void setProcessPaused(ProcessStartedType value);

    /**
     * Returns the value of the '<em><b>Process Succeeded</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates that this process has successfully completed execution. The contents of this human-readable text string is left open to definition by each server, but is expected to include any messages the server may wish to let the clients know, such as how long the process took to execute, or any warning conditions that may have been encountered. The client may display this text string to a human user. The client should make use of the presence of this element to trigger automated or manual access to the results of the process. If manual access is intended, the client should use the presence of this element to present the results as downloadable links to the user.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Process Succeeded</em>' attribute.
     * @see #setProcessSucceeded(String)
     * @see net.opengis.wps10.Wps10Package#getStatusType_ProcessSucceeded()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='ProcessSucceeded' namespace='##targetNamespace'"
     * @generated
     */
    String getProcessSucceeded();

    /**
     * Sets the value of the '{@link net.opengis.wps10.StatusType#getProcessSucceeded <em>Process Succeeded</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Process Succeeded</em>' attribute.
     * @see #getProcessSucceeded()
     * @generated
     */
    void setProcessSucceeded(String value);

    /**
     * Returns the value of the '<em><b>Process Failed</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates that execution of this process has failed, and includes error information.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Process Failed</em>' containment reference.
     * @see #setProcessFailed(ProcessFailedType)
     * @see net.opengis.wps10.Wps10Package#getStatusType_ProcessFailed()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ProcessFailed' namespace='##targetNamespace'"
     * @generated
     */
    ProcessFailedType getProcessFailed();

    /**
     * Sets the value of the '{@link net.opengis.wps10.StatusType#getProcessFailed <em>Process Failed</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Process Failed</em>' containment reference.
     * @see #getProcessFailed()
     * @generated
     */
    void setProcessFailed(ProcessFailedType value);

    /**
     * Returns the value of the '<em><b>Creation Time</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The time (UTC) that the process finished.  If the process is still executing or awaiting execution, this element shall contain the creation time of this document.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Creation Time</em>' attribute.
     * @see #setCreationTime(Object)
     * @see net.opengis.wps10.Wps10Package#getStatusType_CreationTime()
     * @model dataType="org.eclipse.emf.ecore.xml.type.DateTime" required="true"
     *        extendedMetaData="kind='attribute' name='creationTime'"
     * @generated
     */
    Object getCreationTime();

    /**
     * Sets the value of the '{@link net.opengis.wps10.StatusType#getCreationTime <em>Creation Time</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Creation Time</em>' attribute.
     * @see #getCreationTime()
     * @generated
     */
    void setCreationTime(Object value);

} // StatusType
