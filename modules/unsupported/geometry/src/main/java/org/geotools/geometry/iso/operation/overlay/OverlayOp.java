/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 *    (C) 2001-2006  Vivid Solutions
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso.operation.overlay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.geotools.geometry.iso.UnsupportedDimensionException;
import org.geotools.geometry.iso.aggregate.AggregateFactoryImpl;
import org.geotools.geometry.iso.operation.GeometryGraphOperation;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.Depth;
import org.geotools.geometry.iso.topograph2D.DirectedEdge;
import org.geotools.geometry.iso.topograph2D.DirectedEdgeStar;
import org.geotools.geometry.iso.topograph2D.Edge;
import org.geotools.geometry.iso.topograph2D.EdgeList;
import org.geotools.geometry.iso.topograph2D.Label;
import org.geotools.geometry.iso.topograph2D.Location;
import org.geotools.geometry.iso.topograph2D.Node;
import org.geotools.geometry.iso.topograph2D.PlanarGraph;
import org.geotools.geometry.iso.topograph2D.Position;
import org.geotools.geometry.iso.util.Assert;
import org.geotools.geometry.iso.util.algorithm2D.PointLocator;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Computes the overlay of two {@link Geometry}s. The overlay can be used to
 * determine any boolean combination of the geometries.
 *
 * @source $URL$
 */
public class OverlayOp extends GeometryGraphOperation {

	/**
	 * The spatial functions supported by this class. These operations implement
	 * various boolean combinations of the resultants of the overlay.
	 */
	public static final int INTERSECTION = 1;

	public static final int UNION = 2;

	public static final int DIFFERENCE = 3;

	public static final int SYMDIFFERENCE = 4;

	/**
	 * 
	 * @param geom0
	 * @param geom1
	 * @param opCode
	 * @return
	 * @throws UnsupportedDimensionException
	 */
	public static GeometryImpl overlayOp(GeometryImpl geom0,
			GeometryImpl geom1, int opCode)
			throws UnsupportedDimensionException {
		OverlayOp gov = new OverlayOp(geom0, geom1);
		GeometryImpl geomOv = gov.getResultGeometry(opCode);
		return geomOv;
	}

	/**
	 * 
	 * @param label
	 * @param opCode
	 * @return
	 */
	public static boolean isResultOfOp(Label label, int opCode) {
		int loc0 = label.getLocation(0);
		int loc1 = label.getLocation(1);
		return isResultOfOp(loc0, loc1, opCode);
	}

	/**
	 * This method will handle arguments of Location.NONE correctly
	 * 
	 * @param loc0
	 * @param loc1
	 * @param opCode
	 * @return
	 */
	public static boolean isResultOfOp(int loc0, int loc1, int opCode) {
		if (loc0 == Location.BOUNDARY)
			loc0 = Location.INTERIOR;
		if (loc1 == Location.BOUNDARY)
			loc1 = Location.INTERIOR;
		switch (opCode) {
		case INTERSECTION:
			return loc0 == Location.INTERIOR && loc1 == Location.INTERIOR;
		case UNION:
			return loc0 == Location.INTERIOR || loc1 == Location.INTERIOR;
		case DIFFERENCE:
			return loc0 == Location.INTERIOR && loc1 != Location.INTERIOR;
		case SYMDIFFERENCE:
			return (loc0 == Location.INTERIOR && loc1 != Location.INTERIOR)
					|| (loc0 != Location.INTERIOR && loc1 == Location.INTERIOR);
		}
		return false;
	}

	private final PointLocator ptLocator = new PointLocator();

	//private FeatGeomFactoryImpl geomFeatFactory;
	private CoordinateReferenceSystem crs;

	private GeometryImpl resultGeom;

	private PlanarGraph graph;

	private EdgeList edgeList = new EdgeList();

	private List<OrientableSurface> resultPolyList = new ArrayList<OrientableSurface>();

	private List<OrientableCurve> resultLineList = new ArrayList<OrientableCurve>();

	private List<Point> resultPointList = new ArrayList<Point>();

	/**
	 * Initializes a new Overlay Operation between two geometric objects
	 * 
	 * @param g0 First geometric object
	 * @param g1 Second geometric object
	 * 
	 * @throws UnsupportedDimensionException
	 */
	public OverlayOp(GeometryImpl g0, GeometryImpl g1)
			throws UnsupportedDimensionException {
		super(g0, g1);
		graph = new PlanarGraph(new OverlayNodeFactory());
		/**
		 * Use factory of primary geometry. Note that this does NOT handle
		 * mixed-precision arguments where the second arg has greater precision
		 * than the first.
		 */
		//this.geomFeatFactory = g0.getFeatGeometryFactory();
		this.crs = g0.getCoordinateReferenceSystem();
	}

