/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import javax.xml.namespace.QName;
import net.opengis.wfs20.UpdateActionType;
import net.opengis.wfs20.ValueReferenceType;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Value Reference Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.ValueReferenceTypeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.ValueReferenceTypeImpl#getAction <em>Action</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ValueReferenceTypeImpl extends EObjectImpl implements ValueReferenceType {
    /**
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final QName VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected QName value = VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getAction() <em>Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAction()
     * @generated
     * @ordered
     */
    protected static final UpdateActionType ACTION_EDEFAULT = UpdateActionType.REPLACE;

    /**
     * The cached value of the '{@link #getAction() <em>Action</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAction()
     * @generated
     * @ordered
     */
    protected UpdateActionType action = ACTION_EDEFAULT;

    /**
     * This is true if the Action attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean actionESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ValueReferenceTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.VALUE_REFERENCE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QName getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValue(QName newValue) {
        QName oldValue = value;
        value = newValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.VALUE_REFERENCE_TYPE__VALUE, oldValue, value));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UpdateActionType getAction() {
        return action;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAction(UpdateActionType newAction) {
        UpdateActionType oldAction = action;
        action = newAction == null ? ACTION_EDEFAULT : newAction;
        boolean oldActionESet = actionESet;
        actionESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.VALUE_REFERENCE_TYPE__ACTION, oldAction, action, !oldActionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetAction() {
        UpdateActionType oldAction = action;
        boolean oldActionESet = actionESet;
        action = ACTION_EDEFAULT;
        actionESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Wfs20Package.VALUE_REFERENCE_TYPE__ACTION, oldAction, ACTION_EDEFAULT, oldActionESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetAction() {
        return actionESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wfs20Package.VALUE_REFERENCE_TYPE__VALUE:
                return getValue();
            case Wfs20Package.VALUE_REFERENCE_TYPE__ACTION:
                return getAction();
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
            case Wfs20Package.VALUE_REFERENCE_TYPE__VALUE:
                setValue((QName)newValue);
                return;
            case Wfs20Package.VALUE_REFERENCE_TYPE__ACTION:
                setAction((UpdateActionType)newValue);
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
            case Wfs20Package.VALUE_REFERENCE_TYPE__VALUE:
                setValue(VALUE_EDEFAULT);
                return;
            case Wfs20Package.VALUE_REFERENCE_TYPE__ACTION:
                unsetAction();
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
            case Wfs20Package.VALUE_REFERENCE_TYPE__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
            case Wfs20Package.VALUE_REFERENCE_TYPE__ACTION:
                return isSetAction();
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
        result.append(" (value: ");
        result.append(value);
        result.append(", action: ");
        if (actionESet) result.append(action); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //ValueReferenceTypeImpl
