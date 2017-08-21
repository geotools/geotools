/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.PolygonPatchArrayPropertyType;
import net.opengis.gml311.PolygonPatchType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Polygon Patch Array Property Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.PolygonPatchArrayPropertyTypeImpl#getPolygonPatch <em>Polygon Patch</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PolygonPatchArrayPropertyTypeImpl extends SurfacePatchArrayPropertyTypeImpl implements PolygonPatchArrayPropertyType {
    /**
     * The cached value of the '{@link #getPolygonPatch() <em>Polygon Patch</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPolygonPatch()
     * @generated
     * @ordered
     */
    protected EList<PolygonPatchType> polygonPatch;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PolygonPatchArrayPropertyTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getPolygonPatchArrayPropertyType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<PolygonPatchType> getPolygonPatch() {
        if (polygonPatch == null) {
            polygonPatch = new EObjectContainmentEList<PolygonPatchType>(PolygonPatchType.class, this, Gml311Package.POLYGON_PATCH_ARRAY_PROPERTY_TYPE__POLYGON_PATCH);
        }
        return polygonPatch;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.POLYGON_PATCH_ARRAY_PROPERTY_TYPE__POLYGON_PATCH:
                return ((InternalEList<?>)getPolygonPatch()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.POLYGON_PATCH_ARRAY_PROPERTY_TYPE__POLYGON_PATCH:
                return getPolygonPatch();
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
            case Gml311Package.POLYGON_PATCH_ARRAY_PROPERTY_TYPE__POLYGON_PATCH:
                getPolygonPatch().clear();
                getPolygonPatch().addAll((Collection<? extends PolygonPatchType>)newValue);
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
            case Gml311Package.POLYGON_PATCH_ARRAY_PROPERTY_TYPE__POLYGON_PATCH:
                getPolygonPatch().clear();
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
            case Gml311Package.POLYGON_PATCH_ARRAY_PROPERTY_TYPE__POLYGON_PATCH:
                return polygonPatch != null && !polygonPatch.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //PolygonPatchArrayPropertyTypeImpl
