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
 * A representation of the model object '<em><b>Online Resource Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Reference to on-line resource from which data can be obtained.
 * For OWS use in the service metadata document, the CI_OnlineResource class was XML encoded as the attributeGroup "xlink:simpleLink", as used in GML.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.OnlineResourceType#getHref <em>Href</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getOnlineResourceType()
 * @model extendedMetaData="name='OnlineResourceType' kind='empty'"
 * @generated
 */
public interface OnlineResourceType extends EObject {
	
	/**
	 * @model
	 */
	String getHref();

	/**
	 * Sets the value of the '{@link net.opengis.ows11.OnlineResourceType#getHref <em>Href</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Href</em>' attribute.
	 * @see #getHref()
	 * @generated
	 */
	void setHref(String value);
} // OnlineResourceType
