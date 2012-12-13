/**
 */
package net.opengis.wcs20.impl;

import net.opengis.wcs20.RangeIntervalType;
import net.opengis.wcs20.RangeItemType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Range Item Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.RangeItemTypeImpl#getRangeComponent <em>Range Component</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.RangeItemTypeImpl#getRangeInterval <em>Range Interval</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RangeItemTypeImpl extends EObjectImpl implements RangeItemType {
    /**
     * The default value of the '{@link #getRangeComponent() <em>Range Component</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRangeComponent()
     * @generated
     * @ordered
     */
    protected static final String RANGE_COMPONENT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRangeComponent() <em>Range Component</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRangeComponent()
     * @generated
     * @ordered
     */
    protected String rangeComponent = RANGE_COMPONENT_EDEFAULT;

    /**
     * The cached value of the '{@link #getRangeInterval() <em>Range Interval</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRangeInterval()
     * @generated
     * @ordered
     */
    protected RangeIntervalType rangeInterval;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RangeItemTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.RANGE_ITEM_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getRangeComponent() {
        return rangeComponent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRangeComponent(String newRangeComponent) {
        String oldRangeComponent = rangeComponent;
        rangeComponent = newRangeComponent;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.RANGE_ITEM_TYPE__RANGE_COMPONENT, oldRangeComponent, rangeComponent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeIntervalType getRangeInterval() {
        if (rangeInterval != null && rangeInterval.eIsProxy()) {
            InternalEObject oldRangeInterval = (InternalEObject)rangeInterval;
            rangeInterval = (RangeIntervalType)eResolveProxy(oldRangeInterval);
            if (rangeInterval != oldRangeInterval) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, Wcs20Package.RANGE_ITEM_TYPE__RANGE_INTERVAL, oldRangeInterval, rangeInterval));
            }
        }
        return rangeInterval;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RangeIntervalType basicGetRangeInterval() {
        return rangeInterval;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRangeInterval(RangeIntervalType newRangeInterval) {
        RangeIntervalType oldRangeInterval = rangeInterval;
        rangeInterval = newRangeInterval;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.RANGE_ITEM_TYPE__RANGE_INTERVAL, oldRangeInterval, rangeInterval));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.RANGE_ITEM_TYPE__RANGE_COMPONENT:
                return getRangeComponent();
            case Wcs20Package.RANGE_ITEM_TYPE__RANGE_INTERVAL:
                if (resolve) return getRangeInterval();
                return basicGetRangeInterval();
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
            case Wcs20Package.RANGE_ITEM_TYPE__RANGE_COMPONENT:
                setRangeComponent((String)newValue);
                return;
            case Wcs20Package.RANGE_ITEM_TYPE__RANGE_INTERVAL:
                setRangeInterval((RangeIntervalType)newValue);
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
            case Wcs20Package.RANGE_ITEM_TYPE__RANGE_COMPONENT:
                setRangeComponent(RANGE_COMPONENT_EDEFAULT);
                return;
            case Wcs20Package.RANGE_ITEM_TYPE__RANGE_INTERVAL:
                setRangeInterval((RangeIntervalType)null);
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
            case Wcs20Package.RANGE_ITEM_TYPE__RANGE_COMPONENT:
                return RANGE_COMPONENT_EDEFAULT == null ? rangeComponent != null : !RANGE_COMPONENT_EDEFAULT.equals(rangeComponent);
            case Wcs20Package.RANGE_ITEM_TYPE__RANGE_INTERVAL:
                return rangeInterval != null;
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
        result.append(" (rangeComponent: ");
        result.append(rangeComponent);
        result.append(')');
        return result.toString();
    }

} //RangeItemTypeImpl
