/**
 */
package net.opengis.ows20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * XML encoded GetCapabilities operation request. This
 *       operation allows clients to retrieve service metadata about a specific
 *       service instance. In this XML encoding, no "request" parameter is
 *       included, since the element name specifies the specific operation. This
 *       base type shall be extended by each specific OWS to include the
 *       additional required "service" attribute, with the correct value for that
 *       OWS.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows20.GetCapabilitiesType#getAcceptVersions <em>Accept Versions</em>}</li>
 *   <li>{@link net.opengis.ows20.GetCapabilitiesType#getSections <em>Sections</em>}</li>
 *   <li>{@link net.opengis.ows20.GetCapabilitiesType#getAcceptFormats <em>Accept Formats</em>}</li>
 *   <li>{@link net.opengis.ows20.GetCapabilitiesType#getAcceptLanguages <em>Accept Languages</em>}</li>
 *   <li>{@link net.opengis.ows20.GetCapabilitiesType#getUpdateSequence <em>Update Sequence</em>}</li>
 *   <li>{@link net.opengis.ows20.GetCapabilitiesType#getBaseUrl <em>Base Url</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows20.Ows20Package#getGetCapabilitiesType()
 * @model extendedMetaData="name='GetCapabilitiesType' kind='elementOnly'"
 * @generated
 */
public interface GetCapabilitiesType extends EObject {
    /**
     * Returns the value of the '<em><b>Accept Versions</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * When omitted, server shall return latest supported
     *           version.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Accept Versions</em>' containment reference.
     * @see #setAcceptVersions(AcceptVersionsType)
     * @see net.opengis.ows20.Ows20Package#getGetCapabilitiesType_AcceptVersions()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='AcceptVersions' namespace='##targetNamespace'"
     * @generated
     */
    AcceptVersionsType getAcceptVersions();

    /**
     * Sets the value of the '{@link net.opengis.ows20.GetCapabilitiesType#getAcceptVersions <em>Accept Versions</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Accept Versions</em>' containment reference.
     * @see #getAcceptVersions()
     * @generated
     */
    void setAcceptVersions(AcceptVersionsType value);

    /**
     * Returns the value of the '<em><b>Sections</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * When omitted or not supported by server, server shall
     *           return complete service metadata (Capabilities)
     *           document.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Sections</em>' containment reference.
     * @see #setSections(SectionsType)
     * @see net.opengis.ows20.Ows20Package#getGetCapabilitiesType_Sections()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Sections' namespace='##targetNamespace'"
     * @generated
     */
    SectionsType getSections();

    /**
     * Sets the value of the '{@link net.opengis.ows20.GetCapabilitiesType#getSections <em>Sections</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sections</em>' containment reference.
     * @see #getSections()
     * @generated
     */
    void setSections(SectionsType value);

    /**
     * Returns the value of the '<em><b>Accept Formats</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * When omitted or not supported by server, server shall
     *           return service metadata document using the MIME type
     *           "text/xml".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Accept Formats</em>' containment reference.
     * @see #setAcceptFormats(AcceptFormatsType)
     * @see net.opengis.ows20.Ows20Package#getGetCapabilitiesType_AcceptFormats()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='AcceptFormats' namespace='##targetNamespace'"
     * @generated
     */
    AcceptFormatsType getAcceptFormats();

    /**
     * Sets the value of the '{@link net.opengis.ows20.GetCapabilitiesType#getAcceptFormats <em>Accept Formats</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Accept Formats</em>' containment reference.
     * @see #getAcceptFormats()
     * @generated
     */
    void setAcceptFormats(AcceptFormatsType value);

    /**
     * Returns the value of the '<em><b>Accept Languages</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Ordered list of languages desired by the client for
     *           all human readable text in the response, in order of preference. For
     *           every element, the first matching language available from the server
     *           shall be present in the response.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Accept Languages</em>' containment reference.
     * @see #setAcceptLanguages(AcceptLanguagesType)
     * @see net.opengis.ows20.Ows20Package#getGetCapabilitiesType_AcceptLanguages()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='AcceptLanguages' namespace='##targetNamespace'"
     * @generated
     */
    AcceptLanguagesType getAcceptLanguages();

    /**
     * Sets the value of the '{@link net.opengis.ows20.GetCapabilitiesType#getAcceptLanguages <em>Accept Languages</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Accept Languages</em>' containment reference.
     * @see #getAcceptLanguages()
     * @generated
     */
    void setAcceptLanguages(AcceptLanguagesType value);

    /**
     * Returns the value of the '<em><b>Update Sequence</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * When omitted or not supported by server, server shall
     *         return latest complete service metadata document.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Update Sequence</em>' attribute.
     * @see #setUpdateSequence(String)
     * @see net.opengis.ows20.Ows20Package#getGetCapabilitiesType_UpdateSequence()
     * @model dataType="net.opengis.ows20.UpdateSequenceType"
     *        extendedMetaData="kind='attribute' name='updateSequence'"
     * @generated
     */
    String getUpdateSequence();

    /**
     * Sets the value of the '{@link net.opengis.ows20.GetCapabilitiesType#getUpdateSequence <em>Update Sequence</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Update Sequence</em>' attribute.
     * @see #getUpdateSequence()
     * @generated
     */
    void setUpdateSequence(String value);
    
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
     * @see net.opengis.ows11.Ows11Package#getGetCapabilitiesType_BaseUrl()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     */
    String getBaseUrl();

    /**
     * Sets the value of the '{@link net.opengis.ows20.GetCapabilitiesType#getBaseUrl <em>Base Url</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Base Url</em>' attribute.
     * @see #getBaseUrl()
     * @generated
     */
    void setBaseUrl(String value);

} // GetCapabilitiesType
