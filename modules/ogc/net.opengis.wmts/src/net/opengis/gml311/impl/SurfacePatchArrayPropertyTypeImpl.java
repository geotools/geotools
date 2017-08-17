/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.AbstractSurfacePatchType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.SurfacePatchArrayPropertyType;

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
 * An implementation of the model object '<em><b>Surface Patch Array Property Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.SurfacePatchArrayPropertyTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.SurfacePatchArrayPropertyTypeImpl#getSurfacePatchGroup <em>Surface Patch Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.SurfacePatchArrayPropertyTypeImpl#getSurfacePatch <em>Surface Patch</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SurfacePatchArrayPropertyTypeImpl extends MinimalEObjectImpl.Container implements SurfacePatchArrayPropertyType {
    /**
     * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap group;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SurfacePatchArrayPropertyTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getSurfacePatchArrayPropertyType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getGroup() {
        if (group == null) {
            group = new BasicFeatureMap(this, Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE__GROUP);
        }
        return group;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getSurfacePatchGroup() {
        return (FeatureMap)getGroup().<FeatureMap.Entry>list(Gml311Package.eINSTANCE.getSurfacePatchArrayPropertyType_SurfacePatchGroup());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AbstractSurfacePatchType> getSurfacePatch() {
        return getSurfacePatchGroup().list(Gml311Package.eINSTANCE.getSurfacePatchArrayPropertyType_SurfacePatch());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE__GROUP:
                return ((InternalEList<?>)getGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE__SURFACE_PATCH_GROUP:
                return ((InternalEList<?>)getSurfacePatchGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE__SURFACE_PATCH:
                return ((InternalEList<?>)getSurfacePatch()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE__GROUP:
                if (coreType) return getGroup();
                return ((FeatureMap.Internal)getGroup()).getWrapper();
            case Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE__SURFACE_PATCH_GROUP:
                if (coreType) return getSurfacePatchGroup();
                return ((FeatureMap.Internal)getSurfacePatchGroup()).getWrapper();
            case Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE__SURFACE_PATCH:
                return getSurfacePatch();
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
            case Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE__GROUP:
                ((FeatureMap.Internal)getGroup()).set(newValue);
                return;
            case Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE__SURFACE_PATCH_GROUP:
                ((FeatureMap.Internal)getSurfacePatchGroup()).set(newValue);
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
            case Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE__GROUP:
                getGroup().clear();
                return;
            case Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE__SURFACE_PATCH_GROUP:
                getSurfacePatchGroup().clear();
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
            case Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE__GROUP:
                return group != null && !group.isEmpty();
            case Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE__SURFACE_PATCH_GROUP:
                return !getSurfacePatchGroup().isEmpty();
            case Gml311Package.SURFACE_PATCH_ARRAY_PROPERTY_TYPE__SURFACE_PATCH:
                return !getSurfacePatch().isEmpty();
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
        result.append(" (group: ");
        result.append(group);
        result.append(')');
        return result.toString();
    }

} //SurfacePatchArrayPropertyTypeImpl
