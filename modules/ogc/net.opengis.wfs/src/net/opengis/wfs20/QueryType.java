/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import java.net.URI;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;

import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

import net.opengis.fes20.AbstractAdhocQueryExpressionType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Query Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.QueryType#getFeatureVersion <em>Feature Version</em>}</li>
 *   <li>{@link net.opengis.wfs20.QueryType#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.wfs20.QueryType#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfs20.QueryType#getPropertyNames <em>Property Names</em>}</li>
 *   <li>{@link net.opengis.wfs20.QueryType#getSortBy <em>Sort By</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getQueryType()
 * @model extendedMetaData="name='QueryType' kind='elementOnly'"
 * @generated
 */
public interface QueryType extends AbstractAdhocQueryExpressionType {
    /**
     * Returns the value of the '<em><b>Feature Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Version</em>' attribute.
     * @see #setFeatureVersion(String)
     * @see net.opengis.wfs20.Wfs20Package#getQueryType_FeatureVersion()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='featureVersion'"
     * @generated
     */
    String getFeatureVersion();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.QueryType#getFeatureVersion <em>Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Version</em>' attribute.
     * @see #getFeatureVersion()
     * @generated
     */
    void setFeatureVersion(String value);

    /**
     * Returns the value of the '<em><b>Srs Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Srs Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Srs Name</em>' attribute.
     * @see #setSrsName(URI)
     * @see net.opengis.wfs20.Wfs20Package#getQueryType_SrsName()
     * @model dataType="net.opengis.wfs20.URI"
     * @generated
     */
    URI getSrsName();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.QueryType#getSrsName <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Srs Name</em>' attribute.
     * @see #getSrsName()
     * @generated
     */
    void setSrsName(URI value);

    /**
     * Convenience method to cast {@link #getAbstractSelectionClause()} to a filter object.
     *  
     * @model
     */
    Filter getFilter();
    
    /**
     * Sets the value of the '{@link net.opengis.wfs20.QueryType#getFilter <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Filter</em>' attribute.
     * @see #getFilter()
     * @generated
     */
    void setFilter(Filter value);

    /**
     * Convenience method to cast {@link #getAbstractProjectionClause()} to list of QName.
     * 
     * @model
     */
    EList<QName> getPropertyNames();
    
    /**
     * Convenience method to cast {@link #getAbstractSortingClause()} to a list of SortBy.
     * 
     * @model
     */
    EList<SortBy> getSortBy();

} // QueryType
