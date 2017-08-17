/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sphere Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A sphere is a gridded surface given as a
 *    family of circles whose positions vary linearly along the
 *    axis of the sphere, and whise radius varies in proportions to
 *    the cosine function of the central angle. The horizontal 
 *    circles resemble lines of constant latitude, and the vertical
 *    arcs resemble lines of constant longitude. 
 *    NOTE! If the control points are sorted in terms of increasing
 *    longitude, and increasing latitude, the upNormal of a sphere
 *    is the outward normal.
 *    EXAMPLE If we take a gridded set of latitudes and longitudes
 *    in degrees,(u,v) such as
 * 
 * 	(-90,-180)  (-90,-90)  (-90,0)  (-90,  90) (-90, 180) 
 * 	(-45,-180)  (-45,-90)  (-45,0)  (-45,  90) (-45, 180) 
 * 	(  0,-180)  (  0,-90)  (  0,0)  (  0,  90) (  0, 180)
 * 	( 45,-180)  ( 45,-90)  ( 45,0)  ( 45, -90) ( 45, 180)
 * 	( 90,-180)  ( 90,-90)  ( 90,0)  ( 90, -90) ( 90, 180)
 *    
 *    And map these points to 3D using the usual equations (where R
 *    is the radius of the required sphere).
 * 
 *     z = R sin u
 *     x = (R cos u)(sin v)
 *     y = (R cos u)(cos v)
 * 
 *    We have a sphere of Radius R, centred at (0,0), as a gridded
 *    surface. Notice that the entire first row and the entire last
 *    row of the control points map to a single point in each 3D
 *    Euclidean space, North and South poles respectively, and that
 *    each horizontal curve closes back on itself forming a 
 *    geometric cycle. This gives us a metrically bounded (of finite
 *    size), topologically unbounded (not having a boundary, a
 *    cycle) surface.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.SphereType#getHorizontalCurveType <em>Horizontal Curve Type</em>}</li>
 *   <li>{@link net.opengis.gml311.SphereType#getVerticalCurveType <em>Vertical Curve Type</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getSphereType()
 * @model extendedMetaData="name='SphereType' kind='elementOnly'"
 * @generated
 */
public interface SphereType extends AbstractGriddedSurfaceType {
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
     * @see net.opengis.gml311.Gml311Package#getSphereType_HorizontalCurveType()
     * @model default="circularArc3Points" unsettable="true"
     *        extendedMetaData="kind='attribute' name='horizontalCurveType'"
     * @generated
     */
    CurveInterpolationType getHorizontalCurveType();

    /**
     * Sets the value of the '{@link net.opengis.gml311.SphereType#getHorizontalCurveType <em>Horizontal Curve Type</em>}' attribute.
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
     * Unsets the value of the '{@link net.opengis.gml311.SphereType#getHorizontalCurveType <em>Horizontal Curve Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetHorizontalCurveType()
     * @see #getHorizontalCurveType()
     * @see #setHorizontalCurveType(CurveInterpolationType)
     * @generated
     */
    void unsetHorizontalCurveType();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.SphereType#getHorizontalCurveType <em>Horizontal Curve Type</em>}' attribute is set.
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
     * The default value is <code>"circularArc3Points"</code>.
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
     * @see net.opengis.gml311.Gml311Package#getSphereType_VerticalCurveType()
     * @model default="circularArc3Points" unsettable="true"
     *        extendedMetaData="kind='attribute' name='verticalCurveType'"
     * @generated
     */
    CurveInterpolationType getVerticalCurveType();

    /**
     * Sets the value of the '{@link net.opengis.gml311.SphereType#getVerticalCurveType <em>Vertical Curve Type</em>}' attribute.
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
     * Unsets the value of the '{@link net.opengis.gml311.SphereType#getVerticalCurveType <em>Vertical Curve Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetVerticalCurveType()
     * @see #getVerticalCurveType()
     * @see #setVerticalCurveType(CurveInterpolationType)
     * @generated
     */
    void unsetVerticalCurveType();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.SphereType#getVerticalCurveType <em>Vertical Curve Type</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Vertical Curve Type</em>' attribute is set.
     * @see #unsetVerticalCurveType()
     * @see #getVerticalCurveType()
     * @see #setVerticalCurveType(CurveInterpolationType)
     * @generated
     */
    boolean isSetVerticalCurveType();

} // SphereType
