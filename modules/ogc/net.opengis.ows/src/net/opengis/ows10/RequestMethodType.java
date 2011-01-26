/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Request Method Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Connect point URL and any constraints for this HTTP request method for this operation request. In the OnlineResourceType, the xlink:href attribute in the xlink:simpleLink attribute group shall be used to contain this URL. The other attributes in the xlink:simpleLink attribute group should not be used.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows10.RequestMethodType#getConstraint <em>Constraint</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows10.Ows10Package#getRequestMethodType()
 * @model extendedMetaData="name='RequestMethodType' kind='elementOnly'"
 * @generated
 */
public interface RequestMethodType extends OnlineResourceType {
	/**
	 * Returns the value of the '<em><b>Constraint</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.ows10.DomainType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional unordered list of valid domain constraints on non-parameter quantities that each apply to this request method for this operation. If one of these Constraint elements has the same "name" attribute as a Constraint element in the OperationsMetadata or Operation element, this Constraint element shall override the other one for this operation. The list of required and optional constraints for this request method for this operation shall be specified in the Implementation Specification for this service.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Constraint</em>' containment reference list.
	 * @see net.opengis.ows10.Ows10Package#getRequestMethodType_Constraint()
	 * @model type="net.opengis.ows10.DomainType" containment="true"
	 *        extendedMetaData="kind='element' name='Constraint' namespace='##targetNamespace'"
	 * @generated
	 */
	EList getConstraint();

} // RequestMethodType
