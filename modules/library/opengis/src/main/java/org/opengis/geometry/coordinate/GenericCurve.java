/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.coordinate;

import org.opengis.geometry.DirectPosition;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Common interface for {@linkplain org.opengis.geometry.primitive.Curve curve} and
 * {@linkplain org.opengis.geometry.primitive.CurveSegment curve segment}. {@code Curve}
 * and {@code CurveSegment} both represent sections of curvilinear
 * geometry, and therefore share a number of operation signatures.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/coordinate/GenericCurve.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier="GM_GenericCurve", specification=ISO_19107)
public interface GenericCurve {
    /**
     * Returns the direct position of the first point on the {@code GenericCurve}.
     * This differs from the boundary operator in {@link org.opengis.geometry.primitive.Primitive},
     * since it returns only the values of this point, not representative objects.
     *
     * @return The first point on the {@code GenericCurve}.
     *
     * @see #getStartParam
     * @see #getEndPoint
     */
    @UML(identifier="startPoint", obligation=MANDATORY, specification=ISO_19107)
    DirectPosition getStartPoint();

    /**
     * Returns the direct position of the last point on the {@code GenericCurve}.
     * This differs from the boundary operator in {@link org.opengis.geometry.primitive.Primitive},
     * since it returns only the values of this point, not representative objects.
     *
     * @return The last point on the {@code GenericCurve}.
     *
     * @see #getEndParam
     * @see #getStartPoint
     */
    @UML(identifier="endPoint", obligation=MANDATORY, specification=ISO_19107)
    DirectPosition getEndPoint();

    /**
     * Returns the tangent vector along this {@code GenericCurve} at the passed parameter
     * value. This vector approximates the derivative of the parameterization of the curve. The
     * tangent shall be a unit vector (have length 1.0), which is consistent with the
     * parameterization by arc length.
     *
     * @param s The parameter value along this curve.
     * @return The tangent unit vector.
     * @unitof Distance
     *
     * @see #getStartParam
     * @see #getEndParam
     */
    @UML(identifier="tangent", obligation=MANDATORY, specification=ISO_19107)
    double[] getTangent(double s);

    /**
     * Indicates the parameter for the {@linkplain #getStartPoint start point}.
     * The start parameter of a {@linkplain org.opengis.geometry.primitive.Curve curve} shall always be 0.
     * For {@linkplain org.opengis.geometry.primitive.CurveSegment curve segments} within a
     * {@linkplain org.opengis.geometry.primitive.Curve curve}, the start of the
     * {@linkplain org.opengis.geometry.primitive.CurveSegment curve segment} shall be equal to those of the
     * {@linkplain org.opengis.geometry.primitive.Curve curve} where this segment begins, so that the
     * start parameter of any segment (except the first) shall be equal to the end
     * parameter of the previous segment.
     *
     * @return The parameter for the {@linkplain #getStartPoint start point}.
     * @unitof Distance
     *
     * @see #getStartPoint
     * @see #getStartConstructiveParam
     * @see #getEndParam
     * @see #forParam
     */
    @UML(identifier="startParam", obligation=MANDATORY, specification=ISO_19107)
    double getStartParam();

    /**
     * Indicates the parameter for the {@linkplain #getEndPoint end point}.
     * The end parameter of a {@linkplain org.opengis.geometry.primitive.Curve curve} shall always be the arc
     * length of the curve. For {@linkplain org.opengis.geometry.primitive.CurveSegment curve segments} within a
     * {@linkplain org.opengis.geometry.primitive.Curve curve}, the end parameters of the
     * {@linkplain org.opengis.geometry.primitive.CurveSegment curve segment} shall be equal to those of the
     * {@linkplain org.opengis.geometry.primitive.Curve curve} where this segment ends, so that the
     * start parameter of any segment (except the first) shall be equal to the end
     * parameter of the previous segment.
     *
     * @return The parameter for the {@linkplain #getEndPoint end point}.
     * @unitof Distance
     *
     * @see #getEndPoint
     * @see #getEndConstructiveParam
     * @see #getStartParam
     * @see #forParam
     */
    @UML(identifier="endParam", obligation=MANDATORY, specification=ISO_19107)
    double getEndParam();

