/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.lang.String;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.DeleteType;
import net.opengis.cat.csw20.QueryConstraintType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Delete Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.DeleteTypeImpl#getConstraint <em>Constraint</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.DeleteTypeImpl#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.DeleteTypeImpl#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DeleteTypeImpl extends EObjectImpl implements DeleteType {
    /**
     * The cached value of the '{@link #getConstraint() <em>Constraint</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConstraint()
     * @generated
     * @ordered
     */
    protected QueryConstraintType constraint;

    /**
     * The default value of the '{@link #getHandle() <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHandle()
     * @generated
     * @ordered
     */
    protected static final String HANDLE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getHandle() <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHandle()
     * @generated
     * @ordered
     */
    protected String handle = HANDLE_EDEFAULT;

    /**
     * The default value of the '{@link #getTypeName() <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTypeName()
     * @generated
     * @ordered
     */
    protected static final String TYPE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTypeName() <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTypeName()
     * @generated
     * @ordered
     */
    protected String typeName = TYPE_NAME_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DeleteTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.DELETE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public QueryConstraintType getConstraint() {
        return constraint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetConstraint(QueryConstraintType newConstraint, NotificationChain msgs) {
        QueryConstraintType oldConstraint = constraint;
        constraint = newConstraint;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.DELETE_TYPE__CONSTRAINT, oldConstraint, newConstraint);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setConstraint(QueryConstraintType newConstraint) {
        if (newConstraint != constraint) {
            NotificationChain msgs = null;
            if (constraint != null)
                msgs = ((InternalEObject)constraint).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.DELETE_TYPE__CONSTRAINT, null, msgs);
            if (newConstraint != null)
                msgs = ((InternalEObject)newConstraint).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.DELETE_TYPE__CONSTRAINT, null, msgs);
            msgs = basicSetConstraint(newConstraint, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.DELETE_TYPE__CONSTRAINT, newConstraint, newConstraint));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getHandle() {
        return handle;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHandle(String newHandle) {
        String oldHandle = handle;
        handle = newHandle;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.DELETE_TYPE__HANDLE, oldHandle, handle));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTypeName(String newTypeName) {
        String oldTypeName = typeName;
        typeName = newTypeName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.DELETE_TYPE__TYPE_NAME, oldTypeName, typeName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.DELETE_TYPE__CONSTRAINT:
                return basicSetConstraint(null, msgs);
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
            case Csw20Package.DELETE_TYPE__CONSTRAINT:
                return getConstraint();
            case Csw20Package.DELETE_TYPE__HANDLE:
                return getHandle();
            case Csw20Package.DELETE_TYPE__TYPE_NAME:
                return getTypeName();
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
            case Csw20Package.DELETE_TYPE__CONSTRAINT:
                setConstraint((QueryConstraintType)newValue);
                return;
            case Csw20Package.DELETE_TYPE__HANDLE:
                setHandle((String)newValue);
                return;
            case Csw20Package.DELETE_TYPE__TYPE_NAME:
                setTypeName((String)newValue);
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
            case Csw20Package.DELETE_TYPE__CONSTRAINT:
                setConstraint((QueryConstraintType)null);
                return;
            case Csw20Package.DELETE_TYPE__HANDLE:
                setHandle(HANDLE_EDEFAULT);
                return;
            case Csw20Package.DELETE_TYPE__TYPE_NAME:
                setTypeName(TYPE_NAME_EDEFAULT);
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
            case Csw20Package.DELETE_TYPE__CONSTRAINT:
                return constraint != null;
            case Csw20Package.DELETE_TYPE__HANDLE:
                return HANDLE_EDEFAULT == null ? handle != null : !HANDLE_EDEFAULT.equals(handle);
            case Csw20Package.DELETE_TYPE__TYPE_NAME:
                return TYPE_NAME_EDEFAULT == null ? typeName != null : !TYPE_NAME_EDEFAULT.equals(typeName);
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
        result.append(" (handle: ");
        result.append(handle);
        result.append(", typeName: ");
        result.append(typeName);
        result.append(')');
        return result.toString();
    }

} //DeleteTypeImpl
