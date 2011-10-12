/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.math.BigInteger;

import java.util.Calendar;
import javax.xml.datatype.XMLGregorianCalendar;

import net.opengis.wfs20.AdditionalObjectsType;
import net.opengis.wfs20.FeatureCollectionType;
import net.opengis.wfs20.TruncatedResponseType;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Collection Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.FeatureCollectionTypeImpl#getAdditionalObjects <em>Additional Objects</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureCollectionTypeImpl#getTruncatedResponse <em>Truncated Response</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureCollectionTypeImpl#getLockId <em>Lock Id</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureCollectionTypeImpl#getNext <em>Next</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureCollectionTypeImpl#getNumberMatched <em>Number Matched</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureCollectionTypeImpl#getNumberReturned <em>Number Returned</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureCollectionTypeImpl#getPrevious <em>Previous</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.FeatureCollectionTypeImpl#getTimeStamp <em>Time Stamp</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FeatureCollectionTypeImpl extends SimpleFeatureCollectionTypeImpl implements FeatureCollectionType {
    /**
     * The cached value of the '{@link #getAdditionalObjects() <em>Additional Objects</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAdditionalObjects()
     * @generated
     * @ordered
     */
    protected AdditionalObjectsType additionalObjects;

    /**
     * The cached value of the '{@link #getTruncatedResponse() <em>Truncated Response</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTruncatedResponse()
     * @generated
     * @ordered
     */
    protected TruncatedResponseType truncatedResponse;

    /**
     * The default value of the '{@link #getLockId() <em>Lock Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLockId()
     * @generated
     * @ordered
     */
    protected static final String LOCK_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLockId() <em>Lock Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLockId()
     * @generated
     * @ordered
     */
    protected String lockId = LOCK_ID_EDEFAULT;

    /**
     * The default value of the '{@link #getNext() <em>Next</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNext()
     * @generated
     * @ordered
     */
    protected static final String NEXT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getNext() <em>Next</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNext()
     * @generated
     * @ordered
     */
    protected String next = NEXT_EDEFAULT;

    /**
     * The default value of the '{@link #getNumberMatched() <em>Number Matched</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNumberMatched()
     * @generated
     * @ordered
     */
    protected static final Object NUMBER_MATCHED_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getNumberMatched() <em>Number Matched</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNumberMatched()
     * @generated
     * @ordered
     */
    protected Object numberMatched = NUMBER_MATCHED_EDEFAULT;

    /**
     * The default value of the '{@link #getNumberReturned() <em>Number Returned</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNumberReturned()
     * @generated
     * @ordered
     */
    protected static final BigInteger NUMBER_RETURNED_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getNumberReturned() <em>Number Returned</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNumberReturned()
     * @generated
     * @ordered
     */
    protected BigInteger numberReturned = NUMBER_RETURNED_EDEFAULT;

    /**
     * The default value of the '{@link #getPrevious() <em>Previous</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPrevious()
     * @generated
     * @ordered
     */
    protected static final String PREVIOUS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPrevious() <em>Previous</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPrevious()
     * @generated
     * @ordered
     */
    protected String previous = PREVIOUS_EDEFAULT;

    /**
     * The default value of the '{@link #getTimeStamp() <em>Time Stamp</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTimeStamp()
     * @generated
     * @ordered
     */
    protected static final Calendar TIME_STAMP_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTimeStamp() <em>Time Stamp</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTimeStamp()
     * @generated
     * @ordered
     */
    protected Calendar timeStamp = TIME_STAMP_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FeatureCollectionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.FEATURE_COLLECTION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AdditionalObjectsType getAdditionalObjects() {
        return additionalObjects;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetAdditionalObjects(AdditionalObjectsType newAdditionalObjects, NotificationChain msgs) {
        AdditionalObjectsType oldAdditionalObjects = additionalObjects;
        additionalObjects = newAdditionalObjects;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_COLLECTION_TYPE__ADDITIONAL_OBJECTS, oldAdditionalObjects, newAdditionalObjects);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAdditionalObjects(AdditionalObjectsType newAdditionalObjects) {
        if (newAdditionalObjects != additionalObjects) {
            NotificationChain msgs = null;
            if (additionalObjects != null)
                msgs = ((InternalEObject)additionalObjects).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.FEATURE_COLLECTION_TYPE__ADDITIONAL_OBJECTS, null, msgs);
            if (newAdditionalObjects != null)
                msgs = ((InternalEObject)newAdditionalObjects).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.FEATURE_COLLECTION_TYPE__ADDITIONAL_OBJECTS, null, msgs);
            msgs = basicSetAdditionalObjects(newAdditionalObjects, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_COLLECTION_TYPE__ADDITIONAL_OBJECTS, newAdditionalObjects, newAdditionalObjects));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TruncatedResponseType getTruncatedResponse() {
        return truncatedResponse;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetTruncatedResponse(TruncatedResponseType newTruncatedResponse, NotificationChain msgs) {
        TruncatedResponseType oldTruncatedResponse = truncatedResponse;
        truncatedResponse = newTruncatedResponse;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_COLLECTION_TYPE__TRUNCATED_RESPONSE, oldTruncatedResponse, newTruncatedResponse);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTruncatedResponse(TruncatedResponseType newTruncatedResponse) {
        if (newTruncatedResponse != truncatedResponse) {
            NotificationChain msgs = null;
            if (truncatedResponse != null)
                msgs = ((InternalEObject)truncatedResponse).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.FEATURE_COLLECTION_TYPE__TRUNCATED_RESPONSE, null, msgs);
            if (newTruncatedResponse != null)
                msgs = ((InternalEObject)newTruncatedResponse).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wfs20Package.FEATURE_COLLECTION_TYPE__TRUNCATED_RESPONSE, null, msgs);
            msgs = basicSetTruncatedResponse(newTruncatedResponse, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_COLLECTION_TYPE__TRUNCATED_RESPONSE, newTruncatedResponse, newTruncatedResponse));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLockId() {
        return lockId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLockId(String newLockId) {
        String oldLockId = lockId;
        lockId = newLockId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_COLLECTION_TYPE__LOCK_ID, oldLockId, lockId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getNext() {
        return next;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNext(String newNext) {
        String oldNext = next;
        next = newNext;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_COLLECTION_TYPE__NEXT, oldNext, next));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getNumberMatched() {
        return numberMatched;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNumberMatched(Object newNumberMatched) {
        Object oldNumberMatched = numberMatched;
        numberMatched = newNumberMatched;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_COLLECTION_TYPE__NUMBER_MATCHED, oldNumberMatched, numberMatched));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getNumberReturned() {
        return numberReturned;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNumberReturned(BigInteger newNumberReturned) {
        BigInteger oldNumberReturned = numberReturned;
        numberReturned = newNumberReturned;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_COLLECTION_TYPE__NUMBER_RETURNED, oldNumberReturned, numberReturned));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPrevious() {
        return previous;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPrevious(String newPrevious) {
        String oldPrevious = previous;
        previous = newPrevious;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_COLLECTION_TYPE__PREVIOUS, oldPrevious, previous));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Calendar getTimeStamp() {
        return timeStamp;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTimeStamp(Calendar newTimeStamp) {
        Calendar oldTimeStamp = timeStamp;
        timeStamp = newTimeStamp;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.FEATURE_COLLECTION_TYPE__TIME_STAMP, oldTimeStamp, timeStamp));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wfs20Package.FEATURE_COLLECTION_TYPE__ADDITIONAL_OBJECTS:
                return basicSetAdditionalObjects(null, msgs);
            case Wfs20Package.FEATURE_COLLECTION_TYPE__TRUNCATED_RESPONSE:
                return basicSetTruncatedResponse(null, msgs);
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
            case Wfs20Package.FEATURE_COLLECTION_TYPE__ADDITIONAL_OBJECTS:
                return getAdditionalObjects();
            case Wfs20Package.FEATURE_COLLECTION_TYPE__TRUNCATED_RESPONSE:
                return getTruncatedResponse();
            case Wfs20Package.FEATURE_COLLECTION_TYPE__LOCK_ID:
                return getLockId();
            case Wfs20Package.FEATURE_COLLECTION_TYPE__NEXT:
                return getNext();
            case Wfs20Package.FEATURE_COLLECTION_TYPE__NUMBER_MATCHED:
                return getNumberMatched();
            case Wfs20Package.FEATURE_COLLECTION_TYPE__NUMBER_RETURNED:
                return getNumberReturned();
            case Wfs20Package.FEATURE_COLLECTION_TYPE__PREVIOUS:
                return getPrevious();
            case Wfs20Package.FEATURE_COLLECTION_TYPE__TIME_STAMP:
                return getTimeStamp();
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
            case Wfs20Package.FEATURE_COLLECTION_TYPE__ADDITIONAL_OBJECTS:
                setAdditionalObjects((AdditionalObjectsType)newValue);
                return;
            case Wfs20Package.FEATURE_COLLECTION_TYPE__TRUNCATED_RESPONSE:
                setTruncatedResponse((TruncatedResponseType)newValue);
                return;
            case Wfs20Package.FEATURE_COLLECTION_TYPE__LOCK_ID:
                setLockId((String)newValue);
                return;
            case Wfs20Package.FEATURE_COLLECTION_TYPE__NEXT:
                setNext((String)newValue);
                return;
            case Wfs20Package.FEATURE_COLLECTION_TYPE__NUMBER_MATCHED:
                setNumberMatched(newValue);
                return;
            case Wfs20Package.FEATURE_COLLECTION_TYPE__NUMBER_RETURNED:
                setNumberReturned((BigInteger)newValue);
                return;
            case Wfs20Package.FEATURE_COLLECTION_TYPE__PREVIOUS:
                setPrevious((String)newValue);
                return;
            case Wfs20Package.FEATURE_COLLECTION_TYPE__TIME_STAMP:
                setTimeStamp((Calendar)newValue);
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
            case Wfs20Package.FEATURE_COLLECTION_TYPE__ADDITIONAL_OBJECTS:
                setAdditionalObjects((AdditionalObjectsType)null);
                return;
            case Wfs20Package.FEATURE_COLLECTION_TYPE__TRUNCATED_RESPONSE:
                setTruncatedResponse((TruncatedResponseType)null);
                return;
            case Wfs20Package.FEATURE_COLLECTION_TYPE__LOCK_ID:
                setLockId(LOCK_ID_EDEFAULT);
                return;
            case Wfs20Package.FEATURE_COLLECTION_TYPE__NEXT:
                setNext(NEXT_EDEFAULT);
                return;
            case Wfs20Package.FEATURE_COLLECTION_TYPE__NUMBER_MATCHED:
                setNumberMatched(NUMBER_MATCHED_EDEFAULT);
                return;
            case Wfs20Package.FEATURE_COLLECTION_TYPE__NUMBER_RETURNED:
                setNumberReturned(NUMBER_RETURNED_EDEFAULT);
                return;
            case Wfs20Package.FEATURE_COLLECTION_TYPE__PREVIOUS:
                setPrevious(PREVIOUS_EDEFAULT);
                return;
            case Wfs20Package.FEATURE_COLLECTION_TYPE__TIME_STAMP:
                setTimeStamp(TIME_STAMP_EDEFAULT);
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
            case Wfs20Package.FEATURE_COLLECTION_TYPE__ADDITIONAL_OBJECTS:
                return additionalObjects != null;
            case Wfs20Package.FEATURE_COLLECTION_TYPE__TRUNCATED_RESPONSE:
                return truncatedResponse != null;
            case Wfs20Package.FEATURE_COLLECTION_TYPE__LOCK_ID:
                return LOCK_ID_EDEFAULT == null ? lockId != null : !LOCK_ID_EDEFAULT.equals(lockId);
            case Wfs20Package.FEATURE_COLLECTION_TYPE__NEXT:
                return NEXT_EDEFAULT == null ? next != null : !NEXT_EDEFAULT.equals(next);
            case Wfs20Package.FEATURE_COLLECTION_TYPE__NUMBER_MATCHED:
                return NUMBER_MATCHED_EDEFAULT == null ? numberMatched != null : !NUMBER_MATCHED_EDEFAULT.equals(numberMatched);
            case Wfs20Package.FEATURE_COLLECTION_TYPE__NUMBER_RETURNED:
                return NUMBER_RETURNED_EDEFAULT == null ? numberReturned != null : !NUMBER_RETURNED_EDEFAULT.equals(numberReturned);
            case Wfs20Package.FEATURE_COLLECTION_TYPE__PREVIOUS:
                return PREVIOUS_EDEFAULT == null ? previous != null : !PREVIOUS_EDEFAULT.equals(previous);
            case Wfs20Package.FEATURE_COLLECTION_TYPE__TIME_STAMP:
                return TIME_STAMP_EDEFAULT == null ? timeStamp != null : !TIME_STAMP_EDEFAULT.equals(timeStamp);
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
        result.append(" (lockId: ");
        result.append(lockId);
        result.append(", next: ");
        result.append(next);
        result.append(", numberMatched: ");
        result.append(numberMatched);
        result.append(", numberReturned: ");
        result.append(numberReturned);
        result.append(", previous: ");
        result.append(previous);
        result.append(", timeStamp: ");
        result.append(timeStamp);
        result.append(')');
        return result.toString();
    }

} //FeatureCollectionTypeImpl