	/**
	 * Computes and returns the resulting geometry according
	 * to the function code parameter.
	 * 
	 * @param funcCode Function code
	 * @return Result geometry
	 */
	public GeometryImpl getResultGeometry(int funcCode) {
		computeOverlay(funcCode);
		return resultGeom;
	}

	/**
	 * 
	 * @return
	 */
	public PlanarGraph getGraph() {
		return graph;
	}

	/**
	 * Compute the overlay according to the given operation code parameter
	 * 
	 * @param opCode Operation code
	 */
	private void computeOverlay(int opCode) {

		// copy points from input Geometries.
		// This ensures that any Point geometries
		// in the input are considered for inclusion in the result set
		this.copyPoints(0);
		this.copyPoints(1);

		// node the input Geometries
		this.arg[0].computeSelfNodes(li, false);
		this.arg[1].computeSelfNodes(li, false);

		// compute intersections between edges of the two input geometries
		this.arg[0].computeEdgeIntersections(arg[1], li, true);

		List baseSplitEdges = new ArrayList();
		this.arg[0].computeSplitEdges(baseSplitEdges);
		this.arg[1].computeSplitEdges(baseSplitEdges);

		// add the noded edges to this result graph
		insertUniqueEdges(baseSplitEdges);

		// Labels the Edges
		this.computeLabelsFromDepths();

		this.replaceCollapsedEdges();

		// debugging only
		// NodingValidator nv = new NodingValidator(edgeList.getEdges());
		// nv.checkValid();

		this.graph.addEdges(this.edgeList.getEdges());
		this.computeLabelling();

		this.labelIncompleteNodes();

		/**
		 * The ordering of building the result Geometries is important. Areas
		 * must be built before lines, which must be built before points. This
		 * is so that lines which are covered by areas are not included
		 * explicitly, and similarly for points.
		 */

		findResultAreaEdges(opCode);
		cancelDuplicateResultEdges();
		PolygonBuilder polyBuilder = new PolygonBuilder(crs, cga);
		polyBuilder.add(this.graph);

		this.resultPolyList = polyBuilder.getPolygons();

		LineBuilder lineBuilder = new LineBuilder(this, crs,
				ptLocator);
		this.resultLineList = lineBuilder.build(opCode);

		PointBuilder pointBuilder = new PointBuilder(this, crs,
				ptLocator);
		this.resultPointList = pointBuilder.build(opCode);

		// gather the results from all calculations into a single Geometry for
		// the result set
		this.resultGeom = this.computeGeometry(resultPointList, resultLineList,
				resultPolyList);
	}

	/**
	 * 
	 * @param edges
	 */
	private void insertUniqueEdges(List edges) {
		for (Iterator i = edges.iterator(); i.hasNext();) {
			Edge e = (Edge) i.next();
			this.insertUniqueEdge(e);
		}
	}

	/**
	 * Insert an edge from one of the noded input graphs. Checks edges that are
	 * inserted to see if an identical edge already exists. If so, the edge is
	 * not inserted, but its label is merged with the existing edge.
	 */
	protected void insertUniqueEdge(Edge e) {
		// <FIX> MD 8 Oct 03 speed up identical edge lookup
		// fast lookup
		Edge existingEdge = edgeList.findEqualEdge(e);

		// If an identical edge already exists, simply update its label
		if (existingEdge != null) {
			Label existingLabel = existingEdge.getLabel();

			Label labelToMerge = e.getLabel();
			// check if new edge is in reverse direction to existing edge
			// if so, must flip the label before merging it
			if (!existingEdge.isPointwiseEqual(e)) {
				labelToMerge = new Label(e.getLabel());
				labelToMerge.flip();
			}
			Depth depth = existingEdge.getDepth();
			// if this is the first duplicate found for this edge, initialize
			// the depths
			// /*
			if (depth.isNull()) {
				depth.add(existingLabel);
			}
			// */
			depth.add(labelToMerge);
			existingLabel.merge(labelToMerge);
			// Debug.print("inserted edge: "); Debug.println(e);
			// Debug.print("existing edge: "); Debug.println(existingEdge);

		} else { // no matching existing edge was found
			// add this new edge to the list of edges in this graph
			// e.setName(name + edges.size());
			// e.getDepth().add(e.getLabel());
			edgeList.add(e);
		}
	}

