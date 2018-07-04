/**
 */
package net.opengis.gml311.impl;

import java.math.BigDecimal;

import net.opengis.gml311.ClothoidType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.RefLocationType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Clothoid Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.ClothoidTypeImpl#getRefLocation <em>Ref Location</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ClothoidTypeImpl#getScaleFactor <em>Scale Factor</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ClothoidTypeImpl#getStartParameter <em>Start Parameter</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ClothoidTypeImpl#getEndParameter <em>End Parameter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ClothoidTypeImpl extends AbstractCurveSegmentTypeImpl implements ClothoidType {
    /**
     * The cached value of the '{@link #getRefLocation() <em>Ref Location</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRefLocation()
     * @generated
     * @ordered
     */
    protected RefLocationType refLocation;

    /**
     * The default value of the '{@link #getScaleFactor() <em>Scale Factor</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getScaleFactor()
     * @generated
     * @ordered
     */
    protected static final BigDecimal SCALE_FACTOR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getScaleFactor() <em>Scale Factor</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getScaleFactor()
     * @generated
     * @ordered
     */
    protected BigDecimal scaleFactor = SCALE_FACTOR_EDEFAULT;

    /**
     * The default value of the '{@link #getStartParameter() <em>Start Parameter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartParameter()
     * @generated
     * @ordered
     */
    protected static final double START_PARAMETER_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getStartParameter() <em>Start Parameter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStartParameter()
     * @generated
     * @ordered
     */
    protected double startParameter = START_PARAMETER_EDEFAULT;

    /**
     * This is true if the Start Parameter attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean startParameterESet;

    /**
     * The default value of the '{@link #getEndParameter() <em>End Parameter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndParameter()
     * @generated
     * @ordered
     */
    protected static final double END_PARAMETER_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getEndParameter() <em>End Parameter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndParameter()
     * @generated
     * @ordered
     */
    protected double endParameter = END_PARAMETER_EDEFAULT;

    /**
     * This is true if the End Parameter attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean endParameterESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ClothoidTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getClothoidType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RefLocationType getRefLocation() {
        return refLocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRefLocation(RefLocationType newRefLocation, NotificationChain msgs) {
        RefLocationType oldRefLocation = refLocation;
        refLocation = newRefLocation;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.CLOTHOID_TYPE__REF_LOCATION, oldRefLocation, newRefLocation);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRefLocation(RefLocationType newRefLocation) {
        if (newRefLocation != refLocation) {
            NotificationChain msgs = null;
            if (refLocation != null)
                msgs = ((InternalEObject)refLocation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.CLOTHOID_TYPE__REF_LOCATION, null, msgs);
            if (newRefLocation != null)
                msgs = ((InternalEObject)newRefLocation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.CLOTHOID_TYPE__REF_LOCATION, null, msgs);
            msgs = basicSetRefLocation(newRefLocation, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.CLOTHOID_TYPE__REF_LOCATION, newRefLocation, newRefLocation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BigDecimal getScaleFactor() {
        return scaleFactor;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setScaleFactor(BigDecimal newScaleFactor) {
        BigDecimal oldScaleFactor = scaleFactor;
        scaleFactor = newScaleFactor;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.CLOTHOID_TYPE__SCALE_FACTOR, oldScaleFactor, scaleFactor));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getStartParameter() {
        return startParameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setStartParameter(double newStartParameter) {
        double oldStartParameter = startParameter;
        startParameter = newStartParameter;
        boolean oldStartParameterESet = startParameterESet;
        startParameterESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.CLOTHOID_TYPE__START_PARAMETER, oldStartParameter, startParameter, !oldStartParameterESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetStartParameter() {
        double oldStartParameter = startParameter;
        boolean oldStartParameterESet = startParameterESet;
        startParameter = START_PARAMETER_EDEFAULT;
        startParameterESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.CLOTHOID_TYPE__START_PARAMETER, oldStartParameter, START_PARAMETER_EDEFAULT, oldStartParameterESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetStartParameter() {
        return startParameterESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public double getEndParameter() {
        return endParameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEndParameter(double newEndParameter) {
        double oldEndParameter = endParameter;
        endParameter = newEndParameter;
        boolean oldEndParameterESet = endParameterESet;
        endParameterESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.CLOTHOID_TYPE__END_PARAMETER, oldEndParameter, endParameter, !oldEndParameterESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetEndParameter() {
        double oldEndParameter = endParameter;
        boolean oldEndParameterESet = endParameterESet;
        endParameter = END_PARAMETER_EDEFAULT;
        endParameterESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.CLOTHOID_TYPE__END_PARAMETER, oldEndParameter, END_PARAMETER_EDEFAULT, oldEndParameterESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetEndParameter() {
        return endParameterESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.CLOTHOID_TYPE__REF_LOCATION:
                return basicSetRefLocation(null, msgs);
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
            case Gml311Package.CLOTHOID_TYPE__REF_LOCATION:
                return getRefLocation();
            case Gml311Package.CLOTHOID_TYPE__SCALE_FACTOR:
                return getScaleFactor();
            case Gml311Package.CLOTHOID_TYPE__START_PARAMETER:
                return getStartParameter();
            case Gml311Package.CLOTHOID_TYPE__END_PARAMETER:
                return getEndParameter();
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
            case Gml311Package.CLOTHOID_TYPE__REF_LOCATION:
                setRefLocation((RefLocationType)newValue);
                return;
            case Gml311Package.CLOTHOID_TYPE__SCALE_FACTOR:
                setScaleFactor((BigDecimal)newValue);
                return;
            case Gml311Package.CLOTHOID_TYPE__START_PARAMETER:
                setStartParameter((Double)newValue);
                return;
            case Gml311Package.CLOTHOID_TYPE__END_PARAMETER:
                setEndParameter((Double)newValue);
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
            case Gml311Package.CLOTHOID_TYPE__REF_LOCATION:
                setRefLocation((RefLocationType)null);
                return;
            case Gml311Package.CLOTHOID_TYPE__SCALE_FACTOR:
                setScaleFactor(SCALE_FACTOR_EDEFAULT);
                return;
            case Gml311Package.CLOTHOID_TYPE__START_PARAMETER:
                unsetStartParameter();
                return;
            case Gml311Package.CLOTHOID_TYPE__END_PARAMETER:
                unsetEndParameter();
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
            case Gml311Package.CLOTHOID_TYPE__REF_LOCATION:
                return refLocation != null;
            case Gml311Package.CLOTHOID_TYPE__SCALE_FACTOR:
                return SCALE_FACTOR_EDEFAULT == null ? scaleFactor != null : !SCALE_FACTOR_EDEFAULT.equals(scaleFactor);
            case Gml311Package.CLOTHOID_TYPE__START_PARAMETER:
                return isSetStartParameter();
            case Gml311Package.CLOTHOID_TYPE__END_PARAMETER:
                return isSetEndParameter();
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
        result.append(" (scaleFactor: ");
        result.append(scaleFactor);
        result.append(", startParameter: ");
        if (startParameterESet) result.append(startParameter); else result.append("<unset>");
        result.append(", endParameter: ");
        if (endParameterESet) result.append(endParameter); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //ClothoidTypeImpl
