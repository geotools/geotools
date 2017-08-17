/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ring Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A Ring is used to represent a single connected component of a surface boundary. It consists of a sequence of curves connected in a cycle (an object whose boundary is empty).
 * A Ring is structurally similar to a composite curve in that the endPoint of each curve in the sequence is the startPoint of the next curve in the Sequence. Since the sequence is circular, there is no exception to this rule. Each ring, like all boundaries, is a cycle and each ring is simple.
 * NOTE: Even though each Ring is simple, the boundary need not be simple. The easiest case of this is where one of the interior rings of a surface is tangent to its exterior ring.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.RingType#getCurveMember <em>Curve Member</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getRingType()
 * @model extendedMetaData="name='RingType' kind='elementOnly'"
 * @generated
 */
public interface RingType extends AbstractRingType {
    /**
     * Returns the value of the '<em><b>Curve Member</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.CurvePropertyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element references or contains one curve in the composite curve. The curves are contiguous, the collection of curves is ordered.
     * NOTE: This definition allows for a nested structure, i.e. a CompositeCurve may use, for example, another CompositeCurve as a curve member.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Curve Member</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getRingType_CurveMember()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='curveMember' namespace='##targetNamespace'"
     * @generated
     */
    EList<CurvePropertyType> getCurveMember();

} // RingType
