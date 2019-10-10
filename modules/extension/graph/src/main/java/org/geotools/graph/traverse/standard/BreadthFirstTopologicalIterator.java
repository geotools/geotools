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
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.basic.AbstractGraphIterator;

/**
 * Iterates over the nodes of a graph in <B>Breadth First Topological Sort</B> pattern. The
 * following is an illustration of the iteration.<br>
 * <IMG src="doc-files/breadth_topo.gif"><br>
 * <br>
 * Initially all nodes of degree less than two are <B>active</B> (ready to be visited). As nodes are
 * visited, a node can become active when all but one of its related nodes have been visited (
 * degree = counter + 1). When a node becomes active it is placed into the <B>active node queue</B>
 * (queue of nodes to be visited). The Breadth First Topological iterator places nodes into the
 * queue in <B>First In First Out</B> order.<br>
 * <br>
 * To determine when a node is to become active the iterator uses the counter associated with each
 * node. If these counters are modified by an entity other then the iterator, the iteration may be
 * affected in undefined ways.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 */
public class BreadthFirstTopologicalIterator extends AbstractGraphIterator {

    /** Queue of active nodes * */
    private Queue<Graphable> m_queue;

    /**
     * Creates the active queue, and populates it with all nodes of degree less than 2. Counters of
     * all nodes are also reset to 0.
     *
     * @see org.geotools.graph.traverse.GraphIterator#init(Graph)
     */
    @Override
    public void init(Graph graph, GraphTraversal traversal) {
        // create queue
        m_queue = buildQueue(graph);

        // initialize nodes
        graph.visitNodes(
                new GraphVisitor() {
                    @Override
                    public int visit(Graphable component) {
                        Node node = (Node) component;

                        // reset counter to zero
                        node.setCount(0);

                        if (node.getDegree() < 2) m_queue.add(node);

                        return (0);
                    }
                });
    }

    /**
     * Returns the next node in the active node queue.
     *
     * @see org.geotools.graph.traverse.GraphIterator#next()
     */
    @Override
    public Graphable next(GraphTraversal traversal) {
        if (m_queue.isEmpty()) {
            return null;
        }
        return m_queue.remove();
    }

    /**
     * Continues the iteration by incrementing the counters of any unvisited nodes related to the
     * current node. If any related nodes have counters equal to degree of that node - 1, it is
     * placed into the active queue.
     *
     * @see org.geotools.graph.traverse.GraphIterator#cont(Graphable)
     */
    @Override
    public void cont(Graphable current, GraphTraversal traversal) {
        // increment the count of all adjacent nodes by one
        // if the result count is 1 less than the degree, place it into the queue
        for (Iterator<? extends Graphable> itr = current.getRelated(); itr.hasNext(); ) {
            Node related = (Node) itr.next();
            if (!traversal.isVisited(related)) {
                related.setCount(related.getCount() + 1);
                if (related.getDegree() - 1 == related.getCount()) m_queue.add(related);
            }
        }
    }

    /**
     * Kills the current branch of the traversal by not incrementing the counters of any related
     * nodes.
     *
     * @see org.geotools.graph.traverse.GraphIterator#killBranch(Graphable)
     */
    @Override
    public void killBranch(Graphable current, GraphTraversal traversal) {
        // do nothing
    }

    /**
     * Builds the active node queue.
     *
     * @param graph The Graph whose components are being iterated over.
     * @return A first in first out queue
     */
    protected Queue<Graphable> buildQueue(Graph graph) {
        return (new ArrayDeque<>(graph.getNodes().size()));
    }
}
