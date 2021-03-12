/**
 */
package net.opengis.wps20;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Literal Data Domain Type1</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.LiteralDataDomainType1#isDefault <em>Default</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getLiteralDataDomainType1()
 * @model extendedMetaData="name='LiteralDataDomain_._type' kind='elementOnly'"
 * @generated
 */
public interface LiteralDataDomainType1 extends LiteralDataDomainType {
	/**
	 * Returns the value of the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 												Indicates that this LiteralDataDomain is the default domain.
	 * 											
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Default</em>' attribute.
	 * @see #isSetDefault()
	 * @see #unsetDefault()
	 * @see #setDefault(boolean)
	 * @see net.opengis.wps20.Wps20Package#getLiteralDataDomainType1_Default()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='default'"
	 * @generated
	 */
	boolean isDefault();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.LiteralDataDomainType1#isDefault <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default</em>' attribute.
	 * @see #isSetDefault()
	 * @see #unsetDefault()
	 * @see #isDefault()
	 * @generated
	 */
	void setDefault(boolean value);

	/**
	 * Unsets the value of the '{@link net.opengis.wps20.LiteralDataDomainType1#isDefault <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDefault()
	 * @see #isDefault()
	 * @see #setDefault(boolean)
	 * @generated
	 */
	void unsetDefault();

	/**
	 * Returns whether the value of the '{@link net.opengis.wps20.LiteralDataDomainType1#isDefault <em>Default</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Default</em>' attribute is set.
	 * @see #unsetDefault()
	 * @see #isDefault()
	 * @see #setDefault(boolean)
	 * @generated
	 */
	boolean isSetDefault();

} // LiteralDataDomainType1
