/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.util.Collection;

import net.opengis.wfs20.DescribeStoredQueriesResponseType;
import net.opengis.wfs20.StoredQueryDescriptionType;
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
 * An implementation of the model object '<em><b>Describe Stored Queries Response Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.DescribeStoredQueriesResponseTypeImpl#getStoredQueryDescription <em>Stored Query Description</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DescribeStoredQueriesResponseTypeImpl extends EObjectImpl implements DescribeStoredQueriesResponseType {
    /**
     * The cached value of the '{@link #getStoredQueryDescription() <em>Stored Query Description</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStoredQueryDescription()
     * @generated
     * @ordered
     */
    protected EList<StoredQueryDescriptionType> storedQueryDescription;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DescribeStoredQueriesResponseTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.DESCRIBE_STORED_QUERIES_RESPONSE_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<StoredQueryDescriptionType> getStoredQueryDescription() {
        if (storedQueryDescription == null) {
            storedQueryDescription = new EObjectContainmentEList<StoredQueryDescriptionType>(StoredQueryDescriptionType.class, this, Wfs20Package.DESCRIBE_STORED_QUERIES_RESPONSE_TYPE__STORED_QUERY_DESCRIPTION);
        }
        return storedQueryDescription;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case Wfs20Package.DESCRIBE_STORED_QUERIES_RESPONSE_TYPE__STORED_QUERY_DESCRIPTION:
                return ((InternalEList<?>)getStoredQueryDescription()).basicRemove(otherEnd, msgs);
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
            case Wfs20Package.DESCRIBE_STORED_QUERIES_RESPONSE_TYPE__STORED_QUERY_DESCRIPTION:
                return getStoredQueryDescription();
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
            case Wfs20Package.DESCRIBE_STORED_QUERIES_RESPONSE_TYPE__STORED_QUERY_DESCRIPTION:
                getStoredQueryDescription().clear();
                getStoredQueryDescription().addAll((Collection<? extends StoredQueryDescriptionType>)newValue);
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
            case Wfs20Package.DESCRIBE_STORED_QUERIES_RESPONSE_TYPE__STORED_QUERY_DESCRIPTION:
                getStoredQueryDescription().clear();
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
            case Wfs20Package.DESCRIBE_STORED_QUERIES_RESPONSE_TYPE__STORED_QUERY_DESCRIPTION:
                return storedQueryDescription != null && !storedQueryDescription.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //DescribeStoredQueriesResponseTypeImpl
