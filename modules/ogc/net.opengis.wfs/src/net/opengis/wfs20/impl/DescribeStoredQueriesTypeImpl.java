/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.net.URI;
import java.util.Collection;

import net.opengis.wfs20.DescribeStoredQueriesType;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Describe Stored Queries Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.DescribeStoredQueriesTypeImpl#getStoredQueryId <em>Stored Query Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DescribeStoredQueriesTypeImpl extends BaseRequestTypeImpl implements DescribeStoredQueriesType {
    /**
     * The cached value of the '{@link #getStoredQueryId() <em>Stored Query Id</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getStoredQueryId()
     * @generated
     * @ordered
     */
    protected EList<URI> storedQueryId;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DescribeStoredQueriesTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.DESCRIBE_STORED_QUERIES_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<URI> getStoredQueryId() {
        if (storedQueryId == null) {
            storedQueryId = new EDataTypeUniqueEList<URI>(URI.class, this, Wfs20Package.DESCRIBE_STORED_QUERIES_TYPE__STORED_QUERY_ID);
        }
        return storedQueryId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wfs20Package.DESCRIBE_STORED_QUERIES_TYPE__STORED_QUERY_ID:
                return getStoredQueryId();
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
            case Wfs20Package.DESCRIBE_STORED_QUERIES_TYPE__STORED_QUERY_ID:
                getStoredQueryId().clear();
                getStoredQueryId().addAll((Collection<? extends URI>)newValue);
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
            case Wfs20Package.DESCRIBE_STORED_QUERIES_TYPE__STORED_QUERY_ID:
                getStoredQueryId().clear();
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
            case Wfs20Package.DESCRIBE_STORED_QUERIES_TYPE__STORED_QUERY_ID:
                return storedQueryId != null && !storedQueryId.isEmpty();
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
        result.append(" (storedQueryId: ");
        result.append(storedQueryId);
        result.append(')');
        return result.toString();
    }

} //DescribeStoredQueriesTypeImpl
