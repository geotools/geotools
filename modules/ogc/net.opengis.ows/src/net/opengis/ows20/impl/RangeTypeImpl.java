/**
 */
package net.opengis.ows20.impl;

import net.opengis.ows20.RangeClosureType;
import net.opengis.ows20.RangeType;
import net.opengis.ows20.ValueType;
import net.opengis.ows20.Ows20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Range Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows20.impl.RangeTypeImpl#getMinimumValue <em>Minimum Value</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.RangeTypeImpl#getMaximumValue <em>Maximum Value</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.RangeTypeImpl#getSpacing <em>Spacing</em>}</li>
 *   <li>{@link net.opengis.ows20.impl.RangeTypeImpl#getRangeClosure <em>Range Closure</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RangeTypeImpl extends EObjectImpl implements RangeType {
    /**
     * The cached value of the '{@link #getMinimumValue() <em>Minimum Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinimumValue()
     * @generated
     * @ordered
     */
    protected ValueType minimumValue;

    /**
     * The cached value of the '{@link #getMaximumValue() <em>Maximum Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMaximumValue()
     * @generated
     * @ordered
     */
    protected ValueType maximumValue;

    /**
     * The cached value of the '{@link #getSpacing() <em>Spacing</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSpacing()
     * @generated
     * @ordered
     */
    protected ValueType spacing;

    /**
     * The default value of the '{@link #getRangeClosure() <em>Range Closure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRangeClosure()
     * @generated
     * @ordered
     */
    protected static final RangeClosureType RANGE_CLOSURE_EDEFAULT = RangeClosureType.CLOSED;

    /**
     * The cached value of the '{@link #getRangeClosure() <em>Range Closure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRangeClosure()
     * @generated
     * @ordered
     */
    protected RangeClosureType rangeClosure = RANGE_CLOSURE_EDEFAULT;

    /**
     * This is true if the Range Closure attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean rangeClosureESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RangeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.RANGE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueType getMinimumValue() {
        return minimumValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMinimumValue(ValueType newMinimumValue, NotificationChain msgs) {
        ValueType oldMinimumValue = minimumValue;
        minimumValue = newMinimumValue;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows20Package.RANGE_TYPE__MINIMUM_VALUE, oldMinimumValue, newMinimumValue);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMinimumValue(ValueType newMinimumValue) {
        if (newMinimumValue != minimumValue) {
            NotificationChain msgs = null;
            if (minimumValue != null)
                msgs = ((InternalEObject)minimumValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows20Package.RANGE_TYPE__MINIMUM_VALUE, null, msgs);
            if (newMinimumValue != null)
                msgs = ((InternalEObject)newMinimumValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows20Package.RANGE_TYPE__MINIMUM_VALUE, null, msgs);
            msgs = basicSetMinimumValue(newMinimumValue, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.RANGE_TYPE__MINIMUM_VALUE, newMinimumValue, newMinimumValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueType getMaximumValue() {
        return maximumValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMaximumValue(ValueType newMaximumValue, NotificationChain msgs) {
        ValueType oldMaximumValue = maximumValue;
        maximumValue = newMaximumValue;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows20Package.RANGE_TYPE__MAXIMUM_VALUE, oldMaximumValue, newMaximumValue);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMaximumValue(ValueType newMaximumValue) {
        if (newMaximumValue != maximumValue) {
            NotificationChain msgs = null;
            if (maximumValue != null)
                msgs = ((InternalEObject)maximumValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows20Package.RANGE_TYPE__MAXIMUM_VALUE, null, msgs);
            if (newMaximumValue != null)
                msgs = ((InternalEObject)newMaximumValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows20Package.RANGE_TYPE__MAXIMUM_VALUE, null, msgs);
            msgs = basicSetMaximumValue(newMaximumValue, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.RANGE_TYPE__MAXIMUM_VALUE, newMaximumValue, newMaximumValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueType getSpacing() {
        return spacing;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSpacing(ValueType newSpacing, NotificationChain msgs) {
        ValueType oldSpacing = spacing;
        spacing = newSpacing;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Ows20Package.RANGE_TYPE__SPACING, oldSpacing, newSpacing);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSpacing(ValueType newSpacing) {
        if (newSpacing != spacing) {
            NotificationChain msgs = null;
            if (spacing != null)
                msgs = ((InternalEObject)spacing).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Ows20Package.RANGE_TYPE__SPACING, null, msgs);
            if (newSpacing != null)
                msgs = ((InternalEObject)newSpacing).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Ows20Package.RANGE_TYPE__SPACING, null, msgs);
            msgs = basicSetSpacing(newSpacing, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.RANGE_TYPE__SPACING, newSpacing, newSpacing));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeClosureType getRangeClosure() {
        return rangeClosure;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRangeClosure(RangeClosureType newRangeClosure) {
        RangeClosureType oldRangeClosure = rangeClosure;
        rangeClosure = newRangeClosure == null ? RANGE_CLOSURE_EDEFAULT : newRangeClosure;
        boolean oldRangeClosureESet = rangeClosureESet;
        rangeClosureESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Ows20Package.RANGE_TYPE__RANGE_CLOSURE, oldRangeClosure, rangeClosure, !oldRangeClosureESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetRangeClosure() {
        RangeClosureType oldRangeClosure = rangeClosure;
        boolean oldRangeClosureESet = rangeClosureESet;
        rangeClosure = RANGE_CLOSURE_EDEFAULT;
        rangeClosureESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Ows20Package.RANGE_TYPE__RANGE_CLOSURE, oldRangeClosure, RANGE_CLOSURE_EDEFAULT, oldRangeClosureESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetRangeClosure() {
        return rangeClosureESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows20Package.RANGE_TYPE__MINIMUM_VALUE:
                return basicSetMinimumValue(null, msgs);
            case Ows20Package.RANGE_TYPE__MAXIMUM_VALUE:
                return basicSetMaximumValue(null, msgs);
            case Ows20Package.RANGE_TYPE__SPACING:
                return basicSetSpacing(null, msgs);
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
            case Ows20Package.RANGE_TYPE__MINIMUM_VALUE:
                return getMinimumValue();
            case Ows20Package.RANGE_TYPE__MAXIMUM_VALUE:
                return getMaximumValue();
            case Ows20Package.RANGE_TYPE__SPACING:
                return getSpacing();
            case Ows20Package.RANGE_TYPE__RANGE_CLOSURE:
                return getRangeClosure();
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
            case Ows20Package.RANGE_TYPE__MINIMUM_VALUE:
                setMinimumValue((ValueType)newValue);
                return;
            case Ows20Package.RANGE_TYPE__MAXIMUM_VALUE:
                setMaximumValue((ValueType)newValue);
                return;
            case Ows20Package.RANGE_TYPE__SPACING:
                setSpacing((ValueType)newValue);
                return;
            case Ows20Package.RANGE_TYPE__RANGE_CLOSURE:
                setRangeClosure((RangeClosureType)newValue);
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
            case Ows20Package.RANGE_TYPE__MINIMUM_VALUE:
                setMinimumValue((ValueType)null);
                return;
            case Ows20Package.RANGE_TYPE__MAXIMUM_VALUE:
                setMaximumValue((ValueType)null);
                return;
            case Ows20Package.RANGE_TYPE__SPACING:
                setSpacing((ValueType)null);
                return;
            case Ows20Package.RANGE_TYPE__RANGE_CLOSURE:
                unsetRangeClosure();
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
            case Ows20Package.RANGE_TYPE__MINIMUM_VALUE:
                return minimumValue != null;
            case Ows20Package.RANGE_TYPE__MAXIMUM_VALUE:
                return maximumValue != null;
            case Ows20Package.RANGE_TYPE__SPACING:
                return spacing != null;
            case Ows20Package.RANGE_TYPE__RANGE_CLOSURE:
                return isSetRangeClosure();
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
        result.append(" (rangeClosure: ");
        if (rangeClosureESet) result.append(rangeClosure); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //RangeTypeImpl
