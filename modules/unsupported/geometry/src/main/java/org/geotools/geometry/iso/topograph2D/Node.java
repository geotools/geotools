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

import java.io.PrintStream;
import java.util.Iterator;
import org.geotools.util.SuppressFBWarnings;

/**
 * A node in a {@link PlanarGraph}is a location where 0 or more {@link Edge}s meet. A node is
 * connected to each of its incident Edges via an outgoing DirectedEdge.
 */
public class Node extends GraphComponent {

    protected Coordinate coord; // only non-null if this node is precise

    protected EdgeEndStar edges;

    public Node(Coordinate coord, EdgeEndStar edges) {
        this.coord = coord;
        this.edges = edges;
        label = new Label(0, Location.NONE);
    }

    public Coordinate getCoordinate() {
        return coord;
    }

    public EdgeEndStar getEdges() {
        return edges;
    }

    /**
     * Tests whether any incident edge is flagged as being in the result. This test can be used to
     * determine if the node is in the result, since if any incident edge is in the result, the node
     * must be in the result as well.
     *
     * @return <code>true</code> if any indicident edge in the in the result
     */
    public boolean isIncidentEdgeInResult() {
        for (Iterator it = getEdges().getEdges().iterator(); it.hasNext(); ) {
            DirectedEdge de = (DirectedEdge) it.next();
            if (de.getEdge().isInResult()) return true;
        }
        return false;
    }

    public boolean isIsolated() {
        return (label.getGeometryCount() == 1);
    }

    /** Basic nodes do not compute IMs */
    protected void computeIM(IntersectionMatrix im) {}

    /** Add the edge to the list of edges at this node */
    public void add(EdgeEnd e) {
        // Assert: start pt of e is equal to node point
        edges.insert(e);
        e.setNode(this);
    }

    public void mergeLabel(Node n) {
        mergeLabel(n.label);
    }

    /**
     * To merge labels for two nodes, the merged location for each LabelElement is computed. The
     * location for the corresponding node LabelElement is set to the result, as long as the
     * location is non-null.
     */
    public void mergeLabel(Label label2) {
        for (int i = 0; i < 2; i++) {
            int loc = computeMergedLocation(label2, i);
            int thisLoc = label.getLocation(i);
            if (thisLoc == Location.NONE) label.setLocation(i, loc);
        }
    }

    public void setLabel(int argIndex, int onLocation) {
        if (label == null) {
            label = new Label(argIndex, onLocation);
        } else label.setLocation(argIndex, onLocation);
    }

    /** Updates the label of a node to BOUNDARY, obeying the mod-2 boundaryDetermination rule. */
    // label.setLocation might actually NPE, just ignoring it as the module is generally broken
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH")
    public void setLabelBoundary(int argIndex) {
        // determine the current location for the point (if any)
        int loc = Location.NONE;
        if (label != null) loc = label.getLocation(argIndex);
        // flip the loc
        int newLoc;
        switch (loc) {
            case Location.BOUNDARY:
                newLoc = Location.INTERIOR;
                break;
            case Location.INTERIOR:
                newLoc = Location.BOUNDARY;
                break;
            default:
                newLoc = Location.BOUNDARY;
                break;
        }
        label.setLocation(argIndex, newLoc);
    }

    /**
     * The location for a given eltIndex for a node will be one of { null, INTERIOR, BOUNDARY }. A
     * node may be on both the boundary and the interior of a geometry; in this case, the rule is
     * that the node is considered to be in the boundary. The merged location is the maximum of the
     * two input values.
     */
    int computeMergedLocation(Label label2, int eltIndex) {
        int loc = Location.NONE;
        loc = label.getLocation(eltIndex);
        if (!label2.isNull(eltIndex)) {
            int nLoc = label2.getLocation(eltIndex);
            if (loc != Location.BOUNDARY) loc = nLoc;
        }
        return loc;
    }

    public void print(PrintStream out) {
        out.println("node " + coord + " lbl: " + label);
    }
}
