/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20.impl;

import java.net.URI;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.fes20.impl.AbstractAdhocQueryExpressionTypeImpl;

import net.opengis.wfs20.QueryType;
import net.opengis.wfs20.Wfs20Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Query Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs20.impl.QueryTypeImpl#getFeatureVersion <em>Feature Version</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.QueryTypeImpl#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.QueryTypeImpl#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.QueryTypeImpl#getPropertyNames <em>Property Names</em>}</li>
 *   <li>{@link net.opengis.wfs20.impl.QueryTypeImpl#getSortBy <em>Sort By</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class QueryTypeImpl extends AbstractAdhocQueryExpressionTypeImpl implements QueryType {
    /**
     * The default value of the '{@link #getFeatureVersion() <em>Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeatureVersion()
     * @generated
     * @ordered
     */
    protected static final String FEATURE_VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFeatureVersion() <em>Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeatureVersion()
     * @generated
     * @ordered
     */
    protected String featureVersion = FEATURE_VERSION_EDEFAULT;

    /**
     * The default value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
    protected static final URI SRS_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSrsName() <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSrsName()
     * @generated
     * @ordered
     */
    protected URI srsName = SRS_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getFilter() <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilter()
     * @generated
     * @ordered
     */
    protected static final Filter FILTER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFilter() <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFilter()
     * @generated
     * @ordered
     */
    protected Filter filter = FILTER_EDEFAULT;

    /**
     * The cached value of the '{@link #getPropertyNames() <em>Property Names</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPropertyNames()
     * @generated
     * @ordered
     */
    protected EList<QName> propertyNames;

    /**
     * The cached value of the '{@link #getSortBy() <em>Sort By</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSortBy()
     * @generated
     * @ordered
     */
    protected EList<SortBy> sortBy;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected QueryTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return Wfs20Package.Literals.QUERY_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFeatureVersion() {
        return featureVersion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFeatureVersion(String newFeatureVersion) {
        String oldFeatureVersion = featureVersion;
        featureVersion = newFeatureVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.QUERY_TYPE__FEATURE_VERSION, oldFeatureVersion, featureVersion));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public URI getSrsName() {
        return srsName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSrsName(URI newSrsName) {
        URI oldSrsName = srsName;
        srsName = newSrsName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, Wfs20Package.QUERY_TYPE__SRS_NAME, oldSrsName, srsName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case Wfs20Package.QUERY_TYPE__FEATURE_VERSION:
                return getFeatureVersion();
            case Wfs20Package.QUERY_TYPE__SRS_NAME:
                return getSrsName();
            case Wfs20Package.QUERY_TYPE__FILTER:
                return getFilter();
            case Wfs20Package.QUERY_TYPE__PROPERTY_NAMES:
                return getPropertyNames();
            case Wfs20Package.QUERY_TYPE__SORT_BY:
                return getSortBy();
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
            case Wfs20Package.QUERY_TYPE__FEATURE_VERSION:
                setFeatureVersion((String)newValue);
                return;
            case Wfs20Package.QUERY_TYPE__SRS_NAME:
                setSrsName((URI)newValue);
                return;
            case Wfs20Package.QUERY_TYPE__FILTER:
                setFilter((Filter)newValue);
                return;
            case Wfs20Package.QUERY_TYPE__PROPERTY_NAMES:
                getPropertyNames().clear();
                getPropertyNames().addAll((Collection<? extends QName>)newValue);
                return;
            case Wfs20Package.QUERY_TYPE__SORT_BY:
                getSortBy().clear();
                getSortBy().addAll((Collection<? extends SortBy>)newValue);
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
            case Wfs20Package.QUERY_TYPE__FEATURE_VERSION:
                setFeatureVersion(FEATURE_VERSION_EDEFAULT);
                return;
            case Wfs20Package.QUERY_TYPE__SRS_NAME:
                setSrsName(SRS_NAME_EDEFAULT);
                return;
            case Wfs20Package.QUERY_TYPE__FILTER:
                setFilter(FILTER_EDEFAULT);
                return;
            case Wfs20Package.QUERY_TYPE__PROPERTY_NAMES:
                getPropertyNames().clear();
                return;
            case Wfs20Package.QUERY_TYPE__SORT_BY:
                getSortBy().clear();
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
            case Wfs20Package.QUERY_TYPE__FEATURE_VERSION:
                return FEATURE_VERSION_EDEFAULT == null ? featureVersion != null : !FEATURE_VERSION_EDEFAULT.equals(featureVersion);
            case Wfs20Package.QUERY_TYPE__SRS_NAME:
                return SRS_NAME_EDEFAULT == null ? srsName != null : !SRS_NAME_EDEFAULT.equals(srsName);
            case Wfs20Package.QUERY_TYPE__FILTER:
                return FILTER_EDEFAULT == null ? filter != null : !FILTER_EDEFAULT.equals(filter);
            case Wfs20Package.QUERY_TYPE__PROPERTY_NAMES:
                return propertyNames != null && !propertyNames.isEmpty();
            case Wfs20Package.QUERY_TYPE__SORT_BY:
                return sortBy != null && !sortBy.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * @generated NOT
     */
    public void setFilter(Filter filter) {
        setAbstractSelectionClause(filter);
    }

    /**
     * @generated NOT
     */
    public Filter getFilter() {
        return (Filter) getAbstractSelectionClause();
    }
    
    /**
     * @generated NOT
     */
    public EList<QName> getPropertyNames() {
        return (EList) getAbstractProjectionClause();
    }

    /**
     * @generated NOT
     */
    public EList<SortBy> getSortBy() {
        if (abstractSortingClause == null) {
            abstractSortingClause = new UniqueEList<SortBy>();
        }
        return (EList<SortBy>) abstractSortingClause;
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
        result.append(" (featureVersion: ");
        result.append(featureVersion);
        result.append(", srsName: ");
        result.append(srsName);
        result.append(", filter: ");
        result.append(filter);
        result.append(", propertyNames: ");
        result.append(propertyNames);
        result.append(", sortBy: ");
        result.append(sortBy);
        result.append(')');
        return result.toString();
    }

} //QueryTypeImpl
