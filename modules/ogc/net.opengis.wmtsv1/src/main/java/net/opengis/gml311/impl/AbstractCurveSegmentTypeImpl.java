/**
 */
package net.opengis.gml311.impl;

import java.math.BigInteger;

import net.opengis.gml311.AbstractCurveSegmentType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Curve Segment Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractCurveSegmentTypeImpl#getNumDerivativeInterior <em>Num Derivative Interior</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractCurveSegmentTypeImpl#getNumDerivativesAtEnd <em>Num Derivatives At End</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractCurveSegmentTypeImpl#getNumDerivativesAtStart <em>Num Derivatives At Start</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractCurveSegmentTypeImpl extends MinimalEObjectImpl.Container implements AbstractCurveSegmentType {
    /**
     * The default value of the '{@link #getNumDerivativeInterior() <em>Num Derivative Interior</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNumDerivativeInterior()
     * @generated
     * @ordered
     */
    protected static final BigInteger NUM_DERIVATIVE_INTERIOR_EDEFAULT = new BigInteger("0");

    /**
     * The cached value of the '{@link #getNumDerivativeInterior() <em>Num Derivative Interior</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNumDerivativeInterior()
     * @generated
     * @ordered
     */
    protected BigInteger numDerivativeInterior = NUM_DERIVATIVE_INTERIOR_EDEFAULT;

    /**
     * This is true if the Num Derivative Interior attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean numDerivativeInteriorESet;

    /**
     * The default value of the '{@link #getNumDerivativesAtEnd() <em>Num Derivatives At End</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNumDerivativesAtEnd()
     * @generated
     * @ordered
     */
    protected static final BigInteger NUM_DERIVATIVES_AT_END_EDEFAULT = new BigInteger("0");

    /**
     * The cached value of the '{@link #getNumDerivativesAtEnd() <em>Num Derivatives At End</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNumDerivativesAtEnd()
     * @generated
     * @ordered
     */
    protected BigInteger numDerivativesAtEnd = NUM_DERIVATIVES_AT_END_EDEFAULT;

    /**
     * This is true if the Num Derivatives At End attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean numDerivativesAtEndESet;

    /**
     * The default value of the '{@link #getNumDerivativesAtStart() <em>Num Derivatives At Start</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNumDerivativesAtStart()
     * @generated
     * @ordered
     */
    protected static final BigInteger NUM_DERIVATIVES_AT_START_EDEFAULT = new BigInteger("0");

    /**
     * The cached value of the '{@link #getNumDerivativesAtStart() <em>Num Derivatives At Start</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNumDerivativesAtStart()
     * @generated
     * @ordered
     */
    protected BigInteger numDerivativesAtStart = NUM_DERIVATIVES_AT_START_EDEFAULT;

    /**
     * This is true if the Num Derivatives At Start attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean numDerivativesAtStartESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractCurveSegmentTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractCurveSegmentType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getNumDerivativeInterior() {
        return numDerivativeInterior;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNumDerivativeInterior(BigInteger newNumDerivativeInterior) {
        BigInteger oldNumDerivativeInterior = numDerivativeInterior;
        numDerivativeInterior = newNumDerivativeInterior;
        boolean oldNumDerivativeInteriorESet = numDerivativeInteriorESet;
        numDerivativeInteriorESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVE_INTERIOR, oldNumDerivativeInterior, numDerivativeInterior, !oldNumDerivativeInteriorESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetNumDerivativeInterior() {
        BigInteger oldNumDerivativeInterior = numDerivativeInterior;
        boolean oldNumDerivativeInteriorESet = numDerivativeInteriorESet;
        numDerivativeInterior = NUM_DERIVATIVE_INTERIOR_EDEFAULT;
        numDerivativeInteriorESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVE_INTERIOR, oldNumDerivativeInterior, NUM_DERIVATIVE_INTERIOR_EDEFAULT, oldNumDerivativeInteriorESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetNumDerivativeInterior() {
        return numDerivativeInteriorESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getNumDerivativesAtEnd() {
        return numDerivativesAtEnd;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNumDerivativesAtEnd(BigInteger newNumDerivativesAtEnd) {
        BigInteger oldNumDerivativesAtEnd = numDerivativesAtEnd;
        numDerivativesAtEnd = newNumDerivativesAtEnd;
        boolean oldNumDerivativesAtEndESet = numDerivativesAtEndESet;
        numDerivativesAtEndESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVES_AT_END, oldNumDerivativesAtEnd, numDerivativesAtEnd, !oldNumDerivativesAtEndESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetNumDerivativesAtEnd() {
        BigInteger oldNumDerivativesAtEnd = numDerivativesAtEnd;
        boolean oldNumDerivativesAtEndESet = numDerivativesAtEndESet;
        numDerivativesAtEnd = NUM_DERIVATIVES_AT_END_EDEFAULT;
        numDerivativesAtEndESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVES_AT_END, oldNumDerivativesAtEnd, NUM_DERIVATIVES_AT_END_EDEFAULT, oldNumDerivativesAtEndESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetNumDerivativesAtEnd() {
        return numDerivativesAtEndESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigInteger getNumDerivativesAtStart() {
        return numDerivativesAtStart;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNumDerivativesAtStart(BigInteger newNumDerivativesAtStart) {
        BigInteger oldNumDerivativesAtStart = numDerivativesAtStart;
        numDerivativesAtStart = newNumDerivativesAtStart;
        boolean oldNumDerivativesAtStartESet = numDerivativesAtStartESet;
        numDerivativesAtStartESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVES_AT_START, oldNumDerivativesAtStart, numDerivativesAtStart, !oldNumDerivativesAtStartESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetNumDerivativesAtStart() {
        BigInteger oldNumDerivativesAtStart = numDerivativesAtStart;
        boolean oldNumDerivativesAtStartESet = numDerivativesAtStartESet;
        numDerivativesAtStart = NUM_DERIVATIVES_AT_START_EDEFAULT;
        numDerivativesAtStartESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVES_AT_START, oldNumDerivativesAtStart, NUM_DERIVATIVES_AT_START_EDEFAULT, oldNumDerivativesAtStartESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetNumDerivativesAtStart() {
        return numDerivativesAtStartESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVE_INTERIOR:
                return getNumDerivativeInterior();
            case Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVES_AT_END:
                return getNumDerivativesAtEnd();
            case Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVES_AT_START:
                return getNumDerivativesAtStart();
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
            case Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVE_INTERIOR:
                setNumDerivativeInterior((BigInteger)newValue);
                return;
            case Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVES_AT_END:
                setNumDerivativesAtEnd((BigInteger)newValue);
                return;
            case Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVES_AT_START:
                setNumDerivativesAtStart((BigInteger)newValue);
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
            case Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVE_INTERIOR:
                unsetNumDerivativeInterior();
                return;
            case Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVES_AT_END:
                unsetNumDerivativesAtEnd();
                return;
            case Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVES_AT_START:
                unsetNumDerivativesAtStart();
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
            case Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVE_INTERIOR:
                return isSetNumDerivativeInterior();
            case Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVES_AT_END:
                return isSetNumDerivativesAtEnd();
            case Gml311Package.ABSTRACT_CURVE_SEGMENT_TYPE__NUM_DERIVATIVES_AT_START:
                return isSetNumDerivativesAtStart();
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
        result.append(" (numDerivativeInterior: ");
        if (numDerivativeInteriorESet) result.append(numDerivativeInterior); else result.append("<unset>");
        result.append(", numDerivativesAtEnd: ");
        if (numDerivativesAtEndESet) result.append(numDerivativesAtEnd); else result.append("<unset>");
        result.append(", numDerivativesAtStart: ");
        if (numDerivativesAtStartESet) result.append(numDerivativesAtStart); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //AbstractCurveSegmentTypeImpl
