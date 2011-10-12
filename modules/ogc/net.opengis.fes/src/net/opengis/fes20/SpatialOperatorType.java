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
 * A representation of the model object '<em><b>Spatial Operator Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.SpatialOperatorType#getGeometryOperands <em>Geometry Operands</em>}</li>
 *   <li>{@link net.opengis.fes20.SpatialOperatorType#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getSpatialOperatorType()
 * @model extendedMetaData="name='SpatialOperatorType' kind='elementOnly'"
 * @generated
 */
public interface SpatialOperatorType extends EObject {
    /**
     * Returns the value of the '<em><b>Geometry Operands</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Geometry Operands</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Geometry Operands</em>' containment reference.
     * @see #setGeometryOperands(GeometryOperandsType)
     * @see net.opengis.fes20.Fes20Package#getSpatialOperatorType_GeometryOperands()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='GeometryOperands' namespace='##targetNamespace'"
     * @generated
     */
    GeometryOperandsType getGeometryOperands();

    /**
     * Sets the value of the '{@link net.opengis.fes20.SpatialOperatorType#getGeometryOperands <em>Geometry Operands</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geometry Operands</em>' containment reference.
     * @see #getGeometryOperands()
     * @generated
     */
    void setGeometryOperands(GeometryOperandsType value);

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
     * @see net.opengis.fes20.Fes20Package#getSpatialOperatorType_Name()
     * @model dataType="net.opengis.fes20.SpatialOperatorNameType"
     *        extendedMetaData="kind='attribute' name='name'"
     * @generated
     */
    Object getName();

    /**
     * Sets the value of the '{@link net.opengis.fes20.SpatialOperatorType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(Object value);

} // SpatialOperatorType
