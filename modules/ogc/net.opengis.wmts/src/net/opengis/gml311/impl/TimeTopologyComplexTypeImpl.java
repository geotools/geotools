/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.TimeTopologyComplexType;
import net.opengis.gml311.TimeTopologyPrimitivePropertyType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Time Topology Complex Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.TimeTopologyComplexTypeImpl#getPrimitive <em>Primitive</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TimeTopologyComplexTypeImpl extends AbstractTimeComplexTypeImpl implements TimeTopologyComplexType {
    /**
     * The cached value of the '{@link #getPrimitive() <em>Primitive</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPrimitive()
     * @generated
     * @ordered
     */
    protected EList<TimeTopologyPrimitivePropertyType> primitive;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TimeTopologyComplexTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getTimeTopologyComplexType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TimeTopologyPrimitivePropertyType> getPrimitive() {
        if (primitive == null) {
            primitive = new EObjectContainmentEList<TimeTopologyPrimitivePropertyType>(TimeTopologyPrimitivePropertyType.class, this, Gml311Package.TIME_TOPOLOGY_COMPLEX_TYPE__PRIMITIVE);
        }
        return primitive;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.TIME_TOPOLOGY_COMPLEX_TYPE__PRIMITIVE:
                return ((InternalEList<?>)getPrimitive()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.TIME_TOPOLOGY_COMPLEX_TYPE__PRIMITIVE:
                return getPrimitive();
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
            case Gml311Package.TIME_TOPOLOGY_COMPLEX_TYPE__PRIMITIVE:
                getPrimitive().clear();
                getPrimitive().addAll((Collection<? extends TimeTopologyPrimitivePropertyType>)newValue);
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
            case Gml311Package.TIME_TOPOLOGY_COMPLEX_TYPE__PRIMITIVE:
                getPrimitive().clear();
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
            case Gml311Package.TIME_TOPOLOGY_COMPLEX_TYPE__PRIMITIVE:
                return primitive != null && !primitive.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //TimeTopologyComplexTypeImpl
