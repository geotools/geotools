/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.ConversionType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.OperationMethodRefType;
import net.opengis.gml311.ParameterValueType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Conversion Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.ConversionTypeImpl#getUsesMethod <em>Uses Method</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ConversionTypeImpl#getUsesValue <em>Uses Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConversionTypeImpl extends AbstractGeneralConversionTypeImpl implements ConversionType {
    /**
     * The cached value of the '{@link #getUsesMethod() <em>Uses Method</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUsesMethod()
     * @generated
     * @ordered
     */
    protected OperationMethodRefType usesMethod;

    /**
     * The cached value of the '{@link #getUsesValue() <em>Uses Value</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUsesValue()
     * @generated
     * @ordered
     */
    protected EList<ParameterValueType> usesValue;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConversionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getConversionType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationMethodRefType getUsesMethod() {
        return usesMethod;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetUsesMethod(OperationMethodRefType newUsesMethod, NotificationChain msgs) {
        OperationMethodRefType oldUsesMethod = usesMethod;
        usesMethod = newUsesMethod;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.CONVERSION_TYPE__USES_METHOD, oldUsesMethod, newUsesMethod);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUsesMethod(OperationMethodRefType newUsesMethod) {
        if (newUsesMethod != usesMethod) {
            NotificationChain msgs = null;
            if (usesMethod != null)
                msgs = ((InternalEObject)usesMethod).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.CONVERSION_TYPE__USES_METHOD, null, msgs);
            if (newUsesMethod != null)
                msgs = ((InternalEObject)newUsesMethod).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.CONVERSION_TYPE__USES_METHOD, null, msgs);
            msgs = basicSetUsesMethod(newUsesMethod, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.CONVERSION_TYPE__USES_METHOD, newUsesMethod, newUsesMethod));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ParameterValueType> getUsesValue() {
        if (usesValue == null) {
            usesValue = new EObjectContainmentEList<ParameterValueType>(ParameterValueType.class, this, Gml311Package.CONVERSION_TYPE__USES_VALUE);
        }
        return usesValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.CONVERSION_TYPE__USES_METHOD:
                return basicSetUsesMethod(null, msgs);
            case Gml311Package.CONVERSION_TYPE__USES_VALUE:
                return ((InternalEList<?>)getUsesValue()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.CONVERSION_TYPE__USES_METHOD:
                return getUsesMethod();
            case Gml311Package.CONVERSION_TYPE__USES_VALUE:
                return getUsesValue();
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
            case Gml311Package.CONVERSION_TYPE__USES_METHOD:
                setUsesMethod((OperationMethodRefType)newValue);
                return;
            case Gml311Package.CONVERSION_TYPE__USES_VALUE:
                getUsesValue().clear();
                getUsesValue().addAll((Collection<? extends ParameterValueType>)newValue);
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
            case Gml311Package.CONVERSION_TYPE__USES_METHOD:
                setUsesMethod((OperationMethodRefType)null);
                return;
            case Gml311Package.CONVERSION_TYPE__USES_VALUE:
                getUsesValue().clear();
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
            case Gml311Package.CONVERSION_TYPE__USES_METHOD:
                return usesMethod != null;
            case Gml311Package.CONVERSION_TYPE__USES_VALUE:
                return usesValue != null && !usesValue.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ConversionTypeImpl
