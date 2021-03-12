/**
 */
package org.w3._2001.schema;

import javax.xml.namespace.QName;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.Attribute#getSimpleType <em>Simple Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.Attribute#getDefault <em>Default</em>}</li>
 *   <li>{@link org.w3._2001.schema.Attribute#getFixed <em>Fixed</em>}</li>
 *   <li>{@link org.w3._2001.schema.Attribute#getForm <em>Form</em>}</li>
 *   <li>{@link org.w3._2001.schema.Attribute#getName <em>Name</em>}</li>
 *   <li>{@link org.w3._2001.schema.Attribute#getRef <em>Ref</em>}</li>
 *   <li>{@link org.w3._2001.schema.Attribute#getType <em>Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.Attribute#getUse <em>Use</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getAttribute()
 * @model extendedMetaData="name='attribute' kind='elementOnly'"
 * @generated
 */
public interface Attribute extends Annotated {
	/**
	 * Returns the value of the '<em><b>Simple Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Simple Type</em>' containment reference.
	 * @see #setSimpleType(LocalSimpleType)
	 * @see org.w3._2001.schema.SchemaPackage#getAttribute_SimpleType()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='simpleType' namespace='##targetNamespace'"
	 * @generated
	 */
	LocalSimpleType getSimpleType();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Attribute#getSimpleType <em>Simple Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Simple Type</em>' containment reference.
	 * @see #getSimpleType()
	 * @generated
	 */
	void setSimpleType(LocalSimpleType value);

	/**
	 * Returns the value of the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default</em>' attribute.
	 * @see #setDefault(String)
	 * @see org.w3._2001.schema.SchemaPackage#getAttribute_Default()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='default'"
	 * @generated
	 */
	String getDefault();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Attribute#getDefault <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default</em>' attribute.
	 * @see #getDefault()
	 * @generated
	 */
	void setDefault(String value);

	/**
	 * Returns the value of the '<em><b>Fixed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fixed</em>' attribute.
	 * @see #setFixed(String)
	 * @see org.w3._2001.schema.SchemaPackage#getAttribute_Fixed()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='fixed'"
	 * @generated
	 */
	String getFixed();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Attribute#getFixed <em>Fixed</em>}' attribute.
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
	 * @see org.w3._2001.schema.SchemaPackage#getAttribute_Form()
	 * @model unsettable="true"
	 *        extendedMetaData="kind='attribute' name='form'"
	 * @generated
	 */
	FormChoice getForm();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Attribute#getForm <em>Form</em>}' attribute.
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
	 * Unsets the value of the '{@link org.w3._2001.schema.Attribute#getForm <em>Form</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetForm()
	 * @see #getForm()
	 * @see #setForm(FormChoice)
	 * @generated
	 */
	void unsetForm();

	/**
	 * Returns whether the value of the '{@link org.w3._2001.schema.Attribute#getForm <em>Form</em>}' attribute is set.
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
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.w3._2001.schema.SchemaPackage#getAttribute_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.NCName"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Attribute#getName <em>Name</em>}' attribute.
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
	 * @see org.w3._2001.schema.SchemaPackage#getAttribute_Ref()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.QName"
	 *        extendedMetaData="kind='attribute' name='ref'"
	 * @generated
	 */
	QName getRef();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Attribute#getRef <em>Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref</em>' attribute.
	 * @see #getRef()
	 * @generated
	 */
	void setRef(QName value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(QName)
	 * @see org.w3._2001.schema.SchemaPackage#getAttribute_Type()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.QName"
	 *        extendedMetaData="kind='attribute' name='type'"
	 * @generated
	 */
	QName getType();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Attribute#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(QName value);

	/**
	 * Returns the value of the '<em><b>Use</b></em>' attribute.
	 * The default value is <code>"optional"</code>.
	 * The literals are from the enumeration {@link org.w3._2001.schema.UseType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Use</em>' attribute.
	 * @see org.w3._2001.schema.UseType
	 * @see #isSetUse()
	 * @see #unsetUse()
	 * @see #setUse(UseType)
	 * @see org.w3._2001.schema.SchemaPackage#getAttribute_Use()
	 * @model default="optional" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='use'"
	 * @generated
	 */
	UseType getUse();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Attribute#getUse <em>Use</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Use</em>' attribute.
	 * @see org.w3._2001.schema.UseType
	 * @see #isSetUse()
	 * @see #unsetUse()
	 * @see #getUse()
	 * @generated
	 */
	void setUse(UseType value);

	/**
	 * Unsets the value of the '{@link org.w3._2001.schema.Attribute#getUse <em>Use</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetUse()
	 * @see #getUse()
	 * @see #setUse(UseType)
	 * @generated
	 */
	void unsetUse();

	/**
	 * Returns whether the value of the '{@link org.w3._2001.schema.Attribute#getUse <em>Use</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Use</em>' attribute is set.
	 * @see #unsetUse()
	 * @see #getUse()
	 * @see #setUse(UseType)
	 * @generated
	 */
	boolean isSetUse();

} // Attribute
