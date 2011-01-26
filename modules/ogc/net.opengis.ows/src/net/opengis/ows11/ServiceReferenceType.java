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
 * A representation of the model object '<em><b>Service Reference Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Complete reference to a remote resource that needs to be retrieved from an OWS using an XML-encoded operation request. This element shall be used, within an InputData or Manifest element that is used for input data, when that input data needs to be retrieved from another web service using a XML-encoded OWS operation request. This element shall not be used for local payload input data or for requesting the resource from a web server using HTTP Get. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.ServiceReferenceType#getRequestMessage <em>Request Message</em>}</li>
 *   <li>{@link net.opengis.ows11.ServiceReferenceType#getRequestMessageReference <em>Request Message Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getServiceReferenceType()
 * @model extendedMetaData="name='ServiceReferenceType' kind='elementOnly'"
 * @generated
 */
public interface ServiceReferenceType extends ReferenceType {
    /**
     * Returns the value of the '<em><b>Request Message</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The XML-encoded operation request message to be sent to request this input data from another web server using HTTP Post. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Request Message</em>' containment reference.
     * @see #setRequestMessage(EObject)
     * @see net.opengis.ows11.Ows11Package#getServiceReferenceType_RequestMessage()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='RequestMessage' namespace='##targetNamespace'"
     * @generated
     */
    EObject getRequestMessage();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ServiceReferenceType#getRequestMessage <em>Request Message</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Request Message</em>' containment reference.
     * @see #getRequestMessage()
     * @generated
     */
    void setRequestMessage(EObject value);

    /**
     * Returns the value of the '<em><b>Request Message Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to the XML-encoded operation request message to be sent to request this input data from another web server using HTTP Post. The referenced message shall be attached to the same message (using the cid scheme), or be accessible using a URL. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Request Message Reference</em>' attribute.
     * @see #setRequestMessageReference(String)
     * @see net.opengis.ows11.Ows11Package#getServiceReferenceType_RequestMessageReference()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='RequestMessageReference' namespace='##targetNamespace'"
     * @generated
     */
    String getRequestMessageReference();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ServiceReferenceType#getRequestMessageReference <em>Request Message Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Request Message Reference</em>' attribute.
     * @see #getRequestMessageReference()
     * @generated
     */
    void setRequestMessageReference(String value);

} // ServiceReferenceType
