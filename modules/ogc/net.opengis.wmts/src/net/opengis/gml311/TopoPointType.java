/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Topo Point Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The intended use of TopoPoint is to appear within a point feature to express the structural and possibly geometric relationships of this point to other features via shared node definitions. Note the orientation assigned to the directedNode has no meaning in this context. It is preserved for symmetry with the types and elements which follow.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TopoPointType#getDirectedNode <em>Directed Node</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTopoPointType()
 * @model extendedMetaData="name='TopoPointType' kind='elementOnly'"
 * @generated
 */
public interface TopoPointType extends AbstractTopologyType {
    /**
     * Returns the value of the '<em><b>Directed Node</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Directed Node</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Directed Node</em>' containment reference.
     * @see #setDirectedNode(DirectedNodePropertyType)
     * @see net.opengis.gml311.Gml311Package#getTopoPointType_DirectedNode()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='directedNode' namespace='##targetNamespace'"
     * @generated
     */
    DirectedNodePropertyType getDirectedNode();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TopoPointType#getDirectedNode <em>Directed Node</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Directed Node</em>' containment reference.
     * @see #getDirectedNode()
     * @generated
     */
    void setDirectedNode(DirectedNodePropertyType value);

} // TopoPointType
