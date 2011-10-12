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
 * A representation of the model object '<em><b>Comparison Operator Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.ComparisonOperatorType#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getComparisonOperatorType()
 * @model extendedMetaData="name='ComparisonOperatorType' kind='empty'"
 * @generated
 */
public interface ComparisonOperatorType extends EObject {
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(Object)
     * @see net.opengis.fes20.Fes20Package#getComparisonOperatorType_Name()
     * @model dataType="net.opengis.fes20.ComparisonOperatorNameType" required="true"
     *        extendedMetaData="kind='attribute' name='name'"
     * @generated
     */
    Object getName();

    /**
     * Sets the value of the '{@link net.opengis.fes20.ComparisonOperatorType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(Object value);

} // ComparisonOperatorType
