/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.process.geometry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.geotools.geometry.jts.JTS;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.locationtech.jts.densify.Densifier;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.LineStringExtracter;
import org.locationtech.jts.operation.buffer.BufferParameters;
import org.locationtech.jts.operation.polygonize.Polygonizer;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A set of static functions providing the implementation of processes exposed by the {@link
 * GeometryProcessFactory}.
 *
 * @author Andrea Aime
 */
public class GeometryFunctions {

    /**
     * Maps the enumeration into the set of values used by the JTS {@link BufferParameters} class.
     *
     * @author Andrea Aime - OpenGeo
     */
    enum BufferCapStyle {
        Round(BufferParameters.CAP_ROUND),
        Flat(BufferParameters.CAP_FLAT),
        Square(BufferParameters.CAP_SQUARE);
        int value;

        private BufferCapStyle(int value) {
            this.value = value;
        }
    };

    @DescribeProcess(
            title = "Contains Test",
            description =
                    "Tests if no points of the second geometry lie in the exterior of the first geometry and at least one point of the interior of second geometry lies in the interior of first geometry.")
    @DescribeResult(description = "True if the first input contains the second input")
    public static boolean contains(
            @DescribeParameter(name = "a", description = "First input geometry") Geometry a,
            @DescribeParameter(
                            name = "b",
                            description =
                                    "Second input geometry, tested to be contained in first geometry")
                    Geometry b) {
        return a.contains(b);
    }

    @DescribeProcess(
            title = "Empty Test",
            description = "Tests if a geometry contains no vertices.")
    @DescribeResult(description = "True if the input is empty")
    public static boolean isEmpty(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry geom) {
        return geom.isEmpty();
    }

    @DescribeProcess(
            title = "Length",
            description =
                    "Returns the total length of all line segments in a geometry. Measurement is given in the source units, so geographic coordinates are not recommended.")
    @DescribeResult(description = "Total perimeter of the geometry")
    public static double length(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry geom) {
        return geom.getLength();
    }

    @DescribeProcess(title = "Intersects Test", description = "Tests if two geometries intersect.")
    @DescribeResult(description = "True if the inputs intersect")
    public static boolean intersects(
            @DescribeParameter(name = "a", description = "First input geometry") Geometry a,
            @DescribeParameter(name = "b", description = "Second input geometry") Geometry b) {
        return a.intersects(b);
    }

    @DescribeProcess(
            title = "Valid Test",
            description = "Tests if a geometry is topologically valid.")
    @DescribeResult(description = "True if the input is valid")
    public static boolean isValid(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry geom) {
        return geom.isValid();
    }

    @DescribeProcess(
            title = "Geometry Type",
            description =
                    "Returns the name of a geometry's type. Values are one of POINT, LINESTRING, POLYGON, MULTIPOINT, MULTILINESTRING, MULTIPOLYGON, GEOMETRYCOLLECTION.")
    @DescribeResult(description = "The name of the geometry type")
    public static String geometryType(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry geom) {
        return geom.getGeometryType();
    }

    @DescribeProcess(
            title = "Number of Points",
            description = "Returns the number of vertices in a given geometry.")
    @DescribeResult(description = "Total number of vertices")
    public static int numPoints(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry geom) {
        return geom.getNumPoints();
    }

    @DescribeProcess(
            title = "Simple Test",
            description =
                    "Tests if a geometry is topologically simple. Points, polygons, closed line strings, and linear rings are always simple. Other geometries are considered simple if no two points are identical.")
    @DescribeResult(description = "True if the input is simple")
    public static boolean isSimple(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry geom) {
        return geom.isSimple();
    }

    @DescribeProcess(
            title = "Distance",
            description =
                    "Returns the minimum distance between two geometries. Measurement is given in the input units, so geographic coordinates are not recommended.")
    @DescribeResult(description = "Distance between the two input geometries")
    public static double distance(
            @DescribeParameter(name = "a", description = "First input geometry") Geometry a,
            @DescribeParameter(name = "b", description = "Second input geometry") Geometry b) {
        return a.distance(b);
    }

    @DescribeProcess(
            title = "Within Distance Test",
            description =
                    "Tests if the minimum distance between two geometries is less than a tolerance value.")
    @DescribeResult(description = "True if the inputs are within the specified distance")
    public static boolean isWithinDistance(
            @DescribeParameter(name = "a", description = "First input geometry") Geometry a,
            @DescribeParameter(name = "b", description = "Second input geometry") Geometry b,
            @DescribeParameter(
                            name = "distance",
                            description = "Distance tolerance, in units of the input geometry")
                    double distance) {
        return a.isWithinDistance(b, distance);
    }

