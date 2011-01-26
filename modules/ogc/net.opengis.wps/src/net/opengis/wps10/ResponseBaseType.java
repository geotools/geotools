/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Response Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * WPS operation response base, for all WPS operations except GetCapabilities.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ResponseBaseType#getLang <em>Lang</em>}</li>
 *   <li>{@link net.opengis.wps10.ResponseBaseType#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wps10.ResponseBaseType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getResponseBaseType()
 * @model extendedMetaData="name='ResponseBaseType' kind='empty'"
 * @generated
 */
public interface ResponseBaseType extends EObject {
    /**
     * Returns the value of the '<em><b>Lang</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * RFC 4646 language code of the human-readable text (e.g. "en-CA").
     * <!-- end-model-doc -->
     * @return the value of the '<em>Lang</em>' attribute.
     * @see #setLang(String)
     * @see net.opengis.wps10.Wps10Package#getResponseBaseType_Lang()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='lang' namespace='http://www.w3.org/XML/1998/namespace'"
     * @generated
     */
    String getLang();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ResponseBaseType#getLang <em>Lang</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lang</em>' attribute.
     * @see #getLang()
     * @generated
     */
    void setLang(String value);

    /**
     * Returns the value of the '<em><b>Service</b></em>' attribute.
     * The default value is <code>"WPS"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Service type identifier (WPS)
     * <!-- end-model-doc -->
     * @return the value of the '<em>Service</em>' attribute.
     * @see #isSetService()
     * @see #unsetService()
     * @see #setService(String)
     * @see net.opengis.wps10.Wps10Package#getResponseBaseType_Service()
     * @model default="WPS" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='service'"
     * @generated
     */
    String getService();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ResponseBaseType#getService <em>Service</em>}' attribute.
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
     * Unsets the value of the '{@link net.opengis.wps10.ResponseBaseType#getService <em>Service</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetService()
     * @see #getService()
     * @see #setService(String)
     * @generated
     */
    void unsetService();

    /**
     * Returns whether the value of the '{@link net.opengis.wps10.ResponseBaseType#getService <em>Service</em>}' attribute is set.
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
     * The default value is <code>"1.0.0"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Version of the WPS interface specification implemented by the server (1.0.0)
     * <!-- end-model-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #isSetVersion()
     * @see #unsetVersion()
     * @see #setVersion(String)
     * @see net.opengis.wps10.Wps10Package#getResponseBaseType_Version()
     * @model default="1.0.0" unsettable="true" dataType="net.opengis.ows11.VersionType1" required="true"
     *        extendedMetaData="kind='attribute' name='version'"
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ResponseBaseType#getVersion <em>Version</em>}' attribute.
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
     * Unsets the value of the '{@link net.opengis.wps10.ResponseBaseType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetVersion()
     * @see #getVersion()
     * @see #setVersion(String)
     * @generated
     */
    void unsetVersion();

    /**
     * Returns whether the value of the '{@link net.opengis.wps10.ResponseBaseType#getVersion <em>Version</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Version</em>' attribute is set.
     * @see #unsetVersion()
     * @see #getVersion()
     * @see #setVersion(String)
     * @generated
     */
    boolean isSetVersion();

} // ResponseBaseType
