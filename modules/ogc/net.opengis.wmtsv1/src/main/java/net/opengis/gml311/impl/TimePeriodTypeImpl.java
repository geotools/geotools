/**
 */
package net.opengis.gml311.impl;

import javax.xml.datatype.Duration;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.TimeInstantPropertyType;
import net.opengis.gml311.TimeIntervalLengthType;
import net.opengis.gml311.TimePeriodType;
import net.opengis.gml311.TimePositionType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Time Period Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.TimePeriodTypeImpl#getBeginPosition <em>Begin Position</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TimePeriodTypeImpl#getBegin <em>Begin</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TimePeriodTypeImpl#getEndPosition <em>End Position</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TimePeriodTypeImpl#getEnd <em>End</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TimePeriodTypeImpl#getDuration <em>Duration</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TimePeriodTypeImpl#getTimeInterval <em>Time Interval</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TimePeriodTypeImpl extends AbstractTimeGeometricPrimitiveTypeImpl implements TimePeriodType {
    /**
     * The cached value of the '{@link #getBeginPosition() <em>Begin Position</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBeginPosition()
     * @generated
     * @ordered
     */
    protected TimePositionType beginPosition;

    /**
     * The cached value of the '{@link #getBegin() <em>Begin</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBegin()
     * @generated
     * @ordered
     */
    protected TimeInstantPropertyType begin;

    /**
     * The cached value of the '{@link #getEndPosition() <em>End Position</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndPosition()
     * @generated
     * @ordered
     */
    protected TimePositionType endPosition;

    /**
     * The cached value of the '{@link #getEnd() <em>End</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEnd()
     * @generated
     * @ordered
     */
    protected TimeInstantPropertyType end;

    /**
     * The default value of the '{@link #getDuration() <em>Duration</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDuration()
     * @generated
     * @ordered
     */
    protected static final Duration DURATION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDuration() <em>Duration</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDuration()
     * @generated
     * @ordered
     */
    protected Duration duration = DURATION_EDEFAULT;

    /**
     * The cached value of the '{@link #getTimeInterval() <em>Time Interval</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTimeInterval()
     * @generated
     * @ordered
     */
    protected TimeIntervalLengthType timeInterval;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TimePeriodTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getTimePeriodType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimePositionType getBeginPosition() {
        return beginPosition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBeginPosition(TimePositionType newBeginPosition, NotificationChain msgs) {
        TimePositionType oldBeginPosition = beginPosition;
        beginPosition = newBeginPosition;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_PERIOD_TYPE__BEGIN_POSITION, oldBeginPosition, newBeginPosition);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBeginPosition(TimePositionType newBeginPosition) {
        if (newBeginPosition != beginPosition) {
            NotificationChain msgs = null;
            if (beginPosition != null)
                msgs = ((InternalEObject)beginPosition).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_PERIOD_TYPE__BEGIN_POSITION, null, msgs);
            if (newBeginPosition != null)
                msgs = ((InternalEObject)newBeginPosition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_PERIOD_TYPE__BEGIN_POSITION, null, msgs);
            msgs = basicSetBeginPosition(newBeginPosition, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_PERIOD_TYPE__BEGIN_POSITION, newBeginPosition, newBeginPosition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeInstantPropertyType getBegin() {
        return begin;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBegin(TimeInstantPropertyType newBegin, NotificationChain msgs) {
        TimeInstantPropertyType oldBegin = begin;
        begin = newBegin;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_PERIOD_TYPE__BEGIN, oldBegin, newBegin);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBegin(TimeInstantPropertyType newBegin) {
        if (newBegin != begin) {
            NotificationChain msgs = null;
            if (begin != null)
                msgs = ((InternalEObject)begin).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_PERIOD_TYPE__BEGIN, null, msgs);
            if (newBegin != null)
                msgs = ((InternalEObject)newBegin).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_PERIOD_TYPE__BEGIN, null, msgs);
            msgs = basicSetBegin(newBegin, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_PERIOD_TYPE__BEGIN, newBegin, newBegin));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimePositionType getEndPosition() {
        return endPosition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetEndPosition(TimePositionType newEndPosition, NotificationChain msgs) {
        TimePositionType oldEndPosition = endPosition;
        endPosition = newEndPosition;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_PERIOD_TYPE__END_POSITION, oldEndPosition, newEndPosition);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEndPosition(TimePositionType newEndPosition) {
        if (newEndPosition != endPosition) {
            NotificationChain msgs = null;
            if (endPosition != null)
                msgs = ((InternalEObject)endPosition).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_PERIOD_TYPE__END_POSITION, null, msgs);
            if (newEndPosition != null)
                msgs = ((InternalEObject)newEndPosition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_PERIOD_TYPE__END_POSITION, null, msgs);
            msgs = basicSetEndPosition(newEndPosition, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_PERIOD_TYPE__END_POSITION, newEndPosition, newEndPosition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeInstantPropertyType getEnd() {
        return end;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetEnd(TimeInstantPropertyType newEnd, NotificationChain msgs) {
        TimeInstantPropertyType oldEnd = end;
        end = newEnd;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_PERIOD_TYPE__END, oldEnd, newEnd);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEnd(TimeInstantPropertyType newEnd) {
        if (newEnd != end) {
            NotificationChain msgs = null;
            if (end != null)
                msgs = ((InternalEObject)end).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_PERIOD_TYPE__END, null, msgs);
            if (newEnd != null)
                msgs = ((InternalEObject)newEnd).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_PERIOD_TYPE__END, null, msgs);
            msgs = basicSetEnd(newEnd, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_PERIOD_TYPE__END, newEnd, newEnd));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDuration(Duration newDuration) {
        Duration oldDuration = duration;
        duration = newDuration;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_PERIOD_TYPE__DURATION, oldDuration, duration));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TimeIntervalLengthType getTimeInterval() {
        return timeInterval;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTimeInterval(TimeIntervalLengthType newTimeInterval, NotificationChain msgs) {
        TimeIntervalLengthType oldTimeInterval = timeInterval;
        timeInterval = newTimeInterval;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_PERIOD_TYPE__TIME_INTERVAL, oldTimeInterval, newTimeInterval);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTimeInterval(TimeIntervalLengthType newTimeInterval) {
        if (newTimeInterval != timeInterval) {
            NotificationChain msgs = null;
            if (timeInterval != null)
                msgs = ((InternalEObject)timeInterval).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_PERIOD_TYPE__TIME_INTERVAL, null, msgs);
            if (newTimeInterval != null)
                msgs = ((InternalEObject)newTimeInterval).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_PERIOD_TYPE__TIME_INTERVAL, null, msgs);
            msgs = basicSetTimeInterval(newTimeInterval, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_PERIOD_TYPE__TIME_INTERVAL, newTimeInterval, newTimeInterval));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.TIME_PERIOD_TYPE__BEGIN_POSITION:
                return basicSetBeginPosition(null, msgs);
            case Gml311Package.TIME_PERIOD_TYPE__BEGIN:
                return basicSetBegin(null, msgs);
            case Gml311Package.TIME_PERIOD_TYPE__END_POSITION:
                return basicSetEndPosition(null, msgs);
            case Gml311Package.TIME_PERIOD_TYPE__END:
                return basicSetEnd(null, msgs);
            case Gml311Package.TIME_PERIOD_TYPE__TIME_INTERVAL:
                return basicSetTimeInterval(null, msgs);
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
            case Gml311Package.TIME_PERIOD_TYPE__BEGIN_POSITION:
                return getBeginPosition();
            case Gml311Package.TIME_PERIOD_TYPE__BEGIN:
                return getBegin();
            case Gml311Package.TIME_PERIOD_TYPE__END_POSITION:
                return getEndPosition();
            case Gml311Package.TIME_PERIOD_TYPE__END:
                return getEnd();
            case Gml311Package.TIME_PERIOD_TYPE__DURATION:
                return getDuration();
            case Gml311Package.TIME_PERIOD_TYPE__TIME_INTERVAL:
                return getTimeInterval();
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
            case Gml311Package.TIME_PERIOD_TYPE__BEGIN_POSITION:
                setBeginPosition((TimePositionType)newValue);
                return;
            case Gml311Package.TIME_PERIOD_TYPE__BEGIN:
                setBegin((TimeInstantPropertyType)newValue);
                return;
            case Gml311Package.TIME_PERIOD_TYPE__END_POSITION:
                setEndPosition((TimePositionType)newValue);
                return;
            case Gml311Package.TIME_PERIOD_TYPE__END:
                setEnd((TimeInstantPropertyType)newValue);
                return;
            case Gml311Package.TIME_PERIOD_TYPE__DURATION:
                setDuration((Duration) newValue);
                return;
            case Gml311Package.TIME_PERIOD_TYPE__TIME_INTERVAL:
                setTimeInterval((TimeIntervalLengthType)newValue);
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
            case Gml311Package.TIME_PERIOD_TYPE__BEGIN_POSITION:
                setBeginPosition((TimePositionType)null);
                return;
            case Gml311Package.TIME_PERIOD_TYPE__BEGIN:
                setBegin((TimeInstantPropertyType)null);
                return;
            case Gml311Package.TIME_PERIOD_TYPE__END_POSITION:
                setEndPosition((TimePositionType)null);
                return;
            case Gml311Package.TIME_PERIOD_TYPE__END:
                setEnd((TimeInstantPropertyType)null);
                return;
            case Gml311Package.TIME_PERIOD_TYPE__DURATION:
                setDuration(DURATION_EDEFAULT);
                return;
            case Gml311Package.TIME_PERIOD_TYPE__TIME_INTERVAL:
                setTimeInterval((TimeIntervalLengthType)null);
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
            case Gml311Package.TIME_PERIOD_TYPE__BEGIN_POSITION:
                return beginPosition != null;
            case Gml311Package.TIME_PERIOD_TYPE__BEGIN:
                return begin != null;
            case Gml311Package.TIME_PERIOD_TYPE__END_POSITION:
                return endPosition != null;
            case Gml311Package.TIME_PERIOD_TYPE__END:
                return end != null;
            case Gml311Package.TIME_PERIOD_TYPE__DURATION:
                return DURATION_EDEFAULT == null ? duration != null : !DURATION_EDEFAULT.equals(duration);
            case Gml311Package.TIME_PERIOD_TYPE__TIME_INTERVAL:
                return timeInterval != null;
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
        result.append(" (duration: ");
        result.append(duration);
        result.append(')');
        return result.toString();
    }

} //TimePeriodTypeImpl
