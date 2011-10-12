/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.util.Collection;

import net.opengis.wfs20.ListStoredQueriesResponseType;
import net.opengis.wfs20.StoredQueryListItemType;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>List Stored Queries Response Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.ListStoredQueriesResponseTypeImpl#getStoredQuery <em>Stored Query</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ListStoredQueriesResponseTypeImpl extends EObjectImpl implements ListStoredQueriesResponseType {
    /**
     * The cached value of the '{@link #getStoredQuery() <em>Stored Query</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStoredQuery()
     * @generated
     * @ordered
     */
    protected EList<StoredQueryListItemType> storedQuery;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ListStoredQueriesResponseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.LIST_STORED_QUERIES_RESPONSE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<StoredQueryListItemType> getStoredQuery() {
        if (storedQuery == null) {
            storedQuery = new EObjectContainmentEList<StoredQueryListItemType>(StoredQueryListItemType.class, this, Wfs20Package.LIST_STORED_QUERIES_RESPONSE_TYPE__STORED_QUERY);
        }
        return storedQuery;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wfs20Package.LIST_STORED_QUERIES_RESPONSE_TYPE__STORED_QUERY:
                return ((InternalEList<?>)getStoredQuery()).basicRemove(otherEnd, msgs);
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
            case Wfs20Package.LIST_STORED_QUERIES_RESPONSE_TYPE__STORED_QUERY:
                return getStoredQuery();
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
            case Wfs20Package.LIST_STORED_QUERIES_RESPONSE_TYPE__STORED_QUERY:
                getStoredQuery().clear();
                getStoredQuery().addAll((Collection<? extends StoredQueryListItemType>)newValue);
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
            case Wfs20Package.LIST_STORED_QUERIES_RESPONSE_TYPE__STORED_QUERY:
                getStoredQuery().clear();
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
            case Wfs20Package.LIST_STORED_QUERIES_RESPONSE_TYPE__STORED_QUERY:
                return storedQuery != null && !storedQuery.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ListStoredQueriesResponseTypeImpl
