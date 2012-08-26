/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Records Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             The response message for a GetRecords request. Some or all of the
 *             matching records may be included as children of the SearchResults
 *             element. The RequestId is only included if the client specified it.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.GetRecordsResponseType#getRequestId <em>Request Id</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordsResponseType#getSearchStatus <em>Search Status</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordsResponseType#getSearchResults <em>Search Results</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordsResponseType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsResponseType()
 * @model extendedMetaData="name='GetRecordsResponseType' kind='elementOnly'"
 * @generated
 */
public interface GetRecordsResponseType extends EObject {
    /**
     * Returns the value of the '<em><b>Request Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Request Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Request Id</em>' attribute.
     * @see #setRequestId(String)
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsResponseType_RequestId()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='RequestId' namespace='##targetNamespace'"
     * @generated
     */
    String getRequestId();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetRecordsResponseType#getRequestId <em>Request Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Request Id</em>' attribute.
     * @see #getRequestId()
     * @generated
     */
    void setRequestId(String value);

    /**
     * Returns the value of the '<em><b>Search Status</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Search Status</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Search Status</em>' containment reference.
     * @see #setSearchStatus(RequestStatusType)
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsResponseType_SearchStatus()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='SearchStatus' namespace='##targetNamespace'"
     * @generated
     */
    RequestStatusType getSearchStatus();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetRecordsResponseType#getSearchStatus <em>Search Status</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Search Status</em>' containment reference.
     * @see #getSearchStatus()
     * @generated
     */
    void setSearchStatus(RequestStatusType value);

    /**
     * Returns the value of the '<em><b>Search Results</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Search Results</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Search Results</em>' containment reference.
     * @see #setSearchResults(SearchResultsType)
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsResponseType_SearchResults()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='SearchResults' namespace='##targetNamespace'"
     * @generated
     */
    SearchResultsType getSearchResults();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetRecordsResponseType#getSearchResults <em>Search Results</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Search Results</em>' containment reference.
     * @see #getSearchResults()
     * @generated
     */
    void setSearchResults(SearchResultsType value);

    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #setVersion(String)
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsResponseType_Version()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='version'"
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetRecordsResponseType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

} // GetRecordsResponseType
