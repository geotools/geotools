/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contact Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Information required to enable contact with the responsible person and/or organization. 
 * For OWS use in the service metadata document, the optional hoursOfService and contactInstructions elements were retained, as possibly being useful in the ServiceProvider section. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.ContactType#getPhone <em>Phone</em>}</li>
 *   <li>{@link net.opengis.ows11.ContactType#getAddress <em>Address</em>}</li>
 *   <li>{@link net.opengis.ows11.ContactType#getOnlineResource <em>Online Resource</em>}</li>
 *   <li>{@link net.opengis.ows11.ContactType#getHoursOfService <em>Hours Of Service</em>}</li>
 *   <li>{@link net.opengis.ows11.ContactType#getContactInstructions <em>Contact Instructions</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getContactType()
 * @model extendedMetaData="name='ContactType' kind='elementOnly'"
 * @generated
 */
public interface ContactType extends EObject {
    /**
     * Returns the value of the '<em><b>Phone</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Telephone numbers at which the organization or individual may be contacted. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Phone</em>' containment reference.
     * @see #setPhone(TelephoneType)
     * @see net.opengis.ows11.Ows11Package#getContactType_Phone()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Phone' namespace='##targetNamespace'"
     * @generated
     */
    TelephoneType getPhone();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ContactType#getPhone <em>Phone</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Phone</em>' containment reference.
     * @see #getPhone()
     * @generated
     */
    void setPhone(TelephoneType value);

    /**
     * Returns the value of the '<em><b>Address</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Physical and email address at which the organization or individual may be contacted. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Address</em>' containment reference.
     * @see #setAddress(AddressType)
     * @see net.opengis.ows11.Ows11Package#getContactType_Address()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Address' namespace='##targetNamespace'"
     * @generated
     */
    AddressType getAddress();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ContactType#getAddress <em>Address</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Address</em>' containment reference.
     * @see #getAddress()
     * @generated
     */
    void setAddress(AddressType value);

    /**
     * Returns the value of the '<em><b>Online Resource</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * On-line information that can be used to contact the individual or organization. OWS specifics: The xlink:href attribute in the xlink:simpleLink attribute group shall be used to reference this resource. Whenever practical, the xlink:href attribute with type anyURI should be a URL from which more contact information can be electronically retrieved. The xlink:title attribute with type "string" can be used to name this set of information. The other attributes in the xlink:simpleLink attribute group should not be used. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Online Resource</em>' containment reference.
     * @see #setOnlineResource(OnlineResourceType)
     * @see net.opengis.ows11.Ows11Package#getContactType_OnlineResource()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='OnlineResource' namespace='##targetNamespace'"
     * @generated
     */
    OnlineResourceType getOnlineResource();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ContactType#getOnlineResource <em>Online Resource</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Online Resource</em>' containment reference.
     * @see #getOnlineResource()
     * @generated
     */
    void setOnlineResource(OnlineResourceType value);

    /**
     * Returns the value of the '<em><b>Hours Of Service</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Time period (including time zone) when individuals can contact the organization or individual. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Hours Of Service</em>' attribute.
     * @see #setHoursOfService(String)
     * @see net.opengis.ows11.Ows11Package#getContactType_HoursOfService()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='HoursOfService' namespace='##targetNamespace'"
     * @generated
     */
    String getHoursOfService();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ContactType#getHoursOfService <em>Hours Of Service</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Hours Of Service</em>' attribute.
     * @see #getHoursOfService()
     * @generated
     */
    void setHoursOfService(String value);

    /**
     * Returns the value of the '<em><b>Contact Instructions</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Supplemental instructions on how or when to contact the individual or organization. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Contact Instructions</em>' attribute.
     * @see #setContactInstructions(String)
     * @see net.opengis.ows11.Ows11Package#getContactType_ContactInstructions()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='ContactInstructions' namespace='##targetNamespace'"
     * @generated
     */
    String getContactInstructions();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ContactType#getContactInstructions <em>Contact Instructions</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Contact Instructions</em>' attribute.
     * @see #getContactInstructions()
     * @generated
     */
    void setContactInstructions(String value);

} // ContactType
