/**
 */
package net.opengis.gml311.impl;

import java.math.BigInteger;

import java.util.List;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.GridEnvelopeType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Grid Envelope Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.GridEnvelopeTypeImpl#getLow <em>Low</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.GridEnvelopeTypeImpl#getHigh <em>High</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GridEnvelopeTypeImpl extends MinimalEObjectImpl.Container implements GridEnvelopeType {
    /**
     * The default value of the '{@link #getLow() <em>Low</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLow()
     * @generated
     * @ordered
     */
    protected static final List<BigInteger> LOW_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLow() <em>Low</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLow()
     * @generated
     * @ordered
     */
    protected List<BigInteger> low = LOW_EDEFAULT;

    /**
     * The default value of the '{@link #getHigh() <em>High</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHigh()
     * @generated
     * @ordered
     */
    protected static final List<BigInteger> HIGH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getHigh() <em>High</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHigh()
     * @generated
     * @ordered
     */
    protected List<BigInteger> high = HIGH_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GridEnvelopeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getGridEnvelopeType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<BigInteger> getLow() {
        return low;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLow(List<BigInteger> newLow) {
        List<BigInteger> oldLow = low;
        low = newLow;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GRID_ENVELOPE_TYPE__LOW, oldLow, low));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<BigInteger> getHigh() {
        return high;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHigh(List<BigInteger> newHigh) {
        List<BigInteger> oldHigh = high;
        high = newHigh;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.GRID_ENVELOPE_TYPE__HIGH, oldHigh, high));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Gml311Package.GRID_ENVELOPE_TYPE__LOW:
                return getLow();
            case Gml311Package.GRID_ENVELOPE_TYPE__HIGH:
                return getHigh();
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
            case Gml311Package.GRID_ENVELOPE_TYPE__LOW:
                setLow((List<BigInteger>)newValue);
                return;
            case Gml311Package.GRID_ENVELOPE_TYPE__HIGH:
                setHigh((List<BigInteger>)newValue);
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
            case Gml311Package.GRID_ENVELOPE_TYPE__LOW:
                setLow(LOW_EDEFAULT);
                return;
            case Gml311Package.GRID_ENVELOPE_TYPE__HIGH:
                setHigh(HIGH_EDEFAULT);
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
            case Gml311Package.GRID_ENVELOPE_TYPE__LOW:
                return LOW_EDEFAULT == null ? low != null : !LOW_EDEFAULT.equals(low);
            case Gml311Package.GRID_ENVELOPE_TYPE__HIGH:
                return HIGH_EDEFAULT == null ? high != null : !HIGH_EDEFAULT.equals(high);
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
        result.append(" (low: ");
        result.append(low);
        result.append(", high: ");
        result.append(high);
        result.append(')');
        return result.toString();
    }

} //GridEnvelopeTypeImpl
