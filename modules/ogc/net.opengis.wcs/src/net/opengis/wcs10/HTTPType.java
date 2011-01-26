/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>HTTP Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.HTTPType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.wcs10.HTTPType#getGet <em>Get</em>}</li>
 *   <li>{@link net.opengis.wcs10.HTTPType#getPost <em>Post</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getHTTPType()
 * @model extendedMetaData="name='HTTP_._type' kind='elementOnly'"
 * @generated
 */
public interface HTTPType extends EObject {
    /**
	 * Returns the value of the '<em><b>Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' attribute list.
	 * @see net.opengis.wcs10.Wcs10Package#getHTTPType_Group()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:0'"
	 * @generated
	 */
    FeatureMap getGroup();

    /**
	 * Returns the value of the '<em><b>Get</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wcs10.GetType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Get</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Get</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getHTTPType_Get()
	 * @model type="net.opengis.wcs10.GetType" containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Get' namespace='##targetNamespace' group='#group:0'"
	 * @generated
	 */
    EList getGet();

    /**
	 * Returns the value of the '<em><b>Post</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wcs10.PostType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Post</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Post</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getHTTPType_Post()
	 * @model type="net.opengis.wcs10.PostType" containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='Post' namespace='##targetNamespace' group='#group:0'"
	 * @generated
	 */
    EList getPost();

} // HTTPType
