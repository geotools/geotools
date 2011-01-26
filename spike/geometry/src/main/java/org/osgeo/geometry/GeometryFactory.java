/*
 *    OSGeom -- Geometry Collab
 *
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.osgeo.geometry;

import java.util.List;

import org.osgeo.commons.crs.CRS;
import org.osgeo.commons.uom.Angle;
import org.osgeo.commons.uom.Length;
import org.osgeo.geometry.composite.CompositeCurve;
import org.osgeo.geometry.composite.CompositeGeometry;
import org.osgeo.geometry.composite.CompositeSolid;
import org.osgeo.geometry.composite.CompositeSurface;
import org.osgeo.geometry.multi.MultiCurve;
import org.osgeo.geometry.multi.MultiSolid;
import org.osgeo.geometry.multi.MultiSurface;
import org.osgeo.geometry.points.Points;
import org.osgeo.geometry.primitive.Curve;
import org.osgeo.geometry.primitive.GeometricPrimitive;
import org.osgeo.geometry.primitive.LinearRing;
import org.osgeo.geometry.primitive.OrientableCurve;
import org.osgeo.geometry.primitive.OrientableSurface;
import org.osgeo.geometry.primitive.Point;
import org.osgeo.geometry.primitive.PolyhedralSurface;
import org.osgeo.geometry.primitive.Ring;
import org.osgeo.geometry.primitive.Solid;
import org.osgeo.geometry.primitive.Surface;
import org.osgeo.geometry.primitive.Tin;
import org.osgeo.geometry.primitive.TriangulatedSurface;
import org.osgeo.geometry.primitive.patches.Cone;
import org.osgeo.geometry.primitive.patches.Cylinder;
import org.osgeo.geometry.primitive.patches.PolygonPatch;
import org.osgeo.geometry.primitive.patches.Rectangle;
import org.osgeo.geometry.primitive.patches.Sphere;
import org.osgeo.geometry.primitive.patches.SurfacePatch;
import org.osgeo.geometry.primitive.patches.Triangle;
import org.osgeo.geometry.primitive.segments.AffinePlacement;
import org.osgeo.geometry.primitive.segments.Arc;
import org.osgeo.geometry.primitive.segments.ArcByBulge;
import org.osgeo.geometry.primitive.segments.ArcByCenterPoint;
import org.osgeo.geometry.primitive.segments.ArcString;
import org.osgeo.geometry.primitive.segments.ArcStringByBulge;
import org.osgeo.geometry.primitive.segments.BSpline;
import org.osgeo.geometry.primitive.segments.Bezier;
import org.osgeo.geometry.primitive.segments.Circle;
import org.osgeo.geometry.primitive.segments.CircleByCenterPoint;
import org.osgeo.geometry.primitive.segments.Clothoid;
import org.osgeo.geometry.primitive.segments.CubicSpline;
import org.osgeo.geometry.primitive.segments.CurveSegment;
import org.osgeo.geometry.primitive.segments.Geodesic;
import org.osgeo.geometry.primitive.segments.GeodesicString;
import org.osgeo.geometry.primitive.segments.Knot;
import org.osgeo.geometry.primitive.segments.LineStringSegment;
import org.osgeo.geometry.primitive.segments.OffsetCurve;

/**
 * Factory used to plug a Geometry implementation into a builder.
 * <p>
 * This api is not intended to be used directly; client code should use one of the Builders; or
 * WKTReader.
 * <p>
 * The first cut is method compatible with
 * 
 * @author Jody Garnett
 */
public interface GeometryFactory {

    /**
     * Creates a segmented {@link Curve} from one or more {@link CurveSegment}s. The last
     * {@link Point} of segment <code>i</code> must equal the first {@link Point} of segment
     * <code>i+1</code>.
     * 
     * @param id
     *            identifier of the new geometry instance
     * @param segments
     *            segments a curve shall be created from
     * @param crs
     *            coordinate reference system
     * @return created {@link Curve}
     */
    Curve curve(String id, CurveSegment[] segments, CRS crs);

    /**
     * Creates a {@link LineStringSegment} curve segment.
     * 
     * @param points
     *            points to create the {@link LineStringSegment} from
     * @return created {@link CurveSegment}
     */
    LineStringSegment lineStringSegment(Points points);

    /**
     * Creates an {@link Arc} curve segment.
     * 
     * @param p1
     *            first control point
     * @param p2
     *            second control point
     * @param p3
     *            third control point
     * 
     * @return created {@link Arc}
     */
    Arc createArc(Point p1, Point p2, Point p3);

