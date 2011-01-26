/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import java.util.Map;

import net.opengis.ows11.AcceptVersionsType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.GetCapabilitiesType#getAcceptVersions <em>Accept Versions</em>}</li>
 *   <li>{@link net.opengis.wps10.GetCapabilitiesType#getLanguage <em>Language</em>}</li>
 *   <li>{@link net.opengis.wps10.GetCapabilitiesType#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wps10.GetCapabilitiesType#getBaseUrl <em>Base Url</em>}</li>
 *   <li>{@link net.opengis.wps10.GetCapabilitiesType#getExtendedProperties <em>Extended Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getGetCapabilitiesType()
 * @model extendedMetaData="name='GetCapabilities_._type' kind='elementOnly'"
 * @generated
 */
public interface GetCapabilitiesType extends EObject {
    /**
	 * Returns the value of the '<em><b>Accept Versions</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * When omitted, server shall return latest supported version.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Accept Versions</em>' containment reference.
	 * @see #setAcceptVersions(AcceptVersionsType)
	 * @see net.opengis.wps10.Wps10Package#getGetCapabilitiesType_AcceptVersions()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='AcceptVersions' namespace='##targetNamespace'"
	 * @generated
	 */
    AcceptVersionsType getAcceptVersions();

    /**
	 * Sets the value of the '{@link net.opengis.wps10.GetCapabilitiesType#getAcceptVersions <em>Accept Versions</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Accept Versions</em>' containment reference.
	 * @see #getAcceptVersions()
	 * @generated
	 */
    void setAcceptVersions(AcceptVersionsType value);

    /**
	 * Returns the value of the '<em><b>Language</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * RFC 4646 language code of the human-readable text (e.g. "en-CA").
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Language</em>' attribute.
	 * @see #setLanguage(String)
	 * @see net.opengis.wps10.Wps10Package#getGetCapabilitiesType_Language()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='language'"
	 * @generated
	 */
    String getLanguage();

    /**
	 * Sets the value of the '{@link net.opengis.wps10.GetCapabilitiesType#getLanguage <em>Language</em>}' attribute.
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
	 * OGC service type identifier (WPS).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Service</em>' attribute.
	 * @see #isSetService()
	 * @see #unsetService()
	 * @see #setService(String)
	 * @see net.opengis.wps10.Wps10Package#getGetCapabilitiesType_Service()
	 * @model default="WPS" unsettable="true" dataType="net.opengis.ows11.ServiceType" required="true"
	 *        extendedMetaData="kind='attribute' name='service'"
	 * @generated
	 */
    String getService();

    /**
	 * Sets the value of the '{@link net.opengis.wps10.GetCapabilitiesType#getService <em>Service</em>}' attribute.
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
	 * Unsets the value of the '{@link net.opengis.wps10.GetCapabilitiesType#getService <em>Service</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetService()
	 * @see #getService()
	 * @see #setService(String)
	 * @generated
	 */
    void unsetService();

    /**
	 * Returns whether the value of the '{@link net.opengis.wps10.GetCapabilitiesType#getService <em>Service</em>}' attribute is set.
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
	 * Returns the value of the '<em><b>Base Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Base Url</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Url</em>' attribute.
	 * @see #setBaseUrl(String)
	 * @see net.opengis.wps10.Wps10Package#getGetCapabilitiesType_BaseUrl()
	 * @model
	 * @generated
	 */
    String getBaseUrl();

    /**
	 * Sets the value of the '{@link net.opengis.wps10.GetCapabilitiesType#getBaseUrl <em>Base Url</em>}' attribute.
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
	 * Sets the value of the '{@link net.opengis.wps10.GetCapabilitiesType#getExtendedProperties <em>Extended Properties</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extended Properties</em>' attribute.
	 * @see #getExtendedProperties()
	 * @generated
	 */
	void setExtendedProperties(Map value);

} // GetCapabilitiesType
