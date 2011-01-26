/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv.impl;

import java.math.BigInteger;

import java.util.Collection;

import net.opengis.wfs.ResultTypeType;

import net.opengis.wfs.impl.BaseRequestTypeImpl;

import net.opengis.wfsv.DifferenceQueryType;
import net.opengis.wfsv.GetLogType;
import net.opengis.wfsv.WfsvPackage;

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
 * An implementation of the model object '<em><b>Get Log Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfsv.impl.GetLogTypeImpl#getDifferenceQuery <em>Difference Query</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.GetLogTypeImpl#getMaxFeatures <em>Max Features</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.GetLogTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.GetLogTypeImpl#getResultType <em>Result Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetLogTypeImpl extends BaseRequestTypeImpl implements GetLogType {
    /**
     * The cached value of the '{@link #getDifferenceQuery() <em>Difference Query</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDifferenceQuery()
     * @generated
     * @ordered
     */
    protected EList differenceQuery;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GetLogTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return WfsvPackage.Literals.GET_LOG_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getDifferenceQuery() {
        if (differenceQuery == null) {
            differenceQuery = new EObjectContainmentEList(DifferenceQueryType.class, this, WfsvPackage.GET_LOG_TYPE__DIFFERENCE_QUERY);
        }
        return differenceQuery;
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.GET_LOG_TYPE__MAX_FEATURES, oldMaxFeatures, maxFeatures));
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.GET_LOG_TYPE__OUTPUT_FORMAT, oldOutputFormat, outputFormat, !oldOutputFormatESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, WfsvPackage.GET_LOG_TYPE__OUTPUT_FORMAT, oldOutputFormat, OUTPUT_FORMAT_EDEFAULT, oldOutputFormatESet));
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.GET_LOG_TYPE__RESULT_TYPE, oldResultType, resultType, !oldResultTypeESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, WfsvPackage.GET_LOG_TYPE__RESULT_TYPE, oldResultType, RESULT_TYPE_EDEFAULT, oldResultTypeESet));
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
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsvPackage.GET_LOG_TYPE__DIFFERENCE_QUERY:
                return ((InternalEList)getDifferenceQuery()).basicRemove(otherEnd, msgs);
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
            case WfsvPackage.GET_LOG_TYPE__DIFFERENCE_QUERY:
                return getDifferenceQuery();
            case WfsvPackage.GET_LOG_TYPE__MAX_FEATURES:
                return getMaxFeatures();
            case WfsvPackage.GET_LOG_TYPE__OUTPUT_FORMAT:
                return getOutputFormat();
            case WfsvPackage.GET_LOG_TYPE__RESULT_TYPE:
                return getResultType();
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
            case WfsvPackage.GET_LOG_TYPE__DIFFERENCE_QUERY:
                getDifferenceQuery().clear();
                getDifferenceQuery().addAll((Collection)newValue);
                return;
            case WfsvPackage.GET_LOG_TYPE__MAX_FEATURES:
                setMaxFeatures((BigInteger)newValue);
                return;
            case WfsvPackage.GET_LOG_TYPE__OUTPUT_FORMAT:
                setOutputFormat((String)newValue);
                return;
            case WfsvPackage.GET_LOG_TYPE__RESULT_TYPE:
                setResultType((ResultTypeType)newValue);
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
            case WfsvPackage.GET_LOG_TYPE__DIFFERENCE_QUERY:
                getDifferenceQuery().clear();
                return;
            case WfsvPackage.GET_LOG_TYPE__MAX_FEATURES:
                setMaxFeatures(MAX_FEATURES_EDEFAULT);
                return;
            case WfsvPackage.GET_LOG_TYPE__OUTPUT_FORMAT:
                unsetOutputFormat();
                return;
            case WfsvPackage.GET_LOG_TYPE__RESULT_TYPE:
                unsetResultType();
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
            case WfsvPackage.GET_LOG_TYPE__DIFFERENCE_QUERY:
                return differenceQuery != null && !differenceQuery.isEmpty();
            case WfsvPackage.GET_LOG_TYPE__MAX_FEATURES:
                return MAX_FEATURES_EDEFAULT == null ? maxFeatures != null : !MAX_FEATURES_EDEFAULT.equals(maxFeatures);
            case WfsvPackage.GET_LOG_TYPE__OUTPUT_FORMAT:
                return isSetOutputFormat();
            case WfsvPackage.GET_LOG_TYPE__RESULT_TYPE:
                return isSetResultType();
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
        result.append(')');
        return result.toString();
    }

} //GetLogTypeImpl
