/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Topo Curve Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The end Node of each directedEdge of a TopoCurveType
 * is the start Node of the next directedEdge of the TopoCurveType in document order.  The TopoCurve type and element represent a homogeneous topological expression, a list of directed edges, which if realised are isomorphic to a geometric curve primitive. The intended use of TopoCurve is to appear within a line feature instance to express the structural and geometric relationships of this line to other features via the shared edge definitions.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TopoCurveType#getDirectedEdge <em>Directed Edge</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTopoCurveType()
 * @model extendedMetaData="name='TopoCurveType' kind='elementOnly'"
 * @generated
 */
public interface TopoCurveType extends AbstractTopologyType {
    /**
     * Returns the value of the '<em><b>Directed Edge</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.DirectedEdgePropertyType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Directed Edge</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Directed Edge</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getTopoCurveType_DirectedEdge()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='directedEdge' namespace='##targetNamespace'"
     * @generated
     */
    EList<DirectedEdgePropertyType> getDirectedEdge();

} // TopoCurveType
