/**
 */
package net.opengis.ows20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows20.OperationType#getDCP <em>DCP</em>}</li>
 *   <li>{@link net.opengis.ows20.OperationType#getParameter <em>Parameter</em>}</li>
 *   <li>{@link net.opengis.ows20.OperationType#getConstraint <em>Constraint</em>}</li>
 *   <li>{@link net.opengis.ows20.OperationType#getMetadataGroup <em>Metadata Group</em>}</li>
 *   <li>{@link net.opengis.ows20.OperationType#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.ows20.OperationType#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows20.Ows20Package#getOperationType()
 * @model extendedMetaData="name='Operation_._type' kind='elementOnly'"
 * @generated
 */
public interface OperationType extends EObject {
    /**
     * Returns the value of the '<em><b>DCP</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.DCPType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of Distributed Computing Platforms
     *             (DCPs) supported for this operation. At present, only the HTTP DCP
     *             is defined, so this element will appear only once.
     * <!-- end-model-doc -->
     * @return the value of the '<em>DCP</em>' containment reference list.
     * @see net.opengis.ows20.Ows20Package#getOperationType_DCP()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='DCP' namespace='##targetNamespace'"
     * @generated
     */
    EList<DCPType> getDCP();

    /**
     * Returns the value of the '<em><b>Parameter</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.DomainType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of parameter domains that
     *             each apply to this operation which this server implements. If one
     *             of these Parameter elements has the same "name" attribute as a
     *             Parameter element in the OperationsMetadata element, this
     *             Parameter element shall override the other one for this operation.
     *             The list of required and optional parameter domain limitations for
     *             this operation shall be specified in the Implementation
     *             Specification for this service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Parameter</em>' containment reference list.
     * @see net.opengis.ows20.Ows20Package#getOperationType_Parameter()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Parameter' namespace='##targetNamespace'"
     * @generated
     */
    EList<DomainType> getParameter();

    /**
     * Returns the value of the '<em><b>Constraint</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.DomainType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of valid domain constraints
     *             on non-parameter quantities that each apply to this operation. If
     *             one of these Constraint elements has the same "name" attribute as
     *             a Constraint element in the OperationsMetadata element, this
     *             Constraint element shall override the other one for this
     *             operation. The list of required and optional constraints for this
     *             operation shall be specified in the Implementation Specification
     *             for this service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Constraint</em>' containment reference list.
     * @see net.opengis.ows20.Ows20Package#getOperationType_Constraint()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Constraint' namespace='##targetNamespace'"
     * @generated
     */
    EList<DomainType> getConstraint();

    /**
     * Returns the value of the '<em><b>Metadata Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of additional metadata
     *             about this operation and its' implementation. A list of required
     *             and optional metadata elements for this operation should be
     *             specified in the Implementation Specification for this service.
     *             (Informative: This metadata might specify the operation request
     *             parameters or provide the XML Schemas for the operation
     *             request.)
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata Group</em>' attribute list.
     * @see net.opengis.ows20.Ows20Package#getOperationType_MetadataGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='Metadata:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getMetadataGroup();

    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.MetadataType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of additional metadata
     *             about this operation and its' implementation. A list of required
     *             and optional metadata elements for this operation should be
     *             specified in the Implementation Specification for this service.
     *             (Informative: This metadata might specify the operation request
     *             parameters or provide the XML Schemas for the operation
     *             request.)
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference list.
     * @see net.opengis.ows20.Ows20Package#getOperationType_Metadata()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='##targetNamespace' group='Metadata:group'"
     * @generated
     */
    EList<MetadataType> getMetadata();

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Name or identifier of this operation (request) (for
     *           example, GetCapabilities). The list of required and optional
     *           operations implemented shall be specified in the Implementation
     *           Specification for this service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see net.opengis.ows20.Ows20Package#getOperationType_Name()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='name'"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link net.opengis.ows20.OperationType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

} // OperationType
