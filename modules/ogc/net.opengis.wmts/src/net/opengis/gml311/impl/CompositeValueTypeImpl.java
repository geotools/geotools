/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.CompositeValueType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.ValueArrayPropertyType;
import net.opengis.gml311.ValuePropertyType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Composite Value Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.CompositeValueTypeImpl#getValueComponent <em>Value Component</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.CompositeValueTypeImpl#getValueComponents <em>Value Components</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CompositeValueTypeImpl extends AbstractGMLTypeImpl implements CompositeValueType {
    /**
     * The cached value of the '{@link #getValueComponent() <em>Value Component</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValueComponent()
     * @generated
     * @ordered
     */
    protected EList<ValuePropertyType> valueComponent;

    /**
     * The cached value of the '{@link #getValueComponents() <em>Value Components</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValueComponents()
     * @generated
     * @ordered
     */
    protected ValueArrayPropertyType valueComponents;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CompositeValueTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getCompositeValueType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ValuePropertyType> getValueComponent() {
        if (valueComponent == null) {
            valueComponent = new EObjectContainmentEList<ValuePropertyType>(ValuePropertyType.class, this, Gml311Package.COMPOSITE_VALUE_TYPE__VALUE_COMPONENT);
        }
        return valueComponent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ValueArrayPropertyType getValueComponents() {
        return valueComponents;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValueComponents(ValueArrayPropertyType newValueComponents, NotificationChain msgs) {
        ValueArrayPropertyType oldValueComponents = valueComponents;
        valueComponents = newValueComponents;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.COMPOSITE_VALUE_TYPE__VALUE_COMPONENTS, oldValueComponents, newValueComponents);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValueComponents(ValueArrayPropertyType newValueComponents) {
        if (newValueComponents != valueComponents) {
            NotificationChain msgs = null;
            if (valueComponents != null)
                msgs = ((InternalEObject)valueComponents).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.COMPOSITE_VALUE_TYPE__VALUE_COMPONENTS, null, msgs);
            if (newValueComponents != null)
                msgs = ((InternalEObject)newValueComponents).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.COMPOSITE_VALUE_TYPE__VALUE_COMPONENTS, null, msgs);
            msgs = basicSetValueComponents(newValueComponents, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.COMPOSITE_VALUE_TYPE__VALUE_COMPONENTS, newValueComponents, newValueComponents));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.COMPOSITE_VALUE_TYPE__VALUE_COMPONENT:
                return ((InternalEList<?>)getValueComponent()).basicRemove(otherEnd, msgs);
            case Gml311Package.COMPOSITE_VALUE_TYPE__VALUE_COMPONENTS:
                return basicSetValueComponents(null, msgs);
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
            case Gml311Package.COMPOSITE_VALUE_TYPE__VALUE_COMPONENT:
                return getValueComponent();
            case Gml311Package.COMPOSITE_VALUE_TYPE__VALUE_COMPONENTS:
                return getValueComponents();
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
            case Gml311Package.COMPOSITE_VALUE_TYPE__VALUE_COMPONENT:
                getValueComponent().clear();
                getValueComponent().addAll((Collection<? extends ValuePropertyType>)newValue);
                return;
            case Gml311Package.COMPOSITE_VALUE_TYPE__VALUE_COMPONENTS:
                setValueComponents((ValueArrayPropertyType)newValue);
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
            case Gml311Package.COMPOSITE_VALUE_TYPE__VALUE_COMPONENT:
                getValueComponent().clear();
                return;
            case Gml311Package.COMPOSITE_VALUE_TYPE__VALUE_COMPONENTS:
                setValueComponents((ValueArrayPropertyType)null);
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
            case Gml311Package.COMPOSITE_VALUE_TYPE__VALUE_COMPONENT:
                return valueComponent != null && !valueComponent.isEmpty();
            case Gml311Package.COMPOSITE_VALUE_TYPE__VALUE_COMPONENTS:
                return valueComponents != null;
        }
        return super.eIsSet(featureID);
    }

} //CompositeValueTypeImpl
