/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.graph.path;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Node;

/**
 * Represents a cycle in a graph. A <B>cycle</B> C is defined as a closed walk of size n in which nodes 1 through n-1
 * form a path.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 */
public class Cycle extends Walk {

    public Cycle(Collection<Node> nodes) {
        super(nodes);
    }

    /**
     * Tests if the cycle is valid. A valid cycle satisfies two conditions: <br>
     * <br>
     * 1. Each pair of adjacent nodes share an edge.<br>
     * 2. The first and last nodes share an edge. 3. The only node repetition is the first and last nodes.
     */
    @Override
    public boolean isValid() {
        if (super.isValid()) {

            // ensure first and last nodes are same
            if (isClosed()) {
                // ensure no node repetitions except for first and last
                return new HashSet<>(this).size() == size() - 1;
            }
        }
        return false;
    }

    @Override
    protected List<Edge> buildEdges() {
        List<Edge> edges = super.buildEdges();

        // get the edge between the first and last nodes
        Node first = get(0);
        Node last = get(size() - 1);

        Edge e = first.getEdge(last);
        if (e != null) {
            edges.add(e);
            return edges;
        }

        return null;
    }
}
