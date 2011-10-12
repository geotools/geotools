/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import java.util.Collection;

import net.opengis.fes20.Fes20Package;
import net.opengis.fes20.SortByType;
import net.opengis.fes20.SortPropertyType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Sort By Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.SortByTypeImpl#getSortProperty <em>Sort Property</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SortByTypeImpl extends EObjectImpl implements SortByType {
    /**
     * The cached value of the '{@link #getSortProperty() <em>Sort Property</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSortProperty()
     * @generated
     * @ordered
     */
    protected EList<SortPropertyType> sortProperty;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SortByTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.SORT_BY_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<SortPropertyType> getSortProperty() {
        if (sortProperty == null) {
            sortProperty = new EObjectContainmentEList<SortPropertyType>(SortPropertyType.class, this, Fes20Package.SORT_BY_TYPE__SORT_PROPERTY);
        }
        return sortProperty;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Fes20Package.SORT_BY_TYPE__SORT_PROPERTY:
                return ((InternalEList<?>)getSortProperty()).basicRemove(otherEnd, msgs);
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
            case Fes20Package.SORT_BY_TYPE__SORT_PROPERTY:
                return getSortProperty();
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
            case Fes20Package.SORT_BY_TYPE__SORT_PROPERTY:
                getSortProperty().clear();
                getSortProperty().addAll((Collection<? extends SortPropertyType>)newValue);
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
            case Fes20Package.SORT_BY_TYPE__SORT_PROPERTY:
                getSortProperty().clear();
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
            case Fes20Package.SORT_BY_TYPE__SORT_PROPERTY:
                return sortProperty != null && !sortProperty.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //SortByTypeImpl
