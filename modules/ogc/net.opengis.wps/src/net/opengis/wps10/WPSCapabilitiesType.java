/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import net.opengis.ows11.CapabilitiesBaseType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>WPS Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.WPSCapabilitiesType#getProcessOfferings <em>Process Offerings</em>}</li>
 *   <li>{@link net.opengis.wps10.WPSCapabilitiesType#getLanguages <em>Languages</em>}</li>
 *   <li>{@link net.opengis.wps10.WPSCapabilitiesType#getWSDL <em>WSDL</em>}</li>
 *   <li>{@link net.opengis.wps10.WPSCapabilitiesType#getLang <em>Lang</em>}</li>
 *   <li>{@link net.opengis.wps10.WPSCapabilitiesType#getService <em>Service</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getWPSCapabilitiesType()
 * @model extendedMetaData="name='WPSCapabilitiesType' kind='elementOnly'"
 * @generated
 */
public interface WPSCapabilitiesType extends CapabilitiesBaseType {
    /**
     * Returns the value of the '<em><b>Process Offerings</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * List of brief descriptions of the processes offered by this WPS server.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Process Offerings</em>' containment reference.
     * @see #setProcessOfferings(ProcessOfferingsType)
     * @see net.opengis.wps10.Wps10Package#getWPSCapabilitiesType_ProcessOfferings()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='ProcessOfferings' namespace='##targetNamespace'"
     * @generated
     */
    ProcessOfferingsType getProcessOfferings();

    /**
     * Sets the value of the '{@link net.opengis.wps10.WPSCapabilitiesType#getProcessOfferings <em>Process Offerings</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Process Offerings</em>' containment reference.
     * @see #getProcessOfferings()
     * @generated
     */
    void setProcessOfferings(ProcessOfferingsType value);

    /**
     * Returns the value of the '<em><b>Languages</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * List of the default and other languages supported by this service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Languages</em>' containment reference.
     * @see #setLanguages(LanguagesType1)
     * @see net.opengis.wps10.Wps10Package#getWPSCapabilitiesType_Languages()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Languages' namespace='##targetNamespace'"
     * @generated
     */
    LanguagesType1 getLanguages();

    /**
     * Sets the value of the '{@link net.opengis.wps10.WPSCapabilitiesType#getLanguages <em>Languages</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Languages</em>' containment reference.
     * @see #getLanguages()
     * @generated
     */
    void setLanguages(LanguagesType1 value);

    /**
     * Returns the value of the '<em><b>WSDL</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Location of a WSDL document which describes the entire service.
     * <!-- end-model-doc -->
     * @return the value of the '<em>WSDL</em>' containment reference.
     * @see #setWSDL(WSDLType)
     * @see net.opengis.wps10.Wps10Package#getWPSCapabilitiesType_WSDL()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='WSDL' namespace='##targetNamespace'"
     * @generated
     */
    WSDLType getWSDL();

    /**
     * Sets the value of the '{@link net.opengis.wps10.WPSCapabilitiesType#getWSDL <em>WSDL</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>WSDL</em>' containment reference.
     * @see #getWSDL()
     * @generated
     */
    void setWSDL(WSDLType value);

    /**
     * Returns the value of the '<em><b>Lang</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Attempting to install the relevant ISO 2- and 3-letter
     *          codes as the enumerated possible values is probably never
     *          going to be a realistic possibility.  See
     *          RFC 3066 at http://www.ietf.org/rfc/rfc3066.txt and the IANA registry
     *          at http://www.iana.org/assignments/lang-tag-apps.htm for
     *          further information.
     * 
     *          The union allows for the 'un-declaration' of xml:lang with
     *          the empty string.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Lang</em>' attribute.
     * @see #setLang(String)
     * @see net.opengis.wps10.Wps10Package#getWPSCapabilitiesType_Lang()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='lang' namespace='http://www.w3.org/XML/1998/namespace'"
     * @generated
     */
    String getLang();

    /**
     * Sets the value of the '{@link net.opengis.wps10.WPSCapabilitiesType#getLang <em>Lang</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lang</em>' attribute.
     * @see #getLang()
     * @generated
     */
    void setLang(String value);

    /**
     * Returns the value of the '<em><b>Service</b></em>' attribute.
     * The default value is <code>"WPS"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Service</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Service</em>' attribute.
     * @see #isSetService()
     * @see #unsetService()
     * @see #setService(String)
     * @see net.opengis.wps10.Wps10Package#getWPSCapabilitiesType_Service()
     * @model default="WPS" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='service'"
     * @generated
     */
    String getService();

    /**
     * Sets the value of the '{@link net.opengis.wps10.WPSCapabilitiesType#getService <em>Service</em>}' attribute.
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
     * Unsets the value of the '{@link net.opengis.wps10.WPSCapabilitiesType#getService <em>Service</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetService()
     * @see #getService()
     * @see #setService(String)
     * @generated
     */
    void unsetService();

    /**
     * Returns whether the value of the '{@link net.opengis.wps10.WPSCapabilitiesType#getService <em>Service</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Service</em>' attribute is set.
     * @see #unsetService()
     * @see #getService()
     * @see #setService(String)
     * @generated
     */
    boolean isSetService();

} // WPSCapabilitiesType
