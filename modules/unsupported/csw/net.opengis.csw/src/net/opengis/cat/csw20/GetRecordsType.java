/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

import java.math.BigInteger;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Records Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *          The principal means of searching the catalogue. The matching
 *          catalogue entries may be included with the response. The client
 *          may assign a requestId (absolute URI). A distributed search is
 *          performed if the DistributedSearch element is present and the
 *          catalogue is a member of a federation. Profiles may allow
 *          alternative query expressions.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.GetRecordsType#getDistributedSearch <em>Distributed Search</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordsType#getResponseHandler <em>Response Handler</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordsType#getAbstractQueryGroup <em>Abstract Query Group</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordsType#getAbstractQuery <em>Abstract Query</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordsType#getAny <em>Any</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordsType#getMaxRecords <em>Max Records</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordsType#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordsType#getOutputSchema <em>Output Schema</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordsType#getRequestId <em>Request Id</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordsType#getResultType <em>Result Type</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordsType#getStartPosition <em>Start Position</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsType()
 * @model extendedMetaData="name='GetRecordsType' kind='elementOnly'"
 * @generated
 */
public interface GetRecordsType extends RequestBaseType {
    /**
     * Returns the value of the '<em><b>Distributed Search</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Distributed Search</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Distributed Search</em>' containment reference.
     * @see #setDistributedSearch(DistributedSearchType)
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsType_DistributedSearch()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='DistributedSearch' namespace='##targetNamespace'"
     * @generated
     */
    DistributedSearchType getDistributedSearch();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetRecordsType#getDistributedSearch <em>Distributed Search</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Distributed Search</em>' containment reference.
     * @see #getDistributedSearch()
     * @generated
     */
    void setDistributedSearch(DistributedSearchType value);

    /**
     * Returns the value of the '<em><b>Response Handler</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Response Handler</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Response Handler</em>' attribute.
     * @see #setResponseHandler(String)
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsType_ResponseHandler()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='ResponseHandler' namespace='##targetNamespace'"
     * @generated
     */
    String getResponseHandler();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetRecordsType#getResponseHandler <em>Response Handler</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Response Handler</em>' attribute.
     * @see #getResponseHandler()
     * @generated
     */
    void setResponseHandler(String value);

    /**
     * Returns the value of the '<em><b>Abstract Query Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Query Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Query Group</em>' attribute list.
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsType_AbstractQueryGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='AbstractQuery:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getAbstractQueryGroup();

    /**
     * Returns the value of the '<em><b>Abstract Query</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Query</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Query</em>' containment reference.
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsType_AbstractQuery()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AbstractQuery' namespace='##targetNamespace' group='AbstractQuery:group'"
     * @generated
     */
    AbstractQueryType getAbstractQuery();

    /**
     * Returns the value of the '<em><b>Any</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Any</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Any</em>' attribute list.
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsType_Any()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='elementWildcard' wildcards='##other' name=':6' processing='strict'"
     * @generated
     */
    FeatureMap getAny();

    /**
     * Returns the value of the '<em><b>Max Records</b></em>' attribute.
     * The default value is <code>"10"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Max Records</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Max Records</em>' attribute.
     * @see #isSetMaxRecords()
     * @see #unsetMaxRecords()
     * @see #setMaxRecords(BigInteger)
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsType_MaxRecords()
     * @model default="10" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger"
     *        extendedMetaData="kind='attribute' name='maxRecords'"
     * @generated
     */
    BigInteger getMaxRecords();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetRecordsType#getMaxRecords <em>Max Records</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Max Records</em>' attribute.
     * @see #isSetMaxRecords()
     * @see #unsetMaxRecords()
     * @see #getMaxRecords()
     * @generated
     */
    void setMaxRecords(BigInteger value);

    /**
     * Unsets the value of the '{@link net.opengis.cat.csw20.GetRecordsType#getMaxRecords <em>Max Records</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetMaxRecords()
     * @see #getMaxRecords()
     * @see #setMaxRecords(BigInteger)
     * @generated
     */
    void unsetMaxRecords();

    /**
     * Returns whether the value of the '{@link net.opengis.cat.csw20.GetRecordsType#getMaxRecords <em>Max Records</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Max Records</em>' attribute is set.
     * @see #unsetMaxRecords()
     * @see #getMaxRecords()
     * @see #setMaxRecords(BigInteger)
     * @generated
     */
    boolean isSetMaxRecords();

    /**
     * Returns the value of the '<em><b>Output Format</b></em>' attribute.
     * The default value is <code>"application/xml"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Output Format</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Output Format</em>' attribute.
     * @see #isSetOutputFormat()
     * @see #unsetOutputFormat()
     * @see #setOutputFormat(String)
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsType_OutputFormat()
     * @model default="application/xml" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='outputFormat'"
     * @generated
     */
    String getOutputFormat();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetRecordsType#getOutputFormat <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Output Format</em>' attribute.
     * @see #isSetOutputFormat()
     * @see #unsetOutputFormat()
     * @see #getOutputFormat()
     * @generated
     */
    void setOutputFormat(String value);

    /**
     * Unsets the value of the '{@link net.opengis.cat.csw20.GetRecordsType#getOutputFormat <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetOutputFormat()
     * @see #getOutputFormat()
     * @see #setOutputFormat(String)
     * @generated
     */
    void unsetOutputFormat();

    /**
     * Returns whether the value of the '{@link net.opengis.cat.csw20.GetRecordsType#getOutputFormat <em>Output Format</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Output Format</em>' attribute is set.
     * @see #unsetOutputFormat()
     * @see #getOutputFormat()
     * @see #setOutputFormat(String)
     * @generated
     */
    boolean isSetOutputFormat();

    /**
     * Returns the value of the '<em><b>Output Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Output Schema</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Output Schema</em>' attribute.
     * @see #setOutputSchema(String)
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsType_OutputSchema()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='outputSchema'"
     * @generated
     */
    String getOutputSchema();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetRecordsType#getOutputSchema <em>Output Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Output Schema</em>' attribute.
     * @see #getOutputSchema()
     * @generated
     */
    void setOutputSchema(String value);

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
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsType_RequestId()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='requestId'"
     * @generated
     */
    String getRequestId();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetRecordsType#getRequestId <em>Request Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Request Id</em>' attribute.
     * @see #getRequestId()
     * @generated
     */
    void setRequestId(String value);

    /**
     * Returns the value of the '<em><b>Result Type</b></em>' attribute.
     * The default value is <code>"hits"</code>.
     * The literals are from the enumeration {@link net.opengis.cat.csw20.ResultType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Result Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Result Type</em>' attribute.
     * @see net.opengis.cat.csw20.ResultType
     * @see #isSetResultType()
     * @see #unsetResultType()
     * @see #setResultType(ResultType)
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsType_ResultType()
     * @model default="hits" unsettable="true"
     *        extendedMetaData="kind='attribute' name='resultType'"
     * @generated
     */
    ResultType getResultType();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetRecordsType#getResultType <em>Result Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Result Type</em>' attribute.
     * @see net.opengis.cat.csw20.ResultType
     * @see #isSetResultType()
     * @see #unsetResultType()
     * @see #getResultType()
     * @generated
     */
    void setResultType(ResultType value);

    /**
     * Unsets the value of the '{@link net.opengis.cat.csw20.GetRecordsType#getResultType <em>Result Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetResultType()
     * @see #getResultType()
     * @see #setResultType(ResultType)
     * @generated
     */
    void unsetResultType();

    /**
     * Returns whether the value of the '{@link net.opengis.cat.csw20.GetRecordsType#getResultType <em>Result Type</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Result Type</em>' attribute is set.
     * @see #unsetResultType()
     * @see #getResultType()
     * @see #setResultType(ResultType)
     * @generated
     */
    boolean isSetResultType();

    /**
     * Returns the value of the '<em><b>Start Position</b></em>' attribute.
     * The default value is <code>"1"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Start Position</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Start Position</em>' attribute.
     * @see #isSetStartPosition()
     * @see #unsetStartPosition()
     * @see #setStartPosition(BigInteger)
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordsType_StartPosition()
     * @model default="1" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='attribute' name='startPosition'"
     * @generated
     */
    BigInteger getStartPosition();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetRecordsType#getStartPosition <em>Start Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Start Position</em>' attribute.
     * @see #isSetStartPosition()
     * @see #unsetStartPosition()
     * @see #getStartPosition()
     * @generated
     */
    void setStartPosition(BigInteger value);

    /**
     * Unsets the value of the '{@link net.opengis.cat.csw20.GetRecordsType#getStartPosition <em>Start Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetStartPosition()
     * @see #getStartPosition()
     * @see #setStartPosition(BigInteger)
     * @generated
     */
    void unsetStartPosition();

    /**
     * Returns whether the value of the '{@link net.opengis.cat.csw20.GetRecordsType#getStartPosition <em>Start Position</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Start Position</em>' attribute is set.
     * @see #unsetStartPosition()
     * @see #getStartPosition()
     * @see #setStartPosition(BigInteger)
     * @generated
     */
    boolean isSetStartPosition();

} // GetRecordsType
