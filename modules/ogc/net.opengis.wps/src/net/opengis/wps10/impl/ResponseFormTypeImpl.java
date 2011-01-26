/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.wps10.OutputDefinitionType;
import net.opengis.wps10.ResponseDocumentType;
import net.opengis.wps10.ResponseFormType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Response Form Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.ResponseFormTypeImpl#getResponseDocument <em>Response Document</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.ResponseFormTypeImpl#getRawDataOutput <em>Raw Data Output</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResponseFormTypeImpl extends EObjectImpl implements ResponseFormType {
    /**
     * The cached value of the '{@link #getResponseDocument() <em>Response Document</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResponseDocument()
     * @generated
     * @ordered
     */
    protected ResponseDocumentType responseDocument;

    /**
     * The cached value of the '{@link #getRawDataOutput() <em>Raw Data Output</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRawDataOutput()
     * @generated
     * @ordered
     */
    protected OutputDefinitionType rawDataOutput;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ResponseFormTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.RESPONSE_FORM_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResponseDocumentType getResponseDocument() {
        return responseDocument;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetResponseDocument(ResponseDocumentType newResponseDocument, NotificationChain msgs) {
        ResponseDocumentType oldResponseDocument = responseDocument;
        responseDocument = newResponseDocument;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.RESPONSE_FORM_TYPE__RESPONSE_DOCUMENT, oldResponseDocument, newResponseDocument);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResponseDocument(ResponseDocumentType newResponseDocument) {
        if (newResponseDocument != responseDocument) {
            NotificationChain msgs = null;
            if (responseDocument != null)
                msgs = ((InternalEObject)responseDocument).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.RESPONSE_FORM_TYPE__RESPONSE_DOCUMENT, null, msgs);
            if (newResponseDocument != null)
                msgs = ((InternalEObject)newResponseDocument).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.RESPONSE_FORM_TYPE__RESPONSE_DOCUMENT, null, msgs);
            msgs = basicSetResponseDocument(newResponseDocument, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.RESPONSE_FORM_TYPE__RESPONSE_DOCUMENT, newResponseDocument, newResponseDocument));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OutputDefinitionType getRawDataOutput() {
        return rawDataOutput;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRawDataOutput(OutputDefinitionType newRawDataOutput, NotificationChain msgs) {
        OutputDefinitionType oldRawDataOutput = rawDataOutput;
        rawDataOutput = newRawDataOutput;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.RESPONSE_FORM_TYPE__RAW_DATA_OUTPUT, oldRawDataOutput, newRawDataOutput);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRawDataOutput(OutputDefinitionType newRawDataOutput) {
        if (newRawDataOutput != rawDataOutput) {
            NotificationChain msgs = null;
            if (rawDataOutput != null)
                msgs = ((InternalEObject)rawDataOutput).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.RESPONSE_FORM_TYPE__RAW_DATA_OUTPUT, null, msgs);
            if (newRawDataOutput != null)
                msgs = ((InternalEObject)newRawDataOutput).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.RESPONSE_FORM_TYPE__RAW_DATA_OUTPUT, null, msgs);
            msgs = basicSetRawDataOutput(newRawDataOutput, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.RESPONSE_FORM_TYPE__RAW_DATA_OUTPUT, newRawDataOutput, newRawDataOutput));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.RESPONSE_FORM_TYPE__RESPONSE_DOCUMENT:
                return basicSetResponseDocument(null, msgs);
            case Wps10Package.RESPONSE_FORM_TYPE__RAW_DATA_OUTPUT:
                return basicSetRawDataOutput(null, msgs);
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
            case Wps10Package.RESPONSE_FORM_TYPE__RESPONSE_DOCUMENT:
                return getResponseDocument();
            case Wps10Package.RESPONSE_FORM_TYPE__RAW_DATA_OUTPUT:
                return getRawDataOutput();
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
            case Wps10Package.RESPONSE_FORM_TYPE__RESPONSE_DOCUMENT:
                setResponseDocument((ResponseDocumentType)newValue);
                return;
            case Wps10Package.RESPONSE_FORM_TYPE__RAW_DATA_OUTPUT:
                setRawDataOutput((OutputDefinitionType)newValue);
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
            case Wps10Package.RESPONSE_FORM_TYPE__RESPONSE_DOCUMENT:
                setResponseDocument((ResponseDocumentType)null);
                return;
            case Wps10Package.RESPONSE_FORM_TYPE__RAW_DATA_OUTPUT:
                setRawDataOutput((OutputDefinitionType)null);
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
            case Wps10Package.RESPONSE_FORM_TYPE__RESPONSE_DOCUMENT:
                return responseDocument != null;
            case Wps10Package.RESPONSE_FORM_TYPE__RAW_DATA_OUTPUT:
                return rawDataOutput != null;
        }
        return super.eIsSet(featureID);
    }

} //ResponseFormTypeImpl
