/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows10;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>DCP Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows10.DCPType#getHTTP <em>HTTP</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows10.Ows10Package#getDCPType()
 * @model extendedMetaData="name='DCP_._type' kind='elementOnly'"
 * @generated
 */
public interface DCPType extends EObject {
	/**
	 * Returns the value of the '<em><b>HTTP</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>HTTP</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>HTTP</em>' containment reference.
	 * @see #setHTTP(HTTPType)
	 * @see net.opengis.ows10.Ows10Package#getDCPType_HTTP()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='HTTP' namespace='##targetNamespace'"
	 * @generated
	 */
	HTTPType getHTTP();

	/**
	 * Sets the value of the '{@link net.opengis.ows10.DCPType#getHTTP <em>HTTP</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>HTTP</em>' containment reference.
	 * @see #getHTTP()
	 * @generated
	 */
	void setHTTP(HTTPType value);

} // DCPType
