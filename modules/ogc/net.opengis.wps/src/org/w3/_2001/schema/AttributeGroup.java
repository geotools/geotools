/**
 */
package org.w3._2001.schema;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Attribute Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.AttributeGroup#getGroup <em>Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.AttributeGroup#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.w3._2001.schema.AttributeGroup#getAttributeGroup <em>Attribute Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.AttributeGroup#getAnyAttribute1 <em>Any Attribute1</em>}</li>
 *   <li>{@link org.w3._2001.schema.AttributeGroup#getName <em>Name</em>}</li>
 *   <li>{@link org.w3._2001.schema.AttributeGroup#getRef <em>Ref</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getAttributeGroup()
 * @model abstract="true"
 *        extendedMetaData="name='attributeGroup' kind='elementOnly'"
 * @generated
 */
public interface AttributeGroup extends Annotated {
	/**
	 * Returns the value of the '<em><b>Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' attribute list.
	 * @see org.w3._2001.schema.SchemaPackage#getAttributeGroup_Group()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:3'"
	 * @generated
	 */
	FeatureMap getGroup();

	/**
	 * Returns the value of the '<em><b>Attribute</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.Attribute}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getAttributeGroup_Attribute()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='attribute' namespace='##targetNamespace' group='#group:3'"
	 * @generated
	 */
	EList<Attribute> getAttribute();

	/**
	 * Returns the value of the '<em><b>Attribute Group</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.AttributeGroupRef}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute Group</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getAttributeGroup_AttributeGroup()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='attributeGroup' namespace='##targetNamespace' group='#group:3'"
	 * @generated
	 */
	EList<AttributeGroupRef> getAttributeGroup();

	/**
	 * Returns the value of the '<em><b>Any Attribute1</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Any Attribute1</em>' containment reference.
	 * @see #setAnyAttribute1(Wildcard)
	 * @see org.w3._2001.schema.SchemaPackage#getAttributeGroup_AnyAttribute1()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='anyAttribute' namespace='##targetNamespace'"
	 * @generated
	 */
	Wildcard getAnyAttribute1();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.AttributeGroup#getAnyAttribute1 <em>Any Attribute1</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Any Attribute1</em>' containment reference.
	 * @see #getAnyAttribute1()
	 * @generated
	 */
	void setAnyAttribute1(Wildcard value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.w3._2001.schema.SchemaPackage#getAttributeGroup_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.NCName"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.AttributeGroup#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref</em>' attribute.
	 * @see #setRef(QName)
	 * @see org.w3._2001.schema.SchemaPackage#getAttributeGroup_Ref()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.QName"
	 *        extendedMetaData="kind='attribute' name='ref'"
	 * @generated
	 */
	QName getRef();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.AttributeGroup#getRef <em>Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref</em>' attribute.
	 * @see #getRef()
	 * @generated
	 */
	void setRef(QName value);

} // AttributeGroup
