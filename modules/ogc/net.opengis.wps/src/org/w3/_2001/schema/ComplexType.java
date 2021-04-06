/**
 */
package org.w3._2001.schema;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Complex Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.ComplexType#getSimpleContent <em>Simple Content</em>}</li>
 *   <li>{@link org.w3._2001.schema.ComplexType#getComplexContent <em>Complex Content</em>}</li>
 *   <li>{@link org.w3._2001.schema.ComplexType#getGroup <em>Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.ComplexType#getAll <em>All</em>}</li>
 *   <li>{@link org.w3._2001.schema.ComplexType#getChoice <em>Choice</em>}</li>
 *   <li>{@link org.w3._2001.schema.ComplexType#getSequence <em>Sequence</em>}</li>
 *   <li>{@link org.w3._2001.schema.ComplexType#getGroup1 <em>Group1</em>}</li>
 *   <li>{@link org.w3._2001.schema.ComplexType#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.w3._2001.schema.ComplexType#getAttributeGroup <em>Attribute Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.ComplexType#getAnyAttribute1 <em>Any Attribute1</em>}</li>
 *   <li>{@link org.w3._2001.schema.ComplexType#isAbstract <em>Abstract</em>}</li>
 *   <li>{@link org.w3._2001.schema.ComplexType#getBlock <em>Block</em>}</li>
 *   <li>{@link org.w3._2001.schema.ComplexType#getFinal <em>Final</em>}</li>
 *   <li>{@link org.w3._2001.schema.ComplexType#isMixed <em>Mixed</em>}</li>
 *   <li>{@link org.w3._2001.schema.ComplexType#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getComplexType()
 * @model abstract="true"
 *        extendedMetaData="name='complexType' kind='elementOnly'"
 * @generated
 */
public interface ComplexType extends Annotated {
	/**
	 * Returns the value of the '<em><b>Simple Content</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Simple Content</em>' containment reference.
	 * @see #setSimpleContent(SimpleContentType)
	 * @see org.w3._2001.schema.SchemaPackage#getComplexType_SimpleContent()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='simpleContent' namespace='##targetNamespace'"
	 * @generated
	 */
	SimpleContentType getSimpleContent();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ComplexType#getSimpleContent <em>Simple Content</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Simple Content</em>' containment reference.
	 * @see #getSimpleContent()
	 * @generated
	 */
	void setSimpleContent(SimpleContentType value);

	/**
	 * Returns the value of the '<em><b>Complex Content</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Complex Content</em>' containment reference.
	 * @see #setComplexContent(ComplexContentType)
	 * @see org.w3._2001.schema.SchemaPackage#getComplexType_ComplexContent()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='complexContent' namespace='##targetNamespace'"
	 * @generated
	 */
	ComplexContentType getComplexContent();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ComplexType#getComplexContent <em>Complex Content</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Complex Content</em>' containment reference.
	 * @see #getComplexContent()
	 * @generated
	 */
	void setComplexContent(ComplexContentType value);

	/**
	 * Returns the value of the '<em><b>Group</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' containment reference.
	 * @see #setGroup(GroupRef)
	 * @see org.w3._2001.schema.SchemaPackage#getComplexType_Group()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='group' namespace='##targetNamespace'"
	 * @generated
	 */
	GroupRef getGroup();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ComplexType#getGroup <em>Group</em>}' containment reference.
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
	 * @see org.w3._2001.schema.SchemaPackage#getComplexType_All()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='all' namespace='##targetNamespace'"
	 * @generated
	 */
	All getAll();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ComplexType#getAll <em>All</em>}' containment reference.
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
	 * @see org.w3._2001.schema.SchemaPackage#getComplexType_Choice()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='choice' namespace='##targetNamespace'"
	 * @generated
	 */
	ExplicitGroup getChoice();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ComplexType#getChoice <em>Choice</em>}' containment reference.
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
	 * @see org.w3._2001.schema.SchemaPackage#getComplexType_Sequence()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='sequence' namespace='##targetNamespace'"
	 * @generated
	 */
	ExplicitGroup getSequence();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ComplexType#getSequence <em>Sequence</em>}' containment reference.
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
	 * @see org.w3._2001.schema.SchemaPackage#getComplexType_Group1()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:9'"
	 * @generated
	 */
	FeatureMap getGroup1();

	/**
	 * Returns the value of the '<em><b>Attribute</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.Attribute}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getComplexType_Attribute()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='attribute' namespace='##targetNamespace' group='#group:9'"
	 * @generated
	 */
	EList<Attribute> getAttribute();

	/**
	 * Returns the value of the '<em><b>Attribute Group</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.AttributeGroupRef}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute Group</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getComplexType_AttributeGroup()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='attributeGroup' namespace='##targetNamespace' group='#group:9'"
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
	 * @see org.w3._2001.schema.SchemaPackage#getComplexType_AnyAttribute1()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='anyAttribute' namespace='##targetNamespace'"
	 * @generated
	 */
	Wildcard getAnyAttribute1();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ComplexType#getAnyAttribute1 <em>Any Attribute1</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Any Attribute1</em>' containment reference.
	 * @see #getAnyAttribute1()
	 * @generated
	 */
	void setAnyAttribute1(Wildcard value);

	/**
	 * Returns the value of the '<em><b>Abstract</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Abstract</em>' attribute.
	 * @see #isSetAbstract()
	 * @see #unsetAbstract()
	 * @see #setAbstract(boolean)
	 * @see org.w3._2001.schema.SchemaPackage#getComplexType_Abstract()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='abstract'"
	 * @generated
	 */
	boolean isAbstract();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ComplexType#isAbstract <em>Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Abstract</em>' attribute.
	 * @see #isSetAbstract()
	 * @see #unsetAbstract()
	 * @see #isAbstract()
	 * @generated
	 */
	void setAbstract(boolean value);

	/**
	 * Unsets the value of the '{@link org.w3._2001.schema.ComplexType#isAbstract <em>Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAbstract()
	 * @see #isAbstract()
	 * @see #setAbstract(boolean)
	 * @generated
	 */
	void unsetAbstract();

	/**
	 * Returns whether the value of the '{@link org.w3._2001.schema.ComplexType#isAbstract <em>Abstract</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Abstract</em>' attribute is set.
	 * @see #unsetAbstract()
	 * @see #isAbstract()
	 * @see #setAbstract(boolean)
	 * @generated
	 */
	boolean isSetAbstract();

	/**
	 * Returns the value of the '<em><b>Block</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Block</em>' attribute.
	 * @see #setBlock(Object)
	 * @see org.w3._2001.schema.SchemaPackage#getComplexType_Block()
	 * @model dataType="org.w3._2001.schema.DerivationSet"
	 *        extendedMetaData="kind='attribute' name='block'"
	 * @generated
	 */
	Object getBlock();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ComplexType#getBlock <em>Block</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Block</em>' attribute.
	 * @see #getBlock()
	 * @generated
	 */
	void setBlock(Object value);

	/**
	 * Returns the value of the '<em><b>Final</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Final</em>' attribute.
	 * @see #setFinal(Object)
	 * @see org.w3._2001.schema.SchemaPackage#getComplexType_Final()
	 * @model dataType="org.w3._2001.schema.DerivationSet"
	 *        extendedMetaData="kind='attribute' name='final'"
	 * @generated
	 */
	Object getFinal();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ComplexType#getFinal <em>Final</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Final</em>' attribute.
	 * @see #getFinal()
	 * @generated
	 */
	void setFinal(Object value);

	/**
	 * Returns the value of the '<em><b>Mixed</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *       Not allowed if simpleContent child is chosen.
	 *       May be overriden by setting on complexContent child.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Mixed</em>' attribute.
	 * @see #isSetMixed()
	 * @see #unsetMixed()
	 * @see #setMixed(boolean)
	 * @see org.w3._2001.schema.SchemaPackage#getComplexType_Mixed()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='mixed'"
	 * @generated
	 */
	boolean isMixed();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ComplexType#isMixed <em>Mixed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mixed</em>' attribute.
	 * @see #isSetMixed()
	 * @see #unsetMixed()
	 * @see #isMixed()
	 * @generated
	 */
	void setMixed(boolean value);

	/**
	 * Unsets the value of the '{@link org.w3._2001.schema.ComplexType#isMixed <em>Mixed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMixed()
	 * @see #isMixed()
	 * @see #setMixed(boolean)
	 * @generated
	 */
	void unsetMixed();

	/**
	 * Returns whether the value of the '{@link org.w3._2001.schema.ComplexType#isMixed <em>Mixed</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Mixed</em>' attribute is set.
	 * @see #unsetMixed()
	 * @see #isMixed()
	 * @see #setMixed(boolean)
	 * @generated
	 */
	boolean isSetMixed();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *       Will be restricted to required or forbidden
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.w3._2001.schema.SchemaPackage#getComplexType_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.NCName"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ComplexType#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // ComplexType
