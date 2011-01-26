/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import java.util.Collection;

import net.opengis.wps10.DocumentOutputDefinitionType;
import net.opengis.wps10.ResponseDocumentType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Response Document Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.ResponseDocumentTypeImpl#getOutput <em>Output</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.ResponseDocumentTypeImpl#isLineage <em>Lineage</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.ResponseDocumentTypeImpl#isStatus <em>Status</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.ResponseDocumentTypeImpl#isStoreExecuteResponse <em>Store Execute Response</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResponseDocumentTypeImpl extends EObjectImpl implements ResponseDocumentType {
    /**
     * The cached value of the '{@link #getOutput() <em>Output</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOutput()
     * @generated
     * @ordered
     */
    protected EList output;

    /**
     * The default value of the '{@link #isLineage() <em>Lineage</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isLineage()
     * @generated
     * @ordered
     */
    protected static final boolean LINEAGE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isLineage() <em>Lineage</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isLineage()
     * @generated
     * @ordered
     */
    protected boolean lineage = LINEAGE_EDEFAULT;

    /**
     * This is true if the Lineage attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean lineageESet;

    /**
     * The default value of the '{@link #isStatus() <em>Status</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isStatus()
     * @generated
     * @ordered
     */
    protected static final boolean STATUS_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isStatus() <em>Status</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isStatus()
     * @generated
     * @ordered
     */
    protected boolean status = STATUS_EDEFAULT;

    /**
     * This is true if the Status attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean statusESet;

    /**
     * The default value of the '{@link #isStoreExecuteResponse() <em>Store Execute Response</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isStoreExecuteResponse()
     * @generated
     * @ordered
     */
    protected static final boolean STORE_EXECUTE_RESPONSE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isStoreExecuteResponse() <em>Store Execute Response</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isStoreExecuteResponse()
     * @generated
     * @ordered
     */
    protected boolean storeExecuteResponse = STORE_EXECUTE_RESPONSE_EDEFAULT;

    /**
     * This is true if the Store Execute Response attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean storeExecuteResponseESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ResponseDocumentTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.RESPONSE_DOCUMENT_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getOutput() {
        if (output == null) {
            output = new EObjectContainmentEList(DocumentOutputDefinitionType.class, this, Wps10Package.RESPONSE_DOCUMENT_TYPE__OUTPUT);
        }
        return output;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isLineage() {
        return lineage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLineage(boolean newLineage) {
        boolean oldLineage = lineage;
        lineage = newLineage;
        boolean oldLineageESet = lineageESet;
        lineageESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.RESPONSE_DOCUMENT_TYPE__LINEAGE, oldLineage, lineage, !oldLineageESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetLineage() {
        boolean oldLineage = lineage;
        boolean oldLineageESet = lineageESet;
        lineage = LINEAGE_EDEFAULT;
        lineageESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wps10Package.RESPONSE_DOCUMENT_TYPE__LINEAGE, oldLineage, LINEAGE_EDEFAULT, oldLineageESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetLineage() {
        return lineageESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStatus(boolean newStatus) {
        boolean oldStatus = status;
        status = newStatus;
        boolean oldStatusESet = statusESet;
        statusESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.RESPONSE_DOCUMENT_TYPE__STATUS, oldStatus, status, !oldStatusESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetStatus() {
        boolean oldStatus = status;
        boolean oldStatusESet = statusESet;
        status = STATUS_EDEFAULT;
        statusESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wps10Package.RESPONSE_DOCUMENT_TYPE__STATUS, oldStatus, STATUS_EDEFAULT, oldStatusESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetStatus() {
        return statusESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isStoreExecuteResponse() {
        return storeExecuteResponse;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStoreExecuteResponse(boolean newStoreExecuteResponse) {
        boolean oldStoreExecuteResponse = storeExecuteResponse;
        storeExecuteResponse = newStoreExecuteResponse;
        boolean oldStoreExecuteResponseESet = storeExecuteResponseESet;
        storeExecuteResponseESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.RESPONSE_DOCUMENT_TYPE__STORE_EXECUTE_RESPONSE, oldStoreExecuteResponse, storeExecuteResponse, !oldStoreExecuteResponseESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetStoreExecuteResponse() {
        boolean oldStoreExecuteResponse = storeExecuteResponse;
        boolean oldStoreExecuteResponseESet = storeExecuteResponseESet;
        storeExecuteResponse = STORE_EXECUTE_RESPONSE_EDEFAULT;
        storeExecuteResponseESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wps10Package.RESPONSE_DOCUMENT_TYPE__STORE_EXECUTE_RESPONSE, oldStoreExecuteResponse, STORE_EXECUTE_RESPONSE_EDEFAULT, oldStoreExecuteResponseESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetStoreExecuteResponse() {
        return storeExecuteResponseESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__OUTPUT:
                return ((InternalEList)getOutput()).basicRemove(otherEnd, msgs);
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
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__OUTPUT:
                return getOutput();
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__LINEAGE:
                return isLineage() ? Boolean.TRUE : Boolean.FALSE;
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__STATUS:
                return isStatus() ? Boolean.TRUE : Boolean.FALSE;
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__STORE_EXECUTE_RESPONSE:
                return isStoreExecuteResponse() ? Boolean.TRUE : Boolean.FALSE;
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
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__OUTPUT:
                getOutput().clear();
                getOutput().addAll((Collection)newValue);
                return;
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__LINEAGE:
                setLineage(((Boolean)newValue).booleanValue());
                return;
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__STATUS:
                setStatus(((Boolean)newValue).booleanValue());
                return;
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__STORE_EXECUTE_RESPONSE:
                setStoreExecuteResponse(((Boolean)newValue).booleanValue());
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
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__OUTPUT:
                getOutput().clear();
                return;
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__LINEAGE:
                unsetLineage();
                return;
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__STATUS:
                unsetStatus();
                return;
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__STORE_EXECUTE_RESPONSE:
                unsetStoreExecuteResponse();
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
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__OUTPUT:
                return output != null && !output.isEmpty();
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__LINEAGE:
                return isSetLineage();
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__STATUS:
                return isSetStatus();
            case Wps10Package.RESPONSE_DOCUMENT_TYPE__STORE_EXECUTE_RESPONSE:
                return isSetStoreExecuteResponse();
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
        result.append(" (lineage: ");
        if (lineageESet) result.append(lineage); else result.append("<unset>");
        result.append(", status: ");
        if (statusESet) result.append(status); else result.append("<unset>");
        result.append(", storeExecuteResponse: ");
        if (storeExecuteResponseESet) result.append(storeExecuteResponse); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //ResponseDocumentTypeImpl
