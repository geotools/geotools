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
package org.geotools.geometry.iso.operation.relate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.geotools.geometry.iso.coordinate.EnvelopeImpl;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.geotools.geometry.iso.topograph2D.Dimension;
import org.geotools.geometry.iso.topograph2D.Edge;
import org.geotools.geometry.iso.topograph2D.EdgeEnd;
import org.geotools.geometry.iso.topograph2D.EdgeIntersection;
import org.geotools.geometry.iso.topograph2D.GeometryGraph;
import org.geotools.geometry.iso.topograph2D.IntersectionMatrix;
import org.geotools.geometry.iso.topograph2D.Label;
import org.geotools.geometry.iso.topograph2D.Location;
import org.geotools.geometry.iso.topograph2D.Node;
import org.geotools.geometry.iso.topograph2D.NodeMap;
import org.geotools.geometry.iso.topograph2D.index.SegmentIntersector;
import org.geotools.geometry.iso.util.Assert;
import org.geotools.geometry.iso.util.algorithm2D.LineIntersector;
import org.geotools.geometry.iso.util.algorithm2D.PointLocator;
import org.geotools.geometry.iso.util.algorithm2D.RobustLineIntersector;

/**
 * Computes the topological relationship between two Geometries.
 *
 * <p>RelateComputer does not need to build a complete graph structure to compute the
 * IntersectionMatrix. The relationship between the geometries can be computed by simply examining
 * the labelling of edges incident on each node.
 *
 * <p>RelateComputer does not currently support arbitrary GeometryCollections. This is because
 * GeometryCollections can contain overlapping Polygons. In order to correct compute relate on
 * overlapping Polygons, they would first need to be noded and merged (if not explicitly, at least
 * implicitly).
 */
public class RelateComputer {

    private LineIntersector li = new RobustLineIntersector();

    private PointLocator ptLocator = new PointLocator();

    private GeometryGraph[] arg; // the arg(s) of the operation

    private NodeMap nodes = new NodeMap(new RelateNodeFactory());

    // this intersection matrix will hold the results compute for the relate
    // private IntersectionMatrix im = null;

    private ArrayList isolatedEdges = new ArrayList();

    // the intersection point found (if any)
    // private Coordinate invalidPoint;

    public RelateComputer(GeometryGraph[] arg) {
        this.arg = arg;
    }

    /**
     * Computes the Intersection Matrix for the two given geometry objects
     *
     * @return Intersection Matrix
     */
    public IntersectionMatrix computeIM() {

        IntersectionMatrix tIM = new IntersectionMatrix();

        // since Geometries are finite and embedded in a 2-D space, the EE
        // element must always be 2
        tIM.set(Location.EXTERIOR, Location.EXTERIOR, 2);

        // if the Geometries don't overlap there is nothing to do
        //		if (!arg[0].getGeometry().getEnvelopeInternal().intersects(
        //				arg[1].getGeometry().getEnvelopeInternal())) {
        EnvelopeImpl env1 = (EnvelopeImpl) arg[0].getGeometry().getEnvelope();
        EnvelopeImpl env2 = (EnvelopeImpl) arg[1].getGeometry().getEnvelope();
        if (!env1.intersects(env2)) {
            computeDisjointIM(tIM);
            return tIM;
        }
        this.arg[0].computeSelfNodes(this.li, false);
        this.arg[1].computeSelfNodes(this.li, false);

        // compute intersections between edges of the two input geometries
        SegmentIntersector tIntersector =
                this.arg[0].computeEdgeIntersections(this.arg[1], this.li, false);

        this.computeIntersectionNodes(0);
        this.computeIntersectionNodes(1);

        // Copy the labelling for the nodes in the parent Geometries. These
        // override any labels determined by intersections between the
        // geometries.
        this.copyNodesAndLabels(0);
        this.copyNodesAndLabels(1);

        // complete the labelling for any nodes which only have a label for a
        // single geometry
        this.labelIsolatedNodes();

        // If a proper intersection was found,
        // we can set a lower bound on the IM.
        this.computeProperIntersectionIM(tIntersector, tIM);

        // Now process improper intersections (e.g. where one or other of the
        // geometries has a vertex at the intersection point) We need to compute
        // the edge graph at all nodes to determine the IM.

        // build EdgeEnds for all intersections
        EdgeEndBuilder eeBuilder = new EdgeEndBuilder();
        List ee0 = eeBuilder.computeEdgeEnds(arg[0].getEdgeIterator());
        this.insertEdgeEnds(ee0);
        List ee1 = eeBuilder.computeEdgeEnds(arg[1].getEdgeIterator());
        this.insertEdgeEnds(ee1);

        this.labelNodeEdges();

        // Compute the labeling for isolated components <br>
        // Isolated components are components that do not touch any other
        // components in the graph. They can be identified by the fact that they
        // will contain labels containing ONLY a single element, the one for
        // their parent geometry. We only need to check components contained in
        // the input graphs, since isolated components will not have been
        // replaced by new components formed by intersections.

        this.labelIsolatedEdges(0, 1);
        this.labelIsolatedEdges(1, 0);

        // update the IM from all components
        this.updateIM(tIM);

        return tIM;
    }

