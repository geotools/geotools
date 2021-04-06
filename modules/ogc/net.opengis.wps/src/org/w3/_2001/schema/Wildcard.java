/**
 */
package org.w3._2001.schema;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Wildcard</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.Wildcard#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link org.w3._2001.schema.Wildcard#getProcessContents <em>Process Contents</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getWildcard()
 * @model extendedMetaData="name='wildcard' kind='elementOnly'"
 * @generated
 */
public interface Wildcard extends Annotated {
	/**
	 * Returns the value of the '<em><b>Namespace</b></em>' attribute.
	 * The default value is <code>"##any"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Namespace</em>' attribute.
	 * @see #isSetNamespace()
	 * @see #unsetNamespace()
	 * @see #setNamespace(Object)
	 * @see org.w3._2001.schema.SchemaPackage#getWildcard_Namespace()
	 * @model default="##any" unsettable="true" dataType="org.w3._2001.schema.NamespaceList"
	 *        extendedMetaData="kind='attribute' name='namespace'"
	 * @generated
	 */
	Object getNamespace();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Wildcard#getNamespace <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Namespace</em>' attribute.
	 * @see #isSetNamespace()
	 * @see #unsetNamespace()
	 * @see #getNamespace()
	 * @generated
	 */
	void setNamespace(Object value);

	/**
	 * Unsets the value of the '{@link org.w3._2001.schema.Wildcard#getNamespace <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetNamespace()
	 * @see #getNamespace()
	 * @see #setNamespace(Object)
	 * @generated
	 */
	void unsetNamespace();

	/**
	 * Returns whether the value of the '{@link org.w3._2001.schema.Wildcard#getNamespace <em>Namespace</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Namespace</em>' attribute is set.
	 * @see #unsetNamespace()
	 * @see #getNamespace()
	 * @see #setNamespace(Object)
	 * @generated
	 */
	boolean isSetNamespace();

	/**
	 * Returns the value of the '<em><b>Process Contents</b></em>' attribute.
	 * The default value is <code>"strict"</code>.
	 * The literals are from the enumeration {@link org.w3._2001.schema.ProcessContentsType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Process Contents</em>' attribute.
	 * @see org.w3._2001.schema.ProcessContentsType
	 * @see #isSetProcessContents()
	 * @see #unsetProcessContents()
	 * @see #setProcessContents(ProcessContentsType)
	 * @see org.w3._2001.schema.SchemaPackage#getWildcard_ProcessContents()
	 * @model default="strict" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='processContents'"
	 * @generated
	 */
	ProcessContentsType getProcessContents();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.Wildcard#getProcessContents <em>Process Contents</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Process Contents</em>' attribute.
	 * @see org.w3._2001.schema.ProcessContentsType
	 * @see #isSetProcessContents()
	 * @see #unsetProcessContents()
	 * @see #getProcessContents()
	 * @generated
	 */
	void setProcessContents(ProcessContentsType value);

	/**
	 * Unsets the value of the '{@link org.w3._2001.schema.Wildcard#getProcessContents <em>Process Contents</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetProcessContents()
	 * @see #getProcessContents()
	 * @see #setProcessContents(ProcessContentsType)
	 * @generated
	 */
	void unsetProcessContents();

	/**
	 * Returns whether the value of the '{@link org.w3._2001.schema.Wildcard#getProcessContents <em>Process Contents</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Process Contents</em>' attribute is set.
	 * @see #unsetProcessContents()
	 * @see #getProcessContents()
	 * @see #setProcessContents(ProcessContentsType)
	 * @generated
	 */
	boolean isSetProcessContents();

} // Wildcard
