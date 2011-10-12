/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Base Request Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.BaseRequestType#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs20.BaseRequestType#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wfs20.BaseRequestType#getVersion <em>Version</em>}</li>
 *   <li>{@link net.opengis.wfs20.BaseRequestType#getBaseUrl <em>Base Url</em>}</li>
 *   <li>{@link net.opengis.wfs20.BaseRequestType#getExtendedProperties <em>Extended Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getBaseRequestType()
 * @model abstract="true"
 *        extendedMetaData="name='BaseRequestType' kind='empty'"
 * @generated
 */
public interface BaseRequestType extends EObject {
    /**
     * Returns the value of the '<em><b>Handle</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Handle</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Handle</em>' attribute.
     * @see #setHandle(String)
     * @see net.opengis.wfs20.Wfs20Package#getBaseRequestType_Handle()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='handle'"
     * @generated
     */
    String getHandle();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.BaseRequestType#getHandle <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Handle</em>' attribute.
     * @see #getHandle()
     * @generated
     */
    void setHandle(String value);

    /**
     * Returns the value of the '<em><b>Service</b></em>' attribute.
     * The default value is <code>"WFS"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Service</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Service</em>' attribute.
     * @see #isSetService()
     * @see #unsetService()
     * @see #setService(String)
     * @see net.opengis.wfs20.Wfs20Package#getBaseRequestType_Service()
     * @model default="WFS" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='service'"
     * @generated
     */
    String getService();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.BaseRequestType#getService <em>Service</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service</em>' attribute.
     * @see #isSetService()
     * @see #unsetService()
     * @see #getService()
     * @generated
     */
    void setService(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.BaseRequestType#getService <em>Service</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetService()
     * @see #getService()
     * @see #setService(String)
     * @generated
     */
    void unsetService();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.BaseRequestType#getService <em>Service</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Service</em>' attribute is set.
     * @see #unsetService()
     * @see #getService()
     * @see #setService(String)
     * @generated
     */
    boolean isSetService();

    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * The default value is <code>"2.0.0"</code>.
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
     * @see net.opengis.wfs20.Wfs20Package#getBaseRequestType_Version()
     * @model default="2.0.0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='version'"
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.BaseRequestType#getVersion <em>Version</em>}' attribute.
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
     * Unsets the value of the '{@link net.opengis.wfs20.BaseRequestType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetVersion()
     * @see #getVersion()
     * @see #setVersion(String)
     * @generated
     */
    void unsetVersion();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.BaseRequestType#getVersion <em>Version</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Version</em>' attribute is set.
     * @see #unsetVersion()
     * @see #getVersion()
     * @see #setVersion(String)
     * @generated
     */
    boolean isSetVersion();
    
    /**
     * <!-- begin-user-doc -->
     * The base url the request originated from.
     * <p>
     * While not part of the xml schema we add this property after the fact to allow applications
     * to associate a base url with this request object.
     * </p>
     * <!-- end-user-doc -->
     * 
     * @model
     */
    String getBaseUrl();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.BaseRequestType#getBaseUrl <em>Base Url</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Base Url</em>' attribute.
     * @see #getBaseUrl()
     * @generated
     */
    void setBaseUrl(String value);

    /**
     * Extended model properties.
     * <p>
     * This map allows client to store additional properties with the this
     * request object, properties that are not part of the model proper.
     * </p>
     * 
     * @model
     */
    Map getExtendedProperties();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.BaseRequestType#getExtendedProperties <em>Extended Properties</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Extended Properties</em>' attribute.
     * @see #getExtendedProperties()
     * @generated
     */
    void setExtendedProperties(Map value);

} // BaseRequestType
