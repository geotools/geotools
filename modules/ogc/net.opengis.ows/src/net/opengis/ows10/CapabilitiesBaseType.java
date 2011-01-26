/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Capabilities Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * XML encoded GetCapabilities operation response. This document provides clients with service metadata about a specific service instance, usually including metadata about the tightly-coupled data served. If the server does not implement the updateSequence parameter, the server shall always return the complete Capabilities document, without the updateSequence parameter. When the server implements the updateSequence parameter and the GetCapabilities operation request included the updateSequence parameter with the current value, the server shall return this element with only the "version" and "updateSequence" attributes. Otherwise, all optional elements shall be included or not depending on the actual value of the Contents parameter in the GetCapabilities operation request. This base type shall be extended by each specific OWS to include the additional contents needed.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows10.CapabilitiesBaseType#getServiceIdentification <em>Service Identification</em>}</li>
 *   <li>{@link net.opengis.ows10.CapabilitiesBaseType#getServiceProvider <em>Service Provider</em>}</li>
 *   <li>{@link net.opengis.ows10.CapabilitiesBaseType#getOperationsMetadata <em>Operations Metadata</em>}</li>
 *   <li>{@link net.opengis.ows10.CapabilitiesBaseType#getUpdateSequence <em>Update Sequence</em>}</li>
 *   <li>{@link net.opengis.ows10.CapabilitiesBaseType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows10.Ows10Package#getCapabilitiesBaseType()
 * @model extendedMetaData="name='CapabilitiesBaseType' kind='elementOnly'"
 * @generated
 */
public interface CapabilitiesBaseType extends EObject {
	/**
	 * Returns the value of the '<em><b>Service Identification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Service Identification</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service Identification</em>' containment reference.
	 * @see #setServiceIdentification(ServiceIdentificationType)
	 * @see net.opengis.ows10.Ows10Package#getCapabilitiesBaseType_ServiceIdentification()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='ServiceIdentification' namespace='##targetNamespace'"
	 * @generated
	 */
	ServiceIdentificationType getServiceIdentification();

	/**
	 * Sets the value of the '{@link net.opengis.ows10.CapabilitiesBaseType#getServiceIdentification <em>Service Identification</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service Identification</em>' containment reference.
	 * @see #getServiceIdentification()
	 * @generated
	 */
	void setServiceIdentification(ServiceIdentificationType value);

	/**
	 * Returns the value of the '<em><b>Service Provider</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Service Provider</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service Provider</em>' containment reference.
	 * @see #setServiceProvider(ServiceProviderType)
	 * @see net.opengis.ows10.Ows10Package#getCapabilitiesBaseType_ServiceProvider()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='ServiceProvider' namespace='##targetNamespace'"
	 * @generated
	 */
	ServiceProviderType getServiceProvider();

	/**
	 * Sets the value of the '{@link net.opengis.ows10.CapabilitiesBaseType#getServiceProvider <em>Service Provider</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service Provider</em>' containment reference.
	 * @see #getServiceProvider()
	 * @generated
	 */
	void setServiceProvider(ServiceProviderType value);

	/**
	 * Returns the value of the '<em><b>Operations Metadata</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operations Metadata</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operations Metadata</em>' containment reference.
	 * @see #setOperationsMetadata(OperationsMetadataType)
	 * @see net.opengis.ows10.Ows10Package#getCapabilitiesBaseType_OperationsMetadata()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='OperationsMetadata' namespace='##targetNamespace'"
	 * @generated
	 */
	OperationsMetadataType getOperationsMetadata();

	/**
	 * Sets the value of the '{@link net.opengis.ows10.CapabilitiesBaseType#getOperationsMetadata <em>Operations Metadata</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operations Metadata</em>' containment reference.
	 * @see #getOperationsMetadata()
	 * @generated
	 */
	void setOperationsMetadata(OperationsMetadataType value);

	/**
	 * Returns the value of the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Update Sequence</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Update Sequence</em>' attribute.
	 * @see #setUpdateSequence(String)
	 * @see net.opengis.ows10.Ows10Package#getCapabilitiesBaseType_UpdateSequence()
	 * @model unique="false" dataType="net.opengis.ows10.UpdateSequenceType"
	 *        extendedMetaData="kind='attribute' name='updateSequence'"
	 * @generated
	 */
	String getUpdateSequence();

	/**
	 * Sets the value of the '{@link net.opengis.ows10.CapabilitiesBaseType#getUpdateSequence <em>Update Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Update Sequence</em>' attribute.
	 * @see #getUpdateSequence()
	 * @generated
	 */
	void setUpdateSequence(String value);

	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(String)
	 * @see net.opengis.ows10.Ows10Package#getCapabilitiesBaseType_Version()
	 * @model unique="false" dataType="net.opengis.ows10.VersionType" required="true"
	 *        extendedMetaData="kind='attribute' name='version'"
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link net.opengis.ows10.CapabilitiesBaseType#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(String value);

} // CapabilitiesBaseType
