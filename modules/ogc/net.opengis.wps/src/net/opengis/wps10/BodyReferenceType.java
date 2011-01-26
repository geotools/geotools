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
 * A representation of the model object '<em><b>Body Reference Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.BodyReferenceType#getHref <em>Href</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getBodyReferenceType()
 * @model extendedMetaData="name='BodyReference_._type' kind='empty'"
 * @generated
 */
public interface BodyReferenceType extends EObject {
    /**
     * Returns the value of the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to a remote document to be used as the body of the an HTTP POST request message. This attribute shall contain a URL from which this input can be electronically retrieved.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Href</em>' attribute.
     * @see #setHref(String)
     * @see net.opengis.wps10.Wps10Package#getBodyReferenceType_Href()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='attribute' name='href' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    String getHref();

    /**
     * Sets the value of the '{@link net.opengis.wps10.BodyReferenceType#getHref <em>Href</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Href</em>' attribute.
     * @see #getHref()
     * @generated
     */
    void setHref(String value);

} // BodyReferenceType