    private void insertEdgeEnds(List ee) {
        for (Iterator i = ee.iterator(); i.hasNext(); ) {
            EdgeEnd e = (EdgeEnd) i.next();
            nodes.add(e);
        }
    }

    private void computeProperIntersectionIM(
            SegmentIntersector intersector, IntersectionMatrix im) {
        // If a proper intersection is found, we can set a lower bound on the
        // IM.
        //		int dimA = arg[0].getGeometry().getDimension();
        //		int dimB = arg[1].getGeometry().getDimension();
        int dimA = arg[0].getGeometry().getDimension(null);
        int dimB = arg[1].getGeometry().getDimension(null);
        boolean hasProper = intersector.hasProperIntersection();
        boolean hasProperInterior = intersector.hasProperInteriorIntersection();

        // For Geometry's of dim 0 there can never be proper intersections.

        /** If edge segments of Areas properly intersect, the areas must properly overlap. */
        if (dimA == 2 && dimB == 2) {
            if (hasProper) im.setAtLeast("212101212");
        }
        /**
         * If an Line segment properly intersects an edge segment of an Area, it follows that the
         * Interior of the Line intersects the Boundary of the Area. If the intersection is a proper
         * <i>interior</i> intersection, then there is an Interior-Interior intersection too. Note
         * that it does not follow that the Interior of the Line intersects the Exterior of the
         * Area, since there may be another Area component which contains the rest of the Line.
         */
        else if (dimA == 2 && dimB == 1) {
            if (hasProper) im.setAtLeast("FFF0FFFF2");
            if (hasProperInterior) im.setAtLeast("1FFFFF1FF");
        } else if (dimA == 1 && dimB == 2) {
            if (hasProper) im.setAtLeast("F0FFFFFF2");
            if (hasProperInterior) im.setAtLeast("1F1FFFFFF");
        }
        /*
         * If edges of LineStrings properly intersect *in an interior point*,
         * all we can deduce is that the interiors intersect. (We can NOT deduce
         * that the exteriors intersect, since some other segments in the
         * geometries might cover the points in the neighbourhood of the
         * intersection.) It is important that the point be known to be an
         * interior point of both Geometries, since it is possible in a
         * self-intersecting geometry to have a proper intersection on one
         * segment that is also a boundary point of another segment.
         */
        else if (dimA == 1 && dimB == 1) {
            if (hasProperInterior) im.setAtLeast("0FFFFFFFF");
        }
    }

    /**
     * Copy all nodes from an arg geometry into this graph. The node label in the arg geometry
     * overrides any previously computed label for that argIndex. (E.g. a node may be an
     * intersection node with a computed label of BOUNDARY, but in the original arg Geometry it is
     * actually in the interior due to the Boundary Determination Rule)
     */
    private void copyNodesAndLabels(int argIndex) {
        for (Iterator i = arg[argIndex].getNodeIterator(); i.hasNext(); ) {
            Node graphNode = (Node) i.next();
            Node newNode = nodes.addNode(graphNode.getCoordinate());
            newNode.setLabel(argIndex, graphNode.getLabel().getLocation(argIndex));
        }
    }

    /**
     * Insert nodes for all intersections on the edges of a Geometry. Label the created nodes the
     * same as the edge label if they do not already have a label. This allows nodes created by
     * either self-intersections or mutual intersections to be labelled. Endpoint nodes will already
     * be labelled from when they were inserted.
     */
    private void computeIntersectionNodes(int argIndex) {
        for (Iterator i = this.arg[argIndex].getEdgeIterator(); i.hasNext(); ) {
            Edge e = (Edge) i.next();
            int eLoc = e.getLabel().getLocation(argIndex);
            for (Iterator eiIt = e.getEdgeIntersectionList().iterator(); eiIt.hasNext(); ) {
                EdgeIntersection ei = (EdgeIntersection) eiIt.next();
                RelateNode n = (RelateNode) this.nodes.addNode(ei.coord);
                if (eLoc == Location.BOUNDARY) n.setLabelBoundary(argIndex);
                else {
                    if (n.getLabel().isNull(argIndex)) n.setLabel(argIndex, Location.INTERIOR);
                }
            }
        }
    }