    /**
     * Creates an {@link ArcByBulge} curve segment.
     * 
     * @param p1
     *            first control point
     * @param p2
     *            second control point
     * @param bulge
     *            height of the arc (multiplier for the normals)
     * @param normal
     *            normal vector, in 2D only one coordinate is necessary
     * @return created {@link ArcStringByBulge}
     */
    ArcByBulge createArcByBulge(Point p1, Point p2, double bulge, Point normal);

    /**
     * Creates an {@link ArcByCenterPoint} curve segment.
     * 
     * @param midPoint
     * @param radius
     * @param startAngle
     * @param endAngle
     * @return created {@link ArcByCenterPoint}
     */
    public ArcByCenterPoint arcByCenterPoint(Point midPoint, Length radius, Angle startAngle,
            Angle endAngle);

    /**
     * Creates an {@link ArcString} curve segment.
     * 
     * @param points
     *            control points, must contain <code>2 * k + 1</code> points
     * @return created {@link ArcString}
     */
    public ArcString arcString(Points points);

    /**
     * Creates an {@link ArcStringByBulge} curve segment.
     * <p>
     * This variant of the arc computes the mid points of the arcs instead of storing the
     * coordinates directly. The control point sequence consists of the start and end points of each
     * arc plus the bulge.
     * 
     * @param points
     *            list of control points, must contain at least two points
     * @param bulges
     *            heights of the arcs (multipliers for the normals)
     * @param normals
     *            normal vectors
     * @return created {@link ArcStringByBulge}
     */
    public ArcStringByBulge arcStringByBulge(Points points, double[] bulges, Points normals);

    /**
     * Creates a {@link Bezier} curve segment.
     * 
     * @param points
     *            list of control points
     * @param degree
     *            polynomial degree of the spline
     * @param knot1
     *            first of the two knots that define the spline basis functions
     * @param knot2
     *            second of the two knots that define the spline basis functions
     * @return created {@link Bezier}
     */
    public Bezier bezier(Points points, int degree, Knot knot1, Knot knot2);

    /**
     * Creates a {@link BSpline} curve segment.
     * 
     * @param points
     *            list of control points
     * @param degree
     *            polynomial degree of the spline
     * @param knots
     *            sequence of distinct knots that define the spline basis functions
     * @param isPolynomial
     *            set to true if this is a polynomial spline, otherwise it's a rational spline
     * @return created {@link BSpline}
     */
    public BSpline bSpline(Points points, int degree, List<Knot> knots, boolean isPolynomial);

    /**
     * Creates a {@link Circle} curve segment.
     * 
     * @param p1
     *            first control point
     * @param p2
     *            second control point
     * @param p3
     *            third control point
     * 
     * @return created {@link Arc}
     */
    public Circle circle(Point p1, Point p2, Point p3);

    /**
     * Creates an {@link CircleByCenterPoint} curve segment.
     * 
     * @param midPoint
     * @param radius
     * @param startAngle
     * @return created {@link CircleByCenterPoint}
     */
    public CircleByCenterPoint circleByCenterPoint(Point midPoint, Length radius, Angle startAngle);

    /**
     * Creates a {@link Geodesic} curve segment.
     * 
     * @param p1
     *            first control point
     * @param p2
     *            second control point
     * @return created {@link Geodesic}
     */
    public Geodesic geodesic(Point p1, Point p2);

    /**
     * Creates a {@link GeodesicString} curve segment.
     * 
     * @param points
     *            control points, at least two
     * @return created {@link GeodesicString}
     */
    public GeodesicString feodesicString(Points points);

    /**
     * Creates an {@link OffsetCurve} curve segment.
     * 
     * @param baseCurve
     *            the base geometry
     * @param direction
     *            the direction of the offset
     * @param distance
     *            the distance from the base curve
     * @return created {@link GeodesicString}
     */
    public OffsetCurve offsetCurve(Curve baseCurve, Point direction, Length distance);

    /**
     * Creates a {@link Surface} that consists of a number of {@link SurfacePatch} instances. The
     * passed patches must touch in a topological sense to form a valid {@link Surface}.
     * 
     * @param id
     *            identifier of the new geometry instance
     * @param patches
     *            patches to create a surface
     * @param crs
     *            coordinate reference system, may be null
     * @return created {@link Surface}
     */
    public Surface surface(String id, List<SurfacePatch> patches, CRS crs);