	/**
	 * If either of the GeometryLocations for the existing label is exactly
	 * opposite to the one in the labelToMerge, this indicates a dimensional
	 * collapse has happened. In this case, convert the label for that Geometry
	 * to a Line label
	 */
	/*
	 * NOT NEEDED? private void checkDimensionalCollapse(Label labelToMerge,
	 * Label existingLabel) { if (existingLabel.isArea() &&
	 * labelToMerge.isArea()) { for (int i = 0; i < 2; i++) { if (!
	 * labelToMerge.isNull(i) && labelToMerge.getLocation(i, Position.LEFT) ==
	 * existingLabel.getLocation(i, Position.RIGHT) &&
	 * labelToMerge.getLocation(i, Position.RIGHT) ==
	 * existingLabel.getLocation(i, Position.LEFT) ) { existingLabel.toLine(i); } } } }
	 */
	/**
	 * Update the labels for edges according to their depths. For each edge, the
	 * depths are first normalized. Then, if the depths for the edge are equal,
	 * this edge must have collapsed into a line edge. If the depths are not
	 * equal, update the label with the locations corresponding to the depths
	 * (i.e. a depth of 0 corresponds to a Location of EXTERIOR, a depth of 1
	 * corresponds to INTERIOR)
	 */
	private void computeLabelsFromDepths() {
		for (Iterator it = edgeList.iterator(); it.hasNext();) {
			Edge e = (Edge) it.next();
			Label lbl = e.getLabel();
			Depth depth = e.getDepth();
			/**
			 * Only check edges for which there were duplicates, since these are
			 * the only ones which might be the result of dimensional collapses.
			 */
			if (!depth.isNull()) {
				depth.normalize();
				for (int i = 0; i < 2; i++) {
					if (!lbl.isNull(i) && lbl.isArea() && !depth.isNull(i)) {
						/**
						 * if the depths are equal, this edge is the result of
						 * the dimensional collapse of two or more edges. It has
						 * the same location on both sides of the edge, so it
						 * has collapsed to a line.
						 */
						if (depth.getDelta(i) == 0) {
							lbl.toLine(i);
						} else {
							/**
							 * This edge may be the result of a dimensional
							 * collapse, but it still has different locations on
							 * both sides. The label of the edge must be updated
							 * to reflect the resultant side locations indicated
							 * by the depth values.
							 */
							Assert
									.isTrue(!depth.isNull(i, Position.LEFT),
											"depth of LEFT side has not been initialized");
							lbl.setLocation(i, Position.LEFT, depth
									.getLocation(i, Position.LEFT));
							Assert
									.isTrue(!depth.isNull(i, Position.RIGHT),
											"depth of RIGHT side has not been initialized");
							lbl.setLocation(i, Position.RIGHT, depth
									.getLocation(i, Position.RIGHT));
						}
					}
				}
			}
		}
	}

	/**
	 * If edges which have undergone dimensional collapse are found, replace
	 * them with a new edge which is a L edge
	 */
	private void replaceCollapsedEdges() {
		List newEdges = new ArrayList();
		for (Iterator it = edgeList.iterator(); it.hasNext();) {
			Edge e = (Edge) it.next();
			if (e.isCollapsed()) {
				// Debug.print(e);
				it.remove();
				newEdges.add(e.getCollapsedEdge());
			}
		}
		edgeList.addAll(newEdges);
	}

	/**
	 * Copy all nodes from an arg geometry into this graph. The node label in
	 * the arg geometry overrides any previously computed label for that
	 * argIndex. (E.g. a node may be an intersection node with a previously
	 * computed label of BOUNDARY, but in the original arg Geometry it is
	 * actually in the interior due to the Boundary Determination Rule)
	 */
	private void copyPoints(int argIndex) {
		for (Iterator i = arg[argIndex].getNodeIterator(); i.hasNext();) {
			Node graphNode = (Node) i.next();
			Node newNode = graph.addNode(graphNode.getCoordinate());
			newNode.setLabel(argIndex, graphNode.getLabel().getLocation(
					argIndex));
		}
	}

	/**
	 * Compute initial labelling for all DirectedEdges at each node. In this
	 * step, DirectedEdges will acquire a complete labelling (i.e. one with
	 * labels for both Geometries) only if they are incident on a node which has
	 * edges for both Geometries
	 */
	private void computeLabelling() {
		for (Iterator nodeit = graph.getNodes().iterator(); nodeit.hasNext();) {
			Node node = (Node) nodeit.next();
			// if (node.getCoordinate().equals(new Coordinate(222, 100)) )
			// Debug.addWatch(node.getEdges());
			node.getEdges().computeLabelling(arg);
		}
		mergeSymLabels();
		updateNodeLabelling();
	}

