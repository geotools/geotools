/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sort By Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.SortByType#getSortProperty <em>Sort Property</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getSortByType()
 * @model extendedMetaData="name='SortByType' kind='elementOnly'"
 * @generated
 */
public interface SortByType extends EObject {
    /**
     * Returns the value of the '<em><b>Sort Property</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.SortPropertyType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sort Property</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Sort Property</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getSortByType_SortProperty()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='SortProperty' namespace='##targetNamespace'"
     * @generated
     */
    EList<SortPropertyType> getSortProperty();

} // SortByType
