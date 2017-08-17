/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.CodeType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.OperationParameterBaseType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation Parameter Base Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.OperationParameterBaseTypeImpl#getParameterName <em>Parameter Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class OperationParameterBaseTypeImpl extends AbstractGeneralOperationParameterTypeImpl implements OperationParameterBaseType {
    /**
     * The cached value of the '{@link #getParameterName() <em>Parameter Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParameterName()
     * @generated
     * @ordered
     */
    protected CodeType parameterName;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OperationParameterBaseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getOperationParameterBaseType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getParameterName() {
        return parameterName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetParameterName(CodeType newParameterName, NotificationChain msgs) {
        CodeType oldParameterName = parameterName;
        parameterName = newParameterName;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.OPERATION_PARAMETER_BASE_TYPE__PARAMETER_NAME, oldParameterName, newParameterName);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setParameterName(CodeType newParameterName) {
        if (newParameterName != parameterName) {
            NotificationChain msgs = null;
            if (parameterName != null)
                msgs = ((InternalEObject)parameterName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.OPERATION_PARAMETER_BASE_TYPE__PARAMETER_NAME, null, msgs);
            if (newParameterName != null)
                msgs = ((InternalEObject)newParameterName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.OPERATION_PARAMETER_BASE_TYPE__PARAMETER_NAME, null, msgs);
            msgs = basicSetParameterName(newParameterName, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.OPERATION_PARAMETER_BASE_TYPE__PARAMETER_NAME, newParameterName, newParameterName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.OPERATION_PARAMETER_BASE_TYPE__PARAMETER_NAME:
                return basicSetParameterName(null, msgs);
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
            case Gml311Package.OPERATION_PARAMETER_BASE_TYPE__PARAMETER_NAME:
                return getParameterName();
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
            case Gml311Package.OPERATION_PARAMETER_BASE_TYPE__PARAMETER_NAME:
                setParameterName((CodeType)newValue);
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
            case Gml311Package.OPERATION_PARAMETER_BASE_TYPE__PARAMETER_NAME:
                setParameterName((CodeType)null);
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
            case Gml311Package.OPERATION_PARAMETER_BASE_TYPE__PARAMETER_NAME:
                return parameterName != null;
        }
        return super.eIsSet(featureID);
    }

} //OperationParameterBaseTypeImpl
