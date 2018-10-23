/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Request Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * WPS operation request base, for all WPS operations except GetCapabilities. In this XML encoding, no "request" parameter is included, since the element name specifies the specific operation.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps10.RequestBaseType#getLanguage <em>Language</em>}</li>
 *   <li>{@link net.opengis.wps10.RequestBaseType#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wps10.RequestBaseType#getVersion <em>Version</em>}</li>
 *   <li>{@link net.opengis.wps10.RequestBaseType#getBaseUrl <em>Base Url</em>}</li>
 *   <li>{@link net.opengis.wps10.RequestBaseType#getExtendedProperties <em>Extended Properties</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps10.Wps10Package#getRequestBaseType()
 * @model extendedMetaData="name='RequestBaseType' kind='empty'"
 * @generated
 */
public interface RequestBaseType extends EObject {
    /**
   * Returns the value of the '<em><b>Language</b></em>' attribute.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * RFC 4646 language code of the human-readable text (e.g. "en-CA").
   * <!-- end-model-doc -->
   * @return the value of the '<em>Language</em>' attribute.
   * @see #setLanguage(String)
   * @see net.opengis.wps10.Wps10Package#getRequestBaseType_Language()
   * @model dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='language'"
   * @generated
   */
    String getLanguage();

    /**
   * Sets the value of the '{@link net.opengis.wps10.RequestBaseType#getLanguage <em>Language</em>}' attribute.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @param value the new value of the '<em>Language</em>' attribute.
   * @see #getLanguage()
   * @generated
   */
    void setLanguage(String value);

    /**
   * Returns the value of the '<em><b>Service</b></em>' attribute.
   * The default value is <code>"WPS"</code>.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Service type identifier (WPS)
   * <!-- end-model-doc -->
   * @return the value of the '<em>Service</em>' attribute.
   * @see #isSetService()
   * @see #unsetService()
   * @see #setService(String)
   * @see net.opengis.wps10.Wps10Package#getRequestBaseType_Service()
   * @model default="WPS" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   *        extendedMetaData="kind='attribute' name='service'"
   * @generated
   */
    String getService();

    /**
   * Sets the value of the '{@link net.opengis.wps10.RequestBaseType#getService <em>Service</em>}' attribute.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @param value the new value of the '<em>Service</em>' attribute.
   * @see #isSetService()
   * @see #unsetService()
   * @see #getService()
   * @generated
   */
    void setService(String value);

    /**
   * Unsets the value of the '{@link net.opengis.wps10.RequestBaseType#getService <em>Service</em>}' attribute.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @see #isSetService()
   * @see #getService()
   * @see #setService(String)
   * @generated
   */
    void unsetService();

    /**
   * Returns whether the value of the '{@link net.opengis.wps10.RequestBaseType#getService <em>Service</em>}' attribute is set.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @return whether the value of the '<em>Service</em>' attribute is set.
   * @see #unsetService()
   * @see #getService()
   * @see #setService(String)
   * @generated
   */
    boolean isSetService();

    /**
   * Returns the value of the '<em><b>Version</b></em>' attribute.
   * The default value is <code>"1.0.0"</code>.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * Version of the WPS interface specification implemented by the server (1.0.0)
   * <!-- end-model-doc -->
   * @return the value of the '<em>Version</em>' attribute.
   * @see #isSetVersion()
   * @see #unsetVersion()
   * @see #setVersion(String)
   * @see net.opengis.wps10.Wps10Package#getRequestBaseType_Version()
   * @model default="1.0.0" unsettable="true" dataType="net.opengis.ows11.VersionType1" required="true"
   *        extendedMetaData="kind='attribute' name='version'"
   * @generated
   */
    String getVersion();

    /**
   * Sets the value of the '{@link net.opengis.wps10.RequestBaseType#getVersion <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @param value the new value of the '<em>Version</em>' attribute.
   * @see #isSetVersion()
   * @see #unsetVersion()
   * @see #getVersion()
   * @generated
   */
    void setVersion(String value);

    /**
   * Unsets the value of the '{@link net.opengis.wps10.RequestBaseType#getVersion <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @see #isSetVersion()
   * @see #getVersion()
   * @see #setVersion(String)
   * @generated
   */
    void unsetVersion();

    /**
   * Returns whether the value of the '{@link net.opengis.wps10.RequestBaseType#getVersion <em>Version</em>}' attribute is set.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @return whether the value of the '<em>Version</em>' attribute is set.
   * @see #unsetVersion()
   * @see #getVersion()
   * @see #setVersion(String)
   * @generated
   */
    boolean isSetVersion();

    /**
   * Returns the value of the '<em><b>Base Url</b></em>' attribute.
   * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Base Url</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
   * @return the value of the '<em>Base Url</em>' attribute.
   * @see #setBaseUrl(String)
   * @see net.opengis.wps10.Wps10Package#getRequestBaseType_BaseUrl()
   * @model
   * @generated
   */
    String getBaseUrl();

    /**
   * Sets the value of the '{@link net.opengis.wps10.RequestBaseType#getBaseUrl <em>Base Url</em>}' attribute.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @param value the new value of the '<em>Base Url</em>' attribute.
   * @see #getBaseUrl()
   * @generated
   */
    void setBaseUrl(String value);

    /**
     * Extended model properties.
     * <p>
     * This map allows client to store additional properties with the this
     * request object, properties that are not part of the model proper.
     * </p>
     * 
     * @model
     */
    Map getExtendedProperties();

				/**
   * Sets the value of the '{@link net.opengis.wps10.RequestBaseType#getExtendedProperties <em>Extended Properties</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Extended Properties</em>' attribute.
   * @see #getExtendedProperties()
   * @generated
   */
	void setExtendedProperties(Map value);

} // RequestBaseType
