/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.EllipsoidRefType;
import net.opengis.gml311.GeodeticDatumType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.PrimeMeridianRefType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Geodetic Datum Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.GeodeticDatumTypeImpl#getUsesPrimeMeridian <em>Uses Prime Meridian</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GeodeticDatumTypeImpl#getUsesEllipsoid <em>Uses Ellipsoid</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GeodeticDatumTypeImpl extends AbstractDatumTypeImpl implements GeodeticDatumType {
    /**
     * The cached value of the '{@link #getUsesPrimeMeridian() <em>Uses Prime Meridian</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUsesPrimeMeridian()
     * @generated
     * @ordered
     */
    protected PrimeMeridianRefType usesPrimeMeridian;

    /**
     * The cached value of the '{@link #getUsesEllipsoid() <em>Uses Ellipsoid</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUsesEllipsoid()
     * @generated
     * @ordered
     */
    protected EllipsoidRefType usesEllipsoid;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GeodeticDatumTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getGeodeticDatumType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PrimeMeridianRefType getUsesPrimeMeridian() {
        return usesPrimeMeridian;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUsesPrimeMeridian(PrimeMeridianRefType newUsesPrimeMeridian, NotificationChain msgs) {
        PrimeMeridianRefType oldUsesPrimeMeridian = usesPrimeMeridian;
        usesPrimeMeridian = newUsesPrimeMeridian;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.GEODETIC_DATUM_TYPE__USES_PRIME_MERIDIAN, oldUsesPrimeMeridian, newUsesPrimeMeridian);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUsesPrimeMeridian(PrimeMeridianRefType newUsesPrimeMeridian) {
        if (newUsesPrimeMeridian != usesPrimeMeridian) {
            NotificationChain msgs = null;
            if (usesPrimeMeridian != null)
                msgs = ((InternalEObject)usesPrimeMeridian).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEODETIC_DATUM_TYPE__USES_PRIME_MERIDIAN, null, msgs);
            if (newUsesPrimeMeridian != null)
                msgs = ((InternalEObject)newUsesPrimeMeridian).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEODETIC_DATUM_TYPE__USES_PRIME_MERIDIAN, null, msgs);
            msgs = basicSetUsesPrimeMeridian(newUsesPrimeMeridian, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GEODETIC_DATUM_TYPE__USES_PRIME_MERIDIAN, newUsesPrimeMeridian, newUsesPrimeMeridian));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EllipsoidRefType getUsesEllipsoid() {
        return usesEllipsoid;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUsesEllipsoid(EllipsoidRefType newUsesEllipsoid, NotificationChain msgs) {
        EllipsoidRefType oldUsesEllipsoid = usesEllipsoid;
        usesEllipsoid = newUsesEllipsoid;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.GEODETIC_DATUM_TYPE__USES_ELLIPSOID, oldUsesEllipsoid, newUsesEllipsoid);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUsesEllipsoid(EllipsoidRefType newUsesEllipsoid) {
        if (newUsesEllipsoid != usesEllipsoid) {
            NotificationChain msgs = null;
            if (usesEllipsoid != null)
                msgs = ((InternalEObject)usesEllipsoid).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEODETIC_DATUM_TYPE__USES_ELLIPSOID, null, msgs);
            if (newUsesEllipsoid != null)
                msgs = ((InternalEObject)newUsesEllipsoid).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.GEODETIC_DATUM_TYPE__USES_ELLIPSOID, null, msgs);
            msgs = basicSetUsesEllipsoid(newUsesEllipsoid, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GEODETIC_DATUM_TYPE__USES_ELLIPSOID, newUsesEllipsoid, newUsesEllipsoid));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.GEODETIC_DATUM_TYPE__USES_PRIME_MERIDIAN:
                return basicSetUsesPrimeMeridian(null, msgs);
            case Gml311Package.GEODETIC_DATUM_TYPE__USES_ELLIPSOID:
                return basicSetUsesEllipsoid(null, msgs);
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
            case Gml311Package.GEODETIC_DATUM_TYPE__USES_PRIME_MERIDIAN:
                return getUsesPrimeMeridian();
            case Gml311Package.GEODETIC_DATUM_TYPE__USES_ELLIPSOID:
                return getUsesEllipsoid();
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
            case Gml311Package.GEODETIC_DATUM_TYPE__USES_PRIME_MERIDIAN:
                setUsesPrimeMeridian((PrimeMeridianRefType)newValue);
                return;
            case Gml311Package.GEODETIC_DATUM_TYPE__USES_ELLIPSOID:
                setUsesEllipsoid((EllipsoidRefType)newValue);
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
            case Gml311Package.GEODETIC_DATUM_TYPE__USES_PRIME_MERIDIAN:
                setUsesPrimeMeridian((PrimeMeridianRefType)null);
                return;
            case Gml311Package.GEODETIC_DATUM_TYPE__USES_ELLIPSOID:
                setUsesEllipsoid((EllipsoidRefType)null);
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
            case Gml311Package.GEODETIC_DATUM_TYPE__USES_PRIME_MERIDIAN:
                return usesPrimeMeridian != null;
            case Gml311Package.GEODETIC_DATUM_TYPE__USES_ELLIPSOID:
                return usesEllipsoid != null;
        }
        return super.eIsSet(featureID);
    }

} //GeodeticDatumTypeImpl
