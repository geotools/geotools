/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Triangulated Surface Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A triangulated surface is a polyhedral 
 *    surface that is composed only of triangles. There is no
 *    restriction on how the triangulation is derived.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TriangulatedSurfaceType#getTrianglePatches <em>Triangle Patches</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTriangulatedSurfaceType()
 * @model extendedMetaData="name='TriangulatedSurfaceType' kind='elementOnly'"
 * @generated
 */
public interface TriangulatedSurfaceType extends SurfaceType {
    /**
     * Returns the value of the '<em><b>Triangle Patches</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This property encapsulates the patches of 
     *       the triangulated surface.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Triangle Patches</em>' containment reference.
     * @see #setTrianglePatches(TrianglePatchArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getTriangulatedSurfaceType_TrianglePatches()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='trianglePatches' namespace='##targetNamespace'"
     * @generated
     */
    TrianglePatchArrayPropertyType getTrianglePatches();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TriangulatedSurfaceType#getTrianglePatches <em>Triangle Patches</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Triangle Patches</em>' containment reference.
     * @see #getTrianglePatches()
     * @generated
     */
    void setTrianglePatches(TrianglePatchArrayPropertyType value);

} // TriangulatedSurfaceType
