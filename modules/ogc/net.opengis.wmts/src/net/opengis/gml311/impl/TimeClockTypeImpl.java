/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import javax.xml.datatype.XMLGregorianCalendar;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.StringOrRefType;
import net.opengis.gml311.TimeCalendarPropertyType;
import net.opengis.gml311.TimeClockType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Time Clock Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.TimeClockTypeImpl#getReferenceEvent <em>Reference Event</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TimeClockTypeImpl#getReferenceTime <em>Reference Time</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TimeClockTypeImpl#getUtcReference <em>Utc Reference</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TimeClockTypeImpl#getDateBasis <em>Date Basis</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TimeClockTypeImpl extends AbstractTimeReferenceSystemTypeImpl implements TimeClockType {
    /**
     * The cached value of the '{@link #getReferenceEvent() <em>Reference Event</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReferenceEvent()
     * @generated
     * @ordered
     */
    protected StringOrRefType referenceEvent;

    /**
     * The default value of the '{@link #getReferenceTime() <em>Reference Time</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReferenceTime()
     * @generated
     * @ordered
     */
    protected static final XMLGregorianCalendar REFERENCE_TIME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getReferenceTime() <em>Reference Time</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReferenceTime()
     * @generated
     * @ordered
     */
    protected XMLGregorianCalendar referenceTime = REFERENCE_TIME_EDEFAULT;

    /**
     * The default value of the '{@link #getUtcReference() <em>Utc Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUtcReference()
     * @generated
     * @ordered
     */
    protected static final XMLGregorianCalendar UTC_REFERENCE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUtcReference() <em>Utc Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUtcReference()
     * @generated
     * @ordered
     */
    protected XMLGregorianCalendar utcReference = UTC_REFERENCE_EDEFAULT;

    /**
     * The cached value of the '{@link #getDateBasis() <em>Date Basis</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDateBasis()
     * @generated
     * @ordered
     */
    protected EList<TimeCalendarPropertyType> dateBasis;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TimeClockTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getTimeClockType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StringOrRefType getReferenceEvent() {
        return referenceEvent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetReferenceEvent(StringOrRefType newReferenceEvent, NotificationChain msgs) {
        StringOrRefType oldReferenceEvent = referenceEvent;
        referenceEvent = newReferenceEvent;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_CLOCK_TYPE__REFERENCE_EVENT, oldReferenceEvent, newReferenceEvent);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReferenceEvent(StringOrRefType newReferenceEvent) {
        if (newReferenceEvent != referenceEvent) {
            NotificationChain msgs = null;
            if (referenceEvent != null)
                msgs = ((InternalEObject)referenceEvent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_CLOCK_TYPE__REFERENCE_EVENT, null, msgs);
            if (newReferenceEvent != null)
                msgs = ((InternalEObject)newReferenceEvent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.TIME_CLOCK_TYPE__REFERENCE_EVENT, null, msgs);
            msgs = basicSetReferenceEvent(newReferenceEvent, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_CLOCK_TYPE__REFERENCE_EVENT, newReferenceEvent, newReferenceEvent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public XMLGregorianCalendar getReferenceTime() {
        return referenceTime;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReferenceTime(XMLGregorianCalendar newReferenceTime) {
        XMLGregorianCalendar oldReferenceTime = referenceTime;
        referenceTime = newReferenceTime;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_CLOCK_TYPE__REFERENCE_TIME, oldReferenceTime, referenceTime));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public XMLGregorianCalendar getUtcReference() {
        return utcReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUtcReference(XMLGregorianCalendar newUtcReference) {
        XMLGregorianCalendar oldUtcReference = utcReference;
        utcReference = newUtcReference;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.TIME_CLOCK_TYPE__UTC_REFERENCE, oldUtcReference, utcReference));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TimeCalendarPropertyType> getDateBasis() {
        if (dateBasis == null) {
            dateBasis = new EObjectContainmentEList<TimeCalendarPropertyType>(TimeCalendarPropertyType.class, this, Gml311Package.TIME_CLOCK_TYPE__DATE_BASIS);
        }
        return dateBasis;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.TIME_CLOCK_TYPE__REFERENCE_EVENT:
                return basicSetReferenceEvent(null, msgs);
            case Gml311Package.TIME_CLOCK_TYPE__DATE_BASIS:
                return ((InternalEList<?>)getDateBasis()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.TIME_CLOCK_TYPE__REFERENCE_EVENT:
                return getReferenceEvent();
            case Gml311Package.TIME_CLOCK_TYPE__REFERENCE_TIME:
                return getReferenceTime();
            case Gml311Package.TIME_CLOCK_TYPE__UTC_REFERENCE:
                return getUtcReference();
            case Gml311Package.TIME_CLOCK_TYPE__DATE_BASIS:
                return getDateBasis();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Gml311Package.TIME_CLOCK_TYPE__REFERENCE_EVENT:
                setReferenceEvent((StringOrRefType)newValue);
                return;
            case Gml311Package.TIME_CLOCK_TYPE__REFERENCE_TIME:
                setReferenceTime((XMLGregorianCalendar) newValue);
                return;
            case Gml311Package.TIME_CLOCK_TYPE__UTC_REFERENCE:
                setUtcReference((XMLGregorianCalendar) newValue);
                return;
            case Gml311Package.TIME_CLOCK_TYPE__DATE_BASIS:
                getDateBasis().clear();
                getDateBasis().addAll((Collection<? extends TimeCalendarPropertyType>)newValue);
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
            case Gml311Package.TIME_CLOCK_TYPE__REFERENCE_EVENT:
                setReferenceEvent((StringOrRefType)null);
                return;
            case Gml311Package.TIME_CLOCK_TYPE__REFERENCE_TIME:
                setReferenceTime(REFERENCE_TIME_EDEFAULT);
                return;
            case Gml311Package.TIME_CLOCK_TYPE__UTC_REFERENCE:
                setUtcReference(UTC_REFERENCE_EDEFAULT);
                return;
            case Gml311Package.TIME_CLOCK_TYPE__DATE_BASIS:
                getDateBasis().clear();
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
            case Gml311Package.TIME_CLOCK_TYPE__REFERENCE_EVENT:
                return referenceEvent != null;
            case Gml311Package.TIME_CLOCK_TYPE__REFERENCE_TIME:
                return REFERENCE_TIME_EDEFAULT == null ? referenceTime != null : !REFERENCE_TIME_EDEFAULT.equals(referenceTime);
            case Gml311Package.TIME_CLOCK_TYPE__UTC_REFERENCE:
                return UTC_REFERENCE_EDEFAULT == null ? utcReference != null : !UTC_REFERENCE_EDEFAULT.equals(utcReference);
            case Gml311Package.TIME_CLOCK_TYPE__DATE_BASIS:
                return dateBasis != null && !dateBasis.isEmpty();
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
        result.append(" (referenceTime: ");
        result.append(referenceTime);
        result.append(", utcReference: ");
        result.append(utcReference);
        result.append(')');
        return result.toString();
    }

} //TimeClockTypeImpl
