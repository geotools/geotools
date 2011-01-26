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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geotools.geometry.iso.coordinate.EnvelopeImpl;
import org.geotools.geometry.iso.primitive.RingImpl;
import org.geotools.geometry.iso.primitive.RingImplUnsafe;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.DirectedEdge;
import org.geotools.geometry.iso.topograph2D.EdgeRing;
import org.geotools.geometry.iso.topograph2D.Envelope;
import org.geotools.geometry.iso.topograph2D.PlanarGraph;
import org.geotools.geometry.iso.topograph2D.util.CoordinateArrays;
import org.geotools.geometry.iso.util.Assert;
import org.geotools.geometry.iso.util.algorithm2D.CGAlgorithms;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Forms {@link Surface}s out of a graph of {@link DirectedEdge}s. The edges
 * to use are marked as being in the result Area.
 * <p>
 *
 * @source $URL$
 */
public class PolygonBuilder {
    final static int X = 0;
    final static int Y = 1;
    final static int Z = 2;
    
	//private FeatGeomFactoryImpl featGeomyFactory;
    private CoordinateReferenceSystem crs;

	private CGAlgorithms cga;

	// private List dirEdgeList;
	// private NodeMap nodes;
	private List shellList = new ArrayList();

	public PolygonBuilder(CoordinateReferenceSystem crs, CGAlgorithms cga) {
		this.crs = crs;
		this.cga = cga;
	}

	/**
	 * Add a complete graph. The graph is assumed to contain one or more
	 * polygons, possibly with holes.
	 */
	public void add(PlanarGraph graph) {
		add(graph.getEdgeEnds(), graph.getNodes());
	}

	/**
	 * Add a set of edges and nodes, which form a graph. The graph is assumed to
	 * contain one or more polygons, possibly with holes.
	 */
	public void add(Collection dirEdges, Collection nodes) {
		PlanarGraph.linkResultDirectedEdges(nodes);
		List maxEdgeRings = this.buildMaximalEdgeRings(dirEdges);
		List freeHoleList = new ArrayList();
		List edgeRings = this.buildMinimalEdgeRings(maxEdgeRings, shellList,
				freeHoleList);
		this.sortShellsAndHoles(edgeRings, shellList, freeHoleList);
		this.placeFreeHoles(shellList, freeHoleList);
		// Assert: every hole on freeHoleList has a shell assigned to it
	}

	/**
	 * 
	 * @return
	 */
	public List getPolygons() {
		List resultPolyList = this.computeSurfaces(shellList);
		return resultPolyList;
	}

	/**
	 * for all DirectedEdges in result, form them into MaximalEdgeRings
	 */
	private List buildMaximalEdgeRings(Collection dirEdges) {
		List maxEdgeRings = new ArrayList();
		for (Iterator it = dirEdges.iterator(); it.hasNext();) {
			DirectedEdge de = (DirectedEdge) it.next();
			if (de.isInResult() && de.getLabel().isArea()) {
				// if this edge has not yet been processed
				if (de.getEdgeRing() == null) {
					MaximalEdgeRing er = new MaximalEdgeRing(de,
							crs, cga);
					maxEdgeRings.add(er);
					er.setInResult();
					// System.out.println("max node degree = " +
					// er.getMaxDegree());
				}
			}
		}
		return maxEdgeRings;
	}

	private List buildMinimalEdgeRings(List maxEdgeRings, List shellList,
			List freeHoleList) {
		List edgeRings = new ArrayList();
		for (Iterator it = maxEdgeRings.iterator(); it.hasNext();) {
			MaximalEdgeRing er = (MaximalEdgeRing) it.next();
			if (er.getMaxNodeDegree() > 2) {
				er.linkDirectedEdgesForMinimalEdgeRings();
				List minEdgeRings = er.buildMinimalRings();
				// at this point we can go ahead and attempt to place holes, if
				// this EdgeRing is a polygon
				EdgeRing shell = findShell(minEdgeRings);
				if (shell != null) {
					placePolygonHoles(shell, minEdgeRings);
					shellList.add(shell);
				} else {
					freeHoleList.addAll(minEdgeRings);
				}
			} else {
				edgeRings.add(er);
			}
		}
		return edgeRings;
	}

	/**
	 * This method takes a list of MinimalEdgeRings derived from a
	 * MaximalEdgeRing, and tests whether they form a Polygon. This is the case
	 * if there is a single shell in the list. In this case the shell is
	 * returned. The other possibility is that they are a series of connected
	 * holes, in which case no shell is returned.
	 * 
	 * @return the shell EdgeRing, if there is one
	 * @return null, if all the rings are holes
	 */
	private EdgeRing findShell(List minEdgeRings) {
		int shellCount = 0;
		EdgeRing shell = null;
		for (Iterator it = minEdgeRings.iterator(); it.hasNext();) {
			EdgeRing er = (MinimalEdgeRing) it.next();
			if (!er.isHole()) {
				shell = er;
				shellCount++;
			}
		}
		Assert.isTrue(shellCount <= 1,
				"found two shells in MinimalEdgeRing list");
		return shell;
	}

