/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.PointArrayPropertyType;
import net.opengis.gml311.PointType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Point Array Property Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.PointArrayPropertyTypeImpl#getPoint <em>Point</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PointArrayPropertyTypeImpl extends MinimalEObjectImpl.Container implements PointArrayPropertyType {
    /**
     * The cached value of the '{@link #getPoint() <em>Point</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPoint()
     * @generated
     * @ordered
     */
    protected EList<PointType> point;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PointArrayPropertyTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getPointArrayPropertyType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<PointType> getPoint() {
        if (point == null) {
            point = new EObjectContainmentEList<PointType>(PointType.class, this, Gml311Package.POINT_ARRAY_PROPERTY_TYPE__POINT);
        }
        return point;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.POINT_ARRAY_PROPERTY_TYPE__POINT:
                return ((InternalEList<?>)getPoint()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.POINT_ARRAY_PROPERTY_TYPE__POINT:
                return getPoint();
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
            case Gml311Package.POINT_ARRAY_PROPERTY_TYPE__POINT:
                getPoint().clear();
                getPoint().addAll((Collection<? extends PointType>)newValue);
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
            case Gml311Package.POINT_ARRAY_PROPERTY_TYPE__POINT:
                getPoint().clear();
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
            case Gml311Package.POINT_ARRAY_PROPERTY_TYPE__POINT:
                return point != null && !point.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //PointArrayPropertyTypeImpl
