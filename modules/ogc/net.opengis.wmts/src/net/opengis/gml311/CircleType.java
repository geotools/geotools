/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Circle Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A Circle is an arc whose ends coincide to form a simple closed loop. The "start" and "end" bearing are equal and shall be the bearing for the first controlPoint listed. The three control points must be distinct non-co-linear points for the Circle to be unambiguously defined. The arc is simply extended past the third control point until the first control point is encountered.
 * <!-- end-model-doc -->
 *
 *
 * @see net.opengis.gml311.Gml311Package#getCircleType()
 * @model extendedMetaData="name='CircleType' kind='elementOnly'"
 * @generated
 */
public interface CircleType extends ArcType {
} // CircleType
