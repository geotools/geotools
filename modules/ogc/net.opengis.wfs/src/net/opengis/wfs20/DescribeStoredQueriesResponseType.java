/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Describe Stored Queries Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.DescribeStoredQueriesResponseType#getStoredQueryDescription <em>Stored Query Description</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getDescribeStoredQueriesResponseType()
 * @model extendedMetaData="name='DescribeStoredQueriesResponseType' kind='elementOnly'"
 * @generated
 */
public interface DescribeStoredQueriesResponseType extends EObject {
    /**
     * Returns the value of the '<em><b>Stored Query Description</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.StoredQueryDescriptionType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Stored Query Description</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Stored Query Description</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getDescribeStoredQueriesResponseType_StoredQueryDescription()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='StoredQueryDescription' namespace='##targetNamespace'"
     * @generated
     */
    EList<StoredQueryDescriptionType> getStoredQueryDescription();

} // DescribeStoredQueriesResponseType
