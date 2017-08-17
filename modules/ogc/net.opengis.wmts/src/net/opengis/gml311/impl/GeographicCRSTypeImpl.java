/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.EllipsoidalCSRefType;
import net.opengis.gml311.GeodeticDatumRefType;
import net.opengis.gml311.GeographicCRSType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Geographic CRS Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.GeographicCRSTypeImpl#getUsesEllipsoidalCS <em>Uses Ellipsoidal CS</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GeographicCRSTypeImpl#getUsesGeodeticDatum <em>Uses Geodetic Datum</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GeographicCRSTypeImpl extends AbstractReferenceSystemTypeImpl implements GeographicCRSType {
    /**
     * The cached value of the '{@link #getUsesEllipsoidalCS() <em>Uses Ellipsoidal CS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUsesEllipsoidalCS()
     * @generated
     * @ordered
     */
    protected EllipsoidalCSRefType usesEllipsoidalCS;

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
    protected GeographicCRSTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getGeographicCRSType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EllipsoidalCSRefType getUsesEllipsoidalCS() {
        return usesEllipsoidalCS;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUsesEllipsoidalCS(EllipsoidalCSRefType newUsesEllipsoidalCS, NotificationChain msgs) {
        EllipsoidalCSRefType oldUsesEllipsoidalCS = usesEllipsoidalCS;
        usesEllipsoidalCS = newUsesEllipsoidalCS;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_ELLIPSOIDAL_CS, oldUsesEllipsoidalCS, newUsesEllipsoidalCS);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUsesEllipsoidalCS(EllipsoidalCSRefType newUsesEllipsoidalCS) {
        if (newUsesEllipsoidalCS != usesEllipsoidalCS) {
            NotificationChain msgs = null;
            if (usesEllipsoidalCS != null)
                msgs = ((InternalEObject)usesEllipsoidalCS).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_ELLIPSOIDAL_CS, null, msgs);
            if (newUsesEllipsoidalCS != null)
                msgs = ((InternalEObject)newUsesEllipsoidalCS).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_ELLIPSOIDAL_CS, null, msgs);
            msgs = basicSetUsesEllipsoidalCS(newUsesEllipsoidalCS, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_ELLIPSOIDAL_CS, newUsesEllipsoidalCS, newUsesEllipsoidalCS));
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
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_GEODETIC_DATUM, oldUsesGeodeticDatum, newUsesGeodeticDatum);
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
                msgs = ((InternalEObject)usesGeodeticDatum).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_GEODETIC_DATUM, null, msgs);
            if (newUsesGeodeticDatum != null)
                msgs = ((InternalEObject)newUsesGeodeticDatum).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_GEODETIC_DATUM, null, msgs);
            msgs = basicSetUsesGeodeticDatum(newUsesGeodeticDatum, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_GEODETIC_DATUM, newUsesGeodeticDatum, newUsesGeodeticDatum));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_ELLIPSOIDAL_CS:
                return basicSetUsesEllipsoidalCS(null, msgs);
            case Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_GEODETIC_DATUM:
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
            case Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_ELLIPSOIDAL_CS:
                return getUsesEllipsoidalCS();
            case Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_GEODETIC_DATUM:
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
            case Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_ELLIPSOIDAL_CS:
                setUsesEllipsoidalCS((EllipsoidalCSRefType)newValue);
                return;
            case Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_GEODETIC_DATUM:
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
            case Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_ELLIPSOIDAL_CS:
                setUsesEllipsoidalCS((EllipsoidalCSRefType)null);
                return;
            case Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_GEODETIC_DATUM:
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
            case Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_ELLIPSOIDAL_CS:
                return usesEllipsoidalCS != null;
            case Gml311Package.GEOGRAPHIC_CRS_TYPE__USES_GEODETIC_DATUM:
                return usesGeodeticDatum != null;
        }
        return super.eIsSet(featureID);
    }

} //GeographicCRSTypeImpl
