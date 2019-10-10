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
package org.geotools.graph.traverse;

import org.geotools.graph.structure.Graphable;

/**
 * Iterated over the components of a graph using a standard visitor pattern.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 */
public interface GraphWalker {

    /**
     * Visits a graph component.
     *
     * @param element The component being visited.
     * @param traversal The traversal controlling the sequence of graph component visits.
     * @return GraphTraversal#CONTINUE to signal that the traversal should continue.<br>
     *     GraphTraversal#CONTINUE to signal that the traversal should suspend.<br>
     *     GraphTraversal#KILL_BRANCH to signal that the traversal should kill its current branch.
     *     <br>
     *     GraphTraversal#STOP to signal that the traversal should stop.<br>
     * @see GraphTraversal
     * @see GraphIterator
     */
    public int visit(Graphable element, GraphTraversal traversal);

    /**
     * Called when the graph traversal is completed. Whether this method is called after a traversal
     * has been stopped with a return signal is up to the implementation of GraphTraversal.
     *
     * @see GraphTraversal
     */
    public void finish();
}
