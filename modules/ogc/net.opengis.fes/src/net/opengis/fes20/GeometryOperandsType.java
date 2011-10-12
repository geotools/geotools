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
 * A representation of the model object '<em><b>Geometry Operands Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.GeometryOperandsType#getGeometryOperand <em>Geometry Operand</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getGeometryOperandsType()
 * @model extendedMetaData="name='GeometryOperandsType' kind='elementOnly'"
 * @generated
 */
public interface GeometryOperandsType extends EObject {
    /**
     * Returns the value of the '<em><b>Geometry Operand</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.fes20.GeometryOperandType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Geometry Operand</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Geometry Operand</em>' containment reference list.
     * @see net.opengis.fes20.Fes20Package#getGeometryOperandsType_GeometryOperand()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='GeometryOperand' namespace='##targetNamespace'"
     * @generated
     */
    EList<GeometryOperandType> getGeometryOperand();

} // GeometryOperandsType
