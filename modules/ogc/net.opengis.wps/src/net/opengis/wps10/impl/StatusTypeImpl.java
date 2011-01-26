/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import javax.xml.datatype.XMLGregorianCalendar;

import net.opengis.wps10.ProcessFailedType;
import net.opengis.wps10.ProcessStartedType;
import net.opengis.wps10.StatusType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Status Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.StatusTypeImpl#getProcessAccepted <em>Process Accepted</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.StatusTypeImpl#getProcessStarted <em>Process Started</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.StatusTypeImpl#getProcessPaused <em>Process Paused</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.StatusTypeImpl#getProcessSucceeded <em>Process Succeeded</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.StatusTypeImpl#getProcessFailed <em>Process Failed</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.StatusTypeImpl#getCreationTime <em>Creation Time</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StatusTypeImpl extends EObjectImpl implements StatusType {
    /**
     * The default value of the '{@link #getProcessAccepted() <em>Process Accepted</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProcessAccepted()
     * @generated
     * @ordered
     */
    protected static final String PROCESS_ACCEPTED_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProcessAccepted() <em>Process Accepted</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProcessAccepted()
     * @generated
     * @ordered
     */
    protected String processAccepted = PROCESS_ACCEPTED_EDEFAULT;

    /**
     * The cached value of the '{@link #getProcessStarted() <em>Process Started</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProcessStarted()
     * @generated
     * @ordered
     */
    protected ProcessStartedType processStarted;

    /**
     * The cached value of the '{@link #getProcessPaused() <em>Process Paused</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProcessPaused()
     * @generated
     * @ordered
     */
    protected ProcessStartedType processPaused;

    /**
     * The default value of the '{@link #getProcessSucceeded() <em>Process Succeeded</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProcessSucceeded()
     * @generated
     * @ordered
     */
    protected static final String PROCESS_SUCCEEDED_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProcessSucceeded() <em>Process Succeeded</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProcessSucceeded()
     * @generated
     * @ordered
     */
    protected String processSucceeded = PROCESS_SUCCEEDED_EDEFAULT;

    /**
     * The cached value of the '{@link #getProcessFailed() <em>Process Failed</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProcessFailed()
     * @generated
     * @ordered
     */
    protected ProcessFailedType processFailed;

    /**
     * The default value of the '{@link #getCreationTime() <em>Creation Time</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCreationTime()
     * @generated
     * @ordered
     */
    protected static final Object CREATION_TIME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCreationTime() <em>Creation Time</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCreationTime()
     * @generated
     * @ordered
     */
    protected Object creationTime = CREATION_TIME_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected StatusTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.STATUS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProcessAccepted() {
        return processAccepted;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProcessAccepted(String newProcessAccepted) {
        String oldProcessAccepted = processAccepted;
        processAccepted = newProcessAccepted;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.STATUS_TYPE__PROCESS_ACCEPTED, oldProcessAccepted, processAccepted));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProcessStartedType getProcessStarted() {
        return processStarted;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetProcessStarted(ProcessStartedType newProcessStarted, NotificationChain msgs) {
        ProcessStartedType oldProcessStarted = processStarted;
        processStarted = newProcessStarted;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.STATUS_TYPE__PROCESS_STARTED, oldProcessStarted, newProcessStarted);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProcessStarted(ProcessStartedType newProcessStarted) {
        if (newProcessStarted != processStarted) {
            NotificationChain msgs = null;
            if (processStarted != null)
                msgs = ((InternalEObject)processStarted).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.STATUS_TYPE__PROCESS_STARTED, null, msgs);
            if (newProcessStarted != null)
                msgs = ((InternalEObject)newProcessStarted).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.STATUS_TYPE__PROCESS_STARTED, null, msgs);
            msgs = basicSetProcessStarted(newProcessStarted, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.STATUS_TYPE__PROCESS_STARTED, newProcessStarted, newProcessStarted));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProcessStartedType getProcessPaused() {
        return processPaused;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetProcessPaused(ProcessStartedType newProcessPaused, NotificationChain msgs) {
        ProcessStartedType oldProcessPaused = processPaused;
        processPaused = newProcessPaused;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.STATUS_TYPE__PROCESS_PAUSED, oldProcessPaused, newProcessPaused);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProcessPaused(ProcessStartedType newProcessPaused) {
        if (newProcessPaused != processPaused) {
            NotificationChain msgs = null;
            if (processPaused != null)
                msgs = ((InternalEObject)processPaused).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.STATUS_TYPE__PROCESS_PAUSED, null, msgs);
            if (newProcessPaused != null)
                msgs = ((InternalEObject)newProcessPaused).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.STATUS_TYPE__PROCESS_PAUSED, null, msgs);
            msgs = basicSetProcessPaused(newProcessPaused, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.STATUS_TYPE__PROCESS_PAUSED, newProcessPaused, newProcessPaused));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProcessSucceeded() {
        return processSucceeded;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProcessSucceeded(String newProcessSucceeded) {
        String oldProcessSucceeded = processSucceeded;
        processSucceeded = newProcessSucceeded;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.STATUS_TYPE__PROCESS_SUCCEEDED, oldProcessSucceeded, processSucceeded));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProcessFailedType getProcessFailed() {
        return processFailed;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetProcessFailed(ProcessFailedType newProcessFailed, NotificationChain msgs) {
        ProcessFailedType oldProcessFailed = processFailed;
        processFailed = newProcessFailed;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.STATUS_TYPE__PROCESS_FAILED, oldProcessFailed, newProcessFailed);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProcessFailed(ProcessFailedType newProcessFailed) {
        if (newProcessFailed != processFailed) {
            NotificationChain msgs = null;
            if (processFailed != null)
                msgs = ((InternalEObject)processFailed).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.STATUS_TYPE__PROCESS_FAILED, null, msgs);
            if (newProcessFailed != null)
                msgs = ((InternalEObject)newProcessFailed).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.STATUS_TYPE__PROCESS_FAILED, null, msgs);
            msgs = basicSetProcessFailed(newProcessFailed, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.STATUS_TYPE__PROCESS_FAILED, newProcessFailed, newProcessFailed));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object getCreationTime() {
        return creationTime;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCreationTime(Object newCreationTime) {
        Object oldCreationTime = creationTime;
        creationTime = newCreationTime;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.STATUS_TYPE__CREATION_TIME, oldCreationTime, creationTime));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.STATUS_TYPE__PROCESS_STARTED:
                return basicSetProcessStarted(null, msgs);
            case Wps10Package.STATUS_TYPE__PROCESS_PAUSED:
                return basicSetProcessPaused(null, msgs);
            case Wps10Package.STATUS_TYPE__PROCESS_FAILED:
                return basicSetProcessFailed(null, msgs);
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
            case Wps10Package.STATUS_TYPE__PROCESS_ACCEPTED:
                return getProcessAccepted();
            case Wps10Package.STATUS_TYPE__PROCESS_STARTED:
                return getProcessStarted();
            case Wps10Package.STATUS_TYPE__PROCESS_PAUSED:
                return getProcessPaused();
            case Wps10Package.STATUS_TYPE__PROCESS_SUCCEEDED:
                return getProcessSucceeded();
            case Wps10Package.STATUS_TYPE__PROCESS_FAILED:
                return getProcessFailed();
            case Wps10Package.STATUS_TYPE__CREATION_TIME:
                return getCreationTime();
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
            case Wps10Package.STATUS_TYPE__PROCESS_ACCEPTED:
                setProcessAccepted((String)newValue);
                return;
            case Wps10Package.STATUS_TYPE__PROCESS_STARTED:
                setProcessStarted((ProcessStartedType)newValue);
                return;
            case Wps10Package.STATUS_TYPE__PROCESS_PAUSED:
                setProcessPaused((ProcessStartedType)newValue);
                return;
            case Wps10Package.STATUS_TYPE__PROCESS_SUCCEEDED:
                setProcessSucceeded((String)newValue);
                return;
            case Wps10Package.STATUS_TYPE__PROCESS_FAILED:
                setProcessFailed((ProcessFailedType)newValue);
                return;
            case Wps10Package.STATUS_TYPE__CREATION_TIME:
                setCreationTime(newValue);
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
            case Wps10Package.STATUS_TYPE__PROCESS_ACCEPTED:
                setProcessAccepted(PROCESS_ACCEPTED_EDEFAULT);
                return;
            case Wps10Package.STATUS_TYPE__PROCESS_STARTED:
                setProcessStarted((ProcessStartedType)null);
                return;
            case Wps10Package.STATUS_TYPE__PROCESS_PAUSED:
                setProcessPaused((ProcessStartedType)null);
                return;
            case Wps10Package.STATUS_TYPE__PROCESS_SUCCEEDED:
                setProcessSucceeded(PROCESS_SUCCEEDED_EDEFAULT);
                return;
            case Wps10Package.STATUS_TYPE__PROCESS_FAILED:
                setProcessFailed((ProcessFailedType)null);
                return;
            case Wps10Package.STATUS_TYPE__CREATION_TIME:
                setCreationTime(CREATION_TIME_EDEFAULT);
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
            case Wps10Package.STATUS_TYPE__PROCESS_ACCEPTED:
                return PROCESS_ACCEPTED_EDEFAULT == null ? processAccepted != null : !PROCESS_ACCEPTED_EDEFAULT.equals(processAccepted);
            case Wps10Package.STATUS_TYPE__PROCESS_STARTED:
                return processStarted != null;
            case Wps10Package.STATUS_TYPE__PROCESS_PAUSED:
                return processPaused != null;
            case Wps10Package.STATUS_TYPE__PROCESS_SUCCEEDED:
                return PROCESS_SUCCEEDED_EDEFAULT == null ? processSucceeded != null : !PROCESS_SUCCEEDED_EDEFAULT.equals(processSucceeded);
            case Wps10Package.STATUS_TYPE__PROCESS_FAILED:
                return processFailed != null;
            case Wps10Package.STATUS_TYPE__CREATION_TIME:
                return CREATION_TIME_EDEFAULT == null ? creationTime != null : !CREATION_TIME_EDEFAULT.equals(creationTime);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (processAccepted: ");
        result.append(processAccepted);
        result.append(", processSucceeded: ");
        result.append(processSucceeded);
        result.append(", creationTime: ");
        result.append(creationTime);
        result.append(')');
        return result.toString();
    }

} //StatusTypeImpl
