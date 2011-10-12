/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20.impl;

import java.util.Collection;

import net.opengis.fes20.Fes20Package;
import net.opengis.fes20.IdCapabilitiesType;
import net.opengis.fes20.ResourceIdentifierType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Id Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.fes20.impl.IdCapabilitiesTypeImpl#getResourceIdentifier <em>Resource Identifier</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IdCapabilitiesTypeImpl extends EObjectImpl implements IdCapabilitiesType {
    /**
     * The cached value of the '{@link #getResourceIdentifier() <em>Resource Identifier</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResourceIdentifier()
     * @generated
     * @ordered
     */
    protected EList<ResourceIdentifierType> resourceIdentifier;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected IdCapabilitiesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Fes20Package.Literals.ID_CAPABILITIES_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ResourceIdentifierType> getResourceIdentifier() {
        if (resourceIdentifier == null) {
            resourceIdentifier = new EObjectContainmentEList<ResourceIdentifierType>(ResourceIdentifierType.class, this, Fes20Package.ID_CAPABILITIES_TYPE__RESOURCE_IDENTIFIER);
        }
        return resourceIdentifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Fes20Package.ID_CAPABILITIES_TYPE__RESOURCE_IDENTIFIER:
                return ((InternalEList<?>)getResourceIdentifier()).basicRemove(otherEnd, msgs);
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
            case Fes20Package.ID_CAPABILITIES_TYPE__RESOURCE_IDENTIFIER:
                return getResourceIdentifier();
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
            case Fes20Package.ID_CAPABILITIES_TYPE__RESOURCE_IDENTIFIER:
                getResourceIdentifier().clear();
                getResourceIdentifier().addAll((Collection<? extends ResourceIdentifierType>)newValue);
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
            case Fes20Package.ID_CAPABILITIES_TYPE__RESOURCE_IDENTIFIER:
                getResourceIdentifier().clear();
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
            case Fes20Package.ID_CAPABILITIES_TYPE__RESOURCE_IDENTIFIER:
                return resourceIdentifier != null && !resourceIdentifier.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //IdCapabilitiesTypeImpl
