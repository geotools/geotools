/**
 */
package net.opengis.ows20.impl;

import java.util.Collection;

import net.opengis.ows20.AdditionalParameterType;
import net.opengis.ows20.CodeType;
import net.opengis.ows20.Ows20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Additional Parameter Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows20.impl.AdditionalParameterTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.AdditionalParameterTypeImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AdditionalParameterTypeImpl extends EObjectImpl implements AdditionalParameterType {
    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected CodeType name;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected EList<EObject> value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AdditionalParameterTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.ADDITIONAL_PARAMETER_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CodeType getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetName(CodeType newName, NotificationChain msgs) {
        CodeType oldName = name;
        name = newName;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows20Package.ADDITIONAL_PARAMETER_TYPE__NAME, oldName, newName);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(CodeType newName) {
        if (newName != name) {
            NotificationChain msgs = null;
            if (name != null)
                msgs = ((InternalEObject)name).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows20Package.ADDITIONAL_PARAMETER_TYPE__NAME, null, msgs);
            if (newName != null)
                msgs = ((InternalEObject)newName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows20Package.ADDITIONAL_PARAMETER_TYPE__NAME, null, msgs);
            msgs = basicSetName(newName, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.ADDITIONAL_PARAMETER_TYPE__NAME, newName, newName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<EObject> getValue() {
        if (value == null) {
            value = new EObjectContainmentEList<EObject>(EObject.class, this, Ows20Package.ADDITIONAL_PARAMETER_TYPE__VALUE);
        }
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows20Package.ADDITIONAL_PARAMETER_TYPE__NAME:
                return basicSetName(null, msgs);
            case Ows20Package.ADDITIONAL_PARAMETER_TYPE__VALUE:
                return ((InternalEList<?>)getValue()).basicRemove(otherEnd, msgs);
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
            case Ows20Package.ADDITIONAL_PARAMETER_TYPE__NAME:
                return getName();
            case Ows20Package.ADDITIONAL_PARAMETER_TYPE__VALUE:
                return getValue();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Ows20Package.ADDITIONAL_PARAMETER_TYPE__NAME:
                setName((CodeType)newValue);
                return;
            case Ows20Package.ADDITIONAL_PARAMETER_TYPE__VALUE:
                getValue().clear();
                getValue().addAll((Collection<? extends EObject>)newValue);
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
            case Ows20Package.ADDITIONAL_PARAMETER_TYPE__NAME:
                setName((CodeType)null);
                return;
            case Ows20Package.ADDITIONAL_PARAMETER_TYPE__VALUE:
                getValue().clear();
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
            case Ows20Package.ADDITIONAL_PARAMETER_TYPE__NAME:
                return name != null;
            case Ows20Package.ADDITIONAL_PARAMETER_TYPE__VALUE:
                return value != null && !value.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //AdditionalParameterTypeImpl
