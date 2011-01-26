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
 * A representation of the model object '<em><b>Responsible Party Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Identification of, and means of communication with, person responsible for the server. At least one of IndividualName, OrganisationName, or PositionName shall be included. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.ResponsiblePartyType#getIndividualName <em>Individual Name</em>}</li>
 *   <li>{@link net.opengis.ows11.ResponsiblePartyType#getOrganisationName <em>Organisation Name</em>}</li>
 *   <li>{@link net.opengis.ows11.ResponsiblePartyType#getPositionName <em>Position Name</em>}</li>
 *   <li>{@link net.opengis.ows11.ResponsiblePartyType#getContactInfo <em>Contact Info</em>}</li>
 *   <li>{@link net.opengis.ows11.ResponsiblePartyType#getRole <em>Role</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getResponsiblePartyType()
 * @model extendedMetaData="name='ResponsiblePartyType' kind='elementOnly'"
 * @generated
 */
public interface ResponsiblePartyType extends EObject {
    /**
     * Returns the value of the '<em><b>Individual Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Name of the responsible person: surname, given name, title separated by a delimiter. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Individual Name</em>' attribute.
     * @see #setIndividualName(String)
     * @see net.opengis.ows11.Ows11Package#getResponsiblePartyType_IndividualName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='IndividualName' namespace='##targetNamespace'"
     * @generated
     */
    String getIndividualName();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ResponsiblePartyType#getIndividualName <em>Individual Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Individual Name</em>' attribute.
     * @see #getIndividualName()
     * @generated
     */
    void setIndividualName(String value);

    /**
     * Returns the value of the '<em><b>Organisation Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Name of the responsible organization. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Organisation Name</em>' attribute.
     * @see #setOrganisationName(String)
     * @see net.opengis.ows11.Ows11Package#getResponsiblePartyType_OrganisationName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='OrganisationName' namespace='##targetNamespace'"
     * @generated
     */
    String getOrganisationName();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ResponsiblePartyType#getOrganisationName <em>Organisation Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Organisation Name</em>' attribute.
     * @see #getOrganisationName()
     * @generated
     */
    void setOrganisationName(String value);

    /**
     * Returns the value of the '<em><b>Position Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Role or position of the responsible person. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Position Name</em>' attribute.
     * @see #setPositionName(String)
     * @see net.opengis.ows11.Ows11Package#getResponsiblePartyType_PositionName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='PositionName' namespace='##targetNamespace'"
     * @generated
     */
    String getPositionName();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ResponsiblePartyType#getPositionName <em>Position Name</em>}' attribute.
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
     * @see net.opengis.ows11.Ows11Package#getResponsiblePartyType_ContactInfo()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ContactInfo' namespace='##targetNamespace'"
     * @generated
     */
    ContactType getContactInfo();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ResponsiblePartyType#getContactInfo <em>Contact Info</em>}' containment reference.
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
     * @see net.opengis.ows11.Ows11Package#getResponsiblePartyType_Role()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Role' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getRole();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ResponsiblePartyType#getRole <em>Role</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Role</em>' containment reference.
     * @see #getRole()
     * @generated
     */
    void setRole(CodeType value);

} // ResponsiblePartyType