	/**
	 * This method assigns the holes for a Polygon (formed from a list of
	 * MinimalEdgeRings) to its shell. Determining the holes for a
	 * MinimalEdgeRing polygon serves two purposes:
	 * <ul>
	 * <li>it is faster than using a point-in-polygon check later on.
	 * <li>it ensures correctness, since if the PIP test was used the point
	 * chosen might lie on the shell, which might return an incorrect result
	 * from the PIP test
	 * </ul>
	 */
	private void placePolygonHoles(EdgeRing shell, List minEdgeRings) {
		for (Iterator it = minEdgeRings.iterator(); it.hasNext();) {
			MinimalEdgeRing er = (MinimalEdgeRing) it.next();
			if (er.isHole()) {
				er.setShell(shell);
			}
		}
	}

	/**
	 * For all rings in the input list, determine whether the ring is a shell or
	 * a hole and add it to the appropriate list. Due to the way the
	 * DirectedEdges were linked, a ring is a shell if it is oriented CW, a hole
	 * otherwise.
	 */
	private void sortShellsAndHoles(List edgeRings, List shellList,
			List freeHoleList) {
		for (Iterator it = edgeRings.iterator(); it.hasNext();) {
			EdgeRing er = (EdgeRing) it.next();
			// er.setInResult();
			if (er.isHole()) {
				freeHoleList.add(er);
			} else {
				shellList.add(er);
			}
		}
	}

	/**
	 * This method determines finds a containing shell for all holes which have
	 * not yet been assigned to a shell. These "free" holes should all be
	 * <b>properly</b> contained in their parent shells, so it is safe to use
	 * the <code>findEdgeRingContaining</code> method. (This is the case
	 * because any holes which are NOT properly contained (i.e. are connected to
	 * their parent shell) would have formed part of a MaximalEdgeRing and been
	 * handled in a previous step).
	 */
	private void placeFreeHoles(List shellList, List freeHoleList) {
		for (Iterator it = freeHoleList.iterator(); it.hasNext();) {
			EdgeRing hole = (EdgeRing) it.next();
			// only place this hole if it doesn't yet have a shell
			if (hole.getShell() == null) {
				EdgeRing shell = findEdgeRingContaining(hole, shellList);
				Assert
						.isTrue(shell != null,
								"unable to assign hole to a shell");
				hole.setShell(shell);
			}
		}
	}

	/**
	 * Find the innermost enclosing shell EdgeRing containing the argument
	 * EdgeRing, if any. The innermost enclosing ring is the <i>smallest</i>
	 * enclosing ring. The algorithm used depends on the fact that: <br>
	 * ring A contains ring B if envelope(ring A) contains envelope(ring B)
	 * <br>
	 * This routine is only safe to use if the chosen point of the hole is known
	 * to be properly contained in a shell (which is guaranteed to be the case
	 * if the hole does not touch its shell)
	 * 
	 * @return containing EdgeRing, if there is one
	 * @return null if no containing EdgeRing is found
	 */
	private EdgeRing findEdgeRingContaining(EdgeRing testEr, List shellList) {

		Ring testRing = testEr.getRing();
		org.geotools.geometry.iso.coordinate.EnvelopeImpl env = (EnvelopeImpl) testRing
				.getEnvelope();
		Envelope testEnv = new Envelope(env.getLowerCorner().getOrdinate(X), env
				.getUpperCorner().getOrdinate(X), env.getLowerCorner().getOrdinate(Y), env
				.getUpperCorner().getOrdinate(Y));
		// Take a point on the ring to do the point in ring test
		DirectPosition dp = testRing.getRepresentativePoint();
		Coordinate testPt = new Coordinate(dp.getCoordinates());

		EdgeRing minShell = null;
		Envelope minEnv = null;
		for (Iterator it = shellList.iterator(); it.hasNext();) {
			EdgeRing tryShell = (EdgeRing) it.next();
			Ring tryRing = tryShell.getRing();

			env = (EnvelopeImpl) tryRing.getEnvelope();
			Envelope tryEnv = new Envelope(env.getLowerCorner().getOrdinate(X), env
					.getUpperCorner().getOrdinate(X), env.getLowerCorner().getOrdinate(Y), env
					.getUpperCorner().getOrdinate(Y));

			if (minShell != null) {
				env = (EnvelopeImpl) minShell.getRing().getEnvelope();
				minEnv = new Envelope(env.getLowerCorner().getOrdinate(X), env
						.getUpperCorner().getOrdinate(X), env.getLowerCorner().getOrdinate(Y),
						env.getUpperCorner().getOrdinate(Y));
			}

			boolean isContained = false;
			if (tryEnv.contains(testEnv)
					&& CGAlgorithms.isPointInRing(testPt, CoordinateArrays
							.toCoordinateArray( (((RingImplUnsafe) tryRing).asDirectPositions()) ))) {
				isContained = true;
			}
			// check if this new containing ring is smaller than the current
			// minimum ring
			if (isContained) {
				if (minShell == null || minEnv.contains(tryEnv)) {
					minShell = tryShell;
				}
			}
		}
		return minShell;
	}

	private List computeSurfaces(List shellList) {
		List resultPolyList = new ArrayList();
		// add Polygons for all shells
		for (Iterator it = shellList.iterator(); it.hasNext();) {
			EdgeRing er = (EdgeRing) it.next();

			SurfaceImpl poly = er.toPolygon();
			resultPolyList.add(poly);
		}
		return resultPolyList;
	}

	/**
	 * Checks the current set of shells (with their associated holes) to see if
	 * any of them contain the point.
	 */
	public boolean containsPoint(Coordinate p) {
		for (Iterator it = shellList.iterator(); it.hasNext();) {
			EdgeRing er = (EdgeRing) it.next();
			if (er.containsPoint(p))
				return true;
		}
		return false;
	}

}
