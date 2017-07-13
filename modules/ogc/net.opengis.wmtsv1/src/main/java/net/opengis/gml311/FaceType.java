/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Face Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * . The topological boundary of a face consists of a set of directed edges. Note that all edges associated with a Face, including dangling and interior edges, appear in the boundary.  Dangling and interior edges are each referenced by pairs of directedEdges with opposing orientations.  The optional coboundary of a face is a pair of directed solids which are bounded by this face. If present, there is precisely one positively directed and one negatively directed solid in the coboundary of every face. The positively directed solid corresponds to the solid which lies in the direction of the positively directed normal to the face in any geometric realisation.  A face may optionally be realised by a 2-dimensional (surface) geometric primitive.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.FaceType#getDirectedEdge <em>Directed Edge</em>}</li>
 *   <li>{@link net.opengis.gml311.FaceType#getDirectedTopoSolid <em>Directed Topo Solid</em>}</li>
 *   <li>{@link net.opengis.gml311.FaceType#getSurfaceProperty <em>Surface Property</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getFaceType()
 * @model extendedMetaData="name='FaceType' kind='elementOnly'"
 * @generated
 */
public interface FaceType extends AbstractTopoPrimitiveType {
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
     * @see net.opengis.gml311.Gml311Package#getFaceType_DirectedEdge()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='directedEdge' namespace='##targetNamespace'"
     * @generated
     */
    EList<DirectedEdgePropertyType> getDirectedEdge();

    /**
     * Returns the value of the '<em><b>Directed Topo Solid</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.DirectedTopoSolidPropertyType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Directed Topo Solid</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Directed Topo Solid</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getFaceType_DirectedTopoSolid()
     * @model containment="true" upper="2"
     *        extendedMetaData="kind='element' name='directedTopoSolid' namespace='##targetNamespace'"
     * @generated
     */
    EList<DirectedTopoSolidPropertyType> getDirectedTopoSolid();

    /**
     * Returns the value of the '<em><b>Surface Property</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property element either references a surface via the XLink-attributes or contains the surface element. surfaceProperty is the predefined property which can be used by GML Application Schemas whenever a GML Feature has a property with a value that is substitutable for _Surface.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Surface Property</em>' containment reference.
     * @see #setSurfaceProperty(SurfacePropertyType)
     * @see net.opengis.gml311.Gml311Package#getFaceType_SurfaceProperty()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='surfaceProperty' namespace='##targetNamespace'"
     * @generated
     */
    SurfacePropertyType getSurfaceProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.FaceType#getSurfaceProperty <em>Surface Property</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Surface Property</em>' containment reference.
     * @see #getSurfaceProperty()
     * @generated
     */
    void setSurfaceProperty(SurfacePropertyType value);

} // FaceType