    /**
     * Creates a {@link PolygonPatch} surface patch.
     * 
     * @param exteriorRing
     *            ring that defines the outer boundary, this may be null (see section 9.2.2.5 of GML
     *            spec)
     * @param interiorRings
     *            list of rings that define the inner boundaries, may be empty or null
     * @return created {@link PolygonPatch}
     */
    public PolygonPatch polygonPatch(Ring exteriorRing, List<Ring> interiorRings);

    /**
     * Creates a {@link Ring} from a list of passed {@link Curve}s.
     * 
     * @param id
     *            identifier, may be null
     * @param crs
     *            coordinate reference system, may be null
     * @param members
     *            the <code>Curve</code>s that compose the <code>Ring</code>
     * @return created {@link Ring}
     */
    public Ring ring(String id, CRS crs, List<Curve> members);

    /**
     * Creates a simple {@link LinearRing} from a list of passed {@link Point}s.
     * 
     * @param id
     *            identifier, may be null
     * @param crs
     *            coordinate reference system, may be null
     * @param points
     *            control points
     * @return created {@link Ring}
     */
    public LinearRing linearRing(String id, CRS crs, Points points);

    /**
     * Creates an {@link OrientableCurve}.
     * 
     * @param id
     *            identifier, may be null
     * @param crs
     *            coordinate reference system, may be null
     * @param baseCurve
     *            base curve
     * @param isReversed
     *            set to true, if the orientation of the base curve shall be reversed in the created
     *            geometry
     * @return created {@link OrientableCurve}
     */
    public OrientableCurve orientableCurve(String id, CRS crs, Curve baseCurve, boolean isReversed);

    /**
     * Creates a {@link Triangle} surface patch.
     * 
     * @param exterior
     *            ring that contains exactly four planar points, the first and last point must be
     *            coincident
     * @return created {@link Triangle}
     */
    public Triangle triangle(LinearRing exterior);

    /**
     * Creates a {@link Rectangle} surface patch.
     * 
     * @param exterior
     *            ring that contains exactly five planar points, the first and last point must match
     * @return created {@link Rectangle}
     */
    public Rectangle rectangle(LinearRing exterior);

    /**
     * Creates an {@link OrientableSurface}.
     * 
     * @param id
     *            identifier, may be null
     * @param crs
     *            coordinate reference system, may be null
     * @param baseSurface
     *            base surface
     * @param isReversed
     *            set to true, if the orientation of the base surface shall be reversed
     * @return created {@link OrientableCurve}
     */
    public OrientableSurface orientableSurface(String id, CRS crs, Surface baseSurface,
            boolean isReversed);

    /**
     * Creates a {@link PolyhedralSurface}.
     * 
     * @param id
     *            identifier, may be null
     * @param crs
     *            coordinate reference system, may be null
     * @param memberPatches
     *            patches that constitute the surface
     * @return created {@link PolyhedralSurface}
     */
    public PolyhedralSurface polyhedralSurface(String id, CRS crs, List<PolygonPatch> memberPatches);

    /**
     * Creates a {@link TriangulatedSurface}.
     * 
     * @param id
     *            identifier, may be null
     * @param crs
     *            coordinate reference system, may be null
     * @param memberPatches
     *            patches that constitute the surface
     * @return created {@link TriangulatedSurface}
     */
    public TriangulatedSurface triangulatedSurface(String id, CRS crs, List<Triangle> memberPatches);

    /**
     * Creates a {@link Tin}.
     * 
     * @param id
     *            identifier, may be null
     * @param crs
     *            coordinate reference system, may be null
     * @param stopLines
     * @param breakLines
     * @param maxLength
     * @param controlPoints
     * @param patches
     * @return created {@link Tin}
     */
    public Tin tin(String id, CRS crs, List<List<LineStringSegment>> stopLines,
            List<List<LineStringSegment>> breakLines, Length maxLength, Points controlPoints,
            List<Triangle> patches);

    /**
     * Creates a {@link Clothoid} curve segment.
     * 
     * @param referenceLocation
     *            the affine mapping that places the curve defined by the Fresnel Integrals into the
     *            coordinate reference system of this object
     * @param scaleFactor
     *            the value for the constant in the Fresnel's integrals
     * @param startParameter
     *            the arc length distance from the inflection point that will be the start point for
     *            this curve segment
     * @param endParameter
     *            the arc length distance from the inflection point that will be the end point for
     *            this curve segment
     * @return created {@link Clothoid}
     */
    public Clothoid clothoid(AffinePlacement referenceLocation, double scaleFactor,
            double startParameter, double endParameter);

    /**
     * Creates a {@link Cone} surface patch.
     * 
     * @param grid
     *            the grid of control points that defines the Cone
     * @return created {@link Cone}
     */
    public Cone cone(List<Points> grid);

