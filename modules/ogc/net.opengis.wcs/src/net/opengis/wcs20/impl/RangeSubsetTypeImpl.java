/**
 */
package net.opengis.wcs20.impl;

import java.util.Collection;

import net.opengis.wcs20.RangeItemType;
import net.opengis.wcs20.RangeSubsetType;
import net.opengis.wcs20.Wcs20Package;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Range Subset Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wcs20.impl.RangeSubsetTypeImpl#getRangeItems <em>Range Items</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RangeSubsetTypeImpl extends EObjectImpl implements RangeSubsetType {
    /**
     * The cached value of the '{@link #getRangeItems() <em>Range Items</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRangeItems()
     * @generated
     * @ordered
     */
    protected EList<RangeItemType> rangeItems;

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
    @Override
    protected EClass eStaticClass() {
        return Wcs20Package.Literals.RANGE_SUBSET_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<RangeItemType> getRangeItems() {
        if (rangeItems == null) {
            rangeItems = new EObjectResolvingEList<RangeItemType>(RangeItemType.class, this, Wcs20Package.RANGE_SUBSET_TYPE__RANGE_ITEMS);
        }
        return rangeItems;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wcs20Package.RANGE_SUBSET_TYPE__RANGE_ITEMS:
                return getRangeItems();
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
            case Wcs20Package.RANGE_SUBSET_TYPE__RANGE_ITEMS:
                getRangeItems().clear();
                getRangeItems().addAll((Collection<? extends RangeItemType>)newValue);
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
            case Wcs20Package.RANGE_SUBSET_TYPE__RANGE_ITEMS:
                getRangeItems().clear();
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
            case Wcs20Package.RANGE_SUBSET_TYPE__RANGE_ITEMS:
                return rangeItems != null && !rangeItems.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //RangeSubsetTypeImpl
