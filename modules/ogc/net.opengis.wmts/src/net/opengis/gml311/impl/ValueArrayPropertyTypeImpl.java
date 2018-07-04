/**
 */
package net.opengis.gml311.impl;

import java.math.BigInteger;

import java.util.Collection;
import java.util.List;

import net.opengis.gml311.CategoryExtentType;
import net.opengis.gml311.CodeOrNullListType;
import net.opengis.gml311.CodeType;
import net.opengis.gml311.CompositeValueType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.MeasureOrNullListType;
import net.opengis.gml311.MeasureType;
import net.opengis.gml311.QuantityExtentType;
import net.opengis.gml311.ValueArrayPropertyType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Value Array Property Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getBoolean <em>Boolean</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getCategory <em>Category</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getQuantity <em>Quantity</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getCount <em>Count</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getBooleanList <em>Boolean List</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getCategoryList <em>Category List</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getQuantityList <em>Quantity List</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getCountList <em>Count List</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getCategoryExtent <em>Category Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getQuantityExtent <em>Quantity Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getCountExtent <em>Count Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getCompositeValueGroup <em>Composite Value Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getCompositeValue <em>Composite Value</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getObjectGroup <em>Object Group</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getObject <em>Object</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ValueArrayPropertyTypeImpl#getNull <em>Null</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ValueArrayPropertyTypeImpl extends MinimalEObjectImpl.Container implements ValueArrayPropertyType {
    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected FeatureMap value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ValueArrayPropertyTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getValueArrayPropertyType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getValue() {
        if (value == null) {
            value = new BasicFeatureMap(this, Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__VALUE);
        }
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Boolean> getBoolean() {
        return getValue().list(Gml311Package.eINSTANCE.getValueArrayPropertyType_Boolean());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<CodeType> getCategory() {
        return getValue().list(Gml311Package.eINSTANCE.getValueArrayPropertyType_Category());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<MeasureType> getQuantity() {
        return getValue().list(Gml311Package.eINSTANCE.getValueArrayPropertyType_Quantity());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<BigInteger> getCount() {
        return getValue().list(Gml311Package.eINSTANCE.getValueArrayPropertyType_Count());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EList<List> getBooleanList() {
        return getValue().list(Gml311Package.eINSTANCE.getValueArrayPropertyType_BooleanList());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<CodeOrNullListType> getCategoryList() {
        return getValue().list(Gml311Package.eINSTANCE.getValueArrayPropertyType_CategoryList());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<MeasureOrNullListType> getQuantityList() {
        return getValue().list(Gml311Package.eINSTANCE.getValueArrayPropertyType_QuantityList());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EList<List> getCountList() {
        return getValue().list(Gml311Package.eINSTANCE.getValueArrayPropertyType_CountList());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<CategoryExtentType> getCategoryExtent() {
        return getValue().list(Gml311Package.eINSTANCE.getValueArrayPropertyType_CategoryExtent());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<QuantityExtentType> getQuantityExtent() {
        return getValue().list(Gml311Package.eINSTANCE.getValueArrayPropertyType_QuantityExtent());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public EList<List> getCountExtent() {
        return getValue().list(Gml311Package.eINSTANCE.getValueArrayPropertyType_CountExtent());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getCompositeValueGroup() {
        return (FeatureMap)getValue().<FeatureMap.Entry>list(Gml311Package.eINSTANCE.getValueArrayPropertyType_CompositeValueGroup());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<CompositeValueType> getCompositeValue() {
        return getCompositeValueGroup().list(Gml311Package.eINSTANCE.getValueArrayPropertyType_CompositeValue());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureMap getObjectGroup() {
        return (FeatureMap)getValue().<FeatureMap.Entry>list(Gml311Package.eINSTANCE.getValueArrayPropertyType_ObjectGroup());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<EObject> getObject() {
        return getObjectGroup().list(Gml311Package.eINSTANCE.getValueArrayPropertyType_Object());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Object> getNull() {
        return getValue().list(Gml311Package.eINSTANCE.getValueArrayPropertyType_Null());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__VALUE:
                return ((InternalEList<?>)getValue()).basicRemove(otherEnd, msgs);
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__CATEGORY:
                return ((InternalEList<?>)getCategory()).basicRemove(otherEnd, msgs);
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__QUANTITY:
                return ((InternalEList<?>)getQuantity()).basicRemove(otherEnd, msgs);
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__CATEGORY_LIST:
                return ((InternalEList<?>)getCategoryList()).basicRemove(otherEnd, msgs);
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__QUANTITY_LIST:
                return ((InternalEList<?>)getQuantityList()).basicRemove(otherEnd, msgs);
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__CATEGORY_EXTENT:
                return ((InternalEList<?>)getCategoryExtent()).basicRemove(otherEnd, msgs);
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__QUANTITY_EXTENT:
                return ((InternalEList<?>)getQuantityExtent()).basicRemove(otherEnd, msgs);
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COMPOSITE_VALUE_GROUP:
                return ((InternalEList<?>)getCompositeValueGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COMPOSITE_VALUE:
                return ((InternalEList<?>)getCompositeValue()).basicRemove(otherEnd, msgs);
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__OBJECT_GROUP:
                return ((InternalEList<?>)getObjectGroup()).basicRemove(otherEnd, msgs);
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__OBJECT:
                return ((InternalEList<?>)getObject()).basicRemove(otherEnd, msgs);
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
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__VALUE:
                if (coreType) return getValue();
                return ((FeatureMap.Internal)getValue()).getWrapper();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__BOOLEAN:
                return getBoolean();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__CATEGORY:
                return getCategory();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__QUANTITY:
                return getQuantity();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COUNT:
                return getCount();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__BOOLEAN_LIST:
                return getBooleanList();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__CATEGORY_LIST:
                return getCategoryList();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__QUANTITY_LIST:
                return getQuantityList();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COUNT_LIST:
                return getCountList();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__CATEGORY_EXTENT:
                return getCategoryExtent();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__QUANTITY_EXTENT:
                return getQuantityExtent();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COUNT_EXTENT:
                return getCountExtent();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COMPOSITE_VALUE_GROUP:
                if (coreType) return getCompositeValueGroup();
                return ((FeatureMap.Internal)getCompositeValueGroup()).getWrapper();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COMPOSITE_VALUE:
                return getCompositeValue();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__OBJECT_GROUP:
                if (coreType) return getObjectGroup();
                return ((FeatureMap.Internal)getObjectGroup()).getWrapper();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__OBJECT:
                return getObject();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__NULL:
                return getNull();
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
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__VALUE:
                ((FeatureMap.Internal)getValue()).set(newValue);
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__BOOLEAN:
                getBoolean().clear();
                getBoolean().addAll((Collection<? extends Boolean>)newValue);
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__CATEGORY:
                getCategory().clear();
                getCategory().addAll((Collection<? extends CodeType>)newValue);
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__QUANTITY:
                getQuantity().clear();
                getQuantity().addAll((Collection<? extends MeasureType>)newValue);
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COUNT:
                getCount().clear();
                getCount().addAll((Collection<? extends BigInteger>)newValue);
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__BOOLEAN_LIST:
                getBooleanList().clear();
                getBooleanList().addAll((Collection<? extends List>)newValue);
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__CATEGORY_LIST:
                getCategoryList().clear();
                getCategoryList().addAll((Collection<? extends CodeOrNullListType>)newValue);
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__QUANTITY_LIST:
                getQuantityList().clear();
                getQuantityList().addAll((Collection<? extends MeasureOrNullListType>)newValue);
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COUNT_LIST:
                getCountList().clear();
                getCountList().addAll((Collection<? extends List>)newValue);
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__CATEGORY_EXTENT:
                getCategoryExtent().clear();
                getCategoryExtent().addAll((Collection<? extends CategoryExtentType>)newValue);
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__QUANTITY_EXTENT:
                getQuantityExtent().clear();
                getQuantityExtent().addAll((Collection<? extends QuantityExtentType>)newValue);
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COUNT_EXTENT:
                getCountExtent().clear();
                getCountExtent().addAll((Collection<? extends List>)newValue);
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COMPOSITE_VALUE_GROUP:
                ((FeatureMap.Internal)getCompositeValueGroup()).set(newValue);
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COMPOSITE_VALUE:
                getCompositeValue().clear();
                getCompositeValue().addAll((Collection<? extends CompositeValueType>)newValue);
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__OBJECT_GROUP:
                ((FeatureMap.Internal)getObjectGroup()).set(newValue);
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__NULL:
                getNull().clear();
                getNull().addAll((Collection<? extends Object>)newValue);
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
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__VALUE:
                getValue().clear();
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__BOOLEAN:
                getBoolean().clear();
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__CATEGORY:
                getCategory().clear();
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__QUANTITY:
                getQuantity().clear();
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COUNT:
                getCount().clear();
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__BOOLEAN_LIST:
                getBooleanList().clear();
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__CATEGORY_LIST:
                getCategoryList().clear();
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__QUANTITY_LIST:
                getQuantityList().clear();
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COUNT_LIST:
                getCountList().clear();
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__CATEGORY_EXTENT:
                getCategoryExtent().clear();
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__QUANTITY_EXTENT:
                getQuantityExtent().clear();
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COUNT_EXTENT:
                getCountExtent().clear();
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COMPOSITE_VALUE_GROUP:
                getCompositeValueGroup().clear();
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COMPOSITE_VALUE:
                getCompositeValue().clear();
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__OBJECT_GROUP:
                getObjectGroup().clear();
                return;
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__NULL:
                getNull().clear();
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
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__VALUE:
                return value != null && !value.isEmpty();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__BOOLEAN:
                return !getBoolean().isEmpty();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__CATEGORY:
                return !getCategory().isEmpty();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__QUANTITY:
                return !getQuantity().isEmpty();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COUNT:
                return !getCount().isEmpty();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__BOOLEAN_LIST:
                return !getBooleanList().isEmpty();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__CATEGORY_LIST:
                return !getCategoryList().isEmpty();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__QUANTITY_LIST:
                return !getQuantityList().isEmpty();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COUNT_LIST:
                return !getCountList().isEmpty();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__CATEGORY_EXTENT:
                return !getCategoryExtent().isEmpty();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__QUANTITY_EXTENT:
                return !getQuantityExtent().isEmpty();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COUNT_EXTENT:
                return !getCountExtent().isEmpty();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COMPOSITE_VALUE_GROUP:
                return !getCompositeValueGroup().isEmpty();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__COMPOSITE_VALUE:
                return !getCompositeValue().isEmpty();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__OBJECT_GROUP:
                return !getObjectGroup().isEmpty();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__OBJECT:
                return !getObject().isEmpty();
            case Gml311Package.VALUE_ARRAY_PROPERTY_TYPE__NULL:
                return !getNull().isEmpty();
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
        result.append(" (value: ");
        result.append(value);
        result.append(')');
        return result.toString();
    }

} //ValueArrayPropertyTypeImpl
