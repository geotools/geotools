/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Polygon Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A Polygon is a special surface that is defined by a single surface patch. The boundary of this patch is coplanar and the polygon uses planar interpolation in its interior. It is backwards compatible with the Polygon of GML 2, GM_Polygon of ISO 19107 is implemented by PolygonPatch.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.gml.PolygonType#getExterior <em>Exterior</em>}</li>
 *   <li>{@link net.opengis.gml.PolygonType#getInterior <em>Interior</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.gml.GmlPackage#getPolygonType()
 * @model extendedMetaData="name='PolygonType' kind='elementOnly'"
 * @generated
 */
public interface PolygonType extends AbstractSurfaceType {
    /**
	 * Returns the value of the '<em><b>Exterior</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A boundary of a surface consists of a number of rings. In the normal 2D case, one of these rings is distinguished as being the exterior boundary. In a general manifold this is not always possible, in which case all boundaries shall be listed as interior boundaries, and the exterior will be empty.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Exterior</em>' containment reference.
	 * @see #setExterior(AbstractRingPropertyType)
	 * @see net.opengis.gml.GmlPackage#getPolygonType_Exterior()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='exterior' namespace='##targetNamespace'"
	 * @generated
	 */
    AbstractRingPropertyType getExterior();

    /**
	 * Sets the value of the '{@link net.opengis.gml.PolygonType#getExterior <em>Exterior</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Exterior</em>' containment reference.
	 * @see #getExterior()
	 * @generated
	 */
    void setExterior(AbstractRingPropertyType value);

    /**
	 * Returns the value of the '<em><b>Interior</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.gml.AbstractRingPropertyType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A boundary of a surface consists of a number of rings. The "interior" rings seperate the surface / surface patch from the area enclosed by the rings.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Interior</em>' containment reference list.
	 * @see net.opengis.gml.GmlPackage#getPolygonType_Interior()
	 * @model type="net.opengis.gml.AbstractRingPropertyType" containment="true"
	 *        extendedMetaData="kind='element' name='interior' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getInterior();

} // PolygonType
