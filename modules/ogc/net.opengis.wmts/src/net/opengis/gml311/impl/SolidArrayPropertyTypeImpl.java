/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.AbstractSolidType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.SolidArrayPropertyType;

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
 * An implementation of the model object '<em><b>Solid Array Property Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.SolidArrayPropertyTypeImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.SolidArrayPropertyTypeImpl#getSolidGroup <em>Solid Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.SolidArrayPropertyTypeImpl#getSolid <em>Solid</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SolidArrayPropertyTypeImpl extends MinimalEObjectImpl.Container implements SolidArrayPropertyType {
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
    protected SolidArrayPropertyTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getSolidArrayPropertyType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getGroup() {
        if (group == null) {
            group = new BasicFeatureMap(this, Gml311Package.SOLID_ARRAY_PROPERTY_TYPE__GROUP);
        }
        return group;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getSolidGroup() {
        return (FeatureMap)getGroup().<FeatureMap.Entry>list(Gml311Package.eINSTANCE.getSolidArrayPropertyType_SolidGroup());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AbstractSolidType> getSolid() {
        return getSolidGroup().list(Gml311Package.eINSTANCE.getSolidArrayPropertyType_Solid());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.SOLID_ARRAY_PROPERTY_TYPE__GROUP:
                return ((InternalEList<?>)getGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.SOLID_ARRAY_PROPERTY_TYPE__SOLID_GROUP:
                return ((InternalEList<?>)getSolidGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.SOLID_ARRAY_PROPERTY_TYPE__SOLID:
                return ((InternalEList<?>)getSolid()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.SOLID_ARRAY_PROPERTY_TYPE__GROUP:
                if (coreType) return getGroup();
                return ((FeatureMap.Internal)getGroup()).getWrapper();
            case Gml311Package.SOLID_ARRAY_PROPERTY_TYPE__SOLID_GROUP:
                if (coreType) return getSolidGroup();
                return ((FeatureMap.Internal)getSolidGroup()).getWrapper();
            case Gml311Package.SOLID_ARRAY_PROPERTY_TYPE__SOLID:
                return getSolid();
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
            case Gml311Package.SOLID_ARRAY_PROPERTY_TYPE__GROUP:
                ((FeatureMap.Internal)getGroup()).set(newValue);
                return;
            case Gml311Package.SOLID_ARRAY_PROPERTY_TYPE__SOLID_GROUP:
                ((FeatureMap.Internal)getSolidGroup()).set(newValue);
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
            case Gml311Package.SOLID_ARRAY_PROPERTY_TYPE__GROUP:
                getGroup().clear();
                return;
            case Gml311Package.SOLID_ARRAY_PROPERTY_TYPE__SOLID_GROUP:
                getSolidGroup().clear();
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
            case Gml311Package.SOLID_ARRAY_PROPERTY_TYPE__GROUP:
                return group != null && !group.isEmpty();
            case Gml311Package.SOLID_ARRAY_PROPERTY_TYPE__SOLID_GROUP:
                return !getSolidGroup().isEmpty();
            case Gml311Package.SOLID_ARRAY_PROPERTY_TYPE__SOLID:
                return !getSolid().isEmpty();
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

} //SolidArrayPropertyTypeImpl