    @DescribeProcess(
            title = "Area",
            description =
                    "Returns the area of a geometry, in the units of the geometry. Assumes a Cartesian plane, so this process is only recommended for non-geographic CRSes.")
    @DescribeResult(description = "Area of the input geometry")
    public static double area(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry geom) {
        return geom.getArea();
    }

    @DescribeProcess(
            title = "Centroid",
            description =
                    "Returns the geometric centroid of a geometry. Output is a single point.  The centroid point may be located outside the geometry.")
    @DescribeResult(description = "Centroid of the input geometry")
    public static Geometry centroid(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry geom) {
        return geom.getCentroid();
    }

    @DescribeProcess(
            title = "Interior Point",
            description =
                    "Returns a point that lies inside a geometry if possible, or that lies on its boundary.")
    @DescribeResult(description = "Interior point")
    public static Geometry interiorPoint(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry geom) {
        return geom.getInteriorPoint();
    }

    @DescribeProcess(
            title = "Dimension",
            description =
                    "Returns the largest dimension of a geometry or geometry collection: 0 for point, 1 for line, 2 for polygon.")
    @DescribeResult(description = "Dimension of the input geometry")
    public static int dimension(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry geom) {
        return geom.getDimension();
    }

    @DescribeProcess(
            title = "Boundary",
            description =
                    "Returns a geometry boundary. For polygons, returns a linear ring or multi-linestring equal to the boundary of the polygon(s). For linestrings, returns a multipoint equal to the endpoints of the linestring. For points, returns an empty geometry collection.")
    @DescribeResult(description = "Boundary of the input geometry")
    public static Geometry boundary(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry geom) {
        return geom.getBoundary();
    }

    @DescribeProcess(
            title = "Envelope",
            description =
                    "Returns the smallest bounding box polygon that contains a geometry. For a point geometry, returns the same point.")
    @DescribeResult(description = "Envelope of the input geometry")
    public static Geometry envelope(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry geom) {
        return geom.getEnvelope();
    }

    @DescribeProcess(
            title = "Disjoint Test",
            description = "Tests if two geometries do not have any points in common.")
    @DescribeResult(description = "True if the inputs are disjoint")
    public static boolean disjoint(
            @DescribeParameter(name = "a", description = "First input geometry") Geometry a,
            @DescribeParameter(name = "b", description = "Second input geometry") Geometry b) {
        return a.disjoint(b);
    }

    @DescribeProcess(
            title = "Touches Test",
            description =
                    "Tests if two geometries have at least one boundary point in common, but share no interior points.")
    @DescribeResult(description = "True if the inputs touch")
    public static boolean touches(
            @DescribeParameter(name = "a", description = "First input geometry") Geometry a,
            @DescribeParameter(name = "b", description = "Second input geometry") Geometry b) {
        return a.touches(b);
    }

    @DescribeProcess(
            title = "Crosses Test",
            description =
                    "Tests if two geometries have some, but not all, interior points in common.")
    @DescribeResult(description = "True if the inputs cross")
    public static boolean crosses(
            @DescribeParameter(name = "a", description = "First input geometry") Geometry a,
            @DescribeParameter(name = "b", description = "Second input geometry") Geometry b) {
        return a.crosses(b);
    }

    @DescribeProcess(
            title = "Within Test",
            description = "Tests if the first geometry is contained in the second geometry.")
    @DescribeResult(description = "True if the first input is within the second input")
    public static boolean within(
            @DescribeParameter(name = "a", description = "First input geometry") Geometry a,
            @DescribeParameter(name = "b", description = "Second input geometry") Geometry b) {
        return a.within(b);
    }

    @DescribeProcess(
            title = "Overlaps Test",
            description =
                    "Tests if two geometries share some but not all interior points. Points or lines will always return False.")
    @DescribeResult(description = "True if the inputs overlap")
    public static boolean overlaps(
            @DescribeParameter(name = "a", description = "First input geometry") Geometry a,
            @DescribeParameter(name = "b", description = "Second input geometry") Geometry b) {
        return a.overlaps(b);
    }

    @DescribeProcess(
            title = "Relate Test",
            description =
                    "Tests if the spatial relationship between two geometries matches the given DE-9IM intersection matrix pattern. The pattern is given in the form [II][IB][IE][BI][BB][BE][EI][EB][EE] where I=interior, B=boundary, and E=exterior. Pattern symbols can be 2, 1, 0, F or *.")
    @DescribeResult(description = "True if the inputs have the given relationship")
    public static boolean relatePattern(
            @DescribeParameter(name = "a", description = "First input geometry") Geometry a,
            @DescribeParameter(name = "b", description = "First input geometry") Geometry b,
            @DescribeParameter(name = "Relate pattern", description = "Intersection matrix pattern")
                    String pattern) {
        return a.relate(b, pattern);
    }

