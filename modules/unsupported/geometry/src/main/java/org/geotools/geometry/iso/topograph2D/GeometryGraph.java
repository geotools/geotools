/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2006  Vivid Solutions
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
package org.geotools.geometry.iso.topograph2D;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.geometry.iso.aggregate.MultiPrimitiveImpl;
import org.geotools.geometry.iso.complex.CompositeCurveImpl;
import org.geotools.geometry.iso.complex.CompositePointImpl;
import org.geotools.geometry.iso.complex.CompositeSurfaceImpl;
import org.geotools.geometry.iso.primitive.CurveBoundaryImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.geometry.iso.primitive.RingImpl;
import org.geotools.geometry.iso.primitive.RingImplUnsafe;
import org.geotools.geometry.iso.primitive.SurfaceBoundaryImpl;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.geotools.geometry.iso.topograph2D.index.EdgeSetIntersector;
import org.geotools.geometry.iso.topograph2D.index.SegmentIntersector;
import org.geotools.geometry.iso.topograph2D.index.SimpleMonotoneChainSweepLineIntersector;
import org.geotools.geometry.iso.topograph2D.util.CoordinateArrays;
import org.geotools.geometry.iso.util.Assert;
import org.geotools.geometry.iso.util.algorithm2D.LineIntersector;
import org.geotools.util.SuppressFBWarnings;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.aggregate.MultiSurface;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;

/** A GeometryGraph is a graph that models a given Geometry */
public class GeometryGraph extends PlanarGraph {

    /**
     * This method implements the Boundary Determination Rule for determining whether a component
     * (node or edge) that appears multiple times in elements of a MultiGeometry is in the boundary
     * or the interior of the Geometry <br>
     * The SFS uses the "Mod-2 Rule", which this function implements <br>
     * An alternative (and possibly more intuitive) rule would be the "At Most One Rule":
     * isInBoundary = (componentCount == 1)
     */
    @SuppressFBWarnings("IM_BAD_CHECK_FOR_ODD")
    public static boolean isInBoundary(int boundaryCount) {
        if (boundaryCount < 0) {
            throw new IllegalArgumentException(
                    "boundaryCount must be non negative, but was " + boundaryCount);
        }
        // the "Mod-2 Rule"
        return boundaryCount % 2 == 1;
    }

    public static int determineBoundary(int boundaryCount) {
        return isInBoundary(boundaryCount) ? Location.BOUNDARY : Location.INTERIOR;
    }

    private GeometryImpl parentGeom;

    // the precision model of the Geometry represented by this graph
    // private PrecisionModel precisionModel = null;
    // private int SRID;
    /**
     * The lineEdgeMap is a map of the linestring components of the parentGeometry to the edges
     * which are derived from them. This is used to efficiently perform findEdge queries
     */
    private Map lineEdgeMap = new HashMap();

    // private PrecisionModel newPM = null;
    /**
     * If this flag is true, the Boundary Determination Rule will used when deciding whether nodes
     * are in the boundary or not
     */
    private boolean useBoundaryDeterminationRule = true;

    private int argIndex; // the index of this geometry as an argument to a

    // spatial function (used for labelling)

    private Collection boundaryNodes;

    private boolean hasTooFewPoints = false;

    private Coordinate invalidPoint = null;

    private EdgeSetIntersector createEdgeSetIntersector() {
        // various options for computing intersections, from slowest to fastest

        // private EdgeSetIntersector esi = new SimpleEdgeSetIntersector();
        // private EdgeSetIntersector esi = new MonotoneChainIntersector();
        // private EdgeSetIntersector esi = new NonReversingChainIntersector();
        // private EdgeSetIntersector esi = new SimpleSweepLineIntersector();
        // private EdgeSetIntersector esi = new MCSweepLineIntersector();

        // return new SimpleEdgeSetIntersector();
        return new SimpleMonotoneChainSweepLineIntersector();
    }

