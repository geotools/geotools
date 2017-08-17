/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.AbstractCurveType;
import net.opengis.gml311.CurveArrayPropertyType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Curve Array Property Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.CurveArrayPropertyTypeImpl#getCurveGroup <em>Curve Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.CurveArrayPropertyTypeImpl#getCurve <em>Curve</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CurveArrayPropertyTypeImpl extends MinimalEObjectImpl.Container implements CurveArrayPropertyType {
    /**
     * The cached value of the '{@link #getCurveGroup() <em>Curve Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCurveGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap curveGroup;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CurveArrayPropertyTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getCurveArrayPropertyType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getCurveGroup() {
        if (curveGroup == null) {
            curveGroup = new BasicFeatureMap(this, Gml311Package.CURVE_ARRAY_PROPERTY_TYPE__CURVE_GROUP);
        }
        return curveGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AbstractCurveType> getCurve() {
        return getCurveGroup().list(Gml311Package.eINSTANCE.getCurveArrayPropertyType_Curve());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.CURVE_ARRAY_PROPERTY_TYPE__CURVE_GROUP:
                return ((InternalEList<?>)getCurveGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.CURVE_ARRAY_PROPERTY_TYPE__CURVE:
                return ((InternalEList<?>)getCurve()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.CURVE_ARRAY_PROPERTY_TYPE__CURVE_GROUP:
                if (coreType) return getCurveGroup();
                return ((FeatureMap.Internal)getCurveGroup()).getWrapper();
            case Gml311Package.CURVE_ARRAY_PROPERTY_TYPE__CURVE:
                return getCurve();
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
            case Gml311Package.CURVE_ARRAY_PROPERTY_TYPE__CURVE_GROUP:
                ((FeatureMap.Internal)getCurveGroup()).set(newValue);
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
            case Gml311Package.CURVE_ARRAY_PROPERTY_TYPE__CURVE_GROUP:
                getCurveGroup().clear();
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
            case Gml311Package.CURVE_ARRAY_PROPERTY_TYPE__CURVE_GROUP:
                return curveGroup != null && !curveGroup.isEmpty();
            case Gml311Package.CURVE_ARRAY_PROPERTY_TYPE__CURVE:
                return !getCurve().isEmpty();
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
        result.append(" (curveGroup: ");
        result.append(curveGroup);
        result.append(')');
        return result.toString();
    }

} //CurveArrayPropertyTypeImpl
