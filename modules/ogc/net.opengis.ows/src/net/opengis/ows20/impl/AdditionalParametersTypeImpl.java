/**
 */
package net.opengis.ows20.impl;

import java.util.Collection;

import net.opengis.ows20.AdditionalParameterType;
import net.opengis.ows20.AdditionalParametersType;
import net.opengis.ows20.Ows20Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Additional Parameters Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.ows20.impl.AdditionalParametersTypeImpl#getAdditionalParameter1 <em>Additional Parameter1</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AdditionalParametersTypeImpl extends AdditionalParametersBaseTypeImpl implements AdditionalParametersType {
    /**
     * The cached value of the '{@link #getAdditionalParameter1() <em>Additional Parameter1</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAdditionalParameter1()
     * @generated
     * @ordered
     */
    protected EList<AdditionalParameterType> additionalParameter1;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected AdditionalParametersTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Ows20Package.Literals.ADDITIONAL_PARAMETERS_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AdditionalParameterType> getAdditionalParameter1() {
        if (additionalParameter1 == null) {
            additionalParameter1 = new EObjectContainmentEList<AdditionalParameterType>(AdditionalParameterType.class, this, Ows20Package.ADDITIONAL_PARAMETERS_TYPE__ADDITIONAL_PARAMETER1);
        }
        return additionalParameter1;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Ows20Package.ADDITIONAL_PARAMETERS_TYPE__ADDITIONAL_PARAMETER1:
                return ((InternalEList<?>)getAdditionalParameter1()).basicRemove(otherEnd, msgs);
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
            case Ows20Package.ADDITIONAL_PARAMETERS_TYPE__ADDITIONAL_PARAMETER1:
                return getAdditionalParameter1();
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
            case Ows20Package.ADDITIONAL_PARAMETERS_TYPE__ADDITIONAL_PARAMETER1:
                getAdditionalParameter1().clear();
                getAdditionalParameter1().addAll((Collection<? extends AdditionalParameterType>)newValue);
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
            case Ows20Package.ADDITIONAL_PARAMETERS_TYPE__ADDITIONAL_PARAMETER1:
                getAdditionalParameter1().clear();
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
            case Ows20Package.ADDITIONAL_PARAMETERS_TYPE__ADDITIONAL_PARAMETER1:
                return additionalParameter1 != null && !additionalParameter1.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //AdditionalParametersTypeImpl
