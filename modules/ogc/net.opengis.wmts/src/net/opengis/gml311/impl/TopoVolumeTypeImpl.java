/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.DirectedTopoSolidPropertyType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.TopoVolumeType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Topo Volume Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.TopoVolumeTypeImpl#getDirectedTopoSolid <em>Directed Topo Solid</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TopoVolumeTypeImpl extends AbstractTopologyTypeImpl implements TopoVolumeType {
    /**
     * The cached value of the '{@link #getDirectedTopoSolid() <em>Directed Topo Solid</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDirectedTopoSolid()
     * @generated
     * @ordered
     */
    protected EList<DirectedTopoSolidPropertyType> directedTopoSolid;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TopoVolumeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getTopoVolumeType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DirectedTopoSolidPropertyType> getDirectedTopoSolid() {
        if (directedTopoSolid == null) {
            directedTopoSolid = new EObjectContainmentEList<DirectedTopoSolidPropertyType>(DirectedTopoSolidPropertyType.class, this, Gml311Package.TOPO_VOLUME_TYPE__DIRECTED_TOPO_SOLID);
        }
        return directedTopoSolid;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.TOPO_VOLUME_TYPE__DIRECTED_TOPO_SOLID:
                return ((InternalEList<?>)getDirectedTopoSolid()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.TOPO_VOLUME_TYPE__DIRECTED_TOPO_SOLID:
                return getDirectedTopoSolid();
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
            case Gml311Package.TOPO_VOLUME_TYPE__DIRECTED_TOPO_SOLID:
                getDirectedTopoSolid().clear();
                getDirectedTopoSolid().addAll((Collection<? extends DirectedTopoSolidPropertyType>)newValue);
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
            case Gml311Package.TOPO_VOLUME_TYPE__DIRECTED_TOPO_SOLID:
                getDirectedTopoSolid().clear();
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
            case Gml311Package.TOPO_VOLUME_TYPE__DIRECTED_TOPO_SOLID:
                return directedTopoSolid != null && !directedTopoSolid.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //TopoVolumeTypeImpl