    /**
     * Indicates the parameter used in the constructive paramerization for the start point.
     * There is no assumption that the {@code startConstructiveParam} is less than the
     * {@code endConstructiveParam}, but the parameterization must be strictly monotonic
     * (strictly increasing, or strictly decreasing).
     *
     * <blockquote><font size=2>
     * <strong>NOTE:</strong> Constructive parameters are often chosen for convenience of
     * calculation, and seldom have any simple relation to arc distances, which are defined
     * as the default parameterization. Normally, geometric constructions will use constructive
     * parameters, as the programmer deems reasonable, and calculate arc length parameters when
     * queried.
     * </font></blockquote>
     *
     * @return The parameter used in the constructive paramerization for the start point.
     *
     * @see #getStartParam
     * @see #getEndConstructiveParam
     * @see #forConstructiveParam
     */
    @UML(identifier="startConstrParam", obligation=MANDATORY, specification=ISO_19107)
    double getStartConstructiveParam();

    /**
     * Indicates the parameter used in the constructive paramerization for the end point.
     * There is no assumption that the {@code startConstructiveParam} is less than the
     * {@code endConstructiveParam}, but the parameterization must be strictly monotonic
     * (strictly increasing, or strictly decreasing).
     *
     * <blockquote><font size=2>
     * <strong>NOTE:</strong> Constructive parameters are often chosen for convenience of
     * calculation, and seldom have any simple relation to arc distances, which are defined
     * as the default parameterization. Normally, geometric constructions will use constructive
     * parameters, as the programmer deems reasonable, and calculate arc length parameters when
     * queried.
     * </font></blockquote>
     *
     * @return The parameter used in the constructive paramerization for the end point.
     *
     * @see #getEndParam
     * @see #getStartConstructiveParam
     * @see #forConstructiveParam
     */
    @UML(identifier="endConstrParam", obligation=MANDATORY, specification=ISO_19107)
    double getEndConstructiveParam();

    /**
     * Returns the direct position for a constructive parameter. This method shall be
     * an alternate representation of the curve as the continuous image of a real number
     * interval without the restriction that the parameter represents the arc length of the curve,
     * nor restrictions between a {@linkplain org.opengis.geometry.primitive.Curve curve} and its component
     * {@linkplain org.opengis.geometry.primitive.CurveSegment curve segments}. The most common use of this
     * operation is to expose the constructive equations of the underlying curve, especially useful
     * when that curve is used to construct a parametric surface.
     *
     * @param cp The constructive parameter.
     * @return The direct position for the given constructive parameter.
     *
     * @see #getStartConstructiveParam
     * @see #getEndConstructiveParam
     * @see #forParam
     */
    @UML(identifier="constrParam", obligation=MANDATORY, specification=ISO_19107)
    DirectPosition forConstructiveParam(double cp);

    /**
     * Returns the direct position for a parameter. This method shall be the parameterized
     * representation of the curve as the continuous image of a real number interval. The
     * method returns the direct position on the {@code GenericCurve} at the distance
     * passed. The parameterization shall be by arc length, i.e. distance along the
     * {@code GenericCurve} measured from the start point and added to the start parameter.
     *
     * @param s The distance from the start point and added to the start parameter.
     * @return The direct position for the given parameter.
     *
     * @see #getStartParam
     * @see #getEndParam
     * @see #forConstructiveParam
     */
    @UML(identifier="param", obligation=MANDATORY, specification=ISO_19107)
    DirectPosition forParam(double s);

    /**
     * Returns the parameter for this {@code GenericCurve} at the passed direct position.
     * If the direct position is not on the curve, the nearest point on the curve shall be used.
     *
     * @param p The direct position on the curve.
     * @return The parameter closest to the given position.
     *
     * @see #getStartPoint
     * @see #getEndPoint
     * @see #forParam
     */
    @UML(identifier="paramForPoint", obligation=MANDATORY, specification=ISO_19107)
    ParamForPoint getParamForPoint(DirectPosition p);