	/**
	 * For nodes which have edges from only one Geometry incident on them, the
	 * previous step will have left their dirEdges with no labelling for the
	 * other Geometry. However, the sym dirEdge may have a labelling for the
	 * other Geometry, so merge the two labels.
	 */
	private void mergeSymLabels() {
		for (Iterator nodeit = graph.getNodes().iterator(); nodeit.hasNext();) {
			Node node = (Node) nodeit.next();
			((DirectedEdgeStar) node.getEdges()).mergeSymLabels();
			// node.print(System.out);
		}
	}

	private void updateNodeLabelling() {
		// update the labels for nodes
		// The label for a node is updated from the edges incident on it
		// (Note that a node may have already been labelled
		// because it is a point in one of the input geometries)
		for (Iterator nodeit = graph.getNodes().iterator(); nodeit.hasNext();) {
			Node node = (Node) nodeit.next();
			Label lbl = ((DirectedEdgeStar) node.getEdges()).getLabel();
			node.getLabel().merge(lbl);
		}
	}

	/**
	 * Incomplete nodes are nodes whose labels are incomplete. (e.g. the
	 * location for one Geometry is null). These are either isolated nodes, or
	 * nodes which have edges from only a single Geometry incident on them.
	 * 
	 * Isolated nodes are found because nodes in one graph which don't intersect
	 * nodes in the other are not completely labelled by the initial process of
	 * adding nodes to the nodeList. To complete the labelling we need to check
	 * for nodes that lie in the interior of edges, and in the interior of
	 * areas.
	 * <p>
	 * When each node labelling is completed, the labelling of the incident
	 * edges is updated, to complete their labelling as well.
	 */
	private void labelIncompleteNodes() {
		for (Iterator ni = graph.getNodes().iterator(); ni.hasNext();) {
			Node n = (Node) ni.next();
			Label label = n.getLabel();
			if (n.isIsolated()) {
				if (label.isNull(0))
					labelIncompleteNode(n, 0);
				else
					labelIncompleteNode(n, 1);
			}
			// now update the labelling for the DirectedEdges incident on this
			// node
			((DirectedEdgeStar) n.getEdges()).updateLabelling(label);
			// n.print(System.out);
		}
	}

	/**
	 * Label an isolated node with its relationship to the target geometry.
	 */
	private void labelIncompleteNode(Node n, int targetIndex) {
		int loc = ptLocator.locate(n.getCoordinate(), arg[targetIndex]
				.getGeometry());
		n.getLabel().setLocation(targetIndex, loc);
	}

	/**
	 * Find all edges whose label indicates that they are in the result area(s),
	 * according to the operation being performed. Since we want polygon shells
	 * to be oriented CW, choose dirEdges with the interior of the result on the
	 * RHS. Mark them as being in the result. Interior Area edges are the result
	 * of dimensional collapses. They do not form part of the result area
	 * boundary.
	 */
	private void findResultAreaEdges(int opCode) {
		for (Iterator it = graph.getEdgeEnds().iterator(); it.hasNext();) {
			DirectedEdge de = (DirectedEdge) it.next();
			// mark all dirEdges with the appropriate label
			Label label = de.getLabel();
			if (label.isArea()
					&& !de.isInteriorAreaEdge()
					&& isResultOfOp(label.getLocation(0, Position.RIGHT), label
							.getLocation(1, Position.RIGHT), opCode)) {
				de.setInResult(true);
				// Debug.print("in result "); Debug.println(de);
			}
		}
	}

	/**
	 * If both a dirEdge and its sym are marked as being in the result, cancel
	 * them out.
	 */
	private void cancelDuplicateResultEdges() {
		// remove any dirEdges whose sym is also included
		// (they "cancel each other out")
		for (Iterator it = graph.getEdgeEnds().iterator(); it.hasNext();) {
			DirectedEdge de = (DirectedEdge) it.next();
			DirectedEdge sym = de.getSym();
			if (de.isInResult() && sym.isInResult()) {
				de.setInResult(false);
				sym.setInResult(false);
				// Debug.print("cancelled "); Debug.println(de);
				// Debug.println(sym);
			}
		}
	}

