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
 * A representation of the model object '<em><b>Service Provider Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.ServiceProviderType#getProviderName <em>Provider Name</em>}</li>
 *   <li>{@link net.opengis.ows11.ServiceProviderType#getProviderSite <em>Provider Site</em>}</li>
 *   <li>{@link net.opengis.ows11.ServiceProviderType#getServiceContact <em>Service Contact</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getServiceProviderType()
 * @model extendedMetaData="name='ServiceProvider_._type' kind='elementOnly'"
 * @generated
 */
public interface ServiceProviderType extends EObject {
    /**
     * Returns the value of the '<em><b>Provider Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A unique identifier for the service provider organization. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Provider Name</em>' attribute.
     * @see #setProviderName(String)
     * @see net.opengis.ows11.Ows11Package#getServiceProviderType_ProviderName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='ProviderName' namespace='##targetNamespace'"
     * @generated
     */
    String getProviderName();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ServiceProviderType#getProviderName <em>Provider Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Provider Name</em>' attribute.
     * @see #getProviderName()
     * @generated
     */
    void setProviderName(String value);

    /**
     * Returns the value of the '<em><b>Provider Site</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to the most relevant web site of the service provider. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Provider Site</em>' containment reference.
     * @see #setProviderSite(OnlineResourceType)
     * @see net.opengis.ows11.Ows11Package#getServiceProviderType_ProviderSite()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ProviderSite' namespace='##targetNamespace'"
     * @generated
     */
    OnlineResourceType getProviderSite();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ServiceProviderType#getProviderSite <em>Provider Site</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Provider Site</em>' containment reference.
     * @see #getProviderSite()
     * @generated
     */
    void setProviderSite(OnlineResourceType value);

    /**
     * Returns the value of the '<em><b>Service Contact</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Information for contacting the service provider. The OnlineResource element within this ServiceContact element should not be used to reference a web site of the service provider. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Service Contact</em>' containment reference.
     * @see #setServiceContact(ResponsiblePartySubsetType)
     * @see net.opengis.ows11.Ows11Package#getServiceProviderType_ServiceContact()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='ServiceContact' namespace='##targetNamespace'"
     * @generated
     */
    ResponsiblePartySubsetType getServiceContact();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ServiceProviderType#getServiceContact <em>Service Contact</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service Contact</em>' containment reference.
     * @see #getServiceContact()
     * @generated
     */
    void setServiceContact(ResponsiblePartySubsetType value);

} // ServiceProviderType
