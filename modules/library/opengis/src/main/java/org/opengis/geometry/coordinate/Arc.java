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
import org.opengis.geometry.primitive.Bearing;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Arc of the circle determined by 3 points, starting at the first, passing through the second
 * and terminating at the third. If the 3 points are co-linear, then the arc shall be a 3-point
 * line string, and will not be able to return values for center, radius, start angle and end
 * angle.
 *
 * <blockquote><font size=2>
 * <strong>NOTE:</strong> In the model, an  {@code Arc} is a subclass of {@link ArcString},
 * being a trivial arc string consisting of only one arc. This may be counter-intuitive in the
 * sense that subclasses are often thought of as more complex than their superclass (with
 * additional methods and attributes). An {@code Arc} is simpler than a {@linkplain ArcString
 * arc string} in that it has less data, but it is more complex in that it can return geometric
 * information such as "center", "start angle", and "end angle". This additional computational
 * complexity forces the subclassing to be the way it is. In addition the "is type of" semantics
 * works this way and not the other.
 * </font></blockquote>
 *
 * In its simplest representation, the three points in the {@linkplain #getControlPoints control point}
 * sequence for an {@code Arc} shall consist of, in order, the initial point on the arc, some
 * point on the arc neither at the start or end, and the end point of the {@code Arc}. If
 * additional points are given, then all points must lie on the circle defined by any 3 non-colinear
 * points in the control point array. All points shall lie on the same circle, and shall be given
 * in the {@linkplain #getControlPoints control point} array in the order in which they occur on
 * the arc.
 *
 * <blockquote><font size=2>
 * <strong>NOTE:</strong> The use of the term "midPoint" for the center {@linkplain Position position}
 * of the {@linkplain #getControlPoints control point} sequence is not meant to require that
 * the {@linkplain Position position} be the geometric midpoint of the arc. This is the best
 * choice for this {@linkplain Position position} from a computational stability perspective,
 * but it is not absolutely necessary for the mathematics to work.
 * </font></blockquote>
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 *
 * @see GeometryFactory#createArc(Position,Position,Position)
 * @see GeometryFactory#createArc(Position,Position,double,double[])
 */
@UML(identifier="GM_Arc", specification=ISO_19107)
public interface Arc extends ArcString {
    /**
     * Calculates the center of the circle of which this arc is a portion as a direct position.
     * The {@linkplain org.opengis.referencing.crs.CoordinateReferenceSystem coordinate reference system}
     * of the returned {@linkplain DirectPosition direct position} will be the same as that
     * for this {@code Arc}. In some extreme cases, the {@linkplain DirectPosition direct
     * position} as calculated may lie outside the domain of validity of the coordinate reference
     * system used by this {@code Arc} (especially if the underlying arc has a very large
     * radius). Implementations may choose an appropriate course of action in such cases.
     *
     * @return The center of the circle of which this arc is a portion.
     */
    @UML(identifier="center", obligation=MANDATORY, specification=ISO_19107)
    DirectPosition getCenter();

    /**
     * Calculates the radius of the circle of which this arc is a portion.
     *
     * @return The radius of the circle of which this arc is a portion.
     * @unitof Distance
     */
    @UML(identifier="radius", obligation=MANDATORY, specification=ISO_19107)
    double getRadius();

    /**
     * Calculates the bearing of the line from the center of the circle of which this arc is a
     * portion to the start point of the arc. In the 2D case this will be a start angle. In the
     * 3D case, the normal bearing angle implies that the arc is parallel to the reference circle.
     * If this is not the case, then the bearing must include altitude information.
     *
     * @return The bearing from the {@linkplain #getCenter center} of the circle to the
     *         {@link #getStartPoint start point} of this arc.
     *
     * @todo Inconsistent UML: "startAngle" and "startOfArc" are both used.
     *       Which one is the right one?
     */
    @UML(identifier="startAngle", obligation=MANDATORY, specification=ISO_19107)
    Bearing getStartAngle();

    /**
     * Calculates the bearing of the line from the center of the circle of which this arc is a
     * portion to the end point of the arc. In the 2D case this will be an end angle. In the 3D
     * case, the normal bearing angle implies that the arc is parallel to the reference circle.
     * If this is not the case, then the bearing must include altitude information.
     *
     * @return The bearing from the {@linkplain #getCenter center} of the circle to the
     *         {@link #getEndPoint end point} of this arc.
     *
     * @todo Inconsistent UML: "endAngle" and "endOfArc" are both used.
     *       Which one is the right one?
     */
    @UML(identifier="endAngle", obligation=MANDATORY, specification=ISO_19107)
    Bearing getEndAngle();
}
