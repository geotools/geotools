/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10;

import java.lang.Object;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operations Metadata Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows10.OperationsMetadataType#getOperation <em>Operation</em>}</li>
 *   <li>{@link net.opengis.ows10.OperationsMetadataType#getParameter <em>Parameter</em>}</li>
 *   <li>{@link net.opengis.ows10.OperationsMetadataType#getConstraint <em>Constraint</em>}</li>
 *   <li>{@link net.opengis.ows10.OperationsMetadataType#getExtendedCapabilities <em>Extended Capabilities</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows10.Ows10Package#getOperationsMetadataType()
 * @model extendedMetaData="name='OperationsMetadata_._type' kind='elementOnly'"
 * @generated
 */
public interface OperationsMetadataType extends EObject {
	/**
     * Returns the value of the '<em><b>Operation</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows10.OperationType}.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Metadata for unordered list of all the (requests for) operations that this server interface implements. The list of required and optional operations implemented shall be specified in the Implementation Specification for this service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Operation</em>' containment reference list.
     * @see net.opengis.ows10.Ows10Package#getOperationsMetadataType_Operation()
     * @model type="net.opengis.ows10.OperationType" containment="true" lower="2"
     *        extendedMetaData="kind='element' name='Operation' namespace='##targetNamespace'"
     * @generated
     */
	EList getOperation();

	/**
     * Returns the value of the '<em><b>Parameter</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows10.DomainType}.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of parameter valid domains that each apply to one or more operations which this server interface implements. The list of required and optional parameter domain limitations shall be specified in the Implementation Specification for this service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Parameter</em>' containment reference list.
     * @see net.opengis.ows10.Ows10Package#getOperationsMetadataType_Parameter()
     * @model type="net.opengis.ows10.DomainType" containment="true"
     *        extendedMetaData="kind='element' name='Parameter' namespace='##targetNamespace'"
     * @generated
     */
	EList getParameter();

	/**
     * Returns the value of the '<em><b>Constraint</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows10.DomainType}.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of valid domain constraints on non-parameter quantities that each apply to this server. The list of required and optional constraints shall be specified in the Implementation Specification for this service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Constraint</em>' containment reference list.
     * @see net.opengis.ows10.Ows10Package#getOperationsMetadataType_Constraint()
     * @model type="net.opengis.ows10.DomainType" containment="true"
     *        extendedMetaData="kind='element' name='Constraint' namespace='##targetNamespace'"
     * @generated
     */
	EList getConstraint();

	/**
	 * Returns the value of the '<em><b>Extended Capabilities</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extended Capabilities</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extended Capabilities</em>' containment reference.
	 * @see #setExtendedCapabilities(EObject)
	 * @see net.opengis.ows10.Ows10Package#getOperationsMetadataType_ExtendedCapabilities()
	 * @model 
	 * @generated NOT
	 */
	Object getExtendedCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.ows10.OperationsMetadataType#getExtendedCapabilities <em>Extended Capabilities</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Extended Capabilities</em>' reference.
     * @see #getExtendedCapabilities()
     * @generated
     */
    void setExtendedCapabilities(Object value);

} // OperationsMetadataType
