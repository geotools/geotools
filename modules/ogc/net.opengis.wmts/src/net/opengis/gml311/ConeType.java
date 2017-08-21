/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cone Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A cone is a gridded surface given as a
 *    family of conic sections whose control points vary linearly.
 *    NOTE! A 5-point ellipse with all defining positions identical
 *    is a point. Thus, a truncated elliptical cone can be given as a
 *    2x5 set of control points
 *    ((P1, P1, P1, P1, P1), (P2, P3, P4, P5, P6)). P1 is the apex 
 *    of the cone. P2, P3,P4, P5 and P6 are any five distinct points
 *    around the base ellipse of the cone. If the horizontal curves
 *    are circles as opposed to ellipses, the a circular cone can
 *    be constructed using ((P1, P1, P1),(P2, P3, P4)). The apex most     
 *    not coinside with the other plane.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.ConeType#getHorizontalCurveType <em>Horizontal Curve Type</em>}</li>
 *   <li>{@link net.opengis.gml311.ConeType#getVerticalCurveType <em>Vertical Curve Type</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getConeType()
 * @model extendedMetaData="name='ConeType' kind='elementOnly'"
 * @generated
 */
public interface ConeType extends AbstractGriddedSurfaceType {
    /**
     * Returns the value of the '<em><b>Horizontal Curve Type</b></em>' attribute.
     * The default value is <code>"circularArc3Points"</code>.
     * The literals are from the enumeration {@link net.opengis.gml311.CurveInterpolationType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Horizontal Curve Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Horizontal Curve Type</em>' attribute.
     * @see net.opengis.gml311.CurveInterpolationType
     * @see #isSetHorizontalCurveType()
     * @see #unsetHorizontalCurveType()
     * @see #setHorizontalCurveType(CurveInterpolationType)
     * @see net.opengis.gml311.Gml311Package#getConeType_HorizontalCurveType()
     * @model default="circularArc3Points" unsettable="true"
     *        extendedMetaData="kind='attribute' name='horizontalCurveType'"
     * @generated
     */
    CurveInterpolationType getHorizontalCurveType();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ConeType#getHorizontalCurveType <em>Horizontal Curve Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Horizontal Curve Type</em>' attribute.
     * @see net.opengis.gml311.CurveInterpolationType
     * @see #isSetHorizontalCurveType()
     * @see #unsetHorizontalCurveType()
     * @see #getHorizontalCurveType()
     * @generated
     */
    void setHorizontalCurveType(CurveInterpolationType value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.ConeType#getHorizontalCurveType <em>Horizontal Curve Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetHorizontalCurveType()
     * @see #getHorizontalCurveType()
     * @see #setHorizontalCurveType(CurveInterpolationType)
     * @generated
     */
    void unsetHorizontalCurveType();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.ConeType#getHorizontalCurveType <em>Horizontal Curve Type</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Horizontal Curve Type</em>' attribute is set.
     * @see #unsetHorizontalCurveType()
     * @see #getHorizontalCurveType()
     * @see #setHorizontalCurveType(CurveInterpolationType)
     * @generated
     */
    boolean isSetHorizontalCurveType();

    /**
     * Returns the value of the '<em><b>Vertical Curve Type</b></em>' attribute.
     * The default value is <code>"linear"</code>.
     * The literals are from the enumeration {@link net.opengis.gml311.CurveInterpolationType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Vertical Curve Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Vertical Curve Type</em>' attribute.
     * @see net.opengis.gml311.CurveInterpolationType
     * @see #isSetVerticalCurveType()
     * @see #unsetVerticalCurveType()
     * @see #setVerticalCurveType(CurveInterpolationType)
     * @see net.opengis.gml311.Gml311Package#getConeType_VerticalCurveType()
     * @model default="linear" unsettable="true"
     *        extendedMetaData="kind='attribute' name='verticalCurveType'"
     * @generated
     */
    CurveInterpolationType getVerticalCurveType();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ConeType#getVerticalCurveType <em>Vertical Curve Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vertical Curve Type</em>' attribute.
     * @see net.opengis.gml311.CurveInterpolationType
     * @see #isSetVerticalCurveType()
     * @see #unsetVerticalCurveType()
     * @see #getVerticalCurveType()
     * @generated
     */
    void setVerticalCurveType(CurveInterpolationType value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.ConeType#getVerticalCurveType <em>Vertical Curve Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetVerticalCurveType()
     * @see #getVerticalCurveType()
     * @see #setVerticalCurveType(CurveInterpolationType)
     * @generated
     */
    void unsetVerticalCurveType();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.ConeType#getVerticalCurveType <em>Vertical Curve Type</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Vertical Curve Type</em>' attribute is set.
     * @see #unsetVerticalCurveType()
     * @see #getVerticalCurveType()
     * @see #setVerticalCurveType(CurveInterpolationType)
     * @generated
     */
    boolean isSetVerticalCurveType();

} // ConeType