	/**
	 * This method is used to decide if a point node should be included in the
	 * result or not.
	 * 
	 * @return true if the coord point is covered by a result Line or Area
	 *         geometry
	 */
	public boolean isCoveredByLA(Coordinate coord) {
		if (isCovered(coord, this.resultLineList))
			return true;
		if (isCovered(coord, this.resultPolyList))
			return true;
		return false;
	}

	/**
	 * This method is used to decide if an L edge should be included in the
	 * result or not.
	 * 
	 * @return true if the coord point is covered by a result Area geometry
	 */
	public boolean isCoveredByA(Coordinate coord) {
		if (isCovered(coord, this.resultPolyList))
			return true;
		return false;
	}

	/**
	 * @return true if the coord is located in the interior or boundary of a
	 *         geometry in the list.
	 */
	private boolean isCovered(Coordinate coord, List geomList) {
		for (Iterator it = geomList.iterator(); it.hasNext();) {
			GeometryImpl geom = (GeometryImpl) it.next();
			int loc = this.ptLocator.locate(coord, geom);
			if (loc != Location.EXTERIOR)
				return true;
		}
		return false;
	}

	private GeometryImpl computeGeometry(List<Point> resultPointList,
			List<OrientableCurve> resultLineList, List<OrientableSurface> resultPolyList) {

		List geomList = new ArrayList();

		// element geometries of the result are always in the order P,L,A
//		geomList.addAll(resultPointList);
//		geomList.addAll(resultLineList);
//		geomList.addAll(resultPolyList);

		// build the most specific geometry possible
		//FeatGeomFactoryImpl gf = new FeatGeomFactoryImpl(crs);
		return createGeometry(
				resultPolyList, resultLineList, resultPointList);
	}

	/**
	 * Creates a new Geometry object appropriate to the input Primitives. The
	 * method will return a Primitive object, if one list contains only one
	 * element and the rest is empty. In all other cases, that is that exist
	 * more than one Primitive in the lists, the method will return a Complex
	 * object.
	 * 
	 * @param aSurfaces
	 *            List of Surfaces
	 * @param aCurves
	 *            List of Curves
	 * @param aPoints
	 *            List of Points
	 * @return a Geometry instance.
	 * That is a Point/Curve/Surface if the parameters only contain one point or one curve or one surface.
	 * It is a MultiPoint/MultiCurve/MultiSurface if the parameters contain one list with more than two entries and the other two lists are empty.
	 * Or it is a MultiPrimitive if the parameters contain a mixture of points, curves and surfaces.
	 */
	public GeometryImpl createGeometry(List<OrientableSurface> aSurfaces,
			List<OrientableCurve> aCurves, List<Point> aPoints) {

		int nS = aSurfaces.size();
		int nC = aCurves.size();
		int nP = aPoints.size();
		AggregateFactoryImpl aggregateFactory = new AggregateFactoryImpl(crs);
		
		if (nS + nC + nP == 0)
			// Return null if the sets are empty
			return null;
			//throw new IllegalArgumentException("All Sets are empty");

		
		if (nS == 0) {
			
			if (nC == 0) {
				
				// Surfaces empty, Curves empty, Points not empty
				if (nP == 1) {
					
					// POINT
					return (GeometryImpl) aPoints.get(0);
					
				} else {
					
					// MULTIPOINT
					return (GeometryImpl) aggregateFactory.createMultiPoint(new HashSet(aPoints));
					
				}
			} else if (nP == 0) {
				
				// Surfaces empty, Curves not empty, Points empty
				if (nC == 1) {
					
					// CURVE
					
					return (GeometryImpl) aCurves.get(0);
				} else {
					
					// MULTICURVE
					return (GeometryImpl) aggregateFactory.createMultiCurve(new HashSet(aCurves));
				}
			}

		} else {
			
			if (nC == 0 && nP == 0) {
				
				if (nS == 1) {
					
					// SURFACE
					return (GeometryImpl) aSurfaces.get(0);
					
				} else {
					
					// MULTISURFACE
					return (GeometryImpl) aggregateFactory.createMultiSurface(new HashSet(aSurfaces));
					
				}
				
			}

		}
		
		// All other cases: MULTIPRIMITIVE
		Set<Primitive> tPrimitives = new HashSet<Primitive>();
		tPrimitives.addAll(aSurfaces);
		tPrimitives.addAll(aCurves);
		tPrimitives.addAll(aPoints);
		
		return (GeometryImpl) aggregateFactory.createMultiPrimitive(tPrimitives);

	}	
	
}
