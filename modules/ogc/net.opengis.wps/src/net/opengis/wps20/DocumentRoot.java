/**
 */
package net.opengis.wps20;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getBoundingBoxData <em>Bounding Box Data</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getDataDescription <em>Data Description</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getCapabilities <em>Capabilities</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getComplexData <em>Complex Data</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getContents <em>Contents</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getData <em>Data</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getDescribeProcess <em>Describe Process</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getDismiss <em>Dismiss</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getExecute <em>Execute</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getExpirationDate <em>Expiration Date</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getGenericProcess <em>Generic Process</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getGetResult <em>Get Result</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getGetStatus <em>Get Status</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getJobID <em>Job ID</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getLiteralData <em>Literal Data</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getLiteralValue <em>Literal Value</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getProcess <em>Process</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getProcessOffering <em>Process Offering</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getProcessOfferings <em>Process Offerings</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getReference <em>Reference</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getResult <em>Result</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getStatusInfo <em>Status Info</em>}</li>
 *   <li>{@link net.opengis.wps20.DocumentRoot#getSupportedCRS <em>Supported CRS</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mixed</em>' attribute list.
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_Mixed()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='elementWildcard' name=':mixed'"
	 * @generated
	 */
	FeatureMap getMixed();

	/**
	 * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>XMLNS Prefix Map</em>' map.
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_XMLNSPrefixMap()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry&lt;org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString&gt;" transient="true"
	 *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
	 * @generated
	 */
	EMap<String, String> getXMLNSPrefixMap();

	/**
	 * Returns the value of the '<em><b>XSI Schema Location</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>XSI Schema Location</em>' map.
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_XSISchemaLocation()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry&lt;org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString&gt;" transient="true"
	 *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
	 * @generated
	 */
	EMap<String, String> getXSISchemaLocation();

	/**
	 * Returns the value of the '<em><b>Bounding Box Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				Indicates that this Input shall be a BoundingBox data
	 * 				structure that is embedded in the execute request, and provides a
	 * 				list of the Coordinate Reference System support for this Bounding
	 * 				Box.
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bounding Box Data</em>' containment reference.
	 * @see #setBoundingBoxData(BoundingBoxDataType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_BoundingBoxData()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='BoundingBoxData' namespace='##targetNamespace' affiliation='DataDescription'"
	 * @generated
	 */
	BoundingBoxDataType getBoundingBoxData();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getBoundingBoxData <em>Bounding Box Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bounding Box Data</em>' containment reference.
	 * @see #getBoundingBoxData()
	 * @generated
	 */
	void setBoundingBoxData(BoundingBoxDataType value);

	/**
	 * Returns the value of the '<em><b>Data Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Description</em>' containment reference.
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_DataDescription()
	 * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='DataDescription' namespace='##targetNamespace'"
	 * @generated
	 */
	DataDescriptionType getDataDescription();

	/**
	 * Returns the value of the '<em><b>Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * WPS GetCapabilities operation response. This document provides clients with service metadata about a specific service instance, including metadata about the processes that can be executed. Since the server does not implement the updateSequence and Sections parameters, the server shall always return the complete Capabilities document, without the updateSequence parameter. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Capabilities</em>' containment reference.
	 * @see #setCapabilities(WPSCapabilitiesType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_Capabilities()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Capabilities' namespace='##targetNamespace'"
	 * @generated
	 */
	WPSCapabilitiesType getCapabilities();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getCapabilities <em>Capabilities</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Capabilities</em>' containment reference.
	 * @see #getCapabilities()
	 * @generated
	 */
	void setCapabilities(WPSCapabilitiesType value);

	/**
	 * Returns the value of the '<em><b>Complex Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				Indicates that this input/output shall be a complex data structure
	 * 				(such as a GML document or a GeoTiff image that comply with a particular format definition).
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Complex Data</em>' containment reference.
	 * @see #setComplexData(ComplexDataType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_ComplexData()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='ComplexData' namespace='##targetNamespace' affiliation='DataDescription'"
	 * @generated
	 */
	ComplexDataType getComplexData();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getComplexData <em>Complex Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Complex Data</em>' containment reference.
	 * @see #getComplexData()
	 * @generated
	 */
	void setComplexData(ComplexDataType value);

	/**
	 * Returns the value of the '<em><b>Contents</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * List of brief descriptions of the processes offered by this WPS server. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Contents</em>' containment reference.
	 * @see #setContents(ContentsType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_Contents()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Contents' namespace='##targetNamespace'"
	 * @generated
	 */
	ContentsType getContents();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getContents <em>Contents</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contents</em>' containment reference.
	 * @see #getContents()
	 * @generated
	 */
	void setContents(ContentsType value);

	/**
	 * Returns the value of the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data</em>' containment reference.
	 * @see #setData(DataType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_Data()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Data' namespace='##targetNamespace'"
	 * @generated
	 */
	DataType getData();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getData <em>Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data</em>' containment reference.
	 * @see #getData()
	 * @generated
	 */
	void setData(DataType value);

	/**
	 * Returns the value of the '<em><b>Describe Process</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * WPS DescribeProcess operation request. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Describe Process</em>' containment reference.
	 * @see #setDescribeProcess(DescribeProcessType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_DescribeProcess()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='DescribeProcess' namespace='##targetNamespace'"
	 * @generated
	 */
	DescribeProcessType getDescribeProcess();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getDescribeProcess <em>Describe Process</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Describe Process</em>' containment reference.
	 * @see #getDescribeProcess()
	 * @generated
	 */
	void setDescribeProcess(DescribeProcessType value);

	/**
	 * Returns the value of the '<em><b>Dismiss</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				WPS GetStatus operation request. This operation is used to query status information of executed processes.
	 * 				The response to a GetStatus operation is a StatusInfo document or an exception.
	 * 				Depending on the implementation, a WPS may "forget" old process executions sooner or later.
	 * 				In this case, there is no status information available and an exception shall be returned instead of a StatusInfo response. 
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Dismiss</em>' containment reference.
	 * @see #setDismiss(DismissType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_Dismiss()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Dismiss' namespace='##targetNamespace'"
	 * @generated
	 */
	DismissType getDismiss();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getDismiss <em>Dismiss</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dismiss</em>' containment reference.
	 * @see #getDismiss()
	 * @generated
	 */
	void setDismiss(DismissType value);

	/**
	 * Returns the value of the '<em><b>Execute</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Execute</em>' containment reference.
	 * @see #setExecute(ExecuteRequestType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_Execute()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Execute' namespace='##targetNamespace'"
	 * @generated
	 */
	ExecuteRequestType getExecute();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getExecute <em>Execute</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Execute</em>' containment reference.
	 * @see #getExecute()
	 * @generated
	 */
	void setExecute(ExecuteRequestType value);

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
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_ExpirationDate()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.DateTime" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='ExpirationDate' namespace='##targetNamespace'"
	 * @generated
	 */
	XMLGregorianCalendar getExpirationDate();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getExpirationDate <em>Expiration Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expiration Date</em>' attribute.
	 * @see #getExpirationDate()
	 * @generated
	 */
	void setExpirationDate(XMLGregorianCalendar value);

	/**
	 * Returns the value of the '<em><b>Format</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Format</em>' containment reference.
	 * @see #setFormat(FormatType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_Format()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Format' namespace='##targetNamespace'"
	 * @generated
	 */
	FormatType getFormat();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getFormat <em>Format</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Format</em>' containment reference.
	 * @see #getFormat()
	 * @generated
	 */
	void setFormat(FormatType value);

	/**
	 * Returns the value of the '<em><b>Generic Process</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generic Process</em>' containment reference.
	 * @see #setGenericProcess(GenericProcessType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_GenericProcess()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='GenericProcess' namespace='##targetNamespace'"
	 * @generated
	 */
	GenericProcessType getGenericProcess();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getGenericProcess <em>Generic Process</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generic Process</em>' containment reference.
	 * @see #getGenericProcess()
	 * @generated
	 */
	void setGenericProcess(GenericProcessType value);

	/**
	 * Returns the value of the '<em><b>Get Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Request to a WPS server to perform the GetCapabilities operation. This operation allows a client to retrieve a Capabilities XML document providing metadata for the specific WPS server. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Get Capabilities</em>' containment reference.
	 * @see #setGetCapabilities(GetCapabilitiesType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_GetCapabilities()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='GetCapabilities' namespace='##targetNamespace'"
	 * @generated
	 */
	GetCapabilitiesType getGetCapabilities();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Get Capabilities</em>' containment reference.
	 * @see #getGetCapabilities()
	 * @generated
	 */
	void setGetCapabilities(GetCapabilitiesType value);

	/**
	 * Returns the value of the '<em><b>Get Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				WPS GetResult operation request. This operation is used to query the results of asynchrously
	 * 				executed processes. The response to a GetResult operation is a wps:ProcessingResult, a raw data response, or an exception.
	 * 				Depending on the implementation, a WPS may "forget" old process executions sooner or later.
	 * 				In this case, there is no result information available and an exception shall be returned. 
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Get Result</em>' containment reference.
	 * @see #setGetResult(GetResultType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_GetResult()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='GetResult' namespace='##targetNamespace'"
	 * @generated
	 */
	GetResultType getGetResult();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getGetResult <em>Get Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Get Result</em>' containment reference.
	 * @see #getGetResult()
	 * @generated
	 */
	void setGetResult(GetResultType value);

	/**
	 * Returns the value of the '<em><b>Get Status</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				WPS GetStatus operation request. This operation is used to query status information of executed processes.
	 * 				The response to a GetStatus operation is a StatusInfo document or an exception.
	 * 				Depending on the implementation, a WPS may "forget" old process executions sooner or later.
	 * 				In this case, there is no status information available and an exception shall be returned instead of a StatusInfo response. 
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Get Status</em>' containment reference.
	 * @see #setGetStatus(GetStatusType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_GetStatus()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='GetStatus' namespace='##targetNamespace'"
	 * @generated
	 */
	GetStatusType getGetStatus();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getGetStatus <em>Get Status</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Get Status</em>' containment reference.
	 * @see #getGetStatus()
	 * @generated
	 */
	void setGetStatus(GetStatusType value);

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
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_JobID()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='JobID' namespace='##targetNamespace'"
	 * @generated
	 */
	String getJobID();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getJobID <em>Job ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Job ID</em>' attribute.
	 * @see #getJobID()
	 * @generated
	 */
	void setJobID(String value);

	/**
	 * Returns the value of the '<em><b>Literal Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Literal Data</em>' containment reference.
	 * @see #setLiteralData(LiteralDataType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_LiteralData()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='LiteralData' namespace='##targetNamespace' affiliation='DataDescription'"
	 * @generated
	 */
	LiteralDataType getLiteralData();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getLiteralData <em>Literal Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Literal Data</em>' containment reference.
	 * @see #getLiteralData()
	 * @generated
	 */
	void setLiteralData(LiteralDataType value);

	/**
	 * Returns the value of the '<em><b>Literal Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Literal Value</em>' containment reference.
	 * @see #setLiteralValue(LiteralValueType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_LiteralValue()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='LiteralValue' namespace='##targetNamespace'"
	 * @generated
	 */
	LiteralValueType getLiteralValue();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getLiteralValue <em>Literal Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Literal Value</em>' containment reference.
	 * @see #getLiteralValue()
	 * @generated
	 */
	void setLiteralValue(LiteralValueType value);

	/**
	 * Returns the value of the '<em><b>Process</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				The description of a single process, including the input and output items.
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Process</em>' containment reference.
	 * @see #setProcess(ProcessDescriptionType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_Process()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Process' namespace='##targetNamespace'"
	 * @generated
	 */
	ProcessDescriptionType getProcess();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getProcess <em>Process</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Process</em>' containment reference.
	 * @see #getProcess()
	 * @generated
	 */
	void setProcess(ProcessDescriptionType value);

	/**
	 * Returns the value of the '<em><b>Process Offering</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				A process offering is a process description. It has additional attributes that provide additional
	 * 				information on how this process can be executed on a particular service instance (execution modes,
	 * 				data transmission modes, informative process version.)
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Process Offering</em>' containment reference.
	 * @see #setProcessOffering(ProcessOfferingType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_ProcessOffering()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='ProcessOffering' namespace='##targetNamespace'"
	 * @generated
	 */
	ProcessOfferingType getProcessOffering();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getProcessOffering <em>Process Offering</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Process Offering</em>' containment reference.
	 * @see #getProcessOffering()
	 * @generated
	 */
	void setProcessOffering(ProcessOfferingType value);

	/**
	 * Returns the value of the '<em><b>Process Offerings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				List structure that is returned by the WPS DescribeProcess operation.
	 * 				Contains XML descriptions for the queried process identifiers.
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Process Offerings</em>' containment reference.
	 * @see #setProcessOfferings(ProcessOfferingsType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_ProcessOfferings()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='ProcessOfferings' namespace='##targetNamespace'"
	 * @generated
	 */
	ProcessOfferingsType getProcessOfferings();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getProcessOfferings <em>Process Offerings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Process Offerings</em>' containment reference.
	 * @see #getProcessOfferings()
	 * @generated
	 */
	void setProcessOfferings(ProcessOfferingsType value);

	/**
	 * Returns the value of the '<em><b>Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				This element is used for web accessible references to a data set or value.
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Reference</em>' containment reference.
	 * @see #setReference(ReferenceType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_Reference()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Reference' namespace='##targetNamespace'"
	 * @generated
	 */
	ReferenceType getReference();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getReference <em>Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference</em>' containment reference.
	 * @see #getReference()
	 * @generated
	 */
	void setReference(ReferenceType value);

	/**
	 * Returns the value of the '<em><b>Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				A Result document is a structure that contains the results of a process execution.
	 * 				It is a shared element between the Execute and GetResult operations.
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Result</em>' containment reference.
	 * @see #setResult(ResultType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_Result()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Result' namespace='##targetNamespace'"
	 * @generated
	 */
	ResultType getResult();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getResult <em>Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Result</em>' containment reference.
	 * @see #getResult()
	 * @generated
	 */
	void setResult(ResultType value);

	/**
	 * Returns the value of the '<em><b>Status Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				StatusInfo document containing information about executed processes.
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Status Info</em>' containment reference.
	 * @see #setStatusInfo(StatusInfoType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_StatusInfo()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='StatusInfo' namespace='##targetNamespace'"
	 * @generated
	 */
	StatusInfoType getStatusInfo();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getStatusInfo <em>Status Info</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Status Info</em>' containment reference.
	 * @see #getStatusInfo()
	 * @generated
	 */
	void setStatusInfo(StatusInfoType value);

	/**
	 * Returns the value of the '<em><b>Supported CRS</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				Supported CRS supported for this Input/Output. "default" shall be used
	 * 				on only one element. This default element identifies the default CRS.
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Supported CRS</em>' containment reference.
	 * @see #setSupportedCRS(SupportedCRSType)
	 * @see net.opengis.wps20.Wps20Package#getDocumentRoot_SupportedCRS()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='SupportedCRS' namespace='##targetNamespace'"
	 * @generated
	 */
	SupportedCRSType getSupportedCRS();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DocumentRoot#getSupportedCRS <em>Supported CRS</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Supported CRS</em>' containment reference.
	 * @see #getSupportedCRS()
	 * @generated
	 */
	void setSupportedCRS(SupportedCRSType value);

} // DocumentRoot
