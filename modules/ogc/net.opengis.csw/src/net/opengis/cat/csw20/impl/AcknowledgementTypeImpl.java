/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import java.util.Calendar;

import net.opengis.cat.csw20.AcknowledgementType;
import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.EchoedRequestType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Acknowledgement Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.AcknowledgementTypeImpl#getEchoedRequest <em>Echoed Request</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.AcknowledgementTypeImpl#getRequestId <em>Request Id</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.AcknowledgementTypeImpl#getTimeStamp <em>Time Stamp</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AcknowledgementTypeImpl extends EObjectImpl implements AcknowledgementType {
    /**
     * The cached value of the '{@link #getEchoedRequest() <em>Echoed Request</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEchoedRequest()
     * @generated
     * @ordered
     */
    protected EchoedRequestType echoedRequest;

    /**
     * The default value of the '{@link #getRequestId() <em>Request Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRequestId()
     * @generated
     * @ordered
     */
    protected static final String REQUEST_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRequestId() <em>Request Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRequestId()
     * @generated
     * @ordered
     */
    protected String requestId = REQUEST_ID_EDEFAULT;

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
    protected AcknowledgementTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.ACKNOWLEDGEMENT_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EchoedRequestType getEchoedRequest() {
        return echoedRequest;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetEchoedRequest(EchoedRequestType newEchoedRequest, NotificationChain msgs) {
        EchoedRequestType oldEchoedRequest = echoedRequest;
        echoedRequest = newEchoedRequest;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.ACKNOWLEDGEMENT_TYPE__ECHOED_REQUEST, oldEchoedRequest, newEchoedRequest);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEchoedRequest(EchoedRequestType newEchoedRequest) {
        if (newEchoedRequest != echoedRequest) {
            NotificationChain msgs = null;
            if (echoedRequest != null)
                msgs = ((InternalEObject)echoedRequest).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.ACKNOWLEDGEMENT_TYPE__ECHOED_REQUEST, null, msgs);
            if (newEchoedRequest != null)
                msgs = ((InternalEObject)newEchoedRequest).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.ACKNOWLEDGEMENT_TYPE__ECHOED_REQUEST, null, msgs);
            msgs = basicSetEchoedRequest(newEchoedRequest, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.ACKNOWLEDGEMENT_TYPE__ECHOED_REQUEST, newEchoedRequest, newEchoedRequest));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRequestId(String newRequestId) {
        String oldRequestId = requestId;
        requestId = newRequestId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.ACKNOWLEDGEMENT_TYPE__REQUEST_ID, oldRequestId, requestId));
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
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.ACKNOWLEDGEMENT_TYPE__TIME_STAMP, oldTimeStamp, timeStamp));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.ACKNOWLEDGEMENT_TYPE__ECHOED_REQUEST:
                return basicSetEchoedRequest(null, msgs);
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
            case Csw20Package.ACKNOWLEDGEMENT_TYPE__ECHOED_REQUEST:
                return getEchoedRequest();
            case Csw20Package.ACKNOWLEDGEMENT_TYPE__REQUEST_ID:
                return getRequestId();
            case Csw20Package.ACKNOWLEDGEMENT_TYPE__TIME_STAMP:
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
            case Csw20Package.ACKNOWLEDGEMENT_TYPE__ECHOED_REQUEST:
                setEchoedRequest((EchoedRequestType)newValue);
                return;
            case Csw20Package.ACKNOWLEDGEMENT_TYPE__REQUEST_ID:
                setRequestId((String)newValue);
                return;
            case Csw20Package.ACKNOWLEDGEMENT_TYPE__TIME_STAMP:
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
            case Csw20Package.ACKNOWLEDGEMENT_TYPE__ECHOED_REQUEST:
                setEchoedRequest((EchoedRequestType)null);
                return;
            case Csw20Package.ACKNOWLEDGEMENT_TYPE__REQUEST_ID:
                setRequestId(REQUEST_ID_EDEFAULT);
                return;
            case Csw20Package.ACKNOWLEDGEMENT_TYPE__TIME_STAMP:
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
            case Csw20Package.ACKNOWLEDGEMENT_TYPE__ECHOED_REQUEST:
                return echoedRequest != null;
            case Csw20Package.ACKNOWLEDGEMENT_TYPE__REQUEST_ID:
                return REQUEST_ID_EDEFAULT == null ? requestId != null : !REQUEST_ID_EDEFAULT.equals(requestId);
            case Csw20Package.ACKNOWLEDGEMENT_TYPE__TIME_STAMP:
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
        result.append(" (requestId: ");
        result.append(requestId);
        result.append(", timeStamp: ");
        result.append(timeStamp);
        result.append(')');
        return result.toString();
    }

} //AcknowledgementTypeImpl