    public GeometryGraph(int argIndex, GeometryImpl parentGeom) {
        this.argIndex = argIndex;
        this.parentGeom = parentGeom;
        if (parentGeom != null) {
            add(parentGeom);
        }
    }

    //	public GeometryGraph(int argIndex, List<GeometryImpl> parentGeomList) {
    //		this.argIndex = argIndex;
    //		this.parentGeom = null;
    //		if (parentGeomList != null) {
    //			this.addGeometryList(parentGeomList);
    //		}
    //	}

    public boolean hasTooFewPoints() {
        return hasTooFewPoints;
    }

    public Coordinate getInvalidPoint() {
        return invalidPoint;
    }

    public GeometryImpl getGeometry() {
        return parentGeom;
    }

    public Collection getBoundaryNodes() {
        if (boundaryNodes == null) boundaryNodes = nodes.getBoundaryNodes(argIndex);
        return boundaryNodes;
    }

    public Coordinate[] getBoundaryPoints() {
        Collection coll = getBoundaryNodes();
        Coordinate[] pts = new Coordinate[coll.size()];
        int i = 0;
        for (Iterator it = coll.iterator(); it.hasNext(); ) {
            Node node = (Node) it.next();
            pts[i++] = (Coordinate) node.getCoordinate().clone();
        }
        return pts;
    }

    public Edge findEdge(CurveImpl line) {
        return (Edge) lineEdgeMap.get(line);
    }

    public void computeSplitEdges(List edgelist) {
        for (Iterator i = edges.iterator(); i.hasNext(); ) {
            Edge e = (Edge) i.next();
            // SJ: Will add edges of intersections points to "edgelist" -
            // without labeling in relation to the other geometry (the labeling
            // in relation to it´s own geometry is applied)
            e.eiList.addSplitEdges(edgelist);
        }
    }

    /** Add a {@link GeometryImpl} to this {@link GeometryGraph} */
    private void add(GeometryImpl g) {

        // TODO auskommentiert; checken!
        // if (g.isEmpty()) return;

        // TODO auskommentiert; checken!
        // check if this Geometry should obey the Boundary Determination Rule
        // all collections except MultiSurfaces obey the rule
        if (g instanceof MultiSurface) useBoundaryDeterminationRule = false;

        /*
         * The following classes shall be considered for building a geometry graph in 2d/2.5d:
         * CompositeCurve
         * CompositeSurface
         * Curve
         * CurveBoundary
         * Point
         * Ring
         * Surface
         * SurfaceBoundary
         * MultiPrimitive
         * MultiPoint
         * MultiCurve
         * MultiSurface
         */
        if (g instanceof PointImpl) this.addPoint((PointImpl) g);
        else if (g instanceof CurveImpl) this.addCurve((CurveImpl) g);
        else if (g instanceof CurveBoundaryImpl) this.addCurveBoundary((CurveBoundaryImpl) g);
        else if (g instanceof RingImplUnsafe) {
            this.addRing((RingImplUnsafe) g);
        } else if (g instanceof RingImpl) {
            this.addRing((RingImpl) g);
        } else if (g instanceof SurfaceImpl) this.addSurface((SurfaceImpl) g);
        else if (g instanceof SurfaceBoundaryImpl) this.addSurfaceBoundary((SurfaceBoundaryImpl) g);
        else if (g instanceof CompositePointImpl) this.addCompositePoint((CompositePointImpl) g);
        else if (g instanceof CompositeCurveImpl) this.addCompositeCurve((CompositeCurveImpl) g);
        else if (g instanceof CompositeSurfaceImpl)
            this.addCompositeSurface((CompositeSurfaceImpl) g);
        else if (g instanceof MultiPrimitiveImpl) this.addMultiPrimitive((MultiPrimitiveImpl) g);
        else throw new UnsupportedOperationException(g.getClass().getName());
    }

    //	/**
    //	 * Add a list of geometry objects to the geometry graph
    //	 *
    //	 * @param aList Geometry list
    //	 */
    //	private void addGeometryList(List aList) {
    //		this.addGeometryIterator(aList.iterator());
    //	}

