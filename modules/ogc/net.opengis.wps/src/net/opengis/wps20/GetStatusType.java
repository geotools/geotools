/**
 */
package net.opengis.wps20;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Status Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.GetStatusType#getJobID <em>Job ID</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getGetStatusType()
 * @model extendedMetaData="name='GetStatus_._type' kind='elementOnly'"
 * @generated
 */
public interface GetStatusType extends RequestBaseType {
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
	 * @see net.opengis.wps20.Wps20Package#getGetStatusType_JobID()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='JobID' namespace='##targetNamespace'"
	 * @generated
	 */
	String getJobID();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.GetStatusType#getJobID <em>Job ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Job ID</em>' attribute.
	 * @see #getJobID()
	 * @generated
	 */
	void setJobID(String value);

} // GetStatusType
