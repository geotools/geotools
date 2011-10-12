/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import javax.xml.namespace.QName;

import org.opengis.filter.Filter;

import net.opengis.fes20.FilterType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Delete Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.DeleteType#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfs20.DeleteType#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getDeleteType()
 * @model extendedMetaData="name='DeleteType' kind='elementOnly'"
 * @generated
 */
public interface DeleteType extends AbstractTransactionActionType {
    /**
     * Returns the value of the '<em><b>Filter</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Filter</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Filter</em>' containment reference.
     * @see #setFilter(FilterType)
     * @see net.opengis.wfs20.Wfs20Package#getDeleteType_Filter()
     * @model 
     */
    Filter getFilter();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DeleteType#getFilter <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Filter</em>' attribute.
     * @see #getFilter()
     * @generated
     */
    void setFilter(Filter value);

    /**
     * Returns the value of the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type Name</em>' attribute.
     * @see #setTypeName(QName)
     * @see net.opengis.wfs20.Wfs20Package#getDeleteType_TypeName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.QName" required="true"
     *        extendedMetaData="kind='attribute' name='typeName'"
     * @generated
     */
    QName getTypeName();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.DeleteType#getTypeName <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type Name</em>' attribute.
     * @see #getTypeName()
     * @generated
     */
    void setTypeName(QName value);

} // DeleteType
