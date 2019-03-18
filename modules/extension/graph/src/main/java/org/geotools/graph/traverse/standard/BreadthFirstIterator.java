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
package org.geotools.graph.traverse.standard;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.basic.SourceGraphIterator;

/**
 * Iterates over the nodes of a graph in a <B>Breadth First Search</B> pattern starting from a
 * specified node. The following illustrates the iteration order. <br>
 * <br>
 * <IMG src="doc-files/bfs.gif"/><br>
 * <br>
 * The iteration operates by maintaining a node queue of <B>active</B> nodes. An <B>active</B> node
 * is a node that will returned at a later stage of the iteration. The node queue for a Breadth
 * First iteration is implemented as a <B>First In First Out</B> queue. A node is placed in the the
 * node queue if it has not been visited, and it is adjacent to a a node that has been visited. The
 * node queue intially contains only the source node of the traversal.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 */
public class BreadthFirstIterator extends SourceGraphIterator {

    /** Contains all nodes to be returned * */
    private Queue<Graphable> m_active;

    /**
     * Sets the source of the traversal and places it in the node queue. The first call to this
     * method will result in the internal node queue being built. Subsequent calls to the method
     * clear the node queue and reset the iteration.
     *
     * @see SourceGraphIterator#setSource(Graphable)
     */
    @Override
    public void setSource(Graphable source) {
        super.setSource(source);

        // set source of traversal, creating queue if necessary
        if (m_active == null) m_active = buildQueue(getGraph());
        else if (m_active.isEmpty()) m_active.clear();

        m_active.add(getSource());
    }

    /**
     * Does nothing.
     *
     * @see org.geotools.graph.traverse.GraphIterator#init(Graph)
     */
    @Override
    public void init(Graph graph, GraphTraversal traversal) {
        // do nothing
    }

    /**
     * Returns the next node from the node queue that has not yet been visited. It is possible for
     * the node queue to contain duplicate entries. To prevent the iteration returning the same node
     * multiple times, the visited flag is checked on nodes coming out of the queue. If the flag is
     * set, the node is ignored, not returned, and the next node in the queue is returned. This is
     * however transparent to the caller.
     *
     * @see org.geotools.graph.traverse.GraphIterator#next()
     */
    @Override
    public Graphable next(GraphTraversal traversal) {
        while (!m_active.isEmpty()) {
            Graphable next = m_active.remove();
            if (!traversal.isVisited(next)) return (next);
        }
        return (null);
    }

    /**
     * Looks for nodes adjacent to the current node to place into the node queue. An adjacent node
     * is only placed into the node queue if its visited flag is unset.
     *
     * @see org.geotools.graph.traverse.GraphIterator#cont(Graphable)
     */
    @Override
    public void cont(Graphable current, GraphTraversal traversal) {
        for (Iterator<? extends Graphable> itr = current.getRelated(); itr.hasNext(); ) {
            Node related = (Node) itr.next();
            if (!traversal.isVisited(related)) {
                m_active.add(related);
            }
        }
    }

    /**
     * Kills the current branch by not looking for any adjacent nodes to place into the node queue.
     *
     * @see org.geotools.graph.traverse.GraphIterator#killBranch(Graphable)
     */
    @Override
    public void killBranch(Graphable current, GraphTraversal traversal) {
        // do not look for any adjacent nodes to place into the active queue
    }

    /**
     * Builds the node queue for the iteration.
     *
     * @param graph The graph being iterated over.
     * @return A First In First Out queue.
     */
    protected Queue<Graphable> buildQueue(Graph graph) {
        return (new ArrayDeque<>(graph.getNodes().size()));
    }

    /**
     * Returns the node queue.
     *
     * @return The node queue.
     */
    protected Queue<Graphable> getQueue() {
        return (m_active);
    }
}
