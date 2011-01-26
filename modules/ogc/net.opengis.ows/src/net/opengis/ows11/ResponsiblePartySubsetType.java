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
 * A representation of the model object '<em><b>Responsible Party Subset Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Identification of, and means of communication with, person responsible for the server. 
 * For OWS use in the ServiceProvider section of a service metadata document, the optional organizationName element was removed, since this type is always used with the ProviderName element which provides that information. The mandatory "role" element was changed to optional, since no clear use of this information is known in the ServiceProvider section. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.ResponsiblePartySubsetType#getIndividualName <em>Individual Name</em>}</li>
 *   <li>{@link net.opengis.ows11.ResponsiblePartySubsetType#getPositionName <em>Position Name</em>}</li>
 *   <li>{@link net.opengis.ows11.ResponsiblePartySubsetType#getContactInfo <em>Contact Info</em>}</li>
 *   <li>{@link net.opengis.ows11.ResponsiblePartySubsetType#getRole <em>Role</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getResponsiblePartySubsetType()
 * @model extendedMetaData="name='ResponsiblePartySubsetType' kind='elementOnly'"
 * @generated
 */
public interface ResponsiblePartySubsetType extends EObject {
    /**
     * Returns the value of the '<em><b>Individual Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Name of the responsible person: surname, given name, title separated by a delimiter. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Individual Name</em>' attribute.
     * @see #setIndividualName(String)
     * @see net.opengis.ows11.Ows11Package#getResponsiblePartySubsetType_IndividualName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='IndividualName' namespace='##targetNamespace'"
     * @generated
     */
    String getIndividualName();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ResponsiblePartySubsetType#getIndividualName <em>Individual Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Individual Name</em>' attribute.
     * @see #getIndividualName()
     * @generated
     */
    void setIndividualName(String value);

    /**
     * Returns the value of the '<em><b>Position Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Role or position of the responsible person. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Position Name</em>' attribute.
     * @see #setPositionName(String)
     * @see net.opengis.ows11.Ows11Package#getResponsiblePartySubsetType_PositionName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='PositionName' namespace='##targetNamespace'"
     * @generated
     */
    String getPositionName();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ResponsiblePartySubsetType#getPositionName <em>Position Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Position Name</em>' attribute.
     * @see #getPositionName()
     * @generated
     */
    void setPositionName(String value);

    /**
     * Returns the value of the '<em><b>Contact Info</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Address of the responsible party. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Contact Info</em>' containment reference.
     * @see #setContactInfo(ContactType)
     * @see net.opengis.ows11.Ows11Package#getResponsiblePartySubsetType_ContactInfo()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ContactInfo' namespace='##targetNamespace'"
     * @generated
     */
    ContactType getContactInfo();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ResponsiblePartySubsetType#getContactInfo <em>Contact Info</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Contact Info</em>' containment reference.
     * @see #getContactInfo()
     * @generated
     */
    void setContactInfo(ContactType value);

    /**
     * Returns the value of the '<em><b>Role</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Function performed by the responsible party. Possible values of this Role shall include the values and the meanings listed in Subclause B.5.5 of ISO 19115:2003. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Role</em>' containment reference.
     * @see #setRole(CodeType)
     * @see net.opengis.ows11.Ows11Package#getResponsiblePartySubsetType_Role()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Role' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getRole();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ResponsiblePartySubsetType#getRole <em>Role</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Role</em>' containment reference.
     * @see #getRole()
     * @generated
     */
    void setRole(CodeType value);

} // ResponsiblePartySubsetType
