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
 * A representation of the model object '<em><b>DCP Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.DCPType#getHTTP <em>HTTP</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getDCPType()
 * @model extendedMetaData="name='DCP_._type' kind='elementOnly'"
 * @generated
 */
public interface DCPType extends EObject {
    /**
     * Returns the value of the '<em><b>HTTP</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Connect point URLs for the HTTP Distributed Computing Platform (DCP). Normally, only one Get and/or one Post is included in this element. More than one Get and/or Post is allowed to support including alternative URLs for uses such as load balancing or backup. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>HTTP</em>' containment reference.
     * @see #setHTTP(HTTPType)
     * @see net.opengis.ows11.Ows11Package#getDCPType_HTTP()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='HTTP' namespace='##targetNamespace'"
     * @generated
     */
    HTTPType getHTTP();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DCPType#getHTTP <em>HTTP</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>HTTP</em>' containment reference.
     * @see #getHTTP()
     * @generated
     */
    void setHTTP(HTTPType value);

} // DCPType
