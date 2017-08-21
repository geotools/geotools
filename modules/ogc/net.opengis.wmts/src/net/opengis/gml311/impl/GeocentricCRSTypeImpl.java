/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.CartesianCSRefType;
import net.opengis.gml311.GeocentricCRSType;
import net.opengis.gml311.GeodeticDatumRefType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.SphericalCSRefType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Geocentric CRS Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.GeocentricCRSTypeImpl#getUsesCartesianCS <em>Uses Cartesian CS</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GeocentricCRSTypeImpl#getUsesSphericalCS <em>Uses Spherical CS</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GeocentricCRSTypeImpl#getUsesGeodeticDatum <em>Uses Geodetic Datum</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GeocentricCRSTypeImpl extends AbstractReferenceSystemTypeImpl implements GeocentricCRSType {
    /**
     * The cached value of the '{@link #getUsesCartesianCS() <em>Uses Cartesian CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUsesCartesianCS()
     * @generated
     * @ordered
     */
    protected CartesianCSRefType usesCartesianCS;

    /**
     * The cached value of the '{@link #getUsesSphericalCS() <em>Uses Spherical CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUsesSphericalCS()
     * @generated
     * @ordered
     */
    protected SphericalCSRefType usesSphericalCS;

    /**
     * The cached value of the '{@link #getUsesGeodeticDatum() <em>Uses Geodetic Datum</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUsesGeodeticDatum()
     * @generated
     * @ordered
     */
    protected GeodeticDatumRefType usesGeodeticDatum;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GeocentricCRSTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getGeocentricCRSType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CartesianCSRefType getUsesCartesianCS() {
        return usesCartesianCS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUsesCartesianCS(CartesianCSRefType newUsesCartesianCS, NotificationChain msgs) {
        CartesianCSRefType oldUsesCartesianCS = usesCartesianCS;
        usesCartesianCS = newUsesCartesianCS;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.GEOCENTRIC_CRS_TYPE__USES_CARTESIAN_CS, oldUsesCartesianCS, newUsesCartesianCS);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUsesCartesianCS(CartesianCSRefType newUsesCartesianCS) {
        if (newUsesCartesianCS != usesCartesianCS) {
            NotificationChain msgs = null;
            if (usesCartesianCS != null)
                msgs = ((InternalEObject)usesCartesianCS).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEOCENTRIC_CRS_TYPE__USES_CARTESIAN_CS, null, msgs);
            if (newUsesCartesianCS != null)
                msgs = ((InternalEObject)newUsesCartesianCS).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEOCENTRIC_CRS_TYPE__USES_CARTESIAN_CS, null, msgs);
            msgs = basicSetUsesCartesianCS(newUsesCartesianCS, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GEOCENTRIC_CRS_TYPE__USES_CARTESIAN_CS, newUsesCartesianCS, newUsesCartesianCS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SphericalCSRefType getUsesSphericalCS() {
        return usesSphericalCS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUsesSphericalCS(SphericalCSRefType newUsesSphericalCS, NotificationChain msgs) {
        SphericalCSRefType oldUsesSphericalCS = usesSphericalCS;
        usesSphericalCS = newUsesSphericalCS;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.GEOCENTRIC_CRS_TYPE__USES_SPHERICAL_CS, oldUsesSphericalCS, newUsesSphericalCS);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUsesSphericalCS(SphericalCSRefType newUsesSphericalCS) {
        if (newUsesSphericalCS != usesSphericalCS) {
            NotificationChain msgs = null;
            if (usesSphericalCS != null)
                msgs = ((InternalEObject)usesSphericalCS).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEOCENTRIC_CRS_TYPE__USES_SPHERICAL_CS, null, msgs);
            if (newUsesSphericalCS != null)
                msgs = ((InternalEObject)newUsesSphericalCS).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEOCENTRIC_CRS_TYPE__USES_SPHERICAL_CS, null, msgs);
            msgs = basicSetUsesSphericalCS(newUsesSphericalCS, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GEOCENTRIC_CRS_TYPE__USES_SPHERICAL_CS, newUsesSphericalCS, newUsesSphericalCS));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GeodeticDatumRefType getUsesGeodeticDatum() {
        return usesGeodeticDatum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUsesGeodeticDatum(GeodeticDatumRefType newUsesGeodeticDatum, NotificationChain msgs) {
        GeodeticDatumRefType oldUsesGeodeticDatum = usesGeodeticDatum;
        usesGeodeticDatum = newUsesGeodeticDatum;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.GEOCENTRIC_CRS_TYPE__USES_GEODETIC_DATUM, oldUsesGeodeticDatum, newUsesGeodeticDatum);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUsesGeodeticDatum(GeodeticDatumRefType newUsesGeodeticDatum) {
        if (newUsesGeodeticDatum != usesGeodeticDatum) {
            NotificationChain msgs = null;
            if (usesGeodeticDatum != null)
                msgs = ((InternalEObject)usesGeodeticDatum).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEOCENTRIC_CRS_TYPE__USES_GEODETIC_DATUM, null, msgs);
            if (newUsesGeodeticDatum != null)
                msgs = ((InternalEObject)newUsesGeodeticDatum).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEOCENTRIC_CRS_TYPE__USES_GEODETIC_DATUM, null, msgs);
            msgs = basicSetUsesGeodeticDatum(newUsesGeodeticDatum, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GEOCENTRIC_CRS_TYPE__USES_GEODETIC_DATUM, newUsesGeodeticDatum, newUsesGeodeticDatum));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.GEOCENTRIC_CRS_TYPE__USES_CARTESIAN_CS:
                return basicSetUsesCartesianCS(null, msgs);
            case Gml311Package.GEOCENTRIC_CRS_TYPE__USES_SPHERICAL_CS:
                return basicSetUsesSphericalCS(null, msgs);
            case Gml311Package.GEOCENTRIC_CRS_TYPE__USES_GEODETIC_DATUM:
                return basicSetUsesGeodeticDatum(null, msgs);
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
            case Gml311Package.GEOCENTRIC_CRS_TYPE__USES_CARTESIAN_CS:
                return getUsesCartesianCS();
            case Gml311Package.GEOCENTRIC_CRS_TYPE__USES_SPHERICAL_CS:
                return getUsesSphericalCS();
            case Gml311Package.GEOCENTRIC_CRS_TYPE__USES_GEODETIC_DATUM:
                return getUsesGeodeticDatum();
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
            case Gml311Package.GEOCENTRIC_CRS_TYPE__USES_CARTESIAN_CS:
                setUsesCartesianCS((CartesianCSRefType)newValue);
                return;
            case Gml311Package.GEOCENTRIC_CRS_TYPE__USES_SPHERICAL_CS:
                setUsesSphericalCS((SphericalCSRefType)newValue);
                return;
            case Gml311Package.GEOCENTRIC_CRS_TYPE__USES_GEODETIC_DATUM:
                setUsesGeodeticDatum((GeodeticDatumRefType)newValue);
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
            case Gml311Package.GEOCENTRIC_CRS_TYPE__USES_CARTESIAN_CS:
                setUsesCartesianCS((CartesianCSRefType)null);
                return;
            case Gml311Package.GEOCENTRIC_CRS_TYPE__USES_SPHERICAL_CS:
                setUsesSphericalCS((SphericalCSRefType)null);
                return;
            case Gml311Package.GEOCENTRIC_CRS_TYPE__USES_GEODETIC_DATUM:
                setUsesGeodeticDatum((GeodeticDatumRefType)null);
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
            case Gml311Package.GEOCENTRIC_CRS_TYPE__USES_CARTESIAN_CS:
                return usesCartesianCS != null;
            case Gml311Package.GEOCENTRIC_CRS_TYPE__USES_SPHERICAL_CS:
                return usesSphericalCS != null;
            case Gml311Package.GEOCENTRIC_CRS_TYPE__USES_GEODETIC_DATUM:
                return usesGeodeticDatum != null;
        }
        return super.eIsSet(featureID);
    }

} //GeocentricCRSTypeImpl
