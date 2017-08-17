/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Polyhedral Surface Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A polyhedral surface is a surface composed
 *    of polygon surfaces connected along their common boundary 
 *    curves. This differs from the surface type only in the
 *    restriction on the types of surface patches acceptable.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.PolyhedralSurfaceType#getPolygonPatches <em>Polygon Patches</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getPolyhedralSurfaceType()
 * @model extendedMetaData="name='PolyhedralSurfaceType' kind='elementOnly'"
 * @generated
 */
public interface PolyhedralSurfaceType extends SurfaceType {
    /**
     * Returns the value of the '<em><b>Polygon Patches</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property encapsulates the patches of 
     *       the polyhedral surface.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Polygon Patches</em>' containment reference.
     * @see #setPolygonPatches(PolygonPatchArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getPolyhedralSurfaceType_PolygonPatches()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='polygonPatches' namespace='##targetNamespace'"
     * @generated
     */
    PolygonPatchArrayPropertyType getPolygonPatches();

    /**
     * Sets the value of the '{@link net.opengis.gml311.PolyhedralSurfaceType#getPolygonPatches <em>Polygon Patches</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Polygon Patches</em>' containment reference.
     * @see #getPolygonPatches()
     * @generated
     */
    void setPolygonPatches(PolygonPatchArrayPropertyType value);

} // PolyhedralSurfaceType
