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
 * A representation of the model object '<em><b>Spatial Capabilities Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.SpatialCapabilitiesType#getGeometryOperands <em>Geometry Operands</em>}</li>
 *   <li>{@link net.opengis.fes20.SpatialCapabilitiesType#getSpatialOperators <em>Spatial Operators</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getSpatialCapabilitiesType()
 * @model extendedMetaData="name='Spatial_CapabilitiesType' kind='elementOnly'"
 * @generated
 */
public interface SpatialCapabilitiesType extends EObject {
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
     * @see net.opengis.fes20.Fes20Package#getSpatialCapabilitiesType_GeometryOperands()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='GeometryOperands' namespace='##targetNamespace'"
     * @generated
     */
    GeometryOperandsType getGeometryOperands();

    /**
     * Sets the value of the '{@link net.opengis.fes20.SpatialCapabilitiesType#getGeometryOperands <em>Geometry Operands</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geometry Operands</em>' containment reference.
     * @see #getGeometryOperands()
     * @generated
     */
    void setGeometryOperands(GeometryOperandsType value);

    /**
     * Returns the value of the '<em><b>Spatial Operators</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Spatial Operators</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Spatial Operators</em>' containment reference.
     * @see #setSpatialOperators(SpatialOperatorsType)
     * @see net.opengis.fes20.Fes20Package#getSpatialCapabilitiesType_SpatialOperators()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='SpatialOperators' namespace='##targetNamespace'"
     * @generated
     */
    SpatialOperatorsType getSpatialOperators();

    /**
     * Sets the value of the '{@link net.opengis.fes20.SpatialCapabilitiesType#getSpatialOperators <em>Spatial Operators</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Spatial Operators</em>' containment reference.
     * @see #getSpatialOperators()
     * @generated
     */
    void setSpatialOperators(SpatialOperatorsType value);

} // SpatialCapabilitiesType
