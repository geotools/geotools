/**
 */
package org.w3._2001.schema;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Complex Content Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.ComplexContentType#getRestriction <em>Restriction</em>}</li>
 *   <li>{@link org.w3._2001.schema.ComplexContentType#getExtension <em>Extension</em>}</li>
 *   <li>{@link org.w3._2001.schema.ComplexContentType#isMixed <em>Mixed</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getComplexContentType()
 * @model extendedMetaData="name='complexContent_._type' kind='elementOnly'"
 * @generated
 */
public interface ComplexContentType extends Annotated {
	/**
	 * Returns the value of the '<em><b>Restriction</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Restriction</em>' containment reference.
	 * @see #setRestriction(ComplexRestrictionType)
	 * @see org.w3._2001.schema.SchemaPackage#getComplexContentType_Restriction()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='restriction' namespace='##targetNamespace'"
	 * @generated
	 */
	ComplexRestrictionType getRestriction();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ComplexContentType#getRestriction <em>Restriction</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Restriction</em>' containment reference.
	 * @see #getRestriction()
	 * @generated
	 */
	void setRestriction(ComplexRestrictionType value);

	/**
	 * Returns the value of the '<em><b>Extension</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extension</em>' containment reference.
	 * @see #setExtension(ExtensionType)
	 * @see org.w3._2001.schema.SchemaPackage#getComplexContentType_Extension()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='extension' namespace='##targetNamespace'"
	 * @generated
	 */
	ExtensionType getExtension();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ComplexContentType#getExtension <em>Extension</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extension</em>' containment reference.
	 * @see #getExtension()
	 * @generated
	 */
	void setExtension(ExtensionType value);

	/**
	 * Returns the value of the '<em><b>Mixed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        Overrides any setting on complexType parent.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Mixed</em>' attribute.
	 * @see #isSetMixed()
	 * @see #unsetMixed()
	 * @see #setMixed(boolean)
	 * @see org.w3._2001.schema.SchemaPackage#getComplexContentType_Mixed()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='mixed'"
	 * @generated
	 */
	boolean isMixed();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.ComplexContentType#isMixed <em>Mixed</em>}' attribute.
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
	 * Unsets the value of the '{@link org.w3._2001.schema.ComplexContentType#isMixed <em>Mixed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMixed()
	 * @see #isMixed()
	 * @see #setMixed(boolean)
	 * @generated
	 */
	void unsetMixed();

	/**
	 * Returns whether the value of the '{@link org.w3._2001.schema.ComplexContentType#isMixed <em>Mixed</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Mixed</em>' attribute is set.
	 * @see #unsetMixed()
	 * @see #isMixed()
	 * @see #setMixed(boolean)
	 * @generated
	 */
	boolean isSetMixed();

} // ComplexContentType
