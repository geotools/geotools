/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import java.math.BigInteger;

import java.util.Collection;
import java.util.HashMap;

import java.util.Map;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.QueryType;
import net.opengis.wfs.ResultTypeType;
import net.opengis.wfs.WfsPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Feature Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.GetFeatureTypeImpl#getQuery <em>Query</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.GetFeatureTypeImpl#getMaxFeatures <em>Max Features</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.GetFeatureTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.GetFeatureTypeImpl#getResultType <em>Result Type</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.GetFeatureTypeImpl#getTraverseXlinkDepth <em>Traverse Xlink Depth</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.GetFeatureTypeImpl#getTraverseXlinkExpiry <em>Traverse Xlink Expiry</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.GetFeatureTypeImpl#getFormatOptions <em>Format Options</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.GetFeatureTypeImpl#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetFeatureTypeImpl extends BaseRequestTypeImpl implements GetFeatureType {
	/**
     * The cached value of the '{@link #getQuery() <em>Query</em>}' containment reference list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getQuery()
     * @generated
     * @ordered
     */
	protected EList query;

	/**
     * The default value of the '{@link #getMaxFeatures() <em>Max Features</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getMaxFeatures()
     * @generated
     * @ordered
     */
	protected static final BigInteger MAX_FEATURES_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getMaxFeatures() <em>Max Features</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getMaxFeatures()
     * @generated
     * @ordered
     */
	protected BigInteger maxFeatures = MAX_FEATURES_EDEFAULT;

	/**
     * The default value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
	protected static final String OUTPUT_FORMAT_EDEFAULT = "text/xml; subtype=gml/3.1.1";

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
     * The default value of the '{@link #getResultType() <em>Result Type</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getResultType()
     * @generated
     * @ordered
     */
	protected static final ResultTypeType RESULT_TYPE_EDEFAULT = ResultTypeType.RESULTS_LITERAL;

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
     * The default value of the '{@link #getTraverseXlinkDepth() <em>Traverse Xlink Depth</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTraverseXlinkDepth()
     * @generated
     * @ordered
     */
	protected static final String TRAVERSE_XLINK_DEPTH_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getTraverseXlinkDepth() <em>Traverse Xlink Depth</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTraverseXlinkDepth()
     * @generated
     * @ordered
     */
	protected String traverseXlinkDepth = TRAVERSE_XLINK_DEPTH_EDEFAULT;

	/**
     * The default value of the '{@link #getTraverseXlinkExpiry() <em>Traverse Xlink Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTraverseXlinkExpiry()
     * @generated
     * @ordered
     */
	protected static final BigInteger TRAVERSE_XLINK_EXPIRY_EDEFAULT = null;

	/**
     * The cached value of the '{@link #getTraverseXlinkExpiry() <em>Traverse Xlink Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getTraverseXlinkExpiry()
     * @generated
     * @ordered
     */
	protected BigInteger traverseXlinkExpiry = TRAVERSE_XLINK_EXPIRY_EDEFAULT;

	/**
     * The default value of the '{@link #getFormatOptions() <em>Format Options</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFormatOptions()
     * @generated NOT
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
	protected EClass eStaticClass() {
        return WfsPackage.Literals.GET_FEATURE_TYPE;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EList getQuery() {
        if (query == null) {
            query = new EObjectContainmentEList(QueryType.class, this, WfsPackage.GET_FEATURE_TYPE__QUERY);
        }
        return query;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public BigInteger getMaxFeatures() {
        return maxFeatures;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setMaxFeatures(BigInteger newMaxFeatures) {
        BigInteger oldMaxFeatures = maxFeatures;
        maxFeatures = newMaxFeatures;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GET_FEATURE_TYPE__MAX_FEATURES, oldMaxFeatures, maxFeatures));
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GET_FEATURE_TYPE__OUTPUT_FORMAT, oldOutputFormat, outputFormat, !oldOutputFormatESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, WfsPackage.GET_FEATURE_TYPE__OUTPUT_FORMAT, oldOutputFormat, OUTPUT_FORMAT_EDEFAULT, oldOutputFormatESet));
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GET_FEATURE_TYPE__RESULT_TYPE, oldResultType, resultType, !oldResultTypeESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, WfsPackage.GET_FEATURE_TYPE__RESULT_TYPE, oldResultType, RESULT_TYPE_EDEFAULT, oldResultTypeESet));
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
	public String getTraverseXlinkDepth() {
        return traverseXlinkDepth;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setTraverseXlinkDepth(String newTraverseXlinkDepth) {
        String oldTraverseXlinkDepth = traverseXlinkDepth;
        traverseXlinkDepth = newTraverseXlinkDepth;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_DEPTH, oldTraverseXlinkDepth, traverseXlinkDepth));
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public BigInteger getTraverseXlinkExpiry() {
        return traverseXlinkExpiry;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setTraverseXlinkExpiry(BigInteger newTraverseXlinkExpiry) {
        BigInteger oldTraverseXlinkExpiry = traverseXlinkExpiry;
        traverseXlinkExpiry = newTraverseXlinkExpiry;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_EXPIRY, oldTraverseXlinkExpiry, traverseXlinkExpiry));
    }

	/**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public Map getFormatOptions() {
        if ( formatOptions == null ) {
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GET_FEATURE_TYPE__FORMAT_OPTIONS, oldFormatOptions, formatOptions));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Map getMetadata() {
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsPackage.GET_FEATURE_TYPE__METADATA, oldMetadata, metadata));
    }

    /**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsPackage.GET_FEATURE_TYPE__QUERY:
                return ((InternalEList)getQuery()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsPackage.GET_FEATURE_TYPE__QUERY:
                return getQuery();
            case WfsPackage.GET_FEATURE_TYPE__MAX_FEATURES:
                return getMaxFeatures();
            case WfsPackage.GET_FEATURE_TYPE__OUTPUT_FORMAT:
                return getOutputFormat();
            case WfsPackage.GET_FEATURE_TYPE__RESULT_TYPE:
                return getResultType();
            case WfsPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_DEPTH:
                return getTraverseXlinkDepth();
            case WfsPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_EXPIRY:
                return getTraverseXlinkExpiry();
            case WfsPackage.GET_FEATURE_TYPE__FORMAT_OPTIONS:
                return getFormatOptions();
            case WfsPackage.GET_FEATURE_TYPE__METADATA:
                return getMetadata();
        }
        return super.eGet(featureID, resolve, coreType);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case WfsPackage.GET_FEATURE_TYPE__QUERY:
                getQuery().clear();
                getQuery().addAll((Collection)newValue);
                return;
            case WfsPackage.GET_FEATURE_TYPE__MAX_FEATURES:
                setMaxFeatures((BigInteger)newValue);
                return;
            case WfsPackage.GET_FEATURE_TYPE__OUTPUT_FORMAT:
                setOutputFormat((String)newValue);
                return;
            case WfsPackage.GET_FEATURE_TYPE__RESULT_TYPE:
                setResultType((ResultTypeType)newValue);
                return;
            case WfsPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_DEPTH:
                setTraverseXlinkDepth((String)newValue);
                return;
            case WfsPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_EXPIRY:
                setTraverseXlinkExpiry((BigInteger)newValue);
                return;
            case WfsPackage.GET_FEATURE_TYPE__FORMAT_OPTIONS:
                setFormatOptions((Map)newValue);
                return;
            case WfsPackage.GET_FEATURE_TYPE__METADATA:
                setMetadata((Map)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void eUnset(int featureID) {
        switch (featureID) {
            case WfsPackage.GET_FEATURE_TYPE__QUERY:
                getQuery().clear();
                return;
            case WfsPackage.GET_FEATURE_TYPE__MAX_FEATURES:
                setMaxFeatures(MAX_FEATURES_EDEFAULT);
                return;
            case WfsPackage.GET_FEATURE_TYPE__OUTPUT_FORMAT:
                unsetOutputFormat();
                return;
            case WfsPackage.GET_FEATURE_TYPE__RESULT_TYPE:
                unsetResultType();
                return;
            case WfsPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_DEPTH:
                setTraverseXlinkDepth(TRAVERSE_XLINK_DEPTH_EDEFAULT);
                return;
            case WfsPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_EXPIRY:
                setTraverseXlinkExpiry(TRAVERSE_XLINK_EXPIRY_EDEFAULT);
                return;
            case WfsPackage.GET_FEATURE_TYPE__FORMAT_OPTIONS:
                setFormatOptions(FORMAT_OPTIONS_EDEFAULT);
                return;
            case WfsPackage.GET_FEATURE_TYPE__METADATA:
                setMetadata(METADATA_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public boolean eIsSet(int featureID) {
        switch (featureID) {
            case WfsPackage.GET_FEATURE_TYPE__QUERY:
                return query != null && !query.isEmpty();
            case WfsPackage.GET_FEATURE_TYPE__MAX_FEATURES:
                return MAX_FEATURES_EDEFAULT == null ? maxFeatures != null : !MAX_FEATURES_EDEFAULT.equals(maxFeatures);
            case WfsPackage.GET_FEATURE_TYPE__OUTPUT_FORMAT:
                return isSetOutputFormat();
            case WfsPackage.GET_FEATURE_TYPE__RESULT_TYPE:
                return isSetResultType();
            case WfsPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_DEPTH:
                return TRAVERSE_XLINK_DEPTH_EDEFAULT == null ? traverseXlinkDepth != null : !TRAVERSE_XLINK_DEPTH_EDEFAULT.equals(traverseXlinkDepth);
            case WfsPackage.GET_FEATURE_TYPE__TRAVERSE_XLINK_EXPIRY:
                return TRAVERSE_XLINK_EXPIRY_EDEFAULT == null ? traverseXlinkExpiry != null : !TRAVERSE_XLINK_EXPIRY_EDEFAULT.equals(traverseXlinkExpiry);
            case WfsPackage.GET_FEATURE_TYPE__FORMAT_OPTIONS:
                return FORMAT_OPTIONS_EDEFAULT == null ? formatOptions != null : !FORMAT_OPTIONS_EDEFAULT.equals(formatOptions);
            case WfsPackage.GET_FEATURE_TYPE__METADATA:
                return METADATA_EDEFAULT == null ? metadata != null : !METADATA_EDEFAULT.equals(metadata);
        }
        return super.eIsSet(featureID);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (maxFeatures: ");
        result.append(maxFeatures);
        result.append(", outputFormat: ");
        if (outputFormatESet) result.append(outputFormat); else result.append("<unset>");
        result.append(", resultType: ");
        if (resultTypeESet) result.append(resultType); else result.append("<unset>");
        result.append(", traverseXlinkDepth: ");
        result.append(traverseXlinkDepth);
        result.append(", traverseXlinkExpiry: ");
        result.append(traverseXlinkExpiry);
        result.append(", formatOptions: ");
        result.append(formatOptions);
        result.append(", metadata: ");
        result.append(metadata);
        result.append(')');
        return result.toString();
    }

} //GetFeatureTypeImpl