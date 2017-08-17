/**
 */
package net.opengis.gml311.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.opengis.gml311.DMSAngleType;
import net.opengis.gml311.DegreesType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DMS Angle Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.DMSAngleTypeImpl#getDegrees <em>Degrees</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.DMSAngleTypeImpl#getDecimalMinutes <em>Decimal Minutes</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.DMSAngleTypeImpl#getMinutes <em>Minutes</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.DMSAngleTypeImpl#getSeconds <em>Seconds</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DMSAngleTypeImpl extends MinimalEObjectImpl.Container implements DMSAngleType {
    /**
     * The cached value of the '{@link #getDegrees() <em>Degrees</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDegrees()
     * @generated
     * @ordered
     */
    protected DegreesType degrees;

    /**
     * The default value of the '{@link #getDecimalMinutes() <em>Decimal Minutes</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDecimalMinutes()
     * @generated
     * @ordered
     */
    protected static final BigDecimal DECIMAL_MINUTES_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDecimalMinutes() <em>Decimal Minutes</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDecimalMinutes()
     * @generated
     * @ordered
     */
    protected BigDecimal decimalMinutes = DECIMAL_MINUTES_EDEFAULT;

    /**
     * The default value of the '{@link #getMinutes() <em>Minutes</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinutes()
     * @generated
     * @ordered
     */
    protected static final BigInteger MINUTES_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMinutes() <em>Minutes</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMinutes()
     * @generated
     * @ordered
     */
    protected BigInteger minutes = MINUTES_EDEFAULT;

    /**
     * The default value of the '{@link #getSeconds() <em>Seconds</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSeconds()
     * @generated
     * @ordered
     */
    protected static final BigDecimal SECONDS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSeconds() <em>Seconds</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSeconds()
     * @generated
     * @ordered
     */
    protected BigDecimal seconds = SECONDS_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DMSAngleTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getDMSAngleType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DegreesType getDegrees() {
        return degrees;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetDegrees(DegreesType newDegrees, NotificationChain msgs) {
        DegreesType oldDegrees = degrees;
        degrees = newDegrees;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.DMS_ANGLE_TYPE__DEGREES, oldDegrees, newDegrees);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDegrees(DegreesType newDegrees) {
        if (newDegrees != degrees) {
            NotificationChain msgs = null;
            if (degrees != null)
                msgs = ((InternalEObject)degrees).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.DMS_ANGLE_TYPE__DEGREES, null, msgs);
            if (newDegrees != null)
                msgs = ((InternalEObject)newDegrees).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.DMS_ANGLE_TYPE__DEGREES, null, msgs);
            msgs = basicSetDegrees(newDegrees, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.DMS_ANGLE_TYPE__DEGREES, newDegrees, newDegrees));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigDecimal getDecimalMinutes() {
        return decimalMinutes;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDecimalMinutes(BigDecimal newDecimalMinutes) {
        BigDecimal oldDecimalMinutes = decimalMinutes;
        decimalMinutes = newDecimalMinutes;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.DMS_ANGLE_TYPE__DECIMAL_MINUTES, oldDecimalMinutes, decimalMinutes));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getMinutes() {
        return minutes;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMinutes(BigInteger newMinutes) {
        BigInteger oldMinutes = minutes;
        minutes = newMinutes;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.DMS_ANGLE_TYPE__MINUTES, oldMinutes, minutes));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigDecimal getSeconds() {
        return seconds;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSeconds(BigDecimal newSeconds) {
        BigDecimal oldSeconds = seconds;
        seconds = newSeconds;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.DMS_ANGLE_TYPE__SECONDS, oldSeconds, seconds));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.DMS_ANGLE_TYPE__DEGREES:
                return basicSetDegrees(null, msgs);
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
            case Gml311Package.DMS_ANGLE_TYPE__DEGREES:
                return getDegrees();
            case Gml311Package.DMS_ANGLE_TYPE__DECIMAL_MINUTES:
                return getDecimalMinutes();
            case Gml311Package.DMS_ANGLE_TYPE__MINUTES:
                return getMinutes();
            case Gml311Package.DMS_ANGLE_TYPE__SECONDS:
                return getSeconds();
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
            case Gml311Package.DMS_ANGLE_TYPE__DEGREES:
                setDegrees((DegreesType)newValue);
                return;
            case Gml311Package.DMS_ANGLE_TYPE__DECIMAL_MINUTES:
                setDecimalMinutes((BigDecimal)newValue);
                return;
            case Gml311Package.DMS_ANGLE_TYPE__MINUTES:
                setMinutes((BigInteger)newValue);
                return;
            case Gml311Package.DMS_ANGLE_TYPE__SECONDS:
                setSeconds((BigDecimal)newValue);
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
            case Gml311Package.DMS_ANGLE_TYPE__DEGREES:
                setDegrees((DegreesType)null);
                return;
            case Gml311Package.DMS_ANGLE_TYPE__DECIMAL_MINUTES:
                setDecimalMinutes(DECIMAL_MINUTES_EDEFAULT);
                return;
            case Gml311Package.DMS_ANGLE_TYPE__MINUTES:
                setMinutes(MINUTES_EDEFAULT);
                return;
            case Gml311Package.DMS_ANGLE_TYPE__SECONDS:
                setSeconds(SECONDS_EDEFAULT);
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
            case Gml311Package.DMS_ANGLE_TYPE__DEGREES:
                return degrees != null;
            case Gml311Package.DMS_ANGLE_TYPE__DECIMAL_MINUTES:
                return DECIMAL_MINUTES_EDEFAULT == null ? decimalMinutes != null : !DECIMAL_MINUTES_EDEFAULT.equals(decimalMinutes);
            case Gml311Package.DMS_ANGLE_TYPE__MINUTES:
                return MINUTES_EDEFAULT == null ? minutes != null : !MINUTES_EDEFAULT.equals(minutes);
            case Gml311Package.DMS_ANGLE_TYPE__SECONDS:
                return SECONDS_EDEFAULT == null ? seconds != null : !SECONDS_EDEFAULT.equals(seconds);
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
        result.append(" (decimalMinutes: ");
        result.append(decimalMinutes);
        result.append(", minutes: ");
        result.append(minutes);
        result.append(", seconds: ");
        result.append(seconds);
        result.append(')');
        return result.toString();
    }

} //DMSAngleTypeImpl
