/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import java.math.BigInteger;
import java.util.Map;

import net.opengis.fes20.AbstractQueryExpressionType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Feature Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.GetFeatureType#getAbstractQueryExpressionGroup <em>Abstract Query Expression Group</em>}</li>
 *   <li>{@link net.opengis.wfs20.GetFeatureType#getAbstractQueryExpression <em>Abstract Query Expression</em>}</li>
 *   <li>{@link net.opengis.wfs20.GetFeatureType#getCount <em>Count</em>}</li>
 *   <li>{@link net.opengis.wfs20.GetFeatureType#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.wfs20.GetFeatureType#getResolve <em>Resolve</em>}</li>
 *   <li>{@link net.opengis.wfs20.GetFeatureType#getResolveDepth <em>Resolve Depth</em>}</li>
 *   <li>{@link net.opengis.wfs20.GetFeatureType#getResolveTimeout <em>Resolve Timeout</em>}</li>
 *   <li>{@link net.opengis.wfs20.GetFeatureType#getResultType <em>Result Type</em>}</li>
 *   <li>{@link net.opengis.wfs20.GetFeatureType#getStartIndex <em>Start Index</em>}</li>
 *   <li>{@link net.opengis.wfs20.GetFeatureType#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.wfs20.GetFeatureType#getFormatOptions <em>Format Options</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getGetFeatureType()
 * @model extendedMetaData="name='GetFeatureType' kind='elementOnly'"
 * @generated
 */
public interface GetFeatureType extends BaseRequestType {
    /**
     * Returns the value of the '<em><b>Abstract Query Expression Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Query Expression Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Query Expression Group</em>' attribute list.
     * @see net.opengis.wfs20.Wfs20Package#getGetFeatureType_AbstractQueryExpressionGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="true"
     *        extendedMetaData="kind='group' name='AbstractQueryExpression:group' namespace='http://www.opengis.net/fes/2.0'"
     * @generated
     */
    FeatureMap getAbstractQueryExpressionGroup();

    /**
     * Returns the value of the '<em><b>Abstract Query Expression</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.AbstractQueryExpressionType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract Query Expression</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract Query Expression</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getGetFeatureType_AbstractQueryExpression()
     * @model containment="true" required="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AbstractQueryExpression' namespace='http://www.opengis.net/fes/2.0' group='http://www.opengis.net/fes/2.0#AbstractQueryExpression:group'"
     * @generated
     */
    EList<AbstractQueryExpressionType> getAbstractQueryExpression();

    /**
     * Returns the value of the '<em><b>Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Count</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Count</em>' attribute.
     * @see #setCount(BigInteger)
     * @see net.opengis.wfs20.Wfs20Package#getGetFeatureType_Count()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger"
     *        extendedMetaData="kind='attribute' name='count'"
     * @generated
     */
    BigInteger getCount();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.GetFeatureType#getCount <em>Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Count</em>' attribute.
     * @see #getCount()
     * @generated
     */
    void setCount(BigInteger value);

    /**
     * Returns the value of the '<em><b>Output Format</b></em>' attribute.
     * The default value is <code>"application/gml+xml; version=3.2"</code>.
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
     * @see net.opengis.wfs20.Wfs20Package#getGetFeatureType_OutputFormat()
     * @model default="application/gml+xml; version=3.2" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='outputFormat'"
     * @generated
     */
    String getOutputFormat();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.GetFeatureType#getOutputFormat <em>Output Format</em>}' attribute.
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
     * Unsets the value of the '{@link net.opengis.wfs20.GetFeatureType#getOutputFormat <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetOutputFormat()
     * @see #getOutputFormat()
     * @see #setOutputFormat(String)
     * @generated
     */
    void unsetOutputFormat();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.GetFeatureType#getOutputFormat <em>Output Format</em>}' attribute is set.
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
     * Returns the value of the '<em><b>Resolve</b></em>' attribute.
     * The default value is <code>"none"</code>.
     * The literals are from the enumeration {@link net.opengis.wfs20.ResolveValueType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Resolve</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Resolve</em>' attribute.
     * @see net.opengis.wfs20.ResolveValueType
     * @see #isSetResolve()
     * @see #unsetResolve()
     * @see #setResolve(ResolveValueType)
     * @see net.opengis.wfs20.Wfs20Package#getGetFeatureType_Resolve()
     * @model default="none" unsettable="true"
     *        extendedMetaData="kind='attribute' name='resolve'"
     * @generated
     */
    ResolveValueType getResolve();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.GetFeatureType#getResolve <em>Resolve</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Resolve</em>' attribute.
     * @see net.opengis.wfs20.ResolveValueType
     * @see #isSetResolve()
     * @see #unsetResolve()
     * @see #getResolve()
     * @generated
     */
    void setResolve(ResolveValueType value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.GetFeatureType#getResolve <em>Resolve</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetResolve()
     * @see #getResolve()
     * @see #setResolve(ResolveValueType)
     * @generated
     */
    void unsetResolve();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.GetFeatureType#getResolve <em>Resolve</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Resolve</em>' attribute is set.
     * @see #unsetResolve()
     * @see #getResolve()
     * @see #setResolve(ResolveValueType)
     * @generated
     */
    boolean isSetResolve();

    /**
     * Returns the value of the '<em><b>Resolve Depth</b></em>' attribute.
     * The default value is <code>"*"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Resolve Depth</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Resolve Depth</em>' attribute.
     * @see #isSetResolveDepth()
     * @see #unsetResolveDepth()
     * @see #setResolveDepth(Object)
     * @see net.opengis.wfs20.Wfs20Package#getGetFeatureType_ResolveDepth()
     * @model default="*" unsettable="true" dataType="net.opengis.wfs20.PositiveIntegerWithStar"
     *        extendedMetaData="kind='attribute' name='resolveDepth'"
     * @generated
     */
    Object getResolveDepth();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.GetFeatureType#getResolveDepth <em>Resolve Depth</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Resolve Depth</em>' attribute.
     * @see #isSetResolveDepth()
     * @see #unsetResolveDepth()
     * @see #getResolveDepth()
     * @generated
     */
    void setResolveDepth(Object value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.GetFeatureType#getResolveDepth <em>Resolve Depth</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetResolveDepth()
     * @see #getResolveDepth()
     * @see #setResolveDepth(Object)
     * @generated
     */
    void unsetResolveDepth();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.GetFeatureType#getResolveDepth <em>Resolve Depth</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Resolve Depth</em>' attribute is set.
     * @see #unsetResolveDepth()
     * @see #getResolveDepth()
     * @see #setResolveDepth(Object)
     * @generated
     */
    boolean isSetResolveDepth();

    /**
     * Returns the value of the '<em><b>Resolve Timeout</b></em>' attribute.
     * The default value is <code>"300"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Resolve Timeout</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Resolve Timeout</em>' attribute.
     * @see #isSetResolveTimeout()
     * @see #unsetResolveTimeout()
     * @see #setResolveTimeout(BigInteger)
     * @see net.opengis.wfs20.Wfs20Package#getGetFeatureType_ResolveTimeout()
     * @model default="300" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='attribute' name='resolveTimeout'"
     * @generated
     */
    BigInteger getResolveTimeout();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.GetFeatureType#getResolveTimeout <em>Resolve Timeout</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Resolve Timeout</em>' attribute.
     * @see #isSetResolveTimeout()
     * @see #unsetResolveTimeout()
     * @see #getResolveTimeout()
     * @generated
     */
    void setResolveTimeout(BigInteger value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.GetFeatureType#getResolveTimeout <em>Resolve Timeout</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetResolveTimeout()
     * @see #getResolveTimeout()
     * @see #setResolveTimeout(BigInteger)
     * @generated
     */
    void unsetResolveTimeout();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.GetFeatureType#getResolveTimeout <em>Resolve Timeout</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Resolve Timeout</em>' attribute is set.
     * @see #unsetResolveTimeout()
     * @see #getResolveTimeout()
     * @see #setResolveTimeout(BigInteger)
     * @generated
     */
    boolean isSetResolveTimeout();

    /**
     * Returns the value of the '<em><b>Result Type</b></em>' attribute.
     * The default value is <code>"results"</code>.
     * The literals are from the enumeration {@link net.opengis.wfs20.ResultTypeType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Result Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Result Type</em>' attribute.
     * @see net.opengis.wfs20.ResultTypeType
     * @see #isSetResultType()
     * @see #unsetResultType()
     * @see #setResultType(ResultTypeType)
     * @see net.opengis.wfs20.Wfs20Package#getGetFeatureType_ResultType()
     * @model default="results" unsettable="true"
     *        extendedMetaData="kind='attribute' name='resultType'"
     * @generated
     */
    ResultTypeType getResultType();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.GetFeatureType#getResultType <em>Result Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Result Type</em>' attribute.
     * @see net.opengis.wfs20.ResultTypeType
     * @see #isSetResultType()
     * @see #unsetResultType()
     * @see #getResultType()
     * @generated
     */
    void setResultType(ResultTypeType value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.GetFeatureType#getResultType <em>Result Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetResultType()
     * @see #getResultType()
     * @see #setResultType(ResultTypeType)
     * @generated
     */
    void unsetResultType();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.GetFeatureType#getResultType <em>Result Type</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Result Type</em>' attribute is set.
     * @see #unsetResultType()
     * @see #getResultType()
     * @see #setResultType(ResultTypeType)
     * @generated
     */
    boolean isSetResultType();

    /**
     * Returns the value of the '<em><b>Start Index</b></em>' attribute.
     * The default value is <code>"0"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Start Index</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Start Index</em>' attribute.
     * @see #isSetStartIndex()
     * @see #unsetStartIndex()
     * @see #setStartIndex(BigInteger)
     * @see net.opengis.wfs20.Wfs20Package#getGetFeatureType_StartIndex()
     * @model default="0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger"
     *        extendedMetaData="kind='attribute' name='startIndex'"
     * @generated
     */
    BigInteger getStartIndex();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.GetFeatureType#getStartIndex <em>Start Index</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Start Index</em>' attribute.
     * @see #isSetStartIndex()
     * @see #unsetStartIndex()
     * @see #getStartIndex()
     * @generated
     */
    void setStartIndex(BigInteger value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.GetFeatureType#getStartIndex <em>Start Index</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetStartIndex()
     * @see #getStartIndex()
     * @see #setStartIndex(BigInteger)
     * @generated
     */
    void unsetStartIndex();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.GetFeatureType#getStartIndex <em>Start Index</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Start Index</em>' attribute is set.
     * @see #unsetStartIndex()
     * @see #getStartIndex()
     * @see #setStartIndex(BigInteger)
     * @generated
     */
    boolean isSetStartIndex();
    
    /**
     * A generic bag of extra information that implementations can use to carry vendor parameters
     * <p>
     * This property is not part of the standard model but an extension.
     *  </p>
     * @model
     */
     Map getMetadata();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.GetFeatureType#getMetadata <em>Metadata</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Metadata</em>' attribute.
     * @see #getMetadata()
     * @generated
     */
    void setMetadata(Map value);
    
    /**
     * The format options to be applied to any response to the GetFeature request.
     * <p>
     * This property is not part of the standard model but an extension.
     *  </p>
     * @model
     */
     Map getFormatOptions();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.GetFeatureType#getFormatOptions <em>Format Options</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Format Options</em>' attribute.
     * @see #getFormatOptions()
     * @generated
     */
    void setFormatOptions(Map value);


} // GetFeatureType
