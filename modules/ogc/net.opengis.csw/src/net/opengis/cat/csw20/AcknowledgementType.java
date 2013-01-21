/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;
import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Acknowledgement Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This is a general acknowledgement response message for all requests
 *          that may be processed in an asynchronous manner.
 *          EchoedRequest - Echoes the submitted request message
 *          RequestId     - identifier for polling purposes (if no response
 *                          handler is available, or the URL scheme is
 *                          unsupported)
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.AcknowledgementType#getEchoedRequest <em>Echoed Request</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.AcknowledgementType#getRequestId <em>Request Id</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.AcknowledgementType#getTimeStamp <em>Time Stamp</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getAcknowledgementType()
 * @model extendedMetaData="name='AcknowledgementType' kind='elementOnly'"
 * @generated
 */
public interface AcknowledgementType extends EObject {
    /**
     * Returns the value of the '<em><b>Echoed Request</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Echoed Request</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Echoed Request</em>' containment reference.
     * @see #setEchoedRequest(EchoedRequestType)
     * @see net.opengis.cat.csw20.Csw20Package#getAcknowledgementType_EchoedRequest()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='EchoedRequest' namespace='##targetNamespace'"
     * @generated
     */
    EchoedRequestType getEchoedRequest();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.AcknowledgementType#getEchoedRequest <em>Echoed Request</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Echoed Request</em>' containment reference.
     * @see #getEchoedRequest()
     * @generated
     */
    void setEchoedRequest(EchoedRequestType value);

    /**
     * Returns the value of the '<em><b>Request Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Request Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Request Id</em>' attribute.
     * @see #setRequestId(String)
     * @see net.opengis.cat.csw20.Csw20Package#getAcknowledgementType_RequestId()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='RequestId' namespace='##targetNamespace'"
     * @generated
     */
    String getRequestId();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.AcknowledgementType#getRequestId <em>Request Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Request Id</em>' attribute.
     * @see #getRequestId()
     * @generated
     */
    void setRequestId(String value);

    /**
     * Returns the value of the '<em><b>Time Stamp</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Stamp</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Time Stamp</em>' attribute.
     * @see #setTimeStamp(XMLGregorianCalendar)
     * @see net.opengis.cat.csw20.Csw20Package#getAcknowledgementType_TimeStamp()
     * @model
     */
    Calendar getTimeStamp();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.AcknowledgementType#getTimeStamp <em>Time Stamp</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Stamp</em>' attribute.
     * @see #getTimeStamp()
     * @generated
     */
    void setTimeStamp(Calendar value);

    

} // AcknowledgementType
