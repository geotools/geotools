/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.math.BigInteger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.opengis.fes20.AbstractQueryExpressionType;

import net.opengis.wfs20.GetFeatureType;
import net.opengis.wfs20.ResolveValueType;
import net.opengis.wfs20.ResultTypeType;
import net.opengis.wfs20.Wfs20Factory;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Feature Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.GetFeatureTypeImpl#getCount <em>Count</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetFeatureTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetFeatureTypeImpl#getResolve <em>Resolve</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetFeatureTypeImpl#getResolveDepth <em>Resolve Depth</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetFeatureTypeImpl#getResolveTimeout <em>Resolve Timeout</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetFeatureTypeImpl#getResultType <em>Result Type</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetFeatureTypeImpl#getStartIndex <em>Start Index</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetFeatureTypeImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetFeatureTypeImpl#getFormatOptions <em>Format Options</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetFeatureTypeImpl#getViewParams <em>View Params</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetFeatureTypeImpl#getAbstractQueryExpressionGroup <em>Abstract Query Expression Group</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.GetFeatureTypeImpl#getAbstractQueryExpression <em>Abstract Query Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetFeatureTypeImpl extends BaseRequestTypeImpl implements GetFeatureType {
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
    protected static final Object RESOLVE_DEPTH_EDEFAULT = Wfs20Factory.eINSTANCE.createFromString(Wfs20Package.eINSTANCE.getPositiveIntegerWithStar(), "*");

    /**
	 * The cached value of the '{@link #getResolveDepth() <em>Resolve Depth</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getResolveDepth()
	 * @generated
	 * @ordered
	 */
    protected Object resolveDepth = RESOLVE_DEPTH_EDEFAULT;

    /**
	 * This is true if the Resolve Depth attribute has been set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected boolean resolveDepthESet;

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
	 * @generated NOT
	 * @ordered
	 */
    protected static final BigInteger START_INDEX_EDEFAULT = null;

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
	 * The default value of the '{@link #getMetadata() <em>Metadata</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getMetadata()
	 * @generated
	 * @ordered
	 */
    protected static final Map METADATA_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getMetadata()
	 * @generated
	 * @ordered
	 */
    protected Map metadata = METADATA_EDEFAULT;

    /**
	 * The default value of the '{@link #getFormatOptions() <em>Format Options</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getFormatOptions()
	 * @generated
	 * @ordered
	 */
    protected static final Map FORMAT_OPTIONS_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getFormatOptions() <em>Format Options</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getFormatOptions()
	 * @generated
	 * @ordered
	 */
    protected Map formatOptions = FORMAT_OPTIONS_EDEFAULT;

    /**
	 * The cached value of the '{@link #getViewParams() <em>View Params</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getViewParams()
	 * @generated
	 * @ordered
	 */
	protected EList<Map> viewParams;

				/**
	 * The cached value of the '{@link #getAbstractQueryExpressionGroup() <em>Abstract Query Expression Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAbstractQueryExpressionGroup()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap abstractQueryExpressionGroup;

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected GetFeatureTypeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
		return Wfs20Package.Literals.GET_FEATURE_TYPE;
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_FEATURE_TYPE__COUNT, oldCount, count));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_FEATURE_TYPE__OUTPUT_FORMAT, oldOutputFormat, outputFormat, !oldOutputFormatESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.GET_FEATURE_TYPE__OUTPUT_FORMAT, oldOutputFormat, OUTPUT_FORMAT_EDEFAULT, oldOutputFormatESet));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_FEATURE_TYPE__RESOLVE, oldResolve, resolve, !oldResolveESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.GET_FEATURE_TYPE__RESOLVE, oldResolve, RESOLVE_EDEFAULT, oldResolveESet));
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
    public Object getResolveDepth() {
		return resolveDepth;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setResolveDepth(Object newResolveDepth) {
		Object oldResolveDepth = resolveDepth;
		resolveDepth = newResolveDepth;
		boolean oldResolveDepthESet = resolveDepthESet;
		resolveDepthESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_FEATURE_TYPE__RESOLVE_DEPTH, oldResolveDepth, resolveDepth, !oldResolveDepthESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void unsetResolveDepth() {
		Object oldResolveDepth = resolveDepth;
		boolean oldResolveDepthESet = resolveDepthESet;
		resolveDepth = RESOLVE_DEPTH_EDEFAULT;
		resolveDepthESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.GET_FEATURE_TYPE__RESOLVE_DEPTH, oldResolveDepth, RESOLVE_DEPTH_EDEFAULT, oldResolveDepthESet));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isSetResolveDepth() {
		return resolveDepthESet;
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_FEATURE_TYPE__RESOLVE_TIMEOUT, oldResolveTimeout, resolveTimeout, !oldResolveTimeoutESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.GET_FEATURE_TYPE__RESOLVE_TIMEOUT, oldResolveTimeout, RESOLVE_TIMEOUT_EDEFAULT, oldResolveTimeoutESet));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_FEATURE_TYPE__RESULT_TYPE, oldResultType, resultType, !oldResultTypeESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.GET_FEATURE_TYPE__RESULT_TYPE, oldResultType, RESULT_TYPE_EDEFAULT, oldResultTypeESet));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_FEATURE_TYPE__START_INDEX, oldStartIndex, startIndex, !oldStartIndexESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.GET_FEATURE_TYPE__START_INDEX, oldStartIndex, START_INDEX_EDEFAULT, oldStartIndexESet));
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
     * @generated NOT
     */
    public Map getMetadata() {
        if (metadata == null) {
            metadata = new HashMap();
        }
        return metadata;
    }

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMetadata(Map newMetadata) {
		Map oldMetadata = metadata;
		metadata = newMetadata;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_FEATURE_TYPE__METADATA, oldMetadata, metadata));
	}

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public Map getFormatOptions() {
        if (formatOptions == null) {
            formatOptions = new HashMap();
        }
        return formatOptions;
    }

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFormatOptions(Map newFormatOptions) {
		Map oldFormatOptions = formatOptions;
		formatOptions = newFormatOptions;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.GET_FEATURE_TYPE__FORMAT_OPTIONS, oldFormatOptions, formatOptions));
	}

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Map> getViewParams() {
		if (viewParams == null) {
            viewParams = (EList) new EDataTypeEList(Map.class, this, Wfs20Package.GET_FEATURE_TYPE__VIEW_PARAMS);
		}
		return viewParams;
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getAbstractQueryExpressionGroup() {
		if (abstractQueryExpressionGroup == null) {
			abstractQueryExpressionGroup = new BasicFeatureMap(this, Wfs20Package.GET_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION_GROUP);
		}
		return abstractQueryExpressionGroup;
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AbstractQueryExpressionType> getAbstractQueryExpression() {
		return getAbstractQueryExpressionGroup().list(Wfs20Package.Literals.GET_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION);
	}

				/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Wfs20Package.GET_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION_GROUP:
				return ((InternalEList<?>)getAbstractQueryExpressionGroup()).basicRemove(otherEnd, msgs);
			case Wfs20Package.GET_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION:
				return ((InternalEList<?>)getAbstractQueryExpression()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

				/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Wfs20Package.GET_FEATURE_TYPE__COUNT:
				return getCount();
			case Wfs20Package.GET_FEATURE_TYPE__OUTPUT_FORMAT:
				return getOutputFormat();
			case Wfs20Package.GET_FEATURE_TYPE__RESOLVE:
				return getResolve();
			case Wfs20Package.GET_FEATURE_TYPE__RESOLVE_DEPTH:
				return getResolveDepth();
			case Wfs20Package.GET_FEATURE_TYPE__RESOLVE_TIMEOUT:
				return getResolveTimeout();
			case Wfs20Package.GET_FEATURE_TYPE__RESULT_TYPE:
				return getResultType();
			case Wfs20Package.GET_FEATURE_TYPE__START_INDEX:
				return getStartIndex();
			case Wfs20Package.GET_FEATURE_TYPE__METADATA:
				return getMetadata();
			case Wfs20Package.GET_FEATURE_TYPE__FORMAT_OPTIONS:
				return getFormatOptions();
			case Wfs20Package.GET_FEATURE_TYPE__VIEW_PARAMS:
				return getViewParams();
			case Wfs20Package.GET_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION_GROUP:
				if (coreType) return getAbstractQueryExpressionGroup();
				return ((FeatureMap.Internal)getAbstractQueryExpressionGroup()).getWrapper();
			case Wfs20Package.GET_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION:
				return getAbstractQueryExpression();
		}
		return super.eGet(featureID, resolve, coreType);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Wfs20Package.GET_FEATURE_TYPE__COUNT:
				setCount((BigInteger)newValue);
				return;
			case Wfs20Package.GET_FEATURE_TYPE__OUTPUT_FORMAT:
				setOutputFormat((String)newValue);
				return;
			case Wfs20Package.GET_FEATURE_TYPE__RESOLVE:
				setResolve((ResolveValueType)newValue);
				return;
			case Wfs20Package.GET_FEATURE_TYPE__RESOLVE_DEPTH:
				setResolveDepth(newValue);
				return;
			case Wfs20Package.GET_FEATURE_TYPE__RESOLVE_TIMEOUT:
				setResolveTimeout((BigInteger)newValue);
				return;
			case Wfs20Package.GET_FEATURE_TYPE__RESULT_TYPE:
				setResultType((ResultTypeType)newValue);
				return;
			case Wfs20Package.GET_FEATURE_TYPE__START_INDEX:
				setStartIndex((BigInteger)newValue);
				return;
			case Wfs20Package.GET_FEATURE_TYPE__METADATA:
				setMetadata((Map)newValue);
				return;
			case Wfs20Package.GET_FEATURE_TYPE__FORMAT_OPTIONS:
				setFormatOptions((Map)newValue);
				return;
			case Wfs20Package.GET_FEATURE_TYPE__VIEW_PARAMS:
				getViewParams().clear();
				getViewParams().addAll((Collection<? extends Map>)newValue);
				return;
			case Wfs20Package.GET_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION_GROUP:
				((FeatureMap.Internal)getAbstractQueryExpressionGroup()).set(newValue);
				return;
			case Wfs20Package.GET_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION:
				getAbstractQueryExpression().clear();
				getAbstractQueryExpression().addAll((Collection<? extends AbstractQueryExpressionType>)newValue);
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
			case Wfs20Package.GET_FEATURE_TYPE__COUNT:
				setCount(COUNT_EDEFAULT);
				return;
			case Wfs20Package.GET_FEATURE_TYPE__OUTPUT_FORMAT:
				unsetOutputFormat();
				return;
			case Wfs20Package.GET_FEATURE_TYPE__RESOLVE:
				unsetResolve();
				return;
			case Wfs20Package.GET_FEATURE_TYPE__RESOLVE_DEPTH:
				unsetResolveDepth();
				return;
			case Wfs20Package.GET_FEATURE_TYPE__RESOLVE_TIMEOUT:
				unsetResolveTimeout();
				return;
			case Wfs20Package.GET_FEATURE_TYPE__RESULT_TYPE:
				unsetResultType();
				return;
			case Wfs20Package.GET_FEATURE_TYPE__START_INDEX:
				unsetStartIndex();
				return;
			case Wfs20Package.GET_FEATURE_TYPE__METADATA:
				setMetadata(METADATA_EDEFAULT);
				return;
			case Wfs20Package.GET_FEATURE_TYPE__FORMAT_OPTIONS:
				setFormatOptions(FORMAT_OPTIONS_EDEFAULT);
				return;
			case Wfs20Package.GET_FEATURE_TYPE__VIEW_PARAMS:
				getViewParams().clear();
				return;
			case Wfs20Package.GET_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION_GROUP:
				getAbstractQueryExpressionGroup().clear();
				return;
			case Wfs20Package.GET_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION:
				getAbstractQueryExpression().clear();
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
			case Wfs20Package.GET_FEATURE_TYPE__COUNT:
				return COUNT_EDEFAULT == null ? count != null : !COUNT_EDEFAULT.equals(count);
			case Wfs20Package.GET_FEATURE_TYPE__OUTPUT_FORMAT:
				return isSetOutputFormat();
			case Wfs20Package.GET_FEATURE_TYPE__RESOLVE:
				return isSetResolve();
			case Wfs20Package.GET_FEATURE_TYPE__RESOLVE_DEPTH:
				return isSetResolveDepth();
			case Wfs20Package.GET_FEATURE_TYPE__RESOLVE_TIMEOUT:
				return isSetResolveTimeout();
			case Wfs20Package.GET_FEATURE_TYPE__RESULT_TYPE:
				return isSetResultType();
			case Wfs20Package.GET_FEATURE_TYPE__START_INDEX:
				return isSetStartIndex();
			case Wfs20Package.GET_FEATURE_TYPE__METADATA:
				return METADATA_EDEFAULT == null ? metadata != null : !METADATA_EDEFAULT.equals(metadata);
			case Wfs20Package.GET_FEATURE_TYPE__FORMAT_OPTIONS:
				return FORMAT_OPTIONS_EDEFAULT == null ? formatOptions != null : !FORMAT_OPTIONS_EDEFAULT.equals(formatOptions);
			case Wfs20Package.GET_FEATURE_TYPE__VIEW_PARAMS:
				return viewParams != null && !viewParams.isEmpty();
			case Wfs20Package.GET_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION_GROUP:
				return abstractQueryExpressionGroup != null && !abstractQueryExpressionGroup.isEmpty();
			case Wfs20Package.GET_FEATURE_TYPE__ABSTRACT_QUERY_EXPRESSION:
				return !getAbstractQueryExpression().isEmpty();
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
		if (resolveDepthESet) result.append(resolveDepth); else result.append("<unset>");
		result.append(", resolveTimeout: ");
		if (resolveTimeoutESet) result.append(resolveTimeout); else result.append("<unset>");
		result.append(", resultType: ");
		if (resultTypeESet) result.append(resultType); else result.append("<unset>");
		result.append(", startIndex: ");
		if (startIndexESet) result.append(startIndex); else result.append("<unset>");
		result.append(", metadata: ");
		result.append(metadata);
		result.append(", formatOptions: ");
		result.append(formatOptions);
		result.append(", viewParams: ");
		result.append(viewParams);
		result.append(", abstractQueryExpressionGroup: ");
		result.append(abstractQueryExpressionGroup);
		result.append(')');
		return result.toString();
	}

} //GetFeatureTypeImpl
