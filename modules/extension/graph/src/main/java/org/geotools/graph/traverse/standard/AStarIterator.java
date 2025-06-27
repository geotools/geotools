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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import org.geotools.graph.structure.DirectedGraphable;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.basic.SourceGraphIterator;

/**
 * A path iterator that uses a function (usually denoted f(x)) to determine the order in which the algorithm visits
 * nodes, f(x) is a sum of two functions:
 *
 * <ul>
 *   <li>The path-cost function (usually denoted g(x), which may or may not be a heuristic)
 *   <li>An admissible "heuristic estimate" (usually denoted h(x)).
 * </ul>
 *
 * The iterator proceeds as follows (pseudo-code):
 *
 * <pre>
 * <code>
 *     // COST(n,n') : the real cost to go from n to n'
 *     OPEN = [Source]
 *     CLOSE = []
 *
 *     while ( |OPEN| > 0 )
 *         n = a node in OPEN with less f
 *         remove n from OPEN
 *         add n to CLOSE
 *         if ( n == target ) {
 *            return  // path find
 *
 *         // if n != target
 *         for each node n' that relates to n do
 *             if n' in OPEN
 *                 if (g(n) + COST(n,n')) < g(n')
 *                     g(n') = g(n) + COST(n,n')
 *                     parent(n') = n
 *             else
 *                 g(n') = g(n) + COST(n,n')
 *                 parent(n') = n
 *                 add n' to OPEN
 *     // end while
 * </code>
 * </pre>
 *
 * For more details see http://en.wikipedia.org/wiki/A_star
 *
 * @author Germán E. Trouillet, Francisco G. Malbrán. Universidad Nacional de Córdoba (UNC)
 */
public class AStarIterator extends SourceGraphIterator {
    /** function necessaries for A Star algorithm */
    private AStarFunctions m_afuncs;
    /** queue that represents the open list of the A Star algorithm */
    private PriorityQueue<AStarNode> m_pqueue;
    /** map of graph node to internal A* node * */
    private HashMap<Node, AStarNode> m_nodemap;

    public AStarIterator(Node source, AStarFunctions afuncs) {

        m_afuncs = afuncs;
        AStarNode asn = new AStarNode(source, afuncs.h(source));
        asn.setG(0);
        setSource(source);
        m_nodemap = new HashMap<>();
        m_nodemap.put(source, asn);
        m_pqueue = new PriorityQueue<>(100, comparator);

        m_pqueue.add(asn);
    }

    /** Does Nothing. All the work was already done by the constructor. */
    @Override
    public void init(Graph graph, GraphTraversal traversal) {}

    /**
     * Returns the next node in the priority queue. if the queue is empty then there is no path from the source to the
     * destiny in this graph.
     *
     * @see org.geotools.graph.traverse.GraphIterator#next()
     */
    @Override
    public Graphable next(GraphTraversal traversal) {
        if (m_pqueue.isEmpty()) {
            return null;
        }
        AStarNode next = m_pqueue.remove();
        return next.getNode();
    }

    /**
     * Makes a step of the A* algorithm. Takes the current node, looks for its neighbours. The ones which are closed are
     * discarted. The ones "in" the opened queue are checked to see if its necessary to update them. The rest of the
     * nodes are initialized as AStarNodes and inserted in the opened queue.
     *
     * @see org.geotools.graph.traverse.GraphIterator#cont(Graphable)
     */
    @Override
    public void cont(Graphable current, GraphTraversal traversal) {
        Node currdn = (Node) current;
        AStarNode nextAsn;

        AStarNode currAsn = m_nodemap.get(currdn);
        if (currAsn == null) {
            throw new IllegalArgumentException("AStarIterator: The node is not in the open list");
        }
        currAsn.close();
        for (Iterator<?> itr = getRelated(current); itr.hasNext(); ) {
            Node next = (Node) itr.next();
            if (m_nodemap.containsKey(next)) {
                nextAsn = m_nodemap.get(next);
                if (!nextAsn.isClosed()) {
                    if (currAsn.getG() + m_afuncs.cost(currAsn, nextAsn) < nextAsn.getG()) {
                        m_pqueue.remove(nextAsn);
                        nextAsn.setG(currAsn.getG() + m_afuncs.cost(currAsn, nextAsn));
                        nextAsn.setParent(currAsn);
                        m_pqueue.add(nextAsn);
                    }
                }
            } else { // create new AStarNode
                nextAsn = new AStarNode(next, m_afuncs.h(next));
                nextAsn.setG(currAsn.getG() + m_afuncs.cost(currAsn, nextAsn));
                nextAsn.setParent(currAsn);
                m_pqueue.add(nextAsn);
                m_nodemap.put(next, nextAsn);
            }
        }
    }

    /**
     * Kills the branch of the traversal
     *
     * @see org.geotools.graph.traverse.GraphIterator#killBranch(Graphable)
     */
    @Override
    public void killBranch(Graphable current, GraphTraversal traversal) {
        // do nothing
    }

    /** */
    public Node getParent(Node n) {
        AStarNode asn = m_nodemap.get(n);
        return asn == null
                ? null
                : asn.getParent() == null ? null : asn.getParent().getNode();
    }

    protected Iterator<?> getRelated(Graphable current) {
        if (current instanceof DirectedGraphable) {
            return ((DirectedGraphable) current).getOutRelated();
        } else {
            return current.getRelated();
        }
    }

    /** Decides which node has more priority */
    private static Comparator<AStarNode> comparator = (o1, o2) -> {
        AStarNode n1 = o1;
        AStarNode n2 = o2;
        return n1.getF() < n2.getF() ? -1 : n1.getF() > n2.getF() ? 1 : 0;
    };

    /**
     * Internal data structure used to track node costs, and parent nodes.
     *
     * @author German E. Trouillet, Francisco G. Malbrán. Universidad Nacional de Córdoba (UNC)
     */
    public static class AStarNode {
        /** Node of the graph asociated with this A Star Node. */
        private Node node;

        /** AStarNode parent of this node. */
        private AStarNode parent;

        /** the real cost of the path so far. */
        private double g;

        /** the value of the heuristic function. */
        private double h;

        /** The node is in the CLOSE list */
        private boolean closed;

        public AStarNode(Node n, double h_val) {
            node = n;
            parent = null;
            g = Double.POSITIVE_INFINITY;
            h = h_val;
            closed = false;
        }

        public boolean isClosed() {
            return closed;
        }

        public void close() {
            closed = true;
        }

        public double getG() {
            return g;
        }

        public double getH() {
            return h;
        }

        public double getF() {
            return g + h;
        }

        public AStarNode getParent() {
            return parent;
        }

        public Node getNode() {
            return node;
        }

        public void setG(double value) {
            g = value;
        }

        public void setH(double value) {
            h = value;
        }

        public void setNode(Node n) {
            node = n;
        }

        public void setParent(AStarNode an) {
            parent = an;
        }
    }

    /**
     * Defines the functions needed by A Star.
     *
     * @author German E. Trouillet, Francisco G. Malbrán. Universidad Nacional de Córdoba (UNC)
     */
    public abstract static class AStarFunctions {
        private Node dest;

        /** Creates a new instance and sets up the destination node for the algorithm */
        public AStarFunctions(Node destination) {
            dest = destination;
        }

        /** Sets up the destination node for the algorithm */
        public void setDestination(Node destination) {
            dest = destination;
        }

        /** Defines the cost of going from one node to another */
        public abstract double cost(AStarNode n1, AStarNode n2);

        /** Defines the heuristic function for n */
        public abstract double h(Node n);

        public Node getDest() {
            return dest;
        }
    }
}
