/**
 */
package org.w3._2001.schema;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Selector Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.schema.SelectorType#getXpath <em>Xpath</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.schema.SchemaPackage#getSelectorType()
 * @model extendedMetaData="name='selector_._type' kind='elementOnly'"
 * @generated
 */
public interface SelectorType extends Annotated {
	/**
	 * Returns the value of the '<em><b>Xpath</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Xpath</em>' attribute.
	 * @see #setXpath(String)
	 * @see org.w3._2001.schema.SchemaPackage#getSelectorType_Xpath()
	 * @model dataType="org.w3._2001.schema.XpathType1" required="true"
	 *        extendedMetaData="kind='attribute' name='xpath'"
	 * @generated
	 */
	String getXpath();

	/**
	 * Sets the value of the '{@link org.w3._2001.schema.SelectorType#getXpath <em>Xpath</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Xpath</em>' attribute.
	 * @see #getXpath()
	 * @generated
	 */
	void setXpath(String value);

} // SelectorType