    /**
     * Add a set of geometry objects to the geometry graph
     *
     * @param aSet Geometry list
     */
    private void addGeometrySet(Set aSet) {
        Iterator aIter = aSet.iterator();
        while (aIter.hasNext()) {
            this.add((GeometryImpl) aIter.next());
        }
    }

    //	/**
    //	 * Add a iterator of geometry objects to the geometry graph
    //	 *
    //	 * @param aIter Geometry Iterator
    //	 */
    //	private void addGeometryIterator(Iterator aIter) {
    //		while (aIter.hasNext()) {
    //			this.add((GeometryImpl) aIter.next());
    //		}
    //	}

    /**
     * Adds a MultiPrimitive to the graph by adding all contained Primitives to the graph
     * individually
     */
    private void addMultiPrimitive(MultiPrimitiveImpl aMultiPrimitive) {
        // Add all Primitives within the MultiPrimitive
        this.addGeometrySet(aMultiPrimitive.getElements());
    }

    /** Add a Point to the graph. */
    private void addPoint(PointImpl p) {
        Coordinate coord = new Coordinate(p.getDirectPosition().getCoordinate());
        this.insertPoint(argIndex, coord, Location.INTERIOR);
    }

    /** Adds a ring in clockwise orientation */
    private void addRing(Ring aRing) {
        this.addRing(aRing, Location.EXTERIOR, Location.INTERIOR);
    }

    /**
     * Adds a Ring to the graph. The direction of the ring, i.e. whether it´s clockwise or counter
     * clockwise oriented, will be calculated and considered. Thus, the direction of the ring does
     * not matter. The left and right topological location arguments assume that the ring is
     * oriented CW. If the ring is in the opposite orientation, the left and right locations must be
     * interchanged.
     *
     * @param aRing A Ring. The direction of the ring (cw or ccw) does not matter
     * @param cwLeft Label for the left side of the ring
     * @param cwRight Label for the right side of the ring
     */
    private void addRing(Ring aRing, int cwLeft, int cwRight) {

        List<DirectPosition> tDPList = ((RingImplUnsafe) aRing).asDirectPositions();
        Coordinate[] coord = CoordinateArrays.toCoordinateArray(tDPList);

        // Remove neighboured identical points
        coord = CoordinateArrays.removeRepeatedPoints(coord);

        if (coord.length < 3) {
            hasTooFewPoints = true;
            invalidPoint = coord[0];
            return;
        }

        int left = cwLeft;
        int right = cwRight;

        // If Ring is counter clockwise oriented interchange the left and right
        // side for labelling
        if (cga.isCCW(coord)) {
            left = cwRight;
            right = cwLeft;
        }
        Edge e = new Edge(coord, new Label(argIndex, Location.BOUNDARY, left, right));
        lineEdgeMap.put(aRing, e);

        insertEdge(e);
        // insert the endpoint as a node, to mark that it is on the boundary
        insertPoint(argIndex, coord[0], Location.BOUNDARY);
    }

    /** Adds a Surface to the Geometry graph */
    private void addSurface(SurfaceImpl aSurface) {
        this.addSurfaceBoundary(aSurface.getBoundary());
    }

    /** Adds a CurveBoundary, e.g. the start and end point by which it is defined */
    private void addCurveBoundary(CurveBoundaryImpl aCurveBoundary) {
        // Add start and end point which define the CurveBoundary
        this.addPoint(aCurveBoundary.getStartPoint());
        this.addPoint(aCurveBoundary.getEndPoint());
    }

    /** Adds a SurfaceBoundary to the Geometry graph */
    private void addSurfaceBoundary(SurfaceBoundaryImpl aSurfaceBoundary) {
        // Add exterior Ring of the Surface Boundary
        this.addRing(aSurfaceBoundary.getExterior(), Location.EXTERIOR, Location.INTERIOR);

        // Add interior Rings of the Surface Boundary
        Iterator<Ring> tInteriorIter = aSurfaceBoundary.getInteriors().iterator();

        while (tInteriorIter.hasNext()) {
            this.addRing(tInteriorIter.next(), Location.INTERIOR, Location.EXTERIOR);
        }
    }

