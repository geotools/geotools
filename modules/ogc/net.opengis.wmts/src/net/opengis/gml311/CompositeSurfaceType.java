/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Composite Surface Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A CompositeSurface is defined by a set of orientable surfaces. A composite surface is geometry type with all the geometric properties of a (primitive) surface. Essentially, a composite surface is a collection of surfaces that join in pairs on common boundary curves and which, when considered as a whole, form a single surface.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.CompositeSurfaceType#getSurfaceMember <em>Surface Member</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getCompositeSurfaceType()
 * @model extendedMetaData="name='CompositeSurfaceType' kind='elementOnly'"
 * @generated
 */
public interface CompositeSurfaceType extends AbstractSurfaceType {
    /**
     * Returns the value of the '<em><b>Surface Member</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.SurfacePropertyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element references or contains one surface in the composite surface. The surfaces are contiguous.
     * NOTE: This definition allows for a nested structure, i.e. a CompositeSurface may use, for example, another CompositeSurface as a member.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Surface Member</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getCompositeSurfaceType_SurfaceMember()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='surfaceMember' namespace='##targetNamespace'"
     * @generated
     */
    EList<SurfacePropertyType> getSurfaceMember();

} // CompositeSurfaceType
