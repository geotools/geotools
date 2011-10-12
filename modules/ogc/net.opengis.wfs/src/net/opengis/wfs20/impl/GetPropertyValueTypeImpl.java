/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.math.BigInteger;

import net.opengis.fes20.AbstractQueryExpressionType;

import net.opengis.wfs20.GetPropertyValueType;
import net.opengis.wfs20.ResolveValueType;
import net.opengis.wfs20.ResultTypeType;
import net.opengis.wfs20.Wfs20Factory;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Property Value Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.GetPropertyValueTypeImpl#getAbstractQueryExpression <em>Abstract Query Expression</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetPropertyValueTypeImpl#getCount <em>Count</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetPropertyValueTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetPropertyValueTypeImpl#getResolve <em>Resolve</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetPropertyValueTypeImpl#getResolveDepth <em>Resolve Depth</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetPropertyValueTypeImpl#getResolvePath <em>Resolve Path</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetPropertyValueTypeImpl#getResolveTimeout <em>Resolve Timeout</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetPropertyValueTypeImpl#getResultType <em>Result Type</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetPropertyValueTypeImpl#getStartIndex <em>Start Index</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetPropertyValueTypeImpl#getValueReference <em>Value Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetPropertyValueTypeImpl extends BaseRequestTypeImpl implements GetPropertyValueType {
    /**
     * The cached value of the '{@link #getAbstractQueryExpression() <em>Abstract Query Expression</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAbstractQueryExpression()
     * @generated
     * @ordered
     */
    protected AbstractQueryExpressionType abstractQueryExpression;

    /**
     * The default value of the '{@link #getCount() <em>Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCount()
     * @generated
     * @ordered
     */
    protected static final BigInteger COUNT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCount() <em>Count</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCount()
     * @generated
     * @ordered
     */
    protected BigInteger count = COUNT_EDEFAULT;

    /**
     * The default value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected static final String OUTPUT_FORMAT_EDEFAULT = "application/gml+xml; version=3.2";

    /**
     * The cached value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected String outputFormat = OUTPUT_FORMAT_EDEFAULT;

    /**
     * This is true if the Output Format attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean outputFormatESet;

    /**
     * The default value of the '{@link #getResolve() <em>Resolve</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResolve()
     * @generated
     * @ordered
     */
    protected static final ResolveValueType RESOLVE_EDEFAULT = ResolveValueType.NONE;

    /**
     * The cached value of the '{@link #getResolve() <em>Resolve</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResolve()
     * @generated
     * @ordered
     */
    protected ResolveValueType resolve = RESOLVE_EDEFAULT;

    /**
     * This is true if the Resolve attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean resolveESet;

    /**
     * The default value of the '{@link #getResolveDepth() <em>Resolve Depth</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResolveDepth()
     * @generated
     * @ordered
     */
    protected static final Integer RESOLVE_DEPTH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getResolveDepth() <em>Resolve Depth</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResolveDepth()
     * @generated
     * @ordered
     */
    protected Integer resolveDepth = RESOLVE_DEPTH_EDEFAULT;

    /**
     * The default value of the '{@link #getResolvePath() <em>Resolve Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResolvePath()
     * @generated
     * @ordered
     */
    protected static final String RESOLVE_PATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getResolvePath() <em>Resolve Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResolvePath()
     * @generated
     * @ordered
     */
    protected String resolvePath = RESOLVE_PATH_EDEFAULT;

    /**
     * The default value of the '{@link #getResolveTimeout() <em>Resolve Timeout</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResolveTimeout()
     * @generated
     * @ordered
     */
    protected static final BigInteger RESOLVE_TIMEOUT_EDEFAULT = new BigInteger("300");

    /**
     * The cached value of the '{@link #getResolveTimeout() <em>Resolve Timeout</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResolveTimeout()
     * @generated
     * @ordered
     */
    protected BigInteger resolveTimeout = RESOLVE_TIMEOUT_EDEFAULT;

    /**
     * This is true if the Resolve Timeout attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean resolveTimeoutESet;

    /**
     * The default value of the '{@link #getResultType() <em>Result Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResultType()
     * @generated
     * @ordered
     */
    protected static final ResultTypeType RESULT_TYPE_EDEFAULT = ResultTypeType.RESULTS;

    /**
     * The cached value of the '{@link #getResultType() <em>Result Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResultType()
     * @generated
     * @ordered
     */
    protected ResultTypeType resultType = RESULT_TYPE_EDEFAULT;

    /**
     * This is true if the Result Type attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean resultTypeESet;

    /**
     * The default value of the '{@link #getStartIndex() <em>Start Index</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartIndex()
     * @generated
     * @ordered
     */
    protected static final BigInteger START_INDEX_EDEFAULT = new BigInteger("0");

    /**
     * The cached value of the '{@link #getStartIndex() <em>Start Index</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartIndex()
     * @generated
     * @ordered
     */
    protected BigInteger startIndex = START_INDEX_EDEFAULT;

    /**
     * This is true if the Start Index attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean startIndexESet;

    /**
     * The default value of the '{@link #getValueReference() <em>Value Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValueReference()
     * @generated
     * @ordered
     */
    protected static final String VALUE_REFERENCE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValueReference() <em>Value Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValueReference()
     * @generated
     * @ordered
     */
    protected String valueReference = VALUE_REFERENCE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GetPropertyValueTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.GET_PROPERTY_VALUE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractQueryExpressionType getAbstractQueryExpression() {
        if (abstractQueryExpression != null && abstractQueryExpression.eIsProxy()) {
            InternalEObject oldAbstractQueryExpression = (InternalEObject)abstractQueryExpression;
            abstractQueryExpression = (AbstractQueryExpressionType)eResolveProxy(oldAbstractQueryExpression);
            if (abstractQueryExpression != oldAbstractQueryExpression) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, Wfs20Package.GET_PROPERTY_VALUE_TYPE__ABSTRACT_QUERY_EXPRESSION, oldAbstractQueryExpression, abstractQueryExpression));
            }
        }
        return abstractQueryExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractQueryExpressionType basicGetAbstractQueryExpression() {
        return abstractQueryExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAbstractQueryExpression(AbstractQueryExpressionType newAbstractQueryExpression) {
        AbstractQueryExpressionType oldAbstractQueryExpression = abstractQueryExpression;
        abstractQueryExpression = newAbstractQueryExpression;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_PROPERTY_VALUE_TYPE__ABSTRACT_QUERY_EXPRESSION, oldAbstractQueryExpression, abstractQueryExpression));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getCount() {
        return count;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCount(BigInteger newCount) {
        BigInteger oldCount = count;
        count = newCount;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_PROPERTY_VALUE_TYPE__COUNT, oldCount, count));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getOutputFormat() {
        return outputFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOutputFormat(String newOutputFormat) {
        String oldOutputFormat = outputFormat;
        outputFormat = newOutputFormat;
        boolean oldOutputFormatESet = outputFormatESet;
        outputFormatESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_PROPERTY_VALUE_TYPE__OUTPUT_FORMAT, oldOutputFormat, outputFormat, !oldOutputFormatESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetOutputFormat() {
        String oldOutputFormat = outputFormat;
        boolean oldOutputFormatESet = outputFormatESet;
        outputFormat = OUTPUT_FORMAT_EDEFAULT;
        outputFormatESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.GET_PROPERTY_VALUE_TYPE__OUTPUT_FORMAT, oldOutputFormat, OUTPUT_FORMAT_EDEFAULT, oldOutputFormatESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetOutputFormat() {
        return outputFormatESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResolveValueType getResolve() {
        return resolve;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResolve(ResolveValueType newResolve) {
        ResolveValueType oldResolve = resolve;
        resolve = newResolve == null ? RESOLVE_EDEFAULT : newResolve;
        boolean oldResolveESet = resolveESet;
        resolveESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE, oldResolve, resolve, !oldResolveESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetResolve() {
        ResolveValueType oldResolve = resolve;
        boolean oldResolveESet = resolveESet;
        resolve = RESOLVE_EDEFAULT;
        resolveESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE, oldResolve, RESOLVE_EDEFAULT, oldResolveESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetResolve() {
        return resolveESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Integer getResolveDepth() {
        return resolveDepth;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResolveDepth(Integer newResolveDepth) {
        Integer oldResolveDepth = resolveDepth;
        resolveDepth = newResolveDepth;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE_DEPTH, oldResolveDepth, resolveDepth));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getResolvePath() {
        return resolvePath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResolvePath(String newResolvePath) {
        String oldResolvePath = resolvePath;
        resolvePath = newResolvePath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE_PATH, oldResolvePath, resolvePath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getResolveTimeout() {
        return resolveTimeout;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResolveTimeout(BigInteger newResolveTimeout) {
        BigInteger oldResolveTimeout = resolveTimeout;
        resolveTimeout = newResolveTimeout;
        boolean oldResolveTimeoutESet = resolveTimeoutESet;
        resolveTimeoutESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE_TIMEOUT, oldResolveTimeout, resolveTimeout, !oldResolveTimeoutESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetResolveTimeout() {
        BigInteger oldResolveTimeout = resolveTimeout;
        boolean oldResolveTimeoutESet = resolveTimeoutESet;
        resolveTimeout = RESOLVE_TIMEOUT_EDEFAULT;
        resolveTimeoutESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE_TIMEOUT, oldResolveTimeout, RESOLVE_TIMEOUT_EDEFAULT, oldResolveTimeoutESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetResolveTimeout() {
        return resolveTimeoutESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResultTypeType getResultType() {
        return resultType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResultType(ResultTypeType newResultType) {
        ResultTypeType oldResultType = resultType;
        resultType = newResultType == null ? RESULT_TYPE_EDEFAULT : newResultType;
        boolean oldResultTypeESet = resultTypeESet;
        resultTypeESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESULT_TYPE, oldResultType, resultType, !oldResultTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetResultType() {
        ResultTypeType oldResultType = resultType;
        boolean oldResultTypeESet = resultTypeESet;
        resultType = RESULT_TYPE_EDEFAULT;
        resultTypeESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESULT_TYPE, oldResultType, RESULT_TYPE_EDEFAULT, oldResultTypeESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetResultType() {
        return resultTypeESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getStartIndex() {
        return startIndex;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStartIndex(BigInteger newStartIndex) {
        BigInteger oldStartIndex = startIndex;
        startIndex = newStartIndex;
        boolean oldStartIndexESet = startIndexESet;
        startIndexESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_PROPERTY_VALUE_TYPE__START_INDEX, oldStartIndex, startIndex, !oldStartIndexESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetStartIndex() {
        BigInteger oldStartIndex = startIndex;
        boolean oldStartIndexESet = startIndexESet;
        startIndex = START_INDEX_EDEFAULT;
        startIndexESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.GET_PROPERTY_VALUE_TYPE__START_INDEX, oldStartIndex, START_INDEX_EDEFAULT, oldStartIndexESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetStartIndex() {
        return startIndexESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getValueReference() {
        return valueReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValueReference(String newValueReference) {
        String oldValueReference = valueReference;
        valueReference = newValueReference;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_PROPERTY_VALUE_TYPE__VALUE_REFERENCE, oldValueReference, valueReference));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__ABSTRACT_QUERY_EXPRESSION:
                if (resolve) return getAbstractQueryExpression();
                return basicGetAbstractQueryExpression();
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__COUNT:
                return getCount();
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__OUTPUT_FORMAT:
                return getOutputFormat();
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE:
                return getResolve();
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE_DEPTH:
                return getResolveDepth();
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE_PATH:
                return getResolvePath();
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE_TIMEOUT:
                return getResolveTimeout();
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESULT_TYPE:
                return getResultType();
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__START_INDEX:
                return getStartIndex();
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__VALUE_REFERENCE:
                return getValueReference();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__ABSTRACT_QUERY_EXPRESSION:
                setAbstractQueryExpression((AbstractQueryExpressionType)newValue);
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__COUNT:
                setCount((BigInteger)newValue);
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__OUTPUT_FORMAT:
                setOutputFormat((String)newValue);
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE:
                setResolve((ResolveValueType)newValue);
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE_DEPTH:
                setResolveDepth((Integer)newValue);
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE_PATH:
                setResolvePath((String)newValue);
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE_TIMEOUT:
                setResolveTimeout((BigInteger)newValue);
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESULT_TYPE:
                setResultType((ResultTypeType)newValue);
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__START_INDEX:
                setStartIndex((BigInteger)newValue);
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__VALUE_REFERENCE:
                setValueReference((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__ABSTRACT_QUERY_EXPRESSION:
                setAbstractQueryExpression((AbstractQueryExpressionType)null);
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__COUNT:
                setCount(COUNT_EDEFAULT);
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__OUTPUT_FORMAT:
                unsetOutputFormat();
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE:
                unsetResolve();
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE_DEPTH:
                setResolveDepth(RESOLVE_DEPTH_EDEFAULT);
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE_PATH:
                setResolvePath(RESOLVE_PATH_EDEFAULT);
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE_TIMEOUT:
                unsetResolveTimeout();
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESULT_TYPE:
                unsetResultType();
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__START_INDEX:
                unsetStartIndex();
                return;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__VALUE_REFERENCE:
                setValueReference(VALUE_REFERENCE_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__ABSTRACT_QUERY_EXPRESSION:
                return abstractQueryExpression != null;
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__COUNT:
                return COUNT_EDEFAULT == null ? count != null : !COUNT_EDEFAULT.equals(count);
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__OUTPUT_FORMAT:
                return isSetOutputFormat();
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE:
                return isSetResolve();
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE_DEPTH:
                return RESOLVE_DEPTH_EDEFAULT == null ? resolveDepth != null : !RESOLVE_DEPTH_EDEFAULT.equals(resolveDepth);
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE_PATH:
                return RESOLVE_PATH_EDEFAULT == null ? resolvePath != null : !RESOLVE_PATH_EDEFAULT.equals(resolvePath);
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESOLVE_TIMEOUT:
                return isSetResolveTimeout();
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__RESULT_TYPE:
                return isSetResultType();
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__START_INDEX:
                return isSetStartIndex();
            case Wfs20Package.GET_PROPERTY_VALUE_TYPE__VALUE_REFERENCE:
                return VALUE_REFERENCE_EDEFAULT == null ? valueReference != null : !VALUE_REFERENCE_EDEFAULT.equals(valueReference);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (count: ");
        result.append(count);
        result.append(", outputFormat: ");
        if (outputFormatESet) result.append(outputFormat); else result.append("<unset>");
        result.append(", resolve: ");
        if (resolveESet) result.append(resolve); else result.append("<unset>");
        result.append(", resolveDepth: ");
        result.append(resolveDepth);
        result.append(", resolvePath: ");
        result.append(resolvePath);
        result.append(", resolveTimeout: ");
        if (resolveTimeoutESet) result.append(resolveTimeout); else result.append("<unset>");
        result.append(", resultType: ");
        if (resultTypeESet) result.append(resultType); else result.append("<unset>");
        result.append(", startIndex: ");
        if (startIndexESet) result.append(startIndex); else result.append("<unset>");
        result.append(", valueReference: ");
        result.append(valueReference);
        result.append(')');
        return result.toString();
    }

} //GetPropertyValueTypeImpl