    /** Add a Curve to the graph */
    private void addCurve(CurveImpl aCurve) {

        List<DirectPosition> directPositions = aCurve.asDirectPositions();
        Coordinate[] coordinateArray = CoordinateArrays.toCoordinateArray(directPositions);
        Coordinate[] coord = CoordinateArrays.removeRepeatedPoints(coordinateArray);

        if (coord.length < 2) {
            hasTooFewPoints = true;
            invalidPoint = coord[0];
            return;
        }

        // add the edge for the LineString
        // line edges do not have locations for their left and right sides
        Edge e = new Edge(coord, new Label(argIndex, Location.INTERIOR));
        lineEdgeMap.put(aCurve, e);
        insertEdge(e);
        /**
         * Add the boundary points of the LineString, if any. Even if the LineString is closed, add
         * both points as if they were endpoints. This allows for the case that the node already
         * exists and is a boundary point.
         */
        Assert.isTrue(coord.length >= 2, "found LineString with single point");
        insertBoundaryPoint(argIndex, coord[0]);
        insertBoundaryPoint(argIndex, coord[coord.length - 1]);
    }

    /** Adds the Point of a CompositePoint to the Graph. Handling as usual Point. */
    private void addCompositePoint(CompositePointImpl aCompositePoint) {
        List<Primitive> elements = (List<Primitive>) aCompositePoint.getElements();
        if (elements.size() != 1)
            throw new IllegalArgumentException("CompositePoint has to hold exactly one Point");
        // Use addPoint-method
        this.addPoint((PointImpl) elements.get(0));
    }

    /** Adds a CompositeCurve to the graph by adding each Curve separately */
    private void addCompositeCurve(CompositeCurveImpl aCompositeCurve) {
        Iterator<? extends Primitive> elements = aCompositeCurve.getElements().iterator();
        while (elements.hasNext()) {
            this.addCurve((CurveImpl) elements.next());
        }
    }

    /** Adds a CompositeSurface to the graph by adding each Surface separately */
    private void addCompositeSurface(CompositeSurfaceImpl aCompositeSurface) {
        Iterator<? extends Primitive> elements = aCompositeSurface.getElements().iterator();
        while (elements.hasNext()) {
            this.addSurface((SurfaceImpl) elements.next());
        }
    }

    /** Add an Edge computed externally. The label on the Edge is assumed to be correct. */
    public void addEdge(Edge e) {
        insertEdge(e);
        Coordinate[] coord = e.getCoordinates();
        // insert the endpoint as a node, to mark that it is on the boundary
        insertPoint(argIndex, coord[0], Location.BOUNDARY);
        insertPoint(argIndex, coord[coord.length - 1], Location.BOUNDARY);
    }

    /**
     * Add a point computed externally. The point is assumed to be a Point Geometry part, which has
     * a location of INTERIOR.
     */
    public void addPoint(Coordinate pt) {
        insertPoint(argIndex, pt, Location.INTERIOR);
    }

    /**
     * Compute self-nodes, taking advantage of the Geometry type to minimize the number of
     * intersection tests. (E.g. rings are not tested for self-intersection, since they are assumed
     * to be valid).
     *
     * @param li the LineIntersector to use
     * @param computeRingSelfNodes if <false>, intersection checks are optimized to not test rings
     *     for self-intersection
     * @return the SegmentIntersector used, containing information about the intersections found
     */
    public SegmentIntersector computeSelfNodes(LineIntersector li, boolean computeRingSelfNodes) {

        SegmentIntersector si = new SegmentIntersector(li, true, false);

        // get an instance for an edgeSetIntersector (here: MC Sweepline
        // Intersector)
        EdgeSetIntersector esi = this.createEdgeSetIntersector();

        // optimized test for Polygons and Rings
        if (!computeRingSelfNodes
                && (parentGeom instanceof Ring
                        || parentGeom instanceof Surface
                        || parentGeom instanceof MultiPrimitive)) {
            // TODO auskommentiert; checken!
            // && (parentGeom instanceof LinearRing
            // || parentGeom instanceof Polygon
            // || parentGeom instanceof MultiPolygon)
            // Compute Intersections without self-intersections
            esi.computeIntersections(edges, si, false);
        } else {
            // Compute Intersections with self-intersections
            esi.computeIntersections(edges, si, true);
        }

        // System.out.println("SegmentIntersector # tests = " + si.numTests);
        addSelfIntersectionNodes(argIndex);
        return si;
    }

