/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import java.net.URI;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Describe Stored Queries Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.DescribeStoredQueriesType#getStoredQueryId <em>Stored Query Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getDescribeStoredQueriesType()
 * @model extendedMetaData="name='DescribeStoredQueriesType' kind='elementOnly'"
 * @generated
 */
public interface DescribeStoredQueriesType extends BaseRequestType {
    /**
     * Returns the value of the '<em><b>Stored Query Id</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Stored Query Id</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Stored Query Id</em>' attribute list.
     * @see net.opengis.wfs20.Wfs20Package#getDescribeStoredQueriesType_StoredQueryId()
     * @model 
     */
    EList<URI> getStoredQueryId();

} // DescribeStoredQueriesType
