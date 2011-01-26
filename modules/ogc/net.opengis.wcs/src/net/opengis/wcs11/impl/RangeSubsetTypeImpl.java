/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11.impl;

import java.util.Collection;

import net.opengis.wcs11.FieldSubsetType;
import net.opengis.wcs11.RangeSubsetType;
import net.opengis.wcs11.Wcs111Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Range Subset Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs11.impl.RangeSubsetTypeImpl#getFieldSubset <em>Field Subset</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RangeSubsetTypeImpl extends EObjectImpl implements RangeSubsetType {
    /**
     * The cached value of the '{@link #getFieldSubset() <em>Field Subset</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFieldSubset()
     * @generated
     * @ordered
     */
    protected EList fieldSubset;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RangeSubsetTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return Wcs111Package.Literals.RANGE_SUBSET_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList getFieldSubset() {
        if (fieldSubset == null) {
            fieldSubset = new EObjectContainmentEList(FieldSubsetType.class, this, Wcs111Package.RANGE_SUBSET_TYPE__FIELD_SUBSET);
        }
        return fieldSubset;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wcs111Package.RANGE_SUBSET_TYPE__FIELD_SUBSET:
                return ((InternalEList)getFieldSubset()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs111Package.RANGE_SUBSET_TYPE__FIELD_SUBSET:
                return getFieldSubset();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case Wcs111Package.RANGE_SUBSET_TYPE__FIELD_SUBSET:
                getFieldSubset().clear();
                getFieldSubset().addAll((Collection)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(int featureID) {
        switch (featureID) {
            case Wcs111Package.RANGE_SUBSET_TYPE__FIELD_SUBSET:
                getFieldSubset().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case Wcs111Package.RANGE_SUBSET_TYPE__FIELD_SUBSET:
                return fieldSubset != null && !fieldSubset.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //RangeSubsetTypeImpl
