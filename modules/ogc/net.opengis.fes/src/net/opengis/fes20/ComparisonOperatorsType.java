/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;
import org.opengis.filter.capability.ComparisonOperators;
import org.opengis.filter.capability.Operator;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Comparison Operators Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.ComparisonOperatorsType#getGroup <em>Group</em>}</li>
 *   <li>{@link net.opengis.fes20.ComparisonOperatorsType#getComparisonOperator <em>Comparison Operator</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getComparisonOperatorsType()
 * @model extendedMetaData="name='ComparisonOperatorsType' kind='elementOnly'"
 * @generated
 */
public interface ComparisonOperatorsType extends EObject, ComparisonOperators {
    /**
     * Returns the value of the '<em><b>Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Group</em>' attribute list.
     * @see net.opengis.fes20.Fes20Package#getComparisonOperatorsType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:0'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Comparison Operator</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.ComparisonOperatorType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Comparison Operator</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Comparison Operator</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getComparisonOperatorsType_ComparisonOperator()
     * @model containment="true" required="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='ComparisonOperator' namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<Operator> getOperators();

} // ComparisonOperatorsType
