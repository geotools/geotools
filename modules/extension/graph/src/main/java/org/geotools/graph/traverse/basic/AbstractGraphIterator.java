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
package org.geotools.graph.traverse.basic;

import org.geotools.graph.structure.Graph;
import org.geotools.graph.traverse.GraphIterator;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.GraphWalker;

/**
 * An abstract implementation of GraphIterator.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 */
public abstract class AbstractGraphIterator implements GraphIterator {

    private GraphTraversal m_traversal;

    /** @see GraphIterator#setTraversal(GraphTraversal) */
    @Override
    public void setTraversal(GraphTraversal traversal) {
        m_traversal = traversal;
    }

    /** @see GraphIterator#getTraversal() */
    @Override
    public GraphTraversal getTraversal() {
        return m_traversal;
    }

    /**
     * Returns the graph being traversed.
     *
     * @return The graph being traversed.
     * @see Graph
     */
    public Graph getGraph() {
        return m_traversal.getGraph();
    }

    /**
     * Returns the walker being traversed over the graph.
     *
     * @return The walker being traversed over the graph.
     * @see GraphWalker
     */
    public GraphWalker getWalker() {
        return m_traversal.getWalker();
    }
}