    /**
     * If the Geometries are disjoint, we need to enter their dimension and boundary dimension in
     * the EXTERIOR rows in the IM
     */
    private void computeDisjointIM(IntersectionMatrix im) {
        GeometryImpl ga = arg[0].getGeometry();
        //		if (!ga.isEmpty()) {
        //			im.set(Location.INTERIOR, Location.EXTERIOR, ga.getDimension());
        //			im.set(Location.BOUNDARY, Location.EXTERIOR, ga
        //					.getBoundaryDimension());
        //		}
        im.set(Location.INTERIOR, Location.EXTERIOR, ga.getDimension(null));
        if (ga.getBoundary() == null) {
            im.set(Location.BOUNDARY, Location.EXTERIOR, Dimension.FALSE);
        } else {
            im.set(Location.BOUNDARY, Location.EXTERIOR, ga.getBoundary().getDimension(null));
        }

        GeometryImpl gb = arg[1].getGeometry();
        //		if (!gb.isEmpty()) {
        //			im.set(Location.EXTERIOR, Location.INTERIOR, gb.getDimension());
        //			im.set(Location.EXTERIOR, Location.BOUNDARY, gb
        //					.getBoundaryDimension());
        //		}
        im.set(Location.EXTERIOR, Location.INTERIOR, gb.getDimension(null));
        if (gb.getBoundary() == null) {
            im.set(Location.EXTERIOR, Location.BOUNDARY, Dimension.FALSE);
        } else {
            im.set(Location.EXTERIOR, Location.BOUNDARY, gb.getBoundary().getDimension(null));
        }
    }

    private void labelNodeEdges() {
        for (Iterator ni = nodes.iterator(); ni.hasNext(); ) {
            RelateNode node = (RelateNode) ni.next();
            node.getEdges().computeLabelling(arg);
        }
    }

    /** Update the IM with the sum of the IMs for each component */
    private void updateIM(IntersectionMatrix im) {

        for (Iterator ei = isolatedEdges.iterator(); ei.hasNext(); ) {
            Edge e = (Edge) ei.next();
            e.updateIM(im);
        }

        for (Iterator ni = nodes.iterator(); ni.hasNext(); ) {
            RelateNode node = (RelateNode) ni.next();
            // System.out.println(node.getCoordinate());
            node.updateIM(im);
            node.updateIMFromEdges(im);
        }
    }

    /**
     * Processes isolated edges by computing their labelling and adding them to the isolated edges
     * list. Isolated edges are guaranteed not to touch the boundary of the target (since if they
     * did, they would have caused an intersection to be computed and hence would not be isolated)
     */
    private void labelIsolatedEdges(int thisIndex, int targetIndex) {
        for (Iterator ei = arg[thisIndex].getEdgeIterator(); ei.hasNext(); ) {
            Edge e = (Edge) ei.next();
            if (e.isIsolated()) {
                this.labelIsolatedEdge(e, targetIndex, arg[targetIndex].getGeometry());
                isolatedEdges.add(e);
            }
        }
    }

    /**
     * Label an isolated edge of a graph with its relationship to the target geometry. If the target
     * has dim 2 or 1, the edge can either be in the interior or the exterior. If the target has dim
     * 0, the edge must be in the exterior
     */
    private void labelIsolatedEdge(Edge e, int targetIndex, GeometryImpl target) {
        // this won't work for GeometryCollections with both dim 2 and 1 geoms
        //		if (target.getDimension() > 0) {
        if (target.getDimension(null) > 0) {
            // since edge is not in boundary, may not need the full generality
            // of PointLocator?
            // Possibly should use ptInArea locator instead? We probably know
            // here
            // that the edge does not touch the bdy of the target Geometry
            int loc = ptLocator.locate(e.getCoordinate(), target);
            e.getLabel().setAllLocations(targetIndex, loc);
        } else {
            e.getLabel().setAllLocations(targetIndex, Location.EXTERIOR);
        }
    }

    /**
     * Isolated nodes are nodes whose labels are incomplete (e.g. the location for one Geometry is
     * null). This is the case because nodes in one graph which don't intersect nodes in the other
     * are not completely labelled by the initial process of adding nodes to the nodeList. To
     * complete the labelling we need to check for nodes that lie in the interior of edges, and in
     * the interior of areas.
     */
    private void labelIsolatedNodes() {
        for (Iterator ni = nodes.iterator(); ni.hasNext(); ) {
            Node n = (Node) ni.next();
            Label label = n.getLabel();
            // isolated nodes should always have at least one geometry in their
            // label
            Assert.isTrue(label.getGeometryCount() > 0, "node with empty label found");
            if (n.isIsolated()) {
                if (label.isNull(0)) labelIsolatedNode(n, 0);
                else labelIsolatedNode(n, 1);
            }
        }
    }

    /** Label an isolated node with its relationship to the target geometry. */
    private void labelIsolatedNode(Node n, int targetIndex) {
        int loc = ptLocator.locate(n.getCoordinate(), arg[targetIndex].getGeometry());
        n.getLabel().setAllLocations(targetIndex, loc);
    }
}
