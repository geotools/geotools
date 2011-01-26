/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import java.util.Collection;

import net.opengis.ows11.MetadataType;

import net.opengis.ows11.impl.DescriptionTypeImpl;

import net.opengis.wcs11.CoverageDescriptionType;
import net.opengis.wcs11.CoverageDomainType;
import net.opengis.wcs11.RangeType;
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
 * An implementation of the model object '<em><b>Coverage Description Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.CoverageDescriptionTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.CoverageDescriptionTypeImpl#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.CoverageDescriptionTypeImpl#getDomain <em>Domain</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.CoverageDescriptionTypeImpl#getRange <em>Range</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.CoverageDescriptionTypeImpl#getSupportedCRS <em>Supported CRS</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.CoverageDescriptionTypeImpl#getSupportedFormat <em>Supported Format</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CoverageDescriptionTypeImpl extends DescriptionTypeImpl implements CoverageDescriptionType {
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
     * The cached value of the '{@link #getMetadata() <em>Metadata</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMetadata()
     * @generated
     * @ordered
     */
    protected EList metadata;

    /**
     * The cached value of the '{@link #getDomain() <em>Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDomain()
     * @generated
     * @ordered
     */
    protected CoverageDomainType domain;

    /**
     * The cached value of the '{@link #getRange() <em>Range</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRange()
     * @generated
     * @ordered
     */
    protected RangeType range;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CoverageDescriptionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wcs111Package.Literals.COVERAGE_DESCRIPTION_TYPE;
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
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.COVERAGE_DESCRIPTION_TYPE__IDENTIFIER, oldIdentifier, identifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getMetadata() {
        if (metadata == null) {
            metadata = new EObjectContainmentEList(MetadataType.class, this, Wcs111Package.COVERAGE_DESCRIPTION_TYPE__METADATA);
        }
        return metadata;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CoverageDomainType getDomain() {
        return domain;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDomain(CoverageDomainType newDomain, NotificationChain msgs) {
        CoverageDomainType oldDomain = domain;
        domain = newDomain;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs111Package.COVERAGE_DESCRIPTION_TYPE__DOMAIN, oldDomain, newDomain);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDomain(CoverageDomainType newDomain) {
        if (newDomain != domain) {
            NotificationChain msgs = null;
            if (domain != null)
                msgs = ((InternalEObject)domain).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.COVERAGE_DESCRIPTION_TYPE__DOMAIN, null, msgs);
            if (newDomain != null)
                msgs = ((InternalEObject)newDomain).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.COVERAGE_DESCRIPTION_TYPE__DOMAIN, null, msgs);
            msgs = basicSetDomain(newDomain, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.COVERAGE_DESCRIPTION_TYPE__DOMAIN, newDomain, newDomain));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeType getRange() {
        return range;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRange(RangeType newRange, NotificationChain msgs) {
        RangeType oldRange = range;
        range = newRange;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs111Package.COVERAGE_DESCRIPTION_TYPE__RANGE, oldRange, newRange);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRange(RangeType newRange) {
        if (newRange != range) {
            NotificationChain msgs = null;
            if (range != null)
                msgs = ((InternalEObject)range).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.COVERAGE_DESCRIPTION_TYPE__RANGE, null, msgs);
            if (newRange != null)
                msgs = ((InternalEObject)newRange).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.COVERAGE_DESCRIPTION_TYPE__RANGE, null, msgs);
            msgs = basicSetRange(newRange, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.COVERAGE_DESCRIPTION_TYPE__RANGE, newRange, newRange));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getSupportedCRS() {
        if (supportedCRS == null) {
            supportedCRS = new EDataTypeEList(String.class, this, Wcs111Package.COVERAGE_DESCRIPTION_TYPE__SUPPORTED_CRS);
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
            supportedFormat = new EDataTypeEList(String.class, this, Wcs111Package.COVERAGE_DESCRIPTION_TYPE__SUPPORTED_FORMAT);
        }
        return supportedFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__METADATA:
                return ((InternalEList)getMetadata()).basicRemove(otherEnd, msgs);
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__DOMAIN:
                return basicSetDomain(null, msgs);
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__RANGE:
                return basicSetRange(null, msgs);
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
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__IDENTIFIER:
                return getIdentifier();
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__METADATA:
                return getMetadata();
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__DOMAIN:
                return getDomain();
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__RANGE:
                return getRange();
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__SUPPORTED_CRS:
                return getSupportedCRS();
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__SUPPORTED_FORMAT:
                return getSupportedFormat();
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
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__IDENTIFIER:
                setIdentifier((String)newValue);
                return;
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__METADATA:
                getMetadata().clear();
                getMetadata().addAll((Collection)newValue);
                return;
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__DOMAIN:
                setDomain((CoverageDomainType)newValue);
                return;
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__RANGE:
                setRange((RangeType)newValue);
                return;
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__SUPPORTED_CRS:
                getSupportedCRS().clear();
                getSupportedCRS().addAll((Collection)newValue);
                return;
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__SUPPORTED_FORMAT:
                getSupportedFormat().clear();
                getSupportedFormat().addAll((Collection)newValue);
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
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__IDENTIFIER:
                setIdentifier(IDENTIFIER_EDEFAULT);
                return;
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__METADATA:
                getMetadata().clear();
                return;
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__DOMAIN:
                setDomain((CoverageDomainType)null);
                return;
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__RANGE:
                setRange((RangeType)null);
                return;
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__SUPPORTED_CRS:
                getSupportedCRS().clear();
                return;
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__SUPPORTED_FORMAT:
                getSupportedFormat().clear();
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
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__IDENTIFIER:
                return IDENTIFIER_EDEFAULT == null ? identifier != null : !IDENTIFIER_EDEFAULT.equals(identifier);
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__METADATA:
                return metadata != null && !metadata.isEmpty();
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__DOMAIN:
                return domain != null;
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__RANGE:
                return range != null;
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__SUPPORTED_CRS:
                return supportedCRS != null && !supportedCRS.isEmpty();
            case Wcs111Package.COVERAGE_DESCRIPTION_TYPE__SUPPORTED_FORMAT:
                return supportedFormat != null && !supportedFormat.isEmpty();
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
        result.append(" (identifier: ");
        result.append(identifier);
        result.append(", supportedCRS: ");
        result.append(supportedCRS);
        result.append(", supportedFormat: ");
        result.append(supportedFormat);
        result.append(')');
        return result.toString();
    }

} //CoverageDescriptionTypeImpl
