/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Surface Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A Surface is a 2-dimensional primitive and is composed of one or more surface patches. The surface patches are connected to one another.
 * 				The orientation of the surface is positive ("up"). The orientation of a surface chooses an "up" direction through the choice of the upward normal, which, if the surface is not a cycle, is the side of the surface from which the exterior boundary appears counterclockwise. Reversal of the surface orientation reverses the curve orientation of each boundary component, and interchanges the conceptual "up" and "down" direction of the surface. If the surface is the boundary of a solid, the "up" direction is usually outward. For closed surfaces, which have no boundary, the up direction is that of the surface patches, which must be consistent with one another. Its included surface patches describe the interior structure of the Surface.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.SurfaceType#getPatchesGroup <em>Patches Group</em>}</li>
 *   <li>{@link net.opengis.gml311.SurfaceType#getPatches <em>Patches</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getSurfaceType()
 * @model extendedMetaData="name='SurfaceType' kind='elementOnly'"
 * @generated
 */
public interface SurfaceType extends AbstractSurfaceType {
    /**
     * Returns the value of the '<em><b>Patches Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element encapsulates the patches of the surface.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Patches Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getSurfaceType_PatchesGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="false"
     *        extendedMetaData="kind='group' name='patches:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getPatchesGroup();

    /**
     * Returns the value of the '<em><b>Patches</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element encapsulates the patches of the surface.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Patches</em>' containment reference.
     * @see #setPatches(SurfacePatchArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getSurfaceType_Patches()
     * @model containment="true" required="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='patches' namespace='##targetNamespace' group='patches:group'"
     * @generated
     */
    SurfacePatchArrayPropertyType getPatches();

    /**
     * Sets the value of the '{@link net.opengis.gml311.SurfaceType#getPatches <em>Patches</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Patches</em>' containment reference.
     * @see #getPatches()
     * @generated
     */
    void setPatches(SurfacePatchArrayPropertyType value);

} // SurfaceType
