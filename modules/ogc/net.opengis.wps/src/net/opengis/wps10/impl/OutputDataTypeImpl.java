/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10.impl;

import net.opengis.wps10.DataType;
import net.opengis.wps10.OutputDataType;
import net.opengis.wps10.OutputReferenceType;
import net.opengis.wps10.Wps10Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Output Data Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wps10.impl.OutputDataTypeImpl#getReference <em>Reference</em>}</li>
 *   <li>{@link net.opengis.wps10.impl.OutputDataTypeImpl#getData <em>Data</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OutputDataTypeImpl extends DescriptionTypeImpl implements OutputDataType {
    /**
     * The cached value of the '{@link #getReference() <em>Reference</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReference()
     * @generated
     * @ordered
     */
    protected OutputReferenceType reference;

    /**
     * The cached value of the '{@link #getData() <em>Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getData()
     * @generated
     * @ordered
     */
    protected DataType data;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OutputDataTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wps10Package.Literals.OUTPUT_DATA_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OutputReferenceType getReference() {
        return reference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetReference(OutputReferenceType newReference, NotificationChain msgs) {
        OutputReferenceType oldReference = reference;
        reference = newReference;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.OUTPUT_DATA_TYPE__REFERENCE, oldReference, newReference);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setReference(OutputReferenceType newReference) {
        if (newReference != reference) {
            NotificationChain msgs = null;
            if (reference != null)
                msgs = ((InternalEObject)reference).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.OUTPUT_DATA_TYPE__REFERENCE, null, msgs);
            if (newReference != null)
                msgs = ((InternalEObject)newReference).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.OUTPUT_DATA_TYPE__REFERENCE, null, msgs);
            msgs = basicSetReference(newReference, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.OUTPUT_DATA_TYPE__REFERENCE, newReference, newReference));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DataType getData() {
        return data;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetData(DataType newData, NotificationChain msgs) {
        DataType oldData = data;
        data = newData;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Wps10Package.OUTPUT_DATA_TYPE__DATA, oldData, newData);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setData(DataType newData) {
        if (newData != data) {
            NotificationChain msgs = null;
            if (data != null)
                msgs = ((InternalEObject)data).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Wps10Package.OUTPUT_DATA_TYPE__DATA, null, msgs);
            if (newData != null)
                msgs = ((InternalEObject)newData).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Wps10Package.OUTPUT_DATA_TYPE__DATA, null, msgs);
            msgs = basicSetData(newData, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wps10Package.OUTPUT_DATA_TYPE__DATA, newData, newData));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wps10Package.OUTPUT_DATA_TYPE__REFERENCE:
                return basicSetReference(null, msgs);
            case Wps10Package.OUTPUT_DATA_TYPE__DATA:
                return basicSetData(null, msgs);
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
            case Wps10Package.OUTPUT_DATA_TYPE__REFERENCE:
                return getReference();
            case Wps10Package.OUTPUT_DATA_TYPE__DATA:
                return getData();
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
            case Wps10Package.OUTPUT_DATA_TYPE__REFERENCE:
                setReference((OutputReferenceType)newValue);
                return;
            case Wps10Package.OUTPUT_DATA_TYPE__DATA:
                setData((DataType)newValue);
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
            case Wps10Package.OUTPUT_DATA_TYPE__REFERENCE:
                setReference((OutputReferenceType)null);
                return;
            case Wps10Package.OUTPUT_DATA_TYPE__DATA:
                setData((DataType)null);
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
            case Wps10Package.OUTPUT_DATA_TYPE__REFERENCE:
                return reference != null;
            case Wps10Package.OUTPUT_DATA_TYPE__DATA:
                return data != null;
        }
        return super.eIsSet(featureID);
    }

} //OutputDataTypeImpl
