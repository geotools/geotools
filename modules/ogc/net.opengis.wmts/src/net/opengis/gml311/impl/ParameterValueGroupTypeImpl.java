/**
 */
package net.opengis.gml311.impl;

import java.util.Collection;

import net.opengis.gml311.AbstractGeneralParameterValueType;
import net.opengis.gml311.Gml311Package;
import net.opengis.gml311.OperationParameterGroupRefType;
import net.opengis.gml311.ParameterValueGroupType;

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
 * An implementation of the model object '<em><b>Parameter Value Group Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.impl.ParameterValueGroupTypeImpl#getIncludesValue <em>Includes Value</em>}</li>
 *   <li>{@link net.opengis.gml311.impl.ParameterValueGroupTypeImpl#getValuesOfGroup <em>Values Of Group</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ParameterValueGroupTypeImpl extends AbstractGeneralParameterValueTypeImpl implements ParameterValueGroupType {
    /**
     * The cached value of the '{@link #getIncludesValue() <em>Includes Value</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIncludesValue()
     * @generated
     * @ordered
     */
    protected EList<AbstractGeneralParameterValueType> includesValue;

    /**
     * The cached value of the '{@link #getValuesOfGroup() <em>Values Of Group</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValuesOfGroup()
     * @generated
     * @ordered
     */
    protected OperationParameterGroupRefType valuesOfGroup;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ParameterValueGroupTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Gml311Package.eINSTANCE.getParameterValueGroupType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AbstractGeneralParameterValueType> getIncludesValue() {
        if (includesValue == null) {
            includesValue = new EObjectContainmentEList<AbstractGeneralParameterValueType>(AbstractGeneralParameterValueType.class, this, Gml311Package.PARAMETER_VALUE_GROUP_TYPE__INCLUDES_VALUE);
        }
        return includesValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationParameterGroupRefType getValuesOfGroup() {
        return valuesOfGroup;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetValuesOfGroup(OperationParameterGroupRefType newValuesOfGroup, NotificationChain msgs) {
        OperationParameterGroupRefType oldValuesOfGroup = valuesOfGroup;
        valuesOfGroup = newValuesOfGroup;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Gml311Package.PARAMETER_VALUE_GROUP_TYPE__VALUES_OF_GROUP, oldValuesOfGroup, newValuesOfGroup);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValuesOfGroup(OperationParameterGroupRefType newValuesOfGroup) {
        if (newValuesOfGroup != valuesOfGroup) {
            NotificationChain msgs = null;
            if (valuesOfGroup != null)
                msgs = ((InternalEObject)valuesOfGroup).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Gml311Package.PARAMETER_VALUE_GROUP_TYPE__VALUES_OF_GROUP, null, msgs);
            if (newValuesOfGroup != null)
                msgs = ((InternalEObject)newValuesOfGroup).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Gml311Package.PARAMETER_VALUE_GROUP_TYPE__VALUES_OF_GROUP, null, msgs);
            msgs = basicSetValuesOfGroup(newValuesOfGroup, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Gml311Package.PARAMETER_VALUE_GROUP_TYPE__VALUES_OF_GROUP, newValuesOfGroup, newValuesOfGroup));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Gml311Package.PARAMETER_VALUE_GROUP_TYPE__INCLUDES_VALUE:
                return ((InternalEList<?>)getIncludesValue()).basicRemove(otherEnd, msgs);
            case Gml311Package.PARAMETER_VALUE_GROUP_TYPE__VALUES_OF_GROUP:
                return basicSetValuesOfGroup(null, msgs);
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
            case Gml311Package.PARAMETER_VALUE_GROUP_TYPE__INCLUDES_VALUE:
                return getIncludesValue();
            case Gml311Package.PARAMETER_VALUE_GROUP_TYPE__VALUES_OF_GROUP:
                return getValuesOfGroup();
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
            case Gml311Package.PARAMETER_VALUE_GROUP_TYPE__INCLUDES_VALUE:
                getIncludesValue().clear();
                getIncludesValue().addAll((Collection<? extends AbstractGeneralParameterValueType>)newValue);
                return;
            case Gml311Package.PARAMETER_VALUE_GROUP_TYPE__VALUES_OF_GROUP:
                setValuesOfGroup((OperationParameterGroupRefType)newValue);
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
            case Gml311Package.PARAMETER_VALUE_GROUP_TYPE__INCLUDES_VALUE:
                getIncludesValue().clear();
                return;
            case Gml311Package.PARAMETER_VALUE_GROUP_TYPE__VALUES_OF_GROUP:
                setValuesOfGroup((OperationParameterGroupRefType)null);
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
            case Gml311Package.PARAMETER_VALUE_GROUP_TYPE__INCLUDES_VALUE:
                return includesValue != null && !includesValue.isEmpty();
            case Gml311Package.PARAMETER_VALUE_GROUP_TYPE__VALUES_OF_GROUP:
                return valuesOfGroup != null;
        }
        return super.eIsSet(featureID);
    }

} //ParameterValueGroupTypeImpl
