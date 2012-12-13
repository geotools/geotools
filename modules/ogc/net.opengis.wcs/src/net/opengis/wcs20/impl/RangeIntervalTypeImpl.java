/**
 */
package net.opengis.wcs20.impl;

import net.opengis.wcs20.RangeIntervalType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Range Interval Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.RangeIntervalTypeImpl#getStartComponent <em>Start Component</em>}</li>
 *   <li>{@link net.opengis.wcs20.impl.RangeIntervalTypeImpl#getEndComponent <em>End Component</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RangeIntervalTypeImpl extends EObjectImpl implements RangeIntervalType {
    /**
     * The default value of the '{@link #getStartComponent() <em>Start Component</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartComponent()
     * @generated
     * @ordered
     */
    protected static final String START_COMPONENT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getStartComponent() <em>Start Component</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartComponent()
     * @generated
     * @ordered
     */
    protected String startComponent = START_COMPONENT_EDEFAULT;

    /**
     * The default value of the '{@link #getEndComponent() <em>End Component</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndComponent()
     * @generated
     * @ordered
     */
    protected static final String END_COMPONENT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEndComponent() <em>End Component</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndComponent()
     * @generated
     * @ordered
     */
    protected String endComponent = END_COMPONENT_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RangeIntervalTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.RANGE_INTERVAL_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getStartComponent() {
        return startComponent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStartComponent(String newStartComponent) {
        String oldStartComponent = startComponent;
        startComponent = newStartComponent;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.RANGE_INTERVAL_TYPE__START_COMPONENT, oldStartComponent, startComponent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getEndComponent() {
        return endComponent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEndComponent(String newEndComponent) {
        String oldEndComponent = endComponent;
        endComponent = newEndComponent;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wcs20Package.RANGE_INTERVAL_TYPE__END_COMPONENT, oldEndComponent, endComponent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.RANGE_INTERVAL_TYPE__START_COMPONENT:
                return getStartComponent();
            case Wcs20Package.RANGE_INTERVAL_TYPE__END_COMPONENT:
                return getEndComponent();
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
            case Wcs20Package.RANGE_INTERVAL_TYPE__START_COMPONENT:
                setStartComponent((String)newValue);
                return;
            case Wcs20Package.RANGE_INTERVAL_TYPE__END_COMPONENT:
                setEndComponent((String)newValue);
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
            case Wcs20Package.RANGE_INTERVAL_TYPE__START_COMPONENT:
                setStartComponent(START_COMPONENT_EDEFAULT);
                return;
            case Wcs20Package.RANGE_INTERVAL_TYPE__END_COMPONENT:
                setEndComponent(END_COMPONENT_EDEFAULT);
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
            case Wcs20Package.RANGE_INTERVAL_TYPE__START_COMPONENT:
                return START_COMPONENT_EDEFAULT == null ? startComponent != null : !START_COMPONENT_EDEFAULT.equals(startComponent);
            case Wcs20Package.RANGE_INTERVAL_TYPE__END_COMPONENT:
                return END_COMPONENT_EDEFAULT == null ? endComponent != null : !END_COMPONENT_EDEFAULT.equals(endComponent);
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
        result.append(" (startComponent: ");
        result.append(startComponent);
        result.append(", endComponent: ");
        result.append(endComponent);
        result.append(')');
        return result.toString();
    }

} //RangeIntervalTypeImpl
