/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.CodeType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.OperationParameterGroupBaseType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation Parameter Group Base Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.OperationParameterGroupBaseTypeImpl#getGroupName <em>Group Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class OperationParameterGroupBaseTypeImpl extends AbstractGeneralOperationParameterTypeImpl implements OperationParameterGroupBaseType {
    /**
     * The cached value of the '{@link #getGroupName() <em>Group Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGroupName()
     * @generated
     * @ordered
     */
    protected CodeType groupName;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OperationParameterGroupBaseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getOperationParameterGroupBaseType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getGroupName() {
        return groupName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetGroupName(CodeType newGroupName, NotificationChain msgs) {
        CodeType oldGroupName = groupName;
        groupName = newGroupName;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.OPERATION_PARAMETER_GROUP_BASE_TYPE__GROUP_NAME, oldGroupName, newGroupName);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGroupName(CodeType newGroupName) {
        if (newGroupName != groupName) {
            NotificationChain msgs = null;
            if (groupName != null)
                msgs = ((InternalEObject)groupName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.OPERATION_PARAMETER_GROUP_BASE_TYPE__GROUP_NAME, null, msgs);
            if (newGroupName != null)
                msgs = ((InternalEObject)newGroupName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.OPERATION_PARAMETER_GROUP_BASE_TYPE__GROUP_NAME, null, msgs);
            msgs = basicSetGroupName(newGroupName, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.OPERATION_PARAMETER_GROUP_BASE_TYPE__GROUP_NAME, newGroupName, newGroupName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.OPERATION_PARAMETER_GROUP_BASE_TYPE__GROUP_NAME:
                return basicSetGroupName(null, msgs);
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
            case Gml311Package.OPERATION_PARAMETER_GROUP_BASE_TYPE__GROUP_NAME:
                return getGroupName();
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
            case Gml311Package.OPERATION_PARAMETER_GROUP_BASE_TYPE__GROUP_NAME:
                setGroupName((CodeType)newValue);
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
            case Gml311Package.OPERATION_PARAMETER_GROUP_BASE_TYPE__GROUP_NAME:
                setGroupName((CodeType)null);
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
            case Gml311Package.OPERATION_PARAMETER_GROUP_BASE_TYPE__GROUP_NAME:
                return groupName != null;
        }
        return super.eIsSet(featureID);
    }

} //OperationParameterGroupBaseTypeImpl
