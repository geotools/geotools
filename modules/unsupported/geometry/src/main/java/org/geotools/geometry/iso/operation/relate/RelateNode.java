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
package org.geotools.geometry.iso.operation.relate;

/**
 * A RelateNode is a Node that maintains a list of EdgeStubs for the edges that are incident on it.
 */

import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.EdgeEndStar;
import org.geotools.geometry.iso.topograph2D.IntersectionMatrix;
import org.geotools.geometry.iso.topograph2D.Node;

/**
 * Represents a node in the topological graph used to compute spatial relationships.
 *
 * @source $URL$
 */
public class RelateNode extends Node {

    public RelateNode(Coordinate coord, EdgeEndStar edges) {
        super(coord, edges);
    }

    /**
     * Update the IM with the contribution for this component. A component only contributes if it
     * has a labelling for both parent geometries
     */
    protected void computeIM(IntersectionMatrix im) {
        im.setAtLeastIfValid(label.getLocation(0), label.getLocation(1), 0);
    }

    /** Update the IM with the contribution for the EdgeEnds incident on this node. */
    void updateIMFromEdges(IntersectionMatrix im) {
        ((EdgeEndBundleStar) edges).updateIM(im);
    }
}
