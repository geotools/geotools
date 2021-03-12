/**
 */
package org.w3._2001.schema;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Simple Content Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.SimpleContentType#getRestriction <em>Restriction</em>}</li>
 *   <li>{@link org.w3._2001.schema.SimpleContentType#getExtension <em>Extension</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getSimpleContentType()
 * @model extendedMetaData="name='simpleContent_._type' kind='elementOnly'"
 * @generated
 */
public interface SimpleContentType extends Annotated {
	/**
	 * Returns the value of the '<em><b>Restriction</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restriction</em>' containment reference.
	 * @see #setRestriction(SimpleRestrictionType)
	 * @see org.w3._2001.schema.SchemaPackage#getSimpleContentType_Restriction()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='restriction' namespace='##targetNamespace'"
	 * @generated
	 */
	SimpleRestrictionType getRestriction();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.SimpleContentType#getRestriction <em>Restriction</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Restriction</em>' containment reference.
	 * @see #getRestriction()
	 * @generated
	 */
	void setRestriction(SimpleRestrictionType value);

	/**
	 * Returns the value of the '<em><b>Extension</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extension</em>' containment reference.
	 * @see #setExtension(SimpleExtensionType)
	 * @see org.w3._2001.schema.SchemaPackage#getSimpleContentType_Extension()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='extension' namespace='##targetNamespace'"
	 * @generated
	 */
	SimpleExtensionType getExtension();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.SimpleContentType#getExtension <em>Extension</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extension</em>' containment reference.
	 * @see #getExtension()
	 * @generated
	 */
	void setExtension(SimpleExtensionType value);

} // SimpleContentType
