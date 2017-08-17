/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.TimeOrdinalEraPropertyType;
import net.opengis.gml311.TimeOrdinalReferenceSystemType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Time Ordinal Reference System Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.TimeOrdinalReferenceSystemTypeImpl#getComponent <em>Component</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TimeOrdinalReferenceSystemTypeImpl extends AbstractTimeReferenceSystemTypeImpl implements TimeOrdinalReferenceSystemType {
    /**
     * The cached value of the '{@link #getComponent() <em>Component</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getComponent()
     * @generated
     * @ordered
     */
    protected EList<TimeOrdinalEraPropertyType> component;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TimeOrdinalReferenceSystemTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getTimeOrdinalReferenceSystemType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TimeOrdinalEraPropertyType> getComponent() {
        if (component == null) {
            component = new EObjectContainmentEList<TimeOrdinalEraPropertyType>(TimeOrdinalEraPropertyType.class, this, Gml311Package.TIME_ORDINAL_REFERENCE_SYSTEM_TYPE__COMPONENT);
        }
        return component;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.TIME_ORDINAL_REFERENCE_SYSTEM_TYPE__COMPONENT:
                return ((InternalEList<?>)getComponent()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.TIME_ORDINAL_REFERENCE_SYSTEM_TYPE__COMPONENT:
                return getComponent();
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
            case Gml311Package.TIME_ORDINAL_REFERENCE_SYSTEM_TYPE__COMPONENT:
                getComponent().clear();
                getComponent().addAll((Collection<? extends TimeOrdinalEraPropertyType>)newValue);
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
            case Gml311Package.TIME_ORDINAL_REFERENCE_SYSTEM_TYPE__COMPONENT:
                getComponent().clear();
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
            case Gml311Package.TIME_ORDINAL_REFERENCE_SYSTEM_TYPE__COMPONENT:
                return component != null && !component.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //TimeOrdinalReferenceSystemTypeImpl
