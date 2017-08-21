/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Edge Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * There is precisely one positively directed and one negatively directed node in the boundary of every edge. The negatively and positively directed nodes correspond to the start and end nodes respectively. The optional coboundary of an edge is a circular sequence of directed faces which are incident on this edge in document order. Faces which use a particular boundary edge in its positive orientation appear with positive orientation on the coboundary of the same edge. In the 2D case, the orientation of the face on the left of the edge is "+"; the orientation of the face on the right on its right is "-". An edge may optionally be realised by a 1-dimensional (curve) geometric primitive.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.EdgeType#getDirectedNode <em>Directed Node</em>}</li>
 *   <li>{@link net.opengis.gml311.EdgeType#getDirectedFace <em>Directed Face</em>}</li>
 *   <li>{@link net.opengis.gml311.EdgeType#getCurveProperty <em>Curve Property</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getEdgeType()
 * @model extendedMetaData="name='EdgeType' kind='elementOnly'"
 * @generated
 */
public interface EdgeType extends AbstractTopoPrimitiveType {
    /**
     * Returns the value of the '<em><b>Directed Node</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.DirectedNodePropertyType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Directed Node</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Directed Node</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getEdgeType_DirectedNode()
     * @model containment="true" lower="2" upper="2"
     *        extendedMetaData="kind='element' name='directedNode' namespace='##targetNamespace'"
     * @generated
     */
    EList<DirectedNodePropertyType> getDirectedNode();

    /**
     * Returns the value of the '<em><b>Directed Face</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.DirectedFacePropertyType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Directed Face</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Directed Face</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getEdgeType_DirectedFace()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='directedFace' namespace='##targetNamespace'"
     * @generated
     */
    EList<DirectedFacePropertyType> getDirectedFace();

    /**
     * Returns the value of the '<em><b>Curve Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a curve via the XLink-attributes or contains the curve element. curveProperty is the 
     * 			predefined property which can be used by GML Application Schemas whenever a GML Feature has a property with a value that is 
     * 			substitutable for _Curve.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Curve Property</em>' containment reference.
     * @see #setCurveProperty(CurvePropertyType)
     * @see net.opengis.gml311.Gml311Package#getEdgeType_CurveProperty()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='curveProperty' namespace='##targetNamespace'"
     * @generated
     */
    CurvePropertyType getCurveProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.EdgeType#getCurveProperty <em>Curve Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Curve Property</em>' containment reference.
     * @see #getCurveProperty()
     * @generated
     */
    void setCurveProperty(CurvePropertyType value);

} // EdgeType
