/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Polygon Patch Array Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This type defines a container for an array of 
 *    polygon patches.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.PolygonPatchArrayPropertyType#getPolygonPatch <em>Polygon Patch</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getPolygonPatchArrayPropertyType()
 * @model extendedMetaData="name='PolygonPatchArrayPropertyType' kind='elementOnly'"
 * @generated
 */
public interface PolygonPatchArrayPropertyType extends SurfacePatchArrayPropertyType {
    /**
     * Returns the value of the '<em><b>Polygon Patch</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.PolygonPatchType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Polygon Patch</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Polygon Patch</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getPolygonPatchArrayPropertyType_PolygonPatch()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='PolygonPatch' namespace='##targetNamespace'"
     * @generated
     */
    EList<PolygonPatchType> getPolygonPatch();

} // PolygonPatchArrayPropertyType
