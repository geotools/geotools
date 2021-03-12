/**
 */
package org.w3._2001.schema;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Redefine Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.RedefineType#getGroup <em>Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.RedefineType#getAnnotation <em>Annotation</em>}</li>
 *   <li>{@link org.w3._2001.schema.RedefineType#getSimpleType <em>Simple Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.RedefineType#getComplexType <em>Complex Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.RedefineType#getGroup1 <em>Group1</em>}</li>
 *   <li>{@link org.w3._2001.schema.RedefineType#getAttributeGroup <em>Attribute Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.RedefineType#getId <em>Id</em>}</li>
 *   <li>{@link org.w3._2001.schema.RedefineType#getSchemaLocation <em>Schema Location</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getRedefineType()
 * @model extendedMetaData="name='redefine_._type' kind='elementOnly'"
 * @generated
 */
public interface RedefineType extends OpenAttrs {
	/**
	 * Returns the value of the '<em><b>Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' attribute list.
	 * @see org.w3._2001.schema.SchemaPackage#getRedefineType_Group()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:1'"
	 * @generated
	 */
	FeatureMap getGroup();

	/**
	 * Returns the value of the '<em><b>Annotation</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.AnnotationType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Annotation</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRedefineType_Annotation()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='annotation' namespace='##targetNamespace' group='#group:1'"
	 * @generated
	 */
	EList<AnnotationType> getAnnotation();

	/**
	 * Returns the value of the '<em><b>Simple Type</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.TopLevelSimpleType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Simple Type</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRedefineType_SimpleType()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='simpleType' namespace='##targetNamespace' group='#group:1'"
	 * @generated
	 */
	EList<TopLevelSimpleType> getSimpleType();

	/**
	 * Returns the value of the '<em><b>Complex Type</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.TopLevelComplexType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Complex Type</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRedefineType_ComplexType()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='complexType' namespace='##targetNamespace' group='#group:1'"
	 * @generated
	 */
	EList<TopLevelComplexType> getComplexType();

	/**
	 * Returns the value of the '<em><b>Group1</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.NamedGroup}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Group1</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRedefineType_Group1()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='group' namespace='##targetNamespace' group='#group:1'"
	 * @generated
	 */
	EList<NamedGroup> getGroup1();

	/**
	 * Returns the value of the '<em><b>Attribute Group</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.NamedAttributeGroup}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Attribute Group</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getRedefineType_AttributeGroup()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='attributeGroup' namespace='##targetNamespace' group='#group:1'"
	 * @generated
	 */
	EList<NamedAttributeGroup> getAttributeGroup();

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.w3._2001.schema.SchemaPackage#getRedefineType_Id()
	 * @model id="true" dataType="org.eclipse.emf.ecore.xml.type.ID"
	 *        extendedMetaData="kind='attribute' name='id'"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.RedefineType#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Schema Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Schema Location</em>' attribute.
	 * @see #setSchemaLocation(String)
	 * @see org.w3._2001.schema.SchemaPackage#getRedefineType_SchemaLocation()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
	 *        extendedMetaData="kind='attribute' name='schemaLocation'"
	 * @generated
	 */
	String getSchemaLocation();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.RedefineType#getSchemaLocation <em>Schema Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schema Location</em>' attribute.
	 * @see #getSchemaLocation()
	 * @generated
	 */
	void setSchemaLocation(String value);

} // RedefineType
