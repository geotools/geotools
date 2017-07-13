/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.MultiCurveDomainType;
import net.opengis.gml311.MultiCurveType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Multi Curve Domain Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.MultiCurveDomainTypeImpl#getMultiCurve <em>Multi Curve</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MultiCurveDomainTypeImpl extends DomainSetTypeImpl implements MultiCurveDomainType {
    /**
     * The cached value of the '{@link #getMultiCurve() <em>Multi Curve</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMultiCurve()
     * @generated
     * @ordered
     */
    protected MultiCurveType multiCurve;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected MultiCurveDomainTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getMultiCurveDomainType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MultiCurveType getMultiCurve() {
        return multiCurve;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetMultiCurve(MultiCurveType newMultiCurve, NotificationChain msgs) {
        MultiCurveType oldMultiCurve = multiCurve;
        multiCurve = newMultiCurve;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.MULTI_CURVE_DOMAIN_TYPE__MULTI_CURVE, oldMultiCurve, newMultiCurve);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMultiCurve(MultiCurveType newMultiCurve) {
        if (newMultiCurve != multiCurve) {
            NotificationChain msgs = null;
            if (multiCurve != null)
                msgs = ((InternalEObject)multiCurve).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MULTI_CURVE_DOMAIN_TYPE__MULTI_CURVE, null, msgs);
            if (newMultiCurve != null)
                msgs = ((InternalEObject)newMultiCurve).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.MULTI_CURVE_DOMAIN_TYPE__MULTI_CURVE, null, msgs);
            msgs = basicSetMultiCurve(newMultiCurve, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.MULTI_CURVE_DOMAIN_TYPE__MULTI_CURVE, newMultiCurve, newMultiCurve));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.MULTI_CURVE_DOMAIN_TYPE__MULTI_CURVE:
                return basicSetMultiCurve(null, msgs);
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
            case Gml311Package.MULTI_CURVE_DOMAIN_TYPE__MULTI_CURVE:
                return getMultiCurve();
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
            case Gml311Package.MULTI_CURVE_DOMAIN_TYPE__MULTI_CURVE:
                setMultiCurve((MultiCurveType)newValue);
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
            case Gml311Package.MULTI_CURVE_DOMAIN_TYPE__MULTI_CURVE:
                setMultiCurve((MultiCurveType)null);
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
            case Gml311Package.MULTI_CURVE_DOMAIN_TYPE__MULTI_CURVE:
                return multiCurve != null;
        }
        return super.eIsSet(featureID);
    }

} //MultiCurveDomainTypeImpl
