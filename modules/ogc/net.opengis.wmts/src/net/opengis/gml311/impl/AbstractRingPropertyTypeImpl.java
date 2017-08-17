/**
 */
package net.opengis.gml311.impl;

import net.opengis.gml311.AbstractRingPropertyType;
import net.opengis.gml311.AbstractRingType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Ring Property Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.AbstractRingPropertyTypeImpl#getRingGroup <em>Ring Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.AbstractRingPropertyTypeImpl#getRing <em>Ring</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AbstractRingPropertyTypeImpl extends MinimalEObjectImpl.Container implements AbstractRingPropertyType {
    /**
     * The cached value of the '{@link #getRingGroup() <em>Ring Group</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRingGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap ringGroup;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AbstractRingPropertyTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getAbstractRingPropertyType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getRingGroup() {
        if (ringGroup == null) {
            ringGroup = new BasicFeatureMap(this, Gml311Package.ABSTRACT_RING_PROPERTY_TYPE__RING_GROUP);
        }
        return ringGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AbstractRingType getRing() {
        return (AbstractRingType)getRingGroup().get(Gml311Package.eINSTANCE.getAbstractRingPropertyType_Ring(), true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetRing(AbstractRingType newRing, NotificationChain msgs) {
        return ((FeatureMap.Internal)getRingGroup()).basicAdd(Gml311Package.eINSTANCE.getAbstractRingPropertyType_Ring(), newRing, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.ABSTRACT_RING_PROPERTY_TYPE__RING_GROUP:
                return ((InternalEList<?>)getRingGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.ABSTRACT_RING_PROPERTY_TYPE__RING:
                return basicSetRing(null, msgs);
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
            case Gml311Package.ABSTRACT_RING_PROPERTY_TYPE__RING_GROUP:
                if (coreType) return getRingGroup();
                return ((FeatureMap.Internal)getRingGroup()).getWrapper();
            case Gml311Package.ABSTRACT_RING_PROPERTY_TYPE__RING:
                return getRing();
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
            case Gml311Package.ABSTRACT_RING_PROPERTY_TYPE__RING_GROUP:
                ((FeatureMap.Internal)getRingGroup()).set(newValue);
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
            case Gml311Package.ABSTRACT_RING_PROPERTY_TYPE__RING_GROUP:
                getRingGroup().clear();
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
            case Gml311Package.ABSTRACT_RING_PROPERTY_TYPE__RING_GROUP:
                return ringGroup != null && !ringGroup.isEmpty();
            case Gml311Package.ABSTRACT_RING_PROPERTY_TYPE__RING:
                return getRing() != null;
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
        result.append(" (ringGroup: ");
        result.append(ringGroup);
        result.append(')');
        return result.toString();
    }

} //AbstractRingPropertyTypeImpl
