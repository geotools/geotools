/**
 */
package net.opengis.wps20;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Summary Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 				The process summary consists of descriptive elements at the process level,
 * 				the process profiles and the service-specific properties.
 * 				The process summary is not specific about process inputs and outputs.
 * 			
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.ProcessSummaryType#getJobControlOptions <em>Job Control Options</em>}</li>
 *   <li>{@link net.opengis.wps20.ProcessSummaryType#getOutputTransmission <em>Output Transmission</em>}</li>
 *   <li>{@link net.opengis.wps20.ProcessSummaryType#getProcessModel <em>Process Model</em>}</li>
 *   <li>{@link net.opengis.wps20.ProcessSummaryType#getProcessVersion <em>Process Version</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getProcessSummaryType()
 * @model extendedMetaData="name='ProcessSummaryType' kind='elementOnly'"
 * @generated
 */
public interface ProcessSummaryType extends DescriptionType {
	/**
	 * Returns the value of the '<em><b>Job Control Options</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					Defines the valid execution modes for a particular process offering.
	 * 				
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Job Control Options</em>' attribute.
	 * @see #setJobControlOptions(List)
	 * @see net.opengis.wps20.Wps20Package#getProcessSummaryType_JobControlOptions()
	 * @model dataType="net.opengis.wps20.JobControlOptionsType1" required="true" many="false"
	 *        extendedMetaData="kind='attribute' name='jobControlOptions'"
	 * @generated
	 */
	List<Object> getJobControlOptions();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.ProcessSummaryType#getJobControlOptions <em>Job Control Options</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Job Control Options</em>' attribute.
	 * @see #getJobControlOptions()
	 * @generated
	 */
	void setJobControlOptions(List<Object> value);

	/**
	 * Returns the value of the '<em><b>Output Transmission</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					Indicates whether data outputs from this process can be stored by the WPS server as web-accessible resources.
	 * 				
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Output Transmission</em>' attribute.
	 * @see #setOutputTransmission(List)
	 * @see net.opengis.wps20.Wps20Package#getProcessSummaryType_OutputTransmission()
	 * @model dataType="net.opengis.wps20.OutputTransmissionType" many="false"
	 *        extendedMetaData="kind='attribute' name='outputTransmission'"
	 * @generated
	 */
	List<DataTransmissionModeType> getOutputTransmission();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.ProcessSummaryType#getOutputTransmission <em>Output Transmission</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Output Transmission</em>' attribute.
	 * @see #getOutputTransmission()
	 * @generated
	 */
	void setOutputTransmission(List<DataTransmissionModeType> value);

	/**
	 * Returns the value of the '<em><b>Process Model</b></em>' attribute.
	 * The default value is <code>"native"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					Type of the process model. Include when using a different process model than the native process model. This is an
	 * 					extension hook to support processes that have been specified in other OGC Standards, such as SensorML. For those
	 * 					process models, compliance with the abstract process model has to be ensured compatibility with the WPS protocol.
	 * 				
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Process Model</em>' attribute.
	 * @see #isSetProcessModel()
	 * @see #unsetProcessModel()
	 * @see #setProcessModel(String)
	 * @see net.opengis.wps20.Wps20Package#getProcessSummaryType_ProcessModel()
	 * @model default="native" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='processModel'"
	 * @generated
	 */
	String getProcessModel();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.ProcessSummaryType#getProcessModel <em>Process Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Process Model</em>' attribute.
	 * @see #isSetProcessModel()
	 * @see #unsetProcessModel()
	 * @see #getProcessModel()
	 * @generated
	 */
	void setProcessModel(String value);

	/**
	 * Unsets the value of the '{@link net.opengis.wps20.ProcessSummaryType#getProcessModel <em>Process Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetProcessModel()
	 * @see #getProcessModel()
	 * @see #setProcessModel(String)
	 * @generated
	 */
	void unsetProcessModel();

	/**
	 * Returns whether the value of the '{@link net.opengis.wps20.ProcessSummaryType#getProcessModel <em>Process Model</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Process Model</em>' attribute is set.
	 * @see #unsetProcessModel()
	 * @see #getProcessModel()
	 * @see #setProcessModel(String)
	 * @generated
	 */
	boolean isSetProcessModel();

	/**
	 * Returns the value of the '<em><b>Process Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The process version is an informative element in a process offering. It is not intended for version negotiation
	 * 					but can rather be used to communicate updated or changed process implementations on a particular service instance. 
	 * 				
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Process Version</em>' attribute.
	 * @see #setProcessVersion(String)
	 * @see net.opengis.wps20.Wps20Package#getProcessSummaryType_ProcessVersion()
	 * @model dataType="net.opengis.ows20.VersionType1"
	 *        extendedMetaData="kind='attribute' name='processVersion'"
	 * @generated
	 */
	String getProcessVersion();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.ProcessSummaryType#getProcessVersion <em>Process Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Process Version</em>' attribute.
	 * @see #getProcessVersion()
	 * @generated
	 */
	void setProcessVersion(String value);

} // ProcessSummaryType
