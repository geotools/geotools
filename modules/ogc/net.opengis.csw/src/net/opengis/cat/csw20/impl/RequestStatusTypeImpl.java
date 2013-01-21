/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.util.Calendar;
import javax.xml.datatype.XMLGregorianCalendar;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.RequestStatusType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Request Status Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.RequestStatusTypeImpl#getTimestamp <em>Timestamp</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RequestStatusTypeImpl extends EObjectImpl implements RequestStatusType {
    /**
     * The default value of the '{@link #getTimestamp() <em>Timestamp</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTimestamp()
     * @generated
     * @ordered
     */
    protected static final Calendar TIMESTAMP_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTimestamp() <em>Timestamp</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTimestamp()
     * @generated
     * @ordered
     */
    protected Calendar timestamp = TIMESTAMP_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RequestStatusTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.REQUEST_STATUS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Calendar getTimestamp() {
        return timestamp;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTimestamp(Calendar newTimestamp) {
        Calendar oldTimestamp = timestamp;
        timestamp = newTimestamp;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.REQUEST_STATUS_TYPE__TIMESTAMP, oldTimestamp, timestamp));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Csw20Package.REQUEST_STATUS_TYPE__TIMESTAMP:
                return getTimestamp();
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
            case Csw20Package.REQUEST_STATUS_TYPE__TIMESTAMP:
                setTimestamp((Calendar)newValue);
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
            case Csw20Package.REQUEST_STATUS_TYPE__TIMESTAMP:
                setTimestamp(TIMESTAMP_EDEFAULT);
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
            case Csw20Package.REQUEST_STATUS_TYPE__TIMESTAMP:
                return TIMESTAMP_EDEFAULT == null ? timestamp != null : !TIMESTAMP_EDEFAULT.equals(timestamp);
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
        result.append(" (timestamp: ");
        result.append(timestamp);
        result.append(')');
        return result.toString();
    }

} //RequestStatusTypeImpl
