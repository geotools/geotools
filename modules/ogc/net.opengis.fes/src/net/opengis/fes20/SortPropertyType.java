/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sort Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.SortPropertyType#getValueReference <em>Value Reference</em>}</li>
 *   <li>{@link net.opengis.fes20.SortPropertyType#getSortOrder <em>Sort Order</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getSortPropertyType()
 * @model extendedMetaData="name='SortPropertyType' kind='elementOnly'"
 * @generated
 */
public interface SortPropertyType extends EObject {
    /**
     * Returns the value of the '<em><b>Value Reference</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value Reference</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value Reference</em>' attribute.
     * @see #setValueReference(String)
     * @see net.opengis.fes20.Fes20Package#getSortPropertyType_ValueReference()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='ValueReference' namespace='##targetNamespace'"
     * @generated
     */
    String getValueReference();

    /**
     * Sets the value of the '{@link net.opengis.fes20.SortPropertyType#getValueReference <em>Value Reference</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value Reference</em>' attribute.
     * @see #getValueReference()
     * @generated
     */
    void setValueReference(String value);

    /**
     * Returns the value of the '<em><b>Sort Order</b></em>' attribute.
     * The literals are from the enumeration {@link net.opengis.fes20.SortOrderType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sort Order</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sort Order</em>' attribute.
     * @see net.opengis.fes20.SortOrderType
     * @see #isSetSortOrder()
     * @see #unsetSortOrder()
     * @see #setSortOrder(SortOrderType)
     * @see net.opengis.fes20.Fes20Package#getSortPropertyType_SortOrder()
     * @model unsettable="true"
     *        extendedMetaData="kind='element' name='SortOrder' namespace='##targetNamespace'"
     * @generated
     */
    SortOrderType getSortOrder();

    /**
     * Sets the value of the '{@link net.opengis.fes20.SortPropertyType#getSortOrder <em>Sort Order</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Sort Order</em>' attribute.
     * @see net.opengis.fes20.SortOrderType
     * @see #isSetSortOrder()
     * @see #unsetSortOrder()
     * @see #getSortOrder()
     * @generated
     */
    void setSortOrder(SortOrderType value);

    /**
     * Unsets the value of the '{@link net.opengis.fes20.SortPropertyType#getSortOrder <em>Sort Order</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetSortOrder()
     * @see #getSortOrder()
     * @see #setSortOrder(SortOrderType)
     * @generated
     */
    void unsetSortOrder();

    /**
     * Returns whether the value of the '{@link net.opengis.fes20.SortPropertyType#getSortOrder <em>Sort Order</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Sort Order</em>' attribute is set.
     * @see #unsetSortOrder()
     * @see #getSortOrder()
     * @see #setSortOrder(SortOrderType)
     * @generated
     */
    boolean isSetSortOrder();

} // SortPropertyType
