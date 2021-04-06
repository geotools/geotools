/**
 */
package net.opengis.wps20;

import java.math.BigInteger;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Status Info Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.StatusInfoType#getJobID <em>Job ID</em>}</li>
 *   <li>{@link net.opengis.wps20.StatusInfoType#getStatus <em>Status</em>}</li>
 *   <li>{@link net.opengis.wps20.StatusInfoType#getExpirationDate <em>Expiration Date</em>}</li>
 *   <li>{@link net.opengis.wps20.StatusInfoType#getEstimatedCompletion <em>Estimated Completion</em>}</li>
 *   <li>{@link net.opengis.wps20.StatusInfoType#getNextPoll <em>Next Poll</em>}</li>
 *   <li>{@link net.opengis.wps20.StatusInfoType#getPercentCompleted <em>Percent Completed</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getStatusInfoType()
 * @model extendedMetaData="name='StatusInfo_._type' kind='elementOnly'"
 * @generated
 */
public interface StatusInfoType extends EObject {
	/**
	 * Returns the value of the '<em><b>Job ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				A JobID is a unique identifier for a process execution, i.e. a process instance.
	 * 				Particularly suitable JobIDs are UUIDs or monotonic identifiers such as unique timestamps.
	 * 				If the privacy of a Processing Job is imperative, the JobID should be non-guessable.
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Job ID</em>' attribute.
	 * @see #setJobID(String)
	 * @see net.opengis.wps20.Wps20Package#getStatusInfoType_JobID()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='JobID' namespace='##targetNamespace'"
	 * @generated
	 */
	String getJobID();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.StatusInfoType#getJobID <em>Job ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Job ID</em>' attribute.
	 * @see #getJobID()
	 * @generated
	 */
	void setJobID(String value);

	/**
	 * Returns the value of the '<em><b>Status</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							This element is used to communicate basic status information about executed processes.
	 * 						
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Status</em>' attribute.
	 * @see #setStatus(Object)
	 * @see net.opengis.wps20.Wps20Package#getStatusInfoType_Status()
	 * @model dataType="net.opengis.wps20.StatusType" required="true"
	 *        extendedMetaData="kind='element' name='Status' namespace='##targetNamespace'"
	 * @generated
	 */
	Object getStatus();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.StatusInfoType#getStatus <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Status</em>' attribute.
	 * @see #getStatus()
	 * @generated
	 */
	void setStatus(Object value);

	/**
	 * Returns the value of the '<em><b>Expiration Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				Date and time by which the job and its results will be removed from the server. Use if appropriate.
	 * 				In some situations the expiration date may not be known from the start. In this case, it is recommended
	 * 				to specify a timestamp for NextPoll.
	 * 				A typical example is a long running process for which the results are stored 48 hours after completion. While the
	 * 				process is running, clients are provided with updated timestamps for NextPoll. As soon as the process has completed
	 * 				the ExpirationDate is determined.
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Expiration Date</em>' attribute.
	 * @see #setExpirationDate(XMLGregorianCalendar)
	 * @see net.opengis.wps20.Wps20Package#getStatusInfoType_ExpirationDate()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.DateTime"
	 *        extendedMetaData="kind='element' name='ExpirationDate' namespace='##targetNamespace'"
	 * @generated
	 */
	XMLGregorianCalendar getExpirationDate();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.StatusInfoType#getExpirationDate <em>Expiration Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expiration Date</em>' attribute.
	 * @see #getExpirationDate()
	 * @generated
	 */
	void setExpirationDate(XMLGregorianCalendar value);

	/**
	 * Returns the value of the '<em><b>Estimated Completion</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							Estimated date and time by which the job will be completed. Use if available.
	 * 							The time of estimated completion lies significantly before the expiration date of this job.
	 * 						
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Estimated Completion</em>' attribute.
	 * @see #setEstimatedCompletion(XMLGregorianCalendar)
	 * @see net.opengis.wps20.Wps20Package#getStatusInfoType_EstimatedCompletion()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.DateTime"
	 *        extendedMetaData="kind='element' name='EstimatedCompletion' namespace='##targetNamespace'"
	 * @generated
	 */
	XMLGregorianCalendar getEstimatedCompletion();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.StatusInfoType#getEstimatedCompletion <em>Estimated Completion</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Estimated Completion</em>' attribute.
	 * @see #getEstimatedCompletion()
	 * @generated
	 */
	void setEstimatedCompletion(XMLGregorianCalendar value);

	/**
	 * Returns the value of the '<em><b>Next Poll</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							Suggested date and time for the next status poll (GetStatus) for this job. Use if appropriate.
	 * 							The time of the next poll shall lie significantly before the expiration date of this job.
	 * 							If this element is provided but an expiration date for the job is not given, clients are expected to check
	 * 							the job status on time to eventually receive an update on the expiration date and avoid missing the results.
	 * 						
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Next Poll</em>' attribute.
	 * @see #setNextPoll(XMLGregorianCalendar)
	 * @see net.opengis.wps20.Wps20Package#getStatusInfoType_NextPoll()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.DateTime"
	 *        extendedMetaData="kind='element' name='NextPoll' namespace='##targetNamespace'"
	 * @generated
	 */
	XMLGregorianCalendar getNextPoll();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.StatusInfoType#getNextPoll <em>Next Poll</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Next Poll</em>' attribute.
	 * @see #getNextPoll()
	 * @generated
	 */
	void setNextPoll(XMLGregorianCalendar value);

	/**
	 * Returns the value of the '<em><b>Percent Completed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							Use as a progress indicator if appropriate. Like most progress bars the value is an estimate without accuracy guarantees.
	 * 						
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Percent Completed</em>' attribute.
	 * @see #setPercentCompleted(BigInteger)
	 * @see net.opengis.wps20.Wps20Package#getStatusInfoType_PercentCompleted()
	 * @model dataType="net.opengis.wps20.PercentCompletedType"
	 *        extendedMetaData="kind='element' name='PercentCompleted' namespace='##targetNamespace'"
	 * @generated
	 */
	BigInteger getPercentCompleted();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.StatusInfoType#getPercentCompleted <em>Percent Completed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Percent Completed</em>' attribute.
	 * @see #getPercentCompleted()
	 * @generated
	 */
	void setPercentCompleted(BigInteger value);

} // StatusInfoType
