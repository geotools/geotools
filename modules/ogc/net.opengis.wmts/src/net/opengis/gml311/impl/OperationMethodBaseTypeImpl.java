/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.CodeType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.OperationMethodBaseType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation Method Base Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.OperationMethodBaseTypeImpl#getMethodName <em>Method Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class OperationMethodBaseTypeImpl extends DefinitionTypeImpl implements OperationMethodBaseType {
    /**
     * The cached value of the '{@link #getMethodName() <em>Method Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMethodName()
     * @generated
     * @ordered
     */
    protected CodeType methodName;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OperationMethodBaseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getOperationMethodBaseType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getMethodName() {
        return methodName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMethodName(CodeType newMethodName, NotificationChain msgs) {
        CodeType oldMethodName = methodName;
        methodName = newMethodName;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.OPERATION_METHOD_BASE_TYPE__METHOD_NAME, oldMethodName, newMethodName);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMethodName(CodeType newMethodName) {
        if (newMethodName != methodName) {
            NotificationChain msgs = null;
            if (methodName != null)
                msgs = ((InternalEObject)methodName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.OPERATION_METHOD_BASE_TYPE__METHOD_NAME, null, msgs);
            if (newMethodName != null)
                msgs = ((InternalEObject)newMethodName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.OPERATION_METHOD_BASE_TYPE__METHOD_NAME, null, msgs);
            msgs = basicSetMethodName(newMethodName, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.OPERATION_METHOD_BASE_TYPE__METHOD_NAME, newMethodName, newMethodName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.OPERATION_METHOD_BASE_TYPE__METHOD_NAME:
                return basicSetMethodName(null, msgs);
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
            case Gml311Package.OPERATION_METHOD_BASE_TYPE__METHOD_NAME:
                return getMethodName();
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
            case Gml311Package.OPERATION_METHOD_BASE_TYPE__METHOD_NAME:
                setMethodName((CodeType)newValue);
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
            case Gml311Package.OPERATION_METHOD_BASE_TYPE__METHOD_NAME:
                setMethodName((CodeType)null);
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
            case Gml311Package.OPERATION_METHOD_BASE_TYPE__METHOD_NAME:
                return methodName != null;
        }
        return super.eIsSet(featureID);
    }

} //OperationMethodBaseTypeImpl
