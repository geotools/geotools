/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getAbstractTransactionAction <em>Abstract Transaction Action</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getAdditionalObjects <em>Additional Objects</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getAdditionalValues <em>Additional Values</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getBoundedBy <em>Bounded By</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getCreateStoredQuery <em>Create Stored Query</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getCreateStoredQueryResponse <em>Create Stored Query Response</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getDelete <em>Delete</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getDescribeFeatureType <em>Describe Feature Type</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getDescribeStoredQueries <em>Describe Stored Queries</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getDescribeStoredQueriesResponse <em>Describe Stored Queries Response</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getDropStoredQuery <em>Drop Stored Query</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getDropStoredQueryResponse <em>Drop Stored Query Response</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getElement <em>Element</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getFeatureCollection <em>Feature Collection</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getSimpleFeatureCollection <em>Simple Feature Collection</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getFeatureTypeList <em>Feature Type List</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getGetFeature <em>Get Feature</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getGetFeatureWithLock <em>Get Feature With Lock</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getGetPropertyValue <em>Get Property Value</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getInsert <em>Insert</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getListStoredQueries <em>List Stored Queries</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getListStoredQueriesResponse <em>List Stored Queries Response</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getLockFeature <em>Lock Feature</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getLockFeatureResponse <em>Lock Feature Response</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getMember <em>Member</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getNative <em>Native</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getProperty <em>Property</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getPropertyName <em>Property Name</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getQuery <em>Query</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getReplace <em>Replace</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getStoredQuery <em>Stored Query</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getTransaction <em>Transaction</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getTransactionResponse <em>Transaction Response</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getTruncatedResponse <em>Truncated Response</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getTuple <em>Tuple</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getUpdate <em>Update</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getValueCollection <em>Value Collection</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getValueList <em>Value List</em>}</li>
 *   <li>{@link net.opengis.wfs20.DocumentRoot#getWFSCapabilities <em>WFS Capabilities</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XMLNS Prefix Map</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>XMLNS Prefix Map</em>' map.
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_XMLNSPrefixMap()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>" transient="true"
     *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
     * @generated
     */
    EMap<String, String> getXMLNSPrefixMap();

    /**
     * Returns the value of the '<em><b>XSI Schema Location</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XSI Schema Location</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>XSI Schema Location</em>' map.
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_XSISchemaLocation()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>" transient="true"
     *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
     * @generated
     */
    EMap<String, String> getXSISchemaLocation();

    /**
     * Returns the value of the '<em><b>Abstract</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract</em>' containment reference.
     * @see #setAbstract(AbstractType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_Abstract()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Abstract' namespace='##targetNamespace'"
     * @generated
     */
    AbstractType getAbstract();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getAbstract <em>Abstract</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Abstract</em>' containment reference.
     * @see #getAbstract()
     * @generated
     */
    void setAbstract(AbstractType value);

    /**
     * Returns the value of the '<em><b>Abstract Transaction Action</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Transaction Action</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Transaction Action</em>' containment reference.
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_AbstractTransactionAction()
     * @model containment="true" upper="-2" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AbstractTransactionAction' namespace='##targetNamespace'"
     * @generated
     */
    AbstractTransactionActionType getAbstractTransactionAction();

    /**
     * Returns the value of the '<em><b>Additional Objects</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Additional Objects</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Additional Objects</em>' containment reference.
     * @see #setAdditionalObjects(AdditionalObjectsType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_AdditionalObjects()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='additionalObjects' namespace='##targetNamespace'"
     * @generated
     */
    AdditionalObjectsType getAdditionalObjects();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getAdditionalObjects <em>Additional Objects</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Additional Objects</em>' containment reference.
     * @see #getAdditionalObjects()
     * @generated
     */
    void setAdditionalObjects(AdditionalObjectsType value);

    /**
     * Returns the value of the '<em><b>Additional Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Additional Values</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Additional Values</em>' containment reference.
     * @see #setAdditionalValues(AdditionalValuesType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_AdditionalValues()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='additionalValues' namespace='##targetNamespace'"
     * @generated
     */
    AdditionalValuesType getAdditionalValues();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getAdditionalValues <em>Additional Values</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Additional Values</em>' containment reference.
     * @see #getAdditionalValues()
     * @generated
     */
    void setAdditionalValues(AdditionalValuesType value);

    /**
     * Returns the value of the '<em><b>Bounded By</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bounded By</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bounded By</em>' containment reference.
     * @see #setBoundedBy(EnvelopePropertyType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_BoundedBy()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='boundedBy' namespace='##targetNamespace'"
     * @generated
     */
    EnvelopePropertyType getBoundedBy();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getBoundedBy <em>Bounded By</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bounded By</em>' containment reference.
     * @see #getBoundedBy()
     * @generated
     */
    void setBoundedBy(EnvelopePropertyType value);

    /**
     * Returns the value of the '<em><b>Create Stored Query</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Create Stored Query</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Create Stored Query</em>' containment reference.
     * @see #setCreateStoredQuery(CreateStoredQueryType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_CreateStoredQuery()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CreateStoredQuery' namespace='##targetNamespace'"
     * @generated
     */
    CreateStoredQueryType getCreateStoredQuery();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getCreateStoredQuery <em>Create Stored Query</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Create Stored Query</em>' containment reference.
     * @see #getCreateStoredQuery()
     * @generated
     */
    void setCreateStoredQuery(CreateStoredQueryType value);

    /**
     * Returns the value of the '<em><b>Create Stored Query Response</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Create Stored Query Response</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Create Stored Query Response</em>' containment reference.
     * @see #setCreateStoredQueryResponse(CreateStoredQueryResponseType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_CreateStoredQueryResponse()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CreateStoredQueryResponse' namespace='##targetNamespace'"
     * @generated
     */
    CreateStoredQueryResponseType getCreateStoredQueryResponse();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getCreateStoredQueryResponse <em>Create Stored Query Response</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Create Stored Query Response</em>' containment reference.
     * @see #getCreateStoredQueryResponse()
     * @generated
     */
    void setCreateStoredQueryResponse(CreateStoredQueryResponseType value);

    /**
     * Returns the value of the '<em><b>Delete</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Delete</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Delete</em>' containment reference.
     * @see #setDelete(DeleteType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_Delete()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Delete' namespace='##targetNamespace' affiliation='AbstractTransactionAction'"
     * @generated
     */
    DeleteType getDelete();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getDelete <em>Delete</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Delete</em>' containment reference.
     * @see #getDelete()
     * @generated
     */
    void setDelete(DeleteType value);

    /**
     * Returns the value of the '<em><b>Describe Feature Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Describe Feature Type</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Describe Feature Type</em>' containment reference.
     * @see #setDescribeFeatureType(DescribeFeatureTypeType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_DescribeFeatureType()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DescribeFeatureType' namespace='##targetNamespace'"
     * @generated
     */
    DescribeFeatureTypeType getDescribeFeatureType();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getDescribeFeatureType <em>Describe Feature Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Describe Feature Type</em>' containment reference.
     * @see #getDescribeFeatureType()
     * @generated
     */
    void setDescribeFeatureType(DescribeFeatureTypeType value);

    /**
     * Returns the value of the '<em><b>Describe Stored Queries</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Describe Stored Queries</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Describe Stored Queries</em>' containment reference.
     * @see #setDescribeStoredQueries(DescribeStoredQueriesType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_DescribeStoredQueries()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DescribeStoredQueries' namespace='##targetNamespace'"
     * @generated
     */
    DescribeStoredQueriesType getDescribeStoredQueries();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getDescribeStoredQueries <em>Describe Stored Queries</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Describe Stored Queries</em>' containment reference.
     * @see #getDescribeStoredQueries()
     * @generated
     */
    void setDescribeStoredQueries(DescribeStoredQueriesType value);

    /**
     * Returns the value of the '<em><b>Describe Stored Queries Response</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Describe Stored Queries Response</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Describe Stored Queries Response</em>' containment reference.
     * @see #setDescribeStoredQueriesResponse(DescribeStoredQueriesResponseType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_DescribeStoredQueriesResponse()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DescribeStoredQueriesResponse' namespace='##targetNamespace'"
     * @generated
     */
    DescribeStoredQueriesResponseType getDescribeStoredQueriesResponse();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getDescribeStoredQueriesResponse <em>Describe Stored Queries Response</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Describe Stored Queries Response</em>' containment reference.
     * @see #getDescribeStoredQueriesResponse()
     * @generated
     */
    void setDescribeStoredQueriesResponse(DescribeStoredQueriesResponseType value);

    /**
     * Returns the value of the '<em><b>Drop Stored Query</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Drop Stored Query</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Drop Stored Query</em>' containment reference.
     * @see #setDropStoredQuery(DropStoredQueryType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_DropStoredQuery()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DropStoredQuery' namespace='##targetNamespace'"
     * @generated
     */
    DropStoredQueryType getDropStoredQuery();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getDropStoredQuery <em>Drop Stored Query</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Drop Stored Query</em>' containment reference.
     * @see #getDropStoredQuery()
     * @generated
     */
    void setDropStoredQuery(DropStoredQueryType value);

    /**
     * Returns the value of the '<em><b>Drop Stored Query Response</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Drop Stored Query Response</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Drop Stored Query Response</em>' containment reference.
     * @see #setDropStoredQueryResponse(ExecutionStatusType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_DropStoredQueryResponse()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DropStoredQueryResponse' namespace='##targetNamespace'"
     * @generated
     */
    ExecutionStatusType getDropStoredQueryResponse();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getDropStoredQueryResponse <em>Drop Stored Query Response</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Drop Stored Query Response</em>' containment reference.
     * @see #getDropStoredQueryResponse()
     * @generated
     */
    void setDropStoredQueryResponse(ExecutionStatusType value);

    /**
     * Returns the value of the '<em><b>Element</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Element</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Element</em>' containment reference.
     * @see #setElement(ElementType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_Element()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Element' namespace='##targetNamespace'"
     * @generated
     */
    ElementType getElement();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getElement <em>Element</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Element</em>' containment reference.
     * @see #getElement()
     * @generated
     */
    void setElement(ElementType value);

    /**
     * Returns the value of the '<em><b>Feature Collection</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature Collection</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Collection</em>' containment reference.
     * @see #setFeatureCollection(FeatureCollectionType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_FeatureCollection()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='FeatureCollection' namespace='##targetNamespace' affiliation='SimpleFeatureCollection'"
     * @generated
     */
    FeatureCollectionType getFeatureCollection();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getFeatureCollection <em>Feature Collection</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Collection</em>' containment reference.
     * @see #getFeatureCollection()
     * @generated
     */
    void setFeatureCollection(FeatureCollectionType value);

    /**
     * Returns the value of the '<em><b>Simple Feature Collection</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Simple Feature Collection</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Simple Feature Collection</em>' containment reference.
     * @see #setSimpleFeatureCollection(SimpleFeatureCollectionType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_SimpleFeatureCollection()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='SimpleFeatureCollection' namespace='##targetNamespace'"
     * @generated
     */
    SimpleFeatureCollectionType getSimpleFeatureCollection();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getSimpleFeatureCollection <em>Simple Feature Collection</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Simple Feature Collection</em>' containment reference.
     * @see #getSimpleFeatureCollection()
     * @generated
     */
    void setSimpleFeatureCollection(SimpleFeatureCollectionType value);

    /**
     * Returns the value of the '<em><b>Feature Type List</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature Type List</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Type List</em>' containment reference.
     * @see #setFeatureTypeList(FeatureTypeListType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_FeatureTypeList()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='FeatureTypeList' namespace='##targetNamespace'"
     * @generated
     */
    FeatureTypeListType getFeatureTypeList();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getFeatureTypeList <em>Feature Type List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Type List</em>' containment reference.
     * @see #getFeatureTypeList()
     * @generated
     */
    void setFeatureTypeList(FeatureTypeListType value);

    /**
     * Returns the value of the '<em><b>Get Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Get Capabilities</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Get Capabilities</em>' containment reference.
     * @see #setGetCapabilities(GetCapabilitiesType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_GetCapabilities()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetCapabilities' namespace='##targetNamespace'"
     * @generated
     */
    GetCapabilitiesType getGetCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getGetCapabilities <em>Get Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Capabilities</em>' containment reference.
     * @see #getGetCapabilities()
     * @generated
     */
    void setGetCapabilities(GetCapabilitiesType value);

    /**
     * Returns the value of the '<em><b>Get Feature</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Get Feature</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Get Feature</em>' containment reference.
     * @see #setGetFeature(GetFeatureType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_GetFeature()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetFeature' namespace='##targetNamespace'"
     * @generated
     */
    GetFeatureType getGetFeature();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getGetFeature <em>Get Feature</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Feature</em>' containment reference.
     * @see #getGetFeature()
     * @generated
     */
    void setGetFeature(GetFeatureType value);

    /**
     * Returns the value of the '<em><b>Get Feature With Lock</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Get Feature With Lock</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Get Feature With Lock</em>' containment reference.
     * @see #setGetFeatureWithLock(GetFeatureWithLockType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_GetFeatureWithLock()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetFeatureWithLock' namespace='##targetNamespace'"
     * @generated
     */
    GetFeatureWithLockType getGetFeatureWithLock();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getGetFeatureWithLock <em>Get Feature With Lock</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Feature With Lock</em>' containment reference.
     * @see #getGetFeatureWithLock()
     * @generated
     */
    void setGetFeatureWithLock(GetFeatureWithLockType value);

    /**
     * Returns the value of the '<em><b>Get Property Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Get Property Value</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Get Property Value</em>' containment reference.
     * @see #setGetPropertyValue(GetPropertyValueType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_GetPropertyValue()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetPropertyValue' namespace='##targetNamespace'"
     * @generated
     */
    GetPropertyValueType getGetPropertyValue();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getGetPropertyValue <em>Get Property Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Property Value</em>' containment reference.
     * @see #getGetPropertyValue()
     * @generated
     */
    void setGetPropertyValue(GetPropertyValueType value);

    /**
     * Returns the value of the '<em><b>Insert</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Insert</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Insert</em>' containment reference.
     * @see #setInsert(InsertType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_Insert()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Insert' namespace='##targetNamespace' affiliation='AbstractTransactionAction'"
     * @generated
     */
    InsertType getInsert();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getInsert <em>Insert</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Insert</em>' containment reference.
     * @see #getInsert()
     * @generated
     */
    void setInsert(InsertType value);

    /**
     * Returns the value of the '<em><b>List Stored Queries</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>List Stored Queries</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>List Stored Queries</em>' containment reference.
     * @see #setListStoredQueries(ListStoredQueriesType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_ListStoredQueries()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ListStoredQueries' namespace='##targetNamespace'"
     * @generated
     */
    ListStoredQueriesType getListStoredQueries();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getListStoredQueries <em>List Stored Queries</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>List Stored Queries</em>' containment reference.
     * @see #getListStoredQueries()
     * @generated
     */
    void setListStoredQueries(ListStoredQueriesType value);

    /**
     * Returns the value of the '<em><b>List Stored Queries Response</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>List Stored Queries Response</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>List Stored Queries Response</em>' containment reference.
     * @see #setListStoredQueriesResponse(ListStoredQueriesResponseType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_ListStoredQueriesResponse()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ListStoredQueriesResponse' namespace='##targetNamespace'"
     * @generated
     */
    ListStoredQueriesResponseType getListStoredQueriesResponse();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getListStoredQueriesResponse <em>List Stored Queries Response</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>List Stored Queries Response</em>' containment reference.
     * @see #getListStoredQueriesResponse()
     * @generated
     */
    void setListStoredQueriesResponse(ListStoredQueriesResponseType value);

    /**
     * Returns the value of the '<em><b>Lock Feature</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Lock Feature</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Lock Feature</em>' containment reference.
     * @see #setLockFeature(LockFeatureType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_LockFeature()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='LockFeature' namespace='##targetNamespace'"
     * @generated
     */
    LockFeatureType getLockFeature();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getLockFeature <em>Lock Feature</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lock Feature</em>' containment reference.
     * @see #getLockFeature()
     * @generated
     */
    void setLockFeature(LockFeatureType value);

    /**
     * Returns the value of the '<em><b>Lock Feature Response</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Lock Feature Response</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Lock Feature Response</em>' containment reference.
     * @see #setLockFeatureResponse(LockFeatureResponseType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_LockFeatureResponse()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='LockFeatureResponse' namespace='##targetNamespace'"
     * @generated
     */
    LockFeatureResponseType getLockFeatureResponse();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getLockFeatureResponse <em>Lock Feature Response</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lock Feature Response</em>' containment reference.
     * @see #getLockFeatureResponse()
     * @generated
     */
    void setLockFeatureResponse(LockFeatureResponseType value);

    /**
     * Returns the value of the '<em><b>Member</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Member</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Member</em>' containment reference.
     * @see #setMember(MemberPropertyType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_Member()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='member' namespace='##targetNamespace'"
     * @generated
     */
    MemberPropertyType getMember();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getMember <em>Member</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Member</em>' containment reference.
     * @see #getMember()
     * @generated
     */
    void setMember(MemberPropertyType value);

    /**
     * Returns the value of the '<em><b>Native</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Native</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Native</em>' containment reference.
     * @see #setNative(NativeType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_Native()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Native' namespace='##targetNamespace' affiliation='AbstractTransactionAction'"
     * @generated
     */
    NativeType getNative();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getNative <em>Native</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Native</em>' containment reference.
     * @see #getNative()
     * @generated
     */
    void setNative(NativeType value);

    /**
     * Returns the value of the '<em><b>Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Property</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Property</em>' containment reference.
     * @see #setProperty(PropertyType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_Property()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Property' namespace='##targetNamespace'"
     * @generated
     */
    PropertyType getProperty();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getProperty <em>Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Property</em>' containment reference.
     * @see #getProperty()
     * @generated
     */
    void setProperty(PropertyType value);

    /**
     * Returns the value of the '<em><b>Property Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Property Name</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Property Name</em>' containment reference.
     * @see #setPropertyName(PropertyNameType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_PropertyName()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='PropertyName' namespace='##targetNamespace' affiliation='http://www.opengis.net/fes/2.0#AbstractProjectionClause'"
     * @generated
     */
    PropertyNameType getPropertyName();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getPropertyName <em>Property Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Property Name</em>' containment reference.
     * @see #getPropertyName()
     * @generated
     */
    void setPropertyName(PropertyNameType value);

    /**
     * Returns the value of the '<em><b>Query</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Query</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Query</em>' containment reference.
     * @see #setQuery(QueryType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_Query()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Query' namespace='##targetNamespace' affiliation='http://www.opengis.net/fes/2.0#AbstractAdhocQueryExpression'"
     * @generated
     */
    QueryType getQuery();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getQuery <em>Query</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Query</em>' containment reference.
     * @see #getQuery()
     * @generated
     */
    void setQuery(QueryType value);

    /**
     * Returns the value of the '<em><b>Replace</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Replace</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Replace</em>' containment reference.
     * @see #setReplace(ReplaceType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_Replace()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Replace' namespace='##targetNamespace' affiliation='AbstractTransactionAction'"
     * @generated
     */
    ReplaceType getReplace();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getReplace <em>Replace</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Replace</em>' containment reference.
     * @see #getReplace()
     * @generated
     */
    void setReplace(ReplaceType value);

    /**
     * Returns the value of the '<em><b>Stored Query</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Stored Query</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Stored Query</em>' containment reference.
     * @see #setStoredQuery(StoredQueryType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_StoredQuery()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='StoredQuery' namespace='##targetNamespace' affiliation='http://www.opengis.net/fes/2.0#AbstractQueryExpression'"
     * @generated
     */
    StoredQueryType getStoredQuery();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getStoredQuery <em>Stored Query</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Stored Query</em>' containment reference.
     * @see #getStoredQuery()
     * @generated
     */
    void setStoredQuery(StoredQueryType value);

    /**
     * Returns the value of the '<em><b>Title</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Title</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Title</em>' containment reference.
     * @see #setTitle(TitleType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_Title()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Title' namespace='##targetNamespace'"
     * @generated
     */
    TitleType getTitle();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getTitle <em>Title</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Title</em>' containment reference.
     * @see #getTitle()
     * @generated
     */
    void setTitle(TitleType value);

    /**
     * Returns the value of the '<em><b>Transaction</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Transaction</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Transaction</em>' containment reference.
     * @see #setTransaction(TransactionType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_Transaction()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Transaction' namespace='##targetNamespace'"
     * @generated
     */
    TransactionType getTransaction();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getTransaction <em>Transaction</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Transaction</em>' containment reference.
     * @see #getTransaction()
     * @generated
     */
    void setTransaction(TransactionType value);

    /**
     * Returns the value of the '<em><b>Transaction Response</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Transaction Response</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Transaction Response</em>' containment reference.
     * @see #setTransactionResponse(TransactionResponseType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_TransactionResponse()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='TransactionResponse' namespace='##targetNamespace'"
     * @generated
     */
    TransactionResponseType getTransactionResponse();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getTransactionResponse <em>Transaction Response</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Transaction Response</em>' containment reference.
     * @see #getTransactionResponse()
     * @generated
     */
    void setTransactionResponse(TransactionResponseType value);

    /**
     * Returns the value of the '<em><b>Truncated Response</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Truncated Response</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Truncated Response</em>' containment reference.
     * @see #setTruncatedResponse(TruncatedResponseType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_TruncatedResponse()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='truncatedResponse' namespace='##targetNamespace'"
     * @generated
     */
    TruncatedResponseType getTruncatedResponse();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getTruncatedResponse <em>Truncated Response</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Truncated Response</em>' containment reference.
     * @see #getTruncatedResponse()
     * @generated
     */
    void setTruncatedResponse(TruncatedResponseType value);

    /**
     * Returns the value of the '<em><b>Tuple</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Tuple</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Tuple</em>' containment reference.
     * @see #setTuple(TupleType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_Tuple()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Tuple' namespace='##targetNamespace'"
     * @generated
     */
    TupleType getTuple();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getTuple <em>Tuple</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tuple</em>' containment reference.
     * @see #getTuple()
     * @generated
     */
    void setTuple(TupleType value);

    /**
     * Returns the value of the '<em><b>Update</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Update</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Update</em>' containment reference.
     * @see #setUpdate(UpdateType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_Update()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Update' namespace='##targetNamespace' affiliation='AbstractTransactionAction'"
     * @generated
     */
    UpdateType getUpdate();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getUpdate <em>Update</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Update</em>' containment reference.
     * @see #getUpdate()
     * @generated
     */
    void setUpdate(UpdateType value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' containment reference.
     * @see #setValue(EObject)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_Value()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Value' namespace='##targetNamespace'"
     * @generated
     */
    EObject getValue();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getValue <em>Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' containment reference.
     * @see #getValue()
     * @generated
     */
    void setValue(EObject value);

    /**
     * Returns the value of the '<em><b>Value Collection</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value Collection</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value Collection</em>' containment reference.
     * @see #setValueCollection(ValueCollectionType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_ValueCollection()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ValueCollection' namespace='##targetNamespace'"
     * @generated
     */
    ValueCollectionType getValueCollection();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getValueCollection <em>Value Collection</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value Collection</em>' containment reference.
     * @see #getValueCollection()
     * @generated
     */
    void setValueCollection(ValueCollectionType value);

    /**
     * Returns the value of the '<em><b>Value List</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value List</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value List</em>' containment reference.
     * @see #setValueList(ValueListType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_ValueList()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ValueList' namespace='##targetNamespace'"
     * @generated
     */
    ValueListType getValueList();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getValueList <em>Value List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value List</em>' containment reference.
     * @see #getValueList()
     * @generated
     */
    void setValueList(ValueListType value);

    /**
     * Returns the value of the '<em><b>WFS Capabilities</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>WFS Capabilities</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>WFS Capabilities</em>' containment reference.
     * @see #setWFSCapabilities(WFSCapabilitiesType)
     * @see net.opengis.wfs20.Wfs20Package#getDocumentRoot_WFSCapabilities()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='WFS_Capabilities' namespace='##targetNamespace'"
     * @generated
     */
    WFSCapabilitiesType getWFSCapabilities();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DocumentRoot#getWFSCapabilities <em>WFS Capabilities</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>WFS Capabilities</em>' containment reference.
     * @see #getWFSCapabilities()
     * @generated
     */
    void setWFSCapabilities(WFSCapabilitiesType value);

} // DocumentRoot
