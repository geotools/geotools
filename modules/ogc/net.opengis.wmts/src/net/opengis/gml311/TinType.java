/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tin Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A tin is a triangulated surface that uses
 *    the Delauny algorithm or a similar algorithm complemented with
 *    consideration of breaklines, stoplines, and maximum length of 
 *    triangle sides. These networks satisfy the Delauny's criterion
 *    away from the modifications: Fore each triangle in the 
 *    network, the circle passing through its vertices does not
 *    contain, in its interior, the vertex of any other triangle.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.TinType#getStopLines <em>Stop Lines</em>}</li>
 *   <li>{@link net.opengis.gml311.TinType#getBreakLines <em>Break Lines</em>}</li>
 *   <li>{@link net.opengis.gml311.TinType#getMaxLength <em>Max Length</em>}</li>
 *   <li>{@link net.opengis.gml311.TinType#getControlPoint <em>Control Point</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getTinType()
 * @model extendedMetaData="name='TinType' kind='elementOnly'"
 * @generated
 */
public interface TinType extends TriangulatedSurfaceType {
    /**
     * Returns the value of the '<em><b>Stop Lines</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.LineStringSegmentArrayPropertyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Stoplines are lines where the local
     *        continuity or regularity of the surface is questionable.
     *        In the area of these pathologies, triangles intersecting
     *        a stopline shall be removed from the tin surface, leaving
     *        holes in the surface. If coincidence occurs on surface
     *        boundary triangles, the result shall be a change of the 
     *        surface boundary. Stoplines contains all these
     *        pathological segments as a set of line strings.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Stop Lines</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getTinType_StopLines()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='stopLines' namespace='##targetNamespace'"
     * @generated
     */
    EList<LineStringSegmentArrayPropertyType> getStopLines();

    /**
     * Returns the value of the '<em><b>Break Lines</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.LineStringSegmentArrayPropertyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Breaklines are lines of a critical
     *        nature to the shape of the surface, representing local
     *        ridges, or depressions (such as drainage lines) in the
     *        surface. As such their constituent segments must be
     *        included in the tin eve if doing so
     *        violates the Delauny criterion. Break lines contains these
     *        critical segments as a set of line strings.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Break Lines</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getTinType_BreakLines()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='breakLines' namespace='##targetNamespace'"
     * @generated
     */
    EList<LineStringSegmentArrayPropertyType> getBreakLines();

    /**
     * Returns the value of the '<em><b>Max Length</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Areas of the surface where data is not 
     *        sufficiently dense to assure reasonable calculation shall be    
     *        removed by adding a retention criterion for triangles based 
     *        on the length of their sides. For many triangle sides  
     *        exceeding maximum length, the adjacent triangles to that 
     *        triangle side shall be removed from the surface.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Max Length</em>' containment reference.
     * @see #setMaxLength(LengthType)
     * @see net.opengis.gml311.Gml311Package#getTinType_MaxLength()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='maxLength' namespace='##targetNamespace'"
     * @generated
     */
    LengthType getMaxLength();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TinType#getMaxLength <em>Max Length</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Max Length</em>' containment reference.
     * @see #getMaxLength()
     * @generated
     */
    void setMaxLength(LengthType value);

    /**
     * Returns the value of the '<em><b>Control Point</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The corners of the triangles in the TIN 
     *   are often referred to as pots. ControlPoint shall contain a 
     *   set of the GM_Position used as posts for this TIN. Since each  
     *   TIN contains triangles, there must be at least 3 posts. The 
     *        order in which these points are given does not affect the 
     *        surface that is represented. Application schemas may add 
     *        information based on ordering of control points to facilitate 
     *        the reconstruction of the TIN from the control points.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Control Point</em>' containment reference.
     * @see #setControlPoint(ControlPointType)
     * @see net.opengis.gml311.Gml311Package#getTinType_ControlPoint()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='controlPoint' namespace='##targetNamespace'"
     * @generated
     */
    ControlPointType getControlPoint();

    /**
     * Sets the value of the '{@link net.opengis.gml311.TinType#getControlPoint <em>Control Point</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Control Point</em>' containment reference.
     * @see #getControlPoint()
     * @generated
     */
    void setControlPoint(ControlPointType value);

} // TinType
