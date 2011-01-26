/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv.impl;

import java.util.Collection;

import net.opengis.wfs.impl.BaseRequestTypeImpl;

import net.opengis.wfsv.DifferenceQueryType;
import net.opengis.wfsv.GetDiffType;
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
 * An implementation of the model object '<em><b>Get Diff Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfsv.impl.GetDiffTypeImpl#getDifferenceQuery <em>Difference Query</em>}</li>
 *   <li>{@link net.opengis.wfsv.impl.GetDiffTypeImpl#getOutputFormat <em>Output Format</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetDiffTypeImpl extends BaseRequestTypeImpl implements GetDiffType {
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
     * The default value of the '{@link #getOutputFormat() <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutputFormat()
     * @generated
     * @ordered
     */
    protected static final String OUTPUT_FORMAT_EDEFAULT = "text/xml; subtype=wfs-transaction/1.1.0";

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GetDiffTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return WfsvPackage.Literals.GET_DIFF_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getDifferenceQuery() {
        if (differenceQuery == null) {
            differenceQuery = new EObjectContainmentEList(DifferenceQueryType.class, this, WfsvPackage.GET_DIFF_TYPE__DIFFERENCE_QUERY);
        }
        return differenceQuery;
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
            eNotify(new ENotificationImpl(this, Notification.SET, WfsvPackage.GET_DIFF_TYPE__OUTPUT_FORMAT, oldOutputFormat, outputFormat, !oldOutputFormatESet));
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
            eNotify(new ENotificationImpl(this, Notification.UNSET, WfsvPackage.GET_DIFF_TYPE__OUTPUT_FORMAT, oldOutputFormat, OUTPUT_FORMAT_EDEFAULT, oldOutputFormatESet));
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
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsvPackage.GET_DIFF_TYPE__DIFFERENCE_QUERY:
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
            case WfsvPackage.GET_DIFF_TYPE__DIFFERENCE_QUERY:
                return getDifferenceQuery();
            case WfsvPackage.GET_DIFF_TYPE__OUTPUT_FORMAT:
                return getOutputFormat();
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
            case WfsvPackage.GET_DIFF_TYPE__DIFFERENCE_QUERY:
                getDifferenceQuery().clear();
                getDifferenceQuery().addAll((Collection)newValue);
                return;
            case WfsvPackage.GET_DIFF_TYPE__OUTPUT_FORMAT:
                setOutputFormat((String)newValue);
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
            case WfsvPackage.GET_DIFF_TYPE__DIFFERENCE_QUERY:
                getDifferenceQuery().clear();
                return;
            case WfsvPackage.GET_DIFF_TYPE__OUTPUT_FORMAT:
                unsetOutputFormat();
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
            case WfsvPackage.GET_DIFF_TYPE__DIFFERENCE_QUERY:
                return differenceQuery != null && !differenceQuery.isEmpty();
            case WfsvPackage.GET_DIFF_TYPE__OUTPUT_FORMAT:
                return isSetOutputFormat();
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
        result.append(" (outputFormat: ");
        if (outputFormatESet) result.append(outputFormat); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //GetDiffTypeImpl
