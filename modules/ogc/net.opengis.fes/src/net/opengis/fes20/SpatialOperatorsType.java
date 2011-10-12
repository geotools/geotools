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
 * A representation of the model object '<em><b>Spatial Operators Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.SpatialOperatorsType#getSpatialOperator <em>Spatial Operator</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getSpatialOperatorsType()
 * @model extendedMetaData="name='SpatialOperatorsType' kind='elementOnly'"
 * @generated
 */
public interface SpatialOperatorsType extends EObject {
    /**
     * Returns the value of the '<em><b>Spatial Operator</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.SpatialOperatorType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Spatial Operator</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Spatial Operator</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getSpatialOperatorsType_SpatialOperator()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='SpatialOperator' namespace='##targetNamespace'"
     * @generated
     */
    EList<SpatialOperatorType> getSpatialOperator();

} // SpatialOperatorsType
