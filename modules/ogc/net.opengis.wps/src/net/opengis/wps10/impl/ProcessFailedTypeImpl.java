/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.ows11.ExceptionReportType;

import net.opengis.wps10.ProcessFailedType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Process Failed Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.ProcessFailedTypeImpl#getExceptionReport <em>Exception Report</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProcessFailedTypeImpl extends EObjectImpl implements ProcessFailedType {
    /**
     * The cached value of the '{@link #getExceptionReport() <em>Exception Report</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getExceptionReport()
     * @generated
     * @ordered
     */
    protected ExceptionReportType exceptionReport;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ProcessFailedTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.PROCESS_FAILED_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ExceptionReportType getExceptionReport() {
        return exceptionReport;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetExceptionReport(ExceptionReportType newExceptionReport, NotificationChain msgs) {
        ExceptionReportType oldExceptionReport = exceptionReport;
        exceptionReport = newExceptionReport;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.PROCESS_FAILED_TYPE__EXCEPTION_REPORT, oldExceptionReport, newExceptionReport);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExceptionReport(ExceptionReportType newExceptionReport) {
        if (newExceptionReport != exceptionReport) {
            NotificationChain msgs = null;
            if (exceptionReport != null)
                msgs = ((InternalEObject)exceptionReport).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.PROCESS_FAILED_TYPE__EXCEPTION_REPORT, null, msgs);
            if (newExceptionReport != null)
                msgs = ((InternalEObject)newExceptionReport).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.PROCESS_FAILED_TYPE__EXCEPTION_REPORT, null, msgs);
            msgs = basicSetExceptionReport(newExceptionReport, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.PROCESS_FAILED_TYPE__EXCEPTION_REPORT, newExceptionReport, newExceptionReport));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.PROCESS_FAILED_TYPE__EXCEPTION_REPORT:
                return basicSetExceptionReport(null, msgs);
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
            case Wps10Package.PROCESS_FAILED_TYPE__EXCEPTION_REPORT:
                return getExceptionReport();
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
            case Wps10Package.PROCESS_FAILED_TYPE__EXCEPTION_REPORT:
                setExceptionReport((ExceptionReportType)newValue);
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
            case Wps10Package.PROCESS_FAILED_TYPE__EXCEPTION_REPORT:
                setExceptionReport((ExceptionReportType)null);
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
            case Wps10Package.PROCESS_FAILED_TYPE__EXCEPTION_REPORT:
                return exceptionReport != null;
        }
        return super.eIsSet(featureID);
    }

} //ProcessFailedTypeImpl
