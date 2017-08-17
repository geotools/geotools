/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.DirectionPropertyType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.LocationPropertyType;
import net.opengis.gml311.MeasureType;
import net.opengis.gml311.MovingObjectStatusType;
import net.opengis.gml311.StringOrRefType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Moving Object Status Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.MovingObjectStatusTypeImpl#getLocationGroup <em>Location Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.MovingObjectStatusTypeImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.MovingObjectStatusTypeImpl#getSpeed <em>Speed</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.MovingObjectStatusTypeImpl#getBearing <em>Bearing</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.MovingObjectStatusTypeImpl#getAcceleration <em>Acceleration</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.MovingObjectStatusTypeImpl#getElevation <em>Elevation</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.MovingObjectStatusTypeImpl#getStatus <em>Status</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MovingObjectStatusTypeImpl extends AbstractTimeSliceTypeImpl implements MovingObjectStatusType {
    /**
     * The cached value of the '{@link #getLocationGroup() <em>Location Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLocationGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap locationGroup;

    /**
     * The cached value of the '{@link #getSpeed() <em>Speed</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSpeed()
     * @generated
     * @ordered
     */
    protected MeasureType speed;

    /**
     * The cached value of the '{@link #getBearing() <em>Bearing</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBearing()
     * @generated
     * @ordered
     */
    protected DirectionPropertyType bearing;

    /**
     * The cached value of the '{@link #getAcceleration() <em>Acceleration</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAcceleration()
     * @generated
     * @ordered
     */
    protected MeasureType acceleration;

    /**
     * The cached value of the '{@link #getElevation() <em>Elevation</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElevation()
     * @generated
     * @ordered
     */
    protected MeasureType elevation;

    /**
     * The cached value of the '{@link #getStatus() <em>Status</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStatus()
     * @generated
     * @ordered
     */
    protected StringOrRefType status;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MovingObjectStatusTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getMovingObjectStatusType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getLocationGroup() {
        if (locationGroup == null) {
            locationGroup = new BasicFeatureMap(this, Gml311Package.MOVING_OBJECT_STATUS_TYPE__LOCATION_GROUP);
        }
        return locationGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LocationPropertyType getLocation() {
        return (LocationPropertyType)getLocationGroup().get(Gml311Package.eINSTANCE.getMovingObjectStatusType_Location(), true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLocation(LocationPropertyType newLocation, NotificationChain msgs) {
        return ((FeatureMap.Internal)getLocationGroup()).basicAdd(Gml311Package.eINSTANCE.getMovingObjectStatusType_Location(), newLocation, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLocation(LocationPropertyType newLocation) {
        ((FeatureMap.Internal)getLocationGroup()).set(Gml311Package.eINSTANCE.getMovingObjectStatusType_Location(), newLocation);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MeasureType getSpeed() {
        return speed;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSpeed(MeasureType newSpeed, NotificationChain msgs) {
        MeasureType oldSpeed = speed;
        speed = newSpeed;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.MOVING_OBJECT_STATUS_TYPE__SPEED, oldSpeed, newSpeed);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSpeed(MeasureType newSpeed) {
        if (newSpeed != speed) {
            NotificationChain msgs = null;
            if (speed != null)
                msgs = ((InternalEObject)speed).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MOVING_OBJECT_STATUS_TYPE__SPEED, null, msgs);
            if (newSpeed != null)
                msgs = ((InternalEObject)newSpeed).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MOVING_OBJECT_STATUS_TYPE__SPEED, null, msgs);
            msgs = basicSetSpeed(newSpeed, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.MOVING_OBJECT_STATUS_TYPE__SPEED, newSpeed, newSpeed));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DirectionPropertyType getBearing() {
        return bearing;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBearing(DirectionPropertyType newBearing, NotificationChain msgs) {
        DirectionPropertyType oldBearing = bearing;
        bearing = newBearing;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.MOVING_OBJECT_STATUS_TYPE__BEARING, oldBearing, newBearing);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBearing(DirectionPropertyType newBearing) {
        if (newBearing != bearing) {
            NotificationChain msgs = null;
            if (bearing != null)
                msgs = ((InternalEObject)bearing).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MOVING_OBJECT_STATUS_TYPE__BEARING, null, msgs);
            if (newBearing != null)
                msgs = ((InternalEObject)newBearing).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MOVING_OBJECT_STATUS_TYPE__BEARING, null, msgs);
            msgs = basicSetBearing(newBearing, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.MOVING_OBJECT_STATUS_TYPE__BEARING, newBearing, newBearing));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MeasureType getAcceleration() {
        return acceleration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAcceleration(MeasureType newAcceleration, NotificationChain msgs) {
        MeasureType oldAcceleration = acceleration;
        acceleration = newAcceleration;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.MOVING_OBJECT_STATUS_TYPE__ACCELERATION, oldAcceleration, newAcceleration);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAcceleration(MeasureType newAcceleration) {
        if (newAcceleration != acceleration) {
            NotificationChain msgs = null;
            if (acceleration != null)
                msgs = ((InternalEObject)acceleration).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MOVING_OBJECT_STATUS_TYPE__ACCELERATION, null, msgs);
            if (newAcceleration != null)
                msgs = ((InternalEObject)newAcceleration).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MOVING_OBJECT_STATUS_TYPE__ACCELERATION, null, msgs);
            msgs = basicSetAcceleration(newAcceleration, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.MOVING_OBJECT_STATUS_TYPE__ACCELERATION, newAcceleration, newAcceleration));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MeasureType getElevation() {
        return elevation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetElevation(MeasureType newElevation, NotificationChain msgs) {
        MeasureType oldElevation = elevation;
        elevation = newElevation;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.MOVING_OBJECT_STATUS_TYPE__ELEVATION, oldElevation, newElevation);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setElevation(MeasureType newElevation) {
        if (newElevation != elevation) {
            NotificationChain msgs = null;
            if (elevation != null)
                msgs = ((InternalEObject)elevation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MOVING_OBJECT_STATUS_TYPE__ELEVATION, null, msgs);
            if (newElevation != null)
                msgs = ((InternalEObject)newElevation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MOVING_OBJECT_STATUS_TYPE__ELEVATION, null, msgs);
            msgs = basicSetElevation(newElevation, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.MOVING_OBJECT_STATUS_TYPE__ELEVATION, newElevation, newElevation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StringOrRefType getStatus() {
        return status;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetStatus(StringOrRefType newStatus, NotificationChain msgs) {
        StringOrRefType oldStatus = status;
        status = newStatus;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.MOVING_OBJECT_STATUS_TYPE__STATUS, oldStatus, newStatus);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStatus(StringOrRefType newStatus) {
        if (newStatus != status) {
            NotificationChain msgs = null;
            if (status != null)
                msgs = ((InternalEObject)status).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MOVING_OBJECT_STATUS_TYPE__STATUS, null, msgs);
            if (newStatus != null)
                msgs = ((InternalEObject)newStatus).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MOVING_OBJECT_STATUS_TYPE__STATUS, null, msgs);
            msgs = basicSetStatus(newStatus, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.MOVING_OBJECT_STATUS_TYPE__STATUS, newStatus, newStatus));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__LOCATION_GROUP:
                return ((InternalEList<?>)getLocationGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__LOCATION:
                return basicSetLocation(null, msgs);
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__SPEED:
                return basicSetSpeed(null, msgs);
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__BEARING:
                return basicSetBearing(null, msgs);
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__ACCELERATION:
                return basicSetAcceleration(null, msgs);
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__ELEVATION:
                return basicSetElevation(null, msgs);
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__STATUS:
                return basicSetStatus(null, msgs);
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
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__LOCATION_GROUP:
                if (coreType) return getLocationGroup();
                return ((FeatureMap.Internal)getLocationGroup()).getWrapper();
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__LOCATION:
                return getLocation();
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__SPEED:
                return getSpeed();
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__BEARING:
                return getBearing();
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__ACCELERATION:
                return getAcceleration();
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__ELEVATION:
                return getElevation();
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__STATUS:
                return getStatus();
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
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__LOCATION_GROUP:
                ((FeatureMap.Internal)getLocationGroup()).set(newValue);
                return;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__LOCATION:
                setLocation((LocationPropertyType)newValue);
                return;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__SPEED:
                setSpeed((MeasureType)newValue);
                return;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__BEARING:
                setBearing((DirectionPropertyType)newValue);
                return;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__ACCELERATION:
                setAcceleration((MeasureType)newValue);
                return;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__ELEVATION:
                setElevation((MeasureType)newValue);
                return;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__STATUS:
                setStatus((StringOrRefType)newValue);
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
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__LOCATION_GROUP:
                getLocationGroup().clear();
                return;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__LOCATION:
                setLocation((LocationPropertyType)null);
                return;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__SPEED:
                setSpeed((MeasureType)null);
                return;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__BEARING:
                setBearing((DirectionPropertyType)null);
                return;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__ACCELERATION:
                setAcceleration((MeasureType)null);
                return;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__ELEVATION:
                setElevation((MeasureType)null);
                return;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__STATUS:
                setStatus((StringOrRefType)null);
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
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__LOCATION_GROUP:
                return locationGroup != null && !locationGroup.isEmpty();
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__LOCATION:
                return getLocation() != null;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__SPEED:
                return speed != null;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__BEARING:
                return bearing != null;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__ACCELERATION:
                return acceleration != null;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__ELEVATION:
                return elevation != null;
            case Gml311Package.MOVING_OBJECT_STATUS_TYPE__STATUS:
                return status != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (locationGroup: ");
        result.append(locationGroup);
        result.append(')');
        return result.toString();
    }

} //MovingObjectStatusTypeImpl
