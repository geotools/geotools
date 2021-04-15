/**
 */
package net.opengis.wps20;

import net.opengis.ows20.CapabilitiesBaseType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>WPS Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.WPSCapabilitiesType#getContents <em>Contents</em>}</li>
 *   <li>{@link net.opengis.wps20.WPSCapabilitiesType#getExtension <em>Extension</em>}</li>
 *   <li>{@link net.opengis.wps20.WPSCapabilitiesType#getService <em>Service</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getWPSCapabilitiesType()
 * @model extendedMetaData="name='WPSCapabilitiesType' kind='elementOnly'"
 * @generated
 */
public interface WPSCapabilitiesType extends CapabilitiesBaseType {
	/**
	 * Returns the value of the '<em><b>Contents</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * List of brief descriptions of the processes offered by this WPS server. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Contents</em>' containment reference.
	 * @see #setContents(ContentsType)
	 * @see net.opengis.wps20.Wps20Package#getWPSCapabilitiesType_Contents()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='Contents' namespace='##targetNamespace'"
	 * @generated
	 */
	ContentsType getContents();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.WPSCapabilitiesType#getContents <em>Contents</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contents</em>' containment reference.
	 * @see #getContents()
	 * @generated
	 */
	void setContents(ContentsType value);

	/**
	 * Returns the value of the '<em><b>Extension</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * container for elements defined by extension specifications
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Extension</em>' containment reference.
	 * @see #setExtension(ExtensionType)
	 * @see net.opengis.wps20.Wps20Package#getWPSCapabilitiesType_Extension()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Extension' namespace='##targetNamespace'"
	 * @generated
	 */
	ExtensionType getExtension();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.WPSCapabilitiesType#getExtension <em>Extension</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extension</em>' containment reference.
	 * @see #getExtension()
	 * @generated
	 */
	void setExtension(ExtensionType value);

	/**
	 * Returns the value of the '<em><b>Service</b></em>' attribute.
	 * The default value is <code>"WPS"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service</em>' attribute.
	 * @see #isSetService()
	 * @see #unsetService()
	 * @see #setService(Object)
	 * @see net.opengis.wps20.Wps20Package#getWPSCapabilitiesType_Service()
	 * @model default="WPS" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType" required="true"
	 *        extendedMetaData="kind='attribute' name='service'"
	 * @generated
	 */
	Object getService();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.WPSCapabilitiesType#getService <em>Service</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service</em>' attribute.
	 * @see #isSetService()
	 * @see #unsetService()
	 * @see #getService()
	 * @generated
	 */
	void setService(Object value);

	/**
	 * Unsets the value of the '{@link net.opengis.wps20.WPSCapabilitiesType#getService <em>Service</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetService()
	 * @see #getService()
	 * @see #setService(Object)
	 * @generated
	 */
	void unsetService();

	/**
	 * Returns whether the value of the '{@link net.opengis.wps20.WPSCapabilitiesType#getService <em>Service</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Service</em>' attribute is set.
	 * @see #unsetService()
	 * @see #getService()
	 * @see #setService(Object)
	 * @generated
	 */
	boolean isSetService();

} // WPSCapabilitiesType
