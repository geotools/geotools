/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import net.opengis.gml.CodeListType;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Service Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A minimal, human readable rescription of the service.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.ServiceType#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link net.opengis.wcs10.ServiceType#getResponsibleParty <em>Responsible Party</em>}</li>
 *   <li>{@link net.opengis.wcs10.ServiceType#getFees <em>Fees</em>}</li>
 *   <li>{@link net.opengis.wcs10.ServiceType#getAccessConstraints <em>Access Constraints</em>}</li>
 *   <li>{@link net.opengis.wcs10.ServiceType#getUpdateSequence <em>Update Sequence</em>}</li>
 *   <li>{@link net.opengis.wcs10.ServiceType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getServiceType()
 * @model extendedMetaData="name='ServiceType' kind='elementOnly'"
 * @generated
 */
public interface ServiceType extends AbstractDescriptionType {
    /**
	 * Returns the value of the '<em><b>Keywords</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wcs10.KeywordsType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of one or more commonly used or formalised word(s) or phrase(s) used to describe the subject. When needed, the optional "type" can name the type of the associated list of keywords that shall all have the same type. Also when needed, the codeSpace attribute of that "type" can also reference the type name authority and/or thesaurus. (Largely based on MD_Keywords class in ISO 19115.)
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Keywords</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getServiceType_Keywords()
	 * @model type="net.opengis.wcs10.KeywordsType" containment="true"
	 *        extendedMetaData="kind='element' name='keywords' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getKeywords();

    /**
	 * Returns the value of the '<em><b>Responsible Party</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Responsible Party</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Responsible Party</em>' containment reference.
	 * @see #setResponsibleParty(ResponsiblePartyType)
	 * @see net.opengis.wcs10.Wcs10Package#getServiceType_ResponsibleParty()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='responsibleParty' namespace='##targetNamespace'"
	 * @generated
	 */
    ResponsiblePartyType getResponsibleParty();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ServiceType#getResponsibleParty <em>Responsible Party</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Responsible Party</em>' containment reference.
	 * @see #getResponsibleParty()
	 * @generated
	 */
    void setResponsibleParty(ResponsiblePartyType value);

    /**
	 * Returns the value of the '<em><b>Fees</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A text string identifying any fees imposed by the service provider. The keyword NONE shall be used to mean no fees.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fees</em>' containment reference.
	 * @see #setFees(CodeListType)
	 * @see net.opengis.wcs10.Wcs10Package#getServiceType_Fees()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='fees' namespace='##targetNamespace'"
	 * @generated
	 */
    CodeListType getFees();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ServiceType#getFees <em>Fees</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fees</em>' containment reference.
	 * @see #getFees()
	 * @generated
	 */
    void setFees(CodeListType value);

    /**
	 * Returns the value of the '<em><b>Access Constraints</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.gml.CodeListType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A text string identifying any access constraints imposed by the service provider. The keyword NONE shall be used to mean no access constraints are imposed.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Access Constraints</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getServiceType_AccessConstraints()
	 * @model type="net.opengis.gml.CodeListType" containment="true" required="true"
	 *        extendedMetaData="kind='element' name='accessConstraints' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getAccessConstraints();

    /**
	 * Returns the value of the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Service metadata (Capabilities) document version, having values that are "increased" whenever any change is made in service metadata document. Values are selected by each server, and are always opaque to clients. When supported by server, server shall return this attribute.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Update Sequence</em>' attribute.
	 * @see #setUpdateSequence(String)
	 * @see net.opengis.wcs10.Wcs10Package#getServiceType_UpdateSequence()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='updateSequence'"
	 * @generated
	 */
    String getUpdateSequence();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ServiceType#getUpdateSequence <em>Update Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Update Sequence</em>' attribute.
	 * @see #getUpdateSequence()
	 * @generated
	 */
    void setUpdateSequence(String value);

    /**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * The default value is <code>"1.0.0"</code>.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #isSetVersion()
	 * @see #unsetVersion()
	 * @see #setVersion(String)
	 * @see net.opengis.wcs10.Wcs10Package#getServiceType_Version()
	 * @model default="1.0.0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='version'"
	 * @generated
	 */
    String getVersion();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.ServiceType#getVersion <em>Version</em>}' attribute.
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
	 * Unsets the value of the '{@link net.opengis.wcs10.ServiceType#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
    void unsetVersion();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.ServiceType#getVersion <em>Version</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Version</em>' attribute is set.
	 * @see #unsetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
    boolean isSetVersion();

} // ServiceType