    @DescribeProcess(
            title = "Relate Matrix String",
            description =
                    "Returns the DE-9IM intersection matrix string for the spatial relationship between the input geometries. The matrix string is in the form [II][IB][IE][BI][BB][BE][EI][EB][EE] where I=interior, B=boundary, and E=exterior. Matrix symbols are 2, 1, 0 or F.")
    @DescribeResult(description = "Intersection matrix string")
    public static String relate(
            @DescribeParameter(name = "a", description = "First input geometry") Geometry a,
            @DescribeParameter(name = "b", description = "Second input geometry") Geometry b) {
        return a.relate(b).toString();
    }

    @DescribeProcess(
            title = "Buffer",
            description =
                    "Returns a polygonal geometry representing the input geometry enlarged by a given distance around its exterior.")
    @DescribeResult(description = "Buffered geometry")
    public static Geometry buffer(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry geom,
            @DescribeParameter(
                            name = "distance",
                            description =
                                    "Distance to buffer the input geometry, in the units of the geometry")
                    double distance,
            @DescribeParameter(
                            name = "quadrantSegments",
                            description =
                                    "Number determining the style and smoothness of buffer corners. Positive numbers create round corners with that number of segments per quarter-circle, 0 creates flat corners.",
                            min = 0)
                    Integer quadrantSegments,
            @DescribeParameter(
                            name = "capStyle",
                            description =
                                    "Style for the buffer end caps. Values are: Round - rounded ends (default), Flat - flat ends; Square - square ends.",
                            min = 0)
                    BufferCapStyle capStyle) {
        if (quadrantSegments == null) quadrantSegments = BufferParameters.DEFAULT_QUADRANT_SEGMENTS;
        if (capStyle == null) capStyle = BufferCapStyle.Round;
        return geom.buffer(distance, quadrantSegments, capStyle.value);
    }

    @DescribeProcess(
            title = "Convex Hull",
            description =
                    "Returns the smallest convex polygon that contains the entire input geometry.")
    @DescribeResult(description = "Convex hull of input geometry")
    public static Geometry convexHull(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry geom) {
        return geom.convexHull();
    }

    @DescribeProcess(
            title = "Intersection",
            description =
                    "Returns a geometry representing the points that two geometries have in common.  The result may be a heterogeneous geometry collection. If no intersection, returns an empty geometry.")
    @DescribeResult(description = "Intersection of geometries")
    public static Geometry intersection(
            @DescribeParameter(name = "a", description = "First input geometry") Geometry a,
            @DescribeParameter(name = "b", description = "Second input geometry") Geometry b) {
        return a.intersection(b);
    }

    @DescribeProcess(
            title = "Union",
            description =
                    "Returns a geometry representing all points contained in any of the geometries in a geometry collection.")
    @DescribeResult(description = "Union of input geometries")
    public static Geometry union(
            @DescribeParameter(name = "geom", description = "Input geometries (minimum 2)", min = 2)
                    Geometry... geoms) {
        Geometry result = null;
        for (Geometry g : geoms) {
            if (result == null) {
                result = g;
            } else {
                result = result.union(g);
            }
        }
        return result;
    }

    @DescribeProcess(
            title = "Difference",
            description =
                    "Returns a geometry representing the points that are contained in a geometry but not contained in a second geometry. The result may be a heterogeneous geometry collection.")
    @DescribeResult(description = "Geometry representing the difference of the input geometries")
    public static Geometry difference(
            @DescribeParameter(name = "a", description = "First input geometry") Geometry a,
            @DescribeParameter(name = "b", description = "Second input geometry") Geometry b) {
        return a.difference(b);
    }

    @DescribeProcess(
            title = "Symmetric Difference",
            description =
                    "Returns a geometry representing the points contained in either one of two geometries but not in both. The result may be a heterogeneous geometry collection.")
    @DescribeResult(description = "Symmetric difference of the two geometries")
    public static Geometry symDifference(
            @DescribeParameter(name = "a", description = "First input geometry") Geometry a,
            @DescribeParameter(name = "b", description = "Second input geometry") Geometry b) {
        return a.symDifference(b);
    }

