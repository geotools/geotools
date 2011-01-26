/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.wps10.DataInputsType;
import net.opengis.wps10.ProcessDescriptionType;
import net.opengis.wps10.ProcessOutputsType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Process Description Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.ProcessDescriptionTypeImpl#getDataInputs <em>Data Inputs</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.ProcessDescriptionTypeImpl#getProcessOutputs <em>Process Outputs</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.ProcessDescriptionTypeImpl#isStatusSupported <em>Status Supported</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.ProcessDescriptionTypeImpl#isStoreSupported <em>Store Supported</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ProcessDescriptionTypeImpl extends ProcessBriefTypeImpl implements ProcessDescriptionType {
    /**
     * The cached value of the '{@link #getDataInputs() <em>Data Inputs</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDataInputs()
     * @generated
     * @ordered
     */
    protected DataInputsType dataInputs;

    /**
     * The cached value of the '{@link #getProcessOutputs() <em>Process Outputs</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProcessOutputs()
     * @generated
     * @ordered
     */
    protected ProcessOutputsType processOutputs;

    /**
     * The default value of the '{@link #isStatusSupported() <em>Status Supported</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isStatusSupported()
     * @generated
     * @ordered
     */
    protected static final boolean STATUS_SUPPORTED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isStatusSupported() <em>Status Supported</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isStatusSupported()
     * @generated
     * @ordered
     */
    protected boolean statusSupported = STATUS_SUPPORTED_EDEFAULT;

    /**
     * This is true if the Status Supported attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean statusSupportedESet;

    /**
     * The default value of the '{@link #isStoreSupported() <em>Store Supported</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isStoreSupported()
     * @generated
     * @ordered
     */
    protected static final boolean STORE_SUPPORTED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isStoreSupported() <em>Store Supported</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isStoreSupported()
     * @generated
     * @ordered
     */
    protected boolean storeSupported = STORE_SUPPORTED_EDEFAULT;

    /**
     * This is true if the Store Supported attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean storeSupportedESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ProcessDescriptionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.PROCESS_DESCRIPTION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DataInputsType getDataInputs() {
        return dataInputs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDataInputs(DataInputsType newDataInputs, NotificationChain msgs) {
        DataInputsType oldDataInputs = dataInputs;
        dataInputs = newDataInputs;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.PROCESS_DESCRIPTION_TYPE__DATA_INPUTS, oldDataInputs, newDataInputs);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDataInputs(DataInputsType newDataInputs) {
        if (newDataInputs != dataInputs) {
            NotificationChain msgs = null;
            if (dataInputs != null)
                msgs = ((InternalEObject)dataInputs).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.PROCESS_DESCRIPTION_TYPE__DATA_INPUTS, null, msgs);
            if (newDataInputs != null)
                msgs = ((InternalEObject)newDataInputs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.PROCESS_DESCRIPTION_TYPE__DATA_INPUTS, null, msgs);
            msgs = basicSetDataInputs(newDataInputs, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.PROCESS_DESCRIPTION_TYPE__DATA_INPUTS, newDataInputs, newDataInputs));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProcessOutputsType getProcessOutputs() {
        return processOutputs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetProcessOutputs(ProcessOutputsType newProcessOutputs, NotificationChain msgs) {
        ProcessOutputsType oldProcessOutputs = processOutputs;
        processOutputs = newProcessOutputs;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.PROCESS_DESCRIPTION_TYPE__PROCESS_OUTPUTS, oldProcessOutputs, newProcessOutputs);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProcessOutputs(ProcessOutputsType newProcessOutputs) {
        if (newProcessOutputs != processOutputs) {
            NotificationChain msgs = null;
            if (processOutputs != null)
                msgs = ((InternalEObject)processOutputs).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.PROCESS_DESCRIPTION_TYPE__PROCESS_OUTPUTS, null, msgs);
            if (newProcessOutputs != null)
                msgs = ((InternalEObject)newProcessOutputs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.PROCESS_DESCRIPTION_TYPE__PROCESS_OUTPUTS, null, msgs);
            msgs = basicSetProcessOutputs(newProcessOutputs, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.PROCESS_DESCRIPTION_TYPE__PROCESS_OUTPUTS, newProcessOutputs, newProcessOutputs));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isStatusSupported() {
        return statusSupported;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStatusSupported(boolean newStatusSupported) {
        boolean oldStatusSupported = statusSupported;
        statusSupported = newStatusSupported;
        boolean oldStatusSupportedESet = statusSupportedESet;
        statusSupportedESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.PROCESS_DESCRIPTION_TYPE__STATUS_SUPPORTED, oldStatusSupported, statusSupported, !oldStatusSupportedESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetStatusSupported() {
        boolean oldStatusSupported = statusSupported;
        boolean oldStatusSupportedESet = statusSupportedESet;
        statusSupported = STATUS_SUPPORTED_EDEFAULT;
        statusSupportedESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wps10Package.PROCESS_DESCRIPTION_TYPE__STATUS_SUPPORTED, oldStatusSupported, STATUS_SUPPORTED_EDEFAULT, oldStatusSupportedESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetStatusSupported() {
        return statusSupportedESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isStoreSupported() {
        return storeSupported;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStoreSupported(boolean newStoreSupported) {
        boolean oldStoreSupported = storeSupported;
        storeSupported = newStoreSupported;
        boolean oldStoreSupportedESet = storeSupportedESet;
        storeSupportedESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.PROCESS_DESCRIPTION_TYPE__STORE_SUPPORTED, oldStoreSupported, storeSupported, !oldStoreSupportedESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetStoreSupported() {
        boolean oldStoreSupported = storeSupported;
        boolean oldStoreSupportedESet = storeSupportedESet;
        storeSupported = STORE_SUPPORTED_EDEFAULT;
        storeSupportedESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wps10Package.PROCESS_DESCRIPTION_TYPE__STORE_SUPPORTED, oldStoreSupported, STORE_SUPPORTED_EDEFAULT, oldStoreSupportedESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetStoreSupported() {
        return storeSupportedESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__DATA_INPUTS:
                return basicSetDataInputs(null, msgs);
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__PROCESS_OUTPUTS:
                return basicSetProcessOutputs(null, msgs);
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
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__DATA_INPUTS:
                return getDataInputs();
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__PROCESS_OUTPUTS:
                return getProcessOutputs();
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__STATUS_SUPPORTED:
                return isStatusSupported() ? Boolean.TRUE : Boolean.FALSE;
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__STORE_SUPPORTED:
                return isStoreSupported() ? Boolean.TRUE : Boolean.FALSE;
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
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__DATA_INPUTS:
                setDataInputs((DataInputsType)newValue);
                return;
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__PROCESS_OUTPUTS:
                setProcessOutputs((ProcessOutputsType)newValue);
                return;
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__STATUS_SUPPORTED:
                setStatusSupported(((Boolean)newValue).booleanValue());
                return;
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__STORE_SUPPORTED:
                setStoreSupported(((Boolean)newValue).booleanValue());
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
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__DATA_INPUTS:
                setDataInputs((DataInputsType)null);
                return;
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__PROCESS_OUTPUTS:
                setProcessOutputs((ProcessOutputsType)null);
                return;
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__STATUS_SUPPORTED:
                unsetStatusSupported();
                return;
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__STORE_SUPPORTED:
                unsetStoreSupported();
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
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__DATA_INPUTS:
                return dataInputs != null;
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__PROCESS_OUTPUTS:
                return processOutputs != null;
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__STATUS_SUPPORTED:
                return isSetStatusSupported();
            case Wps10Package.PROCESS_DESCRIPTION_TYPE__STORE_SUPPORTED:
                return isSetStoreSupported();
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
        result.append(" (statusSupported: ");
        if (statusSupportedESet) result.append(statusSupported); else result.append("<unset>");
        result.append(", storeSupported: ");
        if (storeSupportedESet) result.append(storeSupported); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //ProcessDescriptionTypeImpl
