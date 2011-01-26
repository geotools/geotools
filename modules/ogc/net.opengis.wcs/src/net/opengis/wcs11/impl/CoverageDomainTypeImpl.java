/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import net.opengis.wcs11.CoverageDomainType;
import net.opengis.wcs11.SpatialDomainType;
import net.opengis.wcs11.TimeSequenceType;
import net.opengis.wcs11.Wcs111Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Coverage Domain Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.CoverageDomainTypeImpl#getSpatialDomain <em>Spatial Domain</em>}</li>
 *   <li>{@link net.opengis.wcs11.impl.CoverageDomainTypeImpl#getTemporalDomain <em>Temporal Domain</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CoverageDomainTypeImpl extends EObjectImpl implements CoverageDomainType {
    /**
     * The cached value of the '{@link #getSpatialDomain() <em>Spatial Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSpatialDomain()
     * @generated
     * @ordered
     */
    protected SpatialDomainType spatialDomain;

    /**
     * The cached value of the '{@link #getTemporalDomain() <em>Temporal Domain</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTemporalDomain()
     * @generated
     * @ordered
     */
    protected TimeSequenceType temporalDomain;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CoverageDomainTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wcs111Package.Literals.COVERAGE_DOMAIN_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SpatialDomainType getSpatialDomain() {
        return spatialDomain;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSpatialDomain(SpatialDomainType newSpatialDomain, NotificationChain msgs) {
        SpatialDomainType oldSpatialDomain = spatialDomain;
        spatialDomain = newSpatialDomain;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs111Package.COVERAGE_DOMAIN_TYPE__SPATIAL_DOMAIN, oldSpatialDomain, newSpatialDomain);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSpatialDomain(SpatialDomainType newSpatialDomain) {
        if (newSpatialDomain != spatialDomain) {
            NotificationChain msgs = null;
            if (spatialDomain != null)
                msgs = ((InternalEObject)spatialDomain).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.COVERAGE_DOMAIN_TYPE__SPATIAL_DOMAIN, null, msgs);
            if (newSpatialDomain != null)
                msgs = ((InternalEObject)newSpatialDomain).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.COVERAGE_DOMAIN_TYPE__SPATIAL_DOMAIN, null, msgs);
            msgs = basicSetSpatialDomain(newSpatialDomain, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.COVERAGE_DOMAIN_TYPE__SPATIAL_DOMAIN, newSpatialDomain, newSpatialDomain));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeSequenceType getTemporalDomain() {
        return temporalDomain;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTemporalDomain(TimeSequenceType newTemporalDomain, NotificationChain msgs) {
        TimeSequenceType oldTemporalDomain = temporalDomain;
        temporalDomain = newTemporalDomain;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wcs111Package.COVERAGE_DOMAIN_TYPE__TEMPORAL_DOMAIN, oldTemporalDomain, newTemporalDomain);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTemporalDomain(TimeSequenceType newTemporalDomain) {
        if (newTemporalDomain != temporalDomain) {
            NotificationChain msgs = null;
            if (temporalDomain != null)
                msgs = ((InternalEObject)temporalDomain).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.COVERAGE_DOMAIN_TYPE__TEMPORAL_DOMAIN, null, msgs);
            if (newTemporalDomain != null)
                msgs = ((InternalEObject)newTemporalDomain).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wcs111Package.COVERAGE_DOMAIN_TYPE__TEMPORAL_DOMAIN, null, msgs);
            msgs = basicSetTemporalDomain(newTemporalDomain, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs111Package.COVERAGE_DOMAIN_TYPE__TEMPORAL_DOMAIN, newTemporalDomain, newTemporalDomain));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs111Package.COVERAGE_DOMAIN_TYPE__SPATIAL_DOMAIN:
                return basicSetSpatialDomain(null, msgs);
            case Wcs111Package.COVERAGE_DOMAIN_TYPE__TEMPORAL_DOMAIN:
                return basicSetTemporalDomain(null, msgs);
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
            case Wcs111Package.COVERAGE_DOMAIN_TYPE__SPATIAL_DOMAIN:
                return getSpatialDomain();
            case Wcs111Package.COVERAGE_DOMAIN_TYPE__TEMPORAL_DOMAIN:
                return getTemporalDomain();
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
            case Wcs111Package.COVERAGE_DOMAIN_TYPE__SPATIAL_DOMAIN:
                setSpatialDomain((SpatialDomainType)newValue);
                return;
            case Wcs111Package.COVERAGE_DOMAIN_TYPE__TEMPORAL_DOMAIN:
                setTemporalDomain((TimeSequenceType)newValue);
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
            case Wcs111Package.COVERAGE_DOMAIN_TYPE__SPATIAL_DOMAIN:
                setSpatialDomain((SpatialDomainType)null);
                return;
            case Wcs111Package.COVERAGE_DOMAIN_TYPE__TEMPORAL_DOMAIN:
                setTemporalDomain((TimeSequenceType)null);
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
            case Wcs111Package.COVERAGE_DOMAIN_TYPE__SPATIAL_DOMAIN:
                return spatialDomain != null;
            case Wcs111Package.COVERAGE_DOMAIN_TYPE__TEMPORAL_DOMAIN:
                return temporalDomain != null;
        }
        return super.eIsSet(featureID);
    }

} //CoverageDomainTypeImpl
