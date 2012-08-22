/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.util.List;

import javax.xml.namespace.QName;

import org.opengis.filter.sort.SortBy;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Query Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Specifies a query to execute against instances of one or
 *          more object types. A set of ElementName elements may be included
 *          to specify an adhoc view of the csw:Record instances in the result
 *          set. Otherwise, use ElementSetName to specify a predefined view.
 *          The Constraint element contains a query filter expressed in a
 *          supported query language. A sorting criterion that specifies a
 *          property to sort by may be included.
 * 
 *          typeNames - a list of object types to query.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.QueryType#getElementSetName <em>Element Set Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.QueryType#getElementName <em>Element Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.QueryType#getConstraint <em>Constraint</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.QueryType#getSortBy <em>Sort By</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.QueryType#getTypeNames <em>Type Names</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getQueryType()
 * @model extendedMetaData="name='QueryType' kind='elementOnly'"
 * @generated
 */
public interface QueryType extends AbstractQueryType {
    /**
     * Returns the value of the '<em><b>Element Set Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Element Set Name</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Element Set Name</em>' containment reference.
     * @see #setElementSetName(ElementSetNameType)
     * @see net.opengis.cat.csw20.Csw20Package#getQueryType_ElementSetName()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ElementSetName' namespace='##targetNamespace'"
     * @generated
     */
    ElementSetNameType getElementSetName();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.QueryType#getElementSetName <em>Element Set Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Element Set Name</em>' containment reference.
     * @see #getElementSetName()
     * @generated
     */
    void setElementSetName(ElementSetNameType value);

    /**
     * Returns the value of the '<em><b>Element Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Element Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Element Name</em>' attribute.
     * @see #setElementName(QName)
     * @see net.opengis.cat.csw20.Csw20Package#getQueryType_ElementName()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.QName"
     *        extendedMetaData="kind='element' name='ElementName' namespace='##targetNamespace'"
     * @generated
     */
    QName getElementName();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.QueryType#getElementName <em>Element Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Element Name</em>' attribute.
     * @see #getElementName()
     * @generated
     */
    void setElementName(QName value);

    /**
     * Returns the value of the '<em><b>Constraint</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Constraint</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Constraint</em>' containment reference.
     * @see #setConstraint(QueryConstraintType)
     * @see net.opengis.cat.csw20.Csw20Package#getQueryType_Constraint()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Constraint' namespace='##targetNamespace'"
     * @generated
     */
    QueryConstraintType getConstraint();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.QueryType#getConstraint <em>Constraint</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Constraint</em>' containment reference.
     * @see #getConstraint()
     * @generated
     */
    void setConstraint(QueryConstraintType value);

    /**
     * Returns the value of the '<em><b>Sort By</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sort By</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sort By</em>' containment reference.
     * @see #setSortBy(SortBy)
     * @see net.opengis.cat.csw20.Csw20Package#getQueryType_SortBy()
     * @model type="net.opengis.cat.csw20.SortBy" containment="true"
     *        extendedMetaData="kind='element' name='SortBy' namespace='http://www.opengis.net/ogc'"
     * @generated
     */
    SortBy getSortBy();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.QueryType#getSortBy <em>Sort By</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sort By</em>' containment reference.
     * @see #getSortBy()
     * @generated
     */
    void setSortBy(SortBy value);

    /**
     * Returns the value of the '<em><b>Type Names</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type Names</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type Names</em>' attribute.
     * @see #setTypeNames(List)
     * @see net.opengis.cat.csw20.Csw20Package#getQueryType_TypeNames()
     * @model dataType="net.opengis.cat.csw20.TypeNameListType_1" required="true"
     *        extendedMetaData="kind='attribute' name='typeNames'"
     * @generated
     */
    List<QName> getTypeNames();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.QueryType#getTypeNames <em>Type Names</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type Names</em>' attribute.
     * @see #getTypeNames()
     * @generated
     */
    void setTypeNames(List<QName> value);

} // QueryType
