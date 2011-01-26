/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Service Identification Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows10.ServiceIdentificationType#getServiceType <em>Service Type</em>}</li>
 *   <li>{@link net.opengis.ows10.ServiceIdentificationType#getServiceTypeVersion <em>Service Type Version</em>}</li>
 *   <li>{@link net.opengis.ows10.ServiceIdentificationType#getFees <em>Fees</em>}</li>
 *   <li>{@link net.opengis.ows10.ServiceIdentificationType#getAccessConstraints <em>Access Constraints</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows10.Ows10Package#getServiceIdentificationType()
 * @model extendedMetaData="name='ServiceIdentification_._type' kind='elementOnly'"
 * @generated
 */
public interface ServiceIdentificationType extends DescriptionType {
	/**
	 * Returns the value of the '<em><b>Service Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A service type name from a registry of services. For example, the values of the codeSpace URI and name and code string may be "OGC" and "catalogue." This type name is normally used for machine-to-machine communication.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Service Type</em>' containment reference.
	 * @see #setServiceType(CodeType)
	 * @see net.opengis.ows10.Ows10Package#getServiceIdentificationType_ServiceType()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='ServiceType' namespace='##targetNamespace'"
	 * @generated
	 */
	CodeType getServiceType();

	/**
	 * Sets the value of the '{@link net.opengis.ows10.ServiceIdentificationType#getServiceType <em>Service Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service Type</em>' containment reference.
	 * @see #getServiceType()
	 * @generated
	 */
	void setServiceType(CodeType value);

	/**
	 * Returns the value of the '<em><b>Service Type Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of one or more versions of this service type implemented by this server. This information is not adequate for version negotiation, and shall not be used for that purpose.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Service Type Version</em>' attribute.
	 * @see #setServiceTypeVersion(String)
	 * @see net.opengis.ows10.Ows10Package#getServiceIdentificationType_ServiceTypeVersion()
	 * @model unique="false" dataType="net.opengis.ows10.VersionType" required="true"
	 *        extendedMetaData="kind='element' name='ServiceTypeVersion' namespace='##targetNamespace'"
	 * @generated
	 */
	String getServiceTypeVersion();

	/**
	 * Sets the value of the '{@link net.opengis.ows10.ServiceIdentificationType#getServiceTypeVersion <em>Service Type Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service Type Version</em>' attribute.
	 * @see #getServiceTypeVersion()
	 * @generated
	 */
	void setServiceTypeVersion(String value);

	/**
	 * Returns the value of the '<em><b>Fees</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * If this element is omitted, no meaning is implied.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fees</em>' attribute.
	 * @see #setFees(String)
	 * @see net.opengis.ows10.Ows10Package#getServiceIdentificationType_Fees()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='Fees' namespace='##targetNamespace'"
	 * @generated
	 */
	String getFees();

	/**
	 * Sets the value of the '{@link net.opengis.ows10.ServiceIdentificationType#getFees <em>Fees</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fees</em>' attribute.
	 * @see #getFees()
	 * @generated
	 */
	void setFees(String value);

	/**
	 * Returns the value of the '<em><b>Access Constraints</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of access constraints applied to assure the protection of privacy or intellectual property, and any other restrictions on retrieving or using data from or otherwise using this server. The reserved value NONE (case insensitive) shall be used to mean no access constraints are imposed. If this element is omitted, no meaning is implied.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Access Constraints</em>' attribute.
	 * @see #setAccessConstraints(String)
	 * @see net.opengis.ows10.Ows10Package#getServiceIdentificationType_AccessConstraints()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='AccessConstraints' namespace='##targetNamespace'"
	 * @generated
	 */
	String getAccessConstraints();

	/**
	 * Sets the value of the '{@link net.opengis.ows10.ServiceIdentificationType#getAccessConstraints <em>Access Constraints</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Access Constraints</em>' attribute.
	 * @see #getAccessConstraints()
	 * @generated
	 */
	void setAccessConstraints(String value);

} // ServiceIdentificationType
