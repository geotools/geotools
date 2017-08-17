/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Topo Volume Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The TopoVolume type and element represent a homogeneous topological expression, a set of directed TopoSolids, which if realised are isomorphic to a geometric solid primitive. The intended use of TopoVolume is to appear within a 3D solid feature instance to express the structural and geometric relationships of this solid to other features via the shared TopoSolid definitions.  . Note the orientation assigned to the directedSolid has no meaning in three dimensions. It is preserved for symmetry with the preceding types and elements.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TopoVolumeType#getDirectedTopoSolid <em>Directed Topo Solid</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTopoVolumeType()
 * @model extendedMetaData="name='TopoVolumeType' kind='elementOnly'"
 * @generated
 */
public interface TopoVolumeType extends AbstractTopologyType {
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
     * @see net.opengis.gml311.Gml311Package#getTopoVolumeType_DirectedTopoSolid()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='directedTopoSolid' namespace='##targetNamespace'"
     * @generated
     */
    EList<DirectedTopoSolidPropertyType> getDirectedTopoSolid();

} // TopoVolumeType
