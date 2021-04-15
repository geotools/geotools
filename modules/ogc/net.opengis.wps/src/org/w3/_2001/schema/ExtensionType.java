/**
 */
package org.w3._2001.schema;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extension Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.ExtensionType#getGroup <em>Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.ExtensionType#getAll <em>All</em>}</li>
 *   <li>{@link org.w3._2001.schema.ExtensionType#getChoice <em>Choice</em>}</li>
 *   <li>{@link org.w3._2001.schema.ExtensionType#getSequence <em>Sequence</em>}</li>
 *   <li>{@link org.w3._2001.schema.ExtensionType#getGroup1 <em>Group1</em>}</li>
 *   <li>{@link org.w3._2001.schema.ExtensionType#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.w3._2001.schema.ExtensionType#getAttributeGroup <em>Attribute Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.ExtensionType#getAnyAttribute1 <em>Any Attribute1</em>}</li>
 *   <li>{@link org.w3._2001.schema.ExtensionType#getBase <em>Base</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getExtensionType()
 * @model extendedMetaData="name='extensionType' kind='elementOnly'"
 * @generated
 */
public interface ExtensionType extends Annotated {
	/**
	 * Returns the value of the '<em><b>Group</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' containment reference.
	 * @see #setGroup(GroupRef)
	 * @see org.w3._2001.schema.SchemaPackage#getExtensionType_Group()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='group' namespace='##targetNamespace'"
	 * @generated
	 */
	GroupRef getGroup();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ExtensionType#getGroup <em>Group</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Group</em>' containment reference.
	 * @see #getGroup()
	 * @generated
	 */
	void setGroup(GroupRef value);

	/**
	 * Returns the value of the '<em><b>All</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>All</em>' containment reference.
	 * @see #setAll(All)
	 * @see org.w3._2001.schema.SchemaPackage#getExtensionType_All()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='all' namespace='##targetNamespace'"
	 * @generated
	 */
	All getAll();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ExtensionType#getAll <em>All</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>All</em>' containment reference.
	 * @see #getAll()
	 * @generated
	 */
	void setAll(All value);

	/**
	 * Returns the value of the '<em><b>Choice</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Choice</em>' containment reference.
	 * @see #setChoice(ExplicitGroup)
	 * @see org.w3._2001.schema.SchemaPackage#getExtensionType_Choice()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='choice' namespace='##targetNamespace'"
	 * @generated
	 */
	ExplicitGroup getChoice();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ExtensionType#getChoice <em>Choice</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Choice</em>' containment reference.
	 * @see #getChoice()
	 * @generated
	 */
	void setChoice(ExplicitGroup value);

	/**
	 * Returns the value of the '<em><b>Sequence</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Sequence</em>' containment reference.
	 * @see #setSequence(ExplicitGroup)
	 * @see org.w3._2001.schema.SchemaPackage#getExtensionType_Sequence()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='sequence' namespace='##targetNamespace'"
	 * @generated
	 */
	ExplicitGroup getSequence();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ExtensionType#getSequence <em>Sequence</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sequence</em>' containment reference.
	 * @see #getSequence()
	 * @generated
	 */
	void setSequence(ExplicitGroup value);

	/**
	 * Returns the value of the '<em><b>Group1</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group1</em>' attribute list.
	 * @see org.w3._2001.schema.SchemaPackage#getExtensionType_Group1()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:7'"
	 * @generated
	 */
	FeatureMap getGroup1();

	/**
	 * Returns the value of the '<em><b>Attribute</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.Attribute}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getExtensionType_Attribute()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='attribute' namespace='##targetNamespace' group='#group:7'"
	 * @generated
	 */
	EList<Attribute> getAttribute();

	/**
	 * Returns the value of the '<em><b>Attribute Group</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.AttributeGroupRef}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute Group</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getExtensionType_AttributeGroup()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='attributeGroup' namespace='##targetNamespace' group='#group:7'"
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
	 * @see org.w3._2001.schema.SchemaPackage#getExtensionType_AnyAttribute1()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='anyAttribute' namespace='##targetNamespace'"
	 * @generated
	 */
	Wildcard getAnyAttribute1();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ExtensionType#getAnyAttribute1 <em>Any Attribute1</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Any Attribute1</em>' containment reference.
	 * @see #getAnyAttribute1()
	 * @generated
	 */
	void setAnyAttribute1(Wildcard value);

	/**
	 * Returns the value of the '<em><b>Base</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base</em>' attribute.
	 * @see #setBase(QName)
	 * @see org.w3._2001.schema.SchemaPackage#getExtensionType_Base()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.QName" required="true"
	 *        extendedMetaData="kind='attribute' name='base'"
	 * @generated
	 */
	QName getBase();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ExtensionType#getBase <em>Base</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base</em>' attribute.
	 * @see #getBase()
	 * @generated
	 */
	void setBase(QName value);

} // ExtensionType
