/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.SurfacePatchArrayPropertyType;
import net.opengis.gml311.SurfaceType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Surface Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.SurfaceTypeImpl#getPatchesGroup <em>Patches Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.SurfaceTypeImpl#getPatches <em>Patches</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SurfaceTypeImpl extends AbstractSurfaceTypeImpl implements SurfaceType {
    /**
     * The cached value of the '{@link #getPatchesGroup() <em>Patches Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPatchesGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap patchesGroup;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SurfaceTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getSurfaceType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getPatchesGroup() {
        if (patchesGroup == null) {
            patchesGroup = new BasicFeatureMap(this, Gml311Package.SURFACE_TYPE__PATCHES_GROUP);
        }
        return patchesGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SurfacePatchArrayPropertyType getPatches() {
        return (SurfacePatchArrayPropertyType)getPatchesGroup().get(Gml311Package.eINSTANCE.getSurfaceType_Patches(), true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetPatches(SurfacePatchArrayPropertyType newPatches, NotificationChain msgs) {
        return ((FeatureMap.Internal)getPatchesGroup()).basicAdd(Gml311Package.eINSTANCE.getSurfaceType_Patches(), newPatches, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPatches(SurfacePatchArrayPropertyType newPatches) {
        ((FeatureMap.Internal)getPatchesGroup()).set(Gml311Package.eINSTANCE.getSurfaceType_Patches(), newPatches);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.SURFACE_TYPE__PATCHES_GROUP:
                return ((InternalEList<?>)getPatchesGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.SURFACE_TYPE__PATCHES:
                return basicSetPatches(null, msgs);
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
            case Gml311Package.SURFACE_TYPE__PATCHES_GROUP:
                if (coreType) return getPatchesGroup();
                return ((FeatureMap.Internal)getPatchesGroup()).getWrapper();
            case Gml311Package.SURFACE_TYPE__PATCHES:
                return getPatches();
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
            case Gml311Package.SURFACE_TYPE__PATCHES_GROUP:
                ((FeatureMap.Internal)getPatchesGroup()).set(newValue);
                return;
            case Gml311Package.SURFACE_TYPE__PATCHES:
                setPatches((SurfacePatchArrayPropertyType)newValue);
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
            case Gml311Package.SURFACE_TYPE__PATCHES_GROUP:
                getPatchesGroup().clear();
                return;
            case Gml311Package.SURFACE_TYPE__PATCHES:
                setPatches((SurfacePatchArrayPropertyType)null);
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
            case Gml311Package.SURFACE_TYPE__PATCHES_GROUP:
                return patchesGroup != null && !patchesGroup.isEmpty();
            case Gml311Package.SURFACE_TYPE__PATCHES:
                return getPatches() != null;
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
        result.append(" (patchesGroup: ");
        result.append(patchesGroup);
        result.append(')');
        return result.toString();
    }

} //SurfaceTypeImpl
