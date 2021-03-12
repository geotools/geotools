/**
 */
package net.opengis.wps20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Supported CRS Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.SupportedCRSType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.wps20.SupportedCRSType#isDefault <em>Default</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getSupportedCRSType()
 * @model extendedMetaData="name='SupportedCRS_._type' kind='simple'"
 * @generated
 */
public interface SupportedCRSType extends EObject {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see net.opengis.wps20.Wps20Package#getSupportedCRSType_Value()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="name=':0' kind='simple'"
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.SupportedCRSType#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

	/**
	 * Returns the value of the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default</em>' attribute.
	 * @see #isSetDefault()
	 * @see #unsetDefault()
	 * @see #setDefault(boolean)
	 * @see net.opengis.wps20.Wps20Package#getSupportedCRSType_Default()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='default'"
	 * @generated
	 */
	boolean isDefault();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.SupportedCRSType#isDefault <em>Default</em>}' attribute.
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
	 * Unsets the value of the '{@link net.opengis.wps20.SupportedCRSType#isDefault <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDefault()
	 * @see #isDefault()
	 * @see #setDefault(boolean)
	 * @generated
	 */
	void unsetDefault();

	/**
	 * Returns whether the value of the '{@link net.opengis.wps20.SupportedCRSType#isDefault <em>Default</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Default</em>' attribute is set.
	 * @see #unsetDefault()
	 * @see #isDefault()
	 * @see #setDefault(boolean)
	 * @generated
	 */
	boolean isSetDefault();

} // SupportedCRSType
