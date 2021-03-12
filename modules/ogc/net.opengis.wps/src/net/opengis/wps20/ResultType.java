/**
 */
package net.opengis.wps20;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Result Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.ResultType#getJobID <em>Job ID</em>}</li>
 *   <li>{@link net.opengis.wps20.ResultType#getExpirationDate <em>Expiration Date</em>}</li>
 *   <li>{@link net.opengis.wps20.ResultType#getOutput <em>Output</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getResultType()
 * @model extendedMetaData="name='Result_._type' kind='elementOnly'"
 * @generated
 */
public interface ResultType extends EObject {
	/**
	 * Returns the value of the '<em><b>Job ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							Include if required. A JobId is usually required for
	 * 							a) asynchronous execution
	 * 							b) the Dismiss operation extension, where the client is allowed to
	 * 							   actively free server-side resources
	 * 						
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Job ID</em>' attribute.
	 * @see #setJobID(String)
	 * @see net.opengis.wps20.Wps20Package#getResultType_JobID()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='JobID' namespace='##targetNamespace'"
	 * @generated
	 */
	String getJobID();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.ResultType#getJobID <em>Job ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Job ID</em>' attribute.
	 * @see #getJobID()
	 * @generated
	 */
	void setJobID(String value);

	/**
	 * Returns the value of the '<em><b>Expiration Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							Identifier of the Process that was executed.
	 * 							This Process identifier shall be as listed in the ProcessOfferings
	 * 							section of the WPS Capabilities document. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Expiration Date</em>' attribute.
	 * @see #setExpirationDate(XMLGregorianCalendar)
	 * @see net.opengis.wps20.Wps20Package#getResultType_ExpirationDate()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.DateTime"
	 *        extendedMetaData="kind='element' name='ExpirationDate' namespace='##targetNamespace'"
	 * @generated
	 */
	XMLGregorianCalendar getExpirationDate();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.ResultType#getExpirationDate <em>Expiration Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expiration Date</em>' attribute.
	 * @see #getExpirationDate()
	 * @generated
	 */
	void setExpirationDate(XMLGregorianCalendar value);

	/**
	 * Returns the value of the '<em><b>Output</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wps20.DataOutputType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output</em>' containment reference list.
	 * @see net.opengis.wps20.Wps20Package#getResultType_Output()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='Output' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<DataOutputType> getOutput();

} // ResultType
