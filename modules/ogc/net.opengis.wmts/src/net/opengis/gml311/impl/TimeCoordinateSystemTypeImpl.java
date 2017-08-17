/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.TimeCoordinateSystemType;
import net.opengis.gml311.TimeInstantPropertyType;
import net.opengis.gml311.TimeIntervalLengthType;
import net.opengis.gml311.TimePositionType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Time Coordinate System Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.TimeCoordinateSystemTypeImpl#getOriginPosition <em>Origin Position</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TimeCoordinateSystemTypeImpl#getOrigin <em>Origin</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TimeCoordinateSystemTypeImpl#getInterval <em>Interval</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TimeCoordinateSystemTypeImpl extends AbstractTimeReferenceSystemTypeImpl implements TimeCoordinateSystemType {
    /**
     * The cached value of the '{@link #getOriginPosition() <em>Origin Position</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOriginPosition()
     * @generated
     * @ordered
     */
    protected TimePositionType originPosition;

    /**
     * The cached value of the '{@link #getOrigin() <em>Origin</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOrigin()
     * @generated
     * @ordered
     */
    protected TimeInstantPropertyType origin;

    /**
     * The cached value of the '{@link #getInterval() <em>Interval</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInterval()
     * @generated
     * @ordered
     */
    protected TimeIntervalLengthType interval;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TimeCoordinateSystemTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getTimeCoordinateSystemType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimePositionType getOriginPosition() {
        return originPosition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOriginPosition(TimePositionType newOriginPosition, NotificationChain msgs) {
        TimePositionType oldOriginPosition = originPosition;
        originPosition = newOriginPosition;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN_POSITION, oldOriginPosition, newOriginPosition);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOriginPosition(TimePositionType newOriginPosition) {
        if (newOriginPosition != originPosition) {
            NotificationChain msgs = null;
            if (originPosition != null)
                msgs = ((InternalEObject)originPosition).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN_POSITION, null, msgs);
            if (newOriginPosition != null)
                msgs = ((InternalEObject)newOriginPosition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN_POSITION, null, msgs);
            msgs = basicSetOriginPosition(newOriginPosition, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN_POSITION, newOriginPosition, newOriginPosition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeInstantPropertyType getOrigin() {
        return origin;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOrigin(TimeInstantPropertyType newOrigin, NotificationChain msgs) {
        TimeInstantPropertyType oldOrigin = origin;
        origin = newOrigin;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN, oldOrigin, newOrigin);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOrigin(TimeInstantPropertyType newOrigin) {
        if (newOrigin != origin) {
            NotificationChain msgs = null;
            if (origin != null)
                msgs = ((InternalEObject)origin).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN, null, msgs);
            if (newOrigin != null)
                msgs = ((InternalEObject)newOrigin).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN, null, msgs);
            msgs = basicSetOrigin(newOrigin, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN, newOrigin, newOrigin));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeIntervalLengthType getInterval() {
        return interval;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetInterval(TimeIntervalLengthType newInterval, NotificationChain msgs) {
        TimeIntervalLengthType oldInterval = interval;
        interval = newInterval;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__INTERVAL, oldInterval, newInterval);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInterval(TimeIntervalLengthType newInterval) {
        if (newInterval != interval) {
            NotificationChain msgs = null;
            if (interval != null)
                msgs = ((InternalEObject)interval).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__INTERVAL, null, msgs);
            if (newInterval != null)
                msgs = ((InternalEObject)newInterval).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__INTERVAL, null, msgs);
            msgs = basicSetInterval(newInterval, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__INTERVAL, newInterval, newInterval));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN_POSITION:
                return basicSetOriginPosition(null, msgs);
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN:
                return basicSetOrigin(null, msgs);
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__INTERVAL:
                return basicSetInterval(null, msgs);
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
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN_POSITION:
                return getOriginPosition();
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN:
                return getOrigin();
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__INTERVAL:
                return getInterval();
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
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN_POSITION:
                setOriginPosition((TimePositionType)newValue);
                return;
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN:
                setOrigin((TimeInstantPropertyType)newValue);
                return;
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__INTERVAL:
                setInterval((TimeIntervalLengthType)newValue);
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
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN_POSITION:
                setOriginPosition((TimePositionType)null);
                return;
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN:
                setOrigin((TimeInstantPropertyType)null);
                return;
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__INTERVAL:
                setInterval((TimeIntervalLengthType)null);
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
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN_POSITION:
                return originPosition != null;
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__ORIGIN:
                return origin != null;
            case Gml311Package.TIME_COORDINATE_SYSTEM_TYPE__INTERVAL:
                return interval != null;
        }
        return super.eIsSet(featureID);
    }

} //TimeCoordinateSystemTypeImpl
