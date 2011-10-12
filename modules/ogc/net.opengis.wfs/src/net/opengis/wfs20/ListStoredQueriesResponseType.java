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
 * A representation of the model object '<em><b>List Stored Queries Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.ListStoredQueriesResponseType#getStoredQuery <em>Stored Query</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getListStoredQueriesResponseType()
 * @model extendedMetaData="name='ListStoredQueriesResponseType' kind='elementOnly'"
 * @generated
 */
public interface ListStoredQueriesResponseType extends EObject {
    /**
     * Returns the value of the '<em><b>Stored Query</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.StoredQueryListItemType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Stored Query</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Stored Query</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getListStoredQueriesResponseType_StoredQuery()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='StoredQuery' namespace='##targetNamespace'"
     * @generated
     */
    EList<StoredQueryListItemType> getStoredQuery();

} // ListStoredQueriesResponseType
