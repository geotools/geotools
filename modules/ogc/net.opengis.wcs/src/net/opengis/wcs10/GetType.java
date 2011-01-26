/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.GetType#getOnlineResource <em>Online Resource</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getGetType()
 * @model extendedMetaData="name='Get_._type' kind='elementOnly'"
 * @generated
 */
public interface GetType extends EObject {
    /**
	 * Returns the value of the '<em><b>Online Resource</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Online Resource</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Online Resource</em>' containment reference.
	 * @see #setOnlineResource(OnlineResourceType)
	 * @see net.opengis.wcs10.Wcs10Package#getGetType_OnlineResource()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='OnlineResource' namespace='##targetNamespace'"
	 * @generated
	 */
    OnlineResourceType getOnlineResource();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.GetType#getOnlineResource <em>Online Resource</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Online Resource</em>' containment reference.
	 * @see #getOnlineResource()
	 * @generated
	 */
    void setOnlineResource(OnlineResourceType value);

} // GetType
