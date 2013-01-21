/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20.impl;

import java.util.Collection;

import net.opengis.cat.csw20.Csw20Package;
import net.opengis.cat.csw20.DomainValuesType;
import net.opengis.cat.csw20.GetDomainResponseType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get Domain Response Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.impl.GetDomainResponseTypeImpl#getDomainValues <em>Domain Values</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GetDomainResponseTypeImpl extends EObjectImpl implements GetDomainResponseType {
    /**
     * The cached value of the '{@link #getDomainValues() <em>Domain Values</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDomainValues()
     * @generated
     * @ordered
     */
    protected EList<DomainValuesType> domainValues;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected GetDomainResponseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Csw20Package.Literals.GET_DOMAIN_RESPONSE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<DomainValuesType> getDomainValues() {
        if (domainValues == null) {
            domainValues = new EObjectContainmentEList<DomainValuesType>(DomainValuesType.class, this, Csw20Package.GET_DOMAIN_RESPONSE_TYPE__DOMAIN_VALUES);
        }
        return domainValues;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Csw20Package.GET_DOMAIN_RESPONSE_TYPE__DOMAIN_VALUES:
                return ((InternalEList<?>)getDomainValues()).basicRemove(otherEnd, msgs);
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
            case Csw20Package.GET_DOMAIN_RESPONSE_TYPE__DOMAIN_VALUES:
                return getDomainValues();
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
            case Csw20Package.GET_DOMAIN_RESPONSE_TYPE__DOMAIN_VALUES:
                getDomainValues().clear();
                getDomainValues().addAll((Collection<? extends DomainValuesType>)newValue);
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
            case Csw20Package.GET_DOMAIN_RESPONSE_TYPE__DOMAIN_VALUES:
                getDomainValues().clear();
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
            case Csw20Package.GET_DOMAIN_RESPONSE_TYPE__DOMAIN_VALUES:
                return domainValues != null && !domainValues.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //GetDomainResponseTypeImpl
