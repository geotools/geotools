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
 * A representation of the model object '<em><b>WSDL Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.WSDLType#getHref <em>Href</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getWSDLType()
 * @model extendedMetaData="name='WSDL_._type' kind='empty'"
 * @generated
 */
public interface WSDLType extends EObject {
    /**
     * Returns the value of the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The URL from which the WSDL document can be retrieved.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Href</em>' attribute.
     * @see #setHref(String)
     * @see net.opengis.wps10.Wps10Package#getWSDLType_Href()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='attribute' name='href' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    String getHref();

    /**
     * Sets the value of the '{@link net.opengis.wps10.WSDLType#getHref <em>Href</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Href</em>' attribute.
     * @see #getHref()
     * @generated
     */
    void setHref(String value);

} // WSDLType
