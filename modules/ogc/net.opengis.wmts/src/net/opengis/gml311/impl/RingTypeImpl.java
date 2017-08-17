/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.CurvePropertyType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.RingType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ring Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.RingTypeImpl#getCurveMember <em>Curve Member</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RingTypeImpl extends AbstractRingTypeImpl implements RingType {
    /**
     * The cached value of the '{@link #getCurveMember() <em>Curve Member</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCurveMember()
     * @generated
     * @ordered
     */
    protected EList<CurvePropertyType> curveMember;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RingTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getRingType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<CurvePropertyType> getCurveMember() {
        if (curveMember == null) {
            curveMember = new EObjectContainmentEList<CurvePropertyType>(CurvePropertyType.class, this, Gml311Package.RING_TYPE__CURVE_MEMBER);
        }
        return curveMember;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.RING_TYPE__CURVE_MEMBER:
                return ((InternalEList<?>)getCurveMember()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.RING_TYPE__CURVE_MEMBER:
                return getCurveMember();
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
            case Gml311Package.RING_TYPE__CURVE_MEMBER:
                getCurveMember().clear();
                getCurveMember().addAll((Collection<? extends CurvePropertyType>)newValue);
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
            case Gml311Package.RING_TYPE__CURVE_MEMBER:
                getCurveMember().clear();
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
            case Gml311Package.RING_TYPE__CURVE_MEMBER:
                return curveMember != null && !curveMember.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //RingTypeImpl
