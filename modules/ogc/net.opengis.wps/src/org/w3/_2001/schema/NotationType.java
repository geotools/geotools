/**
 */
package org.w3._2001.schema;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Notation Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.NotationType#getName <em>Name</em>}</li>
 *   <li>{@link org.w3._2001.schema.NotationType#getPublic <em>Public</em>}</li>
 *   <li>{@link org.w3._2001.schema.NotationType#getSystem <em>System</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getNotationType()
 * @model extendedMetaData="name='notation_._type' kind='elementOnly'"
 * @generated
 */
public interface NotationType extends Annotated {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.w3._2001.schema.SchemaPackage#getNotationType_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.NCName" required="true"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.NotationType#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Public</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Public</em>' attribute.
	 * @see #setPublic(String)
	 * @see org.w3._2001.schema.SchemaPackage#getNotationType_Public()
	 * @model dataType="org.w3._2001.schema.Public"
	 *        extendedMetaData="kind='attribute' name='public'"
	 * @generated
	 */
	String getPublic();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.NotationType#getPublic <em>Public</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Public</em>' attribute.
	 * @see #getPublic()
	 * @generated
	 */
	void setPublic(String value);

	/**
	 * Returns the value of the '<em><b>System</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>System</em>' attribute.
	 * @see #setSystem(String)
	 * @see org.w3._2001.schema.SchemaPackage#getNotationType_System()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='system'"
	 * @generated
	 */
	String getSystem();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.NotationType#getSystem <em>System</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>System</em>' attribute.
	 * @see #getSystem()
	 * @generated
	 */
	void setSystem(String value);

} // NotationType
