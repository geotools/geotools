/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.util.Collection;

import net.opengis.wfs20.CreateStoredQueryType;
import net.opengis.wfs20.StoredQueryDescriptionType;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Create Stored Query Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.CreateStoredQueryTypeImpl#getStoredQueryDefinition <em>Stored Query Definition</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CreateStoredQueryTypeImpl extends BaseRequestTypeImpl implements CreateStoredQueryType {
    /**
     * The cached value of the '{@link #getStoredQueryDefinition() <em>Stored Query Definition</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStoredQueryDefinition()
     * @generated
     * @ordered
     */
    protected EList<StoredQueryDescriptionType> storedQueryDefinition;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CreateStoredQueryTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.CREATE_STORED_QUERY_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<StoredQueryDescriptionType> getStoredQueryDefinition() {
        if (storedQueryDefinition == null) {
            storedQueryDefinition = new EObjectContainmentEList<StoredQueryDescriptionType>(StoredQueryDescriptionType.class, this, Wfs20Package.CREATE_STORED_QUERY_TYPE__STORED_QUERY_DEFINITION);
        }
        return storedQueryDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wfs20Package.CREATE_STORED_QUERY_TYPE__STORED_QUERY_DEFINITION:
                return ((InternalEList<?>)getStoredQueryDefinition()).basicRemove(otherEnd, msgs);
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
            case Wfs20Package.CREATE_STORED_QUERY_TYPE__STORED_QUERY_DEFINITION:
                return getStoredQueryDefinition();
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
            case Wfs20Package.CREATE_STORED_QUERY_TYPE__STORED_QUERY_DEFINITION:
                getStoredQueryDefinition().clear();
                getStoredQueryDefinition().addAll((Collection<? extends StoredQueryDescriptionType>)newValue);
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
            case Wfs20Package.CREATE_STORED_QUERY_TYPE__STORED_QUERY_DEFINITION:
                getStoredQueryDefinition().clear();
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
            case Wfs20Package.CREATE_STORED_QUERY_TYPE__STORED_QUERY_DEFINITION:
                return storedQueryDefinition != null && !storedQueryDefinition.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //CreateStoredQueryTypeImpl
