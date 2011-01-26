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

import java.util.List;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.annotation.UML;
import org.opengis.annotation.Extension;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A factory of {@linkplain Primitive primitive} geometric objects.
 * All primitives created through this interface will use the
 * {@linkplain #getCoordinateReferenceSystem factory's coordinate reference system}.
 * Creating primitives in a different CRS may requires a different instance of
 * {@code PrimitiveFactory}.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface PrimitiveFactory {
    /**
     * Returns the coordinate reference system in use for all {@linkplain Primitive primitive}
     * geometric objects to be created through this interface.
     */
    CoordinateReferenceSystem getCoordinateReferenceSystem();

    /**
     * Returns an envelope as a primitive. An {@linkplain Envelope envelope} will often be
     * used in query operations, and therefore must have a cast operation that returns a
     * {@linkplain org.opengis.geometry.Geometry geometry}. The actual return of the operation depends
     * upon the dimension of the {@linkplain org.opengis.referencing.crs.CoordinateReferenceSystem coordinate
     * reference system} and the extent of the {@linkplain Envelope envelope}. In a 2D system,
     * the primitive returned will be a {@linkplain Surface surface} (if the envelope does not
     * collapse to a point or line). In 3D systems, the usual return is a {@linkplain Solid solid}.
     * <p>
     * <strong>EXAMPLE:</strong> In the case where the {@linkplain Envelope envelope} is totally
     * contained in the domain of validity of its {@linkplain org.opengis.referencing.crs.CoordinateReferenceSystem
     * coordinate reference system} object, its associated {@linkplain Primitive primitive} is the convex
     * hull of the various permutations of the coordinates in the corners. For example, suppose
     * that a particular envelope in 2D is defined as:
     *
     * <blockquote><pre>
     * lowerCorner = (x1, y1)
     * upperCorner = (x2, y2)</pre></blockquote>
     *
     * (we ignore the CRS below, assuming that it is a global variable), then we can take the
     * various permutations of the ordinate values to create a list of polygon corners:
     *
     * <blockquote><pre>
     * {@link org.opengis.geometry.aggregate.MultiPoint} = { (x1, y1), (x1, y2), (x2, y1), (x2, y2) }</pre></blockquote>
     *
     * If we then apply the {@linkplain org.opengis.geometry.Geometry#getConvexHull convex hull}
     * function to the multi point, we get a polygon as a {@linkplain Surface surface}.
     * The extent of a polygon in 2D is totally defined by its
     * {@linkplain org.opengis.geometry.Geometry#getBoundary boundary} (internal surface
     * patches are planar and do not need interior control points) which gives
     * us a data type to represent {@linkplain Surface surface} in 2D:
     *
     * <blockquote><pre>
     * {@link org.opengis.geometry.primitive.Ring} = {
     *     {@link org.opengis.geometry.coordinate.LineString} = { (x1, y1), (x1, y2), (x2, y2), (x2, y1), (x1, y1)}
     * }</pre></blockquote>
     *
     * So that the {@linkplain SurfaceBoundary surface boundary} record contains the above-cited
     * exterior ring, and an empty set of interior rings (convex sets have no "interior" holes).
     *
     * @throws MismatchedReferenceSystemException If geometric objects given in argument don't
     *         use compatible {@linkplain CoordinateReferenceSystem coordinate reference system}.
     * @throws MismatchedDimensionException If geometric objects given in argument don't have
     *         the expected dimension.
     */
    @UML(identifier="GM_Primitive(GM_Envelope)", obligation=MANDATORY, specification=ISO_19107)
    Primitive createPrimitive(Envelope envelope)
            throws MismatchedReferenceSystemException, MismatchedDimensionException;

    /**
     * Creates a point at the specified location specified by coordinates.
     *
     * @throws MismatchedDimensionException If geometric objects given in argument don't have
     *         the expected dimension.
     */
    @Extension
    Point createPoint(double[] coordinates)
            throws MismatchedDimensionException;

    /**
     * Creates a point at the specified position.
     *
     * @throws MismatchedReferenceSystemException If geometric objects given in argument don't
     *         use compatible {@linkplain CoordinateReferenceSystem coordinate reference system}.
     * @throws MismatchedDimensionException If geometric objects given in argument don't have
     *         the expected dimension.
     */
    @UML(identifier="GM_Point(GM_Position)", obligation=MANDATORY, specification=ISO_19107)
    Point createPoint(Position position)
            throws MismatchedReferenceSystemException, MismatchedDimensionException;

    /**
     * Takes a list of {@linkplain CurveSegment curve segments} with the appropriate
     * end-to-start relationships and creates a {@linkplain Curve curve}.
     *
     * @throws MismatchedReferenceSystemException If geometric objects given in argument don't
     *         use compatible {@linkplain CoordinateReferenceSystem coordinate reference system}.
     * @throws MismatchedDimensionException If geometric objects given in argument don't have
     *         the expected dimension.
     */
    @UML(identifier="GM_Curve(GM_CurveSegment[1..n])", obligation=MANDATORY, specification=ISO_19107)
    Curve createCurve(List<CurveSegment> segments)
            throws MismatchedReferenceSystemException, MismatchedDimensionException;

    /**
     * Takes a list of {@linkplain SurfacePatch surface patches} with the appropriate
     * side-toside relationships and creates a {@linkplain Surface surface}.
     *
     * @throws MismatchedReferenceSystemException If geometric objects given in argument don't
     *         use compatible {@linkplain CoordinateReferenceSystem coordinate reference system}.
     * @throws MismatchedDimensionException If geometric objects given in argument don't have
     *         the expected dimension.
     */
    @UML(identifier="GM_Surface(GM_SurfacePatch[1..n])", obligation=MANDATORY, specification=ISO_19107)
    Surface createSurface(List<SurfacePatch> surfaces)
            throws MismatchedReferenceSystemException, MismatchedDimensionException;

    /**
     * Constructs a {@linkplain Surface surface} by indicating its boundary as a collection
     * of {@linkplain Curve curves} organized into the specified {@linkplain SurfaceBoundary
     * surface boundary}. This method is guaranteed to work always in 2D coordinate spaces,
     * In 3D coordinate spaces, this method shall require all of the defining boundary
     * {@linkplain Curve curve} instances to be coplanar (lie in a single plane) which will
     * define the surface interior.
     *
     * @throws MismatchedReferenceSystemException If geometric objects given in argument don't
     *         use compatible {@linkplain CoordinateReferenceSystem coordinate reference system}.
     * @throws MismatchedDimensionException If geometric objects given in argument don't have
     *         the expected dimension.
     */
    @UML(identifier="GM_Surface(GM_SurfaceBoundary)", obligation=MANDATORY, specification=ISO_19107)
    Surface createSurface(SurfaceBoundary boundary)
            throws MismatchedReferenceSystemException, MismatchedDimensionException;

    /**
     * Constructs a new {@linkplain SurfaceBoundary surface boundary} object
     * representing the boundary of a two-dimensional surface.
     *
     * @param exterior In the normal 2D case, this identifies the curve that is
     *        the exterior curve of the surface.  In cases where an exterior
     *        cannot be unambiguously chosen (a bounded cylinder, for example),
     *        this parameter may be null.
     * @param interiors All of the curve components of the boundary that are not
     *        the exterior.
     * @throws MismatchedReferenceSystemException If geometric objects given in
     *         argument don't use a {@linkplain CoordinateReferenceSystem
     *         coordinate reference system} compatible with the one held by this
     *         factory.
     * @throws MismatchedDimensionException If geometric objects given in argument don't have
     *         the expected dimension.
     */
    @Extension
    SurfaceBoundary createSurfaceBoundary(Ring exterior, List<Ring> interiors)
            throws MismatchedReferenceSystemException, MismatchedDimensionException;

    /**
     * Constructs a {@linkplain Solid solid} by indicating its boundary as a collection of
     * {@linkplain Shell shells} organized into a {@linkplain SolidBoundary solid boundary}.
     * Since this specification is limited to 3-dimensional coordinate reference systems,
     * any solid is definable by its boundary.
     *
     * @throws MismatchedReferenceSystemException If geometric objects given in argument don't
     *         use compatible {@linkplain CoordinateReferenceSystem coordinate reference system}.
     * @throws MismatchedDimensionException If geometric objects given in argument don't have
     *         the expected dimension.
     */
    @UML(identifier="GM_Solid(GM_SolidBoundary)", obligation=MANDATORY, specification=ISO_19107)
    Solid createSolid(SolidBoundary boundary)
            throws MismatchedReferenceSystemException, MismatchedDimensionException;

    /**
     * Constructs a {@linkplain Ring ring} out of its component curves.
     *
     * @param curves The list of curves that comprise the newly created Ring.
     *        These curves must connect to form a continuous curve whose start
     *        point is the same as its end point.
     *
     * @throws MismatchedReferenceSystemException If geometric objects given in argument don't
     *         use compatible {@linkplain CoordinateReferenceSystem coordinate reference system}.
     * @throws MismatchedDimensionException If geometric objects given in argument don't have
     *         the expected dimension.
     */
    @Extension
    Ring createRing(List<OrientableCurve> curves)
            throws MismatchedReferenceSystemException, MismatchedDimensionException;
}
