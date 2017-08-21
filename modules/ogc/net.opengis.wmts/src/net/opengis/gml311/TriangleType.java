/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Triangle Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Represents a triangle as a surface with an outer boundary consisting of a linear ring. Note that this is a polygon (subtype) with no inner boundaries. The number of points in the linear ring must be four.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TriangleType#getExteriorGroup <em>Exterior Group</em>}</li>
 *   <li>{@link net.opengis.gml311.TriangleType#getExterior <em>Exterior</em>}</li>
 *   <li>{@link net.opengis.gml311.TriangleType#getInterpolation <em>Interpolation</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTriangleType()
 * @model extendedMetaData="name='TriangleType' kind='elementOnly'"
 * @generated
 */
public interface TriangleType extends AbstractSurfacePatchType {
    /**
     * Returns the value of the '<em><b>Exterior Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Constraint: The Ring shall be a LinearRing and must form a triangle, the first and the last position must be co-incident.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Exterior Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getTriangleType_ExteriorGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="false"
     *        extendedMetaData="kind='group' name='exterior:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getExteriorGroup();

    /**
     * Returns the value of the '<em><b>Exterior</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Constraint: The Ring shall be a LinearRing and must form a triangle, the first and the last position must be co-incident.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Exterior</em>' containment reference.
     * @see #setExterior(AbstractRingPropertyType)
     * @see net.opengis.gml311.Gml311Package#getTriangleType_Exterior()
     * @model containment="true" required="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='exterior' namespace='##targetNamespace' group='exterior:group'"
     * @generated
     */
    AbstractRingPropertyType getExterior();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TriangleType#getExterior <em>Exterior</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Exterior</em>' containment reference.
     * @see #getExterior()
     * @generated
     */
    void setExterior(AbstractRingPropertyType value);

    /**
     * Returns the value of the '<em><b>Interpolation</b></em>' attribute.
     * The default value is <code>"planar"</code>.
     * The literals are from the enumeration {@link net.opengis.gml311.SurfaceInterpolationType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The attribute "interpolation" specifies the interpolation mechanism used for this surface patch. Currently only planar surface patches are defined in GML 3, the attribute is fixed to "planar", i.e. the interpolation method shall return points on a single plane. The boundary of the patch shall be contained within that plane.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Interpolation</em>' attribute.
     * @see net.opengis.gml311.SurfaceInterpolationType
     * @see #isSetInterpolation()
     * @see #unsetInterpolation()
     * @see #setInterpolation(SurfaceInterpolationType)
     * @see net.opengis.gml311.Gml311Package#getTriangleType_Interpolation()
     * @model default="planar" unsettable="true"
     *        extendedMetaData="kind='attribute' name='interpolation'"
     * @generated
     */
    SurfaceInterpolationType getInterpolation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TriangleType#getInterpolation <em>Interpolation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Interpolation</em>' attribute.
     * @see net.opengis.gml311.SurfaceInterpolationType
     * @see #isSetInterpolation()
     * @see #unsetInterpolation()
     * @see #getInterpolation()
     * @generated
     */
    void setInterpolation(SurfaceInterpolationType value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.TriangleType#getInterpolation <em>Interpolation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetInterpolation()
     * @see #getInterpolation()
     * @see #setInterpolation(SurfaceInterpolationType)
     * @generated
     */
    void unsetInterpolation();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.TriangleType#getInterpolation <em>Interpolation</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Interpolation</em>' attribute is set.
     * @see #unsetInterpolation()
     * @see #getInterpolation()
     * @see #setInterpolation(SurfaceInterpolationType)
     * @generated
     */
    boolean isSetInterpolation();

} // TriangleType
