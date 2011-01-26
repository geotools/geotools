/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.ows11.CodeType;

import net.opengis.wps10.DataInputsType1;
import net.opengis.wps10.ExecuteType;
import net.opengis.wps10.ResponseFormType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Execute Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.ExecuteTypeImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.ExecuteTypeImpl#getDataInputs <em>Data Inputs</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.ExecuteTypeImpl#getResponseForm <em>Response Form</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExecuteTypeImpl extends RequestBaseTypeImpl implements ExecuteType {
    /**
     * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected CodeType identifier;

    /**
     * The cached value of the '{@link #getDataInputs() <em>Data Inputs</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDataInputs()
     * @generated
     * @ordered
     */
    protected DataInputsType1 dataInputs;

    /**
     * The cached value of the '{@link #getResponseForm() <em>Response Form</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResponseForm()
     * @generated
     * @ordered
     */
    protected ResponseFormType responseForm;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ExecuteTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.EXECUTE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getIdentifier() {
        return identifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetIdentifier(CodeType newIdentifier, NotificationChain msgs) {
        CodeType oldIdentifier = identifier;
        identifier = newIdentifier;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.EXECUTE_TYPE__IDENTIFIER, oldIdentifier, newIdentifier);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIdentifier(CodeType newIdentifier) {
        if (newIdentifier != identifier) {
            NotificationChain msgs = null;
            if (identifier != null)
                msgs = ((InternalEObject)identifier).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.EXECUTE_TYPE__IDENTIFIER, null, msgs);
            if (newIdentifier != null)
                msgs = ((InternalEObject)newIdentifier).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.EXECUTE_TYPE__IDENTIFIER, null, msgs);
            msgs = basicSetIdentifier(newIdentifier, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.EXECUTE_TYPE__IDENTIFIER, newIdentifier, newIdentifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DataInputsType1 getDataInputs() {
        return dataInputs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDataInputs(DataInputsType1 newDataInputs, NotificationChain msgs) {
        DataInputsType1 oldDataInputs = dataInputs;
        dataInputs = newDataInputs;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.EXECUTE_TYPE__DATA_INPUTS, oldDataInputs, newDataInputs);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDataInputs(DataInputsType1 newDataInputs) {
        if (newDataInputs != dataInputs) {
            NotificationChain msgs = null;
            if (dataInputs != null)
                msgs = ((InternalEObject)dataInputs).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.EXECUTE_TYPE__DATA_INPUTS, null, msgs);
            if (newDataInputs != null)
                msgs = ((InternalEObject)newDataInputs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.EXECUTE_TYPE__DATA_INPUTS, null, msgs);
            msgs = basicSetDataInputs(newDataInputs, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.EXECUTE_TYPE__DATA_INPUTS, newDataInputs, newDataInputs));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResponseFormType getResponseForm() {
        return responseForm;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetResponseForm(ResponseFormType newResponseForm, NotificationChain msgs) {
        ResponseFormType oldResponseForm = responseForm;
        responseForm = newResponseForm;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.EXECUTE_TYPE__RESPONSE_FORM, oldResponseForm, newResponseForm);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResponseForm(ResponseFormType newResponseForm) {
        if (newResponseForm != responseForm) {
            NotificationChain msgs = null;
            if (responseForm != null)
                msgs = ((InternalEObject)responseForm).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.EXECUTE_TYPE__RESPONSE_FORM, null, msgs);
            if (newResponseForm != null)
                msgs = ((InternalEObject)newResponseForm).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.EXECUTE_TYPE__RESPONSE_FORM, null, msgs);
            msgs = basicSetResponseForm(newResponseForm, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.EXECUTE_TYPE__RESPONSE_FORM, newResponseForm, newResponseForm));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.EXECUTE_TYPE__IDENTIFIER:
                return basicSetIdentifier(null, msgs);
            case Wps10Package.EXECUTE_TYPE__DATA_INPUTS:
                return basicSetDataInputs(null, msgs);
            case Wps10Package.EXECUTE_TYPE__RESPONSE_FORM:
                return basicSetResponseForm(null, msgs);
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
            case Wps10Package.EXECUTE_TYPE__IDENTIFIER:
                return getIdentifier();
            case Wps10Package.EXECUTE_TYPE__DATA_INPUTS:
                return getDataInputs();
            case Wps10Package.EXECUTE_TYPE__RESPONSE_FORM:
                return getResponseForm();
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
            case Wps10Package.EXECUTE_TYPE__IDENTIFIER:
                setIdentifier((CodeType)newValue);
                return;
            case Wps10Package.EXECUTE_TYPE__DATA_INPUTS:
                setDataInputs((DataInputsType1)newValue);
                return;
            case Wps10Package.EXECUTE_TYPE__RESPONSE_FORM:
                setResponseForm((ResponseFormType)newValue);
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
            case Wps10Package.EXECUTE_TYPE__IDENTIFIER:
                setIdentifier((CodeType)null);
                return;
            case Wps10Package.EXECUTE_TYPE__DATA_INPUTS:
                setDataInputs((DataInputsType1)null);
                return;
            case Wps10Package.EXECUTE_TYPE__RESPONSE_FORM:
                setResponseForm((ResponseFormType)null);
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
            case Wps10Package.EXECUTE_TYPE__IDENTIFIER:
                return identifier != null;
            case Wps10Package.EXECUTE_TYPE__DATA_INPUTS:
                return dataInputs != null;
            case Wps10Package.EXECUTE_TYPE__RESPONSE_FORM:
                return responseForm != null;
        }
        return super.eIsSet(featureID);
    }

} //ExecuteTypeImpl
