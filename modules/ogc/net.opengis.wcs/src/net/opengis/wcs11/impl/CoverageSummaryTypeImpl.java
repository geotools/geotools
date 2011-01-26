/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import java.util.Collection;

import net.opengis.ows11.MetadataType;
import net.opengis.ows11.WGS84BoundingBoxType;

import net.opengis.ows11.impl.DescriptionTypeImpl;

import net.opengis.wcs11.CoverageSummaryType;
import net.opengis.wcs11.Wcs111Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Coverage Summary Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.CoverageSummaryTypeImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.CoverageSummaryTypeImpl#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.CoverageSummaryTypeImpl#getSupportedCRS <em>Supported CRS</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.CoverageSummaryTypeImpl#getSupportedFormat <em>Supported Format</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.CoverageSummaryTypeImpl#getCoverageSummary <em>Coverage Summary</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.CoverageSummaryTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.CoverageSummaryTypeImpl#getIdentifier1 <em>Identifier1</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CoverageSummaryTypeImpl extends DescriptionTypeImpl implements CoverageSummaryType {
    /**
     * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMetadata()
     * @generated
     * @ordered
     */
    protected EList metadata;

    /**
     * The cached value of the '{@link #getWGS84BoundingBox() <em>WGS84 Bounding Box</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWGS84BoundingBox()
     * @generated
     * @ordered
     */
    protected EList wGS84BoundingBox;

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
     * The cached value of the '{@link #getCoverageSummary() <em>Coverage Summary</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCoverageSummary()
     * @generated
     * @ordered
     */
    protected EList coverageSummary;

    /**
     * The default value of the '{@link #getIdentifier() <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected static final String IDENTIFIER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected String identifier = IDENTIFIER_EDEFAULT;

    /**
     * The default value of the '{@link #getIdentifier1() <em>Identifier1</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier1()
     * @generated
     * @ordered
     */
    protected static final String IDENTIFIER1_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getIdentifier1() <em>Identifier1</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier1()
     * @generated
     * @ordered
     */
    protected String identifier1 = IDENTIFIER1_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CoverageSummaryTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wcs111Package.Literals.COVERAGE_SUMMARY_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getMetadata() {
        if (metadata == null) {
            metadata = new EObjectContainmentEList(MetadataType.class, this, Wcs111Package.COVERAGE_SUMMARY_TYPE__METADATA);
        }
        return metadata;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getWGS84BoundingBox() {
        if (wGS84BoundingBox == null) {
            wGS84BoundingBox = new EObjectContainmentEList(WGS84BoundingBoxType.class, this, Wcs111Package.COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX);
        }
        return wGS84BoundingBox;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getSupportedCRS() {
        if (supportedCRS == null) {
            supportedCRS = new EDataTypeEList(String.class, this, Wcs111Package.COVERAGE_SUMMARY_TYPE__SUPPORTED_CRS);
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
            supportedFormat = new EDataTypeEList(String.class, this, Wcs111Package.COVERAGE_SUMMARY_TYPE__SUPPORTED_FORMAT);
        }
        return supportedFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getCoverageSummary() {
        if (coverageSummary == null) {
            coverageSummary = new EObjectContainmentEList(CoverageSummaryType.class, this, Wcs111Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUMMARY);
        }
        return coverageSummary;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIdentifier(String newIdentifier) {
        String oldIdentifier = identifier;
        identifier = newIdentifier;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.COVERAGE_SUMMARY_TYPE__IDENTIFIER, oldIdentifier, identifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getIdentifier1() {
        return identifier1;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIdentifier1(String newIdentifier1) {
        String oldIdentifier1 = identifier1;
        identifier1 = newIdentifier1;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.COVERAGE_SUMMARY_TYPE__IDENTIFIER1, oldIdentifier1, identifier1));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__METADATA:
                return ((InternalEList)getMetadata()).basicRemove(otherEnd, msgs);
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX:
                return ((InternalEList)getWGS84BoundingBox()).basicRemove(otherEnd, msgs);
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUMMARY:
                return ((InternalEList)getCoverageSummary()).basicRemove(otherEnd, msgs);
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
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__METADATA:
                return getMetadata();
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX:
                return getWGS84BoundingBox();
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__SUPPORTED_CRS:
                return getSupportedCRS();
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__SUPPORTED_FORMAT:
                return getSupportedFormat();
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUMMARY:
                return getCoverageSummary();
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__IDENTIFIER:
                return getIdentifier();
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__IDENTIFIER1:
                return getIdentifier1();
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
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__METADATA:
                getMetadata().clear();
                getMetadata().addAll((Collection)newValue);
                return;
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX:
                getWGS84BoundingBox().clear();
                getWGS84BoundingBox().addAll((Collection)newValue);
                return;
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__SUPPORTED_CRS:
                getSupportedCRS().clear();
                getSupportedCRS().addAll((Collection)newValue);
                return;
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__SUPPORTED_FORMAT:
                getSupportedFormat().clear();
                getSupportedFormat().addAll((Collection)newValue);
                return;
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUMMARY:
                getCoverageSummary().clear();
                getCoverageSummary().addAll((Collection)newValue);
                return;
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__IDENTIFIER:
                setIdentifier((String)newValue);
                return;
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__IDENTIFIER1:
                setIdentifier1((String)newValue);
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
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__METADATA:
                getMetadata().clear();
                return;
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX:
                getWGS84BoundingBox().clear();
                return;
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__SUPPORTED_CRS:
                getSupportedCRS().clear();
                return;
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__SUPPORTED_FORMAT:
                getSupportedFormat().clear();
                return;
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUMMARY:
                getCoverageSummary().clear();
                return;
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__IDENTIFIER:
                setIdentifier(IDENTIFIER_EDEFAULT);
                return;
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__IDENTIFIER1:
                setIdentifier1(IDENTIFIER1_EDEFAULT);
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
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__METADATA:
                return metadata != null && !metadata.isEmpty();
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__WGS84_BOUNDING_BOX:
                return wGS84BoundingBox != null && !wGS84BoundingBox.isEmpty();
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__SUPPORTED_CRS:
                return supportedCRS != null && !supportedCRS.isEmpty();
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__SUPPORTED_FORMAT:
                return supportedFormat != null && !supportedFormat.isEmpty();
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__COVERAGE_SUMMARY:
                return coverageSummary != null && !coverageSummary.isEmpty();
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__IDENTIFIER:
                return IDENTIFIER_EDEFAULT == null ? identifier != null : !IDENTIFIER_EDEFAULT.equals(identifier);
            case Wcs111Package.COVERAGE_SUMMARY_TYPE__IDENTIFIER1:
                return IDENTIFIER1_EDEFAULT == null ? identifier1 != null : !IDENTIFIER1_EDEFAULT.equals(identifier1);
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
        result.append(", identifier: ");
        result.append(identifier);
        result.append(", identifier1: ");
        result.append(identifier1);
        result.append(')');
        return result.toString();
    }

} //CoverageSummaryTypeImpl
