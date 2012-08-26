/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

import javax.xml.datatype.Duration;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Harvest Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *          Requests that the catalogue attempt to harvest a resource from some
 *          network location identified by the source URL.
 * 
 *          Source          - a URL from which the resource is retrieved
 *          ResourceType    - normally a URI that specifies the type of the resource
 *                            (DCMES v1.1) being harvested if it is known.
 *          ResourceFormat  - a media type indicating the format of the
 *                            resource being harvested.  The default is
 *                            "application/xml".
 *          ResponseHandler - a reference to some endpoint to which the
 *                            response shall be forwarded when the
 *                            harvest operation has been completed
 *          HarvestInterval - an interval expressed using the ISO 8601 syntax;
 *                            it specifies the interval between harvest
 *                            attempts (e.g., P6M indicates an interval of
 *                            six months).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.HarvestType#getSource <em>Source</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.HarvestType#getResourceType <em>Resource Type</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.HarvestType#getResourceFormat <em>Resource Format</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.HarvestType#getHarvestInterval <em>Harvest Interval</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.HarvestType#getResponseHandler <em>Response Handler</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getHarvestType()
 * @model extendedMetaData="name='HarvestType' kind='elementOnly'"
 * @generated
 */
public interface HarvestType extends RequestBaseType {
    /**
     * Returns the value of the '<em><b>Source</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source</em>' attribute.
     * @see #setSource(String)
     * @see net.opengis.cat.csw20.Csw20Package#getHarvestType_Source()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='element' name='Source' namespace='##targetNamespace'"
     * @generated
     */
    String getSource();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.HarvestType#getSource <em>Source</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source</em>' attribute.
     * @see #getSource()
     * @generated
     */
    void setSource(String value);

    /**
     * Returns the value of the '<em><b>Resource Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Resource Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Resource Type</em>' attribute.
     * @see #setResourceType(String)
     * @see net.opengis.cat.csw20.Csw20Package#getHarvestType_ResourceType()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='ResourceType' namespace='##targetNamespace'"
     * @generated
     */
    String getResourceType();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.HarvestType#getResourceType <em>Resource Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Resource Type</em>' attribute.
     * @see #getResourceType()
     * @generated
     */
    void setResourceType(String value);

    /**
     * Returns the value of the '<em><b>Resource Format</b></em>' attribute.
     * The default value is <code>"application/xml"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Resource Format</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Resource Format</em>' attribute.
     * @see #isSetResourceFormat()
     * @see #unsetResourceFormat()
     * @see #setResourceFormat(String)
     * @see net.opengis.cat.csw20.Csw20Package#getHarvestType_ResourceFormat()
     * @model default="application/xml" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='ResourceFormat' namespace='##targetNamespace'"
     * @generated
     */
    String getResourceFormat();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.HarvestType#getResourceFormat <em>Resource Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Resource Format</em>' attribute.
     * @see #isSetResourceFormat()
     * @see #unsetResourceFormat()
     * @see #getResourceFormat()
     * @generated
     */
    void setResourceFormat(String value);

    /**
     * Unsets the value of the '{@link net.opengis.cat.csw20.HarvestType#getResourceFormat <em>Resource Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetResourceFormat()
     * @see #getResourceFormat()
     * @see #setResourceFormat(String)
     * @generated
     */
    void unsetResourceFormat();

    /**
     * Returns whether the value of the '{@link net.opengis.cat.csw20.HarvestType#getResourceFormat <em>Resource Format</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Resource Format</em>' attribute is set.
     * @see #unsetResourceFormat()
     * @see #getResourceFormat()
     * @see #setResourceFormat(String)
     * @generated
     */
    boolean isSetResourceFormat();

    /**
     * Returns the value of the '<em><b>Harvest Interval</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Harvest Interval</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Harvest Interval</em>' attribute.
     * @see #setHarvestInterval(Duration)
     * @see net.opengis.cat.csw20.Csw20Package#getHarvestType_HarvestInterval()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Duration"
     *        extendedMetaData="kind='element' name='HarvestInterval' namespace='##targetNamespace'"
     * @generated
     */
    Duration getHarvestInterval();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.HarvestType#getHarvestInterval <em>Harvest Interval</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Harvest Interval</em>' attribute.
     * @see #getHarvestInterval()
     * @generated
     */
    void setHarvestInterval(Duration value);

    /**
     * Returns the value of the '<em><b>Response Handler</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Response Handler</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Response Handler</em>' attribute.
     * @see #setResponseHandler(String)
     * @see net.opengis.cat.csw20.Csw20Package#getHarvestType_ResponseHandler()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='ResponseHandler' namespace='##targetNamespace'"
     * @generated
     */
    String getResponseHandler();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.HarvestType#getResponseHandler <em>Response Handler</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Response Handler</em>' attribute.
     * @see #getResponseHandler()
     * @generated
     */
    void setResponseHandler(String value);

} // HarvestType
