/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *           Request to a WFS to perform the GetCapabilities operation.
 *           This operation allows a client to retrieve a Capabilities
 *           XML document providing metadata for the specific WFS server.
 * 
 *           The GetCapapbilities element is used to request that a Web Feature
 *           Service generate an XML document describing the organization
 *           providing the service, the WFS operations that the service
 *           supports, a list of feature types that the service can operate
 *           on and list of filtering capabilities that the service support.
 *           Such an XML document is called a capabilities document.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.GetCapabilitiesType#getService <em>Service</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getGetCapabilitiesType()
 * @model extendedMetaData="name='GetCapabilitiesType' kind='elementOnly'"
 * @generated
 */
public interface GetCapabilitiesType extends net.opengis.ows10.GetCapabilitiesType {
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
     * @see net.opengis.wfs.WfsPackage#getGetCapabilitiesType_Service()
     * @model default="WFS" unique="false" unsettable="true" dataType="net.opengis.wfs.ServiceType_1"
     *        extendedMetaData="kind='attribute' name='service'"
     * @generated
     */
	String getService();

	/**
     * Sets the value of the '{@link net.opengis.wfs.GetCapabilitiesType#getService <em>Service</em>}' attribute.
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
     * Unsets the value of the '{@link net.opengis.wfs.GetCapabilitiesType#getService <em>Service</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #isSetService()
     * @see #getService()
     * @see #setService(String)
     * @generated
     */
	void unsetService();

	/**
     * Returns whether the value of the '{@link net.opengis.wfs.GetCapabilitiesType#getService <em>Service</em>}' attribute is set.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return whether the value of the '<em>Service</em>' attribute is set.
     * @see #unsetService()
     * @see #getService()
     * @see #setService(String)
     * @generated
     */
	boolean isSetService();

} // GetCapabilitiesType
