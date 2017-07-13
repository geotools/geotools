/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.GeometricComplexType;
import net.opengis.gml311.GeometricPrimitivePropertyType;
import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Geometric Complex Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.GeometricComplexTypeImpl#getElement <em>Element</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GeometricComplexTypeImpl extends AbstractGeometryTypeImpl implements GeometricComplexType {
    /**
     * The cached value of the '{@link #getElement() <em>Element</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getElement()
     * @generated
     * @ordered
     */
    protected EList<GeometricPrimitivePropertyType> element;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GeometricComplexTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getGeometricComplexType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<GeometricPrimitivePropertyType> getElement() {
        if (element == null) {
            element = new EObjectContainmentEList<GeometricPrimitivePropertyType>(GeometricPrimitivePropertyType.class, this, Gml311Package.GEOMETRIC_COMPLEX_TYPE__ELEMENT);
        }
        return element;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.GEOMETRIC_COMPLEX_TYPE__ELEMENT:
                return ((InternalEList<?>)getElement()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.GEOMETRIC_COMPLEX_TYPE__ELEMENT:
                return getElement();
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
            case Gml311Package.GEOMETRIC_COMPLEX_TYPE__ELEMENT:
                getElement().clear();
                getElement().addAll((Collection<? extends GeometricPrimitivePropertyType>)newValue);
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
            case Gml311Package.GEOMETRIC_COMPLEX_TYPE__ELEMENT:
                getElement().clear();
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
            case Gml311Package.GEOMETRIC_COMPLEX_TYPE__ELEMENT:
                return element != null && !element.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //GeometricComplexTypeImpl
