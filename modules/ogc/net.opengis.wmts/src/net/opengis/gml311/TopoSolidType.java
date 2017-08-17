/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Topo Solid Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The topological boundary of a TopoSolid consists of a set of directed faces. Note that all faces associated with the TopoSolid, including dangling faces, appear in the boundary. The coboundary of a TopoSolid is empty and hence requires no representation.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TopoSolidType#getDirectedFace <em>Directed Face</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTopoSolidType()
 * @model extendedMetaData="name='TopoSolidType' kind='elementOnly'"
 * @generated
 */
public interface TopoSolidType extends AbstractTopoPrimitiveType {
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
     * @see net.opengis.gml311.Gml311Package#getTopoSolidType_DirectedFace()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='directedFace' namespace='##targetNamespace'"
     * @generated
     */
    EList<DirectedFacePropertyType> getDirectedFace();

} // TopoSolidType
