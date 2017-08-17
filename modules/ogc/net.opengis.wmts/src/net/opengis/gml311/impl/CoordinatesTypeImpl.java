/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.CoordinatesType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Coordinates Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.CoordinatesTypeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.CoordinatesTypeImpl#getCs <em>Cs</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.CoordinatesTypeImpl#getDecimal <em>Decimal</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.CoordinatesTypeImpl#getTs <em>Ts</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CoordinatesTypeImpl extends MinimalEObjectImpl.Container implements CoordinatesType {
    /**
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final String VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected String value = VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getCs() <em>Cs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCs()
     * @generated
     * @ordered
     */
    protected static final String CS_EDEFAULT = ",";

    /**
     * The cached value of the '{@link #getCs() <em>Cs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCs()
     * @generated
     * @ordered
     */
    protected String cs = CS_EDEFAULT;

    /**
     * This is true if the Cs attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean csESet;

    /**
     * The default value of the '{@link #getDecimal() <em>Decimal</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDecimal()
     * @generated
     * @ordered
     */
    protected static final String DECIMAL_EDEFAULT = ".";

    /**
     * The cached value of the '{@link #getDecimal() <em>Decimal</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDecimal()
     * @generated
     * @ordered
     */
    protected String decimal = DECIMAL_EDEFAULT;

    /**
     * This is true if the Decimal attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean decimalESet;

    /**
     * The default value of the '{@link #getTs() <em>Ts</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTs()
     * @generated
     * @ordered
     */
    protected static final String TS_EDEFAULT = " ";

    /**
     * The cached value of the '{@link #getTs() <em>Ts</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTs()
     * @generated
     * @ordered
     */
    protected String ts = TS_EDEFAULT;

    /**
     * This is true if the Ts attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean tsESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CoordinatesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getCoordinatesType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValue(String newValue) {
        String oldValue = value;
        value = newValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.COORDINATES_TYPE__VALUE, oldValue, value));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCs() {
        return cs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCs(String newCs) {
        String oldCs = cs;
        cs = newCs;
        boolean oldCsESet = csESet;
        csESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.COORDINATES_TYPE__CS, oldCs, cs, !oldCsESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetCs() {
        String oldCs = cs;
        boolean oldCsESet = csESet;
        cs = CS_EDEFAULT;
        csESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.COORDINATES_TYPE__CS, oldCs, CS_EDEFAULT, oldCsESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetCs() {
        return csESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDecimal() {
        return decimal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDecimal(String newDecimal) {
        String oldDecimal = decimal;
        decimal = newDecimal;
        boolean oldDecimalESet = decimalESet;
        decimalESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.COORDINATES_TYPE__DECIMAL, oldDecimal, decimal, !oldDecimalESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetDecimal() {
        String oldDecimal = decimal;
        boolean oldDecimalESet = decimalESet;
        decimal = DECIMAL_EDEFAULT;
        decimalESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.COORDINATES_TYPE__DECIMAL, oldDecimal, DECIMAL_EDEFAULT, oldDecimalESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetDecimal() {
        return decimalESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTs() {
        return ts;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTs(String newTs) {
        String oldTs = ts;
        ts = newTs;
        boolean oldTsESet = tsESet;
        tsESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.COORDINATES_TYPE__TS, oldTs, ts, !oldTsESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetTs() {
        String oldTs = ts;
        boolean oldTsESet = tsESet;
        ts = TS_EDEFAULT;
        tsESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.COORDINATES_TYPE__TS, oldTs, TS_EDEFAULT, oldTsESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetTs() {
        return tsESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Gml311Package.COORDINATES_TYPE__VALUE:
                return getValue();
            case Gml311Package.COORDINATES_TYPE__CS:
                return getCs();
            case Gml311Package.COORDINATES_TYPE__DECIMAL:
                return getDecimal();
            case Gml311Package.COORDINATES_TYPE__TS:
                return getTs();
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
            case Gml311Package.COORDINATES_TYPE__VALUE:
                setValue((String)newValue);
                return;
            case Gml311Package.COORDINATES_TYPE__CS:
                setCs((String)newValue);
                return;
            case Gml311Package.COORDINATES_TYPE__DECIMAL:
                setDecimal((String)newValue);
                return;
            case Gml311Package.COORDINATES_TYPE__TS:
                setTs((String)newValue);
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
            case Gml311Package.COORDINATES_TYPE__VALUE:
                setValue(VALUE_EDEFAULT);
                return;
            case Gml311Package.COORDINATES_TYPE__CS:
                unsetCs();
                return;
            case Gml311Package.COORDINATES_TYPE__DECIMAL:
                unsetDecimal();
                return;
            case Gml311Package.COORDINATES_TYPE__TS:
                unsetTs();
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
            case Gml311Package.COORDINATES_TYPE__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
            case Gml311Package.COORDINATES_TYPE__CS:
                return isSetCs();
            case Gml311Package.COORDINATES_TYPE__DECIMAL:
                return isSetDecimal();
            case Gml311Package.COORDINATES_TYPE__TS:
                return isSetTs();
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
        result.append(", cs: ");
        if (csESet) result.append(cs); else result.append("<unset>");
        result.append(", decimal: ");
        if (decimalESet) result.append(decimal); else result.append("<unset>");
        result.append(", ts: ");
        if (tsESet) result.append(ts); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //CoordinatesTypeImpl
