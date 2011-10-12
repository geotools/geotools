/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Create Stored Query Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.CreateStoredQueryType#getStoredQueryDefinition <em>Stored Query Definition</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getCreateStoredQueryType()
 * @model extendedMetaData="name='CreateStoredQueryType' kind='elementOnly'"
 * @generated
 */
public interface CreateStoredQueryType extends BaseRequestType {
    /**
     * Returns the value of the '<em><b>Stored Query Definition</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.StoredQueryDescriptionType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Stored Query Definition</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Stored Query Definition</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getCreateStoredQueryType_StoredQueryDefinition()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='StoredQueryDefinition' namespace='##targetNamespace'"
     * @generated
     */
    EList<StoredQueryDescriptionType> getStoredQueryDefinition();

} // CreateStoredQueryType