    @DescribeProcess(
            title = "Exactly Equal Test with Tolerance",
            description =
                    "Tests if two geometries are identical on a vertex-by-vertex basis, up to a vertex distance tolerance.")
    @DescribeResult(description = "True if the geometries are vertex-identical within tolerance")
    public static boolean equalsExactTolerance(
            @DescribeParameter(name = "a", description = "First input geometry") Geometry a,
            @DescribeParameter(name = "b", description = "Second input geometry") Geometry b,
            @DescribeParameter(
                            name = "tolerance",
                            description = "Tolerance distance for vertex equality test")
                    double tolerance) {
        return a.equalsExact(b, tolerance);
    }

    @DescribeProcess(
            title = "Exactly Equal Test",
            description = "Tests if two geometries are identical on a vertex-by-vertex basis.")
    @DescribeResult(description = "True if the geometries are vertex-identical")
    public static boolean equalsExact(
            @DescribeParameter(name = "a", description = "First input geometry") Geometry a,
            @DescribeParameter(name = "b", description = "Second input geometry") Geometry b) {
        return a.equalsExact(b);
    }

    @DescribeProcess(
            title = "Geometry Count",
            description =
                    "Returns the total number of elements in a geometry collection. If not a geometry collection, returns 1. If empty, returns 0.")
    @DescribeResult(description = "Total number of geometries")
    public static int numGeometries(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry collection) {
        return collection.getNumGeometries();
    }

    @DescribeProcess(
            title = "Nth Geometry",
            description =
                    "Returns the geometry element at a given index in a geometry collection. Indexing starts at 0.")
    @DescribeResult(description = "Geometry element from the geometry collection")
    public static Geometry getGeometryN(
            @DescribeParameter(name = "geom", description = "Input geometry")
                    GeometryCollection collection,
            @DescribeParameter(
                            name = "index",
                            description = "Index of geometry element (0 is first)")
                    int index) {
        return collection.getGeometryN(index);
    }

    @DescribeProcess(
            title = "Get X Ordinate",
            description =
                    "Returns the X value (first ordinate) for point geometries. For other geometry types returns the X value of the centroid.")
    @DescribeResult(description = "X value of point")
    public static double getX(
            @DescribeParameter(name = "geom", description = "Input point") Point point) {
        return point.getX();
    }

    @DescribeProcess(
            title = "Get Y Ordinate",
            description =
                    "Returns the Y value (second ordinate) for point geometries. For other geometry types returns the Y value of the centroid.")
    @DescribeResult(description = "Y value of point")
    public static double getY(
            @DescribeParameter(name = "geom", description = "Input point") Point point) {
        return point.getY();
    }

    @DescribeProcess(
            title = "Closed Test",
            description =
                    "Tests if the initial vertex equals the final vertex in a linear geometry. Points and polygons always return True.")
    @DescribeResult(description = "True if the input is closed")
    public static boolean isClosed(
            @DescribeParameter(name = "geom", description = "Input geometry") LineString line) {
        return line.isClosed();
    }

    @DescribeProcess(
            title = "Nth Point",
            description =
                    "Returns a point geometry equal to the Nth vertex in a geometry as determined by a given index. First vertex has index 0.")
    @DescribeResult(description = "Vertex as point geometry")
    public static Point pointN(
            @DescribeParameter(name = "geom", description = "Input geometry") LineString line,
            @DescribeParameter(name = "index", description = "Index of vertex (0 is first)")
                    int index) {
        return line.getPointN(index);
    }

    @DescribeProcess(
            title = "Start Point",
            description = "Returns a point geometry equal to the first vertex of a LineString.")
    @DescribeResult(description = "First vertex as point geometry")
    public static Point startPoint(
            @DescribeParameter(name = "geom", description = "Input line") LineString line) {
        return line.getStartPoint();
    }

    @DescribeProcess(
            title = "End Point",
            description = "Returns a point geometry equal to the final vertex of a LineString.")
    @DescribeResult(description = "Final vertex as point geometry")
    public static Point endPoint(
            @DescribeParameter(name = "geom", description = "Input line") LineString line) {
        return line.getEndPoint();
    }

    @DescribeProcess(
            title = "Ring Test",
            description = "Tests if a geometry is both closed and simple.")
    @DescribeResult(description = "True if the input is a ring")
    public static boolean isRing(
            @DescribeParameter(name = "geom", description = "Input geometry") LineString line) {
        return line.isRing();
    }

    @DescribeProcess(
            title = "Exterior Ring",
            description = "Returns the exterior ring of a polygonal geometry.")
    @DescribeResult(description = "Exterior ring of geometry")
    public static Geometry exteriorRing(
            @DescribeParameter(name = "geom", description = "Input geometry") Polygon polygon) {
        return polygon.getExteriorRing();
    }