    /**
     * Creates a {@link Cylinder} surface patch.
     * 
     * @param grid
     *            the grid of control points that defines the Cylinder
     * @return created {@link Cylinder}
     */
    public Cylinder cylinder(List<Points> grid);

    /**
     * Creates a {@link Sphere} surface patch.
     * 
     * @param grid
     *            the grid of control points that defines the Sphere
     * @return created {@link Sphere}
     */
    public Sphere sphere(List<Points> grid);

    /**
     * Creates a {@link Clothoid} curve segment.
     * 
     * @param points
     *            control points, at least two
     * @param vectorAtStart
     *            the unit tangent vector at the start point of the spline
     * @param vectorAtEnd
     *            the unit tangent vector at the end point of the spline
     * @return created {@link Clothoid}
     */
    public CubicSpline cubicSpline(Points points, Point vectorAtStart, Point vectorAtEnd);

    /**
     * Creates a {@link Solid}.
     * 
     * @param id
     *            identifier, may be null
     * @param crs
     *            coordinate reference system, may be null
     * @param exteriorSurface
     *            the exterior surface (shell) of the solid, may be null
     * @param interiorSurfaces
     *            the interior surfaces of the solid, may be null or empty
     * @return created {@link Solid}
     */
    public Solid solid(String id, CRS crs, Surface exteriorSurface, List<Surface> interiorSurfaces);

    /**
     * Creates a {@link MultiCurve} from a list of passed {@link Curve}s.
     * 
     * @param id
     *            identifier, may be null
     * @param crs
     *            coordinate reference system, may be null
     * @param members
     *            curves that constitute the collection
     * @return created {@link MultiCurve}
     */
    public MultiCurve multiCurve(String id, CRS crs, List<Curve> members);

    /**
     * Creates a {@link MultiSurface} from a list of passed {@link Surface}s.
     * 
     * @param id
     *            identifier, may be null
     * @param crs
     *            coordinate reference system, may be null
     * @param members
     *            surfaces that constitute the collection
     * @return created {@link MultiSurface}
     */
    public MultiSurface multiSurface(String id, CRS crs, List<Surface> members);

    /**
     * Creates a {@link MultiSolid} from a list of passed {@link Solid}s.
     * 
     * @param id
     *            identifier, may be null
     * @param crs
     *            coordinate reference system, may be null
     * @param members
     *            solids that constitute the collection
     * @return created {@link MultiSolid}
     */
    public MultiSolid multiSolid(String id, CRS crs, List<Solid> members);

    /**
     * Creates a {@link CompositeCurve} from a list of passed {@link Curve}s.
     * 
     * @param id
     *            identifier, may be null
     * @param crs
     *            coordinate reference system, may be null
     * @param members
     *            curves that constitute the composited curve, each curve must end at the start
     *            point of the subsequent curve in the list
     * @return created {@link CompositeCurve}
     */
    public CompositeCurve compositeCurve(String id, CRS crs, List<Curve> members);

    /**
     * Creates a {@link CompositeSurface} from a list of passed {@link Surface}s.
     * 
     * @param id
     *            identifier, may be null
     * @param crs
     *            coordinate reference system, may be null
     * @param memberSurfaces
     *            surfaces that constitute the composited surface, the surfaces must join in pairs
     *            on common boundary curves and must, when considered as a whole, form a single
     *            surface
     * @return created {@link CompositeSurface}
     */
    public CompositeSurface createCompositeSurface(String id, CRS crs, List<Surface> memberSurfaces);

    /**
     * Creates a {@link CompositeSolid} from a list of passed {@link Solid}s.
     * 
     * @param id
     *            identifier, may be null
     * @param crs
     *            coordinate reference system, may be null
     * @param memberSolids
     *            solids that constitute the composited solid, the solids must join in pairs on
     *            common boundary surfaces and which, when considered as a whole, form a single
     *            solid
     * @return created {@link CompositeSolid}
     */
    public CompositeSolid compositeSolid(String id, CRS crs, List<Solid> memberSolids);

    /**
     * Creates a general {@link CompositeGeometry} from a list of primitive geometries.
     * 
     * @param id
     *            identifier, may be null
     * @param crs
     *            coordinate reference system, may be null
     * @param memberPrimitives
     * @return created {@link CompositeGeometry}
     */
    public CompositeGeometry<GeometricPrimitive> compositeGeometry(String id, CRS crs,
            List<GeometricPrimitive> memberPrimitives);

}
