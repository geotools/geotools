/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import java.math.BigInteger;
import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature Collection Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.FeatureCollectionType#getAdditionalObjects <em>Additional Objects</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureCollectionType#getTruncatedResponse <em>Truncated Response</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureCollectionType#getLockId <em>Lock Id</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureCollectionType#getNext <em>Next</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureCollectionType#getNumberMatched <em>Number Matched</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureCollectionType#getNumberReturned <em>Number Returned</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureCollectionType#getPrevious <em>Previous</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureCollectionType#getTimeStamp <em>Time Stamp</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getFeatureCollectionType()
 * @model extendedMetaData="name='FeatureCollectionType' kind='elementOnly'"
 * @generated
 */
public interface FeatureCollectionType extends SimpleFeatureCollectionType {
    /**
     * Returns the value of the '<em><b>Additional Objects</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Additional Objects</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Additional Objects</em>' containment reference.
     * @see #setAdditionalObjects(AdditionalObjectsType)
     * @see net.opengis.wfs20.Wfs20Package#getFeatureCollectionType_AdditionalObjects()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='additionalObjects' namespace='##targetNamespace'"
     * @generated
     */
    AdditionalObjectsType getAdditionalObjects();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.FeatureCollectionType#getAdditionalObjects <em>Additional Objects</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Additional Objects</em>' containment reference.
     * @see #getAdditionalObjects()
     * @generated
     */
    void setAdditionalObjects(AdditionalObjectsType value);

    /**
     * Returns the value of the '<em><b>Truncated Response</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Truncated Response</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Truncated Response</em>' containment reference.
     * @see #setTruncatedResponse(TruncatedResponseType)
     * @see net.opengis.wfs20.Wfs20Package#getFeatureCollectionType_TruncatedResponse()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='truncatedResponse' namespace='##targetNamespace'"
     * @generated
     */
    TruncatedResponseType getTruncatedResponse();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.FeatureCollectionType#getTruncatedResponse <em>Truncated Response</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Truncated Response</em>' containment reference.
     * @see #getTruncatedResponse()
     * @generated
     */
    void setTruncatedResponse(TruncatedResponseType value);

    /**
     * Returns the value of the '<em><b>Lock Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Lock Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Lock Id</em>' attribute.
     * @see #setLockId(String)
     * @see net.opengis.wfs20.Wfs20Package#getFeatureCollectionType_LockId()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='lockId'"
     * @generated
     */
    String getLockId();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.FeatureCollectionType#getLockId <em>Lock Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lock Id</em>' attribute.
     * @see #getLockId()
     * @generated
     */
    void setLockId(String value);

    /**
     * Returns the value of the '<em><b>Next</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Next</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Next</em>' attribute.
     * @see #setNext(String)
     * @see net.opengis.wfs20.Wfs20Package#getFeatureCollectionType_Next()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='next'"
     * @generated
     */
    String getNext();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.FeatureCollectionType#getNext <em>Next</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Next</em>' attribute.
     * @see #getNext()
     * @generated
     */
    void setNext(String value);

    /**
     * Returns the value of the '<em><b>Number Matched</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Number Matched</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Number Matched</em>' attribute.
     * @see #setNumberMatched(Object)
     * @see net.opengis.wfs20.Wfs20Package#getFeatureCollectionType_NumberMatched()
     * @model dataType="net.opengis.wfs20.NonNegativeIntegerOrUnknown" required="true"
     *        extendedMetaData="kind='attribute' name='numberMatched'"
     * @generated
     */
    Object getNumberMatched();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.FeatureCollectionType#getNumberMatched <em>Number Matched</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Number Matched</em>' attribute.
     * @see #getNumberMatched()
     * @generated
     */
    void setNumberMatched(Object value);

    /**
     * Returns the value of the '<em><b>Number Returned</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Number Returned</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Number Returned</em>' attribute.
     * @see #setNumberReturned(BigInteger)
     * @see net.opengis.wfs20.Wfs20Package#getFeatureCollectionType_NumberReturned()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger" required="true"
     *        extendedMetaData="kind='attribute' name='numberReturned'"
     * @generated
     */
    BigInteger getNumberReturned();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.FeatureCollectionType#getNumberReturned <em>Number Returned</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Number Returned</em>' attribute.
     * @see #getNumberReturned()
     * @generated
     */
    void setNumberReturned(BigInteger value);

    /**
     * Returns the value of the '<em><b>Previous</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Previous</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Previous</em>' attribute.
     * @see #setPrevious(String)
     * @see net.opengis.wfs20.Wfs20Package#getFeatureCollectionType_Previous()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='previous'"
     * @generated
     */
    String getPrevious();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.FeatureCollectionType#getPrevious <em>Previous</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Previous</em>' attribute.
     * @see #getPrevious()
     * @generated
     */
    void setPrevious(String value);

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
     * @see net.opengis.wfs20.Wfs20Package#getFeatureCollectionType_TimeStamp()
     * @model 
     */
    Calendar getTimeStamp();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.FeatureCollectionType#getTimeStamp <em>Time Stamp</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Time Stamp</em>' attribute.
     * @see #getTimeStamp()
     * @generated
     */
    void setTimeStamp(Calendar value);

} // FeatureCollectionType
