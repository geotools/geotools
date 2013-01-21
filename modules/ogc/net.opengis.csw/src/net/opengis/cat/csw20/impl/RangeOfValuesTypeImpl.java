/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.RangeOfValuesType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Range Of Values Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.RangeOfValuesTypeImpl#getMinValue <em>Min Value</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.impl.RangeOfValuesTypeImpl#getMaxValue <em>Max Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RangeOfValuesTypeImpl extends EObjectImpl implements RangeOfValuesType {
    /**
     * The cached value of the '{@link #getMinValue() <em>Min Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinValue()
     * @generated
     * @ordered
     */
    protected EObject minValue;

    /**
     * The cached value of the '{@link #getMaxValue() <em>Max Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaxValue()
     * @generated
     * @ordered
     */
    protected EObject maxValue;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RangeOfValuesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.RANGE_OF_VALUES_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getMinValue() {
        return minValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMinValue(EObject newMinValue, NotificationChain msgs) {
        EObject oldMinValue = minValue;
        minValue = newMinValue;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.RANGE_OF_VALUES_TYPE__MIN_VALUE, oldMinValue, newMinValue);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMinValue(EObject newMinValue) {
        if (newMinValue != minValue) {
            NotificationChain msgs = null;
            if (minValue != null)
                msgs = ((InternalEObject)minValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.RANGE_OF_VALUES_TYPE__MIN_VALUE, null, msgs);
            if (newMinValue != null)
                msgs = ((InternalEObject)newMinValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.RANGE_OF_VALUES_TYPE__MIN_VALUE, null, msgs);
            msgs = basicSetMinValue(newMinValue, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.RANGE_OF_VALUES_TYPE__MIN_VALUE, newMinValue, newMinValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject getMaxValue() {
        return maxValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMaxValue(EObject newMaxValue, NotificationChain msgs) {
        EObject oldMaxValue = maxValue;
        maxValue = newMaxValue;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Csw20Package.RANGE_OF_VALUES_TYPE__MAX_VALUE, oldMaxValue, newMaxValue);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMaxValue(EObject newMaxValue) {
        if (newMaxValue != maxValue) {
            NotificationChain msgs = null;
            if (maxValue != null)
                msgs = ((InternalEObject)maxValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Csw20Package.RANGE_OF_VALUES_TYPE__MAX_VALUE, null, msgs);
            if (newMaxValue != null)
                msgs = ((InternalEObject)newMaxValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Csw20Package.RANGE_OF_VALUES_TYPE__MAX_VALUE, null, msgs);
            msgs = basicSetMaxValue(newMaxValue, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Csw20Package.RANGE_OF_VALUES_TYPE__MAX_VALUE, newMaxValue, newMaxValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.RANGE_OF_VALUES_TYPE__MIN_VALUE:
                return basicSetMinValue(null, msgs);
            case Csw20Package.RANGE_OF_VALUES_TYPE__MAX_VALUE:
                return basicSetMaxValue(null, msgs);
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
            case Csw20Package.RANGE_OF_VALUES_TYPE__MIN_VALUE:
                return getMinValue();
            case Csw20Package.RANGE_OF_VALUES_TYPE__MAX_VALUE:
                return getMaxValue();
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
            case Csw20Package.RANGE_OF_VALUES_TYPE__MIN_VALUE:
                setMinValue((EObject)newValue);
                return;
            case Csw20Package.RANGE_OF_VALUES_TYPE__MAX_VALUE:
                setMaxValue((EObject)newValue);
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
            case Csw20Package.RANGE_OF_VALUES_TYPE__MIN_VALUE:
                setMinValue((EObject)null);
                return;
            case Csw20Package.RANGE_OF_VALUES_TYPE__MAX_VALUE:
                setMaxValue((EObject)null);
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
            case Csw20Package.RANGE_OF_VALUES_TYPE__MIN_VALUE:
                return minValue != null;
            case Csw20Package.RANGE_OF_VALUES_TYPE__MAX_VALUE:
                return maxValue != null;
        }
        return super.eIsSet(featureID);
    }

} //RangeOfValuesTypeImpl
