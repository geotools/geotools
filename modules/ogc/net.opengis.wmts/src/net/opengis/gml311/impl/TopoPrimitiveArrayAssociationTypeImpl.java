/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.AbstractTopoPrimitiveType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.TopoPrimitiveArrayAssociationType;

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
 * An implementation of the model object '<em><b>Topo Primitive Array Association Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.TopoPrimitiveArrayAssociationTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TopoPrimitiveArrayAssociationTypeImpl#getTopoPrimitiveGroup <em>Topo Primitive Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.TopoPrimitiveArrayAssociationTypeImpl#getTopoPrimitive <em>Topo Primitive</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TopoPrimitiveArrayAssociationTypeImpl extends MinimalEObjectImpl.Container implements TopoPrimitiveArrayAssociationType {
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
    protected TopoPrimitiveArrayAssociationTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getTopoPrimitiveArrayAssociationType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getGroup() {
        if (group == null) {
            group = new BasicFeatureMap(this, Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE__GROUP);
        }
        return group;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getTopoPrimitiveGroup() {
        return (FeatureMap)getGroup().<FeatureMap.Entry>list(Gml311Package.eINSTANCE.getTopoPrimitiveArrayAssociationType_TopoPrimitiveGroup());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AbstractTopoPrimitiveType> getTopoPrimitive() {
        return getTopoPrimitiveGroup().list(Gml311Package.eINSTANCE.getTopoPrimitiveArrayAssociationType_TopoPrimitive());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE__GROUP:
                return ((InternalEList<?>)getGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE__TOPO_PRIMITIVE_GROUP:
                return ((InternalEList<?>)getTopoPrimitiveGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE__TOPO_PRIMITIVE:
                return ((InternalEList<?>)getTopoPrimitive()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE__GROUP:
                if (coreType) return getGroup();
                return ((FeatureMap.Internal)getGroup()).getWrapper();
            case Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE__TOPO_PRIMITIVE_GROUP:
                if (coreType) return getTopoPrimitiveGroup();
                return ((FeatureMap.Internal)getTopoPrimitiveGroup()).getWrapper();
            case Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE__TOPO_PRIMITIVE:
                return getTopoPrimitive();
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
            case Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE__GROUP:
                ((FeatureMap.Internal)getGroup()).set(newValue);
                return;
            case Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE__TOPO_PRIMITIVE_GROUP:
                ((FeatureMap.Internal)getTopoPrimitiveGroup()).set(newValue);
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
            case Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE__GROUP:
                getGroup().clear();
                return;
            case Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE__TOPO_PRIMITIVE_GROUP:
                getTopoPrimitiveGroup().clear();
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
            case Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE__GROUP:
                return group != null && !group.isEmpty();
            case Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE__TOPO_PRIMITIVE_GROUP:
                return !getTopoPrimitiveGroup().isEmpty();
            case Gml311Package.TOPO_PRIMITIVE_ARRAY_ASSOCIATION_TYPE__TOPO_PRIMITIVE:
                return !getTopoPrimitive().isEmpty();
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

} //TopoPrimitiveArrayAssociationTypeImpl
