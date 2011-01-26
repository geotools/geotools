/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import java.util.Collection;

import net.opengis.ows11.OnlineResourceType;

import net.opengis.wcs11.ContentsType;
import net.opengis.wcs11.CoverageSummaryType;
import net.opengis.wcs11.Wcs111Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Contents Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.ContentsTypeImpl#getCoverageSummary <em>Coverage Summary</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.ContentsTypeImpl#getSupportedCRS <em>Supported CRS</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.ContentsTypeImpl#getSupportedFormat <em>Supported Format</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.ContentsTypeImpl#getOtherSource <em>Other Source</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ContentsTypeImpl extends EObjectImpl implements ContentsType {
    /**
     * The cached value of the '{@link #getCoverageSummary() <em>Coverage Summary</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageSummary()
     * @generated
     * @ordered
     */
    protected EList coverageSummary;

    /**
     * The cached value of the '{@link #getSupportedCRS() <em>Supported CRS</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSupportedCRS()
     * @generated
     * @ordered
     */
    protected EList supportedCRS;

    /**
     * The cached value of the '{@link #getSupportedFormat() <em>Supported Format</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSupportedFormat()
     * @generated
     * @ordered
     */
    protected EList supportedFormat;

    /**
     * The cached value of the '{@link #getOtherSource() <em>Other Source</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOtherSource()
     * @generated
     * @ordered
     */
    protected EList otherSource;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ContentsTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wcs111Package.Literals.CONTENTS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getCoverageSummary() {
        if (coverageSummary == null) {
            coverageSummary = new EObjectContainmentEList(CoverageSummaryType.class, this, Wcs111Package.CONTENTS_TYPE__COVERAGE_SUMMARY);
        }
        return coverageSummary;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getSupportedCRS() {
        if (supportedCRS == null) {
            supportedCRS = new EDataTypeEList(String.class, this, Wcs111Package.CONTENTS_TYPE__SUPPORTED_CRS);
        }
        return supportedCRS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getSupportedFormat() {
        if (supportedFormat == null) {
            supportedFormat = new EDataTypeEList(String.class, this, Wcs111Package.CONTENTS_TYPE__SUPPORTED_FORMAT);
        }
        return supportedFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getOtherSource() {
        if (otherSource == null) {
            otherSource = new EObjectContainmentEList(OnlineResourceType.class, this, Wcs111Package.CONTENTS_TYPE__OTHER_SOURCE);
        }
        return otherSource;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs111Package.CONTENTS_TYPE__COVERAGE_SUMMARY:
                return ((InternalEList)getCoverageSummary()).basicRemove(otherEnd, msgs);
            case Wcs111Package.CONTENTS_TYPE__OTHER_SOURCE:
                return ((InternalEList)getOtherSource()).basicRemove(otherEnd, msgs);
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
            case Wcs111Package.CONTENTS_TYPE__COVERAGE_SUMMARY:
                return getCoverageSummary();
            case Wcs111Package.CONTENTS_TYPE__SUPPORTED_CRS:
                return getSupportedCRS();
            case Wcs111Package.CONTENTS_TYPE__SUPPORTED_FORMAT:
                return getSupportedFormat();
            case Wcs111Package.CONTENTS_TYPE__OTHER_SOURCE:
                return getOtherSource();
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
            case Wcs111Package.CONTENTS_TYPE__COVERAGE_SUMMARY:
                getCoverageSummary().clear();
                getCoverageSummary().addAll((Collection)newValue);
                return;
            case Wcs111Package.CONTENTS_TYPE__SUPPORTED_CRS:
                getSupportedCRS().clear();
                getSupportedCRS().addAll((Collection)newValue);
                return;
            case Wcs111Package.CONTENTS_TYPE__SUPPORTED_FORMAT:
                getSupportedFormat().clear();
                getSupportedFormat().addAll((Collection)newValue);
                return;
            case Wcs111Package.CONTENTS_TYPE__OTHER_SOURCE:
                getOtherSource().clear();
                getOtherSource().addAll((Collection)newValue);
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
            case Wcs111Package.CONTENTS_TYPE__COVERAGE_SUMMARY:
                getCoverageSummary().clear();
                return;
            case Wcs111Package.CONTENTS_TYPE__SUPPORTED_CRS:
                getSupportedCRS().clear();
                return;
            case Wcs111Package.CONTENTS_TYPE__SUPPORTED_FORMAT:
                getSupportedFormat().clear();
                return;
            case Wcs111Package.CONTENTS_TYPE__OTHER_SOURCE:
                getOtherSource().clear();
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
            case Wcs111Package.CONTENTS_TYPE__COVERAGE_SUMMARY:
                return coverageSummary != null && !coverageSummary.isEmpty();
            case Wcs111Package.CONTENTS_TYPE__SUPPORTED_CRS:
                return supportedCRS != null && !supportedCRS.isEmpty();
            case Wcs111Package.CONTENTS_TYPE__SUPPORTED_FORMAT:
                return supportedFormat != null && !supportedFormat.isEmpty();
            case Wcs111Package.CONTENTS_TYPE__OTHER_SOURCE:
                return otherSource != null && !otherSource.isEmpty();
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
        result.append(" (supportedCRS: ");
        result.append(supportedCRS);
        result.append(", supportedFormat: ");
        result.append(supportedFormat);
        result.append(')');
        return result.toString();
    }

} //ContentsTypeImpl
