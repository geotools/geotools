/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.CompositeSurfaceType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.SurfacePropertyType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Composite Surface Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.CompositeSurfaceTypeImpl#getSurfaceMember <em>Surface Member</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CompositeSurfaceTypeImpl extends AbstractSurfaceTypeImpl implements CompositeSurfaceType {
    /**
     * The cached value of the '{@link #getSurfaceMember() <em>Surface Member</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSurfaceMember()
     * @generated
     * @ordered
     */
    protected EList<SurfacePropertyType> surfaceMember;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CompositeSurfaceTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getCompositeSurfaceType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SurfacePropertyType> getSurfaceMember() {
        if (surfaceMember == null) {
            surfaceMember = new EObjectContainmentEList<SurfacePropertyType>(SurfacePropertyType.class, this, Gml311Package.COMPOSITE_SURFACE_TYPE__SURFACE_MEMBER);
        }
        return surfaceMember;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.COMPOSITE_SURFACE_TYPE__SURFACE_MEMBER:
                return ((InternalEList<?>)getSurfaceMember()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.COMPOSITE_SURFACE_TYPE__SURFACE_MEMBER:
                return getSurfaceMember();
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
            case Gml311Package.COMPOSITE_SURFACE_TYPE__SURFACE_MEMBER:
                getSurfaceMember().clear();
                getSurfaceMember().addAll((Collection<? extends SurfacePropertyType>)newValue);
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
            case Gml311Package.COMPOSITE_SURFACE_TYPE__SURFACE_MEMBER:
                getSurfaceMember().clear();
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
            case Gml311Package.COMPOSITE_SURFACE_TYPE__SURFACE_MEMBER:
                return surfaceMember != null && !surfaceMember.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //CompositeSurfaceTypeImpl
