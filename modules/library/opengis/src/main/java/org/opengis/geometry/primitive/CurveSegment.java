/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.primitive;

import org.opengis.geometry.coordinate.GenericCurve;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.annotation.Association;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Defines a homogeneous segment of a {@linkplain Curve curve}.
 * Each {@code CurveSegment} shall be in, at most, one {@linkplain Curve curve}.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier="GM_CurveSegment", specification=ISO_19107)
public interface CurveSegment extends GenericCurve {
    /**
     * Returns the curve which own this curve segment. This method is <em>optional</em> since
     * the association in ISO 19107 is navigable only from {@code Curve} to {@code CurveSegment},
     * not the other way.
     *
     * <blockquote><font size=2>
     * <strong>NOTE:</strong> In the specification, curve segments do not appear except in the
     * context of a curve, and therefore this method should never returns {@code null} which
     * would preclude the use of curve segments except in this manner. While this would not
     * affect the specification, allowing {@code null} owner allows other standards based on
     * ISO 19107 one to use curve segments in a more open-ended manner.
     * </font></blockquote>
     *
     * @return The owner of this curve segment, or {@code null} if the association is
     *         not available or not implemented that way.
     *
     * @see Curve#getSegments
     * @see SurfacePatch#getSurface
     * @issue http://jira.codehaus.org/browse/GEO-63
     */
    @Association("Segmentation")
    @UML(identifier="curve", obligation=OPTIONAL, specification=ISO_19107)
    Curve getCurve();

    /**
     * Specifies the curve interpolation mechanism used for this segment. This mechanism
     * uses the control points and control parameters to determine the position of this
     * {@code CurveSegment}.
     *
     * @return The interpolation mechanism used for this segment.
     */
    @UML(identifier="interpolation", obligation=MANDATORY, specification=ISO_19107)
    CurveInterpolation getInterpolation();

    /**
     * Specifies the type of continuity between this curve segment and its immediate neighbors.
     * If this is the first curve segment in the curve, this value is ignored.
     *
     * <blockquote><font size=2>
     * <strong>NOTE:</strong> Use of these values is only appropriate when the basic curve
     * definition is an underdetermined system. For example, line strings and segments cannot
     * support continuity above C<sup>0</sup>, since there is no spare control parameter to
     * adjust the incoming angle at the end points of the segment. Spline functions on the
     * other hand often have extra degrees of freedom on end segments that allow them to adjust
     * the values of the derivatives to support C<sup>1</sup> or higher continuity.
     * </font></blockquote>
     *
     * @return The type of continuity between this curve semgent and its immediate neighbors.
     *
     * @see #getNumDerivativesInterior
     * @see #getNumDerivativesAtEnd
     */
    @UML(identifier="numDerivativesAtStart", obligation=MANDATORY, specification=ISO_19107)
    int getNumDerivativesAtStart();

    /**
     * Specifies the type of continuity that is guaranteed interior to the curve. The default
     * value of "0" means simple continuity, which is a mandatory minimum level of continuity.
     * This level is referred to as "C<sup>0</sup>" in mathematical texts. A value of 1 means
     * that the function and its first derivative are continuous at the appropriate end point:
     * "C<sup>1</sup>" continuity. A value of "n" for any integer means the function and its
     * first <var>n</var> derivatives are continuous: "C<sup>n</sup>" continuity.
     *
     * @return The type of continuity that is guaranteed interior to the curve.
     *
     * @see #getNumDerivativesAtStart
     * @see #getNumDerivativesAtEnd
     */
    @UML(identifier="numDerivativesInterior", obligation=MANDATORY, specification=ISO_19107)
    int getNumDerivativesInterior();

    /**
     * Specifies the type of continuity between this curve segment and its immediate neighbors.
     * If this is the last curve segment in the curve, this value is ignored.
     *
     * <blockquote><font size=2>
     * <strong>NOTE:</strong> Use of these values is only appropriate when the basic curve
     * definition is an underdetermined system. For example, line strings and segments cannot
     * support continuity above C<sup>0</sup>, since there is no spare control parameter to
     * adjust the incoming angle at the end points of the segment. Spline functions on the
     * other hand often have extra degrees of freedom on end segments that allow them to adjust
     * the values of the derivatives to support C<sup>1</sup> or higher continuity.
     * </font></blockquote>
     *
     * @return The type of continuity between this curve semgent and its immediate neighbors.
     *
     * @see #getNumDerivativesAtStart
     * @see #getNumDerivativesInterior
     */
    @UML(identifier="numDerivativesAtEnd", obligation=MANDATORY, specification=ISO_19107)
    int getNumDerivativesAtEnd();

    /**
     * Returns an ordered array of point values that lie on the {@linkplain CurveSegment curve segment}.
     * In most cases, these will be related to control points used in the construction of the segment.
     * The control points of a curve segment are use to control its shape, and are not always on the
     * curve segment itself. For example in a spline curve, the curve segment is given as a weighted
     * vector sum of the control points. Each weight function will have a maximum within the
     * constructive parameter interval, which will roughly correspond to the point on the curve
     * where it passes closest that the corresponding control point. These points, the values of
     * the curve at the maxima of the weight functions, will be the sample points for the curve
     * segment.
     *
     * @return The control points.
     */
    @UML(identifier="samplePoint", obligation=MANDATORY, specification=ISO_19107)
    PointArray getSamplePoints();

    /**
     * Returns an ordered pair of points, which are the start point and end point of the curve.
     * This method operates with the same semantics as that on {@linkplain Curve#getBoundary curve}
     * except that the end points of a {@code CurveSegment} are not necessarily existing
     * {@linkplain Point points} and thus the boundary may contain transient {@linkplain Point points}.
     *
     * <blockquote><font size=2>
     * <strong>NOTE:</strong> The above {@linkplain CurveBoundary curve boundary} will almost always
     * be two distinct positions, but, like {@linkplain Curve curves}, {@code CurveSegment}s can
     * be cycles in themselves. The most likely scenario is that all of the points used will be transients
     * (constructed to support the return value), except for the start point and end point of the aggregated
     * {@linkplain Curve curve}. These two positions, in the case where the {@linkplain Curve curve} is
     * involved in a {@linkplain org.opengis.geometry.complex.Complex complex}, will be represented as
     * {@linkplain Point points} in the same {@linkplain org.opengis.geometry.complex.Complex complex}.
     * </font></blockquote>
     *
     * @return The sets of positions on the boundary.
     */
    @UML(identifier="boundary", obligation=MANDATORY, specification=ISO_19107)
    CurveBoundary getBoundary();

    /**
     * Reverses the orientation of the parameterizations of the segment.
     *
     * @return The reverse of this curve segment.
     */
    @UML(identifier="reverse", obligation=MANDATORY, specification=ISO_19107)
    CurveSegment reverse();
}
