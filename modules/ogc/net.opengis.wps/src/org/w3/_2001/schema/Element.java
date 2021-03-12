/**
 */
package org.w3._2001.schema;

import java.math.BigInteger;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *    The element element can be used either
 *    at the top level to define an element-type binding globally,
 *    or within a content model to either reference a globally-defined
 *    element or type or declare an element-type binding locally.
 *    The ref form is not allowed at the top level.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.Element#getSimpleType <em>Simple Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#getComplexType <em>Complex Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#getIdentityConstraint <em>Identity Constraint</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#getUnique <em>Unique</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#getKey <em>Key</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#getKeyref <em>Keyref</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#isAbstract <em>Abstract</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#getBlock <em>Block</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#getDefault <em>Default</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#getFinal <em>Final</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#getFixed <em>Fixed</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#getForm <em>Form</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#getMaxOccurs <em>Max Occurs</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#getMinOccurs <em>Min Occurs</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#getName <em>Name</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#isNillable <em>Nillable</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#getRef <em>Ref</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#getSubstitutionGroup <em>Substitution Group</em>}</li>
 *   <li>{@link org.w3._2001.schema.Element#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getElement()
 * @model abstract="true"
 *        extendedMetaData="name='element' kind='elementOnly'"
 * @generated
 */
public interface Element extends Annotated {
	/**
	 * Returns the value of the '<em><b>Simple Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Simple Type</em>' containment reference.
	 * @see #setSimpleType(LocalSimpleType)
	 * @see org.w3._2001.schema.SchemaPackage#getElement_SimpleType()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='simpleType' namespace='##targetNamespace'"
	 * @generated
	 */
	LocalSimpleType getSimpleType();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Element#getSimpleType <em>Simple Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Simple Type</em>' containment reference.
	 * @see #getSimpleType()
	 * @generated
	 */
	void setSimpleType(LocalSimpleType value);

	/**
	 * Returns the value of the '<em><b>Complex Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Complex Type</em>' containment reference.
	 * @see #setComplexType(LocalComplexType)
	 * @see org.w3._2001.schema.SchemaPackage#getElement_ComplexType()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='complexType' namespace='##targetNamespace'"
	 * @generated
	 */
	LocalComplexType getComplexType();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Element#getComplexType <em>Complex Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Complex Type</em>' containment reference.
	 * @see #getComplexType()
	 * @generated
	 */
	void setComplexType(LocalComplexType value);

	/**
	 * Returns the value of the '<em><b>Identity Constraint</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Identity Constraint</em>' attribute list.
	 * @see org.w3._2001.schema.SchemaPackage#getElement_IdentityConstraint()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='IdentityConstraint:5'"
	 * @generated
	 */
	FeatureMap getIdentityConstraint();

	/**
	 * Returns the value of the '<em><b>Unique</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.Keybase}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Unique</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getElement_Unique()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='unique' namespace='##targetNamespace' group='#IdentityConstraint:5'"
	 * @generated
	 */
	EList<Keybase> getUnique();

	/**
	 * Returns the value of the '<em><b>Key</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.Keybase}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Key</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getElement_Key()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='key' namespace='##targetNamespace' group='#IdentityConstraint:5'"
	 * @generated
	 */
	EList<Keybase> getKey();

	/**
	 * Returns the value of the '<em><b>Keyref</b></em>' containment reference list.
	 * The list contents are of type {@link org.w3._2001.schema.KeyrefType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Keyref</em>' containment reference list.
	 * @see org.w3._2001.schema.SchemaPackage#getElement_Keyref()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='keyref' namespace='##targetNamespace' group='#IdentityConstraint:5'"
	 * @generated
	 */
	EList<KeyrefType> getKeyref();

	/**
	 * Returns the value of the '<em><b>Abstract</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Abstract</em>' attribute.
	 * @see #isSetAbstract()
	 * @see #unsetAbstract()
	 * @see #setAbstract(boolean)
	 * @see org.w3._2001.schema.SchemaPackage#getElement_Abstract()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='abstract'"
	 * @generated
	 */
	boolean isAbstract();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Element#isAbstract <em>Abstract</em>}' attribute.
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
	 * Unsets the value of the '{@link org.w3._2001.schema.Element#isAbstract <em>Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAbstract()
	 * @see #isAbstract()
	 * @see #setAbstract(boolean)
	 * @generated
	 */
	void unsetAbstract();

	/**
	 * Returns whether the value of the '{@link org.w3._2001.schema.Element#isAbstract <em>Abstract</em>}' attribute is set.
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
	 * @see org.w3._2001.schema.SchemaPackage#getElement_Block()
	 * @model dataType="org.w3._2001.schema.BlockSet"
	 *        extendedMetaData="kind='attribute' name='block'"
	 * @generated
	 */
	Object getBlock();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Element#getBlock <em>Block</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Block</em>' attribute.
	 * @see #getBlock()
	 * @generated
	 */
	void setBlock(Object value);

	/**
	 * Returns the value of the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default</em>' attribute.
	 * @see #setDefault(String)
	 * @see org.w3._2001.schema.SchemaPackage#getElement_Default()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='default'"
	 * @generated
	 */
	String getDefault();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Element#getDefault <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default</em>' attribute.
	 * @see #getDefault()
	 * @generated
	 */
	void setDefault(String value);

	/**
	 * Returns the value of the '<em><b>Final</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Final</em>' attribute.
	 * @see #setFinal(Object)
	 * @see org.w3._2001.schema.SchemaPackage#getElement_Final()
	 * @model dataType="org.w3._2001.schema.DerivationSet"
	 *        extendedMetaData="kind='attribute' name='final'"
	 * @generated
	 */
	Object getFinal();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Element#getFinal <em>Final</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Final</em>' attribute.
	 * @see #getFinal()
	 * @generated
	 */
	void setFinal(Object value);

	/**
	 * Returns the value of the '<em><b>Fixed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fixed</em>' attribute.
	 * @see #setFixed(String)
	 * @see org.w3._2001.schema.SchemaPackage#getElement_Fixed()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='fixed'"
	 * @generated
	 */
	String getFixed();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Element#getFixed <em>Fixed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fixed</em>' attribute.
	 * @see #getFixed()
	 * @generated
	 */
	void setFixed(String value);

	/**
	 * Returns the value of the '<em><b>Form</b></em>' attribute.
	 * The literals are from the enumeration {@link org.w3._2001.schema.FormChoice}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Form</em>' attribute.
	 * @see org.w3._2001.schema.FormChoice
	 * @see #isSetForm()
	 * @see #unsetForm()
	 * @see #setForm(FormChoice)
	 * @see org.w3._2001.schema.SchemaPackage#getElement_Form()
	 * @model unsettable="true"
	 *        extendedMetaData="kind='attribute' name='form'"
	 * @generated
	 */
	FormChoice getForm();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Element#getForm <em>Form</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Form</em>' attribute.
	 * @see org.w3._2001.schema.FormChoice
	 * @see #isSetForm()
	 * @see #unsetForm()
	 * @see #getForm()
	 * @generated
	 */
	void setForm(FormChoice value);

	/**
	 * Unsets the value of the '{@link org.w3._2001.schema.Element#getForm <em>Form</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetForm()
	 * @see #getForm()
	 * @see #setForm(FormChoice)
	 * @generated
	 */
	void unsetForm();

	/**
	 * Returns whether the value of the '{@link org.w3._2001.schema.Element#getForm <em>Form</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Form</em>' attribute is set.
	 * @see #unsetForm()
	 * @see #getForm()
	 * @see #setForm(FormChoice)
	 * @generated
	 */
	boolean isSetForm();

	/**
	 * Returns the value of the '<em><b>Max Occurs</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Occurs</em>' attribute.
	 * @see #isSetMaxOccurs()
	 * @see #unsetMaxOccurs()
	 * @see #setMaxOccurs(Object)
	 * @see org.w3._2001.schema.SchemaPackage#getElement_MaxOccurs()
	 * @model default="1" unsettable="true" dataType="org.w3._2001.schema.AllNNI"
	 *        extendedMetaData="kind='attribute' name='maxOccurs'"
	 * @generated
	 */
	Object getMaxOccurs();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Element#getMaxOccurs <em>Max Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Occurs</em>' attribute.
	 * @see #isSetMaxOccurs()
	 * @see #unsetMaxOccurs()
	 * @see #getMaxOccurs()
	 * @generated
	 */
	void setMaxOccurs(Object value);

	/**
	 * Unsets the value of the '{@link org.w3._2001.schema.Element#getMaxOccurs <em>Max Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaxOccurs()
	 * @see #getMaxOccurs()
	 * @see #setMaxOccurs(Object)
	 * @generated
	 */
	void unsetMaxOccurs();

	/**
	 * Returns whether the value of the '{@link org.w3._2001.schema.Element#getMaxOccurs <em>Max Occurs</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Max Occurs</em>' attribute is set.
	 * @see #unsetMaxOccurs()
	 * @see #getMaxOccurs()
	 * @see #setMaxOccurs(Object)
	 * @generated
	 */
	boolean isSetMaxOccurs();

	/**
	 * Returns the value of the '<em><b>Min Occurs</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Occurs</em>' attribute.
	 * @see #isSetMinOccurs()
	 * @see #unsetMinOccurs()
	 * @see #setMinOccurs(BigInteger)
	 * @see org.w3._2001.schema.SchemaPackage#getElement_MinOccurs()
	 * @model default="1" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger"
	 *        extendedMetaData="kind='attribute' name='minOccurs'"
	 * @generated
	 */
	BigInteger getMinOccurs();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Element#getMinOccurs <em>Min Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Occurs</em>' attribute.
	 * @see #isSetMinOccurs()
	 * @see #unsetMinOccurs()
	 * @see #getMinOccurs()
	 * @generated
	 */
	void setMinOccurs(BigInteger value);

	/**
	 * Unsets the value of the '{@link org.w3._2001.schema.Element#getMinOccurs <em>Min Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinOccurs()
	 * @see #getMinOccurs()
	 * @see #setMinOccurs(BigInteger)
	 * @generated
	 */
	void unsetMinOccurs();

	/**
	 * Returns whether the value of the '{@link org.w3._2001.schema.Element#getMinOccurs <em>Min Occurs</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Min Occurs</em>' attribute is set.
	 * @see #unsetMinOccurs()
	 * @see #getMinOccurs()
	 * @see #setMinOccurs(BigInteger)
	 * @generated
	 */
	boolean isSetMinOccurs();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.w3._2001.schema.SchemaPackage#getElement_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.NCName"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Element#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Nillable</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nillable</em>' attribute.
	 * @see #isSetNillable()
	 * @see #unsetNillable()
	 * @see #setNillable(boolean)
	 * @see org.w3._2001.schema.SchemaPackage#getElement_Nillable()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='nillable'"
	 * @generated
	 */
	boolean isNillable();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Element#isNillable <em>Nillable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nillable</em>' attribute.
	 * @see #isSetNillable()
	 * @see #unsetNillable()
	 * @see #isNillable()
	 * @generated
	 */
	void setNillable(boolean value);

	/**
	 * Unsets the value of the '{@link org.w3._2001.schema.Element#isNillable <em>Nillable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetNillable()
	 * @see #isNillable()
	 * @see #setNillable(boolean)
	 * @generated
	 */
	void unsetNillable();

	/**
	 * Returns whether the value of the '{@link org.w3._2001.schema.Element#isNillable <em>Nillable</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Nillable</em>' attribute is set.
	 * @see #unsetNillable()
	 * @see #isNillable()
	 * @see #setNillable(boolean)
	 * @generated
	 */
	boolean isSetNillable();

	/**
	 * Returns the value of the '<em><b>Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref</em>' attribute.
	 * @see #setRef(QName)
	 * @see org.w3._2001.schema.SchemaPackage#getElement_Ref()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.QName"
	 *        extendedMetaData="kind='attribute' name='ref'"
	 * @generated
	 */
	QName getRef();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Element#getRef <em>Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref</em>' attribute.
	 * @see #getRef()
	 * @generated
	 */
	void setRef(QName value);

	/**
	 * Returns the value of the '<em><b>Substitution Group</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Substitution Group</em>' attribute.
	 * @see #setSubstitutionGroup(QName)
	 * @see org.w3._2001.schema.SchemaPackage#getElement_SubstitutionGroup()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.QName"
	 *        extendedMetaData="kind='attribute' name='substitutionGroup'"
	 * @generated
	 */
	QName getSubstitutionGroup();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Element#getSubstitutionGroup <em>Substitution Group</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Substitution Group</em>' attribute.
	 * @see #getSubstitutionGroup()
	 * @generated
	 */
	void setSubstitutionGroup(QName value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(QName)
	 * @see org.w3._2001.schema.SchemaPackage#getElement_Type()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.QName"
	 *        extendedMetaData="kind='attribute' name='type'"
	 * @generated
	 */
	QName getType();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Element#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(QName value);

} // Element
