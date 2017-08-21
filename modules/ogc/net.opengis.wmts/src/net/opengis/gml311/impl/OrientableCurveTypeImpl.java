/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.CurvePropertyType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.OrientableCurveType;
import net.opengis.gml311.SignType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Orientable Curve Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.OrientableCurveTypeImpl#getBaseCurve <em>Base Curve</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.OrientableCurveTypeImpl#getOrientation <em>Orientation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OrientableCurveTypeImpl extends AbstractCurveTypeImpl implements OrientableCurveType {
    /**
     * The cached value of the '{@link #getBaseCurve() <em>Base Curve</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBaseCurve()
     * @generated
     * @ordered
     */
    protected CurvePropertyType baseCurve;

    /**
     * The default value of the '{@link #getOrientation() <em>Orientation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOrientation()
     * @generated
     * @ordered
     */
    protected static final SignType ORIENTATION_EDEFAULT = SignType._1;

    /**
     * The cached value of the '{@link #getOrientation() <em>Orientation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOrientation()
     * @generated
     * @ordered
     */
    protected SignType orientation = ORIENTATION_EDEFAULT;

    /**
     * This is true if the Orientation attribute has been set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    protected boolean orientationESet;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected OrientableCurveTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getOrientableCurveType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CurvePropertyType getBaseCurve() {
        return baseCurve;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetBaseCurve(CurvePropertyType newBaseCurve, NotificationChain msgs) {
        CurvePropertyType oldBaseCurve = baseCurve;
        baseCurve = newBaseCurve;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.ORIENTABLE_CURVE_TYPE__BASE_CURVE, oldBaseCurve, newBaseCurve);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setBaseCurve(CurvePropertyType newBaseCurve) {
        if (newBaseCurve != baseCurve) {
            NotificationChain msgs = null;
            if (baseCurve != null)
                msgs = ((InternalEObject)baseCurve).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ORIENTABLE_CURVE_TYPE__BASE_CURVE, null, msgs);
            if (newBaseCurve != null)
                msgs = ((InternalEObject)newBaseCurve).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.ORIENTABLE_CURVE_TYPE__BASE_CURVE, null, msgs);
            msgs = basicSetBaseCurve(newBaseCurve, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ORIENTABLE_CURVE_TYPE__BASE_CURVE, newBaseCurve, newBaseCurve));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SignType getOrientation() {
        return orientation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOrientation(SignType newOrientation) {
        SignType oldOrientation = orientation;
        orientation = newOrientation == null ? ORIENTATION_EDEFAULT : newOrientation;
        boolean oldOrientationESet = orientationESet;
        orientationESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.ORIENTABLE_CURVE_TYPE__ORIENTATION, oldOrientation, orientation, !oldOrientationESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void unsetOrientation() {
        SignType oldOrientation = orientation;
        boolean oldOrientationESet = orientationESet;
        orientation = ORIENTATION_EDEFAULT;
        orientationESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, Gml311Package.ORIENTABLE_CURVE_TYPE__ORIENTATION, oldOrientation, ORIENTATION_EDEFAULT, oldOrientationESet));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetOrientation() {
        return orientationESet;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ORIENTABLE_CURVE_TYPE__BASE_CURVE:
                return basicSetBaseCurve(null, msgs);
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
            case Gml311Package.ORIENTABLE_CURVE_TYPE__BASE_CURVE:
                return getBaseCurve();
            case Gml311Package.ORIENTABLE_CURVE_TYPE__ORIENTATION:
                return getOrientation();
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
            case Gml311Package.ORIENTABLE_CURVE_TYPE__BASE_CURVE:
                setBaseCurve((CurvePropertyType)newValue);
                return;
            case Gml311Package.ORIENTABLE_CURVE_TYPE__ORIENTATION:
                setOrientation((SignType)newValue);
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
            case Gml311Package.ORIENTABLE_CURVE_TYPE__BASE_CURVE:
                setBaseCurve((CurvePropertyType)null);
                return;
            case Gml311Package.ORIENTABLE_CURVE_TYPE__ORIENTATION:
                unsetOrientation();
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
            case Gml311Package.ORIENTABLE_CURVE_TYPE__BASE_CURVE:
                return baseCurve != null;
            case Gml311Package.ORIENTABLE_CURVE_TYPE__ORIENTATION:
                return isSetOrientation();
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
        result.append(" (orientation: ");
        if (orientationESet) result.append(orientation); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

} //OrientableCurveTypeImpl
