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

import java.util.List;
import org.opengis.geometry.primitive.CurveInterpolation;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A variant of the arc that stores the parameters of the second constructor of
 * the component {@linkplain Arc arcs} and recalculates the other attributes of
 * the standard arc. The {@linkplain ArcString#getControlPoints control point} sequence
 * is similar to that in {@linkplain ArcString arc string}, but the midPoint
 * {@linkplain Position position} is not needed since it is to be calculated.
 * The control point sequence shall consist of the start and end points of each arc.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 *
 * @see GeometryFactory#createArcStringByBulge
 */
@UML(identifier="GM_ArcStringByBulge", specification=ISO_19107)
public interface ArcStringByBulge extends CurveSegment {
    /**
     * Returns the offset of each arc's midpoint. The attribute "bulge" is the real number
     * multiplier for the normal that determines the offset direction of the midpoint of each
     * arc. The length of the bulge sequence is exactly 1 less than the length of the control
     * point array, since a bulge is needed for each pair of adjacent points in the control point
     * array.
     * <p>
     * The bulge is not given by a distance, since it is simply a multiplier for the normal,
     * the unit of the offset distance is determined by the length function for vectors in
     * the coordinate reference system. In the examples in this specification, the normal is
     * often given as a Euclidean unit vector, which may or may not fix its length to one
     * depending of the distance formulae used for the coordinate reference system.
     * <p>
     * The midpoint of the resulting arc is given by:
     *
     * <blockquote><code>
     * midPoint = (({@link #getStartPoint startPoint} +
     *              {@link #getEndPoint     endPoint})/2.0)
     *            + bulge&times;{@link #getNormals normal}
     * </code></blockquote>
     *
     * @return The offset of each arc's midpoint.
     */
    @UML(identifier="bulge", obligation=MANDATORY, specification=ISO_19107)
    double[] getBulges();

    /**
     * Returns the number of circular arcs in the string. Since the interpolation method
     * requires overlapping sets of 2 positions, the number of arcs determines the number
     * of {@linkplain ArcString#getControlPoints control points}.
     *
     * <blockquote>
     * <pre>numArc = {@link ArcString#getControlPoints controlPoints}.length - 1</pre>
     * </blockquote>
     *
     * @return The number of circular arcs.
     */
    @UML(identifier="numArc", obligation=MANDATORY, specification=ISO_19107)
    int getNumArc();

    /**
     * Returns a vector normal (perpendicular) to the chord of the arc, the line joining the
     * first and last point of the arc. In a 2D coordinate system, there are only two possible
     * directions for the normal, and it is often given as a signed real, indicating its length,
     * with a positive sign indicating a left turn angle from the chord line, and a negative sign
     * indicating a right turn from the chord. In 3D, the normal determines the plane of the arc,
     * along with the {@linkplain #getStartPoint start} and {@linkplain #getEndPoint end point}
     * of the arc.
     * <p>
     * The normal is usually a unit vector, but this is not absolutely necessary. If the normal
     * is a zero vector, the geometric object becomes equivalent to the straight line between
     * the two end points. The length of the normal sequence is exactly the same as for the
     * {@linkplain #getBulges bulge} sequence, 1 less than the control point sequence length.
     *
     * @return The sequence of normal vectors.
     */
    @UML(identifier="normal", obligation=MANDATORY, specification=ISO_19107)
    List<double[]> getNormals();

    /**
     * The interpolation for a {@code ArcStringByBulge} is
     * "{@linkplain CurveInterpolation#CIRCULAR_ARC_2_POINTS_WITH_BULGE
     * Circular arc by 2 points and bulge factor}".
     *
     * @return Always {@link CurveInterpolation#CIRCULAR_ARC_2_POINTS_WITH_BULGE}.
     */
    @UML(identifier="interpolation", obligation=MANDATORY, specification=ISO_19107)
    CurveInterpolation getInterpolation();

    /**
     * Recast as a base {@linkplain ArcString arc string}.
     *
     * @return This arc string by bulge as a base {@linkplain ArcString arc string}.
     */
    @UML(identifier="asGM_ArcString", obligation=MANDATORY, specification=ISO_19107)
    ArcString asArcString();
}
