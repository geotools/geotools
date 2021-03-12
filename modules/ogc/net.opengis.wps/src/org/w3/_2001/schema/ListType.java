/**
 */
package org.w3._2001.schema;

import javax.xml.namespace.QName;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>List Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *           itemType attribute and simpleType child are mutually
 *           exclusive, but one or other is required
 *         
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.ListType#getSimpleType <em>Simple Type</em>}</li>
 *   <li>{@link org.w3._2001.schema.ListType#getItemType <em>Item Type</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getListType()
 * @model extendedMetaData="name='list_._type' kind='elementOnly'"
 * @generated
 */
public interface ListType extends Annotated {
	/**
	 * Returns the value of the '<em><b>Simple Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Simple Type</em>' containment reference.
	 * @see #setSimpleType(LocalSimpleType)
	 * @see org.w3._2001.schema.SchemaPackage#getListType_SimpleType()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='simpleType' namespace='##targetNamespace'"
	 * @generated
	 */
	LocalSimpleType getSimpleType();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ListType#getSimpleType <em>Simple Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Simple Type</em>' containment reference.
	 * @see #getSimpleType()
	 * @generated
	 */
	void setSimpleType(LocalSimpleType value);

	/**
	 * Returns the value of the '<em><b>Item Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Item Type</em>' attribute.
	 * @see #setItemType(QName)
	 * @see org.w3._2001.schema.SchemaPackage#getListType_ItemType()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.QName"
	 *        extendedMetaData="kind='attribute' name='itemType'"
	 * @generated
	 */
	QName getItemType();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ListType#getItemType <em>Item Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Item Type</em>' attribute.
	 * @see #getItemType()
	 * @generated
	 */
	void setItemType(QName value);

} // ListType
