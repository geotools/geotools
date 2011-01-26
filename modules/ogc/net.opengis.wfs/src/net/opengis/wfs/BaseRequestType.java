/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Base Request Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             XML encoded WFS operation request base, for all operations
 *             except GetCapabilities.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.BaseRequestType#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs.BaseRequestType#getService <em>Service</em>}</li>
 *   <li>{@link net.opengis.wfs.BaseRequestType#getVersion <em>Version</em>}</li>
 *   <li>{@link net.opengis.wfs.BaseRequestType#getBaseUrl <em>Base Url</em>}</li>
 *   <li>{@link net.opengis.wfs.BaseRequestType#getProvidedVersion <em>Provided Version</em>}</li>
 *   <li>{@link net.opengis.wfs.BaseRequestType#getExtendedProperties <em>Extended Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getBaseRequestType()
 * @model abstract="true"
 *        extendedMetaData="name='BaseRequestType' kind='empty'"
 * @generated
 */
public interface BaseRequestType extends EObject {
	/**
	 * Returns the value of the '<em><b>Handle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                The handle attribute allows a client application
	 *                to assign a client-generated request identifier
	 *                to a WFS request.  The handle is included to
	 *                facilitate error reporting.  A WFS may report the
	 *                handle in an exception report to identify the
	 *                offending request or action.  If the handle is not
	 *                present, then the WFS may employ other means to
	 *                localize the error (e.g. line numbers).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Handle</em>' attribute.
	 * @see #setHandle(String)
	 * @see net.opengis.wfs.WfsPackage#getBaseRequestType_Handle()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='handle'"
	 * @generated
	 */
	String getHandle();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.BaseRequestType#getHandle <em>Handle</em>}' attribute.
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
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *               The service attribute is included to support service
	 *               endpoints that implement more than one OGC service.
	 *               For example, a single CGI that implements WMS, WFS
	 *               and WCS services.
	 *               The endpoint can inspect the value of this attribute
	 *               to figure out which service should process the request.
	 *               The value WFS indicates that a web feature service should
	 *               process the request.
	 *               This parameter is somewhat redundant in the XML encoding
	 *               since the request namespace can be used to determine
	 *               which service should process any give request.  For example,
	 *               wfs:GetCapabilities and easily be distinguished from
	 *               wcs:GetCapabilities using the namespaces.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Service</em>' attribute.
	 * @see #isSetService()
	 * @see #unsetService()
	 * @see #setService(String)
	 * @see net.opengis.wfs.WfsPackage#getBaseRequestType_Service()
	 * @model default="WFS" unique="false" unsettable="true" dataType="net.opengis.wfs.ServiceType"
	 *        extendedMetaData="kind='attribute' name='service'"
	 * @generated
	 */
	String getService();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.BaseRequestType#getService <em>Service</em>}' attribute.
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
	 * Unsets the value of the '{@link net.opengis.wfs.BaseRequestType#getService <em>Service</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetService()
	 * @see #getService()
	 * @see #setService(String)
	 * @generated
	 */
	void unsetService();

	/**
	 * Returns whether the value of the '{@link net.opengis.wfs.BaseRequestType#getService <em>Service</em>}' attribute is set.
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
	 * The default value is <code>"1.1.0"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                The version attribute is used to indicate the version of the
	 *                WFS specification that a request conforms to.  All requests in
	 *                this schema conform to V1.1 of the WFS specification.
	 *                For WFS implementations that support more than one version of
	 *                a WFS sepcification ... if the version attribute is not
	 *                specified then the service should assume that the request
	 *                conforms to greatest available specification version.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #isSetVersion()
	 * @see #unsetVersion()
	 * @see #setVersion(String)
	 * @see net.opengis.wfs.WfsPackage#getBaseRequestType_Version()
	 * @model default="1.1.0" unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='version'"
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link net.opengis.wfs.BaseRequestType#getVersion <em>Version</em>}' attribute.
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
	 * Unsets the value of the '{@link net.opengis.wfs.BaseRequestType#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
	void unsetVersion();

	/**
	 * Returns whether the value of the '{@link net.opengis.wfs.BaseRequestType#getVersion <em>Version</em>}' attribute is set.
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
	 * Returns the value of the '<em><b>Base Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Base Url</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Url</em>' attribute.
	 * @see #setBaseUrl(String)
	 * @see net.opengis.wfs.WfsPackage#getBaseRequestType_BaseUrl()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
    String getBaseUrl();

    /**
	 * Sets the value of the '{@link net.opengis.wfs.BaseRequestType#getBaseUrl <em>Base Url</em>}' attribute.
	 * <!-- begin-user-doc --> 
     * The base url, though not an attribute
     * declared in the schema, is a legacy one added because we need to
     * associate the request url being made by the client with the request
     * object. The reason being that the request object is the only object that
     * makes its way through the entire dispatch chain. 
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Url</em>' attribute.
	 * @see #getBaseUrl()
	 * @generated
	 */
    void setBaseUrl(String value);
    
    /**
     * Provided version of service processing the request.
     * @model
     */
    String getProvidedVersion();

    /**
	 * Sets the value of the '{@link net.opengis.wfs.BaseRequestType#getProvidedVersion <em>Provided Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Provided Version</em>' attribute.
	 * @see #getProvidedVersion()
	 * @generated
	 */
    void setProvidedVersion(String value);

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
	 * Sets the value of the '{@link net.opengis.wfs.BaseRequestType#getExtendedProperties <em>Extended Properties</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extended Properties</em>' attribute.
	 * @see #getExtendedProperties()
	 * @generated
	 */
	void setExtendedProperties(Map value);

} // BaseRequestType
