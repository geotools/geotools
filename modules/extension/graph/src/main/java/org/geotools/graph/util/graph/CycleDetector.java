/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.graph.util.graph;

import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.traverse.GraphIterator;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.GraphWalker;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.standard.BreadthFirstTopologicalIterator;

/**
 * Detects cycles in a graph. A topological iteration of the nodes of the graph is performed. If the iteration includes
 * all nodes in the graph then the graph is cycle free, otherwise a cycle exists.
 *
 * @see org.geotools.graph.traverse.standard.BreadthFirstTopologicalIterator
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 */
public class CycleDetector implements GraphWalker {

    /** the graph to be tested for cycle exisitance * */
    private Graph m_graph;

    /** counter to keep track of the number of nodes visited in the iteration * */
    private int m_nvisited;

    /** iteration to perform on nodes of graph * */
    private GraphIterator m_iterator;

    /**
     * Constructs a new CycleDetector.
     *
     * @param graph The graph to be tested for cycle existance.
     */
    public CycleDetector(Graph graph) {
        m_graph = graph;
        m_nvisited = 0;
        m_iterator = createIterator();
    }

    /**
     * Performs the iteration to determine if a cycle exits in the graph.
     *
     * @return True if a cycle exists, false if not.
     */
    public boolean containsCycle() {
        // initialize visited counter
        m_nvisited = 0;

        // create the traversal that uses the topological iterator
        GraphTraversal traversal = new BasicGraphTraversal(m_graph, this, m_iterator);
        traversal.init();
        traversal.traverse();

        // if all nodes visited then no cycle
        if (m_graph.getNodes().size() == m_nvisited) return false;
        return true;
    }

    /**
     * Increments the count of nodes visited.
     *
     * @see GraphWalker#visit(Graphable, GraphTraversal)
     */
    @Override
    public int visit(Graphable element, GraphTraversal traversal) {
        m_nvisited++;
        return GraphTraversal.CONTINUE;
    }

    /**
     * Does nothing.
     *
     * @see GraphWalker#finish()
     */
    @Override
    public void finish() {}

    /**
     * Creates the iterator to be used in the cycle detection.
     *
     * @return a BreathFirstToplogicalIterator.
     */
    protected GraphIterator createIterator() {
        return new BreadthFirstTopologicalIterator();
    }
}
