/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Domain Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Valid domain (or set of values) of one parameter or other quantity used by this server. A non-parameter quantity may not be explicitly represented in the server software. (Informative: An example is the outputFormat parameter of a WFS. Each WFS server should provide a Parameter element for the outputFormat parameter that lists the supported output formats, such as GML2, GML3, etc. as the allowed "Value" elements.)
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows10.DomainType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.ows10.DomainType#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.ows10.DomainType#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows10.Ows10Package#getDomainType()
 * @model extendedMetaData="name='DomainType' kind='elementOnly'"
 * @generated
 */
public interface DomainType extends EObject {
	/**
     * Returns the value of the '<em><b>Value</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of all the valid values for this parameter or other quantity. For those parameters that contain a list or sequence of values, these values shall be for individual values in the list. The allowed set of values and the allowed server restrictions on that set of values shall be specified in the Implementation Specification for this service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value</em>' attribute list.
     * @see net.opengis.ows10.Ows10Package#getDomainType_Value()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='Value' namespace='##targetNamespace'"
     * @generated
     */
	EList getValue();

	/**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows10.MetadataType}.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of additional metadata about this parameter. A list of required and optional metadata elements for this domain should be specified in the Implementation Specification for this service. (Informative: This metadata might specify the meanings of the valid values.)
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference list.
     * @see net.opengis.ows10.Ows10Package#getDomainType_Metadata()
     * @model type="net.opengis.ows10.MetadataType" containment="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='##targetNamespace'"
     * @generated
     */
	EList getMetadata();

	/**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Name or identifier of this parameter or other quantity.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see net.opengis.ows10.Ows10Package#getDomainType_Name()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='name'"
     * @generated
     */
	String getName();

	/**
     * Sets the value of the '{@link net.opengis.ows10.DomainType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
	void setName(String value);

} // DomainType