    @DescribeProcess(
            title = "Interior Ring Count",
            description =
                    "Returns the total number of interior rings in a polygonal geometry. Points and lines return 0.")
    @DescribeResult(description = "Total number of interior rings")
    public static int numInteriorRing(
            @DescribeParameter(name = "geom", description = "Input geometry") Polygon polygon) {
        return polygon.getNumInteriorRing();
    }

    @DescribeProcess(
            title = "Nth Interior Ring",
            description =
                    "Returns a linear ring from a polygon containing interior rings (holes) determined by a given index. First interior ring has index 0. If no interior rings, returns null.")
    @DescribeResult(description = "Interior ring as a linear ring")
    public static Geometry interiorRingN(
            @DescribeParameter(name = "geom", description = "Input polygon with interior ring")
                    Polygon polygon,
            @DescribeParameter(name = "index", description = "Index of interior ring (0 is first)")
                    int index) {
        return polygon.getInteriorRingN(index);
    }

    @DescribeProcess(
            title = "Simplify",
            description =
                    "Returns a geometry that has been simplified (reduced in vertices) according to the Douglas-Peucker algorithm.")
    @DescribeResult(description = "Simplified geometry")
    public static Geometry simplify(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry geom,
            @DescribeParameter(
                            name = "distance",
                            description =
                                    "Simplification distance tolerance, in units of the input geometry")
                    double distance) {
        return DouglasPeuckerSimplifier.simplify(geom, distance);
    }

    @DescribeProcess(
            title = "Densify",
            description =
                    "Returns a spatially equivalent geometry with vertices added to ensure line segments are no longer than a given distance.")
    @DescribeResult(description = "Densified geometry")
    public static Geometry densify(
            @DescribeParameter(name = "geom", description = "Input geometry") Geometry geom,
            @DescribeParameter(
                            name = "distance",
                            description =
                                    "The maximum segment length in the result, in the units of the geometry")
                    double distance) {
        return Densifier.densify(geom, distance);
    }

    @DescribeProcess(
            title = "Polygonize",
            description =
                    "Creates a set of polygons from linestrings delineating them.  The linestrings must be correctly noded (i.e. touch only at endpoints).")
    @DescribeResult(description = "The collection of created polygons")
    public static Geometry polygonize(
            @DescribeParameter(name = "geom", description = "Linework to polygonize")
                    Geometry geom) {
        List lines = LineStringExtracter.getLines(geom);
        Polygonizer polygonizer = new Polygonizer();
        polygonizer.add(lines);
        Collection polys = polygonizer.getPolygons();
        Polygon[] polyArray = GeometryFactory.toPolygonArray(polys);
        return geom.getFactory().createGeometryCollection(polyArray);
    }

    @DescribeProcess(title = "Split Polygon", description = "Splits a polygon by a linestring")
    @DescribeResult(description = "The collection of split polygons")
    public static Geometry splitPolygon(
            @DescribeParameter(name = "polygon", description = "Polygon to split") Geometry polygon,
            @DescribeParameter(name = "line", description = "Linestring to split by")
                    LineString line) {
        Geometry nodedLinework = polygon.getBoundary().union(line);
        Geometry polys = polygonize(nodedLinework);

        // Only keep polygons which are inside the input
        List<Polygon> output = new ArrayList<>();
        for (int i = 0; i < polys.getNumGeometries(); i++) {
            Polygon candpoly = (Polygon) polys.getGeometryN(i);
            // use interior point to test for inclusion in parent
            if (polygon.contains(candpoly.getInteriorPoint())) {
                output.add(candpoly);
            }
        }
        return polygon.getFactory()
                .createGeometryCollection(GeometryFactory.toGeometryArray(output));
    }

    /** Will reproject a geometry to another CRS. */
    @DescribeProcess(
            title = "Reproject Geometry",
            description =
                    "Reprojects a given geometry into a supplied coordinate reference system.")
    @DescribeResult(name = "result", description = "Reprojected geometry")
    public static Geometry reproject(
            @DescribeParameter(name = "geometry", description = "Input geometry") Geometry geometry,
            @DescribeParameter(
                            name = "sourceCRS",
                            min = 0,
                            description = "Coordinate reference system of input geometry")
                    CoordinateReferenceSystem sourceCRS,
            @DescribeParameter(
                            name = "targetCRS",
                            min = 0,
                            description =
                                    "Target coordinate reference system to use for reprojection")
                    CoordinateReferenceSystem targetCRS) {

        try {
            return JTS.transform(geometry, CRS.findMathTransform(sourceCRS, targetCRS, true));
        } catch (Exception e) {
            throw new ProcessException("Reprojection faiiled", e);
        }
    }
}