    /** */
    public SegmentIntersector computeEdgeIntersections(
            GeometryGraph g, LineIntersector li, boolean includeProper) {
        SegmentIntersector si = new SegmentIntersector(li, includeProper, true);
        si.setBoundaryNodes(this.getBoundaryNodes(), g.getBoundaryNodes());

        EdgeSetIntersector esi = createEdgeSetIntersector();
        esi.computeIntersections(edges, g.edges, si);
        /*
         * for (Iterator i = g.edges.iterator(); i.hasNext();) { Edge e = (Edge)
         * i.next(); Debug.print(e.getEdgeIntersectionList()); }
         */
        return si;
    }

    /**
     * Inserts a single point into the graph
     *
     * @param argIndex Index of the geometry in relation to all input geometries (starts with 0, 1,
     *     ...)
     * @param coord Coordinate which represents the point
     * @param onLocation Initial Location of the point
     */
    private void insertPoint(int argIndex, Coordinate coord, int onLocation) {
        Node n = nodes.addNode(coord);
        Label lbl = n.getLabel();
        if (lbl == null) {
            n.label = new Label(argIndex, onLocation);
        } else lbl.setLocation(argIndex, onLocation);
    }

    /**
     * Adds points using the mod-2 rule of SFS. This is used to add the boundary points of dim-1
     * geometries (Curves/MultiCurves). According to the SFS, an endpoint of a Curve is on the
     * boundary iff if it is in the boundaries of an odd number of Geometries
     */
    // lbl.setLocation might actually NPE, just ignoring it as the module is generally broken
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH")
    private void insertBoundaryPoint(int argIndex, Coordinate coord) {
        Node n = nodes.addNode(coord);
        Label lbl = n.getLabel();
        // the new point to insert is on a boundary
        int boundaryCount = 1;
        // determine the current location for the point (if any)
        int loc = Location.NONE;
        if (lbl != null) loc = lbl.getLocation(argIndex, Position.ON);
        if (loc == Location.BOUNDARY) boundaryCount++;

        // determine the boundary status of the point according to the Boundary
        // Determination Rule
        int newLoc = determineBoundary(boundaryCount);
        lbl.setLocation(argIndex, newLoc);
    }

    private void addSelfIntersectionNodes(int argIndex) {
        for (Iterator i = edges.iterator(); i.hasNext(); ) {
            Edge e = (Edge) i.next();
            int eLoc = e.getLabel().getLocation(argIndex);
            for (Iterator eiIt = e.eiList.iterator(); eiIt.hasNext(); ) {
                EdgeIntersection ei = (EdgeIntersection) eiIt.next();
                addSelfIntersectionNode(argIndex, ei.coord, eLoc);
            }
        }
    }

    /**
     * Add a node for a self-intersection. If the node is a potential boundary node (e.g. came from
     * an edge which is a boundary) then insert it as a potential boundary node. Otherwise, just add
     * it as a regular node.
     */
    private void addSelfIntersectionNode(int argIndex, Coordinate coord, int loc) {
        // if this node is already a boundary node, don't change it
        if (isBoundaryNode(argIndex, coord)) return;
        if (loc == Location.BOUNDARY && useBoundaryDeterminationRule)
            insertBoundaryPoint(argIndex, coord);
        else insertPoint(argIndex, coord, loc);
    }
}