    /**
     * Returns the length between two points.
     * The length of a piece of curvilinear geometry shall be a numeric measure of its
     * length in a coordinate reference system. Since length is an accumulation of distance, its
     * return value shall be in a unit of measure appropriate for measuring distances. This method
     * shall return the distance between the two points along the curve. The default values of the
     * two parameters shall be the start point and the end point, respectively. If either of the
     * points is not on the curve, then it shall be projected to the nearest {@linkplain DirectPosition
     * direct position} on the curve before the distance is calculated. If the curve is not simple and
     * passes through either of the two points more than once, the distance shall be the minimal distance
     * between the two points on this {@linkplain org.opengis.geometry.primitive.Curve curve}.
     *
     * @param point1 The first point, or {@code null} for the
     *               {@linkplain #getStartPoint start point}.
     * @param point2 The second point, or {@code null} for the
     *               {@linkplain #getEndPoint end point}.
     * @return The length between the two specified points.
     * @unitof Length
     */
    @UML(identifier="length", obligation=MANDATORY, specification=ISO_19107)
    double length(Position point1, Position point2);

    /**
     * Returns the length between two constructive parameters.
     * This second form of the method {@code length} shall work directly from the constructive
     * parameters, allowing the direct conversion between the variables used in parameterization and
     * constructive parameters.
     *
     * Distances between direct positions determined by the default parameterization are simply
     * the difference of the parameter. The length function also allows for the conversion of the
     * constructive parameter to the arc length parameter using the following idiom:
     * <p>
     * <center><code>
     * param = length({@linkplain #getStartConstructiveParam startConstructiveParam}, constructiveParam)
     *       + {@linkplain #getStartParam startParam}
     * </code></center>
     *
     * @param cparam1 The first constructive parameter.
     * @param cparam2 The second constructive parameter.
     * @return The length between the two specified constructive parameter.
     * @unitof Length
     */
    @UML(identifier="length", obligation=MANDATORY, specification=ISO_19107)
    double length(double cparam1, double cparam2);

    /**
     * Constructs a line string (sequence of line segments) where the control points (ends of
     * the segments) lie on this curve. If {@code maxSpacing} is given (not zero), then
     * the distance between control points along the generated curve shall be not more than
     * {@code maxSpacing}. If {@code maxOffset} is given (not zero), the distance
     * between generated curve at any point and the original curve shall not be more than the
     * {@code maxOffset}. If both parameters are set, then both criteria shall be met.
     * If the original control points of the curve lie on the curve, then they shall be included
     * in the returned {@linkplain LineString line string}'s control points. If both parameters are
     * set to zero, then the line string returned shall be constructed from the control points of the
     * original curve.
     * <blockquote><font size=2>
     * <strong>NOTE:</strong> This function is useful in creating linear approximations of the
     * curve for simple actions such as display. It is often referred to as a "stroked curve".
     * For this purpose, the {@code maxOffset} version is useful in maintaining a minimal
     * representation of the curve appropriate for the display device being targeted. This
     * function is also useful in preparing to transform a curve from one coordinate reference
     * system to another by transforming its control points. In this case, the
     * {@code maxSpacing} version is more appropriate. Allowing both parameters to default
     * to zero does not seem to have any useful geographic nor geometric interpretation unless
     * further information is known about how the curves were constructed.
     * </font></blockquote>
     *
     * @param maxSpacing The maximal distance between control points along the generated curve,
     *                   or 0 for no constraint.
     * @param maxOffset  The maximal distance between generated curve at any point and the original
     *                   curve, or 0 for no constraint.
     * @return The an approximation of this curve as a line string.
     * @unitof Distance (for arguments)
     */
    @UML(identifier="asLineString", obligation=MANDATORY, specification=ISO_19107)
    LineString asLineString(double maxSpacing, double maxOffset);
}
