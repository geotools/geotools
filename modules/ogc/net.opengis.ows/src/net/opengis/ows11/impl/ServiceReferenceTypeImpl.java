/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11.impl;

import net.opengis.ows11.Ows11Package;
import net.opengis.ows11.ServiceReferenceType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Service Reference Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows11.impl.ServiceReferenceTypeImpl#getRequestMessage <em>Request Message</em>}</li>
 *   <li>{@link net.opengis.ows11.impl.ServiceReferenceTypeImpl#getRequestMessageReference <em>Request Message Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ServiceReferenceTypeImpl extends ReferenceTypeImpl implements ServiceReferenceType {
    /**
     * The cached value of the '{@link #getRequestMessage() <em>Request Message</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRequestMessage()
     * @generated
     * @ordered
     */
    protected EObject requestMessage;

    /**
     * The default value of the '{@link #getRequestMessageReference() <em>Request Message Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRequestMessageReference()
     * @generated
     * @ordered
     */
    protected static final String REQUEST_MESSAGE_REFERENCE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRequestMessageReference() <em>Request Message Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRequestMessageReference()
     * @generated
     * @ordered
     */
    protected String requestMessageReference = REQUEST_MESSAGE_REFERENCE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ServiceReferenceTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Ows11Package.Literals.SERVICE_REFERENCE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getRequestMessage() {
        return requestMessage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRequestMessage(EObject newRequestMessage, NotificationChain msgs) {
        EObject oldRequestMessage = requestMessage;
        requestMessage = newRequestMessage;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows11Package.SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE, oldRequestMessage, newRequestMessage);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRequestMessage(EObject newRequestMessage) {
        if (newRequestMessage != requestMessage) {
            NotificationChain msgs = null;
            if (requestMessage != null)
                msgs = ((InternalEObject)requestMessage).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows11Package.SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE, null, msgs);
            if (newRequestMessage != null)
                msgs = ((InternalEObject)newRequestMessage).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows11Package.SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE, null, msgs);
            msgs = basicSetRequestMessage(newRequestMessage, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE, newRequestMessage, newRequestMessage));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getRequestMessageReference() {
        return requestMessageReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRequestMessageReference(String newRequestMessageReference) {
        String oldRequestMessageReference = requestMessageReference;
        requestMessageReference = newRequestMessageReference;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows11Package.SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE_REFERENCE, oldRequestMessageReference, requestMessageReference));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows11Package.SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE:
                return basicSetRequestMessage(null, msgs);
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
            case Ows11Package.SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE:
                return getRequestMessage();
            case Ows11Package.SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE_REFERENCE:
                return getRequestMessageReference();
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
            case Ows11Package.SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE:
                setRequestMessage((EObject)newValue);
                return;
            case Ows11Package.SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE_REFERENCE:
                setRequestMessageReference((String)newValue);
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
            case Ows11Package.SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE:
                setRequestMessage((EObject)null);
                return;
            case Ows11Package.SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE_REFERENCE:
                setRequestMessageReference(REQUEST_MESSAGE_REFERENCE_EDEFAULT);
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
            case Ows11Package.SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE:
                return requestMessage != null;
            case Ows11Package.SERVICE_REFERENCE_TYPE__REQUEST_MESSAGE_REFERENCE:
                return REQUEST_MESSAGE_REFERENCE_EDEFAULT == null ? requestMessageReference != null : !REQUEST_MESSAGE_REFERENCE_EDEFAULT.equals(requestMessageReference);
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
        result.append(" (requestMessageReference: ");
        result.append(requestMessageReference);
        result.append(')');
        return result.toString();
    }

} //ServiceReferenceTypeImpl
